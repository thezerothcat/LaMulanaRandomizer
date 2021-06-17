package lmr.randomizer.rcd.updater;

import lmr.randomizer.util.FlagConstants;
import lmr.randomizer.Settings;
import lmr.randomizer.rcd.object.*;

public class PotUpdater {
    private boolean templeOfMoonlightRemovedPotPresent;
    private boolean towerOfTheGoddessRemovedPotPresent;

    private Screen templeOfMoonlightRemovedPotScreen;
    private Screen towerOfTheGoddessRemovedPotScreen;

    public PotUpdater() {
        templeOfMoonlightRemovedPotPresent = false;
        towerOfTheGoddessRemovedPotPresent = false;
    }

    public void addMissingPots() {
        if(!towerOfTheGoddessRemovedPotPresent) {
            Pot towerOfTheGoddessRemovedPot = new Pot(towerOfTheGoddessRemovedPotScreen, 1500, 400);

            towerOfTheGoddessRemovedPot.setDrops(DropType.NOTHING, 0);
            towerOfTheGoddessRemovedPot.setFlag(-1, 1);
            towerOfTheGoddessRemovedPot.setPotGraphic(PotGraphic.GODDESS);
            towerOfTheGoddessRemovedPot.setSoundEffects(105, 35, 17);
            towerOfTheGoddessRemovedPot.setPitchShift(0);

            towerOfTheGoddessRemovedPotScreen.getObjects().add(towerOfTheGoddessRemovedPot);
        }
        if(!templeOfMoonlightRemovedPotPresent) {
            Pot templeOfMoonlightRemovedPot = new Pot(templeOfMoonlightRemovedPotScreen, 540, 240);

            templeOfMoonlightRemovedPot.setDrops(DropType.NOTHING, 0);
            templeOfMoonlightRemovedPot.setFlag(-1, 1);
            templeOfMoonlightRemovedPot.setPotGraphic(PotGraphic.MOONLIGHT);
            templeOfMoonlightRemovedPot.setSoundEffects(105, 35, 17);
            templeOfMoonlightRemovedPot.setPitchShift(0);

            templeOfMoonlightRemovedPotScreen.getObjects().add(templeOfMoonlightRemovedPot);
        }
    }

    public boolean updatePot(GameObject pot) {
        ObjectContainer objectContainer = pot.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }

        Screen screen = (Screen)objectContainer;
        int zoneIndex = screen.getZoneIndex();
        int roomIndex = screen.getRoomIndex();
        int screenIndex = screen.getScreenIndex();
        if(Settings.isFools2021Mode() && zoneIndex == 3) {
            // Flares in all Sun pots
            pot.getArgs().set(0, DropType.FLARE_GUN_AMMO.getValue());
            pot.getArgs().set(1, (short)80);
        }

        if (Settings.isFools2021Mode()) {
            if(zoneIndex == 17 && roomIndex == 9 && screenIndex == 1) {
                // Dimensional - Umu Dabrutu's room
                if(pot.getY() > 300) {
                    if(pot.getX() == 700) {
                        pot.getArgs().set(4, (short)7);
                        pot.getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_8, ByteOp.FLAG_EQUALS, 0));
                        pot.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_8, ByteOp.FLAG_EQUALS, 1));
                    }
                    else { // if(obj.getX() == 1200) {
                        pot.getArgs().set(4, (short)7);
                        pot.getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.FLAG_EQUALS, 0));
                        pot.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.FLAG_EQUALS, 1));
                    }
                }
            }
        }

        if(screen.getZoneIndex() == 21 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
            // Pot in retro surface now has coins
            pot.getArgs().set(0, DropType.COINS.getValue());
            pot.getArgs().set(1, (short)10);
            pot.getArgs().set(2, (short)277);
            pot.getArgs().set(3, (short)16);
        }

        if(pot.getTestByteOperations().isEmpty() || pot.getTestByteOperations().get(0).getIndex() != FlagConstants.ILLUSION_WARP_MAZE_ACTIVE) {
            // Pots - update to 1.3 position and then return (no need to add tracking).
            // Note that there is a pot tied to a warp in Illusion which is removed by randomizer (the warp is always active)
            updatePosition(pot);
            return true;
        }
        return false;
    }

    public void updatePosition(GameObject obj) {
        Screen containingScreen = (Screen)obj.getObjectContainer(); // All pots are linked to a Screen
        if(containingScreen.getZoneIndex() == 0) {
            // Gate of Guidance
            if(containingScreen.getRoomIndex() == 4 && containingScreen.getScreenIndex() == 0) {
                if(obj.getX() == 180) {
                    obj.setX(160);
                }
            }
        }
        else if(containingScreen.getZoneIndex() == 2) {
            // Mausoleum of the Giants
            if(containingScreen.getRoomIndex() == 1 && containingScreen.getScreenIndex() == 0) {
                if(obj.getX() == 220) {
                    obj.setX(20);
                    obj.setY(320);
                }
            }
        }
        else if(containingScreen.getZoneIndex() == 7) {
            // Twin Labyrinths
            if(containingScreen.getRoomIndex() == 0 && containingScreen.getScreenIndex() == 0) {
                if(obj.getX() == 400) {
                    obj.setX(100);
                }
            }
            else if(containingScreen.getRoomIndex() == 2 && containingScreen.getScreenIndex() == 1) {
                if(obj.getX() == 680) {
                    obj.setX(660);
                }
                else if(obj.getX() == 720) {
                    obj.setX(700);
                }
            }
            else if(containingScreen.getRoomIndex() == 3 && containingScreen.getScreenIndex() == 2) {
                if(obj.getX() == 100) {
                    obj.setX(20);
                }
            }
            else if(containingScreen.getRoomIndex() == 8 && containingScreen.getScreenIndex() == 1) {
                if(obj.getX() == 1080) {
                    obj.setX(1040);
                }
            }
            else if(containingScreen.getRoomIndex() == 9 && containingScreen.getScreenIndex() == 1) {
                if(obj.getX() == 1140) {
                    obj.setX(840);
                }
            }
            else if(containingScreen.getRoomIndex() == 11 && containingScreen.getScreenIndex() == 1) {
                if(obj.getX() == 680) {
                    obj.setX(660);
                }
            }
            else if(containingScreen.getRoomIndex() == 12 && containingScreen.getScreenIndex() == 0) {
                if(obj.getX() == 160) {
                    obj.setX(20);
                }
            }
            else if(containingScreen.getRoomIndex() == 12 && containingScreen.getScreenIndex() == 1) {
                if(obj.getX() == 580 && obj.getY() == 800) {
                    obj.setY(640);
                }
                else if(obj.getX() == 20 && obj.getY() == 640) {
                    obj.setY(800);
                }
                else if(obj.getX() == 260) {
                    obj.setX(200);
                }
            }
            else if(containingScreen.getRoomIndex() == 16 && containingScreen.getScreenIndex() == 2) {
                if(obj.getX() == 40) {
                    obj.setX(20);
                }
            }
        }
        else if(containingScreen.getZoneIndex() == 8) {
            // Endless Corridor
            if(containingScreen.getRoomIndex() == 5 && containingScreen.getScreenIndex() == 0) {
                if(obj.getX() == 180) {
                    obj.setX(160);
                }
                else if(obj.getX() == 2120) {
                    obj.setX(2100);
                }
            }
        }
        else if(containingScreen.getZoneIndex() == 9) {
            // Shrine of the Mother
            if(containingScreen.getRoomIndex() == 0 && containingScreen.getScreenIndex() == 0) {
                if(obj.getX() == 120) {
                    obj.setX(100);
                }
            }
        }
        else if(containingScreen.getZoneIndex() == 10) {
            // Gate of Illusion
            if(containingScreen.getRoomIndex() == 7 && containingScreen.getScreenIndex() == 0) {
                if(obj.getX() == 200) {
                    obj.setX(20);
                    obj.setY(320);
                }
            }
        }
        else if(containingScreen.getZoneIndex() == 11) {
            // Graveyard of the Giants
            if(containingScreen.getRoomIndex() == 3 && containingScreen.getScreenIndex() == 0) {
                if(obj.getX() == 20 && obj.getY() == 400) {
                    obj.setY(320);
                }
            }
        }
        else if(containingScreen.getZoneIndex() == 12) {
            // Temple of Moonlight
            if(containingScreen.getRoomIndex() == 0 && containingScreen.getScreenIndex() == 0) {
                if(obj.getX() == 120) {
                    obj.setX(20);
                    obj.setY(400);
                }
            }
            else if(containingScreen.getRoomIndex() == 1 && containingScreen.getScreenIndex() == 0) {
                templeOfMoonlightRemovedPotScreen = containingScreen;
                if(obj.getX() == 540) {
                    templeOfMoonlightRemovedPotPresent = true;
                }
            }
            else if(containingScreen.getRoomIndex() == 3 && containingScreen.getScreenIndex() == 0) {
                if(obj.getX() == 380) {
                    obj.setX(300);
                    obj.setY(160);
                }
            }
            else if(containingScreen.getRoomIndex() == 4 && containingScreen.getScreenIndex() == 2) {
                if(obj.getX() == 100) {
                    obj.setX(80);
                }
            }
        }
        else if(containingScreen.getZoneIndex() == 13) {
            // Tower of the Goddess
            if(containingScreen.getRoomIndex() == 1 && containingScreen.getScreenIndex() == 0) {
                if(obj.getX() == 500) {
                    obj.setX(460);
                }
            }
            else if(containingScreen.getRoomIndex() == 1 && containingScreen.getScreenIndex() == 1) {
                if(obj.getX() == 120) {
                    obj.setX(100);
                }
            }
            else if(containingScreen.getRoomIndex() == 3 && containingScreen.getScreenIndex() == 1) {
                if(obj.getX() == 40) {
                    obj.setX(20);
                }
            }
            else if(containingScreen.getRoomIndex() == 4 && containingScreen.getScreenIndex() == 0) {
                if(obj.getX() == 40) {
                    obj.setX(20);
                }
            }
            else if(containingScreen.getRoomIndex() == 6 && containingScreen.getScreenIndex() == 2) {
                if(obj.getX() == 40) {
                    obj.setX(20);
                }
            }
            else if(containingScreen.getRoomIndex() == 6 && containingScreen.getScreenIndex() == 3) {
                if(obj.getX() == 260) {
//                    obj.getArgs().set(4, (short)0);
                }
            }
            else if(containingScreen.getRoomIndex() == 7 && containingScreen.getScreenIndex() == 2) {
                towerOfTheGoddessRemovedPotScreen = containingScreen;
                if(obj.getX() == 1460) {
                    obj.setX(1440);
                }
                else if(obj.getX() == 1500) {
                    towerOfTheGoddessRemovedPotPresent = true;
                }
            }
        }
        else if(containingScreen.getZoneIndex() == 15) {
            // Chamber of Birth (East)
            if (containingScreen.getRoomIndex() == 0 && containingScreen.getScreenIndex() == 1) {
                if (obj.getX() == 120) {
                    obj.setX(20);
                    obj.setY(880);
                }
            }
        }
        else if(containingScreen.getZoneIndex() == 16) {
            // Chamber of Birth (West)
            if (containingScreen.getRoomIndex() == 0 && containingScreen.getScreenIndex() == 1) {
                if (obj.getX() == 780) {
                    obj.setX(760);
                }
            }
            else if (containingScreen.getRoomIndex() == 4 && containingScreen.getScreenIndex() == 0) {
                if (obj.getX() == 440) {
                    obj.setX(400);
                }
            }
        }
        else if(containingScreen.getZoneIndex() == 17) {
            // Dimensional Corridor
            if (containingScreen.getRoomIndex() == 2 && containingScreen.getScreenIndex() == 1) {
                if (obj.getX() == 720) {
                    obj.setX(700);
                }
            }
            else if (containingScreen.getRoomIndex() == 4 && containingScreen.getScreenIndex() == 0) {
                if (obj.getX() == 380) {
                    obj.setX(400);
                }
            }
            else if (containingScreen.getRoomIndex() == 8 && containingScreen.getScreenIndex() == 0) {
                if (obj.getX() == 60) {
                    obj.setX(40);
                }
            }
            else if (containingScreen.getRoomIndex() == 10 && containingScreen.getScreenIndex() == 1) {
                if (obj.getX() == 60) {
                    obj.setX(40);
                }
            }
        }
        else if(containingScreen.getZoneIndex() == 23) {
            if (containingScreen.getRoomIndex() == 10 && containingScreen.getScreenIndex() == 0) {
                if (obj.getX() == 140) {
                    obj.setX(120);
                }
                else if (obj.getX() == 40) {
                    obj.setX(480);
                }
            }
            else if (containingScreen.getRoomIndex() == 18 && containingScreen.getScreenIndex() == 1) {
                if (obj.getX() == 880) {
                    obj.setX(760);
                }
            }
        }
    }
}
