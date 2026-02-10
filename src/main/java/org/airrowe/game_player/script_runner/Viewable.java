package org.airrowe.game_player.script_runner;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

import org.airrowe.game_player.ResourceFolder;
import org.airrowe.game_player.image_grabbing.ImgManager;
import org.opencv.core.Mat;

public class Viewable implements Serializable{
	private static final long serialVersionUID = 1000000001L;
	protected String fileName;
	protected ResourceFolder folder;
	protected transient Mat cachedImg;
	
	public Viewable(ResourceFolder folder, String fileLocation) {
		this.folder = folder;
		this.fileName = fileLocation;
	}
	
	public Mat getMat() {
		if( this.cachedImg == null) {
			try {
				this.cachedImg = this.fileName.endsWith(".bmp") ? 
						ImgManager.bufferedImageBGRToMatBGR(ImageIO.read(folder.getFile(fileName))):
						ImgManager.bufferedImageABGRToMatBGRA(ImageIO.read(folder.getFile(fileName)));
			} catch (IOException e) {
				System.out.println("Unable to find/read file:" + folder.path+fileName);
				e.printStackTrace();
				this.cachedImg = null;
			}
		}
		return this.cachedImg;
	}
	public String getName() {
		return this.fileName;
	}
	public boolean save(Mat mat, boolean overwrite) {
		//Check if file exists
		File imgFile = new File(this.folder.path+fileName);
		if( imgFile.exists() && !overwrite) {
			return false;
		}
		ImgManager.saveMatToFile(cachedImg, folder, fileName);
		return true;
	}
}
