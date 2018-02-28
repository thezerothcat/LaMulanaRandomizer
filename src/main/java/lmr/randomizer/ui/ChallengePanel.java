package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class ChallengePanel extends JPanel {
    private JCheckBox automaticHardmode;
    private JCheckBox coinChestGraphics;

    private DifficultyPanel difficultyPanel;

    public ChallengePanel() {
        super(new MigLayout("fillx, wrap"));

        automaticHardmode = new JCheckBox();
        automaticHardmode.setSelected(Settings.isAutomaticHardmode());

        coinChestGraphics = new JCheckBox();
        coinChestGraphics.setSelected(Settings.isCoinChestGraphics());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(automaticHardmode);
        checkboxContainer.add(coinChestGraphics);
        add(checkboxContainer, "growx, wrap");

        difficultyPanel = new DifficultyPanel();
        add(difficultyPanel, "growx, aligny, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        automaticHardmode.setText(Translations.getText("challenge.automaticHardmode"));
        coinChestGraphics.setText(Translations.getText("challenge.coinChestGraphics"));
        difficultyPanel.updateTranslations();
    }

    public void updateSettings() {
        Settings.setAutomaticHardmode(automaticHardmode.isSelected(), true);
        Settings.setCoinChestGraphics(coinChestGraphics.isSelected(), true);
        difficultyPanel.updateSettings();
    }

    public void reloadSettings() {
        automaticHardmode.setSelected(Settings.getAutomaticHardmode());
        coinChestGraphics.setSelected(Settings.getCoinChestGraphics());
        difficultyPanel.reloadSettings();
    }
}
