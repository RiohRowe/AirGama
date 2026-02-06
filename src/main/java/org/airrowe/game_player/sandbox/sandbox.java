package org.airrowe.game_player.sandbox;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.airrowe.game_player.Sound;
import org.airrowe.game_player.image_grabbing.BasicScreenGrabber;
import org.airrowe.game_player.image_grabbing.ImgManager;
import org.airrowe.game_player.input_emulation.Mouse;
import org.opencv.core.Mat;

public class sandbox {
	public static void main(String[] args) {
		System.out.println("WAITING");
		for( int i=5; i>=0; --i) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("SLEEP FAILED");
				e.printStackTrace();
			}
			System.out.println(i);
			if(i%2==0) {
				Sound.TICK.play();
			} else {
				Sound.TOCK.play();
			}
		}
		Sound.BIKE_BELL.play();
		Mouse.get().moveMouse(1138,431);
		for( int i=0; i<20;++i) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(Mouse.get().getMousePosition().toString());
		}
		Sound.BIKE_BELL.play();
		
		
		
	}
}
