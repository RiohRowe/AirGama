package org.airrowe.game_player.script_runner;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import org.airrowe.game_player.image_grabbing.BasicScreenGrabber;
import org.airrowe.game_player.image_grabbing.ImgManager;
import org.airrowe.game_player.image_processing.DirectImgLocate;
import org.airrowe.game_player.image_processing.MatchResult;
import org.airrowe.game_player.input_emulation.Mouse;
import org.airrowe.game_player.script_runner.areas.Area;
import org.opencv.core.Mat;

public class Monitorable {
	private static final Double DEFAULT_MATCH_THRESHOLD = 0.85;
	private BasicScreenGrabber bsg;
	private Area area;
	private List<Viewable> imgRefs;
	private boolean expectMatch;
	private double matchThreshold;
	private Point lastPoint;
	
	public Monitorable( Area area, List<Viewable> imgRefs, boolean expectMatch, Double matchThreshold) {
		this.bsg = BasicScreenGrabber.get();
		this.area = area;
		this.imgRefs = imgRefs;
		this.expectMatch = expectMatch;
		this.matchThreshold = matchThreshold==null ? DEFAULT_MATCH_THRESHOLD : matchThreshold;
		this.lastPoint = this.area.center;
	}
	public Mat getFirstRefImg() {
		return this.imgRefs.get(0).getMat();
	}
	public String getName() {
		return this.imgRefs.get(0).fileName;
	}
	public boolean check() {
//		this.traceWithMouse();
		MatchResult result = DirectImgLocate.findTemplateNormCoeff(ImgManager.convertToMat(bsg.imgTarget(this.area.areaConcrete)), this.imgRefs, this.matchThreshold, this.expectMatch);
		boolean pass = this.expectMatch ? result.score >= this.matchThreshold : result.score<this.matchThreshold;
		if(pass) {
			this.lastPoint = new Point((int)result.location.x,(int)result.location.y);
//			System.out.println("Passed with score = "+result.score);
		}
		return pass;
	}
	public boolean check(List<Viewable> altViewables, boolean trueIfThere, Double matchThreshold) {
		double mt = matchThreshold==null ? this.matchThreshold : matchThreshold;
		MatchResult result = DirectImgLocate.findTemplateNormCoeff(ImgManager.convertToMat(bsg.imgTarget(this.area.areaConcrete)), altViewables, this.matchThreshold, this.expectMatch);		boolean pass = this.expectMatch ? result.score >= this.matchThreshold : result.score<this.matchThreshold;
		if(pass) {
			this.lastPoint = new Point((int)result.location.x,(int)result.location.y);
//			System.out.println("Passed with score = "+result.score);
		}
		return pass;
	}
	public Point getAreaCenter() {
		return area.center;
	}
	public Point getLastFoundCenter() {
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
	public Rectangle getTargetArea() {
		return this.area.areaConcrete;
	}
}
