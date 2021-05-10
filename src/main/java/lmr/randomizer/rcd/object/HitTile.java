package lmr.randomizer.rcd.object;

public enum HitTile {
    Air(0x00),
    Ladder_Air_Left(0x01),
    Ladder_Air_LeftMiddle(0x02),
    Ladder_Air_RightMiddle(0x03),
    Ladder_Air_Right(0x04),
    Water(0x05),
    Water_FlowUp(0x06),
    Water_FlowRight(0x07),
    Water_FlowDown(0x08),
    Water_FlowLeft(0x09),
    Ladder_Water_Left(0x0a),
    Ladder_Water_LeftMiddle(0x0b),
    Ladder_Water_RightMiddle(0x0c),
    Ladder_Water_Right(0x0d),
    Lava(0x10),
    Lava_FlowUp(0x11),
    Lava_FlowRight(0x12),
    Lava_FlowDown(0x13),
    Lava_FlowLeft(0x14),
    Ladder_Lava_Left(0x15),
    Ladder_Lava_LeftMiddle(0x16),
    Ladder_Lava_RightMiddle(0x17),
    Ladder_Lava_Right(0x18),
    Waterfall(0x20),
    Yowie_Tile(0x30),
    Air_ReplacesSolid(0x7f),
    Solid(0x80), // Wall/Floor
    Slope_Up_45(0x81),
    Slope_Down_45(0x82),
    Slope_Up_30_Base(0x83),
    Slope_Up_30_Top(0x84),
    Slope_Down_30_Top(0x85),
    Slope_Down_30_Base(0x86),
    Slope_Up_60_Base(0x87),
    Slope_Up_60_Top(0x88),
    Slope_Down_60_Top(0x89),
    Slope_Down_60_Base(0x8a),
    Ice_Solid(0x8c),
    Ice_Slope_Up_45(0x8d),
    Ice_Slope_Down_45(0x8e),
    Ice_Slope_Up_30_Base(0x8f),
    Ice_Slope_Up_30_Top(0x90),
    Ice_Slope_Down_30_Top(0x91),
    Ice_Slope_Down_30_Base(0x92),
    Sand_Slope_45_FlowLeft(0xa1),
    Sand_Slope_45_FlowRight(0xa3),
    Sand_Slope_30_FlowLeft_1(0xa6),
    Sand_Slope_30_FlowLeft_2(0xa7),
    Sand_Slope_30_FlowRight_1(0xaa), // This is not used anywhere. It is unknown if it works.
    Sand_Slope_30_FlowRight_2(0xab), // This is not used anywhere. It is unknown if it works.
    Ceiling_Slope_45_1(0xb5),
    Ceiling_Slope_45_2(0xb6),
    Ceiling_Slope_30_1(0xb7), // They are only used in Inferno Cavern twice each, and it is unknown if they do anything.
    Ceiling_Slope_30_2(0xb8), // They are only used in Inferno Cavern twice each, and it is unknown if they do anything.
    CrusherTile_CrushVertical(0xc0),
    CrusherTile_CrushHorizontal(0xc1);

    private short id;

    HitTile(int id) {
        this.id = (short)id;
    }

    public short getId() {
        return id;
    }
}
