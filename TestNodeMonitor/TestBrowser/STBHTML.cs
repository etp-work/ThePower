using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Imaging;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Configuration;
using CefSharp;
using CefSharp.WinForms;

namespace TestBrowser
{
    public partial class STBHTML : Form
    {
        private string m_Url;
        private string m_BaseDir;
        private WebView web_view;

        public STBHTML()
        {
            this.m_BaseDir = ConfigurationManager.AppSettings["PortalBaseDir"];
            this.m_Url = ConfigurationManager.AppSettings["PortalUrl"];
            InitializeComponent();
        }

        private void STBHTML_Load(object sender, EventArgs e)
        {
            BrowserSettings settings = new BrowserSettings();
            web_view = new WebView(this.m_Url, settings);
            web_view.Dock = DockStyle.Fill;
            this.Controls.Add(web_view);
        }

        private void capture()
        {
            int width = this.Bounds.Width;
            int height = this.Bounds.Height;
            Bitmap b = new Bitmap(width, height);
            Graphics g = Graphics.FromImage(b);
            g.CopyFromScreen(this.Location.X, this.Location.Y, 0, 0, this.Bounds.Size);
            string fileName = String.Format("{0}\\webapps\\ROOT\\portal.png", this.m_BaseDir);
            b.Save(fileName, ImageFormat.Jpeg);
        }
    }
}
