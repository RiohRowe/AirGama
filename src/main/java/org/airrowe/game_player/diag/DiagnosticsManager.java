package org.airrowe.game_player.diag;

public class DiagnosticsManager {
	private static DiagnosticsManager instance;
	public int numDiags;
	public boolean diagnose;
	public int diagTypeFlags;
	
	private DiagnosticsManager() {
		
	}
	
	public  static DiagnosticsManager get() {
		if( instance == null ) {
			instance = new DiagnosticsManager();
		}
		return instance;
	}
	public void setNumDiags(int num) {
		this.numDiags = num;
	}
}
