package lmr.randomizer.rcd.object;

import lmr.randomizer.util.ZoneConstants;

/**
 * 0x32 trigger button Reversed
 *     0 -
 *     1 - y offset
 *     2 - sink frames
 *     3 - rise frames
 *     4 - type. 0: Lemeza activates 1: Enemy Activates 2: Enemy + Lemeza Activates 3+ ???
 *     5 - if <=1 then map else eveg
 *     6 - tex u
 *     7 - tex v
 *     8 - tex width
 *     9 - tex height
 *     10- detection size (centered)
 *
 *     update 0 // (test) if world[idx]==val then active else inactive
 *     run updates 1 and 3 when deactivated if initially active
 *     run update 2 when activated if initially deactivated
 */
public class PressurePlate extends GameObject {
    public static final int Detection_Lemeza = 0;
    public static final int Detection_Enemy = 1;
    public static final int Detection_All = 2;

    public static final int GraphicsFile_Map = 0;
    public static final int GraphicsFile_Eveg = 2;

    public PressurePlate(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 11);
        setId(ObjectIdConstants.PressurePlate);
        setX(x);
        setY(y);
        setDefaults();
    }

    public void setDefaults() {
        setArg0(0);
        setSinkFrames(20);
        setRiseFrames(40);
        setGraphicsFileSize(40, 20);
        setDetectionWidth(40);
    }

    public void setArg0(int arg0) {
        getArgs().set(0, (short)arg0);
    }

    public void setYOffset(int offset) {
        getArgs().set(1, (short)offset);
    }

    public void setSinkFrames(int frames) {
        getArgs().set(2, (short)frames);
    }

    public void setRiseFrames(int frames) {
        getArgs().set(3, (short)frames);
    }

    public void setDetectionType(int detectionType) {
        getArgs().set(4, (short)detectionType);
    }

    public void setGraphicsFile(int graphicsFile) {
        getArgs().set(5, (short)graphicsFile);
    }

    public void setGraphicsFilePosition(int x, int y) {
        getArgs().set(6, (short)x);
        getArgs().set(7, (short)y);
    }

    public void setGraphicsFileSize(int width, int height) {
        getArgs().set(8, (short)width);
        getArgs().set(9, (short)height);
    }

    public void setDetectionWidth(int width) {
        getArgs().set(10, (short)width);
    }

    public void setGraphicsFromZone(int zoneIndex) {
        if(zoneIndex == ZoneConstants.SURFACE) {
            setYOffset(-6);
            setGraphicsFile(GraphicsFile_Eveg);
            setGraphicsFilePosition(0, 160);
        }
        else if(zoneIndex == ZoneConstants.GUIDANCE) {
            setYOffset(-6);
            setGraphicsFile(GraphicsFile_Map);
            setGraphicsFilePosition(20, 160);
        }
        else if(zoneIndex == ZoneConstants.MAUSOLEUM) {
            setYOffset(-6);
            setGraphicsFile(GraphicsFile_Map);
            setGraphicsFilePosition(0, 200);
        }
        else if(zoneIndex == ZoneConstants.SUN) {
            setYOffset(-6);
            setGraphicsFile(GraphicsFile_Map);
            setGraphicsFilePosition(40, 120);
        }
        else if(zoneIndex == ZoneConstants.SPRING) {
            setYOffset(-6);
            setGraphicsFile(GraphicsFile_Map);
            setGraphicsFilePosition(60, 40);
        }
        else if(zoneIndex == ZoneConstants.INFERNO) {
            setYOffset(-6);
            setGraphicsFile(GraphicsFile_Map);
            setGraphicsFilePosition(160, 60);
        }
        else if(zoneIndex == ZoneConstants.EXTINCTION) {
            setYOffset(-6);
            setGraphicsFile(GraphicsFile_Map);
            setGraphicsFilePosition(160, 60);
        }
        else if(zoneIndex == ZoneConstants.TWIN_FRONT) {
            setYOffset(-6);
            setGraphicsFile(GraphicsFile_Map);
            setGraphicsFilePosition(220, 80);
        }
        else if(zoneIndex == ZoneConstants.ENDLESS) {
            setYOffset(-6);
            setGraphicsFile(GraphicsFile_Map);
            setGraphicsFilePosition(20, 80);
        }
        else if(zoneIndex == ZoneConstants.SHRINE_FRONT || zoneIndex == ZoneConstants.SHRINE_BACK) {
            setYOffset(-6);
            setGraphicsFile(GraphicsFile_Eveg);
            setGraphicsFilePosition(0, 600);
        }
        else if(zoneIndex == ZoneConstants.ILLUSION) {
            setYOffset(-6);
            setGraphicsFile(GraphicsFile_Map);
            setGraphicsFilePosition(40, 40);
        }
        else if(zoneIndex == ZoneConstants.GRAVEYARD) {
            setYOffset(-6);
            setGraphicsFile(GraphicsFile_Map);
            setGraphicsFilePosition(80, 60);
        }
        else if(zoneIndex == ZoneConstants.MOONLIGHT) {
            setYOffset(-6);
            setGraphicsFile(GraphicsFile_Map);
            setGraphicsFilePosition(40, 80);
        }
        else if(zoneIndex == ZoneConstants.GODDESS) {
            setYOffset(-4);
            setGraphicsFile(GraphicsFile_Map);
            setGraphicsFilePosition(100, 120);
        }
        else if(zoneIndex == ZoneConstants.RUIN) {
            setYOffset(-6);
            setGraphicsFile(GraphicsFile_Map);
            setGraphicsFilePosition(220, 40);
        }
        else if(zoneIndex == ZoneConstants.BIRTH_SWORDS || zoneIndex == ZoneConstants.BIRTH_SKANDA) {
            setYOffset(-4);
            setGraphicsFile(GraphicsFile_Map);
            setGraphicsFilePosition(120, 40);
        }
        else if(zoneIndex == ZoneConstants.DIMENSIONAL) {
            setYOffset(-4);
            setGraphicsFile(GraphicsFile_Map);
            setGraphicsFilePosition(220, 40);
        }
        else if(zoneIndex == ZoneConstants.RETRO_MAUSOLEUM) {
            setYOffset(-4);
            setGraphicsFile(GraphicsFile_Map);
            setGraphicsFilePosition(60, 160);
        }
        else if(zoneIndex == ZoneConstants.RETRO_GUIDANCE) {
            setYOffset(-4);
            setGraphicsFile(GraphicsFile_Map);
            setGraphicsFilePosition(364, 60);
        }
        else if(zoneIndex == ZoneConstants.RETRO_SURFACE) {
            setYOffset(-4);
            setGraphicsFile(GraphicsFile_Map);
            setGraphicsFilePosition(760, 120);
//            setGraphicsFilePosition(420, 100); // Grass
        }
    }
}
