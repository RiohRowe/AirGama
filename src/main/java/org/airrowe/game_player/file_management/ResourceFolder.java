package org.airrowe.game_player.file_management;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum ResourceFolder {
	SOUNDS("sounds/"),
	GAME_WINDOW_REF_IMGS("imgs/GameWindowRefImgs/"),
	GAME_MENU_REF_IMGS("imgs/GameMenuRefImgs/"),
	GAME_TEXT_REF_IMGS("imgs/textRefImgs/"),
	GAME_INVIN_REF_IMGS("imgs/GameInvinRefImgs/"),
	GAME_MAP_REF_IMGS("imgs/GameMapRefImgs/"),
	GAME_WORLD_REF_IMGS("imgs/GameWorldRefImgs/"),
	TEST_IMGS("testImgs/"),
	SAVE_FILES("saveFiles/"),
	WUPSIES("imgs/Wupsies/");
	
	public static final String REF_FOLDER_PATH = "src/main/resources/";
	
	public String path;
	
	ResourceFolder(String path){
		this.path=REF_FOLDER_PATH+path;
	}
	public File getFile(String fileName) {
		return new File(path+fileName);
	}
	
	public boolean establishSubDirectory(String directory){
		try {
			Path current = Paths.get(this.path+directory);
			if(!Files.exists(current)) {
				Files.createDirectories(current);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
