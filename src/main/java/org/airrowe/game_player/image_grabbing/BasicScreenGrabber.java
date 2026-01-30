package org.airrowe.game_player.image_grabbing;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

public class BasicScreenGrabber {
	private static BasicScreenGrabber instance;
	private GameWindow gw;
	private int screenIdx; //For multiple screens
	private Rectangle gameBB;
	private Robot robot;
	
	private BasicScreenGrabber(int screenNum) {
		gw = GameWindow.getGameWindow();
		gw.setScreen(screenNum);
		this.gameBB=gw.getGameBox();
		this.robot = gw.robot;
	}
	
	public static BasicScreenGrabber get(int screen) {
		if(instance == null || instance.screenIdx != screen) {
			if( instance == null ) {
				instance = new BasicScreenGrabber(screen);
			} else {
				instance.gw.setScreen(screen);
			}
		}
		return instance;
	}
	public static BasicScreenGrabber get() {
		if(instance == null) {
			instance = new BasicScreenGrabber(0);
		}
		return instance;
	}
	
	public BufferedImage imgWindow() {
		return robot.createScreenCapture(this.gameBB);
	}
	public BufferedImage imgFullScreen() {
		return robot.createScreenCapture(GameWindow.getGameWindow().getFullScreenBox());
	}
	public BufferedImage imgTarget(Rectangle target) {
		return robot.createScreenCapture(target);
	}
}
