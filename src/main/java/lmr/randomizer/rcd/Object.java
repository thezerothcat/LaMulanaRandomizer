package lmr.randomizer.rcd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/16/2017.
 */
public class Object {
    private short id;

    private short x;
    private short y;

    private List<TestByteOperation> testByteOperations = new ArrayList<>();
    private List<WriteByteOperation> writeByteOperations = new ArrayList<>();
    private List<Short> args = new ArrayList<>();

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public short getX() {
        return x;
    }

    public void setX(short x) {
        this.x = x;
    }

    public short getY() {
        return y;
    }

    public void setY(short y) {
        this.y = y;
    }

    public List<TestByteOperation> getTestByteOperations() {
        return testByteOperations;
    }

    public List<WriteByteOperation> getWriteByteOperations() {
        return writeByteOperations;
    }

    public List<Short> getArgs() {
        return args;
    }
}
