package lmr.randomizer;

import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.rcd.*;
import lmr.randomizer.rcd.Object;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by thezerothcat on 7/10/2017.
 */
public class FileUtils {
    private static final BufferedWriter LOG_WRITER;

    static {
        BufferedWriter temp = null;
        try {
            temp = getFileWriter("log.txt");
        }
        catch (Exception ex) {

        }
        LOG_WRITER = temp;
    }

    public static BufferedWriter getFileWriter(String file) {
        try {
            return new BufferedWriter(new FileWriter(file));
        } catch (Exception ex) {
            return null;
        }
    }

    public static BufferedReader getFileReader(String file) {
        try {
            return new BufferedReader(new FileReader(file));
        } catch (Exception ex) {
            return new BufferedReader(new InputStreamReader(FileUtils.class.getResourceAsStream(file))); // If we can't read the file directly, this might be a jar, and we can just pull from that.
        }
    }

    public static List<String> getList(String file) {
        List<String> listContents = new ArrayList<>();
        try(BufferedReader reader = getFileReader(file)) {
            String line;
            while((line = reader.readLine()) != null) {
                listContents.add(line.trim());
            }
            reader.close();
        } catch (Exception ex) {
            return null;
        }
        return listContents;
    }

    public static void populateRequirements(AccessChecker accessChecker, String file, String prefix) {
        try(BufferedReader reader = getFileReader(file)) {
            String line;
            String[] lineParts;
            while((line = reader.readLine()) != null) {
                lineParts = line.trim().split(" => "); // delimiter
                accessChecker.addNode(prefix == null ? lineParts[0] : (prefix + lineParts[0]), lineParts[1]);
            }
        } catch (Exception ex) {
            return;
        }
    }

    public static void log(String logText) {
        try {
            LOG_WRITER.write(logText);
            LOG_WRITER.newLine();
        } catch (Exception ex) {

        }
    }

    public static void closeAll() {
        try {
            LOG_WRITER.close();
        } catch (Exception ex) {

        }
    }

    private static String getLaMulanaBaseDir() {
        for(String filename : Arrays.asList("C:\\Games\\La-Mulana Remake 1.3.3.1", "C:\\GOG Games\\La-Mulana", "C:\\GOG Games\\La-Mulana")) {
            if(new File(filename).exists()) {
                return filename;
            }
        }
        return null;
    }

    private static byte[] getBytes(String file) {
        try (InputStream inputStream = new FileInputStream(file))
        {
            long fileSize = new File(file).length();

            byte[] allBytes = new byte[(int) fileSize];

            inputStream.read(allBytes);
            return allBytes;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static byte[] getByteArraySlice(byte[] mainArray, int startIndex, int length) {
        byte[] slice = new byte[length];
        for(int i = 0; i < length; i++) {
            slice[i] = mainArray[startIndex + i];
        }
        return slice;
    }

    private static ByteBuffer getField(byte[] mainArray, int startIndex, int length) {
        return ByteBuffer.wrap(getByteArraySlice(mainArray, startIndex, length)).order(ByteOrder.BIG_ENDIAN);
    }

    private static int addZoneObject(Zone zone, byte[] rcdBytes, int rcdByteIndex, boolean hasPosition) {
        Object obj = new Object();

        obj.setId(getField(rcdBytes, rcdByteIndex, 2).getShort());
        rcdByteIndex += 2;

        byte temp = rcdBytes[rcdByteIndex];
        rcdByteIndex += 1;

        int writeOperationCount = temp & 0xf;
        int testOperationCount = temp >> 4;

        byte argc = rcdBytes[rcdByteIndex];
        rcdByteIndex += 1;

        if(hasPosition) {
            obj.setX(getField(rcdBytes, rcdByteIndex, 2).getShort());
            rcdByteIndex += 2;
            obj.setY(getField(rcdBytes, rcdByteIndex, 2).getShort());
            rcdByteIndex += 2;
        }
        else {
            obj.setX((short) -1);
            obj.setY((short) -1);
        }

        for(int i = 0; i < testOperationCount; i++) {
            obj.getTestByteOperations().add(getTestByteOperation(rcdBytes, rcdByteIndex));
            rcdByteIndex += 4;
        }
        for(int i = 0; i < writeOperationCount; i++) {
            obj.getWriteByteOperations().add(getWriteByteOperation(rcdBytes, rcdByteIndex));
            // todo: update byte index
        }

        int i = 0;
        while(i < argc) {
            obj.getArgs().add(getField(rcdBytes, rcdByteIndex, 2).getShort());
            rcdByteIndex += 2;
            i += 2;
        }

        zone.getObjects().add(obj);
        return rcdByteIndex;
    }

    private static TestByteOperation getTestByteOperation(byte[] rcdBytes, int rcdByteIndex) {
        TestByteOperation testByteOperation = new TestByteOperation();

        testByteOperation.setIndex(getField(rcdBytes, rcdByteIndex, 2).getShort());
//        rcdByteIndex += 2;

        testByteOperation.setValue(rcdBytes[rcdByteIndex]);
//        rcdByteIndex += 1;

        testByteOperation.setOp(TestOp.get((int)rcdBytes[rcdByteIndex]));
//        rcdByteIndex += 1;

        return testByteOperation;
    }

    private static WriteByteOperation getWriteByteOperation(byte[] rcdBytes, int rcdByteIndex) {
        WriteByteOperation writeByteOperation = new WriteByteOperation();

        writeByteOperation.setIndex(getField(rcdBytes, rcdByteIndex, 2).getShort());
//        rcdByteIndex += 2;

        writeByteOperation.setValue(rcdBytes[rcdByteIndex]);
//        rcdByteIndex += 1;

        writeByteOperation.setOp(WriteOp.get((int)rcdBytes[rcdByteIndex]));
//        rcdByteIndex += 1;

        return writeByteOperation;
    }

    public static RcdScript getRcdScript() throws Exception {
        String laMulanaBaseDir = getLaMulanaBaseDir();
        if(laMulanaBaseDir == null) {
            return null; // todo: throw exception?
        }
        String mapPath = laMulanaBaseDir + "\\data\\mapdata";

        byte[] rcdBytes = getBytes(mapPath + "\\script.rcd");
        int rcdByteIndex = 2; // Seems we skip the first two bytes?

        List<Zone> zones = new ArrayList<>();

        for(int z = 0; z < 26; z++) {
            byte[] msdBytes = getBytes(mapPath + String.format("\\map%.2d.msd", z));
            Zone zone = new Zone();
            zones.add(zone);
            zone.setIndex(z);

            byte nameLength = getField(rcdBytes, rcdByteIndex, 1).get();
            rcdByteIndex += 1;
            short objCount = getField(rcdBytes, rcdByteIndex, 2).getShort();
            rcdByteIndex += 2;


            zone.setName(new String(getByteArraySlice(rcdBytes, rcdByteIndex, nameLength), "UTF-16BE"));
            rcdByteIndex += nameLength;
            for(int i = 0; i < objCount; i++) {
//                zone.objs = [readobj(rcd) for i in range(objcount)]
                rcdByteIndex += addZoneObject(zone, rcdBytes, rcdByteIndex); // todo: = or +=
            }


            int msdByteIndex = 0;
            while (true) {
                short frames = getField(rcdBytes, rcdByteIndex, 2).getShort();
                msdByteIndex += 2;
                if(frames == 0) {
                    break;
                }
                msdByteIndex += frames * 2;
            }
        }

        while True:
        frames = unpack('>H', msd.read(2))[0]
        if frames == 0:
        break
                msd.seek(frames * 2, 1)
        rooms = unpack('>BBB', msd.read(3))[2]
        for r in range(rooms):
##            print("room: %d-%d" % (z,r), end='\t')
        room = Room()
        zone.rooms.append(room)
        room.idx = z, r
        objcount = unpack('>H', rcd.read(2))[0]
##            print(objcount)
        room.objs = [readobj(rcd) for i in range(objcount)]
##            if r is 0 and z is 0:
##                print(room.objs[0])
        room.layers, room.pr_layer = unpack('>BBB', msd.read(3))[1:]
        room.hit_w, room.hit_h = unpack('>2H', msd.read(4))
        msd.seek(room.hit_w * room.hit_h, 1)

        for l in range(room.layers):
        w, h, sublayers = unpack('>2HB', msd.read(5))
        if l == room.pr_layer:
        room.tile_w, room.tile_h = w, h
        room.screen_w = room.tile_w // 32
        room.screen_h = room.tile_h // 24
        room.numscreens = room.screen_w * room.screen_h
        msd.seek(sublayers * w * h * 2, 1)
        for s in range(room.numscreens):
        screen = Screen()
        room.screens.append(screen)
        screen.idx = z, r, s
        namelen, count_objs, count_nopos = unpack('>BHB', rcd.read(4))
        if z is 0 and r is 0 and s is 0:
        print(namelen, count_objs, count_nopos)
        screen.objs = [readobj(rcd, False) for i in range(count_nopos)]
        screen.objs += [readobj(rcd, True) for i in range(count_objs - count_nopos)]
        screen.name = rcd.read(namelen).decode('utf_16_be')
        screen.exits = [unpack('>bbb', rcd.read(3)) for i in range(4)]

        rcd.close() #tsk    }
}


