package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class FoolsPanel extends JPanel {
    private JCheckBox randomize1;
    private JCheckBox randomize2;
    private JCheckBox randomize3;
    private JCheckBox randomize4;

    public FoolsPanel() {
        super(new MigLayout("fillx, wrap, gapy 0"));

        randomize1 = new JCheckBox();
        randomize1.setSelected(Settings.isRandomize1());

        randomize2 = new JCheckBox();
        randomize2.setSelected(Settings.isRandomize2());

        randomize3 = new JCheckBox();
        randomize3.setSelected(Settings.isRandomize3());

        randomize4 = new JCheckBox();
        randomize4.setSelected(Settings.isRandomize4());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1, "gapy 0, insets 8 0 0 0");
        checkboxContainer.add(randomize1);
        checkboxContainer.add(randomize2);
        checkboxContainer.add(randomize3);
        checkboxContainer.add(randomize4);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        randomize1.setText(Translations.getText("fools.randomize1"));
        randomize2.setText(Translations.getText("fools.randomize2"));
        randomize3.setText(Translations.getText("fools.randomize3"));
        randomize4.setText(Translations.getText("fools.randomize4"));
    }

    public void updateSettings() {
        Settings.setRandomize1(randomize1.isSelected(), true);
        Settings.setRandomize2(randomize2.isSelected(), true);
        Settings.setRandomize3(randomize3.isSelected(), true);
        Settings.setRandomize4(randomize4.isSelected(), true);
    }

    public void reloadSettings() {
        randomize1.setSelected(Settings.isRandomize1());
        randomize2.setSelected(Settings.isRandomize2());
        randomize3.setSelected(Settings.isRandomize3());
        randomize4.setSelected(Settings.isRandomize4());
    }
}
