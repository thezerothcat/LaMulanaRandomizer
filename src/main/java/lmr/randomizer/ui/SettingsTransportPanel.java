package lmr.randomizer.ui;

import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsTransportPanel extends JPanel {
    JButton generateSettingsButton;
    JButton settingsImportButton;
    JTextField stringField;
    TabbedPanel parent;

    public SettingsTransportPanel(TabbedPanel tabbedPanel) {
        super(new MigLayout("center"));

        parent = tabbedPanel;

        stringField = new JTextField();
        add(stringField, "w 300!");

        generateSettingsButton = new JButton("Generate settings string");
        generateSettingsButton.addActionListener(e -> generateSettingsString());
        add(generateSettingsButton, "w 150!");

        settingsImportButton = new JButton("Import settings string");
        settingsImportButton.addActionListener(e -> importSettings(tabbedPanel));

        add(settingsImportButton, "w 150!");
    }

    private void generateSettingsString() {
        parent.updateSettings();

        stringField.setText(Settings.generateShortString());
    }

    private void importSettings(TabbedPanel tabbedPanel) {
        Settings.importShortString(stringField.getText());
        tabbedPanel.reloadSettings();
    }
}

