package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class LogicPanel extends JPanel {
    private JCheckBox requireSoftwareComboForKeyFairy;
    private JCheckBox requireIceCapeForLava;
    private JCheckBox requireFlaresForExtinction;

    public LogicPanel() {
        super(new MigLayout("fillx, wrap"));

        requireIceCapeForLava = new JCheckBox();
        requireIceCapeForLava.setSelected(Settings.isRequireIceCapeForLava());

        requireFlaresForExtinction = new JCheckBox();
        requireFlaresForExtinction.setSelected(Settings.isRequireFlaresForExtinction());

        requireSoftwareComboForKeyFairy = new JCheckBox();
        requireSoftwareComboForKeyFairy.setSelected(Settings.isRequireSoftwareComboForKeyFairy());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(requireIceCapeForLava);
        checkboxContainer.add(requireFlaresForExtinction);
        checkboxContainer.add(requireSoftwareComboForKeyFairy);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        requireIceCapeForLava.setText(Translations.getText("logic.requireIceCapeForLava"));
        requireFlaresForExtinction.setText(Translations.getText("logic.requireFlaresForExtinction"));
        requireSoftwareComboForKeyFairy.setText(Translations.getText("logic.requireSoftwareComboForKeyFairy"));
    }

    public void updateSettings() {
        Settings.setRequireIceCapeForLava(requireIceCapeForLava.isSelected(), true);
        Settings.setRequireFlaresForExtinction(requireFlaresForExtinction.isSelected(), true);
        Settings.setRequireSoftwareComboForKeyFairy(requireSoftwareComboForKeyFairy.isSelected(), true);
    }

    public void reloadSettings() {
        requireIceCapeForLava.setSelected(Settings.isRequireIceCapeForLava());
        requireFlaresForExtinction.setSelected(Settings.isRequireFlaresForExtinction());
        requireSoftwareComboForKeyFairy.setSelected(Settings.isRequireSoftwareComboForKeyFairy());
    }
}
