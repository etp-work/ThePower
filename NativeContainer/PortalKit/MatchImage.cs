using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Emgu.CV;
using Emgu.CV.Structure;
using Emgu.CV.UI;

namespace GriffinsPortalKit
{
    public partial class MatchImage : Form
    {
        private ImageDetector _imageDetector;

        public MatchImage()
        {
            InitializeComponent();
            using (Image<Gray, Byte> modelImage = new Image<Gray, Byte>("images\\sarah.png"))
            using (Image<Gray, Byte> observedImage = new Image<Gray, Byte>("images\\login.png"))
            {
                _imageDetector = new ImageDetector(modelImage);
                ProcessImage(observedImage);
            }
        }

        private void ProcessImage(Image<Gray, byte> img)
        {
            List<Image<Gray, Byte>> imgList = new List<Image<Gray, byte>>();
            List<Rectangle> imgBoxList = new List<Rectangle>();
            _imageDetector.DetectImage(img, imgList, imgBoxList);

            panel1.Controls.Clear();
            Point startPoint = new Point(10, 10);

            for (int i = 0; i < imgList.Count; i++)
            {
                Rectangle rect = imgBoxList[i];
                AddLabelAndImage(
                   ref startPoint,
                   String.Format("Image [{0},{1}]:", rect.Location.Y + rect.Width / 2, rect.Location.Y + rect.Height / 2),
                   imgList[i]);
                img.Draw(rect, new Gray(10), 2);
            }

            imageBox1.Image = img;
        }

        private void AddLabelAndImage(ref Point startPoint, String labelText, IImage image)
        {
            Label label = new Label();
            panel1.Controls.Add(label);
            label.Text = labelText;
            label.Width = 100;
            label.Height = 30;
            label.Location = startPoint;
            startPoint.Y += label.Height;

            ImageBox box = new ImageBox();
            panel1.Controls.Add(box);
            box.ClientSize = image.Size;
            box.Image = image;
            box.Location = startPoint;
            startPoint.Y += box.Height + 10;
        }
    }
}
