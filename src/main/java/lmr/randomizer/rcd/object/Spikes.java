package lmr.randomizer.rcd.object;

/**
 * 0x10 - spikes-static (hurtbox?) (Object)
 * 0 - (0-1) from up
 * 1 - (0-1) from right
 * 2 - (0-1) from down
 * 3 - (0-1) from left
 * 4 - (1-32) horiz size in tiles
 * 5 - (1-24) vert size in tiles
 * 6 - (0-1) (0 = Percent, 1 = flat)
 * 7 - (10-150) damage
 */
public class Spikes extends GameObject {
    public static final int FACE_UP = 0;
    public static final int FACE_RIGHT = 1;
    public static final int FACE_DOWN = 2;
    public static final int FACE_LEFT = 3;
    public static final int FACE_ALL = 4;

    public Spikes(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 8);
        setId(ObjectIdConstants.Spikes);
        setX(x);
        setY(y);
    }

    public Spikes(GameObject gameObject) {
        super(gameObject);
    }

    public void setDirections(boolean fromUp, boolean fromRight, boolean fromDown, boolean fromLeft) {
        getArgs().set(0, (short)(fromUp ? 1 : 0));
        getArgs().set(1, (short)(fromRight ? 1 : 0));
        getArgs().set(2, (short)(fromDown ? 1 : 0));
        getArgs().set(3, (short)(fromLeft ? 1 : 0));
    }

    public void setFacing(int facing) {
        getArgs().set(0, (short)((facing == FACE_UP || facing == FACE_ALL) ? 1 : 0));
        getArgs().set(1, (short)((facing == FACE_RIGHT || facing == FACE_ALL) ? 1 : 0));
        getArgs().set(2, (short)((facing == FACE_DOWN || facing == FACE_ALL) ? 1 : 0));
        getArgs().set(3, (short)((facing == FACE_LEFT || facing == FACE_ALL) ? 1 : 0));
    }

    public void setFacingDown() {
        getArgs().set(0, (short)0);
        getArgs().set(1, (short)0);
        getArgs().set(2, (short)1);
        getArgs().set(3, (short)0);
    }

    public void setFacingUp() {
        getArgs().set(0, (short)1);
        getArgs().set(1, (short)0);
        getArgs().set(2, (short)0);
        getArgs().set(3, (short)0);
    }

    public void setFacingAll() {
        getArgs().set(0, (short)1);
        getArgs().set(1, (short)1);
        getArgs().set(2, (short)1);
        getArgs().set(3, (short)1);
    }

    public int getWidth() {
        return getArgs().get(4);
    }

    public void setWidth(int horizontalTiles) {
        getArgs().set(4, (short)horizontalTiles);
    }

    public int getHeight() {
        return getArgs().get(5);
    }

    public void setHeight(int verticalTiles) {
        getArgs().set(5, (short)verticalTiles);
    }

    public void setDamage(boolean flatDamage, int damage) {
        getArgs().set(6, (short)(flatDamage ? 1 : 0));
        getArgs().set(7, (short)damage);
    }

    public static boolean isFacingUp(GameObject gameObject) {
        return gameObject.getArgs().get(0) > 0 && gameObject.getArgs().get(1) == 0 && gameObject.getArgs().get(2) == 0 && gameObject.getArgs().get(3) == 0;
    }
    public static boolean isFacingDown(GameObject gameObject) {
        return gameObject.getArgs().get(0) == 0 && gameObject.getArgs().get(1) == 0 && gameObject.getArgs().get(2) > 0 && gameObject.getArgs().get(3) == 0;
    }
}
