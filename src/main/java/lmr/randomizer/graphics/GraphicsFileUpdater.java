package lmr.randomizer.graphics;

import lmr.randomizer.FileUtils;
import lmr.randomizer.HolidaySettings;
import lmr.randomizer.Settings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

public class GraphicsFileUpdater {
    public static final int EXISTING_FILE_WIDTH = 1024;
    public static final int EXISTING_FILE_HEIGHT = 512;
    public static final int GRAPHICS_VERSION = 6;

    public static boolean updateGraphicsFiles() {
        if(!update01Effect()) {
            return false;
        }
        return true;
    }

    private static boolean update01Effect() {
        BufferedImage custom;
        try {
            custom = ImageIO.read(FileUtils.class.getResource("01effect-custom.png"));
        }
        catch (IOException ex) {
            return false;
        }

        for(File graphicsPack : getGraphicsPacks()) {
            try {
                if(!backupGraphicsFile(graphicsPack)) {
                    return false;
                }
                File graphicsFile = new File(graphicsPack, "01effect.png");
                BufferedImage existing = ImageIO.read(graphicsFile);
                boolean updateGraphics = false;
                if(existing.getHeight() < 1024) {
                    updateGraphics = true;
                }
                else {
                    int version = existing.getRGB(1023, 1023);
                    if(version != GRAPHICS_VERSION) {
                        updateGraphics = true;
                    }
                }
                if(updateGraphics) {
                    FileUtils.logFlush("Updating graphics file: " + graphicsFile.getAbsolutePath());
                    // Hasn't been updated yet.
                    BufferedImage newImage = new BufferedImage(EXISTING_FILE_WIDTH, EXISTING_FILE_HEIGHT + custom.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    BufferedImage backupImage = ImageIO.read(new File(graphicsPack, "01effect.png.bak"));
                    Graphics2D graphics2D = newImage.createGraphics();
                    graphics2D.drawImage(backupImage, null, 0, 0); // Use backup to ensure no duplication of file
                    graphics2D.drawImage(custom, null, 0, EXISTING_FILE_HEIGHT);
                    graphics2D.dispose();
                    newImage.setRGB(1023, 1023, GRAPHICS_VERSION);
                    ImageIO.write(newImage, "png", graphicsFile);
                    FileUtils.log("Graphics file successfully updated");
                }
                else {
                    FileUtils.logFlush("Graphics file is already up to date: " + graphicsFile.getAbsolutePath());
                }
            }
            catch (IOException ex) {
                return false;
            }
        }
        return true;
    }

    private static boolean copyGraphicsFiles(File graphicsPack, File destinationFolder) {
        try {
            for(File graphicsFile : graphicsPack.listFiles()) {
                if(graphicsFile.isFile()) {
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(destinationFolder, graphicsFile.getName()));
                    Files.copy(graphicsFile.toPath(), fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            }
            return true;
        }
        catch (IOException ex) {
            return false;
        }
    }

    public static boolean updateGraphicsFilesForHalloween2019(String graphicsPack) {
        String graphicsBase = Settings.getLaMulanaBaseDir() + "/data/graphics";
        String halloweenFolderPath = graphicsBase + "/HALLOWEEN";
        File halloweenGraphicsFolder = new File(halloweenFolderPath);
        File graphicsBaseFolder = new File(graphicsBase, graphicsPack);
        if(halloweenGraphicsFolder.exists()) {
            halloweenGraphicsFolder.delete();
        }
        halloweenGraphicsFolder.mkdir();

        if(!copyGraphicsFiles(graphicsBaseFolder, halloweenGraphicsFolder)) {
            FileUtils.logFlush("Problem copying graphics from source folder " + graphicsPack);
            halloweenGraphicsFolder.delete();
            return false;
        }

        final java.util.List<String> modifiedFilesToCopy = Arrays.asList("02comenemy.png", "_banner.png",
                "eveg01.png", "eveg03.png", "eveg04.png", "eveg05.png", "eveg06.png", "eveg08.png", "eveg09.png",
                "eveg10.png", "eveg11.png", "eveg12.png", "eveg13.png", "eveg14.png", "eveg15.png", "eveg16.png",
                "eveg17.png", "eveg18.png", "eveg19.png", "eveg20.png", "map18_1.png");
        for(String file : modifiedFilesToCopy) {
            try {
                File graphicsFileToWrite = new File(halloweenFolderPath, file);
                BufferedImage modified;
                try {
                    modified = ImageIO.read(FileUtils.class.getResource("graphics/halloween/" + file));
                }
                catch (IOException ex) {
                    FileUtils.logFlush("Problem copying graphics file " + file);
                    halloweenGraphicsFolder.delete();
                    return false;
                }
                BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
                BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics2D = newImage.createGraphics();
                graphics2D.drawImage(modified, null, 0, 0); // Use backup to ensure no duplication of file
                graphics2D.dispose();

                ImageIO.write(newImage, "png", graphicsFileToWrite);
            }
            catch(IOException ex) {
                FileUtils.logFlush("Problem copying graphics file " + file);
                halloweenGraphicsFolder.delete();
                return false;
            }
        }

        if(!updateTitle01(halloweenFolderPath)) {
            return false;
        }
        if(!update01Menu(halloweenFolderPath)) {
            return false;
        }

        return true;
    }

    public static boolean updateGraphicsFilesForHalloween2021(String graphicsPack) {
        String graphicsBase = Settings.getLaMulanaBaseDir() + "/data/graphics";
        String halloweenFolderPath = graphicsBase + "/HALLOWEEN21";
        File halloweenGraphicsFolder = new File(halloweenFolderPath);
        File graphicsBaseFolder = new File(graphicsBase, graphicsPack);
        if(halloweenGraphicsFolder.exists()) {
            halloweenGraphicsFolder.delete();
        }
        halloweenGraphicsFolder.mkdir();

        if(!copyGraphicsFiles(graphicsBaseFolder, halloweenGraphicsFolder)) {
            FileUtils.logFlush("Problem copying graphics from source folder " + graphicsPack);
            halloweenGraphicsFolder.delete();
            return false;
        }

        final java.util.List<String> modifiedFilesToCopy = Arrays.asList("_banner.png",
//                "eveg07.png",
                "map18_1.png");
        for(String file : modifiedFilesToCopy) {
            try {
                File graphicsFileToWrite = new File(halloweenFolderPath, file);
                BufferedImage modified;
                try {
                    modified = ImageIO.read(FileUtils.class.getResource(getPathFromSettings(file)));
                }
                catch (IOException ex) {
                    FileUtils.logFlush("Problem copying graphics file " + file);
                    halloweenGraphicsFolder.delete();
                    return false;
                }
                BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
                BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics2D = newImage.createGraphics();
                graphics2D.drawImage(modified, null, 0, 0); // Use backup to ensure no duplication of file
                graphics2D.dispose();

                ImageIO.write(newImage, "png", graphicsFileToWrite);
            }
            catch(IOException ex) {
                FileUtils.logFlush("Problem copying graphics file " + file);
                halloweenGraphicsFolder.delete();
                return false;
            }
        }

        if(!updateTitle01(halloweenFolderPath)) {
            return false;
        }
        if(!update01Menu_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!update02ComEnemy_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateB08_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateEveg00_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateEveg01_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateEveg02_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateEveg03_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateEveg04_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateEveg05_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateEveg06_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateEveg08_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateEveg09_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateEveg10_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateEveg11_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateEveg12_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateEveg13_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateEveg14_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateEveg15_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateEveg16_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateEveg17_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!updateEveg18_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!update00Item_Halloween2021(halloweenFolderPath)) {
            return false;
        }
        if(!update01Effect_Halloween2021(halloweenFolderPath)) {
            return false;
        }

        return true;
    }

    public static boolean updateGraphicsFilesForFools2020(String graphicsPack) {
        String graphicsBase = Settings.getLaMulanaBaseDir() + "/data/graphics";
        String foolFolderPath = graphicsBase + "/FOOLS2020";
        File foolGraphicsFolder = new File(foolFolderPath);
        File graphicsBaseFolder = new File(graphicsBase, graphicsPack);
        if(foolGraphicsFolder.exists()) {
            foolGraphicsFolder.delete();
        }
        foolGraphicsFolder.mkdir();

        if(!copyGraphicsFiles(graphicsBaseFolder, foolGraphicsFolder)) {
            FileUtils.logFlush("Problem copying graphics from source folder " + graphicsPack);
            foolGraphicsFolder.delete();
            return false;
        }

        final List<String> modifiedFilesToCopy = Arrays.asList("_banner.png");
        for(String file : modifiedFilesToCopy) {
            try {
                File graphicsFileToWrite = new File(foolFolderPath, file);
                BufferedImage modified;
                try {
                    modified = ImageIO.read(FileUtils.class.getResource("graphics/fools2020/" + file));
                }
                catch (IOException ex) {
                    FileUtils.logFlush("Problem copying graphics file " + file);
                    foolGraphicsFolder.delete();
                    return false;
                }
                BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
                BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics2D = newImage.createGraphics();
                graphics2D.drawImage(modified, null, 0, 0); // Use backup to ensure no duplication of file
                graphics2D.dispose();

                ImageIO.write(newImage, "png", graphicsFileToWrite);
            }
            catch(IOException ex) {
                FileUtils.logFlush("Problem copying graphics file " + file);
                foolGraphicsFolder.delete();
                return false;
            }
        }

        if(!update01Menu(foolFolderPath)) {
            return false;
        }

        return true;
    }

    private static boolean updateTitle01(String folderPath) {
        String file = "title01.png";
        try {
            File graphicsFileToWrite = new File(folderPath, file);
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            if(HolidaySettings.isHalloween2021Mode()) {
                drawImageWithMissingSection(existingImage, graphics2D, 942, 740, 16, 20);
                graphics2D.drawImage(getCustomGraphic("mapcandy_small.png"), null, 944, 740);
            }
            else {
                drawImageWithMissingSection(existingImage, graphics2D, 942, 740, 16, 20);
                graphics2D.drawImage(getCustomGraphic("candycornoflife_small.png"), null, 942, 740);
            }
            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file " + file);
            return false;
        }
        return true;
    }

    private static boolean update01Menu(String folderPath) {
        String file = "01menu.png";
        String filepath = getPathFromSettings(file);

        try {
            File graphicsFileToWrite = new File(folderPath, file);
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            if(HolidaySettings.isHalloween2019Mode()) {
                BufferedImage modified;
                try {
                    modified = ImageIO.read(FileUtils.class.getResource(filepath));
                }
                catch (IOException ex) {
                    return false;
                }

                drawImageWithMissingSection(existingImage, graphics2D, 780, 320, 40, 40);
                graphics2D.drawImage(modified.getSubimage(780, 320, 40, 40), null, 780, 320);
            }
            else if(HolidaySettings.isFools2020Mode()) {
                final int itemsBeginX = 620;
                final int itemsBeginY = 0;
                final int itemsEndY = 440;
                final int fullWidth = 1024;
                final int fullHeight = 1024;
                BufferedImage leftOfItems = existingImage.getSubimage(0, 0, itemsBeginX, fullHeight);
                BufferedImage belowItems = existingImage.getSubimage(itemsBeginX, itemsEndY, fullWidth - itemsBeginX, fullHeight - itemsEndY);
                graphics2D.drawImage(leftOfItems, null, 0, 0);
                graphics2D.drawImage(belowItems, null, itemsBeginX, itemsEndY);

                for(int verticalIndex = 0; verticalIndex < 11; verticalIndex++) {
                    for(int horizontalIndex = 0; horizontalIndex < 10; horizontalIndex++) {
                        int itemDrawBeginX = itemsBeginX + horizontalIndex * 40;
                        int itemDrawBeginY = itemsBeginY + verticalIndex * 40;
                        int sourceGraphicsBeginX = itemDrawBeginX;
                        int sourceGraphicsBeginY = itemDrawBeginY;
                        if(horizontalIndex == 7 && verticalIndex == 3) {
                            // Heatproof case
                            sourceGraphicsBeginX = itemsBeginX + 240;
                            sourceGraphicsBeginY = itemsBeginY + 200;
                        }
                        else if(horizontalIndex == 6 && verticalIndex == 5) {
                            // Scriptures
                            sourceGraphicsBeginX = itemsBeginX + 280;
                            sourceGraphicsBeginY = itemsBeginY + 120;
                        }
                        else if(horizontalIndex == 4 && verticalIndex == 8) {
                            // Secret Treasure of Life
                            sourceGraphicsBeginX = itemsBeginX + 120;
                            sourceGraphicsBeginY = itemsBeginY + 200;
                        }
                        if(horizontalIndex != 0 || verticalIndex != 7) {
                            BufferedImage itemGraphic = existingImage.getSubimage(sourceGraphicsBeginX, sourceGraphicsBeginY, 40, 40);
                            graphics2D.drawImage(itemGraphic, null, itemDrawBeginX, itemDrawBeginY);
                        }
                    }
                }
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file " + file);
            return false;
        }
        return true;
    }

    private static boolean update01Menu_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "01menu.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getMenuGraphicsFileEntries();

            GraphicsFileEntry existingGraphicsFileEntry = graphicsFileEntries.remove(GraphicsFileData.MAP);
            graphics2D.drawImage(getCustomGraphic("mapcandy.png"), null, existingGraphicsFileEntry.getX(), existingGraphicsFileEntry.getY());

            existingGraphicsFileEntry = graphicsFileEntries.remove(GraphicsFileData.SECRET_TREASURE_OF_LIFE);
            graphics2D.drawImage(getCustomGraphic("mapcandy.png"), null, existingGraphicsFileEntry.getX(), existingGraphicsFileEntry.getY());

            GraphicsFileEntry yellowMedicine = graphicsFileEntries.remove(GraphicsFileData.MEDICINE_OF_THE_MIND_YELLOW);
            GraphicsFileEntry redMedicine = graphicsFileEntries.remove(GraphicsFileData.MEDICINE_OF_THE_MIND_RED);
            BufferedImage yellowMedicineSubimage = existingImage.getSubimage(yellowMedicine.getX(), yellowMedicine.getY(), yellowMedicine.getWidth(), yellowMedicine.getHeight());
            BufferedImage redMedicineSubimage = existingImage.getSubimage(redMedicine.getX(), redMedicine.getY(), redMedicine.getWidth(), redMedicine.getHeight());
            graphics2D.drawImage(yellowMedicineSubimage, null, redMedicine.getX(), redMedicine.getY());
            graphics2D.drawImage(redMedicineSubimage, null, yellowMedicine.getX(), yellowMedicine.getY());

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file 01menu.png");
            return false;
        }
        return true;
    }

    private static boolean update02ComEnemy_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "02comenemy.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getCommonEnemyGraphicsFileEntries();

            GraphicsFileEntry existingGraphicsFileEntry = graphicsFileEntries.remove(GraphicsFileData.MUDMAN_FLAT);
            graphics2D.drawImage(getCustomGraphic("mudman_flat.png"), null, existingGraphicsFileEntry.getX(), existingGraphicsFileEntry.getY());

            existingGraphicsFileEntry = graphicsFileEntries.remove(GraphicsFileData.MUDMAN_WALKING);
            graphics2D.drawImage(getCustomGraphic("mudman_walking.png"), null, existingGraphicsFileEntry.getX(), existingGraphicsFileEntry.getY());

            existingGraphicsFileEntry = graphicsFileEntries.remove(GraphicsFileData.MUDMAN_EXTRA_1);
            graphics2D.drawImage(getCustomGraphic("mudman_extra1.png"), null, existingGraphicsFileEntry.getX(), existingGraphicsFileEntry.getY());

            existingGraphicsFileEntry = graphicsFileEntries.remove(GraphicsFileData.MUDMAN_EXTRA_2);
            graphics2D.drawImage(getCustomGraphic("mudman_extra2.png"), null, existingGraphicsFileEntry.getX(), existingGraphicsFileEntry.getY());

            existingGraphicsFileEntry = graphicsFileEntries.remove(GraphicsFileData.TOG);
            graphics2D.drawImage(getCustomGraphic("tog.png"), null, existingGraphicsFileEntry.getX(), existingGraphicsFileEntry.getY());

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file 02comenemy.png");
            return false;
        }
        return true;
    }

    private static boolean updateB08_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "b08.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getB08GraphicsFileEntries();

            GraphicsFileEntry yellowMedicine = graphicsFileEntries.remove(GraphicsFileData.BOSS_MOTHER_POUR_YELLOW_MEDICINE);
            GraphicsFileEntry redMedicine = graphicsFileEntries.remove(GraphicsFileData.BOSS_MOTHER_POUR_RED_MEDICINE);
            BufferedImage yellowMedicineSubimage = existingImage.getSubimage(yellowMedicine.getX(), yellowMedicine.getY(), yellowMedicine.getWidth(), yellowMedicine.getHeight());
            BufferedImage redMedicineSubimage = existingImage.getSubimage(redMedicine.getX(), redMedicine.getY(), redMedicine.getWidth(), redMedicine.getHeight());
            graphics2D.drawImage(yellowMedicineSubimage, null, redMedicine.getX(), redMedicine.getY());
            graphics2D.drawImage(redMedicineSubimage, null, yellowMedicine.getX(), yellowMedicine.getY());

            GraphicsFileEntry existingGraphicsFileEntry = graphicsFileEntries.remove(GraphicsFileData.BOSS_MOTHER_SECRET_TREASURE_OF_LIFE_SMALL);
            graphics2D.drawImage(getCustomGraphic("mapcandy_small.png"), null, existingGraphicsFileEntry.getX() + 2, existingGraphicsFileEntry.getY() + 2);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file b08.png");
            return false;
        }
        return true;
    }

    private static boolean updateEveg00_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "eveg00.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getEveg00GraphicsFileEntries();

            graphicsFileEntries.remove(GraphicsFileData.ANTLION);
            graphicsFileEntries.remove(GraphicsFileData.RED_SKELETON);
            graphics2D.drawImage(getCustomGraphic("witch_camio.png"), null, 0, 0);
            graphics2D.drawImage(getCustomGraphic("ghost.png"), null, 0, 40);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file eveg00.png");
            return false;
        }
        return true;
    }

    private static boolean updateEveg01_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "eveg01.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getEveg01GraphicsFileEntries();

            graphicsFileEntries.remove(GraphicsFileData.SNAKE);
            graphicsFileEntries.remove(GraphicsFileData.SURFACE_MIRROR_DOOR_COVER);
            graphicsFileEntries.remove(GraphicsFileData.SURFACE_TENT_OPEN_ANIMATION);
            graphicsFileEntries.remove(GraphicsFileData.TIME_ATTACK_2);
            graphicsFileEntries.remove(GraphicsFileData.COCKATRICE_1);
            graphicsFileEntries.remove(GraphicsFileData.COCKATRICE_2);
            graphicsFileEntries.remove(GraphicsFileData.COCKATRICE_3);
            graphics2D.drawImage(getCustomGraphic("witch_camio.png"), null, 0, 0);
            graphics2D.drawImage(getCustomGraphic("ghost.png"), null, 0, 40);
            graphics2D.drawImage(getCustomGraphic("trapdoor_nightsurface.png"), null, 0, 160);
            graphics2D.drawImage(getCustomGraphic("halloweensign.png"), null, 160, 160);
            graphics2D.drawImage(getCustomGraphic("nightsurface_door_cover.png"), null, 900, 60);
            graphics2D.drawImage(getCustomGraphic("nightsurface_tent.png"), null, 800, 0);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file eveg01.png");
            return false;
        }
        return true;
    }

    private static boolean updateEveg02_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "eveg02.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getEveg02GraphicsFileEntries();

            graphicsFileEntries.remove(GraphicsFileData.FIST);
            graphicsFileEntries.remove(GraphicsFileData.GHOST);
            graphics2D.drawImage(getCustomGraphic("witch_camio.png"), null, 0, 0);
            graphics2D.drawImage(getCustomGraphic("ghost.png"), null, 0, 40);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file eveg02.png");
            return false;
        }
        return true;
    }

    private static boolean updateEveg03_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "eveg03.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getEveg03GraphicsFileEntries();

            graphicsFileEntries.remove(GraphicsFileData.CATBALL_CAT);
            graphicsFileEntries.remove(GraphicsFileData.CATBALL_BALL);
            graphicsFileEntries.remove(GraphicsFileData.CATBALL_BOTH);
            graphicsFileEntries.remove(GraphicsFileData.BENNU_1);
            graphics2D.drawImage(getCustomGraphic("catball_cat.png"), null, 0, 0);
            graphics2D.drawImage(getCustomGraphic("catball_withball.png"), null, 240, 40);
            graphics2D.drawImage(getCustomGraphic("ghost.png"), null, 0, 40);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file eveg03.png");
            return false;
        }
        return true;
    }

    private static boolean updateEveg04_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "eveg04.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getEveg04GraphicsFileEntries();

            graphicsFileEntries.remove(GraphicsFileData.GYONIN);
            graphicsFileEntries.remove(GraphicsFileData.GYONIN_WARRIOR);
            graphics2D.drawImage(getCustomGraphic("witch_camio.png"), null, 0, 0);
            graphics2D.drawImage(getCustomGraphic("ghost.png"), null, 0, 40);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.drawImage(getCustomGraphic("trapdoor_spring.png"), null, 720, 180);

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file eveg04.png");
            return false;
        }
        return true;
    }

    private static boolean updateEveg05_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "eveg05.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getEveg05GraphicsFileEntries();

            graphicsFileEntries.remove(GraphicsFileData.INFERNO_LAVA_BALL_1);
            graphicsFileEntries.remove(GraphicsFileData.KAKOUJUU_1);
            graphics2D.drawImage(getCustomGraphic("witch_camio.png"), null, 0, 0);
            graphics2D.drawImage(getCustomGraphic("ghost.png"), null, 0, 40);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file eveg05.png");
            return false;
        }
        return true;
    }

    private static boolean updateEveg06_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "eveg06.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getEveg06GraphicsFileEntries();

            graphicsFileEntries.remove(GraphicsFileData.NAGA_1);
            graphicsFileEntries.remove(GraphicsFileData.NAGA_2);
            graphicsFileEntries.remove(GraphicsFileData.BLOB_HAND);
            graphicsFileEntries.remove(GraphicsFileData.GARUDA_1);
            graphicsFileEntries.remove(GraphicsFileData.GARUDA_2);
            graphics2D.drawImage(getCustomGraphic("witch_camio.png"), null, 0, 0);
            graphics2D.drawImage(getCustomGraphic("ghost.png"), null, 0, 40);
            graphics2D.drawImage(getCustomGraphic("witch1.png"), null, 0, 80);
            graphics2D.drawImage(getCustomGraphic("witch1_projectile.png"), null, 280, 0);
            graphics2D.drawImage(getCustomGraphic("trapdoor_extinction.png"), null, 0, 350);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file eveg06.png");
            return false;
        }
        return true;
    }

    private static boolean updateEveg08_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "eveg08.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getEveg08GraphicsFileEntries();

            graphicsFileEntries.remove(GraphicsFileData.BONNACON_1);
            graphicsFileEntries.remove(GraphicsFileData.BONNACON_2);
            graphicsFileEntries.remove(GraphicsFileData.BONNACON_PROJECTILE);
            graphicsFileEntries.remove(GraphicsFileData.FLOWER_FACED_SNOUTER);
            graphicsFileEntries.remove(GraphicsFileData.FLOWER_FACED_SNOUTER_PROJECTILE);
            graphicsFileEntries.remove(GraphicsFileData.MONOCOLI);
            graphicsFileEntries.remove(GraphicsFileData.JIANGSHI);
            graphics2D.drawImage(getCustomGraphic("witch_camio.png"), null, 0, 0);
            graphics2D.drawImage(getCustomGraphic("ghost.png"), null, 0, 40);
            graphics2D.drawImage(getCustomGraphic("witch1.png"), null, 0, 80);
            graphics2D.drawImage(getCustomGraphic("witch1_projectile.png"), null, 280, 0);
            graphics2D.drawImage(getCustomGraphic("witch2.png"), null, 0, 160);
            graphics2D.drawImage(getCustomGraphic("trapdoor_endless.png"), null, 0, 400);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file eveg08.png");
            return false;
        }
        return true;
    }

    private static boolean updateEveg09_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "eveg09.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getEveg09GraphicsFileEntries();

            graphicsFileEntries.remove(GraphicsFileData.HUNDUN_1);
            graphicsFileEntries.remove(GraphicsFileData.HUNDUN_2);
            graphicsFileEntries.remove(GraphicsFileData.ENKIDU_1);
            graphicsFileEntries.remove(GraphicsFileData.ENKIDU_2);
            graphicsFileEntries.remove(GraphicsFileData.ENKIDU_FLAME);
            graphicsFileEntries.remove(GraphicsFileData.HANUMAN_1);
            graphicsFileEntries.remove(GraphicsFileData.HANUMAN_2);
            graphicsFileEntries.remove(GraphicsFileData.PAN_1);
            graphicsFileEntries.remove(GraphicsFileData.PAN_2);
            graphics2D.drawImage(getCustomGraphic("witch_camio.png"), null, 0, 0);
            graphics2D.drawImage(getCustomGraphic("ghost.png"), null, 0, 40);
            graphics2D.drawImage(getCustomGraphic("witch2.png"), null, 0, 160);
            graphics2D.drawImage(getCustomGraphic("witch3.png"), null, 0, 240);
            graphics2D.drawImage(getCustomGraphic("witch3_projectile_single.png"), null, 240, 120);
            graphics2D.drawImage(getCustomGraphic("witch3_projectile_split.png"), null, 280, 40);
            graphics2D.drawImage(getCustomGraphic("trapdoor_shrine.png"), null, 0, 600);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file eveg09.png");
            return false;
        }
        return true;
    }

    private static boolean updateEveg10_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "eveg10.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getEveg10GraphicsFileEntries();

            graphicsFileEntries.remove(GraphicsFileData.KUI);
            graphicsFileEntries.remove(GraphicsFileData.ASP_1);
            graphicsFileEntries.remove(GraphicsFileData.ASP_2);
            graphics2D.drawImage(getCustomGraphic("catball_cat.png"), null, 0, 0);
            graphics2D.drawImage(getCustomGraphic("catball_withball.png"), null, 240, 40);
            graphics2D.drawImage(getCustomGraphic("ghost.png"), null, 0, 40);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file eveg10.png");
            return false;
        }
        return true;
    }

    private static boolean updateEveg11_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "eveg11.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getEveg11GraphicsFileEntries();

            graphicsFileEntries.remove(GraphicsFileData.DIJIANG_1);
            graphicsFileEntries.remove(GraphicsFileData.DIJIANG_2);
            graphicsFileEntries.remove(GraphicsFileData.BAIZE_1);
            graphicsFileEntries.remove(GraphicsFileData.BAIZE_2);
            graphicsFileEntries.remove(GraphicsFileData.KESERAN);
            graphicsFileEntries.remove(GraphicsFileData.ICE_WIZARD);
            graphics2D.drawImage(getCustomGraphic("ghost.png"), null, 0, 40);
            graphics2D.drawImage(getCustomGraphic("witch2.png"), null, 0, 160);
            graphics2D.drawImage(getCustomGraphic("catball_cat.png"), null, 0, 0);
            graphics2D.drawImage(getCustomGraphic("catball_withball.png"), null, 240, 40);
            graphics2D.drawImage(getCustomGraphic("trapdoor_graveyard.png"), null, 750, 0);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file eveg11.png");
            return false;
        }
        return true;
    }

    private static boolean updateEveg12_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "eveg12.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getEveg12GraphicsFileEntries();

            GraphicsFileEntry existingGraphicsFileEntry = graphicsFileEntries.remove(GraphicsFileData.YOWIE);
            graphics2D.drawImage(getCustomGraphic("spider.png"), null, existingGraphicsFileEntry.getX(), existingGraphicsFileEntry.getY());

            existingGraphicsFileEntry = graphicsFileEntries.remove(GraphicsFileData.YOWIE_DYING);
            graphics2D.drawImage(getCustomGraphic("spider_dying.png"), null, existingGraphicsFileEntry.getX(), existingGraphicsFileEntry.getY());

            graphicsFileEntries.remove(GraphicsFileData.TROLL_1);
            graphicsFileEntries.remove(GraphicsFileData.TROLL_2);
            graphics2D.drawImage(getCustomGraphic("witch_camio.png"), null, 0, 0);
            graphics2D.drawImage(getCustomGraphic("ghost.png"), null, 0, 40);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file eveg12.png");
            return false;
        }
        return true;
    }

    private static boolean updateEveg13_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "eveg13.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getEveg13GraphicsFileEntries();

            graphicsFileEntries.remove(GraphicsFileData.A_BAO_A_QU_1);
            graphicsFileEntries.remove(GraphicsFileData.A_BAO_A_QU_2);
            graphicsFileEntries.remove(GraphicsFileData.CHONCHON);
            graphicsFileEntries.remove(GraphicsFileData.ANDRAS_RIDER_1);
            graphicsFileEntries.remove(GraphicsFileData.ANDRAS_RIDER_2);
            graphicsFileEntries.remove(GraphicsFileData.ANDRAS_PROJECTILES);
            graphics2D.drawImage(getCustomGraphic("ghost.png"), null, 0, 40);
            graphics2D.drawImage(getCustomGraphic("witch2.png"), null, 0, 160);
            graphics2D.drawImage(getCustomGraphic("catball_cat.png"), null, 0, 0);
            graphics2D.drawImage(getCustomGraphic("catball_withball.png"), null, 240, 40);
            graphics2D.drawImage(getCustomGraphic("trapdoor_goddess.png"), null, 0, 420);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file eveg13.png");
            return false;
        }
        return true;
    }

    private static boolean updateEveg14_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "eveg14.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getEveg14GraphicsFileEntries();

            graphicsFileEntries.remove(GraphicsFileData.RUIN_SALAMANDER_1);
            graphicsFileEntries.remove(GraphicsFileData.RUIN_BLACK_DOG_1);
            graphics2D.drawImage(getCustomGraphic("witch_camio.png"), null, 0, 0);
            graphics2D.drawImage(getCustomGraphic("ghost.png"), null, 0, 40);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.drawImage(getCustomGraphic("trapdoor_ruin.png"), null, 0, 580);
            graphics2D.drawImage(getCustomGraphic("trapdoor_ruin2.png"), null, 160, 580);

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file eveg14.png");
            return false;
        }
        return true;
    }

    private static boolean updateEveg15_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "eveg15.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getEveg15GraphicsFileEntries();

            graphicsFileEntries.remove(GraphicsFileData.BIRTH_SWORDBIRD);
            graphicsFileEntries.remove(GraphicsFileData.BIRTH_ELEPHANT);
            graphics2D.drawImage(getCustomGraphic("witch_camio.png"), null, 0, 0);
            graphics2D.drawImage(getCustomGraphic("ghost.png"), null, 0, 40);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file eveg15.png");
            return false;
        }
        return true;
    }

    private static boolean updateEveg16_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "eveg16.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getEveg16GraphicsFileEntries();

            graphicsFileEntries.remove(GraphicsFileData.AMON_1);
            graphicsFileEntries.remove(GraphicsFileData.DEVIL);
            graphics2D.drawImage(getCustomGraphic("ghost.png"), null, 0, 40);
            graphics2D.drawImage(getCustomGraphic("witch2.png"), null, 0, 160);
            graphics2D.drawImage(getCustomGraphic("catball_cat.png"), null, 0, 0);
            graphics2D.drawImage(getCustomGraphic("catball_withball.png"), null, 240, 40);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.drawImage(getCustomGraphic("trapdoor_dimensional.png"), null, 0, 808);

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file eveg16.png");
            return false;
        }
        return true;
    }

    private static boolean updateEveg17_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "eveg17.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getEveg17GraphicsFileEntries();

            graphicsFileEntries.remove(GraphicsFileData.RETRO_ANTLION);
            graphics2D.drawImage(getCustomGraphic("witch_camio.png"), null, 0, 0);

            GraphicsFileEntry existingGraphicsFileEntry = graphicsFileEntries.remove(GraphicsFileData.RETRO_TOG);
            graphics2D.drawImage(getCustomGraphic("retro_tog.png"), null, existingGraphicsFileEntry.getX(), existingGraphicsFileEntry.getY());

            graphics2D.drawImage(getCustomGraphic("gate_of_time_extras.png"), null, 0, 340);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file eveg17.png");
            return false;
        }
        return true;
    }

    private static boolean updateEveg18_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "eveg18.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            Map<Integer, GraphicsFileEntry> graphicsFileEntries = GraphicsFileData.getEveg18GraphicsFileEntries();

            graphicsFileEntries.remove(GraphicsFileData.SNAKE);
            graphicsFileEntries.remove(GraphicsFileData.SURFACE_TENT_OPEN_ANIMATION);
            graphicsFileEntries.remove(GraphicsFileData.TIME_ATTACK_2);
            graphicsFileEntries.remove(GraphicsFileData.COCKATRICE_1);
            graphicsFileEntries.remove(GraphicsFileData.COCKATRICE_2);
            graphicsFileEntries.remove(GraphicsFileData.COCKATRICE_3);
            graphics2D.drawImage(getCustomGraphic("witch_camio.png"), null, 0, 0);
            graphics2D.drawImage(getCustomGraphic("ghost.png"), null, 0, 40);
            graphics2D.drawImage(getCustomGraphic("trapdoor_nightsurface.png"), null, 0, 160);
            graphics2D.drawImage(getCustomGraphic("halloweensign.png"), null, 160, 160);
            graphics2D.drawImage(getCustomGraphic("nightsurface_tent.png"), null, 800, 0);

            for(GraphicsFileEntry graphicsFileEntry : graphicsFileEntries.values()) {
                BufferedImage subimage = existingImage.getSubimage(graphicsFileEntry.getX(), graphicsFileEntry.getY(), graphicsFileEntry.getWidth(), graphicsFileEntry.getHeight());
                graphics2D.drawImage(subimage, null, graphicsFileEntry.getX(), graphicsFileEntry.getY());
            }

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file eveg18.png");
            return false;
        }
        return true;
    }

    private static boolean update00Item_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "00item.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            drawImageWithMissingSection(existingImage, graphics2D, 362, 170, 282, 56); // Everything but section with graphic to swap

            graphics2D.drawImage(existingImage.getSubimage(362, 170, 226, 28), null, 362, 170); // Left part of top row
            graphics2D.drawImage(existingImage.getSubimage(614, 170, 28, 28), null, 614, 170); // Right part of top row
            graphics2D.drawImage(existingImage.getSubimage(390, 198, 252, 28), null, 390, 198); // Right part of bottom row

            graphics2D.drawImage(existingImage.getSubimage(362, 198, 28, 28), null, 586, 170); // Red medicine to yellow
            graphics2D.drawImage(existingImage.getSubimage(586, 170, 28, 28), null, 362, 198); // Yellow medicine to red

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file 00item.png");
            return false;
        }
        return true;
    }

    private static boolean update01Effect_Halloween2021(String folderPath) {
        try {
            File graphicsFileToWrite = new File(folderPath, "01effect.png");
            BufferedImage existingImage = ImageIO.read(graphicsFileToWrite);
            BufferedImage newImage = new BufferedImage(existingImage.getWidth(), existingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();

            BufferedImage subimage = existingImage.getSubimage(0, 0, EXISTING_FILE_WIDTH, 712);
            graphics2D.drawImage(subimage, null, 0, 0);

            graphics2D.drawImage(getCustomGraphic("skull.png"), null, 0, 712);
            graphics2D.drawImage(getCustomGraphic("spikes.png"), null, 120, 712);

            graphics2D.dispose();

            ImageIO.write(newImage, "png", graphicsFileToWrite);
        }
        catch(IOException ex) {
            FileUtils.logFlush("Problem copying graphics file 01menu.png");
            return false;
        }
        return true;
    }

    private static BufferedImage getCustomGraphic(String filename) throws IOException {
        return ImageIO.read(FileUtils.class.getResource(getPathFromSettings(filename)));
    }

    private static boolean backupGraphicsFile(File graphicsPack) {
        try {
            File backup = new File(graphicsPack, "01effect.png.bak");
            if(!backup.exists()) {
                FileOutputStream fileOutputStream = new FileOutputStream(backup);
                Files.copy(new File(graphicsPack, "01effect.png").toPath(), fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            return true;
        }
        catch (IOException ex) {
            return false;
        }
    }

    private static void drawImageWithMissingSection(BufferedImage existingImage, Graphics2D graphics2D, int x, int y, int width, int height) throws IOException{
        BufferedImage leftPart = existingImage.getSubimage(0, 0, x, existingImage.getHeight());
        BufferedImage topPart = existingImage.getSubimage(x, 0, width, y);
        BufferedImage rightPart = existingImage.getSubimage(x + width, 0, existingImage.getWidth() - x - width, existingImage.getHeight());
        BufferedImage bottomPart = existingImage.getSubimage(x, y + height, width, existingImage.getHeight() - y - height);
        graphics2D.drawImage(leftPart, null, 0, 0);
        graphics2D.drawImage(topPart, null, x, 0);
        graphics2D.drawImage(rightPart, null, x + width, 0);
        graphics2D.drawImage(bottomPart, null, x, y + height);
    }

    private static String getPathFromSettings(String file) {
        if(HolidaySettings.isHalloween2019Mode()) {
            return "graphics/halloween/" + file;
        }
        if(HolidaySettings.isHalloween2021Mode()) {
            return "graphics/halloween2021/" + file;
        }
        if(HolidaySettings.isFools2020Mode()) {
            return "graphics/fools2020/" + file;
        }
        return  "";
    }

    private static List<File> getGraphicsPacks() {
        File graphicsFolder = new File(Settings.getLaMulanaBaseDir() + "/data/graphics");
        if(graphicsFolder.exists() && graphicsFolder.isDirectory()) {
            List<File> graphicsSubfolders = new ArrayList<>();
            for(File file : graphicsFolder.listFiles()) {
                if(file.isDirectory()) {
                    graphicsSubfolders.add(file);
                }
            }
            return graphicsSubfolders;
        }
        return new ArrayList<>(0);
    }
}
