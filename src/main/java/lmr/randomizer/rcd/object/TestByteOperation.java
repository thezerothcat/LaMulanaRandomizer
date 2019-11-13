package lmr.randomizer.rcd.object;

/**
 * Created by thezerothcat on 7/17/2017.
 */
public class TestByteOperation {
    private int index;
    private byte value;
    private ByteOp op;

    public TestByteOperation() {
    }

    public TestByteOperation(TestByteOperation toCopy) {
        this.index = toCopy.index;
        this.value = toCopy.value;
        this.op = toCopy.op;
    }

    public TestByteOperation(int index, ByteOp byteOp, int value) {
        this.index = index;
        this.value = (byte)value;
        this.op = byteOp;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public ByteOp getOp() {
        return op;
    }

    public void setOp(ByteOp op) {
        this.op = op;
    }
}
