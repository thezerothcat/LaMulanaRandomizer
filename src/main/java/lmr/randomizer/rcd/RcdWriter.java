package lmr.randomizer.rcd;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by thezerothcat on 7/18/2017.
 */
public final class RcdWriter {
    private RcdWriter() {
    }

    public static void writeRcd(List<Zone> rcdInfo) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream("script.rcd"));
        dataOutputStream.writeShort(0);

        for(Zone zone : rcdInfo) {
            dataOutputStream.writeByte(zone.getName().length());
            dataOutputStream.writeShort(zone.getObjects().size());
            dataOutputStream.writeChars(zone.getName()); // todo: will this work?

            for(GameObject obj : zone.getObjects()) {
                writeObject(obj, dataOutputStream);
            }
            for(Room room : zone.getRooms()) {
                dataOutputStream.writeShort(room.getObjects().size());
                for(GameObject obj : room.getObjects()) {
                    writeObject(obj, dataOutputStream);
                }

                for(Screen screen : room.getScreens()) {
                    dataOutputStream.writeByte(screen.getName().length());
                    dataOutputStream.writeShort(screen.getObjects().size());
                    dataOutputStream.writeByte(getNoPositionScreenObjectCount(screen.getObjects()));
                    for(GameObject obj : room.getObjects()) {
                        writeObject(obj, dataOutputStream);
                    }
                    dataOutputStream.writeChars(screen.getName()); // todo: will this work?
                    for(ScreenExit exit : screen.getScreenExits()) {
                        dataOutputStream.writeByte(exit.getZoneIndex());
                        dataOutputStream.writeByte(exit.getRoomIndex());
                        dataOutputStream.writeByte(exit.getScreenIndex());
                    }
                }
            }
        }

        dataOutputStream.flush();
        dataOutputStream.close();
    }

    private static void writeObject(GameObject obj, DataOutputStream dataOutputStream) throws IOException {
        int testUpdateCount = obj.getTestByteOperations().size();
        testUpdateCount &= 0x0f;
        testUpdateCount = testUpdateCount << 4;
        testUpdateCount |= obj.getWriteByteOperations().size();
//        System.out.println(testUpdateCount);
        dataOutputStream.writeShort(obj.getId());
        dataOutputStream.writeByte(testUpdateCount);
        dataOutputStream.writeByte(obj.getArgs().size());
        if(obj.getX() != -1 && obj.getY() != -1) {
            dataOutputStream.writeShort(obj.getX() / 20);
            dataOutputStream.writeShort(obj.getY() / 20);
        }
        for(TestByteOperation testByteOperation: obj.getTestByteOperations()) {
            writeTestByteOp(testByteOperation, dataOutputStream);
        }
        for(WriteByteOperation writeByteOperation : obj.getWriteByteOperations()) {
            writeWriteByteOp(writeByteOperation, dataOutputStream);
        }
        for(Short arg : obj.getArgs()) {
            dataOutputStream.writeShort(arg);
        }
    }

    private static void writeTestByteOp(TestByteOperation testByteOperation, DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(testByteOperation.getIndex());
        dataOutputStream.writeByte(testByteOperation.getValue());
        dataOutputStream.writeByte(testByteOperation.getOp().getOp());
    }

    private static void writeWriteByteOp(WriteByteOperation writeByteOperation, DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(writeByteOperation.getIndex());
        dataOutputStream.writeByte(writeByteOperation.getValue());
        dataOutputStream.writeByte(writeByteOperation.getOp().getOp());
    }

    private static int getNoPositionScreenObjectCount(List<GameObject> objects) {
        for(int i = 0; i < objects.size(); i++) {
            if(objects.get(i).getX() >= 0) {
                return i;
            }
        }
        return objects.size();
    }

}
