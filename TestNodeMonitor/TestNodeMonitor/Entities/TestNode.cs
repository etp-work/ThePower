using System;
using System.Collections.Generic;
using System.Linq;
using System.Data;
using System.Text;
using System.Configuration;
using System.Diagnostics;
using System.Reflection;
using System.IO;
using System.Net;
using MySql.Data.MySqlClient;
using log4net;
using System.Timers;
using Newtonsoft.Json.Linq;
using TestNodeMonitor;
using System.Drawing;
using System.Drawing.Imaging;

[assembly: log4net.Config.XmlConfigurator(Watch = true)]
namespace TestNodeMonitor.Entities
{
    public class TestNode : IDisposable
    {
        public int NodeID { get; set; }
        public string NodeName { get; set; }
        public string NodeStatus { get; set; }
        public string Location { get; set; }
        public string NodeIPAddress { get; set; }
        public int NodeHttpPort1 { get; set; }
        public int NodeHttpPort2 { get; set; }
        public int NodeHttpPort3 { get; set; }
        public Dictionary<string, string> Configurations { get; set; }
        private Timer timer;
        //private FileSystemWatcher watcher;

        private static Dictionary<int, TestNode> nodes = new Dictionary<int, TestNode>();
        private static ILog logger = log4net.LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private static string JolokiaVersion = "jolokia/version";
        private static string BaseDir = "C:\\testnodes";
        private static string BaseNodeDir = "C:\\testnodes\\tomcat_base";
        private static string TargetWebDir = "C:\\Program Files (x86)\\Apache Software Foundation\\Apache2.2\\htdocs\\portaltest\\logs";
        private static bool checking = false;
        private static bool testing = false;
        private static bool updating = false;
        private static Process testProcess;

        public void startTimer()
        {
            timer = new Timer();
            timer.Elapsed += timer_Elapsed;
            timer.Interval = 30000;
            timer.Enabled = true;
        }

        /*
        public void startWatcher()
        {
            watcher = new FileSystemWatcher();
            watcher.Path = String.Format("{0}\\{1}\\logs", BaseDir, Location);
            watcher.NotifyFilter = NotifyFilters.LastAccess | NotifyFilters.LastWrite
               | NotifyFilters.FileName | NotifyFilters.DirectoryName;
            watcher.Filter = "*.log";
            watcher.Changed += new FileSystemEventHandler(OnChanged);
            watcher.EnableRaisingEvents = true;
        }
        */

        public void start()
        {
            closeTimer();
            startTimer();
            //startWatcher();

            ProcessStartInfo procStartInfo = new ProcessStartInfo("startup.bat");
            procStartInfo.WorkingDirectory = String.Format("{0}\\{1}\\bin", BaseDir, Location);
            procStartInfo.UseShellExecute = true;

            Process proc = new Process();
            proc.StartInfo = procStartInfo;
            proc.Start();
            updateNodeStatus("STARTING");
        }

        void timer_Elapsed(object sender, ElapsedEventArgs e)
        {
            if (checking || testing || updating)
            {
                return;
            }

            bool isrunning = false;
            try
            {
                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(String.Format("http://{0}:{1}/{2}", NodeIPAddress, NodeHttpPort1, JolokiaVersion));
                request.Method = WebRequestMethods.Http.Get;
                HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                StreamReader reader = new StreamReader(response.GetResponseStream());
                string responseString = reader.ReadToEnd();
                response.Close();

                JObject json = JObject.Parse(responseString);
                int retCode = (int)json["status"];
                if (retCode == 200)
                {
                    isrunning = true;
                }
            }
            catch (Exception ex)
            {
                isrunning = false;
                logger.Error(ex.Message, ex);
            }

            if (isrunning)
            {
                if ("RUNNING" != NodeStatus)
                {
                    updateNodeStatus("RUNNING");
                }

                try
                {
                    string logfile = String.Format("{0}\\{1}\\logs\\catalina.{2:yyyy-MM-dd}.log", BaseDir, Location, DateTime.Today);
                    File.Copy(logfile, String.Format("{0}\\{1}.log", TargetWebDir, Location), true);
                }
                catch (Exception ex)
                {
                    logger.Error(ex.Message, ex);
                }

                DateTime nowtime = DateTime.Now;
                int every3hour = nowtime.Hour % 3;
                int minute = nowtime.Minute;
                switch (every3hour)
                {
                    case 0:
                        if (minute == 15 && "UPDATE" != NodeStatus && NodeID == 1)
                        {
                            updateNodeStatus("UPDATE");
                        }
                        break;
                    case 1:
                        if (minute == 15 && "TEST" != NodeStatus && NodeID == 1)
                        {
                            updateNodeStatus("TEST");
                        }
                        break;
                    default:
                        break;
                }
            }
            else if ("RUNNING" == NodeStatus)
            {
                // The actual node is stopped, need to restart
                updateNodeStatus("RESTART");
            }
        }

        private void closeTimer()
        {
            if (null != timer)
            {
                timer.Enabled = false;
                timer.Dispose();
                timer = null;
            }
        }

        /*
        public void stopWatcher()
        {
            if (null != watcher)
            {
                watcher.EnableRaisingEvents = false;
                watcher.Dispose();
                watcher = null;
            }
        }
        */

        public void stop()
        {
            updateNodeStatus("STOPPED");
            closeTimer();
            //stopWatcher();

            if (null != testProcess)
            {
                testProcess.Kill();
                testProcess.Dispose();
                testProcess = null;
            }

            ProcessStartInfo procStartInfo = new ProcessStartInfo("shutdown.bat");
            procStartInfo.WorkingDirectory = String.Format("{0}\\{1}\\bin", BaseDir, Location);
            procStartInfo.UseShellExecute = true;

            Process proc = new Process();
            proc.StartInfo = procStartInfo;
            proc.Start();
            proc.WaitForExit();
            proc = null;
        }

        public void updateNodeStatus(string status)
        {
            if (NodeStatus != status)
            {
                NodeStatus = status;
                logger.Info(String.Format("TestNode ID:{0} Name:{1} Status:{2}", NodeID, NodeName, NodeStatus));
                MySqlConnection connection = getDBConnection();
                if (connection.State != ConnectionState.Open)
                {
                    try
                    {
                        connection.Open();
                        string sql = String.Format("UPDATE testnode SET NodeStatus='{0}' WHERE NodeID={1}", NodeStatus, NodeID);
                        MySqlCommand query = new MySqlCommand(sql, connection);
                        query.ExecuteNonQuery();
                    }
                    catch (MySqlException ex)
                    {
                        logger.Error(ex.Message, ex);
                    }
                    finally
                    {
                        if (connection.State != ConnectionState.Closed)
                        {
                            connection.Close();
                        }
                    }
                }
            }
        }

        private static MySqlConnection getDBConnection()
        {
            string server = ConfigurationManager.AppSettings["DBAddress"];
            string uid = ConfigurationManager.AppSettings["DBUser"];
            string password = ConfigurationManager.AppSettings["DBPassword"];
            string database = ConfigurationManager.AppSettings["DBDataBase"];
            string connectionString = String.Format("SERVER={0};DATABASE={1};UID={2};PASSWORD={3};", server, database, uid, password);
            return new MySqlConnection(connectionString);
        }

        private static Dictionary<int, TestNode> getNodesFromDB()
        {
            Dictionary<int, TestNode> _nodes = new Dictionary<int, TestNode>();
            MySqlConnection connection = getDBConnection();
            if (connection.State != ConnectionState.Open)
            {
                try
                {
                    connection.Open();
                    string sql1 = "SELECT * FROM testnode";
                    MySqlCommand query1 = new MySqlCommand(sql1, connection);
                    using (MySqlDataReader queryResults = query1.ExecuteReader())
                    {
                        while (queryResults.Read())
                        {
                            TestNode _node = new TestNode();

                            try { _node.NodeID = queryResults.GetInt32("NodeID"); }
                            catch (InvalidCastException) { }

                            try { _node.NodeName = queryResults.GetString("NodeName"); }
                            catch (InvalidCastException) { }

                            try { _node.NodeStatus = queryResults.GetString("NodeStatus"); }
                            catch (InvalidCastException) { }

                            try { _node.Location = queryResults.GetString("Location"); }
                            catch (InvalidCastException) { }

                            try { _node.NodeIPAddress = queryResults.GetString("NodeIPAddress"); }
                            catch (InvalidCastException) { }

                            try { _node.NodeHttpPort1 = queryResults.GetInt32("NodeHttpPort1"); }
                            catch (InvalidCastException) { }

                            try { _node.NodeHttpPort2 = queryResults.GetInt32("NodeHttpPort2"); }
                            catch (InvalidCastException) { }

                            try { _node.NodeHttpPort3 = queryResults.GetInt32("NodeHttpPort3"); }
                            catch (InvalidCastException) { }

                            _node.Configurations = new Dictionary<string, string>();
                            _nodes.Add(_node.NodeID, _node);
                        }
                    }

                    var enumerator1 = _nodes.GetEnumerator();
                    while (enumerator1.MoveNext())
                    {
                        TestNode testnode = enumerator1.Current.Value;
                        string sql2 = String.Format("SELECT * FROM testnodecfg where NodeID={0}", testnode.NodeID);
                        MySqlCommand query2 = new MySqlCommand(sql2, connection);
                        using (MySqlDataReader queryResults = query2.ExecuteReader())
                        {
                            while (queryResults.Read())
                            {
                                String propertyName = "";
                                String propertyValue = "";

                                try { propertyName = queryResults.GetString("PropertyName"); }
                                catch (InvalidCastException) { }

                                try { propertyValue = queryResults.GetString("PropertyValue"); }
                                catch (InvalidCastException) { }

                                if (!String.IsNullOrEmpty(propertyName))
                                {
                                    testnode.Configurations.Add(propertyName, propertyValue);
                                }
                            }
                        }
                    }
                }
                catch (MySqlException ex)
                {
                    logger.Error(ex.Message, ex);
                }
                finally
                {
                    if (connection.State != ConnectionState.Closed)
                    {
                        connection.Close();
                    }
                }
            }

            return _nodes;
        }

        public static void check()
        {
            if (checking)
            {
                return;
            }
            else
            {
                checking = true;
            }

            Dictionary<int, TestNode> _nodes = getNodesFromDB();
            var enumerator = _nodes.GetEnumerator();
            while (enumerator.MoveNext())
            {
                TestNode testnode = enumerator.Current.Value;

                if (!nodes.ContainsKey(testnode.NodeID))
                {
                    if (testnode.NodeStatus == "RUNNING")
                    {
                        testnode.startTimer();
                    }
                    nodes.Add(testnode.NodeID, testnode);
                }
                else
                {
                    TestNode _node = nodes[testnode.NodeID];
                    _node.NodeName = testnode.NodeName;
                    _node.Location = testnode.Location;
                    _node.NodeStatus = testnode.NodeStatus;
                    _node.NodeIPAddress = testnode.NodeIPAddress;
                    _node.NodeHttpPort1 = testnode.NodeHttpPort1;
                    _node.NodeHttpPort2 = testnode.NodeHttpPort2;
                    _node.NodeHttpPort3 = testnode.NodeHttpPort3;
                }

                switch (testnode.NodeStatus)
                {
                    case "START":
                        logger.Info(String.Format("{0} TestNode ID:{1}, Name:{2}", testnode.NodeStatus, testnode.NodeID, testnode.NodeName));
                        testnode.start();
                        break;
                    case "RESTART":
                        logger.Info(String.Format("{0} TestNode ID:{1}, Name:{2}", testnode.NodeStatus, testnode.NodeID, testnode.NodeName));
                        testnode.stop();
                        testnode.updateNodeStatus("START");
                        break;
                    case "STOP":
                        logger.Info(String.Format("{0} TestNode ID:{1}, Name:{2}", testnode.NodeStatus, testnode.NodeID, testnode.NodeName));
                        testnode.stop();
                        break;
                    case "UPDATE":
                        updating = true;
                        logger.Info(String.Format("{0} TestNode ID:{1}, Name:{2}", testnode.NodeStatus, testnode.NodeID, testnode.NodeName));
                        testnode.stop();
                        testnode.updateNodeStatus("UPDATING");
                        break;
                    case "UPDATING":
                        updating = true;
                        try
                        {
                            logger.Info(String.Format("{0} TestNode ID:{1}, Name:{2}", testnode.NodeStatus, testnode.NodeID, testnode.NodeName));
                            string dirApps = String.Format("{0}\\{1}\\webapps", BaseDir, testnode.Location);
                            if (Directory.Exists(dirApps))
                            {
                                DeleteDir(dirApps);
                            }
                            DirectoryCopy(BaseNodeDir + "\\webapps", dirApps, true);

                            string dirWork = String.Format("{0}\\{1}\\work", BaseDir, testnode.Location);
                            if (Directory.Exists(dirWork))
                            {
                                DeleteDir(dirWork);
                            }

                            ProcessStartInfo procStartInfo = new ProcessStartInfo("job_allInOne.py");
                            procStartInfo.WorkingDirectory = String.Format("{0}\\scripts", BaseDir);
                            procStartInfo.UseShellExecute = true;

                            Process proc = new Process();
                            proc.StartInfo = procStartInfo;
                            proc.Start();
                            proc.WaitForExit();
                            proc = null;

                            testnode.updateNodeStatus("RESTART");
                        }
                        catch (Exception ex)
                        {
                            logger.Error(ex.Message, ex);
                        }
                        updating = false;
                        break;
                    case "TEST":
                        testing = true;
                        try
                        {
                            if (null != testProcess)
                            {
                                testProcess.Kill();
                                testProcess.Dispose();
                                testProcess = null;
                            }

                            logger.Info(String.Format("{0} TestNode ID:{1}, Name:{2}", testnode.NodeStatus, testnode.NodeID, testnode.NodeName));
                            testnode.updateNodeStatus("STARTTEST");
                            ProcessStartInfo procStartInfo = new ProcessStartInfo("TestBrowser.exe");
                            procStartInfo.WorkingDirectory = ".";
                            procStartInfo.UseShellExecute = true;

                            testProcess = new Process();
                            testProcess.StartInfo = procStartInfo;
                            testProcess.Start();
                        }
                        catch (Exception ex)
                        {
                            testing = false;
                            logger.Error(ex.Message, ex);
                        }
                        break;
                    case "STARTTEST":
                        testing = true;
                        try
                        {
                            string portalTestUrl = "http://127.0.0.1:18080/portal-testserver-war-3.4.2/public/test/startTest.ajax?async=ture";
                            HttpWebRequest request1 = (HttpWebRequest)WebRequest.Create(portalTestUrl);
                            request1.Method = WebRequestMethods.Http.Get;
                            HttpWebResponse response1 = (HttpWebResponse)request1.GetResponse();
                            StreamReader reader1 = new StreamReader(response1.GetResponseStream());
                            string responseString1 = reader1.ReadToEnd();
                            response1.Close();
                            testnode.updateNodeStatus("TESTING");
                        }
                        catch (Exception ex)
                        {
                            testing = false;
                            logger.Error(ex.Message, ex);
                        }
                        break;
                    case "TESTING":
                        testing = true;
                        try
                        {
                            string portalTestResultUrl = "http://127.0.0.1:18080/portal-testserver-war-3.4.2/public/test/data.ajax";
                            HttpWebRequest request2 = (HttpWebRequest)WebRequest.Create(portalTestResultUrl);
                            request2.Method = WebRequestMethods.Http.Get;
                            HttpWebResponse response2 = (HttpWebResponse)request2.GetResponse();
                            StreamReader reader2 = new StreamReader(response2.GetResponseStream());
                            string responseString2 = reader2.ReadToEnd();
                            JArray jsonArray = JArray.Parse(responseString2);
                            JObject deviceJson = (JObject)jsonArray[0];
                            string sessionStatus = (string)deviceJson["sessionStatus"];
                            response2.Close();
                            if ("STOPPED" == sessionStatus.ToUpper())
                            {
                                testing = false;
                                testnode.updateNodeStatus("RUNNING");
                            }
                            else if ("IDLE" == sessionStatus)
                            {
                                testnode.updateNodeStatus("STARTTEST");
                            }
                        }
                        catch (Exception ex)
                        {
                            testing = false;
                            logger.Error(ex.Message, ex);
                        }
                        break;
                }
            }

            checking = false;
        }

        public static void startup()
        {
            Dictionary<int, TestNode> _nodes = getNodesFromDB();
            var enumerator = _nodes.GetEnumerator();
            while (enumerator.MoveNext())
            {
                TestNode testnode = enumerator.Current.Value;
                testnode.updateNodeStatus("RUNNING");
                testnode.startTimer();
            }
        }

        public static void shutdown()
        {
            var enumerator = nodes.GetEnumerator();
            while (enumerator.MoveNext())
            {
                TestNode testnode = enumerator.Current.Value;
                testnode.stop();
            }
        }

        private static void DeleteDir(string dirName)
        {
            ProcessStartInfo procStartInfo = new ProcessStartInfo();
            procStartInfo.Arguments = "/C rd /s /q \"" + dirName + "\"";
            procStartInfo.CreateNoWindow = false;
            procStartInfo.UseShellExecute = true;
            procStartInfo.FileName = "cmd.exe";
            Process proc = new Process();
            proc.StartInfo = procStartInfo;
            proc.Start();
            proc.WaitForExit();
            proc = null;
        }

        private static void DirectoryCopy(string sourceDirName, string destDirName, bool copySubDirs)
        {
            DirectoryInfo dir = new DirectoryInfo(sourceDirName);
            DirectoryInfo[] dirs = dir.GetDirectories();

            if (!dir.Exists)
            {
                throw new DirectoryNotFoundException(
                    "Source directory does not exist or could not be found: "
                    + sourceDirName);
            }

            if (!Directory.Exists(destDirName))
            {
                Directory.CreateDirectory(destDirName);
            }

            FileInfo[] files = dir.GetFiles();
            foreach (FileInfo file in files)
            {
                string temppath = Path.Combine(destDirName, file.Name);
                file.CopyTo(temppath, false);
            }

            if (copySubDirs)
            {
                foreach (DirectoryInfo subdir in dirs)
                {
                    string temppath = Path.Combine(destDirName, subdir.Name);
                    DirectoryCopy(subdir.FullName, temppath, copySubDirs);
                }
            }
        }

        public void Dispose()
        {
            closeTimer();
        }
    }
}
