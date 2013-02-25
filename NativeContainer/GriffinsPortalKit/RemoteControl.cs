using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Configuration;
using Gecko;
using Gecko.DOM;
using Newtonsoft.Json.Linq;

namespace GriffinsPortalKit
{
    public partial class RemoteControl : Form
    {
        public RemoteControl()
        {
            InitializeComponent();
            this.Width = Convert.ToInt32(ConfigurationManager.AppSettings["RemoteControlWidth"]);
            this.Height = Convert.ToInt32(ConfigurationManager.AppSettings["RemoteControlHeight"]);
            this.Left = System.Windows.Forms.Screen.PrimaryScreen.WorkingArea.Right - this.Width * 2;
            this.Top = 0;
            this.geckoWebBrowser1.Navigate(ConfigurationManager.AppSettings["RemoteControlUrl"]);
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
            script.Text += "initRemoteControl();";
            br.Document.Body.AppendChild(script);
        }
    }
}
