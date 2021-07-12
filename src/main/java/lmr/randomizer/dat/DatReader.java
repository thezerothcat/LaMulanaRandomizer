package lmr.randomizer.dat;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.Settings;
import lmr.randomizer.dat.blocks.*;
import lmr.randomizer.dat.blocks.contents.*;
import lmr.randomizer.dat.blocks.contents.entries.*;
import lmr.randomizer.util.BlockConstants;
import lmr.randomizer.util.BlockDataConstants;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thezerothcat on 7/26/2017.
 */
public final class DatReader {
    private DatReader() {
    }

    private static ItemNameBlock buildItemNameBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        int dataIndex = 0;
        ItemNameBlock itemNameBlock = new ItemNameBlock(blockIndex);
        TextEntry textEntry;

        for(int i = 0; i < ItemNameBlock.SoulStone; i++) {
            textEntry = new TextEntry();
            dataIndex += populateTextEntry(textEntry, dataInputStream);
            itemNameBlock.getBlockContents().add(textEntry);
        }

        // Last entry
        textEntry = new TextEntry();
        populateTextEntry(textEntry, dataInputStream, numberOfShortsInThisBlock - dataIndex);
        textEntry.setIncludeEndRecordIndicator(false);
        itemNameBlock.getBlockContents().add(textEntry);

        return itemNameBlock;
    }

    private static Block buildItemDescriptionBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        int dataIndex = 0;
        ItemDescriptionBlock itemDescriptionBlock = new ItemDescriptionBlock(blockIndex);
        TextEntry textEntry;

        for(int i = 0; i < ItemDescriptionBlock.SoftwareLamulana; i++) {
            textEntry = new TextEntry();
            dataIndex += populateTextEntry(textEntry, dataInputStream);
            itemDescriptionBlock.getBlockContents().add(textEntry);
        }

        // Last entry
        textEntry = new TextEntry();
        populateTextEntry(textEntry, dataInputStream, numberOfShortsInThisBlock - dataIndex);
        textEntry.setIncludeEndRecordIndicator(false);
        itemDescriptionBlock.getBlockContents().add(textEntry);

        return itemDescriptionBlock;
    }

    private static Block buildMenuBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        int dataIndex = 0;
        MenuBlock menuBlock = new MenuBlock(blockIndex);

        TextEntry textEntry;
        ListEntry listEntry;
        for(int i = 0; i < MenuBlock.LastEntry; i++) {
            if(i == MenuBlock.ListData_Unknown39 || i == MenuBlock.ListData_Unknown40 || i == MenuBlock.ListData_LaMulaneseBackgroundColor || i == MenuBlock.ListData_XelpudMailerTextColor
                    || i == MenuBlock.ListData_PopInMenuColor || i == MenuBlock.ListData_SelectedTextColor || i == MenuBlock.ListData_RosettaCount || i == MenuBlock.ListData_MantraMenu_TitleTextColor
                    || i == MenuBlock.ListData_SnapshotsMenu_TextColor || i == MenuBlock.ListData_TextTrax_MenuTextColor1 || i == MenuBlock.ListData_TextTrax_MenuTextColor2
                    || i == MenuBlock.ListData_EmusicTitleColor || i == MenuBlock.ListData_UnknownSelectedTextColor || i == MenuBlock.ListData_EmusicHighlightedLineColor
                    || i == MenuBlock.ListData_ShieldHp) {
                listEntry = buildListEntry(dataInputStream, true);
                dataIndex += listEntry.getSize() / 2;
                menuBlock.getBlockContents().add(listEntry);
            } else {
                // Note there are a few odd entries here that don't really fit the pattern
                textEntry = new TextEntry();
                dataIndex += populateTextEntry(textEntry, dataInputStream);
                menuBlock.getBlockContents().add(textEntry);
            }
        }

        // Last entry
        textEntry = new TextEntry();
        populateTextEntry(textEntry, dataInputStream, numberOfShortsInThisBlock - dataIndex);
        textEntry.setIncludeEndRecordIndicator(false);
        menuBlock.getBlockContents().add(textEntry);

        return menuBlock;
    }

    private static Block buildSoftwareBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        int dataIndex = 0;
        SoftwareBlock softwareBlock = new SoftwareBlock(blockIndex);

        for(int softwareIndex = 0; softwareIndex < SoftwareBlock.SoftwareLamulana; softwareIndex++) {
            SoftwareListEntry softwareListEntry = buildSoftwareListEntry(dataInputStream);
            dataIndex += softwareListEntry.getSize() / 2;
            softwareBlock.getBlockContents().add(softwareListEntry);

            TextEntry softwareNameEntry = new TextEntry();
            dataIndex += populateTextEntry(softwareNameEntry, dataInputStream);
            softwareBlock.getBlockContents().add(softwareNameEntry);

            TextEntry softwareCostTextEntry = new TextEntry();
            dataIndex += populateTextEntry(softwareCostTextEntry, dataInputStream);
            softwareBlock.getBlockContents().add(softwareCostTextEntry);
        }

        // Last entry
        SoftwareListEntry softwareListEntry = buildSoftwareListEntry(dataInputStream);
        dataIndex += softwareListEntry.getSize() / 2;
        softwareBlock.getBlockContents().add(softwareListEntry);

        TextEntry softwareNameEntry = new TextEntry();
        dataIndex += populateTextEntry(softwareNameEntry, dataInputStream);
        softwareBlock.getBlockContents().add(softwareNameEntry);

        TextEntry softwareCostTextEntry = new TextEntry();
        populateTextEntry(softwareCostTextEntry, dataInputStream, numberOfShortsInThisBlock - dataIndex);
        softwareBlock.getBlockContents().add(softwareCostTextEntry);

        return softwareBlock;
    }

    private static Block buildGrailPointBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        int dataIndex = 0;
        GrailPointBlock grailPointsBlock = new GrailPointBlock(blockIndex);

        DefaultGrailPointEntry defaultGrailPointEntry = buildDefaultGrailPointEntry(dataInputStream);
        dataIndex += defaultGrailPointEntry.getSize() / 2;
        grailPointsBlock.getBlockContents().add(defaultGrailPointEntry);

        while(dataIndex < numberOfShortsInThisBlock) {
            GrailPointEntry grailPointEntry = buildGrailPointEntry(dataInputStream);
            dataIndex += grailPointEntry.getSize() / 2;
            grailPointsBlock.getBlockContents().add(grailPointEntry);
        }
        return grailPointsBlock;
    }

    private static Block buildMapNamesLimitedBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        int dataIndex = 0;
        MapNamesLimitedBlock mapNamesBlock = new MapNamesLimitedBlock(blockIndex);

        // Music entry
        MapMusicLimitedEntry mapMusicLimitedEntry = buildMapMusicLimitedEntry(dataInputStream);
        dataIndex += mapMusicLimitedEntry.getSize() / 2;
        mapNamesBlock.getBlockContents().add(mapMusicLimitedEntry);

        // Japanese field name
        TextEntry textEntry = new TextEntry();
        dataIndex += populateTextEntry(textEntry, dataInputStream);
        mapNamesBlock.getBlockContents().add(textEntry);

        // Field name
        textEntry = new TextEntry();
        dataIndex += populateTextEntry(textEntry, dataInputStream);
        mapNamesBlock.getBlockContents().add(textEntry);

        // Screen names
        while(dataIndex < numberOfShortsInThisBlock) {
            textEntry = new TextEntry();
            dataIndex += populateTextEntry(textEntry, dataInputStream, numberOfShortsInThisBlock - dataIndex);
            mapNamesBlock.getBlockContents().add(textEntry);
        }
        return mapNamesBlock;
    }

    private static ScannableBlock buildScannableBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        int dataIndex = 0;
        ScannableBlock scannableBlock = new ScannableBlock(blockIndex);

        // Scannable text
        TextEntry textEntry = new TextEntry();
        dataIndex += populateTextEntry(textEntry, dataInputStream);
        scannableBlock.getBlockContents().add(textEntry);

        ListEntry listEntry = buildListEntry(dataInputStream, false);
        dataIndex += listEntry.getSize() / 2;
        scannableBlock.getBlockContents().add(listEntry);
        return scannableBlock;
    }

    private static ShopBlock buildShopBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        int dataIndex = 0;
        ShopBlock shopBlock = new ShopBlock(blockIndex);

        List<BlockListData> shopBlockData = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
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

        for(int i = 0; i < 18; i++) {
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

    private static MasterNpcBlock buildMasterNpcBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        int dataIndex = 0;
        MasterNpcBlock masterNpcBlock = new MasterNpcBlock(blockIndex);

        short cmdShort = dataInputStream.readShort();
        ++dataIndex;
        short length1Short = dataInputStream.readShort();
        ++dataIndex;
        BlockCmdSingle textCard = new BlockCmdSingle(dataInputStream.readShort());
        ++dataIndex;
        dataInputStream.readShort(); // 0x000a
        ++dataIndex;
        masterNpcBlock.setTextCard(textCard);

        cmdShort = dataInputStream.readShort();
        ++dataIndex;
        length1Short = dataInputStream.readShort();
        ++dataIndex;
        BlockCmdSingle background = new BlockCmdSingle(dataInputStream.readShort());
        ++dataIndex;
        dataInputStream.readShort(); // 0x000a
        ++dataIndex;
        masterNpcBlock.setBackground(background);

        cmdShort = dataInputStream.readShort();
        ++dataIndex;
        length1Short = dataInputStream.readShort();
        ++dataIndex;
        BlockCmdSingle npcCard = new BlockCmdSingle(dataInputStream.readShort());
        ++dataIndex;
        dataInputStream.readShort(); // 0x000a
        ++dataIndex;
        masterNpcBlock.setNpcCard(npcCard);

        cmdShort = dataInputStream.readShort();
        ++dataIndex;
        length1Short = dataInputStream.readShort();
        ++dataIndex;
        BlockCmdSingle music = new BlockCmdSingle(dataInputStream.readShort());
        ++dataIndex;
        dataInputStream.readShort(); // 0x000a
        ++dataIndex;
        masterNpcBlock.setMusic(music);

        BlockStringData blockStringData = new BlockStringData();
        while(dataIndex < numberOfShortsInThisBlock) {
            blockStringData.getData().add(dataInputStream.readShort());
            ++dataIndex;
        }
        masterNpcBlock.setNpcName(blockStringData);
        return masterNpcBlock;
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

    private static int populateTextEntry(TextEntry textEntry, DataInputStream dataInputStream) throws IOException{
        int dataIndex = 0;

        while (true) {
            short data = dataInputStream.readShort();
            ++dataIndex;

            if (data == 0x000a) {
//                s = unichr(o)
                return dataIndex;
            }
            else if(data == 0x000c || data == 0x0020) {
                textEntry.getData().add(data);
            }
            else if (data >= 0x0040 && data <= 0x0050) {
                if (data == 0x0040) {
//                    cmd = "{FLAG %d:=%d}" % (ord(b[0]), ord(b[1]))
//                    b = b[2:]

                    textEntry.getData().add(data);
                    textEntry.getData().add(dataInputStream.readShort());
                    textEntry.getData().add(dataInputStream.readShort());
                    ++dataIndex;
                    ++dataIndex;
                } else if (data == 0x0042) {
//                    cmd = "{ITEM %d}" % ord(b[0])
//                    b = b[1:]

                    textEntry.getData().add(data);
                    textEntry.getData().add(dataInputStream.readShort());
                    ++dataIndex;
                } else if (data == 0x0044) {
//                    cmd = "{CLS}"
//                    s = "<p>"
                    textEntry.getData().add(data);
                } else if (data == 0x0045) {
//                    cmd = "{BR}"
//                    s = "<br>"
                    textEntry.getData().add(data);
                } else if (data == 0x0046) {
//                    cmd = "{POSE %d}" % ord(b[0])
//                    b = b[1:]

                    textEntry.getData().add(data);
                    textEntry.getData().add(dataInputStream.readShort());
                    ++dataIndex;
                } else if (data == 0x0047) {
//                    cmd = "{MANTRA %d}" % ord(b[0])
//                    b = b[1:]

                    textEntry.getData().add(data);
                    textEntry.getData().add(dataInputStream.readShort());
                    ++dataIndex;
                } else if (data == 0x004a) {
//                    colors = [ord(x) for x in b[:3]]
//                    cmd = "{COL %03d-%03d-%03d}" % tuple(colors)
//                    if sum(colors) > 0:
//                        s = "<span style='color:#%02x%02x%02x'>" % tuple(200-x for x in colors)
//                    else:
//                        s = "</span>"
//                        b = b[3:] #TODO: colors not verified

                    textEntry.getData().add(data);
                    textEntry.getData().add(dataInputStream.readShort());
                    textEntry.getData().add(dataInputStream.readShort());
                    textEntry.getData().add(dataInputStream.readShort());
                    ++dataIndex;
                    ++dataIndex;
                    ++dataIndex;
                } else if (data == 0x004e) {
//                    lenopts = ord(b[0])
//                    opts = ["%d" % ord(x) for x in b[1:lenopts+1]]
//                    cmd = "{CMD %s}" % "-".join(opts)
//                    b = b[lenopts+1:]
                    textEntry.getData().add(data);
                    short numberOfOpts = dataInputStream.readShort();
                    textEntry.getData().add(numberOfOpts);
                    ++dataIndex;
                    for (int i = 0; i < numberOfOpts; i++) {
                        textEntry.getData().add(dataInputStream.readShort());
                        ++dataIndex;
                    }
                } else if (data == 0x004f) {
//                    cmd = "{SCENE %d}" % ord(b[0])
//                    b = b[1:]
                    textEntry.getData().add(data);
                    textEntry.getData().add(dataInputStream.readShort());
                    ++dataIndex;
                } else {
//                    cmd = "{%02x}" % o
                    textEntry.getData().add(data);
                }
            } else if (data >= 0x0100 && data <= 0x05c0) {
//                s = font00[o-0x0100]
                textEntry.getData().add(data);
            } else if (data == 0x05c1) {
//                s = "Un"
                textEntry.getData().add(data);
            } else if (data == 0x05c2) {
//                s = "defi"
                textEntry.getData().add(data);
            } else if (data == 0x05c3) {
//                s = "ned"
                textEntry.getData().add(data);
            } else {
//                s = "{%04x}" % o
                textEntry.getData().add(data);
            }
        }
    }

    private static int populateTextEntry(TextEntry textEntry, DataInputStream dataInputStream, int shortsRemainingInThisBlock) throws IOException{
        // todo: integrate this with the other populateTextEntry - only difference is this one has a check for length, to be used if the block will end before an end-of-entry indicator is found
        int dataIndex = 0;

        while (dataIndex < shortsRemainingInThisBlock) {
            short data = dataInputStream.readShort();
            ++dataIndex;

            if (data == 0x000a) {
//                s = unichr(o)
                return dataIndex;
            }
            else if(data == 0x000c || data == 0x0020) {
                textEntry.getData().add(data);
            }
            else if (data >= 0x0040 && data <= 0x0050) {
                if (data == 0x0040) {
//                    cmd = "{FLAG %d:=%d}" % (ord(b[0]), ord(b[1]))
//                    b = b[2:]

                    textEntry.getData().add(data);
                    textEntry.getData().add(dataInputStream.readShort());
                    textEntry.getData().add(dataInputStream.readShort());
                    ++dataIndex;
                    ++dataIndex;
                } else if (data == 0x0042) {
//                    cmd = "{ITEM %d}" % ord(b[0])
//                    b = b[1:]

                    textEntry.getData().add(data);
                    textEntry.getData().add(dataInputStream.readShort());
                    ++dataIndex;
                } else if (data == 0x0044) {
//                    cmd = "{CLS}"
//                    s = "<p>"
                    textEntry.getData().add(data);
                } else if (data == 0x0045) {
//                    cmd = "{BR}"
//                    s = "<br>"
                    textEntry.getData().add(data);
                } else if (data == 0x0046) {
//                    cmd = "{POSE %d}" % ord(b[0])
//                    b = b[1:]

                    textEntry.getData().add(data);
                    textEntry.getData().add(dataInputStream.readShort());
                    ++dataIndex;
                } else if (data == 0x0047) {
//                    cmd = "{MANTRA %d}" % ord(b[0])
//                    b = b[1:]

                    textEntry.getData().add(data);
                    textEntry.getData().add(dataInputStream.readShort());
                    ++dataIndex;
                } else if (data == 0x004a) {
//                    colors = [ord(x) for x in b[:3]]
//                    cmd = "{COL %03d-%03d-%03d}" % tuple(colors)
//                    if sum(colors) > 0:
//                        s = "<span style='color:#%02x%02x%02x'>" % tuple(200-x for x in colors)
//                    else:
//                        s = "</span>"
//                        b = b[3:] #TODO: colors not verified

                    textEntry.getData().add(data);
                    textEntry.getData().add(dataInputStream.readShort());
                    textEntry.getData().add(dataInputStream.readShort());
                    textEntry.getData().add(dataInputStream.readShort());
                    ++dataIndex;
                    ++dataIndex;
                    ++dataIndex;
                } else if (data == 0x004e) {
//                    lenopts = ord(b[0])
//                    opts = ["%d" % ord(x) for x in b[1:lenopts+1]]
//                    cmd = "{CMD %s}" % "-".join(opts)
//                    b = b[lenopts+1:]
                    textEntry.getData().add(data);
                    short numberOfOpts = dataInputStream.readShort();
                    textEntry.getData().add(numberOfOpts);
                    ++dataIndex;
                    for (int i = 0; i < numberOfOpts; i++) {
                        textEntry.getData().add(dataInputStream.readShort());
                        ++dataIndex;
                    }
                } else if (data == 0x004f) {
//                    cmd = "{SCENE %d}" % ord(b[0])
//                    b = b[1:]
                    textEntry.getData().add(data);
                    textEntry.getData().add(dataInputStream.readShort());
                    ++dataIndex;
                } else {
//                    cmd = "{%02x}" % o
                    textEntry.getData().add(data);
                }
            } else if (data >= 0x0100 && data <= 0x05c0) {
//                s = font00[o-0x0100]
                textEntry.getData().add(data);
            } else if (data == 0x05c1) {
//                s = "Un"
                textEntry.getData().add(data);
            } else if (data == 0x05c2) {
//                s = "defi"
                textEntry.getData().add(data);
            } else if (data == 0x05c3) {
//                s = "ned"
                textEntry.getData().add(data);
            } else {
//                s = "{%04x}" % o
                textEntry.getData().add(data);
            }
        }
        return dataIndex;
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
                if (data == BlockDataConstants.Flag) {
//                    cmd = "{FLAG %d:=%d}" % (ord(b[0]), ord(b[1]))
//                    b = b[2:]

                    block.getBlockContents().add(new BlockFlagData(dataInputStream.readShort(), dataInputStream.readShort()));
                    ++dataIndex;
                    ++dataIndex;
                }
                else if (data == BlockDataConstants.Item) {
//                    cmd = "{ITEM %d}" % ord(b[0])
//                    b = b[1:]

                    block.getBlockContents().add(new BlockItemData(dataInputStream.readShort()));
                    ++dataIndex;
                }
                else if (data == BlockDataConstants.Cls) {
//                    cmd = "{CLS}"
//                    s = "<p>"
                    block.getBlockContents().add(new BlockSingleData(data));
                }
                else if (data == BlockDataConstants.Newline) {
//                    cmd = "{BR}"
//                    s = "<br>"
                    block.getBlockContents().add(new BlockSingleData(data));
                }
                else if (data == BlockDataConstants.Pose) {
//                    cmd = "{POSE %d}" % ord(b[0])
//                    b = b[1:]

                    block.getBlockContents().add(new BlockPoseData(data, dataInputStream.readShort()));
                    ++dataIndex;
                }
                else if (data == BlockDataConstants.Mantra) {
//                    cmd = "{MANTRA %d}" % ord(b[0])
//                    b = b[1:]

                    block.getBlockContents().add(new BlockMantraData(data, dataInputStream.readShort()));
                    ++dataIndex;
                }
                else if (data == BlockDataConstants.ColorChange) {
//                    colors = [ord(x) for x in b[:3]]
//                    cmd = "{COL %03d-%03d-%03d}" % tuple(colors)
//                    if sum(colors) > 0:
//                        s = "<span style='color:#%02x%02x%02x'>" % tuple(200-x for x in colors)
//                    else:
//                        s = "</span>"
//                        b = b[3:] #TODO: colors not verified

                    block.getBlockContents().add(new BlockColorsData(dataInputStream.readShort(),
                            dataInputStream.readShort(), dataInputStream.readShort()));
                    ++dataIndex;
                    ++dataIndex;
                    ++dataIndex;
                }
                else if (data == BlockDataConstants.DataList) {
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
                else if (data == BlockDataConstants.Anime) {
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

    private static ListEntry buildListEntry(DataInputStream dataInputStream, boolean includeEndOfListEntry) throws IOException {
        dataInputStream.readShort(); // 0x004e
        short listSize = dataInputStream.readShort();
        ListEntry listEntry = new ListEntry(listSize);
        for(int i = 0; i < listSize; i++) {
            listEntry.getData().set(i, dataInputStream.readShort());
        }
        if(includeEndOfListEntry) {
            dataInputStream.readShort(); // 0x000a
        }
        else {
            listEntry.setIncludeEndRecordIndicator(false);
        }
        return listEntry;
    }

    private static MapMusicLimitedEntry buildMapMusicLimitedEntry(DataInputStream dataInputStream) throws IOException {
        dataInputStream.readShort(); // 0x004e
        short listSize = dataInputStream.readShort();
        MapMusicLimitedEntry listEntry = new MapMusicLimitedEntry();
        for(int i = 0; i < listSize; i++) {
            listEntry.getData().set(i, dataInputStream.readShort());
        }
        dataInputStream.readShort(); // 0x000a
        return listEntry;
    }

    private static SoftwareListEntry buildSoftwareListEntry(DataInputStream dataInputStream) throws IOException {
        dataInputStream.readShort(); // 0x004e
        short listSize = dataInputStream.readShort(); // list length, should be 2
        SoftwareListEntry softwareListEntry = new SoftwareListEntry();
        for(int i = 0; i < listSize; i++) {
            softwareListEntry.getData().set(i, dataInputStream.readShort());
        }
        dataInputStream.readShort(); // 0x000a
        return softwareListEntry;
    }

    private static DefaultGrailPointEntry buildDefaultGrailPointEntry(DataInputStream dataInputStream) throws IOException {
        dataInputStream.readShort(); // 0x004e
        short listSize = dataInputStream.readShort(); // list length, should be 6
        DefaultGrailPointEntry defaultGrailPointEntry = new DefaultGrailPointEntry();
        for(int i = 0; i < listSize; i++) {
            defaultGrailPointEntry.getData().set(i, dataInputStream.readShort());
        }
        dataInputStream.readShort(); // 0x000a
        return defaultGrailPointEntry;
    }

    private static GrailPointEntry buildGrailPointEntry(DataInputStream dataInputStream) throws IOException {
        dataInputStream.readShort(); // 0x004e
        short listSize = dataInputStream.readShort(); // list length, should be 8
        GrailPointEntry grailPointEntry = new GrailPointEntry();
        for(int i = 0; i < listSize; i++) {
            grailPointEntry.getData().set(i, dataInputStream.readShort());
        }
        dataInputStream.readShort(); // 0x000a
        return grailPointEntry;
    }

    private static boolean isMasterNpcBlock(int blockIndex) {
        // Others exist, but these are the ones we care about for rando right now
        return blockIndex == BlockConstants.Master_Hiner
                || blockIndex == BlockConstants.Master_Moger
                || blockIndex == BlockConstants.Master_FormerMekuriMaster_Mekuri
                || blockIndex == BlockConstants.Master_PriestZarnac
                || blockIndex == BlockConstants.Master_PriestXanado
                || blockIndex == BlockConstants.Master_PhilosopherGiltoriyo
                || blockIndex == BlockConstants.Master_PriestHidlyda
                || blockIndex == BlockConstants.Master_PriestRomancis
                || blockIndex == BlockConstants.Master_PriestAramo
                || blockIndex == BlockConstants.Master_PriestTriton
                || blockIndex == BlockConstants.Master_PriestJaguarfiv
                || blockIndex == BlockConstants.Master_MrSlushfund_Pepper
                || blockIndex == BlockConstants.Master_PriestAlest
                || blockIndex == BlockConstants.Master_StrayFairy
                || blockIndex == BlockConstants.Master_GiantThexde
                || blockIndex == BlockConstants.Master_PhilosopherAlsedana
                || blockIndex == BlockConstants.Master_PhilosopherSamaranta
                || blockIndex == BlockConstants.Master_PriestLaydoc
                || blockIndex == BlockConstants.Master_PriestAshgine
                || blockIndex == BlockConstants.Master_PhilosopherFobos_Ladder
                || blockIndex == BlockConstants.Master_8BitElder
                || blockIndex == BlockConstants.Master_duplex
                || blockIndex == BlockConstants.Master_Samieru
                || blockIndex == BlockConstants.Master_Naramura
                || blockIndex == BlockConstants.Master_8BitFairy
                || blockIndex == BlockConstants.Master_PriestMadomono
                || blockIndex == BlockConstants.Master_PriestGailious
                || blockIndex == BlockConstants.Master_Dracuet_WaitForNightfall
                || blockIndex == BlockConstants.MulbrukEscapeRegular
                || blockIndex == BlockConstants.MulbrukEscapeSwimsuit
                || blockIndex == BlockConstants.Master_Dracuet_BackInTime
                || blockIndex == BlockConstants.Master_Dracuet_HugeCasket
                || blockIndex == BlockConstants.Master_Fairy_NightSurface
                || blockIndex == BlockConstants.Master_Dracuet_HTUnlocked;
    }

    private static DataInputStream getDatFile(boolean loadFromBackup) throws Exception {
        if(loadFromBackup) {
            return new DataInputStream(new FileInputStream(Settings.getBackupDatFile()));
        }
        return new DataInputStream(new FileInputStream(String.format("%s/data/language/%s/script_code.dat",
                Settings.getLaMulanaBaseDir(), Settings.getLanguage())));

    }
    public static List<Block> getDatScriptInfo(boolean loadFromBackup) throws Exception {
        DataInputStream dataInputStream = getDatFile(loadFromBackup);

        List<Block> datBlocks = new ArrayList<>();
        int numberOfBlocks = (int)dataInputStream.readShort();

        Block block;
        for(int blockIndex = 0; blockIndex < numberOfBlocks; blockIndex++) {
            int numberOfBytesInThisBlock = dataInputStream.readShort();

            if(blockIndex == BlockConstants.ItemNameBlock) {
                block = buildItemNameBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == BlockConstants.ItemDescriptionBlock) {
                block = buildItemDescriptionBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == BlockConstants.MenuBlock) {
                block = buildMenuBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == BlockConstants.SoftwareBlock) {
                block = buildSoftwareBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == BlockConstants.GrailPointBlock) {
                block = buildGrailPointBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == BlockConstants.HTMapNamesLimitedBlock) {
                block = buildMapNamesLimitedBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == BlockConstants.FootOfFuto) {
                block = buildScannableBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == 273) {
                block = buildShopBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == BlockConstants.XelpudFlagCheckBlock || blockIndex == BlockConstants.XelpudScoreCheckBlock
                    || blockIndex == BlockConstants.MulbrukFlagCheckBlock || blockIndex == BlockConstants.MulbrukScoreCheckBlock) {
                block = buildCheckBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(isMasterNpcBlock(blockIndex)) {
                block = buildMasterNpcBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(DataFromFile.getMapOfShopNameToShopBlock().values().contains((Integer)blockIndex)) {
                block = buildShopBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else {
                block = new Block(blockIndex);
                addBlockContentsToBlock(block, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            datBlocks.add(block);
        }
        return datBlocks;
    }
}
