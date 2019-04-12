package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class EnemiesPanel extends JPanel {
    private JCheckBox randomizeBosses;
    private JCheckBox randomizeEnemies;

    public EnemiesPanel() {
        super(new MigLayout("fillx, wrap"));

        randomizeBosses = new JCheckBox();
        randomizeBosses.setSelected(Settings.isRandomizeBosses());

        randomizeEnemies = new JCheckBox();
        randomizeEnemies.setSelected(Settings.isRandomizeEnemies());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(randomizeBosses);
        checkboxContainer.add(randomizeEnemies);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        randomizeBosses.setText(Translations.getText("fools.randomizeBosses"));
        randomizeEnemies.setText(Translations.getText("fools.randomizeEnemies"));
    }

    public void updateSettings() {
        Settings.setRandomizeBosses(randomizeBosses.isSelected(), true);
        Settings.setRandomizeEnemies(randomizeEnemies.isSelected(), true);
    }

    public void reloadSettings() {
        randomizeBosses.setSelected(Settings.isRandomizeBosses());
        randomizeEnemies.setSelected(Settings.isRandomizeEnemies());
    }
}
