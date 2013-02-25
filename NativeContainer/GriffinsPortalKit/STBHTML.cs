using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Gecko;
using Gecko.DOM;
using Newtonsoft.Json.Linq;
using System.Runtime.InteropServices;

namespace GriffinsPortalKit
{
    public partial class STBHTML : Form
    {
        private string m_Url;

        [DllImport("user32.dll", SetLastError = true)]
        static extern void keybd_event(byte bVk, byte bScan, uint dwFlags, UIntPtr dwExtraInfo);
        public static void PressKey(Keys key, bool up)
        {
            const int KEYEVENTF_EXTENDEDKEY = 0x1;
            const int KEYEVENTF_KEYUP = 0x2;
            if (up)
            {
                keybd_event((byte)key, 0x45, KEYEVENTF_EXTENDEDKEY | KEYEVENTF_KEYUP, (UIntPtr)0);
            }
            else
            {
                keybd_event((byte)key, 0x45, KEYEVENTF_EXTENDEDKEY, (UIntPtr)0);
            }
        }

        public STBHTML(string url)
        {
            this.m_Url = url;
            InitializeComponent();
        }

        private void STBHTML_Load(object sender, EventArgs e)
        {
            this.geckoWebBrowser1.Navigate(this.m_Url);
            this.geckoWebBrowser1.GetMarkupDocumentViewer().SetFullZoomAttribute(0.8f);
            this.geckoWebBrowser1.DocumentCompleted += geckoWebBrowser1_DocumentCompleted;
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
            script.Text += "window.autoLogin = 'user1';";
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
                            this.Focus();
                            SendKeys.Send(key);
                            break;
                    }
                    break;
            }
        }
    }
}
