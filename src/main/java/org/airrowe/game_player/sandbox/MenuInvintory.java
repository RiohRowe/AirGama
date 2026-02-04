package org.airrowe.game_player.sandbox;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.airrowe.game_player.script_runner.areas.Area;
import org.airrowe.game_player.script_runner.areas.GameWArea;

public class MenuInvintory {
	private static final Dimension INVIN_ITEM_SLOT_SIZE_PX = new Dimension(42, 36);
	private static final Rectangle INVIN_RECT = new Rectangle(228, 297, 168, 252);
	private static final Dimension MENU_ITEM_SIZE_PX = new Dimension(33,36);
	private static final Rectangle MENU_RECT = new Rectangle(471, 36, 429, 36);
	private static MenuInvintory instance;
	private List<Area> invinItemAreas;
	private GameWArea invinArea;
	private List<Area> menuItemAreas;
	private GameWArea menuArea;
	
	private MenuInvintory(Rectangle gameBB) {
		this.invinItemAreas = new ArrayList<Area>();
		for( int y=0; y<7; ++y) {
			for( int x=0; x<4; ++x) {
				this.invinItemAreas.add(new Area(
						new Rectangle(new Point(x*INVIN_ITEM_SLOT_SIZE_PX.width, y*INVIN_ITEM_SLOT_SIZE_PX.height), INVIN_ITEM_SLOT_SIZE_PX),
						false,
						false,
						null,
						null));
			}
		}
		this.invinArea = new GameWArea(
				INVIN_RECT,
				true,
				true,
				this.invinItemAreas,
				gameBB);
		for( Area child : invinItemAreas ) {
			child.setParent(invinArea);
		}
		
		this.menuItemAreas = new ArrayList<Area>();
		for( int i=0; i<MenuItem.values().length; ++i) {
			this.menuItemAreas.add(new Area(
					new Rectangle(new Point(MENU_ITEM_SIZE_PX.width*i, 0), MENU_ITEM_SIZE_PX),
					false, 
					false,
					null, 
					null));
		}
		this.menuArea = new GameWArea(
				MENU_RECT,
				true,
				true,
				this.menuItemAreas,
				gameBB);
		this.invinArea.calcCurrentArea(gameBB);
		this.menuArea.calcCurrentArea(gameBB);
	}
	public Area getInvinArea() {
		return this.invinArea;
	}
	public static MenuInvintory get(Rectangle gameBB) {
		if( instance == null ) {
			instance = new MenuInvintory(gameBB);
		}
		return instance;
	}
	
	public Area getInvinSlotArea(int idx) {
		return this.invinItemAreas.get(idx);
	}
}
