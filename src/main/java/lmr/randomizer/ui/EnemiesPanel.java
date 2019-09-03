package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class EnemiesPanel extends JPanel {
    private JCheckBox randomizeEnemies;
//    private JCheckBox randomizeBosses;

    public EnemiesPanel() {
        super(new MigLayout("fillx, wrap"));

        randomizeEnemies = new JCheckBox();
        randomizeEnemies.setSelected(Settings.isRandomizeEnemies());

//        randomizeBosses = new JCheckBox();
//        randomizeBosses.setSelected(Settings.isRandomizeBosses());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(randomizeEnemies);
//        checkboxContainer.add(randomizeBosses);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        randomizeEnemies.setText(Translations.getText("enemies.randomizeEnemies"));
//        randomizeBosses.setText(Translations.getText("fools.randomizeBosses"));
    }

    public void updateSettings() {
        Settings.setRandomizeEnemies(randomizeEnemies.isSelected(), true);
//        Settings.setRandomizeBosses(randomizeBosses.isSelected(), true);
    }

    public void reloadSettings() {
        randomizeEnemies.setSelected(Settings.isRandomizeEnemies());
//        randomizeBosses.setSelected(Settings.isRandomizeBosses());
    }
}
