package lmr.randomizer;

import lmr.randomizer.node.AccessChecker;

import java.io.*;
import java.util.ArrayList;
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
        BufferedReader reader = getFileReader(file);
        List<String> listContents = new ArrayList<>();
        try {
            String line;
            while((line = reader.readLine()) != null) {
                listContents.add(line.trim());
            }
        } catch (Exception ex) {
            return null;
        }
        return listContents;
    }

    public static void populateRequirements(AccessChecker accessChecker, String file, String prefix) {
        BufferedReader reader = getFileReader(file);
        try {
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
}


