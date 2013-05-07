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
using WebSocket4Net;
using System.Reflection;
using log4net;
using Newtonsoft.Json.Linq;
using System.Threading;

namespace GriffinsPortalKit
{
    public partial class NativeContainer : Form
    {
        private static string XULRUNNERPATH = "\\xulrunner\\";
        private static ILog logger = log4net.LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private Browser portal;
        private GeckoWebBrowser browser;
        private ThePower frmSplash;
        private WebSocket websocketclient;

        [DllImport("USER32.DLL", CharSet = CharSet.Unicode)]
        public static extern IntPtr FindWindow(string lpClassName, string lpWindowName);

        // Activate an application window.
        [DllImport("USER32.DLL")]
        public static extern bool SetForegroundWindow(IntPtr hWnd);

        public NativeContainer()
        {
            InitializeComponent();
            //this.autoHideHandler.start();
            Gecko.Xpcom.Initialize(Application.StartupPath + XULRUNNERPATH);
            //GeckoPreferences.User["gfx.font_rendering.graphite.enabled"] = true;
            browser = new GeckoWebBrowser();
            browser.Parent = this;
            browser.Dock = DockStyle.Fill;
            browser.DocumentCompleted += browser_DocumentCompleted;
            browser.DomClick += browser_DomClick;
            browser.CreateControl();

            this.Width = Convert.ToInt32(ConfigurationManager.AppSettings["WindowWidth"]);
            this.Height = Convert.ToInt32(ConfigurationManager.AppSettings["WindowHeight"]);
            this.Left = System.Windows.Forms.Screen.PrimaryScreen.WorkingArea.Right - this.Width;
            this.Top = 0;
            this.FormBorderStyle = FormBorderStyle.Fixed3D;
        }

        public void initPortalKitUrl() {
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
            this.Show();
            if (this.frmSplash != null)
            {
                this.frmSplash.stopAndClose();
            }
            GeckoWebBrowser br = sender as GeckoWebBrowser;
            br.WebBrowserFocus.Deactivate();
            string wsuri = ConfigurationManager.AppSettings["WebSocketUri"];
            GeckoScriptElement script = (GeckoScriptElement)br.Document.CreateElement("script");
            script.Type = "text/javascript";
            script.Text = "window.wsuri = '" + wsuri + "';";
            script.Text += "window.nativeID = '" + Program.nativeID.ToString() + "';";
            script.Text += "window.portalID = '" + Program.portalID.ToString() + "';";
            br.Document.Body.AppendChild(script);
            websocketclient = new WebSocket(wsuri);
            websocketclient.Opened += websocketclient_Opened;
            websocketclient.Error += websocketclient_Error;
            websocketclient.Closed += websocketclient_Closed;
            websocketclient.MessageReceived += websocketclient_MessageReceived;
            websocketclient.Open();
        }

        void websocketclient_Opened(object sender, EventArgs e)
        {
            logger.Debug("WebSocket Native Client Opened");
            websocketclient.Send("ID:" + Program.nativeID.ToString());
        }

        void websocketclient_Error(object sender, SuperSocket.ClientEngine.ErrorEventArgs e)
        {
            logger.Debug("WebSocket Native Client Error");
        }

        void websocketclient_Closed(object sender, EventArgs e)
        {
            logger.Debug("WebSocket Native Client Closed");
        }

        void websocketclient_MessageReceived(object sender, MessageReceivedEventArgs e)
        {
            logger.Debug("WebSocket Native Client MessageReceived:" + e.Message);
            try
            {
                JObject json = JObject.Parse(e.Message);
                string type = (string)json["type"];
                switch (type)
                {
                    case "STARTPORTAL":
                        string portaltype = (string)json["portaltype"];
                        switch (portaltype)
                        {
                            case "STBHTML":
                                Thread portalThread = new Thread(new ParameterizedThreadStart(startPortal));
                                portalThread.Start(json);
                                break;
                            /*case "IPAD":
                                IPAD portalIPAD = new IPAD(ConfigurationManager.AppSettings["PORTAL_IPAD"]);
                                portalIPAD.Visible = true;
                                portals.Add(portalIPAD);
                                break;*/
                        }
                        break;
                    default:
                        this.BeginInvoke((Action)delegate
                        {
                            if (null != portal)
                            {
                                portal.onMessage(json);
                            }
                        });
                        break;
                }
            }
            catch (Exception ex)
            {
                logger.Error(ex.Message, ex);
            }
        }

        private void startPortal(object param)
        {
            JObject json = (JObject)param;
            this.BeginInvoke((Action)delegate
            {
                if (null != portal)
                {
                    portal.Close();
                }
                string portalFullUrl = String.Format("http://{0}:{1}{2}", ConfigurationManager.AppSettings["PortalAddress"], ConfigurationManager.AppSettings["PortalPort"], ConfigurationManager.AppSettings["PortalUrl_STBHTML"]);
                portal = new Browser(portalFullUrl, json);
                portal.Visible = true;
            });
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
                this.Show();
                WindowState = FormWindowState.Normal;
        }

        public ThePower FrmSplash {
            set { this.frmSplash = value; }
        }
    }
}
