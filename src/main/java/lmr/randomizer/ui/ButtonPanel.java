package lmr.randomizer.ui;

import lmr.randomizer.Main;
import lmr.randomizer.Translations;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel {
    JButton applyButton;
    JButton restoreButton;

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
    }

    public void updateTranslations() {
        applyButton.setText(Translations.getText("button.apply"));
        restoreButton.setText(Translations.getText("button.restore"));
    }
}
