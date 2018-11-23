package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class ChallengePanel extends JPanel {
    private JCheckBox automaticHardmode;
    private JCheckBox coinChestGraphics;

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

        updateTranslations();
    }

    public void updateTranslations() {
        automaticHardmode.setText(Translations.getText("challenge.automaticHardmode"));
        coinChestGraphics.setText(Translations.getText("challenge.coinChestGraphics"));
    }

    public void updateSettings() {
        Settings.setAutomaticHardmode(automaticHardmode.isSelected(), true);
        Settings.setCoinChestGraphics(coinChestGraphics.isSelected(), true);
    }

    public void reloadSettings() {
        automaticHardmode.setSelected(Settings.isAutomaticHardmode());
        coinChestGraphics.setSelected(Settings.isCoinChestGraphics());
    }
}
