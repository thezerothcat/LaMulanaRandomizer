package lmr.randomizer.rcd.object;

import lmr.randomizer.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/16/2017.
 */
public class GameObject {
    private short id;

    private int x;
    private int y;

    private List<TestByteOperation> testByteOperations = new ArrayList<>();
    private List<WriteByteOperation> writeByteOperations = new ArrayList<>();
    private List<Short> args = new ArrayList<>();

    private ObjectContainer objectContainer;

    public GameObject(ObjectContainer objectContainer) {
        this.objectContainer = objectContainer;
    }

    public GameObject(ObjectContainer objectContainer, int paramCount) {
        this.objectContainer = objectContainer;
        for(int i = 0; i < paramCount; i++) {
            args.add((short)0);
        }
    }

    public GameObject(GameObject objectToCopy) {
        this.id = objectToCopy.id;
        this.x = objectToCopy.x;
        this.y = objectToCopy.y;

        for(TestByteOperation testByteOperation : objectToCopy.testByteOperations) {
            TestByteOperation newTestByteOperation = new TestByteOperation();
            newTestByteOperation.setIndex(testByteOperation.getIndex());
            newTestByteOperation.setOp(testByteOperation.getOp());
            newTestByteOperation.setValue(testByteOperation.getValue());
            testByteOperations.add(newTestByteOperation);
        }

        for(WriteByteOperation writeByteOperation : objectToCopy.writeByteOperations) {
            WriteByteOperation newWriteByteOperation = new WriteByteOperation();
            newWriteByteOperation.setIndex(writeByteOperation.getIndex());
            newWriteByteOperation.setOp(writeByteOperation.getOp());
            newWriteByteOperation.setValue(writeByteOperation.getValue());
            writeByteOperations.add(newWriteByteOperation);
        }

        this.args = new ArrayList<>(objectToCopy.args);

        this.objectContainer = objectToCopy.objectContainer;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
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

    public ObjectContainer getObjectContainer() {
        return objectContainer;
    }

    public GameObject addTests(TestByteOperation... tests) {
        for(TestByteOperation test : tests) {
            testByteOperations.add(test);
        }
        if(testByteOperations.size() > 4) {
            FileUtils.logFlush("Attempted to add more than 4 tests to object:\n" + toString());
            throw new RuntimeException("Too many tests on rcd object");
        }
        return this;
    }

    public GameObject addTests(List<TestByteOperation> tests) {
        for(TestByteOperation test : tests) {
            testByteOperations.add(test);
        }
        if(testByteOperations.size() > 4) {
            FileUtils.logFlush("Attempted to add more than 4 tests to object:\n" + toString());
            throw new RuntimeException("Too many tests on rcd object");
        }
        return this;
    }

    public boolean hasTest(TestByteOperation testToLookFor) {
        TestByteOperation testByteOperation;
        for(int i = 0; i < testByteOperations.size(); i++) {
            testByteOperation = testByteOperations.get(i);
            if(testByteOperation.getIndex() != testToLookFor.getIndex()) {
                continue;
            }
            if(testToLookFor.getOp() != null && !testByteOperation.getOp().equals(testToLookFor.getOp())) {
                continue;
            }
            if(testByteOperation.getValue() != testToLookFor.getValue()) {
                continue;
            }
            return true;
        }
        return false;
    }

    public boolean removeTest(TestByteOperation testToRemove) {
        Integer indexToRemove = null;
        TestByteOperation testByteOperation;
        for(int i = 0; i < testByteOperations.size(); i++) {
            testByteOperation = testByteOperations.get(i);
            if(testByteOperation.getIndex() != testToRemove.getIndex()) {
                continue;
            }
            if(testToRemove.getOp() != null && !testByteOperation.getOp().equals(testToRemove.getOp())) {
                continue;
            }
            if(testByteOperation.getValue() != testToRemove.getValue()) {
                continue;
            }
            indexToRemove = i;
        }
        if(indexToRemove != null) {
            testByteOperations.remove((int)indexToRemove);
            return true;
        }
        return false;
    }

    public GameObject addUpdates(WriteByteOperation... updates) {
        for(WriteByteOperation update : updates) {
            writeByteOperations.add(update);
        }
        if(writeByteOperations.size() > 4) {
            FileUtils.logFlush("Attempted to add more than 4 tests to object:\n" + toString());
            throw new RuntimeException("Too many tests on rcd object");
        }
        return this;
    }

    public GameObject addUpdates(List<WriteByteOperation> updates) {
        for(WriteByteOperation update : updates) {
            writeByteOperations.add(update);
        }
        if(writeByteOperations.size() > 4) {
            FileUtils.logFlush("Attempted to add more than 4 tests to object:\n" + toString());
            throw new RuntimeException("Too many tests on rcd object");
        }
        return this;
    }

    public boolean hasUpdate(WriteByteOperation updateToLookFor) {
        WriteByteOperation writeByteOperation;
        for(int i = 0; i < writeByteOperations.size(); i++) {
            writeByteOperation = writeByteOperations.get(i);
            if(writeByteOperation.getIndex() != updateToLookFor.getIndex()) {
                continue;
            }
            if(updateToLookFor.getOp() != null && !writeByteOperation.getOp().equals(updateToLookFor.getOp())) {
                continue;
            }
            if(writeByteOperation.getValue() != updateToLookFor.getValue()) {
                continue;
            }
            return true;
        }
        return false;
    }

    public void removeUpdate(WriteByteOperation updateToRemove) {
        Integer indexToRemove = null;
        WriteByteOperation writeByteOperation;
        for(int i = 0; i < writeByteOperations.size(); i++) {
            writeByteOperation = writeByteOperations.get(i);
            if(writeByteOperation.getIndex() != updateToRemove.getIndex()) {
                continue;
            }
            if(updateToRemove.getOp() != null && !writeByteOperation.getOp().equals(updateToRemove.getOp())) {
                continue;
            }
            if(writeByteOperation.getValue() != updateToRemove.getValue()) {
                continue;
            }
            indexToRemove = i;
        }
        if(indexToRemove != null) {
            writeByteOperations.remove((int)indexToRemove);
        }
    }

    @Override
    public String toString() {
        StringBuilder returnVal = new StringBuilder(String.format("\nOBJECT Type=0x%x (%s)", id, objectContainer.getContainerString()));
        if(x >= 0) {
            returnVal.append(String.format(" @ %d, %d", x, y));
        }
        if(!testByteOperations.isEmpty()) {
            for(TestByteOperation testByteOperation : testByteOperations) {
                returnVal.append('\n').append(String.format("TEST: %s", testByteOperation.toString()));
            }
        }
        if(!writeByteOperations.isEmpty()) {
            for(WriteByteOperation writeByteOperation : writeByteOperations) {
                returnVal.append('\n').append(String.format("UPDATE: %s", writeByteOperation.toString()));
            }
        }
        for(int i = 0; i < args.size(); i++) {
            returnVal.append('\n').append(String.format("ARG %d: %d", i, args.get(i)));
        }
        return returnVal.append('\n').toString();
    }
}
