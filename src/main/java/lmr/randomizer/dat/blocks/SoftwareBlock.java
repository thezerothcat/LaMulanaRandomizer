package lmr.randomizer.dat.blocks;

import lmr.randomizer.dat.Block;
import lmr.randomizer.dat.blocks.entries.SoftwareListEntry;
import lmr.randomizer.dat.blocks.entries.TextEntry;

public class SoftwareBlock extends Block {
    public static final int SoftwareReader = 0;
    public static final int SoftwareXmailer = 1;
    public static final int SoftwareYagomap = 2;
    public static final int SoftwareYagostr = 3;
    public static final int SoftwareBunemon = 4;
    public static final int SoftwareBunplus = 5;
    public static final int SoftwareTorude = 6;
    public static final int SoftwareGuild = 7;
    public static final int SoftwareMantra = 8;
    public static final int SoftwareEmusic = 9;
    public static final int SoftwareBeolamu = 10;
    public static final int SoftwareDeathv = 11;
    public static final int SoftwareRandc = 12;
    public static final int SoftwareCapstar = 13;
    public static final int SoftwareMove = 14;
    public static final int SoftwareMekuri = 15;
    public static final int SoftwareBounce = 16;
    public static final int SoftwareMiracle = 17;
    public static final int SoftwareMirai = 18;
    public static final int SoftwareLamulana = 19;

    private static final int SoftwareCostIndex = 0;
//    private static final int SoftwareTypeIndex = 1;

    private static final int SoftwareNameOffset = 1;
    private static final int SoftwareCostTextOffset = 2;

    private static final int EntriesPerSoftware = 3;

    public SoftwareBlock(int blockNumber) {
        super(blockNumber);
    }

    /**
     * @param softwareIndex a constant from this class, with the index of the first entry for that software (out of 3)
     * @param newMemoryUsage amount of memory to be used by the software
     */
    public void setSoftwareCost(int softwareIndex, int newMemoryUsage) {
        ((SoftwareListEntry)getBlockContents().get(softwareIndex * EntriesPerSoftware)).getData().set(SoftwareCostIndex, (short)newMemoryUsage);
    }

    public void setSoftwareName(int softwareIndex, TextEntry newSoftwareName) {
        getBlockContents().set(softwareIndex * EntriesPerSoftware + SoftwareNameOffset, newSoftwareName);
    }

    public void setSoftwareCostText(int softwareIndex, TextEntry newSoftwareCostText) {
        getBlockContents().set(softwareIndex * EntriesPerSoftware + SoftwareCostTextOffset, newSoftwareCostText);
    }
}
