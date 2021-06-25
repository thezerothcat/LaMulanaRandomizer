package lmr.randomizer.util;

public final class FlagConstants {
    public static final int TOTAL_COIN_CHESTS = 0x077; // 28 to get luck fairy in the credits
    public static final int GILTORIYO_LADDER = 0x078; // Value 0 > 1 when talking to Giltoriyo, spawns the philosopher ladder in Endless Corridor with frame timers for animation
    public static final int ALSEDANA_LADDER = 0x07a; // Value 0 > 1 when talking to Alsedana, spawns the philosopher ladder in Tower of Ruin with frame timers for animation
    public static final int SCORE = 0x07b; // Complicated value rules, but rando mostly doesn't care
    public static final int SAMARANTA_LADDER = 0x08b; // Value 0 > 1 when talking to Samaranta, spawns the philosopher ladder in Tower of the Goddess with frame timers for animation
    public static final int BOSSES_SHRINE_TRANSFORM = 0x102; // Value += 1 every time a boss is killed; 4-boss item checks this flag (for value = 4); set to 9 for transformed Shrine of the Mother
    public static final int FOBOS_LADDER = 0x10d; // Value 0 > 1 when talking to Fobos, spawns the philosopher ladder in Shrine of the Mother
    public static final int FAIRY_POINTS_ACTIVE = 0x118; // Value 0 > 1 when talking to The Fairy Queen after collecting Isis' Pendant, activates fairy points
    public static final int GUIDANCE_ELEVATOR = 0x134; // Value 0 > 1 when hitting the elevator block in the Guidance mouth (where Dracuet's door is), other increments via FlagTimer for animation
    public static final int GUIDANCE_PUZZLE_TREASURES_CHEST = 0x137; // Value 0 > 1 when using Pepper on the statue, 1 > 2 when the chest is done animating to make it possible to open.
    public static final int SURFACE_RUINS_OPENED = 0x145; // Set in vanilla first conversation with Xelpud, changed in randomizer to use 0xad0. For some reason, warp graphics sometimes test this flag.
    public static final int SURFACE_UNDERPATH_VISIBLE = 0x14c; // Value 0 > 1 by Lemeza detector near the transitions into the Surface underpath, allowing you to navigate the area properly. One of the two detectors is added by randomizer; the other is vanilla.
    public static final int SURFACE_PUZZLE_SEAL_COIN_CHEST = 0x14d; // In vanilla, this is for opening the seal and opening the chest. In randomizer, the chest uses a different world flag, but this remains as the puzzle flag.
    public static final int SURFACE_WATERFALL_WALL_BATS = 0x151; // Value 0 > 1 when the wall/Hitbox is broken for the wall that has bats behind it, 1 > 2 as the wall cover despawns and bats spawn
    public static final int SURFACE_PUZZLE_WATERFALL_WALL_SACRED_ORB = 0x14b; // Value 0 > 1 when the wall/Hitbox is broken for the wall that has bats behind it, also used as the puzzle flag for the chest
    public static final int MAUSOLEUM_PUZZLE_ORB_CHEST = 0x165; // Value 0 > 1 when placing a weight at the proper giant's foot, unlocks the Mausoleum Sacred Orb chest
    public static final int HARDMODE = 0x16a; // Value 0 > 2 when hard mode is triggered.
    public static final int SPHINX_DESTROYED = 0x173; // Value 5 = destroyed
    public static final int SUN_WATCHTOWER_LIGHTS = 0x179; // 1,2=triggered 3=seen
    public static final int SPHINX_DESTROYED_V2 = 0x17d; // Value 0 > 1 when 0x173 set to 5, not sure why both flags exist
    public static final int SUN_MAP_CHEST_LADDER_DESPAWNED = 0x183; // Used to despawn the ladder for the Sun map chest in vanilla // todo: can this be claimed for something?
    public static final int SUN_MAP_CHEST_LADDER_RESTORED = 0x188; // Set to 1 after pushing the statue to respawn the ladder
    public static final int SPRING_FLOODED = 0x193; // Value 0 > 1 after breaking the left hatch in Spring to flood the left side of Goddess.
    public static final int SPRING_LEFT_HATCH = 0x194; // Value 0 > 1 after breaking the left hatch in Spring to flood the left side of Goddess.
    public static final int FISH_SHOP_UNLOCKS = 0x197; // Value 0 > 1 after bulling Mr. Fishman, 1 > 2 after using the seal to open the shop, 2 > 3 after using a Key Fairy to unlock the alternate shop.
    public static final int SPRING_BAHAMUT_ROOM_FLOODED = 0x199; // Value 0 > 1 after hitting the gear outside Bahamut's room.
    public static final int INFERNO_VIY_MANTRA_STATUE = 0x1a9; // 1=break it 2=statue will disappear 3=statue disappears
    public static final int INFERNO_FAKE_ORB_CRUSHER = 0x1ac; // For the fake Sacred Orb trap in Inferno Cavern, made up of a graphic, a Lemeza detector, and a crusher. // todo: can this be claimed for something?
    public static final int INFERNO_CHAIN_WHIP_CRUSHER_LEFT = 0x1b1; // Vanilla sets this when the left crusher starts going up. Randomizer swaps this for a screen flag so it can't perma-fail.
    public static final int INFERNO_CHAIN_WHIP_CRUSHER_RIGHT = 0x1b2; // Vanilla sets this when the right crusher starts going up. Randomizer swaps this for a screen flag so it can't perma-fail.
    public static final int INFERNO_PUZZLE_FLARE_GUN = 0x1b3; // Value 0 > 1 for breaking the wall to enter; 1 > 2 when puzzle solved, 2 > 3 when the weapon cover finishes de-spawning.
    public static final int INFERNO_PUZZLE_ICE_CAPE_CHEST = 0x1b7; // Value 0 > 2 when solving push block puzzle.
    public static final int EXTINCTION_PERMA_LIGHT = 0x1c2; // Value 3 when perma-light is in effect (1 is probably for the seal in Birth and 2 for the seal in Extinction spawning the supertorch).
    public static final int EXTINCTION_PALENQUE_SCREEN_MURAL = 0x1ca; // Value 0 > 1 after placing the weight in the Extinction <> Birth teleport area, 2 and 3 for animation of mural falling
    public static final int EXTINCTION_TEMP_LIGHT = 0x1cd; // Value 0 > 1 when lighting Extinction via Flare Gun
    public static final int EXTINCTION_TRAP_FAKE_ANKH = 0x1cf; // Triggers crushers via Lemeza detector on approaching the fake ankh
    public static final int TWINS_POISON = 0x1d7; // 2 = elevator moving
    public static final int TWINS_FRONT_GRAIL_ELEVATOR = 0x1db; // Value 0 > 1 when pressing first dais to release the twins, 1 > 2 when pressing second dais
    public static final int TWINS_RELEASED = 0x1dc; // Value 0 > 1 when pressing first dais to release the twins, 1 > 2 when pressing second dais
    public static final short TWIN_UNSOLVABLE_PUZZLE = 0x1e4; // Value 0 > 1 when solving the block pushing puzzle in Twin Labyrinths on the screen with the lamp recharge station/Lamp of Time Shop
    public static final int LITTLE_BROTHER_PURCHASES = 0x1ea; // Value += 1 for each purchase made from little brother
    public static final int BIG_BROTHER_UNLOCKED = 0x1f0; // Value 0 > 1 when unlocked, 1 > 2 when confirmed by chatting with little brother
    public static final int ENDLESS_PUZZLE_MAP_CHEST = 0x1f6; // Value 0 > 1 when chest is unlocked
    public static final int ILLUSION_PUZZLE_EXPLODING_CHEST = 0x20a; // Exploding chest uses
    public static final int ILLUSION_WARP_MAZE_ACTIVE = 0x20c; // Removed in randomizer // todo: can this be claimed for something?
    public static final int SHRINE_PUZZLE_DIARY_CHEST = 0x212; // Changed in randomizer. Original: 1=xelpud 2=left room after collecting 3=reported in and got the la-mulana talisman
    public static final int SHRINE_DRAGON_BONE = 0x218; // Value 0 > 1 when Dragon Bone is placed
    public static final int SHRINE_DIARY_CHEST = 0x219; // Value 0 > 1 when Diary chest has been seen, 1 > 2 after talking to Xelpud, 2 > 3 after collecting chest contents
    public static final int SHRINE_SHAWN = 0x21b; // 1 applies to both spots you can see him
    public static final int EDEN_UNLOCKED = 0x226; // Value 0 > 1 when Eden is unlocked (placing a weight after getting Fruit of Eden)
    public static final int ILLUSION_PROGRESS_SKELETON_DAIS_TO_ELEVATOR = 0x227; // Value 0 > 1 when placing a weight on the skeleton in lower Illusion, 2 after solving "extinguish the whole", 3 via Lemeza detector in the elevator room on the way up to the grail
    public static final int MR_SLUSHFUND_CONVERSATION = 0x228; // Set to 1 in the conversation for receiving Pepper, set to 2 in the conversation for receiving Anchor.
    public static final int PROVE_THOU_ART_SMALL = 0x22a; // 2=doll, 3=small, 4=read, 5=block disappears
    public static final int COG_MUDMEN_STATE = 0x23a; // Value 4 when using the Cog of the Soul at the expected tablet to spawn mudmen
    public static final int GRAVEYARD_ILLUSION_LADDER_BLOCKAGE = 0x23f; // Set to 1 after bombing the wall in Graveyard to open up emusic.exe scan
    public static final int GRAVEYARD_WALL_SNAPSHOTS_MURAL = 0x24f; // The small breakable thing that prevents the ice block from dropping so you can push the block in place and spawn the ladder. Looks like it may be used to animate the block dropping
    public static final int MOONLIGHT_DEV_ROOM_BREAKABLE_FLOOR = 0x256; // Value 0 > 1 when the floor is broken
    public static final int MOONLIGHT_TO_TWIN_BREAKABLE_FLOOR = 0x25e; // Value 0 > 1 when the floor is broken
    public static final int MOONLIGHT_3_WOMEN_FACES = 0x262; // Set to 1 after hitting the face in Moon-gazing pit with a subweapon.
    public static final int RUIN_LADDER_NUWA_V2 = 0x265; // Don't remember how/why this is different from 0x369
    public static final int MOONLIGHT_SCAN_DANCING_MAN = 0x270; // Set to 1 after scanning the Dancing Man
    public static final int GODDESS_LIGHTS_ON = 0x271; // 3 = lights on
    public static final int GODDESS_STATUE_RUIN = 0x278; // Value 0 > 1 to remove the Goddess statue blocking entry to Tower of Ruin
    public static final int GODDESS_PUZZLE_FLAIL_WHIP = 0x27b; // Value 0 > 1 when reading the tablet, 1 > 2 when solved, 2 > 3 when weapon cover is gone
    public static final int GODDESS_STATUE_SHIELD_ANIMATION = 0x284; // Goes to 2 when activated; 2 > 3 when gone
    public static final int RUIN_PUZZLE_NUWA = 0x298; // 0 > 1 when hitting the seal, 2 when pressing the dais at the bottom of the philosopher ladder, 3 when Nuwa is dead?
    public static final int MOONLIGHT_SCAN_HANDS = 0x29c; // Set to 1 after scanning the hands of the four
    public static final int MOONLIGHT_SCAN_TRAP = 0x29d; // Set to 1 after scanning the phenomenal trap
    public static final int MOONLIGHT_SCAN_FACE = 0x29e; // Set to 1 after scanning the face of highest rank
    public static final int TRUE_SHRINE_TENTACLE = 0x2b7; // Set to 1 after hitting the tentacle to move it out of the way on the path to the Mother ankh
    public static final int GATE_OF_TIME_FAIRY_UNKNOWN = 0x2b8; // Presumably related to puzzle reset conversations for 8bit fairy
    public static final int DIMENSIONAL_PUZZLE_ANGEL_SHIELD = 0x2c1; // 0 > 1 when activated by the other two daises being pressed, 1 > 2 when weapon cover is gone
    public static final int DIMENSIONAL_CHILDREN_PARITY = 0x2c2; // 0 for even number of Tiamat's children are killed, 1 for odd
    public static final int DIMENSIONAL_ANGEL_SHIELD_DAIS_LEFT = 0x2d2; // 0 > 1 when dais is pressed
    public static final int DIMENSIONAL_ANGEL_SHIELD_DAIS_RIGHT = 0x2d3; // 0 > 1 when activated by the other two daises being pressed
    public static final int SHRINE_FAIRY_BLOCK = 0x2d5; // Value 0 > 1 when spawned, 1 > 2 from talking to fairy queen, 2 > 3 for animation of removal
    public static final int SURFACE_TRANSFORM_WIND_HOWLING = 0x2e1; // Set to 1 when all 8 bosses are down, along with updates to 0x102 and others
    public static final short CONVERSATION_CANT_LEAVE = 0x2e4; // Set to 1 when entering a conversation that can't be interrupted, set back to 0 when it's okay to leave again.
    public static final int TRANSLATION_TABLETS_READ = 0x2e5; // Value += 1 for each translation tablet read.
    public static final int ANCIENT_LAMULANESE_LEARNED = 0x2ea; // Value 0 > 1 when ancient La-Mulanese has been learned.
    public static final int DIMENSIONAL_CHILDREN_DEAD = 0x2ec; // Value += 1 for each of Tiamat's children killed
    public static final int DEV_ROOM_COMBO = 0x348; // 0 when not equipped, 1 when equipped
    public static final int HT_UNLOCK_PROGRESS_EARLY = 0x34c; // Mulbruk conversations? (1=discover 2=talk 3=talk again 4=mouth 5=lots of statues like that)
    public static final int LAMP_OF_TIME_STATE = 0x34d; // 0 = empty lamp, 1 = full lamp
    public static final int GODDESS_STATUE_SHIELD_EXISTS = 0x34e; // Value 0 > 1 after Goddess statue has thrown its shield or you've left the screen after triggering the animation start
    public static final int MEDICINE_SOLVED = 0x34f; // Value 0 > 1 after reciting BIRTH/DEATH; allows re-doing the puzzle for alternate medicine color.
    public static final int SACRED_ORB_COUNT = 0x354; // Value += 1 for each orb collected; incremented via FlagTimer so may not reflect actual HP if cheating with orb duplication.
    public static final int ORB_COUNT_INCREMENTED_GUIDANCE = 0x355; // Value 0 > 1 when updating 0x355 to ensure it only happens once. One for each orb, spanning 0x355 through 0x35e
    public static final int RUIN_LADDER_NUWA = 0x369; // 0 > 1 from the dais, may go to 2 somehow?
    public static final int MULBRUK_BIKINI_ENDING = 0x36a; // Set via conversations, affects escape door/may be tested for Mulbruk bikini credits
    public static final int GODDESS_PIPES_SHORTCUT = 0x36d; // Goes to 2 when activated; 2 > 3 when gone
    public static final int ESCAPE = 0x382; // Value 0 > 1 when escape begins
    public static final int KEY_FAIRY_POINTS = 0x386; // 4 for key fairy in credits
    public static final int NARAMURA_SPOKEN = 0x388; // Value 0 > 1 after talking to Naramura
    public static final int DUPLEX_SPOKEN = 0x389; // Value 0 > 1 after talking to duplex
    public static final int SAMIERU_SPOKEN = 0x38a; // Value 0 > 1 after talking to Samieru
    public static final int GRAVEYARD_HOT_SPRING = 0x3b5; // Set to 1 after dropping the ice block into the hot spring
    public static final int EXTINCTION_BACKUP_JEWEL = 0x3b8; // Value 0 > 1 by timer after failing the Palenque fight, 1 > 2 when collecting the backup ankh jewel.
    public static final int HT_UNLOCK_CHAIN_PRIMARY = 0x3ba; // 1=hear about dracuets from mulbruk 2=dracuets in mouth 3=night 4=xelpud 5=fairy 6=upside down door 7=talked to dracuets inside door 8=talked to dracuets in gate of time 9=talked to dracuets in tower of the goddess
    public static final int HT_UNLOCKED = 0x3bb; // Value 0 > 1 when unlocked (normally at the end of the unlock sequence)
    public static final int BIRTH_GANESHA_SCANNED = 0x3c8; // Value 0 > 1 when scanning the mural of Ganesha in Chamber of Birth, activates 0x3e2 via FlagTimer to unblock the warp.
    public static final int KILL_FLAG = 0x3e9; // Instant death when set
    public static final int LAMP_STATION_UNKNOWN = 0x3ed; // Gets set on the Lemeza detector that lights Lamp of Time. Probably to despawn the flame graphic
    public static final int ESCAPE_TRIGGERED = 0x403; // Value 0 > 1 alongside triggering the escape timer and screenshake.
    public static final int SURFACE_RUINS_FRONT_DOOR_OPEN = 0x414; // For the crusher covering the transition from Surface to Gate of Guidance
    public static final int HT_SOLVED_ROOM33_PILLARS = 0x70d;
    public static final int HT_ROOM19_SPAWNS = 0x7dd; // Value 0 > 1 via FlagTimer based on the flag set when Mushussu is defeated; used for the test to spawn Mushussu and the tests for some enemies on that screen.
    public static final int HT_SOLVED_ROOM19 = 0x7e2; // Value 0 > 1 when Mushussu is defeated, used on the crushers.
    public static final int HT_SOLVED_ROOM30 = 0x7ed; // Value 0 > 1 via FlagTimer when all 3 daises in room 30 (witches) are pressed.
    public static final int HT_SOLVED_ROOM35 = 0x710; // Value 0 > 1 when reading the tablet in room 35, 1 > 2 via FlagTimer to also set a screen flag for sound effects, etc. Allows talking to Dracuet in the final room.
    public static final int HT_ROOM1_SHORTCUT_OPEN = 0x7f4; // Value 0 > 1 via FlagTimer when all 3 daises in room 30 (witches) are pressed.
    public static final int MOTHER_ANKH_JEWEL_RECOVERY = 0xad2; // Rando-specific; 0 > 1 after the sound effect has played to indicate that Shrine map was collected.
    public static final int RANDOMIZER_SAVE_LOADED = 0xad1; // Rando-specific; this gets set in the save file generated by randomizer, and no other way. It's checked as a means of killing the player if they didn't load their save file.
    public static final int WRONG_COLOR_MEDICINE = 0xad4; // Rando-specific; used on a FlagTimer that tests == 0 and assigns = 1 when setting medicine statue to match wrong-color medicine.
    public static final int GRAVEYARD_PUZZLE_TRAP_CHEST = 0xad8; // Rando-specific; for unlocking the trap chest at the bottom of Graveyard which normally spawns a hadouken turtle.
    public static final int MANTRAS_RECITED_COUNT = 0xae9; // Rando-specific; += 1 for each mantra recited
    public static final int MANTRAS_UNLOCKED = 0xaeb; // Rando-specific; 0 > 1 after talking to Giltoriyo to spawn all mantras
    public static final int SOUND_EFFECT_PLAYED_SHRINE_MAP = 0xaee; // Rando-specific; 0 > 1 after the sound effect has played to indicate that Shrine map was collected.
    public static final int ILLUSION_PUZZLE_COG_CHEST = 0xaef; // Rando-specific; replaces flag 0x23a to separate the mudmen spawning state from the puzzle to unlock the Cog of the Soul chest.
    public static final int MR_SLUSHFUND_CONVERSATION_PEPPER = 0xae0; // Rando-specific; tracks Pepper conversation for Mr. Slushfund

    public static final int XELPUD_CONVERSATION_GENERAL = 0x07c; // Assorted vanilla conversations. 1=met 2=immediately went back in  3=urgent 4=lonely 5=sorry 6=urgentforreal 7=legend 8=returned after mulbruk 9=sleepinggirl...
    public static final int XELPUD_CONVERSATION_MSX2 = 0x21d; // Value 0 > 1 when 4-boss item is available from Nebur, 1 > 2 after talking to Xelpud or buying the item
    public static final int XELPUD_CONVERSATION_TALISMAN_FOUND = 0xaec; // Value 0 > 1 via rando-added timer on Xelpud's screen when Talisman conversation is enabled; progress goes toward opening the Diary chest in Shrine of the Mother
    public static final int XELPUD_CONVERSATION_DIARY_FOUND = 0xaed; // Value 0 > 1 via rando-added timer on Xelpud's screen when Diary conversation is enabled
    public static final int XELPUD_CONVERSATION_INTRO = 0xad0; // Randomizer sets this flag after first conversation with Xelpud (where he normally gives xmailer and opens the other tents.

    public static final int MULBRUK_DOOR_UNSEALED = 0x18e; // 1=talked to 2=touched seal 3=talked to mulbruk again
    public static final int MULBRUK_CONVERSATION_AWAKE = 0x391;
    public static final int MULBRUK_CONVERSATIONS_EARLY = 0x079; // 1=unsealed 2=wedjet 3=left after wedjet 4 blah blah doesn't really matter
    public static final int MULBRUK_CONVERSATION_BOOK = 0x32a; // Value 0 > 1 via rando-added timer on Xelpud's screen when Diary conversation is enabled

    public static final int FAIRY_QUEEN_CONVERSATION_FAIRIES = 0x1f5; // Unlocking the ceiling in Buer's room (vanilla only; rando makes it breakable by default) with value 0 > 1; value goes to 2 after the conversation that unlocks fairies/fairy points

    // Ankh flags
    public static final int AMPHISBAENA_ANKH_PUZZLE = 0x133; // State of Amphisbaena ankh; 5 when the ankh spawns, 6 when Amphisbaena is dead
    public static final int SAKIT_ANKH_PUZZLE = 0x164; // State of Sakit ankh; 1 when the ankh spawns, 2 when Sakit is dead
    public static final int ELLMAC_ANKH_PUZZLE = 0x178; // State of Ellmac ankh; 1 when minecart is unlocked, 2-5 for pushing on each screen, 6 when Ellmac is dead
    public static final int BAHAMUT_ANKH_PUZZLE = 0x19f; // State of Bahamut ankh; 1 when ankh is spawned, 2 when Bahamut is dead
    public static final int VIY_ANKH_PUZZLE = 0x1b4; // State of Viy ankh; 1 when visiting the screen above Viy's ankh, 2 when Sakit is dead
    public static final int PALENQUE_ANKH_PUZZLE = 0x1c3; // State of Palenque ankh; 1 after placing the weight in the Extinction <> Birth teleport area, 2 when the Pochette Key mural is spawned, 3 after using Pochette Key, 4 when Palenque is dead
    public static final int BAPHOMET_ANKH_PUZZLE = 0x1e0; // State of Baphomet ankh; 1 for white witch, 2 when ankh is spawned, 3 when Baphomet is dead
    public static final int TIAMAT_ANKH_PUZZLE = 0x2ed; // State of Tiamat ankh; 1 when ankh is spawned, 2 when Tiamat is dead
    public static final int MOTHER_ANKH_PUZZLE = 0x2e0; // State of Mother ankh; 1 after placing the weight in True Shrine, 3 after seals are opened

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
    public static final int SKANDA_STATE = 0x2a6; // Value 0 > 1 from dance of life, 1 > 2 when Skanda dies
    public static final int USHUMGALLU_STATE = 0x2cc; // Value 0 > 1 when Ushumgallu dies, 1 > 2 via FlagTimer

    // Backside door unlock flags
    public static final int AMPHISBAENA_GATE_MIRROR_COVER = 0x15c;
    public static final int AMPHISBAENA_GATE_OPEN = 0x15d;
    public static final int SAKIT_GATE_MIRROR_COVER = 0x16d;
    public static final int SAKIT_GATE_OPEN = 0x16e;
    public static final int ELLMAC_GATE_MIRROR_COVER = 0x175;
    public static final int ELLMAC_GATE_OPEN = 0x176;
    public static final int BAHAMUT_GATE_MIRROR_COVER = 0x1bd;
    public static final int BAHAMUT_GATE_OPEN = 0x1be;
    public static final int VIY_GATE_MIRROR_COVER = 0x152;
    public static final int VIY_GATE_OPEN = 0x153;
    public static final int PALENQUE_GATE_MIRROR_COVER = 0x2b9;
    public static final int PALENQUE_GATE_OPEN = 0x1d0;
    public static final int BAPHOMET_GATE_MIRROR_COVER = 0x3b7;
    public static final int BAPHOMET_GATE_OPEN = 0x1c0;
    public static final int KEY_FAIRY_DOOR_UNLOCKED = 0x1c9;
    public static final int KEY_FAIRY_DOOR_UNLOCKED_V2 = 0x38c;

    // Item world flags - determine if you've collected an item; value is always 0 uncollected > 2 collected, although opening a chest may set it to 1 on occasion
    public static final int WF_KNIFE = 0x07f;
    public static final int WF_FLARE_GUN = 0x086;
    public static final int WF_ANGEL_SHIELD = 0x08d;
    public static final int WF_ANKH_JEWEL_GUIDANCE = 0x08e;
    public static final int WF_ANKH_JEWEL_MAUSOLEUM = 0x08f;
    public static final int WF_ANKH_JEWEL_SUN = 0x090;
    public static final int WF_ANKH_JEWEL_SPRING = 0x091;
    public static final int WF_ANKH_JEWEL_RUIN = 0x092;
    public static final int WF_ANKH_JEWEL_BIRTH = 0x093;
    public static final int WF_ANKH_JEWEL_TWIN = 0x094;
    public static final int WF_ANKH_JEWEL_DIMENSIONAL = 0x095;
    public static final int WF_MINI_DOLL = 0x098;
    public static final int WF_VANILLA_DRAGON_BONE = 0x09d; // Vanilla-specific for Dragon Bone, but didn't work correctly
    public static final int WF_TALISMAN = 0x0a4;
    public static final int WF_SHELL_HORN = 0x0a7;
    public static final int WF_ISIS_PENDANT = 0x0aa;
    public static final int WF_CRUCIFIX = 0x0ab;
    public static final int WF_BRONZE_MIRROR = 0x0ae;
    public static final int WF_PLANE_MODEL = 0x0b4;
    public static final int WF_PHILOSOPHERS_OCARINA = 0x0b5;
    public static final int WF_FEATHER = 0x0b6;
    public static final int WF_SACRED_ORB_GUIDANCE = 0x0c7;
    public static final int WF_SACRED_ORB_SUN = 0x0ca;
    public static final int WF_MAP_SURFACE = 0x0d1;
    public static final int WF_MAP_GUIDANCE = 0x0d2;
    public static final int WF_MAP_MAUSOLEUM = 0x0d3;
    public static final int WF_MAP_SUN = 0x0d4;
    public static final int WF_MAP_SPRING = 0x0d5;
    public static final int WF_MAP_INFERNO = 0x0d6;
    public static final int WF_MAP_EXTINCTION = 0x0d7;
    public static final int WF_MAP_TWIN = 0x0d8;
    public static final int WF_MAP_ENDLESS = 0x0d9;
    public static final int WF_MAP_SHRINE = 0x0da;
    public static final int WF_MAP_ILLUSION = 0x0db;
    public static final int WF_MAP_GRAVEYARD = 0x0dc;
    public static final int WF_MAP_MOONLIGHT = 0x0dd;
    public static final int WF_MAP_GODDESS = 0x0de;
    public static final int WF_MAP_RUIN = 0x0df;
    public static final int WF_MAP_BIRTH = 0x0e0;
    public static final int WF_MAP_DIMENSIONAL = 0x0e1;
    public static final int WF_SOFTWARE_XMAILER = 0x0e3;
    public static final int WF_SOFTWARE_YAGOSTR = 0x0e5;
    public static final int WF_SOFTWARE_MANTRA = 0x0ea;
    public static final int WF_SOFTWARE_EMUSIC = 0x0eb;
    public static final int WF_SOFTWARE_BEOLAMU = 0x0ec;
    public static final int WF_SOFTWARE_MEKURI = 0x0f1;
    public static final int WF_TREASURES = 0x103;
    public static final int WF_DIARY = 0x104;
    public static final int WF_MULANA_TALISMAN = 0x105;
    public static final int WF_PROVOCATIVE_BATHING_SUIT = 0x106;
    public static final int WF_MATERNITY_STATUE = 0x10b;
    public static final int WF_SOFTWARE_DEATHV = 0x14f;
    public static final int WF_COIN_SUN = 0x18b;
    public static final int WF_MSX2 = 0x2e6;
    public static final int WF_PEPPER = 0xa8e;
    public static final int WF_BOOK_OF_THE_DEAD = 0xa8f;
    public static final int WF_ANCHOR = 0xa92;
    public static final int WF_COIN_SURFACE_SEAL = 0xa93; // Randomizer-specific for tracking the coin chest check on the Surface near Argus (requiring Life Seal)
    public static final int WF_TRAP_GRAVEYARD = 0xad9; // Randomizer-specific for tracking the trap check from Graveyard (hadouken turtle).
    public static final int WF_TRAP_ILLUSION = 0xada; // Randomizer-specific for tracking the trap check from Illusion (exploding chest).
    public static final int WF_TRAP_INFERNO = 0xadb; // Randomizer-specific for tracking the trap check from Inferno (fake Sacred Orb).
    public static final int WF_TRAP_TWIN = 0xadc; // Randomizer-specific for tracking the trap check from Twin Labyrinths (fake Ankh Jewel).

    // Chest puzzle flags
    public static final int SUN_CHEST_MAP = 0x177;
    public static final int SUN_CHEST_ISIS_PENDANT = 0x17b;
    public static final int SUN_CHEST_BRONZE_MIRROR = 0x181;
    public static final int SUN_CHEST_COIN = 0x185;
    public static final int SUN_CHEST_SACRED_ORB = 0x18c;
    public static final int SUN_CHEST_ANKH_JEWEL = 0x18f;
    public static final int SHRINE_CHEST_MAP = 0x20e;

    // Removed item flags
    public static final int REMOVED_MAP_SURFACE = 0xa94;
    public static final int REMOVED_MAP_GUIDANCE = 0xa95;
    public static final int REMOVED_MAP_MAUSOLEUM = 0xa96;
    public static final int REMOVED_MAP_SUN = 0xa97;
    public static final int REMOVED_MAP_SPRING = 0xa98;
    public static final int REMOVED_MAP_INFERNO = 0xa99;
    public static final int REMOVED_MAP_EXTINCTION = 0xa9a;
    public static final int REMOVED_MAP_TWIN = 0xa9b;
    public static final int REMOVED_MAP_ENDLESS = 0xa9c;
    public static final int REMOVED_MAP_ILLUSION = 0xa9d;
    public static final int REMOVED_MAP_GRAVEYARD = 0xa9e;
    public static final int REMOVED_MAP_MOONLIGHT = 0xa9f;
    public static final int REMOVED_MAP_GODDESS = 0xaa0;
    public static final int REMOVED_MAP_RUIN = 0xaa1;
    public static final int REMOVED_MAP_BIRTH = 0xaa2;
    public static final int REMOVED_MAP_DIMENSIONAL = 0xaa3;

    // Mantra recited flags
    public static final int MANTRA_FINAL = 0x124; // In vanilla, this is just for reciting LAMULANA, but since randomizer allows out of order mantras, LAMULANA is now a different flag, and this is mostly just set to 4 for empowering the Key Sword after enough mantras have been recited.
    public static final int MANTRA_ABUTO = 0x125;
    public static final int MANTRA_WEDJET = 0x126;
    public static final int MANTRA_BAHRUN = 0x127;
    public static final int MANTRA_VIY = 0x128;
    public static final int MANTRA_MU = 0x129;
    public static final int MANTRA_SABBAT = 0x12a;
    public static final int MANTRA_MARDUK = 0x12b;
    public static final int MANTRA_LAMULANA = 0xaea; // Randomizer uses this to track LAMULANA recited, to allow out of order mantras without empowering Key Sword early.

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
    public static final int MAIL_07 = 0x2f3;
    public static final int MAIL_18 = 0x2fe;
    public static final int MAIL_25 = 0x305;
    public static final int MAIL_41 = 0x315;
    public static final int MAIL_43 = 0x317;

    // Screen flags
    public static final int SCREEN_FLAG_0 = 0x000;
    public static final int SCREEN_FLAG_1 = 0x001;
    public static final int SCREEN_FLAG_2 = 0x002;
    public static final int SCREEN_FLAG_3 = 0x003;
    public static final int SCREEN_FLAG_4 = 0x004;
    public static final int SCREEN_FLAG_5 = 0x005;
    public static final int SCREEN_FLAG_6 = 0x006;
    public static final int SCREEN_FLAG_7 = 0x007;
    public static final int SCREEN_FLAG_8 = 0x008;
    public static final int SCREEN_FLAG_9 = 0x009;
    public static final int SCREEN_FLAG_A = 0x00a; // Commonly used for retribution
    public static final int SCREEN_FLAG_B = 0x00b; // Commonly used for Shell Horn sounds
    public static final int SCREEN_FLAG_C = 0x00c;
    public static final int SCREEN_FLAG_D = 0x00d;
    public static final int SCREEN_FLAG_E = 0x00e;
    public static final int SCREEN_FLAG_F = 0x00f;
    public static final int SCREEN_FLAG_10 = 0x010;
    public static final int SCREEN_FLAG_11 = 0x011;
    public static final int SCREEN_FLAG_12 = 0x012;
    public static final int SCREEN_FLAG_13 = 0x013;
    public static final int SCREEN_FLAG_14 = 0x014;
    public static final int SCREEN_FLAG_15 = 0x015;
    public static final int SCREEN_FLAG_16 = 0x016;
    public static final int SCREEN_FLAG_17 = 0x017;
    public static final int SCREEN_FLAG_18 = 0x018;
    public static final int SCREEN_FLAG_19 = 0x019;
    public static final int SCREEN_FLAG_1A = 0x01a;
    public static final int SCREEN_FLAG_1B = 0x01b;
    public static final int SCREEN_FLAG_1C = 0x01c;
    public static final int SCREEN_FLAG_1D = 0x01d;
    public static final int SCREEN_FLAG_1E = 0x01e;
    public static final int SCREEN_FLAG_1F = 0x01f;
    public static final int SCREEN_FLAG_20 = 0x020;
    public static final int SCREEN_FLAG_21 = 0x021;
    public static final int SCREEN_FLAG_22 = 0x022;
    public static final int SCREEN_FLAG_23 = 0x023;
    public static final int SCREEN_FLAG_24 = 0x024;
    public static final int SCREEN_FLAG_25 = 0x025;
    public static final int SCREEN_FLAG_26 = 0x026;
    public static final int SCREEN_FLAG_27 = 0x027;
    public static final int SCREEN_FLAG_28 = 0x028;
    public static final int SCREEN_FLAG_29 = 0x029;
    public static final int SCREEN_FLAG_2A = 0x02a; // Used in randomizer, for Shrine map sound effect
    public static final short SCREEN_FLAG_2B = 0x02b; // Used in randomizer, for fake item and/or trap screen flag for floating items (spawns bats)
    public static final int SCREEN_FLAG_2C = 0x02c;
    public static final int SCREEN_FLAG_2D = 0x02d;
    public static final short SCREEN_FLAG_2E = 0x02e; // Used in randomizer, for fake item and/or trap screen flag for chests (spawns bats); also doubles as screen flag for one of the crushers on Chain Whip puzzle; also used in Halloween 2019 on Mr. Slushfund's screen
    public static final int SCREEN_FLAG_2F = 0x02f; // Used in randomizer, for the other crusher on Chain Whip puzzle
    public static final int SCREEN_FLAG_30 = 0x030;
    public static final int SCREEN_FLAG_31 = 0x031;

    public static final int ROOM_FLAG_35 = 0x035; // Used by fake Ankh Jewel trap in Twin Labyrinths for non-random traps.
    public static final int ROOM_FLAG_37 = 0x037; // Used for a weight door during the escape.
    public static final int ROOM_FLAG_3C = 0x03c; // Used by Graveyard trap chest for non-random traps.
    public static final int ROOM_FLAG_40 = 0x040; // Set by an arg on Tiamat's ankh when the fight starts
    public static final int ROOM_FLAG_45 = 0x045; // Used for a weight door during the escape.

    // Flags used in vanilla reclaimed for randomizer
    public static final int XELPUD_TENT_OPEN = 0x21c; // For the two-second delay between game start and Xelpud's tent being open
    public static final int XELPUD_READY_TO_TALK = 0x3b6; // Making sure you can't actually talk to Xelpud in his basic conversation if the tent is closed.

    // Fools 2019

    // Halloween 2019
    public static final int CUSTOM_HALLOWEEN_MULBRUK_CONVERSATION = 0xaac; // 0 = not spoken, 1 = did intro, 2 = ready to talk about HT, 3 = HT unlocked
    public static final int CUSTOM_HALLOWEEN_H4 = 0xaad; // Used to track progress for escaping H4. Can't use a screen/room flag because of the abnormal transitions.
    public static final int CUSTOM_SECRET_SHOP = 0xacd; // Unlocks secret shop on the Surface for buying Scriptures / Perfume / Money
    public static final int CUSTOM_HALLOWEEN_MULBRUK_HINT = 0xace; // Allows Mulbruk to cycle through NPC hints.
    public static final int CUSTOM_HALLOWEEN_NPC_COUNT = 0xaca; // Incremented when visiting NPCs
    public static final int CUSTOM_HALLOWEEN_FINAL_DRACUET = 0xaae; // Incremented when visiting NPCs

    // Fools 2020
    public static final int CUSTOM_WF_FAKE_FEATHER = 0xacf; // Triggers some platforms and pots to spawn.
    public static final short CUSTOM_WF_SPAULDER = 0xaca; // Triggers Spaulder to spawn.

    // Fools 2021
    public static final int CUSTOM_ESCAPE_TIMER_STATE = 0xaba; // Incremented when the timer runs out
    public static final int CUSTOM_XMAILER_RECEIVED = 0xabb; // Incremented when given xmailer after the timer runs out
    public static final int CUSTOM_FOOLS2021_TREASURY_SPAULDER = 0xabc; // Spaulder "reward" for doing treasury zip
    public static final int CUSTOM_FOOLS2021_ENDLESS_5F_SPAULDER = 0xabd; // Spaulder "reward" at the bottom of Endless Corridor during the escape sequence.
    public static final int CUSTOM_FOOLS2021_GRAIL_GUIDANCE = 0xaaa; // Grail flag for Gate of Guidance warp, to avoid requiring the location for empowered grail.
    public static final int CUSTOM_FOOLS2021_GRAIL_MAUSOLEUM = 0xaab; // Grail flag for Mausoleum of the Giants warp, to avoid requiring the location for empowered grail.
    public static final int CUSTOM_FOOLS2021_GRAIL_SUN = 0xaac; // Grail flag for Temple of the Sun warp, to avoid requiring the location for empowered grail.
    public static final int CUSTOM_FOOLS2021_GRAIL_SPRING = 0xaad; // Grail flag for Spring in the Sky warp, to avoid requiring the location for empowered grail.
    public static final int CUSTOM_FOOLS2021_GRAIL_INFERNO = 0xaae; // Grail flag for Inferno Cavern warp, to avoid requiring the location for empowered grail.
    public static final int CUSTOM_FOOLS2021_GRAIL_EXTINCTION = 0xaaf; // Grail flag for Chamber of Extinction warp, to avoid requiring the location for empowered grail.
    public static final int CUSTOM_FOOLS2021_GRAIL_TWIN_FRONT = 0xab0; // Grail flag for Twin Labyrinths frontside warp, to avoid requiring the location for empowered grail.
    public static final int CUSTOM_FOOLS2021_GRAIL_ENDLESS = 0xab1; // Grail flag for Endless Corridor warp, to avoid requiring the location for empowered grail.
    public static final int CUSTOM_FOOLS2021_GRAIL_ILLUSION = 0xab2; // Grail flag for Gate of Illusion warp, to avoid requiring the location for empowered grail.
    public static final int CUSTOM_FOOLS2021_GRAIL_GRAVEYARD = 0xab3; // Grail flag for Graveyard of the Giants warp, to avoid requiring the location for empowered grail.
    public static final int CUSTOM_FOOLS2021_GRAIL_MOONLIGHT = 0xab4; // Grail flag for Temple of Moonlight warp, to avoid requiring the location for empowered grail.
    public static final int CUSTOM_FOOLS2021_GRAIL_GODDESS = 0xab5; // Grail flag for Tower of the Goddess warp, to avoid requiring the location for empowered grail.
    public static final int CUSTOM_FOOLS2021_GRAIL_RUIN = 0xab6; // Grail flag for Tower of Ruin warp, to avoid requiring the location for empowered grail.
    public static final int CUSTOM_FOOLS2021_GRAIL_BIRTH = 0xab7; // Grail flag for Chamber of Birth warp, to avoid requiring the location for empowered grail.
    public static final int CUSTOM_FOOLS2021_GRAIL_TWIN_BACK = 0xab8; // Grail flag for Twin Labyrinths backside warp, to avoid requiring the location for empowered grail.
    public static final int CUSTOM_FOOLS2021_GRAIL_DIMENSIONAL = 0xab9; // Grail flag for Dimensional Corridor warp, to avoid requiring the location for empowered grail.

    public static int getAnkhJewelFlag(int zoneIndex) {
        if(zoneIndex == ZoneConstants.GUIDANCE) {
            return WF_ANKH_JEWEL_GUIDANCE;
        }
        if(zoneIndex == ZoneConstants.MAUSOLEUM) {
            return WF_ANKH_JEWEL_MAUSOLEUM;
        }
        if(zoneIndex == ZoneConstants.SUN) {
            return WF_ANKH_JEWEL_SUN;
        }
        if(zoneIndex == ZoneConstants.SPRING) {
            return WF_ANKH_JEWEL_SPRING;
        }
        if(zoneIndex == ZoneConstants.INFERNO) {
            return WF_ANKH_JEWEL_RUIN;
        }
        if(zoneIndex == ZoneConstants.EXTINCTION) {
            return WF_ANKH_JEWEL_BIRTH;
        }
        if(zoneIndex == ZoneConstants.TWIN_FRONT) {
            return WF_ANKH_JEWEL_TWIN;
        }
        if(zoneIndex == ZoneConstants.DIMENSIONAL) {
            return WF_ANKH_JEWEL_DIMENSIONAL;
        }
        return 0;
    }

    public static Integer getBossFlag(Integer bossNumber) {
        if(bossNumber == null) {
            return null;
        }
        if(bossNumber == 1) {
            return AMPHISBAENA_STATE;
        }
        if(bossNumber == 2) {
            return SAKIT_STATE;
        }
        if(bossNumber == 3) {
            return ELLMAC_STATE;
        }
        if(bossNumber == 4) {
            return BAHAMUT_STATE;
        }
        if(bossNumber == 5) {
            return VIY_STATE;
        }
        if(bossNumber == 6) {
            return PALENQUE_STATE;
        }
        if(bossNumber == 7) {
            return BAPHOMET_STATE;
        }
//        if(bossNumber == 8) {
//            return TIAMAT_STATE;
//        }
        if(bossNumber == 9) {
            return KEY_FAIRY_DOOR_UNLOCKED;
        }
        return null;
    }

    public static int getMantraFlag(int mantraNumber) {
        if(mantraNumber == MantraConstants.MARDUK) {
            return MANTRA_MARDUK;
        }
        if(mantraNumber == MantraConstants.SABBAT) {
            return MANTRA_SABBAT;
        }
        if(mantraNumber == MantraConstants.MU) {
            return MANTRA_MU;
        }
        if(mantraNumber == MantraConstants.VIY) {
            return MANTRA_VIY;
        }
        if(mantraNumber == MantraConstants.BAHRUN) {
            return MANTRA_BAHRUN;
        }
        if(mantraNumber == MantraConstants.WEDJET) {
            return MANTRA_WEDJET;
        }
        if(mantraNumber == MantraConstants.ABUTO) {
            return MANTRA_ABUTO;
        }
        if(mantraNumber == MantraConstants.LAMULANA) {
            return MANTRA_LAMULANA;
        }
        return 0;
    }

    public static int getNpcConversationFlag(int conversationBlockNumber) {
        if(conversationBlockNumber == BlockConstants.Master_Hiner) {
            return 0xac9;
        }
        if(conversationBlockNumber == BlockConstants.Master_Moger) {
            return 0xac8;
        }
        if(conversationBlockNumber == BlockConstants.Master_FormerMekuriMaster_Mekuri) {
            return 0xac7;
        }
        if(conversationBlockNumber == BlockConstants.Master_PriestZarnac) {
            return 0xac6;
        }
        if(conversationBlockNumber == BlockConstants.Master_PriestXanado) {
            return 0xac5;
        }
        if(conversationBlockNumber == BlockConstants.Master_PhilosopherGiltoriyo) {
            return 0xac4;
        }
        if(conversationBlockNumber == BlockConstants.Master_PriestHidlyda) {
            return 0xac3;
        }
        if(conversationBlockNumber == BlockConstants.Master_PriestRomancis) {
            return 0xac2;
        }
        if(conversationBlockNumber == BlockConstants.Master_PriestAramo) {
            return 0xac1;
        }
        if(conversationBlockNumber == BlockConstants.Master_PriestTriton) {
            return 0xac0;
        }
        if(conversationBlockNumber == BlockConstants.Master_PriestJaguarfiv) {
            return 0xabf;
        }
        if(conversationBlockNumber == BlockConstants.Master_FairyQueen_RequestPendant) {
            return FlagConstants.FAIRY_QUEEN_CONVERSATION_FAIRIES;
        }
        if(conversationBlockNumber == BlockConstants.Master_MrSlushfund_Pepper) {
            return 0xabe;
        }
        if(conversationBlockNumber == BlockConstants.Master_PriestAlest) {
            return 0xabd;
        }
        if(conversationBlockNumber == BlockConstants.Master_StrayFairy) {
            return 0xabc;
        }
        if(conversationBlockNumber == BlockConstants.Master_GiantThexde) {
            return 0xabb;
        }
        if(conversationBlockNumber == BlockConstants.Master_PhilosopherAlsedana) {
            return 0xaba;
        }
        if(conversationBlockNumber == BlockConstants.Master_PhilosopherSamaranta) {
            return 0xab9;
        }
        if(conversationBlockNumber == BlockConstants.Master_PriestLaydoc) {
            return 0xab8;
        }
        if(conversationBlockNumber == BlockConstants.Master_PriestAshgine) {
            return 0xab7;
        }
        if(conversationBlockNumber == BlockConstants.Master_PhilosopherFobos_Ladder) {
            return 0xab6;
        }
        if(conversationBlockNumber == BlockConstants.Master_8BitElder) {
            return 0xab5;
        }
        if(conversationBlockNumber == BlockConstants.Master_duplex) {
            return 0xab4;
        }
        if(conversationBlockNumber == BlockConstants.Master_Samieru) {
            return 0xab3;
        }
        if(conversationBlockNumber == BlockConstants.Master_Naramura) {
            return 0xab2;
        }
        if(conversationBlockNumber == BlockConstants.Master_8BitFairy) {
            return 0xab1;
        }
        if(conversationBlockNumber == BlockConstants.Master_PriestMadomono) {
            return 0xab0;
        }
        if(conversationBlockNumber == BlockConstants.Master_PriestGailious) {
            return 0xaaf;
        }
        if(conversationBlockNumber == BlockConstants.Master_Fairy_NightSurface) {
            return 0xaab;
        }
        return 0;
    }

    private FlagConstants() { }
}
