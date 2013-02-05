//----------------------------------------------------------------------------
//  Copyright (C) 2004-2012 by EMGU. All rights reserved.       
//----------------------------------------------------------------------------

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Drawing;
using System.Text;
using Emgu.CV;
using Emgu.CV.Features2D;
using Emgu.CV.Structure;
using Emgu.Util;

namespace GriffinsPortalKit
{
    public class ImageDetector : DisposableObject
    {
        private Features2DTracker<float> _tracker;
        private SURFDetector _detector;
        private MemStorage _octagonStorage;
        private Contour<Point> _octagon;

        public ImageDetector(Image<Gray, Byte> imgModel)
        {
            _detector = new SURFDetector(500, false);
            ImageFeature<float>[] features = _detector.DetectFeatures(imgModel, null);
            if (features.Length == 0)
                throw new Exception("No image feature has been found in the image model");
            _tracker = new Features2DTracker<float>(features);

            _octagonStorage = new MemStorage();
            _octagon = new Contour<Point>(_octagonStorage);
            _octagon.PushMulti(new Point[] { 
            new Point(1, 0),
            new Point(2, 0),
            new Point(3, 1),
            new Point(3, 2),
            new Point(2, 3),
            new Point(1, 3),
            new Point(0, 2),
            new Point(0, 1)},
               Emgu.CV.CvEnum.BACK_OR_FRONT.FRONT);
        }

        private void FindImage(Image<Gray, byte> img, List<Image<Gray, Byte>> imgList, List<Rectangle> boxList, Contour<Point> contours)
        {
            for (; contours != null; contours = contours.HNext)
            {
                contours.ApproxPoly(contours.Perimeter * 0.02, 0, contours.Storage);
                if (contours.Area > 200)
                {
                    double ratio = CvInvoke.cvMatchShapes(_octagon, contours, Emgu.CV.CvEnum.CONTOURS_MATCH_TYPE.CV_CONTOURS_MATCH_I3, 0);

                    if (ratio > 0.1) //not a good match of contour shape
                    {
                        Contour<Point> child = contours.VNext;
                        if (child != null)
                            FindImage(img, imgList, boxList, child);
                        continue;
                    }

                    Rectangle box = contours.BoundingRectangle;

                    Image<Gray, Byte> candidate;
                    using (Image<Gray, Byte> tmp = img.Copy(box))
                        candidate = tmp.Convert<Gray, byte>();

                    //set the value of pixels not in the contour region to zero
                    using (Image<Gray, Byte> mask = new Image<Gray, byte>(box.Size))
                    {
                        mask.Draw(contours, new Gray(255), new Gray(255), 0, -1, new Point(-box.X, -box.Y));

                        double mean = CvInvoke.cvAvg(candidate, mask).v0;
                        candidate._ThresholdBinary(new Gray(mean), new Gray(255.0));
                        candidate._Not();
                        mask._Not();
                        candidate.SetValue(0, mask);
                    }

                    ImageFeature<float>[] features = _detector.DetectFeatures(candidate, null);

                    int minMatchCount = 10;

                    if (features != null && features.Length >= minMatchCount)
                    {
                        Features2DTracker<float>.MatchedImageFeature[] matchedFeatures = _tracker.MatchFeature(features, 2);

                        int goodMatchCount = 0;
                        foreach (Features2DTracker<float>.MatchedImageFeature ms in matchedFeatures)
                            if (ms.SimilarFeatures[0].Distance < 0.5) goodMatchCount++;

                        if (goodMatchCount >= minMatchCount)
                        {
                            boxList.Add(box);
                            imgList.Add(candidate);
                        }
                    }
                }
            }
        }

        public void DetectImage(Image<Gray, byte> img, List<Image<Gray, Byte>> imgList, List<Rectangle> boxList)
        {
            Image<Gray, Byte> smoothImg = img.SmoothGaussian(5, 5, 1.5, 1.5);

            //Use Dilate followed by Erode to eliminate small gaps in some countour.
            smoothImg._Dilate(1);
            smoothImg._Erode(1);

            using (Image<Gray, Byte> canny = smoothImg.Canny(100, 50))
            using (MemStorage stor = new MemStorage())
            {
                Contour<Point> contours = canny.FindContours(
                   Emgu.CV.CvEnum.CHAIN_APPROX_METHOD.CV_CHAIN_APPROX_SIMPLE,
                   Emgu.CV.CvEnum.RETR_TYPE.CV_RETR_TREE,
                   stor);
                FindImage(img, imgList, boxList, contours);
            }
        }

        protected override void DisposeObject()
        {
            _tracker.Dispose();
            _octagonStorage.Dispose();
        }

    }
}
