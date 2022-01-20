package lmr.randomizer.util;

import java.util.Arrays;
import java.util.List;

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
    public static final int SURFACE_PUZZLE_WATERFALL_WALL_SACRED_ORB = 0x14b; // Value 0 > 1 when the wall/Hitbox is broken for the wall that has bats behind it, also used as the puzzle flag for the chest
    public static final int SURFACE_UNDERPATH_VISIBLE = 0x14c; // Value 0 > 1 by Lemeza detector near the transitions into the Surface underpath, allowing you to navigate the area properly. One of the two detectors is added by randomizer; the other is vanilla.
    public static final int SURFACE_PUZZLE_SEAL_COIN_CHEST = 0x14d; // In vanilla, this is for opening the seal and opening the chest. In randomizer, the chest uses a different world flag, but this remains as the puzzle flag.
    public static final int SURFACE_WATERFALL_WALL_BATS = 0x151; // Value 0 > 1 when the wall/Hitbox is broken for the wall that has bats behind it, 1 > 2 as the wall cover despawns and bats spawn
    public static final int GUIDANCE_BATS_KILLED_COUNT = 0x159; // Value += 1 every time a bat is killed in Guidance.
    public static final int MAUSOLEUM_PUZZLE_ORB_CHEST = 0x165; // Value 0 > 1 when placing a weight at the proper giant's foot, unlocks the Mausoleum Sacred Orb chest
    public static final int HARDMODE = 0x16a; // Value 0 > 2 when hard mode is triggered.
    public static final int SPHINX_DESTROYED = 0x173; // Value 5 = destroyed
    public static final int SUN_GEYSER = 0x174; // Crusher; 1 = ascending; 2 = seal activated and now receding; 3 = gone
    public static final int SUN_WATCHTOWER_LIGHTS = 0x179; // 1,2=triggered 3=seen
    public static final int SPHINX_DESTROYED_V2 = 0x17d; // Value 0 > 1 when 0x173 set to 5, not sure why both flags exist
    public static final int SUN_PYRAMID_SUN_FALLEN = 0x17e; // Set from hitting the hitbox to make the Sun fall; incremented when the Sun actually falls.
    public static final int SUN_FLOODED = 0x180; // Value 1 = flooded and spawns waterfalls; waterfalls are gone
    public static final int SUN_MAP_CHEST_LADDER_DESPAWNED = 0x183; // Used to despawn the ladder for the Sun map chest in vanilla // todo: can this be claimed for something?
    public static final int SUN_UNSEALED_EXTINCTION = 0x187; // Set to 1 when triggering Seal: O3
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
    public static final int TWINS_POISON_TIMER = 0x1d7; // 1=counting down, 2=fatal in the twins race rooms
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
    public static final int MOONLIGHT_HIDDEN_TABLET_COLLAPSING_FLOOR = 0x257; // Value 0 > 1 when the floor is broken
    public static final int GRAVEYARD_BOMBABLE_WALL = 0x253; // Value 0 > 1 when the wall is broken
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
    public static final int BEELZEBUB_STATE = 0x2df;
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
    public static final int BIRTH_MOVING_PLATFORM_NEAR_SKANDA = 0x3c0; // Value 0 > 1 when placing the weight next to the Dimensional Key wall, causing the MovingPlatform to start moving left to right.
    public static final int BIRTH_GANESHA_SCANNED = 0x3c8; // Value 0 > 1 when scanning the mural of Ganesha in Chamber of Birth, activates 0x3e2 via FlagTimer to unblock the warp.
    public static final int KILL_FLAG = 0x3e9; // Instant death when set
    public static final int LAMP_STATION_UNKNOWN = 0x3ed; // Gets set on the Lemeza detector that lights Lamp of Time. Probably to despawn the flame graphic
    public static final int ESCAPE_TRIGGERED = 0x403; // Value 0 > 1 alongside triggering the escape timer and screenshake.
    public static final int PALENQUE_SMALL_MURAL_ANIMATION_TRIGGERED = 0x40e; // Value 0 > 1 when Palenque's small mural is triggered to animate sliding into place.
    public static final int SURFACE_RUINS_FRONT_DOOR_OPEN = 0x414; // For the crusher covering the transition from Surface to Gate of Guidance
    public static final int MAUSOLEUM_HARDMODE_SKULL_ANIMATION = 0x420; // Goes 0 > 1 by timer when HARDMODE (16a) set to 2, sets screen flag 0x28, and goes 1 > 2 by timer 40 frames later (after animation). Tested on RoomSpawner animation for == 1
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
    public static final int MULBRUK_CONVERSATION_BOOK = 0x32a; // Value 0 > 1 via Book of the Dead conversation.
    public static final int MULBRUK_CONVERSATION_HT = 0x34a; // Nothing sets this value; it's been theorized that it represents owning the DLC on the Wii

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
    public static final int WF_KEY_SWORD = 0x080;
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
    public static final int WF_HAND_SCANNER = 0x096;
    public static final int WF_ANKH_JEWEL_EXTRA = 0xad6;
    public static final int WF_DJED_PILLAR = 0x097;
    public static final int WF_MINI_DOLL = 0x098;
    public static final int WF_MAGATAMA_JEWEL = 0x099;
    public static final int WF_COG_OF_THE_SOUL = 0x09a;
    public static final int WF_POCHETTE_KEY = 0x09c;
    public static final int WF_VANILLA_DRAGON_BONE = 0x09d; // Vanilla-specific for Dragon Bone, but didn't work correctly
    public static final int WF_CRYSTAL_SKULL = 0x09e;
    public static final int WF_VESSEL = 0x09f;
    public static final int WF_WOMAN_STATUE = 0x0a1;
    public static final int WF_KEY_OF_ETERNITY = 0x0a2;
    public static final int WF_SERPENT_STAFF = 0x0a3;
    public static final int WF_TALISMAN = 0x0a4;
    public static final int WF_SHELL_HORN = 0x0a7;
    public static final int WF_GLOVE = 0x0a8;
    public static final int WF_HOLY_GRAIL = 0x0a9;
    public static final int WF_ISIS_PENDANT = 0x0aa;
    public static final int WF_CRUCIFIX = 0x0ab;
    public static final int WF_GRAPPLE_CLAW = 0x0ad;
    public static final int WF_BRONZE_MIRROR = 0x0ae;
    public static final int WF_EYE_OF_TRUTH = 0x0af;
    public static final int WF_RING = 0x0b0;
    public static final int WF_SCALESPHERE = 0x0b1;
    public static final int WF_GAUNTLET = 0x0b2;
    public static final int WF_PLANE_MODEL = 0x0b4;
    public static final int WF_PHILOSOPHERS_OCARINA = 0x0b5;
    public static final int WF_FEATHER = 0x0b6;
    public static final int WF_FAIRY_CLOTHES = 0x0b8;
    public static final int WF_SCRIPTURES = 0x0b9;
    public static final int WF_FRUIT_OF_EDEN = 0x0bb;
    public static final int WF_TWIN_STATUE = 0x0bc;
    public static final int WF_PERFUME = 0x0be;
    public static final int WF_SPAULDER = 0x0bf;
    public static final int WF_DIMENSIONAL_KEY = 0x0c0;
    public static final int WF_ICE_CAPE = 0x0c1;
    public static final int WF_ORIGIN_SEAL = 0x0c2;
    public static final int WF_BIRTH_SEAL = 0x0c3;
    public static final int WF_LIFE_SEAL = 0x0c4;
    public static final int WF_DEATH_SEAL = 0x0c5;
    public static final int WF_SACRED_ORB_GUIDANCE = 0x0c7;
    public static final int WF_SACRED_ORB_SURFACE = 0x0c8;
    public static final int WF_SACRED_ORB_MAUSOLEUM = 0x0c9;
    public static final int WF_SACRED_ORB_SUN = 0x0ca;
    public static final int WF_SACRED_ORB_SPRING = 0x0cb;
    public static final int WF_SACRED_ORB_EXTINCTION = 0x0cc;
    public static final int WF_SACRED_ORB_TWIN = 0x0cd;
    public static final int WF_SACRED_ORB_SHRINE = 0x0ce;
    public static final int WF_SACRED_ORB_RUIN = 0x0cf;
    public static final int WF_SACRED_ORB_DIMENSIONAL = 0x0d0;
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
    public static final int WF_SOFTWARE_BOUNCE = 0x0f2;
    public static final int WF_SOFTWARE_MIRAI = 0x0f4;
    public static final int WF_SOFTWARE_LAMULANA = 0x0f5;
    public static final int WF_TREASURES = 0x103;
    public static final int WF_DIARY = 0x104;
    public static final int WF_MULANA_TALISMAN = 0x105;
    public static final int WF_PROVOCATIVE_BATHING_SUIT = 0x106;
    public static final int WF_MATERNITY_STATUE = 0x10b;
    public static final int WF_COIN_SURFACE_GUIDANCE_TWO = 0x138;
    public static final int WF_COIN_SURFACE_GUIDANCE_ONE = 0x13b;
    public static final int WF_COIN_SURFACE_GUIDANCE_TRAP = 0x13c;
    public static final int WF_SOFTWARE_DEATHV = 0x14f;
    public static final int WF_COIN_SURFACE_RUIN_PATH = 0x155;
    public static final int WF_COIN_SURFACE_WATERFALL = 0x156;
    public static final int WF_COIN_SURFACE_MAUSOLEUM = 0x166;
    public static final int WF_COIN_SUN = 0x18b;
    public static final int WF_COIN_SPRING = 0x1a2;
    public static final int WF_COIN_INFERNO_LAVA = 0x1ab;
    public static final int WF_COIN_INFERNO_SPIKES = 0x1ba;
    public static final int WF_COIN_TWIN_LOWER = 0x1ec;
    public static final int WF_COIN_TWIN_WITCHES = 0x1ee;
    public static final int WF_COIN_ENDLESS = 0x203;
    public static final int WF_COIN_SHRINE = 0x216;
    public static final int WF_COIN_ILLUSION_SPIKES = 0x22c;
    public static final int WF_COIN_ILLUSION_KATANA = 0x233;
    public static final int WF_COIN_GRAVEYARD = 0x242;
    public static final int WF_COIN_MOONLIGHT = 0x25a;
    public static final int WF_COIN_GODDESS_FAIRY = 0x27e;
    public static final int WF_COIN_GODDESS_SHIELD = 0x286;
    public static final int WF_COIN_BIRTH_NINJA = 0x2aa;
    public static final int WF_COIN_BIRTH_SOUTHEAST = 0x2b1;
    public static final int WF_COIN_BIRTH_DANCE = 0x2b3;
    public static final int WF_COIN_RUIN = 0x2bb;
    public static final int WF_COIN_DIMENSIONAL = 0x2bf;
    public static final int WF_MSX2 = 0x2e6;
    public static final int WF_COIN_TWIN_ESCAPE = 0x3fc;
    public static final int WF_COIN_EXTINCTION = 0x401;
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
    public static final int MAIL_00 = 0x2ee;
    public static final int MAIL_01 = 0x2ef;
    public static final int MAIL_02 = 0x349;
    public static final int MAIL_03 = 0x36e;
    public static final int MAIL_04 = 0x2f0;
    public static final int MAIL_05 = 0x2f1;
    public static final int MAIL_06 = 0x2f2;
    public static final int MAIL_07 = 0x2f3;
    public static final int MAIL_08 = 0x2f4;
    public static final int MAIL_09 = 0x2f5;
    public static final int MAIL_10 = 0x2f6;
    public static final int MAIL_11 = 0x2f7;
    public static final int MAIL_12 = 0x2f8;
    public static final int MAIL_13 = 0x2f9;
    public static final int MAIL_14 = 0x2fa;
    public static final int MAIL_15 = 0x2fb;
    public static final int MAIL_16 = 0x2fc;
    public static final int MAIL_17 = 0x2fd;
    public static final int MAIL_18 = 0x2fe;
    public static final int MAIL_19 = 0x2ff;
    public static final int MAIL_20 = 0x300;
    public static final int MAIL_21 = 0x301;
    public static final int MAIL_22 = 0x302;
    public static final int MAIL_23 = 0x303;
    public static final int MAIL_24 = 0x304;
    public static final int MAIL_25 = 0x305;
    public static final int MAIL_26 = 0x306;
    public static final int MAIL_27 = 0x307;
    public static final int MAIL_28 = 0x308;
    public static final int MAIL_29 = 0x309;
    public static final int MAIL_30 = 0x30a;
    public static final int MAIL_31 = 0x30b;
    public static final int MAIL_32 = 0x30c;
    public static final int MAIL_33 = 0x30d;
    public static final int MAIL_34 = 0x30e;
    public static final int MAIL_35 = 0x30f;
    public static final int MAIL_36 = 0x310;
    public static final int MAIL_37 = 0x311;
    public static final int MAIL_38 = 0x312;
    public static final int MAIL_39 = 0x313;
    public static final int MAIL_40 = 0x314;
    public static final int MAIL_41 = 0x315;
    public static final int MAIL_42 = 0x316;
    public static final int MAIL_43 = 0x317;
    public static final int MAIL_44 = 0x318;

    // Tablet glow flags
    public static final int TABLET_GLOW_GUIDANCE_ENTRANCE = 0x44c; // Tablet block 44; room 00-00-00
    public static final int TABLET_GLOW_GUIDANCE_OFFER_3_LIGHTS = 0x44e; // Tablet block 55; room 00-01-01
    public static final int TABLET_GLOW_GUIDANCE_HOLY_GROUND = 0x44f; // Tablet block 48; room 00-02-00
    public static final int TABLET_GLOW_GUIDANCE_PROVE_THINE_COURAGE = 0x450; // Tablet block 49; room 00-02-00
    public static final int TABLET_GLOW_GUIDANCE_CROSS_THE_SACRED_LAKE = 0x451; // Tablet block 63; room 00-02-01
    public static final int TABLET_GLOW_GUIDANCE_DEATH_AWAITS_THE_POWERLESS = 0x452; // Tablet block 43; room 00-03-01
    public static final int TABLET_GLOW_GUIDANCE_8_ANKHS = 0x453; // Tablet block 56; room 00-04-00
    public static final int TABLET_GLOW_GUIDANCE_PUSH_THE_WHITE_BOX = 0x454;; // Tablet block 550; room 00-04-02
    public static final int TABLET_GLOW_GUIDANCE_CHI_YOU = 0x456; // Tablet block 61; room 00-05-00
    public static final int TABLET_GLOW_GUIDANCE_IT_SHALL_CONNECT = 0x457; // Tablet block 71; room 00-06-00
    public static final int TABLET_GLOW_GUIDANCE_WATCHTOWER = 0x458; // Tablet block 67; room 00-07-01
    public static final int TABLET_GLOW_GUIDANCE_HOLY_GRAIL_MEMORIES = 0x459;; // Tablet block 65; room 00-08-00
    public static final int TABLET_GLOW_GUIDANCE_GUILD_DAMAGED = 0x45a; // Tablet block 47; room 00-09-00
    public static final int TABLET_GLOW_GUIDANCE_GIANTS_HISTORY = 0x45b; // Tablet block 57; room 00-09-00

    public static final int TABLET_GLOW_SURFACE_RUINS_ENTRANCE = 0x45d; // Tablet block 473; room 01-11-01

    public static final int TABLET_GLOW_MAUSOLEUM_ABUTO = 0x45f; // Tablet block 79; room 02-00-01
    public static final int TABLET_GLOW_MAUSOLEUM_GHOST_LORD_DAMAGED = 0x460; // Tablet block 85; room 02-01-00
    public static final int TABLET_GLOW_MAUSOLEUM_MIGELA = 0x461; // Tablet block 86; room 02-01-01
    public static final int TABLET_GLOW_MAUSOLEUM_THOU_ART_TINY = 0x462; // Tablet block 80; room 02-02-00
    public static final int TABLET_GLOW_MAUSOLEUM_LEDO = 0x463; // Tablet block 87; room 02-02-01
    public static final int TABLET_GLOW_MAUSOLEUM_RIBU = 0x465; // Tablet block 83; room 02-03-00
    public static final int TABLET_GLOW_MAUSOLEUM_ABUTO_JI_RIBU_SAKIT = 0x466; // Tablet block 92; room 02-04-00
    public static final int TABLET_GLOW_MAUSOLEUM_JI = 0x467; // Tablet block 89; room 02-05-00
    public static final int TABLET_GLOW_MAUSOLEUM_ZEBU = 0x468; // Tablet block 93; room 02-05-01
    public static final int TABLET_GLOW_MAUSOLEUM_NUMBERS = 0x469; // Tablet block 96; room 02-05-02
    public static final int TABLET_GLOW_MAUSOLEUM_BADO_MIGELA_LEDO_FUTO = 0x46a; // Tablet block 97; room 02-05-02
    public static final int TABLET_GLOW_MAUSOLEUM_BADO = 0x46c; // Tablet block 90; room 02-04-00
    public static final int TABLET_GLOW_MAUSOLEUM_MIGELA_LEFT_HANDED = 0x46d; // Tablet block 91; room 02-06-01
    public static final int TABLET_GLOW_MAUSOLEUM_RED_LIGHT_DAMAGED = 0x46e; // Tablet block 94; room 02-07-00
    public static final int TABLET_GLOW_MAUSOLEUM_ZEBU_UNABLE_TO_MOVE = 0x46f; // Tablet block 95; room 02-07-02
    public static final int TABLET_GLOW_MAUSOLEUM_NINE_BROTHERS = 0x470; // Tablet block 98; room 02-08-00
    public static final int TABLET_GLOW_MAUSOLEUM_SAKIT = 0x471; // Tablet block 99; room 02-08-02
    public static final int TABLET_GLOW_MAUSOLEUM_CELESTIAL_BODIES = 0x472; // Tablet block 76; room 02-09-00

    public static final int TABLET_GLOW_SUN_JUMP_INTO_THE_SUN = 0x473; // Tablet block 106; room 03-00-01
    public static final int TABLET_GLOW_SUN_STATUE_OF_THE_GIANT = 0x474; // Tablet block 62; room 03-01-00
    public static final int TABLET_GLOW_SUN_CAST_A_LIGHT = 0x475; // Tablet block 109; room 03-01-00
    public static final int TABLET_GLOW_SUN_AIM_AND_SHOOT = 0x477; // Tablet block 717; room 03-01-02
    public static final int TABLET_GLOW_SUN_CHALLENGE_THE_INFERNO_CAVERN = 0x479; // Tablet block 107; room 03-02-01
    public static final int TABLET_GLOW_SUN_FIRST_MANTRA = 0x47a; // Tablet block 114; room 03-03-00
    public static final int TABLET_GLOW_SUN_SECOND_MANTRA = 0x47c; // Tablet block 116; room 03-03-00
    public static final int TABLET_GLOW_SUN_THIRD_MANTRA = 0x47d; // Tablet block 117; room 03-03-00
    public static final int TABLET_GLOW_SUN_MANTRA_ORDER = 0x47e; // Tablet block 118; room 03-03-00
    public static final int TABLET_GLOW_SUN_FOURTH_MANTRA = 0x47f; // Tablet block 119; room 03-03-00
    public static final int TABLET_GLOW_SUN_FIFTH_MANTRA = 0x480; // Tablet block 120; room 03-03-00
    public static final int TABLET_GLOW_SUN_SEVENTH_MANTRA = 0x481; // Tablet block 121; room 03-03-00
    public static final int TABLET_GLOW_SUN_LAST_MANTRA = 0x482; // Tablet block 122; room 03-03-00
    public static final int TABLET_GLOW_SUN_MAN_WOMAN_WEAPON = 0x483; // Tablet block 123; room 03-04-00
    public static final int TABLET_GLOW_SUN_PATH_THAT_CONNECTS_TWO_PYRAMIDS = 0x484; // Tablet block 112; room 03-04-01
    public static final int TABLET_GLOW_SUN_MOON_GAZING_PIT = 0x485; // Tablet block 113; room 03-04-02
    public static final int TABLET_GLOW_SUN_THOSE_THAT_FLY_SHALL_BE_CAST_DOWN = 0x486; // Tablet block 127; room 03-04-03
    public static final int TABLET_GLOW_SUN_WOMAN_WITH_CHILD = 0x487; // Tablet block 124; room 03-04-04
    public static final int TABLET_GLOW_SUN_FILL_THIS_PLACE_WITH_WATER_DAMAGED = 0x488; // Tablet block 125; room 03-04-05
    public static final int TABLET_GLOW_SUN_NO_LIGHT_UP_AHEAD = 0x489; // Tablet block 126; room 03-05-00
    public static final int TABLET_GLOW_SUN_SOLVED_ALL_PUZZLES = 0x48a; // Tablet block 130; room 03-06-00
    public static final int TABLET_GLOW_SUN_MEDITATE_UNDER_WEDJET = 0x48b; // Tablet block 128; room 03-06-01
    public static final int TABLET_GLOW_SUN_POWER_OF_THE_TWINS = 0x48c; // Tablet block 129; room 03-07-00
    public static final int TABLET_GLOW_SUN_SCALESPHERE = 0x48d; // Tablet block 111; room 03-07-01

    public static final int TABLET_GLOW_SPRING_OPEN_THE_FLOODGATE = 0x48e; // Tablet block 140; room 04-00-02
    public static final int TABLET_GLOW_SPRING_FAIRY = 0x48f; // Tablet block 139; room 04-00-02
    public static final int TABLET_GLOW_SPRING_OANNES_FORM_OF_A_FISH = 0x492; // Tablet block 142; room 04-00-02
    public static final int TABLET_GLOW_SPRING_CHILD_WAS_BORN = 0x493; // Tablet block 53; room 04-05-00
    public static final int TABLET_GLOW_SPRING_COLLECT_THE_FOUR_SIGILS = 0x494; // Tablet block 144; room 04-05-01
    public static final int TABLET_GLOW_SPRING_SAIL_AWAY = 0x495; // Tablet block 143; room 04-06-00
    public static final int TABLET_GLOW_SPRING_WATER_MOVES_THE_TOWER_DAMAGED = 0x496; // Tablet block 145; room 04-07-00
    public static final int TABLET_GLOW_SPRING_MOTHERS_WRATH = 0x497; // Tablet block 146; room 04-07-00
    public static final int TABLET_GLOW_SPRING_ABOVE_THE_SUN = 0x498; // Tablet block 138; room 04-07-01
    public static final int TABLET_GLOW_SPRING_THOSE_SEEKING_EDEN = 0x499; // Tablet block 147; room 04-08-01

    public static final int TABLET_GLOW_INFERNO_SWING_YOUR_WEAPON = 0x49a; // Tablet block 474; room 05-00-00
    public static final int TABLET_GLOW_INFERNO_SNAKES_ARE_ENRAGED = 0x49b; // Tablet block 150; room 05-01-00
    public static final int TABLET_GLOW_INFERNO_STICK_TO_THE_WALLS = 0x49c; // Tablet block 155; room 05-02-00
    public static final int TABLET_GLOW_INFERNO_TREAD_A_DIFFERENT_PATH = 0x49d; // Tablet block 159; room 05-02-02
    public static final int TABLET_GLOW_INFERNO_PHILOSOPHERS_STONE_DAMAGED = 0x49e; // Tablet block 157; room 05-03-02
    public static final int TABLET_GLOW_INFERNO_ERR_JUST_ONCE = 0x49f; // Tablet block 156; room 05-03-02
    public static final int TABLET_GLOW_INFERNO_CLOAK_MADE_FROM_ICE_DAMAGED = 0x4a0; // Tablet block 160; room 05-05-00
    public static final int TABLET_GLOW_INFERNO_BOTTOM_OF_THIS_LAND = 0x4a1; // Tablet block 162; room 05-05-01
    public static final int TABLET_GLOW_INFERNO_THOSE_SEEKING_EDEN = 0x4a2; // Tablet block 163; room 05-07-01
    public static final int TABLET_GLOW_INFERNO_TUNE_OF_THINE_OCARINA = 0x4a3; // Tablet block 161; room 05-08-00
    public static final int TABLET_GLOW_INFERNO_LAST_JEWEL_LIES_WITHIN = 0x4a4; // Tablet block 183; room 05-09-01

    public static final int TABLET_GLOW_EXTINCTION_SEEK_THE_RIGHTEOUS_SPIRIT = 0x4a5; // Tablet block 171; room 06-00-01
    public static final int TABLET_GLOW_EXTINCTION_NUWA_SLEEPS = 0x4a6; // Tablet block 184; room 06-01-01
    public static final int TABLET_GLOW_EXTINCTION_LARGE_CRUCIFIX_SUMMON_PALENQUE = 0x4a7; // Tablet block 179; room 06-02-00
    public static final int TABLET_GLOW_EXTINCTION_LET_THE_LIGHT_BURN_HERE = 0x4a8; // Tablet block 175; room 06-02-00
    public static final int TABLET_GLOW_EXTINCTION_DRAGON_BONE = 0x4aa; // Tablet block 177; room 06-03-00
    public static final int TABLET_GLOW_EXTINCTION_SPRIGGAN_STATUE = 0x4ab; // Tablet block 176; room 06-03-01
    public static final int TABLET_GLOW_EXTINCTION_COG_OF_THE_SOUL_GIVES_LIFE = 0x4ac; // Tablet block 178; room 06-04-00
    public static final int TABLET_GLOW_EXTINCTION_LET_THE_MAP_GUIDE_ITS_PLACEMENT = 0x4ad; // Tablet block 180; room 06-04-01
    public static final int TABLET_GLOW_EXTINCTION_THOSE_SEEKING_EDEN = 0x4ae; // Tablet block 230; room 06-05-00
    public static final int TABLET_GLOW_EXTINCTION_EVERY_HEART_HAS_A_COLOR = 0x4af; // Tablet block 182; room 06-05-01
    public static final int TABLET_GLOW_EXTINCTION_GATEKEEPERS = 0x4b0; // Tablet block 154; room 06-06-01
    public static final int TABLET_GLOW_EXTINCTION_SHELL_IN_THE_LEFT_HAND_POINTS = 0x4b1; // Tablet block 174; room 06-07-00
    public static final int TABLET_GLOW_EXTINCTION_PROCEED_THROUGH_THE_WALL = 0x4b2; // Tablet block 181; room 06-07-00
    public static final int TABLET_GLOW_EXTINCTION_TWO_ANGEL_SHIELD = 0x4b3; // Tablet block 173; room 06-08-01

    public static final int TABLET_GLOW_TWIN_IDIGNA_BURANUN_HERMES = 0x4b4; // Tablet block 192; room 07-00-00
    public static final int TABLET_GLOW_TWIN_BLACK_ONYX_PURPLE_AMETHYST = 0x4b5; // Tablet block 189; room 07-00-01
    public static final int TABLET_GLOW_TWIN_FRONT_AND_BACK_DAMAGED = 0x4b6; // Tablet block 190; room 07-01-00
    public static final int TABLET_GLOW_TWIN_FOOT_OF_THE_FOOTLESS_NEPTUNE_DAMAGED = 0x4b8; // Tablet block 194; room 07-02-01
    public static final int TABLET_GLOW_TWIN_CHANT_A_SPELL_TO_THE_SPIRIT = 0x4ba; // Tablet block 195; room 07-02-02
    public static final int TABLET_GLOW_TWIN_HERO_OF_THE_THIRD_CHILD = 0x4bb; // Tablet block 193; room 07-03-00
    public static final int TABLET_GLOW_TWIN_BEYOND_THE_SKULL_DAMAGED = 0x4bc; // Tablet block 196; room 07-03-01
    public static final int TABLET_GLOW_TWIN_INNOCENT_GIRL = 0x4be; // Tablet block 210; room 07-06-00
    public static final int TABLET_GLOW_TWIN_UNSOLVABLE_PUZZLE = 0x4bf; // Tablet block 198; room 07-06-01
    public static final int TABLET_GLOW_TWIN_BEELZEBUB = 0x4c0; // Tablet block 201; room 07-07-00
    public static final int TABLET_GLOW_TWIN_REVEAL_THE_LIGHT = 0x4c2; // Tablet block 202; room 07-08-00
    public static final int TABLET_GLOW_TWIN_FIVE_WITCHES_PLUS_ONE_MORE = 0x4c3; // Tablet block 199; room 07-08-01
    public static final int TABLET_GLOW_TWIN_HATCH_SKULD_VERDANDI = 0x4c6; // Tablet block 207; room 07-10-01
    public static final int TABLET_GLOW_TWIN_PRIESTS_BECAME_PHILOSOPHERS = 0x4c7; // Tablet block 208; room 07-11-00
    public static final int TABLET_GLOW_TWIN_ANKH_IS_ALWAYS_IN_THE_FRONT = 0x4c8; // Tablet block 209; room 07-11-01
    public static final int TABLET_GLOW_TWIN_DANCE_OF_LIFE = 0x4c9; // Tablet block 197; room 07-11-02
    public static final int TABLET_GLOW_TWIN_DESTROYED_IN_2015 = 0x4cc; // Tablet block 214; room 07-12-02
    public static final int TABLET_GLOW_TWIN_ZU_PERYTON_DAMAGED = 0x4cd; // Tablet block 211; room 07-13-00
    public static final int TABLET_GLOW_TWIN_POWER_TO_STOP_TIME = 0x4ce; // Tablet block 212; room 07-14-00
    public static final int TABLET_GLOW_TWIN_BROTHER_SHOPS = 0x552; // Tablet block 213; room 07-14-01

    public static final int TABLET_GLOW_ENDLESS_DRAGON = 0x4d1; // Tablet block 224; room 08-00-01
    public static final int TABLET_GLOW_ENDLESS_BORN_LIVES_REPRODUCE_ASCENSION = 0x4d2; // Tablet block 225; room 08-02-00
    public static final int TABLET_GLOW_ENDLESS_TOILS_FOR_NAUGHT = 0x4d3; // Tablet block 226; room 08-03-01
    public static final int TABLET_GLOW_ENDLESS_MAP_COLOR = 0x4d4; // Tablet block 612; room 08-03-02
    public static final int TABLET_GLOW_ENDLESS_MIGHT_OR_WISDOM = 0x4d5; // Tablet block 227; room 08-03-03
    public static final int TABLET_GLOW_ENDLESS_ENDURE_TRIALS = 0x4d7; // Tablet block 228; room 08-04-02

    public static final int TABLET_GLOW_SHRINE_CHOSEN_ONLY = 0x4d9; // Tablet block 615; room 09-00-00
    public static final int TABLET_GLOW_SHRINE_ENDLESSNESS_AND_DIMENSIONS = 0x4da; // Tablet block 233; room 09-00-01
    public static final int TABLET_GLOW_SHRINE_PHILOSOPHER_CHOSEN_ONE = 0x4db; // Tablet block 235; room 09-01-00
    public static final int TABLET_GLOW_SHRINE_EIGHT_SPIRITS = 0x4dc; // Tablet block 238; room 09-03-01
    public static final int TABLET_GLOW_SHRINE_SECRET_TREASURE_OF_LIFE = 0x4dd; // Tablet block 240; room 09-04-00
    public static final int TABLET_GLOW_SHRINE_PHILOSOPHER_IMMORTAL = 0x4de; // Tablet block 241; room 09-04-01
    public static final int TABLET_GLOW_SHRINE_SEVENTH_CHILDREN = 0x4df; // Tablet block 242; room 09-05-01
    public static final int TABLET_GLOW_SHRINE_SLEEP_WITHIN_THE_WOMAN = 0x4e0; // Tablet block 243; room 09-08-01
    public static final int TABLET_GLOW_SHRINE_WALL_OF_YOUR_CALLING = 0x4e1; // Tablet block 234; room 09-08-01
//    public static final int TABLET_GLOW_SHRINE_ROSETTA = 0x4e2; // Tablet block 239; room 09-09-00
    public static final int TABLET_GLOW_SHRINE_MUSHUSSU_COMBO = 0x4e3; // Tablet block 236; room 09-09-01

    public static final int TABLET_GLOW_ILLUSION_VIRTUAL_WISE_MAN = 0x4e5; // Tablet block 253; room 10-01-00
    public static final int TABLET_GLOW_ILLUSION_DEVICE_THAT_CREATES_LIFE = 0x4ea; // Tablet block 252; room 10-02-00
    public static final int TABLET_GLOW_ILLUSION_POSSESS_THE_WISDOM_OF_A_WISE_MAN = 0x4ec; // Tablet block 256; room 10-02-01
    public static final int TABLET_GLOW_ILLUSION_FOOL_DESERVES_NAUGHT_BUT_DEATH = 0x4ed; // Tablet block 259; room 10-02-02
    public static final int TABLET_GLOW_ILLUSION_KEY_TO_ENDLESS_CORRIDOR = 0x4ee; // Tablet block 251; room 10-03-00
    public static final int TABLET_GLOW_ILLUSION_TIAMAT_GRAIL_POWERLESS = 0x4ef; // Tablet block 254; room 10-04-00
    public static final int TABLET_GLOW_ILLUSION_GIVE_UP_LEFT = 0x4f0; // Tablet block 258; room 10-04-01
    public static final int TABLET_GLOW_ILLUSION_GIVE_UP_MIDDLE = 0x553; // Tablet block 258; room 10-04-01
    public static final int TABLET_GLOW_ILLUSION_GIVE_UP_RIGHT = 0x554; // Tablet block 258; room 10-04-01
    public static final int TABLET_GLOW_ILLUSION_FREE_THYSELF_OF_DOUBT_AND_ILLUSION = 0x4f1; // Tablet block 257; room 10-05-00
    public static final int TABLET_GLOW_ILLUSION_THE_FAIRIES_UNLOCK_IT = 0x4f3; // Tablet block 260; room 10-06-00
    public static final int TABLET_GLOW_ILLUSION_UNDERSTANDING_IS_IMPRESSIVE = 0x4f5; // Tablet block 262; room 10-07-02
    public static final int TABLET_GLOW_ILLUSION_FOOL_PUZZLE = 0x4f6; // Tablet block 263; room 10-07-02
    public static final int TABLET_GLOW_ILLUSION_WANDER_THE_LABYRINTH_FOR_ETERNITY = 0x4f8; // Tablet block 261; room 10-08-00
    public static final int TABLET_GLOW_ILLUSION_FOOL_WILL_NEVER_FIND_HIS_WAY = 0x4f9; // Tablet block 264; room 10-08-01
    public static final int TABLET_GLOW_ILLUSION_SEARCH_ON_BENDED_KNEE = 0x4fa; // Tablet block 270; room 10-09-00
    public static final int TABLET_GLOW_ILLUSION_EXTINGUISH_THE_WHOLE = 0x4fb; // Tablet block 265; room 10-09-01
    public static final int TABLET_GLOW_ILLUSION_ENTER_EDEN = 0x4fd; // Tablet block 271; room 10-09-01

    public static final int TABLET_GLOW_EDEN = 0x4fc; // Tablet block 967; room 10-09-01

    public static final int TABLET_GLOW_GRAVEYARD_TOUJIN = 0x4fe; // Tablet block 276; room 11-00-01
    public static final int TABLET_GLOW_GRAVEYARD_THOSE_SEEKING_EDEN = 0x4ff; // Tablet block 232; room 11-00-02
    public static final int TABLET_GLOW_GRAVEYARD_MONEY_CANT_BUY_THE_REAL_THING = 0x500; // Tablet block 279; room 11-01-00
    public static final int TABLET_GLOW_GRAVEYARD_MEETING_OF_MAN_AND_WOMAN = 0x501; // Tablet block 280; room 11-01-01
    public static final int TABLET_GLOW_GRAVEYARD_DEMON_ENDLESSLY_CONSUMES_LIFE = 0x502; // Tablet block 281; room 11-02-00
    public static final int TABLET_GLOW_GRAVEYARD_MEANINGS_ARE_INFUSED_INTO_THE_GEMSTONES = 0x503; // Tablet block 283; room 11-03-00
    public static final int TABLET_GLOW_GRAVEYARD_MU_IS_THE_NAMELESS_ONE = 0x504; // Tablet block 277; room 11-04-02
    public static final int TABLET_GLOW_GRAVEYARD_EYES_ARE_NOT_HOLLOWED_OUT_HOLES = 0x505; // Tablet block 278; room 11-04-03
    public static final int TABLET_GLOW_GRAVEYARD_WE_SHALL_AWAIT_THEE_AHEAD = 0x506; // Tablet block 288; room 11-05-00
    public static final int TABLET_GLOW_GRAVEYARD_NEW_WEAPON_TAKE_DOWN_THE_WALL = 0x507; // Tablet block 284; room 11-07-00
    public static final int TABLET_GLOW_GRAVEYARD_JEWEL_SHOP_WHERE_LIFE_SPRINGS_FORTH = 0x509; // Tablet block 287; room 11-07-01
    public static final int TABLET_GLOW_GRAVEYARD_SPAULDER_GODDESS = 0x50a; // Tablet block 285; room 11-09-00
    public static final int TABLET_GLOW_GRAVEYARD_CURSED_TREASURE_RED_STONE = 0x50b; // Tablet block 286; room 11-09-01

    public static final int TABLET_GLOW_MOONLIGHT_ATOP_THE_NAVEL_OF_NEPTUNE = 0x50c; // Tablet block 294; room 12-00-01
    public static final int TABLET_GLOW_MOONLIGHT_EVERY_HEART_HAS_A_WEIGHT = 0x50d; // Tablet block 292; room 12-01-00
    public static final int TABLET_GLOW_MOONLIGHT_MIND_THY_MANNERS = 0x50e; // Tablet block 293; room 12-02-00
    public static final int TABLET_GLOW_MOONLIGHT_ULTIMATE_SACRIFICE_FOR_POWER = 0x50f; // Tablet block 295; room 12-03-00
    public static final int TABLET_GLOW_MOONLIGHT_OCARINA_WOMAN_WITH_CHILD = 0x510; // Tablet block 296; room 12-04-00
    public static final int TABLET_GLOW_MOONLIGHT_GRIND_DOWN_THE_PYRAMID = 0x511; // Tablet block 300; room 12-04-03
    public static final int TABLET_GLOW_MOONLIGHT_WATER_WILL_PROVIDE_THE_POWER = 0x512; // Tablet block 298; room 12-05-01
    public static final int TABLET_GLOW_MOONLIGHT_SIGIL_OF_SPIRIT_THAT_CONTROLS_LIFE = 0x513; // Tablet block 302; room 12-06-01
    public static final int TABLET_GLOW_MOONLIGHT_WEDGES_GIVE_FORM_TO_THE_SOUL = 0x514; // Tablet block 297; room 12-07-00
    public static final int TABLET_GLOW_MOONLIGHT_BOOK_OF_THE_DEAD = 0x515; // Tablet block 299; room 12-08-00
    public static final int TABLET_GLOW_MOONLIGHT_UNDERWORLD_PALACE = 0x516; // Tablet block 301; room 12-09-00
    public static final int TABLET_GLOW_MOONLIGHT_EDEN_SEARCH_OUT_THE_PLACE = 0x517; // Tablet block 191; room 12-09-01

    public static final int TABLET_GLOW_GODDESS_FLY_WITH_THE_GOLDEN_WINGS = 0x518; // Tablet block 312; room 13-00-00
//    public static final int TABLET_GLOW_GODDESS_WATER_TO_THE_TOWER = 0x519; // Tablet block 310; room 13-01-00
    public static final int TABLET_GLOW_GODDESS_MAGATAMA_JEWEL = 0x51a; // Tablet block 314; room 13-01-01
    public static final int TABLET_GLOW_GODDESS_HUNK_OF_FLYING_IRON = 0x51b; // Tablet block 306; room 13-02-01
    public static final int TABLET_GLOW_GODDESS_A_BAO_A_QU = 0x51c; // Tablet block 309; room 13-02-02
    public static final int TABLET_GLOW_GODDESS_RUSALII_YAKSI_DAKINI = 0x51d; // Tablet block 318; room 13-03-01
    public static final int TABLET_GLOW_GODDESS_CHALLENGE_THE_CHAMBER_OF_BIRTH = 0x51e; // Tablet block 311; room 13-04-00
    public static final int TABLET_GLOW_GODDESS_ILLUSION_OF_GODDESS_STATUES = 0x51f; // Tablet block 320; room 13-04-01
//    public static final int TABLET_GLOW_GODDESS_FLAIL_WHIP_PUZZLE = 0x520; // Tablet block 315; room 13-05-00
    public static final int TABLET_GLOW_GODDESS_BALANCE_THE_SPIRITS = 0x521; // Tablet block 316; room 13-05-01
    public static final int TABLET_GLOW_GODDESS_AFTER_THINE_ASCENSION_TO_THE_TOWER = 0x522; // Tablet block 317; room 13-06-03
    public static final int TABLET_GLOW_GODDESS_SECRET_OF_LIFE_POWER_OF_DESTRUCTION = 0x523; // Tablet block 307; room 13-06-03
    public static final int TABLET_GLOW_GODDESS_CANNOT_GRANT_THE_MOTHERS_WISH = 0x524; // Tablet block 308; room 13-07-02

    public static final int TABLET_GLOW_RUIN_UNINTENTIONAL_MISCHIEF = 0x525; // Tablet block 334; room 14-00-00
    public static final int TABLET_GLOW_RUIN_FOLLOW_THE_NAME_THAT_MEANS_UNNAMED = 0x526; // Tablet block 335; room 14-00-02
    public static final int TABLET_GLOW_RUIN_SKANDA = 0x527; // Tablet block 332; room 14-01-00
    public static final int TABLET_GLOW_RUIN_WHITE_DIAMOND_REPRESENTS_PURITY = 0x528; // Tablet block 336; room 14-02-01
//    public static final int TABLET_GLOW_RUIN_ROSETTA = 0x529; // Tablet block 333; room 14-03-00
    public static final int TABLET_GLOW_RUIN_GREAT_BIRD_MERCY_CRUSHING_THAT_HAND = 0x52a; // Tablet block 331; room 14-03-01
    public static final int TABLET_GLOW_RUIN_TOO_QUICK_FOR_THE_EYES = 0x52b; // Tablet block 330; room 14-04-00
    public static final int TABLET_GLOW_RUIN_GLOWING_RED_CRUCIFIX_BEACON = 0x52c; // Tablet block 328; room 14-04-01
    public static final int TABLET_GLOW_RUIN_THE_ONES_THAT_CREATED_NUWA = 0x52d; // Tablet block 329; room 14-05-00
    public static final int TABLET_GLOW_RUIN_IDENTIFY_THE_SEDUCTRESS = 0x52e; // Tablet block 325; room 14-06-01
    public static final int TABLET_GLOW_RUIN_THE_ONE_WHO_CHALLENGES_NUWA = 0x52f; // Tablet block 326; room 14-07-00
    public static final int TABLET_GLOW_RUIN_FACE_THE_LAST_TRIAL = 0x530; // Tablet block 327; room 14-07-01

//    public static final int TABLET_GLOW_BIRTH_SWORDS_ROSETTA = 0x531; // Tablet block 353; room 15-00-00
    public static final int TABLET_GLOW_BIRTH_SWORDS_BRIGHT_LIGHT_WITHIN_THE_DARKNESS = 0x532; // Tablet block 340; room 15-01-00
    public static final int TABLET_GLOW_BIRTH_SWORDS_CONTINUES_WITHOUT_END_BIRTH_DEATH = 0x533; // Tablet block 350; room 15-01-01
    public static final int TABLET_GLOW_BIRTH_SWORDS_SHALL_NOT_MAKE_IT_WITH_THINE_POWER = 0x534; // Tablet block 342; room 15-02-00
    public static final int TABLET_GLOW_BIRTH_SWORDS_PALENQUE_SLUMBERS = 0x535; // Tablet block 346; room 15-02-01
    public static final int TABLET_GLOW_BIRTH_SWORDS_VISHNU_BEHEADED_ASURA = 0x536; // Tablet block 352; room 15-03-00
    public static final int TABLET_GLOW_BIRTH_SWORDS_STRIKE_THE_WEDGE_DISTURB_THE_MOTHER = 0x537; // Tablet block 347; room 15-03-01

    public static final int TABLET_GLOW_BIRTH_SKANDA_CLAY_DOLLS_BECOME_HUMAN = 0x538; // Tablet block 344; room 16-00-00
    public static final int TABLET_GLOW_BIRTH_SKANDA_BECAME_HUMAN_AND_PROSPERED = 0x539; // Tablet block 345; room 16-00-01
    public static final int TABLET_GLOW_BIRTH_SKANDA_PRAISE_LIFE_CLAY_DOLL_GOLDEN_KEY = 0x53a; // Tablet block 348; room 16-01-00
    public static final int TABLET_GLOW_BIRTH_SKANDA_BEYOND_THE_MANY_CORPSES_A_WOMAN_WAITS = 0x53b; // Tablet block 349; room 16-01-01
    public static final int TABLET_GLOW_BIRTH_SKANDA_BE_PREPARED_FOR_DEATH = 0x53c; // Tablet block 343; room 16-02-00
    public static final int TABLET_GLOW_BIRTH_SKANDA_GOLDEN_GLOW_THINLY_STRETCHED_LIGHT = 0x53e; // Tablet block 341; room 16-03-00
    public static final int TABLET_GLOW_BIRTH_SKANDA_CHALLENGE_PALENQUE = 0x53f; // Tablet block 351; room 16-03-01
    public static final int TABLET_GLOW_BIRTH_SKANDA_WHITE_OPAL_REPRESENTS_JOY = 0x540; // Tablet block 354; room 16-04-00
    public static final int TABLET_GLOW_BIRTH_SKANDA_CRYSTAL_SKULL_SUPPRESSES_TIAMAT = 0x541; // Tablet block 355; room 16-04-01
    public static final int TABLET_GLOW_BIRTH_SKANDA_DANCE_IN_THIS_PLACE = 0x542; // Tablet block 356; room 16-04-02

    public static final int TABLET_GLOW_DIMENSIONAL_BEYOND_THE_BOUNDARIES_OF_TIME = 0x543; // Tablet block 359; room 17-01-00
    public static final int TABLET_GLOW_DIMENSIONAL_LIVES_THE_11_CHILDREN_HAVE = 0x544; // Tablet block 362; room 17-01-01
//    public static final int TABLET_GLOW_DIMENSIONAL_BIRTH_MANTRA = 0x545; // Tablet block 1021; room 17-02-00
//    public static final int TABLET_GLOW_DIMENSIONAL_DEATH_MANTRA = 0x546; // Tablet block 1022; room 17-02-00
    public static final int TABLET_GLOW_DIMENSIONAL_ROAD_TO_TREASURE = 0x547; // Tablet block 360; room 17-04-00
    public static final int TABLET_GLOW_DIMENSIONAL_TWO_VESSELS = 0x548; // Tablet block 361; room 17-04-00
    public static final int TABLET_GLOW_DIMENSIONAL_THRUST_INFINITY_THERE = 0x549; // Tablet block 363; room 17-05-01
    public static final int TABLET_GLOW_DIMENSIONAL_ONLY_ONE_PATH_TO_SURVIVAL = 0x54a; // Tablet block 670; room 17-07-01

    public static final int TABLET_GLOW_HT_SLEEP_ALSO_SET_BY_RETROMAUSOLEUM_ENTRANCE = 0x54b; // Tablet block 1004 (HT) and 516 (Retromausoleum); room 23-13-00 and room 19-00-01
    public static final int TABLET_GLOW_HT_I_EXIST_HERE = 0x54c; // Tablet block 1005; room 23-18-00
    public static final int TABLET_GLOW_HT_CLOTH_TO_MAKE_THE_TREASURE = 0x54d; // Tablet block 1009; room 23-22-00

    // Broken tablets
    public static final int TABLET_GLOW_GUIDANCE_ENTRANCE_BROKEN = 0x44d; // Tablet block 471; room 00-00-00
    public static final int TABLET_GLOW_GUIDANCE_BACKSIDE_DOOR_BROKEN = 0x455; // Tablet block 471; room 00-05-00
    public static final int TABLET_GLOW_GUIDANCE_SHOP_BROKEN = 0x45c; // Tablet block 471; room 00-09-01
    public static final int TABLET_GLOW_MAUSOLEUM_TOP_BROKEN = 0x45e; // Tablet block 471; room 02-00-00
    public static final int TABLET_GLOW_MAUSOLEUM_TRAPDOOR_BROKEN = 0x464; // Tablet block 471; room 02-02-01
    public static final int TABLET_GLOW_MAUSOLEUM_ELEVATOR_SHAFT_BROKEN = 0x46b; // Tablet block 471; room 02-05-02
    public static final int TABLET_GLOW_SUN_ABOVE_MULBRUK_BROKEN = 0x476; // Tablet block 471; room 03-01-01
    public static final int TABLET_GLOW_SUN_MULBRUK_SCREEN_BROKEN = 0x54f; // Tablet block 471; room 03-03-00
    public static final int TABLET_GLOW_SPRING_SACRED_LAKE_BROKEN = 0x490; // Tablet block 471; room 04-02-00
    public static final int TABLET_GLOW_SPRING_MAP_SCREEN_BROKEN = 0x491; // Tablet block 471; room 04-02-01
    public static final int TABLET_GLOW_EXTINCTION_SMALL_MURAL_BROKEN = 0x4a9; // Tablet block 471; room 06-02-01
    public static final int TABLET_GLOW_TWIN_UPPER_LEFT_BROKEN = 0x4b7; // Tablet block 471; room 07-01-01
    public static final int TABLET_GLOW_TWIN_MAP_SCREEN_BROKEN = 0x4b9; // Tablet block 471; room 07-02-02
    public static final int TABLET_GLOW_TWIN_YIEGAH_SCREEN_BROKEN = 0x4bd; // Tablet block 471; room 07-02-02
    public static final int TABLET_GLOW_TWIN_BELOW_ZU_BROKEN = 0x550; // Tablet block 471; room 07-05-01
    public static final int TABLET_GLOW_TWIN_IDIGNA_BROKEN = 0x4c1; // Tablet block 471; room 07-07-01
    public static final int TABLET_GLOW_TWIN_POISON_2_BROKEN = 0x4c4; // Tablet block 471; room 07-09-01
    public static final int TABLET_GLOW_TWIN_RIGHT_OF_POISON_2_BROKEN = 0x4c5; // Tablet block 471; room 07-10-00
    public static final int TABLET_GLOW_TWIN_FAKE_ANKH_JEWEL_BROKEN = 0x4ca; // Tablet block 471; room 07-12-00
    public static final int TABLET_GLOW_TWIN_ABOVE_YIEAR_BROKEN = 0x4cb; // Tablet block 471; room 07-12-01
    public static final int TABLET_GLOW_TWIN_ARROGANT_STURDY_SNAKE_BROKEN = 0x4cf; // Tablet block 471; room 07-14-00
    public static final int TABLET_GLOW_TWIN_LEFT_OF_BACKSIDE_GRAIL_BROKEN = 0x4d0; // Tablet block 471; room 07-15-00
    public static final int TABLET_GLOW_ENDLESS_FAIRY_SCREEN_BROKEN = 0x4d6; // Tablet block 471; room 08-03-03
    public static final int TABLET_GLOW_ILLUSION_CHILDS_ROOM_BROKEN = 0x4e9; // Tablet block 471; room 10-01-01
    public static final int TABLET_GLOW_ILLUSION_BACKSIDE_DOOR_BROKEN = 0x551; // Tablet block 471; room 10-03-00
    public static final int TABLET_GLOW_ILLUSION_MOVER_ATHLELAND_SCREEN_BROKEN = 0x4f2; // Tablet block 471; room 10-03-00
    public static final int TABLET_GLOW_ILLUSION_FOOLS_CONFUSION_CORRIDOR_BROKEN = 0x4f4; // Tablet block 471; room 10-07-00
    public static final int TABLET_GLOW_ILLUSION_FOOLS_CONFUSION_CORRIDOR_SCAN_ROOM_BROKEN = 0x4f7; // Tablet block 471; room 10-07-02
    public static final int TABLET_GLOW_GRAVEYARD_GIANT_THEXDE_SCREEN_BROKEN = 0x508; // Tablet block 471; room 11-07-00
    public static final int TABLET_GLOW_BIRTH_SKANDA_ASURAS_ROOM_BROKEN = 0x53d; // Tablet block 471; room 16-02-00

    public static final List<Integer> BLANK_TABLET_GLOW_FLAGS = Arrays.asList(
            TABLET_GLOW_GUIDANCE_ENTRANCE_BROKEN,
            TABLET_GLOW_GUIDANCE_BACKSIDE_DOOR_BROKEN,
            TABLET_GLOW_GUIDANCE_SHOP_BROKEN,
            TABLET_GLOW_MAUSOLEUM_TOP_BROKEN,
            TABLET_GLOW_MAUSOLEUM_TRAPDOOR_BROKEN,
            TABLET_GLOW_MAUSOLEUM_ELEVATOR_SHAFT_BROKEN,
            TABLET_GLOW_SUN_ABOVE_MULBRUK_BROKEN,
            TABLET_GLOW_SUN_MULBRUK_SCREEN_BROKEN,
            TABLET_GLOW_SPRING_SACRED_LAKE_BROKEN,
            TABLET_GLOW_SPRING_MAP_SCREEN_BROKEN,
            TABLET_GLOW_EXTINCTION_SMALL_MURAL_BROKEN,
            TABLET_GLOW_TWIN_UPPER_LEFT_BROKEN,
            TABLET_GLOW_TWIN_MAP_SCREEN_BROKEN,
            TABLET_GLOW_TWIN_YIEGAH_SCREEN_BROKEN,
            TABLET_GLOW_TWIN_BELOW_ZU_BROKEN,
            TABLET_GLOW_TWIN_IDIGNA_BROKEN,
            TABLET_GLOW_TWIN_POISON_2_BROKEN,
            TABLET_GLOW_TWIN_RIGHT_OF_POISON_2_BROKEN,
            TABLET_GLOW_TWIN_FAKE_ANKH_JEWEL_BROKEN,
            TABLET_GLOW_TWIN_ABOVE_YIEAR_BROKEN,
            TABLET_GLOW_TWIN_ARROGANT_STURDY_SNAKE_BROKEN,
            TABLET_GLOW_TWIN_LEFT_OF_BACKSIDE_GRAIL_BROKEN,
            TABLET_GLOW_ENDLESS_FAIRY_SCREEN_BROKEN,
            TABLET_GLOW_ILLUSION_CHILDS_ROOM_BROKEN,
            TABLET_GLOW_ILLUSION_BACKSIDE_DOOR_BROKEN,
            TABLET_GLOW_ILLUSION_MOVER_ATHLELAND_SCREEN_BROKEN,
            TABLET_GLOW_ILLUSION_FOOLS_CONFUSION_CORRIDOR_BROKEN,
            TABLET_GLOW_ILLUSION_FOOLS_CONFUSION_CORRIDOR_SCAN_ROOM_BROKEN,
            TABLET_GLOW_GRAVEYARD_GIANT_THEXDE_SCREEN_BROKEN,
            TABLET_GLOW_BIRTH_SKANDA_ASURAS_ROOM_BROKEN
    );

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
    public static final int SCREEN_FLAG_E = 0x00e; // Often associated with a dais for a toll door
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
    public static final int SCREEN_FLAG_2C = 0x02c; // Commonly used for mantra detection
    public static final int SCREEN_FLAG_2D = 0x02d;
    public static final short SCREEN_FLAG_2E = 0x02e; // Used in randomizer, for fake item and/or trap screen flag for chests (spawns bats); also doubles as screen flag for one of the crushers on Chain Whip puzzle; also used in Halloween 2019 on Mr. Slushfund's screen
    public static final int SCREEN_FLAG_2F = 0x02f; // Used in randomizer, for the other crusher on Chain Whip puzzle
    public static final int SCREEN_FLAG_30 = 0x030;
    public static final int SCREEN_FLAG_31 = 0x031;

    public static final int ROOM_FLAG_35 = 0x035; // Used by fake Ankh Jewel trap in Twin Labyrinths for non-random traps.
    public static final int ROOM_FLAG_37 = 0x037; // Used for a weight door during the escape.
    public static final int ROOM_FLAG_3B = 0x03b; // Used by Sun bats for Sacred Orb chest / cast down those that fly.
    public static final int ROOM_FLAG_3C = 0x03c; // Used by Graveyard trap chest for non-random traps.
    public static final int ROOM_FLAG_3E = 0x03e;
    public static final int ROOM_FLAG_40 = 0x040; // Set by an arg on Tiamat's ankh when the fight starts
    public static final int ROOM_FLAG_45 = 0x045; // Used for a weight door during the escape.

    // Flags used in vanilla reclaimed for randomizer
    public static final int XELPUD_TENT_OPEN = 0x21c; // For the two-second delay between game start and Xelpud's tent being open
    public static final int RETRO_XELPUD_DOOR_COVER = 0x2e3; // Set to 1 after talking to 8bit Elder on Gate of Time Surface in vanilla.
    public static final int XELPUD_READY_TO_TALK = 0x3b6; // Making sure you can't actually talk to Xelpud in his basic conversation if the tent is closed.

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

    // Halloween 2019
    public static final int CUSTOM_HALLOWEEN_MULBRUK_CONVERSATION = 0xaac; // 0 = not spoken, 1 = did intro, 2 = ready to talk about HT, 3 = HT unlocked
    public static final int CUSTOM_HALLOWEEN_H4 = 0xaad; // Used to track progress for escaping H4. Can't use a screen/room flag because of the abnormal transitions.
    public static final int CUSTOM_SECRET_SHOP = 0xacd; // Unlocks secret shop on the Surface for buying Scriptures / Perfume / Money
    public static final int CUSTOM_HALLOWEEN_MULBRUK_HINT = 0xace; // Allows Mulbruk to cycle through NPC hints.
    public static final int CUSTOM_HALLOWEEN_NPC_COUNT = 0xaca; // Incremented when visiting NPCs
    public static final int CUSTOM_HALLOWEEN_FINAL_DRACUET = 0xaae; // Incremented when visiting NPCs

    // Halloween 2021
    public static final int CUSTOM_HALLOWEEN2021_CURSED = 0xaaa; // Incremented with curses.
    public static final int CUSTOM_HALLOWEEN2021_BAT_LEVEL = 0xaab;
    public static final int CUSTOM_HALLOWEEN2021_SKELETON_LEVEL = 0xaac;
    public static final int CUSTOM_HALLOWEEN2021_BAT_RETRIBUTION = 0xaad;
    public static final int CUSTOM_HALLOWEEN2021_MULBRUK_DRACUET = 0xaae;
    public static final int CUSTOM_HALLOWEEN2021_MOTHER_STATE = 0xaaf; // Replaces 0x0fe to avoid side effects for escape sequence sounds.
    public static final int CUSTOM_HALLOWEEN2021_MOTHER_DEFEATED = 0xab0; // Replaces 0x382 to avoid triggering escape sequence after Mother is defeated.
    public static final short CUSTOM_HALLOWEEN2021_XELPUD_BONUS_CANDY_CONVERSATIONS = 0xab1;
    public static final short CUSTOM_HALLOWEEN2021_ESCAPE_SPECIAL = 0xab2; // Triggers a different escape sequence door for 111% completion.
    public static final short CUSTOM_HALLOWEEN2021_ESCAPE_CHEST = 0xab3; // Replaces escape chest world flag for autotracker consistency between random and nonrandom coin chests

    // Steam achievements?
    public static final int TALKED_TO_AWAKENED_MULBRUK = 0xaf0;
    public static final int ACHIEVEMENT_ENTERED_RUINS = 0xbb8; // Set to 1 when opening the ruins
    public static final int ACHIEVEMENT_MAP_COUNT = 0xc06; // Counts up to 17
    public static final int ACHIEVEMENT_SOFTWARE_COUNT = 0xc07; // Counts up to 20

    public static final int ACHIEVEMENT_ALL_SACRED_ORBS = 0xbda; // Set to 1 when sacred orbs collected becomes 10
    public static final int ACHIEVEMENT_ALL_SOFTWARE = 0xbd5; // Set to 1 when software count becomes 20
    public static final int ACHIEVEMENT_ALL_MAPS = 0xbd6; // Set to 1 when map count becomes 17
    public static final int ACHIEVEMENT_ALL_COIN_CHESTS = 0xbd7; // Set to 1 when coin chests collected becomes 28
    public static final int ACHIEVEMENT_ALL_EMAILS = 0xbdb; // Set to 1 when email count becomes 45

    public static final int ACHIEVEMENT_SOFTWARE_COUNT_UPDATED_DEATHV = 0xc13; // Incremented when deathv.exe is collected.

    public static final int ACHIEVEMENT_MAP_COUNT_UPDATED_SURFACE = 0xc1d; // Incremented when Surface map is collected.
    public static final int ACHIEVEMENT_MAP_COUNT_UPDATED_GUIDANCE = 0xc1c; // Incremented when Gate of Guidance map is collected.
    public static final int ACHIEVEMENT_MAP_COUNT_UPDATED_MAUSOLEUM = 0xc1e; // Incremented when Mausoleum of the Giants map is collected.
    public static final int ACHIEVEMENT_MAP_COUNT_UPDATED_SUN = 0xc1f; // Incremented when Temple of the Sun map is collected.
    public static final int ACHIEVEMENT_MAP_COUNT_UPDATED_SPRING = 0xc20; // Incremented when Spring in the Sky map is collected.
    public static final int ACHIEVEMENT_MAP_COUNT_UPDATED_INFERNO = 0xc21; // Incremented when Inferno Cavern map is collected.
    public static final int ACHIEVEMENT_MAP_COUNT_UPDATED_EXTINCTION = 0xc22; // Incremented when Chamber of Extinction map is collected.
    public static final int ACHIEVEMENT_MAP_COUNT_UPDATED_TWIN = 0xc23; // Incremented when Twin Labyrinths map is collected.
    public static final int ACHIEVEMENT_MAP_COUNT_UPDATED_ENDLESS = 0xc24; // Incremented when Endless Corridor map is collected.
    public static final int ACHIEVEMENT_MAP_COUNT_UPDATED_SHRINE = 0xc25; // Incremented when Shrine of the Mother map is collected.
    public static final int ACHIEVEMENT_MAP_COUNT_UPDATED_ILLUSION = 0xc26; // Incremented when Gate of Illusion map is collected.
    public static final int ACHIEVEMENT_MAP_COUNT_UPDATED_GRAVEYARD = 0xc27; // Incremented when Graveyard of the Giants map is collected.
    public static final int ACHIEVEMENT_MAP_COUNT_UPDATED_MOONLIGHT = 0xc28; // Incremented when Temple of Moonlight map is collected.
    public static final int ACHIEVEMENT_MAP_COUNT_UPDATED_GODDESS = 0xc29; // Incremented when Tower of the Goddess map is collected.
    public static final int ACHIEVEMENT_MAP_COUNT_UPDATED_RUIN = 0xc2a; // Incremented when Tower of Ruin map is collected.
    public static final int ACHIEVEMENT_MAP_COUNT_UPDATED_BIRTH = 0xc2b; // Incremented when Chamber of Birth map is collected.
    public static final int ACHIEVEMENT_MAP_COUNT_UPDATED_DIMENSIONAL = 0xc2c; // Incremented when Dimensional Corridor map is collected.

    // Version specific?
    public static final int OLD_WF_DEATHV = 0x0ed; // Checked for achievement, unknown how/where it's set

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

    public static int getMapCountUpdatedFlag(int mapWorldFlag) {
        if(mapWorldFlag == WF_MAP_SURFACE) {
            return ACHIEVEMENT_MAP_COUNT_UPDATED_SURFACE;
        }
        if(mapWorldFlag == WF_MAP_GUIDANCE) {
            return ACHIEVEMENT_MAP_COUNT_UPDATED_GUIDANCE;
        }
        if(mapWorldFlag == WF_MAP_MAUSOLEUM) {
            return ACHIEVEMENT_MAP_COUNT_UPDATED_MAUSOLEUM;
        }
        if(mapWorldFlag == WF_MAP_SUN) {
            return ACHIEVEMENT_MAP_COUNT_UPDATED_SUN;
        }
        if(mapWorldFlag == WF_MAP_SPRING) {
            return ACHIEVEMENT_MAP_COUNT_UPDATED_SPRING;
        }
        if(mapWorldFlag == WF_MAP_INFERNO) {
            return ACHIEVEMENT_MAP_COUNT_UPDATED_INFERNO;
        }
        if(mapWorldFlag == WF_MAP_EXTINCTION) {
            return ACHIEVEMENT_MAP_COUNT_UPDATED_EXTINCTION;
        }
        if(mapWorldFlag == WF_MAP_TWIN) {
            return ACHIEVEMENT_MAP_COUNT_UPDATED_TWIN;
        }
        if(mapWorldFlag == WF_MAP_ENDLESS) {
            return ACHIEVEMENT_MAP_COUNT_UPDATED_ENDLESS;
        }
        if(mapWorldFlag == WF_MAP_SHRINE) {
            return ACHIEVEMENT_MAP_COUNT_UPDATED_SHRINE;
        }
        if(mapWorldFlag == WF_MAP_ILLUSION) {
            return ACHIEVEMENT_MAP_COUNT_UPDATED_ILLUSION;
        }
        if(mapWorldFlag == WF_MAP_GRAVEYARD) {
            return ACHIEVEMENT_MAP_COUNT_UPDATED_GRAVEYARD;
        }
        if(mapWorldFlag == WF_MAP_MOONLIGHT) {
            return ACHIEVEMENT_MAP_COUNT_UPDATED_MOONLIGHT;
        }
        if(mapWorldFlag == WF_MAP_GODDESS) {
            return ACHIEVEMENT_MAP_COUNT_UPDATED_GODDESS;
        }
        if(mapWorldFlag == WF_MAP_RUIN) {
            return ACHIEVEMENT_MAP_COUNT_UPDATED_RUIN;
        }
        if(mapWorldFlag == WF_MAP_BIRTH) {
            return ACHIEVEMENT_MAP_COUNT_UPDATED_BIRTH;
        }
        if(mapWorldFlag == WF_MAP_DIMENSIONAL) {
            return ACHIEVEMENT_MAP_COUNT_UPDATED_DIMENSIONAL;
        }
        throw new RuntimeException("Unable to find flag for Map with world flag " + mapWorldFlag);
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

    public static short getSafeScreenFlag(int locationWorldFlag) {
        if(locationWorldFlag == WF_ANKH_JEWEL_DIMENSIONAL) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_ANKH_JEWEL_GUIDANCE) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_ANKH_JEWEL_MAUSOLEUM) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_ANKH_JEWEL_SPRING) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_ANKH_JEWEL_SUN) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_ANKH_JEWEL_RUIN) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_ANKH_JEWEL_TWIN) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_BIRTH_SEAL) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_SOFTWARE_BOUNCE) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_BRONZE_MIRROR) {
            return SCREEN_FLAG_10;
        }
        if(locationWorldFlag == WF_COG_OF_THE_SOUL) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_CRUCIFIX) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_CRYSTAL_SKULL) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_DEATH_SEAL) {
            return SCREEN_FLAG_10;
        }
        if(locationWorldFlag == WF_DIARY) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_DIMENSIONAL_KEY) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_DJED_PILLAR) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_EYE_OF_TRUTH) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_FAIRY_CLOTHES) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_FEATHER) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_FRUIT_OF_EDEN) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_GAUNTLET) {
            return SCREEN_FLAG_10; // 9 is probably fine, but it's used in an adjoining room so we'll stick with it for safety.
        }
        if(locationWorldFlag == WF_GLOVE) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_GRAPPLE_CLAW) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_HOLY_GRAIL) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_ICE_CAPE) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_ISIS_PENDANT) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_KEY_OF_ETERNITY) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_SOFTWARE_LAMULANA) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_LIFE_SEAL) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_MAGATAMA_JEWEL) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_MAP_BIRTH) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_MAP_EXTINCTION) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_MAP_DIMENSIONAL) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_MAP_ENDLESS) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_MAP_GUIDANCE) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_MAP_ILLUSION) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_MAP_GRAVEYARD) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_MAP_INFERNO) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_MAP_MAUSOLEUM) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_MAP_SHRINE) {
            return SCREEN_FLAG_10; // 9 is probably fine, but it's used in the adjoining room so we'll stick with it for safety.
        }
        if(locationWorldFlag == WF_MAP_SPRING) {
            return SCREEN_FLAG_10;
        }
        if(locationWorldFlag == WF_MAP_MOONLIGHT) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_MAP_SUN) {
            return SCREEN_FLAG_10; // 9 is probably fine, but it's used in the adjoining room so we'll stick with it for safety.
        }
        if(locationWorldFlag == WF_MAP_RUIN) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_MAP_GODDESS) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_MAP_TWIN) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_SOFTWARE_MIRAI) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_ORIGIN_SEAL) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_PERFUME) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_PLANE_MODEL) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_POCHETTE_KEY) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_SACRED_ORB_GUIDANCE) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_SACRED_ORB_SURFACE) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_SACRED_ORB_MAUSOLEUM) {
            return SCREEN_FLAG_11; // 10 is probably fine, but could be used if randomizing which giant opens the Sacred Orb chest.
        }
        if(locationWorldFlag == WF_SACRED_ORB_SUN) {
            return SCREEN_FLAG_11; // 9 is probably fine, but it's used in the adjoining room so we'll stick with it for safety, and 10 is used for the coin chest in the also-adjoining room.
        }
        if(locationWorldFlag == WF_SACRED_ORB_SPRING) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_SACRED_ORB_EXTINCTION) {
            return SCREEN_FLAG_11; // 9 and 10 are probably fine, but they're used in an adjoining room so we'll stick with it for safety.
        }
        if(locationWorldFlag == WF_SACRED_ORB_DIMENSIONAL) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_SACRED_ORB_TWIN) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_SACRED_ORB_SHRINE) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_SACRED_ORB_RUIN) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_SCALESPHERE) {
            return SCREEN_FLAG_10; // 9 is probably fine, but it's used in the adjoining room so we'll stick with it for safety.
        }
        if(locationWorldFlag == WF_SERPENT_STAFF) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_SHELL_HORN) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_SPAULDER) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_TREASURES) {
            return SCREEN_FLAG_10; // 9 is probably fine, but it's used in the adjoining room so we'll stick with it for safety.
        }
        if(locationWorldFlag == WF_TWIN_STATUE) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_VESSEL) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_WOMAN_STATUE) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_SOFTWARE_YAGOSTR) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_COIN_SURFACE_WATERFALL) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_COIN_SURFACE_SEAL) {
            return SCREEN_FLAG_10; // 9 is probably fine, but it's used in the adjoining room so we'll stick with it for safety.
        }
        if(locationWorldFlag == WF_COIN_SURFACE_RUIN_PATH) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_COIN_SURFACE_GUIDANCE_ONE) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_COIN_SURFACE_GUIDANCE_TWO) {
            return SCREEN_FLAG_10;
        }
        if(locationWorldFlag == WF_COIN_SURFACE_GUIDANCE_TRAP) {
            return SCREEN_FLAG_10; // 9 is probably fine, but it's used in the adjoining room so we'll stick with it for safety.
        }
        if(locationWorldFlag == WF_COIN_SURFACE_MAUSOLEUM) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_COIN_SUN) {
            return SCREEN_FLAG_10;
        }
        if(locationWorldFlag == WF_COIN_SPRING) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_COIN_INFERNO_LAVA) {
            return SCREEN_FLAG_10; // 9 is probably fine, but it's used in the adjoining room so we'll stick with it for safety.
        }
        if(locationWorldFlag == WF_COIN_INFERNO_SPIKES) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_COIN_EXTINCTION) {
            return SCREEN_FLAG_10;
        }
        if(locationWorldFlag == WF_COIN_TWIN_WITCHES) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_COIN_TWIN_LOWER) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_COIN_ENDLESS) {
            return SCREEN_FLAG_10; // 9 is probably fine, but it's used in the adjoining room so we'll stick with it for safety.
        }
        if(locationWorldFlag == WF_COIN_SHRINE) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_COIN_ILLUSION_KATANA) {
            return SCREEN_FLAG_10;
        }
        if(locationWorldFlag == WF_COIN_ILLUSION_SPIKES) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_COIN_GRAVEYARD) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_COIN_MOONLIGHT) {
            return SCREEN_FLAG_10; // 9 is probably fine, but it's used in the adjoining room so we'll stick with it for safety.
        }
        if(locationWorldFlag == WF_COIN_GODDESS_SHIELD) {
            return SCREEN_FLAG_10; // 9 is probably fine, but it's used in the adjoining room so we'll stick with it for safety.
        }
        if(locationWorldFlag == WF_COIN_GODDESS_FAIRY) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_COIN_RUIN) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_COIN_BIRTH_NINJA) {
            return SCREEN_FLAG_10; // 9 is probably fine, but it's used in the adjoining room so we'll stick with it for safety.
        }
        if(locationWorldFlag == WF_COIN_BIRTH_DANCE) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_COIN_BIRTH_SOUTHEAST) {
            return SCREEN_FLAG_9;
        }
        if(locationWorldFlag == WF_COIN_DIMENSIONAL) {
            return SCREEN_FLAG_10; // 9 is probably fine, but it's used in the adjoining room so we'll stick with it for safety.
        }
        if(locationWorldFlag == WF_TRAP_GRAVEYARD) {
            return SCREEN_FLAG_11; // 9 and 10 are probably fine, but they're used in an adjoining room so we'll stick with it for safety.
        }
        if(locationWorldFlag == WF_TRAP_ILLUSION) {
            return SCREEN_FLAG_10;
        }
        if(locationWorldFlag == WF_COIN_TWIN_ESCAPE) {
            return SCREEN_FLAG_10; // 9 is probably fine, but it's used in the adjoining room so we'll stick with it for safety.
        }
        return FlagConstants.SCREEN_FLAG_2E;
    }

    private FlagConstants() { }
}
