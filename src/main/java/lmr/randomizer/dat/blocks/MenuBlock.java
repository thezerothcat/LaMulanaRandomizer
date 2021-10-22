package lmr.randomizer.dat.blocks;

import lmr.randomizer.dat.blocks.contents.entries.ListEntry;
import lmr.randomizer.dat.blocks.contents.entries.TextEntry;

public class MenuBlock extends Block {
    // Inventory menu headers
//    public static final int LaptopMenu_MainWeaponHeader = 0;
//    public static final int LaptopMenu_SubWeaponHeader = 1;
//    public static final int LaptopMenu_UsableItemsHeader = 2;
//    public static final int LaptopMenu_PassiveItemsHeader = 3;
//    public static final int LaptopMenu_SealsHeader = 4;
//    public static final int LaptopMenu_ItemDescriptionHeader = 5;

    // Unused. Main menu text
//    public static final int MainMenuOption_Start = 6;
//    public static final int MainMenuOption_Continue = 7;
//    public static final int MainMenuOption_Load = 8;
//    public static final int Unknown_VsGuardian = 9;
//    public static final int Unknown_VsRoomGuardians = 10;

    // Unused. Game Over menu text
//    public static final int DeathScreen_GameOver = 11;
//    public static final int DeathScreen_Title = 12;
//    public static final int DeathScreen_Continue = 13;

    // Software menu text
//    public static final int SoftwareMenu_MemorySpace = 14;
//    public static final int SoftwareMenu_Memory = 15;
//    public static final int SoftwareMenu_Free = 16; // M, as in 1600M/2000M
//    public static final int SoftwareMenu_MemoryUnitAbbreviation = 17;
//    public static final int SoftwareMenu_ComputerNameMSX = 18;
//    public static final int SoftwareMenu_ComputerNameMSX2 = 19;

    // Numbers in menus
//    public static final int Menu_Number0 = 20;
//    public static final int Menu_Number1 = 21;
//    public static final int Menu_Number2 = 22;
//    public static final int Menu_Number3 = 23;
//    public static final int Menu_Number4 = 24;
//    public static final int Menu_Number5 = 25;
//    public static final int Menu_Number6 = 26;
//    public static final int Menu_Number7 = 27;
//    public static final int Menu_Number8 = 28;
//    public static final int Menu_Number9 = 29;

    // Software type text
//    public static final int SoftwareMenu_SoftwareTypeAppLauncher = 30;
//    public static final int SoftwareMenu_SoftwareTypeSupplementalFunctions = 31;
//    public static final int SoftwareMenu_SoftwareTypeAdditionalApplications = 32;
//    public static final int SoftwareMenu_SoftwareTypeGameSoftware = 33;

//    public static final int SoftwareMenu_MemorySpaceDenominatorMSX = 34;
//    public static final int SoftwareMenu_MemorySpaceDenominatorMSX2 = 35;
//    public static final int SoftwareMenu_SoftwareHeader = 36;

    public static final int XelpudMailer_AppHeaderText = 37;
//    public static final int Unknown_ColonSymbol = 38;
    public static final int ListData_Unknown39 = 39;
    public static final int ListData_Unknown40 = 40;
    public static final int ListData_LaMulaneseBackgroundColor = 41;
    public static final int ListData_XelpudMailerTextColor = 42;

    // Pop-in menu text
//    public static final int PopInMenuText_RecordConversation = 43;
//    public static final int PopInMenuText_UseHolyGrail = 44;
//    public static final int PopInMenuText_ReturnToPauseScreen = 45;
    public static final int ListData_PopInMenuColor = 46; // Unconfirmed

//    public static final int SaveMenu_OverwritePrompt = 47; // Unused?
//    public static final int SaveMenu_Yes = 48; // Unconfirmed
//    public static final int SaveMenu_No = 49; // Unconfirmed
    public static final int ListData_SelectedTextColor = 50; // Unconfirmed

    public static final int ListData_RosettaCount = 51; // Number required to understand Ancient Mulanese. Caps at 32, defaults to 3
//    public static final int RosettaPrompt1 = 52;
//    public static final int RosettaPrompt2 = 53;
//    public static final int RosettaPrompt3 = 54;

//    public static final int MantraMenu_Header = 55;
    public static final int ListData_MantraMenu_TitleTextColor = 56; // Unconfirmed
//    public static final int MantraMenu_NoMantra = 57;
    public static final int MantraMenu_BIRTH = 58;
    public static final int MantraMenu_DEATH = 59;
    public static final int MantraMenu_MARDUK = 60;
    public static final int MantraMenu_SABBAT = 61;
    public static final int MantraMenu_MU = 62;
    public static final int MantraMenu_VIY = 63;
    public static final int MantraMenu_BAHRUN = 64;
    public static final int MantraMenu_WEDJET = 65;
    public static final int MantraMenu_ABUTO = 66;
    public static final int MantraMenu_LAMULANA = 67;

    public static final int ListData_SnapshotsMenu_TextColor = 68;
//    public static final int SnapshotsMenu_TitleText = 69;
//    public static final int SnapshotsMenu_ScanPrompt = 70;
//    public static final int SnapshotsMenu_Scanning = 71;
//    public static final int SnapshotsMenu_ScanningEllipsis = 72;
//    public static final int SnapshotsMenu_Complete = 73;

//    public static final int TextTrax_Record = 74; // Replaces the Read option when accessing from a conversation
//    public static final int TextTrax_Number0 = 75;
//    public static final int TextTrax_Number1 = 76;
//    public static final int TextTrax_Number2 = 77;
//    public static final int TextTrax_Number3 = 78;
//    public static final int TextTrax_Number4 = 79;
//    public static final int TextTrax_Number5 = 80;
//    public static final int TextTrax_Number6 = 81;
//    public static final int TextTrax_Number7 = 82;
//    public static final int TextTrax_Number8 = 83;
//    public static final int TextTrax_Number9 = 84;
    public static final int ListData_TextTrax_MenuTextColor1 = 85; // Unknown/unconfirmed
    public static final int ListData_TextTrax_MenuTextColor2 = 86; // Unknown/unconfirmed
//    public static final int TextTrax_NumberPrefixAbbreviation = 87; // "No." as in "No.01"
//    public static final int TextTrax_Unregistered = 88;
//    public static final int TextTrax_Ellipsis = 89;
//    public static final int TextTrax_OK = 90;
//    public static final int TextTrax_DisplayText = 91;
//    public static final int TextTrax_CANCEL = 92;
//    public static final int TextTrax_DeleteText = 93;
//    public static final int TextTrax_Back = 94;
//    public static final int TextTrax_OverwritePrompt = 95;
//    public static final int TextTrax_DeletePrompt = 96;

//    public static final int Unused_ControllerSetting = 97;
//    public static final int Unused_ScreenSetting = 98;
//    public static final int Unused_SoundSetting = 99;
//    public static final int Unused_ButtonConfig = 100;
//    public static final int Unused_ActiveLabel = 101;
//    public static final int Unused_Size = 102;
//    public static final int Unused_Status = 103;
//    public static final int Unused_Brightness = 104;
//    public static final int Unused_Scroll = 105;
//    public static final int Unused_BGM = 106;
//    public static final int Unused_Sound = 107;
//    public static final int Unused_Normal = 108;
//    public static final int Unused_Bright = 109;
//    public static final int Unused_Brighter = 110;
//    public static final int Unused_ON = 111;
//    public static final int Unused_OFF = 112;
//    public static final int Unused_USBController = 113;
//    public static final int Unused_XBOX360Controller = 114;
//    public static final int Unused_ClassicController = 115;
//    public static final int Unused_Keyboard = 117;

//    public static final int Unknown_Percent = 118;
//    public static final int Unknown_Width = 119;

//    public static final int SnapshotsFailText = 120;

    public static final int ListData_EmusicTitleColor = 201;
    public static final int ListData_UnknownSelectedTextColor = 202;
    public static final int ListData_EmusicHighlightedLineColor = 203;

    public static final int ListData_ShieldHp = 204; // HP and some other value - Buckler 20hp, Silver Shield 70hp, Angel Shield 10000hp (hardcoded infinite), Fake Silver Shield 1hp
//    public static final int BossDefeat_Congratulations = 205;
//    public static final int BossDefeat_AdventureContinues = 206;
//    public static final int BossDefeat_JourneyNearEnd = 207;
//    public static final int BossDefeat_Amphisbaena = 208;
//    public static final int BossDefeat_Sakit = 209;
//    public static final int BossDefeat_Ellmac = 210;
//    public static final int BossDefeat_Bahamut = 211;
//    public static final int BossDefeat_Viy = 212;
//    public static final int BossDefeat_Palenque = 213;
//    public static final int BossDefeat_Baphomet = 214;
//    public static final int BossDefeat_Tiamat = 215;

//    public static final int Credit_Congratulations = 257;
//    public static final int Credit_PlayTime = 258;

//    public static final int ListData_Intro = 268;
//    public static final int IntroText_AdventureStartsHere = 269;
    public static final int LastEntry = 293;

    public MenuBlock(int blockNumber) {
        super(blockNumber);
    }

    public void setBucklerHp(int hp) {
        ((ListEntry)getBlockContents().get(ListData_ShieldHp)).getData().set(0, (short)hp);
    }
    public void setSilverShieldHp(int hp) {
        ((ListEntry)getBlockContents().get(ListData_ShieldHp)).getData().set(2, (short)hp);
    }
    public void setAngelShieldHp(int hp) {
        ((ListEntry)getBlockContents().get(ListData_ShieldHp)).getData().set(4, (short)hp);
    }
    public void setFakeSilverShieldHp(int hp) {
        ((ListEntry)getBlockContents().get(ListData_ShieldHp)).getData().set(6, (short)hp);
    }

    public void setXmailerAppHeader(TextEntry newAppHeader) {
        getBlockContents().set(XelpudMailer_AppHeaderText, newAppHeader);
    }

    public void setMantraName(int mantra, TextEntry newMantraName) {
        getBlockContents().set(MantraMenu_BIRTH + mantra, newMantraName);
    }
}