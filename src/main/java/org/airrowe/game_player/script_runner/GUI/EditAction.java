package org.airrowe.game_player.script_runner.GUI;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.airrowe.game_player.script_runner.Script;

public class EditAction extends AbstractAction {

    private final GuiHomeFrame frame;

    public EditAction(GuiHomeFrame frame) {
        super("Edit");
        System.out.println("Making Edit Action");
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	System.out.println("Performing Edit Action");
        int row = frame.getTable().getSelectedRow();
        if (row < 0) return;

        Script item = frame.getTableModel().getItem(row);
        System.out.println("Started Creating Pupup");
        PopUp dialog = new PopUp(frame, item);
        System.out.println("Finished Creating Popup");
        dialog.setVisible(true);

        Script updated = dialog.getResult();
        if (updated != null) {
            frame.getTableModel().fireTableRowsUpdated(row, row);
        }
    }
}