package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class WeaponRandomizationPanel extends JPanel {
    private JCheckBox allowWhipStart;
    private JCheckBox allowMainWeaponStart;
    private JCheckBox allowSubweaponStart;

    public WeaponRandomizationPanel() {
        super(new MigLayout("gapy 0, insets 8 14 8 0", "", ""));
        setBorder(BorderFactory.createTitledBorder(Translations.getText("randomization.randomizeWeapon")));

        allowWhipStart = new JCheckBox(Translations.getText("randomization.randomizeWeapon.allowWhipStart"));
        allowWhipStart.setSelected(Settings.isAllowWhipStart());

        allowMainWeaponStart = new JCheckBox(Translations.getText("randomization.randomizeWeapon.allowMainWeaponStart"));
        allowMainWeaponStart.setSelected(Settings.isAllowMainWeaponStart());

        allowSubweaponStart = new JCheckBox(Translations.getText("randomization.randomizeWeapon.allowSubweaponStart"));
        allowSubweaponStart.setSelected(Settings.isAllowSubweaponStart());

        add(allowWhipStart);
        add(allowMainWeaponStart);
        add(allowSubweaponStart);
    }

    public void updateSettings() {
        Settings.setAllowWhipStart(allowWhipStart.isSelected(), true);
        Settings.setAllowMainWeaponStart(allowMainWeaponStart.isSelected(), true);
        Settings.setAllowSubweaponStart(allowSubweaponStart.isSelected(), true);
    }

    public void updateTranslations() {
        setBorder(BorderFactory.createTitledBorder(Translations.getText("randomization.randomizeWeapon")));
        allowWhipStart.setText(Translations.getText("items.Whip"));
        allowMainWeaponStart.setText(Translations.getText("randomization.randomizeWeapon.allowMainWeaponStart"));
        allowSubweaponStart.setText(Translations.getText("randomization.randomizeWeapon.allowSubweaponStart"));
    }

    public void reloadSettings() {
        allowWhipStart.setSelected(Settings.isAllowWhipStart());
        allowMainWeaponStart.setSelected(Settings.isAllowMainWeaponStart());
        allowSubweaponStart.setSelected(Settings.isAllowSubweaponStart());
    }
}
