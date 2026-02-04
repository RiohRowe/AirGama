package org.airrowe.game_player.script_runner.areas;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class AreaManager {
	private static AreaManager instance;
	
	private List<GameWArea> gameWindowAreas;
	private List<GameWArea> unInitialized;
	private List<PointEventArea> pointEventAreas;
	
	private AreaManager() {
		this.gameWindowAreas = new ArrayList<GameWArea>();
		this.unInitialized = new ArrayList<GameWArea>();
		this.pointEventAreas = new ArrayList<PointEventArea>();
	}
	
	public static AreaManager get(){
		if( instance == null) {
			instance = new AreaManager();
		}
		return instance;
	}
	
	public void addGameWindowArea(GameWArea gwa) {
		this.gameWindowAreas.add(gwa);
		this.unInitialized.add(gwa);
	}
	public void initializeGameWindowAreas(Rectangle gameBoundingBox) {
		for( GameWArea gwa : this.unInitialized) {
			gwa.calcCurrentArea(gameBoundingBox);
		}
		this.unInitialized.clear();
	}
	public void updateGameBoundingBox(Rectangle gameBoundingBox) {
		for( GameWArea gwa : this.gameWindowAreas ) {
			gwa.calcCurrentArea(gameBoundingBox);
		}
		this.unInitialized.clear();
	}

}
