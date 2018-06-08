package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class RandomizationPanel extends JPanel {
    private RadioPanel radioPanel;

    private JCheckBox randomizeCoinChests;
    private JCheckBox randomizeTrapItems;
    private JCheckBox randomizeMainWeapon;
    private JCheckBox randomizeCursedChests;
    private JCheckBox randomizeDracuetShop;

    private ShopRandomizationRadio shopRandomization;
    private SwimsuitRandomizationPanel swimsuitRandomization;

    public RandomizationPanel() {
        super(new MigLayout("fillx, wrap, gapy 0"));

        radioPanel = new RadioPanel();
        add(radioPanel, "growx");

        shopRandomization = new ShopRandomizationRadio();
        add(shopRandomization, "growx, wrap");

        swimsuitRandomization = new SwimsuitRandomizationPanel();
        add(swimsuitRandomization, "growx, wrap");

        randomizeCoinChests = new JCheckBox();
        randomizeCoinChests.setSelected(Settings.isRandomizeCoinChests());

        randomizeTrapItems = new JCheckBox();
        randomizeTrapItems.setSelected(Settings.isRandomizeTrapItems());

        randomizeMainWeapon = new JCheckBox();
        randomizeMainWeapon.setSelected(Settings.isRandomizeMainWeapon());

        randomizeCursedChests = new JCheckBox();
        randomizeCursedChests.setSelected(Settings.isRandomizeCursedChests());

        randomizeDracuetShop = new JCheckBox();
        randomizeDracuetShop.setSelected(Settings.isRandomizeDracuetShop());

        CheckboxContainer checkboxContainer = new CheckboxContainer(2, "gapy 0, insets 8 0 0 0");
        checkboxContainer.add(randomizeCoinChests);
        checkboxContainer.add(randomizeMainWeapon);
        checkboxContainer.add(randomizeTrapItems);
        checkboxContainer.add(randomizeCursedChests);
        checkboxContainer.add(randomizeDracuetShop);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        radioPanel.updateTranslations();
        randomizeCoinChests.setText(Translations.getText("randomization.randomizeCoinChests"));
        randomizeTrapItems.setText(Translations.getText("randomization.randomizeTrapItems"));
        randomizeMainWeapon.setText(Translations.getText("randomization.randomizeMainWeapon"));
        randomizeCursedChests.setText(Translations.getText("randomization.randomizeCursedChests"));
        randomizeDracuetShop.setText(Translations.getText("randomization.randomizeDracuetShop"));
        shopRandomization.updateTranslations();
        swimsuitRandomization.updateTranslations();
    }

    public void updateSettings() {
        radioPanel.updateSettings();
        Settings.setRandomizeCoinChests(randomizeCoinChests.isSelected(), true);
        Settings.setRandomizeTrapItems(randomizeTrapItems.isSelected(), true);
        Settings.setRandomizeMainWeapon(randomizeMainWeapon.isSelected(), true);
        Settings.setRandomizeCursedChests(randomizeCursedChests.isSelected(), true);
        Settings.setRandomizeDracuetShop(randomizeDracuetShop.isSelected(), true);
        shopRandomization.updateSettings();
        swimsuitRandomization.updateSettings();
    }

    public void reloadSettings() {
        radioPanel.reloadSettings();
        shopRandomization.reloadSettings();
        swimsuitRandomization.reloadSettings();

        randomizeCoinChests.setSelected(Settings.isRandomizeCoinChests());
        randomizeTrapItems.setSelected(Settings.isRandomizeTrapItems());
        randomizeMainWeapon.setSelected(Settings.isRandomizeMainWeapon());
        randomizeCursedChests.setSelected(Settings.isRandomizeCursedChests());
        randomizeDracuetShop.setSelected(Settings.isRandomizeDracuetShop());
    }
}
