package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class GameItemRadio extends JPanel {
    private ButtonGroup itemRandomization;
    private String itemName;
    private JLabel itemLabel;
    private JRadioButton randomItem;
    private JRadioButton initialItem;
    private JRadioButton nonrandomOrSurfaceItem;

    public GameItemRadio(String item) {
        super(new MigLayout("gap rel 0, wrap, aligny top", "sizegroup rowheight", ""));

        itemLabel = new JLabel(Translations.getItemText(item), JLabel.LEFT);
        itemLabel.setVerticalAlignment(JLabel.TOP);
        add(itemLabel);

        itemRandomization = new ButtonGroup();

        randomItem = new JRadioButton(Translations.getText("randomization.random"));
        randomItem.setActionCommand("RANDOM");
        itemRandomization.add(randomItem);

        initialItem = new JRadioButton(Translations.getText("randomization.initial"));
        initialItem.setActionCommand("INITIAL");
        itemRandomization.add(initialItem);

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(randomItem);
        checkboxContainer.add(initialItem);
        add(checkboxContainer);

        if("Holy Grail".equals(item)) {
            nonrandomOrSurfaceItem = new JRadioButton(Translations.getText("randomization.nonrandom"));
            nonrandomOrSurfaceItem.setActionCommand("NONRANDOM");
            itemRandomization.add(nonrandomOrSurfaceItem);
            checkboxContainer.add(nonrandomOrSurfaceItem);
        }
        else if("Hand Scanner".equals(item) || "reader.exe".equals(item) || "Hermes' Boots".equals(item)
                || "Feather".equals(item) || "Grapple Claw".equals(item) || "bunemon.exe".equals(item)) {
            nonrandomOrSurfaceItem = new JRadioButton(Translations.getText("randomization.surface"));
            nonrandomOrSurfaceItem.setActionCommand("V_EARLY");
            itemRandomization.add(nonrandomOrSurfaceItem);
            checkboxContainer.add(nonrandomOrSurfaceItem);
        }
        else {
            nonrandomOrSurfaceItem = null;
        }

        if(Settings.getInitiallyAccessibleItems().contains(item)) {
            initialItem.setSelected(true);
        }
        else if(Settings.getNonRandomizedItems().contains(item)
            || Settings.getSurfaceItems().contains(item)) {
            nonrandomOrSurfaceItem.setSelected(true);
        }
        else {
            randomItem.setSelected(true);
        }

        this.itemName = item;
    }

    public String getActionCommand() {
        return itemRandomization.getSelection().getActionCommand();
    }

    public String getItemName() {
        return itemName;
    }

    public void updateTranslations() {
        itemLabel.setText(Translations.getItemText(itemName));
        randomItem.setText(Translations.getText("randomization.random"));
        initialItem.setText(Translations.getText("randomization.initial"));
        if(nonrandomOrSurfaceItem != null) {
            if("Holy Grail".equals(itemName)) {
                nonrandomOrSurfaceItem.setText(Translations.getText("randomization.nonrandom"));
            }
            else {
                nonrandomOrSurfaceItem.setText(Translations.getText("randomization.surface"));
            }
        }
    }

    public void setSelected(String actionCommand) {
        if("RANDOM".equals(actionCommand)) {
            randomItem.setSelected(true);
        } else if("INITIAL".equals(actionCommand)) {
            initialItem.setSelected(true);
        } else if ("SURFACE".equals(actionCommand)) {
            nonrandomOrSurfaceItem.setSelected(true);
        } else {
            if(nonrandomOrSurfaceItem != null) {
                nonrandomOrSurfaceItem.setSelected(true);
            } else {
                randomItem.setSelected(true);
            }
        }
    }
}
