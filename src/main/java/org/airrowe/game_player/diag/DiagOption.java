package org.airrowe.game_player.diag;

public enum DiagOption {
	BEST3MATCH(0),
	WORST3MATCH(1),
	EXPECTED_MATCH_LOC(2),
	REFERENCE_IMG(3),
	MATCH_SPACE_HEAT_MAP(4),
	SEARCH_AREA(5);
//	TARGET_MONITORS(6),
//	START_MONITORS(7),
//	PROGRESS_MONITORS(8),
//	FINNISHED_MONITORS(9);
//	
	public int bitRep;
	
	DiagOption(int bitShift){
		bitRep = 1<<bitShift;
	}
	
	public boolean doDiag(int flags) {
		return (bitRep & flags) > 0;
	}
}
