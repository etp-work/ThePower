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
    public partial class STBHTML : Form
    {
        private string m_Url;

        public STBHTML(string url)
        {
            this.m_Url = url;
            InitializeComponent();
        }

        private void STBHTML_Load(object sender, EventArgs e)
        {
            this.geckoWebBrowser1.Navigate(this.m_Url);
            this.geckoWebBrowser1.GetMarkupDocumentViewer().SetFullZoomAttribute(0.8f);
        }
    }
}
