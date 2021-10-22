package lmr.randomizer.randomization;

import lmr.randomizer.HolidaySettings;
import lmr.randomizer.Settings;
import lmr.randomizer.rcd.object.ByteOp;
import lmr.randomizer.rcd.object.GameObject;
import lmr.randomizer.rcd.object.ObjectIdConstants;
import lmr.randomizer.rcd.object.TestByteOperation;
import lmr.randomizer.util.FlagConstants;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Halloween2021EnemyRandomizer extends EnemyRandomizer {
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

    public Halloween2021EnemyRandomizer(Random random) {
        super(random);
    }

    @Override
    protected int getNewEnemyId(GameObject enemyObject, int zoneIndex) {
        int enemyId = enemyObject.getId();
        if(enemyId == ObjectIdConstants.Kuusarikku || enemyId == ObjectIdConstants.Girtablilu || enemyId == ObjectIdConstants.Ushum
                || enemyId == ObjectIdConstants.Mushussu || enemyId == ObjectIdConstants.Hekatonkheires || enemyId == ObjectIdConstants.Buer) {
            return enemyId; // Miniboss swaps
        }

        if(enemyId == ObjectIdConstants.RedSkeleton) {
            return ObjectIdConstants.Enemy_Skeleton;
        }

        if(enemyId == 0x69) {
            return enemyId; // Moonlight bugs can only walk in certain places, so they aren't random yet (except params)
        }

        if(SPAWNER_ENEMIES.contains(enemyId)) {
            // Spawners not random yet.
            return enemyId;
        }

//        else if(enemyId == 0x44) {
//            return getExtinctionEnemyId(false);
//        }
//        else if(enemyId == 0x4f) {
//            // Hundun / Blue Fire Rock
//            return getShrineEnemyId(false);
//        }
        return getEnemyIdByArea(zoneIndex, isGroundEnemy(enemyObject), isWaterEnemy(enemyObject),
                isNoCollisionEnemy(enemyObject), !enemyObject.getWriteByteOperations().isEmpty());
    }

    public void replaceEnemyParams(GameObject enemy, int newEnemyId, int zoneIndex) {
        if(newEnemyId == ObjectIdConstants.Enemy_Bat) {
            setBatArgs(enemy, zoneIndex);
        }
        else if(newEnemyId == ObjectIdConstants.Enemy_Skeleton) {
            setSkeletonArgs(enemy);
        }
        else if(newEnemyId == ObjectIdConstants.GhostLord) {
            setGhostLordArgs(enemy);
        }
        else if(newEnemyId == ObjectIdConstants.RedSkeleton) {
            setRedSkeletonArgs(enemy);
        }
        else if(newEnemyId == ObjectIdConstants.Enemy_CatBall) {
            setCatBallArgs(enemy);
        }
        else if(newEnemyId == ObjectIdConstants.Enemy_Witch) {
            setWitchArgs(enemy, zoneIndex);
        }
        else if(newEnemyId == ObjectIdConstants.Enemy_SwordBird) {
            setSwordBirdArgs(enemy);
        }
        else if(newEnemyId == ObjectIdConstants.Enemy_MiniBoss) {
            // Bomb-throwing Slime
            setMiniBossArgs(enemy);
        }
        adjustEnemyHeight(enemy, newEnemyId);
        enemy.setId((short) newEnemyId);
    }

    private int getEnemyIdByArea(int zoneIndex, boolean isGround, boolean isWater, boolean isNoCollision, boolean hasUpdateFlags) {
        if(zoneIndex == 0) {
            if(isGround) {
                return ObjectIdConstants.Enemy_Skeleton;
            }
            if(isNoCollision) {
                if(random.nextInt(10) < 8) {
                    return ObjectIdConstants.Enemy_Bat;
                }
                return ObjectIdConstants.Enemy_SwordBird;
            }
        }
        else if(zoneIndex == 1) {
            if(isGround) {
                return ObjectIdConstants.Enemy_Skeleton;
            }
            if(isNoCollision) {
                if(random.nextInt(10) < 8) {
                    return ObjectIdConstants.Enemy_Bat;
                }
                return ObjectIdConstants.Enemy_SwordBird;
            }
        }
        else if(zoneIndex == 2) {
            if(isGround) {
                return ObjectIdConstants.Enemy_Skeleton;
            }
            if(isNoCollision) {
                if(random.nextInt(10) < 8) {
                    return ObjectIdConstants.Enemy_Bat;
                }
                return ObjectIdConstants.Enemy_SwordBird;
            }
        }
        else if(zoneIndex == 3) {
            if(isGround) {
                if(random.nextInt(10) < 6) {
                    return ObjectIdConstants.Enemy_CatBall;
                }
                return ObjectIdConstants.Enemy_Skeleton;
            }
            if(isNoCollision) {
                return ObjectIdConstants.Enemy_Bat;
            }
        }
        else if(zoneIndex == 4) {
            if(isGround) {
                return ObjectIdConstants.Enemy_Skeleton;
            }
            if(isWater || isNoCollision) {
                if(random.nextInt(10) < 8) {
                    return ObjectIdConstants.Enemy_Bat;
                }
                return ObjectIdConstants.Enemy_SwordBird;
            }
        }
        else if(zoneIndex == 5) {
            if(isGround) {
                return ObjectIdConstants.Enemy_Skeleton;
            }
            if(isWater || isNoCollision) {
                if(random.nextInt(10) < 8) {
                    return ObjectIdConstants.Enemy_Bat;
                }
                return ObjectIdConstants.Enemy_SwordBird;
            }
        }
        else if(zoneIndex == 6) {
            if(isGround) {
                if(random.nextInt(10) < 9) {
                    return ObjectIdConstants.Enemy_Skeleton;
                }
                return ObjectIdConstants.Enemy_Witch;
            }
            if(isNoCollision) {
                if(random.nextInt(10) < 8) {
                    return ObjectIdConstants.Enemy_Bat;
                }
                return ObjectIdConstants.Enemy_SwordBird;
            }
        }
        else if(zoneIndex == 7) {
            if(isGround) {
                if(random.nextInt(10) < 8) {
                    return ObjectIdConstants.Enemy_Skeleton;
                }
                return ObjectIdConstants.Enemy_Witch;
            }
            if(isNoCollision) {
                return ObjectIdConstants.Enemy_Bat;
            }
        }
        else if(zoneIndex == 8) {
            if(isGround) {
                if(random.nextInt(10) < 9) {
                    return ObjectIdConstants.Enemy_Skeleton;
                }
                return ObjectIdConstants.Enemy_Witch;
            }
            if(isNoCollision) {
                if(random.nextInt(10) < 8) {
                    return ObjectIdConstants.Enemy_Bat;
                }
                return ObjectIdConstants.Enemy_SwordBird;
            }
        }
        else if(zoneIndex == 9) {
            if(isGround) {
                if(random.nextInt(10) < 8) {
                    return ObjectIdConstants.Enemy_Skeleton;
                }
                return ObjectIdConstants.Enemy_Witch;
            }
            if(isNoCollision) {
                if(random.nextInt(10) < 9) {
                    return ObjectIdConstants.Enemy_Bat;
                }
                return ObjectIdConstants.Enemy_SwordBird;
            }
        }
        else if(zoneIndex == 10) {
            if(isGround) {
                if(random.nextInt(10) < 5) {
                    return ObjectIdConstants.Enemy_Skeleton;
                }
                return ObjectIdConstants.Enemy_CatBall;
            }
            if(isNoCollision) {
                return ObjectIdConstants.Enemy_Bat;
            }
        }
        else if(zoneIndex == 11) {
            if(isGround) {
                int randomVal = random.nextInt(10);
                if(randomVal < 5) {
                    return ObjectIdConstants.Enemy_CatBall;
                }
                if(randomVal < 8) {
                    return ObjectIdConstants.Enemy_Skeleton;
                }
                return ObjectIdConstants.Enemy_Witch;
            }
            if(isNoCollision) {
                return ObjectIdConstants.Enemy_Bat;
            }
        }
        else if(zoneIndex == 12) {
            if(isGround) {
                return ObjectIdConstants.Enemy_Skeleton;
            }
            if(isNoCollision) {
                if(random.nextInt(10) < 8) {
                    return ObjectIdConstants.Enemy_Bat;
                }
                return ObjectIdConstants.Enemy_SwordBird;
            }
        }
        else if(zoneIndex == 13) {
            if(isGround) {
                int randomVal = random.nextInt(10);
                if(randomVal < 5) {
                    return ObjectIdConstants.Enemy_CatBall;
                }
                if(randomVal < 8) {
                    return ObjectIdConstants.Enemy_Skeleton;
                }
                return ObjectIdConstants.Enemy_Witch;
            }
            if(isNoCollision) {
                return ObjectIdConstants.Enemy_Bat;
            }
        }
        else if(zoneIndex == 14) {
            if(isGround) {
                return ObjectIdConstants.Enemy_Skeleton;
            }
            if(isNoCollision) {
                if(random.nextInt(10) < 8) {
                    return ObjectIdConstants.Enemy_Bat;
                }
                return ObjectIdConstants.Enemy_SwordBird;
            }
        }
        else if(zoneIndex == 15 || zoneIndex == 16) {
            if(isGround) {
                return ObjectIdConstants.Enemy_Skeleton;
            }
            if(isNoCollision) {
                if(random.nextInt(10) < 5) {
                    return ObjectIdConstants.Enemy_Bat;
                }
                return ObjectIdConstants.Enemy_SwordBird;
            }
        }
        else if(zoneIndex == 17) {
            if(isGround) {
                int randomVal = random.nextInt(10);
                if(randomVal < 7) {
                    return ObjectIdConstants.Enemy_CatBall;
                }
                if(randomVal < 9) {
                    return ObjectIdConstants.Enemy_Witch;
                }
                return ObjectIdConstants.Enemy_Skeleton;
            }
            if(isNoCollision) {
                return ObjectIdConstants.Enemy_Bat;
            }
        }
        else if(zoneIndex == 18) {
            if(isGround) {
                if(random.nextInt(10) < 6) {
                    return ObjectIdConstants.Enemy_Skeleton;
                }
                return ObjectIdConstants.Enemy_Witch;
            }
            if(isNoCollision) {
                if(random.nextInt(10) < 8) {
                    return ObjectIdConstants.Enemy_Bat;
                }
                return ObjectIdConstants.Enemy_SwordBird;
            }
        }
        else if(zoneIndex == 19) {
            if(isGround) {
                return ObjectIdConstants.Enemy_Skeleton;
            }
            if(isNoCollision) {
                if(random.nextInt(10) < 9) {
                    return ObjectIdConstants.Enemy_Bat;
                }
                return ObjectIdConstants.Enemy_SwordBird;
            }
        }
        else if(zoneIndex == 20) {
            if(isGround) {
                return ObjectIdConstants.Enemy_Skeleton;
            }
            if(isNoCollision) {
                if(random.nextInt(10) < 9) {
                    return ObjectIdConstants.Enemy_Bat;
                }
                return ObjectIdConstants.Enemy_SwordBird;
            }
        }
        else if(zoneIndex == 21) {
            if(isGround) {
                return ObjectIdConstants.Enemy_Skeleton;
            }
            if(isNoCollision) {
                if(random.nextInt(10) < 9) {
                    return ObjectIdConstants.Enemy_Bat;
                }
                return ObjectIdConstants.Enemy_SwordBird;
            }
        }
        else if(zoneIndex == 22) {
            // Night surface
            if(isGround) {
                return ObjectIdConstants.Enemy_Skeleton;
            }
            if(isNoCollision) {
                if(random.nextInt(10) < 8) {
                    return ObjectIdConstants.Enemy_Bat;
                }
                return ObjectIdConstants.Enemy_SwordBird;
            }
        }
        else if(zoneIndex == 23) {
            if(isGround) {
                int randomVal = random.nextInt(10);
                if(randomVal < 5) {
                    return ObjectIdConstants.Enemy_MiniBoss;
                }
                if(randomVal < 7) {
                    return ObjectIdConstants.Enemy_Skeleton;
                }
                return ObjectIdConstants.Enemy_Witch;
            }
            if(isNoCollision) {
                if(random.nextInt(10) < 5) {
                    return ObjectIdConstants.Enemy_Bat;
                }
                return ObjectIdConstants.Enemy_SwordBird;
            }
        }

        return ObjectIdConstants.Enemy_Bat;
    }

    @Override
    protected void setBatArgs(GameObject enemy, int zoneIndex) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)1); // 0 = start resting / 1 = start flying
        enemy.getArgs().add((short)(random.nextInt(2) + 1)); // Drop type - nothing or coins
        enemy.getArgs().add((short)(random.nextInt(3) + 1)); // UNKNOWN - probably meant to be speed but bugged?
        enemy.getArgs().add((short)0); // Bat type

        enemy.getArgs().add((short)2); // Damage
    }

    private void setSkeletonArgs(GameObject enemy) {
        int facing = getFacing(enemy);
        Integer speedBonus = HolidaySettings.isHalloween2021Mode() ? null : getSpeedBonus(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)(random.nextBoolean() ? 1 : 11)); // Droptype - 1 is coins, 11 for skeleton is either coins or weights
        enemy.getArgs().add((short)(speedBonus == null ? random.nextInt(4) : Math.min(speedBonus, 3))); // Speed
        enemy.getArgs().add((short)random.nextInt(2)); // Collapsed or standing

        int typeVal = random.nextInt(10);
        if(typeVal < 5) {
            enemy.getArgs().add((short)0); // Type
            enemy.getArgs().add((short)(random.nextInt(3) + 3)); // Health
            enemy.getArgs().add((short)(random.nextInt(3) + 2)); // Contact damage
            enemy.getArgs().add((short)0); // Projectile damage
            enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Soul drop
        }
        else if(typeVal < 8) {
            enemy.getArgs().add((short)1); // Type
            enemy.getArgs().add((short)(random.nextInt(6) + 3)); // Health
            enemy.getArgs().add((short)(random.nextInt(3) + 3)); // Contact damage
            enemy.getArgs().add((short)(random.nextInt(3) + 3)); // Projectile damage
            enemy.getArgs().add((short)(random.nextInt(3) + 3)); // Soul drop
        }
        else {
            enemy.getArgs().add((short)2); // Type
            enemy.getArgs().add((short)(random.nextInt(9) + 3)); // Health
            enemy.getArgs().add((short)(random.nextInt(5) + 4)); // Contact damage
            enemy.getArgs().add((short)(random.nextInt(3) + 3)); // Projectile damage
            enemy.getArgs().add((short)(random.nextInt(5) + 4)); // Soul drop
        }

        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // Projectile speed
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

        enemy.getArgs().clear();
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)1); // Speed
        if(HolidaySettings.isHalloweenMode()) {
            enemy.getArgs().add((short)(random.nextBoolean() ? 1 : 11)); // Droptype - 1 is coins, 11 for skeleton is either coins or weights
            enemy.getArgs().add((short)1); // Amount
            enemy.getArgs().add((short)(random.nextInt(11) + 3)); // Health
            enemy.getArgs().add((short)(random.nextInt(5) + 2)); // Contact damage
            enemy.getArgs().add((short)1); // Projectile Speed
            enemy.getArgs().add((short)1); // Projectile Count
            enemy.getArgs().add((short)(random.nextInt(4) + 2)); // Projectile damage
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

    private void setCatBallArgs(GameObject enemy) {
        int facing = getFacing(enemy);
        Integer speedBonus = getSpeedBonus(enemy);

        enemy.getArgs().clear();
        enemy.getArgs().add((short)random.nextInt(2)); // 0 = start on ball, 1 = start off ball
        enemy.getArgs().add((short)facing);
        enemy.getArgs().add((short)(speedBonus == null ? (random.nextBoolean() ? 2 : 4) : (speedBonus <= 0 ? 2 : 4))); // Speed
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
            enemy.getArgs().add((short)2); // Speed
            enemy.getArgs().add((short)(isHardmode ? (random.nextBoolean() ? 10 : 4) : 4)); // Health
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
            enemy.getArgs().add((short)2); // Speed
            enemy.getArgs().add((short)(isHardmode ? (random.nextBoolean() ? 8 : 4) : 4)); // Health
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
            enemy.getArgs().add((short)2); // Speed
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
            enemy.getArgs().add((short)2); // Speed
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

    private void setSwordBirdArgs(GameObject enemy) {
        enemy.getArgs().clear();
        enemy.getArgs().add((short)random.nextInt(2)); // Facing?
        enemy.getArgs().add((short)2); // Drop type
        enemy.getArgs().add((short)2); // Speed?
        enemy.getArgs().add((short)4); // Health
        enemy.getArgs().add((short)8); // Contact damage
        enemy.getArgs().add((short)6); // UNKNOWN
        enemy.getArgs().add((short)(random.nextInt(2) + 2)); // UNKNOWN
        enemy.getArgs().add((short)6); // Projectile damage
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

    private boolean isGroundEnemy(GameObject enemyObject) {
        int enemyId = (int)enemyObject.getId();
        if(enemyId == ObjectIdConstants.Enemy_Bennu) {
            return enemyObject.getArgs().get(0) == 0;
        }
        if(enemyId == ObjectIdConstants.Enemy_Devil) {
            return enemyObject.getArgs().get(10) == 0;
        }
        return GROUND_ENEMIES.contains(enemyId);
    }

    private boolean isNoCollisionEnemy(GameObject enemyObject) {
        int enemyId = (int)enemyObject.getId();
        if(enemyId == ObjectIdConstants.Enemy_Bat) {
            return enemyObject.getArgs().get(0) != 0;
        }
        if(enemyId == ObjectIdConstants.Enemy_Bennu) {
            return enemyObject.getArgs().get(0) != 0;
        }
        return NO_COLLISION_ENEMIES.contains(enemyId);
    }

    private boolean isWaterEnemy(GameObject enemyObject) {
        int enemyId = enemyObject.getId();
        return WATER_ENEMIES.contains(enemyId);
    }

    private int getFacing(GameObject enemy) {
        int originalEnemyId = enemy.getId();
        if(originalEnemyId == ObjectIdConstants.Enemy_Antlion) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Skeleton) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Snouter) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_KodamaRat) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Snake) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Cockatrice) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Condor) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_MaskedMan) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Nozuchi) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Fist) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.RedSkeleton) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Sonic) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_CatBall) {
            return enemy.getArgs().get(1);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Bennu) {
            return enemy.getArgs().get(1);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Gyonin) {
            return enemy.getArgs().get(1);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Hippocamp) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Slime) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Kakoujuu) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Naga) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Garuda) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Bonnacon) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Monocoli) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_JiangShi) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_RongXuanwangCorpse) {
            return enemy.getArgs().get(0);
        }
//        if(originalEnemyId == ObjectIdConstants.Enemy_Hundun) {
//            return enemy.getArgs().get(0);
//        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Pan) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Hanuman) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Enkidu) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Marchosias) {
            return enemy.getArgs().get(0);
        }
        if(originalEnemyId == ObjectIdConstants.Enemy_Witch) {
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
}
