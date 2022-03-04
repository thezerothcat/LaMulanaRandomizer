package lmr.randomizer.util;

public final class BlockDataConstants {
    public static final short EndOfEntry = 0x000a; // 10
    public static final short Space = 0x0020; // 32
    public static final short Unknown_C = 0x000c;
    public static final short Flag = 0x0040;
    public static final short Flag_Alt = 0x0041; // Apparently no difference between this and 0x0040?
    public static final short Item = 0x0042;
    public static final short Cls = 0x0044; // For advancing dialogue boxes
    public static final short Newline = 0x0045;
    public static final short Pose = 0x0046;
    public static final short Mantra = 0x0047;
    public static final short ColorChange = 0x004a;
    public static final short ItemName = 0x004d;
    public static final short DataList = 0x004e;
    public static final short Anime = 0x004f; // Scene

    public static final short BlockNoPrompt = 0;
    public static final short RandomBlockListSize = 2;
    public static final short FlagBlockListSize = 4;
    public static final short HeldItemBlockListSize = 4;
    public static final short ScoreBlockListSize = 4;
    public static final short ConversationTreeReferenceBlocksListSize = 4;


    private BlockDataConstants() { }
}
