package asset;

import static utils.Constants.Tiles.ORIGIANLTILESIZE;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class MapAsset {

    private static MapAsset instance;

    public HashMap<MapType, BufferedImage[]> tiles = new HashMap<>();
    public HashMap<MapType, BufferedImage[]> objects = new HashMap<>();

    private MapAsset(){}

    public static MapAsset getInstance() {
        if (instance == null) {
            instance = new MapAsset();
        }
        return instance;
    }

    public void load() {
        loadBasicMap();
        loadSnowMap();
    }

    private void loadBasicMap(){
        BufferedImage[] basic = new BufferedImage[10];
        BufferedImage[] obj = new BufferedImage[20];

        try {
            BufferedImage atlas = ImageIO.read(getClass().getResourceAsStream("/tile/1 Tiles/FieldsTileset.png"));

            basic[0] = atlas.getSubimage(5 * ORIGIANLTILESIZE, 4 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE);
            basic[1] = atlas.getSubimage(5 * ORIGIANLTILESIZE, 3 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE);
            basic[2] = atlas.getSubimage(7 * ORIGIANLTILESIZE, 4 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE); 
            basic[3] = atlas.getSubimage(5 * ORIGIANLTILESIZE, 2 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE);
            basic[4] = atlas.getSubimage(5 * ORIGIANLTILESIZE, 0 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE); 
            basic[5] = atlas.getSubimage(7 * ORIGIANLTILESIZE, 2 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE); 
            basic[6] = atlas.getSubimage(7 * ORIGIANLTILESIZE, 0 * ORIGIANLTILESIZE, ORIGIANLTILESIZE, ORIGIANLTILESIZE); 

            obj[0] = ImageIO.read(getClass().getResource("/tile/2 Objects/1.png"));
            obj[1] = ImageIO.read(getClass().getResource("/tile/2 Objects/Tree1.png")); 
            obj[2] = ImageIO.read(getClass().getResource("/tile/2 Objects/Tree2.png")); 
            obj[3] = ImageIO.read(getClass().getResource("/tile/2 Objects/Grass.png")); 
            obj[4] = ImageIO.read(getClass().getResource("/tile/2 Objects/Bush.png")); 
            obj[5] = ImageIO.read(getClass().getResource("/tile/2 Objects/Stone1.png")); 
            obj[6] = ImageIO.read(getClass().getResource("/tile/2 Objects/Stone2.png")); 
            obj[7] = ImageIO.read(getClass().getResource("/tile/2 Objects/Shadow1.png"));
            obj[8] = ImageIO.read(getClass().getResource("/tile/2 Objects/Shadow2.png"));
            obj[9] = ImageIO.read(getClass().getResource("/tile/2 Objects/Shadow3.png"));
            obj[10] = ImageIO.read(getClass().getResource("/tile/2 Objects/Flower.png"));
            obj[11] = ImageIO.read(getClass().getResource("/tile/2 Objects/Camp.png"));
            obj[12] = ImageIO.read(getClass().getResource("/tile/2 Objects/Lamp.png"));

            tiles.put(MapType.BASIC, basic);
            objects.put(MapType.BASIC, obj);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadSnowMap(){
        BufferedImage[] snow = new BufferedImage[10];
        BufferedImage[] obj = new BufferedImage[10];

        try {
            snow[0] = ImageIO.read(getClass().getResource("/tile/1 Tiles/snow/Snow.png"));
            snow[1] = ImageIO.read(getClass().getResource("/tile/1 Tiles/snow/Ice.png"));
            snow[2] = ImageIO.read(getClass().getResource("/tile/1 Tiles/snow/Round.png"));

            obj[0] = ImageIO.read(getClass().getResource("/tile/1 Tiles/snow/Snowman_0.png"));
            obj[1] = ImageIO.read(getClass().getResource("/tile/1 Tiles/snow/Snowman_3.png"));
            obj[2] = ImageIO.read(getClass().getResource("/tile/1 Tiles/snow/Snowman_4.png"));
            obj[3] = ImageIO.read(getClass().getResource("/tile/1 Tiles/snow/Tree_3.png"));
            obj[4] = ImageIO.read(getClass().getResource("/tile/1 Tiles/snow/Log.png"));

            tiles.put(MapType.SNOW, snow);
            objects.put(MapType.SNOW, obj);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}