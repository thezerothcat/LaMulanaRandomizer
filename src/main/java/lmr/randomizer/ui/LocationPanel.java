package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LocationPanel extends JPanel {
    private JCheckBox randomizeBacksideDoors;
    private JCheckBox randomizeNonBossDoors;
    private JCheckBox randomizeTransitionGates;
    private JCheckBox randomizeOneWayTransitions;
    private JCheckBox randomizeStartingLocation;

    public LocationPanel() {
        super(new MigLayout("fillx, wrap"));

        randomizeBacksideDoors = new JCheckBox();
        randomizeBacksideDoors.setSelected(Settings.isRandomizeBacksideDoors());

        randomizeNonBossDoors = new JCheckBox();
        randomizeNonBossDoors.setSelected(Settings.isRandomizeNonBossDoors());
        randomizeNonBossDoors.setEnabled(Settings.isRandomizeBacksideDoors());

        randomizeBacksideDoors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() instanceof JCheckBox) {
                    randomizeNonBossDoors.setEnabled(((JCheckBox)e.getSource()).isSelected());
                }
            }
        });

        randomizeTransitionGates = new JCheckBox();
        randomizeTransitionGates.setSelected(Settings.isRandomizeTransitionGates());

        randomizeOneWayTransitions = new JCheckBox();
        randomizeOneWayTransitions.setSelected(Settings.isRandomizeOneWayTransitions());
        randomizeOneWayTransitions.setEnabled(Settings.isRandomizeTransitionGates());

        randomizeTransitionGates.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() instanceof JCheckBox) {
                    randomizeOneWayTransitions.setEnabled(((JCheckBox)e.getSource()).isSelected());
                }
            }
        });

        randomizeStartingLocation = new JCheckBox();
        randomizeStartingLocation.setSelected(Settings.isRandomizeStartingLocation());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(randomizeBacksideDoors);
        checkboxContainer.add(randomizeNonBossDoors);
        checkboxContainer.add(randomizeTransitionGates);
        checkboxContainer.add(randomizeOneWayTransitions);
        checkboxContainer.add(randomizeStartingLocation);
        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        randomizeBacksideDoors.setText(Translations.getText("randomization.randomizeBacksideDoors"));
        randomizeNonBossDoors.setText(Translations.getText("randomization.randomizeNonBossDoors"));
        randomizeTransitionGates.setText(Translations.getText("randomization.randomizeTransitionGates"));
        randomizeOneWayTransitions.setText(Translations.getText("randomization.randomizeOneWayTransitions"));
        randomizeStartingLocation.setText(Translations.getText("randomization.randomizeStartingLocation"));
    }

    public void updateSettings() {
        Settings.setRandomizeBacksideDoors(randomizeBacksideDoors.isSelected(), true);
        Settings.setRandomizeTransitionGates(randomizeTransitionGates.isSelected(), true);
        Settings.setRandomizeOneWayTransitions(randomizeOneWayTransitions.isEnabled() && randomizeOneWayTransitions.isSelected(), true);
        Settings.setRandomizeNonBossDoors(randomizeNonBossDoors.isEnabled() && randomizeNonBossDoors.isSelected(), true);
        Settings.setRandomizeStartingLocation(randomizeStartingLocation.isSelected(), true);
    }

    public void reloadSettings() {
        randomizeBacksideDoors.setSelected(Settings.isRandomizeBacksideDoors());
        randomizeTransitionGates.setSelected(Settings.isRandomizeTransitionGates());
        randomizeOneWayTransitions.setSelected(Settings.isRandomizeOneWayTransitions());
        randomizeOneWayTransitions.setEnabled(Settings.isRandomizeTransitionGates());
        randomizeNonBossDoors.setSelected(Settings.isRandomizeNonBossDoors());
        randomizeNonBossDoors.setEnabled(Settings.isRandomizeBacksideDoors());
        randomizeStartingLocation.setSelected(Settings.isRandomizeStartingLocation());
    }
}
