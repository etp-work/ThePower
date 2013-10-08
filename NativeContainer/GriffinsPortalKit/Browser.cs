using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Runtime.InteropServices;
using System.Configuration;
using Gecko;
using Gecko.DOM;
using Newtonsoft.Json.Linq;

namespace DevelopmentToolkit
{
    public partial class Browser : Form
    {
        private string m_Url;
        private JObject m_Param;

        public Browser(string p_Url, JObject p_Param)
        {
            this.m_Url = p_Url;
            this.m_Param = p_Param;
            InitializeComponent();
            this.Left = 0;
            this.Top = 0;
            this.geckoWebBrowser1.Navigate(ConfigurationManager.AppSettings["RemoteControlUrl"]);
            this.geckoWebBrowser1.DocumentCompleted += geckoWebBrowser1_DocumentCompleted;
            this.geckoWebBrowser2.Navigate(p_Url);
            this.geckoWebBrowser2.DocumentCompleted += geckoWebBrowser2_DocumentCompleted;
        }

        void geckoWebBrowser2_DocumentCompleted(object sender, EventArgs e)
        {
            GeckoWebBrowser br = sender as GeckoWebBrowser;
            string wsuri = ConfigurationManager.AppSettings["WebSocketUri"];
            GeckoScriptElement script = (GeckoScriptElement)br.Document.CreateElement("script");
            script.Type = "text/javascript";
            script.Text = "window.wsuri = '" + wsuri + "';";
            script.Text += "window.nativeID = '" + Program.nativeID.ToString() + "';";
            script.Text += "window.portalID = '" + Program.portalID.ToString() + "';";
            br.Document.Body.AppendChild(script);
        }

        void geckoWebBrowser1_DocumentCompleted(object sender, EventArgs e)
        {
            GeckoWebBrowser br = sender as GeckoWebBrowser;
            string wsuri = ConfigurationManager.AppSettings["WebSocketUri"];
            GeckoScriptElement script = (GeckoScriptElement)br.Document.CreateElement("script");
            script.Type = "text/javascript";
            script.Text = "window.wsuri = '" + wsuri + "';";
            script.Text += "window.nativeID = '" + Program.nativeID.ToString() + "';";
            script.Text += "window.portalID = '" + Program.portalID.ToString() + "';";
            script.Text += "initRemoteControl();";
            br.Document.Body.AppendChild(script);
        }

        public void onMessage(JObject json)
        {
            string type = (string)json["type"];
            switch (type)
            {
                case "REMOTECONTROL":
                    string key = (string)json["key"];
                    switch (key)
                    {
                        default:
                            if (key.Length > 1)
                            {
                                key = "{" + key + "}";
                            }
                            this.geckoWebBrowser2.Focus();
                            SendKeys.SendWait(key);
                            break;
                    }
                    break;
            }
        }
    }
}
