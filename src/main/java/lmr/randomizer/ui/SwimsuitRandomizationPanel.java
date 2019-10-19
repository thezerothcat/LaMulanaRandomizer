package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class SwimsuitRandomizationPanel extends JPanel {
    private ButtonGroup swimsuitItem;
    private JLabel swimsuitItemLabel;

    private JRadioButton nonrandom;
    private JRadioButton mapOnly;
    private JRadioButton random;

    public SwimsuitRandomizationPanel() {
        super(new MigLayout("gapy 0, insets 8 0 0 0"));

        swimsuitItemLabel = new JLabel(Translations.getText("randomization.swimsuitItem"), JLabel.LEFT);
        add(swimsuitItemLabel);

        swimsuitItem = new ButtonGroup();

        nonrandom = new JRadioButton(Translations.getText("randomization.swimsuitItem.nonrandom"));
        nonrandom.setActionCommand("NONRANDOM");
        swimsuitItem.add(nonrandom);

        mapOnly = new JRadioButton(Translations.getText("randomization.swimsuitItem.mapOnly"));
        mapOnly.setActionCommand("MAPONLY");
        swimsuitItem.add(mapOnly);

        random = new JRadioButton(Translations.getText("randomization.random"));
        random.setActionCommand("RANDOM");
        swimsuitItem.add(random);

        add(nonrandom);
        add(mapOnly);
        add(random);

        if(!Settings.isRandomizeForbiddenTreasure()) {
            nonrandom.setSelected(true);
        }
        else if(!Settings.isHTFullRandom()) {
            mapOnly.setSelected(true);
        }
        else {
            random.setSelected(true);
        }
    }

    public void updateSettings() {
        if("NONRANDOM".equals(swimsuitItem.getSelection().getActionCommand())) {
            Settings.setRandomizeForbiddenTreasure(false, true);
            Settings.setHTFullRandom(false, true);
        }
        else if("MAPONLY".equals(swimsuitItem.getSelection().getActionCommand())) {
            Settings.setRandomizeForbiddenTreasure(true, true);
            Settings.setHTFullRandom(false, true);
        }
        else {
            Settings.setRandomizeForbiddenTreasure(true, true);
            Settings.setHTFullRandom(true, true);
        }
    }

    public void updateTranslations() {
        swimsuitItemLabel.setText(Translations.getText("randomization.swimsuitItem"));
        nonrandom.setText(Translations.getText("randomization.swimsuitItem.nonrandom"));
        mapOnly.setText(Translations.getText("randomization.swimsuitItem.mapOnly"));
        random.setText(Translations.getText("randomization.random"));
    }

    public void reloadSettings() {
        if(!Settings.isRandomizeForbiddenTreasure()) {
            nonrandom.setSelected(true);
        }
        else if(!Settings.isHTFullRandom()) {
            mapOnly.setSelected(true);
        }
        else {
            random.setSelected(true);
        }
    }
}
