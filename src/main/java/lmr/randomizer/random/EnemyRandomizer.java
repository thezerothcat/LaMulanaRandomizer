package lmr.randomizer.random;

import lmr.randomizer.Settings;
import lmr.randomizer.rcd.object.GameObject;
import lmr.randomizer.rcd.object.Screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class EnemyRandomizer {
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
    private static final List<Integer> NO_COLLISION_ENEMIES = Arrays.asList(0x02, 0x18, 0x1b, 0x29, 0x3b, 0x43,
            0x65, 0x6d, 0x7d);
    private static final List<Integer> SPAWNER_ENEMIES = Arrays.asList(0x1f, 0x6c, 0x7c);
    private static final List<Integer> NO_FLAG_UPDATE_ENEMIES = Arrays.asList(0x06, 0x3b);

    private Random random;

    public EnemyRandomizer(Random random) {
        this.random = random;
    }

    public void randomizeEnemy(GameObject enemyObject) {
        int zoneIndex = ((Screen)enemyObject.getObjectContainer()).getZoneIndex();
        replaceEnemyParams(enemyObject, getEnemyId(enemyObject, zoneIndex), zoneIndex);
    }

    private int getEnemyId(GameObject enemyObject, int zoneIndex) {
        int enemyId = (int)enemyObject.getId();
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
        else if(newEnemyId == 0x02) {
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
            setKakaojuuArgs(enemy);
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
        else if(newEnemyId == 0x7d) {
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

        List<Integer> enemyIds = getSharedEnemyIds(includeGround, isNoCollisionEnemy, hasUpdateFlags);
        if(zoneIndex == 0) {
            enemyIds.addAll(getGuidanceEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 1) {
            enemyIds.addAll(getSurfaceEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 2) {
            enemyIds.addAll(getMausoleumEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 3) {
            enemyIds.addAll(getSunEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 4) {
            enemyIds.addAll(getSpringEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 5) {
            enemyIds.addAll(getInfernoEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 6) {
            enemyIds.addAll(getExtinctionEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 7) {
            enemyIds.addAll(getTwinLabsEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 8) {
            enemyIds.addAll(getEndlessEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 9) {
            enemyIds.addAll(getShrineEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 10) {
            enemyIds.addAll(getIllusionEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 11) {
            enemyIds.addAll(getGraveyardEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 12) {
            enemyIds.addAll(getMoonlightEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 13) {
            enemyIds.addAll(getGoddessEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 14) {
            enemyIds.addAll(getRuinEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 15 || zoneIndex == 16) {
            enemyIds.addAll(getBirthEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 17) {
            enemyIds.addAll(getDimensionalEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 18) {
            enemyIds.addAll(getShrineEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 22) {
            // Night surface
            enemyIds.addAll(getSurfaceEnemyIds(includeGround, isNoCollisionEnemy));
        }
        else if(zoneIndex == 23) {
            enemyIds.addAll(getHellTempleEnemyIds(includeGround, isNoCollisionEnemy));
        }
        return enemyIds.get(random.nextInt(enemyIds.size()));
    }

    private List<Integer> getSharedGroundEnemyIds(boolean hasUpdateFlags) {
        List<Integer> enemyIds = new ArrayList<>();
        enemyIds.add(0x03); // Skeleton
        enemyIds.add(0x05); // Snouter
        if(!hasUpdateFlags) {
            enemyIds.add(0x06); // Kodama Rat / Pink exploding rat pig
        }
        enemyIds.add(0x1c); // Masked Man
        enemyIds.add(0x26); // Sonic
        enemyIds.add(0x3c); // Jump Slime
        enemyIds.add(0x41); // Mandrake
        enemyIds.add(0x62); // Toujin / Hadouken turtle
//        enemyIds.add(0x6c); // Ninja spawner
//        enemyIds.add(0x7c); // Mudman spawner
        return enemyIds;
    }

    private List<Integer> getSharedAirEnemyIds() {
        List<Integer> enemyIds = new ArrayList<>();
        enemyIds.add(0x68); // Anubis
        return enemyIds;
    }

    private List<Integer> getSharedNoCollisionEnemyIds(boolean hasUpdateFlags) {
        List<Integer> enemyIds = new ArrayList<>();
        enemyIds.add(0x02); // Bat
        enemyIds.add(0x1b); // Mirror Ghost
        if(!hasUpdateFlags) {
            enemyIds.add(0x3b); // Explode Rock / Mine
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
        enemyIds.add(0x18); // Surface - Vulture
        if(!isNoCollisionEnemy && includeGround) {
            enemyIds.add(0x16); // Surface - Snake
            enemyIds.add(0x17); // Surface - Bird
        }
        return enemyIds;
    }
    
    private List<Integer> getGuidanceEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!isNoCollisionEnemy && includeGround) {
            enemyIds.add(0x01); // Guidance - Myrmecoleon
            enemyIds.add(0x21); // Guidance - Red Skeleton
        }
        return enemyIds;
    }

    private List<Integer> getMausoleumEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
//        enemyIds.add(0x1f); // Mausoleum Ghosts
        if(!isNoCollisionEnemy) {
            enemyIds.add(0x1e); // Mausoleum - Fist
            if(includeGround) {
                enemyIds.add(0x1d); // Mausoleum - Nozuchi
            }
        }
        return enemyIds;
    }

    private List<Integer> getSunEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        enemyIds.add(0x29); // Sun - Mask
        if(!isNoCollisionEnemy) {
            enemyIds.add(0x28); // Sun - Bird
            if(includeGround) {
                enemyIds.add(0x27); // Sun - Cait Sith / CatBall
            }
        }
        return enemyIds;
    }

    private List<Integer> getSpringEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!isNoCollisionEnemy && includeGround) {
            enemyIds.add(0x35); // Spring - Gyonin
        }
        return enemyIds;
    }

    private List<Integer> getInfernoEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!isNoCollisionEnemy && includeGround) {
            enemyIds.add(0x3e); // Inferno - Kakaojuu / Fire Lizard
        }
        return enemyIds;
    }

    private List<Integer> getExtinctionEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        enemyIds.add(0x43); // Extinction - Garuda
        if(!isNoCollisionEnemy && includeGround) {
            enemyIds.add(0x42); // Extinction - Naga
//            enemyIds.add(0x44); // Extinction - Blob
        }
        return enemyIds;
    }

    private List<Integer> getTwinLabsEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!isNoCollisionEnemy) {
            enemyIds.add(0x55); // Twin Labs - Witch
            if(includeGround) {
                enemyIds.add(0x56); // Twin Labs - Siren // todo: might not be ground only
                enemyIds.add(0x57); // Twin Labs - Xingtian
                enemyIds.add(0x58); // Twin Labs - Zaochi
                enemyIds.add(0x59); // Twin Labs - Lizard / Leucrotta / gator
            }
        }
        return enemyIds;
    }

    private List<Integer> getEndlessEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!isNoCollisionEnemy && includeGround) {
            enemyIds.add(0x48); // Endless - Bonnacon
            enemyIds.add(0x49); // Endless - Flower-faced snouter
            enemyIds.add(0x4a); // Endless - Monocoli / Baby Snowman
            enemyIds.add(0x4b); // Endless - Jiangshi
            enemyIds.add(0x4c); // Endless - Rongxuanwangcorpse
        }
        return enemyIds;
    }

    private List<Integer> getShrineEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!isNoCollisionEnemy && includeGround) {
            enemyIds.add(0x50); // Shrine - Pan
            enemyIds.add(0x51); // Shrine - Hanuman
            enemyIds.add(0x52); // Shrine - Enkidu
            enemyIds.add(0x53); // Shrine - Marchosias
        }
        return enemyIds;
    }

    private List<Integer> getIllusionEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!isNoCollisionEnemy && includeGround) {
            enemyIds.add(0x5c); // Illusion - Lizard
            enemyIds.add(0x5d); // Illusion - Asp
            enemyIds.add(0x5e); // Illusion - Kui / Hopper
        }
        return enemyIds;
    }

    private List<Integer> getGraveyardEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        enemyIds.add(0x65); // Graveyard - Cloud / Puffball
        if(!isNoCollisionEnemy && includeGround) {
            enemyIds.add(0x63); // Graveyard - Dijiang / Faceless
            enemyIds.add(0x64); // Graveyard - Ice Wizard
            enemyIds.add(0x66); // Graveyard - Baize / Icicle shot / Spiked Dinosaur
        }
        return enemyIds;
    }

    private List<Integer> getMoonlightEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!isNoCollisionEnemy) {
//            enemyIds.add(0x69); // Moonlight - Bug
            if(includeGround) {
                enemyIds.add(0x6a); // Moonlight - Troll
            }
        }
        return enemyIds;
    }

    private List<Integer> getGoddessEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        enemyIds.add(0x6d); // Goddess - A Bao A Qu
        if(!isNoCollisionEnemy && includeGround) {
            enemyIds.add(0x6e); // Goddess - Andras
            enemyIds.add(0x70); // Goddess - Cyclops
        }
        return enemyIds;
    }

    private List<Integer> getRuinEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!isNoCollisionEnemy && includeGround) {
            enemyIds.add(0x73); // Ruin - Dog
            enemyIds.add(0x74); // Ruin - Salamander
        }
        return enemyIds;
    }

    private List<Integer> getBirthEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        enemyIds.add(0x7d); // Birth - Sword Bird
        if(!isNoCollisionEnemy && includeGround) {
            enemyIds.add(0x7e); // Birth - Elephant
        }
        return enemyIds;
    }

    private List<Integer> getDimensionalEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        if(!isNoCollisionEnemy) {
            enemyIds.add(0x83); // Dimensional - Gargoyle / Satan / Telephone Demon
            if(includeGround) {
                enemyIds.add(0x81); // Dimensional - Amon / Teleport Demon / Flame Summoner
                enemyIds.add(0x82); // Dimensional - Devil Crown Skull
            }
        }
        return enemyIds;
    }

    private List<Integer> getRetroEnemyIds(boolean includeGround, boolean isNoCollisionEnemy, boolean hasUpdateFlags) {
        List<Integer> enemyIds = new ArrayList<>();
        enemyIds.add(0x02); // Bat
        enemyIds.add(0x1b); // Mirror Ghost
        if(!hasUpdateFlags) {
            enemyIds.add(0x3b); // Explode Rock / Mine
        }
//        enemyIds.add(0x1f); // Mausoleum Ghosts
        if(!isNoCollisionEnemy) {
            enemyIds.add(0x1e); // Mausoleum - Fist
            enemyIds.add(0x68); // Anubis
            if(includeGround) {
                enemyIds.add(0x01); // Guidance - Myrmecoleon
                enemyIds.add(0x03); // Skeleton
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
        return enemyIds;
    }

    private List<Integer> getHellTempleEnemyIds(boolean includeGround, boolean isNoCollisionEnemy) {
        List<Integer> enemyIds = new ArrayList<>();
        enemyIds.add(0x7d); // Birth - Sword Bird
        if(!isNoCollisionEnemy) {
            enemyIds.add(0x1e); // Mausoleum - Fist
            enemyIds.add(0x83); // Dimensional - Gargoyle / Satan / Telephone Demon
            if(includeGround) {
                enemyIds.add(0x50); // Shrine - Pan
                enemyIds.add(0x51); // Shrine - Hanuman
                enemyIds.add(0x52); // Shrine - Enkidu
                enemyIds.add(0x53); // Shrine - Marchosias
                enemyIds.add(0x55); // Twin Labs - Witch
                enemyIds.add(0x5c); // Illusion - Lizard
                enemyIds.add(0x64); // Graveyard - Ice Wizard
                enemyIds.add(0x6e); // Goddess - Andras
                enemyIds.add(0x7e); // Birth - Elephant
                enemyIds.add(0x81); // Dimensional - Amon / Teleport Demon / Flame Summoner
                enemyIds.add(0x82); // Dimensional - Devil Crown Skull
                enemyIds.add(0x8f); // HT - MiniBoss / Bomb-throwing Slime
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

    private void setBatArgs(GameObject enemy, int zoneIndex) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)1); // 0 = start resting / 1 = start flying
        enemy.getArgs().add((short)(random.nextInt(2) + 1)); // Drop type - nothing or coins
        enemy.getArgs().add((short)(random.nextInt(3) + 1)); // UNKNOWN

        // Bat type is special - backside bats are invisible in retro areas
        if(zoneIndex == 19 || zoneIndex == 20 || zoneIndex == 21) {
            enemy.getArgs().add((short)0);
        }
        else {
            enemy.getArgs().add((short)random.nextInt(2));
        }

        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Damage
    }

    private void setSkeletonArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)(random.nextBoolean() ? 1 : 11)); // Droptype - 1 is coins, 11 for skeleton is either coins or weights
        enemy.getArgs().add((short)random.nextInt(4)); // Speed
        enemy.getArgs().add((short)random.nextInt(2)); // Collapsed or standing
        enemy.getArgs().add((short)random.nextInt(3)); // Type
        enemy.getArgs().add((short)(random.nextInt(11) + 3)); // Health
        enemy.getArgs().add((short)(random.nextInt(5) + 2)); // Contact damage
        enemy.getArgs().add((short)(random.nextInt(4) + 2)); // Projectile damage
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

    private void setRedSkeletonArgs(GameObject enemy) {
        int facing = getFacing(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Speed
        enemy.getArgs().add((short)20); // Drop type
        enemy.getArgs().add((short)0); // Amount
        enemy.getArgs().add((short)12); // Health
        enemy.getArgs().add((short)10); // Contact damage
        enemy.getArgs().add((short)1); // Projectile Speed
        enemy.getArgs().add((short)1); // Projectile Count
        enemy.getArgs().add((short)3); // Projectile Damage
        enemy.getArgs().add((short)15); // Soul
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

        enemy.getArgs().clear();
        enemy.getArgs().add((short)random.nextInt(2)); // 0 = start on ball, 1 = start off ball
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)(random.nextInt(3) + 2)); // Speed
        enemy.getArgs().add((short)1); // Drop type - coins
        enemy.getArgs().add((short)(random.nextInt(9) + 2)); // Cat Health
        enemy.getArgs().add((short)(random.nextInt(7) + 2)); // Cat Damage
        enemy.getArgs().add((short)3); // Cat Soul
        enemy.getArgs().add((short)(random.nextInt(15) + 2)); // Ball Health
        enemy.getArgs().add((short)(random.nextInt(13) + 4)); // Ball Damage
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

    private void setKakaojuuArgs(GameObject enemy) {
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

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)random.nextInt(6)); //  Type - 0 = lightning, 1 = fire, 2 = paralyze, 3 = splitting orb, 4 = white mage, 5+ = black mage // todo: only some of these work graphically outside of Twin labs
        enemy.getArgs().add((short)random.nextInt(10)); // Drop type
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed
        enemy.getArgs().add((short)(random.nextInt(9) + 4)); // Health
        enemy.getArgs().add((short)(random.nextInt(4) + 3)); // Contact damage
        enemy.getArgs().add((short)(random.nextInt(3) + 6)); // Soul
        enemy.getArgs().add((short)(random.nextInt(119) + 2)); // Time between volleys attacks // todo: make this sane, probably per witch type
        enemy.getArgs().add((short)(random.nextInt(6) + 1)); // Projectiles per volley // todo: make this sane, probably per witch type
        enemy.getArgs().add((short)(random.nextInt(79) + 2)); // Delay after shot // todo: make this sane, probably per witch type
        enemy.getArgs().add((short)2); // Projectile speed
        enemy.getArgs().add((short)2); // Secondary projectile speed (first split for witches)
        enemy.getArgs().add((short)2); // Tertiary projectile speed (first split for witches)
        enemy.getArgs().add((short)(random.nextInt(21) + 4)); // Initial projectile damage // todo: make this sane, probably per witch type
        enemy.getArgs().add((short)(random.nextInt(21) + 4)); // Secondary projectile damage (lingering flame, first split) // todo: make this sane, probably per witch type
        enemy.getArgs().add((short)(random.nextInt(21) + 4)); // Tertiary projectile damage (second split) // todo: make this sane, probably per witch type
        enemy.getArgs().add((short)(random.nextInt(59) + 2)); // Initial projectile duration (time to first split) // todo: make this sane, probably per witch type
        enemy.getArgs().add((short)(random.nextInt(149) + 2)); // Secondary projectile duration (flame duration, time to second split) // todo: make this sane, probably per witch type
        enemy.getArgs().add((short)(random.nextInt(59) + 2)); // Tertiary projectile duration (flame duration, time to second split) // todo: make this sane, probably per witch type
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

    private void setSwordBirdArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)random.nextInt(2)); // Facing?
        enemy.getArgs().add((short)2); // Drop type - weights?
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed?
        enemy.getArgs().add((short)4); // Health?
        enemy.getArgs().add((short)8); // UNKNOWN
        enemy.getArgs().add((short)6); // UNKNOWN
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // UNKNOWN
        enemy.getArgs().add((short)6); // UNKNOWN
    }

    private void setElephantArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)random.nextInt(2)); // Facing?
        enemy.getArgs().add((short)11); // UNKNOWN - maybe Drop type - 11 = "nothing for pots, coins or weights for skeletons"
        enemy.getArgs().add((short)(random.nextInt(4) + 1)); // Speed?
        enemy.getArgs().add((short)30); // Health?
        enemy.getArgs().add((short)10); // UNKNOWN
        enemy.getArgs().add((short)10); // UNKNOWN
        enemy.getArgs().add((short)36); // UNKNOWN
        enemy.getArgs().add((short)20); // UNKNOWN
    }

    private void setAmonArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)random.nextInt(2)); // Facing?
        enemy.getArgs().add((short)11); // UNKNOWN - maybe Drop type - 11 = "nothing for pots, coins or weights for skeletons"
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed?
        enemy.getArgs().add((short)15); // Health?
        enemy.getArgs().add((short)10); // UNKNOWN
        enemy.getArgs().add((short)11); // UNKNOWN
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // UNKNOWN
        enemy.getArgs().add((short)(random.nextInt(3) + 2)); // UNKNOWN
        enemy.getArgs().add((short)10); // UNKNOWN
        enemy.getArgs().add((short)3); // UNKNOWN
    }

    private void setDevilCrownSkullArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)0); // UNKNOWN
        enemy.getArgs().add((short)0); // UNKNOWN
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed?
        enemy.getArgs().add((short)18); // Health?
        enemy.getArgs().add((short)16); // UNKNOWN
        enemy.getArgs().add((short)7); // UNKNOWN
    }

    private void setGargoyleArgs(GameObject enemy) {
        int facing = getFacing(enemy);
        boolean isGroundEnemy = isGroundEnemy(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing); // 0 = left, 1 = right
        enemy.getArgs().add((short)11); // UNKNOWN - maybe Drop type - 11 = "nothing for pots, coins or weights for skeletons"
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Speed?
        enemy.getArgs().add((short)10); // UNKNOWN
        enemy.getArgs().add((short)8); // UNKNOWN
        enemy.getArgs().add((short)8); // UNKNOWN
        enemy.getArgs().add((short)16); // UNKNOWN
        enemy.getArgs().add((short)(random.nextInt(2) + 1)); // UNKNOWN
        enemy.getArgs().add((short)4); // UNKNOWN
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // UNKNOWN
        enemy.getArgs().add((short)(isGroundEnemy ? random.nextInt(2) : 1)); // 0 = start standing, 1 = start flying
    }

    private void setMiniBossArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)random.nextInt(2)); // Facing?
        enemy.getArgs().add((short)1); // UNKNOWN
        enemy.getArgs().add((short)2); // UNKNOWN
        enemy.getArgs().add((short)10); // UNKNOWN
        enemy.getArgs().add((short)6); // UNKNOWN
        enemy.getArgs().add((short)7); // UNKNOWN
        enemy.getArgs().add((short)2); // UNKNOWN
        enemy.getArgs().add((short)300); // UNKNOWN
        enemy.getArgs().add((short)60); // UNKNOWN
        enemy.getArgs().add((short)16); // UNKNOWN
        enemy.getArgs().add((short)0); // UNKNOWN
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

    private void setMudmanSpawnerArgs(GameObject enemy) {
    }
}
