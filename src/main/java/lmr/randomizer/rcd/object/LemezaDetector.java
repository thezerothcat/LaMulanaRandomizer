package lmr.randomizer.rcd.object;

/**
 * 0x14 Lemeza Detector args: Reversed
 *     0 - seconds wait
 *     1 - frames wait
 *     2 - continuous/total
 *     3 - interaction type 0 = any time except paused 1 = 2 = 3 = 4 = just be on the ground, ok. default: sleep
 *     4 - graphical tile width
 *     5 - graphical tile height
 */
public class LemezaDetector extends GameObject {
    public LemezaDetector(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 6);
        setId((short)0x14);
        setX(x);
        setY(y);
    }

    public void setSecondsWait(int secondsWait) {
        getArgs().set(0, (short)secondsWait);
    }

    public void setFramesWait(int framesWait) {
        getArgs().set(1, (short)framesWait);
    }

    public void setCumulative(boolean cumulative) {
        // continuous (0) / total (1)
        getArgs().set(2, (short)(cumulative ? 1 : 0));
    }

    public void setInteractionType(int interactionType) {
        // interaction type 0 = any time except paused 1 = 2 = 3 = 4 = just be on the ground, ok. default: sleep
        getArgs().set(3, (short)interactionType);
    }

    public void setGraphicalTileWidth(int graphicalTileWidth) {
        getArgs().set(4, (short)graphicalTileWidth);
    }

    public void setGraphicalTileHeight(int graphicalTileHeight) {
        getArgs().set(5, (short)graphicalTileHeight);
    }
}
