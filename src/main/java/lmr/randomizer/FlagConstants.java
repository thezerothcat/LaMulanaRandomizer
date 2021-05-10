package lmr.randomizer;

/**
 * Created by thezerothcat on 7/20/2017.
 */
public final class FlagConstants {
    public static final int BOSSES_SHRINE_TRANSFORM = 0x102; // Value += 1 every time a boss is killed; 4-boss item checks this flag (for value = 4); set to 9 for transformed Shrine of the Mother
    public static final int HARDMODE = 0x16a; // Value 0 > 2 when hard mode is triggered.
    public static final int FISH_SHOP_UNLOCKS = 0x197; // Value 0 > 1 after bulling Mr. Fishman, 1 > 2 after using the seal to open the shop, 2 > 3 after using a Key Fairy to unlock the alternate shop.
    public static final int EXTINCTION_PERMA_LIGHT = 0x1c2; // Value 3 when perma-light is in effect (1 is probably for the seal in Birth and 2 for the seal in Extinction spawning the supertorch).
    public static final int EXTINCTION_TEMP_LIGHT = 0x1cd; // Value 0 > 1 when lighting Extinction via Flare Gun
    public static final int EDEN_UNLOCKED = 0x226; // Value 0 > 1 when Eden is unlocked (placing a weight after getting Fruit of Eden)
    public static final int SACRED_ORB_COUNT = 0x354; // Value += 1 for each orb collected; incremented via FlagTimer so may not reflect actual HP if cheating with orb duplication.
    public static final int ESCAPE = 0x382; // Value 0 > 1 when escape begins
    public static final int XELPUD_TALISMAN_CONVOS = 0xaec; // Value 0 > 1 when escape begins

    // Ankh flags
    public static final int AMPHISBAENA_ANKH_PUZZLE = 0x133; // State of Amphisbaena ankh; 5 when the ankh spawns, 6 when Amphisbaena is dead

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

    // Item world flags - determine if you've collected an item; value is always 0 uncollected > 2 collected, although opening a chest may set it to 1 on occasion
    public static final int WF_SHELL_HORN = 0x0a7;
    public static final int WF_BRONZE_MIRROR = 0x0ae;
    public static final int WF_PHILOSOPHERS_OCARINA = 0x0b5;

    // Tablet flags
    public static final int TABLET_SURFACE_GRAIL = 0x54e;

    // Fools 2021
    public static final int CUSTOM_ESCAPE_TIMER_STATE = 0xaba; // Incremented when the timer runs out
    public static final int CUSTOM_XMAILER_RECEIVED = 0xabb; // Incremented when given xmailer after the timer runs out


    private FlagConstants() { }
}
