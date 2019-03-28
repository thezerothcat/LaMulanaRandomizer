package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class LogicPanel extends JPanel {
    private DboostPanel dboostPanel;
    private GlitchPanel glitchPanel;

    private DifficultyPanel difficultyPanel;

    private JCheckBox requireSoftwareComboForKeyFairy;
    private JCheckBox requireIceCapeForLava;
    private JCheckBox requireFlaresForExtinction;
    private JCheckBox subweaponOnlyLogic;
    private JCheckBox requireFullAccess;

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

        subweaponOnlyLogic = new JCheckBox();
        subweaponOnlyLogic.setSelected(Settings.isSubweaponOnlyLogic());

        CheckboxContainer checkboxContainer = new CheckboxContainer(2);
        checkboxContainer.add(requireIceCapeForLava);
        checkboxContainer.add(requireFlaresForExtinction);
        checkboxContainer.add(requireSoftwareComboForKeyFairy);
        checkboxContainer.add(subweaponOnlyLogic);
        add(checkboxContainer, "growx, wrap");

        difficultyPanel = new DifficultyPanel();
        add(difficultyPanel, "growx, aligny, wrap");

        requireFullAccess = new JCheckBox();
        requireFullAccess.setSelected(Settings.isRequireFullAccess());
        checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(requireFullAccess);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        dboostPanel.updateTranslations();
        glitchPanel.updateTranslations();
        difficultyPanel.updateTranslations();
        requireIceCapeForLava.setText(Translations.getText("logic.requireIceCapeForLava"));
        requireFlaresForExtinction.setText(Translations.getText("logic.requireFlaresForExtinction"));
        requireSoftwareComboForKeyFairy.setText(Translations.getText("logic.requireSoftwareComboForKeyFairy"));
        subweaponOnlyLogic.setText(Translations.getText("logic.subweaponOnlyLogic"));
        requireFullAccess.setText(Translations.getText("logic.requireFullAccess"));
    }

    public void updateSettings() {
        dboostPanel.updateSettings();
        glitchPanel.updateSettings();
        difficultyPanel.updateSettings();
        Settings.setRequireIceCapeForLava(requireIceCapeForLava.isSelected(), true);
        Settings.setRequireFlaresForExtinction(requireFlaresForExtinction.isSelected(), true);
        Settings.setRequireSoftwareComboForKeyFairy(requireSoftwareComboForKeyFairy.isSelected(), true);
        Settings.setSubweaponOnlyLogic(subweaponOnlyLogic.isSelected(), true);
        Settings.setRequireFullAccess(requireFullAccess.isSelected(), true);
    }

    public void reloadSettings() {
        dboostPanel.reloadSettings();
        glitchPanel.reloadSettings();
        difficultyPanel.reloadSettings();
        requireIceCapeForLava.setSelected(Settings.isRequireIceCapeForLava());
        requireFlaresForExtinction.setSelected(Settings.isRequireFlaresForExtinction());
        requireSoftwareComboForKeyFairy.setSelected(Settings.isRequireSoftwareComboForKeyFairy());
        subweaponOnlyLogic.setSelected(Settings.isSubweaponOnlyLogic());
        requireFullAccess.setSelected(Settings.isRequireFullAccess());
    }
}
