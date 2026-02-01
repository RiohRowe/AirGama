package org.airrowe.game_player.input_emulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser.INPUT;

public class Keyboard {

    // --- Constants ---
    static final int INPUT_KEYBOARD = 1;
    static final int KEYEVENTF_UNICODE = 0x0004;
    static final int KEYEVENTF_KEYUP   = 0x0002;
    static final int KEYEVENTF_KEYDOWN   = 0x0000;
    
    private static Keyboard instance;
    private Set<VKey> pressedKeys;

    private Keyboard() {
    	this.pressedKeys = new TreeSet<VKey>();
    }
    
    public static Keyboard get() {
    	if( instance == null ) {
    		instance = new Keyboard();
    	}
    	return instance;
    }
    
    // --- Helper ---
    //Types a string as is by injecting it.
    static INPUT keyInject(INPUT[] ins, int idx, char ch, boolean up) {
    	// Prepare input reference
        INPUT input = ins[idx];

        input.type = new WinDef.DWORD( INPUT.INPUT_KEYBOARD );
        input.input.setType("ki");
        input.input.ki.wScan = new WinDef.WORD( ch );
        input.input.ki.time = new WinDef.DWORD( 0 );
        input.input.ki.dwExtraInfo = new BaseTSD.ULONG_PTR( 0 );
        input.input.ki.wVk = new WinDef.WORD( 0 );
        input.input.ki.dwFlags = new WinDef.DWORD( KEYEVENTF_UNICODE | (up ? KEYEVENTF_KEYUP : 0) );
        return input;
    }
    //Simulates a series of key presses.
    static INPUT keyPress(INPUT[] ins, int idx, VKey keyCode, boolean up) {
    	// Prepare input reference
        INPUT input = ins[idx];

        input.type = new WinDef.DWORD( INPUT.INPUT_KEYBOARD );
        input.input.setType("ki");
        input.input.ki.wScan = new WinDef.WORD( );
        input.input.ki.time = new WinDef.DWORD( 0 );
        input.input.ki.dwExtraInfo = new BaseTSD.ULONG_PTR( 0 );
        input.input.ki.wVk = new WinDef.WORD( keyCode.code );
        input.input.ki.dwFlags = new WinDef.DWORD((up ? KEYEVENTF_KEYUP : 0) );
        return input;
    }
	
	public void typeString(String str){
		int numChars = str.length();
		INPUT[] inputs = (INPUT[]) new INPUT().toArray(numChars*2);
		int j = 0;
		for( int i=0; i<str.length(); ++i) {
		    char ch = str.charAt(i);
			keyInject(inputs,j++,ch,false);
			keyInject(inputs,j++,ch,true);
		}
		for( INPUT input : inputs) {
			input.write();
		}

        User32.INSTANCE.SendInput( new WinDef.DWORD( inputs.length ), inputs, inputs[0].size() );
	}
	//presses provided keys in order, then releases them in reverse order.
	public void pressKeys(List<VKey> keys){
		int numInput = keys.size()*2;
		INPUT[] inputs = (INPUT[]) new INPUT().toArray(numInput);
		for( int i=0,j=keys.size()-1; i<keys.size(); ++i) {
			keyPress(inputs,i,keys.get(i),false);
			keyPress(inputs,j,keys.get(i),true);
		}
		for( INPUT input : inputs) {
			input.write();
		}
        User32.INSTANCE.SendInput( new WinDef.DWORD( inputs.length ), inputs, inputs[0].size() );
	}
	public void KeysDown(List<VKey> keys){
		int numInput = keys.size();
		INPUT[] inputs = (INPUT[]) new INPUT().toArray(numInput);
		for( int i=0,j=keys.size()-1; i<keys.size(); ++i) {
			keyPress(inputs,i,keys.get(i),false);
			if( !this.pressedKeys.contains(keys.get(i)) ) {
				this.pressedKeys.add(keys.get(i));
			}
		}
		for( INPUT input : inputs) {
			input.write();
		}
        User32.INSTANCE.SendInput( new WinDef.DWORD( inputs.length ), inputs, inputs[0].size() );
	}
	public void KeysUp(List<VKey> keys){
		int numInput = keys.size();
		INPUT[] inputs = (INPUT[]) new INPUT().toArray(numInput);
		for( int i=0,j=keys.size()-1; i<keys.size(); ++i) {
			keyPress(inputs,i,keys.get(i),true);
			this.pressedKeys.remove(keys.get(i));
		}
		for( INPUT input : inputs) {
			input.write();
		}
        User32.INSTANCE.SendInput( new WinDef.DWORD( inputs.length ), inputs, inputs[0].size() );
	}
	public void releaseKeys() {
		List<VKey> releaseList = new ArrayList();
		for(VKey keyCode : this.pressedKeys) {
			releaseList.add(keyCode);
		}
		this.KeysUp(releaseList);
	}
}
