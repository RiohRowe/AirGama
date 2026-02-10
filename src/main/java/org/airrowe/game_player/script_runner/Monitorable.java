package org.airrowe.game_player.script_runner;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.airrowe.game_player.image_grabbing.BasicScreenGrabber;
import org.airrowe.game_player.image_grabbing.ImgManager;
import org.airrowe.game_player.image_processing.DirectImgLocate;
import org.airrowe.game_player.image_processing.ImgAnalyser;
import org.airrowe.game_player.image_processing.MatchResult;
import org.airrowe.game_player.input_emulation.Mouse;
import org.airrowe.game_player.script_runner.areas.Area;
import org.opencv.core.Mat;

public class Monitorable {
	private static final Double DEFAULT_MATCH_THRESHOLD = 0.9999;
	private BasicScreenGrabber bsg;
	private Area area;
	private List<Viewable> imgRefs;
	private boolean expectMatch;
	private double matchThreshold;
	private Point lastPoint;
	private boolean diagnose;
	
	public Monitorable( Area area, List<Viewable> imgRefs, boolean expectMatch, Double matchThreshold) {
		this.bsg = BasicScreenGrabber.get();
		this.area = area;
		this.imgRefs = imgRefs;
		this.expectMatch = expectMatch;
		this.matchThreshold = matchThreshold==null ? DEFAULT_MATCH_THRESHOLD : matchThreshold;
		this.diagnose = false;
//		this.lastPoint=area.center;
	}
	public Mat getFirstRefImg() {
		return this.imgRefs.get(0).getMat();
	}
	public String getName() {
		return this.imgRefs.get(0).fileName;
	}
	public List<Viewable> getImgRefs(){
		return this.imgRefs;
	}
	public boolean check() {
		return this.check(null,null,null);
	}
	public boolean check(List<Viewable> altViewables, Boolean altPassIfMatch, Double altMatchThreshold) {
		if ( this.imgRefs.size()==0 ) {
			this.lastPoint = this.area.center;
			return true;
		}
		List<Viewable> viewables = altViewables==null ? this.imgRefs : altViewables;
		boolean passIfMatch = altPassIfMatch==null ? this.expectMatch : altPassIfMatch;
		double thresholdForMatch = altMatchThreshold==null ? this.matchThreshold : altMatchThreshold;
		
		MatchResult result = DirectImgLocate.findMonitorTemplateNormCorr(ImgManager.bufferedImageBGRToMatBGR(bsg.imgTarget(this.area.areaConcrete)), this, viewables, thresholdForMatch);		
		boolean pass = passIfMatch ? result.score >= thresholdForMatch : result.score<thresholdForMatch;
		if(result.score > thresholdForMatch) {
			this.lastPoint = new Point(
					this.area.areaConcrete.x+result.locationC.x,
					this.area.areaConcrete.y+result.locationC.y);
		}
		return pass;
	}
	public Point getAreaCenter() {
		return area.center;
	}
	public Point getLastFoundCenter() {
		if( this.lastPoint == null ) {
			return getAreaCenter();
		}
		return this.lastPoint;
	}
	public void traceWithMouse() {
		for(int i=0; i<5; ++i) {
			try {
				Mouse.get().moveMouse(area.areaConcrete.x, area.areaConcrete.y);
				System.out.println("MouseMoveArea"+area.areaConcrete.toString());
				Thread.sleep(1000);
				System.out.println("ActualMousePos"+Mouse.get().getMousePosition().toString());
//				Mouse.get().moveMouse(1138, 431);				
				Thread.sleep(1000);
//				Mouse.get().moveMouse(1138, 431);
				Mouse.get().moveMouse(area.areaConcrete.x+area.areaConcrete.width, area.areaConcrete.y+area.areaConcrete.height);
				Thread.sleep(1000);
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	public void recordArea(long msRecTime, long msSampleInterval) {
		//getName
		String baseFileName = this.imgRefs.size()==0?"PLACEHOLDER_NAME-":this.imgRefs.get(0).fileName;
		List<Mat> distinctImgs = new ArrayList<Mat>();
		System.out.println("num Viewables="+this.imgRefs.size());
		for( Viewable existing : this.imgRefs ) {
			distinctImgs.add(existing.getMat());
		}
		if( this.imgRefs.size()==1 && baseFileName.charAt(baseFileName.length()-2)!='-') {
			baseFileName = baseFileName+'-';
		}
		
		long startTimeMs = System.currentTimeMillis();
		long lastIntervalStart = startTimeMs;
		while( System.currentTimeMillis()-startTimeMs <= msRecTime) {
			lastIntervalStart = System.currentTimeMillis();
			Mat nextCapture = ImgManager.bufferedImageBGRToMatBGR(this.bsg.imgTarget(this.getTargetArea()));
			boolean match = false;
			for( Mat prevCapture : distinctImgs ) {
				if( DirectImgLocate.compareMatNormCorr(prevCapture, nextCapture, 1.0) ) {
					match = true;
					break;
				}
			}
			if( !match ) {
				distinctImgs.add(nextCapture);
			}
			long elapsedTime = System.currentTimeMillis()-lastIntervalStart;
			if( elapsedTime < msSampleInterval) {
				try {
					Thread.sleep(msSampleInterval-elapsedTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		//save imgs.
		ImgAnalyser.saveAsPNG(ImgAnalyser.findIdenticalPixlesInMatStackAsAlphaMat(distinctImgs), baseFileName);
//		for(int i=this.imgRefs.size(); i<distinctImgs.size(); ++i) {
//			ImgManager.saveMatImgDiag(distinctImgs.get(i), baseFileName+(i+1));
//		}
	}
	public void startDiagnostic() {
		this.diagnose = true;
	}
	public void stopDiagnostic() {
		this.diagnose = false;
	}
	public boolean diagnose() {
		return this.diagnose;
	}
	public double getMatchThreshold() {
		return this.matchThreshold;
	}
	public boolean isMatchExpecte() {
		return this.expectMatch;
	}
	public Rectangle getTargetArea() {
		return this.area.areaConcrete;
	}
}
