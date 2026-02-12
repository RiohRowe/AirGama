package org.airrowe.game_player.image_processing;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.airrowe.game_player.diag.DiagOption;
import org.airrowe.game_player.diag.DiagnosticsManager;
import org.airrowe.game_player.image_grabbing.ImgManager;
import org.airrowe.game_player.script_runner.Monitorable;
import org.airrowe.game_player.script_runner.viewables.Viewable;
import org.airrowe.game_player.script_runner.viewables.ViewableGroup;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;import nu.pattern.OpenCV;

public class DirectImgLocate {
	static {
	    OpenCV.loadLocally();
	}
	public static MatchResult findMonitorTemplateNormCorr(Mat source, Monitorable monitor) {
		return findMonitorTemplateNormCorr(source, monitor, null, null);
	}
	public static MatchResult findMonitorTemplateNormCorr(Mat source, Monitorable monitor, ViewableGroup lookFors, Double matchThreshold) {
		MatchResult matchResult = null;
		Mat template = null;
		Mat result = null;
        Mat mask = null;
        Mat rawTemplate;
        Viewable match = null;
		for( Viewable lookFor : lookFors.getViewables()) {

	        rawTemplate = lookFor.getMat();

	        // Handle alpha templates
	        if (rawTemplate.channels() == 4) {
	            mask = ImgManager.extractAlphaMask(rawTemplate);
	            template = new Mat();
	            Imgproc.cvtColor(rawTemplate, template, Imgproc.COLOR_BGRA2BGR);
	        } else {
	            template = rawTemplate;
	        }
	        //Now we have template and possible mask layer

	        int resultCols = source.cols() - template.cols() + 1;
	        int resultRows = source.rows() - template.rows() + 1;

	        result = new Mat(resultRows, resultCols, CvType.CV_32FC1);

	        if( mask == null ) {
		        Imgproc.matchTemplate(
		                source,
		                template,
		                result,
		                Imgproc.TM_CCORR_NORMED
		        );
	        } else {
				Imgproc.matchTemplate(
				    source,
				    template,
				    result,
				    Imgproc.TM_CCORR_NORMED,
				    mask   // ← this is what matters
				);
	        }

	        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

	        if (matchResult == null || matchResult.score < mmr.maxVal) {
	            matchResult = new MatchResult(mmr.maxLoc, template, mmr.maxVal);
	            if (matchResult.score >= matchThreshold) {
	            	match = lookFor;
	                break;
	            }
	        }
	//	    System.out.println("Result Sizespace=Height:"+result.rows()+" Width:"+result.cols());
		}
		if( monitor.diagnose()/*!(expectToMatch ^ matchResult.score<matchThreshold)*/ ) {// If Not expected result;
			DiagnosticsManager dm = DiagnosticsManager.get();
		    if( DiagnosticsManager.get().diagnose && dm.numDiags > 0 ) {
		    	System.out.println("DUMPING DIAG-"+dm.numDiags);
		    	if((DiagOption.BEST3MATCH.bitRep | 
	    			DiagOption.WORST3MATCH.bitRep | 
	    			DiagOption.EXPECTED_MATCH_LOC.bitRep | 
	    			DiagOption.REFERENCE_IMG.bitRep & 
	    			dm.diagTypeFlags)>0) {
		    		ImgManager.dumpMatchDiagnostics(source, template, result, new Point(10, 10));
		    	}
		    	if(DiagOption.MATCH_SPACE_HEAT_MAP.doDiag(dm.diagTypeFlags)) {
		    		ImgManager.saveMatchHeatmap(result, dm.numDiags+"-h"+template.height()+"-w"+template.width()+"-match-space.bmp");
		    	}
		    	if(DiagOption.SEARCH_AREA.doDiag(dm.diagTypeFlags)) {
		    		Rectangle target = monitor.getTargetArea();
		    		String fileNameOfMatch = match==null?"noTarget":match.getName();
		    		ImgManager.saveMatToFile(source, null, dm.numDiags+fileNameOfMatch+"x-"+target.x+"y-"+target.y+"w-"+target.width+"h-"+target.height+"-SOURCE");
		    	}
		    	dm.numDiags--;
		    }
    	}
		return matchResult;
	}
	
	public static MatchResult findTemplateNormCorr(Mat source, ViewableGroup viewables, double matchThreshold) {
		MatchResult matchResult = null;
		Mat template = null;
		Mat result = null;
		for( Viewable lookFor : viewables.getViewables()) {
			template = lookFor.getMat();
	        //Now we have template and possible mask layer
	        int resultCols = source.cols() - template.cols() + 1;
	        int resultRows = source.rows() - template.rows() + 1;

	        result = new Mat(resultRows, resultCols, CvType.CV_32FC1);

	        Imgproc.matchTemplate(
	                source,
	                template,
	                result,
	                Imgproc.TM_CCORR_NORMED
	        );

	        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

	        if (matchResult == null || matchResult.score < mmr.maxVal) {
	            matchResult = new MatchResult(mmr.maxLoc, template, mmr.maxVal);
	            if (matchResult.score >= matchThreshold) {
	                break;
	            }
	        }
		}
		return matchResult;
	}
	
	public static boolean compareMatNormCorr(Mat source, Mat compare, double matchThreshold) {

		//Get height and width of test space (grid of possible check locations)
	    int resultCols = source.cols() - compare.cols() + 1;
	    int resultRows = source.rows() - compare.rows() + 1;
	    //Create blank Mat grid of Float(CV_32FC1) values representing every test location
	    System.out.println("resultCols="+resultCols+"\tresultRows="+resultRows);
	    Mat result = new Mat(resultRows, resultCols, CvType.CV_32FC1);
	    //Perform NORMALIZATION_OF_COEFFICIENTS operation on every overlap of source and search image. 
	    //Generate closeness (1=perfect match, 0=not a match) values and populate result map
	    Imgproc.matchTemplate(
	        source,
	        compare,
	        result,
	        Imgproc.TM_CCOEFF_NORMED
	    );
	    
	    Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
	    return mmr.maxVal >= matchThreshold;
	}
}
