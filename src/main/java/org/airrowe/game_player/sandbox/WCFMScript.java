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
	private Viewable willowLog;
	private Viewable willowLogSelected;
	private Viewable tinderBox;
	private Viewable fireMakingXpGainImg;
	private List<Viewable> tree1ImgRefs;
	private List<Viewable> tree2ImgRefs;
	private GameWArea fireGA;
	private GameWArea pos1IndGA;
	private GameWArea pos2IndGA;
	private GameWArea confirmBurnButtonGA;
	private GameWArea tree1Area;
	private GameWArea tree2Area;
	private GameWArea fireMakingXpGainGA;
	private Monitorable fire;
	private Monitorable Pos1Ind;
	private Monitorable Pos2Ind;
	private Monitorable confirmBurnButton;
	private Monitorable confirmBurnButtonDone;
	private Monitorable invinFilledWithLogs;
	private Monitorable fireMakingXpGainIndicator;
	private List<Monitorable> emptyInvinSlots;
	private List<Monitorable> willowLogInvinSlots;
	private List<Monitorable> selectedWillowLogInvinSlots;
	private MenuInvintory menuInvin;
	private MouseActionable tree1;
	private MouseActionable tree2;
	private MouseActionable moveToPos2;
	private MouseActionable clickWillowLogsLightFire;
	private MouseActionable clickWillowLogsAddFire;
	private MouseActionable clickFireAddLogs;
	private MouseActionable confirmAddWillowLogsFire;
	private MouseActionable confirmAddWillowLogsFireFast;
	private MouseActionable lightFireTinderBox;
	private WaitActionable waitForLastLogToBurn;
	private List<MouseActionable> clickWillowLogInvinSlotsFirst;
	private List<MouseActionable> clickWillowLogInvinSlotsRest;
	
	private List<Integer> nonLogItemInvinIdx = new ArrayList<Integer>();

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
		this.fireGA = new GameWArea(new Rectangle(666,344,25,25),false,false,null,gameBB);
		AreaManager.get().initializeGameWindowAreas(gameBB);
		this.fire = new Monitorable(
				this.fireGA,
				List.of(/*new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos1Fire1.bmp"),*/
						new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos1Fire2.bmp"),
						new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos1Fire3.bmp"),
						new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos1Fire4.bmp")),
				true,
				null);
		this.pos1IndGA = new GameWArea(new Rectangle(737,346,25,25),false,false,null,gameBB);
		this.Pos1Ind = new Monitorable(
				this.pos1IndGA,
				List.of(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos1Ind.bmp")),
				true,
				null);
		this.pos2IndGA = new GameWArea(new Rectangle(719,346,24,25),false,false,null,gameBB);
		this.Pos2Ind = new Monitorable(
				this.pos2IndGA,
				List.of(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos2Ind.bmp")),
				true,
				null);
		this.confirmBurnButtonGA = new GameWArea(new Rectangle(236,91,40,32),true,false,null,gameBB);
		this.confirmBurnButton = new Monitorable(
				this.confirmBurnButtonGA,
				List.of(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "confirmBurnButton.bmp")),
				true,
				null);
		this.confirmBurnButtonDone = new Monitorable(
				this.confirmBurnButtonGA,
				List.of(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "confirmBurnButton.bmp")),
				false,
				null);
		this.invinFilledWithLogs = new Monitorable(
				menuInvin.getInvinSlotArea(27),
				List.of(willowLog), 
				true, 
				null);
		this.tree1Area = new GameWArea(new Rectangle(656,293,29,29),false,false,null,gameBB);
		this.tree1ImgRefs = List.of(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "1Pos1Tree1.bmp"),
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
					this.tree1Area,
					this.tree1ImgRefs,
					true,
					null),
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(
					new Monitorable(
						this.tree1Area,
						this.tree1ImgRefs,
						false,
						null),
					this.invinFilledWithLogs),
				1000,
				120000,
				null,
				null);
		this.tree2Area = new GameWArea(new Rectangle(640,366,27,26),false,false,null,gameBB);
		this.tree2ImgRefs = List.of(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "1Pos1Tree2.bmp"),
				new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "2Pos1Tree2.bmp"),
				new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "3Pos1Tree2.bmp"),
				new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "4Pos1Tree2.bmp"),
				new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "5Pos1Tree2.bmp"),
				new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "6Pos1Tree2.bmp"));
		this.tree2 = new MouseActionable(
				new Monitorable(
						this.tree2Area,
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
					this.fireGA,
					List.of(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos1Fire1.bmp")),
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
						this.menuInvin.getInvinSlotArea(27),
						List.of(this.willowLog),
						true,
						null),
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(new Monitorable(
						this.menuInvin.getInvinSlotArea(27),
						List.of(this.willowLogSelected),
						true,
						null)),
				100,
				1000,
				null,
				null);
		this.lightFireTinderBox = new MouseActionable(
				new Monitorable(
						this.menuInvin.getInvinSlotArea(1),
						List.of(this.tinderBox),
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
						this.menuInvin.getInvinSlotArea(26),
						List.of(this.willowLog),
						true,
						null),
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(new Monitorable(
						this.menuInvin.getInvinSlotArea(26),
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
		this.clickFireAddLogs.msMonitorInterval=0;
		
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

		this.fireMakingXpGainImg = new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "fireXpInd.bmp");
		this.fireMakingXpGainGA = new GameWArea(new Rectangle(301,37,19,185),false,true,null,gameBB);
		this.fireMakingXpGainIndicator = new Monitorable(this.fireMakingXpGainGA, List.of(this.fireMakingXpGainImg), true, null);
		this.confirmAddWillowLogsFireFast = new MouseActionable(
				this.confirmBurnButton,
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(this.confirmBurnButtonDone),
				0,
				10000,
				null,
				null);
		this.confirmAddWillowLogsFireFast.msMonitorInterval=0;
		this.waitForLastLogToBurn = new WaitActionable(Action.WAIT, 20000, List.of(this.emptyInvinSlots.get(27)), null);
		
		//WillowLogs
		this.willowLogInvinSlots = new ArrayList<Monitorable>();
		this.selectedWillowLogInvinSlots = new ArrayList<Monitorable>();
		for(int i=2; i<28; ++i) {
			this.willowLogInvinSlots.add(new Monitorable(
					menuInvin.getInvinSlotArea(i), 
					List.of(this.willowLog), 
					true, 
					null));
			this.selectedWillowLogInvinSlots.add(new Monitorable(
					menuInvin.getInvinSlotArea(i), 
					List.of(this.willowLogSelected), 
					true, 
					null));			
		}
		this.clickWillowLogInvinSlotsFirst = new ArrayList<MouseActionable>();
		this.clickWillowLogInvinSlotsRest = new ArrayList<MouseActionable>();
		for(int i=0; i<this.willowLogInvinSlots.size(); ++i) {
			this.clickWillowLogInvinSlotsFirst.add(new MouseActionable(
					this.willowLogInvinSlots.get(i), 
					Action.MOUSE_LEFT_CLICK, 
					null, 
					null, 
					null, 
					List.of(this.selectedWillowLogInvinSlots.get(i)), 
					0, 
					10000, 
					null, 
					null));		
			this.clickWillowLogInvinSlotsRest.add(new MouseActionable(
					this.willowLogInvinSlots.get(i), 
					Action.MOUSE_LEFT_CLICK, 
					null, 
					null, 
					null, 
					List.of(this.fireMakingXpGainIndicator), 
					0, 
					10000, 
					null, 
					null));		
		}
		AreaManager.get().initializeGameWindowAreas(gameBB);
	}
	public void testArea() {
		diagMonitor(this.fireMakingXpGainIndicator);
	}
	public void diagMonitor(Monitorable monitor) {
		ImgManager.saveMatToFile(ImgManager.bufferedImageBGRToMatBGR(BasicScreenGrabber.get().imgTarget(monitor.getTargetArea())),null, "TEST_AREA_IMG");
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
				Sound.BIKE_BELL.play();
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
		Sound.BIKE_BELL.play();
		//If fire, feed fire, else build fire
		DiagnosticsManager.get().diagnose=true;
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
		DiagnosticsManager.get().diagnose=false;
		// add logs fire
//		System.out.println((System.currentTimeMillis()-startTimeMs)+":click willow logs to add to fire");
//		if( !this.clickWillowLogsAddFire.doActionable() ) {
//			System.out.println("Couldn't click logs to add to fire!");
//			return false;
//		}
//		System.out.println((System.currentTimeMillis()-startTimeMs)+":click on fire to add logs");
//		if( !this.clickFireAddLogs.doActionable() ) {
//			System.out.println("Couldn't click fire to add logs!");
//			return false;
//		}
//		System.out.println((System.currentTimeMillis()-startTimeMs)+":confirm add logs");
//		if( !this.confirmAddWillowLogsFire.doActionable() ) {
//			System.out.println("Couldn't click confirm to add logs to fire!");
//			return false;
//		}
//		System.out.println((System.currentTimeMillis()-startTimeMs)+":wait for last log to burn");
//		if( !this.waitForLastLogToBurn.doActionable() ) {
//			System.out.println("Failed to wait for log to burn!");
//			return false;
//		}
		
		this.nonLogItemInvinIdx.clear();
		boolean first = true;
		for(int i=0, j=2; j<28; ++i, ++j ) {
			System.out.println("INVIN_IDX:"+j);
			//Click log
			System.out.println((System.currentTimeMillis()-startTimeMs)+":click on willow logs to add to fire");
			if( first ) {
				if( !this.clickWillowLogInvinSlotsFirst.get(i).doActionable() ) {
					System.out.println("NO willow log to select & burn in invin slot:"+(i+3));
					if( this.emptyInvinSlots.get(j).check() ) {
						this.nonLogItemInvinIdx.add(j);
					}
					continue;
				}
				first = false;
			} else {
				if( !this.clickWillowLogInvinSlotsRest.get(i).doActionable() ) {
					System.out.println("NO willow log to select & burn in invin slot:"+(i+3));
					if( this.emptyInvinSlots.get(j).check() ) {
						this.nonLogItemInvinIdx.add(j);
					}
					continue;
				}
			}
			//add to fire
			System.out.println((System.currentTimeMillis()-startTimeMs)+":click on fire to add logs");
			if( !this.clickFireAddLogs.doActionable() ) {
				System.out.println("Couldn't click fire to add logs!");
				//Count remaining logs
				int remainingLogs = 0;
				for(Monitorable willowLogInvin : this.willowLogInvinSlots) {
					if( willowLogInvin.check() ) {
						remainingLogs++;
					}
				}
				if( remainingLogs <= 1) {
					break;
				}
				return false;
			}
			if( j>=27 ) {
				break;
			}
			//confirmAdd
			System.out.println((System.currentTimeMillis()-startTimeMs)+":confirm Add to fire.");
			if( !this.confirmAddWillowLogsFireFast.doActionable() ) {
				System.out.println("Couldn't confirm to burn logs");
				return false;
			}
		}

		//Wait for last log
		System.out.println((System.currentTimeMillis()-startTimeMs)+":Wait for last log to finish.");
		if( !this.waitForLastLogToBurn.doActionable() ) {
			System.out.println("Failed to wait for last log to burn.");
			return false;
		}
			
		if( this.nonLogItemInvinIdx.size()>0 ) {
			for(int i=0; i<this.nonLogItemInvinIdx.size(); ++i) {
				System.out.println("UnexpectedInvinItem: SLOT "+i);
			}
		}
		//Check all logs are gone
		Sound.BIKE_BELL.play();
		System.out.println((System.currentTimeMillis()-startTimeMs)+":LOOP FINISHED");
		return true;
	}
	
	public boolean doLoop(int numTimes) {
		int loopCounter = 0;
		for(; loopCounter<numTimes && this.scriptLoop(); loopCounter++) {}
		Sound.BIKE_BELL.play();
		return loopCounter == numTimes;
	}
}
