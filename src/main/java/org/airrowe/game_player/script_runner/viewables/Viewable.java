package org.airrowe.game_player.script_runner.viewables;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.airrowe.game_player.file_management.ResourceFolder;
import org.airrowe.game_player.image_grabbing.ImgManager;
import org.opencv.core.Mat;

public class Viewable implements Serializable, Comparable<Viewable>{
	private static final long serialVersionUID = 1000000001L;
	protected String fileName;
	protected ViewableGroup parentGroup;
	protected Long lastTouchedTimestamp;
	protected double bestMatchValue;
	protected double averageMatchValue;
	protected int matchCount;
	protected transient Mat cachedImg;
	
	public Viewable(ViewableGroup parentGroup, String baseFileName, int index, Mat mat) {
		this.parentGroup = parentGroup;
		this.fileName = baseFileName+"-"+index+(mat.channels()==4 ? ".png" : ".bmp");
		this.lastTouchedTimestamp = 0L;
		this.bestMatchValue = 0;
		this.averageMatchValue = 0;
		this.matchCount = 0;
		this.cachedImg = mat;
		this.save(mat, true);//True=overwrite is exists already
	}
	
	public Viewable(ViewableGroup parentGroup, String fileName) {
		this.parentGroup = parentGroup;
		this.fileName = fileName;
		this.lastTouchedTimestamp = 0L;
		this.bestMatchValue = 0;
		this.averageMatchValue = 0;
		this.matchCount = 0;
	}

	public Mat getMat() {
		if( this.cachedImg == null) {
			File file = new File(parentGroup.getDirectoryPath()+this.fileName);
			try {
				this.cachedImg = this.fileName.endsWith(".bmp") ? 
						ImgManager.bufferedImageBGRToMatBGR(ImageIO.read(file)):
						ImgManager.bufferedImageABGRToMatBGRA(ImageIO.read(file));
			} catch (IOException e) {
				System.out.println("Unable to find/read file:" + file.getPath());
				e.printStackTrace();
				this.cachedImg = null;
			}
		}
		return this.cachedImg;
	}
	public String getName() {
		return this.fileName;
	}
	public String getFileFullPath() {
		return this.parentGroup.getDirectoryPath()+fileName;
	}
	public boolean save(Mat mat, boolean overwrite) {
		//Check if file exists
		File imgFile = new File(this.getFileFullPath());
		if( imgFile.exists() && !overwrite) {
			return false;
		}
		ImgManager.saveMatToFile(mat, this.getFileFullPath());
		return true;
	}
	public boolean delete() {
		File imgFile = new File(this.parentGroup.getDirectoryPath()+this.fileName);
		this.cachedImg = null;
		return imgFile.delete();
		
	}
	public void doStatistics(double matchScore) {
		Long timestamp = System.currentTimeMillis();
		this.lastTouchedTimestamp=timestamp;
		this.parentGroup.touch(timestamp);
		this.bestMatchValue = matchScore>this.bestMatchValue ? matchScore : this.bestMatchValue;
		this.averageMatchValue = ((this.averageMatchValue*this.matchCount)+matchScore)/(this.matchCount+1);
		this.matchCount++;
		
	}

	@Override
	public int hashCode() {
		return Objects.hash(fileName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Viewable other = (Viewable) obj;
		return Objects.equals(fileName, other.fileName);
	}

	@Override
	public int compareTo(Viewable o) {
		return this.hashCode()-o.hashCode();
	}
}
