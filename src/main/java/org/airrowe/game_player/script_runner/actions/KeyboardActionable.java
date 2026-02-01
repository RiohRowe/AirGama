package org.airrowe.game_player.script_runner.actions;

import java.util.ArrayList;
import java.util.List;

import org.airrowe.game_player.input_emulation.Keyboard;
import org.airrowe.game_player.input_emulation.VKey;
import org.airrowe.game_player.script_runner.Monitorable;

public class KeyboardActionable extends Actionable{
	private static final Keyboard keyboard = Keyboard.get();
	private String text;
	private List<VKey> keys;

	public KeyboardActionable(Action interactionType, String text, List<VKey> keys, Monitorable notStartIndicator,
			Monitorable progressIndicator, Monitorable finishedIndicator, int msExpectedStart, int msExpectedEnd,
			Actionable next) {
		super(null, interactionType, notStartIndicator, progressIndicator, finishedIndicator, msExpectedStart, msExpectedEnd,
				next);
		this.text = text==null ? "" : text;
		this.keys = keys==null ? new ArrayList<VKey>() : keys;
		if( !interactionType.isKeyboardAction()) {
//			throw new RuntimeException("INVALID action.\t"+interactionType.toString()+" is NOT a keyboard action." );
			System.out.println("INVALID action.\t"+interactionType.toString()+" is NOT a keyboard action. Defaulting to "+Action.DEFAULT_KEYBOARD_ACTION.toString());
			interactionType = Action.DEFAULT_KEYBOARD_ACTION;
		}
	}

	@Override
	protected void doAction() {
		//Perform MouseAction -> targetCenter.
		switch( interactionType ) {
			case KEYBOARD_TYPE:
				keyboard.typeString(this.text);
				break;
			case KEYBOARD_KEYS_PRESS:
				keyboard.pressKeys(this.keys);;
				break;
			case KEYBOARD_KEYS_DOWN:
				keyboard.KeysDown(this.keys);
				break;
			case KEYBOARD_KEYS_UP:
				keyboard.KeysUp(keys);
				break;
			default:
				System.out.println("KeyboardAction does not recognize this action: "+interactionType.toString());
		}
	}
}
