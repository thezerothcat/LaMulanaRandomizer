package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class LogicPanel extends JPanel {
    private DboostPanel dboostPanel;
    private GlitchPanel glitchPanel;

    private JCheckBox requireSoftwareComboForKeyFairy;
    private JCheckBox requireIceCapeForLava;
    private JCheckBox requireFlaresForExtinction;
    private JCheckBox ushumgalluAssist;
    private JCheckBox subweaponOnlyLogic;

    public LogicPanel() {
        super(new MigLayout("fillx, wrap"));

        dboostPanel = new DboostPanel();
        add(dboostPanel, "growx");

        glitchPanel = new GlitchPanel();
        add(glitchPanel, "growx");

        requireIceCapeForLava = new JCheckBox();
        requireIceCapeForLava.setSelected(Settings.isRequireIceCapeForLava());

        requireFlaresForExtinction = new JCheckBox();
        requireFlaresForExtinction.setSelected(Settings.isRequireFlaresForExtinction());

        requireSoftwareComboForKeyFairy = new JCheckBox();
        requireSoftwareComboForKeyFairy.setSelected(Settings.isRequireSoftwareComboForKeyFairy());

        ushumgalluAssist = new JCheckBox();
        ushumgalluAssist.setSelected(Settings.isUshumgalluAssist());

        subweaponOnlyLogic = new JCheckBox();
        subweaponOnlyLogic.setSelected(Settings.isSubweaponOnlyLogic());

        CheckboxContainer checkboxContainer = new CheckboxContainer(2);
        checkboxContainer.add(requireIceCapeForLava);
        checkboxContainer.add(requireFlaresForExtinction);
        checkboxContainer.add(requireSoftwareComboForKeyFairy);
        checkboxContainer.add(ushumgalluAssist);
        checkboxContainer.add(subweaponOnlyLogic);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        dboostPanel.updateTranslations();
        glitchPanel.updateTranslations();
        requireIceCapeForLava.setText(Translations.getText("logic.requireIceCapeForLava"));
        requireFlaresForExtinction.setText(Translations.getText("logic.requireFlaresForExtinction"));
        requireSoftwareComboForKeyFairy.setText(Translations.getText("logic.requireSoftwareComboForKeyFairy"));
        ushumgalluAssist.setText(Translations.getText("logic.ushumgalluAssist"));
        subweaponOnlyLogic.setText(Translations.getText("logic.subweaponOnlyLogic"));
    }

    public void updateSettings() {
        dboostPanel.updateSettings();
        glitchPanel.updateSettings();
        Settings.setRequireIceCapeForLava(requireIceCapeForLava.isSelected(), true);
        Settings.setRequireFlaresForExtinction(requireFlaresForExtinction.isSelected(), true);
        Settings.setRequireSoftwareComboForKeyFairy(requireSoftwareComboForKeyFairy.isSelected(), true);
        Settings.setUshumgalluAssist(ushumgalluAssist.isSelected(), true);
        Settings.setSubweaponOnlyLogic(subweaponOnlyLogic.isSelected(), true);
    }

    public void reloadSettings() {
        dboostPanel.reloadSettings();
        glitchPanel.reloadSettings();
        requireIceCapeForLava.setSelected(Settings.isRequireIceCapeForLava());
        requireFlaresForExtinction.setSelected(Settings.isRequireFlaresForExtinction());
        requireSoftwareComboForKeyFairy.setSelected(Settings.isRequireSoftwareComboForKeyFairy());
        ushumgalluAssist.setSelected(Settings.isUshumgalluAssist());
        subweaponOnlyLogic.setSelected(Settings.isSubweaponOnlyLogic());
    }
}
