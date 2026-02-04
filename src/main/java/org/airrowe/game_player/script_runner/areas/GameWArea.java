package org.airrowe.game_player.script_runner.areas;

import java.awt.Rectangle;
import java.util.List;

import org.airrowe.game_player.image_grabbing.GameWindow;

public class GameWArea extends Area{
	public static final GameWindow GW = GameWindow.getGameWindow();
	
	public GameWArea(Rectangle areaRelativeToParent, boolean fromBottom, boolean fromRight, List<Area> children, Rectangle gameBB){
		super(areaRelativeToParent, fromBottom, fromRight, null, children);
		this.parentArea = gameBB;
		AreaManager.get().addGameWindowArea(this);
	}
	
	@Override
	public void setParent(Area parent) {
		this.parent = null;
	}
}
