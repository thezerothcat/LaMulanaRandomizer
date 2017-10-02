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

    // 50-70
    private List<String> PRICE_TIER1 = Arrays.asList("yagomap.exe", "bunemon.exe", "Glove", "Shell Horn",
            "xmailer.exe", "bunplus.com", "guild.exe", "Buckler", "Helmet", "Bronze Mirror", "emusic.exe", "beolamu.exe",
            "Waterproof Case", "Heatproof Case", "Map");

    // 80-120
    private List<String> PRICE_TIER2 = Arrays.asList("Key of Eternity", "Birth Seal", "Life Seal", "Death Seal",
            "Knife", "Key Sword", "Shuriken", "Rolling Shuriken", "Scalesphere", "Pepper", "Talisman", "Magatama Jewel",
            "yagostr.exe", "Mini Doll", "Treasures", "Anchor", "Grapple Claw", "Perfume", "Hand Scanner", "Hermes' Boots",
            "bounce.exe", "Ankh Jewel", "Woman Statue", "Maternity Statue", "Flare Gun", "Serpent Staff", "Holy Grail",
            "mirai.exe", "mekuri.exe", "reader.exe", "capstar.exe", "Dragon Bone", "Diary", "Eye of Truth",
            "Cog of the Soul", "Dimensional Key", "Earth Spear", "Pochette Key");

    // 140-160
    private List<String> PRICE_TIER3 = Arrays.asList("Feather", "Origin Seal", "Fruit of Eden", "Twin Statue",
            "Ice Cape", "Fake Silver Shield", "Silver Shield",
            "Isis' Pendant", "Bracelet", "Crucifix", "miracle.exe", "torude.exe", "Mobile Super X2", "Sacred Orb",
            "mantra.exe", "Djed Pillar", "Plane Model", "Philosopher's Ocarina", "Mulana Talisman", "Book of the Dead",
            "Caltrops", "Pistol", "Katana", "Chain Whip", "Chakram",  "Bomb", "Axe", "Crystal Skull");

    // 180-220
    private List<String> PRICE_TIER4 = Arrays.asList(
            "Lamp of Time", "Vessel", "Ring", "Angel Shield",
            "move.exe", "randc.exe", "Fairy Clothes", "Scriptures", "Gauntlet", "deathv.exe",
            "Provocative Bathing Suit", "Spaulder", "Flail Whip", "lamulana.exe");

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
            if(!normalPriceWeightsPlaced) {
                normalPriceWeightsPlaced = true;
                return 10;
            }
            int priceRoll = random.nextInt(20);
            if(priceRoll == 0 || priceRoll == 1) {
                return 15;
            }
            if(priceRoll == 2) {
                return 20;
            }
            return 10;
        }
        if("Shuriken Ammo".equals(itemName)) {
            return 10;
        }
        if("Rolling Shuriken Ammo".equals(itemName)) {
            return 10;
        }
        if("Earth Spear Ammo".equals(itemName)) {
            int priceRoll = random.nextInt(5);
            if(priceRoll < 3) {
                return 20;
            }
            return 25;
        }
        if("Flare Gun Ammo".equals(itemName)) {
            int priceRoll = random.nextInt(4);
            if(priceRoll < 2) {
                return 35;
            }
            if(priceRoll == 2) {
                return 40;
            }
            return 45;
        }
        if("Bomb Ammo".equals(itemName)) {
            int priceRoll = random.nextInt(5);
            if(priceRoll < 3) {
                return 60;
            }
            if(priceRoll == 3) {
                return 50;
            }
            return 70;
        }
        if("Chakram Ammo".equals(itemName)) {
            int priceRoll = random.nextInt(2);
            if(priceRoll == 0) {
                return 40;
            }
            return 45;
        }
        if("Caltrops Ammo".equals(itemName)) {
            int priceRoll = random.nextInt(5);
            if(priceRoll < 4) {
                return 30;
            }
            return 35;
        }
        if("Pistol Ammo".equals(itemName)) {
            // It looks like 6 is the count given by the Moonlight shop
            int priceRoll = random.nextInt(4);
            if(priceRoll < 3) {
                return 400;
            }
            return 350;
        }

        if("Hand Scanner".equals(itemName) && Settings.getNonRandomizedItems().contains("Hand Scanner")) {
            return 10;
        }
        if("Hermes' Boots".equals(itemName) && Settings.getNonRandomizedItems().contains("Hermes' Boots")) {
            return 60;
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
            return (short)(50 + 5 * random.nextInt(5));
        }
        else if(PRICE_TIER2.contains(itemName)) {
            return (short)(80 + 10 * random.nextInt(5));
        }
        else if(PRICE_TIER3.contains(itemName)) {
            return (short)(140 + 5 * random.nextInt(5));
        }
        else if(PRICE_TIER4.contains(itemName)) {
            return (short)(180 + 10 * random.nextInt(5));
        }
//        else if(PRICE_TIER5.contains(itemName)) {
//            return (short)(250 + 50 * random.nextInt(3));
//        }
        return (short)(10 + 10 * random.nextInt(25) + 1);
    }

    private static short getCount(String item) {
        if("Weights".equals(item)) {
            return 5;
        }
        if("Shuriken Ammo".equals(item)) {
            return 10;
        }
        if("Rolling Shuriken Ammo".equals(item)) {
            return 10;
        }
        if("Earth Spear Ammo".equals(item)) {
            return 10;
        }
        if("Flare Gun Ammo".equals(item)) {
            return 10;
        }
        if("Bomb Ammo".equals(item)) {
            return 10;
        }
        if("Chakram Ammo".equals(item)) {
            return 2;
        }
        if("Caltrops Ammo".equals(item)) {
            return 10;
        }
        if("Pistol Ammo".equals(item)) {
            return 1; // It looks like 6 is the count given by the Moonlight shop
        }
        return 1;
    }
}
