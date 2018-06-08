package lmr.randomizer.ui;

import lmr.randomizer.Settings;
import lmr.randomizer.Translations;
import lmr.randomizer.random.ShopRandomizationEnum;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class ShopRandomizationRadio extends JPanel {
    private ButtonGroup shopRandomization;
    private JLabel shopRandomizationLabel;
    private JRadioButton shopCategorizedRandomization;
    private JRadioButton shopEverythingRandomization;

    public ShopRandomizationRadio() {
        super(new MigLayout("gapy 0, insets 8 0 0 0"));

        shopRandomizationLabel = new JLabel(Translations.getText("randomization.randomizeShops"), JLabel.LEFT);
        add(shopRandomizationLabel);

        shopRandomization = new ButtonGroup();

        shopCategorizedRandomization = new JRadioButton(Translations.getText("randomization.randomizeShops.categorized"));
        shopCategorizedRandomization.setActionCommand("CATEGORIZED");
        shopRandomization.add(shopCategorizedRandomization);

        shopEverythingRandomization = new JRadioButton(Translations.getText("randomization.randomizeShops.everything"));
        shopEverythingRandomization.setActionCommand("EVERYTHING");
        shopRandomization.add(shopEverythingRandomization);

        add(shopCategorizedRandomization);
        add(shopEverythingRandomization);

        if (ShopRandomizationEnum.CATEGORIZED.equals(Settings.getShopRandomization())) {
            shopCategorizedRandomization.setSelected(true);
        } else {
            shopEverythingRandomization.setSelected(true);
        }
    }

    public void updateSettings() {
        Settings.setShopRandomization(shopRandomization.getSelection().getActionCommand(), true);
    }

    public void updateTranslations() {
        shopRandomizationLabel.setText(Translations.getText("randomization.randomizeShops"));
        shopCategorizedRandomization.setText(Translations.getText("randomization.randomizeShops.categorized"));
        shopEverythingRandomization.setText(Translations.getText("randomization.randomizeShops.everything"));
    }

    public void reloadSettings() {
        if("CATEGORIZED".equals(Settings.getShopRandomization().toString())) {
            shopCategorizedRandomization.setSelected(true);
        }
        if("EVERYTHING".equals(Settings.getShopRandomization().toString())) {
            shopEverythingRandomization.setSelected(true);
        }
    }
}
