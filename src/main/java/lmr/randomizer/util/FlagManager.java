package lmr.randomizer.util;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.HolidaySettings;
import lmr.randomizer.Settings;
import lmr.randomizer.randomization.data.GameObjectId;

import java.util.*;

public class FlagManager {
    private static final int FIRST_AVAILABLE_RANDOM_GRAPHICS_FLAG = (HolidaySettings.isFools2020Mode() || HolidaySettings.isFools2021Mode()) ? 2762 : 2730;
    private static final int LAST_AVAILABLE_RANDOM_GRAPHICS_FLAG = (HolidaySettings.isFools2020Mode() || HolidaySettings.isFools2021Mode()) ? 2765 : 2760;

    private Map<Integer, Integer> mapOfItemLocationWorldFlagToAssignedReplacementFlag;
    private List<Integer> availableFlags;

    public FlagManager() {
        mapOfItemLocationWorldFlagToAssignedReplacementFlag = new HashMap<>();
        availableFlags = new ArrayList<>(getAvailableFlags());
    }

    public int getTotalUnallocatedFlags() {
        return availableFlags.size();
    }

    public short getNewWorldFlag(int itemLocationWorldFlag) {
        Integer newWorldFlag = mapOfItemLocationWorldFlagToAssignedReplacementFlag.get(itemLocationWorldFlag);
        if (newWorldFlag == null) {
            newWorldFlag = getNextFlag();
            mapOfItemLocationWorldFlagToAssignedReplacementFlag.put(itemLocationWorldFlag, newWorldFlag);
        }
        return newWorldFlag.shortValue();
    }

    private int getNextFlag() {
        return availableFlags.remove(0);
    }

    private Set<Integer> getAvailableFlags() {
        Set<Integer> availableFlags = new HashSet<>();
        Map<String, GameObjectId> nameToDataMap = DataFromFile.getMapOfItemToUsefulIdentifyingRcdData();
        for(String removedItem : Settings.getCurrentRemovedItems()) {
            if(!isFlagDependentItem(removedItem)) {
                availableFlags.add(nameToDataMap.get(removedItem).getWorldFlag());
            }
        }
        for(String removedItem : Settings.getRemovedItems()) {
            if(!isFlagDependentItem(removedItem)) {
                availableFlags.add(nameToDataMap.get(removedItem).getWorldFlag());
            }
        }
        availableFlags.add(2781);
        availableFlags.add(FlagConstants.REMOVED_MAP_SURFACE);
        availableFlags.add(FlagConstants.REMOVED_MAP_GUIDANCE);
        availableFlags.add(FlagConstants.REMOVED_MAP_MAUSOLEUM);
        availableFlags.add(FlagConstants.REMOVED_MAP_SUN);
        availableFlags.add(FlagConstants.REMOVED_MAP_SPRING);
        availableFlags.add(FlagConstants.REMOVED_MAP_INFERNO);
        availableFlags.add(FlagConstants.REMOVED_MAP_EXTINCTION);
        availableFlags.add(FlagConstants.REMOVED_MAP_TWIN);
        availableFlags.add(FlagConstants.REMOVED_MAP_ENDLESS);
        availableFlags.add(FlagConstants.REMOVED_MAP_ILLUSION);
        availableFlags.add(FlagConstants.REMOVED_MAP_GRAVEYARD);
        availableFlags.add(FlagConstants.REMOVED_MAP_MOONLIGHT);
        availableFlags.add(FlagConstants.REMOVED_MAP_GODDESS);
        availableFlags.add(FlagConstants.REMOVED_MAP_RUIN);
        availableFlags.add(FlagConstants.REMOVED_MAP_BIRTH);
        availableFlags.add(FlagConstants.REMOVED_MAP_DIMENSIONAL);
        availableFlags.add(FlagConstants.TABLET_GLOW_TWIN_RIGHT_OF_POISON_2_BROKEN);
        availableFlags.add(FlagConstants.TABLET_GLOW_TWIN_FAKE_ANKH_JEWEL_BROKEN);
        availableFlags.add(FlagConstants.TABLET_GLOW_TWIN_ABOVE_YIEAR_BROKEN);
        availableFlags.add(FlagConstants.TABLET_GLOW_TWIN_ARROGANT_STURDY_SNAKE_BROKEN);
        availableFlags.add(FlagConstants.TABLET_GLOW_TWIN_LEFT_OF_BACKSIDE_GRAIL_BROKEN);
        availableFlags.add(FlagConstants.TABLET_GLOW_ENDLESS_FAIRY_SCREEN_BROKEN);
        availableFlags.add(FlagConstants.TABLET_GLOW_ILLUSION_CHILDS_ROOM_BROKEN);
        availableFlags.add(FlagConstants.TABLET_GLOW_ILLUSION_BACKSIDE_DOOR_BROKEN);
        availableFlags.add(FlagConstants.TABLET_GLOW_ILLUSION_MOVER_ATHLELAND_SCREEN_BROKEN);
        availableFlags.add(FlagConstants.TABLET_GLOW_ILLUSION_FOOLS_CONFUSION_CORRIDOR_BROKEN);
        availableFlags.add(FlagConstants.TABLET_GLOW_ILLUSION_FOOLS_CONFUSION_CORRIDOR_SCAN_ROOM_BROKEN);
        availableFlags.add(FlagConstants.TABLET_GLOW_GRAVEYARD_GIANT_THEXDE_SCREEN_BROKEN);
        availableFlags.add(FlagConstants.TABLET_GLOW_BIRTH_SKANDA_ASURAS_ROOM_BROKEN);

        for(int flag = FIRST_AVAILABLE_RANDOM_GRAPHICS_FLAG; flag < LAST_AVAILABLE_RANDOM_GRAPHICS_FLAG; flag++) {
            availableFlags.add(flag);
        }
        for(int flag = 2699; flag >= 2600; flag--) {
            availableFlags.add(flag);
        }
        return availableFlags;
    }

    private boolean isFlagDependentItem(String itemName) {
        return "Shell Horn".equals(itemName) || "Bronze Mirror".equals(itemName) || "Plane Model".equals(itemName)
                || "Philospher's Ocarina".equals(itemName) || "Fruit of Eden".equals(itemName)
                || "Talisman".equals(itemName) || "Diary".equals(itemName) || "Treasures".equals(itemName)
                || "Dimensional Key".equals(itemName) || "Twin Statue".equals(itemName) || "Eye of Truth".equals(itemName)
                || "Isis' Pendant".equals(itemName) || "Provocative Bathing Suit".equals(itemName) || "Book of the Dead".equals(itemName)
                || "Woman Statue".equals(itemName) || "Lamp of Time".equals(itemName)
                || itemName.startsWith("Map (") || itemName.startsWith("Sacred Orb (") || itemName.startsWith("Ankh Jewel (");
    }
}
