package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DboostPanel extends JPanel {
    private JCheckBox itemBased;
    private JCheckBox enemyBased;
    private JCheckBox environmentBased;

    public DboostPanel() {
        super(new MigLayout("fillx"));
        setBorder(BorderFactory.createTitledBorder(Translations.getText("settings.dboost")));

        itemBased = new JCheckBox();
        itemBased.setSelected(Settings.getEnabledDamageBoosts().contains("Item"));
        itemBased.setActionCommand("Item");

        environmentBased = new JCheckBox();
        environmentBased.setSelected(Settings.getEnabledDamageBoosts().contains("Environment"));
        environmentBased.setActionCommand("Environment");

        enemyBased = new JCheckBox();
        enemyBased.setSelected(Settings.getEnabledDamageBoosts().contains("Enemy"));
        enemyBased.setActionCommand("Enemy");

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(itemBased);
        checkboxContainer.add(environmentBased);
        checkboxContainer.add(enemyBased);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateSettings() {
        List<String> enabledDamageBoosts = new ArrayList<>();
        for(JCheckBox dboostOption : Arrays.asList(itemBased, environmentBased, enemyBased)) {
            if(dboostOption.isSelected()) {
                enabledDamageBoosts.add(dboostOption.getActionCommand());
            }
        }
        Settings.setEnabledDamageBoosts(enabledDamageBoosts, true);
    }

    public void updateTranslations() {
        setBorder(BorderFactory.createTitledBorder(Translations.getText("settings.dboost")));
        itemBased.setText(Translations.getText("dboost.Item"));
        environmentBased.setText(Translations.getText("dboost.Environment"));
        enemyBased.setText(Translations.getText("dboost.Enemy"));
    }

    public void reloadSettings() {
        List<String> enabledDamageBoosts = Settings.getEnabledDamageBoosts();
        for(JCheckBox dboostOption : Arrays.asList(itemBased, environmentBased, enemyBased)) {
            dboostOption.setSelected(enabledDamageBoosts.contains(dboostOption.getActionCommand()));
        }
    }
}
