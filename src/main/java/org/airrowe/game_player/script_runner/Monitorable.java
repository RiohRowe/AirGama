package org.airrowe.game_player.script_runner;
import java.awt.Point;

import org.airrowe.game_player.image_grabbing.BasicScreenGrabber;
import org.airrowe.game_player.image_grabbing.ImgManager;
import org.airrowe.game_player.image_processing.DirectImgLocate;
import org.airrowe.game_player.image_processing.MatchResult;
import org.airrowe.game_player.input_emulation.Mouse;
import org.airrowe.game_player.script_runner.areas.Area;

public class Monitorable {
	private static final Double DEFAULT_MATCH_THRESHOLD = 0.85;
	private DirectImgLocate dil;
	private BasicScreenGrabber bsg;
	private Area area;
	private Viewable imgRef;
	private boolean expectMatch;
	private double matchThreshold;
	
	public Monitorable( Area area, Viewable imgRef, boolean expectMatch, Double matchThreshold) {
		this.dil = new DirectImgLocate();
		this.bsg = BasicScreenGrabber.get();
		this.area = area;
		this.imgRef = imgRef;
		this.expectMatch = expectMatch;
		this.matchThreshold = matchThreshold==null ? DEFAULT_MATCH_THRESHOLD : matchThreshold;
	}
	
	public boolean check() {
//		this.traceWithMouse();
		MatchResult result = DirectImgLocate.findTemplateNormCoeff(ImgManager.convertToMat(bsg.imgTarget(this.area.areaConcrete)), imgRef.getMat(), true);
		return ( result.score >= this.matchThreshold ) ? this.expectMatch : !this.expectMatch;
	}
	public boolean check(Viewable altViewable, boolean trueIfThere, Double matchThreshold) {
		double mt = matchThreshold==null ? this.matchThreshold : matchThreshold;
		MatchResult result = DirectImgLocate.findTemplateNormCoeff(ImgManager.convertToMat(bsg.imgTarget(this.area.areaConcrete)), altViewable.getMat(), false);
		return ( result.score >= mt ) ? trueIfThere : !trueIfThere;
	}
	public Point getAreaCenter() {
		return area.center;
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
}
