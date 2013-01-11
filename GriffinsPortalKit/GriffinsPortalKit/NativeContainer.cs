using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Configuration;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using Gecko;

namespace GriffinsPortalKit
{
    public partial class NativeContainer : Form
    {
        static private string XULRUNNERPATH = "\\xulrunner\\";
        private GeckoWebBrowser browser;

        public NativeContainer()
        {
            InitializeComponent();

            Gecko.Xpcom.Initialize(Application.StartupPath + XULRUNNERPATH);
            browser = new GeckoWebBrowser();
            browser.Parent = this;
            browser.Dock = DockStyle.Fill;
            browser.DocumentCompleted += browser_DocumentCompleted;
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
            browser.Navigate(serverFullUrl);
        }

        void browser_DocumentCompleted(object sender, EventArgs e)
        {
            GeckoWebBrowser br = sender as GeckoWebBrowser;
            if (br.Url.ToString() == "about:blank") { return; }
        }
    }
}
