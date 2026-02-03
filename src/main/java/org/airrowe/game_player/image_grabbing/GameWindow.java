package org.airrowe.game_player.image_grabbing;
import static org.airrowe.game_player.image_grabbing.CoordIdx.*;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;

import org.airrowe.game_player.image_processing.game_window.GameWindowFinder;

public class GameWindow {
	private static GameWindow instance;
	private GraphicsDevice[] screenDevs;
	private Dimension[] screens;
	private int primaryScreenIdx;
	private Rectangle gameBB;
	public Robot robot;
	
	private GameWindow() throws AWTException {
		this.screenDevs = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		this.screens = new Dimension[this.screenDevs.length];
		for(int i=0; i<this.screenDevs.length; ++i) {
			this.screens[i]=new Dimension(this.screenDevs[i].getDisplayMode().getWidth(), screenDevs[i].getDisplayMode().getHeight());
		}
		this.primaryScreenIdx=0;
		this.gameBB = new Rectangle(
				0,
				0,
				(int)this.screens[this.primaryScreenIdx].getWidth(),
				(int)this.screens[this.primaryScreenIdx].getHeight());
		this.robot = new Robot(screenDevs[this.primaryScreenIdx]);
		
	}
	
	public static GameWindow getGameWindow() {
		if(instance==null) {
			try {
				instance = new GameWindow();
			} catch (AWTException e) {
				e.printStackTrace();
				return null;
			}
		}
		return instance;
		
	}
	public Rectangle getGameBox() {
		if( !GameWindowFinder.get().gameWindowValid(gameBB)) {
			this.gameBB = GameWindowFinder.get().getGameWindowDimensions();
		}
		System.out.println("Gamebox="+this.gameBB.toString());
		return this.gameBB;
	}
	public Rectangle getFullScreenBox() {
		return new Rectangle(this.screens[this.primaryScreenIdx].width, this.screens[this.primaryScreenIdx].height);
	}
	public void setScreen(int idx) {
		if(this.screens.length>idx) {
			try {
				this.robot = new Robot(screenDevs[idx]);
			} catch(AWTException e) {
				return;
			}
			this.gameBB.setBounds(0, 0, this.screens[idx].width, this.screens[idx].height);
		}
	}
	public int pixIdxToabsolutePos(int pixIdx, boolean height) {
		return height ? pixIdx*65535/(this.screens[primaryScreenIdx].height - 1) : pixIdx*65535/(this.screens[primaryScreenIdx].width - 1);
	}
	public Robot getRobot() {
		return this.robot;
	}
}
