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

namespace GriffinsPortalKit
{
    public partial class IPAD : Form
    {
        private string m_Url;
        private WebView web_view;

        public IPAD(string url)
        {
            this.m_Url = url;
            InitializeComponent();
        }

        private void IPAD_Load(object sender, EventArgs e)
        {
            BrowserSettings settings = new BrowserSettings();
            web_view = new WebView(this.m_Url, settings);
            web_view.Dock = DockStyle.Fill;
            //web_view.ShowDevTools();
            this.Controls.Add(web_view);
            //this.geckoWebBrowser1.Navigate(this.m_Url);
            //this.geckoWebBrowser1.GetMarkupDocumentViewer().SetFullZoomAttribute(0.5f);
        }
    }
}
