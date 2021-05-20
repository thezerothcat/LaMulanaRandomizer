package lmr.randomizer.rcd;

import lmr.randomizer.FlagConstants;
import lmr.randomizer.Settings;
import lmr.randomizer.ZoneConstants;
import lmr.randomizer.dat.AddObject;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.update.LocationCoordinateMapper;

import java.util.Arrays;

public class HalloweenRcdUpdater extends RcdUpdater {
    public HalloweenRcdUpdater(RcdData rcdData) {
        super(rcdData);
    }

    @Override
    boolean updatePot(GameObject pot) {
        return true;
    }

    @Override
    boolean updateBat(GameObject bat) {
        ObjectContainer objectContainer = bat.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 15 && screen.getScreenIndex() == 0) {
            return false;
        }
        return true;
    }

    @Override
    boolean updateSkeleton(GameObject skeleton) {
        if(Settings.isIncludeHellTempleNPCs()) {
            ObjectContainer objectContainer = skeleton.getObjectContainer();
            if(!(objectContainer instanceof Screen)) {
                return true;
            }
            Screen screen = (Screen)objectContainer;
            if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 16 && screen.getScreenIndex() == 0) {
                // Fix flag for looping punishment room enemies
                for(int i = 0; i < skeleton.getWriteByteOperations().size(); i++) {
                    WriteByteOperation flagUpdate = skeleton.getWriteByteOperations().get(i);
                    if(flagUpdate.getIndex() == FlagConstants.SCREEN_FLAG_0) {
                        flagUpdate.setIndex(FlagConstants.CUSTOM_HALLOWEEN_H4);
                    }
                }
            }
        }
        return true;
    }

    @Override
    boolean updateLadder(GameObject ladder) {
        return true;
    }

    @Override
    boolean updateDais(GameObject dais) {
        return true;
    }

    @Override
    boolean updateFlagTimer(GameObject flagTimer) {
        ObjectContainer objectContainer = flagTimer.getObjectContainer();
        if(flagTimer.getObjectContainer() instanceof Screen) {
            Screen screen = (Screen)objectContainer;
            if(Settings.isIncludeHellTempleNPCs()) {
                if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 16 && screen.getScreenIndex() == 0) {
                    for (int i = 0; i < flagTimer.getTestByteOperations().size(); i++) {
                        TestByteOperation flagTest = flagTimer.getTestByteOperations().get(i);
                        if (flagTest.getIndex() == FlagConstants.SCREEN_FLAG_0) {
                            flagTest.setIndex(FlagConstants.CUSTOM_HALLOWEEN_H4);
                            break;
                        }
                    }
                }
            }
        }
        for(int i = 0; i < flagTimer.getTestByteOperations().size(); i++) {
            TestByteOperation flagTest = flagTimer.getTestByteOperations().get(i);
            if(flagTest.getIndex() == FlagConstants.GATE_OF_TIME_FAIRY_UNKNOWN && flagTest.getValue() == 0) {
                // 8bit Fairy timer - conversation needs added test
                flagTimer.getTestByteOperations().add(new TestByteOperation(FlagConstants.getNpcConversationFlag((short)710), ByteOp.FLAG_GT, 0));
            }
        }
        return true;
    }

    @Override
    boolean updateRoomSpawner(GameObject roomSpawner) {
        return true;
    }

    @Override
    boolean updateCrusher(GameObject crusher) {
        ObjectContainer objectContainer = crusher.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 20 && screen.getScreenIndex() == 0) {
            return false;
        }
        return true;
    }

    @Override
    boolean updateHitbox(GameObject hitbox) {
        return true;
    }

    @Override
    boolean updateLemezaDetector(GameObject lemezaDetector) {
        return true;
    }

    @Override
    boolean updateFist(GameObject fist) {
        return updateHalloweenEnemy(fist);
    }

    @Override
    boolean updateSteam(GameObject steam) {
        return true;
    }

    @Override
    boolean updateSonic(GameObject sonic) {
        return true;
    }

    @Override
    boolean updateGhostSpawner(GameObject ghostSpawner) {
        boolean clearFlags = false;
        for(TestByteOperation testByteOperation : ghostSpawner.getTestByteOperations()) {
            if(testByteOperation.getIndex() == FlagConstants.SAKIT_ANKH_PUZZLE) {
                clearFlags = true;
                break;
            }
        }
        if(clearFlags) {
            ghostSpawner.getTestByteOperations().clear();
        }
        return true;
    }

    @Override
    boolean updateChest(GameObject chest) {
        return true;
    }

    @Override
    boolean updateWeaponCover(GameObject weaponCover) {
        return true;
    }

    @Override
    boolean updateAnkh(GameObject ankh) {
        ObjectContainer objectContainer = ankh.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        // Bahamut to Night Surface
        if(screen.getZoneIndex() == 4) {
            ankh.getArgs().set(24, (short)22);
            ankh.getArgs().set(28, (short)22);
        }
        return true;
    }

    @Override
    boolean updateFloatingItem(GameObject floatingItem) {
        return true;
    }

    @Override
    boolean updateTrapdoor(GameObject trapdoor) {
        return true;
    }

    @Override
    boolean updateSeal(GameObject seal) {
        ObjectContainer objectContainer = seal.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if (screen.getZoneIndex() == 3 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 0) {
            // Don't require Birth Seal for Mulbruk stuff
            return false;
        }
        if(screen.getZoneIndex() == 18 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 0) {
            // Remove seal for mother ankh
            return false;
        }
        return true;
    }

    @Override
    boolean updateSlime(GameObject slime) {
        return true;
    }

    @Override
    boolean updateLavaRock(GameObject lavaRock) {
        return false; // Remove lava rocks in favor of ghosts.
    }

    @Override
    boolean updateSpriggan(GameObject spriggan) {
        return true;
    }

    @Override
    boolean updateHundun(GameObject hundun) {
        return false; // Remove Hundun shrine enemy in favor of ghosts.
    }

    @Override
    boolean updatePan(GameObject pan) {
        if(Settings.isIncludeHellTempleNPCs()) {
            ObjectContainer objectContainer = pan.getObjectContainer();
            if(!(objectContainer instanceof Screen)) {
                return true;
            }
            Screen screen = (Screen)objectContainer;
            if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 16 && screen.getScreenIndex() == 0) {
                // Fix flag for looping punishment room enemies
                for(int i = 0; i < pan.getWriteByteOperations().size(); i++) {
                    WriteByteOperation flagUpdate = pan.getWriteByteOperations().get(i);
                    if(flagUpdate.getIndex() == FlagConstants.SCREEN_FLAG_0) {
                        flagUpdate.setIndex(FlagConstants.CUSTOM_HALLOWEEN_H4);
                    }
                }
            }
        }
        return updateHalloweenEnemy(pan);
    }

    @Override
    boolean updateHanuman(GameObject hanuman) {
        return updateHalloweenEnemy(hanuman);
    }

    @Override
    boolean updateEnkidu(GameObject enkidu) {
        return updateHalloweenEnemy(enkidu);
    }

    @Override
    boolean updateMarchosias(GameObject marchosias) {
        return updateHalloweenEnemy(marchosias);
    }

    @Override
    boolean updateWitch(GameObject witch) {
        return updateHalloweenEnemy(witch);
    }

    @Override
    boolean updateLizardMan(GameObject lizardMan) {
        return updateHalloweenEnemy(lizardMan);
    }

    @Override
    boolean updateChiYou(GameObject chiYou) {
        return true;
    }

    @Override
    boolean updateToujin(GameObject toujin) {
        return updateHalloweenEnemy(toujin);
    }

    @Override
    boolean updateIceWizard(GameObject iceWizard) {
        if(Settings.isIncludeHellTempleNPCs()) {
            ObjectContainer objectContainer = iceWizard.getObjectContainer();
            if(!(objectContainer instanceof Screen)) {
                return true;
            }
            Screen screen = (Screen)objectContainer;
            if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 16 && screen.getScreenIndex() == 0) {
                // Fix flag for looping punishment room enemies
                for(int i = 0; i < iceWizard.getWriteByteOperations().size(); i++) {
                    WriteByteOperation flagUpdate = iceWizard.getWriteByteOperations().get(i);
                    if(flagUpdate.getIndex() == FlagConstants.SCREEN_FLAG_0) {
                        flagUpdate.setIndex(FlagConstants.CUSTOM_HALLOWEEN_H4);
                    }
                }
            }
        }
        return updateHalloweenEnemy(iceWizard);
    }

    @Override
    boolean updateAnubis(GameObject anubis) {
        return true;
    }

    @Override
    boolean updateNinjaSpawner(GameObject ninjaSpawner) {
        return updateHalloweenEnemy(ninjaSpawner);
    }

    @Override
    boolean updateAndras(GameObject andras) {
        return updateHalloweenEnemy(andras);
    }

    @Override
    boolean updateChonchonSpawner(GameObject chonchonSpawner) {
        return false; // Ban Medusa heads in favor of ghosts
    }

    @Override
    boolean updateVimana(GameObject vimana) {
        return true;
    }

    @Override
    boolean updateSwordBird(GameObject swordBird) {
        if(Settings.isIncludeHellTempleNPCs()) {
            ObjectContainer objectContainer = swordBird.getObjectContainer();
            if(!(objectContainer instanceof Screen)) {
                return true;
            }
            Screen screen = (Screen)objectContainer;
            if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 16 && screen.getScreenIndex() == 0) {
                // Fix flag for looping punishment room enemies
                for(int i = 0; i < swordBird.getWriteByteOperations().size(); i++) {
                    WriteByteOperation flagUpdate = swordBird.getWriteByteOperations().get(i);
                    if(flagUpdate.getIndex() == FlagConstants.SCREEN_FLAG_0) {
                        flagUpdate.setIndex(FlagConstants.CUSTOM_HALLOWEEN_H4);
                    }
                }
            }
        }
        return updateHalloweenEnemy(swordBird);
    }

    @Override
    boolean updateElephant(GameObject elephant) {
        return updateHalloweenEnemy(elephant);
    }

    @Override
    boolean updateAmon(GameObject amon) {
        return updateHalloweenEnemy(amon);
    }

    @Override
    boolean updateSatan(GameObject satan) {
        return updateHalloweenEnemy(satan);
    }

    @Override
    boolean updateDevil(GameObject devil) {
        if(Settings.isIncludeHellTempleNPCs()) {
            ObjectContainer objectContainer = devil.getObjectContainer();
            if(!(objectContainer instanceof Screen)) {
                return true;
            }
            Screen screen = (Screen)objectContainer;
            if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 16 && screen.getScreenIndex() == 0) {
                // Fix flag for looping punishment room enemies
                for(int i = 0; i < devil.getWriteByteOperations().size(); i++) {
                    WriteByteOperation flagUpdate = devil.getWriteByteOperations().get(i);
                    if(flagUpdate.getIndex() == FlagConstants.SCREEN_FLAG_0) {
                        flagUpdate.setIndex(FlagConstants.CUSTOM_HALLOWEEN_H4);
                    }
                }
            }
        }
        return updateHalloweenEnemy(devil);
    }

    @Override
    boolean updateUmuDabrutu(GameObject umuDabrutu) {
        return true;
    }

    @Override
    boolean updateUrmahlullu(GameObject urmahlullu) {
        return true;
    }

    @Override
    boolean updateMushnahhu(GameObject mushnahhu) {
        return true;
    }

    @Override
    boolean updateUshum(GameObject ushum) {
        ObjectContainer objectContainer = ushum.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(Settings.isIncludeHellTempleNPCs()) {
            if(screen.getZoneIndex() == 24) {
                Integer flagToRemoveIndex = null;
                for (int i = 0; i < ushum.getTestByteOperations().size(); i++) {
                    if (ushum.getTestByteOperations().get(i).getIndex() == FlagConstants.SCREEN_FLAG_16) {
                        flagToRemoveIndex = i;
                        break;
                    }
                }
                if(flagToRemoveIndex != null) {
                    ushum.getTestByteOperations().remove((int)flagToRemoveIndex);
                }
            }
        }
        return true;
    }

    @Override
    boolean updateMushussu(GameObject mushussu) {
        ObjectContainer objectContainer = mushussu.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(Settings.isIncludeHellTempleNPCs()) {
            if(screen.getZoneIndex() == 24) {
                return false;
            }
        }
        return true;
    }

    @Override
    boolean updateMiniBoss(GameObject miniBoss) {
        if(Settings.isIncludeHellTempleNPCs()) {
            ObjectContainer objectContainer = miniBoss.getObjectContainer();
            if(!(objectContainer instanceof Screen)) {
                return true;
            }
            Screen screen = (Screen)objectContainer;
            if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 16 && screen.getScreenIndex() == 0) {
                // Fix flag for looping punishment room enemies
                for(int i = 0; i < miniBoss.getWriteByteOperations().size(); i++) {
                    WriteByteOperation flagUpdate = miniBoss.getWriteByteOperations().get(i);
                    if(flagUpdate.getIndex() == FlagConstants.SCREEN_FLAG_0) {
                        flagUpdate.setIndex(FlagConstants.CUSTOM_HALLOWEEN_H4);
                    }
                }
            }
        }
        return updateHalloweenEnemy(miniBoss);
    }

    @Override
    boolean updateTheBoss(GameObject theBoss) {
        // The Boss ankh
//        if(Settings.isIncludeHellTempleNPCs()) {
//            theBoss.getArgs().set(3, (short)160); // HP
//            theBoss.getArgs().set(7, (short)1); // Max number of slimes
//            theBoss.getArgs().set(12, (short)240); // Delay between bombs
//        }
        return true;
    }

    @Override
    boolean updateFairyPoint(GameObject fairyPoint) {
        return true;
    }

    @Override
    boolean updateFog(GameObject fog) {
        return true;
    }

    @Override
    boolean updateGraphicsTextureDraw(GameObject graphicsTextureDraw) {
        ObjectContainer objectContainer = graphicsTextureDraw.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(screen.getZoneIndex() == 3 && screen.getRoomIndex() == 3 && screen.getScreenIndex() == 0) {
            // Graphic for closed Mulbruk door should be removed since the door won't be closed.
            return false;
        }
        if(Settings.isIncludeHellTempleNPCs()) {
            Integer flagToRemove = null;
            for(int i = 0; i < graphicsTextureDraw.getTestByteOperations().size(); i++) {
                TestByteOperation flagTest = graphicsTextureDraw.getTestByteOperations().get(i);
                if(flagTest.getIndex() == FlagConstants.HT_SOLVED_ROOM35) {
                    flagToRemove = i;
                }
            }
            if(flagToRemove != null) {
                graphicsTextureDraw.getTestByteOperations().remove((int)flagToRemove);
            }

            for(int i = 0; i < graphicsTextureDraw.getTestByteOperations().size(); i++) {
                TestByteOperation flagTest = graphicsTextureDraw.getTestByteOperations().get(i);
                if(flagTest.getIndex() == FlagConstants.WF_PROVOCATIVE_BATHING_SUIT) {
                    flagTest.setIndex(FlagConstants.CUSTOM_HALLOWEEN_FINAL_DRACUET);
                    flagTest.setValue((byte)0);
                }
            }
        }
        return true;
    }

    @Override
    boolean updateEyeOfRetribution(GameObject eyeOfRetribution) {
        return true;
    }

    @Override
    boolean updateExtendableSpikes(GameObject extendableSpikes) {
        return true;
    }

    @Override
    boolean updateWarpPortal(GameObject warpPortal) {
        ObjectContainer objectContainer = warpPortal.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 21 && screen.getScreenIndex() == 0) {
            // Disable HT exit during HT escape sequence.
            warpPortal.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 0));
        }
        return true;
    }

    @Override
    boolean updateWarpDoor(GameObject warpDoor) {
        int destinationZoneIndex = warpDoor.getArgs().get(1);
        if(destinationZoneIndex == ZoneConstants.SURFACE) {
            // Night Surface, not normal Surface.
            warpDoor.getArgs().set(1, (short)ZoneConstants.NIGHT_SURFACE);
        }

        if(Settings.isIncludeHellTempleNPCs()) {
            // Add escape door in place of normal HT to Guidance door
            if(warpDoor.getArgs().get(0) == 0) {
                Screen screen = (Screen)warpDoor.getObjectContainer();
                if(screen.getZoneIndex() == 23 && screen.getRoomIndex() == 0 && screen.getScreenIndex() == 0) {
                    if(warpDoor.getArgs().get(1) == 0) {
                        warpDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 0));
                    }
                }
            }
        }
        return true;
    }

    @Override
    boolean updateFallingRoom(GameObject crusher) {
        return true;
    }

    @Override
    boolean updateSoundEffect(GameObject soundEffect) {
        return true;
    }

    @Override
    boolean updateUseItemDetector(GameObject useItemDetector) {
        return true;
    }

    @Override
    boolean updateScannable(GameObject scannable) {
        return true;
    }

    @Override
    boolean updateAutosave(GameObject autosave) {
        return true;
    }

    @Override
    boolean updateConversationDoor(GameObject conversationDoor) {
        if(conversationDoor.getArgs().get(4) == 685) {
            // Halloween doesn't need this Fairy Queen conversation.
            return false;
        }
        else if(conversationDoor.getArgs().get(4) == 985) {
            // Halloween doesn't need this Fairy Queen conversation either.
            return false;
        }
        else if(conversationDoor.getArgs().get(4) == 1011) {
            if(Settings.isIncludeHellTempleNPCs()) {
                // Dracuet conversation has some unwanted flags
                Integer flagToRemove = null;
                for (int i = 0; i < conversationDoor.getTestByteOperations().size(); i++) {
                    TestByteOperation flagTest = conversationDoor.getTestByteOperations().get(i);
                    if (flagTest.getIndex() == FlagConstants.HT_SOLVED_ROOM35) {
                        flagToRemove = i;
                    }
                }
                if(flagToRemove != null) {
                    conversationDoor.getTestByteOperations().remove((int)flagToRemove);
                }

                for (int i = 0; i < conversationDoor.getTestByteOperations().size(); i++) {
                    TestByteOperation flagTest = conversationDoor.getTestByteOperations().get(i);
                    if (flagTest.getIndex() == FlagConstants.WF_PROVOCATIVE_BATHING_SUIT) {
                        flagTest.setIndex(FlagConstants.CUSTOM_HALLOWEEN_FINAL_DRACUET);
                        flagTest.setValue((byte)0);
                    }
                }
            }
        }
        else if(conversationDoor.getArgs().get(4) == 924) {
            // Mulbruk escape
            if(Settings.isIncludeHellTempleNPCs()) {
                return false;
            }
        }
        else if(conversationDoor.getArgs().get(4) == 926) {
            // Mulbruk escape
            if(Settings.isIncludeHellTempleNPCs()) {
                return false;
            }
        }

        int blockNumber = conversationDoor.getArgs().get(4);
        if(blockNumber == 681) {
            // Priest Triton - Extinction NPC, 06-09-01
            for (TestByteOperation flagTest : conversationDoor.getTestByteOperations()) {
                if (flagTest.getIndex() == FlagConstants.PALENQUE_ANKH_PUZZLE) {
                    // Fix conversation to be based on whether the Palenque fight is active, rather than whether the ankh is present.
                    flagTest.setIndex(FlagConstants.PALENQUE_STATE);
                    flagTest.setOp(ByteOp.FLAG_NOT_EQUAL);
                    flagTest.setValue((byte)2);
                    break;
                }
            }
        }
        else if(blockNumber == 684) {
            // Fairy Queen - Endless NPC, 08-01-00
            Integer flagToRemoveIndex = null;
            for (int i = 0; i < conversationDoor.getWriteByteOperations().size(); i++) {
                if (conversationDoor.getWriteByteOperations().get(i).getIndex() == FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES) {
                    flagToRemoveIndex = i;
                    break;
                }
            }
            if(flagToRemoveIndex != null) {
                conversationDoor.getWriteByteOperations().remove((int)flagToRemoveIndex);
            }
        }
        else if(blockNumber == 686) {
            // The Fairy Queen - Endless NPC, 08-01-00
            conversationDoor.getTestByteOperations().clear();
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES, ByteOp.FLAG_EQUALS, 2));
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_ISIS_PENDANT, ByteOp.FLAG_EQUALS, 2));
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.MOTHER_STATE, ByteOp.FLAG_NOT_EQUAL, 3));

            for(WriteByteOperation writeByteOperation : conversationDoor.getWriteByteOperations()) {
                if(writeByteOperation.getIndex() == FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES) {
                    writeByteOperation.setValue(3);
                    break;
                }
            }
        }
        else if(blockNumber == 687) {
            conversationDoor.getTestByteOperations().clear();
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES, ByteOp.FLAG_EQUALS, 2));
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.WF_ISIS_PENDANT, ByteOp.FLAG_NOT_EQUAL, 2));
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.MOTHER_STATE, ByteOp.FLAG_NOT_EQUAL, 3));
        }
        else if(blockNumber == 688) {
            conversationDoor.getArgs().set(4, (short)687);

            conversationDoor.getTestByteOperations().clear();
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES, ByteOp.FLAG_EQUALS, 3));
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.MOTHER_STATE, ByteOp.FLAG_NOT_EQUAL, 3));

            conversationDoor.getWriteByteOperations().clear();
        }
        else if(blockNumber == 690) {
            // Conversation after receiving Pepper if you don't have Treasures
            // Mr. Slushfund - Illusion NPC, 10-08-00
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.getNpcConversationFlag((short)689), ByteOp.FLAG_GT, 0));
        }
        else if(blockNumber == 691) {
            // Conversation to give Treasures and receive Anchor
            // Mr. Slushfund - Illusion NPC, 10-08-00
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.getNpcConversationFlag((short)689), ByteOp.FLAG_GT, 0));
        }
        else if(blockNumber == 692) {
            // Conversation after receiving both Pepper and Anchor
            // Mr. Slushfund - Illusion NPC, 10-08-00
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.getNpcConversationFlag((short)689), ByteOp.FLAG_GT, 0));
        }
        else if(blockNumber == 705) {
            // Philosopher Fobos - Dimensional NPC, 17-02-00
            // Post-Medicine version of Fobos
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.getNpcConversationFlag((short)704), ByteOp.FLAG_GT, 0));
        }
        else if(blockNumber == 714) {
            // 8bit Fairy - conversation needs added test
            conversationDoor.getTestByteOperations().add(new TestByteOperation(FlagConstants.getNpcConversationFlag((short)710), ByteOp.FLAG_GT, 0));
        }
        return true;
    }

    @Override
    boolean updateAnimation(GameObject animation) {
        return true;
    }

    @Override
    boolean updateKeyFairySpot(GameObject keyFairySpot) {
        return true;
    }

    @Override
    boolean updatePushableBlock(GameObject pushableBlock) {
        return true;
    }

    @Override
    boolean updateBlockButton(GameObject blockButton) {
        return true;
    }

    @Override
    boolean updateHotSpring(GameObject hotSpring) {
        return true;
    }

    @Override
    boolean updateExplosion(GameObject explosion) {
        return true;
    }

    @Override
    boolean updateItemGive(GameObject itemGive) {
        return true;
    }

    @Override
    boolean updateSavePoint(GameObject savePoint) {
        return true;
    }

    @Override
    boolean updateGrailToggle(GameObject grailToggle) {
        return true;
    }

    @Override
    boolean updateMotherAnkh(GameObject motherAnkh) {
        return false;
    }

    @Override
    boolean updateMantraDetector(GameObject mantraDetector) {
        return true;
    }

    @Override
    boolean updateSnapshotsScan(GameObject snapshotsScan) {
        return true;
    }

    @Override
    boolean updateTransitionGate(GameObject transitionGate) {
        return true;
    }

    @Override
    boolean updateEscapeTimer(GameObject escapeTimer) {
        return true;
    }

    @Override
    boolean updateEscapeScreenShake(GameObject escapeScreenShake) {
        return true;
    }

    private boolean updateHalloweenEnemy(GameObject enemy) {
        ObjectContainer objectContainer = enemy.getObjectContainer();
        if(!(objectContainer instanceof Screen)) {
            return true;
        }
        Screen screen = (Screen)objectContainer;
        if(Settings.isIncludeHellTempleNPCs()) {
            if(screen.getZoneIndex() == 23) {
                if(screen.getRoomIndex() == 1) {
                    if(screen.getScreenIndex() == 0) {
                        // HT room 1
                        return false;
                    }
                }
                if(screen.getRoomIndex() == 2) {
                    if(screen.getScreenIndex() == 0 || screen.getScreenIndex() == 1) {
                        // HT rooms 3 and 4
                        return false;
                    }
                }
                if(screen.getRoomIndex() == 5) {
                    if(screen.getScreenIndex() == 0) {
                        // HT room 7
                        return false;
                    }
                }
                if(screen.getRoomIndex() == 6) {
                    if(screen.getScreenIndex() == 0) {
                        // HT room 10
                        return false;
                    }
                }
                if(screen.getRoomIndex() == 8) {
                    if(screen.getScreenIndex() == 0) {
                        // HT room 13
                        return false;
                    }
                }
                if(screen.getRoomIndex() == 9) {
                    if(screen.getScreenIndex() == 0) {
                        // HT room 15
                        return false;
                    }
                }
                if(screen.getRoomIndex() == 10) {
                    if(screen.getScreenIndex() == 1) {
                        // HT room 18
                        return false;
                    }
                }
                if(screen.getRoomIndex() == 15) {
                    if(screen.getScreenIndex() == 0 || screen.getScreenIndex() == 1) {
                        // HT room 25 and 26
                        return false;
                    }
                }
                if(screen.getRoomIndex() == 18) {
                    if(screen.getScreenIndex() == 0) {
                        // HT room 27
                        return false;
                    }
                }
                if(screen.getRoomIndex() == 22) {
                    if(screen.getScreenIndex() == 0) {
                        // HT room 35
                        return false;
                    }
                }
            }
            else if(screen.getZoneIndex() == 24) {
                if(enemy.getWriteByteOperations().isEmpty()
                        || (screen.getZoneIndex() == 23 && screen.getRoomIndex() == 1 && screen.getScreenIndex() == 0)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    void addUntrackedCustomPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex == 10) {
            if(roomIndex == 8 && screenIndex == 0) {
                // Mr. Slushfund - 689
                GameObject warp = AddObject.addWarp(screen, 80, 40, 4, 4, 10, 8, 1, 80, 352);

                TestByteOperation warpTest = new TestByteOperation();
                warpTest.setIndex(FlagConstants.SCREEN_FLAG_2E);
                warpTest.setValue((byte)1);
                warpTest.setOp(ByteOp.FLAG_EQUALS);
                warp.getTestByteOperations().add(warpTest);
            }
        }
        else if(zoneIndex == 23) {
            if(Settings.isIncludeHellTempleNPCs()) {
                if(roomIndex == 0) {
                    // Add escape door in place of normal HT to Guidance door
                    AddObject.addCreditsDoor(screen, 300, 320);
                }
                if(roomIndex == 1) {
                    if(screenIndex == 0) {
                        // HT room 1
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);

                        GameObject warp = AddObject.addWarp(screen, 40, 440, 6, 2, 23, 5, 0, 40, 152);
                        warp.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 1));

                        GameObject laserWall = AddObject.addLaserWall(screen, 120, 200, true, 1);
                        laserWall.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 1));
                        laserWall.getTestByteOperations().add(new TestByteOperation(FlagConstants.HT_ROOM1_SHORTCUT_OPEN, ByteOp.FLAG_GT, 0));

                        AddObject.addHTExitDoor(screen);
                    }
                }
                else if(roomIndex == 2) {
                    if(screenIndex == 0 || screenIndex == 1) {
                        // HT rooms 3 and 4
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);
                    }
                }
                else if(roomIndex == 5) {
                    if(screenIndex == 0) {
                        // HT room 7
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);
                    }
                    else if(screenIndex == 1) {
                        // HT room 8
                        GameObject warp = AddObject.addWarp(screen, 120, 920, 26, 2, 23, 5, 0, 40, 152);
                        warp.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 1));
                    }
                }
                else if(roomIndex == 6) {
                    if(screenIndex == 0) {
                        // HT room 10
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);
                    }
                }
                else if(roomIndex == 8) {
                    if(screenIndex == 0) {
                        // HT room 13
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);
                    }
                }
                else if(roomIndex == 9) {
                    if(screenIndex == 0) {
                        // HT room 15
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);
                    }
                }
                else if(roomIndex == 10) {
                    if(screenIndex == 1) {
                        // HT room 18
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);
                    }
                }
                else if(roomIndex == 12) {
                    if(screenIndex == 0) {
                        // HT room 20
                        GameObject warp = AddObject.addWarp(screen, 0, 0, 32, 2, 24, 1, 0, 100, 160);
                        warp.getTestByteOperations().add(new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 1));
                    }
                }
                else if(roomIndex == 15) {
                    if(screenIndex == 0) {
                        // HT room 25
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);
                    }
                    else if(screenIndex == 1) {
                        // HT room 26
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);

                        AddObject.addWarp(screen, 0, 920, 32, 2, 23, 15, 1, 80, 392);
                        AddObject.addWarp(screen, 600, 840, 2, 8, 23, 14, 0, 80, 212);
                    }
                }
                else if(roomIndex == 18) {
                    if(screenIndex == 0) {
                        // HT room 27
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);

                        AddObject.addWarp(screen, 0, 360, 2, 8, 23, 15, 1, 80, 392);
                    }
                }
                else if(roomIndex == 20) {
                    if(screenIndex == 0) {
                        // HT room 30
                        AddObject.addLemezaDetector(screen, 0, 280, 3, 4,
                                Arrays.asList(new TestByteOperation(FlagConstants.SCREEN_FLAG_5, ByteOp.FLAG_EQUALS, 0),
                                        new TestByteOperation(FlagConstants.HT_SOLVED_ROOM30, ByteOp.FLAG_EQUALS, 0),
                                        new TestByteOperation(FlagConstants.ESCAPE, ByteOp.FLAG_EQUALS, 1)),
                                Arrays.asList(new WriteByteOperation(FlagConstants.SCREEN_FLAG_5, ByteOp.ASSIGN_FLAG, 1)));
                        AddObject.addPunchyFist(screen, 0, 300,
                                Arrays.asList(new WriteByteOperation(FlagConstants.SCREEN_FLAG_5, ByteOp.ASSIGN_FLAG, 1),
                                        new WriteByteOperation(FlagConstants.SCREEN_FLAG_5, ByteOp.ASSIGN_FLAG, 0)));
                    }
                }
                else if(roomIndex == 22) {
                    if(screenIndex == 0) {
                        // HT room 35
                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 5, 20);
                        AddObject.addAutosave(screen, 580, 380, 918,
                                Arrays.asList(new TestByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.FLAG_EQUALS, 0)),
                                new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ASSIGN_FLAG, 1));
                    }
                    else if(screenIndex == 1) {
//                        // HT room 34 (The Boss)
//                        AddObject.addGhostLord(screen, 300, 220, 0, 400, 20, 0).getTestByteOperations().add(new TestByteOperation(FlagConstants.THE_BOSS_STATE, ByteOp.FLAG_EQUALS, 2));
//                        AddObject.addLaserWall(screen, 680, 440, false, 30).getTestByteOperations().add(new TestByteOperation(FlagConstants.THE_BOSS_STATE, ByteOp.FLAG_EQUALS, 2));
//                        AddObject.addLaserWall(screen, 1220, 440, false, 30).getTestByteOperations().add(new TestByteOperation(FlagConstants.THE_BOSS_STATE, ByteOp.FLAG_EQUALS, 2));

//                        AddObject.addMovingPlatforms(screen);
//                        AddObject.addStunWitch(screen,720, 400, true).getTestByteOperations().add(new TestByteOperation(FlagConstants.THE_BOSS_STATE, ByteOp.FLAG_EQUALS, 2));
//                        AddObject.addStunWitch(screen,1180, 400, false).getTestByteOperations().add(new TestByteOperation(FlagConstants.THE_BOSS_STATE, ByteOp.FLAG_EQUALS, 2));

//                        AddObject.addAutosave(screen, 1220, 400, new TestByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.FLAG_EQUALS, 0),
//                                new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ASSIGN_FLAG, 1));
                    }
                }
            }
        }
        else if(zoneIndex == 24) {
            if(Settings.isIncludeHellTempleNPCs()) {
                if(roomIndex == 1 && screenIndex == 0) {
//                    int wave1Health = 20;
//                    int wave2Health = 40;
//                    int wave3Health = 40;
//                    int wave4Health = 80;
                    int wave1Health = 30;
                    int wave2Health = 48;
                    int wave3Health = 48;
                    int wave4Health = 100;

                    int wave1Speed = 1;
                    int wave2Speed = 2;
                    int wave3Speed = 2;
                    int wave4Speed = 3;

                    int wave1Damage = 10;
                    int wave2Damage = 5;
                    int wave3Damage = 5;
                    int wave4Damage = 20;

                    int delayPerWave = 5; // Can't go much lower or it'll break the ghosts, probably from too many objects.

                    int fromLeft = 40;
                    int fromTop = 40;
                    int fromBottom = 360;
                    int fromRight= 520;
                    int midWidth = 260;
                    int midHeight = 180;

                    // Top ghosts, wave 1
                    GameObject ghostLord = AddObject.addGhostLord(screen, fromLeft, fromTop, wave1Speed, wave1Health, wave1Damage, 0);
                    ghostLord.getTestByteOperations().add(new TestByteOperation(FlagConstants.HT_ROOM19_SPAWNS, ByteOp.FLAG_EQUALS, 0));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ADD_FLAG, 1));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1));

                    ghostLord = AddObject.addGhostLord(screen, fromRight, fromTop, wave1Speed, wave1Health, wave1Damage, 0);
                    ghostLord.getTestByteOperations().add(new TestByteOperation(FlagConstants.HT_ROOM19_SPAWNS, ByteOp.FLAG_EQUALS, 0));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ADD_FLAG, 1));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1));


                    // Mid ghosts, wave 2
                    AddObject.addSecondsTimer(screen, delayPerWave,
                            Arrays.asList(new TestByteOperation(FlagConstants.HT_ROOM19_SPAWNS, ByteOp.FLAG_EQUALS, 0),
                                    new TestByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.FLAG_EQUALS, 0)),
                            Arrays.asList(new WriteByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.ASSIGN_FLAG, 1)));

                    ghostLord = AddObject.addGhostLord(screen, fromLeft, midHeight, wave2Speed, wave2Health, wave2Damage, 0);
                    ghostLord.getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.FLAG_EQUALS, 1));
                    ghostLord.getTestByteOperations().add(new TestByteOperation(FlagConstants.HT_ROOM19_SPAWNS, ByteOp.FLAG_EQUALS, 0));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ADD_FLAG, 1));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1));

                    ghostLord = AddObject.addGhostLord(screen, fromRight, midHeight, wave2Speed, wave2Health, wave2Damage, 0);
                    ghostLord.getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.FLAG_EQUALS, 1));
                    ghostLord.getTestByteOperations().add(new TestByteOperation(FlagConstants.HT_ROOM19_SPAWNS, ByteOp.FLAG_EQUALS, 0));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ADD_FLAG, 1));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1));

                    // Bottom ghosts, wave 3
                    AddObject.addSecondsTimer(screen, delayPerWave,
                            Arrays.asList(new TestByteOperation(FlagConstants.HT_ROOM19_SPAWNS, ByteOp.FLAG_EQUALS, 0),
                                    new TestByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.FLAG_EQUALS, 1)),
                            Arrays.asList(new WriteByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.ASSIGN_FLAG, 2)));
                    ghostLord = AddObject.addGhostLord(screen, fromLeft, fromBottom, wave3Speed, wave3Health, wave3Damage, 0);
                    ghostLord.getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.FLAG_EQUALS, 2));
                    ghostLord.getTestByteOperations().add(new TestByteOperation(FlagConstants.HT_ROOM19_SPAWNS, ByteOp.FLAG_EQUALS, 0));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ADD_FLAG, 1));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1));

                    ghostLord = AddObject.addGhostLord(screen, fromRight, fromBottom, wave3Speed, wave3Health, wave3Damage, 0);
                    ghostLord.getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.FLAG_EQUALS, 2));
                    ghostLord.getTestByteOperations().add(new TestByteOperation(FlagConstants.HT_ROOM19_SPAWNS, ByteOp.FLAG_EQUALS, 0));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ADD_FLAG, 1));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1));

                    // Center ghost, final wave, true boss
                    AddObject.addSecondsTimer(screen, delayPerWave,
                            Arrays.asList(new TestByteOperation(FlagConstants.HT_ROOM19_SPAWNS, ByteOp.FLAG_EQUALS, 0),
                                    new TestByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.FLAG_EQUALS, 2)),
                            Arrays.asList(new WriteByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.ASSIGN_FLAG, 3)));
                    ghostLord = AddObject.addGhostLord(screen, midWidth, midHeight, wave4Speed, wave4Health, wave4Damage, 352);
                    ghostLord.getTestByteOperations().add(new TestByteOperation(FlagConstants.SCREEN_FLAG_3, ByteOp.FLAG_EQUALS, 3));
                    ghostLord.getTestByteOperations().add(new TestByteOperation(FlagConstants.HT_ROOM19_SPAWNS, ByteOp.FLAG_EQUALS, 0));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.ADD_FLAG, 1));
                    ghostLord.getWriteByteOperations().add(new WriteByteOperation(FlagConstants.SCREEN_FLAG_B, ByteOp.ASSIGN_FLAG, 1));

                    // Timer to trigger end of fight
                    AddObject.addSecondsTimer(screen,
                            0, Arrays.asList(new TestByteOperation(FlagConstants.HT_ROOM19_SPAWNS, ByteOp.FLAG_EQUALS, 0), new TestByteOperation(FlagConstants.SCREEN_FLAG_2, ByteOp.FLAG_EQUALS, 7)),
                            Arrays.asList(new WriteByteOperation(FlagConstants.HT_SOLVED_ROOM19, ByteOp.ASSIGN_FLAG, 1)));

                    // Autosave
                    AddObject.addAutosave(screen, 580, 400, 918,
                            Arrays.asList(new TestByteOperation(FlagConstants.SCREEN_FLAG_4, ByteOp.FLAG_EQUALS, 0)),
                            new WriteByteOperation(FlagConstants.SCREEN_FLAG_4, ByteOp.ASSIGN_FLAG, 1));
                }
            }
        }
    }

    @Override
    void addUntrackedCustomNoPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
        if(zoneIndex == 0) {
            if(roomIndex == 4 && screenIndex == 1) {
                // Priest Zarnac - 674
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xac6);
            }
        }
        else if(zoneIndex == 1) {
            if(roomIndex == 0 && screenIndex == 2) {
                // Hiner - 671
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xac9);
            }
            else if(roomIndex == 2 && screenIndex == 0) {
                // Moger - 672
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xac8);
            }
            else if(roomIndex == 7 && screenIndex == 0) {
                // Former Mekuri Master - 673
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xac7);
            }
        }
        else if(zoneIndex == 2) {
            if(roomIndex == 2 && screenIndex == 0) {
                // Priest Xanado - - 675
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xac5);
            }
        }
        else if(zoneIndex == 3) {
            if(roomIndex == 3 && screenIndex == 0) {
                // Mulbruk
                if(Settings.isIncludeHellTempleNPCs()) {
                    // Timer to set flag for talking about HT
                    AddObject.addSecondsTimer(screen,
                            0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, ByteOp.FLAG_EQUALS, 29),
                                    new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN_MULBRUK_CONVERSATION, ByteOp.FLAG_EQUALS, 1)),
                            Arrays.asList(new WriteByteOperation(FlagConstants.CUSTOM_HALLOWEEN_MULBRUK_CONVERSATION, ByteOp.ASSIGN_FLAG, 2)));
                }
            }
            else if(roomIndex == 4 && screenIndex == 2) {
                // Priest Madomono - 718
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xab0);
            }
        }
        else if(zoneIndex == 4) {
            if(roomIndex == 0 && screenIndex == 1) {
                // Philosopher Giltoriyo - 677
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xac4);
            }
            else if(roomIndex == 6 && screenIndex == 1) {
                // Priest Hidlyda - 678
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xac3);
            }
        }
        else if(zoneIndex == 5) {
            if(roomIndex == 3 && screenIndex == 2) {
                // Priest Romancis - 679
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xac2);
            }
            else if(roomIndex == 2 && screenIndex == 1) {
                // Priest Gailious - 723
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xaaf);
            }
        }
        else if(zoneIndex == 6) {
            if(roomIndex == 6 && screenIndex == 0) {
                // Priest Aramo - 680
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xac1);
            }
            else if(roomIndex == 9 && screenIndex == 1) {
                // Priest Triton - 681
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xac0);
            }
        }
        else if(zoneIndex == 7) {
            if(roomIndex == 10 && screenIndex == 1) {
                // Priest Jaguarfiv - 683
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xabf);
            }
        }
        else if(zoneIndex == 8) {
            if(roomIndex == 1 && screenIndex == 0) {
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES);
            }
        }
        else if(zoneIndex == 10) {
            if(roomIndex == 8 && screenIndex == 0) {
                // Mr. Slushfund - 689
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xabe);
            }
            else if(roomIndex == 8 && screenIndex == 1) {
                // Priest Alest - 693
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xabd);
            }
            else if(roomIndex == 0 && screenIndex == 1) {
                // Stray fairy - 694
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xabc);
            }
            else if(roomIndex == 2 && screenIndex == 2) {
                // duplex - 707
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xab4);
            }
        }
        else if(zoneIndex == 11) {
            if(roomIndex == 7 && screenIndex == 0) {
                // Giant Thexde - 696
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xabb);
            }
        }
        else if(zoneIndex == 12) {
            if(roomIndex == 6 && screenIndex == 0) {
                // Philosopher Alsedana - 698
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xaba);
            }
            else if(roomIndex == 3 && screenIndex == 0) {
                // Samieru - 708
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xab3);
            }
        }
        else if(zoneIndex == 13) {
            if(roomIndex == 5 && screenIndex == 1) {
                // Philosopher Samaranta - 700
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xab9);
            }
            else if(roomIndex == 6 && screenIndex == 3) {
                // Naramura - 709
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xab2);
            }
        }
        else if(zoneIndex == 14) {
            if(roomIndex == 0 && screenIndex == 1) {
                // Priest Laydoc - 701
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xab8);
            }
        }
        else if(zoneIndex == 16) {
            if(roomIndex == 1 && screenIndex == 0) {
                // Priest Ashgine - 702
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xab7);
            }
        }
        else if(zoneIndex == 17) {
            if(roomIndex == 2 && screenIndex == 0) {
                // Fobos - 704
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xab6);
            }
        }
        else if(zoneIndex == 21) {
            if(roomIndex == 0 && screenIndex == 0) {
                // 8bit Elder - 706
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xab5);
            }
        }
        else if(zoneIndex == 20) {
            if(roomIndex == 0 && screenIndex == 1) {
                // 8bit Fairy - 710
                if(!Settings.isIncludeHellTempleNPCs()) {
                    AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_NPC_COUNT, 28);
                }
                AddObject.addNpcConversationTimer(screen, 0xab1);
            }
        }
        else if(zoneIndex == 23) {
            if(Settings.isIncludeHellTempleNPCs()) {
                if(roomIndex == 22) {
                    if(screenIndex == 0) {
                        // Dracuet - 1011
                        AddObject.addEscapeTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_FINAL_DRACUET, 1);
                        AddObject.addNpcConversationTimer(screen, FlagConstants.CUSTOM_HALLOWEEN_FINAL_DRACUET);
                    }
                }
                else if(roomIndex == 12 && screenIndex == 1) {
                    AddObject.addSecondsTimer(screen,
                            0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN_H4, ByteOp.FLAG_GT, 0)),
                            Arrays.asList(new WriteByteOperation(FlagConstants.CUSTOM_HALLOWEEN_H4, ByteOp.ASSIGN_FLAG, 0)));
                }
                else if(roomIndex == 14 && screenIndex == 1) {
                    AddObject.addSecondsTimer(screen,
                            0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN_H4, ByteOp.FLAG_GT, 0)),
                            Arrays.asList(new WriteByteOperation(FlagConstants.CUSTOM_HALLOWEEN_H4, ByteOp.ASSIGN_FLAG, 0)));
                }
                else if(roomIndex == 15 && screenIndex == 1) {
                    AddObject.addSecondsTimer(screen,
                            0, Arrays.asList(new TestByteOperation(FlagConstants.CUSTOM_HALLOWEEN_H4, ByteOp.FLAG_GT, 0)),
                            Arrays.asList(new WriteByteOperation(FlagConstants.CUSTOM_HALLOWEEN_H4, ByteOp.ASSIGN_FLAG, 0)));
                }
            }
        }
    }

    @Override
    void addUntrackedCustomNoPositionZoneObjects(Zone zone) {
    }

    @Override
    void doUntrackedPostUpdates(){
    }

    @Override
    void addTrackedCustomPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
    }

    @Override
    void addTrackedCustomNoPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) {
    }

    @Override
    void doTrackedPostUpdates(){
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

        if(Settings.isIncludeHellTempleNPCs()) {
            if(screen.getZoneIndex() == 23) {
                if(screen.getRoomIndex() == 0) {
                    if(screen.getScreenIndex() == 0) {
                        // Entrance, ban raindrop skip
                        // H1
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 3, 0);
                    }
                }
                else if(screen.getRoomIndex() == 1) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 1, no change
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 2
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 1, 1);
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 1, 1);
                    }
                }
                else if(screen.getRoomIndex() == 2) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 4
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(23, 2, 0);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(23, 2, 0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 3
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(23, 2, 1);

                        // H1
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 3, 0);
                    }
                }
                else if(screen.getRoomIndex() == 3) {
                    if(screen.getScreenIndex() == 0) {
                        // H1
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 1, 1);
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(23, 3, 0);
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 3, 0);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(23, 3, 0);
                    }
                }
                else if(screen.getRoomIndex() == 4) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 6
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 4, 0);

                        // H1
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 3, 0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 5
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 4, 1);
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(23, 2, 0);
                    }
                }
                else if(screen.getRoomIndex() == 5) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 7
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 5, 0);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(23, 5, 0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 8
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 5, 0);
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(23, 5, 0);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(23, 5, 0);
                    }
                }
                else if(screen.getRoomIndex() == 6) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 10
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 6, 1);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(23, 6, 0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 9
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 6, 1);

                        // H2
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 7, 0);
                    }
                }
                else if(screen.getRoomIndex() == 7) {
                    if(screen.getScreenIndex() == 0) {
                        // H2
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 6, 1);
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(23, 7, 0);
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 7, 0);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(23, 7, 0);
                    }
                }
                else if(screen.getRoomIndex() == 8) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 13
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 8, 0);

                        // H2
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 7, 0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 14
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 8, 1);

                        // H3
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 11, 0);
                    }
                }
                else if(screen.getRoomIndex() == 9) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 15
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 9, 0);

                        // H3
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 11, 0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 16
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 9, 0);
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(23, 9, 0);
                    }
                }
                else if(screen.getRoomIndex() == 10) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 17
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 9, 1);
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(23, 9, 1);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(23, 9, 1);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 18
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 10, 0);
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(23, 10, 0);

                        // H3
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 11, 0);
                    }
                }
                else if(screen.getRoomIndex() == 11) {
                    if(screen.getScreenIndex() == 0) {
                        // H3
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 8, 1);
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(23, 11, 0);
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 11, 0);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(23, 11, 0);
                    }
                }
                else if(screen.getRoomIndex() == 12) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 20, no change
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 22
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(23, 12, 1);
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 12, 1);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(23, 12, 1);
                    }
                }
                else if(screen.getRoomIndex() == 13) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 21
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 13, 0);
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(23, 13, 0);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(23, 13, 0);
                    }
                }
                else if(screen.getRoomIndex() == 14) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 24
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 14, 1);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(23, 14, 0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 23
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 14, 1);

                        // H4
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 16, 0);
                    }
                }
                else if(screen.getRoomIndex() == 15) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 25
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 21, 0);
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(23, 14, 0);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(23, 14, 0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 26
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(23, 15, 1);

                        // H4
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 15, 0);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(23, 14, 0);
                    }
                }
                else if(screen.getRoomIndex() == 16) {
                    if(screen.getScreenIndex() == 0) {
                        // H4
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 12, 1);
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(23, 16, 0);
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 16, 0);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(23, 16, 0);
                    }
                }
                else if(screen.getRoomIndex() == 17) {
                    if(screen.getScreenIndex() == 0) {
                        // H4
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 21, 0);
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(23, 17, 0);
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 17, 0);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(23, 17, 0);
                    }
                }
                else if(screen.getRoomIndex() == 18) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 27
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 18, 0);

                        // H5
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 17, 0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 28
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 18, 0);
                    }
                }
                else if(screen.getRoomIndex() == 19) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 29
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(23, 19, 0);
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 19, 0);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(23, 19, 0);
                    }
                }
                else if(screen.getRoomIndex() == 20) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 30
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 20, 0);

                        // H5
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 17, 0);
                    }
                }
                else if(screen.getRoomIndex() == 21) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 33
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(24, 2, 1);
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(24, 2, 1);
                    }
                }
                else if(screen.getRoomIndex() == 22) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 35
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 21, 0);
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 21, 0);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(23, 21, 0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 34
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 21, 0);
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 21, 0);
                    }
                }
            }
            else if(screen.getZoneIndex() == 24) {
                if(screen.getRoomIndex() == 0) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 11
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 6, 0);
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(24, 0, 0);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(24, 0, 0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 12
                        screen.getScreenExit(ScreenExit.UP).setDestination(24, 0, 0);

                        // H2
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 7, 0);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(24, 0, 0);
                    }
                }
                else if(screen.getRoomIndex() == 1) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 19
                        screen.getScreenExit(ScreenExit.UP).setDestination(23, 10, 1);

                        // H3
                        screen.getScreenExit(ScreenExit.DOWN).setDestination(23, 11, 0);
                    }
                }
                else if(screen.getRoomIndex() == 2) {
                    if(screen.getScreenIndex() == 0) {
                        // Room 31
                        screen.getScreenExit(ScreenExit.UP).setDestination(24, 2, 0);
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(24, 2, 0);
                    }
                    else if(screen.getScreenIndex() == 1) {
                        // Room 32
                        screen.getScreenExit(ScreenExit.UP).setDestination(24, 2, 0);
                        screen.getScreenExit(ScreenExit.RIGHT).setDestination(24, 2, 1);
                        screen.getScreenExit(ScreenExit.LEFT).setDestination(24, 2, 1);
                    }
                }
            }
        }
    }
}
