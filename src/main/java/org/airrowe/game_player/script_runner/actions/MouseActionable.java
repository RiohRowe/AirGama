package org.airrowe.game_player.script_runner.actions;

import java.awt.Point;
import java.util.List;

import org.airrowe.game_player.file_management.Sound;
import org.airrowe.game_player.input_emulation.Mouse;
import org.airrowe.game_player.script_runner.Monitorable;

public class MouseActionable extends Actionable{
	private static final Mouse mouse = Mouse.get();
	private int scrollAmnt;
	private int prepositionMouseDelay;

	public MouseActionable(Monitorable target, Action interactionType, Integer scrollAmnt, Monitorable notStartIndicator,
			Monitorable progressIndicator, List<Monitorable> finishedIndicators, int msExpectedStart, int msExpectedEnd,
			Actionable next, Integer prepositionMouseDelay) {
		super(target, interactionType, notStartIndicator, progressIndicator, finishedIndicators, msExpectedStart, msExpectedEnd,
				next);
		this.scrollAmnt = scrollAmnt==null ? 0 : scrollAmnt;
		this.prepositionMouseDelay = prepositionMouseDelay==null ? mouse.getDefaultActionPrepositionDelay() : prepositionMouseDelay;
		if( !interactionType.isMouseAction()) {
//			throw new RuntimeException("INVALID action.\t"+interactionType.toString()+" is NOT a mouse action." );
			System.out.println("INVALID action.\t"+interactionType.toString()+" is NOT a mouse action. Defaulting to "+Action.DEFAULT_MOUSE_ACTION.toString());
			interactionType = Action.DEFAULT_MOUSE_ACTION;
		}
	}
	public MouseActionable(Monitorable target, Action interactionType, Integer scrollAmnt, Monitorable notStartIndicator,
			Monitorable progressIndicator, List<Monitorable> finishedIndicators, int msExpectedStart, int msExpectedEnd,
			Actionable next) {
		super(target, interactionType, notStartIndicator, progressIndicator, finishedIndicators, msExpectedStart, msExpectedEnd,
				next);
		this.scrollAmnt = scrollAmnt==null ? 0 : scrollAmnt;
		this.prepositionMouseDelay = 0;
		if( !interactionType.isMouseAction()) {
//			throw new RuntimeException("INVALID action.\t"+interactionType.toString()+" is NOT a mouse action." );
			System.out.println("INVALID action.\t"+interactionType.toString()+" is NOT a mouse action. Defaulting to "+Action.DEFAULT_MOUSE_ACTION.toString());
			interactionType = Action.DEFAULT_MOUSE_ACTION;
		}
	}
	
	protected void prepositionMouse(Point targetPoint) {
		mouse.moveMouse(targetPoint.x, targetPoint.y);
		try {
			Sound.TICK.play();
			Thread.sleep(prepositionMouseDelay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doAction() {
		//GetTarget
		Point targetPoint = this.target.getLastFoundCenter();
		//PrepositionMouseAndDelay
		//Perform MouseAction -> targetCenter.
		switch( interactionType ) {
			case MOUSE_MOVE:
				mouse.moveMouse(targetPoint.x, targetPoint.y);
				break;
			case MOUSE_LEFT_CLICK:
				if( this.prepositionMouseDelay>0 ) {
					this.prepositionMouse(targetPoint);
				}
				mouse.leftClick(targetPoint.x, targetPoint.y);
				break;
			case MOUSE_RIGHT_CLICK:
				if( this.prepositionMouseDelay>0 ) {
					this.prepositionMouse(targetPoint);
				}
				mouse.rightClick(targetPoint.x, targetPoint.y);
				break;
			case MOUSE_MIDDLE_CLICK:
				if( this.prepositionMouseDelay>0 ) {
					this.prepositionMouse(targetPoint);
				}
				mouse.middleClick(targetPoint.x, targetPoint.y);
				break;
			case MOUSE_SCROLL:
				if( this.prepositionMouseDelay>0 ) {
					this.prepositionMouse(targetPoint);
				}
				mouse.scroll(this.scrollAmnt);
			default:
				System.out.println("MouseAction does not recognize this action: "+interactionType.toString());
		}
	}
	
	
}
