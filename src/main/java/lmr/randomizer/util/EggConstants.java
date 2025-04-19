package lmr.randomizer.util;

public final class EggConstants {
    public static final int TOTAL_HIDDEN_EGGS = 100;

    public static short getEggFlag(int eggNumber) {
        return (short)(2600 + eggNumber - 1);
    }

    public static int getEggImageX(int eggNumber) {
        return ((eggNumber - 1) % 25) * 40;
    }

    public static int getEggImageY(int eggNumber) {
        return 712 + ((eggNumber - 1) / 25) * 40;
    }

    public static String getEggGraphic(int eggNumber) {
        switch(eggNumber) {
            case 1:
                return "egg_green.png"; // Left of Xelpud
            case 2:
                return "egg_green.png"; // Surface Hot Spring
            case 3:
                return "egg_ice.png"; // Graveyard Hot Spring
            case 4:
                return "egg_mausoleum.png"; // Volcano
            case 5:
                return "egg_striped.png"; // Striped egg
            case 6:
                return "egg_herja.png"; // Guidance striped egg behind statue, may need to swap out graphic
            case 7:
                return "egg_blue.png"; // Goddess hand, might swap with the Spring egg
            case 8:
                return "egg_dna.png"; // Spike pit
            case 9:
                return "egg_moonlight.png"; // Dancing man v2
            case 10:
                return "egg_naramura.png"; // Punchy fist
            case 11:
                return "egg_spotted.png"; // Foot of Abuto
            case 12:
                return "egg_guidance.png"; // Behind pillar in Guidance
            case 13:
                return "egg_blue.png"; // Statue nose
            case 14:
                return "egg_kara.png"; // Spring behind block
            case 15:
                return "egg_green.png"; // Lizard's Room
            case 16:
                return "egg_pink.png"; // Behind pot in Ruin - formerly egg13
            case 17:
                return "egg_behemoth.png"; // Behind pot in Birth
            case 18:
                return "egg_herja.png"; // Flame in Dimensional
            case 19:
                return "egg_endless.png"; // Endless ceiling - formerly egg06
            case 20:
                return "egg_dark.png"; // Endless keyhole
            case 21:
                return "egg_shrine.png"; // Shrine crusher
            case 22:
                return "egg_true_shrine.png"; // True Shrine crusher
            case 23:
                return "egg_mulbruk.png"; // Shrine other crusher
            case 24:
                return "egg_skanda.png"; // Birth near grail tablet
            case 25:
                return "egg_retro_s.png"; // Retro green egg
            case 26:
                return "egg_dark.png"; // Dark egg
            case 27:
                return "egg_inferno.png"; // Chain whip room
            case 28:
                return "egg_skull_s.png"; // Mausoleum retribution tablet; swap to Mulbruk egg?
            case 29:
                return "egg_ice.png"; // Moonlight among spikes
            case 30:
                return "egg_backbeard.png"; // Endless lamp recharge
            case 31:
                return "egg_vividblue.png"; // Sun retribution eye
            case 32:
                return "egg_guidance_s.png"; // Guidance mural
            case 33:
                return "egg_bone.png"; // Guidance skeleton
            case 34:
                return "egg_mulbruk.png"; // Hardmode tablet
            case 35:
                return "egg_sun.png"; // Sun trap
            case 36:
                return "egg_blue.png";  // Fish tank
            case 37:
                return "egg_spring.png"; // Top of Spring
            case 38:
                return "egg_inferno_s.png"; // Inferno statue
            case 39:
                return "egg_inferno.png"; // Cat pause room
            case 40:
                return "egg_extinction.png"; // Extinction hand
            case 41:
                return "egg_twin.png";
            case 42:
                return "egg_twin.png";
            case 43:
                return "egg_xelpud.png";
            case 44:
                return "egg_illusion.png";
            case 45:
                return "egg_burning.png";
            case 46:
                return "egg_eir.png";
            case 47:
                return "egg_kara.png";
            case 48:
                return "egg_onyx.png";
            case 49:
                return "egg_moonlight.png";
            case 50:
                return "egg_moonlight.png";
            case 51:
                return "egg_ruin.png";
            case 52:
                return "egg_dna.png";
            case 53:
                return "egg_dimensional.png";
            case 54:
                return "egg_creation.png";
            case 55:
                return "egg_dimensional.png";
            case 56:
                return "egg_shrine.png";
            case 57:
                return "egg_true_shrine.png";
            case 58:
                return "egg_retro.png";
            case 59:
                return "egg_retro.png";
            case 60:
                return "egg_retro.png";
            case 61:
                return "egg_burning.png";
            case 62:
                return "egg_burning.png";
            case 63:
                return "egg_regret.png";
            case 64:
                return "egg_naramura.png";
            case 65:
                return "egg_naramura.png";
            case 66:
                return "egg_regret.png";
            case 67:
                return "egg_xelpud.png";
            case 68:
                return "egg_ice.png";
            case 69:
                return "egg_bone.png";
            case 70:
                return "egg_alruna.png";
            case 71:
                return "egg_bone.png";
            case 72:
                return "egg_regret.png";
            case 73:
                return "egg_catball.png";
            case 74:
                return "egg_mulbruk.png";
            case 75:
                return "egg_tog.png";
            case 76:
                return "egg_mausoleum.png";
            case 77:
                return "egg_skanda.png";
            case 78:
                return "egg_eir.png";
            case 79:
                return "egg_mulbruk.png";
            case 80:
                return "egg_alruna.png";
            case 81:
                return "egg_onyx.png";
            case 82:
                return "egg_tog.png";
            case 83:
                return "egg_ruin.png";
            case 84:
                return "egg_pink.png";
            case 85:
                return "egg_enkidu.png";
            case 86:
                return "egg_pink.png";
            case 87:
                return "egg_illusion.png";
            case 88:
                return "egg_twin.png";
            case 89:
                return "egg_guidance.png";
            case 90:
                return "egg_eye.png";
            case 91:
                return "egg_enkidu.png";
            case 92:
                return "egg_eye.png";
            case 93:
                return "egg_spotted.png";
            case 94:
                return "egg_green.png";
            case 95:
                return "egg_kara.png";
            case 96:
                return "egg_spring.png";
            case 97:
                return "egg_retro.png";
            case 98:
                return "egg_dimensional.png";
            case 99:
                return "egg_vividblue.png";
            case 100:
                return "egg_pickup_guidance_s.png";
            default:
                return "egg_spotted.png";
        }
    }

    public static int getEggInnerXOffset(int eggNumber) {
        switch(eggNumber) {
            case 1:
                return 14;
            case 2:
                return 10;
            case 3:
                return 18;
            case 4:
                return 16;
            case 5:
                return 0;
            case 6:
                return 24;
            case 7:
                return 28;
            case 8:
                return 0;
            case 9:
                return 32;
            case 10:
                return 12;
            case 11:
                return 0;
            case 12:
                return 18;
            case 13:
                return 16;
            case 14:
                return 16;
            case 15:
                return 16;
            case 16:
                return 0;
            case 17:
                return 16;
            case 18:
                return 6;
            case 19:
                return 14;
            case 20:
                return 15;
            case 21:
                return 8;
            case 22:
                return 8;
            case 23:
                return 3;
            case 24:
                return 15;
            case 25:
                return 22;
            case 26:
                return 0;
            case 27:
                return 0;
            case 28:
                return 16;
            case 29:
                return 0;
            case 30:
                return 16;
            case 31:
                return 16;
            case 32:
                return 18;
            case 33:
                return 24;
            case 34:
                return 16;
            case 35:
                return 16;
            case 36:
                return 16;
            case 37:
                return 15;
            case 38:
                return 16;
            case 39:
                return 16;
            case 40:
                return 16;
            case 41:
                return 16;
            case 42:
                return 22;
            case 43:
                return 16;
            case 44:
                return 16;
            case 45:
                return 16;
            case 46:
                return 20;
            case 47:
                return 16;
            case 48:
                return 16;
            case 49:
                return 18;
            case 50:
                return 0;
            case 51:
                return 16;
            case 52:
                return 20;
            case 53:
                return 16;
            case 54:
                return 8;
            case 55:
                return 16;
            case 56:
                return 0;
            case 57:
                return 20;
            case 58:
                return 16;
            case 59:
                return 16;
            case 60:
                return 20;
            case 61:
                return 0;
            case 62:
                return 32;
            case 63:
                return 10;
            case 65:
                return 0;
            case 66:
                return 16;
            case 67:
                return 16;
            case 68:
                return 16;
            case 69:
                return 17;
            case 70:
                return 16;
            case 71:
                return 26;
            case 72:
                return 16;
            case 73:
                return 32;
            case 74:
                return 16;
            case 75:
                return 16;
            case 76:
                return 20;
            case 77:
                return 18;
            case 78:
                return 16;
            case 79:
                return 16;
            case 80:
                return 25;
            case 81:
                return 22;
            case 82:
                return 16;
            case 83:
                return 16;
            case 84:
                return 16;
            case 85:
                return 15;
            case 86:
                return 16;
            case 87:
                return 14;
            case 88:
                return 16;
            case 89:
                return 16;
            case 90:
                return 18;
            case 91:
                return 32;
            case 92:
                return 10;
            case 93:
                return 0;
            case 94:
                return 6;
            case 95:
                return 0;
            case 96:
                return 20;
            case 97:
                return 16;
            case 98:
                return 16;
            case 99:
                return 16;
            case 100:
                return 18;
            default:
                return 0;
        }
    }

    public static int getEggInnerYOffset(int eggNumber) {
        switch(eggNumber) {
            case 1:
                return 30;
            case 2:
                return 26;
            case 3:
                return 30;
            case 4:
                return 20;
            case 5:
                return 30;
            case 6:
                return 30;
            case 7:
                return 15;
            case 8:
                return 29;
            case 9:
                return 30;
            case 10:
                return 30;
            case 11:
                return 30;
            case 12:
                return 30;
            case 13:
                return 12;
            case 14:
                return 30;
            case 15:
                return 12;
            case 16:
                return 30;
            case 17:
                return 30;
            case 18:
                return 22;
            case 19:
                return 18;
            case 20:
                return 12;
            case 21:
                return 30;
            case 22:
                return 30;
            case 23:
                return 16;
            case 24:
                return 30;
            case 25:
                return 30;
            case 26:
                return 6;
            case 27:
                return 30;
            case 28:
                return 30;
            case 29:
                return 0;
            case 30:
                return 30;
            case 31:
                return 13;
            case 32:
                return 10;
            case 33:
                return 30;
            case 34:
                return 30;
            case 35:
                return 30;
            case 36:
                return 30;
            case 37:
                return 18;
            case 38:
                return 20;
            case 39:
                return 30;
            case 40:
                return 30;
            case 41:
                return 30;
            case 42:
                return 30;
            case 43:
                return 0;
            case 44:
                return 30;
            case 45:
                return 12;
            case 46:
                return 30;
            case 47:
                return 30;
            case 48:
                return 20;
            case 49:
                return 30;
            case 50:
                return 30;
            case 51:
                return 24;
            case 52:
                return 16;
            case 53:
                return 20;
            case 54:
                return 30;
            case 55:
                return 30;
            case 56:
                return 30;
            case 57:
                return 30;
            case 58:
                return 10;
            case 59:
                return 10;
            case 60:
                return 30;
            case 61:
                return 0;
            case 62:
                return 0;
            case 63:
                return 30;
            case 64:
                return 10;
            case 65:
                return 30;
            case 66:
                return 30;
            case 67:
                return 30;
            case 68:
                return 30;
            case 69:
                return 11;
            case 70:
                return 6;
            case 71:
                return 16;
            case 72:
                return 16;
            case 73:
                return 6;
            case 74:
                return 30;
            case 75:
                return 24;
            case 76:
                return 0;
            case 77:
                return 30;
            case 78:
                return 30;
            case 79:
                return 30;
            case 80:
                return 20;
            case 81:
                return 30;
            case 82:
                return 24;
            case 83:
                return 30;
            case 84:
                return 30;
            case 85:
                return 20;
            case 86:
                return 0;
            case 87:
                return 16;
            case 88:
                return 30;
            case 89:
                return 16;
            case 90:
                return 30;
            case 91:
                return 30;
            case 92:
                return 24;
            case 93:
                return 13;
            case 94:
                return 8;
            case 95:
                return 30;
            case 96:
                return 16;
            case 97:
                return 30;
            case 98:
                return 30;
            case 99:
                return 13;
            case 100:
                return 0;
            default:
                return 30;
        }
    }
}
