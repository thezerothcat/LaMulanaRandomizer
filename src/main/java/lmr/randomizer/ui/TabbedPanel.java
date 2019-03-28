package lmr.randomizer.ui;

import lmr.randomizer.Translations;

import javax.swing.*;

public class TabbedPanel extends JTabbedPane {
    private RandomizationPanel randomizationPanel;
    private LogicPanel logicPanel;
    private ChallengePanel challengePanel;
    private GameplayChangesPanel gameplayChangesPanel;
    private RemovedItemsPanel removedItemsPanel;
    private SettingsTransportPanel settingsTransportPanel;
    private MainPanel mainPanel;

    public TabbedPanel(MainPanel _mainPanel) {
        mainPanel = _mainPanel;

        randomizationPanel = new RandomizationPanel();
        addTab(Translations.getText("settings.randomization"), randomizationPanel);

        logicPanel = new LogicPanel();
        addTab(Translations.getText("settings.logic"), logicPanel);

        challengePanel = new ChallengePanel();
        addTab(Translations.getText("settings.challenge"), challengePanel);

        gameplayChangesPanel = new GameplayChangesPanel();
        addTab(Translations.getText("settings.gameplay"), gameplayChangesPanel);

        removedItemsPanel = new RemovedItemsPanel();
        addTab(Translations.getText("settings.removal"), removedItemsPanel);

        settingsTransportPanel = new SettingsTransportPanel(this);
        addTab(Translations.getText("settings.share"), settingsTransportPanel);
    }

    public void reloadSettings() {
        mainPanel.reloadSettings();
        randomizationPanel.reloadSettings();
        logicPanel.reloadSettings();
        challengePanel.reloadSettings();
        gameplayChangesPanel.reloadSettings();
        removedItemsPanel.reloadSettings();
    }

    public void updateSettings() {
        mainPanel.updateSettings();
        randomizationPanel.updateSettings();
        logicPanel.updateSettings();
        challengePanel.updateSettings();
        gameplayChangesPanel.updateSettings();
        removedItemsPanel.updateSettings();
    }

    public void updateTranslations() {
        randomizationPanel.updateTranslations();
        logicPanel.updateTranslations();
        challengePanel.updateTranslations();
        gameplayChangesPanel.updateTranslations();
        removedItemsPanel.updateTranslations();

        setTitleAt(0, Translations.getText("settings.randomization"));
        setTitleAt(1, Translations.getText("settings.logic"));
        setTitleAt(2, Translations.getText("settings.challenge"));
        setTitleAt(3, Translations.getText("settings.gameplay"));
        setTitleAt(4, Translations.getText("settings.removal"));
        setTitleAt(5, Translations.getText("settings.share"));
    }
}
