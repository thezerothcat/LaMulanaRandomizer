package lmr.randomizer.ui;

import lmr.randomizer.HolidaySettings;
import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class EventPanel extends JPanel {
    private JCheckBox holidayMode;
    private JCheckBox holidayOption1;
    private JCheckBox fools2021Pt1;
    private JCheckBox fools2021Pt2;
    private JCheckBox fools2021Pt3;
    private JTextField graphicsPack;
    private JLabel graphicsPackLabel;
    private JButton chooseGraphicsButton;

    public EventPanel(TabbedPanel tabbedPanel) {
        super(new MigLayout("fillx, wrap"));

        if (HolidaySettings.isHalloweenMode() || HolidaySettings.isFools2020Mode()) {
            holidayMode = new JCheckBox();
            holidayMode.setSelected(true);
            holidayMode.setEnabled(false);
        }

        if(HolidaySettings.isHalloweenMode()) {
            holidayOption1 = new JCheckBox();
            holidayOption1.setSelected(HolidaySettings.isIncludeHellTempleNPCs());
        }
        if(HolidaySettings.isFools2020Mode()) {
            holidayOption1 = new JCheckBox();
            holidayOption1.setSelected(HolidaySettings.isUpdatedVersion());
        }
        if(HolidaySettings.isFools2021Mode()) {
            fools2021Pt1 = new JCheckBox();
            fools2021Pt1.setSelected(true);
            fools2021Pt1.setEnabled(false);

            fools2021Pt2 = new JCheckBox();
            fools2021Pt2.setSelected(true);
            fools2021Pt2.setEnabled(false);

            fools2021Pt3 = new JCheckBox();
            fools2021Pt3.setSelected(true);
            fools2021Pt3.setEnabled(false);
        }

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        if (HolidaySettings.isHalloweenMode() || HolidaySettings.isFools2020Mode()) {
            checkboxContainer.add(holidayMode);
        }
        if(HolidaySettings.isHalloweenMode()) {
            checkboxContainer.add(holidayOption1);
        }
        if(HolidaySettings.isFools2020Mode()) {
            checkboxContainer.add(holidayOption1);
        }
        if(HolidaySettings.isFools2021Mode()) {
            checkboxContainer.add(fools2021Pt1);
            checkboxContainer.add(fools2021Pt2);
            checkboxContainer.add(fools2021Pt3);
        }
        add(checkboxContainer, "growx, wrap");

        if(HolidaySettings.isHalloweenMode() || HolidaySettings.isFools2020Mode()) {
            graphicsPack = new JTextField(Settings.getGraphicsPack());
            graphicsPackLabel = new JLabel(Translations.getText("settings.graphicsPack"));

            chooseGraphicsButton = new JButton(Translations.getText("button.browse"));
            chooseGraphicsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser graphicsFileChooser = new JFileChooser();
                    graphicsFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    graphicsFileChooser.setCurrentDirectory(new File(Settings.getLaMulanaBaseDir() + "/data/graphics"));
                    if(graphicsFileChooser.showOpenDialog(tabbedPanel.getParent()) == JFileChooser.APPROVE_OPTION) {
                        graphicsPack.setText(graphicsFileChooser.getSelectedFile().getName());
                    }
                }
            });
            JPanel panel = new JPanel(new MigLayout("fillx", "[right]rel[grow,fill]rel[]", "[]10[]10[]"));
            panel.add(graphicsPackLabel);
            panel.add(graphicsPack);
            panel.add(chooseGraphicsButton);
            add(panel, "growx, wrap");
        }

        updateTranslations();
    }

    public void updateTranslations() {
        if(HolidaySettings.isHalloweenMode()) {
            holidayMode.setText(Translations.getText("event.halloween"));
            holidayOption1.setText(Translations.getText("event.includeHTNPCs"));
        }
        else if(HolidaySettings.isFools2020Mode()) {
            holidayMode.setText(Translations.getText("event.fools2020"));
            holidayOption1.setText(Translations.getText("event.useUpdatedVersion"));
        }
        else if(HolidaySettings.isFools2021Mode()) {
            fools2021Pt1.setText(Translations.getText("randomization.fools2021"));
            fools2021Pt2.setText(Translations.getText("randomization.npc.fools2021"));
            fools2021Pt3.setText(Translations.getText("gameplay.fools2021"));
        }
        if(HolidaySettings.isHalloweenMode() || HolidaySettings.isFools2020Mode()) {
            graphicsPackLabel.setText(Translations.getText("settings.graphicsPack"));
            chooseGraphicsButton.setText(Translations.getText("button.browse"));
        }
    }

    public void updateSettings() {
        if(HolidaySettings.isHalloweenMode()) {
            HolidaySettings.setIncludeHellTempleNPCs(holidayOption1.isSelected(), true);
        }
        if(HolidaySettings.isFools2020Mode()) {
            HolidaySettings.setUpdatedVersion(holidayOption1.isSelected(), true);
        }
        if(HolidaySettings.isHalloweenMode() || HolidaySettings.isFools2020Mode()) {
            Settings.setGraphicsPack(graphicsPack.getText(), true);
        }
    }

    public void reloadSettings() {
        if(HolidaySettings.isHalloweenMode()) {
            holidayOption1.setSelected(HolidaySettings.isIncludeHellTempleNPCs());
        }
        if(HolidaySettings.isFools2020Mode()) {
            holidayOption1.setSelected(HolidaySettings.isUpdatedVersion());
        }
        if(HolidaySettings.isHalloweenMode() || HolidaySettings.isFools2020Mode()) {
            graphicsPack.setText(Settings.getGraphicsPack());
        }
    }
}
