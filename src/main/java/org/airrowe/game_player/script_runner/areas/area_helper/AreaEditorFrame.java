package org.airrowe.game_player.script_runner.areas.area_helper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.airrowe.game_player.ResourceFolder;

public class AreaEditorFrame extends JFrame {
    public AreaEditorFrame() throws IOException {
        BufferedImage img = ImageIO.read(ResourceFolder.WUPSIES.getFile("refImg.png"));

        DefaultListModel<Rectangle> listModel = new DefaultListModel<>();
        JList<Rectangle> rectList = new JList<>(listModel);

        rectList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(
                "Rect " + index + ": x=" + value.x + " y=" + value.y +
                " w=" + value.width + " h=" + value.height
            );
            if (isSelected) label.setBackground(Color.LIGHT_GRAY);
            label.setOpaque(true);
            return label;
        });

        AreaEditorPanel editor = new AreaEditorPanel(img, listModel);
        
//        rectList.addListSelectionListener(e -> {
//            editor.setSelected(rectList.getSelectedValue());
//        });
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Areas"), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(rectList), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            leftPanel,
            new JScrollPane(editor)
        );
        split.setDividerLocation(250);

        add(split);
        add(new JScrollPane(editor));

        setTitle("Area Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(img.getWidth() + 20, img.getHeight() + 40);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new AreaEditorFrame();
    }
}