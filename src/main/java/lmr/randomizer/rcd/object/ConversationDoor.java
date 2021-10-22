package lmr.randomizer.rcd.object;

import lmr.randomizer.HolidaySettings;
import lmr.randomizer.util.BlockConstants;

/**
 * 0xa0 args: Partial Reversed; Tested
 *     0 -
 *     1 -
 *     2 -
 *     3 - conv-type
 *     4 - block
 *     5 -
 *     6 - Disallow Music Change
 */
public class ConversationDoor extends GameObject {
    public static int SingleConversation = 0;
    public static int Shop = 1;
    public static int ConversationTree = 2;

    public ConversationDoor(ObjectContainer objectContainer, int x, int y) {
        super(objectContainer, 7);
        setId(ObjectIdConstants.ConversationDoor);
        setX(x);
        setY(y);
    }

    public ConversationDoor(GameObject gameObject) {
        super(gameObject.getObjectContainer(), 7);
        setId(ObjectIdConstants.ConversationDoor);
        setX(gameObject.getX());
        setY(gameObject.getY());
        addTests(gameObject.getTestByteOperations());
        addUpdates(gameObject.getWriteByteOperations());
        setArg0(gameObject.getArgs().get(0));
        setArg1(gameObject.getArgs().get(1));
        setArg2(gameObject.getArgs().get(2));
        setDoorType(gameObject.getArgs().get(3));
        setBlockNumber(gameObject.getArgs().get(4));
        setArg5(gameObject.getArgs().get(5));
        setDisallowMusicChange(gameObject.getArgs().get(6) > 0);
    }

    public void setShopDefaults() {
        setArg0(0);
        setArg1(0);
        setArg2(0);
        setDoorType(Shop);
        setBlockNumber(36);
        setArg5(0);
        setDisallowMusicChange(false);
    }

    public void setArg0(int arg0) {
        getArgs().set(0, (short)arg0);
    }

    public void setArg1(int arg1) {
        getArgs().set(1, (short)arg1);
    }

    public void setArg2(int arg2) {
        getArgs().set(2, (short)arg2);
    }

    public void setDoorType(int doorType) {
        getArgs().set(3, (short)doorType);
    }

    public short getBlockNumber() {
        return getArgs().get(4);
    }
    public void setBlockNumber(int blockNumber) {
        getArgs().set(4, (short)blockNumber);
    }

    public void setArg5(int arg5) {
        getArgs().set(5, (short)arg5);
    }

    public void setDisallowMusicChange(boolean disallowMusicChange) {
        getArgs().set(6, (short)(disallowMusicChange ? 1 : 0));
    }

    public static int getDoorType(String npcAssigned) {
        if(isConversationTree(npcAssigned)) {
            return ConversationTree;
        }
        return isShop(npcAssigned) ? Shop : SingleConversation;
    }

    public static boolean isConversationTree(String npcAssigned) {
        if("NPC: Elder Xelpud".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Mulbruk".equals(npcAssigned)) {
            return true;
        }
        return false;
    }

    public static boolean isShop(String npcAssigned) {
        if("NPC: Nebur".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Sidro".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Modro".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Penadvent of ghost".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Greedy Charlie".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Shalom III".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Usas VI".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Kingvalley I".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Mr. Fishman (Original)".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Mr. Fishman (Alt)".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Operator Combaker".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Yiegah Kungfu".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Arrogant Metagear".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Arrogant Sturdy Snake".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Yiear Kungfu".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Affected Knimare".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Mover Athleland".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Giant Mopiran".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Kingvalley II".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Energetic Belmont".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Mechanical Efspi".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Mud Man Qubert".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Hot-blooded Nemesistwo".equals(npcAssigned)) {
            return true;
        }
        if("NPC: Tailor Dracuet".equals(npcAssigned)) {
            return true;
        }
        return false;
    }

    public static short getConversationArg(String npcAssigned) {
        if("NPC: Nebur".equals(npcAssigned)) {
            return BlockConstants.ShopBlockNebur; // 490 for Alt
        }
        if("NPC: Sidro".equals(npcAssigned)) {
            return BlockConstants.ShopBlockSidro;
        }
        if("NPC: Modro".equals(npcAssigned)) {
            return BlockConstants.ShopBlockModro;
        }
        if("NPC: Penadvent of ghost".equals(npcAssigned)) {
            return BlockConstants.ShopBlockPenadventOfGhost;
        }
        if("NPC: Greedy Charlie".equals(npcAssigned)) {
            return BlockConstants.ShopBlockGreedyCharlie;
        }
        if("NPC: Shalom III".equals(npcAssigned)) {
            return BlockConstants.ShopBlockShalomIII;
        }
        if("NPC: Usas VI".equals(npcAssigned)) {
            return BlockConstants.ShopBlockUsasVI;
        }
        if("NPC: Kingvalley I".equals(npcAssigned)) {
            return BlockConstants.ShopBlockKingvalleyI;
        }
        if("NPC: Mr. Fishman (Original)".equals(npcAssigned)) {
            return BlockConstants.ShopBlockMrFishman;
        }
        if("NPC: Mr. Fishman (Alt)".equals(npcAssigned)) {
            return BlockConstants.ShopBlockMrFishmanAlt;
        }
        if("NPC: Operator Combaker".equals(npcAssigned)) {
            return BlockConstants.ShopBlockOperatorCombaker;
        }
        if("NPC: Yiegah Kungfu".equals(npcAssigned)) {
            return BlockConstants.ShopBlockYiegahKungfu;
        }
        if("NPC: Arrogant Metagear".equals(npcAssigned)) {
            return BlockConstants.ShopBlockArrogantMetagear;
        }
        if("NPC: Arrogant Sturdy Snake".equals(npcAssigned)) {
            return BlockConstants.ShopBlockArrogantSturdySnake ;
        }
        if("NPC: Yiear Kungfu".equals(npcAssigned)) {
            return BlockConstants.ShopBlockYiearKungfu;
        }
        if("NPC: Affected Knimare".equals(npcAssigned)) {
            return BlockConstants.ShopBlockAffectedKnimare;
        }
        if("NPC: Mover Athleland".equals(npcAssigned)) {
            return BlockConstants.ShopBlockMoverAthleland;
        }
        if("NPC: Giant Mopiran".equals(npcAssigned)) {
            return (short)(HolidaySettings.isFools2020Mode() ? BlockConstants.ShopBlockGiantMopiranAngelShield : BlockConstants.ShopBlockGiantMopiran);
        }
        if("NPC: Kingvalley II".equals(npcAssigned)) {
            return BlockConstants.ShopBlockKingvalleyII;
        }
        if("NPC: Energetic Belmont".equals(npcAssigned)) {
            return BlockConstants.ShopBlockEnergeticBelmont;
        }
        if("NPC: Mechanical Efspi".equals(npcAssigned)) {
            return BlockConstants.ShopBlockMechanicalEfspi;
        }
        if("NPC: Mud Man Qubert".equals(npcAssigned)) {
            return BlockConstants.ShopBlockMudManQubert;
        }
        if("NPC: Hot-blooded Nemesistwo".equals(npcAssigned)) {
            return BlockConstants.ShopBlockHotbloodedNemesistwo;
        }
        if("NPC: Elder Xelpud".equals(npcAssigned)) {
            return BlockConstants.Master_ElderXelpudRandomSetA;
        }
        if("NPC: Hiner".equals(npcAssigned)) {
            return BlockConstants.Master_Hiner;
        }
        if("NPC: Moger".equals(npcAssigned)) {
            return BlockConstants.Master_Moger;
        }
        if("NPC: Former Mekuri Master".equals(npcAssigned)) {
            return BlockConstants.Master_FormerMekuriMaster_Mekuri;
        }
        if("NPC: Priest Zarnac".equals(npcAssigned)) {
            return BlockConstants.Master_PriestZarnac;
        }
        if("NPC: Priest Xanado".equals(npcAssigned)) {
            return BlockConstants.Master_PriestXanado;
        }
        if("NPC: Philosopher Giltoriyo".equals(npcAssigned)) {
            return BlockConstants.Master_PhilosopherGiltoriyo;
        }
        if("NPC: Priest Hidlyda".equals(npcAssigned)) {
            return BlockConstants.Master_PriestHidlyda;
        }
        if("NPC: Priest Romancis".equals(npcAssigned)) {
            return BlockConstants.Master_PriestRomancis;
        }
        if("NPC: Priest Aramo".equals(npcAssigned)) {
            return BlockConstants.Master_PriestAramo;
        }
        if("NPC: Priest Triton".equals(npcAssigned)) {
            return BlockConstants.Master_PriestTriton;
        }
        if("NPC: Priest Jaguarfiv".equals(npcAssigned)) {
            return BlockConstants.Master_PriestJaguarfiv;
        }
        if("NPC: The Fairy Queen".equals(npcAssigned)) {
            return 686;
        }
        if("NPC: Mr. Slushfund".equals(npcAssigned)) {
            return BlockConstants.Master_MrSlushfund_Pepper;
        }
        if("NPC: Priest Alest".equals(npcAssigned)) {
            return BlockConstants.Master_PriestAlest;
        }
        if("NPC: Stray fairy".equals(npcAssigned)) {
            return BlockConstants.Master_StrayFairy;
        }
        if("NPC: Giant Thexde".equals(npcAssigned)) {
            return BlockConstants.Master_GiantThexde;
        }
        if("NPC: Philosopher Alsedana".equals(npcAssigned)) {
            return BlockConstants.Master_PhilosopherAlsedana;
        }
        if("NPC: Philosopher Samaranta".equals(npcAssigned)) {
            return BlockConstants.Master_PhilosopherSamaranta;
        }
        if("NPC: Priest Laydoc".equals(npcAssigned)) {
            return BlockConstants.Master_PriestLaydoc;
        }
        if("NPC: Priest Ashgine".equals(npcAssigned)) {
            return BlockConstants.Master_PriestAshgine;
        }
        if("NPC: Philosopher Fobos".equals(npcAssigned)) {
            return BlockConstants.Master_Fobos_MedicineCheck;
        }
        if("NPC: 8bit Elder".equals(npcAssigned)) {
            return BlockConstants.Master_8BitElder;
        }
        if("NPC: duplex".equals(npcAssigned)) {
            return BlockConstants.Master_duplex;
        }
        if("NPC: Samieru".equals(npcAssigned)) {
            return BlockConstants.Master_Samieru;
        }
        if("NPC: Naramura".equals(npcAssigned)) {
            return BlockConstants.Master_Naramura;
        }
        if("NPC: 8bit Fairy".equals(npcAssigned)) {
            return BlockConstants.Master_8BitFairy;
        }
        if("NPC: Priest Madomono".equals(npcAssigned)) {
            return BlockConstants.Master_PriestMadomono;
        }
        if("NPC: Priest Gailious".equals(npcAssigned)) {
            return BlockConstants.Master_PriestGailious;
        }
        if("NPC: Mulbruk".equals(npcAssigned)) {
            return BlockConstants.Master_MulbrukRandomSetA;
        }
        if("NPC: Tailor Dracuet".equals(npcAssigned)) {
            return BlockConstants.ShopBlockTailorDracuet;
        }
        return 0;
    }
}
