package org.airrowe.game_player.sandbox;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.airrowe.game_player.diag.DiagnosticsManager;
import org.airrowe.game_player.file_management.ResourceFolder;
import org.airrowe.game_player.file_management.Sound;
import org.airrowe.game_player.image_grabbing.BasicScreenGrabber;
import org.airrowe.game_player.image_grabbing.GameWindow;
import org.airrowe.game_player.image_grabbing.ImgManager;
import org.airrowe.game_player.input_emulation.Mouse;
import org.airrowe.game_player.script_runner.Monitorable;
import org.airrowe.game_player.script_runner.actions.Action;
import org.airrowe.game_player.script_runner.actions.MouseActionable;
import org.airrowe.game_player.script_runner.actions.WaitActionable;
import org.airrowe.game_player.script_runner.areas.Area;
import org.airrowe.game_player.script_runner.areas.AreaManager;
import org.airrowe.game_player.script_runner.areas.GameWArea;
import org.airrowe.game_player.script_runner.viewables.Viewable;

public class YewWCFMScript1 {
	private Viewable yewLog_invin_img;
	private Viewable yewLog_invin_selected_img;
	private Viewable tinderBox_invin_img;
	private Viewable fireMakingXpGainImg;
	private Viewable confirmBurnButtonImgRef;
	private List<Viewable> Pos1Tree1ImgRefs;
	private List<Viewable> Pos2Tree1ImgRefs;
	private List<Viewable> PosFaTree1ImgRefs;
	private List<Viewable> Pos1Tree2ImgRefs;
	private List<Viewable> Pos2Tree2ImgRefs;
	private List<Viewable> PosFaTree2ImgRefs;
	private List<Viewable> Pos1FireImgRefs;
	private List<Viewable> Pos2FireImgRefs;
	private List<Viewable> PosFaFireImgRefs;
	private List<Viewable> Pos1FaImgRefs;
	private List<Viewable> Pos2FaImgRefs;
	private List<Viewable> Pos1IndImgRefs;
	private List<Viewable> Pos2IndImgRefs;
	private List<Viewable> PosFaIndImgRefs;
	private List<Viewable> PosFIndImgRefs;
	
	private GameWArea confirmBurnButtonGA;
	private GameWArea fireMakingXpGainGA;
	private GameWArea pos1IndGA;
	private GameWArea pos2IndGA;
	private GameWArea posFaIndGA;
	private GameWArea posFIndGA;
	private GameWArea[] fireGA;//0:pos1, 1:pos2, 2:posFa
	private GameWArea[] tree1Area;//0:pos1, 1:pos2, 2:posFa
	private GameWArea[] tree2Area;//0:pos1, 1:pos2, 2:posFa
	private GameWArea[] pos1_pos;//0:pos2, 1:posF, 2:posFa
	private GameWArea[] pos2_pos;//0:pos1, 1:posF, 2:posFa
	private GameWArea[] posfa_pos;//0:pos1, 1:pos2, 2:posF

	private Monitorable tinderboxMonitor;
	private Monitorable Pos1Ind;
	private Monitorable Pos2Ind;
	private Monitorable PosFInd;
	private Monitorable PosFaInd;
	private Monitorable confirmBurnButton;
	private Monitorable confirmBurnButtonDone;
	private Monitorable invinFilledWithLogs;
	private Monitorable fireMakingXpGainIndicator;
	private Monitorable[] fire;//0:pos1,1:pos2,2:posFa
	private Monitorable[] tree1Monitors;//0:pos1,1:pos2,2:posFa
	private Monitorable[] tree1DoneMonitors;//0:pos1,1:pos2,2:posFa
	private Monitorable[] tree2Monitors;//0:pos1,1:pos2,2:posFa
	private Monitorable[] tree2DoneMonitors;//0:pos1,1:pos2,2:posFa
	private Monitorable[] pos1_posMonitors;//0:pos2, 1:posF, 2:posFa
	private Monitorable[] pos2_posMonitors;//0:pos1, 1:posF, 2:posFa
	private Monitorable[] posfa_posMonitors;//0:pos1, 1:pos2, 2:posF
	private List<Monitorable> emptyInvinSlots;
	private List<Monitorable> yewLogInvinSlots;
	private List<Monitorable> selectedYewLogInvinSlots;
	
	private MenuInvintory menuInvin;
	
	private MouseActionable clickYewLogs_INVIN_LightFire;
	private MouseActionable clickYewLogs_INVIN_AddFire;
	private MouseActionable clickFireAddLogs;
	private MouseActionable confirmAddYewLogsFire;
	private MouseActionable confirmAddYewLogsFireFast;
	private MouseActionable lightFireTinderBox;
	private WaitActionable waitForLastLogToBurn;
	private MouseActionable[] tree1;
	private MouseActionable[] tree2;
	private MouseActionable[] moveToPos1;//from 0:pos2, 1:posFa
	private MouseActionable[] moveToPos2;//from 0:pos1, 1:posFa
	private MouseActionable[] moveToPosF;//from 0:pos1, 2:pos2, 3:posFa
	private MouseActionable[] moveToPosFa;//from 0:pos1, 2:pos2
	private List<MouseActionable> clickYewLogInvinSlotsFirst;
	private List<MouseActionable> clickYewLogInvinSlotsRest;
	
	private List<Integer> nonLogItemInvinIdx = new ArrayList<Integer>();
	private int pos=1;//1=1,2=2,3=f,4=fa

	YewWCFMScript1(){
		Mouse.get().setDefaultActionPrepositionDelay(100);
		Rectangle gameBB = GameWindow.getGameWindow().getGameBox();
		
		this.menuInvin = MenuInvintory.get(gameBB);

		this.yewLog_invin_img= new Viewable(ResourceFolder.GAME_INVIN_REF_IMGS, "YewLog.bmp");
		this.yewLog_invin_selected_img = new Viewable(ResourceFolder.GAME_INVIN_REF_IMGS, "YewLogSelected.bmp");
		this.tinderBox_invin_img = new Viewable(ResourceFolder.GAME_INVIN_REF_IMGS, "TinderBox.bmp");
		this.fireMakingXpGainImg = new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "fireXpInd.bmp");
		this.confirmBurnButtonImgRef = new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, "confirmBurnButtonYew.bmp");
		String baseFnP1T1 = "pos1Tree1RefImg";
		this.Pos1Tree1ImgRefs = new ArrayList<Viewable>();
		for(int i=0; i<3; ++i) {
			this.Pos1Tree1ImgRefs.add(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, baseFnP1T1+"-"+(i+1)+".bmp"));
		}
		String baseFnP2T1 = "pos2Tree1RefImg";
		this.Pos2Tree1ImgRefs = new ArrayList<Viewable>();
		for(int i=0; i<8; ++i) {
			this.Pos2Tree1ImgRefs.add(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, baseFnP2T1+"-"+(i+1)+".bmp"));
		}
		String baseFnPfaT1 = "posfaTree1RefImg";
		this.PosFaTree1ImgRefs = new ArrayList<Viewable>();
		for(int i=0; i<3; ++i) {
			this.PosFaTree1ImgRefs.add(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, baseFnPfaT1+"-"+(i+1)+".bmp"));
		}
		String baseFnP1T2 = "pos1Tree2RefImg";
		this.Pos1Tree2ImgRefs = new ArrayList<Viewable>();
		for(int i=0; i<3; ++i) {
			this.Pos1Tree2ImgRefs.add(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, baseFnP1T2+"-"+(i+1)+".bmp"));
		}
		String baseFnP2T2 = "pos2Tree2RefImg";
		this.Pos2Tree2ImgRefs = new ArrayList<Viewable>();
		for(int i=0; i<8; ++i) {
			this.Pos2Tree2ImgRefs.add(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, baseFnP2T2+"-"+(i+1)+".bmp"));
		}
		String baseFnPFaT2 = "posFaTree2RefImg";
		this.PosFaTree2ImgRefs = new ArrayList<Viewable>();
		for(int i=0; i<3; ++i) {
			this.PosFaTree2ImgRefs.add(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, baseFnPFaT2+"-"+(i+1)+".bmp"));
		}
		String baseFnP1F = "pos1FireRefImg";
		this.Pos1FireImgRefs = new ArrayList<Viewable>();
		for(int i=0; i<2; ++i) {
			this.Pos1FireImgRefs.add(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, baseFnP1F+"-"+(i+1)+".png"));
		}
		String baseFnP2F = "pos2FireRefImg";
		this.Pos2FireImgRefs = new ArrayList<Viewable>();
		for(int i=0; i<2; ++i) {
			this.Pos2FireImgRefs.add(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, baseFnP2F+"-"+(i+1)+".png"));
		}
		String baseFnPFaF = "posFaFireRefImg";
		this.PosFaFireImgRefs = new ArrayList<Viewable>();
		for(int i=0; i<3; ++i) {
			this.PosFaFireImgRefs.add(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, baseFnPFaF+"-"+(i+1)+".png"));
		}
		String baseFnP1Fa = "pos1FaRefImg";
		this.Pos1FaImgRefs = new ArrayList<Viewable>();
		for(int i=0; i<0; ++i) {
			this.Pos1FaImgRefs.add(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, baseFnP1Fa+"-"+(i+1)+".bmp"));
		}
		String baseFnP2Fa = "pos2FaRefImg";
		this.Pos2FaImgRefs = new ArrayList<Viewable>();
		for(int i=0; i<0; ++i) {
			this.Pos2FaImgRefs.add(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, baseFnP2Fa+"-"+(i+1)+".bmp"));
		}
		String baseFnP1Ind = "pos1IndRefImg";
		this.Pos1IndImgRefs = new ArrayList<Viewable>();
		for(int i=0; i<2; ++i) {
			this.Pos1IndImgRefs.add(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, baseFnP1Ind+"-"+(i+1)+".bmp"));
		}
		String baseFnP2Ind = "pos2IndRefImg";
		this.Pos2IndImgRefs = new ArrayList<Viewable>();
		for(int i=0; i<4; ++i) {
			this.Pos2IndImgRefs.add(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, baseFnP2Ind+"-"+(i+1)+".bmp"));
		}
		String baseFnPfaInd = "posFaIndRefImg";
		this.PosFaIndImgRefs = new ArrayList<Viewable>();
		for(int i=0; i<3; ++i) {
			this.PosFaIndImgRefs.add(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, baseFnPfaInd+"-"+(i+1)+".bmp"));
		}
		String baseFnPfInd = "posFIndRefImg";
		this.PosFIndImgRefs = new ArrayList<Viewable>();
		for(int i=0; i<1; ++i) {
			this.PosFIndImgRefs.add(new Viewable(ResourceFolder.GAME_WORLD_REF_IMGS, baseFnPfInd+"-"+(i+1)+".bmp"));
		}
		
		
		
		
		
		
		this.confirmBurnButtonGA = new GameWArea(new Rectangle(236,91,40,32),true,false,null,gameBB);
		this.fireMakingXpGainGA = new GameWArea(new Rectangle(301,37,19,185),false,true,null,gameBB);
		this.pos1IndGA = new GameWArea(new Rectangle(672,286,25,25),false,false,null,gameBB);
		this.pos2IndGA = new GameWArea(new Rectangle(739,268,25,25),false,false,null,gameBB);
		this.posFaIndGA = new GameWArea(new Rectangle(716,322,25,25),false,false,null,gameBB);
		this.posFIndGA = new GameWArea(new Rectangle(699,322,25,25),false,false,null,gameBB);
		this.fireGA = new GameWArea[] {//0:pos1, 1:pos2, 2:posFa
				new GameWArea(new Rectangle(711,303,25,25),false,false,null,gameBB),
				new GameWArea(new Rectangle(694,286,25,25),false,false,null,gameBB),
				new GameWArea(new Rectangle(669,340,25,25),false,false,null,gameBB)};
		this.tree1Area = new GameWArea[] {//0:pos1, 1:pos2, 2:posFa
				new GameWArea(new Rectangle(609,303,25,25),false,false,null,gameBB),
				new GameWArea(new Rectangle(589,280,25,25),false,false,null,gameBB),
				new GameWArea(new Rectangle(558,342,25,25),false,false,null,gameBB)};
		this.tree2Area = new GameWArea[] {//0:pos1, 1:pos2, 2:posFa
				new GameWArea(new Rectangle(713,393,25,25),false,false,null,gameBB),
				new GameWArea(new Rectangle(687,377,25,25),false,false,null,gameBB),
				new GameWArea(new Rectangle(654,448,25,25),false,false,null,gameBB)};
		this.pos1_pos = new GameWArea[] {//0:pos2, 1:posF, 2:posFa
				new GameWArea(new Rectangle(669,369,25,25),false,false,null,gameBB),
				new GameWArea(new Rectangle(711,303,25,25),false,false,null,gameBB),
				new GameWArea(new Rectangle(689,303,25,25),false,false,null,gameBB)};
		this.pos2_pos = new GameWArea[] {//0:pos1, 1:posF, 2:posFa
				new GameWArea(new Rectangle(630,327,25,25),false,false,null,gameBB),
				new GameWArea(new Rectangle(694,286,25,25),false,false,null,gameBB),
				new GameWArea(new Rectangle(673,286,25,25),false,false,null,gameBB)};
		this.posfa_pos = new GameWArea[] {//0:pos1, 1:pos2, 2:posF
				new GameWArea(new Rectangle(601,384,25,25),false,false,null,gameBB),
				new GameWArea(new Rectangle(624,407,25,25),false,false,null,gameBB),
				new GameWArea(new Rectangle(669,340,25,25),false,false,null,gameBB)};

		
		
		

		this.tinderboxMonitor = new Monitorable(this.menuInvin.getInvinSlotArea(1), List.of(this.tinderBox_invin_img), true, null);
		this.Pos1Ind = new Monitorable(this.pos1IndGA, this.Pos1IndImgRefs, true, null);
		this.Pos2Ind = new Monitorable(this.pos2IndGA, this.Pos2IndImgRefs, true, null);
		this.PosFInd = new Monitorable(this.posFIndGA, this.PosFIndImgRefs, true, null);
		this.PosFaInd = new Monitorable(this.posFaIndGA, this.PosFaIndImgRefs, true, null);
		this.confirmBurnButton = new Monitorable(this.confirmBurnButtonGA, List.of(this.confirmBurnButtonImgRef), true, null);
		this.confirmBurnButtonDone = new Monitorable(this.confirmBurnButtonGA, List.of(this.confirmBurnButtonImgRef), false, null);
		this.invinFilledWithLogs = new Monitorable(menuInvin.getInvinSlotArea(27), List.of(this.yewLog_invin_img), true, null);
		this.fireMakingXpGainIndicator = new Monitorable(this.fireMakingXpGainGA, List.of(this.fireMakingXpGainImg), true, null);
		this.fire = new Monitorable[] {//0:pos1,1:pos2,2:posFa
				new Monitorable(this.fireGA[0], this.Pos1FireImgRefs, true, 0.98),
				new Monitorable(this.fireGA[1], this.Pos2FireImgRefs, true, 0.98),
				new Monitorable(this.fireGA[2], this.PosFaFireImgRefs, true, 0.98)
		};
		this.tree1Monitors = new Monitorable[] {//0:pos1,1:pos2,2:posFa
				new Monitorable(this.tree1Area[0], this.Pos1Tree1ImgRefs, true, null),
				new Monitorable(this.tree1Area[1], this.Pos2Tree1ImgRefs, true, null),
				new Monitorable(this.tree1Area[2], this.PosFaTree1ImgRefs, true, null)
		};
		this.tree1DoneMonitors = new Monitorable[] {//0:pos1,1:pos2,2:posFa
				new Monitorable(this.tree1Area[0], this.Pos1Tree1ImgRefs, false, null),
				new Monitorable(this.tree1Area[1], this.Pos2Tree1ImgRefs, false, null),
				new Monitorable(this.tree1Area[2], this.PosFaTree1ImgRefs, false, null)
		};
		this.tree2Monitors = new Monitorable[] {//0:pos1,1:pos2,2:posFa
				new Monitorable(this.tree2Area[0], this.Pos1Tree2ImgRefs, true, null),
				new Monitorable(this.tree2Area[1], this.Pos2Tree2ImgRefs, true, null),
				new Monitorable(this.tree2Area[2], this.PosFaTree2ImgRefs, true, null)
		};
		this.tree2DoneMonitors = new Monitorable[] {//0:pos1,1:pos2,2:posFa
				new Monitorable(this.tree2Area[0], this.Pos1Tree2ImgRefs, false, null),
				new Monitorable(this.tree2Area[1], this.Pos2Tree2ImgRefs, false, null),
				new Monitorable(this.tree2Area[2], this.PosFaTree2ImgRefs, false, null)
		};
		this.pos1_posMonitors = new Monitorable[] {//0:pos2, 1:posF, 2:posFa
				new Monitorable(this.pos1_pos[0], List.of(), true, null),
				new Monitorable(this.pos1_pos[1], List.of(), true, null),
				new Monitorable(this.pos1_pos[2], List.of(), true, null)
		};
		this.pos2_posMonitors = new Monitorable[] {//0:pos1, 1:posF, 2:posFa
				new Monitorable(this.pos2_pos[0], List.of(), true, null),
				new Monitorable(this.pos2_pos[1], List.of(), true, null),
				new Monitorable(this.pos2_pos[2], List.of(), true, null)
		};
		this.posfa_posMonitors = new Monitorable[] {//0:pos1, 1:pos2, 2:posF
				new Monitorable(this.posfa_pos[0], List.of(), true, null),
				new Monitorable(this.posfa_pos[1], List.of(), true, null),
				new Monitorable(this.posfa_pos[2], List.of(), true, null)
		};
		this.emptyInvinSlots = new ArrayList<Monitorable>();
		for( int i=0; i<28; ++i) {
			this.emptyInvinSlots.add(new Monitorable(
				this.menuInvin.getInvinSlotArea(i),
				List.of(new Viewable(ResourceFolder.GAME_INVIN_REF_IMGS, (i+1)+"-empty.bmp")),
				true,
				null));
		}
		this.yewLogInvinSlots = new ArrayList<Monitorable>();
		this.selectedYewLogInvinSlots = new ArrayList<Monitorable>();
		for(int i=2; i<28; ++i) {
			this.yewLogInvinSlots.add(new Monitorable(
					menuInvin.getInvinSlotArea(i), 
					List.of(this.yewLog_invin_img), 
					true, 
					null));
			this.selectedYewLogInvinSlots.add(new Monitorable(
					menuInvin.getInvinSlotArea(i), 
					List.of(this.yewLog_invin_selected_img), 
					true, 
					null));			
		}
		
		
		
		
		

		this.clickYewLogs_INVIN_LightFire = new MouseActionable(
				this.yewLogInvinSlots.get(25), //target monitor
				Action.MOUSE_LEFT_CLICK, //action 
				null, //scroll amount
				null, //started monitors
				null, //progress monitors 
				List.of(this.selectedYewLogInvinSlots.get(25)), //finished indicators
				0, //ms before finished check
				10000, //ms before give up looking for finished ACTION_TIMEOUT
				null, //next action 
				null);//delay between mouse move and action execute
		this.clickYewLogs_INVIN_AddFire = new MouseActionable(
				this.yewLogInvinSlots.get(24), //target monitor
				Action.MOUSE_LEFT_CLICK, //action 
				null, //scroll amount
				null, //started monitors
				null, //progress monitors 
				List.of(this.selectedYewLogInvinSlots.get(24)), //finished indicators
				0, //ms before finished check
				10000, //ms before give up looking for finished ACTION_TIMEOUT
				null, //next action 
				null);//delay between mouse move and action execute
		this.clickFireAddLogs = new MouseActionable(
				this.fire[2], //target monitor
				Action.MOUSE_LEFT_CLICK, //action 
				null, //scroll amount
				null, //started monitors
				null, //progress monitors 
				List.of(this.confirmBurnButton), //finished indicators
				0, //ms before finished check
				10000, //ms before give up looking for finished ACTION_TIMEOUT
				null, //next action 
				null);//delay between mouse move and action execute
		this.confirmAddYewLogsFire = new MouseActionable(
				this.confirmBurnButton, //target monitor
				Action.MOUSE_LEFT_CLICK, //action 
				null, //scroll amount
				null, //started monitors
				null, //progress monitors 
				List.of(this.emptyInvinSlots.get(26)), //finished indicators
				1000, //ms before finished check
				150000, //ms before give up looking for finished ACTION_TIMEOUT
				null, //next action 
				1000);//delay between mouse move and action execute
		this.confirmAddYewLogsFireFast = new MouseActionable(
				this.confirmBurnButton, //target monitor
				Action.MOUSE_LEFT_CLICK, //action 
				null, //scroll amount
				null, //started monitors
				null, //progress monitors 
				List.of(this.confirmBurnButtonDone), //finished indicators
				0, //ms before finished check
				10000, //ms before give up looking for finished ACTION_TIMEOUT
				null, //next action 
				null);//delay between mouse move and action execute
		this.lightFireTinderBox = new MouseActionable(
				this.tinderboxMonitor, //target monitor
				Action.MOUSE_LEFT_CLICK, //action 
				null, //scroll amount
				null, //started monitors
				null, //progress monitors 
				List.of(this.PosFaInd), //finished indicators
				0, //ms before finished check
				15000, //ms before give up looking for finished ACTION_TIMEOUT
				null, //next action 
				null);//delay between mouse move and action execute
		this.waitForLastLogToBurn = new WaitActionable(Action.WAIT, 20000, List.of(this.emptyInvinSlots.get(27)), null);
		this.tree1 = new MouseActionable[] {
				new MouseActionable(
						this.tree1Monitors[0], //target monitor
						Action.MOUSE_LEFT_CLICK, //action 
						null, //scroll amount
						null, //started monitors
						null, //progress monitors 
						List.of(this.tree1DoneMonitors[0], this.invinFilledWithLogs), //finished indicators
						3000, //ms before finished check
						120000, //ms before give up looking for finished ACTION_TIMEOUT
						null, //next action 
						null),//delay between mouse move and action execute
				new MouseActionable(
						this.tree1Monitors[1], //target monitor
						Action.MOUSE_LEFT_CLICK, //action 
						null, //scroll amount
						null, //started monitors
						null, //progress monitors 
						List.of(this.tree1DoneMonitors[0], this.invinFilledWithLogs), //finished indicators
						3000, //ms before finished check
						120000, //ms before give up looking for finished ACTION_TIMEOUT
						null, //next action 
						null),//delay between mouse move and action execute
				new MouseActionable(
						this.tree1Monitors[2], //target monitor
						Action.MOUSE_LEFT_CLICK, //action 
						null, //scroll amount
						null, //started monitors
						null, //progress monitors 
						List.of(this.tree1DoneMonitors[0], this.invinFilledWithLogs), //finished indicators
						3000, //ms before finished check
						120000, //ms before give up looking for finished ACTION_TIMEOUT
						null, //next action 
						null)//delay between mouse move and action execute
		};
		this.tree2 = new MouseActionable[] {
				new MouseActionable(
						this.tree2Monitors[0], //target monitor
						Action.MOUSE_LEFT_CLICK, //action 
						null, //scroll amount
						null, //started monitors
						null, //progress monitors 
						List.of(this.tree2DoneMonitors[1], this.invinFilledWithLogs), //finished indicators
						3000, //ms before finished check
						120000, //ms before give up looking for finished ACTION_TIMEOUT
						null, //next action 
						null),//delay between mouse move and action execute
				new MouseActionable(
						this.tree2Monitors[1], //target monitor
						Action.MOUSE_LEFT_CLICK, //action 
						null, //scroll amount
						null, //started monitors
						null, //progress monitors 
						List.of(this.tree2DoneMonitors[1], this.invinFilledWithLogs), //finished indicators
						3000, //ms before finished check
						120000, //ms before give up looking for finished ACTION_TIMEOUT
						null, //next action 
						null),//delay between mouse move and action execute
				new MouseActionable(
						this.tree2Monitors[2], //target monitor
						Action.MOUSE_LEFT_CLICK, //action 
						null, //scroll amount
						null, //started monitors
						null, //progress monitors 
						List.of(this.tree2DoneMonitors[1], this.invinFilledWithLogs), //finished indicators
						3000, //ms before finished check
						120000, //ms before give up looking for finished ACTION_TIMEOUT
						null, //next action 
						null)//delay between mouse move and action execute
		};

		this.moveToPos1 = new MouseActionable[] {//from 0:pos2, 1:posFa
				new MouseActionable(
						this.pos2_posMonitors[0], //target monitor
						Action.MOUSE_LEFT_CLICK, //action 
						null, //scroll amount
						null, //started monitors
						null, //progress monitors 
						List.of(this.Pos1Ind), //finished indicators
						1000, //ms before finished check
						10000, //ms before give up looking for finished ACTION_TIMEOUT
						null, //next action 
						null),//delay between mouse move and action execute	
				new MouseActionable(
						this.posfa_posMonitors[0], //target monitor
						Action.MOUSE_LEFT_CLICK, //action 
						null, //scroll amount
						null, //started monitors
						null, //progress monitors 
						List.of(this.Pos1Ind), //finished indicators
						1000, //ms before finished check
						10000, //ms before give up looking for finished ACTION_TIMEOUT
						null, //next action 
						null)//delay between mouse move and action execute
		};
		this.moveToPos2 = new MouseActionable[] {//from 0:pos1, 1:posFa
				new MouseActionable(
						this.pos1_posMonitors[0], //target monitor
						Action.MOUSE_LEFT_CLICK, //action 
						null, //scroll amount
						null, //started monitors
						null, //progress monitors 
						List.of(this.Pos2Ind), //finished indicators
						1000, //ms before finished check
						10000, //ms before give up looking for finished ACTION_TIMEOUT
						null, //next action 
						null),//delay between mouse move and action execute	
				new MouseActionable(
						this.posfa_posMonitors[1], //target monitor
						Action.MOUSE_LEFT_CLICK, //action 
						null, //scroll amount
						null, //started monitors
						null, //progress monitors 
						List.of(this.Pos2Ind), //finished indicators
						1000, //ms before finished check
						10000, //ms before give up looking for finished ACTION_TIMEOUT
						null, //next action 
						null)//delay between mouse move and action execute
		};
		this.moveToPosF = new MouseActionable[] {//from 0:pos1, 1:pos2, 2:posFa
				new MouseActionable(
						this.pos1_posMonitors[1], //target monitor
						Action.MOUSE_LEFT_CLICK, //action 
						null, //scroll amount
						null, //started monitors
						null, //progress monitors 
						List.of(this.PosFInd), //finished indicators
						1000, //ms before finished check
						10000, //ms before give up looking for finished ACTION_TIMEOUT
						null, //next action 
						null),//delay between mouse move and action execute	
				new MouseActionable(
						this.pos2_posMonitors[1], //target monitor
						Action.MOUSE_LEFT_CLICK, //action 
						null, //scroll amount
						null, //started monitors
						null, //progress monitors 
						List.of(this.PosFInd), //finished indicators
						1000, //ms before finished check
						10000, //ms before give up looking for finished ACTION_TIMEOUT
						null, //next action 
						null),//delay between mouse move and action execute	
				new MouseActionable(
						this.posfa_posMonitors[2], //target monitor
						Action.MOUSE_LEFT_CLICK, //action 
						null, //scroll amount
						null, //started monitors
						null, //progress monitors 
						List.of(this.PosFInd), //finished indicators
						1000, //ms before finished check
						10000, //ms before give up looking for finished ACTION_TIMEOUT
						null, //next action 
						null)//delay between mouse move and action execute
		};
		this.moveToPosFa = new MouseActionable[] {//from 0:pos1, 2:pos2
				new MouseActionable(
						this.pos1_posMonitors[2], //target monitor
						Action.MOUSE_LEFT_CLICK, //action 
						null, //scroll amount
						null, //started monitors
						null, //progress monitors 
						List.of(this.PosFaInd), //finished indicators
						1000, //ms before finished check
						10000, //ms before give up looking for finished ACTION_TIMEOUT
						null, //next action 
						null),//delay between mouse move and action execute	
				new MouseActionable(
						this.pos2_posMonitors[2], //target monitor
						Action.MOUSE_LEFT_CLICK, //action 
						null, //scroll amount
						null, //started monitors
						null, //progress monitors 
						List.of(this.PosFaInd), //finished indicators
						1000, //ms before finished check
						10000, //ms before give up looking for finished ACTION_TIMEOUT
						null, //next action 
						null)//delay between mouse move and action execute
		};
		this.clickYewLogInvinSlotsFirst = new ArrayList<MouseActionable>();
		this.clickYewLogInvinSlotsRest = new ArrayList<MouseActionable>();
		for(int i=0; i<this.yewLogInvinSlots.size(); ++i) {
			this.clickYewLogInvinSlotsFirst.add(new MouseActionable(
					this.yewLogInvinSlots.get(i), 
					Action.MOUSE_LEFT_CLICK, 
					null, 
					null, 
					null, 
					List.of(this.selectedYewLogInvinSlots.get(i)), 
					0, 
					10000, 
					null, 
					null));		
			this.clickYewLogInvinSlotsRest.add(new MouseActionable(
					this.yewLogInvinSlots.get(i), 
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
//		diagMonitor(this.fireMakingXpGainIndicator);
		Sound.CHIMES_ASCENDING.play();
		this.fire[2].recordArea(20000, 100);
		Sound.BELL_HIGH.play();
	}
	public void diagMonitor(Monitorable monitor) {
		ImgManager.saveMatToFile(ImgManager.bufferedImageBGRToMatBGR(BasicScreenGrabber.get().imgTarget(monitor.getTargetArea())),null, "TEST_AREA_IMG");
		System.out.println("Check PASSES="+monitor.check());
		monitor.traceWithMouse();
	}
	public boolean scriptLoop() {
		Long startTimeMs = System.currentTimeMillis();
		System.out.println((System.currentTimeMillis()-startTimeMs)+":LoopStart");
		//Check position;
		while ( true ) {
			if( this.Pos1Ind.check() ) {
				System.out.println((System.currentTimeMillis()-startTimeMs)+":Position is 1");
				this.pos = 1;
				break;
			}
			if( this.Pos2Ind.check() ) {
				System.out.println((System.currentTimeMillis()-startTimeMs)+":Position is 2");
				this.pos = 2;
				break;
			}
			if( this.PosFaInd.check() ) {
				System.out.println((System.currentTimeMillis()-startTimeMs)+":Position is FireAdj");
				this.pos = 4;
				break;
			}
			System.out.println("Not in a valid starting position");
			return false;
		}
		//While inventory is not full, chop trees
		int lastLogOrEmptySlotIdx = 27;
		Monitorable lastInvinSlot = this.emptyInvinSlots.get(lastLogOrEmptySlotIdx);
		boolean lastSlotIsEmpty = lastInvinSlot.check();
		boolean lastSlotIsLog = lastInvinSlot.check(List.of(this.yewLog_invin_img), true, null);
		System.out.println((System.currentTimeMillis()-startTimeMs)+":Last Invin slot Empty?"+lastSlotIsEmpty+"\t Last Invin slot is Log?"+lastSlotIsLog);
		while( lastSlotIsEmpty && !lastSlotIsLog) {
			//TryChop Tree1
			System.out.println((System.currentTimeMillis()-startTimeMs)+":Try cut tree1");
			switch(this.pos) {
				case 1:
					if( !this.tree1[0].doActionable() ) {
						System.out.println("From1:Can't chop Tree1");
					}
					this.pos=1;
					break;
				case 2:
					if( !this.tree1[1].doActionable() ) {
						System.out.println("From2:Can't chop Tree1");
					}
					this.pos=1;
					break;
				case 4:
					if( this.moveToPos1[1].doActionable() ) {
						if( !this.tree1[0].doActionable() ) {
							System.out.println("FromFa:Can't chop Tree1");
						}
						this.pos=1;
						break;
					} else {
						System.out.println("Failed to move to Tree1 from fire adj pos.");
						return false;
					}
				default:
					System.out.println("Bad position to cut tree1");
					return false;
			}
			//TryChop Tree2
			System.out.println((System.currentTimeMillis()-startTimeMs)+":Try cut tree2");

			switch(this.pos) {
				case 1:
					if( !this.tree2[0].doActionable() ) {
						System.out.println("From1:Can't chop Tree2");
					}
					this.pos=2;
					break;
				case 2:
					if( !this.tree2[1].doActionable() ) {
						System.out.println("From2:Can't chop Tree2");
					}
					this.pos=2;
					break;
				case 4:
					if( this.moveToPos2[2].doActionable() ) {
						if( !this.tree2[1].doActionable() ) {
							System.out.println("FromFa:Can't chop Tree2");
						}
						this.pos=2;
						break;
					} else {
						System.out.println("Failed to move to Tree2 from fire adj pos.");
						return false;
					}
				default:
					System.out.println("Bad position to cut tree2");
					return false;
			}
			lastSlotIsEmpty = lastInvinSlot.check();
			lastSlotIsLog = lastInvinSlot.check(List.of(this.yewLog_invin_img), true, null);

			//Last slot is item other than log.
			if(!lastSlotIsEmpty && !lastSlotIsLog) {//Never false
				System.out.println((System.currentTimeMillis()-startTimeMs)+":Non-Log item in last invin slot.");
				Sound.BIKE_BELL.play();
				lastLogOrEmptySlotIdx-=1;
				lastInvinSlot = this.emptyInvinSlots.get(lastLogOrEmptySlotIdx);
				lastSlotIsEmpty = lastInvinSlot.check();
				lastSlotIsLog = lastInvinSlot.check(List.of(this.yewLog_invin_img), true, null);
				continue;
			}
		}
		System.out.println((System.currentTimeMillis()-startTimeMs)+":Invin full of logs.");
		Sound.BIKE_BELL.play();
		//If fire, feed fire, else build fire
		System.out.println((System.currentTimeMillis()-startTimeMs)+":Check if fire exists");
		boolean startFire = false;
		switch( this.pos ) {
			case 1:
				if( !this.fire[0].check() ) {
					if( !this.moveToPosF[0].doActionable() ) {
						System.out.println("Unable to move to fire spot to start fire");
						return false;
					}
					this.pos=3;
					startFire = true;
				} else {
					if( !this.moveToPosFa[0].doActionable() ) {
						System.out.println("Unable to move to fire adj spot to add to fire.");
						return false;
					}
					this.pos=4;
				}
				break;
			case 2:
				if( !this.fire[1].check() ) {
					if( !this.moveToPosF[1].doActionable() ) {
						System.out.println("Unable to move to fire spot to start fire");
						return false;
					}
					this.pos=3;
					startFire = true;
				} else {
					if( !this.moveToPosFa[1].doActionable() ) {
						System.out.println("Unable to move to fire adj spot to add to fire.");
						return false;
					}
					this.pos=4;
				}
				break;
			case 4:
				if( !this.fire[2].check() ) {
					if( !this.moveToPosF[2].doActionable() ) {
						System.out.println("Unable to move to fire spot to start fire");
						return false;
					}
					startFire = true;
				}
				break;
			default:
				System.out.println("Bad position to get to fire start pos");
				return false;
		}
		if( startFire ) {
			//StartFire
			System.out.println((System.currentTimeMillis()-startTimeMs)+":click yew logs,click Tinderbox,moveTopos1");
			if( !this.clickYewLogs_INVIN_LightFire.doActionable() || !this.lightFireTinderBox.doActionable()) {
				System.out.println("Couldn't light fire");
				return false;
			}
		}
		// add logs fire
		System.out.println((System.currentTimeMillis()-startTimeMs)+":click yew logs to add to fire");
		if( !this.clickYewLogs_INVIN_AddFire.doActionable() ) {
			System.out.println("Couldn't click logs to add to fire!");
			return false;
		}
		this.clickFireAddLogs.getTarget().startDiagnostic();
		System.out.println((System.currentTimeMillis()-startTimeMs)+":click on fire to add logs");
		if( !this.clickFireAddLogs.doActionable() ) {
			System.out.println("Couldn't click fire to add logs!");
			return false;
		}
		this.clickFireAddLogs.getTarget().stopDiagnostic();
		System.out.println((System.currentTimeMillis()-startTimeMs)+":confirm add logs");
		if( !this.confirmAddYewLogsFire.doActionable() ) {
			System.out.println("Couldn't click confirm to add logs to fire!");
			return false;
		}
		System.out.println((System.currentTimeMillis()-startTimeMs)+":wait for last log to burn");
		if( !this.waitForLastLogToBurn.doActionable() ) {
			System.out.println("Failed to wait for log to burn!");
			return false;
		}
		
//		this.nonLogItemInvinIdx.clear();
//		boolean first = true;
//		for(int i=0, j=2; j<28; ++i, ++j ) {
//			System.out.println("INVIN_IDX:"+j);
//			//Click log
//			System.out.println((System.currentTimeMillis()-startTimeMs)+":click on willow logs to add to fire");
//			if( first ) {
//				if( !this.clickWillowLogInvinSlotsFirst.get(i).doActionable() ) {
//					System.out.println("NO willow log to select & burn in invin slot:"+(i+3));
//					if( this.emptyInvinSlots.get(j).check() ) {
//						this.nonLogItemInvinIdx.add(j);
//					}
//					continue;
//				}
//				first = false;
//			} else {
//				if( !this.clickWillowLogInvinSlotsRest.get(i).doActionable() ) {
//					System.out.println("NO willow log to select & burn in invin slot:"+(i+3));
//					if( this.emptyInvinSlots.get(j).check() ) {
//						this.nonLogItemInvinIdx.add(j);
//					}
//					continue;
//				}
//			}
//			//add to fire
//			System.out.println((System.currentTimeMillis()-startTimeMs)+":click on fire to add logs");
//			if( !this.clickFireAddLogs.doActionable() ) {
//				System.out.println("Couldn't click fire to add logs!");
//				//Count remaining logs
//				int remainingLogs = 0;
//				for(Monitorable willowLogInvin : this.willowLogInvinSlots) {
//					if( willowLogInvin.check() ) {
//						remainingLogs++;
//					}
//				}
//				if( remainingLogs <= 1) {
//					break;
//				}
//				return false;
//			}
//			if( j>=27 ) {
//				break;
//			}
//			//confirmAdd
//			System.out.println((System.currentTimeMillis()-startTimeMs)+":confirm Add to fire.");
//			if( !this.confirmAddWillowLogsFireFast.doActionable() ) {
//				System.out.println("Couldn't confirm to burn logs");
//				return false;
//			}
//		}
//
//		//Wait for last log
//		System.out.println((System.currentTimeMillis()-startTimeMs)+":Wait for last log to finish.");
//		if( !this.waitForLastLogToBurn.doActionable() ) {
//			System.out.println("Failed to wait for last log to burn.");
//			return false;
//		}
			
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
