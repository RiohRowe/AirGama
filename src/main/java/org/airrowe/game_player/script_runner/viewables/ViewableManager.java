package org.airrowe.game_player.script_runner.viewables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import org.airrowe.game_player.file_management.ResourceFolder;

public class ViewableManager implements Serializable{
	private static final long serialVersionUID = 1000000003L;
	private static final String SAVE_FILE_LOC = ResourceFolder.SAVE_FILES.path+"viewables.vw";
	private static ViewableManager instance;
	private Map<ResourceFolder, Map<String, Map<String, ViewableGroup>>> viewableGroups;
	
	private ViewableManager() {
		this.viewableGroups = new TreeMap<ResourceFolder, Map<String, Map<String, ViewableGroup>>>();
		for(ResourceFolder rf : ResourceFolder.values()) {
			this.viewableGroups.put(rf, new TreeMap<String, Map<String, ViewableGroup>>());
		}
	}
	private static ViewableManager loadInstance() {
		File viewablesFile = new File(SAVE_FILE_LOC);
		if( viewablesFile.exists() ) {
			try(FileInputStream fis = new FileInputStream(viewablesFile); ObjectInputStream ois = new ObjectInputStream(fis)) {
				Object readObj = ois.readObject();
				instance = (ViewableManager)readObj;
				return instance;
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		instance = new ViewableManager();
		return instance;
	}
	public static void saveInstance() {
		if( instance == null) {
			return;
		}
		try(FileOutputStream fos = new FileOutputStream(SAVE_FILE_LOC); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(instance);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ViewableManager get() {
		if( instance == null ) {
			//Try and load from file, and create new if that fails.
			instance = loadInstance();
		}
		return instance;
	}
	
	public boolean groupExists(ViewableGroup group) {
		Map<String, Map<String, ViewableGroup>> vgm = this.viewableGroups.get(group.getMainDirectory());
		return vgm.containsKey(group.getSubDirectory()) && vgm.get(group.getSubDirectory()).containsKey(group.getBaseName());
	}
	public boolean addViewableGroup(ViewableGroup group) {
		Map<String, Map<String, ViewableGroup>> vgmm = this.viewableGroups.get(group.getMainDirectory());
		if( !vgmm.containsKey(group.getSubDirectory()) ) {
			vgmm.put(group.getSubDirectory(), new TreeMap<String, ViewableGroup>());
		}
		Map<String, ViewableGroup> vgm = vgmm.get(group.getSubDirectory());
		if( !vgm.containsKey(group.getBaseName()) ) {
			vgm.put(group.getBaseName(), group);
			return true;
		}
		return false;
	}
	public Map<ResourceFolder, Map<String, Map<String, ViewableGroup>>> getViewableGroups(){
		return this.viewableGroups;
	}
}
