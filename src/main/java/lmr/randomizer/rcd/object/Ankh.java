package lmr.randomizer.rcd.object;

/**
 * 0x2e - Ankh (Object)
 * 0- (0-7) Boss. 8(mother) is valid.
 * 1- (0-3) Speed
 * 2- (32-300) Health
 * 3- (16-80) Contact damage
 * The next parameters do different things depending on which boss it is.
 * 4- (0-700)
 * 5- (4-48)
 * 6- (0-60)
 * 7- (0-64)
 * 8- (0-1800)
 * 9- (0-85)
 * 10- (0-180)
 * 11- (0-14000)
 * 12- (0-470)
 * 13- (0-2700)
 * 14- (0-100)
 * 15- (0-55)
 * 16- (0-260)
 * 17- (0-864)
 * 18- (0-90)
 * 19- (0-100)
 * 20- (0-85)
 * 21- (0-64)
 * 22- (0-48)
 * 23- (0-8)
 * 24- (-1-17) Victory Field warp (-1 for same as Ankh)
 * 25- (-1-901) Victory Tile-block and Screen warp (-1 for same as Ankh) (decimal number in the form XXYY where XX = target tile-block and YY = target screen)
 * 26- (1500-2920) Victory combined x-y position (decimal number in the form XXYY where XX = target x-pos and YY = target y-pos in G-tiles)
 * 27- (0-3) Victory Splat animation (0 = Normal, 1 = Splat from position, 2 = splat from top of screen, 3 = splat flowing left, 4 = splat flowing right, 5+ = no splat, place lemeza near top-left of screen, fail to load graphics)
 * 28- (-1-17) Defeat Field warp
 * 29- (-1-901) Defeat Tile-block and Screen warp
 * 30- (911-2920) Defeat combined x-y position
 * 31- (0-3) Defeat Splat animation
 */
public class Ankh extends GameObject {
    public static final int Amphisbaena = 0;
    public static final int Sakit = 1;
    public static final int Ellmac = 2;
    public static final int Bahamut = 3;
    public static final int Viy = 4;
    public static final int Palenque = 5;
    public static final int Baphomet = 6;
    public static final int Tiamat = 7;
    public static final int Mother = 8;

    public Ankh(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 32);
        setId((short)0x2e);
        setX(x);
        setY(y);
    }

    public void setBoss(int bossNumber, boolean hardmodeDefaultValues) {
        setBossNumber(bossNumber);

        if(bossNumber == Amphisbaena) {
            setAmphisbaenaDefaults(hardmodeDefaultValues);
        }
        else if(bossNumber == Sakit) {
            setSakitDefaults(hardmodeDefaultValues);
        }
        else if(bossNumber == Ellmac) {
            setEllmacDefaults(hardmodeDefaultValues);
        }
        else if(bossNumber == Bahamut) {
            setBahamutDefaults(hardmodeDefaultValues);
        }
        else if(bossNumber == Viy) {
            setViyDefaults(hardmodeDefaultValues);
        }
        else if(bossNumber == Palenque) {
            setPalenqueDefaults(hardmodeDefaultValues);
        }
    }

    private void setAmphisbaenaDefaults(boolean hardmodeDefaultValues) {
        resetBossParams();

        setSpeed(hardmodeDefaultValues ? 1 : 0);
        setHealth(hardmodeDefaultValues ? 48 : 32);
        setContactDamage(hardmodeDefaultValues ? 24 : 16);
        setAmphisbaenaFlameSpeed(hardmodeDefaultValues ? 2 : 1);
        setAmphisbaenaFlameDamage(hardmodeDefaultValues ? 12 : 6);
    }

    public void setAmphisbaenaFlameSpeed(int speed) {
        getArgs().set(4, (short)speed);
    }

    public void setAmphisbaenaFlameDamage(int damage) {
        getArgs().set(5, (short)damage);
    }

    private void setSakitDefaults(boolean hardmodeDefaultValues) {
        resetBossParams();

        setSpeed(2);
        setHealth(hardmodeDefaultValues ? 64 : 45);
        setContactDamage(hardmodeDefaultValues ? 24 : 16); // Also Sakit's chain-punch damage
        setSakitOrbProjectileSpeed(hardmodeDefaultValues ? 1 : 2);
        setSakitOrbDamage(hardmodeDefaultValues ? 16 : 8);
        setSakitOrbChargeTime(60);
        setSakitChainRecoilAndFlameDamage(hardmodeDefaultValues ? 24 : 16);
        setSakitPhase2Health(hardmodeDefaultValues ? 24 : 20);
        setSakitPhase2DelayBetweenActions(hardmodeDefaultValues ? 75 : 85);
        setSakitRocketPunchSpeed(hardmodeDefaultValues ? 2 : 1);
        setSakitRocketPunchDamage(hardmodeDefaultValues ? 32 : 24);
        setSakitStatueBreakFlag(470);
        setSakitStatueBreakFlagValue(1);
        setSakitFallingRockDamage(3);
        setSakitSplitRockDamage(hardmodeDefaultValues ? 1 : 0);
    }

    public void setSakitOrbProjectileSpeed(int speed) {
        getArgs().set(4, (short)speed);
    }

    public void setSakitOrbDamage(int damage) {
        getArgs().set(5, (short)damage);
    }

    public void setSakitOrbChargeTime(int chargeTime) {
        getArgs().set(6, (short)chargeTime);
    }

    public void setSakitChainRecoilAndFlameDamage(int damage) {
        getArgs().set(7, (short)damage);
    }

    public void setSakitPhase2Health(int health) {
        getArgs().set(8, (short)health);
    }

    public void setSakitPhase2DelayBetweenActions(int delay) {
        getArgs().set(9, (short)delay);
    }

    public void setSakitRocketPunchSpeed(int speed) {
        getArgs().set(10, (short)speed);
    }

    public void setSakitRocketPunchDamage(int damage) {
        getArgs().set(11, (short)damage);
    }

    public void setSakitStatueBreakFlag(int flag) {
        getArgs().set(12, (short)flag);
    }

    public void setSakitStatueBreakFlagValue(int flagValue) {
        getArgs().set(13, (short)flagValue);
    }

    public void setSakitFallingRockDamage(int damage) {
        getArgs().set(14, (short)damage);
    }

    public void setSakitSplitRockDamage(int damage) {
        getArgs().set(15, (short)damage);
    }

    private void setEllmacDefaults(boolean hardmodeDefaultValues) {
        resetBossParams();

        setSpeed(hardmodeDefaultValues ? 3 : 2);
        setHealth(hardmodeDefaultValues ? 72 : 54);
        setContactDamage(hardmodeDefaultValues ? 32 : 18);
        setEllmacProjectileSpeed(2);
        setEllmacProjectileDamage(hardmodeDefaultValues ? 16 : 8);
        setEllmacProjectileLingeringFlameDamage(hardmodeDefaultValues ? 32 : 16);
        setEllmacChargeDamage(hardmodeDefaultValues ? 64 : 40);
        setEllmacActionCooldown(hardmodeDefaultValues ? 150 : 200);
        setEllmacTrackSegmentsArg9(hardmodeDefaultValues ? 24 : 32);
        setEllmacTrackSegmentsLengthArg10(hardmodeDefaultValues ? 48 : 64);
        setEllmacTrackSpeed(14000);
        setEllmacEnrageHealthThreshold(hardmodeDefaultValues ? 36 : 30);
        setEllmacEnrageSpeedIncrease(hardmodeDefaultValues ? 20 : 10);
        setEllmacTrolleyRemovalFlag(67);
    }

    public void setEllmacProjectileSpeed(int speed) {
        getArgs().set(4, (short)speed);
    }

    public void setEllmacProjectileDamage(int damage) {
        getArgs().set(5, (short)damage);
    }

    public void setEllmacProjectileLingeringFlameDamage(int damage) {
        getArgs().set(6, (short)damage);
    }

    public void setEllmacChargeDamage(int damage) {
        getArgs().set(7, (short)damage);
    }

    public void setEllmacActionCooldown(int cooldown) {
        getArgs().set(8, (short)cooldown);
    }

    public void setEllmacTrackSegmentsArg9(int arg9) {
        getArgs().set(9, (short)arg9);
    }

    public void setEllmacTrackSegmentsLengthArg10(int arg10) {
        getArgs().set(10, (short)arg10);
    }

    public void setEllmacTrackSpeed(int speed) {
        getArgs().set(11, (short)speed);
    }

    public void setEllmacEnrageHealthThreshold(int healthThreshold) {
        getArgs().set(12, (short)healthThreshold);
    }

    public void setEllmacEnrageSpeedIncrease(int speedIncrease) {
        getArgs().set(13, (short)speedIncrease);
    }

    public void setEllmacTrolleyRemovalFlag(int flag) {
        getArgs().set(14, (short)flag);
    }

    private void setBahamutDefaults(boolean hardmodeDefaultValues) {
        resetBossParams();

        setSpeed(hardmodeDefaultValues ? 3 : 2);
        setHealth(hardmodeDefaultValues ? 72 : 54);
        setContactDamage(hardmodeDefaultValues ? 32 : 18);
        setBahamutProjectileSpeed(2);
        setBahamutProjectileDamage(hardmodeDefaultValues ? 32 : 16);
        setBahamutCheeseBallDamage(hardmodeDefaultValues ? 32 : 24);
        setBahamutCheeseBallCount(50);
        setBahamutDelayBetweenAttacks(hardmodeDefaultValues ? 45 : 90);
        setBahamutEnrageHealthThreshold(hardmodeDefaultValues ? 32 : 24);
        setBahamutEnrageSpeedChange(hardmodeDefaultValues ? 80 : 85);
        setBahamutBoatRemovalFlag(67);
    }

    public void setBahamutProjectileSpeed(int speed) {
        getArgs().set(4, (short)speed);
    }

    public void setBahamutProjectileDamage(int damage) {
        getArgs().set(5, (short)damage);
    }

    public void setBahamutCheeseBallDamage(int damage) {
        getArgs().set(6, (short)damage);
    }

    public void setBahamutCheeseBallCount(int cheeseBallCount) {
        getArgs().set(7, (short)cheeseBallCount);
    }

    public void setBahamutDelayBetweenAttacks(int delay) {
        getArgs().set(8, (short)delay);
    }

    public void setBahamutEnrageHealthThreshold(int healthThreshold) {
        getArgs().set(9, (short)healthThreshold);
    }

    public void setBahamutEnrageSpeedChange(int speedIncrease) {
        getArgs().set(10, (short)speedIncrease);
    }

    public void setBahamutBoatRemovalFlag(int flag) {
        getArgs().set(11, (short)flag);
    }

    private void setViyDefaults(boolean hardmodeDefaultValues) {
        resetBossParams();

        setSpeed(2);
        setHealth(hardmodeDefaultValues ? 100 : 80);
        setContactDamage(hardmodeDefaultValues ? 24 : 16);
        setViyVerticalSpeed(hardmodeDefaultValues ? 700 : 500);
        setViyTentacleHealth(hardmodeDefaultValues ? 6 : 4);
        setViyTentacleSpeedAndDamage(hardmodeDefaultValues ? 2 : 1);
        setViyArg7(hardmodeDefaultValues ? 16 : 8);
        setViyTentacleRespawnTime(hardmodeDefaultValues ? 1200 : 1800);
        setViyEyeFlameProjectileSpeed(1);
        setViyEyeFlameProjectileDamage(hardmodeDefaultValues ? 12 : 6);
        setViyEyeLaserSpeed(hardmodeDefaultValues ? 3 : 2);
        setViyEyeLaserDamage(hardmodeDefaultValues ? 32 : 24);
        setViyBigLaserChargeTime(hardmodeDefaultValues ? 80 : 100);
        setViyBigLaserDamage(90);
        setViyMinionHealth(hardmodeDefaultValues ? 6 : 4);
        setViyMinionDamage(hardmodeDefaultValues ? 6 : 3);
        setViyFloorBreakFlag(864);
        setViyFloorBreakFlagValue(1);
        setViyEnrageHealthThreshold(hardmodeDefaultValues ? 50 : 40);
        setViyEnrageSpeedChange(hardmodeDefaultValues ? 75 : 85);
    }

    public void setViyVerticalSpeed(int speed) {
        getArgs().set(4, (short)speed);
    }

    public void setViyTentacleHealth(int health) {
        getArgs().set(5, (short)health);
    }

    public void setViyTentacleSpeedAndDamage(int speedAndDamage) {
        getArgs().set(6, (short)speedAndDamage);
    }

    public void setViyArg7(int arg7) {
        getArgs().set(7, (short)arg7);
    }

    public void setViyTentacleRespawnTime(int respawnTime) {
        getArgs().set(8, (short)respawnTime);
    }

    public void setViyEyeFlameProjectileSpeed(int speed) {
        getArgs().set(9, (short)speed);
    }

    public void setViyEyeFlameProjectileDamage(int damage) {
        getArgs().set(10, (short)damage);
    }

    public void setViyEyeLaserSpeed(int speed) {
        getArgs().set(11, (short)speed);
    }

    public void setViyEyeLaserDamage(int damage) {
        getArgs().set(12, (short)damage);
    }

    public void setViyBigLaserChargeTime(int chargeTime) {
        getArgs().set(13, (short)chargeTime);
    }

    public void setViyBigLaserDamage(int damage) {
        getArgs().set(14, (short)damage);
    }

    public void setViyMinionHealth(int health) {
        getArgs().set(15, (short)health);
    }

    public void setViyMinionDamage(int damage) {
        getArgs().set(16, (short)damage);
    }

    public void setViyFloorBreakFlag(int flag) {
        getArgs().set(17, (short)flag);
    }

    public void setViyFloorBreakFlagValue(int flagValue) {
        getArgs().set(18, (short)flagValue);
    }

    public void setViyEnrageHealthThreshold(int healthThreshold) {
        getArgs().set(19, (short)healthThreshold);
    }

    public void setViyEnrageSpeedChange(int speedChange) {
        getArgs().set(20, (short)speedChange);
    }

    private void setPalenqueDefaults(boolean hardmodeDefaultValues) {
        resetBossParams();

        setSpeed(hardmodeDefaultValues ? 2 : 1);
        setHealth(hardmodeDefaultValues ? 120 : 100);
        setContactDamage(hardmodeDefaultValues ? 80 : 64);
        setPalenqueLaserTurretSpeed(hardmodeDefaultValues ? 2 : 1);
        setPalenqueArg5(hardmodeDefaultValues ? 12 : 6);
        setPalenqueLaserFireRateAndLength(hardmodeDefaultValues ? 20 : 30);
        setPalenqueLaserTurretDamage(hardmodeDefaultValues ? 24 : 10);
        setPalenqueArg8(hardmodeDefaultValues ? 10 : 5);
        setPalenqueArg9(hardmodeDefaultValues ? 64 : 32);
        setPalenqueArg10(hardmodeDefaultValues ? 80 : 48);
        setPalenqueForwardBulletSpraySpeed(1);
        setPalenqueForwardBulletSprayDamage(hardmodeDefaultValues ? 16 : 8);
        setPalenqueLobbedBulletSpeed(2);
        setPalenqueLobbedBulletDamage(hardmodeDefaultValues ? 24 : 16);
        setPalenqueLobbedBulletLingeringFlameDamage(hardmodeDefaultValues ? 32 : 24);
        setPalenqueChargeDamage(hardmodeDefaultValues ? 100 : 80);
        setPalenquePlaneArg(13); // Changing makes the plane not show up; set to 0 if placing an ankh at the fake-ankh in the other part of Extinction
        setPalenqueLaserDuration(hardmodeDefaultValues ? 60 : 90);
        setPalenqueLaserDamage(hardmodeDefaultValues ? 100 : 64);
        setPalenqueMuralArg(12); // Changing this makes the mural not open; set to 0 if placing an ankh at the fake-ankh in the other part of Extinction
        setPalenqueWallHealth(hardmodeDefaultValues ? 5 : 4);
        setPalenqueWallCrashDamage(hardmodeDefaultValues ? 48 : 24);

        setVictoryDestination(6,9, 1, 300, 380);
        setVictorySplatAnimation(0);
        setDefeatDestination(6,9, 1, 300, 380);
        setDefeatSplatAnimation(0);
    }

    public void setPalenqueLaserTurretSpeed(int speed) {
        getArgs().set(4, (short)speed);
    }

    public void setPalenqueArg5(int arg5) {
        getArgs().set(5, (short)arg5);
    }

    public void setPalenqueLaserFireRateAndLength(int laserFireRateAndLength) {
        getArgs().set(6, (short)laserFireRateAndLength);
    }

    public void setPalenqueLaserTurretDamage(int damage) {
        getArgs().set(7, (short)damage);
    }

    public void setPalenqueArg8(int arg8) {
        getArgs().set(8, (short)arg8);
    }

    public void setPalenqueArg9(int arg9) {
        getArgs().set(9, (short)arg9);
    }

    public void setPalenqueArg10(int arg10) {
        getArgs().set(10, (short)arg10);
    }

    public void setPalenqueForwardBulletSpraySpeed(int speed) {
        getArgs().set(11, (short)speed);
    }

    public void setPalenqueForwardBulletSprayDamage(int damage) {
        getArgs().set(12, (short)damage);
    }

    public void setPalenqueLobbedBulletSpeed(int speed) {
        getArgs().set(13, (short)speed);
    }

    public void setPalenqueLobbedBulletDamage(int damage) {
        getArgs().set(14, (short)damage);
    }

    public void setPalenqueLobbedBulletLingeringFlameDamage(int damage) {
        getArgs().set(15, (short)damage);
    }

    public void setPalenqueChargeDamage(int damage) {
        getArgs().set(16, (short)damage);
    }

    public void setPalenquePlaneArg(int planeArg) {
        getArgs().set(17, (short)planeArg); // Changing makes the plane not show up
    }

    public void setPalenqueLaserDuration(int duration) {
        getArgs().set(18, (short)duration);
    }

    public void setPalenqueLaserDamage(int damage) {
        getArgs().set(19, (short)damage);
    }

    public void setPalenqueMuralArg(int muralArg) {
        getArgs().set(20, (short)muralArg); // Changing this makes the mural not open
    }

    public void setPalenqueWallHealth(int health) {
        getArgs().set(21, (short)health);
    }

    public void setPalenqueWallCrashDamage(int damage) {
        getArgs().set(22, (short)damage);
    }

    private void resetBossParams() {
        for(int i = 1; i < 24; i++) {
            getArgs().set(i, (short)0);
        }
    }

    public void setBossNumber(int bossNumber) {
        getArgs().set(0, (short)bossNumber);
    }

    public void setSpeed(int speed) {
        getArgs().set(1, (short)speed);
    }

    public void setHealth(int health) {
        getArgs().set(2, (short)health);
    }

    public void setContactDamage(int contactDamage) {
        // Also Sakit's chain-punch damage
        getArgs().set(3, (short)contactDamage);
    }

    public void setVictoryDestination(int destZone, int destRoom, int destScreen, int x, int y) {
        getArgs().set(24, (short)destZone); // -1 for same as Ankh
        getArgs().set(25, (short)(destRoom * 100 + destScreen)); // Victory Tile-block and Screen warp (-1 for same as Ankh) (decimal number in the form XXYY where XX = target tile-block and YY = target screen)
        getArgs().set(26, (short)((x / 20) * 100 + (y / 20))); // Victory combined x-y position (decimal number in the form XXYY where XX = target x-pos and YY = target y-pos in G-tiles)
    }

    public void setVictorySplatAnimation(int splatAnimation) {
        // 0 = Normal, 1 = Splat from position, 2 = splat from top of screen, 3 = splat flowing left, 4 = splat flowing right, 5+ = no splat, place lemeza near top-left of screen, fail to load graphics
        getArgs().set(27, (short)splatAnimation);
    }

    public void setDefeatDestination(int destZone, int destRoom, int destScreen, int x, int y) {
        getArgs().set(28, (short)destZone); // -1 for same as Ankh
        getArgs().set(29, (short)(destRoom * 100 + destScreen)); // Victory Tile-block and Screen warp (-1 for same as Ankh) (decimal number in the form XXYY where XX = target tile-block and YY = target screen)
        getArgs().set(30, (short)((x / 20) * 100 + (y / 20))); // Defeat combined x-y position (decimal number in the form XXYY where XX = target x-pos and YY = target y-pos in G-tiles)
    }

    public void setDefeatSplatAnimation(int splatAnimation) {
        // 0 = Normal, 1 = Splat from position, 2 = splat from top of screen, 3 = splat flowing left, 4 = splat flowing right, 5+ = no splat, place lemeza near top-left of screen, fail to load graphics
        getArgs().set(31, (short)splatAnimation);
    }
}
