package lmr.randomizer.rcd.object;

/**
 * 0xc2 - detection-mantra (Effect)
 * 0- mantraNumber?
 *
 * Does not despawn when tests are no longer passing.
 */
public class MantraDetector extends GameObject {
    public MantraDetector(ObjectContainer objectContainer) {
        super(objectContainer, 1);
        setId(ObjectIdConstants.MantraDetector);
        setX(-1);
        setY(-1);
    }

    public void setMantraNumber(int mantraNumber) {
        getArgs().set(0, (short)mantraNumber);
    }
}
