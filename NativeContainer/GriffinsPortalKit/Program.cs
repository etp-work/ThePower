using System;
using System.Collections.Generic;
using System.Windows.Forms;
using System.ComponentModel;
using System.Diagnostics;
using System.Net;
using System.Net.Sockets;
using SuperSocket.SocketBase;
using SuperWebSocket;
using System.Configuration;
using System.Reflection;
using log4net;
using Newtonsoft.Json.Linq;

[assembly: log4net.Config.XmlConfigurator(Watch = true)]
namespace GriffinsPortalKit
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>

        static BackgroundWorker worker;
        static NativeContainer nativeContainer;
        static Process proc;
        static WebSocketServer websocketserver;
        static Dictionary<String, WebSocketSession> sessions = new Dictionary<String, WebSocketSession>();
        public static Guid nativeID;
        public static Guid portalID;
        static ILog logger = log4net.LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        [STAThread]
        static void Main()
        {
            try
            {
                nativeID = Guid.NewGuid();
                portalID = Guid.NewGuid();
                websocketserver = new WebSocketServer();
                int port = Int32.Parse(ConfigurationManager.AppSettings["WebSocketPort"]);
                if (!websocketserver.Setup(port))
                {
                    throw new Exception("ERROR:" + port + " already used!");
                }

                if (!websocketserver.Start())
                {
                    throw new Exception("ERROR:Failed to start server!");
                }

                websocketserver.NewMessageReceived += websocketserver_NewMessageReceived;
            }
            catch (Exception e)
            {
                logger.Error(e.Message, e);
                MessageBox.Show(e.Message);
                return;
            }

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            worker = new BackgroundWorker();
            worker.DoWork += worker_DoWork;
            worker.ProgressChanged += worker_ProgressChanged;
            worker.RunWorkerCompleted += worker_RunWorkerCompleted;
            worker.WorkerReportsProgress = true;
            worker.RunWorkerAsync();

            nativeContainer = new NativeContainer();
            nativeContainer.FormClosed += nativeContainer_FormClosed;
            Application.Run(nativeContainer);
        }


        static void websocketserver_NewMessageReceived(WebSocketSession session, string value)
        {
            logger.Debug("WebSocket Server NewMessageReceived:" + value + ", Session:" + session.SessionID);
            if (value.StartsWith("ID:"))
            {
                sessions.Remove(value);
                sessions.Add(value, session);
            }
            else
            {
                try
                {
                    JObject json = JObject.Parse(value);
                    string to = "ID:" + (string)json["to"];
                    if (sessions.ContainsKey(to))
                    {
                        WebSocketSession toSession = sessions[to];
                        toSession.Send(value);
                    }
                    else
                    {
                        logger.Error("Message Destination Not Found:" + to);
                    }
                }
                catch (Exception ex)
                {
                    logger.Error(ex.Message, ex);
                }
            }
        }

        static void nativeContainer_FormClosed(object sender, FormClosedEventArgs e)
        {
            if (null != proc)
            {
                proc.Kill();
                proc.Dispose();
                proc = null;
            }

            if (null != websocketserver)
            {
                if (websocketserver.State == ServerState.Running)
                {
                    websocketserver.Stop();
                }
                websocketserver.Dispose();
                websocketserver = null;
            }
        }

        static void worker_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            // complete
        }

        static void worker_ProgressChanged(object sender, ProgressChangedEventArgs e)
        {
            // e.ProgressPercentage.ToString() + "% complete";
        }

        static void worker_DoWork(object sender, DoWorkEventArgs e)
        {
            string javahome = Environment.GetEnvironmentVariable("JAVA_HOME");
            string tomcathome = Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData);
            ProcessStartInfo procStartInfo = new ProcessStartInfo(javahome + "\\bin\\java.exe", "-jar EmbeddedTomcat.jar port=58888");
            procStartInfo.WorkingDirectory = tomcathome + "\\CustomizedTomcat";
            procStartInfo.UseShellExecute = false;
            procStartInfo.CreateNoWindow = true;

            proc = new Process();
            proc.StartInfo = procStartInfo;
            proc.Start();

            BackgroundWorker worker = (BackgroundWorker)sender;
            worker.ReportProgress(100);
        }
    }
}
