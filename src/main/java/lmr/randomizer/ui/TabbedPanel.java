package lmr.randomizer.ui;

import lmr.randomizer.Translations;

import javax.swing.*;

public class TabbedPanel extends JTabbedPane {
    private FoolsPanel foolsPanel;
    private RandomizationPanel randomizationPanel;
    private LogicPanel logicPanel;
    private ChallengePanel challengePanel;
    private SpeedPanel speedPanel;
    private RemovedItemsPanel removedItemsPanel;
    private SettingsTransportPanel settingsTransportPanel;
    private MainPanel mainPanel;

    public TabbedPanel(MainPanel _mainPanel) {
        mainPanel = _mainPanel;

        foolsPanel = new FoolsPanel();
        addTab(Translations.getText("fools.randomization"), foolsPanel);

        randomizationPanel = new RandomizationPanel();
        addTab(Translations.getText("settings.randomization"), randomizationPanel);

        logicPanel = new LogicPanel();
        addTab(Translations.getText("settings.logic"), logicPanel);

        challengePanel = new ChallengePanel();
        addTab(Translations.getText("settings.challenge"), challengePanel);

        speedPanel = new SpeedPanel();
        addTab(Translations.getText("settings.speed"), speedPanel);

        removedItemsPanel = new RemovedItemsPanel();
        addTab(Translations.getText("settings.removal"), removedItemsPanel);

        settingsTransportPanel = new SettingsTransportPanel(this);
        addTab(Translations.getText("settings.share"), settingsTransportPanel);
    }

    public void reloadSettings() {
        mainPanel.reloadSettings();
        foolsPanel.reloadSettings();
        randomizationPanel.reloadSettings();
        logicPanel.reloadSettings();
        challengePanel.reloadSettings();
        speedPanel.reloadSettings();
        removedItemsPanel.reloadSettings();
    }

    public void updateSettings() {
        mainPanel.updateSettings();
        foolsPanel.updateSettings();
        randomizationPanel.updateSettings();
        logicPanel.updateSettings();
        challengePanel.updateSettings();
        speedPanel.updateSettings();
        removedItemsPanel.updateSettings();
    }

    public void updateTranslations() {
        foolsPanel.updateTranslations();
        randomizationPanel.updateTranslations();
        logicPanel.updateTranslations();
        challengePanel.updateTranslations();
        speedPanel.updateTranslations();
        removedItemsPanel.updateTranslations();

        setTitleAt(0, Translations.getText("settings.randomization"));
        setTitleAt(1, Translations.getText("settings.logic"));
        setTitleAt(2, Translations.getText("settings.challenge"));
        setTitleAt(3, Translations.getText("settings.speed"));
        setTitleAt(4, Translations.getText("settings.removal"));
        setTitleAt(5, Translations.getText("settings.share"));
    }
}
