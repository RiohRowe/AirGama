package org.airrowe.game_player.image_processing.game_window;

import java.awt.Rectangle;
import java.util.List;

import org.airrowe.game_player.ResourceFolder;
import org.airrowe.game_player.image_grabbing.BasicScreenGrabber;
import org.airrowe.game_player.image_grabbing.ImgManager;
import org.airrowe.game_player.image_processing.DirectImgLocate;
import org.airrowe.game_player.image_processing.MatchResult;
import org.airrowe.game_player.script_runner.Viewable;
import org.opencv.core.Mat;

public class GameWindowFinder {
	public static final Viewable TLGW_IMG = new Viewable(ResourceFolder.GAME_WINDOW_REF_IMGS, "GameWindowTopLeft.bmp");
	public static final Viewable BRGW_IMG = new Viewable(ResourceFolder.GAME_WINDOW_REF_IMGS, "GameWindowBottomRight.bmp");
	
	private final BasicScreenGrabber bsg = BasicScreenGrabber.get(0);
	private static GameWindowFinder instance;
	private Mat topLeftRefImg;
	private Mat bottomRightRefImg;
	
	private GameWindowFinder(){
		topLeftRefImg = TLGW_IMG.getMat();
		bottomRightRefImg = BRGW_IMG.getMat();
	}
	
	public static GameWindowFinder get() {
		if(instance == null) {
			instance = new GameWindowFinder();
		}
		return instance;
	}
	
	public Rectangle getGameWindowDimensions() {
		Mat fullScreenImg = ImgManager.convertToMat(bsg.imgFullScreen());
		MatchResult tlMatch = DirectImgLocate.findTemplateNormCoeff(fullScreenImg, List.of(TLGW_IMG), 0.8, true);
		MatchResult brMatch = DirectImgLocate.findTemplateNormCoeff(fullScreenImg, List.of(BRGW_IMG), 0.8, true);
		if(tlMatch.score < 0.8 || brMatch.score < 0.8 ) {
			System.out.println("GAME BOX NOT VISIBLE");
			return null;
		}
		return new Rectangle(
				(int)tlMatch.location.x,
				(int)tlMatch.location.y+topLeftRefImg.height(),
				(int)(brMatch.location.x-tlMatch.location.x+bottomRightRefImg.width()), 
				(int)(brMatch.location.y-tlMatch.location.y+bottomRightRefImg.height()-topLeftRefImg.height()));
	}
	
	public boolean gameWindowValid(Rectangle gameBB) {
		Rectangle expectedBRLoc = new Rectangle(
				(gameBB.x+gameBB.width)-(this.bottomRightRefImg.cols()),
				(gameBB.y+gameBB.height)-(this.bottomRightRefImg.rows()),
				this.bottomRightRefImg.cols(),
				this.bottomRightRefImg.rows());

		Rectangle expectedTLLoc = new Rectangle(
				gameBB.x,
				gameBB.y-this.topLeftRefImg.rows(),
				this.topLeftRefImg.cols(),
				this.topLeftRefImg.rows());
		Mat expectedBRImg = ImgManager.convertToMat(bsg.imgTarget(expectedBRLoc));
		Mat expectedTLImg = ImgManager.convertToMat(bsg.imgTarget(expectedTLLoc));
		MatchResult tlMatch = DirectImgLocate.findTemplateNormCoeff(expectedTLImg, List.of(TLGW_IMG), 0.8, true);
		MatchResult brMatch = DirectImgLocate.findTemplateNormCoeff(expectedBRImg, List.of(BRGW_IMG), 0.8, true);
		return (tlMatch.score>=.8 && brMatch.score>=.8);
	}
}
