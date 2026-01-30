package org.airrowe.game_player.script_runner;

import java.awt.Rectangle;

public interface Actionable {
	public Rectangle findTarget();
	public int checkProgress();
	public void execute();
}
