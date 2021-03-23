package lmr.randomizer;

import lmr.randomizer.node.CustomDoorPlacement;
import lmr.randomizer.node.CustomItemPlacement;
import lmr.randomizer.node.CustomTransitionPlacement;
import lmr.randomizer.random.ShopRandomizationEnum;

import java.util.List;

public class HolidayModePlacements {
    public static void applyCustomPlacements() {
        if(Settings.isHalloweenMode()) {
            addCustomPlacementsHalloween();
        }
        else if(Settings.isFools2020Mode()) {
            addCustomPlacementsFools2020();
        }
        else if(Settings.isFools2021Mode()) {
            addCustomPlacementsFools2021();
        }
    }

    private static void addCustomPlacementsHalloween() {
        CustomItemPlacement customItemPlacement = new CustomItemPlacement("xmailer.exe", "Provocative Bathing Suit", null);
        DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

        customItemPlacement = new CustomItemPlacement("Shop 2 (Surface) Item 3", "Buckler", (short)5, (short)1);
        DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);
    }

    private static void addCustomPlacementsFools2020() {
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

    private static void addCustomPlacementsFools2021() {
        if(Settings.isFoolsGameplay()) {
            DataFromFile.getCustomPlacementData().setAutomaticMantras(true);
            DataFromFile.getCustomPlacementData().setAlternateMotherAnkh(false);
            DataFromFile.getCustomPlacementData().setMedicineColor("Yellow");
            DataFromFile.getCustomPlacementData().setStartingLocation(0);

            List<String> removedItems = DataFromFile.getCustomPlacementData().getRemovedItems();
            if (!removedItems.contains("Ankh Jewel (Chamber of Birth)")) {
                removedItems.add("Ankh Jewel (Chamber of Birth)");
            }

            CustomItemPlacement customItemPlacement = new CustomItemPlacement("Treasures", "Map (Temple of the Sun)", null); // Unreachable/removed item.
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Coin: Twin (Escape)", "Pepper", null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 18 (Lil Bro) Item 1", "Weights", null, null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 20 (Twin Labs) Item 1", "Weights", null, null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            customItemPlacement = new CustomItemPlacement("Shop 21 (Unsolvable) Item 1", "Weights", null, null);
            DataFromFile.getCustomPlacementData().getCustomItemPlacements().add(customItemPlacement);

            List<CustomTransitionPlacement> transitions = DataFromFile.getCustomPlacementData().getCustomTransitionPlacements();
            List<CustomDoorPlacement> doors = DataFromFile.getCustomPlacementData().getCustomDoorPlacements();

            transitions.add(new CustomTransitionPlacement("Transition: Sun L1", "Transition: Sun R1"));
            transitions.add(new CustomTransitionPlacement("Transition: Sun R2", "Transition: Moonlight L1"));
            transitions.add(new CustomTransitionPlacement("Transition: Sun U1", "Transition: Shrine D3"));
            doors.add(new CustomDoorPlacement("Door: F3", "Door: F8", null));
        }
    }
}
