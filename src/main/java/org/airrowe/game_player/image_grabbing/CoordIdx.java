package org.airrowe.game_player.image_grabbing;

public enum CoordIdx {
	
	X1(0),
	Y1(1),
	X2(2),
	Y2(3);
	
	private int coordIdx;
	
	private CoordIdx(int i) {
		this.coordIdx=i;
	}
	public int idx() {
		return coordIdx;
	}
}
