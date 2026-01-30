package org.airrowe.game_player.image_processing;

import org.opencv.core.Point;

public class MatchResult {
	public Point location;
	public double score;
	
	public MatchResult(Point location, double score) {
		this.location = location;
		this.score = score;
	}
}
