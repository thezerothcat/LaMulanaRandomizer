package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class RandomizationPanel extends JPanel {
    private RadioPanel radioPanel;

    private JCheckBox randomizeCoinChests;
    private JCheckBox randomizeTrapItems;
    private JCheckBox randomizeForbiddenTreasure;

    private ShopRandomizationRadio shopRandomization;
    private XmailerRandomizationRadio xmailerRandomization;

    public RandomizationPanel() {
        super(new MigLayout("fillx, wrap"));

        radioPanel = new RadioPanel();
        add(radioPanel, "growx");

        xmailerRandomization = new XmailerRandomizationRadio();
        add(xmailerRandomization, "gap rel 0, growx, wrap");

        shopRandomization = new ShopRandomizationRadio();
        add(shopRandomization, "gap rel 0, growx, wrap");

        randomizeCoinChests = new JCheckBox();
        randomizeCoinChests.setSelected(Settings.isRandomizeCoinChests());

        randomizeTrapItems = new JCheckBox();
        randomizeTrapItems.setSelected(Settings.isRandomizeTrapItems());

        randomizeForbiddenTreasure = new JCheckBox();
        randomizeForbiddenTreasure.setSelected(Settings.isRandomizeForbiddenTreasure());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(randomizeCoinChests);
        checkboxContainer.add(randomizeForbiddenTreasure);
        checkboxContainer.add(randomizeTrapItems);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        radioPanel.updateTranslations();
        randomizeForbiddenTreasure.setText(Translations.getText("randomization.randomizeForbiddenTreasure"));
        randomizeCoinChests.setText(Translations.getText("randomization.randomizeCoinChests"));
        randomizeTrapItems.setText(Translations.getText("randomization.randomizeTrapItems"));
        shopRandomization.updateTranslations();
        xmailerRandomization.updateTranslations();
    }

    public void updateSettings() {
        radioPanel.updateSettings();
        Settings.setRandomizeForbiddenTreasure(randomizeForbiddenTreasure.isSelected(), true);
        Settings.setRandomizeCoinChests(randomizeCoinChests.isSelected(), true);
        Settings.setRandomizeTrapItems(randomizeTrapItems.isSelected(), true);
        shopRandomization.updateSettings();
        xmailerRandomization.updateSettings();
    }

    public void reloadSettings() {
        radioPanel.reloadSettings();
        shopRandomization.reloadSettings();
        xmailerRandomization.reloadSettings();

        randomizeForbiddenTreasure.setSelected(Settings.isRandomizeForbiddenTreasure());
        randomizeCoinChests.setSelected(Settings.isRandomizeCoinChests());
        randomizeTrapItems.setSelected(Settings.isRandomizeTrapItems());
    }
}
