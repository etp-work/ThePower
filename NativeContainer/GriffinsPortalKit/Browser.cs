using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace GriffinsPortalKit
{
    public partial class Browser : Form
    {
        private string m_Url;
        private string m_Type;

        public Browser(string url, string type)
        {
            this.m_Url = url;
            this.m_Type = type;
            InitializeComponent();
        }

        private void Browser_Load(object sender, EventArgs e)
        {
            this.geckoWebBrowser1.Navigate(this.m_Url);
            this.geckoWebBrowser1.GetMarkupDocumentViewer().SetFullZoomAttribute(0.8f);
        }
    }
}
