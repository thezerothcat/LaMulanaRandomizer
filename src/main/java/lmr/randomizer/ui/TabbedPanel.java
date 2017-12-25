package lmr.randomizer.ui;

import lmr.randomizer.Translations;

import javax.swing.*;

public class TabbedPanel extends JTabbedPane {
    private RandomizationPanel randomizationPanel;
    private LogicPanel logicPanel;
    private ChallengePanel challengePanel;
    private DboostPanel dboostPanel;
    private GlitchPanel glitchPanel;
    private SpeedPanel speedPanel;
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

        dboostPanel = new DboostPanel();
        addTab(Translations.getText("settings.dboost"), dboostPanel);

        glitchPanel = new GlitchPanel();
        addTab(Translations.getText("settings.glitches"), glitchPanel);

        speedPanel = new SpeedPanel();
        addTab(Translations.getText("settings.speed"), speedPanel);

        removedItemsPanel = new RemovedItemsPanel();
        addTab(Translations.getText("settings.removal"), removedItemsPanel);

        settingsTransportPanel = new SettingsTransportPanel(this);
        addTab(Translations.getText("settings.share"), settingsTransportPanel);
    }

    public void reloadSettings() {
        mainPanel.reloadSettings();
        randomizationPanel.reloadSettings();
        dboostPanel.reloadSettings();
        glitchPanel.reloadSettings();
        logicPanel.reloadSettings();
        challengePanel.reloadSettings();
        speedPanel.reloadSettings();
        removedItemsPanel.reloadSettings();
    }

    public void updateSettings() {
        mainPanel.updateSettings();
        randomizationPanel.updateSettings();
        dboostPanel.updateSettings();
        glitchPanel.updateSettings();
        logicPanel.updateSettings();
        challengePanel.updateSettings();
        speedPanel.updateSettings();
        removedItemsPanel.updateSettings();
    }

    public void updateTranslations() {
        randomizationPanel.updateTranslations();
        dboostPanel.updateTranslations();
        glitchPanel.updateTranslations();
        logicPanel.updateTranslations();
        challengePanel.updateTranslations();
        speedPanel.updateTranslations();
        removedItemsPanel.updateTranslations();

        setTitleAt(0, Translations.getText("settings.randomization"));
        setTitleAt(1, Translations.getText("settings.logic"));
        setTitleAt(2, Translations.getText("settings.challenge"));
        setTitleAt(3, Translations.getText("settings.dboost"));
        setTitleAt(4, Translations.getText("settings.glitches"));
        setTitleAt(5, Translations.getText("settings.speed"));
        setTitleAt(6, Translations.getText("settings.removal"));
        setTitleAt(7, Translations.getText("settings.share"));
    }
}
