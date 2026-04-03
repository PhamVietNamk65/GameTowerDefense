package asset;

import static utils.Constants.Tiles.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class MapAsset {
    private static MapAsset instance;
    public static BufferedImage[] tiles;
    public static BufferedImage[] objects;
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
        objects = new BufferedImage[20];
        try {
            BufferedImage atlas = ImageIO.read(getClass().getResourceAsStream("/tile/1 Tiles/FieldsTileset.png"));
            tiles[0] = atlas.getSubimage(5 * ORIGIANLTILESIZE, 4 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE); // grass
            tiles[1] = atlas.getSubimage(5 * ORIGIANLTILESIZE, 3 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE); // road horizontal
            tiles[2] = atlas.getSubimage(7 * ORIGIANLTILESIZE, 4 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE); // road down
            tiles[3] = atlas.getSubimage(5 * ORIGIANLTILESIZE, 2 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE); // road left up 
            tiles[4] = atlas.getSubimage(5 * ORIGIANLTILESIZE, 0 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE); // road left down
            tiles[5] = atlas.getSubimage(7 * ORIGIANLTILESIZE, 2 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE); // road right up
            tiles[6] = atlas.getSubimage(7 * ORIGIANLTILESIZE, 0 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE); // road right down
            
            objects[0] = ImageIO.read(getClass().getResource("/tile/2 Objects/1.png"));
            objects[1] = ImageIO.read(getClass().getResource("/tile/2 Objects/Tree1.png")); 
            objects[2] = ImageIO.read(getClass().getResource("/tile/2 Objects/Tree2.png")); 
            objects[3] = ImageIO.read(getClass().getResource("/tile/2 Objects/Grass.png")); 
            objects[4] = ImageIO.read(getClass().getResource("/tile/2 Objects/Bush.png")); 
            objects[5] = ImageIO.read(getClass().getResource("/tile/2 Objects/Stone1.png")); 
            objects[6] = ImageIO.read(getClass().getResource("/tile/2 Objects/Stone2.png")); 
            objects[7] = ImageIO.read(getClass().getResource("/tile/2 Objects/Shadow1.png"));
            objects[8] = ImageIO.read(getClass().getResource("/tile/2 Objects/Shadow2.png"));
            objects[9] = ImageIO.read(getClass().getResource("/tile/2 Objects/Shadow3.png"));
            objects[10] = ImageIO.read(getClass().getResource("/tile/2 Objects/Flower.png"));
            objects[11] = ImageIO.read(getClass().getResource("/tile/2 Objects/Camp.png"));
            objects[12] = ImageIO.read(getClass().getResource("/tile/2 Objects/Lamp.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
