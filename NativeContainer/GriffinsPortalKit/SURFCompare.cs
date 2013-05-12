using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using OpenSURFcs;

namespace DevelopmentToolkit
{
    public partial class SURFCompare : Form
    {
        public SURFCompare()
        {
            InitializeComponent();
            
            Bitmap templateImg = (Bitmap)Bitmap.FromFile("images\\sarah.png");
            pictureBox1.Image = templateImg;
            IntegralImage iTemplateImg = IntegralImage.FromImage(templateImg);
            List<IPoint> templateImgInterestPoints = FastHessian.getIpoints(0.0002f, 5, 2, iTemplateImg);
            SurfDescriptor.DecribeInterestPoints(templateImgInterestPoints, false, false, iTemplateImg);
            PaintSURF(templateImg, templateImgInterestPoints);

            Bitmap sourceImg = (Bitmap)Bitmap.FromFile("images\\login.png");
            pictureBox2.Image = sourceImg;
            IntegralImage iSourceImg = IntegralImage.FromImage(sourceImg);
            List<IPoint> sourceImgInterestPoints = FastHessian.getIpoints(0.0002f, 5, 2, iSourceImg);
            SurfDescriptor.DecribeInterestPoints(sourceImgInterestPoints, false, false, iSourceImg);
            PaintSURF(sourceImg, sourceImgInterestPoints);
        }

        private void PaintSURF(Bitmap img, List<IPoint> interestPoints)
        {
            Graphics g = Graphics.FromImage(img);

            Pen redPen = new Pen(Color.Red);
            Pen bluePen = new Pen(Color.Blue);
            Pen myPen;

            foreach (IPoint interestPoint in interestPoints)
            {
                int S = 2 * Convert.ToInt32(2.5f * interestPoint.scale);
                int R = Convert.ToInt32(S / 2f);

                Point pt = new Point(Convert.ToInt32(interestPoint.x), Convert.ToInt32(interestPoint.y));
                Point ptR = new Point(Convert.ToInt32(R * Math.Cos(interestPoint.orientation)),
                             Convert.ToInt32(R * Math.Sin(interestPoint.orientation)));

                myPen = (interestPoint.laplacian > 0 ? bluePen : redPen);

                g.DrawEllipse(myPen, pt.X - R, pt.Y - R, S, S);
                g.DrawLine(new Pen(Color.FromArgb(0, 255, 0)), new Point(pt.X, pt.Y), new Point(pt.X + ptR.X, pt.Y + ptR.Y));
            }
        }
    }
}
