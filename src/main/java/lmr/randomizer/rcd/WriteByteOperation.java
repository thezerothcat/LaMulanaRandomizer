package lmr.randomizer.rcd;

/**
 * Created by thezerothcat on 7/17/2017.
 */
public class WriteByteOperation {
    private int index;
    private int value;
    private ByteOp op;

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

    public ByteOp getOp() {
        return op;
    }

    public void setOp(ByteOp op) {
        this.op = op;
    }
}
