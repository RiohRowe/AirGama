package org.airrowe.game_player.script_runner.areas;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.airrowe.game_player.image_grabbing.GameWindow;

public class Area implements Serializable{
	private static final long serialVersionUID = 1000000004L;
	protected Area parent;
	public List<Area> children = new ArrayList<Area>();
	public Rectangle parentArea;
	public boolean fromRight;
	public boolean fromBottom;
	public Rectangle areaRelative;
	public Rectangle areaConcrete;
	public Point center;
	//Created from the top down. Requires a parent.
	public Area(Rectangle areaRelativeToParent, boolean fromBottom, boolean fromRight, Area parent){
		this.areaRelative = areaRelativeToParent;
		this.fromBottom = fromBottom;
		this.fromRight = fromRight;
		this.calcAreaActual();
		this.setParent(parent);
		this.children = new ArrayList<Area>();
	}
	
	public void setParent(Area parent) {
		this.parent = parent;
		if( this.parent != null) {
			this.parentArea = parent.areaConcrete;
		} else {
			this.parentArea = areaRelative;
		}
	}
	
	private boolean isInBounds() {
		//Check In Bounds
		if(this.areaConcrete.x < this.parentArea.x) {
			System.out.println("GameArea Out of bounds Left. Adjusting");
//			this.areaConcretex = parentArea.x;
			return false;
		}
		if(this.areaConcrete.y < parentArea.y) {
			System.out.println("GameArea Out of bounds up. Adjusting");
//			this.areaConcrete.y = parentArea.y;
			return false;
		}
		if( this.areaConcrete.x+this.areaConcrete.width > this.parentArea.x+this.parentArea.width ) {
			System.out.println("game Area too wide for game window. Shrinking.");
//			this.areaConcrete.width = this.parentArea.x+this.parentArea.width-this.areaConcrete.x;
			return false;
		}
		if( this.areaConcrete.y+this.areaConcrete.height > this.parentArea.y+this.parentArea.height ) {
			System.out.println("game Area too tall for game window. Shrinking.");
//			this.areaConcrete.height = this.parentArea.y+this.parentArea.height-this.areaConcrete.y;
			return false;
		}
		return true;
	}
	
	public void calcAreaActual() {
		this.areaConcrete = new Rectangle(
				this.fromRight ? this.parentArea.x+this.parentArea.width-this.areaRelative.x : this.parentArea.x+this.areaRelative.x,
				this.fromBottom ? this.parentArea.y+this.parentArea.height-this.areaRelative.y : this.parentArea.y+this.areaRelative.y,
				this.areaRelative.width,
				this.areaRelative.height);
		this.center = new Point(
				this.areaConcrete.x+(int)(this.areaConcrete.width/2),
				this.areaConcrete.y+(int)(this.areaConcrete.height/2));
	}
	public void reCalculateArea() {
		this.reCalculateArea(this.parent.areaConcrete);
	}	
	public void reCalculateArea(Rectangle parentArea) {
		this.parentArea = parentArea;
		this.calcAreaActual();
		this.isInBounds();
		for( Area child : this.children) {
			child.reCalculateArea(this.areaConcrete);
		}
	}
	public Area getParent() {
		return this.parent;
	}
}
