package org.airrowe.game_player.input_emulation;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.Set;
import java.util.TreeSet;

import org.airrowe.game_player.image_grabbing.GameWindow;

import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser.INPUT;
import com.sun.jna.platform.win32.WinUser.MOUSEINPUT;
import static com.sun.jna.platform.win32.WinUser.INPUT.INPUT_MOUSE;

public class Mouse {
	public static final int MOUSEEVENTF_MOVE        = 0x0001;
	public static final int MOUSEEVENTF_LEFTDOWN    = 0x0002;
	public static final int MOUSEEVENTF_LEFTUP      = 0x0004;
	public static final int MOUSEEVENTF_RIGHTDOWN   = 0x0008;
	public static final int MOUSEEVENTF_RIGHTUP     = 0x0010;
	public static final int MOUSEEVENTF_MIDDLEDOWN  = 0x0020;
	public static final int MOUSEEVENTF_MIDDLEUP    = 0x0040;
	public static final int MOUSEEVENTF_WHEEL       = 0x0800;
	public static final int MOUSEEVENTF_ABSOLUTE    = 0x8000;
	
	private Set<MButton> buttonDownSet = new TreeSet<MButton>();
	
	static INPUT mouseMove(INPUT[] ins, int idx, int x, int y) {
	    INPUT in = ins[idx];
	    in.type=new WinDef.DWORD( INPUT_MOUSE );
	    in.input.setType(MOUSEINPUT.class);

	    MOUSEINPUT mi = new MOUSEINPUT();
	    mi.dx = new WinDef.LONG(GameWindow.getGameWindow().pixIdxToabsolutePos(x, false));
	    mi.dy = new WinDef.LONG(GameWindow.getGameWindow().pixIdxToabsolutePos(y, true));
	    mi.dwFlags = new WinDef.DWORD( MOUSEEVENTF_MOVE | MOUSEEVENTF_ABSOLUTE);

	    in.input.mi = mi;
	    return in;
	}
	public void moveMouse(int x, int y) {
	    INPUT[] inputs = (INPUT[]) new INPUT().toArray(1);
	    
	    mouseMove(inputs, 0, x, y);
	    
	    for (INPUT i : inputs) i.write();
	    User32.INSTANCE.SendInput(new WinDef.DWORD(inputs.length), inputs, inputs[0].size());
	}
	public Point getMousePosition() {
		return MouseInfo.getPointerInfo().getLocation();
	}
	public void leftClick(int x, int y) {
	    INPUT[] inputs = (INPUT[]) new INPUT().toArray(3);

	    mouseMove(inputs, 0, x, y);
	    mouseButton(inputs, 1, MOUSEEVENTF_LEFTDOWN);
	    mouseButton(inputs, 2, MOUSEEVENTF_LEFTUP);

	    for (INPUT i : inputs) i.write();
	    User32.INSTANCE.SendInput(new WinDef.DWORD(inputs.length), inputs, inputs[0].size());
	}
	
	static INPUT mouseButton(INPUT[] ins, int idx, int flag) {
	    INPUT in = ins[idx];
	    in.type=new WinDef.DWORD( INPUT_MOUSE );
	    in.input.setType(MOUSEINPUT.class);

	    MOUSEINPUT mi = new MOUSEINPUT();
	    mi.dwFlags = new WinDef.DWORD(flag);

	    in.input.mi = mi;
	    return in;
	}
	
	public void scroll(int amount) {
	    INPUT input = new INPUT();
	    input.type=new WinDef.DWORD( INPUT_MOUSE );
	    input.input.setType(MOUSEINPUT.class);

	    MOUSEINPUT mi = new MOUSEINPUT();
	    mi.dwFlags = new WinDef.DWORD(MOUSEEVENTF_WHEEL);
	    mi.mouseData = new WinDef.DWORD(amount); // +120 up, -120 down

	    input.input.mi = mi;
	    input.write();

	    User32.INSTANCE.SendInput(new WinDef.DWORD(1), new INPUT[]{input}, input.size());
	}
	
	public void middleClick(int x, int y) {
	    INPUT[] inputs = (INPUT[]) new INPUT().toArray(3);

	    mouseMove(inputs, 0, x, y);
	    mouseButton(inputs, 1, MOUSEEVENTF_MIDDLEDOWN);
	    mouseButton(inputs, 2, MOUSEEVENTF_MIDDLEUP);

	    for (INPUT i : inputs) i.write();
	    User32.INSTANCE.SendInput(new WinDef.DWORD(inputs.length), inputs, inputs[0].size());
	}
}
