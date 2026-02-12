package org.airrowe.game_player.script_runner.GUI;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import org.airrowe.game_player.script_runner.Script;

public class GuiHomeFrame extends JFrame {

    private final ScriptsTableModel tableModel;
    private final JTable table;

    public GuiHomeFrame(List<Script> scripts) {
        super("AirGama");

        this.tableModel = new ScriptsTableModel(scripts);
        this.table = new JTable(tableModel);

        setLayout(new BorderLayout());

        add(createToolbar(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
    }
    
    private JComponent createToolbar() {
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);

        bar.add(new JButton(new EditAction(this)));
        bar.add(new JButton(new EditAction(this)));
        bar.add(new JButton(new EditAction(this)));
        bar.addSeparator();
        bar.add(new JButton(new EditAction(this)));
        bar.add(new JButton(new EditAction(this)));
        bar.addSeparator();
        bar.add(new JButton(new EditAction(this)));

        return bar;
    }
    public JTable getTable() {
    	return this.table;
    }
    public ScriptsTableModel getTableModel() {
    	return this.tableModel;
    }
}
