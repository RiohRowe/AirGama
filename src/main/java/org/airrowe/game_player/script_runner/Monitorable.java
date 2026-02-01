package org.airrowe.game_player.script_runner;
import java.awt.Point;

import org.airrowe.game_player.image_grabbing.BasicScreenGrabber;
import org.airrowe.game_player.image_grabbing.ImgManager;
import org.airrowe.game_player.image_processing.DirectImgLocate;
import org.airrowe.game_player.image_processing.MatchResult;
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
		MatchResult result = DirectImgLocate.findTemplateNormCoeff(ImgManager.convertToMat(bsg.imgTarget(this.area.areaConcrete)), imgRef.getMat(), false);
		return ( result.score >= this.matchThreshold ) ? this.expectMatch : !this.expectMatch;
	}
	public Point getAreaCenter() {
		return area.center;
	}
}
