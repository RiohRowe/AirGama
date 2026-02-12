package org.airrowe.game_player.script_runner.areas;

import java.awt.Rectangle;
import java.util.List;

import org.airrowe.game_player.image_grabbing.GameWindow;

public class GameWArea extends Area{
	private static final long serialVersionUID = 1000000005L;
	public static final GameWindow GW = GameWindow.getGameWindow();
	private String activity;
	private String position;
	private String target;
	
	public GameWArea(String activity, String position, String target, Rectangle areaRelativeToParent, boolean fromBottom, boolean fromRight, Rectangle gameBB){
		super(areaRelativeToParent, fromBottom, fromRight, null);
		this.parentArea = gameBB;
		this.activity = activity;
		this.position = position;
		this.target = target;
		AreaManager.get().addGameWindowArea(this);
	}
	
	@Override
	public void setParent(Area parent) {
		this.parent = null;
	}

	public String getActivity() {
		return activity;
	}

	public String getPosition() {
		return position;
	}

	public String getTarget() {
		return target;
	}
}
