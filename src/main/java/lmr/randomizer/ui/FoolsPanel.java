package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FoolsPanel extends JPanel {
    private JCheckBox randomize1;
    private JCheckBox randomize2;
    private JCheckBox randomize3;
    private JCheckBox randomize4;
    private JCheckBox randomize5;

    public FoolsPanel() {
        super(new MigLayout("fillx, wrap, gapy 0"));

        randomize1 = new JCheckBox();
        randomize1.setSelected(Settings.isRandomize1());

        randomize2 = new JCheckBox();
        randomize2.setSelected(Settings.isRandomize2());
        randomize2.setEnabled(Settings.isRandomize1());

        randomize1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() instanceof JCheckBox) {
                    randomize2.setEnabled(((JCheckBox)e.getSource()).isSelected());
                }
            }
        });

        randomize3 = new JCheckBox();
        randomize3.setSelected(Settings.isRandomize3());

        randomize4 = new JCheckBox();
        randomize4.setSelected(Settings.isRandomize4());

        randomize5 = new JCheckBox();
        randomize5.setSelected(Settings.isRandomize5());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1, "gapy 0, insets 8 0 0 0");
        checkboxContainer.add(randomize1);
        checkboxContainer.add(randomize2);
        checkboxContainer.add(randomize3);
        checkboxContainer.add(randomize4);
        checkboxContainer.add(randomize5);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        randomize1.setText(Translations.getText("fools.randomize1"));
        randomize2.setText(Translations.getText("fools.randomize2"));
        randomize3.setText(Translations.getText("fools.randomize3"));
        randomize4.setText(Translations.getText("fools.randomize4"));
        randomize5.setText(Translations.getText("fools.randomize5"));
    }

    public void updateSettings() {
        Settings.setRandomize1(randomize1.isSelected(), true);
        Settings.setRandomize2(randomize2.isEnabled() && randomize2.isSelected(), true);
        Settings.setRandomize3(randomize3.isSelected(), true);
        Settings.setRandomize4(randomize4.isSelected(), true);
        Settings.setRandomize5(randomize5.isSelected(), true);
    }

    public void reloadSettings() {
        randomize1.setSelected(Settings.isRandomize1());
        randomize2.setSelected(Settings.isRandomize2());
        randomize2.setEnabled(Settings.isRandomize1());
        randomize3.setSelected(Settings.isRandomize3());
        randomize4.setSelected(Settings.isRandomize4());
        randomize5.setSelected(Settings.isRandomize5());
    }
}
