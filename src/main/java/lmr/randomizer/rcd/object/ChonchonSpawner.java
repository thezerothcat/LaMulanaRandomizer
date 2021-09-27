package lmr.randomizer.rcd.object;

/**
 * 0x6f - enemy-goddess-medusaheads (Effect)
 * 0- (120) Delay between spawns
 * 1- (0-4) ?
 * 2- (2) Number of Chonchons
 * 3- (60) Oscillation Amplitude
 * 4- (1) Drop Type
 * 5- (2) Speed
 * 6- (2) Health
 * 7- (2) Damage
 * 8- (4) Soul
 */
public class ChonchonSpawner extends GameObject {
    public ChonchonSpawner(ObjectContainer objectContainer) {
        super(objectContainer, 9);
        setId(ObjectIdConstants.ChonchonSpawner);
        setDefaults();
        setX(-1);
        setY(-1);
    }

    public void setDefaults() {
        setSpawnRate(120);
        setArg1(0); // 4 is also used sometimes
        setMaxChonchons(2);
        setOscillationAmplitude(60);
        setDropType(DropType.COINS.getValue());
        setSpeed(2);
        setHealth(2);
        setDamage(2);
        setSoul(4);
    }

    public void setSpawnRate(int spawnRate) {
        getArgs().set(0, (short)spawnRate);
    }

    public void setArg1(int arg1) {
        getArgs().set(1, (short)arg1);
    }

    public void setMaxChonchons(int maxChonchons) {
        getArgs().set(2, (short)maxChonchons);
    }

    public void setOscillationAmplitude(int oscillationAmplitude) {
        getArgs().set(3, (short)oscillationAmplitude);
    }

    public void setDropType(int dropType) {
        getArgs().set(4, (short)dropType);
    }

    public void setSpeed(int speed) {
        getArgs().set(5, (short)speed);
    }

    public void setHealth(int health) {
        getArgs().set(6, (short)health);
    }

    public void setDamage(int damage) {
        getArgs().set(7, (short)damage);
    }

    public void setSoul(int soul) {
        getArgs().set(8, (short)soul);
    }
}
