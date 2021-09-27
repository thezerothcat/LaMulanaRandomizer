package lmr.randomizer.rcd.object;

/**
 * 0x0c Moving Platform Not Reversed; Tested
 *     0 - Tile Sheet map=0,1 eveg=2
 *     1 - (0-980) TileX
 *     2 - (0-562) TileY
 *     3 - (20-200) dX
 *     4 - (20-120) dY
 *     5 - (0-6) Displaces the platform sprite number of pixels left. Setting too high can break vertical platforms
 *     6 - (0-20) Displaces the platform sprite num pixels up. Interferes with vert. screen transitioning standing on platform. Setting too high can break horiz platforms
 *     7 - (20-200) hitbox width
 *     8 - (20-120) hitbox height
 *     9 - (-1-1980) Platform left bound in Tile-Block (-1 causes horizontal platforms to wrap around the screen, like the one in hell temple)
 *     10- (80-1320) Platform upper bound in Tile-Block (-1 causes vertical platforms to wrap)
 *     11- (0-520) How far the platform moves right. Setting too low can break vertical platforms
 *     12- (40-1120) How far the platform moves down. Setting too low can break horizontal platforms
 *     13- (0-270) Platform Direction (angle CW from x direction)
 *     14- (0-1) 0 = Stops when it reaches the edge on the side, 1 = moves back and forth
 *     15- (0-1) ??
 *     16- (0-1) ??
 *     17- (100-240) Platform Speed
 *
 * 0x0c - Moving Platform (Object)
 * 0- (0-2) Tile Sheet?
 * 1- (0-980) Tile x
 * 2- (0-562) Tile y
 * 3- (20-200) Tile Width
 * 4- (20-120) Tile Height
 * 5- (0-6) Displaces the platform sprite number of pixels left. Setting too high can break vertical platforms
 * 6- (0-20) Displaces the platform sprite num pixels up. Interferes with vert. screen transitioning standing on platform. Setting too high can break horiz platforms
 * 7- (20-200) hitbox width
 * 8- (20-120) hitbox height
 * 9- (-1-1980) Platform left bound in Tile Block (-1 causes horizontal platforms to wrap around the screen, like the one in hell temple)
 * 10- (80-1320) Platform upper bound in Tile Block (-1 causes vertical platforms to wrap)
 * 11- (0-520) Horizx distance traveled by platform. Setting too low can break vertical platforms
 * 12- (40-1120) Vert distance traveled by platform. Setting too low can break horizontal platforms
 * 13- (0-270) Platform Direction (angle CW from x direction)
 * 14- (0-1) 0 = Stops when it reaches the edge on the side, 1 = moves back and forth
 * 15- (0-1) ?? (0 on sun platform)
 * 16- (0-1) ?? (0 on birth cog wall)
 * 17- (100-240) Platform Speed
 */
public class MovingPlatform extends GameObject{
    public static final int GraphicsFile_Map = 0;
    public static final int GraphicsFile_Eveg = 2;

    public static final int LeftBound_Wrap = -1;
    public static final int TopBound_Wrap = -1;

    public MovingPlatform(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 18);
        setId(ObjectIdConstants.MovingPlatform);
        setX(x);
        setY(y);
    }

    public MovingPlatform(GameObject gameObject) {
        super(gameObject);
    }

    public void setGraphicsFile(int graphicsFile) {
        getArgs().set(0, (short)graphicsFile);
    }

    public void setTileX(int tileX) {
        getArgs().set(1, (short)tileX);
    }

    public void setTileY(int tileY) {
        getArgs().set(2, (short)tileY);
    }

    public void setTileWidth(int tileWidth) {
        getArgs().set(3, (short)tileWidth);
    }

    public void setTileHeight(int tileHeight) {
        getArgs().set(4, (short)tileHeight);
    }

    public void setLeftShift(int pixelsToShiftLeft) {
        getArgs().set(5, (short)pixelsToShiftLeft);
    }

    public void setUpShift(int pixelsToShiftUp) {
        getArgs().set(6, (short)pixelsToShiftUp);
    }

    public void setHitboxWidth(int hitboxWidth) {
        getArgs().set(7, (short)hitboxWidth);
    }

    public void setHitboxHeight(int hitboxHeight) {
        getArgs().set(8, (short)hitboxHeight);
    }

    public void setLeftBound(int leftBound) {
        getArgs().set(9, (short)leftBound);
    }

    public void setTopBound(int topBound) {
        getArgs().set(10, (short)topBound);
    }

    public void setHorizontalDistance(int horizontalDistance) {
        getArgs().set(11, (short)horizontalDistance);
    }

    public void setVerticalDistance(int verticalDistance) {
        getArgs().set(12, (short)verticalDistance);
    }

    public void setPlatformDirection(int angleClockwiseFromX) {
        getArgs().set(13, (short)angleClockwiseFromX);
    }

    public void setPlatformBehavior(int platformBehavior) {
        getArgs().set(14, (short)platformBehavior);
    }

    public void setArg15(int arg15) {
        getArgs().set(15, (short)arg15);
    }

    public void setArg16(int arg16) {
        getArgs().set(16, (short)arg16);
    }

    public void setPlatformSpeed(int speed) {
        getArgs().set(17, (short)speed);
    }
}
