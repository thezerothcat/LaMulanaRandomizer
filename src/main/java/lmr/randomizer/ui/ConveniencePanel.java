package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class ConveniencePanel extends JPanel {
    private JCheckBox automaticGrailPoints;
    private JCheckBox automaticTranslations;
    private JCheckBox ushumgalluAssist;
    private JCheckBox bossCheckpoints;

    public ConveniencePanel() {
        super(new MigLayout("fillx"));
        setBorder(BorderFactory.createTitledBorder(Translations.getText("settings.convenience")));

        automaticGrailPoints = new JCheckBox();
        automaticGrailPoints.setSelected(Settings.isAutomaticGrailPoints());

        automaticTranslations = new JCheckBox();
        automaticTranslations.setSelected(Settings.isAutomaticTranslations());

        ushumgalluAssist = new JCheckBox();
        ushumgalluAssist.setSelected(Settings.isUshumgalluAssist());

        bossCheckpoints = new JCheckBox();
        bossCheckpoints.setSelected(Settings.isBossCheckpoints());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(automaticGrailPoints);
        checkboxContainer.add(automaticTranslations);
        checkboxContainer.add(ushumgalluAssist);
        checkboxContainer.add(bossCheckpoints);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateSettings() {
        Settings.setAutomaticGrailPoints(automaticGrailPoints.isSelected(), true);
        Settings.setAutomaticTranslations(automaticTranslations.isSelected(), true);
        Settings.setUshumgalluAssist(ushumgalluAssist.isSelected(), true);
        Settings.setBossCheckpoints(bossCheckpoints.isSelected(), true);
    }

    public void updateTranslations() {
        setBorder(BorderFactory.createTitledBorder(Translations.getText("settings.convenience")));
        automaticGrailPoints.setText(Translations.getText("gameplay.automaticGrailPoints"));
        automaticTranslations.setText(Translations.getText("gameplay.automaticTranslations"));
        ushumgalluAssist.setText(Translations.getText("gameplay.ushumgalluAssist"));
        bossCheckpoints.setText(Translations.getText("gameplay.bossCheckpoints"));
    }

    public void reloadSettings() {
        automaticGrailPoints.setSelected(Settings.isAutomaticGrailPoints());
        automaticTranslations.setSelected(Settings.isAutomaticTranslations());
        ushumgalluAssist.setSelected(Settings.isUshumgalluAssist());
    }
}
