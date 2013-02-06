using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Configuration;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using Gecko;
using Gecko.DOM;
using System.Diagnostics;
using System.Runtime.InteropServices;

namespace GriffinsPortalKit
{
    public partial class NativeContainer : Form
    {
        private static string XULRUNNERPATH = "\\xulrunner\\";
        private static List<Form> portals = new List<Form>();
        private GeckoWebBrowser browser;
        private Action<String> message = new Action<String>(NativeContainer.onMessage);
        private SplashForm frmSplash = new SplashForm();

        [DllImport("USER32.DLL", CharSet = CharSet.Unicode)]
        public static extern IntPtr FindWindow(string lpClassName, string lpWindowName);

        // Activate an application window.
        [DllImport("USER32.DLL")]
        public static extern bool SetForegroundWindow(IntPtr hWnd);

        public NativeContainer()
        {
            InitializeComponent();

            Gecko.Xpcom.Initialize(Application.StartupPath + XULRUNNERPATH);
            browser = new GeckoWebBrowser();
            browser.Parent = this;
            browser.Dock = DockStyle.Fill;
            browser.DocumentCompleted += browser_DocumentCompleted;
            browser.DomClick += browser_DomClick;
            browser.CreateControl();

            this.Width = Convert.ToInt32(ConfigurationManager.AppSettings["WindowWidth"]);
            this.Height = Convert.ToInt32(ConfigurationManager.AppSettings["WindowHeight"]);
            this.FormBorderStyle = FormBorderStyle.Fixed3D;
        }

        void browser_DomClick(object sender, DomEventArgs e)
        {
            GeckoElement clicked = e.Target;
            if (clicked.TagName == "INPUT" || clicked.TagName == "SELECT")
            {
                browser.WebBrowserFocus.Activate();
            }
            else
            {
                browser.WebBrowserFocus.Deactivate();
            }
        }

        void browser_DocumentCompleted(object sender, EventArgs e)
        {
            GeckoWebBrowser br = sender as GeckoWebBrowser;
            browser.AddMessageEventListener("startPortal", message);
            browser.WebBrowserFocus.Deactivate();
            this.Show();
        }

        static void onMessage(String message)
        {
            if (message.StartsWith("STARTPORTAL:"))
            {
                foreach (Form portal in portals)
                {
                    portal.Close();
                }
                portals.Clear();

                message = message.Replace("STARTPORTAL:", "").ToUpper();
                switch (message)
                {
                    case "STBHTML":
                        string portalFullUrl = String.Format("http://{0}:{1}{2}", ConfigurationManager.AppSettings["PortalAddress"], ConfigurationManager.AppSettings["PortalPort"], ConfigurationManager.AppSettings["PortalUrl_STBHTML"]);
                        STBHTML portalSTBHTML = new STBHTML(portalFullUrl);
                        portalSTBHTML.Visible = true;
                        portals.Add(portalSTBHTML);
                        break;
                    /*case "IPAD":
                        IPAD portalIPAD = new IPAD(ConfigurationManager.AppSettings["PORTAL_IPAD"]);
                        portalIPAD.Visible = true;
                        portals.Add(portalIPAD);
                        break;*/
                }

            }
            else if (message.StartsWith("EXECMD:"))
            {
                message = message.Replace("EXECMD:", "");
                ProcessStartInfo procStartInfo = new ProcessStartInfo("cmd.exe", "/c " + message);

                //procStartInfo.FileName = "cmd.exe";
                procStartInfo.WorkingDirectory = "c:\\";
                //procStartInfo.Arguments = "/c " + message;

                procStartInfo.RedirectStandardOutput = true;
                procStartInfo.UseShellExecute = false;
                procStartInfo.CreateNoWindow = true;

                Process proc = new Process();
                proc.StartInfo = procStartInfo;
                proc.Start();
                string result = proc.StandardOutput.ReadToEnd();
                MessageBox.Show(result);
            }
            else if (message.StartsWith("BUILD:"))
            {
                message = message.Replace("BUILD:", "");
                ProcessStartInfo procStartInfo = new ProcessStartInfo("mvn", "clean install");
                procStartInfo.WorkingDirectory = message;
                //procStartInfo.RedirectStandardOutput = true;
                procStartInfo.UseShellExecute = true;
                procStartInfo.CreateNoWindow = false;

                Process proc = new Process();
                proc.StartInfo = procStartInfo;
                proc.Start();
                //proc.WaitForExit();
                //string result = proc.StandardOutput.ReadToEnd();
                //MessageBox.Show(result);
            }
            else if (message.StartsWith("KEY:"))
            {
                message = message.Replace("KEY:", "");
                SendKeys.SendWait(message);
            }
        }

        private void SplashScreen_Load(object sender, EventArgs e)
        {
            this.Hide();
            frmSplash.ShowDialog();
            string serverFullUrl = "";
            if (null != ConfigurationManager.AppSettings["ServerAddress"] && null != ConfigurationManager.AppSettings["ServerPort"] && null != ConfigurationManager.AppSettings["ServerUrl"])
            {
                serverFullUrl = String.Format("http://{0}:{1}{2}", ConfigurationManager.AppSettings["ServerAddress"], ConfigurationManager.AppSettings["ServerPort"], ConfigurationManager.AppSettings["ServerUrl"]);
            }

            if (null != ConfigurationManager.AppSettings["ServerFullUrl"])
            {
                serverFullUrl = ConfigurationManager.AppSettings["ServerFullUrl"];
            }
            browser.Navigate(serverFullUrl);
        }

        private void NativeContainer_Resize(object sender, EventArgs e)
        {
            if (WindowState == FormWindowState.Minimized)
            {
                Hide();

                notifyIcon1.BalloonTipTitle = "Portal Kit Hidden";
                notifyIcon1.BalloonTipText = "Your Portal Kit has been minimized to the taskbar.";
                notifyIcon1.ShowBalloonTip(3000);
            }
        }

        private void notifyIcon1_DoubleClick(object sender, EventArgs e)
        {
            Show();
            WindowState = FormWindowState.Normal;
        }
    }
}
