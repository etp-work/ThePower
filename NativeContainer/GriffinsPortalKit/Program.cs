using System;
using System.Collections.Generic;
using System.Windows.Forms;
using System.ComponentModel;
using System.Diagnostics;

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

        [STAThread]
        static void Main()
        {
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

        static void nativeContainer_FormClosed(object sender, FormClosedEventArgs e)
        {
            proc.Kill();
            proc.Dispose();
            proc = null;
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
