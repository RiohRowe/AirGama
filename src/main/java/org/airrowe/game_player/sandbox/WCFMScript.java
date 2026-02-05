package org.airrowe.game_player.sandbox;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.airrowe.game_player.ResourceFolder;
import org.airrowe.game_player.Sound;
import org.airrowe.game_player.diag.DiagnosticsManager;
import org.airrowe.game_player.image_grabbing.BasicScreenGrabber;
import org.airrowe.game_player.image_grabbing.GameWindow;
import org.airrowe.game_player.image_grabbing.ImgManager;
import org.airrowe.game_player.input_emulation.Mouse;
import org.airrowe.game_player.script_runner.Monitorable;
import org.airrowe.game_player.script_runner.Viewable;
import org.airrowe.game_player.script_runner.actions.Action;
import org.airrowe.game_player.script_runner.actions.MouseActionable;
import org.airrowe.game_player.script_runner.actions.WaitActionable;
import org.airrowe.game_player.script_runner.areas.Area;
import org.airrowe.game_player.script_runner.areas.AreaManager;
import org.airrowe.game_player.script_runner.areas.GameWArea;

public class WCFMScript {
	private MenuInvintory menuInvin;
	private MouseActionable tree1;
	private MouseActionable tree2;
	private MouseActionable moveToPos2;
	private MouseActionable clickWillowLogsLightFire;
	private MouseActionable clickWillowLogsAddFire;
	private MouseActionable clickFireAddLogs;
	private MouseActionable confirmAddWillowLogsFire;
	private MouseActionable lightFireTinderBox;
	private Monitorable fire;
	private Viewable willowLog;
	private Viewable willowLogSelected;
	private Viewable tinderBox;
	private Monitorable Pos1Ind;
	private Monitorable Pos2Ind;
	private Monitorable confirmBurnButton;
	private List<Monitorable> emptyInvinSlots;
	private Monitorable invinFilledWithLogs;
	private WaitActionable waitForLastLogToBurn;

	WCFMScript(){

		Mouse.get().setDefaultActionPrepositionDelay(300);
		Rectangle gameBB = GameWindow.getGameWindow().getGameBox();

		this.willowLog = new Viewable(ResourceFolder.GAME_INVIN_REF_IMGS, "WillowLog.bmp");
		this.willowLogSelected = new Viewable(ResourceFolder.GAME_INVIN_REF_IMGS, "WillowLogSelected.bmp");
		this.tinderBox = new Viewable(ResourceFolder.GAME_INVIN_REF_IMGS, "TinderBox.bmp");
		this.menuInvin = MenuInvintory.get(gameBB);
		this.emptyInvinSlots = new ArrayList<Monitorable>();
		for( int i=0; i<28; ++i) {
			this.emptyInvinSlots.add(new Monitorable(
				this.menuInvin.getInvinSlotArea(i),
				List.of(new Viewable(ResourceFolder.GAME_INVIN_REF_IMGS, (i+1)+"-empty.bmp")),
				true,
				null));
		}
		GameWArea fireGA = new GameWArea(new Rectangle(669,351,25,23),false,false,null,gameBB);
		this.fire = new Monitorable(
				fireGA,
				List.of(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos1Fire1.bmp"),
						new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos1Fire2.bmp"),
						new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos1Fire3.bmp")),
				true,
				null);
		GameWArea pos1IndGA = new GameWArea(new Rectangle(737,346,25,25),false,false,null,gameBB);
		this.Pos1Ind = new Monitorable(
				pos1IndGA,
				List.of(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos1Ind.bmp")),
				true,
				null);
		GameWArea pos2IndGA = new GameWArea(new Rectangle(719,346,24,25),false,false,null,gameBB);
		this.Pos2Ind = new Monitorable(
				pos2IndGA,
				List.of(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos2Ind.bmp")),
				true,
				null);
		GameWArea confirmBurnButtonGA = new GameWArea(new Rectangle(236,91,40,32),true,false,null,gameBB);
		this.confirmBurnButton = new Monitorable(
				confirmBurnButtonGA,
				List.of(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "confirmBurnButton.bmp")),
				true,
				null);
		this.invinFilledWithLogs = new Monitorable(
				menuInvin.getInvinSlotArea(27),
				List.of(willowLog), 
				true, 
				null);
		GameWArea tree1Area = new GameWArea(new Rectangle(656,293,29,29),false,false,null,gameBB);
		List<Viewable> tree1ImgRefs = List.of(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "1Pos1Tree1.bmp"),
		new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "2Pos1Tree1.bmp"),
		new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "3Pos1Tree1.bmp"),
		new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "4Pos1Tree1.bmp"),
		new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "5Pos1Tree1.bmp"),
		new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "6Pos1Tree1.bmp"),
		new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "7Pos1Tree1.bmp"),
		new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "8Pos1Tree1.bmp")
		);
		this.tree1 = new MouseActionable(
				new Monitorable(
					tree1Area,
					tree1ImgRefs,
					true,
					null),
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(
					new Monitorable(
						tree1Area,
						tree1ImgRefs,
						false,
						null),
					this.invinFilledWithLogs),
				1000,
				120000,
				null,
				null);
		GameWArea tree2Area = new GameWArea(new Rectangle(640,366,27,26),false,false,null,gameBB);
		List<Viewable> tree2ImgRefs = List.of(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "1Pos1Tree2.bmp"),
				new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "2Pos1Tree2.bmp"),
				new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "3Pos1Tree2.bmp"),
				new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "4Pos1Tree2.bmp"),
				new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "5Pos1Tree2.bmp"),
				new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "6Pos1Tree2.bmp"));
		this.tree2 = new MouseActionable(
				new Monitorable(
						tree2Area,
						tree2ImgRefs,
						true,
						null),
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(
					new Monitorable(
						tree2Area,
						tree2ImgRefs,
						false,
						null),
					this.invinFilledWithLogs),
				1000,
				120000,
				null,
				null);
		
		this.moveToPos2 = new MouseActionable(
				new Monitorable(
					fireGA,
					List.of(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos1Fire.bmp")),
					false,
					null),
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(this.Pos2Ind),
				100,
				3000,
				null,
				null);

		this.clickWillowLogsLightFire = new MouseActionable(
				new Monitorable(
						menuInvin.getInvinSlotArea(27),
						List.of(willowLog),
						true,
						null),
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(new Monitorable(
						menuInvin.getInvinSlotArea(27),
						List.of(this.willowLogSelected),
						true,
						null)),
				100,
				1000,
				null,
				null);
		this.lightFireTinderBox = new MouseActionable(
				new Monitorable(
						menuInvin.getInvinSlotArea(1),
						List.of(tinderBox),
						true,
						null),
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(this.Pos1Ind),
				500,
				10000,
				null,
				null);
		
		this.clickWillowLogsAddFire = new MouseActionable(
				new Monitorable(
						menuInvin.getInvinSlotArea(26),
						List.of(willowLog),
						true,
						null),
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(new Monitorable(
						menuInvin.getInvinSlotArea(26),
						List.of(this.willowLogSelected),
						true,
						null)),
				100,
				1000,
				null,
				null);
		
		this.clickFireAddLogs = new MouseActionable(
				this.fire,
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(this.confirmBurnButton),
				0,
				10000,
				null,
				null);
		
		this.confirmAddWillowLogsFire = new MouseActionable(
				this.confirmBurnButton,
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(this.emptyInvinSlots.get(26)),
				130000,
				150000,
				null,
				null);
		this.waitForLastLogToBurn = new WaitActionable(Action.WAIT, 20000, List.of(this.emptyInvinSlots.get(27)), null);
		AreaManager.get().initializeGameWindowAreas(gameBB);
//		ImgManager.saveMatImgDiag(ImgManager.convertToMat(BasicScreenGrabber.get().imgTarget(gameBB)), "GAME_BOUNDING_BOX");
	}
	public void testArea() {
//		new Monitorable(new Area( new Rectangle(1138,431,228,297),false,false,),tinderBox,false,null).traceWithMouse();
//		new Monitorable(this.menuInvin.getInvinArea(), List.of(tinderBox), false, null).traceWithMouse();
		diagMonitor(this.confirmBurnButton);
	}//java.awt.Point[x=1138,y=431]
	public void diagMonitor(Monitorable monitor) {
		ImgManager.saveMatImgDiag(ImgManager.convertToMat(BasicScreenGrabber.get().imgTarget(monitor.getTargetArea())), "TEST_AREA_IMG");
		System.out.println("Check PASSES="+monitor.check());
		monitor.traceWithMouse();
	}
	public boolean scriptLoop() {
		Long startTimeMs = System.currentTimeMillis();
		System.out.println((System.currentTimeMillis()-startTimeMs)+":LoopStart");
		//While inventory is not full, chop trees
		int lastLogOrEmptySlotIdx = 27;
		Monitorable lastEmpty = this.emptyInvinSlots.get(lastLogOrEmptySlotIdx);
		boolean lastEmptyIsEmpty = lastEmpty.check();
		boolean lastEmptyIsLog = lastEmpty.check(List.of(this.willowLog), true, null);
		while( lastEmptyIsEmpty && !lastEmptyIsLog) {
			//LastSlotNotLog
			if(!lastEmptyIsEmpty) {
				Sound.BELL.play();
				lastLogOrEmptySlotIdx-=1;
				lastEmptyIsEmpty = lastEmpty.check();
				lastEmptyIsLog = lastEmpty.check(List.of(this.willowLog), true, null);
				continue;
			}
//			DiagnosticsManager.get().diagnose=true;
			//TryChop Tree1
			System.out.println((System.currentTimeMillis()-startTimeMs)+":Try cut tree1");
			if( !this.tree1.doActionable() ) {
				System.out.println("Can't chop Tree1");
			}
			//TryChop Tree2
			System.out.println((System.currentTimeMillis()-startTimeMs)+":Try cut tree2");
			if( !this.tree2.doActionable() ) {
				System.out.println("Can't chop Tree2");
			}
//			DiagnosticsManager.get().diagnose=false;
			lastEmptyIsEmpty = lastEmpty.check();
			lastEmptyIsLog = lastEmpty.check(List.of(this.willowLog), true, null);
		}
		Sound.BELL.play();
		//If fire, feed fire, else build fire

		System.out.println((System.currentTimeMillis()-startTimeMs)+":Check if fire exists");
		if( !this.fire.check() ) {
			//Walk to position 2
			System.out.println((System.currentTimeMillis()-startTimeMs)+":Walk to pos 2");
			if(!this.moveToPos2.doActionable()) {
				System.out.println("Couldn't move to spot 2.");
				return false;
			}
			//StartFire
			System.out.println((System.currentTimeMillis()-startTimeMs)+":click willow logs,click Tinderbox,moveTopos1");
			if( !this.clickWillowLogsLightFire.doActionable() || !this.lightFireTinderBox.doActionable()) {
				System.out.println("Couldn't light fire");
				return false;
			}
		}
		// add logs fire
		System.out.println((System.currentTimeMillis()-startTimeMs)+":click willow logs to add to fire");
		DiagnosticsManager.get().diagnose=true;
		System.out.println((System.currentTimeMillis()-startTimeMs)+":click fire");
		if( !this.clickWillowLogsAddFire.doActionable() ) {
			System.out.println("Couldn't click logs to add to fire!");
			return false;
		}
		System.out.println((System.currentTimeMillis()-startTimeMs)+":click on fire to add logs");
		if( !this.clickFireAddLogs.doActionable() ) {
			System.out.println("Couldn't click fire to add logs!");
			return false;
		}
		System.out.println((System.currentTimeMillis()-startTimeMs)+":confirm add logs");
		if( !this.confirmAddWillowLogsFire.doActionable() ) {
			System.out.println("Couldn't click confirm to add logs to fire!");
			return false;
		}
		if( !this.waitForLastLogToBurn.doActionable() ) {
			System.out.println("Failed to wait for log to burn!");
			return false;
		}
			
		//Check all logs are gone
		DiagnosticsManager.get().diagnose=false;
		Sound.BELL.play();
		System.out.println((System.currentTimeMillis()-startTimeMs)+":LOOP FINISHED");
		return true;
	}
	
	public boolean doLoop(int numTimes) {
		int loopCounter = 0;
		for(; loopCounter<numTimes && this.scriptLoop(); loopCounter++) {}
		Sound.BELL.play();
		return loopCounter == numTimes;
	}
}
