package lmr.randomizer.dat;

import lmr.randomizer.dat.shop.BlockStringData;

import java.io.DataOutputStream;
import java.io.IOException;

public class TabletBlock extends Block {
    private BlockStringData tabletText;
    private BlockListData langPictureData;
    private BlockListData graphicFileCoordinateData;
    private BlockListData tabletDrawCoordinateData;

    public TabletBlock(int blockNumber) {
        super(blockNumber);
    }

    public BlockStringData getTabletText() {
        return tabletText;
    }

    public void setTabletText(BlockStringData tabletText) {
        this.tabletText = tabletText;
    }

    public BlockListData getLangPictureData() {
        return langPictureData;
    }

    public void setLangPictureData(BlockListData langPictureData) {
        this.langPictureData = langPictureData;
    }

    public BlockListData getGraphicFileCoordinateData() {
        return graphicFileCoordinateData;
    }

    public void setGraphicFileCoordinateData(BlockListData graphicFileCoordinateData) {
        this.graphicFileCoordinateData = graphicFileCoordinateData;
    }

    public BlockListData getTabletDrawCoordinateData() {
        return tabletDrawCoordinateData;
    }

    public void setTabletDrawCoordinateData(BlockListData tabletDrawCoordinateData) {
        this.tabletDrawCoordinateData = tabletDrawCoordinateData;
    }

    @Override
    public int getBlockSize() {
        int size = tabletText.getSize();
        size += langPictureData.getSize();
        if(langPictureData.getData().get(1) != 0) {
            // Has slate image data
            size += graphicFileCoordinateData.getSize();
            size += tabletDrawCoordinateData.getSize();
        }
        return size;
    }

    @Override
    public void write(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(getBlockSize());

        tabletText.writeBytes(dataOutputStream);
        langPictureData.writeBytes(dataOutputStream);
        if(langPictureData.getData().get(1) != 0) {
            // Has slate image data
            graphicFileCoordinateData.writeBytes(dataOutputStream);
            tabletDrawCoordinateData.writeBytes(dataOutputStream);
        }
    }
}
