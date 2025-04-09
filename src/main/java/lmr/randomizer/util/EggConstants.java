package lmr.randomizer.util;

import java.util.*;

public final class EggConstants {
    public static final int TOTAL_HIDDEN_EGGS = 69;
    public static final int NON_GRAPHIC_EGGS = 1;

    private static Map<Integer, Integer> mapOfEggNumberToCustomFlag;
    private static List<Integer> availableFlags;

    static {
        mapOfEggNumberToCustomFlag = new HashMap<>();
        availableFlags = new ArrayList<>(getAvailableFlags());
    }

    public static short getEggFlag(int eggNumber) {
        Integer eggFlag = mapOfEggNumberToCustomFlag.get(eggNumber);
        if (eggFlag == null) {
            eggFlag = getNextFlag();
            mapOfEggNumberToCustomFlag.put(eggNumber, eggFlag);
        }
        return eggFlag.shortValue();
    }

    private static int getNextFlag() {
        return availableFlags.remove(0);
    }

    private static Set<Integer> getAvailableFlags() {
        Set<Integer> availableFlags = new HashSet<>();
        for(int flag = 2699; flag >= 2600; flag--) {
            availableFlags.add(flag);
        }
        return availableFlags;
    }

    public static int getEggsWithGraphics() {
        return TOTAL_HIDDEN_EGGS - NON_GRAPHIC_EGGS;
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
                return "egg03.png"; // Left of Xelpud
            case 2:
                return "egg03.png"; // Surface Hot Spring
            case 3:
                return "egg15.png"; // Graveyard Hot Spring
            case 4:
                return "egg05.png"; // Volcano
            case 5:
                return "egg23.png"; // Striped egg
            case 6:
                return "egg04.png"; // Guidance striped egg behind statue, may need to swap out graphic
            case 7:
                return "egg17.png"; // Goddess hand, might swap with the Spring egg
            case 8:
                return "egg13.png";
            case 9:
                return "egg16.png";
            case 10:
                return "egg37.png";
            case 11:
                return "egg02.png";
            case 12:
                return "egg04.png";
            case 13:
                return "egg17.png";
            case 14:
                return "egg14.png";
            case 15:
                return "egg03.png";
            case 16:
                return "egg13.png";
            case 17:
                return "egg19.png";
            case 18:
                return "egg11.png";
            case 19:
                return "egg06.png";
            case 20:
                return "egg27.png";
            case 21:
                return "egg12.png";
            case 22:
                return "egg10.png";
            case 23:
                return "egg22.png";
            case 24:
                return "egg21.png";
            case 25:
                return "egg25.png";
            case 26:
                return "egg24.png";
            case 27:
                return "egg04.png";
            case 28:
                return "egg26.png"; // Same as egg12
            case 29:
                return "egg15.png";
            case 30:
                return "egg02.png";
            case 31:
                return "egg29.png";
            case 32:
                return "egg30.png";
            case 33:
                return "egg31.png";
            case 34:
                return "egg18.png";
            case 35:
                return "egg01.png";
            case 36:
                return "egg07.png";
            case 37:
                return "egg07.png";
            case 38:
                return "egg32.png";
            case 39:
                return "egg08.png";
            case 40:
                return "egg09.png";
            case 41:
                return "egg20.png";
            case 42:
                return "egg10.png";
            case 43:
                return "egg33.png";
            case 44:
                return "egg14.png";
            case 45:
                return "egg29.png";
            case 46:
                return "egg16.png";
            case 47:
                return "egg02.png";
            case 48:
                return "egg17.png";
            case 49:
                return "egg16.png";
            case 50:
                return "egg16.png";
            case 51:
                return "egg15.png";
            case 52:
                return "egg34.png";
            case 53:
                return "egg21.png";
            case 54:
                return "egg35.png";
            case 55:
                return "egg21.png";
            case 56:
                return "egg12.png"; // Mulbruk egg?
            case 57:
                return "egg22.png";
            case 58:
                return "egg36.png"; // better graphic if possible
            case 59:
                return "egg36.png";
            case 60:
                return "egg36.png";
            case 61:
                return "egg36.png"; // burning cavern egg
            case 62:
                return "egg36.png"; // burning cavern egg
            case 63:
                return "egg13.png";
            case 64:
                return "egg37.png";
            case 65:
                return "egg37.png";
            case 66:
                return "egg13.png";
            case 67:
                return "egg33.png";
            case 68:
                return "egg15.png";
            default:
                return "egg01.png";
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
                return 18;
            case 15:
                return 16;
            case 16:
                return 0;
            case 17:
                return 16;
            case 18:
                return 4;
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
//            case 58: // retromaus
//                return 0;
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
                return 32;
            case 100:
                return 0;
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
                return 14;
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
//            case 58: // retromaus
//                return 0;
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
            case 100:
                return 30;
            default:
                return 30;
        }
    }

//    public String getEggName(int eggNumber) {
//        switch(eggNumber) {
//            case 1:
//                return "Grassy Egg";
//            case 2:
//                return "Algae Egg";
//            default:
//                return "Easter Egg";
//        }
//    }
}
