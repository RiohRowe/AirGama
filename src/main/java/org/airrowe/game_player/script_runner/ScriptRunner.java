package org.airrowe.game_player.script_runner;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.airrowe.game_player.input_emulation.VKey;
import org.airrowe.game_player.script_runner.GUI.GuiHomeFrame;
import org.airrowe.game_player.script_runner.areas.AreaManager;
import org.airrowe.game_player.script_runner.viewables.ViewableManager;

public class ScriptRunner {	
	public static void main(String[] args) {
		ViewableManager vm = ViewableManager.get();
		AreaManager am = AreaManager.get();
		List<Script> scripts = new ArrayList<Script>();
		scripts.add(new Script("YewWoodCutting", VKey.F1, VKey.F3, RepeatType.REPEAT_UNTIL_STOP_ORDER));
		scripts.add(new Script("tellJokes", VKey.F2, VKey.F3, RepeatType.REPEAT_N_TIMES));
		scripts.add(new Script("doClueScroll", VKey.F4, VKey.F3, RepeatType.DO_ONCE));
		GuiHomeFrame hframe = new GuiHomeFrame(scripts);

        hframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hframe.pack();
        hframe.setVisible(true);
	}
}
