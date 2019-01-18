package lmr.randomizer;

import lmr.randomizer.node.CustomPlacement;
import lmr.randomizer.node.NodeWithRequirements;
import lmr.randomizer.update.GameObjectId;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by thezerothcat on 7/10/2017.
 */
public class FileUtils {
    public static final String VERSION = "2.8.0";
    private static final int CUSTOM_IMAGE_HEIGHT = 80;

    private static BufferedWriter logWriter;
    private static final List<String> KNOWN_RCD_FILE_HASHES = new ArrayList<>();
    private static final String CHAR_TO_SHORT_CONVERSION = "!\"&'(),-./0123456789:?ABCDEFGHIJKLMNOPQRSTUVWXYZ　]^_abcdefghijklmnopqrstuvwxyz…♪、。々「」ぁあぃいぅうぇえぉおかがきぎくぐけげこごさざしじすずせぜそぞただちぢっつづてでとどなにぬねのはばぱひびぴふぶぷへべぺほぼぽまみむめもゃやゅゆょよらりるれろわをんァアィイゥウェエォオカガキギクグケゲコゴサザシジスズセゼソゾタダチヂッツヅテデトドナニヌネノハバパヒビピフブプヘベペホボポマミムメモャヤュユョヨラリルレロワヲンヴ・ー一三上下不与世丘両中丸主乗乙乱乳予争事二人今介仕他付代以仮仲件会伝位低住体何作使供侵係保信俺倍倒値偉側偶備傷像僧元兄先光兜入全公具典内再冒冥出刀分切列初別利刻則前剣創力加助効勇勉動化匹十半協博印危去参双反取受叡口古召可台史右司合同名向否周呪味呼命品唯唱問喜営器噴四回囲図国土在地坂型域基堂報場塊塔墓増壁壇壊士声売壷変外多夜夢大天太央失奇契奥女好妊妖妻始姿娘婦子字存孤学宇守官宙定宝実客室宮家密寝対封専導小少尾屋屏属山岩崖崩嵐左巨己布帯帰常年幸幻幾広床底店度座庫廊廟弁引弟弱張強弾当形影役彼待後心必忍忘応念怒思急性怨恐息恵悔悟悪悲情惑想意愚愛感慈態憶我戦戻所扉手扱投抜押拝拡拳拾持指振探撃撮操支攻放敗教散数敵敷文料斧断新方旅族日早昇明昔星映時晩普晶智暗曲書最月有服望未末本杉村杖束来杯板析果架柱査格械棺検椿楼楽槍様槽模樹橋機欠次欲歓止正武歩歯歳歴死殊残段殺殿母毒毛気水氷永求汝池決治法波泥注洞洪流海消涙涯深済減湖満源溶滅滝火灯灼炎無然熱爆爪父版牛物特犬状狂独獄獅獣玄玉王珠現球理瓶生産用男画界略番発登白百的盤目直盾看真眼着知石研破碑示礼社祈祖神祠祭禁福私秘秤移種穴究空突窟立竜章竪端笛符第筒答箱範精系約納純紫細紹終経結続緑練罠罪罰義羽習翻翼老考者耐聖聞肉肩胸能脱腕自至船色若苦英荷華落葉蔵薇薔薬蛇血行術衛表裁装裏補製複要見覚親解言記訳証試話詳認誕誘語誠説読誰調論謁謎謝識議護谷貝財貧貯買貸資賢贄贖赤走起超足跡路踊蹴身車軽輝辞込辿近返迷追送逃通速造連進遊過道達違遠適選遺還郎部配重野量金針鉄銀銃銅録鍵鎖鏡長門閉開間関闇闘防限険陽階隠雄雑難雨霊青静面革靴音順領頭題顔願類風飛食館馬駄験骨高魂魔魚鳥鳴黄黒泉居転清成仏拠維視宿浮熟飾冷得集安割栄偽屍伸巻緒捨固届叩越激彫蘇狭浅Ⅱ［］：！？～／０１２３４５６７８９ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＲＳＴＵＶＷＸＹａｂｄｅｇｈｉｌｍｏｐｒｓｔｕｘ辺薄島異温復称狙豊穣虫絶ＱＺｃｆｊｋｎｑｖｗｙｚ＋－旧了設更橫幅似確置整＞％香ü描園為渡象相聴比較掘酷艇原民雷絵南米平木秋田県湯環砂漠角運湿円背負構授輪圏隙草植快埋寺院妙該式判（）警告収首腰芸酒美組各演点勝観編丈夫姫救’，．霧節幽技師柄期瞬電購任販Á;û+→↓←↑⓪①②③④⑤⑥⑦⑧⑨<”挑朝痛魅鍛戒飲憂照磨射互降沈醜触煮疲素競際易堅豪屈潔削除替Ü♡*$街極";

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

    public static BufferedReader getFileReader(String file, boolean inFolder) {
        try {
            return new BufferedReader(new FileReader(inFolder ? ("src/main/resources/lmr/randomizer/" + file) : file));
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

    public static byte[] getBytes(String path) throws IOException {
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
            String md5Hash = printHexBinary(md5.digest(fileBytes)).toUpperCase();
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

    // Copied from java 8, to avoid importing a dependency no longer present in java 11.
    private static String printHexBinary(byte[] data) {
        char[] hexCode = "0123456789ABCDEF".toCharArray();
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

    public static boolean hashDatFile(File file) {
        return true; // todo: implement this when I get file hash info
    }

    public static List<String> getList(String file) {
        List<String> listContents = new ArrayList<>();
        try(BufferedReader reader = getFileReader(file, true)) {
            String line;
            while((line = reader.readLine()) != null) {
                line = line.split("#", 2)[0].trim(); // remove comments
                if(!line.isEmpty()) {
                    listContents.add(line);
                }
            }
            reader.close();
        } catch (Exception ex) {
            FileUtils.log("Unable to read file " + file + ", " + ex.getMessage());
            return new ArrayList<>(0);
        }
        return listContents;
    }

    public static List<Map.Entry<String, List<String>>> getListOfLists(String file, boolean inFolder) {
        List<Map.Entry<String, List<String>>> data = new ArrayList<>();
        try(BufferedReader reader = getFileReader(file, inFolder)) {
            if(reader != null) {
                String line;
                String[] lineParts;
                while((line = reader.readLine()) != null) {
                    line = line.split("#", 2)[0].trim(); // remove comments
                    if (line.isEmpty())
                        continue;
                    lineParts = line.split("=>", 2); // delimiter
                    List<String> reqs = new ArrayList<>();
                    for (String req : lineParts[1].split(","))
                        reqs.add(req.trim());
                    data.add(new AbstractMap.SimpleImmutableEntry(lineParts[0].trim(), reqs));
                }
            }
        } catch (Exception ex) {
            FileUtils.log("Unable to read file " + file + ", " + ex.getMessage());
            return new ArrayList<>();
        }
        return data;
    }

    public static void populateRequirements(Map<String, NodeWithRequirements> mapOfNodeNameToRequirementsObject, String file, boolean inFolder) {
        for (Map.Entry<String, List<String>> line : getListOfLists(file, inFolder))
            addNode(mapOfNodeNameToRequirementsObject, line.getKey(), line.getValue());
    }

    public static Map<String, List<String>> getAccessibleLocations(String file) {
        Map<String, List<String>> accessibleLocations = new HashMap<>();
        for (Map.Entry<String, List<String>> line : getListOfLists(file, true)) {
            if (accessibleLocations.containsKey(line.getKey()))
                throw new RuntimeException(file + " contains duplicated key " + line.getKey());
            accessibleLocations.put(line.getKey(), line.getValue());
        }
        return accessibleLocations;
    }

    private static void addNode(Map<String, NodeWithRequirements> mapOfNodeNameToRequirementsObject, String name, List<String> requirementSet) {
        for(String glitch : DataFromFile.POSSIBLE_GLITCHES) {
            if(!Settings.getEnabledGlitches().contains(glitch)) {
                if(requirementSet.contains("Glitch: " + glitch)) {
                    return; // This requirement set includes a glitch that's not enabled, don't include it.
                }
            }
        }
        for(String dboost : DataFromFile.POSSIBLE_DBOOSTS) {
            if(!Settings.getEnabledDamageBoosts().contains(dboost)) {
                if(requirementSet.contains("Boost: " + dboost)) {
                    return; // This requirement set includes a dboost that's not enabled, don't include it.
                }
            }
        }
        if(Settings.isRequireIceCapeForLava() && requirementSet.contains("Setting: Lava HP")) {
            return;
        }
        NodeWithRequirements node = mapOfNodeNameToRequirementsObject.get(name);
        if(node == null) {
            node = new NodeWithRequirements(name);
            mapOfNodeNameToRequirementsObject.put(name, node);
        }
        node.addRequirementSet(buildRequirementSet(requirementSet));
    }

    private static List<String> buildRequirementSet(List<String> requirementSet) {
        List<String> requirements = new ArrayList<>(requirementSet);
        requirements.removeIf(x -> "None".equalsIgnoreCase(x));
        return requirements;
    }

    public static Map<String, GameObjectId> getRcdDataIdMap(String filename) {
        Map<String, GameObjectId> map = new HashMap<>();
        for (Map.Entry<String, List<String>> line : getListOfLists(filename, true)) {
            if (map.containsKey(line.getKey()))
                throw new RuntimeException(filename + " contains duplicated key " + line.getKey());
            if (line.getValue().size() != 2)
                throw new RuntimeException("Malformed RCD data for key " + line.getKey());
            map.put(line.getKey(), new GameObjectId(line.getValue().get(0), line.getValue().get(1)));
        }
        return map;
    }

    public static Map<String, Integer> getShopBlockMap(String filename) {
        Map<String, Integer> map = new HashMap<>();
        for (Map.Entry<String, List<String>> line : getListOfLists(filename, true)) {
            if (map.containsKey(line.getKey()))
                throw new RuntimeException(filename + " contains duplicated key " + line.getKey());
            if (line.getValue().size() != 1)
                throw new RuntimeException("Malformed shop block entry: " + line.getKey());
            int block = Integer.parseInt(line.getValue().get(0));
            map.put(line.getKey(), Integer.parseInt(line.getValue().get(0)));
        }
        return map;
    }

    public static Map<String, List<String>> getShopOriginalContentsMap(String filename) {
        Map<String, List<String>> map = new HashMap<>();
        for (Map.Entry<String, List<String>> line : getListOfLists(filename, true)) {
            if (map.containsKey(line.getKey()))
                throw new RuntimeException(filename + " contains duplicated key " + line.getKey());
            if (line.getValue().size() != 3)
                throw new RuntimeException("Malformed shop contents: " + line.getKey());
            map.put(line.getKey(), line.getValue());
        }
        return map;
    }

    public static List<Short> stringToData(String stringToConvert) {
        List<Short> dataString = new ArrayList<>(stringToConvert.length());
        short data;
        char charAtIndex;
        for (int i = 0; i < stringToConvert.length(); i++) {
            charAtIndex = stringToConvert.charAt(i);
            if (charAtIndex == ' ') {
                dataString.add((short)32);
            }
            else {
                data = (short)(CHAR_TO_SHORT_CONVERSION.indexOf(charAtIndex) + 0x0100);
                dataString.add(data);
            }
        }
        return dataString;
    }

    public static List<CustomPlacement> getCustomPlacementData() {
        if (!(new File("custom-placement.txt").exists())) {
            return new ArrayList<>(0);
        }
        try(BufferedReader reader = getFileReader("custom-placement.txt", false)) {
            if(reader == null) {
                return new ArrayList<>(0);
            }
            List<CustomPlacement> data = new ArrayList<>();
            String line;
            String[] lineParts;
            boolean alternateMotherAnkh = false;
            boolean automaticMantras = false;
            String medicineColor = null;
            while((line = reader.readLine()) != null) {
                line = line.split("#", 2)[0].trim(); // remove comments
                if (line.isEmpty()) {
                    continue;
                }

                if (line.contains("=")) {
                    lineParts = line.split("=", 2); // delimiter
                    if (lineParts.length > 1) {
                        String location = lineParts[0].trim();
                        String contents = lineParts[1].trim();
                        if (contents.contains("{") && contents.contains("}")) {
                            String specialData = contents.substring(contents.indexOf("{") + 1).replace("}", "");
                            contents = contents.substring(0, contents.indexOf('{')).trim();
                            if(contents.startsWith("Trap:")) {
                                data.add(new CustomPlacement(location, contents, specialData, false));
                            }
                            else if(location.startsWith("Shop ")) {
                                lineParts = specialData.split(",");
                                if (lineParts.length > 1) {
                                    data.add(new CustomPlacement(location, contents, Short.parseShort(lineParts[1].trim()), Short.parseShort(lineParts[0].trim())));
                                }
                                else {
                                    data.add(new CustomPlacement(location, contents, Short.parseShort(lineParts[0].trim()), null));
                                }
                            }
                        } else {
                            data.add(new CustomPlacement(lineParts[0].trim(), contents, null, false));
                        }
                    }
                }
                else {
                    if(line.startsWith("!")) {
                        String removeItem = line.trim();
                        if(removeItem.startsWith("!")) {
                            removeItem = removeItem.substring(1);
                            data.add(new CustomPlacement(removeItem, true, false));
                        }
                    }
                    else if (line.startsWith("Remove")) {
                        data.add(new CustomPlacement(line.replace("Remove", "").trim(), true, true));
                    }
                    else if (line.startsWith("Curse")) {
                        data.add(new CustomPlacement(line.replace("Curse", "").trim(), null, null, true));
                    }
                    else if (line.startsWith("Weapon:")) {
                        data.add(new CustomPlacement(line.replace("Weapon:", "").trim(), false, true));
                    }
                    else if (line.startsWith("Start:")) {
                        data.add(new CustomPlacement(line.replace("Start:", "").trim(), false, false));
                    }
                    else if (line.startsWith("Remove Logic:")) {
                        data.add(new CustomPlacement(line.replace("Remove Logic:", "").trim()));
                    }
                    else if (line.equals("Skip Mantras")) {
                        automaticMantras = true;
                    }
                    else if (line.equals("Alternate Mother Ankh")) {
                        alternateMotherAnkh = true;
                    }
                    else if (line.startsWith("Fill Vessel ")) {
                        String color = line.replace("Fill Vessel ", "").trim();
                        if(color.equalsIgnoreCase("red")) {
                            medicineColor = "Red";
                        }
                        else if(color.equalsIgnoreCase("green")) {
                            medicineColor = "Green";
                        }
                        else if(color.equalsIgnoreCase("yellow")) {
                            medicineColor = "Yellow";
                        }
                    }
                }
            }
            Settings.setAlternateMotherAnkh(alternateMotherAnkh);
            Settings.setAutomaticMantras(automaticMantras);
            Settings.setMedicineColor(medicineColor);
            return data;
        } catch (Exception ex) {
            FileUtils.log("Unable to read file custom-placement.txt, " + ex.getMessage());
        }
        return new ArrayList<>(0);
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
        Set<String> startingItems = new HashSet<>();
        while((line = reader.readLine()) != null) {
            if(line.startsWith("randomization.")) {
                settingAndValue = line.replace("randomization.", "").split("=");
                if("INITIAL".equals(settingAndValue[1])) {
                    initiallyAvailableItems.add(settingAndValue[0]);
                }
                else if("STARTING".equals(settingAndValue[1])) {
                    startingItems.add(settingAndValue[0]);
                }
                else if("REMOVED".equals(settingAndValue[1])) {
                    Settings.setRemovedItem(settingAndValue[0], true, false);
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
            else if(line.startsWith("automaticHardmode")) {
                Settings.setAutomaticHardmode(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("automaticTranslations")) {
                Settings.setAutomaticTranslations(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("requireSoftwareComboForKeyFairy")) {
                Settings.setRequireSoftwareComboForKeyFairy(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("requireFullAccess")) {
                Settings.setRequireFullAccess(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("requireIceCapeForLava")) {
                Settings.setRequireIceCapeForLava(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("requireFlaresForExtinction")) {
                Settings.setRequireFlaresForExtinction(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("randomizeXmailer")) {
                Settings.setRandomizeXmailer(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("randomizeForbiddenTreasure")) {
                Settings.setRandomizeForbiddenTreasure(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("htFullRandom")) {
                Settings.setHTFullRandom(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("randomizeDracuetShop")) {
                Settings.setRandomizeDracuetShop(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("randomizeCoinChests")) {
                Settings.setRandomizeCoinChests(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("randomizeTrapItems")) {
                Settings.setRandomizeTrapItems(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("randomizeEscapeChest")) {
                Settings.setRandomizeEscapeChest(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("allowWhipStart")) {
                Settings.setAllowWhipStart(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("allowMainWeaponStart")) {
                Settings.setAllowMainWeaponStart(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("allowSubweaponStart")) {
                Settings.setAllowSubweaponStart(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("subweaponOnlyLogic")) {
                Settings.setSubweaponOnlyLogic(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("removeMainWeapons")) {
                Settings.setRemoveMainWeapons(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("randomizeCursedChests")) {
                Settings.setRandomizeCursedChests(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("randomizeTransitionGates")) {
                Settings.setRandomizeTransitionGates(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("randomizeOneWayTransitions")) {
                Settings.setRandomizeOneWayTransitions(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("randomizeBacksideDoors")) {
                Settings.setRandomizeBacksideDoors(Boolean.valueOf(line.split("=")[1]), false);
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
            else if(line.startsWith("ushumgalluAssist")) {
                Settings.setUshumgalluAssist(Boolean.valueOf(line.split("=")[1]), false);
            }
            else if(line.startsWith("quickStartItemsEnabled")) {
                // Upgrade legacy settings
                if(Boolean.valueOf(line.split("=")[1])) {
                    startingItems.add("Holy Grail");
                    startingItems.add("Hermes' Boots");
                    startingItems.add("mirai.exe");
                }
            }
            else if(line.startsWith("laMulanaBaseDir")) {
                Settings.setLaMulanaBaseDir(line.substring(line.indexOf("=") + 1), false);
            }
            else if(line.startsWith("laMulanaSaveDir")) {
                Settings.setLaMulanaSaveDir(line.substring(line.indexOf("=") + 1), false);
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
        Settings.setStartingItems(startingItems, false);
    }

    public static void saveSettings() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("randomizer-config.txt"));
        writer.write(String.format("version=%s", VERSION));
        writer.newLine();

        writer.write(String.format("seed=%s", Settings.getStartingSeed()));
        writer.newLine();

        writer.write(String.format("shopRandomization=%s", Settings.getShopRandomization().toString()));
        writer.newLine();

        writer.write(String.format("automaticHardmode=%s", Settings.isAutomaticHardmode()));
        writer.newLine();

        writer.write(String.format("requireSoftwareComboForKeyFairy=%s", Settings.isRequireSoftwareComboForKeyFairy()));
        writer.newLine();

        writer.write(String.format("requireFullAccess=%s", Settings.isRequireFullAccess()));
        writer.newLine();

        writer.write(String.format("requireIceCapeForLava=%s", Settings.isRequireIceCapeForLava()));
        writer.newLine();

        writer.write(String.format("requireFlaresForExtinction=%s", Settings.isRequireFlaresForExtinction()));
        writer.newLine();

        writer.write(String.format("randomizeXmailer=%s", Settings.isRandomizeXmailer()));
        writer.newLine();

        writer.write(String.format("randomizeForbiddenTreasure=%s", Settings.isRandomizeForbiddenTreasure()));
        writer.newLine();

        writer.write(String.format("htFullRandom=%s", Settings.isHTFullRandom()));
        writer.newLine();

        writer.write(String.format("randomizeDracuetShop=%s", Settings.isRandomizeDracuetShop()));
        writer.newLine();

        writer.write(String.format("randomizeCoinChests=%s", Settings.isRandomizeCoinChests()));
        writer.newLine();

        writer.write(String.format("randomizeTrapItems=%s", Settings.isRandomizeTrapItems()));
        writer.newLine();

        writer.write(String.format("randomizeEscapeChest=%s", Settings.isRandomizeEscapeChest()));
        writer.newLine();

        writer.write(String.format("allowWhipStart=%s", Settings.isAllowWhipStart()));
        writer.newLine();

        writer.write(String.format("allowMainWeaponStart=%s", Settings.isAllowMainWeaponStart()));
        writer.newLine();

        writer.write(String.format("allowSubweaponStart=%s", Settings.isAllowSubweaponStart()));
        writer.newLine();

        writer.write(String.format("subweaponOnlyLogic=%s", Settings.isSubweaponOnlyLogic()));
        writer.newLine();

        writer.write(String.format("removeMainWeapons=%s", Settings.isRemoveMainWeapons()));
        writer.newLine();

        writer.write(String.format("randomizeCursedChests=%s", Settings.isRandomizeCursedChests()));
        writer.newLine();

        writer.write(String.format("randomizeTransitionGates=%s", Settings.isRandomizeTransitionGates()));
        writer.newLine();

        writer.write(String.format("randomizeOneWayTransitions=%s", Settings.isRandomizeOneWayTransitions()));
        writer.newLine();

        writer.write(String.format("randomizeBacksideDoors=%s", Settings.isRandomizeBacksideDoors()));
        writer.newLine();

        writer.write(String.format("replaceMapsWithWeights=%s", Settings.isReplaceMapsWithWeights()));
        writer.newLine();

        writer.write(String.format("coinChestGraphics=%s", Settings.isCoinChestGraphics()));
        writer.newLine();

        writer.write(String.format("automaticGrailPoints=%s", Settings.isAutomaticGrailPoints()));
        writer.newLine();

        writer.write(String.format("automaticTranslations=%s", Settings.isAutomaticTranslations()));
        writer.newLine();

        writer.write(String.format("ushumgalluAssist=%s", Settings.isUshumgalluAssist()));
        writer.newLine();

        writer.write(String.format("laMulanaBaseDir=%s", Settings.getLaMulanaBaseDir()));
        writer.newLine();

        writer.write(String.format("laMulanaSaveDir=%s", Settings.getLaMulanaSaveDir()));
        writer.newLine();

        writer.write(String.format("language=%s", Settings.getLanguage()));
        writer.newLine();

        writer.write(String.format("bossDifficulty=%s", Settings.getBossDifficulty().name()));
        writer.newLine();

        writer.write(String.format("minRandomRemovedItems=%s", Settings.getMinRandomRemovedItems()));
        writer.newLine();

        writer.write(String.format("maxRandomRemovedItems=%s", Settings.getMaxRandomRemovedItems()));
        writer.newLine();

        List<String> items = new ArrayList<>(DataFromFile.getAllItems());
        items.remove("xmailer.exe");
        items.remove("Provocative Bathing Suit");
        for(String item : items) {
            if(Settings.getRemovedItems().contains(item)) {
                writer.write(String.format("randomization.%s=%s", item, "REMOVED"));
            }
            else if(Settings.getInitiallyAccessibleItems().contains(item)) {
                writer.write(String.format("randomization.%s=%s", item, "INITIAL"));
            }
            else if(Settings.getStartingItems().contains(item)) {
                writer.write(String.format("randomization.%s=%s", item, "STARTING"));
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

    public static boolean importExistingSeed(File zipFile) {
        File destinationFolder = new File(zipFile.getName().replaceAll("\\.zip$", ""));
        if(!destinationFolder.exists()) {
            destinationFolder.mkdir();
        }
        byte[] buffer = new byte[1024];
        try(ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while(zipEntry != null) {
                File newFile = getSafeFile(destinationFolder, zipEntry);
                FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                int length;
                while((length = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }
                fileOutputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.closeEntry();

            FileUtils.logFlush("Copying rcd file from seed folder to La-Mulana install directory");
            FileOutputStream fileOutputStream = new FileOutputStream(new File(Settings.getLaMulanaBaseDir() + "/data/mapdata/script.rcd"));
            Files.copy(new File(String.format("%s/script.rcd", destinationFolder)).toPath(), fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            FileUtils.logFlush("rcd copy complete");

            FileUtils.logFlush("Copying dat file from seed folder to La-Mulana install directory");
            fileOutputStream = new FileOutputStream(new File(String.format("%s/data/language/%s/script_code.dat",
                    Settings.getLaMulanaBaseDir(), Settings.getLanguage())));
            Files.copy(new File(String.format("%s/script_code.dat", destinationFolder)).toPath(), fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            FileUtils.logFlush("dat copy complete");

            File saveFile = new File(destinationFolder, "lm_00.sav");
            if(saveFile.exists()) {
                FileUtils.logFlush("Copying save file from seed folder to La-Mulana save directory");
                    fileOutputStream = new FileOutputStream(
                            new File(String.format("%s/lm_00.sav", Settings.getLaMulanaSaveDir())));
                    Files.copy(saveFile.toPath(), fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
            }

            FileUtils.updateGraphicsFiles();

            FileUtils.logFlush("Save file copy complete");
        }
        catch (IOException ex) {
            FileUtils.log("Unable to unpack zipfile");
            FileUtils.logException(ex);
            return false;
        }
        return true;
    }

    public static File getSafeFile(File destinationFolder, ZipEntry zipEntry) throws IOException {
        File destinationFile = new File(destinationFolder, zipEntry.getName());
        if(!destinationFile.getCanonicalPath().startsWith(destinationFolder.getCanonicalPath() + File.separator)) {
            throw new IOException("Entry is outside of target folder: " + zipEntry.getName());
        }
        return destinationFile;
    }

    private static boolean backupGraphicsFile(File graphicsPack) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(graphicsPack, "01effect.png.bak"));
            Files.copy(new File(graphicsPack, "01effect.png").toPath(), fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        }
        catch (IOException ex) {
            return false;
        }
    }

    public static boolean updateGraphicsFiles() {
        BufferedImage custom;
        try {
            custom = ImageIO.read(FileUtils.class.getResource("01effect-custom.png"));
        }
        catch (IOException ex) {
            return false;
        }

        for(File graphicsPack : getGraphicsPacks()) {
            try {
                if(!backupGraphicsFile(graphicsPack)) {
                    return false;
                }
                File graphicsFile = new File(graphicsPack, "01effect.png");
                BufferedImage existing = ImageIO.read(graphicsFile);
                if(existing.getHeight() != (512 + CUSTOM_IMAGE_HEIGHT)) {
                    FileUtils.logFlush("Updating graphics file: " + graphicsFile.getAbsolutePath());
                    // Hasn't been updated yet.
                    BufferedImage newImage = new BufferedImage(existing.getWidth(), existing.getHeight() + custom.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D graphics2D = newImage.createGraphics();
                    graphics2D.drawImage(existing, null, 0, 0);
                    graphics2D.drawImage(custom, null, 0, existing.getHeight());
                    graphics2D.dispose();
                    ImageIO.write(newImage, "png", graphicsFile);
                    FileUtils.log("Graphics file successfully updated");
                }
            }
            catch (IOException ex) {
                return false;
            }
        }
        return true;
    }

    private static List<File> getGraphicsPacks() {
        File graphicsFolder = new File(Settings.getLaMulanaBaseDir() + "/data/graphics");
        if(graphicsFolder.exists() && graphicsFolder.isDirectory()) {
            List<File> graphicsSubfolders = new ArrayList<>();
            for(File file : graphicsFolder.listFiles()) {
                if(file.isDirectory()) {
                    graphicsSubfolders.add(file);
                }
            }
            return graphicsSubfolders;
        }
        return new ArrayList<>(0);
    }

    public static void logException(Exception ex) {
        log(ex.getClass().getName() + ": " + ex.getMessage());
        log("File: " + ex.getStackTrace()[0].getFileName());
        log("Method: " + ex.getStackTrace()[0].getMethodName());
        log("Line: " + ex.getStackTrace()[0].getLineNumber());
        log("File: " + ex.getStackTrace()[1].getFileName());
        log("Method: " + ex.getStackTrace()[1].getMethodName());
        log("Line: " + ex.getStackTrace()[1].getLineNumber());
        flush();
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

    public static void logFlush(String logText) {
        log(logText);
        flush();
    }

    public static void logDetail(String logText, Integer attemptNumber) {
        if(Settings.isDetailedLoggingAttempt(attemptNumber)) {
            log(logText);
        }
    }

    public static void flush() {
        try {
            logWriter.flush();
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