package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RandomizationPanel extends JPanel {
    private RadioPanel radioPanel;

    private JCheckBox randomizeCoinChests;
    private JCheckBox randomizeTrapItems;
    private JCheckBox randomizeEscapeChest;
    private JCheckBox randomizeCursedChests;
    private JCheckBox randomizeDracuetShop;
    private JCheckBox randomizeBacksideDoors;
    private JCheckBox randomizeNonBossDoors;
    private JCheckBox randomizeTransitionGates;
    private JCheckBox randomizeOneWayTransitions;

    private WeaponRandomizationPanel weaponRandomization;
    private ShopRandomizationRadio shopRandomization;
    private SwimsuitRandomizationPanel swimsuitRandomization;

    public RandomizationPanel() {
        super(new MigLayout("fillx, wrap, gapy 0"));

        radioPanel = new RadioPanel();
        add(radioPanel, "growx");

        weaponRandomization = new WeaponRandomizationPanel();
        add(weaponRandomization, "growx, wrap");

        shopRandomization = new ShopRandomizationRadio();
        add(shopRandomization, "growx, wrap");

        swimsuitRandomization = new SwimsuitRandomizationPanel();
        add(swimsuitRandomization, "growx, wrap");

        randomizeCoinChests = new JCheckBox();
        randomizeCoinChests.setSelected(Settings.isRandomizeCoinChests());

        randomizeTrapItems = new JCheckBox();
        randomizeTrapItems.setSelected(Settings.isRandomizeTrapItems());

        randomizeEscapeChest = new JCheckBox();
        randomizeEscapeChest.setSelected(Settings.isRandomizeEscapeChest());

        randomizeCursedChests = new JCheckBox();
        randomizeCursedChests.setSelected(Settings.isRandomizeCursedChests());

        randomizeDracuetShop = new JCheckBox();
        randomizeDracuetShop.setSelected(Settings.isRandomizeDracuetShop());

        randomizeBacksideDoors = new JCheckBox();
        randomizeBacksideDoors.setSelected(Settings.isRandomizeBacksideDoors());

        randomizeNonBossDoors = new JCheckBox();
        randomizeNonBossDoors.setSelected(Settings.isRandomizeNonBossDoors());
        randomizeNonBossDoors.setEnabled(Settings.isRandomizeBacksideDoors());

        randomizeBacksideDoors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() instanceof JCheckBox) {
                    randomizeNonBossDoors.setEnabled(((JCheckBox)e.getSource()).isSelected());
                }
            }
        });

        randomizeTransitionGates = new JCheckBox();
        randomizeTransitionGates.setSelected(Settings.isRandomizeTransitionGates());

        randomizeOneWayTransitions = new JCheckBox();
        randomizeOneWayTransitions.setSelected(Settings.isRandomizeOneWayTransitions());
        randomizeOneWayTransitions.setEnabled(Settings.isRandomizeTransitionGates());

        randomizeTransitionGates.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() instanceof JCheckBox) {
                    randomizeOneWayTransitions.setEnabled(((JCheckBox)e.getSource()).isSelected());
                }
            }
        });

        CheckboxContainer checkboxContainer = new CheckboxContainer(5, "gapy 0, insets 8 0 0 0");
        checkboxContainer.add(randomizeCoinChests);
        checkboxContainer.add(randomizeTrapItems);
        checkboxContainer.add(randomizeEscapeChest);
        checkboxContainer.add(randomizeCursedChests);
        checkboxContainer.add(randomizeDracuetShop);
        checkboxContainer.add(randomizeBacksideDoors);
        checkboxContainer.add(randomizeNonBossDoors);
        checkboxContainer.add(randomizeTransitionGates);
        checkboxContainer.add(randomizeOneWayTransitions);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        radioPanel.updateTranslations();
        randomizeCoinChests.setText(Translations.getText("randomization.randomizeCoinChests"));
        randomizeTrapItems.setText(Translations.getText("randomization.randomizeTrapItems"));
        randomizeEscapeChest.setText(Translations.getText("randomization.randomizeEscapeChest"));
        randomizeCursedChests.setText(Translations.getText("randomization.randomizeCursedChests"));
        randomizeDracuetShop.setText(Translations.getText("randomization.randomizeDracuetShop"));
        randomizeBacksideDoors.setText(Translations.getText("randomization.randomizeBacksideDoors"));
        randomizeNonBossDoors.setText(Translations.getText("randomization.randomizeNonBossDoors"));
        randomizeTransitionGates.setText(Translations.getText("randomization.randomizeTransitionGates"));
        randomizeOneWayTransitions.setText(Translations.getText("randomization.randomizeOneWayTransitions"));
        weaponRandomization.updateTranslations();
        shopRandomization.updateTranslations();
        swimsuitRandomization.updateTranslations();
    }

    public void updateSettings() {
        radioPanel.updateSettings();
        Settings.setRandomizeCoinChests(randomizeCoinChests.isSelected(), true);
        Settings.setRandomizeTrapItems(randomizeTrapItems.isSelected(), true);
        Settings.setRandomizeEscapeChest(randomizeEscapeChest.isSelected(), true);
        Settings.setRandomizeCursedChests(randomizeCursedChests.isSelected(), true);
        Settings.setRandomizeDracuetShop(randomizeDracuetShop.isSelected(), true);
        Settings.setRandomizeBacksideDoors(randomizeBacksideDoors.isSelected(), true);
        Settings.setRandomizeTransitionGates(randomizeTransitionGates.isSelected(), true);
        Settings.setRandomizeOneWayTransitions(randomizeOneWayTransitions.isEnabled() && randomizeOneWayTransitions.isSelected(), true);
        Settings.setRandomizeNonBossDoors(randomizeNonBossDoors.isEnabled() && randomizeNonBossDoors.isSelected(), true);
        weaponRandomization.updateSettings();
        shopRandomization.updateSettings();
        swimsuitRandomization.updateSettings();
    }

    public void reloadSettings() {
        radioPanel.reloadSettings();
        weaponRandomization.reloadSettings();
        shopRandomization.reloadSettings();
        swimsuitRandomization.reloadSettings();

        randomizeCoinChests.setSelected(Settings.isRandomizeCoinChests());
        randomizeTrapItems.setSelected(Settings.isRandomizeTrapItems());
        randomizeEscapeChest.setSelected(Settings.isRandomizeEscapeChest());
        randomizeCursedChests.setSelected(Settings.isRandomizeCursedChests());
        randomizeDracuetShop.setSelected(Settings.isRandomizeDracuetShop());
        randomizeBacksideDoors.setSelected(Settings.isRandomizeBacksideDoors());
        randomizeTransitionGates.setSelected(Settings.isRandomizeTransitionGates());
        randomizeOneWayTransitions.setSelected(Settings.isRandomizeOneWayTransitions());
        randomizeOneWayTransitions.setEnabled(Settings.isRandomizeTransitionGates());
        randomizeNonBossDoors.setSelected(Settings.isRandomizeNonBossDoors());
        randomizeNonBossDoors.setEnabled(Settings.isRandomizeBacksideDoors());
    }
}
