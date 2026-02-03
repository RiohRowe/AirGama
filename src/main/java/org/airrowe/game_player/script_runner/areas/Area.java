package org.airrowe.game_player.script_runner.areas;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.airrowe.game_player.image_grabbing.GameWindow;

public class Area {
	public static final GameWindow GW = GameWindow.getGameWindow();
	protected Area parent;
	public List<Area> children = new ArrayList<Area>();
	public Rectangle parentArea;
	public boolean fromRight;
	public boolean fromBottom;
	public Rectangle areaRelative;
	public Rectangle areaConcrete;
	public Point center;
	
	public Area(Rectangle areaRelativeToParent, boolean fromBottom, boolean fromRight, Area parent, List<Area> children){
		this.areaRelative = areaRelativeToParent;
		this.fromBottom = fromBottom;
		this.fromRight = fromRight;
		this.setParent(parent);
		this.children = children == null ? new ArrayList<Area>() : children;
	}
	
	public void setParent(Area parent) {
		this.parent = parent;
		if( this.parent != null) {
			this.parentArea = parent.areaConcrete;
		} else {
			this.parentArea = areaRelative;
		}
	}
	
	private boolean isAreaInBounds(Rectangle area) {
		//Check In Bounds
		if(area.x < this.parentArea.x) {
			System.out.println("GameArea Out of bounds Left. Adjusting");
//			area.x = parentArea.x;
			return false;
		}
		if(area.y < parentArea.y) {
			System.out.println("GameArea Out of bounds up. Adjusting");
//			area.y = parentArea.y;
			return false;
		}
		if( area.x+area.width > this.parentArea.x+this.parentArea.width ) {
			System.out.println("game Area too wide for game window. Shrinking.");
//			area.width = this.parentArea.x+this.parentArea.width-area.x;
			return false;
		}
		if( area.y+area.height > this.parentArea.y+this.parentArea.height ) {
			System.out.println("game Area too tall for game window. Shrinking.");
//			area.height = this.parentArea.y+this.parentArea.height-area.y;
			return false;
		}
		return true;
	}
	
	public void calcCurrentArea(Rectangle parentArea) {
		this.parentArea = parentArea;
		int xCoord = this.fromRight ? this.parentArea.x+this.parentArea.width-this.areaRelative.x : this.parentArea.x+this.areaRelative.x;
		int yCoord = this.fromBottom ? this.parentArea.y+this.parentArea.height-this.areaRelative.y : this.parentArea.y+this.areaRelative.y;
		Rectangle newArea =  new Rectangle(
				xCoord,
				yCoord,
				this.areaRelative.width,
				this.areaRelative.height);
		if( !isAreaInBounds(newArea) ) {
			throw new RuntimeException("Area excedes bounds of parent."+
					"\nParentBB:x="+parentArea.x+"\ty="+parentArea.y+"\twidth="+parentArea.width+"\theight="+parentArea.height+
					"\nAreaBB  :x="+newArea.x+"\ty="+newArea.y+"\twidth="+newArea.width+"\theight="+newArea.height);
		}
		this.areaConcrete = newArea;
		this.center = new Point(newArea.x+(int)(newArea.width/2),newArea.y+(int)(newArea.height/2));
		for( Area child : this.children) {
			child.calcCurrentArea(this.areaConcrete);
		}
	}	
	//child target array is treated as stack. Order children in reverse order.
	public Area getChild(LinkedList<Integer> childTarget) {
		//Check GameBoundingBox Change.
		if( childTarget.isEmpty()) {
			return this;
		}
//		Area nextChild = children.get(childTarget.pop());
//		return nextChild.getChild(childTarget);
		return children.get(childTarget.pop()).getChild(childTarget);
	}
}
