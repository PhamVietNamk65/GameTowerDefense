package asset;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class MapAsset {
    private static MapAsset instance;
    public static BufferedImage[] tiles;
    private MapAsset() {

    }
    public static MapAsset getInstance() {
        if(instance == null) {
            instance = new MapAsset();
        }
        return instance;
    }
    public void load() {
        tiles = new BufferedImage[10];
        try {
            BufferedImage atlas = ImageIO.read(getClass().getResourceAsStream("/tile/1 Tiles/FieldsTileset.png"));
            tiles[0] = atlas.getSubimage(5 * 32, 4 * 32, 32, 32); // grass
            tiles[1] = atlas.getSubimage(0 * 32, 0 * 32, 32, 32); // path
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
