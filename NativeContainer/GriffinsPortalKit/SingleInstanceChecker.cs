using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Diagnostics;
using System.Windows.Forms;

namespace DevelopmentToolkit
{
    public class SingleInstanceChecker
    {
        public static void checkThePowerRunning() {
            Process[] processes = Process.GetProcessesByName("DevelopmentToolkit");
            if (processes != null && processes.Length > 1)
            {
                 throw new Exception("You can only run one instance at one time.");   
            }
        }
    }
}
