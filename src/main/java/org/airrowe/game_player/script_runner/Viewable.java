package org.airrowe.game_player.script_runner;

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
				this.cachedImg = ImgManager.convertToMat(ImageIO.read(folder.getFile(fileName)));
			} catch (IOException e) {
				System.out.println("Unable to find/read file:" + folder.path+fileName);
				e.printStackTrace();
				this.cachedImg = null;
			}
		}
		return this.cachedImg;
	}
	
}
