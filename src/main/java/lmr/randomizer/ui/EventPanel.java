package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class EventPanel extends JPanel {
    private JCheckBox halloweenMode;
    private JCheckBox includeHT;

    public EventPanel() {
        super(new MigLayout("fillx, wrap"));

        halloweenMode = new JCheckBox();
        halloweenMode.setSelected(true);
        halloweenMode.setEnabled(false);

        includeHT = new JCheckBox();
        includeHT.setSelected(true);
        includeHT.setSelected(Settings.isIncludeHellTempleNPCs());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(halloweenMode);
        checkboxContainer.add(includeHT);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        halloweenMode.setText(Translations.getText("event.halloween"));
        includeHT.setText(Translations.getText("event.includeHTNPCs"));
    }

    public void updateSettings() {
        Settings.setIncludeHellTempleNPCs(includeHT.isSelected(), true);
    }

    public void reloadSettings() {
        includeHT.setSelected(Settings.isIncludeHellTempleNPCs());
    }
}
