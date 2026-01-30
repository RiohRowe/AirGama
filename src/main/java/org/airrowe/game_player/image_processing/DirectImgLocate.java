package org.airrowe.game_player.image_processing;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.airrowe.game_player.image_grabbing.ImgManager;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;import nu.pattern.OpenCV;

public class DirectImgLocate {
	static {
	    OpenCV.loadLocally();
	}
	public static MatchResult findTemplateNormCoeff(Mat source, Mat template, boolean diag) {
		//Get height and width of test space (grid of possible check locations)
	    int resultCols = source.cols() - template.cols() + 1;
	    int resultRows = source.rows() - template.rows() + 1;
	    //Create blank Mat grid of Float(CV_32FC1) values representing every test location
	    Mat result = new Mat(resultRows, resultCols, CvType.CV_32FC1);
	    //Perform NORMALIZATION_OF_COEFFICIENTS operation on every overlap of source and search image. 
	    //Generate closeness (1=perfect match, 0=not a match) values and populate result map
	    Imgproc.matchTemplate(
	        source,
	        template,
	        result,
	        Imgproc.TM_CCOEFF_NORMED
	    );
	    if(diag) {
	    	ImgManager.dumpMatchDiagnostics(source, template, result, new Point(1324, 726));
			ImgManager.saveMatchHeatmap(result, template.width()+"diagRes.bmp");
	    }
	    System.out.println("Result Sizespace=Height:"+result.rows()+" Width:"+result.cols());

	    Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

	    return new MatchResult(mmr.maxLoc, mmr.maxVal);
	}
}
