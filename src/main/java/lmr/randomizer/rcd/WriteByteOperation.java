package lmr.randomizer.rcd;

/**
 * Created by thezerothcat on 7/17/2017.
 */
public class WriteByteOperation {
    private int index;
    private int value;
    private WriteOp op;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public WriteOp getOp() {
        return op;
    }

    public void setOp(WriteOp op) {
        this.op = op;
    }
}
