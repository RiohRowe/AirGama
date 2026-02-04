package org.airrowe.game_player.image_processing;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.airrowe.game_player.diag.DiagOption;
import org.airrowe.game_player.diag.DiagnosticsManager;
import org.airrowe.game_player.image_grabbing.ImgManager;
import org.airrowe.game_player.script_runner.Viewable;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;import nu.pattern.OpenCV;

public class DirectImgLocate {
	private static int diagOrderIdx=1;
	static {
	    OpenCV.loadLocally();
	}
	public static MatchResult findTemplateNormCoeff(Mat source, List<Viewable> lookFors, double matchThreshold, boolean expectToMatch) {
		MatchResult matchResult = null;
		Mat template = null;
		Mat result = null;
		for( Viewable lookFor : lookFors) {
			template = lookFor.getMat();
			//Get height and width of test space (grid of possible check locations)
		    int resultCols = source.cols() - template.cols() + 1;
		    int resultRows = source.rows() - template.rows() + 1;
		    //Create blank Mat grid of Float(CV_32FC1) values representing every test location
		    result = new Mat(resultRows, resultCols, CvType.CV_32FC1);
		    //Perform NORMALIZATION_OF_COEFFICIENTS operation on every overlap of source and search image. 
		    //Generate closeness (1=perfect match, 0=not a match) values and populate result map
		    Imgproc.matchTemplate(
		        source,
		        template,
		        result,
		        Imgproc.TM_CCOEFF_NORMED
		    );
		    
		    Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
		    //If match found, finish
		    if( matchResult == null || matchResult.score < mmr.maxVal) {
		    	matchResult = new MatchResult(mmr.maxLoc, mmr.maxVal);
		    	if( matchResult.score >= matchThreshold) {
		    		break;
		    	}
		    }
	//	    System.out.println("Result Sizespace=Height:"+result.rows()+" Width:"+result.cols());
		}
		if( !(expectToMatch ^ matchResult.score<matchThreshold) ) {
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
		    		ImgManager.saveMatImgDiag(source, dm.numDiags+"-SOURCE");
		    	}
		    	dm.numDiags--;
		    }
    	} else {
    		return matchResult;
    	}
		return matchResult;
	}
}
