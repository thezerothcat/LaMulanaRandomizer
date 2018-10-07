package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class WeaponRandomizationRadio extends JPanel {
    private ButtonGroup weaponRandomization;
    private JLabel weaponRandomizationLabel;
    private JRadioButton whipStart;
    private JRadioButton randomMainWeapon;
    private JRadioButton randomAnyWeapon;

    public WeaponRandomizationRadio() {
        super(new MigLayout("gapy 0, insets 8 0 0 0"));

        weaponRandomizationLabel = new JLabel(Translations.getText("randomization.randomizeWeapon"), JLabel.LEFT);
        add(weaponRandomizationLabel);

        weaponRandomization = new ButtonGroup();

        whipStart = new JRadioButton(Translations.getText("items.Whip"));
        whipStart.setActionCommand("NONRANDOM");
        weaponRandomization.add(whipStart);

        randomMainWeapon = new JRadioButton(Translations.getText("randomization.randomizeWeapon.mainWeaponOnly"));
        randomMainWeapon.setActionCommand("MAINWEAPON");
        weaponRandomization.add(randomMainWeapon);

        randomAnyWeapon = new JRadioButton(Translations.getText("randomization.randomizeWeapon.allowSubweaponStart"));
        randomAnyWeapon.setActionCommand("ANYWEAPON");
        weaponRandomization.add(randomAnyWeapon);

        add(whipStart);
        add(randomMainWeapon);
        add(randomAnyWeapon);

        if(!Settings.isRandomizeMainWeapon()) {
            whipStart.setSelected(true);
        }
        else if(Settings.isAllowSubweaponStart()) {
            randomAnyWeapon.setSelected(true);
        }
        else {
            randomMainWeapon.setSelected(true);
        }
    }

    public void updateSettings() {
        if("NONRANDOM".equals(weaponRandomization.getSelection().getActionCommand())) {
            Settings.setRandomizeMainWeapon(false, true);
            Settings.setAllowSubweaponStart(false, true);
        }
        else if("MAINWEAPON".equals(weaponRandomization.getSelection().getActionCommand())) {
            Settings.setRandomizeMainWeapon(true, true);
            Settings.setAllowSubweaponStart(false, true);
        }
        else if("ANYWEAPON".equals(weaponRandomization.getSelection().getActionCommand())) {
            Settings.setRandomizeMainWeapon(true, true);
            Settings.setAllowSubweaponStart(true, true);
        }
    }

    public void updateTranslations() {
        weaponRandomizationLabel.setText(Translations.getText("randomization.randomizeWeapon"));
        whipStart.setText(Translations.getText("items.Whip"));
        randomMainWeapon.setText(Translations.getText("randomization.randomizeWeapon.mainWeaponOnly"));
        randomAnyWeapon.setText(Translations.getText("randomization.randomizeWeapon.allowSubweaponStart"));
    }

    public void reloadSettings() {
        if(!Settings.isRandomizeMainWeapon()) {
            whipStart.setSelected(true);
        }
        else if(Settings.isAllowSubweaponStart()) {
            randomAnyWeapon.setSelected(true);
        }
        else {
            randomMainWeapon.setSelected(true);
        }
    }
}
