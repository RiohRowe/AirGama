package org.airrowe.game_player.sandbox;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.airrowe.game_player.ResourceFolder;
import org.airrowe.game_player.Sound;
import org.airrowe.game_player.image_grabbing.BasicScreenGrabber;
import org.airrowe.game_player.image_grabbing.GameWindow;
import org.airrowe.game_player.image_grabbing.ImgManager;
import org.airrowe.game_player.script_runner.Monitorable;
import org.airrowe.game_player.script_runner.Viewable;
import org.airrowe.game_player.script_runner.actions.Action;
import org.airrowe.game_player.script_runner.actions.MouseActionable;
import org.airrowe.game_player.script_runner.areas.Area;
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
	WCFMScript(){
		Rectangle gameBB = GameWindow.getGameWindow().getGameBox();
		GameWArea fireGA = new GameWArea(new Rectangle(669,351,25,23),false,false,null,gameBB);
		this.fire = new Monitorable(
				fireGA,
				new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos1Fire.bmp"),
				true,
				null);
		GameWArea pos1IndGA = new GameWArea(new Rectangle(737,346,25,25),false,false,null,gameBB);
		this.Pos1Ind = new Monitorable(
				pos1IndGA,
				new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos1Ind.bmp"),
				true,
				null);
		GameWArea pos2IndGA = new GameWArea(new Rectangle(719,346,24,25),false,false,null,gameBB);
		this.Pos2Ind = new Monitorable(
				pos2IndGA,
				new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos2Ind.bmp"),
				true,
				null);
		GameWArea confirmBurnButtonGA = new GameWArea(new Rectangle(236,68,40,32),true,false,null,gameBB);
		this.confirmBurnButton = new Monitorable(
				confirmBurnButtonGA,
				new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "confirmBurnButton.bmp"),
				true,
				null);
		this.willowLog = new Viewable(ResourceFolder.GAME_INVIN_REF_IMGS, "WillowLog.bmp");
		this.willowLogSelected = new Viewable(ResourceFolder.GAME_INVIN_REF_IMGS, "WillowLogSelected.bmp");
		this.tinderBox = new Viewable(ResourceFolder.GAME_INVIN_REF_IMGS, "TinderBox.bmp");
		this.menuInvin = MenuInvintory.get(gameBB);
		this.emptyInvinSlots = new ArrayList<Monitorable>();
		for( int i=0; i<28; ++i) {
			this.emptyInvinSlots.add(new Monitorable(
				this.menuInvin.getInvinSlotArea(i),
				new Viewable(ResourceFolder.GAME_INVIN_REF_IMGS, (i+1)+"-empty.bmp"),
				true,
				null));
		}
		GameWArea tree1Area = new GameWArea(new Rectangle(656,293,29,29),false,false,null,gameBB);
		Viewable tree1ImgRef = new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos1Tree1.bmp");
		this.tree1 = new MouseActionable(
				new Monitorable(
					tree1Area,
					tree1ImgRef,
					true,
					null),
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(
					new Monitorable(
						tree1Area,
						tree1ImgRef,
						false,
						null),
					invinFilledWithLogs),
				1000,
				120000,
				null);
		GameWArea tree2Area = new GameWArea(new Rectangle(640,366,27,26),false,false,null,gameBB);
		Viewable tree2ImgRef = new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos1Tree2.bmp");
		this.tree2 = new MouseActionable(
				new Monitorable(
						tree2Area,
						tree2ImgRef,
						true,
						null),
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(
					new Monitorable(
						tree2Area,
						tree2ImgRef,
						false,
						null),
					this.invinFilledWithLogs),
				1000,
				120000,
				null);
		
		this.moveToPos2 = new MouseActionable(
				new Monitorable(
					fireGA,
					new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "Pos1Fire.bmp"),
					false,
					null),
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(this.Pos2Ind),
				100,
				3000,
				null);

		this.clickWillowLogsLightFire = new MouseActionable(
				new Monitorable(
						menuInvin.getInvinSlotArea(27),
						willowLog,
						true,
						null),
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(new Monitorable(
						menuInvin.getInvinSlotArea(27),
						this.willowLogSelected,
						true,
						null)),
				100,
				1000,
				null);
		this.lightFireTinderBox = new MouseActionable(
				new Monitorable(
						menuInvin.getInvinSlotArea(1),
						tinderBox,
						true,
						null),
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(this.Pos1Ind),
				500,
				10000,
				null);
		
		this.clickWillowLogsAddFire = new MouseActionable(
				new Monitorable(
						menuInvin.getInvinSlotArea(26),
						willowLog,
						true,
						null),
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(new Monitorable(
						menuInvin.getInvinSlotArea(26),
						this.willowLogSelected,
						true,
						null)),
				100,
				1000,
				null);
		
		this.clickFireAddLogs = new MouseActionable(
				this.fire,
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(this.confirmBurnButton),
				0,
				1000,
				null);
		
		this.confirmAddWillowLogsFire = new MouseActionable(
				this.confirmBurnButton,
				Action.MOUSE_LEFT_CLICK,
				null,
				null,
				null,
				List.of(this.emptyInvinSlots.get(26)),
				130000,
				140000,
				null);
		fireGA.calcCurrentArea(gameBB);
		pos1IndGA.calcCurrentArea(gameBB);
		pos2IndGA.calcCurrentArea(gameBB);
		confirmBurnButtonGA.calcCurrentArea(gameBB);
		tree1Area.calcCurrentArea(gameBB);
		tree2Area.calcCurrentArea(gameBB);
		ImgManager.saveMatImgDiag(ImgManager.convertToMat(BasicScreenGrabber.get().imgTarget(gameBB)), "GAME_BOUNDING_BOX");
	}
	public void testArea() {
//		new Monitorable(new Area( new Rectangle(1138,431,228,297),false,false,),tinderBox,false,null).traceWithMouse();
		new Monitorable(this.menuInvin.getInvinArea(), tinderBox, false, null).traceWithMouse();
	}//java.awt.Point[x=1138,y=431]
	public boolean scriptLoop() {
		//While inventory is not full, chop trees
		int lastLogOrEmptySlotIdx = 27;
		Monitorable lastEmpty = this.emptyInvinSlots.get(lastLogOrEmptySlotIdx);
		boolean lastEmptyIsEmpty = lastEmpty.check();
		boolean lastEmptyIsLog = lastEmpty.check(this.willowLog, true, null);
		while( lastEmptyIsEmpty && !lastEmptyIsLog) {
			//LastSlotNotLog
			if(!lastEmptyIsEmpty) {
				Sound.BELL.play();
				lastLogOrEmptySlotIdx-=1;
				lastEmptyIsEmpty = lastEmpty.check();
				lastEmptyIsLog = lastEmpty.check(this.willowLog, true, null);
				continue;
			}
			//TryChop Tree1
			if( !this.tree1.doActionable() ) {
				System.out.println("Can't chop Tree1");
			}
			//TryChop Tree2
			if( !this.tree2.doActionable() ) {
				System.out.println("Can't chop Tree2");
			}
			lastEmptyIsEmpty = lastEmpty.check();
			lastEmptyIsLog = lastEmpty.check(this.willowLog, true, null);
		}
		
		//If fire, feed fire, else build fire
		if( !this.fire.check() ) {
			//Walk to position 2
			if(!this.moveToPos2.doActionable()) {
				System.out.println("Couldn't move to spot 2.");
				return false;
			}
			//StartFire
			if( !this.clickWillowLogsLightFire.doActionable() || !this.lightFireTinderBox.doActionable()) {
				System.out.println("Couldn't light fire");
				return false;
			}
		}
		// add logs fire
		if( !this.clickWillowLogsAddFire.doActionable() || !this.clickFireAddLogs.doActionable() || !this.confirmAddWillowLogsFire.doActionable()) {
			System.out.println("Couldn't add logs to fire!");
			return false;
		}
		return true;
	}
	
	public boolean doLoop(int numTimes) {
		int loopCounter = 0;
		for(; loopCounter<numTimes && this.scriptLoop(); loopCounter++) {}
		Sound.BELL.play();
		return loopCounter == numTimes;
	}
}
