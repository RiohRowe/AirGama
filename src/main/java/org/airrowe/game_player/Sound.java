package org.airrowe.game_player;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public enum Sound {
	TICK("tick.wav"),
	TOCK("tock.wav"),
	BELL("bell.wav");
	
	private static final String SOUND_LOC = "src/main/resources/sounds/";
	
	private String fileName;
	
	Sound(String fileName) {
		this.fileName = fileName;
	}
	
	public void play() {
		try {
            // Create a File object from the file path
            File soundFile = new File(SOUND_LOC+this.fileName);
            
            // Get an AudioInputStream from the file
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            
            // Obtain a sound clip resource
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
//            Thread.sleep(clip.getMicrosecondLength() / 1000); 

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
