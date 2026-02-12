package org.airrowe.game_player.script_runner.GUI;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.airrowe.game_player.script_runner.Script;

public class ScriptsTableModel extends AbstractTableModel {
    private static final String[] COLS = {
        "Name", "CreateTime", "Status",
        "StartHotkey", "StopHotkey", "RepeatType"
    };
    private List<Script> items = new ArrayList<Script>();

    ScriptsTableModel(List<Script> scripts){
    	items = scripts;
    }
    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return COLS.length;
    }

    @Override
    public String getColumnName(int col) {
        return COLS[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Script item = items.get(row);
        return switch (col) {
            case 0 -> item.getName();
            case 1 -> item.getCreateTime();
            case 2 -> item.getStatus();
            case 3 -> item.getStartHotkey();
            case 4 -> item.getStopHotkey();
            case 5 -> item.getRepeatType();
            default -> null;
        };
    }

    public Script getItem(int row) {
        return items.get(row);
    }

    public void addItem(Script item) {
        items.add(item);
        fireTableRowsInserted(items.size() - 1, items.size() - 1);
    }

    public void removeItem(int row) {
        items.remove(row);
        fireTableRowsDeleted(row, row);
    }
}
