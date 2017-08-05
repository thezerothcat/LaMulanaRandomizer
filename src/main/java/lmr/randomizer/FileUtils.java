package lmr.randomizer;

import lmr.randomizer.node.AccessChecker;
import lmr.randomizer.node.NodeWithRequirements;
import lmr.randomizer.update.GameObjectId;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.MessageDigest;
import java.util.*;

/**
 * Created by thezerothcat on 7/10/2017.
 */
public class FileUtils {
    private static final BufferedWriter LOG_WRITER;
    private static final List<String> KNOWN_RCD_FILE_HASHES = new ArrayList<>();

    static {
        BufferedWriter temp = null;
        try {
            temp = getFileWriter("log.txt");
        }
        catch (Exception ex) {

        }
        LOG_WRITER = temp;

        KNOWN_RCD_FILE_HASHES.add("181C959BF2F2567279CC717C8AD03A20"); // 1.0.0.1
        KNOWN_RCD_FILE_HASHES.add("89D8BF2DD6B8FA365A83DDBFD947CCFA"); // 1.1.1.1
        KNOWN_RCD_FILE_HASHES.add("922C4FB1552843B73CF14ADCC923CF17"); // 1.3.3.1
        // 1.5.5.x is unknown
        KNOWN_RCD_FILE_HASHES.add("21869050145662F6DAAC6A1B3D54F3B9"); // 1.6.6.x
    }

    public static BufferedWriter getFileWriter(String file) {
        try {
            return new BufferedWriter(new FileWriter(file));
        } catch (Exception ex) {
            System.out.println("unable to get file writer for " + file);
                ex.printStackTrace();
            return null;
        }
    }

    public static BufferedReader getFileReader(String file) {
        try {
            return new BufferedReader(new FileReader("src/main/resources/lmr/randomizer/" + file));
        } catch (IOException ex) {
            try {
                return new BufferedReader(new InputStreamReader(FileUtils.class.getResourceAsStream(file))); // If we can't read the file directly, this might be a jar, and we can just pull from that.
            }
            catch (Exception ex2) {
                System.out.println("unable to get file reader for " + file);
                ex.printStackTrace();
                return null;
            }
        }
    }

    public static byte[] getBytes(String path, boolean rcdFile) throws IOException {
        if(rcdFile && Settings.rcdFileLocation != null) {
            try {
                return getBytesInner(Settings.rcdFileLocation);
            }
            catch (IOException ex) {
                try {
                    return getBytesInner(path);
                }
                catch (Exception ex2) {
                    System.out.println("unable to get file reader for " + path);
                    ex.printStackTrace();
                    return null;
                }
            }
        }
        else {
            try {
                return getBytesInner(path);
            }
            catch (Exception ex) {
                System.out.println("unable to get file reader for " + path);
                ex.printStackTrace();
                return null;
            }
        }
    }

    private static byte[] getBytesInner(String path) throws IOException {
        InputStream inputStream = new FileInputStream(path);
        long fileSize = new File(path).length();
        return getBytes(inputStream, fileSize);
    }

    private static byte[] getBytes(InputStream inputStream, long fileSize) throws IOException {
        byte[] allBytes = new byte[(int) fileSize];
        inputStream.read(allBytes);
        inputStream.close();
        return allBytes;
    }

    public static boolean hashRcdFile(File file) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] fileBytes = getBytes(new FileInputStream(file), file.length());
            return KNOWN_RCD_FILE_HASHES.contains(DatatypeConverter.printHexBinary(md5.digest(fileBytes)).toUpperCase());
        }
        catch (Exception ex) {
            return false;
        }
    }

    public static boolean hashDatFile(File file) {
        return true; // todo: implement this when I get file hash info
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

    public static void populateRequirements(Map<String, NodeWithRequirements> mapOfNodeNameToRequirementsObject, String file, String prefix) {
        try(BufferedReader reader = getFileReader(file)) {
            String line;
            String[] lineParts;
            while((line = reader.readLine()) != null) {
                lineParts = line.trim().split(" => "); // delimiter
                addNode(mapOfNodeNameToRequirementsObject, prefix == null ? lineParts[0] : (prefix + lineParts[0]), lineParts[1]);
            }
        } catch (Exception ex) {
            return;
        }
    }

    private static void addNode(Map<String, NodeWithRequirements> mapOfNodeNameToRequirementsObject, String name, String requirementSet) {
        NodeWithRequirements node = mapOfNodeNameToRequirementsObject.get(name);
        if(node == null) {
            node = new NodeWithRequirements(name);
            mapOfNodeNameToRequirementsObject.put(name, node);
        }
        node.addRequirementSet(buildRequirementSet(requirementSet));
    }

    private static List<String> buildRequirementSet(String requirementSet) {
        if(requirementSet == null || requirementSet.isEmpty() || "None".equalsIgnoreCase(requirementSet)) {
            return new ArrayList<>();
        }
        if(!requirementSet.contains(",")) {
            return Arrays.asList(requirementSet);
        }
        List<String> requirements = new ArrayList<>();
        for(String requirement : requirementSet.split(", ?")) {
            requirements.add(requirement);
        }
        return requirements;
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

    public static Map<String, Integer> getShopBlockMap(String filename) {
        try(BufferedReader reader = getFileReader(filename)) {
            String line;
            String[] lineParts;
            Map<String, Integer> mapOfShopNameToShopBlock = new HashMap<>();
            while((line = reader.readLine()) != null) {
                lineParts = line.trim().split(" => "); // delimiter
                mapOfShopNameToShopBlock.put(lineParts[0], Integer.parseInt(lineParts[1]));
            }
            return mapOfShopNameToShopBlock;
        } catch (Exception ex) {
            return null;
        }
    }

    public static Map<String, List<String>> getShopOriginalContentsMap(String filename) {
        try(BufferedReader reader = getFileReader(filename)) {
            String line;
            String[] lineParts;
            String[] items;
            Map<String, List<String>> mapOfShopNameToOriginalShopContents = new HashMap<>();
            List<String> shopContents;
            while((line = reader.readLine()) != null) {
                lineParts = line.trim().split(" => "); // delimiter
                items = lineParts[1].split(", ?");
                shopContents = new ArrayList<>(3);
                shopContents.add(items[0]);
                shopContents.add(items[1]);
                shopContents.add(items[2]);
                mapOfShopNameToOriginalShopContents.put(lineParts[0], shopContents);
            }
            return mapOfShopNameToOriginalShopContents;
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
            LOG_WRITER.flush();
            LOG_WRITER.close();
        } catch (Exception ex) {

        }
    }
}


