package lmr.randomizer.dat;

import lmr.randomizer.DataFromFile;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import lmr.randomizer.dat.conversation.CheckBlock;
import lmr.randomizer.dat.shop.BlockCmdSingle;
import lmr.randomizer.dat.shop.BlockStringData;
import lmr.randomizer.dat.shop.MasterNpcBlock;
import lmr.randomizer.dat.shop.ShopBlock;
import lmr.randomizer.update.GameDataTracker;
import lmr.randomizer.update.LocationCoordinateMapper;

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

    private static Block buildSoftwareMenuBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        Block block = new Block(blockIndex);

        for(int softwareIndex = 0; softwareIndex < 20; softwareIndex++) {
            BlockListData softwareCostBehavior = new BlockListData(dataInputStream.readShort(), dataInputStream.readShort());
            if(softwareIndex == 18) {
                softwareCostBehavior.getData().add((short)0x0c8);
                dataInputStream.readShort(); // Waste the original value
            }
            else {
                softwareCostBehavior.getData().add(dataInputStream.readShort());
            }
            softwareCostBehavior.getData().add(dataInputStream.readShort());
            dataInputStream.readShort(); // 0x000a

            block.getBlockContents().add(softwareCostBehavior);
            block.getBlockContents().add(new BlockSingleData((short)0x000a));

            BlockStringData softwareName = new BlockStringData();
            populateBlockStringData(softwareName, dataInputStream);
            if(Settings.isFools2020Mode()) {
                if(softwareIndex == 2) {
                    softwareName.getData().clear();
                    softwareName.getData().addAll(FileUtils.stringToData(Translations.getText("items.yagostr.exe")));
                }
                else if(softwareIndex == 3) {
                    softwareName.getData().clear();
                    softwareName.getData().addAll(FileUtils.stringToData(Translations.getText("items.yagomap.exe")));
                }
                else if(softwareIndex == 6) {
                    softwareName.getData().clear();
                    softwareName.getData().addAll(FileUtils.stringToData(Translations.getText("items.miracle.exe")));
                }
                else if(softwareIndex == 7) {
                    softwareName.getData().clear();
                    softwareName.getData().addAll(FileUtils.stringToData(Translations.getText("items.lamulana.exe")));
                }
                else if(softwareIndex == 17) {
                    softwareName.getData().clear();
                    softwareName.getData().addAll(FileUtils.stringToData(Translations.getText("items.torude.exe")));
                }
                else if(softwareIndex == 19) {
                    softwareName.getData().clear();
                    softwareName.getData().addAll(FileUtils.stringToData(Translations.getText("items.guild.exe")));
                }
            }

            block.getBlockContents().add(softwareName);
            block.getBlockContents().add(new BlockSingleData((short)0x000a));

            BlockStringData softwareCostDisplay = new BlockStringData();
            populateBlockStringData(softwareCostDisplay, dataInputStream);
            if(Settings.isFools2020Mode()) {
                if(softwareIndex == 18) {
                    softwareCostDisplay.getData().clear();
                    softwareCostDisplay.getData().addAll(FileUtils.stringToData(Translations.getText("software.cost.mirai")));
                }
            }

            block.getBlockContents().add(softwareCostDisplay);
            block.getBlockContents().add(new BlockSingleData((short)0x000a));
        }
        return block;
    }

//        addBlockContentsToBlock(block, dataInputStream, numberOfShortsInThisBlock);
//
//        if(blockIndex == 5) {
//            block = new Block(blockIndex); // Build a new one
//
//            // Software costs
//            int softwareIndex = 0;
//            BlockContents blockContents;
//            List<BlockContents> newBlockContents = new ArrayList<>();
//            for(int i = 0; i < block.getBlockContents().size(); i++) {
//                blockContents = block.getBlockContents().get(i);
//                if(blockContents instanceof BlockListData) {
//                    ++softwareIndex;
//                    if(softwareIndex == 19) {
//                        // mirai
//                        ((BlockListData)blockContents).getData().set(0, (short)0x0c8); // Set cost to 200
//                        block.getBlockContents().set(i + 16, new BlockSingleData(FileUtils.stringToData("2").get(0))); // Set display to 200
//                        break;
//                    }
//                    if(Settings.isFools2020Mode()) {
//                        block
//                    }
//                }
//                else {
//                    if(!Settings.isFools2020Mode() || (softwareIndex != 6 && softwareIndex != 18) {
//                        if()
//                    }
//                }
//            }
//        }

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

    private static Block buildItemNameBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        int dataIndex = 0;
        Block itemNameBlock = new Block(blockIndex);
        BlockStringData blockStringData;

        // Add everything before Lamp of Time
        for(int i = 0; i < 25; i ++) {
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }

        if(Settings.isFools2020Mode()) {
            // Get the data for the item, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("items.LampofTime")));
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }
        else {
            // Get normal item name
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }

        for(int i = 0; i < 11; i ++) {
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }

        if(Settings.isFools2020Mode()) {
            // Get the data for the item, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("items.Scriptures")));
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }
        else {
            // Get normal item name
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }

        // Add everything before Scriptures
        for(int i = 0; i < 18; i ++) {
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }

        if(Settings.isFools2020Mode()) {
            // Get the data for the item, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("items.HeatproofCase")));
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }
        else {
            // Get normal item name
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }

        // Add everything before Lamp of Time
        for(int i = 0; i < 23; i ++) {
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }

        if(Settings.isFools2020Mode()) {
            // Get the data for the item, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("items.LampofTime")));
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }
        else {
            // Get normal item name
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }

        // Add everything before Secret Treasure of Life
        for(int i = 0; i < 3; i ++) {
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }

        if(Settings.isHalloweenMode()) {
            // Get the data for Secret Treasure of Life, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("event.halloween.candy.name")));
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }
        else if(Settings.isFools2020Mode()) {
            // Get the data for Secret Treasure of Life, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("items.Feather")));
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }
        else {
            // Get Secret Treasure of Life item name
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }

        // Add everything before yagomap/yagostr
        for(int i = 0; i < 2; i ++) {
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }

        if(Settings.isFools2020Mode()) {
            // Get the data for the item, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("items.YagooStr")));
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);

            // Get the data for the item, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("items.YagooMap")));
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }
        else {
            // Get normal item name
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);

            // Get normal item name
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }

        // Add everything before Snapshots / torude.exe
        for(int i = 0; i < 2; i ++) {
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }

        if(Settings.isFools2020Mode()) {
            // Get the data for the item, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("items.MiracleWitch")));
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }
        else {
            // Get normal item name
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }

        // Guild
        if(Settings.isFools2020Mode()) {
            // Get the data for the item, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("items.LaMulana")));
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }
        else {
            // Get normal item name
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }

        // Add everything before Miracle Witch / miracle.exe
        for(int i = 0; i < 9; i ++) {
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }

        if(Settings.isFools2020Mode()) {
            // Get the data for the item, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("items.Snapshots")));
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }
        else {
            // Get normal item name
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }

        // Mirai
        blockStringData = new BlockStringData();
        dataIndex += populateBlockStringData(blockStringData, dataInputStream);
        blockStringData.getData().add((short)0x000a);
        itemNameBlock.getBlockContents().add(blockStringData);

        // La-Mulana software
        if(Settings.isFools2020Mode()) {
            // Get the data for the item, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("items.Guild")));
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }
        else {
            // Get normal item name
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemNameBlock.getBlockContents().add(blockStringData);
        }

        while(dataIndex < numberOfShortsInThisBlock) {
            short blockData = dataInputStream.readShort();
            ++dataIndex;
            itemNameBlock.getBlockContents().add(new BlockSingleData(blockData));
        }
        return itemNameBlock;
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
        blockStringData.getData().addAll(FileUtils.stringToData("Mobile　Super　X：" + versionString));
        blockStringData.getData().add((short)0x0045);
        blockStringData.getData().addAll(FileUtils.stringToData(settingsString));
        blockStringData.getData().add((short)0x000a);
        itemDescriptionBlock.getBlockContents().add(blockStringData);

        // Add Waterproof Case
        blockStringData = new BlockStringData();
        dataIndex += populateBlockStringData(blockStringData, dataInputStream);
        blockStringData.getData().add((short)0x000a);
        itemDescriptionBlock.getBlockContents().add(blockStringData);

        if(Settings.isFools2020Mode()) {
            // Get the data for the item, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("event.fools2020.heatproof.description")));
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }
        else {
            // Get normal item description
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }

        // Add everything before Scriptures
        for(int i = 0; i < 18; i ++) {
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }

        if(Settings.isFools2020Mode()) {
            // Get the data for the item, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("event.fools2020.scriptures.description")));
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }
        else {
            // Get normal item description
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }

        for(int i = 0; i < 19; i ++) {
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }

        // Get the data for Mobile Super X2, but throw it away in favor of custom replacement
        dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
        blockStringData = new BlockStringData();
        blockStringData.getData().addAll(FileUtils.stringToData("Mobile　Super　X2：" + versionString));
        blockStringData.getData().add((short)0x0045);
        blockStringData.getData().addAll(FileUtils.stringToData(settingsString));
        blockStringData.getData().add((short)0x000a);
        itemDescriptionBlock.getBlockContents().add(blockStringData);

        // Add everything before Lamp of Time
        for(int i = 0; i < 3; i ++) {
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }

        if(Settings.isFools2020Mode()) {
            // Get the data for the item, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("event.fools2020.lamp.description")));
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }
        else {
            // Get normal item description
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }

        // Add everything before Secret Treasure of Life
        for(int i = 0; i < 3; i ++) {
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }

        if(Settings.isHalloweenMode()) {
            // Get the data for Secret Treasure of Life, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("event.halloween.candy.description")));
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }
        else if(Settings.isFools2020Mode()) {
            // Get the data for Secret Treasure of Life, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("event.fools2020.stol.description")));
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }
        else {
            // Get Secret Treasure of Life item description
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }

        // Add everything before yagomap / yagostr
        for(int i = 0; i < 2; i ++) {
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }

        if(Settings.isFools2020Mode()) {
            // Get the data for the item, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("event.fools2020.yagomap.description")));
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);

            // Get the data for the item, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("event.fools2020.yagostr.description")));
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }
        else {
            // Get normal item description
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);

            // Get normal item description
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }

        // Add everything before Snapshots / torude.exe
        for(int i = 0; i < 2; i ++) {
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }

        if(Settings.isFools2020Mode()) {
            // Get the data for the item, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("event.fools2020.torude.description")));
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }
        else {
            // Get normal item description
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }

        // Guild
        if(Settings.isFools2020Mode()) {
            // Get the data for the item, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("event.fools2020.guild.description")));
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }
        else {
            // Get normal item description
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }

        // Add everything before Miracle Witch / miracle.exe
        for(int i = 0; i < 9; i ++) {
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }

        if(Settings.isFools2020Mode()) {
            // Get the data for the item, but throw it away in favor of custom replacement
            dataIndex += populateBlockStringData(new BlockStringData(), dataInputStream);
            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("event.fools2020.miracle.description")));
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }
        else {
            // Get normal item description
            blockStringData = new BlockStringData();
            dataIndex += populateBlockStringData(blockStringData, dataInputStream);
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }

        // Mirai
        blockStringData = new BlockStringData();
        dataIndex += populateBlockStringData(blockStringData, dataInputStream);
        blockStringData.getData().add((short)0x000a);
        itemDescriptionBlock.getBlockContents().add(blockStringData);

        // La-Mulana software
        if(Settings.isFools2020Mode()) {
            // Get the data for the item, but throw it away in favor of custom replacement
            while(dataIndex < numberOfShortsInThisBlock) {
                dataInputStream.readShort();
                ++dataIndex;
            }

            blockStringData = new BlockStringData();
            blockStringData.getData().addAll(FileUtils.stringToData(Translations.getText("event.fools2020.lamulana.description")));
            blockStringData.getData().add((short)0x000a);
            itemDescriptionBlock.getBlockContents().add(blockStringData);
        }
        else {
            while(dataIndex < numberOfShortsInThisBlock) {
                short blockData = dataInputStream.readShort();
                ++dataIndex;
                itemDescriptionBlock.getBlockContents().add(new BlockSingleData(blockData));
            }
        }

        return itemDescriptionBlock;
    }

    private static Block buildGrailPointBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        Block grailPointsBlock = new Block(blockIndex);
        addBlockContentsToBlock(grailPointsBlock, dataInputStream, numberOfShortsInThisBlock);
        if(!LocationCoordinateMapper.isSurfaceStart() || Settings.getCurrentStartingLocation() == 22) {
            grailPointsBlock.getBlockContents().clear();

            boolean front = LocationCoordinateMapper.isFrontsideStart();

            // Build a new warp for actual starting area
            BlockListData blockListData = new BlockListData((short)0x004e, (short)6);
            blockListData.getData().add(LocationCoordinateMapper.getImageIndex(LocationCoordinateMapper.getStartingZone(), front));
            blockListData.getData().add((short)LocationCoordinateMapper.getStartingZone());
            blockListData.getData().add((short)LocationCoordinateMapper.getStartingRoom());
            blockListData.getData().add((short)LocationCoordinateMapper.getStartingScreen());
            blockListData.getData().add((short)(LocationCoordinateMapper.getStartingX() % 640));
            blockListData.getData().add((short)(LocationCoordinateMapper.getStartingY() % 480));
            grailPointsBlock.getBlockContents().add(blockListData);
            grailPointsBlock.getBlockContents().add(new BlockSingleData((short)0x000a));

            List<Short> warpsAdded = new ArrayList<>();
            warpsAdded.add((short)LocationCoordinateMapper.getStartingZone());
            short nextWarp = LocationCoordinateMapper.getNextWarpZone(LocationCoordinateMapper.getStartingZone(), front);
            while(!warpsAdded.contains(nextWarp)) {
                blockListData = new BlockListData((short)0x004e, (short)8);
                blockListData.getData().add((short)0); // No mirai needed
                blockListData.getData().add(LocationCoordinateMapper.getGrailFlag(nextWarp, front));
                blockListData.getData().add(LocationCoordinateMapper.getImageIndex(nextWarp, front));
                blockListData.getData().add(nextWarp);
                blockListData.getData().add((short)LocationCoordinateMapper.getStartingRoom(nextWarp, front));
                blockListData.getData().add((short)LocationCoordinateMapper.getStartingScreen(nextWarp));
                blockListData.getData().add((short)(LocationCoordinateMapper.getStartingX(nextWarp, front) % 640));
                blockListData.getData().add((short)(LocationCoordinateMapper.getStartingY(nextWarp, front) % 480));
                grailPointsBlock.getBlockContents().add(blockListData);
                grailPointsBlock.getBlockContents().add(new BlockSingleData((short)0x000a));

                warpsAdded.add(nextWarp);
                nextWarp = LocationCoordinateMapper.getNextWarpZone(nextWarp, front);
            }
            front = !front;
            nextWarp = LocationCoordinateMapper.getNextWarpZone(-1, front);
            warpsAdded.clear(); // Necessary since Twin Labyrinths is on both sides.
            while(!warpsAdded.contains(nextWarp)) {
                blockListData = new BlockListData((short)0x004e, (short)8);
                blockListData.getData().add((short)1); // Require mirai
                blockListData.getData().add(LocationCoordinateMapper.getGrailFlag(nextWarp, front));
                blockListData.getData().add(LocationCoordinateMapper.getImageIndex(nextWarp, front));
                blockListData.getData().add(nextWarp);
                blockListData.getData().add((short)LocationCoordinateMapper.getStartingRoom(nextWarp, front));
                blockListData.getData().add((short)LocationCoordinateMapper.getStartingScreen(nextWarp));
                blockListData.getData().add((short)(LocationCoordinateMapper.getStartingX(nextWarp, front) % 640));
                blockListData.getData().add((short)(LocationCoordinateMapper.getStartingY(nextWarp, front) % 480));
                grailPointsBlock.getBlockContents().add(blockListData);
                grailPointsBlock.getBlockContents().add(new BlockSingleData((short)0x000a));

                warpsAdded.add(nextWarp);
                nextWarp = LocationCoordinateMapper.getNextWarpZone(nextWarp, front);
            }
        }
        return grailPointsBlock;
    }

    private static Block buildHTMapBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        Block htMapBlock = new Block(blockIndex);
        addBlockContentsToBlock(htMapBlock, dataInputStream, numberOfShortsInThisBlock);
        if(Settings.isHalloweenMode()) {
            List<Short> textToAdd = FileUtils.stringToData(Translations.getText("event.halloween.HTsuffix"));
            for(int i = textToAdd.size() - 1; i >= 0; i--) {
                Short character = textToAdd.get(i);
                htMapBlock.getBlockContents().add(11, new BlockSingleData(character));
            }
        }
        return htMapBlock;
    }

    private static Block buildFairyQueenFirstConversationBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        // Throw away entire existing block contents in favor of custom
        int dataIndex = 0;
        while(dataIndex < numberOfShortsInThisBlock) {
            short blockData = dataInputStream.readShort();
            ++dataIndex;
        }

        Block halloweenBlock = new Block(blockIndex);
        halloweenBlock.getBlockContents().add(new BlockFlagData((short) 0x0040, (short) 740, (short) 1));
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.text1"));
        for (Short shortCharacter : stringCharacters) {
            halloweenBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }
        halloweenBlock.getBlockContents().add(new BlockItemData((short)0x0042, (short)84)); // Secret Treasure of Life
        halloweenBlock.getBlockContents().add(new BlockFlagData((short)0x0040, (short)0x1f5, (short)1));
        halloweenBlock.getBlockContents().add(new BlockSingleData((short) 0x0044)); // {CLS}

        stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.text2"));
        for (Short shortCharacter : stringCharacters) {
            halloweenBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }

        halloweenBlock.getBlockContents().add(new BlockFlagData((short) 0x0040, (short) 740, (short) 0)); // Can-exit flag

        return halloweenBlock;
    }

    private static Block buildFairyQueenLastConversationBlock(int blockIndex, DataInputStream dataInputStream, int numberOfShortsInThisBlock) throws IOException {
        // Throw away entire existing block contents in favor of custom
        int dataIndex = 0;
        while(dataIndex < numberOfShortsInThisBlock) {
            short blockData = dataInputStream.readShort();
            ++dataIndex;
        }

        Block halloweenBlock = new Block(blockIndex);
        halloweenBlock.getBlockContents().add(new BlockFlagData((short) 0x0040, (short) 740, (short) 1));
        List<Short> stringCharacters = FileUtils.stringToData(Translations.getText("event.halloween.fairyqueen"));
        for (Short shortCharacter : stringCharacters) {
            halloweenBlock.getBlockContents().add(new BlockSingleData(shortCharacter));
        }

        halloweenBlock.getBlockContents().add(new BlockFlagData((short) 0x0040, (short) 740, (short) 0)); // Can-exit flag

        return halloweenBlock;
    }

    private static short getNextZone(int startingZone) {
        if(startingZone == 21) {
            return 19;
        }
        return 1;
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
            else if(blockIndex == 1) {
                block = buildItemNameBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == 2) {
                block = buildItemDescriptionBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == 4) {
                block = new Block(blockIndex);
                addBlockContentsToBlock(block, dataInputStream, numberOfBytesInThisBlock / 2);
                if(Settings.isFools2020Mode()) {
                    for(BlockContents blockContents : block.getBlockContents()) {
                        if(blockContents instanceof BlockListData) {
                            BlockListData blockListData = (BlockListData)blockContents;
                            if(blockListData.getData().size() == 8
                                    && blockListData.getData().get(0) == 0x14
                                    && blockListData.getData().get(1) == 0x5a
                                    && blockListData.getData().get(2) == 0x46
                                    && blockListData.getData().get(3) == 0x5a
                                    && blockListData.getData().get(4) == 0x2710
                                    && blockListData.getData().get(5) == 0x1e
                                    && blockListData.getData().get(6) == 0x1
                                    && blockListData.getData().get(7) == 0x5a) {
                                blockListData.getData().set(4, (short)(352 * 10 + 1)); // Nerf Angel Shield hp
                                blockListData.getData().set(6, (short)0x2710); // Buff Fake Silver Shield hp
                            }
                        }
                    }
                }
            }
            else if(blockIndex == 5) {
                block = buildSoftwareMenuBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == 7) {
                block = buildGrailPointBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == 0x1c) {
                block = buildHTMapBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == 0xd7 && Settings.isHalloweenMode()) {
                block = buildFairyQueenFirstConversationBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == 0xda && Settings.isHalloweenMode()) {
                block = buildFairyQueenLastConversationBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == 671 || blockIndex == 672 || blockIndex == 673 || blockIndex == 674 || blockIndex == 675 || blockIndex == 677 || blockIndex == 678 || blockIndex == 679
                    || blockIndex == 680 || blockIndex == 681 || blockIndex == 683 || blockIndex == 689
                    || blockIndex == 693 || blockIndex == 694 || blockIndex == 696 || blockIndex == 698
                    || blockIndex == 700 || blockIndex == 701 || blockIndex == 702 || blockIndex == 704 || blockIndex == 706 || blockIndex == 707 || blockIndex == 708 || blockIndex == 709
                    || blockIndex == 710 || blockIndex == 718 || blockIndex == 723
                    || blockIndex == 726 || blockIndex == 0x39c || blockIndex == 0x39e || blockIndex == 991 || blockIndex == 993 || blockIndex == 1000) {
                block = buildMasterNpcBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
            }
            else if(blockIndex == 998 && Settings.isIncludeHellTempleNPCs()) {
                block = buildMasterNpcBlock(blockIndex, dataInputStream, numberOfBytesInThisBlock / 2);
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
        if(!LocationCoordinateMapper.isSurfaceStart() && Settings.getCurrentStartingLocation() != 23 && Settings.getCurrentStartingLocation() != 24) {
            block = AddObject.addShopBlock(datBlocks);
            if(!DataFromFile.getMapOfShopNameToShopBlock().containsKey(DataFromFile.CUSTOM_SHOP_NAME)) {
                DataFromFile.getMapOfShopNameToShopBlock().put(DataFromFile.CUSTOM_SHOP_NAME, block.getBlockNumber());
            }
            GameDataTracker.setCustomShopBlock(block.getBlockNumber());
        }
        GameDataTracker.addCustomBlocks(datBlocks);
        return datBlocks;
    }
}
