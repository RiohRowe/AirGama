package org.airrowe.GamePlayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import org.airrowe.game_player.file_management.ResourceFolder;
import org.airrowe.game_player.image_processing.DirectImgLocate;
import org.airrowe.game_player.image_processing.MatchResult;
import org.airrowe.game_player.script_runner.Monitorable;
import org.airrowe.game_player.script_runner.areas.Area;
import org.airrowe.game_player.script_runner.viewables.Viewable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opencv.core.Mat;

@ExtendWith(MockitoExtension.class)
public class DirectImageLocateTester {
	
	private Viewable bmpFireSource;
	private Viewable pngFireRefImg;
	
	@Mock()
	private Monitorable fireMonitor;
	
	
	@BeforeEach
	public void setup() {
		bmpFireSource = new Viewable(ResourceFolder.TEST_IMGS, "source.bmp");
		pngFireRefImg = new Viewable(ResourceFolder.TEST_IMGS, "fire6-16.png");
		Mat fireSource = bmpFireSource.getMat();
		Rectangle targetArea = new Rectangle(0,0,fireSource.cols(),fireSource.rows());
		when(fireMonitor.getTargetArea()).thenReturn(targetArea);
		when(fireMonitor.diagnose()).thenReturn(false);
				
	}
	
	@Test
	public void testMatchingWithAlphaLayer() {
		MatchResult mr = DirectImgLocate.findMonitorTemplateNormCorr(this.bmpFireSource.getMat(), fireMonitor, List.of(pngFireRefImg), 0.99);
		assertEquals(new Point(6, 16), mr.locationTL);
		assertEquals(1.0, mr.score);
	}
}
