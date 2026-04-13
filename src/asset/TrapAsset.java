package asset;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import helpz.LoadSave;

public class TrapAsset {
    private static TrapAsset instance;
    public static final int WALL_WIDTH = 36;
    public static final int WALL_HEIGHT = 32;

    public static Map<Integer, Map<Integer,BufferedImage[]>> wallBuild = new HashMap<>();
    public static Map<Integer, Map<Integer, BufferedImage[]>> wallDestroyed = new HashMap<>();

    public static BufferedImage[] bombPlaced;
    public static BufferedImage[] bombCountDown;
    public static BufferedImage[] bombExploded;
    public static BufferedImage[] effect;
    
    public static Map<Integer, BufferedImage[]> spikes = new HashMap<>();

    public static TrapAsset getInstance() {
        if (instance == null) {
            instance = new TrapAsset();
        }
        return instance;
    }

    public void load() {
        for(int level = 1; level <= 4; level++) {
            loadWall(level, 0, "walls/U_" + level + "_Build.png", "walls/U_" + level + "_Destroy.png");
            loadWall(level, 1, "walls/D_" + level + "_Build.png", "walls/D_" + level + "_Destroy.png");
            loadWall(level, 2, "walls/S_" + level + "_Build.png", "walls/S_" + level + "_Destroy.png");
            loadWall(level, 3, "walls/S_" + level + "_Build.png", "walls/S_" + level + "_Destroy.png");
        }
        bombPlaced = LoadSave.getSpriteFrames("bom/1.png", 48, 48);
        bombCountDown = LoadSave.getSpriteFrames("bom/2.png", 48, 48);
        bombExploded = LoadSave.getSpriteFrames("bom/3.png", 48,48);
        effect = LoadSave.getSpriteFrames("bom/effect.png", 48, 48);

        for(int level = 1; level <= 4; level++){
            spikes.put(level, LoadSave.getSpriteFrames("spikes/"+ level +".png",32,32));
        }
    }

    private void loadWall(int level, int direction, String buildPath, String destroyPath) {
        wallBuild.putIfAbsent(level, new HashMap<>());
        wallDestroyed.putIfAbsent(level, new HashMap<>());

        wallBuild.get(level).put(direction, LoadSave.getSpriteFrames(buildPath, 36, 64));
        wallDestroyed.get(level).put(direction, LoadSave.getSpriteFrames(destroyPath, 36, 64));

    }

}
