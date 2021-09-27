package lmr.randomizer.rcd.object;

import lmr.randomizer.util.ZoneConstants;

/**
 * 0x30 Trapdoor tested
 *     0 - FlagOpen (0 is no flag)
 *     1 - FramesWaitBeforeTrap | DesiredFlagVlaue
 *     2 - FramesOpenAfterAnimation
 *     3 - ImageX
 *     4 - ImageY
 *
 * 0x30 - Trapdoor (Object)
 * 0- (0-1) Flag Open
 * 1- (0-5) Frame wait before open | flag value
 * 2- (20-60) frames open after animation
 * 3- (700-860) Image x
 * 4- (0-260) Image y
 */
public class Trapdoor extends GameObject {
    public Trapdoor(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 5);
        setId(ObjectIdConstants.Trapdoor);
        setX(x);
        setY(y);
    }

    public void setOpenFlagIndex(int flagIndex) {
        getArgs().set(0, (short)flagIndex);
    }

    public void setFrameDelayOrFlagValue(int frameDelayOrFlagValue) {
        getArgs().set(1, (short)frameDelayOrFlagValue);
    }

    public void setFramesOpenAfterAnimation(int framesOpen) {
        getArgs().set(2, (short)framesOpen);
    }

    public void setImageX(int x) {
        getArgs().set(3, (short)x);
    }

    public void setImageY(int y) {
        getArgs().set(4, (short)y);
    }

    public void setImageCoordinates(int zoneIndex) {
        if(zoneIndex == ZoneConstants.GUIDANCE) {
            setImageX(700);
            setImageY(0);
        }
        else if(zoneIndex == ZoneConstants.SURFACE) {
            setImageX(0);
            setImageY(160);
        }
        else if(zoneIndex == ZoneConstants.MAUSOLEUM) {
            setImageX(700);
            setImageY(0);
        }
        else if(zoneIndex == ZoneConstants.SUN) {
            setImageX(540);
            setImageY(0);
        }
        else if(zoneIndex == ZoneConstants.SPRING) {
            setImageX(720);
            setImageY(180);
        }
        else if(zoneIndex == ZoneConstants.INFERNO) {
            setImageX(700);
            setImageY(0);
        }
        else if(zoneIndex == ZoneConstants.EXTINCTION) {
            setImageX(0);
            setImageY(350);
        }
        else if(zoneIndex == ZoneConstants.TWIN_FRONT) {
            setImageX(700);
            setImageY(0);
        }
        else if(zoneIndex == ZoneConstants.ENDLESS) {
            setImageX(0);
            setImageY(400);
        }
        else if(zoneIndex == ZoneConstants.SHRINE_FRONT || zoneIndex == ZoneConstants.SHRINE_BACK) {
            setImageX(0);
            setImageY(600);
        }
        else if(zoneIndex == ZoneConstants.ILLUSION) {
            setImageX(700);
            setImageY(0);
        }
        else if(zoneIndex == ZoneConstants.GRAVEYARD) {
            setImageX(750);
            setImageY(0);
        }
        else if(zoneIndex == ZoneConstants.MOONLIGHT) {
            setImageX(700);
            setImageY(80);
        }
        else if(zoneIndex == ZoneConstants.GODDESS) {
            setImageX(0);
            setImageY(420);
        }
        else if(zoneIndex == ZoneConstants.RUIN) {
            setImageX(0);
            setImageY(580);
        }
        else if(zoneIndex == ZoneConstants.BIRTH_SWORDS || zoneIndex == ZoneConstants.BIRTH_SKANDA) {
            setImageX(700);
            setImageY(0);
        }
        else if(zoneIndex == ZoneConstants.DIMENSIONAL) {
            setImageX(0);
            setImageY(808);
        }
        else if(zoneIndex == ZoneConstants.RETRO_MAUSOLEUM) {
            setImageX(860);
            setImageY(260);
        }
        else if(zoneIndex == ZoneConstants.RETRO_GUIDANCE) {
            setImageX(860);
            setImageY(340);
        }
        else if(zoneIndex == ZoneConstants.RETRO_SURFACE) {
            setImageX(860);
            setImageY(420);
        }
        else if(zoneIndex == ZoneConstants.NIGHT_SURFACE) {
            setImageX(0);
            setImageY(160);
        }
        else {
            // Defaults
            setImageX(700);
            setImageY(0);
        }
    }
}
