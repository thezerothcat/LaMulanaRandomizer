package lmr.randomizer.util;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.HolidaySettings;
import lmr.randomizer.Settings;
import lmr.randomizer.randomization.data.GameObjectId;

import java.util.*;

public class FlagManager {
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
        if (!HolidaySettings.isEaster2025Mode()) {
            for(int flag = 2699; flag >= 2600; flag--) {
                availableFlags.add(flag);
            }
            availableFlags.add(0xadd); // 2781
        }
        for(int flag = FlagConstants.REMOVED_MAP_SURFACE; flag <= FlagConstants.REMOVED_MAP_DIMENSIONAL; flag++) {
            availableFlags.add(flag); // 2708-2723 / 0xa94-0xaa3
        }
        availableFlags.add(FlagConstants.TABLET_GLOW_TWIN_RIGHT_OF_POISON_2_BROKEN); // 0x4c5
        availableFlags.add(FlagConstants.TABLET_GLOW_TWIN_FAKE_ANKH_JEWEL_BROKEN); // 0x4ca
        availableFlags.add(FlagConstants.TABLET_GLOW_TWIN_ABOVE_YIEAR_BROKEN); // 0x4cb
        availableFlags.add(FlagConstants.TABLET_GLOW_TWIN_ARROGANT_STURDY_SNAKE_BROKEN); // 0x4cf
        availableFlags.add(FlagConstants.TABLET_GLOW_TWIN_LEFT_OF_BACKSIDE_GRAIL_BROKEN); // 0x4d0
        availableFlags.add(FlagConstants.TABLET_GLOW_ENDLESS_FAIRY_SCREEN_BROKEN); // 0x4d6
        availableFlags.add(FlagConstants.TABLET_GLOW_ILLUSION_CHILDS_ROOM_BROKEN); // 0x4e9
        availableFlags.add(FlagConstants.TABLET_GLOW_ILLUSION_BACKSIDE_DOOR_BROKEN); // 0x551
        availableFlags.add(FlagConstants.TABLET_GLOW_ILLUSION_MOVER_ATHLELAND_SCREEN_BROKEN); // 0x4f2
        availableFlags.add(FlagConstants.TABLET_GLOW_ILLUSION_FOOLS_CONFUSION_CORRIDOR_BROKEN); // 0x4f4
        availableFlags.add(FlagConstants.TABLET_GLOW_ILLUSION_FOOLS_CONFUSION_CORRIDOR_SCAN_ROOM_BROKEN); // 0x4f7
        availableFlags.add(FlagConstants.TABLET_GLOW_GRAVEYARD_GIANT_THEXDE_SCREEN_BROKEN); // 0x508
        availableFlags.add(FlagConstants.TABLET_GLOW_BIRTH_SKANDA_ASURAS_ROOM_BROKEN); // 0x53d
        availableFlags.add(FlagConstants.OBSOLETE_FREE_FLAG); // 0xad4

        if(HolidaySettings.isFools2020Mode() || HolidaySettings.isFools2021Mode()) {
            for(int flag = 2762; flag < 2765; flag++) {
                availableFlags.add(flag);
            }
        } else {
            for(int flag = 2730; flag < 2760; flag++) { // 0xaaa through 0xac8 and beyond
                availableFlags.add(flag);
            }
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
