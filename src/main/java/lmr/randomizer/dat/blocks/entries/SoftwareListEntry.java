package lmr.randomizer.dat.blocks.entries;

public class SoftwareListEntry extends ListEntry {
    public SoftwareListEntry() {
        super(2);
    }

    public SoftwareListEntry(SoftwareListEntry objectToCopy) {
        super(objectToCopy);
    }

    public void setSoftwareCost(int softwareCost) {
        getData().set(0, (short)softwareCost);
    }

    public void setSoftwareType(int softwareType) {
        getData().set(1, (short)softwareType);
    }
}
