using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using AForge.Imaging;
using System.Drawing;

namespace DevelopmentToolkit
{
    public class ImageCompare
    {
        public ImageCompare()
        {
            Bitmap sourceImage = (Bitmap)Bitmap.FromFile("images\\login.png");
            Bitmap template = (Bitmap)Bitmap.FromFile("images\\sarah.png");
            ExhaustiveTemplateMatching tm = new ExhaustiveTemplateMatching(0.921f);
            TemplateMatch[] matchings = tm.ProcessImage(sourceImage, template);
        }
    }
}
