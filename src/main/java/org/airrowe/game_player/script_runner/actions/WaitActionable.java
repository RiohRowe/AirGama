package org.airrowe.game_player.script_runner.actions;

import java.util.List;

import org.airrowe.game_player.script_runner.Monitorable;

public class WaitActionable extends Actionable{
	public WaitActionable(Action interactionType, int timeMs, List<Monitorable> finishedIndicators, Actionable next) {
		super(null, interactionType, null, null, finishedIndicators, 0, timeMs, next);
		if( !interactionType.isWaitAction()) {
//			throw new RuntimeException("INVALID action.\t"+interactionType.toString()+" is NOT a wait action." );
			System.out.println("INVALID action.\t"+interactionType.toString()+" is NOT a wait action. Defaulting to "+Action.WAIT.toString());
			interactionType = Action.WAIT;
		}
	}

	@Override
	protected void doAction() {
		//Perform MouseAction -> targetCenter.
		switch( interactionType ) {
			case WAIT:
				System.out.println("WAITING...");
//				try {
//					Thread.sleep(this.msWaitTime);
//				} catch(InterruptedException e) {
//					System.out.println("WAIT failed");
//					e.printStackTrace();
//				}
				break;
			default:
				System.out.println("WaitAction does not recognize this action: "+interactionType.toString());
		}
	}
}
