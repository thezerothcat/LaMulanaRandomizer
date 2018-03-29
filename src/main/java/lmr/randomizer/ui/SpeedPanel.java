package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class SpeedPanel extends JPanel {
    private JCheckBox automaticGrailPoints;
    private JCheckBox quickStartItemsEnabled;

    public SpeedPanel() {
        super(new MigLayout("fillx, wrap"));

        automaticGrailPoints = new JCheckBox();
        automaticGrailPoints.setSelected(Settings.isAutomaticGrailPoints());

        quickStartItemsEnabled = new JCheckBox();
        quickStartItemsEnabled.setSelected(Settings.isQuickStartItemsEnabled());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(automaticGrailPoints);
        checkboxContainer.add(quickStartItemsEnabled);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        automaticGrailPoints.setText(Translations.getText("speed.automaticGrailPoints"));
        quickStartItemsEnabled.setText(Translations.getText("speed.quickStartItemsEnabled"));
    }

    public void updateSettings() {
        Settings.setAutomaticGrailPoints(automaticGrailPoints.isSelected(), true);
        Settings.setQuickStartItemsEnabled(quickStartItemsEnabled.isSelected(), true);
    }

    public void reloadSettings() {
        automaticGrailPoints.setSelected(Settings.isAutomaticGrailPoints());
        quickStartItemsEnabled.setSelected(Settings.isQuickStartItemsEnabled());
    }
}
