package org.airrowe.game_player.sandbox;

import java.awt.Rectangle;

import org.airrowe.game_player.Sound;

public class test {
	public static void printRectangle(Rectangle rec) {
		System.out.print("x:"+rec.x+" y:"+rec.y+"\tWidth:"+rec.width+" Height:"+rec.height);
	}
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
		Sound.BELL.play();
		System.out.println("BUILDING");
		WCFMScript script = new WCFMScript();
//		script.testArea();
		script.doLoop(1);
		
	}
}
