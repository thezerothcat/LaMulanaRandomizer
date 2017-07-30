package lmr.randomizer.dat;

import lmr.randomizer.Settings;
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
    private DatReader() {
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
//        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(Settings.laMulanaBaseDir + "\\data\\language\\en\\script_code.dat"));
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(Settings.datFileLocation));

        List<Block> datBlocks = new ArrayList<>();
        int numberOfBlocks = (int)dataInputStream.readShort();

        Block block;
        for(int blockIndex = 0; blockIndex < numberOfBlocks; blockIndex++) {
            int numberOfBytesInThisBlock = dataInputStream.readShort();

            block = new Block(blockIndex);
            addBlockContentsToBlock(block, dataInputStream, numberOfBytesInThisBlock / 2);
            datBlocks.add(block);
            GameDataTracker.addBlock(block);
        }
        return datBlocks;
    }
}
