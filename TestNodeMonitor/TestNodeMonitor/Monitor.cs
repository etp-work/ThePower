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
using System.IO;
using TestNodeMonitor.Entities;

namespace TestNodeMonitor
{
    public partial class Monitor : Form
    {
        public Monitor()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }

        private void button1_Click(object sender, EventArgs e)
        {
            timer1.Enabled = true;
            button1.Enabled = false;
            button2.Enabled = true;
            TestNode.startup();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            timer1.Enabled = false;
            button1.Enabled = true;
            button2.Enabled = false;
            TestNode.shutdown();
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            TestNode.check();
        }

        private void Monitor_FormClosed(object sender, FormClosedEventArgs e)
        {
            TestNode.shutdown();
        }
    }
}
