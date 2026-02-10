package org.airrowe.game_player.image_processing.text_parsing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.airrowe.game_player.image_grabbing.ImgManager;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

public class TextReader {
	private static final ITesseract tesseract = new Tesseract();
	private static final Net eastNet = Dnn.readNetFromTensorflow("src/main/resources/tesseract/frozen_east_text_detection.pb");
	
	static {
		tesseract.setDatapath("src/main/resources/tesseract"); // parent folder of tessdata
		tesseract.setLanguage("eng");
		tesseract.setVariable("user_defined_dpi", "300");
	}
	
	public static List<DetectedText> extractTextData(
	        Mat source
	) {
		List<Rect> regions = detectTextRegions(source);
		ImgManager.saveTextRegionOverlay(source, regions, "textFindDiag.bmp");
	    List<DetectedText> results = new ArrayList<>();
	    
	    int numRegions = regions.size();
	    for (Rect r : regions) {

	        Mat roi = new Mat(source, r);

	        // 1️⃣ Center
	        Point center = new Point(
	            r.x + r.width / 2.0,
	            r.y + r.height / 2.0
	        );

	        // 2️⃣ Color
	        Scalar color = estimateTextColor(roi);

	        // 3️⃣ OCR
	        String text = runOCR(roi);

	        results.add(new DetectedText(center, color, text));
	    }

	    return results;
	}
	
	private static Scalar estimateTextColor(Mat roi) {

	    Mat gray = new Mat();
	    Imgproc.cvtColor(roi, gray, Imgproc.COLOR_BGR2GRAY);

	    Mat mask = new Mat();
	    Imgproc.threshold(
	        gray,
	        mask,
	        0,
	        255,
	        Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU
	    );

	    Scalar meanColor = Core.mean(roi, mask);
	    return meanColor; // BGR
	}
	//Uses OCR
	private static String runOCR(Mat roi) {
	    try {
	        BufferedImage img = ImgManager.matBgrToBufferedImageBgr(roi);
	        return tesseract.doOCR(img).trim();
	    } catch (Exception e) {
	        return "";
	    }
	}

	public static List<Rect> detectTextRegions(Mat image) {
		
		//Manipulate Mat Image to enhance text detection
		Mat gray = new Mat();
		Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

		Mat enhanced = new Mat();
		Imgproc.createCLAHE(2.0, new Size(8,8)).apply(gray, enhanced);

		Imgproc.cvtColor(enhanced, image, Imgproc.COLOR_GRAY2BGR);
		//Convert Mat and do detection
		double ratio = 1.0;
	    int width = ((int)((image.cols()*ratio)/32))*32;
	    int height = ((int)((image.rows()*ratio)/32))*32;
	    // Create Neural Network format, shrinking image
	    Mat blob = Dnn.blobFromImage(
	        image,
	        1.0,//Scale factor
	        new Size(width, height),//scale image to size
	        new Scalar(123.68, 116.78, 103.94),//Alter pixel color values
	        true,//bgr -> rgb
	        false//crop
	    );
	    ImgManager.saveBlobAsBmp(blob, width,height, "blobDiag.bmp");

	    eastNet.setInput(blob);

	    List<Mat> outputs = new ArrayList<>();
	    List<String> outNames = List.of(
	        "feature_fusion/Conv_7/Sigmoid",
	        "feature_fusion/concat_3"
	    );

	    eastNet.forward(outputs, outNames);

	    Mat scores = outputs.get(0);
	    Mat geometry = outputs.get(1);

	    List<Rect> boxes = new ArrayList<Rect>();
	    List<Float> confidences = new ArrayList<Float>();
	    decodeBoundingBoxesNoRotation(scores, geometry, 0.8f, boxes, confidences);
	    return boxes;
	}
	
	public static void decodeBoundingBoxesNoRotation(
	        Mat scores,
	        Mat geometry,
	        float scoreThresh,
	        List<Rect> boxes,
	        List<Float> confidences
	) {
	    int height = scores.size(2);
	    int width  = scores.size(3);

	    for (int y = 0; y < height; y++) {
	        for (int x = 0; x < width; x++) {

	            float score = (float) scores.get(new int[] {0, 0, y, x})[0];
	            if (score < scoreThresh) continue;

	            float top    = (float) geometry.get(new int[] {0, 0, y, x})[0];
	            float right  = (float) geometry.get(new int[] {0, 1, y, x})[0];
	            float bottom = (float) geometry.get(new int[] {0, 2, y, x})[0];
	            float left   = (float) geometry.get(new int[] {0, 3, y, x})[0];

	            int offsetX = x * 4;
	            int offsetY = y * 4;

	            int x0 = (int) (offsetX - left);
	            int y0 = (int) (offsetY - top);
	            int x1 = (int) (offsetX + right);
	            int y1 = (int) (offsetY + bottom);

	            Rect rect = new Rect(
	                    new Point(x0, y0),
	                    new Point(x1, y1)
	            );

	            boxes.add(rect);
	            confidences.add(score);
	        }
	    }
	}

}
