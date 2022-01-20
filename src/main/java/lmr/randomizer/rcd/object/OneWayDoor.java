package lmr.randomizer.rcd.object;

/**
 * 0x000f one-way-door (Object)
 * 0 - (0-1) Direction (0 = opens right, 1 = opens left)
 * 1 - (0)
 * 2 - (3) Tilesheet
 * 3 - (818) Tilesheet x
 * 4 - (0) Tilesheet y
 */
public class OneWayDoor extends GameObject {
    public static int EXIT_RIGHT = 0;
    public static int EXIT_LEFT = 1;

    public OneWayDoor(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 5);
        setId(ObjectIdConstants.OneWayDoor);
        setX(x);
        setY(y);
        setDefaults();
    }

    public void setDefaults() {
        setImageFile(3);
        setImageX(818);
        setImageY(0);
    }

    public void setDirection(int direction) {
        getArgs().set(0, (short)direction);
    }

    public void setImageFile(int imageFile) {
        getArgs().set(2, (short)imageFile);
    }

    public void setImageFile(String imageFile) {
        // Image 0=mapxx_1.png 1=evegxx.png 2=00prof.png 3=02comenemy.png 4=6=00item.png 5=01menu.png 6=4=00item.png Default:01effect.png
        if(imageFile.startsWith("map") && imageFile.endsWith("_1.png")) {
            getArgs().set(2, (short)0);
        }
        else if(imageFile.startsWith("eveg") && imageFile.endsWith(".png")) {
            getArgs().set(2, (short)1);
        }
        else if("00prof.png".equals(imageFile)) {
            getArgs().set(2, (short)2);
        }
        else if("02comenemy.png".equals(imageFile)) {
            getArgs().set(2, (short)3);
        }
        else if("00item.png".equals(imageFile)) {
            getArgs().set(2, (short)4);
        }
        else if("01menu.png".equals(imageFile)) {
            getArgs().set(2, (short)5);
        }
        else if("01effect.png".equals(imageFile)) {
            getArgs().set(2, (short)-1);
        }
        else {
            // todo: throw error for invalid option
        }
    }

    public void setImageX(int imageX) {
        // X-component of top left corner for where to begin drawing, on the image file
        getArgs().set(3, (short)imageX);
    }

    public void setImageY(int imageY) {
        // Y-component of top left corner for where to begin drawing, on the image file
        getArgs().set(4, (short)imageY);
    }
}
