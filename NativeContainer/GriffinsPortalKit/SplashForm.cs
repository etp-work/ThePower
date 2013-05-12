using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace DevelopmentToolkit
{
    public partial class ThePower : Form
    {
        bool fadeIn = true;
        bool fadeOut = false;

        public ThePower()
        {
            InitializeComponent();
            this.Opacity = 0.5;
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            // Fade in by increasing the opacity of the splash to 1.0
            if (fadeIn)
            {
                if (this.Opacity < 1.0)
                {
                    this.Opacity += 0.01;
                }
                // After fadeIn complete, begin fadeOut
                else
                {
                    fadeIn = false;
                    fadeOut = true;
                }
            }
            else if (fadeOut) // Fade out by increasing the opacity of the splash to 1.0
            {
                if (this.Opacity > 0)
                {
                    this.Opacity -= 0.01;
                }
                else
                {
                    fadeOut = false;
                }
            }

            // After fadeIn and fadeOut complete, stop the timer and close this splash. 
            if (!(fadeIn || fadeOut))
            {
                stopAndClose();
            }
        }

        public void stopAndClose() {
            if (this.Visible)
            {
                timer1.Stop();
                this.Hide();
            }
        }
    }
}
