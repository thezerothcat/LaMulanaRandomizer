package lmr.randomizer.dat;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.dat.conversation.CheckBlock;
import lmr.randomizer.dat.shop.BlockCmdSingle;
import lmr.randomizer.dat.shop.BlockStringData;
import lmr.randomizer.dat.shop.ShopBlock;
import lmr.randomizer.update.GameDataTracker;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/26/2017.
 */
public final class DatReader {
    private static final String CHAR_TO_SHORT_CONVERSION = "!\"&'(),-./0123456789:?ABCDEFGHIJKLMNOPQRSTUVWXYZ　]^_abcdefghijklmnopqrstuvwxyz…♪、。々「」ぁあぃいぅうぇえぉおかがきぎくぐけげこごさざしじすずせぜそぞただちぢっつづてでとどなにぬねのはばぱひびぴふぶぷへべぺほぼぽまみむめもゃやゅゆょよらりるれろわをんァアィイゥウェエォオカガキギクグケゲコゴサザシジスズセゼソゾタダチヂッツヅテデトドナニヌネノハバパヒビピフブプヘベペホボポマミムメモャヤュユョヨラリルレロワヲンヴ・ー一三上下不与世丘両中丸主乗乙乱乳予争事二人今介仕他付代以仮仲件会伝位低住体何作使供侵係保信俺倍倒値偉側偶備傷像僧元兄先光兜入全公具典内再冒冥出刀分切列初別利刻則前剣創力加助効勇勉動化匹十半協博印危去参双反取受叡口古召可台史右司合同名向否周呪味呼命品唯唱問喜営器噴四回囲図国土在地坂型域基堂報場塊塔墓増壁壇壊士声売壷変外多夜夢大天太央失奇契奥女好妊妖妻始姿娘婦子字存孤学宇守官宙定宝実客室宮家密寝対封専導小少尾屋屏属山岩崖崩嵐左巨己布帯帰常年幸幻幾広床底店度座庫廊廟弁引弟弱張強弾当形影役彼待後心必忍忘応念怒思急性怨恐息恵悔悟悪悲情惑想意愚愛感慈態憶我戦戻所扉手扱投抜押拝拡拳拾持指振探撃撮操支攻放敗教散数敵敷文料斧断新方旅族日早昇明昔星映時晩普晶智暗曲書最月有服望未末本杉村杖束来杯板析果架柱査格械棺検椿楼楽槍様槽模樹橋機欠次欲歓止正武歩歯歳歴死殊残段殺殿母毒毛気水氷永求汝池決治法波泥注洞洪流海消涙涯深済減湖満源溶滅滝火灯灼炎無然熱爆爪父版牛物特犬状狂独獄獅獣玄玉王珠現球理瓶生産用男画界略番発登白百的盤目直盾看真眼着知石研破碑示礼社祈祖神祠祭禁福私秘秤移種穴究空突窟立竜章竪端笛符第筒答箱範精系約納純紫細紹終経結続緑練罠罪罰義羽習翻翼老考者耐聖聞肉肩胸能脱腕自至船色若苦英荷華落葉蔵薇薔薬蛇血行術衛表裁装裏補製複要見覚親解言記訳証試話詳認誕誘語誠説読誰調論謁謎謝識議護谷貝財貧貯買貸資賢贄贖赤走起超足跡路踊蹴身車軽輝辞込辿近返迷追送逃通速造連進遊過道達違遠適選遺還郎部配重野量金針鉄銀銃銅録鍵鎖鏡長門閉開間関闇闘防限険陽階隠雄雑難雨霊青静面革靴音順領頭題顔願類風飛食館馬駄験骨高魂魔魚鳥鳴黄黒泉居転清成仏拠維視宿浮熟飾冷得集安割栄偽屍伸巻緒捨固届叩越激彫蘇狭浅Ⅱ［］：！？～／０１２３４５６７８９ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＲＳＴＵＶＷＸＹａｂｄｅｇｈｉｌｍｏｐｒｓｔｕｘ辺薄島異温復称狙豊穣虫絶ＱＺｃｆｊｋｎｑｖｗｙｚ＋－旧了設更橫幅似確置整＞％香ü描園為渡象相聴比較掘酷艇原民雷絵南米平木秋田県湯環砂漠角運湿円背負構授輪圏隙草植快埋寺院妙該式判（）警告収首腰芸酒美組各演点勝観編丈夫姫救’，．霧節幽技師柄期瞬電購任販Á;û+→↓←↑⓪①②③④⑤⑥⑦⑧⑨<”挑朝痛魅鍛戒飲憂照磨射互降沈醜触煮疲素競際易堅豪屈潔削除替Ü♡*$街極";

    private DatReader() {
    }


    private static ShopBlock buildShopBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        int dataIndex = 0;
        ShopBlock shopBlock = new ShopBlock(blockIndex);

        List<BlockListData> shopBlockData = new ArrayList<>();
        for(int i = 0; i < 4; i ++) {
            short cmdShort = dataInputStream.readShort();
            ++dataIndex;

            short numberOfOpts = dataInputStream.readShort();
            ++dataIndex;
            BlockListData blockListData = new BlockListData(cmdShort, numberOfOpts);
            for(int blockListDataIndex = 0; blockListDataIndex < numberOfOpts; blockListDataIndex++) {
                blockListData.getData().add(dataInputStream.readShort());
                ++dataIndex;
            }

            dataInputStream.readShort(); // 0x000a
            ++dataIndex;

            shopBlockData.add(blockListData);
        }
        shopBlock.setInventoryItemArgsList(shopBlockData.get(0));
        shopBlock.setInventoryPriceList(shopBlockData.get(1));
        shopBlock.setInventoryCountList(shopBlockData.get(2));
        shopBlock.setFlagList(shopBlockData.get(3));

        for(int i = 0; i < 18; i ++) {
            BlockStringData blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            shopBlock.setString(blockStringData, i);
        }

        dataInputStream.readShort(); // Ignore CMD
        ++dataIndex;
        dataInputStream.readShort(); // Ignore length, we already know it's 1
        ++dataIndex;
        shopBlock.setBackground(new BlockCmdSingle(dataInputStream.readShort()));
        ++dataIndex;
        dataInputStream.readShort(); // Ignore 0x000a
        ++dataIndex;

        dataInputStream.readShort(); // Ignore CMD
        ++dataIndex;
        dataInputStream.readShort(); // Ignore length, we already know it's 1
        ++dataIndex;
        shopBlock.setSprite(new BlockCmdSingle(dataInputStream.readShort()));
        ++dataIndex;
        dataInputStream.readShort(); // Ignore 0x000a
        ++dataIndex;

        // Exit flags
        short cmdShort = dataInputStream.readShort(); // Ignore CMD
        ++dataIndex;
        short numberOfOpts = dataInputStream.readShort();
        ++dataIndex;
        BlockListData blockListData = new BlockListData(cmdShort, numberOfOpts);
        for(int blockListDataIndex = 0; blockListDataIndex < numberOfOpts; blockListDataIndex++) {
            blockListData.getData().add(dataInputStream.readShort());
            ++dataIndex;
        }
        dataInputStream.readShort(); // Ignore 0x000a
        ++dataIndex;
        shopBlock.setExitFlagList(blockListData);

        dataInputStream.readShort(); // Ignore CMD
        ++dataIndex;
        dataInputStream.readShort(); // Ignore length, we already know it's 1
        ++dataIndex;
        shopBlock.setMusic(new BlockCmdSingle(dataInputStream.readShort()));
        ++dataIndex;
        dataInputStream.readShort(); // Ignore 0x000a
        ++dataIndex;

        BlockStringData blockStringData = new BlockStringData();
        dataIndex += populateBlockStringData(blockStringData, dataInputStream);
        shopBlock.setBunemonLocation(blockStringData);

        blockStringData = new BlockStringData();
        while(dataIndex < numberOfShortsInThisBlock) {
            blockStringData.getData().add(dataInputStream.readShort());
            ++dataIndex;
        }
        shopBlock.setBunemonText(blockStringData);
        return shopBlock;
    }

    private static Block buildCheckBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        int dataIndex = 0;
        CheckBlock checkBlock = new CheckBlock(blockIndex);

        while(dataIndex < numberOfShortsInThisBlock) {
            short cmdShort = dataInputStream.readShort();
            ++dataIndex;

            short numberOfOpts = dataInputStream.readShort();
            ++dataIndex;

            BlockListData blockListData = new BlockListData(cmdShort, numberOfOpts);
            for(int blockListDataIndex = 0; blockListDataIndex < numberOfOpts; blockListDataIndex++) {
                blockListData.getData().add(dataInputStream.readShort());
                ++dataIndex;
            }
            checkBlock.getFlagCheckReferences().add(blockListData);

            if(dataIndex < numberOfShortsInThisBlock) {
                dataInputStream.readShort(); // 0x000a
                ++dataIndex;
            }
        }
        return checkBlock;
    }

    private static Block buildItemDescriptionBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        int dataIndex = 0;
        Block itemDescriptionBlock = new Block(blockIndex);
        String versionString = " Randomizer version " + FileUtils.VERSION;
        String settingsString = "Settings " + Settings.generateShortString();

        BlockStringData blockStringData;

        // Add everything before Mobile Super X
        for(int i = 0; i < 35; i ++) {
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }

        // Get the data for Mobile Super X, but throw it away in favor of custom replacement
        dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(stringToData("Mobile　Super　X：" + versionString));
        blockStringData.getData().add((short)0x0045);
        blockStringData.getData().addAll(stringToData(settingsString));
        blockStringData.getData().add((short)0x000a);
        itemDescriptionBlock.getBlockContents().add(blockStringData);

        // Add everything before Mobile Super X2
        for(int i = 0; i < 40; i ++) {
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }

        // Get the data for Mobile Super X2, but throw it away in favor of custom replacement
        dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(stringToData("Mobile　Super　X2：" + versionString));
        blockStringData.getData().add((short)0x0045);
        blockStringData.getData().addAll(stringToData(settingsString));
        blockStringData.getData().add((short)0x000a);
        itemDescriptionBlock.getBlockContents().add(blockStringData);

        while(dataIndex < numberOfShortsInThisBlock) {
            short blockData = dataInputStream.readShort();
            ++dataIndex;
            itemDescriptionBlock.getBlockContents().add(new BlockSingleData(blockData));
        }
        return itemDescriptionBlock;
    }

    private static List<Short> stringToData(String stringToConvert) {
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

    private static Block buildGrailPointBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        Block grailPointsBlock = new Block(blockIndex);
        addBlockContentsToBlock(grailPointsBlock, dataInputStream, numberOfShortsInThisBlock);
        BlockListData blockListData;
//        for(BlockContents blockContents : grailPointsBlock.getBlockContents()) {
//            if(blockContents instanceof BlockListData) {
//                blockListData = (BlockListData)blockContents;
//                if(blockListData.getData().get(1).equals((short)108)) {
//                    blockListData.getData().set(1, (short)2783);
//                    blockListData.getData().set(2, (short)18);
//                }
//            }
//        }


//        blockListData = new BlockListData((short)0x004e, (short)8);
//        blockListData.getData().add((short)0);
//        blockListData.getData().add((short)2782);
//        blockListData.getData().add((short)18);
//        blockListData.getData().add((short)9);
//        blockListData.getData().add((short)7);
//        blockListData.getData().add((short)0);
//        blockListData.getData().add((short)420);
//        blockListData.getData().add((short)72);
//        grailPointsBlock.getBlockContents().add(19, blockListData);
//        grailPointsBlock.getBlockContents().add(19, new BlockSingleData((short)0x000a));
        return grailPointsBlock;
    }

    private static int populateBlockStringData(BlockStringData blockStringData, DataInputStream dataInputStream) throws IOException{
        int dataIndex = 0;

        while (true) {
            short data = dataInputStream.readShort();
            ++dataIndex;

            if (data == 0x000a) {
//                s = unichr(o)
                return dataIndex;
            }
            else if(data == 0x000c || data == 0x0020) {
                blockStringData.getData().add(data);
            }
            else if (data >= 0x0040 && data <= 0x0050) {
                if (data == 0x0040) {
//                    cmd = "{FLAG %d:=%d}" % (ord(b[0]), ord(b[1]))
//                    b = b[2:]

                    blockStringData.getData().add(data);
                    blockStringData.getData().add(dataInputStream.readShort());
                    blockStringData.getData().add(dataInputStream.readShort());
                    ++dataIndex;
                    ++dataIndex;
                } else if (data == 0x0042) {
//                    cmd = "{ITEM %d}" % ord(b[0])
//                    b = b[1:]

                    blockStringData.getData().add(data);
                    blockStringData.getData().add(dataInputStream.readShort());
                    ++dataIndex;
                } else if (data == 0x0044) {
//                    cmd = "{CLS}"
//                    s = "<p>"
                    blockStringData.getData().add(data);
                } else if (data == 0x0045) {
//                    cmd = "{BR}"
//                    s = "<br>"
                    blockStringData.getData().add(data);
                } else if (data == 0x0046) {
//                    cmd = "{POSE %d}" % ord(b[0])
//                    b = b[1:]

                    blockStringData.getData().add(data);
                    blockStringData.getData().add(dataInputStream.readShort());
                    ++dataIndex;
                } else if (data == 0x0047) {
//                    cmd = "{MANTRA %d}" % ord(b[0])
//                    b = b[1:]

                    blockStringData.getData().add(data);
                    blockStringData.getData().add(dataInputStream.readShort());
                    ++dataIndex;
                } else if (data == 0x004a) {
//                    colors = [ord(x) for x in b[:3]]
//                    cmd = "{COL %03d-%03d-%03d}" % tuple(colors)
//                    if sum(colors) > 0:
//                        s = "<span style='color:#%02x%02x%02x'>" % tuple(200-x for x in colors)
//                    else:
//                        s = "</span>"
//                        b = b[3:] #TODO: colors not verified

                    if(blockStringData.getItemNameStartIndex() != null && blockStringData.getItemNameEndIndex() == null) {
                        blockStringData.setItemNameEndIndex(blockStringData.getData().size());
                    }

                    blockStringData.getData().add(data);
                    blockStringData.getData().add(dataInputStream.readShort());
                    blockStringData.getData().add(dataInputStream.readShort());
                    blockStringData.getData().add(dataInputStream.readShort());
                    ++dataIndex;
                    ++dataIndex;
                    ++dataIndex;

                    if(blockStringData.getItemNameStartIndex() == null) {
                        blockStringData.setItemNameStartIndex(blockStringData.getData().size());
                    }
                } else if (data == 0x004e) {
//                    lenopts = ord(b[0])
//                    opts = ["%d" % ord(x) for x in b[1:lenopts+1]]
//                    cmd = "{CMD %s}" % "-".join(opts)
//                    b = b[lenopts+1:]
                    blockStringData.getData().add(data);
                    short numberOfOpts = dataInputStream.readShort();
                    blockStringData.getData().add(numberOfOpts);
                    ++dataIndex;
                    for (int i = 0; i < numberOfOpts; i++) {
                        blockStringData.getData().add(dataInputStream.readShort());
                        ++dataIndex;
                    }
                } else if (data == 0x004f) {
//                    cmd = "{SCENE %d}" % ord(b[0])
//                    b = b[1:]
                    blockStringData.getData().add(data);
                    blockStringData.getData().add(dataInputStream.readShort());
                    ++dataIndex;
                } else {
//                    cmd = "{%02x}" % o
                    blockStringData.getData().add(data);
                }
            } else if (data >= 0x0100 && data <= 0x05c0) {
//                s = font00[o-0x0100]
                blockStringData.getData().add(data);
            } else if (data == 0x05c1) {
//                s = "Un"
                blockStringData.getData().add(data);
            } else if (data == 0x05c2) {
//                s = "defi"
                blockStringData.getData().add(data);
            } else if (data == 0x05c3) {
//                s = "ned"
                blockStringData.getData().add(data);
            } else {
//                s = "{%04x}" % o
                blockStringData.getData().add(data);
            }
        }
    }

    private static void addBlockContentsToBlock(Block block, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        int dataIndex = 0;
        while (dataIndex < numberOfShortsInThisBlock) {
            short data = dataInputStream.readShort();
            ++dataIndex;


            if(data == 0x000a || data == 0x000c || data == 0x0020) {
//                s = unichr(o)
                block.getBlockContents().add(new BlockSingleData(data));
            }
            else if(data >= 0x0040 && data <= 0x0050) {
                if (data == 0x0040) {
//                    cmd = "{FLAG %d:=%d}" % (ord(b[0]), ord(b[1]))
//                    b = b[2:]

                    block.getBlockContents().add(new BlockFlagData(data,
                            dataInputStream.readShort(), dataInputStream.readShort()));
                    ++dataIndex;
                    ++dataIndex;
                }
                else if (data == 0x0042) {
//                    cmd = "{ITEM %d}" % ord(b[0])
//                    b = b[1:]

                    block.getBlockContents().add(new BlockItemData(data, dataInputStream.readShort()));
                    ++dataIndex;
                }
                else if (data == 0x0044) {
//                    cmd = "{CLS}"
//                    s = "<p>"
                    block.getBlockContents().add(new BlockSingleData(data));
                }
                else if (data == 0x0045) {
//                    cmd = "{BR}"
//                    s = "<br>"
                    block.getBlockContents().add(new BlockSingleData(data));
                }
                else if (data == 0x0046) {
//                    cmd = "{POSE %d}" % ord(b[0])
//                    b = b[1:]

                    block.getBlockContents().add(new BlockPoseData(data, dataInputStream.readShort()));
                    ++dataIndex;
               }
                else if (data == 0x0047) {
//                    cmd = "{MANTRA %d}" % ord(b[0])
//                    b = b[1:]

                    block.getBlockContents().add(new BlockMantraData(data, dataInputStream.readShort()));
                    ++dataIndex;
                }
                else if (data == 0x004a) {
//                    colors = [ord(x) for x in b[:3]]
//                    cmd = "{COL %03d-%03d-%03d}" % tuple(colors)
//                    if sum(colors) > 0:
//                        s = "<span style='color:#%02x%02x%02x'>" % tuple(200-x for x in colors)
//                    else:
//                        s = "</span>"
//                        b = b[3:] #TODO: colors not verified

                    block.getBlockContents().add(new BlockColorsData(data, dataInputStream.readShort(),
                            dataInputStream.readShort(), dataInputStream.readShort()));
                    ++dataIndex;
                    ++dataIndex;
                    ++dataIndex;
                }
                else if (data == 0x004e) {
//                    lenopts = ord(b[0])
//                    opts = ["%d" % ord(x) for x in b[1:lenopts+1]]
//                    cmd = "{CMD %s}" % "-".join(opts)
//                    b = b[lenopts+1:]
                    short numberOfOpts = dataInputStream.readShort();
                    ++dataIndex;
                    BlockListData blockListData = new BlockListData(data, numberOfOpts);
                    for(int i = 0; i < numberOfOpts; i++) {
                        blockListData.getData().add(dataInputStream.readShort());
                        ++dataIndex;
                    }
                    block.getBlockContents().add(blockListData);
                }
                else if (data == 0x004f) {
//                    cmd = "{SCENE %d}" % ord(b[0])
//                    b = b[1:]
                    block.getBlockContents().add(new BlockSceneData(data, dataInputStream.readShort()));
                    ++dataIndex;
                }
                else {
//                    cmd = "{%02x}" % o
                    block.getBlockContents().add(new BlockSingleData(data));
                }
            }
            else if(data >= 0x0100 && data <= 0x05c0) {
//                s = font00[o-0x0100]
                block.getBlockContents().add(new BlockSingleData(data));
            }
            else if(data == 0x05c1) {
//                s = "Un"
                block.getBlockContents().add(new BlockSingleData(data));
            }
            else if(data == 0x05c2) {
//                s = "defi"
                block.getBlockContents().add(new BlockSingleData(data));
            }
            else if(data == 0x05c3) {
//                s = "ned"
                block.getBlockContents().add(new BlockSingleData(data));
            }
            else {
//                s = "{%04x}" % o
                block.getBlockContents().add(new BlockSingleData(data));
            }
        }
    }

    public static List<Block> getDatScriptInfo() throws Exception {
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(Settings.getBackupDatFile()));

        List<Block> datBlocks = new ArrayList<>();
        int numberOfBlocks = (int)dataInputStream.readShort();

        Block block;
        for(int blockIndex = 0; blockIndex < numberOfBlocks; blockIndex++) {
            int numberOfBytesInThisBlock = dataInputStream.readShort();

            if(DataFromFile.getMapOfShopNameToShopBlock().values().contains((Integer)blockIndex)) {
                block = buildShopBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == 480 || blockIndex == 482 || blockIndex == 486) {
                block = buildCheckBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == 2) {
                block = buildItemDescriptionBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == 7) {
                block = buildGrailPointBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else {
                block = new Block(blockIndex);
                addBlockContentsToBlock(block, dataInputStream, numberOfBytesInThisBlock / 2);
                if(blockIndex == 249) {
                    // Remove Mini Doll's becoming small flag from conversation.
                    Integer becomingSmallFlagIndex = null;
                    BlockContents blockContents;
                    for(int i = 0; i < block.getBlockContents().size(); i++) {
                        blockContents = block.getBlockContents().get(i);
                        if(blockContents instanceof BlockFlagData && ((BlockFlagData) blockContents).getWorldFlag() == 554) {
                            becomingSmallFlagIndex = i;
                        }
                    }
                    if(becomingSmallFlagIndex != null) {
                        block.getBlockContents().remove((int)becomingSmallFlagIndex);
                    }
                }
            }
            datBlocks.add(block);
            GameDataTracker.addBlock(block);
        }
        return datBlocks;
    }
}
