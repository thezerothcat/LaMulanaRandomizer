package lmr.randomizer.ui;

import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Random;

public class MainPanel extends JPanel {
    private JTextField laMulanaDirectory;
    private JTextField laMulanaSaveDirectory;
    private JTextField seedNumber;
    private JComboBox language;

    public MainPanel() {
        super(new MigLayout("fillx", "[][sg fields, fill, grow 80]", "[]"));
        setBorder(BorderFactory.createTitledBorder(Translations.getText("settings.main")));

        seedNumber = new JTextField(Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));
        add(new JLabel(Translations.getText("settings.seed")), "gap related");
        add(seedNumber);

        language = new JComboBox(new String[]{"English", "日本語"});
        language.setSelectedIndex("en".equals(Settings.getLanguage()) ? 0 : 1);
        add(language, "grow 50, wrap");

        laMulanaDirectory = new JTextField(Settings.getLaMulanaBaseDir());
        add(new JLabel(Translations.getText("settings.dir")), "gap related");
        add(laMulanaDirectory, "span 2, grow 100, wrap");

        laMulanaSaveDirectory = new JTextField(Settings.getLaMulanaSaveDir());
        add(new JLabel(Translations.getText("settings.saveDir")), "gap related");
        add(laMulanaSaveDirectory, "span 2, grow 100");
    }

    public void addActionListener(ActionListener actionListener) {
        language.addActionListener(actionListener);
    }

    public void rerollRandomSeed() {
        seedNumber.setText(Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));
    }

    public void updateTranslations() {
        Settings.setLanguage(language.getSelectedIndex() == 0 ? "en" : "jp", true);
        setBorder(BorderFactory.createTitledBorder(Translations.getText("settings.main")));
        seedNumber.setText(Translations.getText("settings.seed"));
        laMulanaDirectory.setText(Translations.getText("settings.dir"));
        laMulanaSaveDirectory.setText(Translations.getText("settings.saveDir"));
    }

    public void updateSettings() {
        try {
            Settings.setStartingSeed(Integer.parseInt(seedNumber.getText()));
            Settings.setLaMulanaBaseDir(laMulanaDirectory.getText(), true);
            Settings.setLaMulanaSaveDir(laMulanaSaveDirectory.getText(), true);
            Settings.setLanguage(language.getSelectedIndex() == 0 ? "en" : "jp", true);
        }
        catch (Exception ex) {
            FileUtils.log("unable to save edit for seedNumber");
        }
    }

    public void reloadSettings() {
        seedNumber.setText(String.valueOf(Settings.getStartingSeed()));
    }
}
