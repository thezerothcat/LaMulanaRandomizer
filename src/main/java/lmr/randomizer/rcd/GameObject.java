package lmr.randomizer.rcd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/16/2017.
 */
public class GameObject {
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

    @Override
    public String toString() {
        StringBuilder returnVal = new StringBuilder(String.format("OBJECT Type=0x%x", id));
        if(x >= 0) {
            returnVal.append(String.format(" @ %d, %d", x, y));
        }
        if(!testByteOperations.isEmpty()) {
            for(TestByteOperation testByteOperation : testByteOperations) {
                returnVal.append('\n').append(String.format("TEST:\n%s", testByteOperation.toString()));
            }
        }
        if(!writeByteOperations.isEmpty()) {
            for(WriteByteOperation writeByteOperation : writeByteOperations) {
                returnVal.append('\n').append(String.format("UPDATE:\n%s", writeByteOperation.toString()));
            }
        }
        for(int i = 0; i < args.size(); i++) {
            returnVal.append('\n').append(String.format("ARG %d: %d", i, args.get(i)));
        }
        return returnVal.toString();
    }
}
