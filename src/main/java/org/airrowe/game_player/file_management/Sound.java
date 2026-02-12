package org.airrowe.game_player.file_management;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public enum Sound {
	TICK("tick.wav"),
	TOCK("tock.wav"),
	BIKE_BELL("bell.wav"),
	CHIMES_ASCENDING("AscendingChimes.wav"),
	BELL_HIGH("BellHigh.wav"),
	BELL_LOW("BellLow.wav"),
	DING_DONG_DOOR_BELL("DingDongDoorBell.wav"),
	HARD_BUZZER_MED("HardBuzzerMed.wav"),
	HARD_BUZZER_SHORT("hardBuzzerShort.wav"),
	SOFT_BUZZER_MED("softBuzzerMed.wav"),
	SOFT_DOUBLE_BUZZER_SHORT("SoftDoubleBuzzerShort.wav"),
	METAL_HIT_REV("ReverseMetalDing.wav"),
	HIT_SOFT("softHit.wav");
	
	
	private static final String SOUND_LOC = "src/main/resources/sounds/";
	
	private String fileName;
	
	Sound(String fileName) {
		this.fileName = fileName;
	}
	
	public void play() {
//		System.out.println("soundPlay");
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
