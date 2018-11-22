package lmr.randomizer.ui;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GlitchPanel extends JPanel {
    private List<JCheckBox> glitchOptions = new ArrayList<>();

    public GlitchPanel() {
        super(new MigLayout("fillx, wrap 4", "[sizegroup checkboxes]", "[]4[]"));
        setBorder(BorderFactory.createTitledBorder(Translations.getText("settings.glitches")));

        for(String availableGlitch : DataFromFile.getAvailableGlitches()) {
            JCheckBox glitchCheckbox = new JCheckBox();
            glitchCheckbox.setSelected(Settings.getEnabledGlitches().contains(availableGlitch));
            glitchCheckbox.setActionCommand(availableGlitch);
            glitchOptions.add(glitchCheckbox);
            add(glitchCheckbox);
        }

        updateTranslations();
    }

    public void updateSettings() {
        List<String> enabledGlitches = new ArrayList<>();
        for(JCheckBox glitchOption : glitchOptions) {
            if(glitchOption.isSelected()) {
                enabledGlitches.add(glitchOption.getActionCommand());
            }
        }
        Settings.setEnabledGlitches(enabledGlitches, true);
    }

    public void updateTranslations() {
        setBorder(BorderFactory.createTitledBorder(Translations.getText("settings.glitches")));
        for(JCheckBox glitchOption : glitchOptions) {
            glitchOption.setText(Translations.getGlitchText(glitchOption.getActionCommand()));
        }
    }

    public void reloadSettings() {
        List<String> enabledGlitches = Settings.getEnabledGlitches();
        for(JCheckBox glitchOption : glitchOptions) {
            glitchOption.setSelected(enabledGlitches.contains(glitchOption.getActionCommand()));
        }
    }
}
