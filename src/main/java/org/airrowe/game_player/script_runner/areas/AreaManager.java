package org.airrowe.game_player.script_runner.areas;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.airrowe.game_player.file_management.ResourceFolder;

public class AreaManager implements Serializable {
	private static final long serialVersionUID = 1000000006L;
	private static final String SAVE_FILE_LOC = ResourceFolder.SAVE_FILES.path+"areas.ar";
	private static AreaManager instance;
	//Activity->position->target
	private Map<String, Map<String, Map<String, GameWArea>>> gameWindowAreas;
	private List<PointEventArea> pointEventAreas;
	
	private AreaManager() {
		this.gameWindowAreas = new TreeMap<String, Map<String, Map<String, GameWArea>>>();
		this.pointEventAreas = new ArrayList<PointEventArea>();
	}
	
	public static AreaManager get(){
		if( instance == null) {
			loadInstance();
		}
		return instance;
	}
	
	public boolean addGameWindowArea(GameWArea gwa) {
		if( !this.gameWindowAreas.containsKey(gwa.getActivity()) ) {
			this.gameWindowAreas.put(gwa.getActivity(), new TreeMap<String, Map<String,GameWArea>>());
		}
		Map<String, Map<String, GameWArea>> activityGAs = this.gameWindowAreas.get(gwa.getActivity());
		if( !activityGAs.containsKey(gwa.getPosition()) ) {
			activityGAs.put(gwa.getPosition(), new TreeMap<String, GameWArea>());
		}
		Map<String, GameWArea> positionGAs = activityGAs.get(gwa.getPosition());
		if( positionGAs.containsKey(gwa.getTarget()) ) {
			System.out.println("GameArea "+gwa.getActivity()+":"+gwa.getPosition()+":"+gwa.getTarget()+" already exists!");
			return false;
		}
		positionGAs.put(gwa.getTarget(), gwa);
		return true;
	}
	public void refreshGameWindowAreas(Rectangle gameBoundingBox) {
		for( Map<String, Map<String, GameWArea>> activityGAs : this.gameWindowAreas.values() ) {
			for( Map<String, GameWArea> positionGAs : activityGAs.values() ) {
				for(GameWArea targetGA : positionGAs.values()) {
					targetGA.reCalculateArea(gameBoundingBox);
				}
			}
		}
	}
	
	public static AreaManager loadInstance() {
		File file = new File(SAVE_FILE_LOC);
		if( !file.exists() ) {
			System.out.println("Save file does not exits!");
			instance = new AreaManager();
			return instance;
		}
		try(FileInputStream fis = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(fis)) {
			instance = (AreaManager) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return instance;
	}
		
	public static void saveInstance() {
		try(FileOutputStream fos = new FileOutputStream(SAVE_FILE_LOC); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(instance);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
