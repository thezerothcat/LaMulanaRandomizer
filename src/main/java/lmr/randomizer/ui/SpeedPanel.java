package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class SpeedPanel extends JPanel {
    private JCheckBox automaticGrailPoints;

    public SpeedPanel() {
        super(new MigLayout("fillx, wrap"));

        automaticGrailPoints = new JCheckBox();
        automaticGrailPoints.setSelected(Settings.isAutomaticGrailPoints());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(automaticGrailPoints);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        automaticGrailPoints.setText(Translations.getText("speed.automaticGrailPoints"));
    }

    public void updateSettings() {
        Settings.setAutomaticGrailPoints(automaticGrailPoints.isSelected(), true);
    }

    public void reloadSettings() {
        automaticGrailPoints.setSelected(Settings.isAutomaticGrailPoints());
    }
}
