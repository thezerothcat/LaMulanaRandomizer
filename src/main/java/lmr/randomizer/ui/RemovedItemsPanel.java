package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.*;

public class RemovedItemsPanel extends JPanel {
    private static final List<String> SUPPORTED_REMOVED_ITEMS = Arrays.asList("Spaulder");

    private JCheckBox removeNonShrineMaps;
    private List<RemoveItemToggle> removableItems;

    public RemovedItemsPanel() {
        super(new MigLayout("fillx, wrap"));

        removeNonShrineMaps = new JCheckBox();
        removeNonShrineMaps.setSelected(Settings.isReplaceMapsWithWeights());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(removeNonShrineMaps);

        removableItems = new ArrayList<>(SUPPORTED_REMOVED_ITEMS.size());
        for(String itemName : SUPPORTED_REMOVED_ITEMS) {
            removableItems.add(new RemoveItemToggle(itemName, checkboxContainer));
        }

        add(checkboxContainer, "growx, wrap");

        updateTranslations();
    }

    public void updateTranslations() {
        removeNonShrineMaps.setText(Translations.getText("randomization.replaceMapsWithWeights"));
        for(RemoveItemToggle removeItemToggle : removableItems) {
            removeItemToggle.updateTranslations();
        }
    }

    public void updateSettings() {
        Settings.setReplaceMapsWithWeights(removeNonShrineMaps.isSelected(), true);
        Set<String> removedItems = new HashSet<>();
        for(RemoveItemToggle removeItemToggle : removableItems) {
            removeItemToggle.updateRemovedItems(removedItems);
        }
        Settings.setRemovedItems(removedItems, true);
    }

    public void reloadSettings() {
        removeNonShrineMaps.setSelected(Settings.isReplaceMapsWithWeights());
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

        public void updateRemovedItems(Set<String> removedItems) {
            if(checkbox.isSelected()) {
                removedItems.add(itemName);
            }
        }

        public void reloadSettings() {
            checkbox.setSelected(Settings.getRemovedItems().contains(itemName));
        }
    }
}
