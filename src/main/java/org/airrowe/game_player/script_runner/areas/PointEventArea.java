package org.airrowe.game_player.script_runner.areas;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import org.airrowe.game_player.image_grabbing.GameWindow;

public class PointEventArea  extends Area{
	public static final GameWindow GW = GameWindow.getGameWindow();
	private Point eventSource;
	
	PointEventArea(Rectangle areaRelativeToParent, boolean fromBottom, boolean fromRight, List<Area> children, Rectangle gameBB){
		super(areaRelativeToParent, fromBottom, fromRight, null, children);
		this.parentArea = gameBB;
	}
	
	@Override
	public void setParent(Area parent) {
		this.parent = null;
	}
	public void calcCurrentArea(Rectangle parentArea, Point eventSource) {
		this.eventSource = eventSource;
		this.calcCurrentArea(parentArea);
	}
	@Override
	public void calcCurrentArea(Rectangle parentArea) {
		this.parentArea = parentArea;
		int xCoord = this.eventSource.x+this.areaRelative.x;
		int yCoord = this.eventSource.y+this.areaRelative.y;
		Rectangle newArea =  new Rectangle(
				xCoord,
				yCoord,
				this.areaRelative.width,
				this.areaRelative.height);
		boolean unfixableOutOfBounds = false;
		//Check if out of bounds left
		if( newArea.x < this.parentArea.x) {
			newArea.x = this.parentArea.x;
			if(newArea.x + newArea.width > this.parentArea.x+this.parentArea.width) {
				unfixableOutOfBounds = true;
			}
		}
		//Check if out of bounds up
		if( !unfixableOutOfBounds && newArea.y < this.parentArea.y) {
			newArea.y = this.parentArea.y;
			if(newArea.y + newArea.height > this.parentArea.y+this.parentArea.height) {
				unfixableOutOfBounds = true;
			}
		}
		//Check if out of bounds right
		if( !unfixableOutOfBounds && newArea.x+newArea.width > this.parentArea.x+this.parentArea.width) {
			newArea.x = this.parentArea.x+this.parentArea.width-newArea.width;
			if(newArea.x < this.parentArea.x) {
				unfixableOutOfBounds = true;
			}
		}
		//Check if out of bounds down
		if( !unfixableOutOfBounds && newArea.y+newArea.height > this.parentArea.y+this.parentArea.height) {
			newArea.y = this.parentArea.y+this.parentArea.height-newArea.height;
			if(newArea.y < this.parentArea.y) {
				unfixableOutOfBounds = true;
			}
		}
		if( unfixableOutOfBounds ) {
			throw new RuntimeException("Area excedes bounds of parent."+
					"\nParentBB:x="+this.parentArea.x+"\ty="+this.parentArea.y+"\twidth="+this.parentArea.width+"\theight="+this.parentArea.height+
					"\nAreaBB  :x="+newArea.x+"\ty="+newArea.y+"\twidth="+newArea.width+"\theight="+newArea.height);
		}
		this.areaConcrete = newArea;
		this.center = new Point(newArea.x+(int)(newArea.width/2),newArea.y+(int)(newArea.height/2));
		for( Area child : this.children) {
			child.calcCurrentArea(this.areaConcrete);
		}
	}
}