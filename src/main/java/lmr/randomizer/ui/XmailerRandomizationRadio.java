package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class XmailerRandomizationRadio extends JPanel {
    private ButtonGroup xmailerItem;
    private JLabel xmailerItemLabel;

    private JRadioButton random;
    private JRadioButton hermes;
    private JRadioButton textTrax;
    private JRadioButton xmailer;

    public XmailerRandomizationRadio() {
        super(new MigLayout("gap rel 0"));

        xmailerItemLabel = new JLabel(Translations.getText("randomization.xmailerItem"), JLabel.LEFT);
        add(xmailerItemLabel);

        xmailerItem = new ButtonGroup();

        xmailer = new JRadioButton(Translations.getText("randomization.xmailerItem.xmailer"));
        xmailer.setActionCommand("xmailer.exe");
        xmailerItem.add(xmailer);

        hermes = new JRadioButton(Translations.getText("randomization.xmailerItem.hermes"));
        hermes.setActionCommand("Hermes' Boots");
        xmailerItem.add(hermes);

        textTrax = new JRadioButton(Translations.getText("randomization.xmailerItem.textTrax"));
        textTrax.setActionCommand("bunemon.exe");
        xmailerItem.add(textTrax);

        random = new JRadioButton(Translations.getText("randomization.random"));
        random.setActionCommand(null);
        xmailerItem.add(random);

        add(xmailer);
        add(hermes);
        add(textTrax);
        add(random);

        if("Hermes' Boots".equals(Settings.getXmailerItem())) {
            hermes.setSelected(true);
        }
        else if("xmailer.exe".equals(Settings.getXmailerItem())) {
            xmailer.setSelected(true);
        }
        else if("bunemon.exe".equals(Settings.getXmailerItem())) {
            textTrax.setSelected(true);
        }
        else {
            random.setSelected(true);
        }
    }

    public void updateSettings() {
        if(xmailerItem.getSelection().getActionCommand() == null) {
            Settings.setXmailerItem(null, true);
        }
        else {
            Settings.setXmailerItem(xmailerItem.getSelection().getActionCommand(), true);
        }
    }

    public void updateTranslations() {
        xmailerItemLabel.setText(Translations.getText("randomization.xmailerItem"));
        xmailer.setText(Translations.getText("randomization.xmailerItem.xmailer"));
        hermes.setText(Translations.getText("randomization.xmailerItem.hermes"));
        textTrax.setText(Translations.getText("randomization.xmailerItem.textTrax"));
        random.setText(Translations.getText("randomization.random"));
    }

    public void reloadSettings() {
        if("Hermes' Boots".equals(Settings.getXmailerItem())) {
            hermes.setSelected(true);
        }
        else if("xmailer.exe".equals(Settings.getXmailerItem())) {
            xmailer.setSelected(true);
        }
        else if("bunemon.exe".equals(Settings.getXmailerItem())) {
            textTrax.setSelected(true);
        }
        else {
            random.setSelected(true);
        }
    }
}
