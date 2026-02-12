package org.airrowe.game_player.script_runner.viewables;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.airrowe.game_player.file_management.ResourceFolder;
import org.opencv.core.Mat;

public class ViewableGroup implements Serializable, Comparable<ViewableGroup>{

	private static final long serialVersionUID = 1000000002L;
	private Set<Viewable> viewables;
	private ResourceFolder mainDirectory;
	private String subDirectory;//ScriptName/positionName/AreaName/
	private String baseName;
	private Long lastTouchedTimestamp;
	
	public ViewableGroup(ResourceFolder mainDirectory, String[] subDirectory, String baseName) {
		//Check for name Duplicate
		this.mainDirectory = mainDirectory;
		this.subDirectory = String.join("/", subDirectory);
		this.baseName = baseName;
		if( !ViewableManager.get().addViewableGroup(this) ) {
			throw new RuntimeException("Viewable group with name \""+this.mainDirectory.path+this.subDirectory+'/'+baseName+"\" already exists." );
		}
		mainDirectory.establishSubDirectory(this.subDirectory);
		this.lastTouchedTimestamp = 0L;
		this.viewables = new TreeSet<Viewable>();
	}
	
	public String getDirectoryPath() {
		return this.mainDirectory.path+this.subDirectory+'/';
	}
	public void touch(Long timestamp) {
		this.lastTouchedTimestamp = timestamp;
	}
	
	public void addViewable(Mat mat) {
		this.viewables.add(new Viewable(this, this.baseName, this.viewables.size(), mat));
	}
	public boolean assimilateExistingFile(String fileName) {
		//Find file
		File file = new File(this.getDirectoryPath()+fileName);
		if( file.exists() ) {
			String fileExt = fileName.substring(fileName.lastIndexOf('.'));
			String newFileName = this.baseName+'-'+this.viewables.size()+fileExt;
			String newFilePathName = this.getDirectoryPath()+newFileName;
			if( !file.renameTo(new File(newFilePathName)) ) {
				System.out.println("Unable to rename file.");
				return false;
			}
			this.viewables.add( new Viewable(this, newFileName) );
			return true;
		}
		System.out.println("Could not find file named \""+fileName+"\" in "+this.getDirectoryPath());
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(baseName, mainDirectory, subDirectory);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ViewableGroup other = (ViewableGroup) obj;
		return Objects.equals(baseName, other.baseName) && mainDirectory == other.mainDirectory
				&& Objects.equals(subDirectory, other.subDirectory);
	}

	@Override
	public int compareTo(ViewableGroup o) {
		return this.hashCode()-o.hashCode();
	}

	public Set<Viewable> getViewables() {
		return viewables;
	}

	public ResourceFolder getMainDirectory() {
		return mainDirectory;
	}

	public String getSubDirectory() {
		return subDirectory;
	}

	public String getBaseName() {
		return baseName;
	}

	public Long getLastTouchedTimestamp() {
		return lastTouchedTimestamp;
	}
}
