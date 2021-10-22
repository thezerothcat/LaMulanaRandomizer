package lmr.randomizer.randomization;

import lmr.randomizer.HolidaySettings;
import lmr.randomizer.rcd.object.*;
import lmr.randomizer.util.FlagConstants;
import lmr.randomizer.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EnemyRandomizer {
    private static final List<Integer> ENEMY_HEIGHT_RAISED = Arrays.asList(0x02, 0x1b, 0x21, 0x4c, 0x6e, 0x70, 0x7e, 0x81); // Bats are here for the purpose of flying ones sitting on ceilings - a normal enemy should be placed closer to the ground.
    private static final List<Integer> ENEMY_HEIGHT_LOWERED = Arrays.asList(0x3c, 0x41, 0x59, 0x74);
    private static final List<Integer> ENEMY_HEIGHT_NORMAL = Arrays.asList(0x01, 0x03, 0x05, 0x06, 0x16, 0x17,
            0x1d, 0x27, 0x35, 0x3e, 0x42, 0x48, 0x49, 0x4a, 0x4b, 0x52, 0x56, 0x57, 0x58, 0x5c, 0x5d, 0x5e,
            0x63, 0x64, 0x66, 0x6a, 0x73, 0x82);

    private static final List<Integer> GROUND_ENEMIES = Arrays.asList(0x01, 0x03, 0x05, 0x06, 0x16, 0x17, 0x1c, 0x1d,
            0x21, 0x26, 0x27, 0x35, 0x3c, 0x3e, 0x41, 0x42, 0x48, 0x49, 0x4a, 0x4b, 0x4c, 0x50, 0x51, 0x52, 0x53,
            0x57, 0x58, 0x59, 0x5c, 0x5d, 0x5e, 0x62, 0x63, 0x64, 0x66, 0x6a, 0x6e, 0x70, 0x73, 0x74, 0x7e,
            0x81, 0x82, 0x8f);
    private static final List<Integer> AIR_ENEMIES = Arrays.asList(0x1e, 0x55, 0x56, 0x68, 0x69); // Witch and Siren act more like ground enemies, but are classed as air because other enemies may behave oddly if placed in certain of their locations.
    private static final List<Integer> WATER_ENEMIES = Arrays.asList(0x37, 0x38);
    private static final List<Integer> NO_COLLISION_ENEMIES = Arrays.asList(0x02, 0x18, 0x1b, 0x21, 0x29, 0x3b, 0x43, 0x44,
            0x65, 0x6d, 0x7d);
    private static final List<Integer> SPAWNER_ENEMIES = Arrays.asList(0x1f, 0x6c, 0x7c);
    private static final List<Integer> NO_FLAG_UPDATE_ENEMIES = Arrays.asList(0x06, 0x3b);

    protected Random random;

    public EnemyRandomizer(Random random) {
        this.random = random;
    }

    public void randomizeEnemy(GameObject enemyObject) {
        int zoneIndex = ((Screen)enemyObject.getObjectContainer()).getZoneIndex();
        replaceEnemyParams(enemyObject, getNewEnemyId(enemyObject, zoneIndex), zoneIndex);
    }

    public void modifyAnkh(GameObject ankh) {
        if(ankh.getId() != 0x2e) {
            return;
        }
        setAnkhArgs(ankh);
    }

    protected int getNewEnemyId(GameObject enemyObject, int zoneIndex) {
        int enemyId = (int)enemyObject.getId();
        if(enemyId == ObjectIdConstants.Kuusarikku || enemyId == ObjectIdConstants.Girtablilu || enemyId == ObjectIdConstants.Ushum
                || enemyId == ObjectIdConstants.Mushussu || enemyId == ObjectIdConstants.Hekatonkheires || enemyId == ObjectIdConstants.Buer) {
            return HolidaySettings.isHalloween2019Mode() ? ObjectIdConstants.GhostLord : enemyId; // Miniboss swaps
        }
        if(HolidaySettings.isHalloween2021Mode() && enemyId == ObjectIdConstants.RedSkeleton) {
            return ObjectIdConstants.Enemy_Skeleton;
        }

        if(enemyId == 0x69) {
            return enemyId; // Moonlight bugs can only walk in certain places, so they aren't random yet (except params)
        }
        if(SPAWNER_ENEMIES.contains(enemyId)) {
            // Spawners not random yet.
            return enemyId;
        }
        if(WATER_ENEMIES.contains(enemyId)) {
            // Hippocampus / Sea Horse
            // Jelly
            if(HolidaySettings.isHalloweenMode()) {
                return 0x02;
            }
            return getSpringWaterEnemyId(!enemyObject.getWriteByteOperations().isEmpty());
        }
//        else if(enemyId == 0x44) {
//            return getExtinctionEnemyId(false);
//        }
//        else if(enemyId == 0x4f) {
//            // Hundun / Blue Fire Rock
//            return getShrineEnemyId(false);
//        }
        return getEnemyIdByArea(zoneIndex, isGroundEnemy(enemyObject), isNoCollisionEnemy(enemyObject),
                !enemyObject.getWriteByteOperations().isEmpty());
    }

    public void replaceEnemyParams(GameObject enemy, int newEnemyId, int zoneIndex) {
        if(newEnemyId == 0x01) {
            setMyrmecoleonArgs(enemy);
        }
        else if(newEnemyId == ObjectIdConstants.Enemy_Bat) {
            setBatArgs(enemy, zoneIndex);
        }
        else if(newEnemyId == 0x03) {
            setSkeletonArgs(enemy);
        }
        else if(newEnemyId == 0x05) {
            setSnouterArgs(enemy);
        }
        else if(newEnemyId == 0x06) {
            setKodamaRatArgs(enemy);
        }
        else if(newEnemyId == 0x16) {
            setSurfaceSnakeArgs(enemy);
        }
        else if(newEnemyId == 0x17) {
            setSurfaceBirdArgs(enemy);
        }
        else if(newEnemyId == 0x18) {
            setVultureArgs(enemy);
        }
        else if(newEnemyId == 0x1b) {
            setMirrorGhostArgs(enemy);
        }
        else if(newEnemyId == 0x1c) {
            setMaskedManArgs(enemy);
        }
        else if(newEnemyId == 0x1d) {
            setNozuchiArgs(enemy);
        }
        else if(newEnemyId == 0x1e) {
            setFistArgs(enemy);
        }
        else if(newEnemyId == 0x1f) {
            setGhostSpawnerArgs(enemy);
        }
        else if(newEnemyId == ObjectIdConstants.GhostLord) {
            setGhostLordArgs(enemy);
        }
        else if(newEnemyId == 0x21) {
            setRedSkeletonArgs(enemy);
        }
        else if(newEnemyId == 0x26) {
            setSonicArgs(enemy);
        }
        else if(newEnemyId == 0x27) {
            setCatBallArgs(enemy);
        }
        else if(newEnemyId == 0x28) {
            setSunBirdArgs(enemy);
        }
        else if(newEnemyId == 0x29) {
            setSunMaskArgs(enemy);
        }
        else if(newEnemyId == 0x35) {
            setGyoninArgs(enemy);
        }
        else if(newEnemyId == 0x37) {
            setHippocampusArgs(enemy);
        }
        else if(newEnemyId == 0x38) {
            setJellyArgs(enemy);
        }
        else if(newEnemyId == 0x3e) {
            setKakoujuuArgs(enemy);
        }
        else if(newEnemyId == 0x3b) {
            setExplodeRockArgs(enemy);
        }
        else if(newEnemyId == 0x3c) {
            setJumpSlimeArgs(enemy);
        }
        else if(newEnemyId == 0x41) {
            setMandrakeArgs(enemy);
        }
        else if(newEnemyId == 0x42) {
            setNagaArgs(enemy);
        }
        else if(newEnemyId == 0x43) {
            setGarudaArgs(enemy);
        }
//        else if(newEnemyId == 0x44) {
//            setBlobArgs(enemy);
//        }
        else if(newEnemyId == 0x48) {
            setBonnaconArgs(enemy);
        }
        else if(newEnemyId == 0x49) {
            setFlowerFacedSnouterArgs(enemy);
        }
        else if(newEnemyId == 0x4a) {
            setMonocoliArgs(enemy);
        }
        else if(newEnemyId == 0x4b) {
            setJiangshiArgs(enemy);
        }
        else if(newEnemyId == 0x4c) {
            setRongXuanWangCorpseArgs(enemy);
        }
//        else if(newEnemyId == 0x4f) {
//            setHundunArgs(enemy);
//        }
        else if(newEnemyId == 0x50) {
            setPanArgs(enemy);
        }
        else if(newEnemyId == 0x51) {
            setHanumanArgs(enemy);
        }
        else if(newEnemyId == 0x52) {
            setEnkiduArgs(enemy);
        }
        else if(newEnemyId == 0x53) {
            setMarchosiasArgs(enemy);
        }
        else if(newEnemyId == 0x55) {
            setWitchArgs(enemy, zoneIndex);
        }
        else if(newEnemyId == 0x56) {
            setSirenArgs(enemy);
        }
        else if(newEnemyId == 0x57) {
            // Xingtian / Axe Guy
            setXingtianArgs(enemy);
        }
        else if(newEnemyId == 0x58) {
            // Zaochi / Jump monkey
            setZaochiArgs(enemy);
        }
        else if(newEnemyId == 0x59) {
            // Lizard / leucrotta / gator
            setTwinLizardArgs(enemy);
        }
        else if(newEnemyId == 0x5c) {
            // Illusion Lizard
            setIllusionLizardArgs(enemy);
        }
        else if(newEnemyId == 0x5d) {
            // Asp
            setAspArgs(enemy);
        }
        else if(newEnemyId == 0x5e) {
            // Kui / Illusion Hopper
            setKuiArgs(enemy);
        }
        else if(newEnemyId == 0x62) {
            // Hadouken turtle
            setToujinArgs(enemy);
        }
        else if(newEnemyId == 0x63) {
            // Faceless
            setDijiangArgs(enemy);
        }
        else if(newEnemyId == 0x64) {
            setIceWizardArgs(enemy);
        }
        else if(newEnemyId == 0x65) {
            // Puffball
            setCloudArgs(enemy);
        }
        else if(newEnemyId == 0x66) {
            // Icicle shot / Spiked Dinosaur
            setBaizeArgs(enemy);
        }
        else if(newEnemyId == 0x68) {
            setAnubisArgs(enemy);
        }
        else if(newEnemyId == 0x69) {
            setMoonlightBugArgs(enemy);
        }
        else if(newEnemyId == 0x6a) {
            setTrollArgs(enemy);
        }
        else if(newEnemyId == 0x6c) {
            setNinjaSpawnerArgs(enemy);
        }
        else if(newEnemyId == 0x6d) {
            setABaoAQuArgs(enemy);
        }
        else if(newEnemyId == 0x6e) {
            // Wolf Riding Demon
            setAndrasArgs(enemy);
        }
        else if(newEnemyId == 0x70) {
            setCyclopsArgs(enemy);
        }
        else if(newEnemyId == 0x73) {
            setRuinDogArgs(enemy);
        }
        else if(newEnemyId == 0x74) {
            setSalamanderArgs(enemy);
        }
        else if(newEnemyId == 0x7c) {
            setMudmanSpawnerArgs(enemy);
        }
        else if(newEnemyId == ObjectIdConstants.Enemy_SwordBird) {
            setSwordBirdArgs(enemy);
        }
        else if(newEnemyId == 0x7e) {
            setElephantArgs(enemy);
        }
        else if(newEnemyId == 0x81) {
            setAmonArgs(enemy);
        }
        else if(newEnemyId == 0x82) {
            setDevilCrownSkullArgs(enemy);
        }
        else if(newEnemyId == 0x83) {
            setGargoyleArgs(enemy);
        }
        else if(newEnemyId == 0x8f) {
            // Bomb-throwing Slime
            setMiniBossArgs(enemy);
        }
        adjustEnemyHeight(enemy, newEnemyId);
        enemy.setId((short) newEnemyId);
    }

    private int getEnemyIdByArea(int zoneIndex, boolean includeGround, boolean isNoCollisionEnemy, boolean hasUpdateFlags) {
        if(zoneIndex == 19 || zoneIndex == 20 || zoneIndex == 21) {
            // Special case for retro areas
            List<Integer> enemyIds = getRetroEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags);
            return enemyIds.get(random.nextInt(enemyIds.size()));
        }

        List<Integer> enemyIds = new ArrayList<>();
        if(zoneIndex == 0) {
            enemyIds.addAll(getGuidanceEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 1) {
            enemyIds.addAll(getSurfaceEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSurfaceEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 2) {
            enemyIds.addAll(getMausoleumEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getMausoleumEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 3) {
            enemyIds.addAll(getSunEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSunEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 4) {
            enemyIds.addAll(getSpringEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSpringEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 5) {
            enemyIds.addAll(getInfernoEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getInfernoEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 6) {
            enemyIds.addAll(getExtinctionEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getExtinctionEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 7) {
            enemyIds.addAll(getTwinLabsEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getTwinLabsEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 8) {
            enemyIds.addAll(getEndlessEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getEndlessEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 9) {
            enemyIds.addAll(getShrineEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getShrineEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 10) {
            enemyIds.addAll(getIllusionEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getIllusionEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 11) {
            enemyIds.addAll(getGraveyardEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getGraveyardEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 12) {
            enemyIds.addAll(getMoonlightEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getMoonlightEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 13) {
            enemyIds.addAll(getGoddessEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getGoddessEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 14) {
            enemyIds.addAll(getRuinEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getRuinEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 15 || zoneIndex == 16) {
            enemyIds.addAll(getBirthEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getBirthEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 17) {
            enemyIds.addAll(getDimensionalEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getDimensionalEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 18) {
            enemyIds.addAll(getShrineEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getShrineEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 19) {
            enemyIds.addAll(getRetroEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
            enemyIds.addAll(getRetroEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 20) {
            enemyIds.addAll(getRetroEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
            enemyIds.addAll(getRetroEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 21) {
            enemyIds.addAll(getRetroEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
            enemyIds.addAll(getRetroEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 22) {
            // Night surface
            enemyIds.addAll(getSurfaceEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSurfaceEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        else if(zoneIndex == 23) {
            enemyIds.addAll(getHellTempleEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getHellTempleEnemyIds(includeGround, isNoCollisionEnemy));
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }
        if(enemyIds.isEmpty()) {
            enemyIds.addAll(getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags));
        }

        if(includeGround && !isNoCollisionEnemy && HolidaySettings.isHalloween2021Mode()) {
            while(enemyIds.contains((int)ObjectIdConstants.Enemy_Bat)) {
                enemyIds.remove((Integer)((int)ObjectIdConstants.Enemy_Bat));
            }
            if(enemyIds.isEmpty()) {
                enemyIds.add((int)ObjectIdConstants.Enemy_Bat);
            }
        }
        return enemyIds.get(random.nextInt(enemyIds.size()));
    }

    private List<Integer> getSharedGroundEnemyIds(boolean hasUpdateFlags) {
        List<Integer> enemyIds = new ArrayList<>();
        enemyIds.add(0x03); // Skeleton
        if(!HolidaySettings.isHalloweenMode()) {
            enemyIds.add(0x05); // Snouter
            if(!hasUpdateFlags) {
                enemyIds.add(0x06); // Kodama Rat / Pink exploding rat pig
            }
            enemyIds.add(0x1c); // Masked Man
            enemyIds.add(0x26); // Sonic
            enemyIds.add(0x3c); // Jump Slime
            enemyIds.add(0x41); // Mandrake
            enemyIds.add(0x62); // Toujin / Hadouken turtle
//            enemyIds.add(0x6c); // Ninja spawner
//            enemyIds.add(0x7c); // Mudman spawner
        }
        return enemyIds;
    }

    private List<Integer> getSharedAirEnemyIds() {
        List<Integer> enemyIds = new ArrayList<>();
        if(!HolidaySettings.isHalloweenMode()) {
            enemyIds.add(0x68); // Anubis
        }
        return enemyIds;
    }

    private List<Integer> getSharedNoCollisionEnemyIds(boolean hasUpdateFlags) {
        List<Integer> enemyIds = new ArrayList<>();
        enemyIds.add(0x02); // Bat
        if(!HolidaySettings.isHalloweenMode()) {
            enemyIds.add(0x1b); // Mirror Ghost
            if(!hasUpdateFlags) {
                enemyIds.add(0x3b); // Explode Rock / Mine
            }
        }
        return enemyIds;
    }

    private List<Integer> getSharedEnemyIds(boolean includeGround, boolean isNoCollisionEnemy, boolean hasUpdateFlags) {
        List<Integer> enemyIds = new ArrayList<>();
        enemyIds.addAll(getSharedNoCollisionEnemyIds(hasUpdateFlags));
        if(!isNoCollisionEnemy) {
            enemyIds.addAll(getSharedAirEnemyIds());
            if(includeGround) {
                enemyIds.addAll(getSharedGroundEnemyIds(hasUpdateFlags));
            }
        }
        return enemyIds;
    }

    private List<Integer> getSurfaceEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!HolidaySettings.isHalloweenMode()) {
            enemyIds.add(0x18); // Surface - Vulture
            if(!isNoCollisionEnemy && includeGround) {
                enemyIds.add(0x16); // Surface - Snake
                enemyIds.add(0x17); // Surface - Bird
            }
        }
        if(HolidaySettings.isHalloween2021Mode()) {
            enemyIds.add((int)ObjectIdConstants.Enemy_SwordBird); // Birth - Camio
        }
        return enemyIds;
    }
    
    private List<Integer> getGuidanceEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!isNoCollisionEnemy && includeGround) {
            if(!HolidaySettings.isHalloweenMode()) {
                enemyIds.add(0x01); // Guidance - Myrmecoleon
            }
            if(HolidaySettings.isHalloween2021Mode()) {
                enemyIds.add((int)ObjectIdConstants.Enemy_SwordBird); // Birth - Camio
            }
            else {
                enemyIds.add(0x21); // Guidance - Red Skeleton
            }
        }
        return enemyIds;
    }

    private List<Integer> getMausoleumEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
//        enemyIds.add(0x1f); // Mausoleum Ghosts
//        enemyIds.add(ObjectIdConstants.GhostLord); // Mausoleum - Ghost Lord
        if(!isNoCollisionEnemy) {
            if(!HolidaySettings.isHalloweenMode()) {
                enemyIds.add(0x1e); // Mausoleum - Fist
                if(includeGround) {
                    enemyIds.add(0x1d); // Mausoleum - Nozuchi
                }
            }
        }
        return enemyIds;
    }

    private List<Integer> getSunEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!HolidaySettings.isHalloweenMode()) {
            enemyIds.add(0x29); // Sun - Mask
        }
        if(!isNoCollisionEnemy) {
            if(!HolidaySettings.isHalloweenMode()) {
                enemyIds.add(0x28); // Sun - Bird
            }
            if(includeGround) {
                enemyIds.add(0x27); // Sun - Cait Sith / CatBall
            }
        }
        return enemyIds;
    }

    private List<Integer> getSpringEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!isNoCollisionEnemy && includeGround) {
            if(!HolidaySettings.isHalloweenMode()) {
                enemyIds.add(0x35); // Spring - Gyonin
            }
        }
        return enemyIds;
    }

    private List<Integer> getInfernoEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!isNoCollisionEnemy && includeGround) {
            if(!HolidaySettings.isHalloweenMode()) {
                enemyIds.add(0x3e); // Inferno - Kakoujuu / Fire Lizard
            }
        }
        return enemyIds;
    }

    private List<Integer> getExtinctionEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!HolidaySettings.isHalloweenMode()) {
            enemyIds.add(0x43); // Extinction - Garuda
        }
        if(!isNoCollisionEnemy && includeGround) {
            if(HolidaySettings.isHalloweenMode()) {
                enemyIds.add(0x55); // Twin Labs - Witch
            }
            else {
                enemyIds.add(0x42); // Extinction - Naga
//                enemyIds.add(0x44); // Extinction - Blob
            }
        }
        return enemyIds;
    }

    private List<Integer> getTwinLabsEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!isNoCollisionEnemy) {
            if(includeGround) {
                enemyIds.add(0x55); // Twin Labs - Witch
                if(!HolidaySettings.isHalloweenMode()) {
                    enemyIds.add(0x56); // Twin Labs - Siren // todo: might not be ground only
                    enemyIds.add(0x57); // Twin Labs - Xingtian
                    enemyIds.add(0x58); // Twin Labs - Zaochi
                    enemyIds.add(0x59); // Twin Labs - Lizard / Leucrotta / gator
                }
            }
        }
        return enemyIds;
    }

    private List<Integer> getEndlessEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!isNoCollisionEnemy && includeGround) {
            if(HolidaySettings.isHalloweenMode()) {
                enemyIds.add(0x55); // Twin Labs - Witch
            }
            else {
                enemyIds.add(0x48); // Endless - Bonnacon
                enemyIds.add(0x49); // Endless - Flower-faced snouter
                enemyIds.add(0x4a); // Endless - Monocoli / Baby Snowman
                enemyIds.add(0x4b); // Endless - Jiangshi
                enemyIds.add(0x4c); // Endless - Rongxuanwangcorpse
            }
        }
        return enemyIds;
    }

    private List<Integer> getShrineEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!isNoCollisionEnemy && includeGround) {
            if(HolidaySettings.isHalloweenMode()) {
                enemyIds.add(0x55); // Twin Labs - Witch
            }
            else {
                enemyIds.add(0x50); // Shrine - Pan
                enemyIds.add(0x51); // Shrine - Hanuman
                enemyIds.add(0x52); // Shrine - Enkidu
                enemyIds.add(0x53); // Shrine - Marchosias
            }
        }
        return enemyIds;
    }

    private List<Integer> getIllusionEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!isNoCollisionEnemy && includeGround) {
            if(!HolidaySettings.isHalloweenMode()) {
                enemyIds.add(0x5c); // Illusion - Lizard
                enemyIds.add(0x5d); // Illusion - Asp
                enemyIds.add(0x5e); // Illusion - Kui / Hopper
            }
        }
        return enemyIds;
    }

    private List<Integer> getGraveyardEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!HolidaySettings.isHalloweenMode()) {
            enemyIds.add(0x65); // Graveyard - Cloud / Puffball
        }
        if(!isNoCollisionEnemy && includeGround) {
            if(HolidaySettings.isHalloweenMode()) {
                enemyIds.add(0x27); // Sun - Cait Sith / CatBall
                enemyIds.add(0x55); // Twin Labs - Witch // todo: consider removal
            }
            else {
                enemyIds.add(0x63); // Graveyard - Dijiang / Faceless
                enemyIds.add(0x64); // Graveyard - Ice Wizard
                enemyIds.add(0x66); // Graveyard - Baize / Icicle shot / Spiked Dinosaur
            }
        }
        return enemyIds;
    }

    private List<Integer> getMoonlightEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!HolidaySettings.isHalloweenMode()) {
            if(!isNoCollisionEnemy) {
//                enemyIds.add(0x69); // Moonlight - Bug
                if(includeGround) {
                    enemyIds.add(0x6a); // Moonlight - Troll
                }
            }
        }
        return enemyIds;
    }

    private List<Integer> getGoddessEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!HolidaySettings.isHalloweenMode()) {
            enemyIds.add(0x6d); // Goddess - A Bao A Qu
        }
        if(!isNoCollisionEnemy && includeGround) {
            if(HolidaySettings.isHalloweenMode()) {
                enemyIds.add(0x27); // Sun - Cait Sith / CatBall
                enemyIds.add(0x55); // Twin Labs - Witch // todo: consider removal
            }
            else {
                enemyIds.add(0x6e); // Goddess - Andras
                enemyIds.add(0x70); // Goddess - Cyclops
            }
        }
        return enemyIds;
    }

    private List<Integer> getRuinEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!HolidaySettings.isHalloweenMode()) {
            if(!isNoCollisionEnemy && includeGround) {
                enemyIds.add(0x73); // Ruin - Dog
                enemyIds.add(0x74); // Ruin - Salamander
            }
        }
        return enemyIds;
    }

    private List<Integer> getBirthEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        enemyIds.add(0x7d); // Birth - Sword Bird
        if(!HolidaySettings.isHalloweenMode()) {
            if(!isNoCollisionEnemy && includeGround) {
                enemyIds.add(0x7e); // Birth - Elephant
            }
        }
        return enemyIds;
    }

    private List<Integer> getDimensionalEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!isNoCollisionEnemy) {
            if(HolidaySettings.isHalloweenMode()) {
                if(includeGround) {
                    enemyIds.add(0x55); // Twin Labs - Witch // todo: consider removal
                }
            }
            else {
                enemyIds.add(0x83); // Dimensional - Gargoyle / Satan / Telephone Demon
                if(includeGround) {
                    enemyIds.add(0x81); // Dimensional - Amon / Teleport Demon / Flame Summoner
                    enemyIds.add(0x82); // Dimensional - Devil Crown Skull
                }
            }
        }
        return enemyIds;
    }

    private List<Integer> getRetroEnemyIds(boolean includeGround, boolean isNoCollisionEnemy, boolean hasUpdateFlags) {
        List<Integer> enemyIds = new ArrayList<>();
        enemyIds.add(0x02); // Bat
        if(!HolidaySettings.isHalloweenMode()) {
            enemyIds.add(0x1b); // Mirror Ghost
            if(!hasUpdateFlags) {
                enemyIds.add(0x3b); // Explode Rock / Mine
            }
//            enemyIds.add(0x1f); // Mausoleum Ghosts
        }
        if(!isNoCollisionEnemy) {
            if(!HolidaySettings.isHalloweenMode()) {
                enemyIds.add(0x1e); // Mausoleum - Fist
                enemyIds.add(0x68); // Anubis
            }
            if(includeGround) {
                if(!HolidaySettings.isHalloweenMode()) {
                    enemyIds.add(0x01); // Guidance - Myrmecoleon
                }
                enemyIds.add(0x03); // Skeleton
                if(!HolidaySettings.isHalloweenMode()) {
                    enemyIds.add(0x05); // Snouter
                    if(!hasUpdateFlags) {
                        enemyIds.add(0x06); // Kodama Rat / Pink exploding rat pig
                    }
                    enemyIds.add(0x1c); // Masked Man
                    enemyIds.add(0x1d); // Mausoleum - Nozuchi
                    enemyIds.add(0x26); // Sonic
                    enemyIds.add(0x3c); // Jump Slime
                    enemyIds.add(0x41); // Mandrake
                    enemyIds.add(0x62); // Toujin / Hadouken turtle
                }
            }
        }
        return enemyIds;
    }

    private List<Integer> getHellTempleEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!HolidaySettings.isHalloweenMode()) {
            enemyIds.add(0x7d); // Birth - Sword Bird
        }
        if(!isNoCollisionEnemy) {
            if(!HolidaySettings.isHalloweenMode()) {
                enemyIds.add(0x1e); // Mausoleum - Fist
                enemyIds.add(0x83); // Dimensional - Gargoyle / Satan / Telephone Demon
            }
            if(includeGround) {
                if(!HolidaySettings.isHalloweenMode()) {
                    enemyIds.add(0x50); // Shrine - Pan
                    enemyIds.add(0x51); // Shrine - Hanuman
                    enemyIds.add(0x52); // Shrine - Enkidu
                    enemyIds.add(0x53); // Shrine - Marchosias
                }
                enemyIds.add(0x55); // Twin Labs - Witch
                if(!HolidaySettings.isHalloweenMode()) {
                    enemyIds.add(0x5c); // Illusion - Lizard
                    enemyIds.add(0x64); // Graveyard - Ice Wizard
                    enemyIds.add(0x6e); // Goddess - Andras
                    enemyIds.add(0x7e); // Birth - Elephant
                    enemyIds.add(0x81); // Dimensional - Amon / Teleport Demon / Flame Summoner
                    enemyIds.add(0x82); // Dimensional - Devil Crown Skull
                    enemyIds.add(0x8f); // HT - MiniBoss / Bomb-throwing Slime
                }
            }
        }
        return enemyIds;
    }

    private int getSpringWaterEnemyId(boolean hasUpdateFlags) {
        List<Integer> enemyIds = new ArrayList<>(WATER_ENEMIES);
        enemyIds.addAll(getSharedAirEnemyIds());
        enemyIds.addAll(getSharedNoCollisionEnemyIds(hasUpdateFlags));
        return enemyIds.get(random.nextInt(enemyIds.size()));
    }

    private void setMyrmecoleonArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)(random.nextInt(3) + 2)); // NOTHING
        enemy.getArgs().add((short)(random.nextInt(7) + 2)); // Damage
    }

    protected void setBatArgs(GameObject enemy, int zoneIndex) {
        Integer contactDamage = getContactDamage(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)1); // 0 = start resting / 1 = start flying
        enemy.getArgs().add((short)(random.nextInt(2) + 1)); // Drop type - nothing or coins
        enemy.getArgs().add((short)(random.nextInt(3) + 1)); // UNKNOWN - probably meant to be speed but bugged?

        // Bat type is special - backside bats are invisible in retro areas
        if(zoneIndex == 19 || zoneIndex == 20 || zoneIndex == 21) {
            enemy.getArgs().add((short)0);
        }
        else if(zoneIndex == 7) {
            enemy.getArgs().add((short)random.nextInt(2));
        }
        else if(zoneIndex <= 9) {
            enemy.getArgs().add((short)0);
        }
        else {
            enemy.getArgs().add((short)1);
        }

        enemy.getArgs().add((short)(contactDamage == null
                ? random.nextInt(2) + 2
                : contactDamage)); // Damage
    }

    private void setSkeletonArgs(GameObject enemy) {
        int facing = getFacing(enemy);
        Integer health = getHealth(enemy);
        Integer speedBonus = getSpeedBonus(enemy);
        Integer contactDamage = getContactDamage(enemy);
        Integer projectileDamage = getProjectileDamage(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)(random.nextBoolean() ? 1 : 11)); // Droptype - 1 is coins, 11 for skeleton is either coins or weights
        enemy.getArgs().add((short)(speedBonus == null
                ? random.nextInt(4)
                : Math.min(speedBonus, 3))); // Speed
        enemy.getArgs().add((short)random.nextInt(2)); // Collapsed or standing
        enemy.getArgs().add((short)random.nextInt(3)); // Type
        enemy.getArgs().add((short)(health == null
                ? random.nextInt(11) + 3
                : health)); // Health
        enemy.getArgs().add((short)(contactDamage == null
                ? random.nextInt(5) + 2
                : contactDamage)); // Contact damage
        enemy.getArgs().add((short)(projectileDamage == null
                ? random.nextInt(4) + 2
                : projectileDamage)); // Projectile damage
        enemy.getArgs().add((short)(random.nextInt(6) + 3)); // Soul drop
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Projectile speed
    }

    private void setSnouterArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)1); // Jumping frequency
        enemy.getArgs().add((short)2); // Health
        enemy.getArgs().add((short)2); // Damage
        enemy.getArgs().add((short)3); // Soul
    }

    private void setKodamaRatArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)(random.nextInt(3) + 1)); // Drop type - coins, weight, or shuriken?
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed
        enemy.getArgs().add((short)(random.nextInt(8) + 1)); // Health
        enemy.getArgs().add((short)(random.nextInt(8) + 1)); // Contact Damage
        enemy.getArgs().add((short)(random.nextInt(23) + 2)); // Projectile Damage
        enemy.getArgs().add((short)3); // Soul
    }

    private void setSurfaceSnakeArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)1); // Speed
        enemy.getArgs().add((short)1); // Health
        enemy.getArgs().add((short)1); // Damage
        enemy.getArgs().add((short)2); // Soul
    }

    private void setSurfaceBirdArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)(random.nextInt(2) + 1)); // Drop type - coins or weight
        enemy.getArgs().add((short)1); // Speed
        enemy.getArgs().add((short)1); // Health
        enemy.getArgs().add((short)1); // Contact Damage
        enemy.getArgs().add((short)2); // Swoop Damage
        enemy.getArgs().add((short)3); // Soul
    }

    private void setVultureArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)11); // Drop type - 11 for skeleton is either coins or weights
        enemy.getArgs().add((short)(random.nextInt(3) + 1)); // Speed (while flying)
        enemy.getArgs().add((short)100); // Patrol Distance
        enemy.getArgs().add((short)3); // Health
        enemy.getArgs().add((short)2); // Contact Damage
        enemy.getArgs().add((short)4); // Swoop Damage
        enemy.getArgs().add((short)5); // Soul
    }

    private void setMirrorGhostArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)random.nextInt(2)); // Rotation; 0 = CCW, 1 = CW
        enemy.getArgs().add((short)0); // Drop type - nothing
        enemy.getArgs().add((short)1); // Speed
        enemy.getArgs().add((short)3); // Health
        enemy.getArgs().add((short)2); // Contact Damage
        enemy.getArgs().add((short)3); // Swooping Damage
        enemy.getArgs().add((short)2); // Soul
    }

    private void setMaskedManArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)4); // Drop type - rolling shuriken
        enemy.getArgs().add((short)(random.nextInt(2) + 1)); // Speed
        enemy.getArgs().add((short)(random.nextInt(3) + 3)); // Health
        enemy.getArgs().add((short)3); // Contact Damage
        enemy.getArgs().add((short)3); // Soul
        enemy.getArgs().add((short)3); // Damage to break shield
    }

    private void setNozuchiArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)1); // Speed
        enemy.getArgs().add((short)2); // Health
        enemy.getArgs().add((short)2); // Damage
        enemy.getArgs().add((short)3); // Soul
    }

    private void setFistArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)(random.nextInt(3) + 2)); // Speed
        enemy.getArgs().add((short)(random.nextInt(5) + 6)); // Health
        enemy.getArgs().add((short)(random.nextInt(6) + 3)); // Contact damage
        enemy.getArgs().add((short)(random.nextInt(9) + 8)); // Punch damage
        enemy.getArgs().add((short)(random.nextInt(5) + 2)); // "Multiplies? damage taken while punching"
        enemy.getArgs().add((short)7); // Soul
    }

    private void setGhostSpawnerArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)120); // Spawning period
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Maximum Ghosts
        enemy.getArgs().add((short)0); // UNKNOWN - bugged?
        enemy.getArgs().add((short)(HolidaySettings.isHalloweenMode() ? 1 : 0)); // Speed AND Drop-type
        enemy.getArgs().add((short)1); // Health
        enemy.getArgs().add((short)2); // Damage AND Soul
        enemy.getArgs().add((short)3); // UNKNOWN - bugged?
    }

    private void setGhostLordArgs(GameObject enemy) {
        enemy.getArgs().clear();

        if(enemy.getX() > 640) {
            enemy.setX(enemy.getX() % 640);
        }
        else if(enemy.getX() == 640) {
            enemy.setX(620);
        }

        if(enemy.getX() == 0) {
            enemy.setX(40);
        }

        if(enemy.getY() > 480) {
            enemy.setY(enemy.getY() % 480);
        }
        else if(enemy.getY() == 480) {
            enemy.setY(440);
        }

        if(enemy.getY() == 0) {
            enemy.setY(20);
        }

//        enemy.getArgs().add((short)1); // Drop type - coins
//        enemy.getArgs().add((short)(random.nextInt(10) + 1)); // Amount
//        enemy.getArgs().add((short)(random.nextInt(4) + 1)); // Speed (up to 4 is allowed for this mode)
//        enemy.getArgs().add((short)(random.nextInt(4) + 5)); // Health
//        enemy.getArgs().add((short)(random.nextInt(8) + 3)); // Damage
//        enemy.getArgs().add((short)5); // Soul
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)30); // Amount
        enemy.getArgs().add((short)3); // Speed (up to 4 is allowed for this mode)
        enemy.getArgs().add((short)32); // Health
        enemy.getArgs().add((short)10); // Damage
        enemy.getArgs().add((short)20); // Soul
    }

    private void setRedSkeletonArgs(GameObject enemy) {
        int facing = getFacing(enemy);
        Integer health = getHealth(enemy);
        Integer contactDamage = getContactDamage(enemy);
        Integer projectileDamage = getProjectileDamage(enemy);


        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Speed
        if(HolidaySettings.isHalloweenMode()) {
            enemy.getArgs().add((short)(random.nextBoolean() ? 1 : 11)); // Droptype - 1 is coins, 11 for skeleton is either coins or weights
            enemy.getArgs().add((short)1); // Amount
            enemy.getArgs().add((short)(health == null
                    ? random.nextInt(11) + 3
                    : health)); // Health
            enemy.getArgs().add((short)(contactDamage == null
                    ? random.nextInt(5) + 2
                    : contactDamage)); // Contact damage
            enemy.getArgs().add((short)1); // Projectile Speed
            enemy.getArgs().add((short)1); // Projectile Count
            enemy.getArgs().add((short)(projectileDamage == null
                    ? random.nextInt(4) + 2
                    : projectileDamage)); // Projectile damage
            enemy.getArgs().add((short)(random.nextInt(6) + 3)); // Soul drop
        }
        else {
            enemy.getArgs().add((short)20); // Drop type
            enemy.getArgs().add((short)0); // Amount
            enemy.getArgs().add((short)12); // Health
            enemy.getArgs().add((short)10); // Contact damage
            enemy.getArgs().add((short)1); // Projectile Speed
            enemy.getArgs().add((short)1); // Projectile Count
            enemy.getArgs().add((short)3); // Projectile Damage
            enemy.getArgs().add((short)15); // Soul
        }
    }

    private void setSonicArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)(random.nextInt(3) + 3)); // Health
        enemy.getArgs().add((short)2); // Contact Damage
        enemy.getArgs().add((short)4); // Spindash Damage
        enemy.getArgs().add((short)4); // Soul
    }

    private void setCatBallArgs(GameObject enemy) {
        int facing = getFacing(enemy);
        Integer health = getHealth(enemy);
        Integer speedBonus = getSpeedBonus(enemy);
        Integer contactDamage = getContactDamage(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)random.nextInt(2)); // 0 = start on ball, 1 = start off ball
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)(speedBonus == null
                ? (random.nextBoolean() ? 2 : 4)
                : (speedBonus <= 0 ? 2 : 4))); // Speed
        enemy.getArgs().add((short)1); // Drop type - coins
//        if(health == null) {
            if(Settings.isAutomaticHardmode()) {
                enemy.getArgs().add((short)(random.nextBoolean() && random.nextBoolean() ? 10 : 2)); // Cat Health
            }
            else {
                enemy.getArgs().add((short)2); // Cat Health
            }
//        }
//        else {
//            enemy.getArgs().add(health.shortValue()); // Cat Health
//        }
//        if(contactDamage == null) {
            if(Settings.isAutomaticHardmode()) {
                enemy.getArgs().add((short)(random.nextBoolean() && random.nextBoolean() ? 8 : 2)); // Cat Damage
            }
            else {
                enemy.getArgs().add((short)2); // Cat Damage
            }
//        }
//        else {
//            enemy.getArgs().add(contactDamage.shortValue()); // Cat Damage
//        }
        enemy.getArgs().add((short)3); // Cat Soul
//        if(health == null) {
            if(Settings.isAutomaticHardmode()) {
                enemy.getArgs().add((short)(random.nextBoolean() && random.nextBoolean() ? 16 : 2)); // Ball Health
            }
            else {
                enemy.getArgs().add((short)2); // Ball Health
            }
//        }
//        else {
//            enemy.getArgs().add(health.shortValue()); // Ball Health
//        }
//        if(contactDamage == null) {
            if(Settings.isAutomaticHardmode()) {
                enemy.getArgs().add((short)(random.nextBoolean() && random.nextBoolean() ? 16 : 4)); // Ball Damage
            }
            else {
                enemy.getArgs().add((short)4); // Ball Damage
            }
//        }
//        else {
//            enemy.getArgs().add(contactDamage.shortValue()); // Ball Damage
//        }
        enemy.getArgs().add((short)2); // UNKNOWN
    }

    private void setSunBirdArgs(GameObject enemy) {
        int facing = getFacing(enemy);
        boolean isGroundEnemy = isGroundEnemy(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)(isGroundEnemy ? random.nextInt(2) : 1)); // 0 = standing, 1 = flying
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed
        enemy.getArgs().add((short)6); // Drop type - flares
        enemy.getArgs().add((short)(random.nextInt(3) + 2)); // Health
        enemy.getArgs().add((short)3); // Contact Damage
        enemy.getArgs().add((short)2); // Flame Damage
        enemy.getArgs().add((short)5); // Soul
    }

    private void setSunMaskArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)2); // Horizontal speed
        enemy.getArgs().add((short)11); // "Probably drop-type, but I had trouble getting anything but soul to drop"
        enemy.getArgs().add((short)3); // Health
        enemy.getArgs().add((short)2); // Contact Damage
        enemy.getArgs().add((short)90); // Shot period
        enemy.getArgs().add((short)2); // Projectile Speed
        enemy.getArgs().add((short)2); // Projectile Damage
        enemy.getArgs().add((short)7); // Soul
    }

    private void setGyoninArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)random.nextInt(2)); // 0 = Standard, 1 = IRON PIPE
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)(enemy.getArgs().get(0) == 0 ? 3 : 9)); // Drop type - shuriken for standard gyonin, caltrops for alt gyonin
        enemy.getArgs().add((short)(enemy.getArgs().get(0) == 0 ? 0 : 1)); // Speed
        enemy.getArgs().add((short)(random.nextInt(2) + 1)); // Health
        enemy.getArgs().add((short)2); // Contact damage
        enemy.getArgs().add((short)(random.nextInt(3) + 2)); // Soul
        enemy.getArgs().add((short)0); // Bubble speed
        enemy.getArgs().add((short)2); // Bubble damage
    }

    private void setHippocampusArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)random.nextInt(2)); // AI - 0 = water-walking, 1 = underwater
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)(random.nextInt(2) + 1)); // Speed
        enemy.getArgs().add((short)(random.nextInt(3) + 2)); // Health
        enemy.getArgs().add((short)3); // Damage
        enemy.getArgs().add((short)4); // Soul
    }

    private void setJellyArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)2); // Speed
        enemy.getArgs().add((short)2); // Health
        enemy.getArgs().add((short)3); // Damage
        enemy.getArgs().add((short)3); // Soul
    }

    private void setExplodeRockArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)2); // Drop type - weight
        enemy.getArgs().add((short)(random.nextInt(3) + 1)); // Speed
        enemy.getArgs().add((short)100); // Activation radius
        enemy.getArgs().add((short)(Settings.isAutomaticHardmode() ? 40 : 60)); // Explosion delay
        enemy.getArgs().add((short)3); // UNKNOWN
        enemy.getArgs().add((short)4); // Health
        enemy.getArgs().add((short)3); // *both Soul and Contact Damage
        enemy.getArgs().add((short)10); // Explosion damage
        enemy.getArgs().add((short)10); // UNKNOWN
    }

    private void setJumpSlimeArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)5); // "Probably Drop type, but they don't seem able to drop anything"
        enemy.getArgs().add((short)2); // Speed
        enemy.getArgs().add((short)2); // Health
        enemy.getArgs().add((short)2); // Damage
        enemy.getArgs().add((short)3); // "Probably Soul, but they don't seem able to drop anything"
    }

    private void setKakoujuuArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)2); // Speed
        enemy.getArgs().add((short)120); // Flame spawning period
        enemy.getArgs().add((short)4); // Health
        enemy.getArgs().add((short)3); // Contact Damage
        enemy.getArgs().add((short)5); // Soul
        enemy.getArgs().add((short)2); // Flame Damage
        enemy.getArgs().add((short)180); // How long the flames persist
        enemy.getArgs().add((short)2); // UNKNOWN
    }

    private void setMandrakeArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)2); // Drop type - weight
        enemy.getArgs().add((short)12); // Health
        enemy.getArgs().add((short)8); // Contact damage
        enemy.getArgs().add((short)8); // Soul
    }

    private void setNagaArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)5); // Drop type - earth spear
        enemy.getArgs().add((short)2); // Speed
        enemy.getArgs().add((short)5); // Health
        enemy.getArgs().add((short)3); // Contact damage
        enemy.getArgs().add((short)2); // Projectile speed
        enemy.getArgs().add((short)4); // Projectile damage
        enemy.getArgs().add((short)6); // Soul
    }

    private void setGarudaArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)6); // Drop type - flares
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed
        enemy.getArgs().add((short)4); // Health
        enemy.getArgs().add((short)4); // Contact damage
        enemy.getArgs().add((short)(random.nextInt(3) + 6)); // Kick damage
        enemy.getArgs().add((short)8); // Soul
    }

    private void setBonnaconArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)2); // Speed
        enemy.getArgs().add((short)(random.nextInt(3) + 6)); // Health
        enemy.getArgs().add((short)5); // Contact damage
        enemy.getArgs().add((short)6); // Soul
        enemy.getArgs().add((short)10); // Projectiles beyond the first
        enemy.getArgs().add((short)5); // Projectile period
        enemy.getArgs().add((short)2); // Projectile speed
        enemy.getArgs().add((short)2); // Projectile damage
    }

    private void setFlowerFacedSnouterArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)0); // Facing
        enemy.getArgs().add((short)0); // Drop type - nothing
        enemy.getArgs().add((short)(random.nextInt(8) + 7)); // "Probably broken"
        enemy.getArgs().add((short)6); // Health and contact damage
        enemy.getArgs().add((short)7); // Soul
        enemy.getArgs().add((short)200); // "5 & 6 control timing of volleys, not clear on details"
        enemy.getArgs().add((short)100); // "5 & 6 control timing of volleys, not clear on details"
        enemy.getArgs().add((short)(random.nextInt(3) + 3)); // Number of extra projectiles
        enemy.getArgs().add((short)10); // Projectile period
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Projectile speed
        enemy.getArgs().add((short)2); // Projectile damage
    }

    private void setMonocoliArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed
        enemy.getArgs().add((short)4); // Health
        enemy.getArgs().add((short)3); // Contact damage
        enemy.getArgs().add((short)8); // Stomp damage
        enemy.getArgs().add((short)5); // Soul
    }

    private void setJiangshiArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)3); // Drop type - shuriken
        enemy.getArgs().add((short)(random.nextInt(2) + 1)); // Speed
        enemy.getArgs().add((short)(random.nextInt(3) + 5)); // Health
        enemy.getArgs().add((short)5); // Contact damage
        enemy.getArgs().add((short)6); // Soul
    }

    private void setRongXuanWangCorpseArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)(random.nextInt(2) + 1)); // Speed
        enemy.getArgs().add((short)8); // Health
        enemy.getArgs().add((short)6); // Contact damage
        enemy.getArgs().add((short)10); // "Probably broken"
        enemy.getArgs().add((short)6); // Degen period and Soul
        enemy.getArgs().add((short)1); // Degen damage
    }

    private void setPanArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)3); // Drop type - shuriken
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed
        enemy.getArgs().add((short)8); // Health
        enemy.getArgs().add((short)6); // Contact damage
        enemy.getArgs().add((short)10); // Kick Damage
        enemy.getArgs().add((short)8); // Soul
    }

    private void setHanumanArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)11); // Drop type - 11 for skeleton is either coins or weights
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed
        enemy.getArgs().add((short)(random.nextInt(13) + 12)); // Health
        enemy.getArgs().add((short)(random.nextInt(13) + 8)); // Contact damage
        enemy.getArgs().add((short)12); // Soul
        enemy.getArgs().add((short)(random.nextInt(25) + 16)); // Shock damage
    }

    private void setEnkiduArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)11); // Drop type - 11 for skeleton is either coins or weights
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed
        enemy.getArgs().add((short)14); // Health
        enemy.getArgs().add((short)8); // Contact damage
        enemy.getArgs().add((short)9); // Soul
        enemy.getArgs().add((short)12); // Flame damage
        enemy.getArgs().add((short)(20 * random.nextInt(3) + 160)); // Flame speed/reach
    }

    private void setMarchosiasArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)8); // Drop type - chakram
        enemy.getArgs().add((short)2); // Speed
        enemy.getArgs().add((short)(10 * random.nextInt(2) + 20)); // UNKNOWN
        enemy.getArgs().add((short)12); // Health
        enemy.getArgs().add((short)10); // Contact damage
        enemy.getArgs().add((short)9); // Soul
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Projectile speed
        enemy.getArgs().add((short)6); // Projectile damage
    }

    private void setWitchArgs(GameObject enemy, int zoneIndex) {
        int facing = getFacing(enemy);
        int witchType = getWitchType(zoneIndex);
        boolean isHardmode = Settings.isAutomaticHardmode();
        if(!isHardmode) {
            for(TestByteOperation testByteOperation : enemy.getTestByteOperations()) {
                if(testByteOperation.getIndex() == FlagConstants.HARDMODE
                        && ByteOp.FLAG_EQUALS.equals(testByteOperation.getOp())
                        && testByteOperation.getValue() == 2) {
                    isHardmode = true;
                    break;
                }
            }
        }

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)witchType);

        if(witchType == 0) {
            setLightningWitchArgs(enemy, isHardmode, zoneIndex);
        }
        else if(witchType == 1) {
            setFireWitchArgs(enemy, isHardmode, zoneIndex);
        }
        else if(witchType == 2) {
            setStunWitchArgs(enemy, isHardmode, zoneIndex);
        }
        else if(witchType == 3) {
            setOrbWitchArgs(enemy, isHardmode, zoneIndex);
        }
        else if(witchType == 4) {
            setWhiteWitchArgs(enemy, isHardmode, zoneIndex);
        }
        else {
            setBlackWitchArgs(enemy, isHardmode, zoneIndex);
        }
    }

    private int getWitchType(int zoneIndex) {
        // Type - 0 = pink = lightning, 1 = green = fire, 2 = blue = paralyze, 3 = brown = splitting orb, 4 = white mage, 5+ = black mage
        if(HolidaySettings.isHalloweenMode()) {
            if(zoneIndex == 23) {
                return random.nextInt(2) + 2; // Only allowing 2 or 3 due to ghost graphics collision
            }
            if(zoneIndex == 6) {
                return 1;
            }
            if(zoneIndex == 8) {
                return random.nextInt(2) + 1; // Only allowing 1 or 2 due to ghost graphics collision
            }
            if(zoneIndex == 9 || zoneIndex == 18) {
                return 3;
            }
            if(zoneIndex == 10) {
                return 2;
            }
            if(zoneIndex == 11) {
                return 2;
            }
            if(zoneIndex == 13) {
                return 2;
            }
            if(zoneIndex == 17) {
                return 2;
            }
        }
        return random.nextInt(6);
    }

    private void setLightningWitchArgs(GameObject enemy, boolean isHardmode, int zoneIndex) {
        enemy.getArgs().add((short)3); // Drop type
        if(zoneIndex == 23) {
            enemy.getArgs().add((short)2); // Speed
            enemy.getArgs().add((short)4); // Health
        }
        else {
            enemy.getArgs().add((short)((random.nextInt(3) / 2) + 2)); // Speed
            enemy.getArgs().add((short)(isHardmode
                    ? (random.nextBoolean() ? 10 : 4)
                    : 4)); // Health
        }
        enemy.getArgs().add((short)3); // Contact damage
        enemy.getArgs().add((short)6); // Soul
        enemy.getArgs().add((short)60); // Time between volleys attacks
        if(zoneIndex == 23) {
            enemy.getArgs().add((short)2); // Projectiles per volley
            enemy.getArgs().add((short)20); // Delay after shot
        }
        else {
            enemy.getArgs().add((short)(isHardmode
                    ? (random.nextBoolean() ? 3 : 2)
                    : 2)); // Projectiles per volley
            enemy.getArgs().add((short)(enemy.getArgs().get(8) == 3 ? 40 : 20)); // Delay after shot
        }
        enemy.getArgs().add((short)2); // Projectile speed
        enemy.getArgs().add((short)2); // Secondary projectile speed (first split for witches)
        enemy.getArgs().add((short)2); // Tertiary projectile speed (first split for witches)
        enemy.getArgs().add((short)4); // Initial projectile damage
        enemy.getArgs().add((short)4); // Secondary projectile damage (lingering flame, first split)
        enemy.getArgs().add((short)4); // Tertiary projectile damage (second split)
        if(zoneIndex == 23 && random.nextBoolean() && random.nextBoolean() && random.nextBoolean()) {
            enemy.getArgs().add((short)60); // Initial projectile duration (time to first split)
            enemy.getArgs().add((short)60); // Secondary projectile duration (flame duration, time to second split)
            enemy.getArgs().add((short)60); // Tertiary projectile duration (flame duration, time to second split)
        }
        else {
            enemy.getArgs().add((short)2); // Initial projectile duration (time to first split)
            enemy.getArgs().add((short)2); // Secondary projectile duration (flame duration, time to second split)
            enemy.getArgs().add((short)2); // Tertiary projectile duration (flame duration, time to second split)
        }
        enemy.getArgs().add((short)0); // Crashes the game when changed
    }

    private void setFireWitchArgs(GameObject enemy, boolean isHardmode, int zoneIndex) {
        enemy.getArgs().add((short)4); // Drop type
        if(zoneIndex == 23) {
            enemy.getArgs().add((short)2); // Speed
            enemy.getArgs().add((short)4); // Health
        }
        else {
            enemy.getArgs().add((short)((random.nextInt(3) / 2) + 2)); // Speed
            enemy.getArgs().add((short)(isHardmode
                    ? (random.nextBoolean() ? 8 : 4)
                    : 4)); // Health
        }
        enemy.getArgs().add((short)3); // Contact damage
        enemy.getArgs().add((short)6); // Soul
        enemy.getArgs().add((short)120); // Time between volleys attacks
        if(zoneIndex == 23) {
            enemy.getArgs().add((short)2); // Projectiles per volley
            enemy.getArgs().add((short)10); // Delay after shot
        }
        else {
            if(isHardmode) {
                int[] projectilesOptions = {3, 3, 5, 6};
                int projectilesPerVolley = projectilesOptions[random.nextInt(projectilesOptions.length)];
                enemy.getArgs().add((short)projectilesPerVolley);
                if(projectilesPerVolley == 3) {
                    enemy.getArgs().add((short)30); // Delay after shot
                }
                else if(projectilesPerVolley == 5) {
                    enemy.getArgs().add((short)20); // Delay after shot
                }
                else {
                    enemy.getArgs().add((short)50); // Delay after shot
                }
            }
            else {
                enemy.getArgs().add((short)1); // Projectiles per volley
                enemy.getArgs().add((short)10); // Delay after shot
            }
        }
        enemy.getArgs().add((short)2); // Projectile speed
        enemy.getArgs().add((short)2); // Secondary projectile speed (first split for witches)
        enemy.getArgs().add((short)2); // Tertiary projectile speed (first split for witches)
        enemy.getArgs().add((short)8); // Initial projectile damage
        enemy.getArgs().add((short)8); // Secondary projectile damage (lingering flame, first split)
        enemy.getArgs().add((short)8); // Tertiary projectile damage (second split)
        enemy.getArgs().add((short)2); // Initial projectile duration (time to first split)
        enemy.getArgs().add((short)120); // Secondary projectile duration (flame duration, time to second split)
        enemy.getArgs().add((short)2); // Tertiary projectile duration (flame duration, time to second split)
        enemy.getArgs().add((short)0); // Crashes the game when changed
    }

    private void setStunWitchArgs(GameObject enemy, boolean isHardmode, int zoneIndex) {
        enemy.getArgs().add((short)5); // Drop type
        if(zoneIndex == 23) {
            enemy.getArgs().add((short)2); // Speed
            enemy.getArgs().add((short)4); // Health
        }
        else {
            random.nextInt(3);
            enemy.getArgs().add((short)2); // Speed
//            enemy.getArgs().add((short)((random.nextInt(3) / 2) + 2)); // Speed
            enemy.getArgs().add((short)(isHardmode
                    ? (random.nextInt(3) + 6)
                    : 4)); // Health
        }
        enemy.getArgs().add((short)3); // Contact damage
        enemy.getArgs().add((short)7); // Soul
        if(zoneIndex == 23) {
            enemy.getArgs().add((short)120); // Time between volleys attacks
            enemy.getArgs().add((short)2); // Projectiles per volley
            enemy.getArgs().add((short)20); // Delay after shot
        }
        else if(zoneIndex == 7){
            enemy.getArgs().add((short)120); // Time between volleys attacks
            enemy.getArgs().add((short)(isHardmode
                    ? (random.nextBoolean() && random.nextBoolean() ? 2 : 1)
                    : 1)); // Projectiles per volley
            enemy.getArgs().add((short)(enemy.getArgs().get(8) == 2 ? 50 : 20)); // Delay after shot
        }
        else {
            enemy.getArgs().add((short)180); // Time between volleys attacks
            enemy.getArgs().add((short)(isHardmode
                    ? (random.nextBoolean() && random.nextBoolean() ? 2 : 1)
                    : 1)); // Projectiles per volley
            enemy.getArgs().add((short)(enemy.getArgs().get(8) == 2 ? 50 : 20)); // Delay after shot
        }
        enemy.getArgs().add((short)2); // Projectile speed
        enemy.getArgs().add((short)2); // Secondary projectile speed (first split for witches)
        enemy.getArgs().add((short)2); // Tertiary projectile speed (first split for witches)
        enemy.getArgs().add((short)8); // Initial projectile damage
        enemy.getArgs().add((short)8); // Secondary projectile damage (lingering flame, first split)
        enemy.getArgs().add((short)8); // Tertiary projectile damage (second split)
        enemy.getArgs().add((short)2); // Initial projectile duration (time to first split)
        enemy.getArgs().add((short)150); // Secondary projectile duration (flame duration, time to second split)
        enemy.getArgs().add((short)2); // Tertiary projectile duration (flame duration, time to second split)
        enemy.getArgs().add((short)0); // Crashes the game when changed
    }

    private void setOrbWitchArgs(GameObject enemy, boolean isHardmode, int zoneIndex) {
        enemy.getArgs().add((short)9); // Drop type
        if(zoneIndex == 23) {
            enemy.getArgs().add((short)2); // Speed
            enemy.getArgs().add((short)4); // Health
        }
        else {
            enemy.getArgs().add((short)((random.nextInt(3) / 2) + 2)); // Speed
            enemy.getArgs().add((short)(isHardmode
                    ? (random.nextBoolean() ? 7 : 4)
                    : 4)); // Health
        }
        enemy.getArgs().add((short)3); // Contact damage
        enemy.getArgs().add((short)7); // Soul
        enemy.getArgs().add((short)120); // Time between volleys attacks
        if(zoneIndex == 23) {
            enemy.getArgs().add((short)(isHardmode
                    ? (random.nextBoolean() && random.nextBoolean() ? 2 : 1)
                    : 1)); // Projectiles per volley
            enemy.getArgs().add((short)(enemy.getArgs().get(8) == 2 ? 60 : 20)); // Delay after shot
        }
        else {
            enemy.getArgs().add((short)(isHardmode
                    ? (random.nextBoolean() && random.nextBoolean() ? 3 : 1)
                    : 1)); // Projectiles per volley
            enemy.getArgs().add((short)(enemy.getArgs().get(8) == 3 ? 80 : 20)); // Delay after shot
        }
        enemy.getArgs().add((short)2); // Projectile speed
        enemy.getArgs().add((short)2); // Secondary projectile speed (first split for witches)
        enemy.getArgs().add((short)2); // Tertiary projectile speed (first split for witches)
        enemy.getArgs().add((short)12); // Initial projectile damage
        enemy.getArgs().add((short)12); // Secondary projectile damage (lingering flame, first split)
        enemy.getArgs().add((short)12); // Tertiary projectile damage (second split)
        enemy.getArgs().add((short)60); // Initial projectile duration (time to first split)
        enemy.getArgs().add((short)60); // Secondary projectile duration (flame duration, time to second split)
        enemy.getArgs().add((short)60); // Tertiary projectile duration (flame duration, time to second split)
        enemy.getArgs().add((short)0); // Crashes the game when changed
    }

    private void setWhiteWitchArgs(GameObject enemy, boolean isHardmode, int zoneIndex) {
        enemy.getArgs().add((short)0); // Drop type
        enemy.getArgs().add((short)2); // Speed
        if(zoneIndex == 23) {
            enemy.getArgs().add((short)12); // Health
        }
        else {
            enemy.getArgs().add((short)(HolidaySettings.isHalloweenMode() ? 4 : 8)); // Health
        }
        enemy.getArgs().add((short)6); // Contact damage
        enemy.getArgs().add((short)8); // Soul
        enemy.getArgs().add((short)2); // Time between volleys attacks
        enemy.getArgs().add((short)1); // Projectiles per volley
        enemy.getArgs().add((short)2); // Delay after shot
        enemy.getArgs().add((short)2); // Projectile speed
        enemy.getArgs().add((short)2); // Secondary projectile speed (first split for witches)
        enemy.getArgs().add((short)2); // Tertiary projectile speed (first split for witches)
        if(HolidaySettings.isHalloweenMode()) {
            enemy.getArgs().add((short)8); // Initial projectile damage
            enemy.getArgs().add((short)8); // Secondary projectile damage (lingering flame, first split)
            enemy.getArgs().add((short)8); // Tertiary projectile damage (second split)
        }
        else {
            enemy.getArgs().add((short)24); // Initial projectile damage
            enemy.getArgs().add((short)24); // Secondary projectile damage (lingering flame, first split)
            enemy.getArgs().add((short)24); // Tertiary projectile damage (second split)
        }
        enemy.getArgs().add((short)2); // Initial projectile duration (time to first split)
        enemy.getArgs().add((short)2); // Secondary projectile duration (flame duration, time to second split)
        enemy.getArgs().add((short)2); // Tertiary projectile duration (flame duration, time to second split)
        enemy.getArgs().add((short)0); // Crashes the game when changed
    }

    private void setBlackWitchArgs(GameObject enemy, boolean isHardmode, int zoneIndex) {
        enemy.getArgs().add((short)0); // Drop type
        enemy.getArgs().add((short)2); // Speed
        if(zoneIndex == 23) {
            enemy.getArgs().add((short)(random.nextBoolean() ? 12 : 8)); // Health
        }
        else {
            enemy.getArgs().add((short)(HolidaySettings.isHalloweenMode() ? 4 : 8)); // Health
        }
        enemy.getArgs().add((short)6); // Contact damage
        enemy.getArgs().add((short)8); // Soul
        enemy.getArgs().add((short)2); // Time between volleys attacks
        enemy.getArgs().add((short)1); // Projectiles per volley
        enemy.getArgs().add((short)2); // Delay after shot
        enemy.getArgs().add((short)2); // Projectile speed
        enemy.getArgs().add((short)2); // Secondary projectile speed (first split for witches)
        enemy.getArgs().add((short)2); // Tertiary projectile speed (first split for witches)
        if(HolidaySettings.isHalloweenMode()) {
            enemy.getArgs().add((short)8); // Initial projectile damage
            enemy.getArgs().add((short)8); // Secondary projectile damage (lingering flame, first split)
            enemy.getArgs().add((short)8); // Tertiary projectile damage (second split)
        }
        else {
            enemy.getArgs().add((short)16); // Initial projectile damage
            enemy.getArgs().add((short)16); // Secondary projectile damage (lingering flame, first split)
            enemy.getArgs().add((short)16); // Tertiary projectile damage (second split)
        }
        enemy.getArgs().add((short)2); // Initial projectile duration (time to first split)
        enemy.getArgs().add((short)2); // Secondary projectile duration (flame duration, time to second split)
        enemy.getArgs().add((short)2); // Tertiary projectile duration (flame duration, time to second split)
        enemy.getArgs().add((short)0); // Crashes the game when changed
    }

    private void setSirenArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)2); // Drop type - weight
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed
        enemy.getArgs().add((short)(random.nextInt(5) + 4)); // Health
        enemy.getArgs().add((short)5); // Contact damage
        enemy.getArgs().add((short)8); // Soul
        enemy.getArgs().add((short)2); // Projectile speed
        enemy.getArgs().add((short)3); // Projectile damage
    }

    private void setXingtianArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)9); // Drop type - caltrops
        enemy.getArgs().add((short)random.nextInt(2)); // Speed
        enemy.getArgs().add((short)10); // Health
        enemy.getArgs().add((short)8); // Damage
        enemy.getArgs().add((short)8); // Soul
    }

    private void setZaochiArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)4); // Drop type - rolling shuriken
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed
        enemy.getArgs().add((short)(random.nextInt(5) + 4)); // Health
        enemy.getArgs().add((short)(random.nextInt(2) + 7)); // Damage
        enemy.getArgs().add((short)7); // Soul
    }

    private void setTwinLizardArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)(random.nextInt(3) + 2)); // Speed
        enemy.getArgs().add((short)(random.nextInt(5) + 4)); // Health
        enemy.getArgs().add((short)4); // Damage
        enemy.getArgs().add((short)5); // Soul
    }

    private void setIllusionLizardArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)2); // Drop type - weights
        enemy.getArgs().add((short)(random.nextInt(4) + 1)); // Speed
        enemy.getArgs().add((short)(random.nextInt(13) + 12)); // Health
        enemy.getArgs().add((short)(random.nextInt(7) + 6)); // Contact Damage
        enemy.getArgs().add((short)9); // Soul
        enemy.getArgs().add((short)20); // Shield health
        enemy.getArgs().add((short)(random.nextInt(3) + 1)); // Projectile Speed
        enemy.getArgs().add((short)(random.nextInt(11) + 6)); // Projectile Damage
        enemy.getArgs().add((short)(random.nextInt(17) + 16)); // Slash Damage
    }

    private void setAspArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)1); // Speed
        enemy.getArgs().add((short)3); // Health
        enemy.getArgs().add((short)3); // Contact Damage
        enemy.getArgs().add((short)6); // Leap Damage
        enemy.getArgs().add((short)5); // Soul
    }

    private void setKuiArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)(random.nextInt(2) + 1)); // Speed
        enemy.getArgs().add((short)4); // Health
        enemy.getArgs().add((short)4); // Damage
        enemy.getArgs().add((short)6); // Soul
    }

    private void setToujinArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)11); // Drop type - 11 = "nothing for pots, coins or weights for skeletons"
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed
        enemy.getArgs().add((short)(random.nextInt(15) + 10)); // Health
        enemy.getArgs().add((short)(random.nextInt(9) + 8)); // Contact Damage
        enemy.getArgs().add((short)11); // Soul
        enemy.getArgs().add((short)(random.nextInt(3) + 1)); // Projectile speed
        enemy.getArgs().add((short)(random.nextInt(3) + 3)); // Projectiles per volley
        enemy.getArgs().add((short)(random.nextInt(11) + 10)); // Delay between shots
        enemy.getArgs().add((short)(random.nextInt(9) + 8)); // Projectile Damage
    }

    private void setDijiangArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)2); // Speed
        enemy.getArgs().add((short)14); // Health
        enemy.getArgs().add((short)14); // Damage
        enemy.getArgs().add((short)9); // Soul
    }

    private void setIceWizardArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)0); // Facing
        enemy.getArgs().add((short)11); // Drop type - 11 = "nothing for pots, coins or weights for skeletons"
        enemy.getArgs().add((short)(random.nextInt(2) + 1)); // Speed
        enemy.getArgs().add((short)(random.nextInt(3) + 5)); // Health
        enemy.getArgs().add((short)1); // Contact Damage
        enemy.getArgs().add((short)10); // Soul
        enemy.getArgs().add((short)random.nextInt(2)); // Projectile Speed
        enemy.getArgs().add((short)6); // Projectile Damage
    }

    private void setCloudArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)2); // Speed
        enemy.getArgs().add((short)1); // Health
        enemy.getArgs().add((short)8); // Damage
        enemy.getArgs().add((short)5); // Soul
    }

    private void setBaizeArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed
        enemy.getArgs().add((short)5); // Health
        enemy.getArgs().add((short)6); // Contact Damage
        enemy.getArgs().add((short)7); // Soul
        enemy.getArgs().add((short)(60 * random.nextInt(3) + 60)); // "Minimum delay between icicle shots?"
        enemy.getArgs().add((short)(random.nextInt(2) + 1)); // Projectile Speed
        enemy.getArgs().add((short)3); // Projectile Damage
    }

    private void setAnubisArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)11); // Drop type - 11 = "nothing for pots, coins or weights for skeletons"
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed
        enemy.getArgs().add((short)8); // Health
        enemy.getArgs().add((short)6); // Contact Damage
        enemy.getArgs().add((short)9); // Soul
        enemy.getArgs().add((short)2); // Drain rate (frames per tick)
        enemy.getArgs().add((short)1); // Drain tick damage
    }

    private void setMoonlightBugArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)random.nextInt(4)); // Facing - 0 = UL, 1 = DL, 2 = UR, 3 = DR
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)random.nextInt(2)); // Speed
        enemy.getArgs().add((short)3); // Health
        enemy.getArgs().add((short)3); // Contact Damage
        enemy.getArgs().add((short)4); // Soul
    }

    private void setTrollArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)2); // Speed
        enemy.getArgs().add((short)10); // Health
        enemy.getArgs().add((short)6); // Contact Damage
        enemy.getArgs().add((short)7); // Soul
        enemy.getArgs().add((short)random.nextInt(2)); // 0 = Spawns walkling, 1 = Spawns rolling
        enemy.getArgs().add((short)10); // Rolling Damage
    }

    private void setABaoAQuArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)11); // Drop type - 11 = "nothing for pots, coins or weights for skeletons"
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed
        enemy.getArgs().add((short)6); // Health
        enemy.getArgs().add((short)3); // Damage
        enemy.getArgs().add((short)9); // Soul
    }

    private void setAndrasArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)8); // Rider Drop type - chakram
        enemy.getArgs().add((short)2); // Rider Speed
        enemy.getArgs().add((short)6); // Rider Health
        enemy.getArgs().add((short)6); // Rider Contact Damage
        enemy.getArgs().add((short)10); // Rider Soul
        enemy.getArgs().add((short)10); // Rider Projectile Damage
        enemy.getArgs().add((short)(random.nextInt(2) + 3)); // Wolf Speed
        enemy.getArgs().add((short)4); // Wolf Health
        enemy.getArgs().add((short)8); // Wolf Damage
        enemy.getArgs().add((short)6); // UNKNOWN
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Rider Projectile speed
    }

    private void setCyclopsArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)(random.nextInt(3) + 1)); // Speed
        enemy.getArgs().add((short)(random.nextInt(11) + 12)); // Health
        enemy.getArgs().add((short)16); // Contact Damage
        enemy.getArgs().add((short)11); // Soul
        enemy.getArgs().add((short)2); // Eye Speed
        enemy.getArgs().add((short)6); // Eye Health
        enemy.getArgs().add((short)10); // UNKNOWN
    }

    private void setRuinDogArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)2); // Speed
        enemy.getArgs().add((short)(random.nextInt(3) + 5)); // Health
        enemy.getArgs().add((short)6); // Damage
        enemy.getArgs().add((short)6); // Soul
    }

    private void setSalamanderArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)5); // Drop type - earth spear
        enemy.getArgs().add((short)2); // Speed
        enemy.getArgs().add((short)4); // Health
        enemy.getArgs().add((short)6); // Contact Damage
        enemy.getArgs().add((short)7); // Soul
        enemy.getArgs().add((short)random.nextInt(3)); // UNKNOWN
        enemy.getArgs().add((short)2); // Flame Health
        enemy.getArgs().add((short)4); // Flame Damage
        enemy.getArgs().add((short)2); // Flame Soul
    }

    private void setMudmanSpawnerArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)180); // UNKNOWN
        enemy.getArgs().add((short)3); // Max on screen
        enemy.getArgs().add((short)0); // UNKNOWN
        enemy.getArgs().add((short)1); // Drop type
        enemy.getArgs().add((short)(random.nextInt(2) + 1)); // UNKNOWN
        enemy.getArgs().add((short)8); // Health
        enemy.getArgs().add((short)7); // Contact Damage
        enemy.getArgs().add((short)7); // UNKNOWN
    }

    private void setSwordBirdArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)random.nextInt(2)); // Facing?
        enemy.getArgs().add((short)2); // Drop type
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed?
        enemy.getArgs().add((short)4); // Health
        enemy.getArgs().add((short)8); // Contact damage
        enemy.getArgs().add((short)6); // UNKNOWN
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // UNKNOWN
        enemy.getArgs().add((short)6); // Projectile damage
    }

    private void setElephantArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)random.nextInt(2)); // Facing?
        enemy.getArgs().add((short)11); // Drop type
        enemy.getArgs().add((short)(random.nextInt(4) + 1)); // Speed?
        enemy.getArgs().add((short)30); // Health
        enemy.getArgs().add((short)10); // Contact damage
        enemy.getArgs().add((short)10); // UNKNOWN
        enemy.getArgs().add((short)36); // UNKNOWN
        enemy.getArgs().add((short)20); // Stomp damage
    }

    private void setAmonArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)random.nextInt(2)); // Facing?
        enemy.getArgs().add((short)11); // Drop type
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed?
        enemy.getArgs().add((short)15); // Health
        enemy.getArgs().add((short)10); // Contact damage
        enemy.getArgs().add((short)11); // UNKNOWN
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Seems like something to do with projectile acceleration or tracking ability, at high numbers it speeds up after a while and gets really hard to dodge
        enemy.getArgs().add((short)(random.nextInt(3) + 2)); // Projectile health
        enemy.getArgs().add((short)10); // Projectile damage
        enemy.getArgs().add((short)3); // UNKNOWN
    }

    private void setDevilCrownSkullArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)0); // UNKNOWN
        enemy.getArgs().add((short)0); // Drop type
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed?
        enemy.getArgs().add((short)18); // Health
        enemy.getArgs().add((short)16); // Contact damage
        enemy.getArgs().add((short)7); // UNKNOWN
    }

    private void setGargoyleArgs(GameObject enemy) {
        int facing = getFacing(enemy);
        boolean isGroundEnemy = isGroundEnemy(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing); // 0 = left, 1 = right
        enemy.getArgs().add((short)11); // Drop type
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed?
        enemy.getArgs().add((short)10); // Health
        enemy.getArgs().add((short)8); // Contact damage
        enemy.getArgs().add((short)8); // UNKNOWN
        enemy.getArgs().add((short)16); // UNKNOWN
        enemy.getArgs().add((short)(random.nextInt(2) + 1)); // Laser projectile damage
        enemy.getArgs().add((short)4); // UNKNOWN
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // UNKNOWN
        enemy.getArgs().add((short)(isGroundEnemy ? random.nextInt(2) : 1)); // 0 = start standing, 1 = start flying
    }

    private void setMiniBossArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)random.nextInt(2)); // Facing?
        enemy.getArgs().add((short)1); // Drop type
        enemy.getArgs().add((short)2); // UNKNOWN
        enemy.getArgs().add((short)10); // Health
        enemy.getArgs().add((short)6); // Contact Damage
        enemy.getArgs().add((short)7); // UNKNOWN
        enemy.getArgs().add((short)2); // UNKNOWN
        enemy.getArgs().add((short)300); // UNKNOWN
        enemy.getArgs().add((short)60); // UNKNOWN
        enemy.getArgs().add((short)16); // Bomb explosion damage
        enemy.getArgs().add((short)0); // UNKNOWN
    }

    private void setAnkhArgs(GameObject ankh) {
        // Arg 0 is boss, vanilla uses 0-7, 8 (mom) is valid.
        if(ankh.getArgs().get(0) == 7) {
            setTiamatArgs(ankh);
        }
    }

    private void setTiamatArgs(GameObject ankh) {
        ankh.getArgs().clear();
        if(Settings.isAutomaticHardmode()) {
            // Hard mode Tiamat
            ankh.getArgs().set(1, (short)3); // Speed
            ankh.getArgs().set(2, (short)300); // Health
            ankh.getArgs().set(3, (short)64); // Contact damage
            ankh.getArgs().set(4, (short)1); // Red Fireball speed
            ankh.getArgs().set(5, (short)24); // Red Fireball damage
            ankh.getArgs().set(6, (short)2); // Blue Fireball speed
            ankh.getArgs().set(7, (short)32); // Blue Fireball damage
            ankh.getArgs().set(8, (short)2); // Purple Fireball speed
            ankh.getArgs().set(9, (short)48); // Purple Fireball damage
            ankh.getArgs().set(10, (short)180); // Waterfall damage
            ankh.getArgs().set(11, (short)200); // Big Laser damage
            ankh.getArgs().set(12, (short)24); // Green Laser damage
            ankh.getArgs().set(13, (short)55); // Laser duration/length
            ankh.getArgs().set(14, (short)3); // Purple spray proj speed
            ankh.getArgs().set(15, (short)16); // Purple spray damage
            ankh.getArgs().set(16, (short)260); // 2nd phase health threshold
            ankh.getArgs().set(17, (short)160); // 3rd phase health threshold
            ankh.getArgs().set(18, (short)55); // Flag to set at beginning of fight (removes red glowing circles)
            ankh.getArgs().set(19, (short)1); // Flag value
            ankh.getArgs().set(20, (short)61); // Flag that makes the doors move
            ankh.getArgs().set(21, (short)64); // Flag that makes Tiamat spawn
            ankh.getArgs().set(22, (short)0);
            ankh.getArgs().set(23, (short)0);
        }
        else {
            // Normal mode Tiamat
            ankh.getArgs().set(1, (short)2); // Speed
            ankh.getArgs().set(2, (short)260); // Health
            ankh.getArgs().set(3, (short)32); // Contact damage
            ankh.getArgs().set(4, (short)0); // Red Fireball speed
            ankh.getArgs().set(5, (short)12); // Red Fireball damage
            ankh.getArgs().set(6, (short)2); // Blue Fireball speed
            ankh.getArgs().set(7, (short)24); // Blue Fireball damage
            ankh.getArgs().set(8, (short)1); // Purple Fireball speed
            ankh.getArgs().set(9, (short)32); // Purple Fireball damage
            ankh.getArgs().set(10, (short)120); // Waterfall damage
            ankh.getArgs().set(11, (short)150); // Big Laser damage
            ankh.getArgs().set(12, (short)12); // Green Laser damage
            ankh.getArgs().set(13, (short)70); // Laser duration/length
            ankh.getArgs().set(14, (short)3); // Purple spray proj speed
            ankh.getArgs().set(15, (short)16); // Purple spray damage
            ankh.getArgs().set(16, (short)200); // 2nd phase health threshold
            ankh.getArgs().set(17, (short)100); // 3rd phase health threshold
            ankh.getArgs().set(18, (short)55); // Flag to set at beginning of fight (removes red glowing circles)
            ankh.getArgs().set(19, (short)1); // Flag value
            ankh.getArgs().set(20, (short)61); // Flag that makes the doors move
            ankh.getArgs().set(21, (short)64); // Flag that makes Tiamat spawn
            ankh.getArgs().set(22, (short)0);
            ankh.getArgs().set(23, (short)0);
        }
    }

    private boolean isGroundEnemy(GameObject enemyObject) {
        int enemyId = (int)enemyObject.getId();
        if(enemyId == 0x28) {
            // Sun Bird
            return enemyObject.getArgs().get(0) == 0;
        }
        if(enemyId == 0x83) {
            // Gargoyle / Satan / Telephone Demon
            return enemyObject.getArgs().get(10) == 0;
        }
        return GROUND_ENEMIES.contains(enemyId);
    }

    private boolean isNoCollisionEnemy(GameObject enemyObject) {
        int enemyId = (int)enemyObject.getId();
        if(enemyId == 0x02) {
            // Bat
            return enemyObject.getArgs().get(0) != 0;
        }
        if(enemyId == 0x28) {
            // Sun Bird
            return enemyObject.getArgs().get(0) != 0;
        }
        return NO_COLLISION_ENEMIES.contains(enemyId);
    }

    private int getFacing(GameObject enemy) {
        int originalEnemyId = enemy.getId();
        if(originalEnemyId == 0x01) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x03) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x05) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x06) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x16) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x17) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x18) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x1c) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x1d) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x1e) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x21) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x26) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x27) {
            return enemy.getArgs().get(1);
        }
        if(originalEnemyId == 0x28) {
            return enemy.getArgs().get(1);
        }
        if(originalEnemyId == 0x35) {
            return enemy.getArgs().get(1);
        }
        if(originalEnemyId == 0x37) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x3c) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x3e) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x42) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x43) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x48) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x4a) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x4b) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x4c) {
            return enemy.getArgs().get(0);
        }
//        if(originalEnemyId == 0x4f) {
//            return enemy.getArgs().get(0);
//        }
        if(originalEnemyId == 0x50) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x51) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x52) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x53) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x55) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x56) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x57) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x58) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x59) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x5c) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x5d) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x5e) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x62) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x63) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x65) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x66) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x68) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x6a) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x6d) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x6e) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x70) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x73) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == 0x74) {
            return enemy.getArgs().get(0);
        }
        return (short)random.nextInt(2);
    }

    private Integer getHealth(GameObject enemy) {
        int originalEnemyId = enemy.getId();
        if(originalEnemyId == 0x01) {
            return 1;
        }
        if(originalEnemyId == 0x02) {
            return 1;
        }
        if(originalEnemyId == 0x03) {
            return (int)enemy.getArgs().get(5);
        }
        if(originalEnemyId == 0x05) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x06) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x16) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x17) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x18) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x1b) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x1c) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x1d) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x1e) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x21) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x26) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x27) {
            return random.nextBoolean()
                    ? (int)enemy.getArgs().get(4)
                    : (int)enemy.getArgs().get(7);
        }
        if(originalEnemyId == 0x28) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x29) {
            return (int)enemy.getArgs().get(2);
        }
        if(originalEnemyId == 0x35) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x37) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x38) {
            return (int)enemy.getArgs().get(2);
        }
        if(originalEnemyId == 0x39) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x3b) {
            return (int)enemy.getArgs().get(5);
        }
        if(originalEnemyId == 0x3c) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x3e) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x41) {
            return (int)enemy.getArgs().get(1);
        }
        if(originalEnemyId == 0x42) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x43) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x44) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x48) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x49) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x4a) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x4b) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x4c) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x4f) {
            return random.nextBoolean()
                    ? (int)enemy.getArgs().get(3)
                    : (int)enemy.getArgs().get(6);
        }
        if(originalEnemyId == 0x50) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x51) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x52) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x53) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x55) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x56) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x57) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x58) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x59) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x5c) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x5d) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x5e) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x62) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x63) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x64) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x65) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x66) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x68) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x69) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x6a) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x6c) {
            return (int)enemy.getArgs().get(5);
        }
        if(originalEnemyId == 0x6d) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x6e) {
            return random.nextBoolean()
                    ? (int)enemy.getArgs().get(3)
                    : (int)enemy.getArgs().get(8);
        }
        if(originalEnemyId == 0x70) {
            return random.nextBoolean()
                    ? (int)enemy.getArgs().get(3)
                    : (int)enemy.getArgs().get(7);
        }
        if(originalEnemyId == 0x73) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x74) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x7d) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x7e) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x81) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x82) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x83) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x8f) {
            return (int)enemy.getArgs().get(3);
        }
        return null;
    }

    private Integer getContactDamage(GameObject enemy) {
        int originalEnemyId = enemy.getId();
        if(originalEnemyId == 0x01) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x02) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x03) {
            return (int)enemy.getArgs().get(6);
        }
        if(originalEnemyId == 0x05) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x06) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x16) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x17) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x18) {
            return (int)enemy.getArgs().get(5);
        }
        if(originalEnemyId == 0x1b) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x1c) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x1d) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x1e) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x21) {
            return (int)enemy.getArgs().get(5);
        }
        if(originalEnemyId == 0x26) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x27) {
            return random.nextBoolean()
                    ? (int)enemy.getArgs().get(5)
                    : (int)enemy.getArgs().get(8);
        }
        if(originalEnemyId == 0x28) {
            return (int)enemy.getArgs().get(5);
        }
        if(originalEnemyId == 0x29) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x35) {
            return (int)enemy.getArgs().get(5);
        }
        if(originalEnemyId == 0x37) {
            return (int)enemy.getArgs().get(5);
        }
        if(originalEnemyId == 0x38) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x39) {
            return (int)enemy.getArgs().get(5);
        }
        if(originalEnemyId == 0x3b) {
            return (int)enemy.getArgs().get(6);
        }
        if(originalEnemyId == 0x3c) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x3e) {
            return (int)enemy.getArgs().get(5);
        }
        if(originalEnemyId == 0x41) {
            return (int)enemy.getArgs().get(2);
        }
        if(originalEnemyId == 0x42) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x43) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x44) {
            return (int)enemy.getArgs().get(5);
        }
        if(originalEnemyId == 0x48) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x49) {
            // Basing this on projectile speed
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x4a) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x4b) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x4c) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x4f) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x50) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x51) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x52) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x53) {
            return (int)enemy.getArgs().get(5);
        }
        if(originalEnemyId == 0x55) {
            return (int)enemy.getArgs().get(5);
        }
        if(originalEnemyId == 0x56) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x57) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x58) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x59) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x5c) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x5d) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x5e) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x62) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x63) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x64) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x65) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x66) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x68) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x69) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x6a) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x6c) {
            return (int)enemy.getArgs().get(8);
        }
        if(originalEnemyId == 0x6d) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x6e) {
            // 2 is rider speed, 7 is wolf speed, 11 is projectile speed
            return random.nextBoolean()
                    ? (int)enemy.getArgs().get(4)
                    : (int)enemy.getArgs().get(9);
        }
        if(originalEnemyId == 0x6f) {
            return (int)enemy.getArgs().get(7);
        }
        if(originalEnemyId == 0x70) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x73) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x74) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x7d) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x7e) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x81) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x82) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x83) {
            return (int)enemy.getArgs().get(4);
        }
        if(originalEnemyId == 0x8f) {
            return (int)enemy.getArgs().get(4);
        }
        return null;
    }

    private Integer getProjectileDamage(GameObject enemy) {
        int originalEnemyId = enemy.getId();
        if(originalEnemyId == 0x03) {
            if(enemy.getArgs().get(4) != 0) {
                return (int)enemy.getArgs().get(7);
            }
        }
        if(originalEnemyId == 0x06) {
            return (int)enemy.getArgs().get(5);
        }
//        if(originalEnemyId == 0x17) {
//            return (int)enemy.getArgs().get(5); // Swoop Damage
//        }
//        if(originalEnemyId == 0x18) {
//            return (int)enemy.getArgs().get(6); // Swoop Damage
//        }
//        if(originalEnemyId == 0x1b) {
//            return (int)enemy.getArgs().get(5); // Swoop Damage
//        }
//        if(originalEnemyId == 0x1e) {
//            return (int)enemy.getArgs().get(5); // Punch Damage
//        }
        if(originalEnemyId == 0x21) {
            return (int)enemy.getArgs().get(8);
        }
        if(originalEnemyId == 0x26) {
            return (int)enemy.getArgs().get(5); // Spindash Damage
        }
//        if(originalEnemyId == 0x27) {
//            return random.nextBoolean()
//                    ? (int)enemy.getArgs().get(5)
//                    : (int)enemy.getArgs().get(8);
//        }
        if(originalEnemyId == 0x28) {
            return (int)enemy.getArgs().get(6);
        }
        if(originalEnemyId == 0x29) {
            return (int)enemy.getArgs().get(6);
        }
        if(originalEnemyId == 0x35) {
            return (int)enemy.getArgs().get(8);
        }
//        if(originalEnemyId == 0x3b) {
//            return (int)enemy.getArgs().get(7); // Explosion damage
//        }
        if(originalEnemyId == 0x3e) {
            return (int)enemy.getArgs().get(7);
        }
        if(originalEnemyId == 0x42) {
            return (int)enemy.getArgs().get(6);
        }
//        if(originalEnemyId == 0x43) {
//            return (int)enemy.getArgs().get(5); // Kick Damage
//        }
        if(originalEnemyId == 0x48) {
            return (int)enemy.getArgs().get(9);
        }
        if(originalEnemyId == 0x49) {
            return (int)enemy.getArgs().get(10);
        }
//        if(originalEnemyId == 0x4a) {
//            return (int)enemy.getArgs().get(5); // Stomp Damage
//        }
//        if(originalEnemyId == 0x50) {
//            return (int)enemy.getArgs().get(5); // Kick Damage
//        }
        if(originalEnemyId == 0x51) {
            return (int)enemy.getArgs().get(6); // Shock Damage
        }
        if(originalEnemyId == 0x52) {
            return (int)enemy.getArgs().get(6); // Flame damage
        }
        if(originalEnemyId == 0x53) {
            return (int)enemy.getArgs().get(8);
        }
        if(originalEnemyId == 0x55) {
            return (int)enemy.getArgs().get(13); // Initial projectile damage
        }
        if(originalEnemyId == 0x56) {
            return (int)enemy.getArgs().get(7);
        }
        if(originalEnemyId == 0x5c) {
            return (int)enemy.getArgs().get(8);
        }
//        if(originalEnemyId == 0x5d) {
//            return (int)enemy.getArgs().get(5); // Leap damage
//        }
        if(originalEnemyId == 0x62) {
            return (int)enemy.getArgs().get(9);
        }
        if(originalEnemyId == 0x64) {
            return (int)enemy.getArgs().get(7);
        }
        if(originalEnemyId == 0x66) {
            return (int)enemy.getArgs().get(8);
        }
//        if(originalEnemyId == 0x6a) {
//            return (int)enemy.getArgs().get(7); // Rolling damage
//        }
        if(originalEnemyId == 0x6e) {
            return (int)enemy.getArgs().get(6);
        }
        if(originalEnemyId == 0x74) {
            return (int)enemy.getArgs().get(8); // Flame damage
        }
//        if(originalEnemyId == 0x7d) {
//            return (int)enemy.getArgs().get(4);
//        }
//        if(originalEnemyId == 0x81) {
//            return (int)enemy.getArgs().get(4);
//        }
//        if(originalEnemyId == 0x82) {
//            return (int)enemy.getArgs().get(4);
//        }
//        if(originalEnemyId == 0x83) {
//            return (int)enemy.getArgs().get(4);
//        }
//        if(originalEnemyId == 0x8f) {
//            return (int)enemy.getArgs().get(4);
//        }
        return null;
    }

    private Integer getSpeedBonus(GameObject enemy) {
        int originalEnemyId = enemy.getId();
        if(originalEnemyId == 0x01) {
            return 0;
        }
        if(originalEnemyId == 0x02) {
            return (int)enemy.getArgs().get(2);
        }
        if(originalEnemyId == 0x03) {
            return (int)enemy.getArgs().get(2);
        }
        if(originalEnemyId == 0x05) {
            return 0;
        }
        if(originalEnemyId == 0x06) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x16) {
            return 0;
        }
        if(originalEnemyId == 0x17) {
            return 0;
        }
        if(originalEnemyId == 0x18) {
            return Math.max(0, enemy.getArgs().get(2) - 1);
        }
        if(originalEnemyId == 0x1b) {
            return 0;
        }
        if(originalEnemyId == 0x1c) {
            return Math.max(0, enemy.getArgs().get(2) - 1);
        }
        if(originalEnemyId == 0x1d) {
            return 0;
        }
        if(originalEnemyId == 0x1e) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x21) {
            return 0;
        }
        if(originalEnemyId == 0x26) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x27) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x28) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x29) {
            return 0;
        }
        if(originalEnemyId == 0x35) {
            return (int)enemy.getArgs().get(3);
        }
        if(originalEnemyId == 0x37) {
            return Math.max(0, enemy.getArgs().get(3) - 1);
        }
        if(originalEnemyId == 0x38) {
            return 0;
        }
        if(originalEnemyId == 0x39) {
            return 0;
        }
        if(originalEnemyId == 0x3b) {
            return Math.max(0, enemy.getArgs().get(1) - 1);
        }
        if(originalEnemyId == 0x3c) {
            return 0;
        }
        if(originalEnemyId == 0x3e) {
            return 0;
        }
        if(originalEnemyId == 0x41) {
            return 0;
        }
        if(originalEnemyId == 0x42) {
            return 0;
        }
        if(originalEnemyId == 0x43) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x44) {
            return 0;
        }
        if(originalEnemyId == 0x48) {
            return 0;
        }
        if(originalEnemyId == 0x49) {
            // Basing this on projectile speed
            return Math.max(0, enemy.getArgs().get(9) - 2);
        }
        if(originalEnemyId == 0x4a) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x4b) {
            return Math.max(0, enemy.getArgs().get(2) - 1);
        }
        if(originalEnemyId == 0x4c) {
            return Math.max(0, enemy.getArgs().get(2) - 1);
        }
        if(originalEnemyId == 0x4f) {
            return 0;
        }
        if(originalEnemyId == 0x50) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x51) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x52) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x53) {
            // Basing this on projectile speed
            return Math.max(0, enemy.getArgs().get(7) - 2);
        }
        if(originalEnemyId == 0x55) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x56) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x57) {
            return (int)enemy.getArgs().get(2);
        }
        if(originalEnemyId == 0x58) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x59) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x5c) {
            return Math.max(0, enemy.getArgs().get(2) - 1);
        }
        if(originalEnemyId == 0x5d) {
            return 0;
        }
        if(originalEnemyId == 0x5e) {
            return Math.max(0, enemy.getArgs().get(2) - 1);
        }
        if(originalEnemyId == 0x62) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x63) {
            return 0;
        }
        if(originalEnemyId == 0x64) {
            return Math.max(0, enemy.getArgs().get(2) - 1);
        }
        if(originalEnemyId == 0x65) {
            return 0;
        }
        if(originalEnemyId == 0x66) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x68) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x69) {
            return (int)enemy.getArgs().get(2);
        }
        if(originalEnemyId == 0x6a) {
            return 0;
        }
        if(originalEnemyId == 0x6c) {
            return Math.max(0, enemy.getArgs().get(4) - 2);
        }
        if(originalEnemyId == 0x6d) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x6e) {
            // 2 is rider speed, 7 is wolf speed, 11 is projectile speed
            return random.nextBoolean()
                    ? 0
                    : Math.max(0, enemy.getArgs().get(7) - 3);
        }
        if(originalEnemyId == 0x6f) {
            return 0;
        }
        if(originalEnemyId == 0x70) {
            return Math.max(0, enemy.getArgs().get(2) - 1);
        }
        if(originalEnemyId == 0x73) {
            return 0;
        }
        if(originalEnemyId == 0x74) {
            return 0;
        }
        if(originalEnemyId == 0x7d) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x7e) {
            return Math.max(0, enemy.getArgs().get(2) - 1);
        }
        if(originalEnemyId == 0x81) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x82) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x83) {
            return Math.max(0, enemy.getArgs().get(2) - 2);
        }
        if(originalEnemyId == 0x8f) {
            return 0;
        }
        return null;
    }

    private void adjustEnemyHeight(GameObject enemy, int newEnemyId) {
        int existingEnemyHeightClass = getHeightClass(enemy.getId());
        int newEnemyHeightClass = getHeightClass(newEnemyId);
        if(existingEnemyHeightClass == newEnemyHeightClass) {
            return;
        }
        int heightClassDifference = newEnemyHeightClass - existingEnemyHeightClass;
        enemy.setY(enemy.getY() + 20 * heightClassDifference);
    }

    private int getHeightClass(int enemyId) {
//        if(enemyId == 0x02) {
//            // Bat
//            return -2;
//        }
        if(enemyId == 0x28) {
            // Sun Bird
            return -2;
        }
        if(enemyId == 0x68) {
            // Anubis
            return -2;
        }
        if(ENEMY_HEIGHT_LOWERED.contains(enemyId)) {
            return 1;
        }
        if(ENEMY_HEIGHT_RAISED.contains(enemyId)) {
            return -1;
        }
        return 0;
    }

    private void setTogSpawnerArgs(GameObject enemy) {
    }

    private void setNinjaSpawnerArgs(GameObject enemy) {
    }
}
