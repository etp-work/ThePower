using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using CefSharp;
using CefSharp.WinForms;
using Newtonsoft.Json.Linq;
using System.Runtime.InteropServices;
using System.Configuration;

namespace DevelopmentToolkit
{
    public partial class Browser : Form
    {
        private WebView m_WebView;
        private RemoteControl m_RemoteControl;
        private string m_Url;
        private JObject m_Param;

        public Browser(string p_Url, JObject p_Param)
        {
            this.m_Url = p_Url;
            this.m_Param = p_Param;
            m_RemoteControl = new RemoteControl();
            InitializeComponent();
            this.Left = 0;
            this.Top = 0;
        }

        private void Browser_Load(object sender, EventArgs e)
        {
            BrowserSettings settings = new BrowserSettings();
            m_WebView = new WebView(this.m_Url, settings);
            m_WebView.Dock = DockStyle.Fill;
            this.Controls.Add(m_WebView);
            m_WebView.PropertyChanged += web_view_PropertyChanged;
        }

        void web_view_PropertyChanged(object sender, PropertyChangedEventArgs e)
        {
            switch (e.PropertyName)
            {
                case "IsBrowserInitialized":
                    string wsuri = ConfigurationManager.AppSettings["WebSocketUri"];
                    m_WebView.ExecuteScript("window.wsuri = '" + wsuri + "';");
                    m_WebView.ExecuteScript("window.nativeID = '" + Program.nativeID.ToString() + "';");
                    m_WebView.ExecuteScript("window.portalID = '" + Program.portalID.ToString() + "';");
                    m_WebView.ExecuteScript("window.autoLogin = 'user1';");
                    if ((bool)this.m_Param["enableDevTool"])
                    {
                        m_WebView.ShowDevTools();

                    }

                    if ((bool)this.m_Param["enableRemoteControl"])
                    {
                        m_RemoteControl.Visible = true;
                    }
                    break;
            }
        }

        public void onMessage(JObject json)
        {
            string type = (string)json["type"];
            switch (type)
            {
                case "ENABLEDEVTOOL":
                    if ((bool)json["enableDevTool"])
                    {
                        m_WebView.ShowDevTools();
                    }
                    else
                    {
                        m_WebView.CloseDevTools();
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
