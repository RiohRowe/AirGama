package org.airrowe.game_player.script_runner.areas;

import java.awt.image.BufferedImage;

import org.airrowe.game_player.image_grabbing.BasicScreenGrabber;

public class AreaMaker implements Runnable{

	@Override
	public void run() {
		//Take Image of screen
		BufferedImage bi = BasicScreenGrabber.get().imgFullScreen();
		//Open window with screen capture as background
		//
		
	}

}
