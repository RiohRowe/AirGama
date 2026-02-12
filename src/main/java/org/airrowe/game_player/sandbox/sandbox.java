package org.airrowe.game_player.sandbox;
import java.util.Map;

import org.airrowe.game_player.file_management.ResourceFolder;
import org.airrowe.game_player.script_runner.viewables.ViewableGroup;
import org.airrowe.game_player.script_runner.viewables.ViewableManager;

public class sandbox {
	public static void main(String[] args) {
		ViewableManager vm = ViewableManager.get();
//		ViewableGroup BR = vm.getViewableGroups()
//				.get(ResourceFolder.GAME_WINDOW_REF_IMGS)
//				.get("windowBoundary/bottomRight")
//				.get("bottomRight");
////		TL.assimilateExistingFile("topLeft-0.bmp");
//		BR.assimilateExistingFile("bottomRight-0.bmp");
		Map<ResourceFolder, Map<String, Map<String, ViewableGroup>>> groups = vm.getViewableGroups();
		for( ResourceFolder rf : groups.keySet() ) {
			Map<String, Map<String, ViewableGroup>> vgmm = groups.get(rf);
			for( String sd : vgmm.keySet() ) {
				Map<String, ViewableGroup> vgm = vgmm.get(sd);
				for( String bn : vgm.keySet() ) {
					System.out.println(rf+" "+sd+" "+bn);
				}
			}
		}
		ViewableManager.saveInstance();
	}
}
