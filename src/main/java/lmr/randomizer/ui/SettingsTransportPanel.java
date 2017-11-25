package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class SettingsTransportPanel extends JPanel {
    JButton generateSettingsButton;
    JButton importButton;
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

        importButton = new JButton("Import settings string");
        importButton.addActionListener(e -> importSettings(tabbedPanel));

        add(importButton, "w 150!");
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

