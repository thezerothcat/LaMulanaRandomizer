package lmr.randomizer.rcd.updater;

import lmr.randomizer.randomization.data.CustomBlockEnum;
import lmr.randomizer.dat.DatFileData;
import lmr.randomizer.rcd.RcdFileData;
import lmr.randomizer.rcd.object.*;

import java.util.Random;

/**
 * Superclass for making updates/additions to the rcd file.
 */
public abstract class RcdUpdater {
    protected RcdFileData rcdFileData;
    private DatFileData datFileData;

    public RcdUpdater(RcdFileData rcdFileData, DatFileData datFileData) {
        this.rcdFileData = rcdFileData;
        this.datFileData = datFileData;
    }

    protected Short getCustomBlockIndex(CustomBlockEnum customBlockEnum) {
        return datFileData.getCustomBlockIndex(customBlockEnum);
    }

    public void updateObjects() {
        for(Zone zone : rcdFileData.getZones()) {
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
        for(Zone zone : rcdFileData.getZones()) {
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
            return updatePot((Pot)gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Antlion) {
            return updateAntlion(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Bat) {
            return updateBat((Bat)gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Skeleton) {
            return updateSkeleton((Skeleton)gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.TogSpawner) {
            return updateTogSpawner(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Snouter) {
            return updateSnouter(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_KodamaRat) {
            return updateKodamaRat(gameObject);
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
        else if(gameObject.getId() == ObjectIdConstants.CycleTimer) {
            return updateCycleTimer(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.RoomSpawner) {
            return updateRoomSpawner(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.OneWayDoor) {
            return updateOneWayDoor(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Crusher) {
            return updateCrusher(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Hitbox) {
            return updateHitbox((Hitbox)gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.LemezaDetector) {
            return updateLemezaDetector(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Argus) {
            return updateArgus(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Snake) {
            return updateSnake(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Cockatrice) {
            return updateCockatrice(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Condor) {
            return updateCondor(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_MirrorGhosts) {
            return updateMirrorGhosts(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_MaskedMan) {
            return updateMaskedMan(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Nozuchi) {
            return updateNozuchi(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Fist) {
            return updateFist(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.GhostSpawner) {
            return updateGhostSpawner(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.GhostLord) {
            return updateGhostLord(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.RedSkeleton) {
            return updateRedSkeleton(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Steam) {
            return updateSteam(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Sonic) {
            return updateSonic(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_CatBall) {
            return updateCatBall(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Bennu) {
            return updateBennu(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_PharaohHead) {
            return updatePharaohHead(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Buer) {
            return updateBuer(gameObject);
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
        else if(gameObject.getId() == ObjectIdConstants.PressurePlate) {
            return updatePressurePlate((PressurePlate)gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Seal) {
            return updateSeal(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Gyonin) {
            return updateGyonin(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.MrGyonin) {
            return updateMrGyonin(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Hippocamp) {
            return updateHippocamp(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Kraken) {
            return updateKraken(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_WaterLeaper) {
            return updateWaterLeaper(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Nuckelavee) {
            return updateNuckelavee(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_ExplodeRock) {
            return updateExplodeRock(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Slime) {
            return updateSlime(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.LavaRock) {
            return updateLavaRock(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Kakoujuu) {
            return updateKakoujuu(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Pazuzu) {
            return updatePazuzu(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Mandrake) {
            return updateMandrake(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Naga) {
            return updateNaga(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Garuda) {
            return updateGaruda(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Blob) {
            return updateBlob(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Hekatonkheires) {
            return updateHekatonkheires(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Spriggan) {
            return updateSpriggan(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.OxHeadAndHorseFace) {
            return updateOxHeadAndHorseFace(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Bonnacon) {
            return updateBonnacon(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_FlowerFacedSnouter) {
            return updateFlowerFacedSnouter(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Monocoli) {
            return updateMonocoli(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_JiangShi) {
            return updateJiangShi(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_RongXuanwangCorpse) {
            return updateRongXuanwangCorpse(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Backbeard) {
            return updateBackbeard(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.TaiSui) {
            return updateTaiSui(gameObject);
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
        else if(gameObject.getId() == ObjectIdConstants.Beelzebub) {
            return updateBeelzebub(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Witch) {
            return updateWitch(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Siren) {
            return updateSiren(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_XingTian) {
            return updateXingTian(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_ZaoChi) {
            return updateZaoChi(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Leucrotta) {
            return updateLeucrotta(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Peryton) {
            return updatePeryton(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Zu) {
            return updateZu(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_LizardMan) {
            return updateLizardMan(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Asp) {
            return updateAsp(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Kui) {
            return updateKui(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Ba) {
            return updateBa(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.ChiYou) {
            return updateChiYou(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Toujin) {
            return updateToujin(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_DiJiang) {
            return updateDiJiang(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_IceWizard) {
            return updateIceWizard(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Keseran) {
            return updateKeseran(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_BaiZe) {
            return updateBaiZe(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Kamaitachi) {
            return updateKamaitachi(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Anubis) {
            return updateAnubis(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Yowie) {
            return updateYowie(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Troll) {
            return updateTroll(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Anubis) {
            return updateBigAnubis(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.NinjaSpawner) {
            return updateNinjaSpawner(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_ABaoAQu) {
            return updateABaoAQu(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Andras) {
            return updateAndras(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.ChonchonSpawner) {
            return updateChonchonSpawner(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Cyclops) {
            return updateCyclops(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Vimana) {
            return updateVimana(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_BlackDog) {
            return updateBlackDog(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Salamander) {
            return updateSalamander(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Skyfish) {
            return updateSkyfish(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Thunderbird) {
            return updateThunderbird(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Rusalii) {
            return updateRusalii(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Yaksi) {
            return updateYaksi(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Dakini) {
            return updateDakini(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Nuwa) {
            return updateNuwa(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.MudManSpawner) {
            return updateMudManSpawner(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_SwordBird) {
            return updateSwordBird(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Enemy_Elephant) {
            return updateElephant(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Skanda) {
            return updateSkanda(gameObject);
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
        else if(gameObject.getId() == ObjectIdConstants.Ugallu) {
            return updateUgallu(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Kuusarikku) {
            return updateKuusarikku(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Girtablilu) {
            return updateGirtablilu(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Kulullu) {
            return updateKulullu(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Mushnahhu) {
            return updateMushnahhu(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Lahamu) {
            return updateLahamu(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Ushumgallu) {
            return updateUshumgallu(gameObject);
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
            return updateScannable((Scannable)gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.Autosave) {
            return updateAutosave(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.ConversationDoor) {
            return updateConversationDoor(gameObject);
        }
        else if(gameObject.getId() == ObjectIdConstants.FallingSun) {
            return updateFallingSun(gameObject);
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

    boolean updatePot(Pot pot) {
        return true;
    }
    boolean updateAntlion(GameObject antlion) {
        return true;
    }
    boolean updateBat(Bat bat) {
        return true;
    }
    boolean updateSkeleton(Skeleton skeleton) {
        return true;
    }
    boolean updateTogSpawner(GameObject togSpawner) {
        return true;
    }
    boolean updateSnouter(GameObject snouter) {
        return true;
    }
    boolean updateKodamaRat(GameObject kodamaRat) {
        return true;
    }
    boolean updateLadder(GameObject ladder) {
        return true;
    }
    boolean updateDais(GameObject dais) {
        return true;
    }
    boolean updateFlagTimer(GameObject flagTimer) {
        return true;
    }
    boolean updateCycleTimer(GameObject flagTimer) {
        return true;
    }
    boolean updateRoomSpawner(GameObject roomSpawner) {
        return true;
    }
    boolean updateOneWayDoor(GameObject oneWayDoor) {
        return true;
    }
    boolean updateCrusher(GameObject crusher) {
        return true;
    }
    boolean updateHitbox(Hitbox hitbox) {
        return true;
    }
    boolean updateLemezaDetector(GameObject lemezaDetector) {
        return true;
    }
    boolean updateArgus(GameObject argus) {
        return true;
    }
    boolean updateSnake(GameObject snake) {
        return true;
    }
    boolean updateCockatrice(GameObject cockatrice) {
        return true;
    }
    boolean updateCondor(GameObject condor) {
        return true;
    }
    boolean updateMirrorGhosts(GameObject mirrorGhosts) {
        return true;
    }
    boolean updateMaskedMan(GameObject maskedMan) {
        return true;
    }
    boolean updateNozuchi(GameObject nozuchi) {
        return true;
    }
    boolean updateFist(GameObject fist) {
        return true;
    }
    boolean updateGhostSpawner(GameObject ghostSpawner) {
        return true;
    }
    boolean updateGhostLord(GameObject ghostLord) {
        return true;
    }
    boolean updateRedSkeleton(GameObject redSkeleton) {
        return true;
    }
    boolean updateSteam(GameObject steam) {
        return true;
    }
    boolean updateSonic(GameObject sonic) {
        return true;
    }
    boolean updateCatBall(GameObject catBall) {
        return true;
    }
    boolean updateBennu(GameObject bennu) {
        return true;
    }
    boolean updatePharaohHead(GameObject pharaohHead) {
        return true;
    }
    boolean updateBuer(GameObject buer) {
        return true;
    }
    boolean updateChest(GameObject chest) {
        return true;
    }
    boolean updateWeaponCover(GameObject weaponCover) {
        return true;
    }
    boolean updateAnkh(GameObject ankh) {
        return true;
    }
    boolean updateFloatingItem(GameObject floatingItem) {
        return true;
    }
    boolean updateTrapdoor(GameObject trapdoor) {
        return true;
    }
    boolean updatePressurePlate(GameObject pressurePlate) {
        return true;
    }
    boolean updateSeal(GameObject seal) {
        return true;
    }
    boolean updateGyonin(GameObject gyonin) {
        return true;
    }
    boolean updateMrGyonin(GameObject mrGyonin) {
        return true;
    }
    boolean updateHippocamp(GameObject hippocamp) {
        return true;
    }
    boolean updateKraken(GameObject kraken) {
        return true;
    }
    boolean updateWaterLeaper(GameObject waterLeaper) {
        return true;
    }
    boolean updateNuckelavee(GameObject nuckelavee) {
        return true;
    }
    boolean updateExplodeRock(GameObject explodeRock) {
        return true;
    }
    boolean updateSlime(GameObject slime) {
        return true;
    }
    boolean updateLavaRock(GameObject lavaRock) {
        return true;
    }
    boolean updateKakoujuu(GameObject kakoujuu) {
        return true;
    }
    boolean updatePazuzu(GameObject pazuzu) {
        return true;
    }
    boolean updateMandrake(GameObject mandrake) {
        return true;
    }
    boolean updateNaga(GameObject naga) {
        return true;
    }
    boolean updateGaruda(GameObject garuda) {
        return true;
    }
    boolean updateBlob(GameObject blob) {
        return true;
    }
    boolean updateHekatonkheires(GameObject hekatonkheires) {
        return true;
    }
    boolean updateSpriggan(GameObject spriggan) {
        return true;
    }
    boolean updateOxHeadAndHorseFace(GameObject oxHeadAndHorseFace) {
        return true;
    }
    boolean updateBonnacon(GameObject bonnacon) {
        return true;
    }
    boolean updateFlowerFacedSnouter(GameObject flowerFacedSnouter) {
        return true;
    }
    boolean updateMonocoli(GameObject monocoli) {
        return true;
    }
    boolean updateJiangShi(GameObject jiangShi) {
        return true;
    }
    boolean updateRongXuanwangCorpse(GameObject rongXuanwangCorpse) {
        return true;
    }
    boolean updateBackbeard(GameObject backbeard) {
        return true;
    }
    boolean updateTaiSui(GameObject taiSui) {
        return true;
    }
    boolean updateHundun(GameObject hundun) {
        return true;
    }
    boolean updatePan(GameObject pan) {
        return true;
    }
    boolean updateHanuman(GameObject hanuman) {
        return true;
    }
    boolean updateEnkidu(GameObject enkidu) {
        return true;
    }
    boolean updateMarchosias(GameObject marchosias) {
        return true;
    }
    boolean updateBeelzebub(GameObject beelzebub) {
        return true;
    }
    boolean updateWitch(GameObject witch) {
        return true;
    }
    boolean updateSiren(GameObject siren) {
        return true;
    }
    boolean updateXingTian(GameObject xingTian) {
        return true;
    }
    boolean updateZaoChi(GameObject zaoChi) {
        return true;
    }
    boolean updateLeucrotta(GameObject leucrotta) {
        return true;
    }
    boolean updatePeryton(GameObject peryton) {
        return true;
    }
    boolean updateZu(GameObject zu) {
        return true;
    }
    boolean updateLizardMan(GameObject lizardMan) {
        return true;
    }
    boolean updateAsp(GameObject asp) {
        return true;
    }
    boolean updateKui(GameObject kui) {
        return true;
    }
    boolean updateBa(GameObject ba) {
        return true;
    }
    boolean updateChiYou(GameObject chiYou) {
        return true;
    }
    boolean updateToujin(GameObject toujin) {
        return true;
    }
    boolean updateDiJiang(GameObject diJiang) {
        return true;
    }
    boolean updateIceWizard(GameObject iceWizard) {
        return true;
    }
    boolean updateKeseran(GameObject keseran) {
        return true;
    }
    boolean updateBaiZe(GameObject baiZe) {
        return true;
    }
    boolean updateKamaitachi(GameObject baiZe) {
        return true;
    }
    boolean updateAnubis(GameObject anubis) {
        return true;
    }
    boolean updateYowie(GameObject yowie) {
        return true;
    }
    boolean updateTroll(GameObject troll) {
        return true;
    }
    boolean updateBigAnubis(GameObject anubis) {
        return true;
    }
    boolean updateNinjaSpawner(GameObject ninjaSpawner) {
        return true;
    }
    boolean updateABaoAQu(GameObject aBaoAQu) {
        return true;
    }
    boolean updateAndras(GameObject andras) {
        return true;
    }
    boolean updateChonchonSpawner(GameObject chonchonSpawner) {
        return true;
    }
    boolean updateCyclops(GameObject cyclops) {
        return true;
    }
    boolean updateVimana(GameObject vimana) {
        return true;
    }
    boolean updateBlackDog(GameObject blackDog) {
        return true;
    }
    boolean updateSalamander(GameObject salamander) {
        return true;
    }
    boolean updateSkyfish(GameObject salamander) {
        return true;
    }
    boolean updateThunderbird(GameObject thunderbird) {
        return true;
    }
    boolean updateRusalii(GameObject rusalii) {
        return true;
    }
    boolean updateYaksi(GameObject yaksi) {
        return true;
    }
    boolean updateDakini(GameObject dakini) {
        return true;
    }
    boolean updateNuwa(GameObject nuwa) {
        return true;
    }
    boolean updateMudManSpawner(GameObject mudManSpawner) {
        return true;
    }
    boolean updateSwordBird(GameObject swordBird) {
        return true;
    }
    boolean updateElephant(GameObject elephant) {
        return true;
    }
    boolean updateSkanda(GameObject skanda) {
        return true;
    }
    boolean updateAmon(GameObject amon) {
        return true;
    }
    boolean updateSatan(GameObject satan) {
        return true;
    }
    boolean updateDevil(GameObject devil) {
        return true;
    }
    boolean updateUmuDabrutu(GameObject umuDabrutu) {
        return true;
    }
    boolean updateUrmahlullu(GameObject urmahlullu) {
        return true;
    }
    boolean updateUgallu(GameObject ugallu) {
        return true;
    }
    boolean updateKuusarikku(GameObject kuusarikku) {
        return true;
    }
    boolean updateGirtablilu(GameObject girtablilu) {
        return true;
    }
    boolean updateKulullu(GameObject kulullu) {
        return true;
    }
    boolean updateMushnahhu(GameObject mushnahhu) {
        return true;
    }
    boolean updateLahamu(GameObject lahamu) {
        return true;
    }
    boolean updateUshumgallu(GameObject ushumgallu) {
        return true;
    }
    boolean updateUshum(GameObject ushum) {
        return true;
    }
    boolean updateMushussu(GameObject mushussu) {
        return true;
    }
    boolean updateMiniBoss(GameObject miniBoss) {
        return true;
    }
    boolean updateTheBoss(GameObject theBoss) {
        return true;
    }
    boolean updateFairyPoint(GameObject fairyPoint) {
        return true;
    }
    boolean updateFog(GameObject fog) {
        return true;
    }
    boolean updateGraphicsTextureDraw(GameObject graphicsTextureDraw) {
        return true;
    }
    boolean updateEyeOfRetribution(GameObject eyeOfRetribution) {
        return true;
    }
    boolean updateExtendableSpikes(GameObject extendableSpikes) {
        return true;
    }
    boolean updateWarpPortal(GameObject warpPortal) {
        return true;
    }
    boolean updateWarpDoor(GameObject warpDoor) {
        return true;
    }
    boolean updateFallingRoom(GameObject crusher) {
        return true;
    }
    boolean updateSoundEffect(GameObject soundEffect) {
        return true;
    }
    boolean updateUseItemDetector(GameObject useItemDetector) {
        return true;
    }
    boolean updateScannable(Scannable scannable) {
        return true;
    }
    boolean updateAutosave(GameObject autosave) {
        return true;
    }
    boolean updateFallingSun(GameObject fallingSun) {
        return true;
    }
    boolean updateConversationDoor(GameObject conversationDoor) {
        return true;
    }
    boolean updateAnimation(GameObject animation) {
        return true;
    }
    boolean updateKeyFairySpot(GameObject keyFairySpot) {
        return true;
    }
    boolean updatePushableBlock(GameObject pushableBlock) {
        return true;
    }
    boolean updateBlockButton(GameObject blockButton) {
        return true;
    }
    boolean updateHotSpring(GameObject hotSpring) {
        return true;
    }
    boolean updateExplosion(GameObject explosion) {
        return true;
    }
    boolean updateItemGive(GameObject itemGive) {
        return true;
    }
    boolean updateSavePoint(GameObject savePoint) {
        return true;
    }
    boolean updateGrailToggle(GameObject grailToggle) {
        return true;
    }
    boolean updateMotherAnkh(GameObject motherAnkh) {
        return true;
    }
    boolean updateMantraDetector(GameObject mantraDetector) {
        return true;
    }
    boolean updateSnapshotsScan(GameObject snapshotsScan) {
        return true;
    }
    boolean updateTransitionGate(GameObject transitionGate) {
        return true;
    }
    boolean updateEscapeTimer(GameObject escapeTimer) {
        return true;
    }
    boolean updateEscapeScreenShake(GameObject escapeScreenShake) {
        return true;
    }

    void addUntrackedCustomPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) { }

    void addUntrackedCustomNoPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) { }

    void addUntrackedCustomNoPositionZoneObjects(Zone zone) { }

    void doUntrackedPostUpdates() { }

    void addTrackedCustomPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) { }

    void addTrackedCustomNoPositionObjects(Screen screen, int zoneIndex, int roomIndex, int screenIndex) { }

    void doTrackedPostUpdates() { }

    public void trackObject(GameObject gameObject) { }

    public void doShuffleUpdates(Random random) { }

    public void doPostShuffleUpdates() { }

    void updateScreenExits(Screen screen) { }
}
