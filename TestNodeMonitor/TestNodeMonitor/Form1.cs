using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Diagnostics;

namespace TestNodeMonitor
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            ProcessStartInfo procStartInfo = new ProcessStartInfo("startup.bat");

            //procStartInfo.FileName = "cmd.exe";
            procStartInfo.WorkingDirectory = "C:\\Griffins\\tomcat\\bin\\";
            //procStartInfo.Arguments = "/c " + message;

            procStartInfo.UseShellExecute = true;

            Process proc = new Process();
            proc.StartInfo = procStartInfo;
            proc.Start();


        }
    }
}
