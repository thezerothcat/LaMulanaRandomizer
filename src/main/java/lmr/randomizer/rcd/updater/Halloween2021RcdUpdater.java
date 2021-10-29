package lmr.randomizer.rcd.updater;

import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.dat.DatFileData;
import lmr.randomizer.randomization.data.CustomBlockEnum;
import lmr.randomizer.rcd.RcdFileData;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.update.AddObject;
import lmr.randomizer.util.*;

import java.util.*;

public class Halloween2021RcdUpdater extends RcdUpdater {
    public Halloween2021RcdUpdater(RcdFileData rcdFileData, DatFileData datFileData) {
        super(rcdFileData, datFileData);
    }

    @Override
    boolean updateCrusher(GameObject crusher) {
        ObjectContainer objectContainer = crusher.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(screen.getZoneIndex() == ZoneConstants.SHRINE_BACK && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 1) {
            if(crusher.getX() == 640) {
                crusher.getTestByteOperations().add(new TestByteOperation(FlagConstants.BEELZEBUB_STATE, ByteOp.FLAG_EQUALS, 0));
                crusher.getWriteByteOperations().get(0).setIndex(3);
                crusher.getWriteByteOperations().get(0).setValue(0);
            }
        }
        return true;
    }

    @Override
    boolean updateGhostSpawner(GameObject ghostSpawner) {
        return !ghostSpawner.getWriteByteOperations().isEmpty();
    }

    @Override
    boolean updateAnkh(GameObject ankh) {
        ObjectContainer objectContainer = ankh.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(screen.getZoneIndex() == 4) {
            // Bahamut to Night Surface
            ankh.getArgs().set(24, (short)22);
            ankh.getArgs().set(28, (short)22);
        }
        if(screen.getZoneIndex() == ZoneConstants.SHRINE_BACK) {
            // Mother Ankh
            for(TestByteOperation testByteOperation : ankh.getTestByteOperations()) {
                if(testByteOperation.getIndex() == FlagConstants.MOTHER_STATE) {
                    testByteOperation.setIndex(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_STATE);
                }
            }
            for(WriteByteOperation writeByteOperation : ankh.getWriteByteOperations()) {
                if(writeByteOperation.getIndex() == FlagConstants.MOTHER_STATE) {
                    writeByteOperation.setIndex(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_STATE);
                }
            }

            for(WriteByteOperation writeByteOperation : ankh.getWriteByteOperations()) {
                if(writeByteOperation.getIndex() == FlagConstants.ESCAPE) {
                    writeByteOperation.setIndex(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_DEFEATED);
                }
            }
        }
        return true;
    }

    @Override
    boolean updateWaterLeaper(GameObject waterLeaper) {
        return false;
    }

    @Override
    boolean updateExplodeRock(GameObject explodeRock) {
        ObjectContainer objectContainer = explodeRock.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(screen.getZoneIndex() == 2 && screen.getRoomIndex() == 8 && screen.getScreenIndex() == 1) {
            // Sakit screen
            return false;
        }
        return true;
    }

    @Override
    boolean updateLavaRock(GameObject lavaRock) {
        return false; // Remove lava rocks in favor of ghosts.
    }

    @Override
    boolean updateHundun(GameObject hundun) {
        return false; // Remove Hundun shrine enemy in favor of ghosts.
    }

    @Override
    boolean updateNinjaSpawner(GameObject ninjaSpawner) {
        Screen screen = (Screen)ninjaSpawner.getObjectContainer();
        if(screen.getZoneIndex() == 15 && screen.getRoomIndex() == 1 && screen.getScreenIndex() == 0) {
            return true;
        }
        return false;
    }

    @Override
    boolean updateChonchonSpawner(GameObject chonchonSpawner) {
        return false; // Ban Medusa heads in favor of ghosts
    }

    @Override
    boolean updateGraphicsTextureDraw(GameObject graphicsTextureDraw) {
        Screen screen = (Screen)graphicsTextureDraw.getObjectContainer();
        if(screen.getZoneIndex() == 0 && screen.getRoomIndex() == 6 && screen.getScreenIndex() == 0) {
            for(TestByteOperation testByteOperation : graphicsTextureDraw.getTestByteOperations()) {
                if(testByteOperation.getIndex() == FlagConstants.HT_UNLOCK_PROGRESS_EARLY) {
                    testByteOperation.setIndex(FlagConstants.CUSTOM_HALLOWEEN2021_MULBRUK_DRACUET);
                    if(testByteOperation.getValue() == 1) {
                        testByteOperation.setOp(ByteOp.FLAG_LT);
                    }
                    else {
                        testByteOperation.setOp(ByteOp.FLAG_EQUALS);
                        testByteOperation.setValue(1);
                    }
                }
            }
        }
        return true;
    }

    @Override
    boolean updateConversationDoor(GameObject conversationDoor) {
        Screen screen = (Screen)conversationDoor.getObjectContainer();
        if(screen.getZoneIndex() == 0 && screen.getRoomIndex() == 6 && screen.getScreenIndex() == 0) {
            conversationDoor.getTestByteOperations().clear();
            conversationDoor.getWriteByteOperations().clear();
            conversationDoor.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_MULBRUK_DRACUET, ByteOp.FLAG_EQUALS, 1),
                    new TestByteOperation(FlagConstants.WF_ANKH_JEWEL_EXTRA, ByteOp.FLAG_EQUALS, 0)
            );
            conversationDoor.getArgs().set(4, (short)BlockConstants.Master_Dracuet_ProvocativeBathingSuit);
        }
        return true;
    }

    @Override
    void addUntrackedCustomPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        addTrapdoors(screen, zoneIndex, roomIndex, screenIndex);
        addPressurePlates(screen, zoneIndex, roomIndex, screenIndex);
        addSpikes(screen, zoneIndex, roomIndex, screenIndex);
        addSkeletons(screen, zoneIndex, roomIndex, screenIndex);
        if(zoneIndex == 1 && roomIndex == 2 && screenIndex == 1) {
            GraphicsTextureDraw shopSign = new GraphicsTextureDraw(screen, 1200, 200);
            shopSign.setLayer(0);
            shopSign.setImageFile(GraphicsTextureDraw.ImageFile_eveg);
            shopSign.setImagePosition(160, 160);
            shopSign.setImageSize(80, 80);
            shopSign.setAnimation(0, 0, 1, 0);
            shopSign.setCollision(HitTile.Air);
            shopSign.setRGBAMax(0, 0, 0, 255);
            shopSign.setArg23(1);
            screen.getObjects().add(shopSign);
        }
    }

    @Override
    void addUntrackedCustomNoPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        AddObject.addFramesTimer(screen, 0, Arrays.asList(
                new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_BAT_RETRIBUTION, ByteOp.FLAG_GTEQ, 1),
                new TestByteOperation(FlagConstants.GUIDANCE_BATS_KILLED_COUNT, ByteOp.FLAG_GT, 0)),
                Arrays.asList(
                        new WriteByteOperation(FlagConstants.GUIDANCE_BATS_KILLED_COUNT, ByteOp.ASSIGN_FLAG, 0),
                        new WriteByteOperation(FlagConstants.SCREEN_FLAG_A, ByteOp.ADD_FLAG, 1)));

        if((zoneIndex == ZoneConstants.SURFACE && roomIndex == 2 && screenIndex == 1) || (zoneIndex == ZoneConstants.NIGHT_SURFACE && roomIndex == 2 && screenIndex == 1)) {
            AddObject.addFramesTimer(screen, 0, Arrays.asList(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_DEFEATED, ByteOp.FLAG_GTEQ, 1),
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_XELPUD_BONUS_CANDY_CONVERSATIONS, ByteOp.FLAG_EQUALS, 0)),
                    Arrays.asList(
                            new WriteByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_XELPUD_BONUS_CANDY_CONVERSATIONS, ByteOp.ASSIGN_FLAG, 1)));
            AddObject.addFramesTimer(screen, 0, Arrays.asList(
                    new TestByteOperation(FlagConstants.WF_COIN_EXTINCTION, ByteOp.FLAG_EQUALS, 2),
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_XELPUD_BONUS_CANDY_CONVERSATIONS, ByteOp.FLAG_EQUALS, 2)),
                    Arrays.asList(
                            new WriteByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_XELPUD_BONUS_CANDY_CONVERSATIONS, ByteOp.ASSIGN_FLAG, 3)));
        }

        if(zoneIndex == 18 && roomIndex == 3) {
            AddObject.addFramesTimer(screen, 0, Arrays.asList(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_DEFEATED, ByteOp.FLAG_GTEQ, 1),
                    new TestByteOperation(FlagConstants.MAIL_44, ByteOp.FLAG_EQUALS, 0)),
                    Arrays.asList(
                            new WriteByteOperation(FlagConstants.MAIL_44, ByteOp.ASSIGN_FLAG, 1),
                            new WriteByteOperation(FlagConstants.MAIL_COUNT, ByteOp.ADD_FLAG, 1)));
        }
        if(zoneIndex == 18 && roomIndex == 7 && screenIndex == 1) {
            AddObject.addFramesTimer(screen, 0, Arrays.asList(
                    new TestByteOperation(FlagConstants.BEELZEBUB_STATE, ByteOp.FLAG_GTEQ, 1),
                    new TestByteOperation(FlagConstants.SCREEN_FLAG_0, ByteOp.FLAG_EQUALS, 0),
                    new TestByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.FLAG_EQUALS, 0)),
                    Arrays.asList(
                            new WriteByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.ASSIGN_FLAG, 1)));
        }
    }

    @Override
    public void doPostShuffleUpdates(){
        int maxCurseLevel = determineMaxCurseLevel();

        for(Zone zone : rcdFileData.getZones()) {
            AddObject.addHalloweenEmailTimers(zone, maxCurseLevel);
        }

        addGhostSpawners(maxCurseLevel);
        updateSpikes();
        updateChests();
        updateDaises();
        updateMovingPlatforms();
        updateMiscSpeed();
        updateEnemies();
        updateMudmanPressurePlate();
        updateForEscapeChest();
        replaceNightSurfaceWithSurface();
        fixTransitionGates();
    }

    private int determineMaxCurseLevel() {
        Set<String> cursedChests = new HashSet<>();
        Chest chest;
        for(GameObject gameObject : rcdFileData.getObjectsById(ObjectIdConstants.Chest)) {
            chest = (Chest)gameObject;
            if(chest.isCursed()) {
                cursedChests.add(gameObject.getObjectContainer().getContainerString()
                        + String.valueOf(chest.getX())
                        + String.valueOf(chest.getY()));
            }
        }
        return cursedChests.size();
    }

    private void addGhostSpawners(int maxCurseLevel) {
        final int maxGhosts = 1;
        final int maxChonchons = 2;
        final int chonchonAmplitude = 0;
        final DropType chonchonDropType = DropType.RANDOM_COINS_WEIGHTS_SOUL;

        final int ghostDamageAndSoul1 = 2;
        final int ghostDamageAndSoul2 = 32;
        final int ghostDamageAndSoul3 = 704;

        final int ghostSpeedAndDropType1 = DropType.COINS.getValue();
        final int ghostSpeedAndDropType2 = DropType.RANDOM_COINS_WEIGHTS_SOUL.getValue();
        final int spawnRate1 = 240;
        final int spawnRate2 = 480;

        final int chonchonSpeed1 = 1;
        final int chonchonSpeed2 = 3;
        final int chonchonSpeed3 = 6;

        int zoneIndex;
        for(Zone zone : rcdFileData.getZones()) {
            zoneIndex = zone.getZoneIndex();
            for(Room room : zone.getRooms()) {
                for(Screen screen : room.getScreens()) {
                    if(zoneIndex == 7) {
                        AddObject.addChonchonSpawner(screen, maxChonchons, ghostDamageAndSoul1, spawnRate1,
                                chonchonSpeed1, chonchonAmplitude, chonchonDropType,
                                new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.GHOST_WITCH_THRESHOLD1),
                                new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.GHOST_THRESHOLD2));

                        AddObject.addChonchonSpawner(screen, maxChonchons, ghostDamageAndSoul1, spawnRate1,
                                chonchonSpeed2, chonchonAmplitude, chonchonDropType,
                                new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.GHOST_THRESHOLD2),
                                new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.GHOST_THRESHOLD3));

                        AddObject.addChonchonSpawner(screen, maxChonchons, ghostDamageAndSoul1, spawnRate1,
                                chonchonSpeed2, chonchonAmplitude, chonchonDropType,
                                new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.GHOST_THRESHOLD3),
                                new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, maxCurseLevel));

                        AddObject.addChonchonSpawner(screen, maxChonchons, ghostDamageAndSoul3, spawnRate1,
                                chonchonSpeed3, chonchonAmplitude, chonchonDropType,
                                new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, maxCurseLevel));
                    }
                    else {
                        AddObject.addGhostSpawner(screen, maxGhosts, ghostDamageAndSoul1, spawnRate1, ghostSpeedAndDropType1,
                                new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.GHOST_WITCH_THRESHOLD1),
                                new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.GHOST_THRESHOLD2));

                        AddObject.addGhostSpawner(screen, maxGhosts, ghostDamageAndSoul1, spawnRate2, ghostSpeedAndDropType2,
                                new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.GHOST_THRESHOLD2),
                                new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.GHOST_THRESHOLD3));

                        AddObject.addGhostSpawner(screen, maxGhosts, ghostDamageAndSoul2, spawnRate2, ghostSpeedAndDropType2,
                                new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.GHOST_THRESHOLD3),
                                new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, maxCurseLevel));

                        AddObject.addGhostSpawner(screen, maxGhosts, ghostDamageAndSoul3, spawnRate2, ghostSpeedAndDropType2,
                                new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, maxCurseLevel));
                    }
                }
            }
        }
    }

    private void updateMiscSpeed() {
        Screen screen;
        for(GameObject existingExtendableSpikes : rcdFileData.getObjectsById(ObjectIdConstants.ExtendableSpikes)) {
            screen = (Screen)existingExtendableSpikes.getObjectContainer();
            if(screen.getZoneIndex() == ZoneConstants.MOONLIGHT && screen.getRoomIndex() == 7 && screen.getScreenIndex() == 0) {
                ExtendableSpikes cursedExtendableSpikes = new ExtendableSpikes(existingExtendableSpikes);
                cursedExtendableSpikes.setActivationDelay(5);
                cursedExtendableSpikes.setUpdate1Delay(5);
                cursedExtendableSpikes.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingExtendableSpikes.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingExtendableSpikes.getObjectContainer().getObjects().add(cursedExtendableSpikes);
            }
        }
        for(GameObject existingHotSpring : rcdFileData.getObjectsById(ObjectIdConstants.HotSpring)) {
            HotSpring cursedHotSpring = new HotSpring(existingHotSpring);
            cursedHotSpring.setHealAmount(8 * existingHotSpring.getArgs().get(3));
            cursedHotSpring.setFramesOfNoHeal(ValueConstants.HALLOWEEN2021_HOT_SPRING_DELAY);
            cursedHotSpring.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
            existingHotSpring.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
            existingHotSpring.getObjectContainer().getObjects().add(cursedHotSpring);
        }
        for(GameObject existingSphinx : rcdFileData.getObjectsById(ObjectIdConstants.Sphinx)) {
            GameObject cursedSphinx = new GameObject(existingSphinx);
            cursedSphinx.getArgs().set(1, (short)(existingSphinx.getArgs().get(1) * 2));
            cursedSphinx.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
            existingSphinx.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
            existingSphinx.getObjectContainer().getObjects().add(cursedSphinx);
        }
    }

    private void addSpikes(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex == 0 && roomIndex == 1 && screenIndex == 1) {
            AddObject.addSpikes(screen, 1040, 240, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 0 && roomIndex == 9 && screenIndex == 1) {
            AddObject.addSpikes(screen, 700, 80, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 1 && roomIndex == 1 && screenIndex == 1) {
            AddObject.addSpikes(screen, 20, 780, 2, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 1 && roomIndex == 10 && screenIndex == 3) {
            AddObject.addSpikes(screen, 880, 640, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 2 && roomIndex == 5 && screenIndex == 0) {
            AddObject.addSpikes(screen, 160, 220, 2, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 2 && roomIndex == 7 && screenIndex == 0) {
            AddObject.addSpikes(screen, 180, 320, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 2 && roomIndex == 7 && screenIndex == 2) {
            AddObject.addSpikes(screen, 1420, 160, 2, 2, Spikes.FACE_LEFT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 1460, 160, 2, 2, Spikes.FACE_RIGHT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 3 && roomIndex == 1 && screenIndex == 0) {
            AddObject.addSpikes(screen, 160, 240, 2, 2, Spikes.FACE_UP,
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD),
                    new TestByteOperation(FlagConstants.SUN_WATCHTOWER_LIGHTS, ByteOp.FLAG_EQUALS, ValueConstants.SUN_WATCHTOWER_PLATFORMS_SPAWNED));
        }
        if(zoneIndex == 3 && roomIndex == 3 && screenIndex == 0) {
            AddObject.addSpikes(screen, 400, 80, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 4 && roomIndex == 1 && screenIndex == 0) {
            AddObject.addSpikes(screen, 240, 320, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 360, 320, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 520, 240, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 5 && roomIndex == 0 && screenIndex == 0) {
            AddObject.addSpikes(screen, 160, 80, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 440, 80, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 5 && roomIndex == 0 && screenIndex == 1) {
            AddObject.addSpikes(screen, 840, 40, 10, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD)); // todo: this needs width 14 at x = 800
        }
        if(zoneIndex == 6 && roomIndex == 2 && screenIndex == 1) {
            AddObject.addSpikes(screen, 1040, 320, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 1040, 160, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 6 && roomIndex == 6 && screenIndex == 1) {
            AddObject.addSpikes(screen, 500, 600, 2, 8, Spikes.FACE_LEFT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 6 && roomIndex == 7 && screenIndex == 0) {
            AddObject.addSpikes(screen, 260, 320, 6, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 7 && roomIndex == 0 && screenIndex == 1) {
            AddObject.addSpikes(screen, 720, 40, 2, 5, Spikes.FACE_RIGHT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 7 && roomIndex == 8 && screenIndex == 0) {
            AddObject.addSpikes(screen, 60, 0, 2, 7, Spikes.FACE_RIGHT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 7 && roomIndex == 8 && screenIndex == 1) {
            AddObject.addSpikes(screen, 840, 240, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 7 && roomIndex == 15 && screenIndex == 0) {
            AddObject.addSpikes(screen, 240, 80, 2, 2, Spikes.FACE_UP,
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD),
                    new TestByteOperation(FlagConstants.WF_FEATHER, ByteOp.FLAG_GTEQ, ValueConstants.ITEM_COLLECTED));
        }
        if(zoneIndex == 8 && roomIndex == 3 && screenIndex == 3) {
            AddObject.addSpikes(screen, 2420, 40, 2, 5, Spikes.FACE_LEFT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 8 && roomIndex == 5 && screenIndex == 2) {
            AddObject.addSpikes(screen, 1360, 80, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 1480, 80, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 1600, 80, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 1720, 80, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 9 && roomIndex == 0 && screenIndex == 1) {
            AddObject.addSpikes(screen, 760, 140, 2, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 9 && roomIndex == 2 && screenIndex == 0) {
            AddObject.addSpikes(screen, 540, 140, 2, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 9 && roomIndex == 4 && screenIndex == 0) {
            AddObject.addSpikes(screen, 100, 240, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 9 && roomIndex == 6 && screenIndex == 0) {
            AddObject.addSpikes(screen, 60, 240, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 10 && roomIndex == 8 && screenIndex == 0) {
            AddObject.addSpikes(screen, 360, 380, 2, 5, Spikes.FACE_LEFT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 10 && roomIndex == 9 && screenIndex == 1) {
            AddObject.addSpikes(screen, 840, 360, 2, 4, Spikes.FACE_RIGHT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 11 && roomIndex == 0 && screenIndex == 1) {
            AddObject.addSpikes(screen, 1160, 40, 2, 5, Spikes.FACE_LEFT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 11 && roomIndex == 1 && screenIndex == 0) {
            AddObject.addSpikes(screen, 420, 140, 2, 3, Spikes.FACE_RIGHT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 340, 300, 2, 3, Spikes.FACE_RIGHT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 11 && roomIndex == 2 && screenIndex == 1) {
            AddObject.addSpikes(screen, 1120, 300, 2, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 11 && roomIndex == 3 && screenIndex == 0) {
            AddObject.addSpikes(screen, 240, 40, 2, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 11 && roomIndex == 8 && screenIndex == 0) {
            AddObject.addSpikes(screen, 40, 140, 2, 2, Spikes.FACE_RIGHT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 11 && roomIndex == 9 && screenIndex == 0) {
            AddObject.addSpikes(screen, 520, 280, 2, 4, Spikes.FACE_LEFT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 11 && roomIndex == 9 && screenIndex == 1) {
            AddObject.addSpikes(screen, 920, 240, 2, 4, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 12 && roomIndex == 0 && screenIndex == 0) {
            AddObject.addSpikes(screen, 120, 40, 2, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 460, 40, 2, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 12 && roomIndex == 1 && screenIndex == 0) {
            AddObject.addSpikes(screen, 580, 40, 2, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 13 && roomIndex == 2 && screenIndex == 2) {
            AddObject.addSpikes(screen, 60, 1280, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 13 && roomIndex == 4 && screenIndex == 1) {
            AddObject.addSpikes(screen, 20, 520, 2, 2, Spikes.FACE_RIGHT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 13 && roomIndex == 5 && screenIndex == 1) {
            AddObject.addSpikes(screen, 220, 800, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 13 && roomIndex == 7 && screenIndex == 1) {
            AddObject.addSpikes(screen, 980, 400, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD))
                    .setDamage(true, 1);
        }
        if(zoneIndex == 14 && roomIndex == 4 && screenIndex == 0) {
            AddObject.addSpikes(screen, 240, 220, 2, 3, Spikes.FACE_LEFT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 360, 220, 2, 3, Spikes.FACE_RIGHT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 14 && roomIndex == 4 && screenIndex == 1) {
            AddObject.addSpikes(screen, 1040, 280, 2, 2, Spikes.FACE_RIGHT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 15 && roomIndex == 1 && screenIndex == 0) {
            AddObject.addSpikes(screen, 320, 160, 4, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 480, 80, 4, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 15 && roomIndex == 1 && screenIndex == 1) {
            AddObject.addSpikes(screen, 800, 140, 2, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 880, 240, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 16 && roomIndex == 1 && screenIndex == 1) {
            AddObject.addSpikes(screen, 1120, 140, 2, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 16 && roomIndex == 2 && screenIndex == 0) {
            AddObject.addSpikes(screen, 260, 160, 6, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 16 && roomIndex == 4 && screenIndex == 0) {
            AddObject.addSpikes(screen, 280, 400, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 17 && roomIndex == 0 && screenIndex == 1) {
            AddObject.addSpikes(screen, 100, 600, 2, 8, Spikes.FACE_RIGHT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 500, 600, 2, 8, Spikes.FACE_LEFT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 17 && roomIndex == 1 && screenIndex == 0) {
            AddObject.addSpikes(screen, 120, 160, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 17 && roomIndex == 1 && screenIndex == 1) {
            AddObject.addSpikes(screen, 200, 720, 4, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 18 && roomIndex == 0 && screenIndex == 1) {
            AddObject.addSpikes(screen, 760, 140, 2, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 18 && roomIndex == 3 && screenIndex == 0) {
//            AddObject.addSpikes(screen, 300, 40, 2, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD), new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_STATE, ByteOp.FLAG_GTEQ, ValueConstants.MOTHER_FIGHT_STARTED));
            AddObject.addSpikes(screen, 140, 40, 2, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD), new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_STATE, ByteOp.FLAG_GTEQ, ValueConstants.MOTHER_FIGHT_STARTED));
            AddObject.addSpikes(screen, 460, 40, 2, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD), new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_STATE, ByteOp.FLAG_GTEQ, ValueConstants.MOTHER_FIGHT_STARTED));
//            AddObject.addSpikes(screen, 300, 400, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD), new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_STATE, ByteOp.FLAG_GTEQ, ValueConstants.MOTHER_FIGHT_STARTED)).setDirections(true, true, false, true);
            AddObject.addSpikes(screen, 140, 400, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD), new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_STATE, ByteOp.FLAG_GTEQ, ValueConstants.MOTHER_FIGHT_STARTED)).setDirections(true, true, false, true);
            AddObject.addSpikes(screen, 460, 400, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD), new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_STATE, ByteOp.FLAG_GTEQ, ValueConstants.MOTHER_FIGHT_STARTED)).setDirections(true, true, false, true);
            AddObject.addSpikes(screen, 20, 220, 2, 2, Spikes.FACE_RIGHT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD), new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_STATE, ByteOp.FLAG_GTEQ, ValueConstants.MOTHER_FIGHT_STARTED));
            AddObject.addSpikes(screen, 580, 220, 2, 2, Spikes.FACE_LEFT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD), new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_STATE, ByteOp.FLAG_GTEQ, ValueConstants.MOTHER_FIGHT_STARTED));
        }
        if(zoneIndex == 18 && roomIndex == 4 && screenIndex == 0) {
            AddObject.addSpikes(screen, 100, 240, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 19 && roomIndex == 1 && screenIndex == 0) {
            AddObject.addSpikes(screen, 180, 320, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 19 && roomIndex == 1 && screenIndex == 2) {
            AddObject.addSpikes(screen, 1420, 160, 2, 2, Spikes.FACE_LEFT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 1460, 160, 2, 2, Spikes.FACE_RIGHT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 19 && roomIndex == 3 && screenIndex == 0) {
            AddObject.addSpikes(screen, 280, 240, 4, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 19 && roomIndex == 4 && screenIndex == 0) {
            AddObject.addSpikes(screen, 360, 140, 2, 3, Spikes.FACE_RIGHT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 460, 40, 2, 4, Spikes.FACE_RIGHT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 19 && roomIndex == 4 && screenIndex == 1) {
            AddObject.addSpikes(screen, 840, 220, 2, 3, Spikes.FACE_RIGHT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 20 && roomIndex == 2 && screenIndex == 0) {
            AddObject.addSpikes(screen, 220, 80, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 300, 40, 2, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 380, 90, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));

            AddObject.addSpikes(screen, 100, 300, 2, 3, Spikes.FACE_LEFT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 500, 300, 2, 3, Spikes.FACE_RIGHT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));

            AddObject.addSpikes(screen, 240, 200, 2, 2, Spikes.FACE_LEFT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 300, 160, 2, 2, Spikes.FACE_UP, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 360, 200, 2, 2, Spikes.FACE_RIGHT, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
            AddObject.addSpikes(screen, 300, 240, 2, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
        if(zoneIndex == 21 && roomIndex == 0 && screenIndex == 1) {
            AddObject.addSpikes(screen, 1160, 220, 4, 2, Spikes.FACE_DOWN, new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.SPIKES_THRESHOLD));
        }
    }

    private void addSkeletons(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex == ZoneConstants.BIRTH_SWORDS && roomIndex == 4 && screenIndex == 0) {
            AddObject.addSkeleton(screen, 60, 420).addTests(
                    new TestByteOperation(0x2ad, ByteOp.FLAG_EQUALS, 0),
                    new TestByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.FLAG_EQUALS, 1));
            AddObject.addSkeleton(screen, 140, 420).addTests(
                    new TestByteOperation(0x2ad, ByteOp.FLAG_EQUALS, 0),
                    new TestByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.FLAG_EQUALS, 1));
            AddObject.addSkeleton(screen, 220, 420).addTests(
                    new TestByteOperation(0x2ad, ByteOp.FLAG_EQUALS, 0),
                    new TestByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.FLAG_EQUALS, 1));
            AddObject.addSkeleton(screen, 300, 420).addTests(
                    new TestByteOperation(0x2ad, ByteOp.FLAG_EQUALS, 0),
                    new TestByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.FLAG_EQUALS, 1));
        }
    }

    private void addPressurePlates(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        final int explosionPercentDamage = 20;

        if(zoneIndex == 0 && roomIndex == 7 && screenIndex == 1) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 680, 440);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_10, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_10, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_10, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 640, 0, explosionPercentDamage, FlagConstants.SCREEN_FLAG_10);
        }
        if(zoneIndex == 1 && roomIndex == 1 && screenIndex == 1) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 280, 840);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_10, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_10, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_10, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 0, 480, explosionPercentDamage, FlagConstants.SCREEN_FLAG_10);
        }
        if(zoneIndex == 2 && roomIndex == 1 && screenIndex == 1) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 840, 120);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_10, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_10, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_10, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 640, 0, explosionPercentDamage, FlagConstants.SCREEN_FLAG_10);
        }
        if(zoneIndex == 2 && roomIndex == 5 && screenIndex == 0) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 200, 360);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 0, 0, explosionPercentDamage, FlagConstants.SCREEN_FLAG_9);
        }
        if(zoneIndex == 3 && roomIndex == 4 && screenIndex == 0) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 600, 360);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_12, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_12, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_12, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 0, 0, explosionPercentDamage, FlagConstants.SCREEN_FLAG_12);
        }
        if(zoneIndex == 4 && roomIndex == 8 && screenIndex == 0) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 160, 120);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_10, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_10, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_10, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 0, 0, explosionPercentDamage, FlagConstants.SCREEN_FLAG_10);
        }
        if(zoneIndex == 5 && roomIndex == 6 && screenIndex == 0) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 240, 120);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 0, 0, explosionPercentDamage, FlagConstants.SCREEN_FLAG_9);
        }
        if(zoneIndex == 6 && roomIndex == 4 && screenIndex == 1) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 1200, 280);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 640, 0, explosionPercentDamage, FlagConstants.SCREEN_FLAG_9);
        }
        if(zoneIndex == 7 && roomIndex == 9 && screenIndex == 1) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 1040, 200);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 640, 0, explosionPercentDamage, FlagConstants.SCREEN_FLAG_9);
        }
        if(zoneIndex == 7 && roomIndex == 10 && screenIndex == 0) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 400, 280);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 0, 0, explosionPercentDamage, FlagConstants.SCREEN_FLAG_9);
        }
        if(zoneIndex == 8 && roomIndex == 2 && screenIndex == 2) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 1780, 360);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 1280, 0, explosionPercentDamage, FlagConstants.SCREEN_FLAG_9);
        }
        if(zoneIndex == 9 && roomIndex == 4 && screenIndex == 1) {
//            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 1020, 120);
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 680, 120);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 640, 0, explosionPercentDamage, FlagConstants.SCREEN_FLAG_9);
        }
        if(zoneIndex == 10 && roomIndex == 6 && screenIndex == 0) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 300, 440);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_11, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_11, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_11, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 0, 0, explosionPercentDamage, FlagConstants.SCREEN_FLAG_11);
        }
        if(zoneIndex == 11 && roomIndex == 4 && screenIndex == 3) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 80, 1640);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_12, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_12, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_12, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 0, 1440, explosionPercentDamage, FlagConstants.SCREEN_FLAG_12);
        }
        if(zoneIndex == 12 && roomIndex == 4 && screenIndex == 3) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 300, 1880);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_C, ByteOp.ASSIGN_FLAG, 1));
        }
        if(zoneIndex == 13 && roomIndex == 0 && screenIndex == 1) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 260, 760);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 0, 480, explosionPercentDamage, FlagConstants.SCREEN_FLAG_9);
        }
        if(zoneIndex == 14 && roomIndex == 6 && screenIndex == 0) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 220, 440);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 0, 0, explosionPercentDamage, FlagConstants.SCREEN_FLAG_9);
        }
        if(zoneIndex == 16 && roomIndex == 0 && screenIndex == 0) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 260, 440);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 0, 0, explosionPercentDamage, FlagConstants.SCREEN_FLAG_9);
        }
        if(zoneIndex == 17 && roomIndex == 5 && screenIndex == 1) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 180, 760);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 0, 480, explosionPercentDamage, FlagConstants.SCREEN_FLAG_9);
        }
        if(zoneIndex == 18 && roomIndex == 7 && screenIndex == 1) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 1200, 440);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 640, 0, explosionPercentDamage, FlagConstants.SCREEN_FLAG_9);
        }
        if(zoneIndex == 19 && roomIndex == 2 && screenIndex == 1) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 700, 120);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 640, 0, explosionPercentDamage, FlagConstants.SCREEN_FLAG_9);
        }
        if(zoneIndex == 20 && roomIndex == 4 && screenIndex == 1) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 760, 200);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_9, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 640, 0, explosionPercentDamage, FlagConstants.SCREEN_FLAG_9);
        }
        if(zoneIndex == 21 && roomIndex == 0 && screenIndex == 1) {
            PressurePlate pressurePlate = AddObject.addPressurePlate(screen, 1140, 360);
            pressurePlate.addTests(
                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.PRESSURE_PLATE_THRESHOLD));
            pressurePlate.addUpdates(
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_10, ByteOp.ASSIGN_FLAG, 0),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_10, ByteOp.ASSIGN_FLAG, 1),
                    new WriteByteOperation(FlagConstants.SCREEN_FLAG_10, ByteOp.ASSIGN_FLAG, 0));
            AddObject.addFullscreenExplosion(screen, 640, 0, explosionPercentDamage, FlagConstants.SCREEN_FLAG_10);
        }
    }

    private void addTrapdoors(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex == 0 && roomIndex == 1 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 380, 280).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
            AddObject.addTrapdoor(screen, 460, 280).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 1 && roomIndex == 2 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 420, 200).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 1 && roomIndex == 2 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 880, 200).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 1 && roomIndex == 5 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 320, 280).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 2 && roomIndex == 6 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 200, 360).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 2 && roomIndex == 8 && screenIndex == 2) {
            AddObject.addTrapdoor(screen, 1780, 200).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 3 && roomIndex == 4 && screenIndex == 2) {
            AddObject.addTrapdoor(screen, 1280, 360).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 4 && roomIndex == 3 && screenIndex == 2) {
            AddObject.addTrapdoor(screen, 180, 1080).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 4 && roomIndex == 3 && screenIndex == 4) {
            AddObject.addTrapdoor(screen, 140, 2120).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 5 && roomIndex == 1 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 140, 360).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 5 && roomIndex == 1 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 340, 600).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 5 && roomIndex == 5 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 460, 280).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 6 && roomIndex == 2 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 800, 200).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 6 && roomIndex == 3 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 960, 280).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 6 && roomIndex == 6 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 40, 200).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 6 && roomIndex == 7 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 900, 120).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 7 && roomIndex == 3 && screenIndex == 2) {
            AddObject.addTrapdoor(screen, 160, 1160).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 7 && roomIndex == 5 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 340, 600).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 7 && roomIndex == 15 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 1080, 120).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 8 && roomIndex == 2 && screenIndex == 2) {
            AddObject.addTrapdoor(screen, 1580, 200).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD)); // todo: retest
        }
        if(zoneIndex == 8 && roomIndex == 3 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 80, 120).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 9 && roomIndex == 0 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 140, 200).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 9 && roomIndex == 4 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 160, 360).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 9 && roomIndex == 6 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 160, 120).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 10 && roomIndex == 3 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 520, 120).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 10 && roomIndex == 7 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 960, 360).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 10 && roomIndex == 8 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 200, 600).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
            AddObject.addTrapdoor(screen, 460, 680).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
            AddObject.addTrapdoor(screen, 460, 840).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 11 && roomIndex == 4 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 340, 120).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 11 && roomIndex == 4 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 140, 680).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 11 && roomIndex == 5 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 580, 200).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 11 && roomIndex == 5 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 1220, 200).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 12 && roomIndex == 1 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 340, 760).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 12 && roomIndex == 2 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 220, 280).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 13 && roomIndex == 2 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 340, 120).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 13 && roomIndex == 7 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 980, 280).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
            AddObject.addTrapdoor(screen, 980, 360).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 14 && roomIndex == 0 && screenIndex == 0) {
            Trapdoor trapdoor = AddObject.addTrapdoor(screen, 100, 200);
            trapdoor.setImageX(160);
            trapdoor.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 14 && roomIndex == 0 && screenIndex == 1) {
            Trapdoor trapdoor = AddObject.addTrapdoor(screen, 1200, 200);
            trapdoor.setImageX(160);
            trapdoor.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
            trapdoor = AddObject.addTrapdoor(screen, 1200, 280);
            trapdoor.setImageX(160);
            trapdoor.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 14 && roomIndex == 5 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 1080, 200).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
//        if(zoneIndex == 15 && roomIndex == 1 && screenIndex == 0) {
//            AddObject.addTrapdoor(screen, 340, 200).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
//        }
        if(zoneIndex == 15 && roomIndex == 3 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 520, 280).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 16 && roomIndex == 1 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 660, 360).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 16 && roomIndex == 2 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 720, 360).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 17 && roomIndex == 4 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 400, 680).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 18 && roomIndex == 5 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 40, 280).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 19 && roomIndex == 3 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 420, 360).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 20 && roomIndex == 0 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 100, 200).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
            AddObject.addTrapdoor(screen, 240, 280).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 20 && roomIndex == 2 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 980, 360).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 20 && roomIndex == 4 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 260, 360).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 20 && roomIndex == 4 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 1000, 120).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 21 && roomIndex == 0 && screenIndex == 0) {
            AddObject.addTrapdoor(screen, 380, 200).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
        if(zoneIndex == 21 && roomIndex == 0 && screenIndex == 1) {
            AddObject.addTrapdoor(screen, 640, 280).addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TRAPDOOR_THRESHOLD));
        }
    }

    private void updateSpikes() {
        Screen screen;
        for(GameObject spikes : rcdFileData.getObjectsById(ObjectIdConstants.Spikes)) {
            if(Spikes.isFacingUp(spikes)) {
                screen = (Screen)spikes.getObjectContainer();
                if(!(screen.getZoneIndex() == 18 && screen.getRoomIndex() == 1 && screen.getScreenIndex() == 0)) {
                    if((!(screen.getZoneIndex() == 8 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 2)
                            && !(screen.getZoneIndex() == 8 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 3))
                            || spikes.getY() != 80) {
                        Spikes nonWalkableSpikes = new Spikes(spikes);
                        spikes.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.NON_WALKABLE_SPIKE_THRESHOLD));
                        nonWalkableSpikes.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.NON_WALKABLE_SPIKE_THRESHOLD));
                        nonWalkableSpikes.setDirections(true, true, true, true);
                        spikes.getObjectContainer().getObjects().add(nonWalkableSpikes);
                    }
                }
            }
            else if(Spikes.isFacingDown(spikes)) {
                Spikes nonWalkableSpikes = new Spikes(spikes);
                spikes.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.NON_WALKABLE_SPIKE_THRESHOLD));
                nonWalkableSpikes.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.NON_WALKABLE_SPIKE_THRESHOLD));
                nonWalkableSpikes.setDirections(true, true, true, true);
                spikes.getObjectContainer().getObjects().add(nonWalkableSpikes);
            }
            else if(Spikes.isFacingLeft(spikes)) {
                Spikes nonWalkableSpikes = new Spikes(spikes);
                spikes.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.NON_WALKABLE_SPIKE_THRESHOLD));
                nonWalkableSpikes.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.NON_WALKABLE_SPIKE_THRESHOLD));
                nonWalkableSpikes.setDirections(true, true, true, true);
                spikes.getObjectContainer().getObjects().add(nonWalkableSpikes);
            }
            else if(Spikes.isFacingRight(spikes)) {
                Spikes nonWalkableSpikes = new Spikes(spikes);
                spikes.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.NON_WALKABLE_SPIKE_THRESHOLD));
                nonWalkableSpikes.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.NON_WALKABLE_SPIKE_THRESHOLD));
                nonWalkableSpikes.setDirections(true, true, true, true);
                spikes.getObjectContainer().getObjects().add(nonWalkableSpikes);
            }
        }
    }

    private void updateChests() {
        List<String> updatedMapScreens = new ArrayList<>();
        String screenString;
        for(GameObject chest : rcdFileData.getObjectsById(ObjectIdConstants.Chest)) {
            if(chest.getArgs().get(0) == DropType.NOTHING.getValue()) {
                continue;
            }

            screenString = chest.getObjectContainer().getContainerString();
            if(chest.getArgs().get(0) == DropType.COINS.getValue()) {
                Chest povertyChest = new Chest(chest);
                chest.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.POVERTY_CHEST_THRESHOLD));
                povertyChest.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.POVERTY_CHEST_THRESHOLD));
                povertyChest.setDropQuantity(povertyChest.getDropQuantity() / 5);
                chest.getObjectContainer().getObjects().add(povertyChest);
                FileUtils.logFlush("Adding poverty chest to screen " + screenString);
            }
            if(chest.getArgs().get(0) == ItemConstants.MAP + 11) {
                AddObject.addMapCountTimer(chest.getObjectContainer(), chest.getWriteByteOperations().get(0).getIndex());
                if(!updatedMapScreens.contains(screenString)) {
                    AddObject.addEscapeTimer((Screen)chest.getObjectContainer(), FlagConstants.ACHIEVEMENT_MAP_COUNT, 17);
                }
                updatedMapScreens.add(screenString);
            }
        }
    }

    private void updateDaises() {
        final int edenDaisSlowTime = 500;
        final int edenDaisFastTime = 10;
        final int womanStatueDaisTime = 700;
        final int backbeardDaisTime = 500;
        final int chainWhipCrusherDaisSlowTime = 500;
        final int chainWhipCrusherDaisFastTime = 10;

        Screen screen;
        for(GameObject existingDais : rcdFileData.getObjectsById(ObjectIdConstants.Dais)) {
            screen = (Screen)existingDais.getObjectContainer();
            if(screen.getZoneIndex() == ZoneConstants.MOONLIGHT && screen.getRoomIndex() == 2 && screen.getScreenIndex() == 0) {
                if(existingDais.getX() == 160 && existingDais.getY() == 180) {
                    // Eden chest 1, top left dais
                    Dais cursedDais = new Dais(existingDais);
                    cursedDais.setFallingTime(edenDaisSlowTime);
                    cursedDais.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                    existingDais.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                    screen.getObjects().add(cursedDais);
                }
                else if(existingDais.getX() == 320 && existingDais.getY() == 180) {
                    // Eden chest 2, top right dais
                    Dais cursedDais = new Dais(existingDais);
                    cursedDais.setFallingTime(edenDaisSlowTime);
                    cursedDais.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                    existingDais.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                    screen.getObjects().add(cursedDais);
                }
                else if(existingDais.getX() == 320 && existingDais.getY() == 340) {
                    // Eden chest 3, bottom dais
                    Dais cursedDais = new Dais(existingDais);
                    cursedDais.setFallingTime(edenDaisFastTime);
                    cursedDais.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                    existingDais.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                    screen.getObjects().add(cursedDais);
                }
                else if(existingDais.getX() == 320 && existingDais.getY() == 260) {
                    // Eden chest 4, middle dais
                }
            }

            if(screen.getZoneIndex() == ZoneConstants.INFERNO && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 0) {
                if(existingDais.getX() == 260 && existingDais.getY() == 420) {
                    Dais cursedDais = new Dais(existingDais);
                    cursedDais.setFallingTime(chainWhipCrusherDaisFastTime);
                    cursedDais.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                    existingDais.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                    screen.getObjects().add(cursedDais);
                }
                else if(existingDais.getX() == 340 && existingDais.getY() == 420) {
                    Dais cursedDais = new Dais(existingDais);
                    cursedDais.setFallingTime(chainWhipCrusherDaisSlowTime);
                    cursedDais.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                    existingDais.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                    screen.getObjects().add(cursedDais);
                }
            }

            if(screen.getZoneIndex() == ZoneConstants.BIRTH_SWORDS && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
                if(existingDais.getX() == 420 && existingDais.getY() == 420) {
                    // Woman Statue puzzle, skeleton dais
                    Dais cursedDais = new Dais(existingDais);
                    cursedDais.setFallingTime(womanStatueDaisTime);
                    cursedDais.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                    existingDais.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                    screen.getObjects().add(cursedDais);
                }
            }

            if(screen.getZoneIndex() == ZoneConstants.ENDLESS && screen.getRoomIndex() == 5 && screen.getScreenIndex() == 0) {
                if(existingDais.getX() == 100 && existingDais.getY() == 260) {
                    // Backbeard vulnerability dais
                    Dais cursedDais = new Dais(existingDais);
                    cursedDais.setFallingTime(backbeardDaisTime);
                    cursedDais.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                    existingDais.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                    screen.getObjects().add(cursedDais);
                }
            }
        }
    }

    private void updateMovingPlatforms() {
        final int guidancePlatformSpeed = 400;
        final int mausoleumPlatformSpeed = 500;
        final int sunColonnadePlatformSpeed = 300;
        final int springLeftPlatformSpeed = 650;
        final int springLowerPlatformSpeed = 800;
        final int springWaterfallPlatformSpeed = 500; // the one 4-3-0 to 4-3-2 might do with a bit of slowing
        final int extinctionGrailPlatformSpeed = 500;

        final int twinFrontGrailPlatformSpeed = 500;
        final int twinFrontRightOfGrailPlatformSlowSpeed = 300;
        final int twinFrontRightOfGrailPlatformFastSpeed = 500;
        final int twinBraceletShopPlatformSpeed = 800;
        final int twinPerytonPlatformSpeed = 450;
        final int twinOtherPlatformSpeed = 300;
        final int endless4FPlatformSpeed = 500;
        final int shrinePlatformSpeed = 500;

        final int illusionMiddlePlatformSpeed = 100; // from 190
        final int illusionMoveShopPlatformSpeed = 500;
        final int illusionSlushfundPlatformSpeed = 800;
        final int illusionChiYouPlatformSpeed = 400;
        final int illusionAboveGrailPlatformSpeed = 500;
        final int graveyardPlatformSpeed = 500;
        final int moonlightPlatformSpeed = 500;
        final int moonlightPyramidPlatformSpeed = 100; // from 180
        final int skandaBlockingPlatformSpeed = 300;
        final int birthGrailCogPlatformSpeed = 100;
        final int dimensionalPlatformSpeed = 500;
        final int retroMausoleumPlatformSpeed = 500;
        final int retroGuidancePlatformSpeed = 500;

        Screen screen;
        for(GameObject existingMovingPlatform : rcdFileData.getObjectsById(ObjectIdConstants.MovingPlatform)) {
            screen = (Screen)existingMovingPlatform.getObjectContainer();
            if(screen.getZoneIndex() == ZoneConstants.GUIDANCE) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(guidancePlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }

            if(screen.getZoneIndex() == ZoneConstants.MAUSOLEUM && screen.getRoomIndex() == 5) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(mausoleumPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }

            if(screen.getZoneIndex() == ZoneConstants.SUN && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 0) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(sunColonnadePlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }

            if(screen.getZoneIndex() == ZoneConstants.SPRING && screen.getRoomIndex() == 0) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(springLeftPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }
            else if(screen.getZoneIndex() == ZoneConstants.SPRING && screen.getRoomIndex() == 3) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(springWaterfallPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }
            else if(screen.getZoneIndex() == ZoneConstants.SPRING && screen.getRoomIndex() == 7) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(springLowerPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }

            if(screen.getZoneIndex() == ZoneConstants.EXTINCTION && screen.getRoomIndex() == 6) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(extinctionGrailPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }

            if(screen.getZoneIndex() == ZoneConstants.TWIN_FRONT && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 1) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(twinFrontGrailPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }
            else if(screen.getZoneIndex() == ZoneConstants.TWIN_FRONT && screen.getRoomIndex() == 9 && screen.getScreenIndex() == 0) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                if(existingMovingPlatform.getX() == 100 && existingMovingPlatform.getY() == 220) {
                    cursedMovingPlatform.setPlatformSpeed(twinFrontRightOfGrailPlatformSlowSpeed);
                }
                else {
                    cursedMovingPlatform.setPlatformSpeed(twinFrontRightOfGrailPlatformFastSpeed);
                }
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }
            else if(screen.getZoneIndex() == ZoneConstants.TWIN_FRONT && screen.getRoomIndex() == 14 && screen.getScreenIndex() == 0) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(twinBraceletShopPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }
            else if(screen.getZoneIndex() == ZoneConstants.TWIN_FRONT && screen.getRoomIndex() == 16) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(twinPerytonPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }
            else if(screen.getZoneIndex() == ZoneConstants.TWIN_FRONT) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(twinOtherPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }

            if(screen.getZoneIndex() == ZoneConstants.ENDLESS && screen.getRoomIndex() == 4) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(endless4FPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }

            if(screen.getZoneIndex() == ZoneConstants.SHRINE_FRONT || screen.getZoneIndex() == ZoneConstants.SHRINE_BACK) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(shrinePlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }

            if(screen.getZoneIndex() == ZoneConstants.ILLUSION && screen.getRoomIndex() == 2) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(illusionChiYouPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }
            else if(screen.getZoneIndex() == ZoneConstants.ILLUSION && screen.getRoomIndex() == 3) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(illusionAboveGrailPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }
            else if(screen.getZoneIndex() == ZoneConstants.ILLUSION && screen.getRoomIndex() == 4) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(illusionMiddlePlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }
            else if(screen.getZoneIndex() == ZoneConstants.ILLUSION && screen.getRoomIndex() == 5 && screen.getScreenIndex() == 0) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(illusionMoveShopPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }
            else if(screen.getZoneIndex() == ZoneConstants.ILLUSION && screen.getRoomIndex() == 8) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(illusionSlushfundPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }

            if(screen.getZoneIndex() == ZoneConstants.GRAVEYARD) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(graveyardPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }

            if(screen.getZoneIndex() == ZoneConstants.MOONLIGHT) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(screen.getRoomIndex() == 5 && screen.getScreenIndex() == 1 ? moonlightPyramidPlatformSpeed : moonlightPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }

            if(screen.getZoneIndex() == ZoneConstants.BIRTH_SKANDA && screen.getRoomIndex() == 0) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(birthGrailCogPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }
            else if(screen.getZoneIndex() == ZoneConstants.BIRTH_SKANDA) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(skandaBlockingPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }

            if(screen.getZoneIndex() == ZoneConstants.DIMENSIONAL && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(dimensionalPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }

            if(screen.getZoneIndex() == ZoneConstants.RETRO_MAUSOLEUM) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(retroMausoleumPlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }

            if(screen.getZoneIndex() == ZoneConstants.RETRO_GUIDANCE) {
                MovingPlatform cursedMovingPlatform = new MovingPlatform(existingMovingPlatform);
                cursedMovingPlatform.setPlatformSpeed(retroGuidancePlatformSpeed);
                cursedMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_LT, ValueConstants.TIME_THRESHOLD));
                existingMovingPlatform.getObjectContainer().getObjects().add(cursedMovingPlatform);
            }
        }
    }

    @Override
    void updateScreenExits(Screen screen) {
        if(Settings.getCurrentStartingLocation() == ZoneConstants.NIGHT_SURFACE) {
            for(ScreenExit screenExit : screen.getScreenExits()) {
                if(screenExit.getZoneIndex() == -1 && screenExit.getRoomIndex() == -1 && screenExit.getScreenIndex() == -1) {
                    screenExit.setDestination(LocationCoordinateMapper.getStartingZone(),
                            LocationCoordinateMapper.getStartingRoom(),
                            LocationCoordinateMapper.getStartingScreen());
                }
            }
        }
        if(Settings.getCurrentStartingLocation() == ZoneConstants.NIGHT_SURFACE) {
            for(ScreenExit screenExit : screen.getScreenExits()) {
                if(screenExit.getZoneIndex() == 1) {
                    screenExit.setZoneIndex((byte)22);
                }
            }
        }
    }

    private void replaceNightSurfaceWithSurface() {
        Zone surface = rcdFileData.getZone(ZoneConstants.SURFACE);
        Zone nightSurface = rcdFileData.getZone(ZoneConstants.NIGHT_SURFACE);
        for(GameObject gameObject : surface.getObjects()) {
            nightSurface.getObjects().add(new GameObject(gameObject));
        }
        for(Room surfaceRoom : surface.getRooms()) {
            Room nightSurfaceRoom = nightSurface.getRoom(surfaceRoom.getRoomIndex());
            nightSurfaceRoom.getObjects().clear();
            for(GameObject gameObject : surfaceRoom.getObjects()) {
                nightSurfaceRoom.getObjects().add(new GameObject(gameObject));
            }
            for(Screen surfaceScreen : surfaceRoom.getScreens()) {
                Screen nightSurfaceScreen = nightSurfaceRoom.getScreen(surfaceScreen.getScreenIndex());
                List<GameObject> positionalObjectsToPreserve = new ArrayList<>();
                if(nightSurfaceScreen.getRoomIndex() == 7 && nightSurfaceScreen.getScreenIndex() == 0) {
                    for(GameObject gameObject : nightSurfaceScreen.getObjects()) {
                        if(gameObject.getId() == ObjectIdConstants.ConversationDoor) {
                            positionalObjectsToPreserve.add(gameObject);
                        }
                    }
                }
                nightSurfaceScreen.getObjects().clear();
                boolean isXelpudStatueScreen = surfaceScreen.getRoomIndex() == 4 && surfaceScreen.getScreenIndex() == 2;
                for(GameObject gameObject : surfaceScreen.getObjects()) {
                    if(isXelpudStatueScreen && gameObject.getId() == ObjectIdConstants.LemezaDetector && gameObject.getArgs().size() > 3) {
                        int detectorType = gameObject.getArgs().get(3);
                        if(detectorType < 0 || detectorType > 4) {
                            // Sleep detector - exclude this so we don't mess with HT unlock state
                            continue;
                        }
                    }
                    nightSurfaceScreen.getObjects().add(new GameObject(gameObject));
                }
                nightSurfaceScreen.getObjects().addAll(positionalObjectsToPreserve);
                if(nightSurfaceScreen.getRoomIndex() == 2 && nightSurfaceScreen.getScreenIndex() == 1) {
                    AddObject.addStartingItems(nightSurfaceScreen);
                    if (Settings.isAutomaticHardmode()) {
                        AddObject.addAutomaticHardmodeTimer(nightSurfaceScreen);
                    }
                    if (Settings.isAutomaticTranslations()) {
                        AddObject.addAutomaticTranslationsTimer(nightSurfaceScreen);
                    }
                }
                if(nightSurfaceScreen.getRoomIndex() == 9 && nightSurfaceScreen.getScreenIndex() == 0) {
                    AddObject.addDanceDetector(nightSurfaceScreen, getCustomBlockIndex(CustomBlockEnum.HalloweenDanceBlock));
                    AddObject.addSecretShop(nightSurfaceScreen, getCustomBlockIndex(CustomBlockEnum.HalloweenSecretShopBlock));
                }

                nightSurfaceScreen.getScreenExits().clear();
                for(ScreenExit surfaceScreenExit : surfaceScreen.getScreenExits()) {
                    ScreenExit nightSurfaceScreenExit = new ScreenExit();
                    nightSurfaceScreenExit.setZoneIndex(surfaceScreenExit.getZoneIndex() == ZoneConstants.SURFACE ? ZoneConstants.NIGHT_SURFACE : surfaceScreenExit.getZoneIndex());
                    nightSurfaceScreenExit.setRoomIndex(surfaceScreenExit.getRoomIndex());
                    nightSurfaceScreenExit.setScreenIndex(surfaceScreenExit.getScreenIndex());
                    nightSurfaceScreen.getScreenExits().add(nightSurfaceScreenExit);
                }
            }
        }
    }

    private void updateMudmanPressurePlate() {
        Screen perfumeChestScreen = rcdFileData.getScreen(15, 2, 0);
        for(GameObject gameObject : perfumeChestScreen.getObjectsById(ObjectIdConstants.PressurePlate)) {
            PressurePlate lemezaOnlyPressurePlate = new PressurePlate(gameObject);
            lemezaOnlyPressurePlate.setDetectionType(PressurePlate.Detection_Lemeza);
            lemezaOnlyPressurePlate.addTests(new TestByteOperation(FlagConstants.COG_MUDMEN_STATE, ByteOp.FLAG_LT, 4));
            gameObject.addTests(new TestByteOperation(FlagConstants.COG_MUDMEN_STATE, ByteOp.FLAG_EQUALS, 4));
            perfumeChestScreen.getObjects().add(lemezaOnlyPressurePlate);
        }
    }

    private void updateForEscapeChest() {
        Screen escapeChestScreen = rcdFileData.getScreen(7, 2, 0);
        for(GameObject gameObject : escapeChestScreen.getObjectsById(ObjectIdConstants.FlagTimer)) {
            for(TestByteOperation testByteOperation : gameObject.getTestByteOperations()) {
                if(testByteOperation.getIndex() == FlagConstants.ESCAPE) {
                    testByteOperation.setOp(ByteOp.FLAG_GTEQ);
                    testByteOperation.setIndex(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_DEFEATED);
                }
            }
        }
        for(GameObject gameObject : escapeChestScreen.getObjectsById(ObjectIdConstants.RoomSpawner)) {
            for(TestByteOperation testByteOperation : gameObject.getTestByteOperations()) {
                if(testByteOperation.getIndex() == FlagConstants.ESCAPE) {
                    testByteOperation.setIndex(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_DEFEATED);
                }
            }
        }
        for(GameObject gameObject : escapeChestScreen.getObjectsById(ObjectIdConstants.OneWayDoor)) {
            for(TestByteOperation testByteOperation : gameObject.getTestByteOperations()) {
                if(testByteOperation.getIndex() == FlagConstants.ESCAPE) {
                    testByteOperation.setIndex(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_DEFEATED);
                }
            }
        }
        for(GameObject gameObject : escapeChestScreen.getObjectsById(ObjectIdConstants.Chest)) {
            Chest chest = (Chest)gameObject;
            chest.setDrops(ItemConstants.SECRET_TREASURE_OF_LIFE + 11, 1);
            chest.setChestGraphic(Chest.ITEM_CHEST);
            chest.setEmptyCheck(new WriteByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_ESCAPE_CHEST, ByteOp.ASSIGN_FLAG, 2));
            chest.setUpdateWhenOpened(new WriteByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_ESCAPE_CHEST, ByteOp.ASSIGN_FLAG, 1));
            chest.setUpdateWhenCollected(new WriteByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_ESCAPE_CHEST, ByteOp.ASSIGN_FLAG, 2));
        }
        for(GameObject gameObject : escapeChestScreen.getObjectsById(ObjectIdConstants.FallingRoom)) {
            for(TestByteOperation testByteOperation : gameObject.getTestByteOperations()) {
                if(testByteOperation.getIndex() == FlagConstants.ESCAPE) {
                    testByteOperation.setOp(ByteOp.FLAG_GTEQ);
                    testByteOperation.setIndex(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_DEFEATED);
                }
            }
        }
        Screen screenRightOfMotherAnkh = rcdFileData.getScreen(18, 3, 1);

        ItemGive itemGive = new ItemGive(screenRightOfMotherAnkh, 640, 0);
        itemGive.setInventoryWord(ItemConstants.HOLY_GRAIL);
        itemGive.setWidth(32);
        itemGive.setHeight(24);
        itemGive.setSoundEffect(39);

        itemGive.getTestByteOperations().add(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_DEFEATED, ByteOp.FLAG_EQUALS, 1));
        itemGive.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_MOTHER_DEFEATED, ByteOp.ASSIGN_FLAG, 2));
        screenRightOfMotherAnkh.getObjects().add(itemGive);
    }

    private void fixTransitionGates() {
        for(GameObject gameObject : rcdFileData.getObjectsById(ObjectIdConstants.TransitionGate)) {
            if(gameObject.getArgs().get(0) == ZoneConstants.SURFACE) {
                gameObject.getArgs().set(0, (short)ZoneConstants.NIGHT_SURFACE);
            }
        }
    }

    private void updateEnemies() {
        updateBats();
        updateSkeletons();

        for(GameObject gameObject : rcdFileData.getObjectsById(ObjectIdConstants.TogSpawner)) {
            gameObject.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.ENEMY_THRESHOLD1));
        }
        for(GameObject gameObject : rcdFileData.getObjectsById(ObjectIdConstants.Enemy_CatBall)) {
            gameObject.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.ENEMY_THRESHOLD1));
        }
        for(GameObject gameObject : rcdFileData.getObjectsById(ObjectIdConstants.Enemy_Yowie)) {
            gameObject.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.ENEMY_THRESHOLD1));
        }

        for(GameObject gameObject : rcdFileData.getObjectsById(ObjectIdConstants.Enemy_SwordBird)) {
            gameObject.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.STRONGER_ENEMIES_THRESHOLD));
        }

        int witchType;
        for(GameObject gameObject : rcdFileData.getObjectsById(ObjectIdConstants.Enemy_Witch)) {
            witchType = gameObject.getArgs().get(1);
            if(witchType == ValueConstants.WITCH_TYPE_LIGHTNING || witchType == ValueConstants.WITCH_TYPE_FIRE
                    || witchType == ValueConstants.WITCH_TYPE_STUN || witchType == ValueConstants.WITCH_TYPE_ORB) {
                gameObject.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.ENEMY_THRESHOLD1));
            }
            else if(witchType == ValueConstants.WITCH_TYPE_WHITE || witchType == ValueConstants.WITCH_TYPE_BLACK) {
                Screen screen = (Screen)gameObject.getObjectContainer();
                if(!(screen.getZoneIndex() == 7 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 1)
                        && !(screen.getZoneIndex() == 7 && screen.getRoomIndex() == 13 && screen.getScreenIndex() == 0)) {
                    gameObject.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_CURSED, ByteOp.FLAG_GTEQ, ValueConstants.STRONGER_ENEMIES_THRESHOLD));
                }
            }
        }
    }

    private void updateBats() {
        List<GameObject> bats = new ArrayList<>(rcdFileData.getObjectsById(ObjectIdConstants.Enemy_Bat));
        Screen screen;
        boolean addBatKillUpdate;
        boolean puzzleRelevantBat;
        for(GameObject bat : bats) {
            screen = (Screen)bat.getObjectContainer();

            addBatKillUpdate = true;
            puzzleRelevantBat = false;
            for(WriteByteOperation writeByteOperation : bat.getWriteByteOperations()) {
                if(writeByteOperation.getIndex() == FlagConstants.GUIDANCE_BATS_KILLED_COUNT) {
                    addBatKillUpdate = false;
                    break;
                }
                else if(writeByteOperation.getIndex() == FlagConstants.ROOM_FLAG_3B) {
                    if(screen.getZoneIndex() == 3 && screen.getRoomIndex() == 4 && screen.getScreenIndex() == 3) {
                        // Cast down those that fly
                        addBatKillUpdate = false;
                        puzzleRelevantBat = true;
                        break;
                    }
                }
            }
            if(addBatKillUpdate) {
                bat.addUpdates(new WriteByteOperation(FlagConstants.GUIDANCE_BATS_KILLED_COUNT, ByteOp.ADD_FLAG, 1));
            }

            Bat backsideBat = new Bat(bat);
            backsideBat.setFrontsideBat(false);
            backsideBat.setDamage(backsideBat.getDamage() + 2);
            backsideBat.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_BAT_LEVEL, ByteOp.FLAG_EQUALS, 2));
            screen.getObjects().add(backsideBat);
            if(!puzzleRelevantBat) {
                bat.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_BAT_LEVEL, ByteOp.FLAG_EQUALS, 1));
            }
        }
    }

    private void updateSkeletons() {
        List<GameObject> skeletons = new ArrayList<>(rcdFileData.getObjectsById(ObjectIdConstants.Enemy_Skeleton));
        Screen screen;
        for(GameObject skeleton : skeletons) {
            screen = (Screen)skeleton.getObjectContainer();
            if(screen.getZoneIndex() == 0 && screen.getRoomIndex() == 6 && screen.getScreenIndex() == 1) {
                if(skeleton.hasUpdate(new WriteByteOperation(FlagConstants.SCREEN_FLAG_0, ByteOp.ADD_FLAG, 1))) {
                    // Red Skeletons
                    continue;
                }
            }
            Skeleton strongSkeleton = new Skeleton(skeleton);
            strongSkeleton.setHealth(Short.MAX_VALUE);
            strongSkeleton.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_SKELETON_LEVEL, ByteOp.FLAG_EQUALS, 2));
            screen.getObjects().add(strongSkeleton);
            skeleton.addTests(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN2021_SKELETON_LEVEL, ByteOp.FLAG_EQUALS, 1));
        }
    }
}
