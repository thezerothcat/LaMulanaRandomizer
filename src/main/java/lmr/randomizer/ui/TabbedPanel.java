package lmr.randomizer.ui;

import lmr.randomizer.Translations;

import javax.swing.*;

public class TabbedPanel extends JTabbedPane {
    private EventPanel eventPanel;
    private RandomizationPanel randomizationPanel;
    private LocationPanel locationPanel;
    private EnemiesPanel enemiesPanel;
    private LogicPanel logicPanel;
    private AppearancePanel appearancePanel;
    private GameplayChangesPanel gameplayChangesPanel;
    private RemovedItemsPanel removedItemsPanel;
    private SettingsTransportPanel settingsTransportPanel;
    private MainPanel mainPanel;

    public TabbedPanel(MainPanel _mainPanel) {
        mainPanel = _mainPanel;

        eventPanel = new EventPanel();
        addTab(Translations.getText("settings.event"), eventPanel);

        randomizationPanel = new RandomizationPanel();
        addTab(Translations.getText("settings.items"), randomizationPanel);

        locationPanel = new LocationPanel();
        addTab(Translations.getText("settings.locations"), locationPanel);

        enemiesPanel = new EnemiesPanel();
        addTab(Translations.getText("settings.enemies"), enemiesPanel);

        logicPanel = new LogicPanel();
        addTab(Translations.getText("settings.logic"), logicPanel);

        appearancePanel = new AppearancePanel();
        addTab(Translations.getText("settings.appearance"), appearancePanel);

        gameplayChangesPanel = new GameplayChangesPanel();
        addTab(Translations.getText("settings.gameplay"), gameplayChangesPanel);

        removedItemsPanel = new RemovedItemsPanel();
        addTab(Translations.getText("settings.removal"), removedItemsPanel);

        settingsTransportPanel = new SettingsTransportPanel(this);
        addTab(Translations.getText("settings.share"), settingsTransportPanel);
    }

    public void reloadSettings() {
        mainPanel.reloadSettings();
        eventPanel.reloadSettings();
        randomizationPanel.reloadSettings();
        locationPanel.reloadSettings();
        enemiesPanel.reloadSettings();
        logicPanel.reloadSettings();
        appearancePanel.reloadSettings();
        gameplayChangesPanel.reloadSettings();
        removedItemsPanel.reloadSettings();
    }

    public void updateSettings() {
        eventPanel.updateSettings();
        mainPanel.updateSettings();
        randomizationPanel.updateSettings();
        locationPanel.updateSettings();
        enemiesPanel.updateSettings();
        logicPanel.updateSettings();
        appearancePanel.updateSettings();
        gameplayChangesPanel.updateSettings();
        removedItemsPanel.updateSettings();
    }

    public void updateTranslations() {
        eventPanel.updateTranslations();
        randomizationPanel.updateTranslations();
        locationPanel.updateTranslations();
        enemiesPanel.updateTranslations();
        logicPanel.updateTranslations();
        appearancePanel.updateTranslations();
        gameplayChangesPanel.updateTranslations();
        removedItemsPanel.updateTranslations();

        setTitleAt(0, Translations.getText("settings.event"));
        setTitleAt(1, Translations.getText("settings.items"));
        setTitleAt(2, Translations.getText("settings.locations"));
        setTitleAt(3, Translations.getText("settings.enemies"));
        setTitleAt(4, Translations.getText("settings.logic"));
        setTitleAt(5, Translations.getText("settings.appearance"));
        setTitleAt(6, Translations.getText("settings.gameplay"));
        setTitleAt(7, Translations.getText("settings.removal"));
        setTitleAt(8, Translations.getText("settings.share"));
    }
}
