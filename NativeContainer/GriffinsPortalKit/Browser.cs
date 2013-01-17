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
        private int m_Width;
        private int m_Height;

        public Browser(string url, int width, int height)
        {
            this.m_Url = url;
            this.m_Width = width;
            this.m_Height = height;
            InitializeComponent();
        }

        private void Browser_Load(object sender, EventArgs e)
        {
            this.Width = this.m_Width;
            this.Height = this.m_Height;
            this.geckoWebBrowser1.Navigate(this.m_Url);
        }
    }
}
