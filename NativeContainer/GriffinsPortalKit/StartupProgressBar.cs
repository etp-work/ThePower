using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace GriffinsPortalKit
{
    public partial class StartupProgressBar : Form
    {
        private BackgroundWorker worker;

        private bool RUNNING;

        public StartupProgressBar()
        {
            InitializeComponent();
            worker = new BackgroundWorker();
            worker.WorkerReportsProgress = true;
            worker.WorkerSupportsCancellation = true;
            worker.DoWork += worker_DoWork;
            worker.ProgressChanged += worker_ProgressChanged;
            worker.RunWorkerCompleted += worker_RunWorkerCompleted;
        }

        public void start() {
            worker.RunWorkerAsync();
            RUNNING = true;
        }

        public void stop() {
            RUNNING = false;
        }

        private void worker_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            if (this.Visible) {
                this.Hide();
            }
        }

        private void worker_ProgressChanged(object sender, ProgressChangedEventArgs e)
        {
            this.progressBar.Value = e.ProgressPercentage * 4;
        }

        private void worker_DoWork(object sender, DoWorkEventArgs e)
        {
            BackgroundWorker worker = sender as BackgroundWorker;
            int i = 0;
            while (RUNNING)
            {
                if(i==99){
                    System.Threading.Thread.Sleep(150);
                    i--;
                    continue;
                }
                // Perform a time consuming operation and report progress.
                System.Threading.Thread.Sleep(150);
                worker.ReportProgress(i);
                i++;
            }
            worker.ReportProgress(100);
        }
    }
}
