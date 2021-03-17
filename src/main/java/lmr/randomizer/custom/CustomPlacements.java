package lmr.randomizer.custom;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.Main;
import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import lmr.randomizer.node.CustomDoorPlacement;
import lmr.randomizer.node.CustomItemPlacement;
import lmr.randomizer.node.CustomPlacementData;
import lmr.randomizer.node.CustomTransitionPlacement;
import lmr.randomizer.random.ItemRandomizer;
import lmr.randomizer.random.ShopRandomizationEnum;
import lmr.randomizer.random.TransitionGateRandomizer;
import lmr.randomizer.update.LocationCoordinateMapper;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomPlacements {
    public static boolean validateCustomPlacements(Main.RandomizerUI randomizerUI) {
        CustomPlacementData customPlacementData = DataFromFile.getCustomPlacementData();

        List<String> locations = new ArrayList<>();
        List<String> items = new ArrayList<>();
        if(customPlacementData.getStartingWeapon() != null) {
            if(Settings.getStartingItemsIncludingCustom().contains(customPlacementData.getStartingWeapon())) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Custom starting weapon cannot be the same as starting item" + customPlacementData.getStartingWeapon() + " not valid with current settings for starting item",
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if(customPlacementData.getRemovedItems().contains(customPlacementData.getStartingWeapon())) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Cannot remove starting weapon",
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if(ItemRandomizer.ALL_SUBWEAPONS.contains(customPlacementData.getStartingWeapon())) {
                if(!Settings.isAllowSubweaponStart()) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Custom starting weapon not enabled",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            else if(DataFromFile.MAIN_WEAPONS.contains(customPlacementData.getStartingWeapon())) {
                if("Whip".equals(customPlacementData.getStartingWeapon())) {
                    if(!Settings.isAllowWhipStart()) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Custom starting weapon not enabled",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                else if(!Settings.isAllowMainWeaponStart()) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Custom starting weapon not enabled",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

            }
            else {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Invalid starting weapon: " + customPlacementData.getStartingWeapon(),
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        if(customPlacementData.getStartingLocation() != null) {
            if(!Settings.isRandomizeStartingLocation()) {
                JOptionPane.showMessageDialog(randomizerUI,
                        String.format("Please enable \"%s\"", Translations.getText("randomization.randomizeStartingLocation")),
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(!DataFromFile.STARTING_LOCATIONS.contains(customPlacementData.getStartingLocation())) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Invalid starting location: " + LocationCoordinateMapper.getStartingZoneName(customPlacementData.getStartingLocation()),
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(!Settings.isRandomizeTransitionGates()) {
                if(customPlacementData.getStartingLocation() == 11
                        || customPlacementData.getStartingLocation() == 14
                        || customPlacementData.getStartingLocation() == 16
                        || customPlacementData.getStartingLocation() == 21) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            String.format("Please enable \"%s\"", Translations.getText("randomization.randomizeTransitionGates")),
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        if(!customPlacementData.getCustomDoorPlacements().isEmpty()) {
            if(!Settings.isRandomizeBacksideDoors()) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Please enable the setting \"" + Translations.getText("randomization.randomizeBacksideDoors") + "\"",
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            Map<String, String> placedDoorsAndDestinations = new HashMap<>();
            Map<String, Integer> placedDoorsAndBosses = new HashMap<>();
            for(CustomDoorPlacement customDoorPlacement : customPlacementData.getCustomDoorPlacements()) {
                if(!customDoorPlacement.getTargetDoor().startsWith("Door F") && !customDoorPlacement.getTargetDoor().startsWith("Door B")) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Backside door " + customDoorPlacement.getTargetDoor() + " is invalid",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(!customDoorPlacement.getDestinationDoor().startsWith("Door F") && !customDoorPlacement.getDestinationDoor().startsWith("Door B")) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Backside door " + customDoorPlacement.getDestinationDoor() + " is invalid",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(!Settings.isRandomizeNonBossDoors()) {
                    if(isNonBossDoor(customDoorPlacement.getTargetDoor())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Please enable the setting \"" + Translations.getText("randomization.randomizeNonBossDoors") + "\"",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(isNonBossDoor(customDoorPlacement.getDestinationDoor())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Please enable the setting \"" + Translations.getText("randomization.randomizeNonBossDoors") + "\"",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                if(placedDoorsAndDestinations.keySet().contains(customDoorPlacement.getTargetDoor())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Backside door " + customDoorPlacement.getTargetDoor() + " cannot be assigned multiple destinations",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(placedDoorsAndDestinations.values().contains(customDoorPlacement.getDestinationDoor())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Multiple backside doors cannot lead to destination door " + customDoorPlacement.getDestinationDoor(),
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(customDoorPlacement.getTargetDoor().equals(customDoorPlacement.getDestinationDoor())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Backside door " + customDoorPlacement.getTargetDoor() + " cannot lead to itself",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(customDoorPlacement.getAssignedBoss() != null
                        && customDoorPlacement.getAssignedBoss() != 9 && (customDoorPlacement.getAssignedBoss() < 1 || customDoorPlacement.getAssignedBoss() > 7)) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Assigned boss for backside door " + customDoorPlacement.getTargetDoor() + " could not be processed; please use boss name or numbers 1-7",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(placedDoorsAndDestinations.values().contains(customDoorPlacement.getTargetDoor())) {
                    if(!customDoorPlacement.getTargetDoor().equals(placedDoorsAndDestinations.get(customDoorPlacement.getDestinationDoor()))) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Support for non-reversible backside door placement does not exist at this time; please update assignment for " + customDoorPlacement.getTargetDoor() + " or " + customDoorPlacement.getDestinationDoor(),
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                if(placedDoorsAndDestinations.keySet().contains(customDoorPlacement.getDestinationDoor())) {
                    if(!customDoorPlacement.getTargetDoor().equals(placedDoorsAndDestinations.get(customDoorPlacement.getDestinationDoor()))) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Support for non-reversible backside door placement does not exist at this time; please update assignment for " + customDoorPlacement.getTargetDoor() + " or " + customDoorPlacement.getDestinationDoor(),
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                Integer existingBoss = placedDoorsAndBosses.get(customDoorPlacement.getDestinationDoor());
                if(existingBoss != null && !existingBoss.equals(customDoorPlacement.getAssignedBoss())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "A pair of backside doors cannot be assigned two different bosses at this time; please update assignment for " + customDoorPlacement.getTargetDoor() + " or " + customDoorPlacement.getDestinationDoor(),
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if((customDoorPlacement.getAssignedBoss() != null && customDoorPlacement.getAssignedBoss() > 0 && customDoorPlacement.getAssignedBoss() < 9)
                        && (customDoorPlacement.getTargetDoor().endsWith("B8") || customDoorPlacement.getDestinationDoor().endsWith("B8"))) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Dimensional Corridor may not be paired with a Bronze Mirror door",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if((customDoorPlacement.getAssignedBoss() != null && customDoorPlacement.getAssignedBoss() > 0 && customDoorPlacement.getAssignedBoss() < 9)
                        && (customDoorPlacement.getTargetDoor().endsWith("B9") || customDoorPlacement.getDestinationDoor().endsWith("B9"))) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Gate of Time may not be paired with a Bronze Mirror door",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                placedDoorsAndDestinations.put(customDoorPlacement.getTargetDoor(), customDoorPlacement.getDestinationDoor());
                placedDoorsAndBosses.put(customDoorPlacement.getTargetDoor(), customDoorPlacement.getAssignedBoss());
                placedDoorsAndBosses.put(customDoorPlacement.getDestinationDoor(), customDoorPlacement.getAssignedBoss());
            }
        }
        if(!customPlacementData.getCustomTransitionPlacements().isEmpty()) {
            if(!Settings.isRandomizeTransitionGates()) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Please enable the setting \"" + Translations.getText("randomization.randomizeTransitionGates") + "\"",
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            Map<String, String> placedTargetAndDestination = new HashMap<>();
            for(CustomTransitionPlacement customTransitionPlacement : customPlacementData.getCustomTransitionPlacements()) {
                if(!isValidTransition(customTransitionPlacement.getTargetTransition())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Transition " + customTransitionPlacement.getTargetTransition().replaceAll("^Transition:? ", "") + " is invalid",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(!isValidTransition(customTransitionPlacement.getDestinationTransition())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Transition " + customTransitionPlacement.getDestinationTransition().replaceAll("^Transition:? ", "") + " is invalid",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(!isValidTransitionDirection(customTransitionPlacement.getTargetTransition(), customTransitionPlacement.getDestinationTransition())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Transition " + customTransitionPlacement.getDestinationTransition().replaceAll("^Transition:? ", "") + " is the wrong direction for " + customTransitionPlacement.getTargetTransition().replaceAll("^Transition:? ", ""),
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(!Settings.isRandomizeOneWayTransitions()) {
                    if(isOneWayTransition(customTransitionPlacement.getTargetTransition())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Please enable the setting \"" + Translations.getText("randomization.randomizeOneWayTransitions") + "\"",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if(isOneWayTransition(customTransitionPlacement.getDestinationTransition())) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Please enable the setting \"" + Translations.getText("randomization.randomizeOneWayTransitions") + "\"",
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }

                if(placedTargetAndDestination.keySet().contains(customTransitionPlacement.getTargetTransition())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Transition " + customTransitionPlacement.getTargetTransition().replaceAll("^Transition:? ", "") + " cannot be assigned multiple destinations",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(placedTargetAndDestination.values().contains(customTransitionPlacement.getDestinationTransition())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Multiple transitions cannot lead to destination transition " + customTransitionPlacement.getDestinationTransition().replaceAll("^Transition:? ", ""),
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(customTransitionPlacement.getTargetTransition().equals(customTransitionPlacement.getDestinationTransition())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Transition " + customTransitionPlacement.getTargetTransition().replaceAll("^Transition:? ", "") + " cannot lead to itself",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(placedTargetAndDestination.values().contains(customTransitionPlacement.getTargetTransition())) {
                    if(!customTransitionPlacement.getTargetTransition().equals(placedTargetAndDestination.get(customTransitionPlacement.getDestinationTransition()))) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Support for non-reversible transition placement does not exist at this time; please update assignment for " + customTransitionPlacement.getTargetTransition().replaceAll("^Transition:? ", "") + " or " + customTransitionPlacement.getDestinationTransition().replaceAll("^Transition:? ", ""),
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                if(placedTargetAndDestination.keySet().contains(customTransitionPlacement.getDestinationTransition())) {
                    if(!customTransitionPlacement.getTargetTransition().equals(placedTargetAndDestination.get(customTransitionPlacement.getDestinationTransition()))) {
                        JOptionPane.showMessageDialog(randomizerUI,
                                "Support for non-reversible backside door placement does not exist at this time; please update assignment for " + customTransitionPlacement.getTargetTransition().replaceAll("^Transition:? ", "") + " or " + customTransitionPlacement.getDestinationTransition().replaceAll("^Transition:? ", ""),
                                "Custom placement error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                placedTargetAndDestination.put(customTransitionPlacement.getTargetTransition(), customTransitionPlacement.getDestinationTransition());
            }
        }
        if(Settings.isRequireFullAccess() && !customPlacementData.getRemovedItems().isEmpty()) {
            if(Settings.isRequireFullAccess()) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Please disable the setting \"Require all items to be accessible\"",
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        for(String customRemovedItem : customPlacementData.getRemovedItems()) {
            if(Settings.getStartingItems().contains(customRemovedItem)) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Custom placement of " + customRemovedItem + " not valid with current settings for starting item",
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if(customRemovedItem.startsWith("Coin:")) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Coin chests cannot be removed items",
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(customRemovedItem.startsWith("Trap:")) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Traps cannot be removed items",
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(Settings.isRequireIceCapeForLava() && customRemovedItem.equals("Ice Cape")) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Please disable the setting \"Require Ice Cape for swimming through lava\"",
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(Settings.isRequireFlaresForExtinction() && customRemovedItem.equals("Flare Gun")) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Please disable the setting \"Require Flare Gun for Chamber of Extinction\"",
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(!DataFromFile.getRandomRemovableItems().contains(customRemovedItem)
                    && !"Whip".equals(customRemovedItem)) {
                JOptionPane.showMessageDialog(randomizerUI,
                        customRemovedItem + " cannot be a removed item",
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if(Settings.isHalloweenMode() && customRemovedItem.equals("Provocative Bathing Suit")) {
                JOptionPane.showMessageDialog(randomizerUI,
                        String.format("Custom placement of \"%s\" cannot be used with this mode",
                                Translations.getText("items.ProvocativeBathingSuit")),
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if(!isValidContents(customRemovedItem)) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Removed item not valid: " + customRemovedItem,
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        for(String customStartingItem : customPlacementData.getStartingItems()) {
            if (!isValidContents(customStartingItem)) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Starting item not valid: " + customStartingItem,
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if(Settings.isHalloweenMode() && customStartingItem.equals("Provocative Bathing Suit")) {
                JOptionPane.showMessageDialog(randomizerUI,
                        String.format("Custom placement of \"%s\" cannot be used with this mode",
                                Translations.getText("items.ProvocativeBathingSuit")),
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        for(String cursedChestLocation : customPlacementData.getCursedChests()) {
            if(!Settings.isRandomizeCursedChests()) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Custom placement of cursed chest not valid with current settings for cursed chest randomization",
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(DataFromFile.FLOATING_ITEM_LOCATIONS.contains(cursedChestLocation)
                    || DataFromFile.LOCATIONS_RELATED_TO_BLOCKS.contains(cursedChestLocation)
                    || "mantra.exe".equals(cursedChestLocation)
                    || "emusic.exe".equals(cursedChestLocation)
                    || "beolamu.exe".equals(cursedChestLocation)) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Non-chest location " + cursedChestLocation + " cannot be cursed",
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        for(CustomItemPlacement customItemPlacement : customPlacementData.getCustomItemPlacements()) {
            if(Settings.getStartingItems().contains(customItemPlacement.getContents())) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Custom placement of " + customItemPlacement.getContents() + " not valid with current settings for starting item",
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if(locations.contains(customItemPlacement.getLocation())) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Location used for multiple items: " + customItemPlacement.getLocation(),
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(items.contains(customItemPlacement.getContents())
                    && !"Weights".equals(customItemPlacement.getContents())
                    && !customItemPlacement.getContents().endsWith(" Ammo")) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Item placed in multiple locations: " + customItemPlacement.getContents(),
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(Settings.isHalloweenMode() && customItemPlacement.getContents().equals("Provocative Bathing Suit")) {
                JOptionPane.showMessageDialog(randomizerUI,
                        String.format("Custom placement of \"%s\" cannot be used with this mode",
                                Translations.getText("items.ProvocativeBathingSuit")),
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(!isValidLocation(customItemPlacement.getLocation())) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Location not valid: " + customItemPlacement.getLocation() + " for item " + customItemPlacement.getContents(),
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(!isValidContents(customItemPlacement.getContents())) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Placed item not valid: " + customItemPlacement.getContents() + " at location " + customItemPlacement.getLocation(),
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(DataFromFile.SHOP_ITEMS.contains(customItemPlacement.getLocation())) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "To place " + customItemPlacement.getLocation() + " in a shop, please use the shop name and item number instead of the item name",
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(!Settings.isRandomizeTrapItems()) {
                if(customItemPlacement.getLocation().startsWith("Trap:")) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Custom placement at location " + customItemPlacement.getLocation() + " not valid with current settings for randomized traps",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(customItemPlacement.getContents().startsWith("Trap:")) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Custom placement of item " + customItemPlacement.getContents() + " not valid with current settings for randomized traps",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            if(!Settings.isRandomizeEscapeChest()) {
                if(customItemPlacement.getLocation().equals(DataFromFile.ESCAPE_CHEST_NAME) || customItemPlacement.getContents().startsWith(DataFromFile.ESCAPE_CHEST_NAME)) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Custom placement not valid with current settings for randomized escape chest",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            if(!Settings.isRandomizeCoinChests()) {
                if(customItemPlacement.getLocation().startsWith("Coin:")) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Custom placement at location " + customItemPlacement.getLocation() + " not valid with current settings for randomized coin chests",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if(customItemPlacement.getContents().startsWith("Coin:")) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Custom placement of item " + customItemPlacement.getContents() + " not valid with current settings for randomized coin chests",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            if(!Settings.isRandomizeForbiddenTreasure()) {
                if(customItemPlacement.getLocation().equals("Provocative Bathing Suit")
                        || customItemPlacement.getContents().equals("Provocative Bathing Suit")) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Custom placement not valid with current settings for Provocative Bathing Suit randomization",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            else if(!Settings.isHTFullRandom()) {
                if(customItemPlacement.getLocation().equals("Provocative Bathing Suit")
                        && !customItemPlacement.getContents().startsWith("Map (")) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Custom placement not valid with current settings for Provocative Bathing Suit randomization",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            if(customItemPlacement.getContents().startsWith("Coin:") || DataFromFile.EXPLODING_CHEST_NAME.equals(customItemPlacement.getContents())) {
                if(DataFromFile.FLOATING_ITEM_LOCATIONS.contains(customItemPlacement.getLocation())
                        || DataFromFile.LOCATIONS_RELATED_TO_BLOCKS.contains(customItemPlacement.getLocation())
                        || "mantra.exe".equals(customItemPlacement.getLocation())
                        || "emusic.exe".equals(customItemPlacement.getLocation())
                        || "beolamu.exe".equals(customItemPlacement.getLocation())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Item " + customItemPlacement.getContents() + " cannot be placed at non-chest location " + customItemPlacement.getLocation(),
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }

            if(!Settings.isRandomizeDracuetShop() && customItemPlacement.getLocation().startsWith("Shop 23 (HT)")) {
                JOptionPane.showMessageDialog(randomizerUI,
                        "Custom placement not valid with current settings for Dracuet shop randomization",
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if(ShopRandomizationEnum.CATEGORIZED.equals(Settings.getShopRandomization())
                    && customItemPlacement.getLocation().startsWith("Shop ")) {
                if(!DataFromFile.CATEGORIZED_SHOP_ITEM_LOCATIONS.contains(customItemPlacement.getLocation())) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Custom placement of item at " + customItemPlacement.getLocation() + " not valid with current settings for shop randomization",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                else if("Weights".equals(customItemPlacement.getContents())
                        || customItemPlacement.getContents().endsWith(" Ammo")) {
                    JOptionPane.showMessageDialog(randomizerUI,
                            "Custom placement of " + customItemPlacement.getContents() + " not valid with current settings for shop randomization",
                            "Custom placement error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }

            if(!Settings.isRandomizeStartingLocation() && customItemPlacement.getLocation().startsWith("Shop 0")) {
                JOptionPane.showMessageDialog(randomizerUI,
                        String.format("Please enable the setting \"%s\"", Translations.getText("randomization.randomizeStartingLocation")),
                        "Custom placement error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            locations.add(customItemPlacement.getLocation());
            items.add(customItemPlacement.getContents());
        }
        return true;
    }

    public static void addHolidayPlacements() {
        if(Settings.isHalloweenMode()) {
            CustomItemPlacement customItemPlacement = new CustomItemPlacement("xmailer.exe", "Provocative Bathing Suit", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 2 (Surface) Item 3", "Buckler", (short)5, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
        } else if(Settings.isFools2020Mode()) {
            DataFromFile.clearCustomPlacementData();
            DataFromFile.getCustomPlacementData().setAlternateMotherAnkh(true);
            DataFromFile.getCustomPlacementData().setMedicineColor("Yellow");
            DataFromFile.getCustomPlacementData().setStartingWeapon("Whip");
            DataFromFile.getCustomPlacementData().getStartingItems().add("mirai.exe");
            if(Settings.getStartingItems().contains("Hermes' Boots")) {
                DataFromFile.getCustomPlacementData().getStartingItems().add("Hermes' Boots");
            }
            if(Settings.getStartingItems().contains("bunemon.exe")) {
                DataFromFile.getCustomPlacementData().getStartingItems().add("bunemon.exe");
            }
            DataFromFile.getCustomPlacementData().setStartingLocation(1);

            Settings.setRandomizeEscapeChest(true, false);
            Settings.setRandomizeTransitionGates(false, false);
            Settings.setRandomizeBacksideDoors(false, false);
            Settings.setRandomizeNonBossDoors(false, false);
            Settings.setRandomizeCoinChests(true, false);
            Settings.setRandomizeTrapItems(true, false);
            Settings.setRandomizeCursedChests(true, false);
            Settings.setAllowWhipStart(true, false);
            Settings.setShopRandomization(ShopRandomizationEnum.EVERYTHING.name(), false);
            Settings.setRandomizeGraphics(false, false);
            Settings.setMinRandomRemovedItems(0, false);
            Settings.setMaxRandomRemovedItems(0, false);
            Settings.setReplaceMapsWithWeights(false, false);
            Settings.setRemoveSpaulder(false, false);
            Settings.setRemoveMainWeapons(false, false);
            Settings.getStartingItems().clear();
            Settings.getInitiallyAccessibleItems().clear();
            Settings.getRemovedItems().clear();
            if(Settings.isRandomizeForbiddenTreasure() && !Settings.isHTFullRandom()) {
                Settings.setHTFullRandom(true, false);
            }

            // Cursed chests
            DataFromFile.getCustomPlacementData().getCursedChests().add("Glove");
            DataFromFile.getCustomPlacementData().getCursedChests().add("Feather");
            DataFromFile.getCustomPlacementData().getCursedChests().add("Magatama Jewel");
            DataFromFile.getCustomPlacementData().getCursedChests().add("Dimensional Key");
            DataFromFile.getCustomPlacementData().getCursedChests().add("Djed Pillar");
            DataFromFile.getCustomPlacementData().getCursedChests().add("Coin: Twin (Escape)");

            // Maps
            CustomItemPlacement customItemPlacement = new CustomItemPlacement("Map (Gate of Guidance)", "Map (Gate of Guidance)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Map (Surface)", "Map (Surface)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            // Ankh jewels
            customItemPlacement = new CustomItemPlacement("Ankh Jewel (Gate of Guidance)", "Ankh Jewel (Gate of Guidance)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Map (Mausoleum of the Giants)", "Map (Mausoleum of the Giants)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Ankh Jewel (Mausoleum of the Giants)", "Ankh Jewel (Mausoleum of the Giants)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Sacred Orb (Mausoleum of the Giants)", "Sacred Orb (Mausoleum of the Giants)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            // Sacred Orbs
            customItemPlacement = new CustomItemPlacement("Sacred Orb (Surface)", "Sacred Orb (Surface)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Sacred Orb (Gate of Guidance)", "Sacred Orb (Gate of Guidance)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Sacred Orb (Dimensional Corridor)", "Coin: Guidance (Two)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Coin: Inferno (Spikes)", "Sacred Orb (Dimensional Corridor)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            // Weapons
            customItemPlacement = new CustomItemPlacement("Axe", "Katana", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Bomb", "Key Sword", null); // Key sword not required in this
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Caltrops", "Caltrops", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Chain Whip", "Flare Gun", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Chakram", "Knife", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Earth Spear", "Chakram", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Flail Whip", "Flail Whip", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Flare Gun", "Glove", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Katana", "Axe", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Key Sword", "Earth Spear", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Knife", "Bomb", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Rolling Shuriken", "Rolling Shuriken", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Shuriken", "Shuriken", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            // Swap
            customItemPlacement = new CustomItemPlacement("Coin: Twin (Lower)", "Ankh Jewel (Twin Labyrinths)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Ankh Jewel (Twin Labyrinths)", "Coin: Twin (Lower)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Gauntlet", "Silver Shield", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Glove", "Gauntlet", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Holy Grail", "Spaulder", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Spaulder", "Ankh Jewel (Extra)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Trap: Twin Ankh", "Holy Grail", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Feather", "Angel Shield", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("mirai.exe", "Chain Whip", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Coin: Twin (Escape)", "Feather", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Djed Pillar", "Dimensional Key", "Feather");
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Death Seal", "Cog of the Soul", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Cog of the Soul", "Coin: Guidance (One)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Dimensional Key", "Death Seal", "Feather");
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("yagostr.exe", "yagomap.exe", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("lamulana.exe", "guild.exe", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Perfume", "Djed Pillar", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            // Traps
            customItemPlacement = new CustomItemPlacement("Trap: Inferno Orb", "Trap: Inferno Orb", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Trap: Exploding", "Trap: Exploding", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Angel Shield", "Trap: Twin Ankh", "Angel Shield");
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Silver Shield", "Trap: Graveyard", "Silver Shield");
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            // Other
            customItemPlacement = new CustomItemPlacement("Coin: Surface (Waterfall)", "Coin: Surface (Waterfall)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Coin: Surface (Seal)", "Coin: Surface (Seal)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Coin: Surface (Ruin Path)", "Coin: Surface (Ruin Path)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Coin: Guidance (One)", "Coin: Guidance (One)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Coin: Guidance (Two)", "Coin: Guidance (Two)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("Coin: Guidance (Trap)", "Coin: Guidance (Trap)", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            customItemPlacement = new CustomItemPlacement("beolamu.exe", "beolamu.exe", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Birth Seal", "Birth Seal", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Book of the Dead", "Book of the Dead", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Bronze Mirror", "Bronze Mirror", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            if(Settings.isRandomizeForbiddenTreasure()) {
                customItemPlacement = new CustomItemPlacement("bunplus.com", "Ice Cape", null);
                DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            }

            customItemPlacement = new CustomItemPlacement("Cog of the Soul", "Coin: Spring", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Coin: Graveyard", "Coin: Graveyard", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Crystal Skull", "Crystal Skull", "Feather");
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("deathv.exe", "deathv.exe", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Diary", "Diary", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("emusic.exe", "emusic.exe", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Eye of Truth", "Eye of Truth", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Coin: Illusion (Katana)", "Fairy Clothes", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Fruit of Eden", "Fruit of Eden", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Grapple Claw", "Grapple Claw", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            if(Settings.isRandomizeForbiddenTreasure()) {
                customItemPlacement = new CustomItemPlacement("Ice Cape", "Provocative Bathing Suit", null);
                DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            }
            else {
                customItemPlacement = new CustomItemPlacement("Ice Cape", "Ice Cape", null);
                DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            }

            customItemPlacement = new CustomItemPlacement("Life Seal", "Life Seal", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Map (Endless Corridor)", "Isis' Pendant", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Key of Eternity", "Key of Eternity", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("mantra.exe", "mantra.exe", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Magatama Jewel", "Magatama Jewel", "Feather");
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("mekuri.exe", "mekuri.exe", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("mirai.exe", "mirai.exe", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Mulana Talisman", "Mulana Talisman", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Scalesphere", "Origin Seal", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Pepper", "Pepper", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Philosopher's Ocarina", "Philosopher's Ocarina", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Plane Model", "Plane Model", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Pochette Key", "Pochette Key", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            if(Settings.isRandomizeForbiddenTreasure()) {
                customItemPlacement = new CustomItemPlacement("Provocative Bathing Suit", "bunplus.com", null);
                DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            }
            else {
                customItemPlacement = new CustomItemPlacement("bunplus.com", "bunplus.com", null);
                DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
            }

            customItemPlacement = new CustomItemPlacement("Ring", "Ring", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Serpent Staff", "Serpent Staff", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shell Horn", "Shell Horn", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Talisman", "Talisman", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Treasures", "Treasures", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Twin Statue", "Twin Statue", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Vessel", "Vessel", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Woman Statue", "Woman Statue", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("xmailer.exe", "xmailer.exe", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            // Shops
            customItemPlacement = new CustomItemPlacement("Shop 1 (Surface) Item 1", "Hand Scanner", (short)10, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 1 (Surface) Item 2", "Pistol Ammo", (short)400, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 1 (Surface) Item 3", "Shuriken Ammo", (short)10, (short)10);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 2 (Surface) Item 1", "Weights", (short)10, (short)5);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 2 (Surface) Item 2", "reader.exe", (short)50, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 2 (Surface) Item 3", "yagostr.exe", (short)20, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 2 Alt (Surface) Item 1", "Bracelet", null, null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 3 (Surface) Item 1", "Buckler", (short)10, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 3 (Surface) Item 2", "Waterproof Case", (short)50, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 3 (Surface) Item 3", "Pistol", (short)100, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 4 (Guidance) Item 1", "Shuriken Ammo", (short)10, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 4 (Guidance) Item 2", "lamulana.exe", (short)60, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 4 (Guidance) Item 3", "Weights", (short)10, (short)5);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 5 (Illusion) Item 1", "move.exe", null, null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 6 (Mausoleum) Item 1", "Hermes' Boots", (short)60, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 7 (Graveyard) Item 2", "Fake Silver Shield", (short)150, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 8 (Sun) Item 3", "bunemon.exe", (short)50, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 9 (Sun) Item 1", "Scriptures", (short)250, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 11 (Moonlight) Item 1", "Heatproof Case", (short)250, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 11 (Moonlight) Item 3", "Pistol Ammo", (short)4, (short)6);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 12 (Spring) Item 3", "capstar.exe", (short)200, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 13 (Goddess) Item 1", "torude.exe", (short)200, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 13 (Goddess) Item 2", "Weights", null, null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 13 (Goddess) Item 3", "Weights", null, null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 14 (Inferno) Item 1", "randc.exe", (short)150, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 15 (Ruin) Item 1", "miracle.exe", (short)200, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 17 (Birth) Item 2", "Ankh Jewel (Chamber of Birth)", null, null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 18 (Lil Bro) Item 1", "Dragon Bone", (short)60, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 18 (Lil Bro) Item 2", "Weights", (short)1, (short)50);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 18 (Lil Bro) Item 3", "Weights", (short)50, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 19 (Big Bro) Item 1", "Map (Shrine of the Mother)", (short)100, (short)1);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 20 (Twin Labs) Item 1", "Weights", null, null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 20 (Twin Labs) Item 2", "Weights", null, null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 20 (Twin Labs) Item 3", "Weights", null, null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 21 (Unsolvable) Item 1", "Lamp of Time", null, null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 21 (Unsolvable) Item 2", "Helmet", null, null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
        }
    }

    private static boolean isValidLocation(String location) {
        if(DataFromFile.getAllItems().contains(location)) {
            return true;
        }
        if(DataFromFile.getAllCoinChests().contains(location)
                || DataFromFile.ESCAPE_CHEST_NAME.equals(location)) {
            return true;
        }
        if(DataFromFile.TRAP_ITEMS.contains(location)) {
            return true;
        }
        if(location.startsWith("Shop ")
                && (location.endsWith(" Item 1") || location.endsWith(" Item 2") || location.endsWith(" Item 3"))) {
            for(String shopName : DataFromFile.getAllShops()) {
                if(location.startsWith(shopName)) {
                    return !location.startsWith("Shop 2 Alt (Surface)") || location.endsWith("1");
                }
            }
            if(Settings.isRandomizeStartingLocation() && location.startsWith(DataFromFile.CUSTOM_SHOP_NAME)) {
                // Assumes random starting location that isn't the Surface.
                return true;
            }
        }
        return false;
    }

    private static boolean isValidContents(String contents) {
        if(DataFromFile.getAllItems().contains(contents)
                || "Whip".equals(contents) || "Ankh Jewel (Extra)".equals(contents)) {
            return true;
        }
        if(DataFromFile.getAllCoinChests().contains(contents)
                || DataFromFile.ESCAPE_CHEST_NAME.equals(contents)) {
            return true;
        }
        if(DataFromFile.TRAP_ITEMS.contains(contents)) {
            return true;
        }
        if(contents.equals("Weights") || contents.equals("Shuriken Ammo") || contents.equals("Rolling Shuriken Ammo")
                || contents.equals("Caltrops Ammo") || contents.equals("Chakram Ammo") || contents.equals("Flare Gun Ammo")
                || contents.equals("Earth Spear Ammo") || contents.equals("Pistol Ammo") || contents.equals("Bomb Ammo")) {
            return true;
        }
        return false;
    }

    private static boolean isValidTransition(String transition) {
        String formattedTransition = transition.replace("Transition ", "Transition: ");
        return "Transition: Goddess W1".equals(formattedTransition)
                || "Transition: Inferno W1".equals(formattedTransition)
                || (TransitionGateRandomizer.getTransitionList().contains(formattedTransition)
                && !formattedTransition.startsWith("Transition: Sun R")
                && !formattedTransition.startsWith("Transition: Extinction L"));
    }

    private static boolean isOneWayTransition(String transition) {
        String formattedTransition = transition.replace("Transition ", "Transition: ");
        return !"Transition: Goddess L1".equals(formattedTransition)
                && !"Transition: Illusion R1".equals(formattedTransition)
                && !"Transition: Twin U3".equals(formattedTransition)
                && !"Transition: Dimensional D1".equals(formattedTransition)
                && !"Transition: Shrine U1".equals(formattedTransition)
                && !"Transition: Endless D1".equals(formattedTransition)
                && !"Transition: Extinction U3".equals(formattedTransition)
                && !"Transition: Inferno W1".equals(formattedTransition)
                && !"Transition: Retromausoleum D1".equals(formattedTransition)
                && !"Transition: Goddess W1".equals(formattedTransition)
                && !"Transition: Twin U2".equals(formattedTransition)
                && !"Transition: Shrine D3".equals(formattedTransition)
                && !"Transition: Endless U1".equals(formattedTransition)
                && !"Transition: Shrine D2".equals(formattedTransition);
    }

    private static boolean isNonBossDoor(String door) {
        return door.endsWith("8") || door.endsWith("9");
    }

    private static boolean isValidTransitionDirection(String transitionTarget, String transitionDestination) {
        char transitionDirection1 = transitionTarget.charAt(transitionTarget.length() - 2);
        char transitionDirection2 = transitionDestination.charAt(transitionDestination.length() - 2);

        // Handle special transitions
        if(transitionDirection1 == 'W' && transitionTarget.contains("Goddess W1")) {
            transitionDirection1 = 'U';
        }
        if(transitionDirection2 == 'W' && transitionDestination.contains("Goddess W1")) {
            transitionDirection2 = 'U';
        }
        if(transitionDirection1 == 'W' && transitionTarget.contains("Inferno W1")) {
            transitionDirection1 = 'D';
        }
        if(transitionDirection2 == 'W' && transitionDestination.contains("Inferno W1")) {
            transitionDirection2 = 'D';
        }

        // Check direction
        if (transitionDirection1 == 'U') {
            return transitionDirection2 == 'D';
        }
        else if (transitionDirection1 == 'R') {
            return transitionDirection2 == 'L';
        }
        else if (transitionDirection1 == 'D') {
            return transitionDirection2 == 'U';
        }
        else if (transitionDirection1 == 'L') {
            return transitionDirection2 == 'R';
        }
        return false;
    }
}
