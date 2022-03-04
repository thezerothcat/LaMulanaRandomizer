package lmr.randomizer.dat.update;

import lmr.randomizer.dat.blocks.contents.BlockContents;
import lmr.randomizer.dat.blocks.contents.BlockSingleData;
import lmr.randomizer.util.BlockDataConstants;
import lmr.randomizer.FileUtils;
import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import lmr.randomizer.dat.*;
import lmr.randomizer.dat.blocks.Block;
import lmr.randomizer.dat.blocks.ScannableBlock;

import java.util.List;

public class Fools2021DatUpdater extends DatUpdater {
    public Fools2021DatUpdater(DatFileData datFileData) {
        super(datFileData);
    }

    @Override
    public void updateFootOfFutoScannableBlock(ScannableBlock scannableBlock) {
        String hintText = String.format(Translations.getText("hintText.giants"), Settings.getCurrentGiant(), Settings.getCurrentGiant());
        scannableBlock.setScanText(buildTextEntry(hintText));
    }

    @Override
    public void updateEmailBlock(Block emailBlock, int mailNumber) {
        List<BlockContents> blockContents = emailBlock.getBlockContents();
        blockContents.clear();
        for (Short singleCharacter : FileUtils.stringToData(Translations.getText("event.fools2021.mailTitle"))) {
            blockContents.add(new BlockSingleData(singleCharacter));
        }
        blockContents.add(new BlockSingleData(BlockDataConstants.EndOfEntry));
        for (Short singleCharacter : FileUtils.stringToData(Translations.getText("event.fools2021.mailText"))) {
            blockContents.add(new BlockSingleData(singleCharacter));
        }
    }
}
