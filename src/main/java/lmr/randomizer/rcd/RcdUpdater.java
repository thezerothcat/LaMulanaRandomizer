package lmr.randomizer.rcd;

import lmr.randomizer.rcd.object.*;

/**
 * Superclass for making updates/additions to the rcd file.
 */
public abstract class RcdUpdater {
    protected RcdData rcdData;

    public RcdUpdater(RcdData rcdData) {
        this.rcdData = rcdData;
    }

    public void updateObjects() {
        for(Zone zone : rcdData.getZones()) {
            int zoneObjIndex = 0;
            while(zoneObjIndex < zone.getObjects().size()) {
                if(updateObject(zone.getObjects().get(zoneObjIndex))) {
                    zone.getObjects().get(zoneObjIndex);
                    ++zoneObjIndex;
                }
                else {
                    zone.getObjects().remove(zoneObjIndex);
                }
            }
            for(Room room : zone.getRooms()) {
                int roomObjIndex = 0;
                while(roomObjIndex < room.getObjects().size()) {
                    if(updateObject(room.getObjects().get(roomObjIndex))) {
                        room.getObjects().get(roomObjIndex);
                        ++roomObjIndex;
                    }
                    else {
                        room.getObjects().remove(roomObjIndex);
                    }
                }
                for(Screen screen : room.getScreens()) {
                    int zoneIndex = screen.getZoneIndex();
                    int roomIndex = screen.getRoomIndex();
                    int screenIndex = screen.getScreenIndex();
                    int screenObjIndex = 0;
                    while(screenObjIndex < screen.getObjects().size()) {
                        if(updateObject(screen.getObjects().get(screenObjIndex))) {
                            screen.getObjects().get(screenObjIndex);
                            ++screenObjIndex;
                        }
                        else {
                            screen.getObjects().remove(screenObjIndex);
                        }
                    }
                    updateScreenExits(screen);
                    addTrackedCustomNoPositionObjects(screen, zoneIndex, roomIndex, screenIndex);
                    addTrackedCustomPositionObjects(screen, zoneIndex, roomIndex, screenIndex);
                }
            }
        }
        doTrackedPostUpdates();
    }

    public void addUntrackedObjects() {
        for(Zone zone : rcdData.getZones()) {
            for(Room room : zone.getRooms()) {
                for(Screen screen : room.getScreens()) {
                    int zoneIndex = screen.getZoneIndex();
                    int roomIndex = screen.getRoomIndex();
                    int screenIndex = screen.getScreenIndex();
                    addUntrackedCustomNoPositionObjects(screen, zoneIndex, roomIndex, screenIndex);
                    addUntrackedCustomPositionObjects(screen, zoneIndex, roomIndex, screenIndex);
                }
            }
            addUntrackedCustomNoPositionZoneObjects(zone);
        }
        doUntrackedPostUpdates();
    }

    /**
     * @param gameObject the object to update
     * @return false if the object should be deleted
     */
    private boolean updateObject(GameObject gameObject) {
        if(gameObject.getId() == ObjectIdConstants.Pot) {
            return updatePot(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Bat) {
            return updateBat(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Skeleton) {
            return updateSkeleton(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Ladder) {
            return updateLadder(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Dais) {
            return updateDais(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.FlagTimer) {
            return updateFlagTimer(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.RoomSpawner) {
            return updateRoomSpawner(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Crusher) {
            return updateCrusher(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Hitbox) {
            return updateHitbox(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.LemezaDetector) {
            return updateLemezaDetector(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Fist) {
            return updateFist(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.GhostSpawner) {
            return updateGhostSpawner(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Steam) {
            return updateSteam(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Sonic) {
            return updateSonic(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Chest) {
            return updateChest(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.WeaponCover) {
            return updateWeaponCover(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Ankh) {
            return updateAnkh(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.FloatingItem) {
            return updateFloatingItem(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Trapdoor) {
            return updateTrapdoor(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Seal) {
            return updateSeal(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Slime) {
            return updateSlime(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.LavaRock) {
            return updateLavaRock(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Spriggan) {
            return updateSpriggan(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Hundun) {
            return updateHundun(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Pan) {
            return updatePan(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Hanuman) {
            return updateHanuman(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Enkidu) {
            return updateEnkidu(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Marchosias) {
            return updateMarchosias(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Witch) {
            return updateWitch(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_LizardMan) {
            return updateLizardMan(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.ChiYou) {
            return updateChiYou(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Toujin) {
            return updateToujin(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_IceWizard) {
            return updateIceWizard(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Anubis) {
            return updateAnubis(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.NinjaSpawner) {
            return updateNinjaSpawner(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Andras) {
            return updateAndras(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.ChonchonSpawner) {
            return updateChonchonSpawner(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Vimana) {
            return updateVimana(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_SwordBird) {
            return updateSwordBird(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Elephant) {
            return updateElephant(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Amon) {
            return updateAmon(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Satan) {
            return updateSatan(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Devil) {
            return updateDevil(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.UmuDabrutu) {
            return updateUmuDabrutu(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Urmahlullu) {
            return updateUrmahlullu(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Mushnahhu) {
            return updateMushnahhu(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Ushum) {
            return updateUshum(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Mushussu) {
            return updateMushussu(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_MiniBoss) {
            return updateMiniBoss(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.TheBoss) {
            return updateTheBoss(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.FairyPoint) {
            return updateFairyPoint(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Fog) {
            return updateFog(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.GraphicsTextureDraw) {
            return updateGraphicsTextureDraw(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.EyeOfRetribution) {
            return updateEyeOfRetribution(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.ExtendableSpikes) {
            return updateExtendableSpikes(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.WarpPortal) {
            return updateWarpPortal(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.WarpDoor) {
            return updateWarpDoor(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.FallingRoom) {
            return updateFallingRoom(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.SoundEffect) {
            return updateSoundEffect(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.UseItemDetector) {
            return updateUseItemDetector(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Scannable) {
            return updateScannable(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Autosave) {
            return updateAutosave(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.ConversationDoor) {
            return updateConversationDoor(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Animation) {
            return updateAnimation(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.KeyFairySpot) {
            return updateKeyFairySpot(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.PushableBlock) {
            return updatePushableBlock(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.BlockButton) {
            return updateBlockButton(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.HotSpring) {
            return updateHotSpring(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Explosion) {
            return updateExplosion(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.ItemGive) {
            return updateItemGive(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.SavePoint) {
            return updateSavePoint(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.GrailToggle) {
            return updateGrailToggle(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.MotherAnkh) {
            return updateMotherAnkh(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.MantraDetector) {
            return updateMantraDetector(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.SnapshotsScan) {
            return updateSnapshotsScan(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.TransitionGate) {
            return updateTransitionGate(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.EscapeTimer) {
            return updateEscapeTimer(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.EscapeScreenShake) {
            return updateEscapeScreenShake(gameObject);
        }
        return true;
    }

    abstract boolean updatePot(GameObject pot);
    abstract boolean updateBat(GameObject bat);
    abstract boolean updateSkeleton(GameObject skeleton);
    abstract boolean updateLadder(GameObject ladder);
    abstract boolean updateDais(GameObject dais);
    abstract boolean updateFlagTimer(GameObject flagTimer);
    abstract boolean updateRoomSpawner(GameObject roomSpawner);
    abstract boolean updateCrusher(GameObject crusher);
    abstract boolean updateHitbox(GameObject hitbox);
    abstract boolean updateLemezaDetector(GameObject lemezaDetector);
    abstract boolean updateFist(GameObject fist);
    abstract boolean updateSteam(GameObject steam);
    abstract boolean updateSonic(GameObject sonic);
    abstract boolean updateGhostSpawner(GameObject ghostSpawner);
    abstract boolean updateChest(GameObject chest);
    abstract boolean updateWeaponCover(GameObject weaponCover);
    abstract boolean updateAnkh(GameObject ankh);
    abstract boolean updateFloatingItem(GameObject floatingItem);
    abstract boolean updateTrapdoor(GameObject trapdoor);
    abstract boolean updateSeal(GameObject seal);
    abstract boolean updateSlime(GameObject slime);
    abstract boolean updateLavaRock(GameObject lavaRock);
    abstract boolean updateSpriggan(GameObject spriggan);
    abstract boolean updateHundun(GameObject hundun);
    abstract boolean updatePan(GameObject pan);
    abstract boolean updateHanuman(GameObject hanuman);
    abstract boolean updateEnkidu(GameObject enkidu);
    abstract boolean updateMarchosias(GameObject marchosias);
    abstract boolean updateWitch(GameObject witch);
    abstract boolean updateLizardMan(GameObject lizardMan);
    abstract boolean updateChiYou(GameObject chiYou);
    abstract boolean updateToujin(GameObject toujin);
    abstract boolean updateIceWizard(GameObject iceWizard);
    abstract boolean updateAnubis(GameObject anubis);
    abstract boolean updateNinjaSpawner(GameObject ninjaSpawner);
    abstract boolean updateAndras(GameObject andras);
    abstract boolean updateChonchonSpawner(GameObject chonchonSpawner);
    abstract boolean updateVimana(GameObject vimana);
    abstract boolean updateSwordBird(GameObject swordBird);
    abstract boolean updateElephant(GameObject elephant);
    abstract boolean updateAmon(GameObject amon);
    abstract boolean updateSatan(GameObject satan);
    abstract boolean updateDevil(GameObject devil);
    abstract boolean updateUmuDabrutu(GameObject umuDabrutu);
    abstract boolean updateUrmahlullu(GameObject urmahlullu);
    abstract boolean updateMushnahhu(GameObject mushnahhu);
    abstract boolean updateUshum(GameObject ushum);
    abstract boolean updateMushussu(GameObject mushussu);
    abstract boolean updateMiniBoss(GameObject miniBoss);
    abstract boolean updateTheBoss(GameObject theBoss);
    abstract boolean updateFairyPoint(GameObject fairyPoint);
    abstract boolean updateFog(GameObject fog);
    abstract boolean updateGraphicsTextureDraw(GameObject graphicsTextureDraw);
    abstract boolean updateEyeOfRetribution(GameObject eyeOfRetribution);
    abstract boolean updateExtendableSpikes(GameObject extendableSpikes);
    abstract boolean updateWarpPortal(GameObject warpPortal);
    abstract boolean updateWarpDoor(GameObject warpDoor);
    abstract boolean updateFallingRoom(GameObject fallingRoom);
    abstract boolean updateSoundEffect(GameObject soundEffect);
    abstract boolean updateUseItemDetector(GameObject useItemDetector);
    abstract boolean updateScannable(GameObject scannable);
    abstract boolean updateAutosave(GameObject autosave);
    abstract boolean updateConversationDoor(GameObject conversationDoor);
    abstract boolean updateAnimation(GameObject animation);
    abstract boolean updateKeyFairySpot(GameObject keyFairySpot);
    abstract boolean updatePushableBlock(GameObject pushableBlock);
    abstract boolean updateBlockButton(GameObject blockButton);
    abstract boolean updateHotSpring(GameObject hotSpring);
    abstract boolean updateExplosion(GameObject explosion);
    abstract boolean updateItemGive(GameObject itemGive);
    abstract boolean updateSavePoint(GameObject savePoint);
    abstract boolean updateGrailToggle(GameObject grailToggle);
    abstract boolean updateMotherAnkh(GameObject motherAnkh);
    abstract boolean updateMantraDetector(GameObject mantraDetector);
    abstract boolean updateSnapshotsScan(GameObject snapshotsScan);
    abstract boolean updateTransitionGate(GameObject transitionGate);
    abstract boolean updateEscapeTimer(GameObject escapeTimer);
    abstract boolean updateEscapeScreenShake(GameObject escapeScreenShake);

    abstract void addUntrackedCustomPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex);

    abstract void addUntrackedCustomNoPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex);

    abstract void addUntrackedCustomNoPositionZoneObjects(Zone zone);

    abstract void doUntrackedPostUpdates();

    abstract void addTrackedCustomPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex);

    abstract void addTrackedCustomNoPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex);

    abstract void doTrackedPostUpdates();

    abstract void updateScreenExits(Screen screen);
}
