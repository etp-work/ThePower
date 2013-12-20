using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.ComponentModel;
using System.Diagnostics;
using System.IO;

namespace DevelopmentToolkit
{
    public class EmbeddedTomcatController
    {
        private BackgroundWorker worker;
        private Process process;

        private static string TOMCAT_HOME = Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData) + @"\CustomizedTomcat";
        private static string PROCESS_ID_FILE = "RUNNING_PROCESS.INI";
        
        public EmbeddedTomcatController() {
            worker = new BackgroundWorker();
            worker.DoWork += worker_DoWork;
            worker.WorkerReportsProgress = true;
        }

        public void startTomcat() {
            if (File.Exists(TOMCAT_HOME + @"\" + PROCESS_ID_FILE))
            {
                File.Delete(TOMCAT_HOME + @"\" + PROCESS_ID_FILE);
            }
            worker.RunWorkerAsync();
        }

        public void stopTomcat() {
            if (null != process)
            {
                try
                {
                    process.Kill();
                    process.Dispose();
                }catch(Exception){
                   //
                }
                process = null;
            }
            if (File.Exists(TOMCAT_HOME + @"\" + PROCESS_ID_FILE))
            {
                File.Delete(TOMCAT_HOME + @"\" + PROCESS_ID_FILE);
            }
        }

        public static void killExist() { 
            if(File.Exists(TOMCAT_HOME+@"\"+PROCESS_ID_FILE)){
                try
                {
                    Process proc = Process.GetProcessById(readProcessId());
                    proc.Kill();
                }
                catch (Exception ex)
                {
                    //
                }
              File.Delete(TOMCAT_HOME + @"\" + PROCESS_ID_FILE);
            }
        }

        private static int readProcessId() {
            int id = 0;
            TextReader tr = null;
            try
            {
                tr = new StreamReader(TOMCAT_HOME + @"\" + PROCESS_ID_FILE);
                id = int.Parse(tr.ReadLine());
                
            }
            catch (Exception ex)
            {
                //
            }
            finally {
                tr.Close();
            }
            return id;
        }

        private static void writeProcessId(int processId)
        {
            int id = 0;
            TextWriter tw = null;
            try
            {
                tw = new StreamWriter(TOMCAT_HOME + @"\" + PROCESS_ID_FILE);
                tw.WriteLine(processId);

            }
            catch (Exception ex)
            {
                //
            }
            finally
            {
                tw.Close();
            }
        }

        private void worker_DoWork(object sender, DoWorkEventArgs e)
        {
            string javahome = Environment.GetEnvironmentVariable("JAVA_HOME");
            ProcessStartInfo procStartInfo = new ProcessStartInfo();
            procStartInfo.FileName = javahome + "\\bin\\java.exe";
            procStartInfo.Arguments = "-jar EmbeddedTomcat.jar port=58888";
            procStartInfo.WorkingDirectory = TOMCAT_HOME;
            procStartInfo.UseShellExecute = false;
            procStartInfo.CreateNoWindow = true;

            process = new Process();
            process.StartInfo = procStartInfo;
            process.Start();

            BackgroundWorker worker = (BackgroundWorker)sender;
            worker.ReportProgress(100);
            writeProcessId(process.Id);
        }
    }
}
