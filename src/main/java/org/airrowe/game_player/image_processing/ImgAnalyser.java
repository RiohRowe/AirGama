package org.airrowe.game_player.image_processing;

import java.util.List;

import org.airrowe.game_player.file_management.ResourceFolder;
import org.airrowe.game_player.script_runner.viewables.ViewableGroup;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImgAnalyser {

	public static Mat findIdenticalPixlesInMatStackAsAlphaMat(List<Mat> images) {
        if (images == null || images.size() < 2) {
            throw new IllegalArgumentException("At least 2 images required");
        }

        Mat base = images.get(0);

        if (base.channels() != 3) {
            throw new IllegalArgumentException("Base image must be BGR (3 channels)");
        }

        // Convert base image to BGRA
        Mat baseBGRA = new Mat();
        Imgproc.cvtColor(base, baseBGRA, Imgproc.COLOR_BGR2BGRA);

        // Alpha mask (start fully opaque)
        Mat alphaMask = Mat.ones(base.size(), CvType.CV_8UC1);
        alphaMask.setTo(new Scalar(255));

        Mat diff = new Mat();
        Mat grayDiff = new Mat();
        Mat nonZeroMask = new Mat();

        for (int i = 1; i < images.size(); i++) {
            Mat current = images.get(i);

            if (current.size().equals(base.size()) == false) {
                throw new IllegalArgumentException("All images must be same size");
            }

            // Absolute difference
            Core.absdiff(base, current, diff);

            // Convert diff to grayscale
            Imgproc.cvtColor(diff, grayDiff, Imgproc.COLOR_BGR2GRAY);

            // Any non-zero pixel means difference
            Imgproc.threshold(
                    grayDiff,
                    nonZeroMask,
                    0,
                    255,
                    Imgproc.THRESH_BINARY
            );

            // Clear alpha where difference exists
            alphaMask.setTo(new Scalar(0), nonZeroMask);
        }

        // Insert alpha channel into BGRA image
        List<Mat> channels = new java.util.ArrayList<>(4);
        Core.split(baseBGRA, channels);
        channels.set(3, alphaMask); // replace alpha channel
        Core.merge(channels, baseBGRA);

        return baseBGRA;
    }

    public static void saveAsPNG(Mat bgra, ViewableGroup group) {
    	group.addViewable(bgra);
//        Imgcodecs.imwrite(ResourceFolder.WUPSIES.path+fileName+".png", bgra);
    }
}
