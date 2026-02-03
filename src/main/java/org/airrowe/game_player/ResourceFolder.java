package org.airrowe.game_player;

import java.io.File;

public enum ResourceFolder {
	SOUNDS("sounds/"),
	GAME_WINDOW_REF_IMGS("imgs/GameWindowRefImgs/"),
	GAME_MENU_REF_IMGS("imgs/GameMenuRefImgs/"),
	GAME_TEXT_REF_IMGS("imgs/textRefImgs/"),
	GAME_INVIN_REF_IMGS("imgs/GameInvinRefImgs/"),
	GAME_MAP_REF_IMGS("imgs/GameMapRefImgs/"),
	GAME_WORLD_REF_IMGS("imgs/GameWorldRefImgs/"),
	WUPSIES("imgs/Wupsies/");
	
	public static final String REF_FOLDER_PATH = "src/main/resources/";
	
	public String path;
	
	ResourceFolder(String path){
		this.path=REF_FOLDER_PATH+path;
	}
	public File getFile(String fileName) {
		return new File(path+fileName);
	}
	
	
}
