package lmr.randomizer.rcd.object;

public enum PotGraphic {
    SURFACE(0),
    GUIDANCE(0),
    MAUSOLEUM(0),
    SUN(1),
    SPRING(2),
    INFERNO(3),
    EXTINCTION_WALL(4),
    EXTINCTION_POT(5),
    TWIN_LABYRINTHS(6),
    ENDLESS(7),
    SHRINE(8),
    UNKNOWN3(9),
    ILLUSION(10),
    GRAVEYARD(11),
    MOONLIGHT(12),
    GODDESS(13),
    RUIN(14),
    BIRTH(15),
    DIMENSIONAL(16),
    UNKNOWN4(17),
    UNKNOWN5(18),
    GATE_OF_TIME(19);

    private short graphic;

    PotGraphic(int graphic) {
        this.graphic = (short)graphic;
    }

    public short getGraphic() {
        return graphic;
    }
}
