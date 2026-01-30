package org.airrowe.game_player.sandbox;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import org.airrowe.game_player.Sound;
import org.airrowe.game_player.image_grabbing.BasicScreenGrabber;
import org.airrowe.game_player.image_grabbing.GameWindow;
import org.airrowe.game_player.image_grabbing.ImgManager;
import org.airrowe.game_player.image_processing.game_window.GameWindowFinder;
import org.airrowe.game_player.image_processing.text_parsing.DetectedText;
import org.airrowe.game_player.image_processing.text_parsing.TextReader;
import org.airrowe.game_player.input_emulation.Keyboard;
import org.airrowe.game_player.input_emulation.Mouse;
import org.opencv.core.Mat;

public class test {
	public static void printRectangle(Rectangle rec) {
		System.out.print("x:"+rec.x+" y:"+rec.y+"\tWidth:"+rec.width+" Height:"+rec.height);
	}
	public static void main(String[] args) {
		Mouse mouse = new Mouse();
		GameWindowFinder gwf = GameWindowFinder.get();
		BasicScreenGrabber bsg = BasicScreenGrabber.get(0);
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
		Sound.BELL.play();
		System.out.println("DETECTING");
		
		Rectangle gbb = gwf.get().getGameWindowDimensions();
		if ( gwf.get().gameWindowValid(gbb)) {
			System.out.println("SUCCESS!");
		} else {
			System.out.println("FAILURE!");
		}
//		Mat gameWindowImg = ImgManager.convertToMat(bsg.imgWindow());
//		List<DetectedText> finds = TextReader.extractTextData(gameWindowImg);
//		for( DetectedText find : finds) {
//			System.out.println("x:"+find.center.x+"\ty:"+find.center.y+"\t["+/**find.color.val[0]+","+find.color.val[1]+**/","+find.color.val[2]+"]-("+find.text+")");
//		}
		
	}
}
