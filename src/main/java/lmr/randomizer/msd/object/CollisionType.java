package lmr.randomizer.msd.object;

import java.util.HashMap;
import java.util.Map;

public enum CollisionType {
    EMPTY(0x00),
    LADDER_LEFT(0x01),
    LADDER_LEFT_CENTER(0x02),
    LADDER_RIGHT_CENTER(0x03),
    LADDER_RIGHT(0x04),
    WATER(0x05),
    WATER_FLOW_UP(0x06),
    WATER_FLOW_RIGHT(0x07),
    WATER_FLOW_DOWN(0x08),
    WATER_FLOW_LEFT(0x09),
    WATER_LADDER_LEFT(0x0A),
    WATER_LADDER_LEFT_CENTER(0x0B),
    WATER_LADDER_RIGHT_CENTER(0x0C),
    WATER_LADDER_RIGHT(0x0D),
    LAVA(0x10),
    LAVA_FLOW_UP(0x11),
    LAVA_FLOW_RIGHT(0x12),
    LAVA_FLOW_DOWN(0x13),
    LAVA_FLOW_LEFT(0x14),
    LAVA_LADDER_LEFT(0x15),
    LAVA_LADDER_LEFT_CENTER(0x16),
    LAVA_LADDER_RIGHT_CENTER(0x17),
    LAVA_LADDER_RIGHT(0x18),
    WATERFALL(0x20),
    YOWIE_TILE(0x30), // For wall-scarabs in Moonlight
    OVERRIDE_EMPTY(0x7F),
    // Values past here are written unsigned to match the docs, but are actually stored as the shown value - 256 since
    // Java doesn't have unsigned int types.
    GROUND(0x80),
    SLOPE_45_ASC(0x81),
    SLOPE_45_DESC(0x82),
    SLOPE_30_ASC_LOWER(0x83),
    SLOPE_30_ASC_UPPER(0x84),
    SLOPE_30_DESC_UPPER(0x85),
    SLOPE_30_DESC_LOWER(0x86),
    SLOPE_60_ASC_LOWER(0x87),
    SLOPE_60_ASC_UPPER(0x88),
    SLOPE_60_DESC_UPPER(0x89),
    SLOPE_60_DESC_LOWER(0x8A),
    ICE(0x8C),
    ICE_SLOPE_45_ASC(0x8D),
    ICE_SLOPE_45_DESC(0x8E),
    ICE_SLOPE_30_ASC_LOWER(0x8F),
    ICE_SLOPE_30_ASC_UPPER(0x90),
    ICE_SLOPE_30_DESC_UPPER(0x91),
    ICE_SLOPE_30_DESC_LOWER(0x92),
    SAND_SLOPE_45_ASC(0xA1),
    SAND_SLOPE_45_DESC(0xA3),
    SAND_SLOPE_30_ASC_LOWER(0xA6),
    SAND_SLOPE_30_ASC_UPPER(0xA7),
    SAND_SLOPE_30_DESC_UPPER(0xAA),
    SAND_SLOPE_30_DESC_LOWER(0xAB),
    CEILING_SLOPE_45_ASC(0xB5),
    CEILING_SLOPE_45_DESC(0xB6),
    CEILING_SLOPE_30_ASC_LOWER(0xB7),
    CEILING_SLOPE_30_ASC_UPPER(0xB8),
    CRUSH_VERTICAL(0xC0),
    CRUSH_HORIZONTAL(0xC1);

    public final byte value;

    CollisionType(int value) {
        var signedVal = value > 127 ? value - 256 : value;
        this.value = (byte) signedVal;
    }

    private static final Map<Byte, CollisionType> BY_VALUE = new HashMap<>();

    static {
        for (CollisionType t : CollisionType.values())
            BY_VALUE.put(t.value, t);
    }

    public static CollisionType get(byte value) {
        return BY_VALUE.get(value);
    }
}
