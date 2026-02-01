package org.airrowe.game_player.sandbox;

import java.awt.Rectangle;

public class sandbox {
	public static void main(String[] args) {
		Rectangle rect = new Rectangle(-10,15,100,100);		
		System.out.println("CENTER=("+rect.getCenterX()+","+rect.getCenterY()+")");
	}
}
