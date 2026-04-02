package helpz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class LoadSave {

    public static BufferedImage getSprite(String path) {
        BufferedImage img = null;

        try (InputStream is = LoadSave.class.getResourceAsStream("/" + path)) {
            if (is != null) {
                img = ImageIO.read(is);
                if (img != null)
                    return img;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            File file = new File("res/" + path);
            if (file.exists())
                return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            File file = new File(path);
            if (file.exists())
                return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Could not find sprite: " + path);
        return null;
    }

    public static BufferedImage[] getSpriteFrames(String path, int frameWidth, int frameHeight) {
        BufferedImage sheet = getSprite(path);

        if (sheet == null)
            return new BufferedImage[0];

        int framesCount = sheet.getWidth() / frameWidth;
        BufferedImage[] frames = new BufferedImage[framesCount];

        for (int i = 0; i < framesCount; i++) {
            frames[i] = sheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
        }

        return frames;
    }

    public static int getFrameAmount(String path, int frameWidth) {
        BufferedImage sheet = getSprite(path);
        if (sheet == null) return 0;
        return sheet.getWidth() / frameWidth;
    }

public static BufferedImage[] getSpriteFramesFromFolder(String folderPath) {
    File folder = new File("res/" + folderPath);

    if (!folder.exists()) {
        folder = new File(folderPath);
    }

    if (!folder.exists() || !folder.isDirectory()) {
        System.out.println("Could not find sprite folder: " + folderPath);
        return new BufferedImage[0];
    }

    File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

    if (files == null || files.length == 0) {
        return new BufferedImage[0];
    }

    java.util.Arrays.sort(files, java.util.Comparator.comparingInt(file -> {
        String name = file.getName().replace(".png", "").trim();
        try {
            return Integer.parseInt(name);
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }));

    BufferedImage[] frames = new BufferedImage[files.length];

    for (int i = 0; i < files.length; i++) {
        try {
            frames[i] = javax.imageio.ImageIO.read(files[i]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    return frames;
    }
}