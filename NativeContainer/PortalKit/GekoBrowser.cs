using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Newtonsoft.Json.Linq;
using Gecko;
using Gecko.DOM;
using log4net;
using System.Reflection;

namespace GriffinsPortalKit
{
    public partial class GekoBrowser : Form
    {
        private static string XULRUNNERPATH = "\\xulrunner\\";
        private static ILog logger = log4net.LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private GeckoWebBrowser browser;
        private RemoteControl m_RemoteControl;
        private string m_Url;
        private JObject m_Param;

        public GekoBrowser(string p_Url, JObject p_Param)
        {
            this.m_Url = p_Url;
            this.m_Param = p_Param;
            m_RemoteControl = new RemoteControl();
            InitializeComponent();
            this.Left = 100;
            this.Top = 100;
        }

        private void GekoBrowser_Load(object sender, EventArgs e)
        {
            Gecko.Xpcom.Initialize(Application.StartupPath + XULRUNNERPATH);
            browser = new GeckoWebBrowser();
            browser.Dock = DockStyle.Fill;
            this.Controls.Add(browser);
            browser.Parent = this;
            browser.Dock = DockStyle.Fill;

            this.FormBorderStyle = FormBorderStyle.FixedToolWindow;
            browser.DocumentCompleted += browser_DocumentCompleted;
            browser.CreateControl();
            browser.Navigate(this.m_Url);
        }

        void browser_DocumentCompleted(object sender, EventArgs e)
        {
            //
        }

        public void onMessage(JObject json)
        {
            string type = (string)json["type"];
            switch (type)
            {
                case "ENABLEDEVTOOL":
                    if ((bool)json["enableDevTool"])
                    {
                        //browser.ShowDevTools();
                    }
                    else
                    {
                        //browser.CloseDevTools();
                    }
                    break;
                case "ENABLEREMOTECONTROL":
                    if ((bool)json["enableRemoteControl"])
                    {
                        m_RemoteControl.Visible = true;
                    }
                    else
                    {
                        m_RemoteControl.Visible = false;
                    }
                    break;
                case "REMOTECONTROL":
                    string key = (string)json["key"];
                    switch (key)
                    {
                        default:
                            if (key.Length > 1)
                            {
                                key = "{" + key + "}";
                            }
                            this.Focus();
                            SendKeys.SendWait(key);
                            break;
                    }
                    break;
            }
        }
    }
}
