package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.*;
import java.util.List;

public class RemovedItemsPanel extends JPanel {
    private static final List<String> SUPPORTED_REMOVED_ITEMS = Arrays.asList("Spaulder");

    private JCheckBox removeNonShrineMaps;
    private JPanel randomRemovableItemsPanel;
    private JSpinner minRandomRemovableItems;
    private JSpinner maxRandomRemovableItems;
    private JLabel minRandomRemovableItemsLabel;
    private JLabel maxRandomRemovableItemsLabel;
    private List<RemoveItemToggle> removableItems;

    public RemovedItemsPanel() {
        super(new MigLayout("fillx, wrap"));

        removeNonShrineMaps = new JCheckBox();
        removeNonShrineMaps.setSelected(Settings.isReplaceMapsWithWeights());

        minRandomRemovableItems = new JSpinner(new SpinnerNumberModel(Settings.getMinRandomRemovedItems(),
                0, Settings.MAX_RANDOM_REMOVED_ITEMS_CURRENTLY_SUPPORTED, 1));
        minRandomRemovableItemsLabel = new JLabel(Translations.getText("settings.removal.min"));
        maxRandomRemovableItems = new JSpinner(new SpinnerNumberModel(Settings.getMaxRandomRemovedItems(),
                0, Settings.MAX_RANDOM_REMOVED_ITEMS_CURRENTLY_SUPPORTED, 1));
        maxRandomRemovableItemsLabel = new JLabel(Translations.getText("settings.removal.max"));

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(removeNonShrineMaps);

        removableItems = new ArrayList<>(SUPPORTED_REMOVED_ITEMS.size());
        for(String itemName : SUPPORTED_REMOVED_ITEMS) {
            removableItems.add(new RemoveItemToggle(itemName, checkboxContainer));
        }

        randomRemovableItemsPanel = new CheckboxContainer(2);
        randomRemovableItemsPanel.setBorder(BorderFactory.createTitledBorder(Translations.getText("settings.removal.randomCount")));

        JPanel innerPanelMin = new JPanel();
        innerPanelMin.add(minRandomRemovableItemsLabel, "gap related");
        innerPanelMin.add(minRandomRemovableItems);
        randomRemovableItemsPanel.add(innerPanelMin);

        JPanel innerPanelMax = new JPanel();
        innerPanelMax.add(maxRandomRemovableItemsLabel, "gap related");
        innerPanelMax.add(maxRandomRemovableItems);
        randomRemovableItemsPanel.add(innerPanelMax);

        add(checkboxContainer, "growx, wrap");
        add(randomRemovableItemsPanel, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        removeNonShrineMaps.setText(Translations.getText("randomization.replaceMapsWithWeights"));
        randomRemovableItemsPanel.setBorder(BorderFactory.createTitledBorder(Translations.getText("settings.removal.randomCount")));
        for(RemoveItemToggle removeItemToggle : removableItems) {
            removeItemToggle.updateTranslations();
        }
        minRandomRemovableItemsLabel.setText(Translations.getText("settings.removal.min"));
        maxRandomRemovableItemsLabel.setText(Translations.getText("settings.removal.max"));
    }

    public void updateSettings() {
        Settings.setReplaceMapsWithWeights(removeNonShrineMaps.isSelected(), true);
        for(RemoveItemToggle removeItemToggle : removableItems) {
            Settings.setRemovedItem(removeItemToggle.getItemName(), removeItemToggle.isRemovedItem(), true);
        }
        Settings.setMinRandomRemovedItems((int)minRandomRemovableItems.getValue(), true);
        Settings.setMaxRandomRemovedItems((int)maxRandomRemovableItems.getValue(), true);
    }

    public void reloadSettings() {
        removeNonShrineMaps.setSelected(Settings.isReplaceMapsWithWeights());
        minRandomRemovableItems.setValue(Settings.getMinRandomRemovedItems());
        maxRandomRemovableItems.setValue(Settings.getMaxRandomRemovedItems());
        for(RemoveItemToggle removeItemToggle : removableItems) {
            removeItemToggle.reloadSettings();
        }
    }

    class RemoveItemToggle {
        private String itemName;
        private JCheckBox checkbox;

        public RemoveItemToggle(String itemName, CheckboxContainer checkboxContainer) {
            this.itemName = itemName;
            checkbox = new JCheckBox();
            checkbox.setSelected(Settings.getRemovedItems().contains(itemName));
            checkboxContainer.add(checkbox);
        }

        public void updateTranslations() {
            checkbox.setText(Translations.getItemText(itemName));
        }

        public String getItemName() {
            return itemName;
        }

        public boolean isRemovedItem() {
            return checkbox.isSelected();
        }

        public void reloadSettings() {
            checkbox.setSelected(Settings.getRemovedItems().contains(itemName));
        }
    }
}
