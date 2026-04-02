package levels;
import static utils.Constants.Tiles.TILE_SIZE;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

import asset.MapAsset;
import entity.TowerSlot;
import system.PathFinder;

public class Level {
    private int levelid;

    private int[][] pathMap;
    private int[][] towerMap;
    private int[][] groundMap;
    private int[][] objectsMap;

    private Point[] path;
    
    private ArrayList<TowerSlot> allSlots = new ArrayList<>();
    private ArrayList<TowerSlot> activeSlots;
    public Level(int levelid){
        this.levelid = levelid;
        loadLevel();
    }
    private void loadLevel() {
        groundMap = LoadMap.loadLevel("res/Map/Level" + levelid + "/ground.csv");
        pathMap  = LoadMap.loadLevel("res/Map/Level" + levelid + "/path.csv");
        towerMap = LoadMap.loadLevel("res/Map/Level" + levelid + "/tower.csv");
        objectsMap = LoadMap.loadLevel("res/Map/Level" + levelid + "/objects.csv");
        path = PathFinder.buildPath(pathMap);
        generateTowerSlots();
        activeSlots = new ArrayList<>(allSlots);
    }
    public void update() {
       
    }
    private void generateTowerSlots() {
        for (int y = 0; y < towerMap.length; y++) {
            for (int x = 0; x < towerMap[y].length; x++) {

                if (towerMap[y][x] == 1) {
                    allSlots.add(new TowerSlot(x, y));
                }
            }
        }
    }

    public void removeSlot(TowerSlot slot) {
        if (slot == null) return;
        // Xóa tất cả slot có cùng tọa độ x, y với slot được chọn
        activeSlots.removeIf(s -> s.getX() == slot.getX() && s.getY() == slot.getY());
    }
    
    public void addBackSlot(int x, int y) {
        for (TowerSlot s : allSlots) {
            if (s.getX() == x && s.getY() == y) {
                if (!activeSlots.contains(s)) {
                    activeSlots.add(s);
                }
                break;
            }
        }
    }

    public void render(Graphics g) {
        // ===== VẼ DOUND =====
        for (int y = 0; y < groundMap.length; y++) {
            for (int x = 0; x < groundMap[0].length; x++) {
                int tileId = groundMap[y][x];
                g.drawImage(MapAsset.tiles[tileId], x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
            }
        }
        
        for (int y = 0; y < objectsMap.length; y++) {
            for (int x = 0; x < objectsMap[0].length; x++) {
                int tileId = objectsMap[y][x];
                if (tileId > 0 && tileId < MapAsset.objects.length) {
                    if( tileId == 1) {
                        g.drawImage(MapAsset.objects[7], x * TILE_SIZE - 18, y * TILE_SIZE + 40 ,MapAsset.objects[7].getWidth(), MapAsset.objects[7].getHeight(), null);
                        g.drawImage(MapAsset.objects[tileId], x * TILE_SIZE, y * TILE_SIZE,MapAsset.objects[tileId].getWidth() , MapAsset.objects[tileId].getHeight(), null);
                    }
                    else if( tileId == 5 || tileId == 6) drawObjectWithGrass(g, MapAsset.objects[tileId], x, y);
                }
            }
        }
        // ===== VẼ TOWER SLOT (debug) =====
        for (TowerSlot slot : activeSlots) {
            slot.render(g);
        }
    }

    public Point[] getPath() {
        return path;
    }

    public ArrayList<TowerSlot> getTowerSlots() {
        return activeSlots;
    }

    public TowerSlot getSlotAt(int mouseX, int mouseY) {

        for (TowerSlot slot : activeSlots) {
            if (slot.isClicked(mouseX, mouseY)) {
                return slot;
            }
        }

        return null;
    }

    public void drawObjectWithGrass(Graphics g, BufferedImage objImg, int x, int y) {
    int drawX = x * TILE_SIZE;
    int drawY = (y + 1) * TILE_SIZE - objImg.getHeight();

    // 1. Draw object
    g.drawImage(objImg, drawX, drawY,objImg.getWidth(), objImg.getHeight(), null);


    // 2. Random theo vị trí (không bị nhấp nháy)
    long seed = x * 73856093 ^ y * 19349663;
    java.util.Random rand = new java.util.Random(seed);

    int grassCount = rand.nextInt(3); // 0 -> 2 bụi

    for (int i = 0; i < grassCount; i++) {
        int offsetX = rand.nextInt(objImg.getWidth());
        int offsetY = rand.nextInt(10);

        int size = 10 + rand.nextInt(8);

        g.drawImage(
            MapAsset.objects[3],
            drawX + offsetX,
            drawY + objImg.getHeight() - offsetY,
            MapAsset.objects[3].getWidth(),
            MapAsset.objects[3].getHeight(),
            null
        );
    }
}
}
