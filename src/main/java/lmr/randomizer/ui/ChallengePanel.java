package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import lmr.randomizer.random.ChestGraphics;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class ChallengePanel extends JPanel {
    private JCheckBox automaticHardmode;

    private DifficultyPanel difficultyPanel;
    private ChestGraphicsPanel chestGraphicsPanel;

    public ChallengePanel() {
        super(new MigLayout("fillx, wrap"));

        automaticHardmode = new JCheckBox();
        automaticHardmode.setSelected(Settings.isAutomaticHardmode());

        CheckboxContainer checkboxContainer = new CheckboxContainer(1);
        checkboxContainer.add(automaticHardmode);
        add(checkboxContainer, "growx, wrap");

        difficultyPanel = new DifficultyPanel();
        add(difficultyPanel, "growx, aligny, wrap");
        
        chestGraphicsPanel = new ChestGraphicsPanel();
        add(chestGraphicsPanel);
        chestGraphicsPanel.reloadSettings();

        updateTranslations();
    }

    public void updateTranslations() {
        automaticHardmode.setText(Translations.getText("challenge.automaticHardmode"));
        chestGraphicsPanel.updateTranslations();
        difficultyPanel.updateTranslations();
    }

    public void updateSettings() {
        Settings.setAutomaticHardmode(automaticHardmode.isSelected(), true);
        Settings.setChestGraphics(chestGraphicsPanel.getItem(), true);
        difficultyPanel.updateSettings();
    }

    public void reloadSettings() {
        automaticHardmode.setSelected(Settings.isAutomaticHardmode());
        chestGraphicsPanel.reloadSettings();
        difficultyPanel.reloadSettings();
    }
}

class ChestGraphicsPanel extends JPanel {
    private ButtonGroup chestSetting;
    private JRadioButton chestSettingKeepDefault;
    private JRadioButton chestSettingCoin;
    private JRadioButton chestSettingTreasure;
    private JRadioButton chestSettingRandom;
    
    public ChestGraphicsPanel() {
        super(new MigLayout("filly, wrap 6", "", "[fill]"));
        setBorder(BorderFactory.createTitledBorder(Translations.getText("challenge.chestSpriteRBGroup")));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        chestSetting = new ButtonGroup();
        chestSettingKeepDefault = new JRadioButton(Translations.getText("challenge.chestSpriteKeepDefault"));
        chestSettingKeepDefault.setActionCommand(ChestGraphics.KEEP.name());
        chestSettingCoin = new JRadioButton(Translations.getText("challenge.chestSpriteCoin"));
        chestSettingCoin.setActionCommand(ChestGraphics.ALLCOIN.name());
        chestSettingTreasure = new JRadioButton(Translations.getText("challenge.chestSpriteTreasure"));
        chestSettingTreasure.setActionCommand(ChestGraphics.ALLTREASURE.name());
        chestSettingRandom = new JRadioButton(Translations.getText("challenge.chestSpriteFullRandom"));
        chestSettingRandom.setActionCommand(ChestGraphics.ALLRANDOM.name());
        
        chestSetting.add(chestSettingKeepDefault);
        chestSetting.add(chestSettingCoin);
        chestSetting.add(chestSettingTreasure);
        chestSetting.add(chestSettingRandom);
        
        setBorder(BorderFactory.createTitledBorder(Translations.getText("challenge.chestSpriteRBGroup")));
        add(chestSettingKeepDefault);
        add(chestSettingCoin);
        add(chestSettingTreasure);
        add(chestSettingRandom);
    }
        
    public String getItem() {
        return chestSetting.getSelection().getActionCommand();
    }
    
    public void updateTranslations() {
        chestSettingKeepDefault.setText(Translations.getText("challenge.chestSpriteKeepDefault"));
        chestSettingCoin.setText(Translations.getText("challenge.chestSpriteCoin"));
        chestSettingTreasure.setText(Translations.getText("challenge.chestSpriteTreasure"));
        chestSettingRandom.setText(Translations.getText("challenge.chestSpriteFullRandom"));
    }
    
    public void reloadSettings() {
        switch (Settings.getChestGraphics()) {
        case ALLCOIN:
            chestSettingCoin.setSelected(true);
            break;
            
        case ALLTREASURE:
            chestSettingTreasure.setSelected(true);
            break;
            
        case ALLRANDOM:
            chestSettingRandom.setSelected(true);
            break;

        case KEEP:
        default:
            chestSettingKeepDefault.setSelected(true);
            break;
        }
    }
}