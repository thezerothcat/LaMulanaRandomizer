package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class SpeedPanel extends JPanel {
    private JCheckBox automaticGrailPoints;
    private JCheckBox automaticTranslations;

    public SpeedPanel() {
        super(new MigLayout("fillx, wrap"));

        automaticGrailPoints = new JCheckBox();
        automaticGrailPoints.setSelected(Settings.isAutomaticGrailPoints());

        automaticTranslations = new JCheckBox();
        automaticTranslations.setSelected(Settings.isAutomaticTranslations());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(automaticGrailPoints);
        checkboxContainer.add(automaticTranslations);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        automaticGrailPoints.setText(Translations.getText("speed.automaticGrailPoints"));
        automaticTranslations.setText(Translations.getText("speed.automaticTranslations"));
    }

    public void updateSettings() {
        Settings.setAutomaticGrailPoints(automaticGrailPoints.isSelected(), true);
        Settings.setAutomaticTranslations(automaticTranslations.isSelected(), true);
    }

    public void reloadSettings() {
        automaticGrailPoints.setSelected(Settings.isAutomaticGrailPoints());
        automaticTranslations.setSelected(Settings.isAutomaticTranslations());
    }
}
