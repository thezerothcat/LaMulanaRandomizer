package lmr.randomizer.rcd.object;

/**
 * 0x1f - enemy-maus-ghosts (Effect)
 * 0- (120) Spawning Period
 * 1- (2-3) Maximum Ghosts
 * 2- (0)
 * 3- (0) Speed AND Drop-type
 * 4- (1) Health
 * 5- (2) Damage AND Soul
 * 6- (3)
 */
public class GhostSpawner extends GameObject {
    public GhostSpawner(ObjectContainer objectContainer) {
        super(objectContainer, 7);
        setId(ObjectIdConstants.GhostSpawner);
        setX(-1);
        setY(-1);
    }

    public void setSpawnRate(int spawnRate) {
        getArgs().set(0, (short)spawnRate);
    }

    public void setMaxGhosts(int maxGhosts) {
        getArgs().set(1, (short)maxGhosts);
    }

    public void setArg2(int arg2) {
        getArgs().set(2, (short)arg2);
    }

    public void setGhostSpeedAndDropType(int ghostSpeedAndDropType) {
        getArgs().set(3, (short)ghostSpeedAndDropType);
    }

    public void setGhostHealth(int ghostHealth) {
        getArgs().set(4, (short)ghostHealth);
    }

    public void setGhostDamageAndSoul(int ghostDamageAndSoul) {
        getArgs().set(5, (short)ghostDamageAndSoul);
    }

    public void setArg6(int arg6) {
        getArgs().set(6, (short)arg6);
    }
}
