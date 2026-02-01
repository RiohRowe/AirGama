package org.airrowe.game_player.script_runner.actions;

public enum Action {
	MOUSE_RIGHT_CLICK(0),
	MOUSE_LEFT_CLICK(1),
	MOUSE_MIDDLE_CLICK(2),
	MOUSE_SCROLL(3),
	MOUSE_MOVE(4),
	KEYBOARD_KEYS_DOWN(5),
	KEYBOARD_KEYS_UP(6),
	KEYBOARD_KEYS_PRESS(7),
	KEYBOARD_TYPE(8),
	WAIT(9);
	
	private static final int MOUSE_ACTIONS = KEYBOARD_KEYS_DOWN.bitRep-1;
	private static final int KEYBOARD_ACTIONS = WAIT.bitRep-KEYBOARD_KEYS_DOWN.bitRep;
	public static final Action DEFAULT_MOUSE_ACTION = MOUSE_MOVE;
	public static final Action DEFAULT_KEYBOARD_ACTION = KEYBOARD_TYPE;
	public static final Action DEFAULT_WAIT_ACTION = WAIT;
	public int bitRep;
	
	Action(int index){
		this.bitRep = 1<<index;
	}
	public boolean isMouseAction() {
		return ( this.bitRep & MOUSE_ACTIONS )==this.bitRep;
	}
	public boolean isKeyboardAction() {
		return ( this.bitRep & KEYBOARD_ACTIONS )==this.bitRep;
	}
	public boolean isWaitAction() {
		return bitRep>=WAIT.bitRep;
	}
}
