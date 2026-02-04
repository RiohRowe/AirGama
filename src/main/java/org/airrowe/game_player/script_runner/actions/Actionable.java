package org.airrowe.game_player.script_runner.actions;

import java.util.List;

import org.airrowe.game_player.script_runner.Monitorable;

public class Actionable {
	private static final int DEFAULT_MONITOR_INTERVAL_MS = 300;
	Monitorable target;
	Monitorable notStartIndicator;
	Monitorable progressIndicator;
	List<Monitorable> finishedIndicators;
	int msMonitorInterval;
	int msExpectedStart;
	int msExpectedEnd;
	boolean hardStop;
	Action interactionType;
	Actionable nextAction;
	
	public Actionable(Monitorable target, Action interactionType, Monitorable notStartIndicator, Monitorable progressIndicator,
			List<Monitorable> finishedIndicator, int msExpectedStart, int msExpectedEnd, Actionable next) {
		this.target = target;
		this.interactionType = interactionType;
		this.notStartIndicator = notStartIndicator;
		this.progressIndicator = progressIndicator;
		this.finishedIndicators = finishedIndicator;
		this.msExpectedStart = msExpectedStart;
		this.msExpectedEnd = msExpectedEnd;
		this.nextAction = next;
		this.msMonitorInterval = DEFAULT_MONITOR_INTERVAL_MS;
		this.hardStop = false;
	}
	
	public void abortAction() {
		this.hardStop = true;
	}

	protected void doAction() {
		System.out.println("No action specified... Lovely day eh?");
	}

	public Monitorable getTarget() {
		return this.target;
	}
	public boolean doActionable(){
		//Check if action is valid
			if(this.target == null || this.target.check()) {
				//do action
				this.doAction();
			}
			else {
				return false;
			}
			//Wait for action to become monitorable
			try {
				Thread.sleep(msExpectedStart);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long timerStart = System.currentTimeMillis();
			//loop monitors
			while( !this.hardStop && (this.finishedIndicators.size()==0 || !this.checkMonitors(finishedIndicators)) ) {
				if( !this.hardStop && (this.progressIndicator == null || !this.progressIndicator.check()) ) {
					if( (System.currentTimeMillis()-timerStart) > (long)this.msExpectedEnd ) {
					   //If action failed, break and return false
						return false;
					}
					try {
						Thread.sleep(this.msMonitorInterval);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		// When finished, try next action
		//TODO DO ACTION LOGIC
		  //
		if( this.nextAction == null) {
			return true;
		}
		return this.nextAction.doActionable();
	}
	private boolean checkMonitors(List<Monitorable> monitors) {
		for( Monitorable monitor : monitors) {
			if (monitor.check()) {
				return true;
			}
		}
		return false;
	}
}
