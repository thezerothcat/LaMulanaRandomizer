package lmr.randomizer;

/**
 * Created by thezerothcat on 7/20/2017.
 */
public final class FlagConstants {
    public static final int SCORE = 0x07b; // Complicated value rules, but rando mostly doesn't care
    public static final int BOSSES_SHRINE_TRANSFORM = 0x102; // Value += 1 every time a boss is killed; 4-boss item checks this flag (for value = 4); set to 9 for transformed Shrine of the Mother
    public static final int SURFACE_UNDERPATH_VISIBLE = 0x14c; // Value 0 > 1 by Lemeza detector near the transitions into the Surface underpath, allowing you to navigate the area properly. One of the two detectors is added by randomizer; the other is vanilla.
    public static final int MAUSOLEUM_PUZZLE_ORB_CHEST = 0x165;
    public static final int HARDMODE = 0x16a; // Value 0 > 2 when hard mode is triggered.
    public static final int SPHINX_DESTROYED = 0x173; // Value 5 = destroyed
    public static final int SPRING_LEFT_HATCH = 0x194; // Value 0 > 1 after breaking the left hatch in Spring to flood the left side of Goddess.
    public static final int FISH_SHOP_UNLOCKS = 0x197; // Value 0 > 1 after bulling Mr. Fishman, 1 > 2 after using the seal to open the shop, 2 > 3 after using a Key Fairy to unlock the alternate shop.
    public static final int EXTINCTION_PERMA_LIGHT = 0x1c2; // Value 3 when perma-light is in effect (1 is probably for the seal in Birth and 2 for the seal in Extinction spawning the supertorch).
    public static final int EXTINCTION_TEMP_LIGHT = 0x1cd; // Value 0 > 1 when lighting Extinction via Flare Gun
    public static final int TWINS_POISON = 0x1d7; // Value 0 > 1 when entering the poison room (counting down), 1 > 2 activates the poison after time runs out
    public static final int TWINS_RELEASED = 0x1dc; // Value 0 > 1 when pressing first dais to release the twins, 1 > 2 when pressing second dais
    public static final int LITTLE_BROTHER_PURCHASES = 0x1ea; // Value += 1 for each purchase made from little brother
    public static final int BIG_BROTHER_UNLOCKED = 0x1f0; // Value 0 > 1 when unlocked, 1 > 2 when confirmed by chatting with little brother
    public static final int ENDLESS_PUZZLE_MAP_CHEST = 0x1f6; // Value 0 > 1 when chest is unlocked
    public static final int SHRINE_DRAGON_BONE = 0x218; // Value 0 > 1 when Dragon Bone is placed
    public static final int SHRINE_DIARY_CHEST = 0x219; // Value 0 > 1 when Diary chest has been seen, 1 > 2 after talking to Xelpud, 2 > 3 after collecting chest contents
    public static final int EDEN_UNLOCKED = 0x226; // Value 0 > 1 when Eden is unlocked (placing a weight after getting Fruit of Eden)
    public static final int MOONLIGHT_TO_TWIN_BREAKABLE_FLOOR = 0x25e; // Value 0 > 1 when the floor is broken
    public static final int GODDESS_STATUE_RUIN = 0x278; // Value 0 > 1 to remove the Goddess statue blocking entry to Tower of Ruin
    public static final int GODDESS_PUZZLE_FLAIL_WHIP = 0x27b; // Value 0 > 1 when reading the tablet, 1 > 2 when solved, 2 > 3 when weapon cover is gone
    public static final int GODDESS_STATUE_SHIELD = 0x284; // Goes to 2 when activated; 2 > 3 when gone
    public static final int TRANSLATION_TABLETS_READ = 0x2e5; // Value += 1 for each translation tablet read.
    public static final int ANCIENT_LAMULANESE_LEARNED = 0x2ea; // Value 0 > 1 when ancient La-Mulanese has been learned.
    public static final int LAMP_OF_TIME_STATE = 0x34d; // 0 = empty lamp, 1 = full lamp
    public static final int GODDESS_PIPES_SHORTCUT = 0x36d; // Goes to 2 when activated; 2 > 3 when gone
    public static final int DIMENSIONAL_PUZZLE_ANGEL_SHIELD = 0x2c1; // 0 > 1 when activated by the other two daises being pressed, 1 > 2 when weapon cover is gone
    public static final int DIMENSIONAL_CHILDREN_PARITY = 0x2c2; // 0 for even number of Tiamat's children are killed, 1 for odd
    public static final int DIMENSIONAL_ANGEL_SHIELD_DAIS_LEFT = 0x2d2; // 0 > 1 when dais is pressed
    public static final int DIMENSIONAL_ANGEL_SHIELD_DAIS_RIGHT = 0x2d3; // 0 > 1 when activated by the other two daises being pressed
    public static final int DIMENSIONAL_CHILDREN_DEAD = 0x2ec; // Value += 1 for each of Tiamat's children killed
    public static final int DEV_ROOM_COMBO = 0x348; // 0 when not equipped, 1 when equipped
    public static final int MEDICINE_PUZZLE_SOLVED = 0x34f; // Value 0 > 1 after reciting BIRTH/DEATH; allows re-doing the puzzle for alternate medicine color.
    public static final int SACRED_ORB_COUNT = 0x354; // Value += 1 for each orb collected; incremented via FlagTimer so may not reflect actual HP if cheating with orb duplication.
    public static final int ESCAPE = 0x382; // Value 0 > 1 when escape begins
    public static final int KILL_FLAG = 0x3e9; // Instant death when set
    public static final int ESCAPE_TRIGGERED = 0x403; // Value 0 > 1 alongside triggering the escape timer and screenshake.
    public static final int HT_PUZZLE_ROOM33_PILLARS = 0x70d;
    public static final int MOTHER_ANKH_JEWEL_RECOVERY = 0xad2; // Rando-specific; 0 > 1 after the sound effect has played to indicate that Shrine map was collected.
    public static final int WRONG_COLOR_MEDICINE = 0xad4; // Rando-specific; used on a FlagTimer that tests == 0 and assigns = 1 when setting medicine statue to match wrong-color medicine.
    public static final int SOUND_EFFECT_PLAYED_SHRINE_MAP = 0xaee; // Rando-specific; 0 > 1 after the sound effect has played to indicate that Shrine map was collected.

    public static final int XELPUD_CONVERSATION_GENERAL = 0x07c; // Assorted vanilla conversations. 1=met 2=immediately went back in  3=urgent 4=lonely 5=sorry 6=urgentforreal 7=legend 8=returned after mulbruk 9=sleepinggirl...
    public static final int XELPUD_CONVERSATION_MSX2 = 0x21d; // Value 0 > 1 when 4-boss item is available from Nebur, 1 > 2 after talking to Xelpud or buying the item
    public static final int XELPUD_CONVERSATION_TALISMAN_FOUND = 0xaec; // Value 0 > 1 via rando-added timer on Xelpud's screen when Talisman conversation is enabled; progress goes toward opening the Diary chest in Shrine of the Mother
    public static final int XELPUD_CONVERSATION_DIARY_FOUND = 0xaed; // Value 0 > 1 via rando-added timer on Xelpud's screen when Diary conversation is enabled

    public static final int MULBRUK_CONVERSATION_AWAKE = 0x391;
    public static final int MULBRUK_CONVERSATION_BOOK = 0x32a; // Value 0 > 1 via rando-added timer on Xelpud's screen when Diary conversation is enabled

    // Ankh flags
    public static final int AMPHISBAENA_ANKH_PUZZLE = 0x133; // State of Amphisbaena ankh; 5 when the ankh spawns, 6 when Amphisbaena is dead
    public static final int SAKIT_ANKH_PUZZLE = 0x164; // State of Sakit ankh; 1 when the ankh spawns, 2 when Sakit is dead

    // Boss state flags - Ankh has updates for 1, 2, and 3; value = 3 when the boss fight is active
    public static final int AMPHISBAENA_STATE = 0x0f6;
    public static final int SAKIT_STATE = 0x0f7;
    public static final int ELLMAC_STATE = 0x0f8;
    public static final int BAHAMUT_STATE = 0x0f9;
    public static final int VIY_STATE = 0x0fa;
    public static final int PALENQUE_STATE = 0x0fb;
    public static final int BAPHOMET_STATE = 0x0fc;
    public static final int TIAMAT_STATE = 0x0fd;
    public static final int MOTHER_STATE = 0x0fe; // Goes to 3 when dead; vanilla shops test for value = 3 to close doors during the escape
    public static final int THE_BOSS_STATE = 0x7f1;

    // Sub-boss state flags
    public static final int BUER_STATE = 0x17a;
    public static final int USHUMGALLU_STATE = 0x2cc; // Value 0 > 1 when Ushumgallu dies, 1 > 2 via FlagTimer

    // Backside door unlock flags
    public static final int KEY_FAIRY_DOOR_UNLOCKED = 0x1c9;

    // Item world flags - determine if you've collected an item; value is always 0 uncollected > 2 collected, although opening a chest may set it to 1 on occasion
    public static final int WF_TALISMAN = 0x0a4;
    public static final int WF_SHELL_HORN = 0x0a7;
    public static final int WF_BRONZE_MIRROR = 0x0ae;
    public static final int WF_PHILOSOPHERS_OCARINA = 0x0b5;
    public static final int WF_FEATHER = 0x0b6;
    public static final int WF_MAP_SHRINE = 0x0da;
    public static final int WF_DIARY = 0x104;
    public static final int WF_MSX2 = 0x2e6;

    // Mantra recited flags
    public static final int MANTRA_FINAL = 0x124; // In vanilla, this is just for reciting LAMULANA, but since randomizer allows out of order mantras, LAMULANA is now a different flag, and this is mostly just set to 4 for empowering the Key Sword after enough mantras have been recited.

    // Tablet flags
    public static final int TABLET_GRAIL_SURFACE = 0x54e; // Randomizer uses this for the grail flag if non-Surface starting location; vanilla only uses it for the tablet glow effect.
    public static final int TABLET_GRAIL_GUIDANCE = 0x064;
    public static final int TABLET_GRAIL_MAUSOLEUM = 0x065;
    public static final int TABLET_GRAIL_SUN = 0x066;
    public static final int TABLET_GRAIL_SPRING = 0x067;
    public static final int TABLET_GRAIL_INFERNO = 0x068;
    public static final int TABLET_GRAIL_EXTINCTION = 0x069;
    public static final int TABLET_GRAIL_TWIN_FRONT = 0x06a;
    public static final int TABLET_GRAIL_ENDLESS = 0x06b;
    public static final int TABLET_GRAIL_SHRINE_FRONT = 0x06c;
    public static final int TABLET_GRAIL_ILLUSION = 0x06d;
    public static final int TABLET_GRAIL_GRAVEYARD = 0x06e;
    public static final int TABLET_GRAIL_MOONLIGHT = 0x06f;
    public static final int TABLET_GRAIL_GODDESS = 0x070;
    public static final int TABLET_GRAIL_RUIN = 0x071;
    public static final int TABLET_GRAIL_BIRTH = 0x072;
    public static final int TABLET_GRAIL_TWIN_BACK = 0x073;
    public static final int TABLET_GRAIL_DIMENSIONAL = 0x074;
    public static final int TABLET_GRAIL_SHRINE_BACK = 0x075;

    // Mail flags
    public static final int MAIL_COUNT = 0x328;
    public static final int MAIL_43 = 0x317;

    // Fools 2021
    public static final int CUSTOM_ESCAPE_TIMER_STATE = 0xaba; // Incremented when the timer runs out
    public static final int CUSTOM_XMAILER_RECEIVED = 0xabb; // Incremented when given xmailer after the timer runs out

    private FlagConstants() { }
}
