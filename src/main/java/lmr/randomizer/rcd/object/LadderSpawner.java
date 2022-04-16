package lmr.randomizer.rcd.object;

/**
 * 0x07 - Ladder Spawner (Object)
 * 0- (0-1) 0 = From Top, 1 = From Bottom
 * 1- (4-18) Height in graphical tiles
 * 2- (0-2) Ladder Type? Sheet?
 * 3- (0)
 * 4- (360-900) Ladder Tile? (x?)
 * 5- (0-720) Ladder Tile? (y?)
 * 6- (0-1) Spawn Style: 0 = Standard, 1 = Philosopher
 * 7- (0-1)
 */
public class LadderSpawner extends GameObject {
    public static final short FromTop = 0;
    public static final short FromBottom = 1;

    public static final short ImageFile_Map = 0;
    public static final short ImageFile_Eveg = 2;

    public static final short StandardLadder = 0;
    public static final short PhilosopherLadder = 1;

    public static final short Collision_TwoTilesBelow = 0;
    public static final short Collision_AtTop = 1;

    public LadderSpawner(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 8);
        setId(ObjectIdConstants.Ladder);
        setX(x);
        setY(y);
    }

    public void setSpawnDirection(short spawnDirection) {
        getArgs().set(0, spawnDirection);
    }

    public void setHeight(int height) {
        getArgs().set(1, (short)height);
    }

    public void setGraphicsFile(int graphicsFile) {
        getArgs().set(2, (short)graphicsFile);
    }

    public void setArg3(int arg3) {
        getArgs().set(3, (short)arg3);
    }

    public void setImageX(int imageX) {
        getArgs().set(4, (short)imageX);
    }

    public void setImageY(int imageY) {
        getArgs().set(5, (short)imageY);
    }

    public void setLadderType(short ladderType) {
        getArgs().set(6, ladderType);
    }

    public void setCollisionStarts(short collisionType) {
        getArgs().set(7, collisionType);
    }
}
