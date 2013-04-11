using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Drawing;

namespace GriffinsPortalKit
{
    public class AutoHideHandler
    {

        private AnchorStyles stopAnchor = AnchorStyles.None;

        private Form nativeContainer;
        private Timer timer;

        public AutoHideHandler()
        {
            //
        }

        public NativeContainer NativeContainer {
            set { this.nativeContainer = value;
                  this.timer = new Timer();
                  this.nativeContainer.LocationChanged += new EventHandler(form_LocationChanged);
                  this.nativeContainer.TopMost = true;
                  this.timer.Enabled = true;
                  this.timer.Interval = 300;
                  this.timer.Tick += new EventHandler(timer_Tick);
            }
        }

        private void start() {
            this.timer.Start();
        }


        private void timer_Tick(object sender, EventArgs e)
        {
            if (nativeContainer.Bounds.Contains(Cursor.Position))
            {
                switch (this.stopAnchor)
                {
                    case AnchorStyles.Top:
                        showToNormal();
                        break;
                }
            }
            else
            {
                switch (this.stopAnchor)
                {
                    case AnchorStyles.Top:
                        this.hideToTop();
                        break;
                    default:
                        break;
                }
            }
        }

        public void hideToTop() {
            nativeContainer.Location = new Point(nativeContainer.Location.X, (nativeContainer.Height - 4) * (-1));
            nativeContainer.ShowInTaskbar = false;
        }

        public void showToNormal() {
            nativeContainer.Location = new Point(nativeContainer.Location.X, 0);
            nativeContainer.ShowInTaskbar = true;
        }

        public void stop() {
            this.timer.Stop();
        }

        private void mStopAnchor()
        {
            if (nativeContainer.Top <= 0 && nativeContainer.Left <= 0)
            {
                stopAnchor = AnchorStyles.None;
            }
            else if (nativeContainer.Top <= 0)
            {
                stopAnchor = AnchorStyles.Top;
            }
            else
            {
                stopAnchor = AnchorStyles.None;
            }
        }

        private void form_LocationChanged(object sender, EventArgs e)
        {
            this.mStopAnchor();
            start();
        }

        public AnchorStyles StopAnchor {
            get { return this.stopAnchor; }
            set { this.stopAnchor = value; }
        }

    }
}
