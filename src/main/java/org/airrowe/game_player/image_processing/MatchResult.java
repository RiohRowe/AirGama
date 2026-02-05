package org.airrowe.game_player.image_processing;

import org.opencv.core.Mat;

import java.awt.Point;

public class MatchResult {
	public Point locationTL;
	public Point locationC;
	public double score;
	
	public MatchResult(Point locationTL, Point locationC, double score) {
		this.locationTL = locationTL;
		this.locationC = locationC;
		this.score = score;
	}

	public MatchResult(org.opencv.core.Point locationTL, Mat template, double score) {
		this.locationTL = new Point((int)locationTL.x, (int)locationTL.y);
		this.locationC = new Point(this.locationTL.x+(template.cols()/2), this.locationTL.y+(template.rows()/2));
		this.score = score;
	}
}
