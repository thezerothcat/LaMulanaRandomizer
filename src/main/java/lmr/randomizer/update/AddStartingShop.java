package lmr.randomizer.update;

import lmr.randomizer.Settings;
import lmr.randomizer.randomization.data.ShopInventory;
import lmr.randomizer.rcd.object.ConversationDoor;
import lmr.randomizer.rcd.object.GraphicsTextureDraw;
import lmr.randomizer.rcd.object.HitTile;
import lmr.randomizer.rcd.object.Screen;

public final class AddStartingShop {
    private AddStartingShop() { }

    public static void addStartingShop(Screen startingScreen, ShopInventory shopInventory,
                                       Short startingShopBlockIndex, Short transformedShopBlockIndex) {
        int zoneIndex = startingScreen.getZoneIndex();
        ConversationDoor conversationDoor;
        if(zoneIndex == 0) {
            conversationDoor = addGuidanceShop(startingScreen, startingShopBlockIndex);
        }
        else if(zoneIndex == 2) {
            conversationDoor = addMausoleumShop(startingScreen, startingShopBlockIndex);
        }
        else if(zoneIndex == 3) {
            conversationDoor = addSunShop(startingScreen, startingShopBlockIndex);
        }
        else if(zoneIndex == 4) {
            conversationDoor = addSpringShop(startingScreen, startingShopBlockIndex);
        }
        else if(zoneIndex == 5) {
            conversationDoor = addInfernoShop(startingScreen, startingShopBlockIndex);
        }
        else if(zoneIndex == 6) {
            conversationDoor = addExtinctionShop(startingScreen, startingShopBlockIndex);
        }
        else if(zoneIndex == 7) {
            if(Settings.getCurrentStartingLocation() == -7) {
                conversationDoor = addTwinLabsBackShop(startingScreen, startingShopBlockIndex);
            }
            else {
                conversationDoor = addTwinLabsFrontShop(startingScreen, startingShopBlockIndex);
            }
        }
        else if(zoneIndex == 8) {
            conversationDoor = addEndlessShop(startingScreen, startingShopBlockIndex);
        }
        else if(zoneIndex == 10) {
            conversationDoor = addIllusionShop(startingScreen, startingShopBlockIndex);
        }
        else if(zoneIndex == 11) {
            conversationDoor = addGraveyardShop(startingScreen, startingShopBlockIndex);
        }
        else if(zoneIndex == 12) {
            conversationDoor = addMoonlightShop(startingScreen, startingShopBlockIndex);
        }
        else if(zoneIndex == 13) {
            conversationDoor = addGoddessShop(startingScreen, startingShopBlockIndex);
        }
        else if(zoneIndex == 14) {
            conversationDoor = addRuinShop(startingScreen, startingShopBlockIndex);
        }
        else if(zoneIndex == 16) {
            conversationDoor = addBirthStartStuff(startingScreen, startingShopBlockIndex);
        }
        else if(zoneIndex == 21) {
            conversationDoor = addRetroSurfaceShop(startingScreen, startingShopBlockIndex);
        }
        else {
            conversationDoor = null;
        }

        if(conversationDoor != null) {
            AddObject.addShopObjects(conversationDoor, shopInventory, transformedShopBlockIndex);
        }
    }

    public static ConversationDoor addGuidanceShop(Screen screen, short shopBlockIndex) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 240, 380);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(200);
        graphic.setImageY(100);
        graphic.setImageWidth(40);
        graphic.setImageHeight(60);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 240, 400);
        shop.setShopDefaults();
        shop.setBlockNumber(shopBlockIndex);

        screen.getObjects().add(shop);
        return shop;
    }

    public static ConversationDoor addMausoleumShop(Screen screen, short shopBlockIndex) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 300, 240);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(320);
        graphic.setImageY(212);
        graphic.setImageWidth(40);
        graphic.setImageHeight(60);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 300, 240);
        shop.setShopDefaults();
        shop.setBlockNumber(shopBlockIndex);

        screen.getObjects().add(shop);
        return shop;
    }

    public static ConversationDoor addSunShop(Screen screen, short shopBlockIndex) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 1020, 200);

        graphic.setLayer(0);
        graphic.setImageFile("01effect.png");
        graphic.setImagePosition(0, 628);
        graphic.setImageSize(80, 80);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 1040, 240);
        shop.setShopDefaults();
        shop.setBlockNumber(shopBlockIndex);

        screen.getObjects().add(shop);
        return shop;
    }

    public static ConversationDoor addSpringShop(Screen screen, short shopBlockIndex) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 60, 140);

        graphic.setLayer(0);
        graphic.setImageFile("01effect.png");
        graphic.setImagePosition(280, 628);
        graphic.setImageSize(40, 60);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 60, 160);
        shop.setShopDefaults();
        shop.setBlockNumber(shopBlockIndex);

        screen.getObjects().add(shop);
        return shop;
    }

    public static ConversationDoor addInfernoShop(Screen screen, short shopBlockIndex) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 220, 60);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(525);
        graphic.setImageY(40);
        graphic.setImageWidth(70);
        graphic.setImageHeight(60);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 240, 80);
        shop.setShopDefaults();
        shop.setBlockNumber(shopBlockIndex);

        screen.getObjects().add(shop);
        return shop;
    }

    public static ConversationDoor addExtinctionShop(Screen screen, short shopBlockIndex) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 40, 700);

        graphic.setLayer(0);
        graphic.setImageFile("01effect.png");
        graphic.setImagePosition(320, 628);
        graphic.setImageSize(40, 60);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 40, 720);
        shop.setShopDefaults();
        shop.setBlockNumber(shopBlockIndex);

        screen.getObjects().add(shop);
        return shop;
    }

    public static ConversationDoor addIllusionShop(Screen screen, short shopBlockIndex) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 220, 80);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(940);
        graphic.setImageY(172);
        graphic.setImageWidth(40);
        graphic.setImageHeight(50);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 220, 80);
        shop.setShopDefaults();
        shop.setBlockNumber(shopBlockIndex);

        screen.getObjects().add(shop);
        return shop;
    }

    public static ConversationDoor addTwinLabsFrontShop(Screen screen, short shopBlockIndex) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 880, 380);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(500);
        graphic.setImageY(200);
        graphic.setImageWidth(80);
        graphic.setImageHeight(60);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 900, 400);
        shop.setShopDefaults();
        shop.setBlockNumber(shopBlockIndex);

        screen.getObjects().add(shop);
        return shop;
    }

    public static ConversationDoor addTwinLabsBackShop(Screen screen, short shopBlockIndex) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 920, 220);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(500);
        graphic.setImageY(200);
        graphic.setImageWidth(80);
        graphic.setImageHeight(60);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 940, 240);
        shop.setShopDefaults();
        shop.setBlockNumber(shopBlockIndex);

        screen.getObjects().add(shop);
        return shop;
    }

    public static ConversationDoor addEndlessShop(Screen screen, short shopBlockIndex) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 500, 40);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(540);
        graphic.setImageY(40);
        graphic.setImageWidth(80);
        graphic.setImageHeight(80);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 520, 80);
        shop.setShopDefaults();
        shop.setBlockNumber(shopBlockIndex);

        screen.getObjects().add(shop);
        return shop;
    }

    public static ConversationDoor addGraveyardShop(Screen screen, short shopBlockIndex) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 880, 140);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(560);
        graphic.setImageY(280);
        graphic.setImageWidth(50);
        graphic.setImageHeight(60);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 880, 160);
        shop.setShopDefaults();
        shop.setBlockNumber(shopBlockIndex);

        screen.getObjects().add(shop);
        return shop;
    }

    public static ConversationDoor addMoonlightShop(Screen screen, short shopBlockIndex) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 280, 520);

        graphic.setLayer(0);
        graphic.setImageFile("01effect.png");
        graphic.setImagePosition(80, 628);
        graphic.setImageSize(80, 80);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 300, 560);
        shop.setShopDefaults();
        shop.setBlockNumber(shopBlockIndex);

        screen.getObjects().add(shop);
        return shop;
    }

    public static ConversationDoor addGoddessShop(Screen screen, short shopBlockIndex) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 940, 300);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(660);
        graphic.setImageY(520);
        graphic.setImageWidth(40);
        graphic.setImageHeight(60);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 940, 320);
        shop.setShopDefaults();
        shop.setBlockNumber(shopBlockIndex);

        screen.getObjects().add(shop);
        return shop;
    }

    public static ConversationDoor addRuinShop(Screen screen, short shopBlockIndex) {
        GraphicsTextureDraw graphic = new GraphicsTextureDraw(screen, 200, 380);

        graphic.setLayer(0);
        graphic.setImageFile("map*_1.png");
        graphic.setImageX(280);
        graphic.setImageY(180);
        graphic.setImageWidth(40);
        graphic.setImageHeight(60);
        graphic.setAnimation(0, 0, 1, 0);
        graphic.setCollision(HitTile.Air);
        graphic.setRGBAMax(0, 0, 0, 255);
        graphic.setArg23(1);

        screen.getObjects().add(graphic);

        ConversationDoor shop = new ConversationDoor(screen, 200, 400);
        shop.setShopDefaults();
        shop.setBlockNumber(shopBlockIndex);

        screen.getObjects().add(shop);
        return shop;
    }

    public static ConversationDoor addBirthStartStuff(Screen screen, short shopBlockIndex) {
        GraphicsTextureDraw shopGraphic = new GraphicsTextureDraw(screen, 140, 300);

        shopGraphic.setLayer(0);
        shopGraphic.setImageFile("map*_1.png");
        shopGraphic.setImageX(40);
        shopGraphic.setImageY(100);
        shopGraphic.setImageWidth(40);
        shopGraphic.setImageHeight(60);
        shopGraphic.setAnimation(0, 0, 1, 0);
        shopGraphic.setCollision(HitTile.Air);
        shopGraphic.setRGBAMax(0, 0, 0, 255);
        shopGraphic.setArg23(1);

        screen.getObjects().add(shopGraphic);

        ConversationDoor shop = new ConversationDoor(screen, 140, 320);
        shop.setShopDefaults();
        shop.setBlockNumber(shopBlockIndex);

        screen.getObjects().add(shop);

        GraphicsTextureDraw coverGraphic = new GraphicsTextureDraw(screen, 320, 300);

        coverGraphic.setLayer(0);
        coverGraphic.setImageFile("map*_1.png");
        coverGraphic.setImageX(600);
        coverGraphic.setImageY(140);
        coverGraphic.setImageWidth(60);
        coverGraphic.setImageHeight(60);
        coverGraphic.setAnimation(0, 0, 1, 0);
        coverGraphic.setCollision(HitTile.Air);
        coverGraphic.setRGBAMax(0, 0, 0, 255);
        coverGraphic.setArg23(1);

        screen.getObjects().add(coverGraphic);

        GraphicsTextureDraw tabletGraphic1 = new GraphicsTextureDraw(screen, 200, 380);

        tabletGraphic1.setLayer(0);
        tabletGraphic1.setImageFile("map*_1.png");
        tabletGraphic1.setImageX(320);
        tabletGraphic1.setImageY(0);
        tabletGraphic1.setImageWidth(40);
        tabletGraphic1.setImageHeight(40);
        tabletGraphic1.setAnimation(0, 0, 1, 0);
        tabletGraphic1.setCollision(HitTile.Air);
        tabletGraphic1.setRGBAMax(0, 0, 0, 255);
        tabletGraphic1.setArg23(1);

        screen.getObjects().add(tabletGraphic1);

        GraphicsTextureDraw tabletGraphic2 = new GraphicsTextureDraw(screen, 200, 420);

        tabletGraphic2.setLayer(0);
        tabletGraphic2.setImageFile("map*_1.png");
        tabletGraphic2.setImageX(360);
        tabletGraphic2.setImageY(0);
        tabletGraphic2.setImageWidth(40);
        tabletGraphic2.setImageHeight(20);
        tabletGraphic2.setAnimation(0, 0, 1, 0);
        tabletGraphic2.setCollision(HitTile.Air);
        tabletGraphic2.setRGBAMax(0, 0, 0, 255);
        tabletGraphic2.setArg23(1);

        screen.getObjects().add(tabletGraphic2);

        return shop;
    }

    public static ConversationDoor addRetroSurfaceShop(Screen screen, short shopBlockIndex) {
        GraphicsTextureDraw tent = new GraphicsTextureDraw(screen, 480,200);

        tent.setLayer(0);
        tent.setImageFile("map*_1.png");
        tent.setImageX(0);
        tent.setImageY(120);
        tent.setImageWidth(80);
        tent.setImageHeight(40);
        tent.setAnimation(0, 0, 1, 0);
        tent.setCollision(HitTile.Air);
        tent.setRGBAMax(0, 0, 0, 255);
        tent.setArg23(1);

        screen.getObjects().add(tent);

        GraphicsTextureDraw tent2 = new GraphicsTextureDraw(screen, 480, 240);

        tent2.setLayer(0);
        tent2.setImageFile("map*_1.png");
        tent2.setImageX(80);
        tent2.setImageY(120);
        tent2.setImageWidth(80);
        tent2.setImageHeight(40);
        tent2.setAnimation(0, 0, 1, 0);
        tent2.setCollision(HitTile.Air);
        tent2.setRGBAMax(0, 0, 0, 255);
        tent2.setArg23(1);

        screen.getObjects().add(tent2);

        ConversationDoor shop = new ConversationDoor(screen, 500, 240);
        shop.setShopDefaults();
        shop.setBlockNumber(shopBlockIndex);

        screen.getObjects().add(shop);
        return shop;
    }
}
