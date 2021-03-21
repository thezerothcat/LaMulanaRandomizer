package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class EventPanel extends JPanel {
    private JCheckBox holidayMode;
    private JCheckBox includeHT;
    private JCheckBox foolsGameplay;
    private JCheckBox foolsLogic;
    private JTextField graphicsPack;
    private JLabel graphicsPackLabel;
    private JButton chooseGraphicsButton;

    public EventPanel(TabbedPanel tabbedPanel) {
        super(new MigLayout("fillx, wrap"));

        holidayMode = new JCheckBox();
        holidayMode.setSelected(true);
        if (Settings.isHalloweenMode() || Settings.isFools2020Mode() || Settings.isFools2021Mode()) {
            holidayMode.setEnabled(false);
        }

        if(Settings.isHalloweenMode()) {
            includeHT = new JCheckBox();
            includeHT.setSelected(Settings.isIncludeHellTempleNPCs());
        }
        if(Settings.isFools2021Mode()) {
            foolsGameplay = new JCheckBox();
            foolsGameplay.setSelected(true);
            foolsGameplay.setEnabled(false);

            foolsLogic = new JCheckBox();
            foolsLogic.setSelected(true);
            foolsLogic.setEnabled(false);
        }

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(holidayMode);
        if(Settings.isHalloweenMode()) {
            checkboxContainer.add(includeHT);
        }
        if(Settings.isFools2021Mode()) {
            checkboxContainer.add(foolsGameplay);
            checkboxContainer.add(foolsLogic);
        }
        add(checkboxContainer, "growx, wrap");

        if(Settings.isHalloweenMode() || Settings.isFools2020Mode()) {
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
        if(Settings.isHalloweenMode()) {
            holidayMode.setText(Translations.getText("event.halloween"));
            includeHT.setText(Translations.getText("event.includeHTNPCs"));
        }
        else if(Settings.isFools2020Mode()) {
            holidayMode.setText(Translations.getText("event.fools2020"));
        }
        else if(Settings.isFools2021Mode()) {
            holidayMode.setText(Translations.getText("event.fools2021"));
            foolsGameplay.setText(Translations.getText("gameplay.fools2021"));
            foolsLogic.setText(Translations.getText("randomization.fools2021"));
        }
        if(Settings.isHalloweenMode() || Settings.isFools2020Mode()) {
            graphicsPackLabel.setText(Translations.getText("settings.graphicsPack"));
            chooseGraphicsButton.setText(Translations.getText("button.browse"));
        }
    }

    public void updateSettings() {
        if(Settings.isHalloweenMode()) {
            Settings.setIncludeHellTempleNPCs(includeHT.isSelected(), true);
        }
        if(Settings.isFools2021Mode()) {
            Settings.setFoolsGameplay(foolsGameplay.isSelected(), true);
            Settings.setFoolsLogic(foolsLogic.isSelected(), true);
        }
        if(Settings.isHalloweenMode() || Settings.isFools2020Mode()) {
            Settings.setGraphicsPack(graphicsPack.getText(), true);
        }
    }

    public void reloadSettings() {
        if(Settings.isHalloweenMode()) {
            includeHT.setSelected(Settings.isIncludeHellTempleNPCs());
        }
        if(Settings.isFools2021Mode()) {
            foolsGameplay.setSelected(Settings.isFoolsGameplay());
            foolsLogic.setSelected(Settings.isFoolsLogic());
        }
        if(Settings.isHalloweenMode() || Settings.isFools2020Mode()) {
            graphicsPack.setText(Settings.getGraphicsPack());
        }
    }
}
