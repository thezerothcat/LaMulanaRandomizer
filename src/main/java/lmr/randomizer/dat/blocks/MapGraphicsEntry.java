package lmr.randomizer.dat.blocks;

import lmr.randomizer.dat.blocks.contents.BlockContents;
import lmr.randomizer.util.BlockDataConstants;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapGraphicsEntry implements BlockContents {
    public static int Icon_Blank = 0;
    public static int Icon_BacksideDoor = 1;
    public static int Icon_GrailTablet = 2;
    public static int Icon_CrossOfLight = 3;
    public static int Icon_FairyPoint = 4;
    public static int Icon_BrownDoor = 5;
    public static int Icon_BlueDoor = 6;
    public static int Icon_PhilosopherDoor = 7;
    public static int Icon_UpExit = 8;
    public static int Icon_DownExit = 9;
    public static int Icon_LeftExit = 10;
    public static int Icon_RightExit = 11;
    public static int Icon_DragonBone = 12;

    public static int IconColor_Invisible = 0;
    public static int IconColor_White = 1;
    public static int IconColor_Green = 2;
    public static int IconColor_Yellow = 3;
    public static int IconColor_Red = 4;
    public static int IconColor_Blue = 5;
    public static int IconColor_Transparent = 6;

    private int positionData; // position = issecret * 10000 + X*100 + Y
    private int appearanceData;// appearance: Icon1 * 1000 + Icon2 * 10 + Color

    public MapGraphicsEntry(int positionData, int appearanceData) {
        this.positionData = positionData;
        this.appearanceData = appearanceData;
    }

    public int getIcon1() {
        return appearanceData / 1000;
    }

    public int getIcon2() {
        return appearanceData % 1000 / 10;
    }

    public int getIconColor() {
        return appearanceData % 10;
    }

    public void setIcon1(int icon) {
        setAppearanceData(icon, getIcon2(), getIconColor());
    }

    public void setIcon2(int icon) {
        setAppearanceData(getIcon1(), icon, getIconColor());
    }

    public void setIconColor(int iconColor) {
        setAppearanceData(getIcon1(), getIcon2(), iconColor);
    }

    private void setAppearanceData(int icon1, int icon2, int iconColor) {
        appearanceData = icon1 * 1000 + icon2 * 10 + iconColor;
    }

    public boolean isSecret() {
        return positionData >= 10000;
    }

    public int getX() {
        return positionData % 10000 / 100;
    }

    public int getY() {
        return positionData % 100;
    }

    public void setX(int x) {
        setPositionData(x, getY(), isSecret());
    }

    public void setY(int y) {
        setPositionData(getX(), y, isSecret());
    }

    public void setSecret(boolean isSecret) {
        setPositionData(getX(), getY(), isSecret);
    }

    private void setPositionData(int x, int y, boolean isSecret) {
        positionData = (isSecret ? 10000 : 0) + x * 100 + y;
    }

    @Override
    public int getSize() {
        return 8; // CMD, list length, then two items
    }

    @Override
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        rawData.add(BlockDataConstants.DataList);
        rawData.add((short)2);
        rawData.add((short)positionData);
        rawData.add((short)appearanceData);
        return rawData;
    }

    @Override
    public void writeBytes(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeShort(BlockDataConstants.DataList);
        dataOutputStream.writeShort(2);
        dataOutputStream.writeShort(positionData);
        dataOutputStream.writeShort(appearanceData);
    }
}
