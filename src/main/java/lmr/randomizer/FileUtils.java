package lmr.randomizer;

import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.update.GameObjectId;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public static Map<String, GameObjectId> getRcdDataIdMap(String filename) {
        try(BufferedReader reader = getFileReader(filename)) {
            String line;
            String[] lineParts;
            String[] ids;
            Map<String, GameObjectId> mapOfItemToUsefulIdentifyingRcdData = new HashMap<>();
            while((line = reader.readLine()) != null) {
                lineParts = line.trim().split(" => "); // delimiter
                ids = lineParts[1].split(", ?");
                mapOfItemToUsefulIdentifyingRcdData.put(lineParts[0], new GameObjectId(ids[0], ids[1]));
            }
            return mapOfItemToUsefulIdentifyingRcdData;
        } catch (Exception ex) {
            return null;
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
}


