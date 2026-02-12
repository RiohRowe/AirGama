package org.airrowe.game_player.image_grabbing;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.airrowe.game_player.diag.DiagOption;
import org.airrowe.game_player.diag.DiagnosticsManager;
import org.airrowe.game_player.file_management.ResourceFolder;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.dnn.Dnn;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import nu.pattern.OpenCV;

public class ImgManager {
	static {
	    OpenCV.loadLocally();
	}
	private static ImgManager instance;
//	public enum ImgCategory {
//		GAME_WINDOW("GameWindowRefImgs/");
//		public String url;
//		
//		ImgCategory(String url){
//			this.url = url;
//		}
//	}
	private ImgManager() {
		
	}
	public static ImgManager getInst() {
		if( instance == null) {
			instance = new ImgManager();
		}
		return instance;
	}
	
	
	
//	public File getResource(ImgCategory category, String imgName) {
//		String url = category==null?"":category.url;
//		try {
//			return new File(getClass().getClassLoader().getResource(url+imgName).toURI());
//		} catch (URISyntaxException e) {
//			System.out.println("Failed to read file: "+url+imgName+".");
//			e.printStackTrace();
//			return null;
//		}
//	}
	
	public static Mat bufferedImageBGRToMatBGR(BufferedImage bi) {
	    if (bi.getType() != BufferedImage.TYPE_3BYTE_BGR) {
	        BufferedImage converted =
	            new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
	        converted.getGraphics().drawImage(bi, 0, 0, null);
	        bi = converted;
	    }

	    byte[] pixels = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
	    Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
	    mat.put(0, 0, pixels);
	    return mat;
	}
	
	public static Mat bufferedImageABGRToMatBGRA(BufferedImage bi) {  
		if (bi.getType() != BufferedImage.TYPE_4BYTE_ABGR) {
			BufferedImage converted = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            converted.getGraphics().drawImage(bi, 0, 0, null);
            bi = converted;
        }

        byte[] abgr = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        byte[] bgra = new byte[abgr.length];

        // Convert ABGR → BGRA
        for (int i = 0; i < abgr.length; i += 4) {
        	bgra[i + 3] = abgr[i];
            bgra[i] = abgr[i + 1];
            bgra[i + 1] = abgr[i + 2];
            bgra[i + 2] = abgr[i + 3];
        }

        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC4);
        mat.put(0, 0, bgra);
        return mat;
	}
	
	public static BufferedImage matBgrToBufferedImageBgr(Mat mat) {
	    int type = BufferedImage.TYPE_3BYTE_BGR;
	    byte[] data = new byte[mat.rows() * mat.cols() * (int) mat.elemSize()];
	    mat.get(0, 0, data);

	    BufferedImage image = new BufferedImage(
	        mat.cols(),
	        mat.rows(),
	        type
	    );
	    image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
	    return image;
	}

	
//	public static BufferedImage rgbaMatToBufferedImage(Mat mat) {
//	    int width = mat.cols();
//	    int height = mat.rows();
//
//	    byte[] bgra = new byte[width * height * 4];
//	    mat.get(0, 0, bgra);
//
//	    byte[] abgr = new byte[bgra.length];
//
//	    for (int i = 0; i < bgra.length; i += 4) {
//	    	abgr[i + 1] = bgra[i];
//	    	abgr[i + 2] = bgra[i + 1];
//	        abgr[i + 3] = bgra[i + 2];
//	        abgr[i] = bgra[i + 3];
//	    }
//
//	    BufferedImage image = new BufferedImage(
//	        width,
//	        height,
//	        BufferedImage.TYPE_4BYTE_ABGR
//	    );
//
//	    image.getRaster().setDataElements(0, 0, width, height, abgr);
//	    return image;
//	}
	
	public static Mat extractAlphaMask(Mat mat) {
		    List<Mat> channels = new ArrayList<>(4);
		    Core.split(mat, channels);

		   	return channels.get(3);
	}
	
	
	public static void saveMatToFile(
	        Mat source,
	        ResourceFolder resourceFolder,
	        String fileName
	) {
	    String directoryPath = resourceFolder == null ? ResourceFolder.WUPSIES.path : resourceFolder.path;
	    saveMatToFile(source,directoryPath+fileName);
	}
	public static void saveMatToFile(
	        Mat source,
	        String fileFullPath
	) {
	    Imgcodecs.imwrite(
	        fileFullPath,
	        source
	    );
	}
	
	public static void saveMatchHeatmap(Mat matchResult, String fileName) {

	    // 1. Normalize result to [0, 255]
	    Mat normalized = new Mat();
	    Core.normalize(
	        matchResult,
	        normalized,
	        0,
	        255,
	        Core.NORM_MINMAX,
	        CvType.CV_8UC1
	    );

	    // 2. Apply color map (JET: blue→green→red)
	    Mat heatmap = new Mat();
	    Imgproc.applyColorMap(normalized, heatmap, Imgproc.COLORMAP_JET);

	    // 3. Flip colors so HIGH = green, LOW = red (optional tweak)
	    // JET maps high to red by default; invert if desired
	    Core.bitwise_not(heatmap, heatmap);

	    // 4. Save image
	    Path outputPath = Paths.get("src/main/resources", fileName);
	    Imgcodecs.imwrite(outputPath.toString(), heatmap);

	    System.out.println("Saved heatmap to: " + outputPath.toAbsolutePath());
	}
	public static void saveBlobAsBmp(//Text Recognition stuff
            Mat blob,
            int width,
            int height,
            String outputName
    ) {
        // Step 1: Remove batch dimension
        // blob: [1, 3, H, W] → [3, H, W]
    	List<Mat> images = new ArrayList<>();
    	Dnn.imagesFromBlob(blob, images);
    	Mat image = images.get(0);
//        Mat reshaped = blob.reshape(1, new int[]{3, height, width});
//
//        // Step 2: Split into channels
//        List<Mat> channels = new ArrayList<>(3);
//        for (int c = 0; c < 3; c++) {
//            Mat channel = reshaped.row(c).reshape(1, height);
//            channels.add(channel);
//        }
//
//        // Step 3: Merge channels into HxWx3
//        Mat image = new Mat();
//        Core.merge(channels, image);

        // Step 4: Undo mean subtraction (must match blobFromImage)
        Core.add(
            image,
            new Scalar(123.68, 116.78, 103.94),
            image
        );

        // Step 5: Convert to 8-bit image
        Mat output8u = new Mat();
        image.convertTo(output8u, CvType.CV_8UC3);

        // Step 6: Save
        String path = "src/main/resources/" + outputName;
        Imgcodecs.imwrite(path, output8u);

        // Cleanup
        image.release();
        output8u.release();
    }
	 public static void saveTextRegionOverlay(
	            Mat source,
	            List<Rect> textRects,
	            String outputName) {
        // Clone so we don't modify original image
        Mat overlay = source.clone();

        Scalar green = new Scalar(0, 255, 0); // BGR
        int thickness = 2;

        for (Rect rect : textRects) {
            Imgproc.rectangle(
                overlay,
                rect.tl(),   // top-left
                rect.br(),   // bottom-right
                green,
                thickness
            );
        }

        String outputPath = "src/main/resources/" + outputName;
        Imgcodecs.imwrite(outputPath, overlay);

        overlay.release();
    }
	 
	    
	
	public static void dumpMatchDiagnostics(
	        Mat source,
	        Mat reference,
	        Mat result,
	        Point expectedMatch
	) {
		DiagnosticsManager dm = DiagnosticsManager.get();
	    int refW = reference.cols();
	    int refH = reference.rows();
	    String wupsiesPath = ResourceFolder.WUPSIES.path;
	    Path baseDir = Paths.get(wupsiesPath.substring(0, wupsiesPath.length()-1));

	    // 1️⃣ Save reference image
	    if( DiagOption.REFERENCE_IMG.doDiag(dm.diagTypeFlags)) {
		    Imgcodecs.imwrite(
		        baseDir.resolve(dm.numDiags+"-REFERENCE.bmp").toString(),
		        reference
		    );
	    }
	    // 2️⃣ Collect all match scores with coordinates
	    List<MatchPoint> points = new ArrayList<>();

	    for (int y = 0; y < result.rows(); y++) {
	        for (int x = 0; x < result.cols(); x++) {
	            double score = result.get(y, x)[0];
	            points.add(new MatchPoint(x, y, score));
	        }
	    }

	    // 3️⃣ Sort by score (descending = best first)
	    points.sort(Comparator.comparingDouble(MatchPoint::score).reversed());

	    // 🔴 If using TM_SQDIFF / TM_SQDIFF_NORMED, use this instead:
	    // points.sort(Comparator.comparingDouble(MatchPoint::score));

	    if( DiagOption.BEST3MATCH.doDiag(dm.diagTypeFlags)) {
		    // 4️⃣ Save best 3
	    	int numSamples = points.size()<3 ? points.size() : 3;
		    saveSamples(dm.numDiags+"-BEST", points.subList(0, numSamples), source, refW, refH, baseDir);
	    }
	    if( DiagOption.WORST3MATCH.doDiag(dm.diagTypeFlags)) {
	    // 5️⃣ Save worst 3
	    	int numSamples = points.size()<3 ? points.size() : 3;
		    saveSamples(
		    		dm.numDiags+"WORST",
		        points.subList(points.size() - numSamples, points.size()),
		        source,
		        refW,
		        refH,
		        baseDir
		    );
	    }
	    
	    if( DiagOption.EXPECTED_MATCH_LOC.doDiag(dm.diagTypeFlags)) {
		    // 6️⃣ Save expected match
		    double expectedScore = result.get(
		        (int) expectedMatch.y,
		        (int) expectedMatch.x
		    )[0];
	
		    saveCrop(
		        source,
		        refW,
		        refH,
		        (int) expectedMatch.x,
		        (int) expectedMatch.y,
		        baseDir.resolve(
		            String.format(
		            	 dm.numDiags+"-EXPECTED_x%d_y%d_score%.5f.bmp",
		                (int) expectedMatch.x,
		                (int) expectedMatch.y,
		                expectedScore
		            )
		        ).toString()
		    );
	    }
	}

	
	private static class MatchPoint {
	    int x, y;
	    double score;

	    MatchPoint(int x, int y, double score) {
	        this.x = x;
	        this.y = y;
	        this.score = score;
	    }

	    double score() {
	        return score;
	    }
	}

	private static void saveSamples(
	        String prefix,
	        List<MatchPoint> samples,
	        Mat source,
	        int refW,
	        int refH,
	        Path baseDir
	) {
	    int index = 1;
	    for (MatchPoint mp : samples) {
	        saveCrop(
	            source,
	            refW,
	            refH,
	            mp.x,
	            mp.y,
	            baseDir.resolve(
	                String.format(
	                    "%s_%d_x%d_y%d_score%.5f.bmp",
	                    prefix,
	                    index++,
	                    mp.x,
	                    mp.y,
	                    mp.score
	                )
	            ).toString()
	        );
	    }
	}

	private static void saveCrop(
	        Mat source,
	        int width,
	        int height,
	        int x,
	        int y,
	        String outputPath
	) {
	    // Bounds check (VERY important)
	    if (x + width > source.cols() || y + height > source.rows()) {
	        return;
	    }

	    Rect roi = new Rect(x, y, width, height);
	    Mat cropped = new Mat(source, roi);
	    Imgcodecs.imwrite(outputPath, cropped);
	}
	
}
