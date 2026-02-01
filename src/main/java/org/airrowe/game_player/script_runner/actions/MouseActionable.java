package org.airrowe.game_player.script_runner.actions;

import java.awt.Point;

import org.airrowe.game_player.input_emulation.Mouse;
import org.airrowe.game_player.script_runner.Monitorable;

public class MouseActionable extends Actionable{
	private static final Mouse mouse = Mouse.get();
	private int scrollAmnt;

	public MouseActionable(Monitorable target, Action interactionType, Integer scrollAmnt, Monitorable notStartIndicator,
			Monitorable progressIndicator, Monitorable finishedIndicator, int msExpectedStart, int msExpectedEnd,
			Actionable next) {
		super(target, interactionType, notStartIndicator, progressIndicator, finishedIndicator, msExpectedStart, msExpectedEnd,
				next);
		this.scrollAmnt = scrollAmnt==null ? 0 : scrollAmnt;
		if( !interactionType.isMouseAction()) {
//			throw new RuntimeException("INVALID action.\t"+interactionType.toString()+" is NOT a mouse action." );
			System.out.println("INVALID action.\t"+interactionType.toString()+" is NOT a mouse action. Defaulting to "+Action.DEFAULT_MOUSE_ACTION.toString());
			interactionType = Action.DEFAULT_MOUSE_ACTION;
		}
	}

	@Override
	protected void doAction() {
		//GetTarget
		Point targetPoint = this.target.getAreaCenter();
		//Perform MouseAction -> targetCenter.
		switch( interactionType ) {
			case MOUSE_MOVE:
				mouse.moveMouse(targetPoint.x, targetPoint.y);
				break;
			case MOUSE_LEFT_CLICK:
				mouse.leftClick(targetPoint.x, targetPoint.y);
				break;
			case MOUSE_RIGHT_CLICK:
				mouse.rightClick(targetPoint.x, targetPoint.y);
				break;
			case MOUSE_MIDDLE_CLICK:
				mouse.middleClick(targetPoint.x, targetPoint.y);
				break;
			case MOUSE_SCROLL:
				mouse.scroll(this.scrollAmnt);
			default:
				System.out.println("MouseAction does not recognize this action: "+interactionType.toString());
		}
	}
	
	
}
