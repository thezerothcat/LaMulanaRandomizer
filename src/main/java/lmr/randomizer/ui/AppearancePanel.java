package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class AppearancePanel extends JPanel {
    private JCheckBox coinChestGraphics;
    private JCheckBox randomizeGraphics;

    public AppearancePanel() {
        super(new MigLayout("fillx, wrap"));

        coinChestGraphics = new JCheckBox();
        coinChestGraphics.setSelected(Settings.isCoinChestGraphics());

        randomizeGraphics = new JCheckBox();
        randomizeGraphics.setSelected(Settings.isRandomizeGraphics());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(coinChestGraphics);
        checkboxContainer.add(randomizeGraphics);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        coinChestGraphics.setText(Translations.getText("appearance.coinChestGraphics"));
        randomizeGraphics.setText(Translations.getText("appearance.randomizeGraphics"));
    }

    public void updateSettings() {
        Settings.setCoinChestGraphics(coinChestGraphics.isSelected(), true);
        Settings.setRandomizeGraphics(randomizeGraphics.isSelected(), true);
    }

    public void reloadSettings() {
        coinChestGraphics.setSelected(Settings.isCoinChestGraphics());
        randomizeGraphics.setSelected(Settings.isRandomizeGraphics());
    }
}
