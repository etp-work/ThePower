using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Diagnostics;

namespace GriffinsPortalKit
{
    public partial class SplashForm : Form
    {
        static private string TOMCATPATH = "\\tomcat\\bin\\";
        bool fadeIn = true;
        bool fadeOut = false;
        private BackgroundWorker worker;

        public SplashForm()
        {
            InitializeComponent();

            /*
            this.worker = new BackgroundWorker();
            this.worker.DoWork += worker_DoWork;
            this.worker.ProgressChanged += worker_ProgressChanged;
            this.worker.RunWorkerCompleted += worker_RunWorkerCompleted;
            this.worker.WorkerReportsProgress = true;
            this.worker.RunWorkerAsync();
             * */

            this.Opacity = 0.5;
            
        }

        void worker_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            fadeIn = false;
            fadeOut = true;
        }

        void worker_ProgressChanged(object sender, ProgressChangedEventArgs e)
        {
            // e.ProgressPercentage.ToString() + "% complete";
        }

        void worker_DoWork(object sender, DoWorkEventArgs e)
        {
            ProcessStartInfo procStartInfo = new ProcessStartInfo("startup.bat");
            procStartInfo.WorkingDirectory = Application.StartupPath + TOMCATPATH;

            //procStartInfo.RedirectStandardOutput = true;
            procStartInfo.UseShellExecute = true;
            procStartInfo.CreateNoWindow = false;

            Process proc = new Process();
            proc.StartInfo = procStartInfo;
            proc.Start();
            proc.WaitForExit();

            BackgroundWorker worker = (BackgroundWorker)sender;
            worker.ReportProgress(100);
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            // Fade in by increasing the opacity of the splash to 1.0
            if (fadeIn)
            {
                if (this.Opacity < 1.0)
                {
                    this.Opacity += 0.02;
                }
                // After fadeIn complete, begin fadeOut
                else
                {
                    fadeIn = false;
                    fadeOut = true;
                }
            }
            else if (fadeOut) // Fade out by increasing the opacity of the splash to 1.0
            {
                if (this.Opacity > 0)
                {
                    this.Opacity -= 0.02;
                }
                else
                {
                    fadeOut = false;
                }
            }

            // After fadeIn and fadeOut complete, stop the timer and close this splash. 
            if (!(fadeIn || fadeOut))
            {
                timer1.Stop();
                this.Close();
            }
        }
    }
}
