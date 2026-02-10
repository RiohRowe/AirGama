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
		Mat fullScreenImg = ImgManager.bufferedImageBGRToMatBGR(bsg.imgFullScreen());
		MatchResult tlMatch = DirectImgLocate.findTemplateNormCorr(fullScreenImg, List.of(TLGW_IMG), 0.8);
		MatchResult brMatch = DirectImgLocate.findTemplateNormCorr(fullScreenImg, List.of(BRGW_IMG), 0.8);
		if(tlMatch.score < 0.8 || brMatch.score < 0.8 ) {
			System.out.println("GAME BOX NOT VISIBLE");
			return null;
		}
		return new Rectangle(
				(int)tlMatch.locationTL.x,
				(int)tlMatch.locationTL.y+topLeftRefImg.height(),
				(int)(brMatch.locationTL.x-tlMatch.locationTL.x+bottomRightRefImg.width()), 
				(int)(brMatch.locationTL.y-tlMatch.locationTL.y+bottomRightRefImg.height()-topLeftRefImg.height()));
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
		Mat expectedBRImg = ImgManager.bufferedImageBGRToMatBGR(bsg.imgTarget(expectedBRLoc));
		Mat expectedTLImg = ImgManager.bufferedImageBGRToMatBGR(bsg.imgTarget(expectedTLLoc));
		MatchResult tlMatch = DirectImgLocate.findTemplateNormCorr(expectedTLImg, List.of(TLGW_IMG), 0.8);
		MatchResult brMatch = DirectImgLocate.findTemplateNormCorr(expectedBRImg, List.of(BRGW_IMG), 0.8);
		return (tlMatch.score>=.8 && brMatch.score>=.8);
	}
}
