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

namespace GriffinsPortalKit
{
    public partial class NativeContainer : Form
    {
        static private string XULRUNNERPATH = "\\xulrunner\\";
        private GeckoWebBrowser browser;
        private Action<String> message = new Action<String>(NativeContainer.onMessage);
        private SplashForm frmSplash = new SplashForm();

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

            string serverFullUrl = "";

            if (null != ConfigurationManager.AppSettings["ServerAddress"] && null != ConfigurationManager.AppSettings["ServerPort"] && null != ConfigurationManager.AppSettings["ServerUrl"])
            {
                serverFullUrl = String.Format("http://{0}:{1}{2}", ConfigurationManager.AppSettings["ServerAddress"], ConfigurationManager.AppSettings["ServerPort"], ConfigurationManager.AppSettings["ServerUrl"]);
            }

            if (null != ConfigurationManager.AppSettings["ServerFullUrl"])
            {
                serverFullUrl = ConfigurationManager.AppSettings["ServerFullUrl"];
            }

            this.Width = Convert.ToInt32(ConfigurationManager.AppSettings["WindowWidth"]);
            this.Height = Convert.ToInt32(ConfigurationManager.AppSettings["WindowHeight"]);
            this.FormBorderStyle = FormBorderStyle.Fixed3D;
            browser.Navigate(serverFullUrl);
        }

        void browser_DomClick(object sender, DomEventArgs e)
        {
            browser.WebBrowserFocus.Activate();
        }

        void browser_DocumentCompleted(object sender, EventArgs e)
        {
            GeckoWebBrowser br = sender as GeckoWebBrowser;
            if (br.Url.ToString() == "about:blank") { return; }
            browser.AddMessageEventListener("startPortal", message);

            // Call JavaScript
            /*
            GeckoScriptElement script = (GeckoScriptElement)br.Document.CreateElement("script");
            script.Type = "text/javascript";
            script.Text = "alert('My alert');";
            br.Document.Body.AppendChild(script);
            */
        }

        static void onMessage(String message)
        {
            if (message.StartsWith("STARTPORTAL:"))
            {
                message = message.Replace("STARTPORTAL:", "");
                Browser portal = new Browser(message, 720, 596);
                portal.Visible = true;
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
        }

        private void SplashScreen_Load(object sender, EventArgs e)
        {
            frmSplash.ShowDialog();
            this.Show();
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
