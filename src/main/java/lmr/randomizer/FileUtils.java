package lmr.randomizer;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thezerothcat on 7/10/2017.
 */
public class FileUtils {
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

    public static void populateRequirements(Requirements requirements, String file) {
        BufferedReader reader = getFileReader(file);
        try {
            String line;
            String[] lineParts;
            while((line = reader.readLine()) != null) {
                lineParts = line.trim().split(" => "); // delimiter
                requirements.addNode(lineParts[0], lineParts[1]);
            }
        } catch (Exception ex) {
            return;
        }
    }
}


