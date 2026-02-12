package org.airrowe.game_player.script_runner.GUI;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.airrowe.game_player.script_runner.Script;

public class PopUp extends JDialog {

    private Script script;
    private JFrame owner;

    public PopUp(JFrame owner, Script original) {
        super(owner, "Edit Item", true); // true = modal
    	System.out.println("Running Popup code!");
        this.owner = owner;
        this.owner.setVisible(false);
        this.setVisible(true);
        setLayout(new BorderLayout());

        // build form fields here

        JButton ok = new JButton("OK");
        ok.addActionListener(e -> {
            script = buildItemFromFields();
            this.owner.setVisible(true);
            dispose();
        });

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> {
            script = null;
            owner.setVisible(true);
            dispose();
        });

        pack();
        setLocationRelativeTo(owner);
    }

    public Script getResult() {
        return script;
    }
    public Script buildItemFromFields() {
    	return script;
    }
}
