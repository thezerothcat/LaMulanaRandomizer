package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class AppearancePanel extends JPanel {
    private JCheckBox coinChestGraphics;
    private JCheckBox randomizeGraphics;
    private JCheckBox screenshakeDisabled;

    public AppearancePanel() {
        super(new MigLayout("fillx, wrap"));

        coinChestGraphics = new JCheckBox();
        coinChestGraphics.setSelected(Settings.isCoinChestGraphics());

        randomizeGraphics = new JCheckBox();
        randomizeGraphics.setSelected(Settings.isRandomizeGraphics());

        screenshakeDisabled = new JCheckBox();
        screenshakeDisabled.setSelected(Settings.isScreenshakeDisabled());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(coinChestGraphics);
        checkboxContainer.add(randomizeGraphics);
        checkboxContainer.add(screenshakeDisabled);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        coinChestGraphics.setText(Translations.getText("appearance.coinChestGraphics"));
        randomizeGraphics.setText(Translations.getText("appearance.randomizeGraphics"));
        screenshakeDisabled.setText(Translations.getText("appearance.screenshakeDisabled"));
    }

    public void updateSettings() {
        Settings.setCoinChestGraphics(coinChestGraphics.isSelected(), true);
        Settings.setRandomizeGraphics(randomizeGraphics.isSelected(), true);
        Settings.setScreenshakeDisabled(screenshakeDisabled.isSelected(), true);
    }

    public void reloadSettings() {
        coinChestGraphics.setSelected(Settings.isCoinChestGraphics());
        randomizeGraphics.setSelected(Settings.isRandomizeGraphics());
        screenshakeDisabled.setSelected(Settings.isScreenshakeDisabled());
    }
}
