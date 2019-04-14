package lmr.randomizer.node;

import java.util.ArrayList;
import java.util.List;

public class CustomPlacementData {
    private List<CustomItemPlacement> customItemPlacements = new ArrayList<>();
    private List<CustomDoorPlacement> customDoorPlacements = new ArrayList<>();
    private List<CustomTransitionPlacement> customTransitionPlacements = new ArrayList<>();
    private List<String> removedLogicNodes = new ArrayList<>();
    private List<String> removedItems = new ArrayList<>();
    private List<String> cursedChests = new ArrayList<>();
    private List<String> startingItems = new ArrayList<>();
    private String startingWeapon;
    private Integer startingLocation;

    private String medicineColor;

    private boolean customized;
    private boolean alternateMotherAnkh;
    private boolean automaticMantras;

    public List<CustomItemPlacement> getCustomItemPlacements() {
        return customItemPlacements;
    }

    public List<CustomDoorPlacement> getCustomDoorPlacements() {
        return customDoorPlacements;
    }

    public List<CustomTransitionPlacement> getCustomTransitionPlacements() {
        return customTransitionPlacements;
    }

    public List<String> getRemovedLogicNodes() {
        return removedLogicNodes;
    }

    public List<String> getRemovedItems() {
        return removedItems;
    }

    public List<String> getCursedChests() {
        return cursedChests;
    }

    public List<String> getStartingItems() {
        return startingItems;
    }

    public String getStartingWeapon() {
        return startingWeapon;
    }

    public void setStartingWeapon(String startingWeapon) {
        this.startingWeapon = startingWeapon;
    }

    public Integer getStartingLocation() {
        return startingLocation;
    }

    public void setStartingLocation(Integer startingLocation) {
        this.startingLocation = startingLocation;
    }

    public String getMedicineColor() {
        return medicineColor;
    }

    public void setMedicineColor(String medicineColor) {
        this.medicineColor = medicineColor;
    }

    public boolean isCustomized() {
        return customized;
    }

    public void setCustomized(boolean customized) {
        this.customized = customized;
    }

    public boolean isAlternateMotherAnkh() {
        return alternateMotherAnkh;
    }

    public void setAlternateMotherAnkh(boolean alternateMotherAnkh) {
        this.alternateMotherAnkh = alternateMotherAnkh;
    }

    public boolean isAutomaticMantras() {
        return automaticMantras;
    }

    public void setAutomaticMantras(boolean automaticMantras) {
        this.automaticMantras = automaticMantras;
    }
}
