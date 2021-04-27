package lmr.randomizer.random;

import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.rcd.object.GameObject;
import lmr.randomizer.rcd.object.Screen;
import lmr.randomizer.update.GameDataTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SealRandomizer {
    private List<String> originSeals;
    private List<String> birthSeals;
    private List<String> lifeSeals;
    private List<String> deathSeals;

    public SealRandomizer() {
        originSeals = new ArrayList<>();
        birthSeals = new ArrayList<>();
        lifeSeals = new ArrayList<>();
        deathSeals = new ArrayList<>();

        if(!Settings.isRandomizeSeals()) {
            originSeals.add("Seal: O1");
            originSeals.add("Seal: O2");
            originSeals.add("Seal: O3");
            originSeals.add("Seal: O4");
            originSeals.add("Seal: O5");
            originSeals.add("Seal: O6");
            originSeals.add("Seal: O7");
            originSeals.add("Seal: O8");
            birthSeals.add("Seal: B1");
            birthSeals.add("Seal: B2");
            birthSeals.add("Seal: B3");
            birthSeals.add("Seal: B4");
            birthSeals.add("Seal: B5");
            birthSeals.add("Seal: B6");
            birthSeals.add("Seal: B7");
            lifeSeals.add("Seal: L1");
            lifeSeals.add("Seal: L2");
            lifeSeals.add("Seal: L3");
            lifeSeals.add("Seal: L4");
            lifeSeals.add("Seal: L5");
            lifeSeals.add("Seal: L6");
            lifeSeals.add("Seal: L7");
            lifeSeals.add("Seal: L8");
            deathSeals.add("Seal: D1");
            deathSeals.add("Seal: D2");
            deathSeals.add("Seal: D3");
            deathSeals.add("Seal: D4");
            deathSeals.add("Seal: D5");
            deathSeals.add("Seal: D6");
        }
    }

    public void assignSeals(Random random) {
        if(Settings.isRandomizeSeals()) {
            originSeals.clear();
            birthSeals.clear();
            lifeSeals.clear();
            deathSeals.clear();

            // Origin seals
            for (int i = 1; i <= 8; i++) {
                int sealNumber = random.nextInt(4);
                if(sealNumber == 0) {
                    originSeals.add("Seal: O" + i);
                }
                else if(sealNumber == 1) {
                    birthSeals.add("Seal: O" + i);
                }
                else if(sealNumber == 2) {
                    lifeSeals.add("Seal: O" + i);
                }
                else if(sealNumber == 3) {
                    deathSeals.add("Seal: O" + i);
                }
            }

            // Birth seals
            for (int i = 1; i <= 7; i++) {
                int sealNumber = random.nextInt(4);
                if(sealNumber == 0) {
                    originSeals.add("Seal: B" + i);
                }
                else if(sealNumber == 1) {
                    birthSeals.add("Seal: B" + i);
                }
                else if(sealNumber == 2) {
                    lifeSeals.add("Seal: B" + i);
                }
                else if(sealNumber == 3) {
                    deathSeals.add("Seal: B" + i);
                }
            }

            // Life seals
            for (int i = 1; i <= 8; i++) {
                int sealNumber = random.nextInt(4);
                if(sealNumber == 0) {
                    originSeals.add("Seal: L" + i);
                }
                else if(sealNumber == 1) {
                    birthSeals.add("Seal: L" + i);
                }
                else if(sealNumber == 2) {
                    lifeSeals.add("Seal: L" + i);
                }
                else if(sealNumber == 3) {
                    deathSeals.add("Seal: L" + i);
                }
            }

            // Death seals
            for (int i = 1; i <= 6; i++) {
                int sealNumber = random.nextInt(4);
                if(sealNumber == 0) {
                    originSeals.add("Seal: D" + i);
                }
                else if(sealNumber == 1) {
                    birthSeals.add("Seal: D" + i);
                }
                else if(sealNumber == 2) {
                    lifeSeals.add("Seal: D" + i);
                }
                else if(sealNumber == 3) {
                    deathSeals.add("Seal: D" + i);
                }
            }
        }
    }

    public List<String> getNodesForSeal(String itemName) {
        if ("Origin Seal".equals(itemName)) {
            return originSeals;
        }
        if ("Birth Seal".equals(itemName)) {
            return birthSeals;
        }
        if ("Life Seal".equals(itemName)) {
            return lifeSeals;
        }
        if ("Death Seal".equals(itemName)) {
            return deathSeals;
        }
        return new ArrayList<>(0);
    }

    public int getSealArgByNodeName(String nodeName) {
        for(String originSealNode : originSeals) {
            if(nodeName.equals(originSealNode)) {
                return 0;
            }
        }
        for(String birthSealNode : birthSeals) {
            if(nodeName.equals(birthSealNode)) {
                return 1;
            }
        }
        for(String lifeSealNode : lifeSeals) {
            if(nodeName.equals(lifeSealNode)) {
                return 2;
            }
        }
        for(String deathSealNode : deathSeals) {
            if(nodeName.equals(deathSealNode)) {
                return 3;
            }
        }
        FileUtils.logFlush("Failed to find assigned seal for node " + nodeName);
        return 0;
    }

    public void updateSeals() {
        for (String sealNode : originSeals) {
            GameDataTracker.writeSeals(sealNode, (short)0);
        }
        for (String sealNode : birthSeals) {
            GameDataTracker.writeSeals(sealNode, (short)1);
        }
        for (String sealNode : lifeSeals) {
            GameDataTracker.writeSeals(sealNode, (short)2);
        }
        for (String sealNode : deathSeals) {
            GameDataTracker.writeSeals(sealNode, (short)3);
        }
    }

    public static String getSealNode(GameObject sealObject) {
        if (sealObject.getObjectContainer() instanceof Screen) {
            Screen screen = (Screen)sealObject.getObjectContainer();
            if(screen.getZoneIndex() == 0) {
                if(screen.getRoomIndex() == 1 && screen.getScreenIndex() == 1) {
                    // Guidance - Crucifix seal / Offer 3 lights; governed by flag 0x12d
                    return "Seal: L1";
                }
            }
            else if(screen.getZoneIndex() == 1) {
                if(screen.getRoomIndex() == 1 && screen.getScreenIndex() == 0) {
                    // Surface - coin chest; governed by flag 0x14d
                    return "Seal: L2";
                }
                else if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
                    // Surface - Birth Seal chest; governed by flag 0x14a
                    return "Seal: O1";
                }
            }
            else if(screen.getZoneIndex() == 3) {
                if(screen.getRoomIndex() == 3 && screen.getScreenIndex() == 0) {
                    // Sun - Mulbruk's seal; governed by flag 0x18e
                    return "Seal: O2";
                }
                else if(screen.getRoomIndex() == 5 && screen.getScreenIndex() == 0) {
                    // Sun - Bronze Mirror room; governed by flag 0x187
                    return "Seal: O3";
                }
                else if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                    // Sun - Discount shop room; governed by flag 0x18d
                    return "Seal: D1";
                }
            }
            else if(screen.getZoneIndex() == 4) {
                if(screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                    // Spring - Sacred Orb chest; governed by flag 0x190
                    return "Seal: B1";
                }
                else if(screen.getRoomIndex() == 3 && screen.getScreenIndex() == 3) {
                    // Spring - Mr Gyonin's shop; governed by flag 0x197
                    return "Seal: O4";
                }
                else if(screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
                    // Spring - Bahamut's room; governed by flag 0x19b
                    return "Seal: O5";
                }
            }
            else if(screen.getZoneIndex() == 5) {
                if(screen.getRoomIndex() == 2 && screen.getScreenIndex() == 1) {
                    // Inferno - seal blocking entry to Pazuzu; governed by flag 0x1af
                    return "Seal: B2";
                }
            }
            else if(screen.getZoneIndex() == 6) {
                if(screen.getRoomIndex() == 6 && screen.getScreenIndex() == 1) {
                    if(sealObject.getArgs().get(0) == 1) {
                        // Extinction - Life Seal chest; governed by flag 0x1c8
                        return "Seal: B3";
                    }
                    else if(sealObject.getArgs().get(0) == 2) {
                        // Extinction - Extinction perma-light; governed by flag 0x1c2
                        return "Seal: L3";
                    }
                }
            }
            else if(screen.getZoneIndex() == 8) {
                if(screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                    // Endless - Shop seal; governed by flag 0x200
                    return "Seal: O6";
                }
            }
            else if(screen.getZoneIndex() == 9) {
                if(screen.getRoomIndex() == 1 && screen.getScreenIndex() == 0) {
                    // Shrine - Crystal Skull chest; governed by flag 0x211
                    return "Seal: L4";
                }
                else if(screen.getRoomIndex() == 1 && screen.getScreenIndex() == 1) {
                    if(sealObject.getArgs().get(0) == 0) {
                        // Shrine - 4 Seals Origin edition; governed by flag 0x383
                        return "Seal: O7";
                    }
                    else if(sealObject.getArgs().get(0) == 1) {
                        // Shrine - 4 Seals Birth edition; governed by flag 0x385
                        return "Seal: B4";
                    }
                    else if(sealObject.getArgs().get(0) == 2) {
                        // Shrine - 4 Seals Life edition; governed by flag 0x38f
                        return "Seal: L5";
                    }
                    else if(sealObject.getArgs().get(0) == 3) {
                        // Shrine - 4 Seals Death edition; governed by flag 0x390
                        return "Seal: D2";
                    }
                }
                else if(screen.getRoomIndex() == 3 && screen.getScreenIndex() == 1) {
                    // Shrine - Laptop room; governed by flag 0x210
                    return "Seal: D3";
                }
            }
            else if(screen.getZoneIndex() == 10) {
                if(screen.getRoomIndex() == 2 && screen.getScreenIndex() == 1) {
                    // Illusion - Chi You seal; governed by flag 0x230
                    return "Seal: B5";
                }
            }
            else if(screen.getZoneIndex() == 11) {
                if(screen.getRoomIndex() == 4 && screen.getScreenIndex() == 1) {
                    // Graveyard - Gauntlet chest; governed by flag 0x248
                    return "Seal: L6";
                }
            }
            else if(screen.getZoneIndex() == 12) {
                if(screen.getRoomIndex() == 9 && screen.getScreenIndex() == 1) {
                    // Moonlight - Path to Anubis; governed by flag 0x269
                    return "Seal: B6";
                }
            }
            else if(screen.getZoneIndex() == 14) {
                if(screen.getRoomIndex() == 8 && screen.getScreenIndex() == 2) {
                    // Ruin - Nuwa; governed by flag 0x298
                    return "Seal: D4";
                }
            }
            else if(screen.getZoneIndex() == 15) {
                if(screen.getRoomIndex() == 3 && screen.getScreenIndex() == 0) {
                    // Birth - Extinction perma-light, Chamber of Birth edition; governed by flag 0x1c2
                    return "Seal: L7";
                }
            }
            else if(screen.getZoneIndex() == 17) {
                if(screen.getRoomIndex() == 10 && screen.getScreenIndex() == 0) {
                    // Dimensional - Sacred Orb chest; governed by flag 0x2bd
                    return "Seal: D5";
                }
            }
            else if(screen.getZoneIndex() == 18) {
                if(screen.getRoomIndex() == 3 && screen.getScreenIndex() == 0) {
                    if(sealObject.getArgs().get(0) == 0) {
                        // Shrine - Mother Ankh seal, Origin edition
                        return "Seal: O8";
                    }
                    else if(sealObject.getArgs().get(0) == 1) {
                        // Shrine - Mother Ankh seal, Birth edition
                        return "Seal: B7";
                    }
                    else if(sealObject.getArgs().get(0) == 2) {
                        // Shrine - Mother Ankh seal, Life edition
                        return "Seal: L8";
                    }
                    else if(sealObject.getArgs().get(0) == 3) {
                        // Shrine - Mother Ankh seal, Death edition
                        return "Seal: D6";
                    }
                }
            }
            else if(screen.getZoneIndex() == 22) {
                if(screen.getRoomIndex() == 1 && screen.getScreenIndex() == 0) {
                    // Night Surface - coin chest; governed by flag 0x14d
                    return "Seal: L2";
                }
                else if(screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
                    // Night Surface - Birth Seal chest; governed by flag 0x14a
                    return "Seal: O1";
                }
            }
        }
        FileUtils.logFlush("Unable to find node name for seal object");
        return null;
    }
}
