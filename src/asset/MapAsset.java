package asset;

import static utils.Constants.Tiles.*;
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
            tiles[0] = atlas.getSubimage(5 * ORIGIANLTILESIZE, 4 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE); // grass
            tiles[1] = atlas.getSubimage(5 * ORIGIANLTILESIZE, 3 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE); // road horizontal
            tiles[2] = atlas.getSubimage(7 * ORIGIANLTILESIZE, 4 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE); // road down
            tiles[3] = atlas.getSubimage(5 * ORIGIANLTILESIZE, 2 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE); // road left up 
            tiles[4] = atlas.getSubimage(5 * ORIGIANLTILESIZE, 0 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE); // road left down
            tiles[5] = atlas.getSubimage(7 * ORIGIANLTILESIZE, 2 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE); // road right up
            tiles[6] = atlas.getSubimage(7 * ORIGIANLTILESIZE, 0 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE); // road right down
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
