package lmr.randomizer.ui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class CheckboxContainer extends JPanel {
    public CheckboxContainer(int checkboxesPerRow) {
        super(new MigLayout(String.format("wrap %d", checkboxesPerRow),
                "[sizegroup checkboxes]",
                String.format("[]%d[]0", checkboxesPerRow)));
    }

    public void add(JCheckBox jCheckBox) {
        super.add(jCheckBox);
    }
}
