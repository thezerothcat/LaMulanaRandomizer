package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class GameplayChangesPanel extends JPanel {
    private ConveniencePanel conveniencePanel;
    private JCheckBox alternateMotherAnkh;

    public GameplayChangesPanel() {
        super(new MigLayout("fillx, wrap"));

        conveniencePanel = new ConveniencePanel();
        add(conveniencePanel, "growx");

        alternateMotherAnkh = new JCheckBox();
        alternateMotherAnkh.setSelected(Settings.isAlternateMotherAnkh());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(alternateMotherAnkh);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        conveniencePanel.updateTranslations();
        alternateMotherAnkh.setText(Translations.getText("gameplay.alternateMotherAnkh"));
    }

    public void updateSettings() {
        conveniencePanel.updateSettings();
        Settings.setAlternateMotherAnkh(alternateMotherAnkh.isSelected(), true);
    }

    public void reloadSettings() {
        conveniencePanel.reloadSettings();
        alternateMotherAnkh.setSelected(Settings.isAlternateMotherAnkh());
    }
}
