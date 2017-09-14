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
    private List<String> PROGRESSION_ITEMS = Arrays.asList("Helmet", "Bronze Mirror", "Eye of Truth", "Anchor", "Plane Model",
            "Philosopher's Ocarina", "Fruit of Eden", "Twin Statue", "Dimensional Key", "yagomap.exe", "yagostr.exe", "torude.exe",
            "mantra.exe", "Djed Pillar", "Ice Cape", "Magatama Jewel", "Woman Statue", "Maternity Statue", "Cog of the Soul",
            "Origin Seal", "Birth Seal", "Life Seal", "Death Seal", "Crystal Skull", "Dragon Bone", "Mini Doll", "Treasures",
            "Mulana Talisman", "Diary", "Pepper", "Serpent Staff", "Talisman", "Vessel", "Key of Eternity", "Pochette Key");
    private List<String> PROGRESSION_ITEMS_WITH_UTILITY = Arrays.asList("Book of the Dead", "Isis' Pendant", "Lamp of Time",
            "Shuriken", "Caltrops", "Rolling Shuriken", "Bomb", "Flare Gun", "Chakram", "Earth Spear", "Pistol", "Angel Shield",
            "Knife", "Key Sword", "Axe", "Katana", "Ring", "Scalesphere", "Fake Silver Shield", "Silver Shield",
            "Gauntlet", "deathv.exe", "move.exe", "randc.exe", "miracle.exe", "mekuri.exe", "Grapple Claw");
    private List<String> LUXURY_ITEMS = Arrays.asList("Mobile Super X2", "Glove", "Crucifix", "Fairy Clothes", "Scriptures",
            "Bracelet", "Perfume", "Spaulder", "bounce.exe", "lamulana.exe", "Forbidden Treasure");
    private List<String> USELESS_ITEMS = Arrays.asList("Waterproof Case", "Heatproof Case", "Shell Horn", "xmailer.exe",
            "bunemon.exe", "bunplus.com", "guild.exe", "emusic.exe", "beolamu.exe", "Buckler");

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
                return 40;
            }
            if(priceRoll == 2) {
                return 45;
            }
            return 50;
        }
        if("Bomb Ammo".equals(itemName)) {
            int priceRoll = random.nextInt(5);
            if(priceRoll < 3) {
                return 100;
            }
            if(priceRoll == 3) {
                return 80;
            }
            return 110;
        }
        if("Chakram Ammo".equals(itemName)) {
            int priceRoll = random.nextInt(2);
            if(priceRoll == 0) {
                return 50;
            }
            return 55;
        }
        if("Caltrops Ammo".equals(itemName)) {
            int priceRoll = random.nextInt(5);
            if(priceRoll < 4) {
                return 30;
            }
            return 40;
        }
        if("Pistol Ammo".equals(itemName)) {
            // It looks like 6 is the count given by the Moonlight shop
            int priceRoll = random.nextInt(4);
            if(priceRoll < 3) {
                return 400;
            }
            return 350;
        }

        if(itemName.contains("Map") || USELESS_ITEMS.contains(itemName)) {
            // 20-60, in increments of 10
            return (short)(20 + 10 * random.nextInt(5));
        }
        if(itemName.contains("Ankh Jewel")) {
            // 100-150, in increments of 10
            return (short)(100 + 10 * random.nextInt(6));
        }
        if(itemName.contains("Sacred Orb")) {
            // 100-200, in increments of 50
            return (short)(100 + 50 * random.nextInt(3));
        }
        if("Hand Scanner".equals(itemName)) {
            // 5-15, in increments of 5
            return (short)(5 + 5 * random.nextInt(3));
        }
        if("Isis' Pendant".equals(itemName) || "capstar.exe".equals(itemName) || "mirai.exe".equals(itemName)) {
            // 80-120, in increments of 10
            return (short)(80 + 10 * random.nextInt(5));
        }
        if("Flail Whip".equals(itemName)) {
            // 200-300, in increments of 25
            int randomRoll = random.nextInt(5);
            if(randomRoll < 3) {
                return 250;
            }
            if(randomRoll == 3) {
                return 275;
            }
            return 300;
        }
        if("Chain Whip".equals(itemName)) {
            // 150-200, in increments of 10
            return (short)(150 + 10 * random.nextInt(6));
        }
        if("reader.exe".equals(itemName)) {
            // 40-50, in increments of 5
            return (short)(40 + 5 * random.nextInt(3));
        }
        if(itemName.equals("Hermes' Boots") || itemName.equals("Feather") || itemName.equals("Holy Grail")
                || ("Grapple Claw".equals(itemName) && Settings.getEnabledGlitches().contains("Raindrop"))
                || PROGRESSION_ITEMS.contains(itemName)) {
            // 30-80, in increments of 10, weighted towards the middle
            int randomRoll = random.nextInt(8);
            if(randomRoll < 2) {
                return 30;
            }
            if(randomRoll == 2) {
                return 50;
            }
            if(randomRoll == 3) {
                return 60;
            }
            if(randomRoll == 4) {
                return 70;
            }
            return 80;
        }

        if(PROGRESSION_ITEMS_WITH_UTILITY.contains(itemName)) {
            // 50-150, in increments of 5
            return (short)(50 + 5 * random.nextInt(11));
        }
        if(LUXURY_ITEMS.contains(itemName)) {
            // 150-300, in increments of 30
            return (short)(150 + 30 * random.nextInt(6));
        }
        if(USELESS_ITEMS.contains(itemName)) {
            // 20-100, in increments of 20
            return (short)(20 + 20 * random.nextInt(5));
        }
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
