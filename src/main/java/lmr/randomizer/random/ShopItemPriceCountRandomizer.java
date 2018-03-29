package lmr.randomizer.random;

import javafx.util.Pair;
import lmr.randomizer.Settings;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by thezerothcat on 8/19/2017.
 */
public class ShopItemPriceCountRandomizer {
    private boolean specialAmmoPlaced = false;
    private boolean normalPriceWeightsPlaced = false;

    private Random random;

//    // 50-70
//    private List<String> PRICE_TIER1 = Arrays.asList("yagomap.exe", "bunemon.exe", "Glove", "Shell Horn",
//            "xmailer.exe", "bunplus.com", "guild.exe", "Buckler", "Helmet", "Bronze Mirror", "emusic.exe", "beolamu.exe",
//            "Waterproof Case", "Heatproof Case", "Map");
//
//    // 80-120
//    private List<String> PRICE_TIER2 = Arrays.asList("Key of Eternity", "Birth Seal", "Life Seal", "Death Seal",
//            "Knife", "Key Sword", "Shuriken", "Rolling Shuriken", "Scalesphere", "Pepper", "Talisman", "Magatama Jewel",
//            "yagostr.exe", "Mini Doll", "Treasures", "Anchor", "Grapple Claw", "Perfume", "Hand Scanner", "Hermes' Boots",
//            "bounce.exe", "Ankh Jewel", "Woman Statue", "Maternity Statue", "Flare Gun", "Serpent Staff", "Holy Grail");
//
//    // 140-160
//    private List<String> PRICE_TIER3 = Arrays.asList("Feather", "Origin Seal", "Fruit of Eden", "Twin Statue",
//            "Eye of Truth", "Diary", "Ice Cape", "Dragon Bone", "Caltrops", "Earth Spear", "Pistol", "Katana",
//            "Fake Silver Shield", "Silver Shield", "Isis' Pendant", "Bracelet", "Crucifix", "miracle.exe", "torude.exe",
//            "mirai.exe", "mekuri.exe", "reader.exe", "capstar.exe", "Sacred Orb", "Mobile Super X2");
//
//    // 180-220
//    private List<String> PRICE_TIER4 = Arrays.asList("Plane Model", "Philosopher's Ocarina", "Dimensional Key",
//            "mantra.exe", "Djed Pillar", "Cog of the Soul", "Crystal Skull", "Mulana Talisman", "Vessel", "Pochette Key",
//            "Ring", "Chain Whip", "Axe", "Chakram",  "Bomb", "Book of the Dead", "Angel Shield", "Lamp of Time",
//            "move.exe", "randc.exe");
//
//    // 250-350
//    private List<String> PRICE_TIER5 = Arrays.asList("Fairy Clothes", "Scriptures", "Gauntlet", "deathv.exe",
//            "Provocative Bathing Suit", "Spaulder", "Flail Whip", "lamulana.exe");

    // 40-60
    private List<String> PRICE_TIER1 = Arrays.asList("yagomap.exe", "bunemon.exe", "Glove", "Shell Horn",
            "xmailer.exe", "bunplus.com", "guild.exe", "Buckler", "Helmet", "Bronze Mirror", "emusic.exe", "beolamu.exe",
            "Waterproof Case", "Heatproof Case", "Map");

    // 70-110
    private List<String> PRICE_TIER2 = Arrays.asList("Key of Eternity", "Birth Seal", "Life Seal", "Death Seal",
            "Knife", "Key Sword", "Shuriken", "Rolling Shuriken", "Scalesphere", "Pepper", "Talisman", "Magatama Jewel",
            "yagostr.exe", "Mini Doll", "Treasures", "Anchor", "Grapple Claw", "Perfume", "Hand Scanner", "Hermes' Boots",
            "bounce.exe", "Ankh Jewel", "Woman Statue", "Maternity Statue", "Flare Gun", "Serpent Staff", "Holy Grail",
            "mirai.exe", "mekuri.exe", "reader.exe", "capstar.exe", "Dragon Bone", "Diary", "Eye of Truth",
            "Cog of the Soul", "Dimensional Key", "Earth Spear", "Pochette Key");

    // 130-150
    private List<String> PRICE_TIER3 = Arrays.asList("Feather", "Origin Seal", "Fruit of Eden", "Twin Statue",
            "Ice Cape", "Fake Silver Shield", "Silver Shield", "Vessel", "Whip",
            "Isis' Pendant", "Bracelet", "Crucifix", "miracle.exe", "torude.exe", "Mobile Super X2", "Sacred Orb",
            "mantra.exe", "Djed Pillar", "Plane Model", "Philosopher's Ocarina", "Mulana Talisman", "Book of the Dead",
            "Caltrops", "Pistol", "Katana", "Chain Whip", "Chakram",  "Bomb", "Axe", "Ring", "Crystal Skull");

    // 170-210
    private List<String> PRICE_TIER4 = Arrays.asList("Flail Whip");

    public ShopItemPriceCountRandomizer(Random random) {
        this.random = random;
    }

    public Pair<Short, Short> getItemPriceAndCount(String itemName) {
        if(!specialAmmoPlaced && "Pistol Ammo".equals(itemName)) {
            // Special case
            if(random.nextInt(10) == 0) {
                return new Pair<>((short)400, (short)6);
            }
        }
        short price = getPrice(itemName);
        short count = getCount(itemName);
        return new Pair<>(price, count);
    }

    public short getPrice(String itemName) {
        if("Weights".equals(itemName)) {
            return 1;
        }
        if("Shuriken Ammo".equals(itemName)) {
            return 1;
        }
        if("Rolling Shuriken Ammo".equals(itemName)) {
            return 1;
        }
        if("Earth Spear Ammo".equals(itemName)) {
            return 1;
        }
        if("Flare Gun Ammo".equals(itemName)) {
            return 1;
        }
        if("Bomb Ammo".equals(itemName)) {
            return 11;
        }
        if("Chakram Ammo".equals(itemName)) {
            return 1;
        }
        if("Caltrops Ammo".equals(itemName)) {
            return 1;
        }
        if("Pistol Ammo".equals(itemName)) {
            return 111;
        }

        if("Hand Scanner".equals(itemName) && Settings.getSurfaceItems().contains("Hand Scanner")) {
            return 11;
        }
        if("reader.exe".equals(itemName) && Settings.getSurfaceItems().contains("reader.exe")) {
            return 11;
        }
        if("Hermes' Boots".equals(itemName) && Settings.getSurfaceItems().contains("Hermes' Boots")) {
            return 11;
        }
        if("Feather".equals(itemName) && Settings.getSurfaceItems().contains("Feather")) {
            return 11;
        }
        if("Grapple Claw".equals(itemName) && Settings.getSurfaceItems().contains("Grapple Claw")) {
            return 11;
        }
        if("bunemon.exe".equals(itemName) && Settings.getSurfaceItems().contains("bunemon.exe")) {
            return 1;
        }

        if(itemName.contains("Map")) {
            itemName = "Map";
        }
        else if(itemName.contains("Ankh Jewel")) {
            itemName = "Ankh Jewel";
        }
        else if(itemName.contains("Sacred Orb")) {
            itemName = "Sacred Orb";
        }

        if(PRICE_TIER1.contains(itemName)) {
            return 11;
        }
        else if(PRICE_TIER2.contains(itemName)) {
            return 11;
        }
        else if(PRICE_TIER3.contains(itemName)) {
            return 11;
        }
        else if(PRICE_TIER4.contains(itemName)) {
            return (short)999;
        }
//        else if(PRICE_TIER5.contains(itemName)) {
//            return (short)(250 + 50 * random.nextInt(3));
//        }
        return (short)11;
    }

    private short getCount(String item) {
        if("Weights".equals(item)) {
            return (short)(random.nextInt(10) + 1);
        }
        if("Shuriken Ammo".equals(item)) {
            return 11;
        }
        if("Rolling Shuriken Ammo".equals(item)) {
            return 11;
        }
        if("Earth Spear Ammo".equals(item)) {
            return 11;
        }
        if("Flare Gun Ammo".equals(item)) {
            return 11;
        }
        if("Bomb Ammo".equals(item)) {
            return 11;
        }
        if("Chakram Ammo".equals(item)) {
            return 4;
        }
        if("Caltrops Ammo".equals(item)) {
            return 11;
        }
        if("Pistol Ammo".equals(item)) {
            return 1; // It looks like 6 is the count given by the Moonlight shop
        }
        return 1;
    }
}
