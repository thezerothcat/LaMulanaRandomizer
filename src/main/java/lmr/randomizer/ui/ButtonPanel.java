package lmr.randomizer.ui;

import lmr.randomizer.FileUtils;
import lmr.randomizer.Main;
import lmr.randomizer.Translations;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel {
    JButton applyButton;
    JButton restoreButton;
    JButton restoreSavesButton;
    JButton seedImportButton;

    public ButtonPanel(Main.RandomizerUI randomizerUI) {
        super(new FlowLayout());

        applyButton = new JButton(Translations.getText("button.apply"));
        applyButton.addActionListener(randomizerUI);
        applyButton.setActionCommand("apply");
        add(applyButton);

        restoreButton = new JButton(Translations.getText("button.restore"));
        restoreButton.addActionListener(randomizerUI);
        restoreButton.setActionCommand("restore");
        add(restoreButton);

        restoreSavesButton = new JButton(Translations.getText("button.restoreSaves"));
        restoreSavesButton.addActionListener(randomizerUI);
        restoreSavesButton.setActionCommand("restoreSaves");
        add(restoreSavesButton);

        seedImportButton = new JButton(Translations.getText("button.importSeed"));
        seedImportButton.addActionListener(randomizerUI);
        seedImportButton.setActionCommand("importSeed");
        add(seedImportButton);
    }

    public void updateTranslations() {
        applyButton.setText(Translations.getText("button.apply"));
        restoreButton.setText(Translations.getText("button.restore"));
        restoreSavesButton.setText(Translations.getText("button.restoreSaves"));
        seedImportButton.setText(Translations.getText("button.importSeed"));
    }
}
