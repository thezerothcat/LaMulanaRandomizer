package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class EventPanel extends JPanel {
    private JCheckBox halloweenMode;
    private JCheckBox includeHT;
    private JTextField graphicsPack;
    private JLabel graphicsPackLabel;
    private JButton chooseGraphicsButton;

    public EventPanel(TabbedPanel tabbedPanel) {
        super(new MigLayout("fillx, wrap"));

        halloweenMode = new JCheckBox();
        halloweenMode.setSelected(true);
        halloweenMode.setEnabled(false);

        includeHT = new JCheckBox();
        includeHT.setSelected(true);
        includeHT.setSelected(Settings.isIncludeHellTempleNPCs());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(halloweenMode);
        checkboxContainer.add(includeHT);
        add(checkboxContainer, "growx, wrap");

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

        updateTranslations();
    }

    public void updateTranslations() {
        halloweenMode.setText(Translations.getText("event.halloween"));
        includeHT.setText(Translations.getText("event.includeHTNPCs"));
        graphicsPackLabel.setText(Translations.getText("settings.graphicsPack"));
        chooseGraphicsButton.setText(Translations.getText("button.browse"));
    }

    public void updateSettings() {
        Settings.setIncludeHellTempleNPCs(includeHT.isSelected(), true);
        Settings.setGraphicsPack(graphicsPack.getText(), true);
    }

    public void reloadSettings() {
        includeHT.setSelected(Settings.isIncludeHellTempleNPCs());
        graphicsPack.setText(Settings.getGraphicsPack());
    }
}
