package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import lmr.randomizer.random.BossDifficulty;

import javax.swing.*;
import java.awt.*;

public class DifficultyPanel extends JPanel {
    private ButtonGroup difficultySetting;
    private JLabel label;
    private JRadioButton medium;
    private JRadioButton hard;

    public DifficultyPanel() {
        super(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        label = new JLabel(Translations.getText("logic.bossDifficulty"), JLabel.LEFT);
        add(label);

        difficultySetting = new ButtonGroup();
//            JRadioButton easy = new JRadioButton("Easy");
//            easy.setActionCommand("EASY");
        medium = new JRadioButton(Translations.getText("logic.bossDifficulty.lower"));
        medium.setActionCommand("MEDIUM");
        hard = new JRadioButton(Translations.getText("logic.bossDifficulty.higher"));
        hard.setActionCommand("HARD");
//            difficultySetting.add(easy);
        difficultySetting.add(medium);
        difficultySetting.add(hard);

//            add(easy);
        add(medium);
        add(hard);

        if(BossDifficulty.EASY.equals(Settings.getBossDifficulty())) {
            medium.setSelected(true); // todo: easy is not a setting yet
        }
        else if(BossDifficulty.MEDIUM.equals(Settings.getBossDifficulty())) {
            medium.setSelected(true);
        }
        else {
            hard.setSelected(true);
        }
    }

    public void updateSettings() {
        Settings.setBossDifficulty(difficultySetting.getSelection().getActionCommand(), true);
    }

    public void updateTranslations() {
        label.setText(Translations.getText("logic.bossDifficulty"));
        medium.setText(Translations.getText("logic.bossDifficulty.lower"));
        hard.setText(Translations.getText("logic.bossDifficulty.higher"));
    }

    public void reloadSettings() {
        if("MEDIUM".equals(Settings.getBossDifficulty().toString())) {
            medium.setSelected(true);
        }
        if("HARD".equals(Settings.getBossDifficulty().toString())) {
            hard.setSelected(true);
        }
    }
}
