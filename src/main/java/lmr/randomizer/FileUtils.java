package lmr.randomizer;

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
    public static final String VERSION = "1.22.0";

    private static BufferedWriter logWriter;
    private static final List<String> KNOWN_RCD_FILE_HASHES = new ArrayList<>();

    static {
        BufferedWriter temp = null;
        try {
            temp = getFileWriter("log.txt");
        }
        catch (Exception ex) {

        }
        logWriter = temp;

        KNOWN_RCD_FILE_HASHES.add("181C959BF2F2567279CC717C8AD03A20"); // 1.0.0.1
        KNOWN_RCD_FILE_HASHES.add("89D8BF2DD6B8FA365A83DDBFD947CCFA"); // 1.1.1.1
        KNOWN_RCD_FILE_HASHES.add("922C4FB1552843B73CF14ADCC923CF17"); // 1.2.2.1 and 1.3.3.1
        // 1.5.5.x is unknown
        KNOWN_RCD_FILE_HASHES.add("21869050145662F6DAAC6A1B3D54F3B9"); // 1.6.6.x
    }

    public static BufferedWriter getFileWriter(String file) {
        try {
            return new BufferedWriter(new FileWriter(file, false));
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
        try {
            return getBytesInner(path);
        }
        catch (Exception ex) {
            System.out.println("unable to get file reader for " + path);
            ex.printStackTrace();
            return null;
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
            String md5Hash = DatatypeConverter.printHexBinary(md5.digest(fileBytes)).toUpperCase();
            if(KNOWN_RCD_FILE_HASHES.contains(md5Hash)) {
                return true;
            }
            FileUtils.log("MD5 hash " + md5Hash + " for file " + file.getAbsolutePath() + " does not match any known versions of La-Mulana. Is this a modified file?");
            return false;
        }
        catch (Exception ex) {
            FileUtils.log("Unable to read file " + file.getAbsolutePath() + ", " + ex.getMessage());
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
                if(!line.isEmpty()) {
                    listContents.add(line.trim());
                }
            }
            reader.close();
        } catch (Exception ex) {
            FileUtils.log("Unable to read file " + file + ", " + ex.getMessage());
            return new ArrayList<>(0);
        }
        return listContents;
    }

    public static void populateRequirements(Map<String, NodeWithRequirements> mapOfNodeNameToRequirementsObject, String file) {
        try(BufferedReader reader = getFileReader(file)) {
            String line;
            String[] lineParts;
            while((line = reader.readLine()) != null) {
                lineParts = line.trim().split(" => "); // delimiter
                addNode(mapOfNodeNameToRequirementsObject, lineParts[0], lineParts[1]);
            }
        } catch (Exception ex) {
            FileUtils.log("Unable to read file " + file + ", " + ex.getMessage());
            return;
        }
    }

    public static Map<String, List<String>> getAccessibleLocations(String file) {
        Map<String, List<String>> accessibleLocations = new HashMap<>();
        try(BufferedReader reader = getFileReader(file)) {
            String line;
            String[] lineParts;
            while((line = reader.readLine()) != null) {
                if(line.startsWith("#")) {
                    continue;
                }
                lineParts = line.trim().split(" => "); // delimiter
                String locationString = lineParts[1];
                if(!locationString.contains(",")) {
                    accessibleLocations.put(lineParts[0], Arrays.asList(locationString));
                }

                List<String> locations = new ArrayList<>();
                for(String location : locationString.split(", ?")) {
                    locations.add(location);
                }
                accessibleLocations.put(lineParts[0], locations);
            }
            return accessibleLocations;
        } catch (Exception ex) {
            FileUtils.log("Unable to read file " + file + ", " + ex.getMessage());
            return new HashMap<>(0);
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
            FileUtils.log("Unable to read file " + filename + ", " + ex.getMessage());
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
            FileUtils.log("Unable to read file " + filename + ", " + ex.getMessage());
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
            FileUtils.log("Unable to read file " + filename + ", " + ex.getMessage());
            return null;
        }
    }

    public static void readSettings() throws IOException {
        if(!(new File("randomizer-config.txt").exists())) {
            return;
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("randomizer-config.txt"));
        }
        catch (Exception ex) {
            FileUtils.log("Unable to read settings file" + ", " + ex.getMessage());
            return;
        }

        String line;
        String[] settingAndValue;
        List<String> enabledGlitches = new ArrayList<>();
        List<String> enabledDamageBoosts = new ArrayList<>();
        Set<String> initiallyAvailableItems = new HashSet<>();
        Set<String> nonRandomizedItems = new HashSet<>();
        Set<String> surfaceItems = new HashSet<>();
        Set<String> removedItems = new HashSet<>();
        while((line = reader.readLine()) != null) {
            if(line.startsWith("randomization.")) {
                settingAndValue = line.replace("randomization.", "").split("=");
                if("INITIAL".equals(settingAndValue[1])) {
                    initiallyAvailableItems.add(settingAndValue[0]);
                }
                else if("NONRANDOM".equals(settingAndValue[1])) {
                    nonRandomizedItems.add(settingAndValue[0]);
                }
                else if("V_EARLY".equals(settingAndValue[1])) {
                    surfaceItems.add(settingAndValue[0]);
                }
                else if("REMOVED".equals(settingAndValue[1])) {
                    removedItems.add(settingAndValue[0]);
                }
            }
            else if(line.startsWith("glitches.")) {
                settingAndValue = line.replace("glitches.", "").split("=");
                if(Boolean.valueOf(settingAndValue[1])) {
                    enabledGlitches.add(settingAndValue[0]);
                }
            }
            else if(line.startsWith("dboost.")) {
                settingAndValue = line.replace("dboost.", "").split("=");
                if(Boolean.valueOf(settingAndValue[1])) {
                    enabledDamageBoosts.add(settingAndValue[0]);
                }
            }
            else if(line.startsWith("shopRandomization")) {
                Settings.setShopRandomization(line.split("=")[1], false);
            }
            else if(line.startsWith("xmailerItem")) {
                Settings.setXmailerItem(line.split("=")[1], false);
            }
            else if(line.startsWith("automaticHardmode")) {
                Settings.setAutomaticHardmode(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("requireSoftwareComboForKeyFairy")) {
                Settings.setRequireSoftwareComboForKeyFairy(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("requireIceCapeForLava")) {
                Settings.setRequireIceCapeForLava(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("requireFlaresForExtinction")) {
                Settings.setRequireFlaresForExtinction(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("randomizeForbiddenTreasure")) {
                Settings.setRandomizeForbiddenTreasure(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("randomizeCoinChests")) {
                Settings.setRandomizeCoinChests(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("replaceMapsWithWeights")) {
                Settings.setReplaceMapsWithWeights(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("coinChestGraphics")) {
                Settings.setCoinChestGraphics(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("automaticGrailPoints")) {
                Settings.setAutomaticGrailPoints(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("laMulanaBaseDir")) {
                Settings.setLaMulanaBaseDir(line.substring(line.indexOf("=") + 1), false);
            }
            else if(line.startsWith("language")) {
                Settings.setLanguage(line.substring(line.indexOf("=") + 1), false);
            }
            else if(line.startsWith("bossDifficulty")) {
                Settings.setBossDifficulty(line.split("=")[1], false);
            }
            else if(line.startsWith("minRandomRemovedItems")) {
                Settings.setMinRandomRemovedItems(Integer.parseInt(line.split("=")[1]), false);
            }
            else if(line.startsWith("maxRandomRemovedItems")) {
                Settings.setMaxRandomRemovedItems(Integer.parseInt(line.split("=")[1]), false);
            }
        }
        Settings.setEnabledGlitches(enabledGlitches, false);
        Settings.setEnabledDamageBoosts(enabledDamageBoosts, false);
        Settings.setInitiallyAccessibleItems(initiallyAvailableItems, false);
        Settings.setNonRandomizedItems(nonRandomizedItems, false);
        Settings.setSurfaceItems(surfaceItems, false);
        Settings.setRemovedItems(removedItems, false);
    }

    public static void saveSettings() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("randomizer-config.txt"));
        writer.write(String.format("version=%s", VERSION));
        writer.newLine();

        writer.write(String.format("seed=%s", Settings.getStartingSeed()));
        writer.newLine();

        writer.write(String.format("shopRandomization=%s", Settings.getShopRandomization().toString()));
        writer.newLine();

        if(Settings.getXmailerItem() != null) {
            writer.write(String.format("xmailerItem=%s", Settings.getXmailerItem()));
            writer.newLine();
        }

        writer.write(String.format("automaticHardmode=%s", Settings.isAutomaticHardmode()));
        writer.newLine();

        writer.write(String.format("requireSoftwareComboForKeyFairy=%s", Settings.isRequireSoftwareComboForKeyFairy()));
        writer.newLine();

        writer.write(String.format("requireIceCapeForLava=%s", Settings.isRequireIceCapeForLava()));
        writer.newLine();

        writer.write(String.format("requireFlaresForExtinction=%s", Settings.isRequireFlaresForExtinction()));
        writer.newLine();

        writer.write(String.format("randomizeForbiddenTreasure=%s", Settings.isRandomizeForbiddenTreasure()));
        writer.newLine();

        writer.write(String.format("randomizeCoinChests=%s", Settings.isRandomizeCoinChests()));
        writer.newLine();

        writer.write(String.format("replaceMapsWithWeights=%s", Settings.isReplaceMapsWithWeights()));
        writer.newLine();

        writer.write(String.format("coinChestGraphics=%s", Settings.isCoinChestGraphics()));
        writer.newLine();

        writer.write(String.format("automaticGrailPoints=%s", Settings.isAutomaticGrailPoints()));
        writer.newLine();

        writer.write(String.format("laMulanaBaseDir=%s", Settings.getLaMulanaBaseDir()));
        writer.newLine();

        writer.write(String.format("language=%s", Settings.getLanguage()));
        writer.newLine();

        writer.write(String.format("bossDifficulty=%s", Settings.getBossDifficulty().name()));
        writer.newLine();

        writer.write(String.format("minRandomRemovedItems=%s", Settings.getMinRandomRemovedItems()));
        writer.newLine();

        writer.write(String.format("maxRandomRemovedItems=%s", Settings.getMaxRandomRemovedItems()));
        writer.newLine();

        for(String item : DataFromFile.getAllItems()) {
            if(Settings.getRemovedItems().contains(item)) {
                writer.write(String.format("randomization.%s=%s", item, "REMOVED"));
            }
            else if(Settings.getInitiallyAccessibleItems().contains(item)) {
                writer.write(String.format("randomization.%s=%s", item, "INITIAL"));
            }
            else if(Settings.getNonRandomizedItems().contains(item)) {
                writer.write(String.format("randomization.%s=%s", item, "NONRANDOM"));
            }
            else if(Settings.getSurfaceItems().contains(item)) {
                writer.write(String.format("randomization.%s=%s", item, "V_EARLY"));
            }
            else {
                writer.write(String.format("randomization.%s=%s", item, "RANDOM"));
            }
            writer.newLine();
        }

        for(String glitchOption : DataFromFile.getAvailableGlitches()) {
            writer.write(String.format("glitches.%s=%s", glitchOption, Settings.getEnabledGlitches().contains(glitchOption)));
            writer.newLine();
        }

        for(String dboostOption : Arrays.asList("Enemy", "Item", "Environment")) {
            writer.write(String.format("dboost.%s=%s", dboostOption, Settings.getEnabledDamageBoosts().contains(dboostOption)));
            writer.newLine();
        }

        writer.flush();
        writer.close();
    }

    public static void logException(Exception ex) {
        FileUtils.log(ex.getClass().getName() + ": " + ex.getMessage());
        FileUtils.log("File: " + ex.getStackTrace()[0].getFileName());
        FileUtils.log("Method: " + ex.getStackTrace()[0].getMethodName());
        FileUtils.log("Line: " + ex.getStackTrace()[0].getLineNumber());
        FileUtils.log("File: " + ex.getStackTrace()[1].getFileName());
        FileUtils.log("Method: " + ex.getStackTrace()[1].getMethodName());
        FileUtils.log("Line: " + ex.getStackTrace()[1].getLineNumber());
    }

    public static void log(String logText) {
        try {
            if(logWriter == null) {
                logWriter = getFileWriter("log.txt");
            }

            logWriter.write(logText);
            logWriter.newLine();
        } catch (Exception ex) {

        }
    }

    public static void closeAll() {
        try {
            logWriter.flush();
            logWriter.close();
            logWriter = null;
        } catch (Exception ex) {

        }
    }
}