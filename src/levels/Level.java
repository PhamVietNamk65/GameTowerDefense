package levels;
import static utils.Constants.Tiles.TILE_SIZE;

import java.awt.Graphics;
import java.awt.Point;

import java.util.ArrayList;

import asset.MapAsset;
import entity.TowerSlot;
import system.PathFinder;

public class Level {
    private int levelid;

    private int[][] pathMap;
    private int[][] towerMap;
    private int[][] groundMap;

    private Point[] path;
    
    private ArrayList<TowerSlot> allSlots = new ArrayList<>();
    private ArrayList<TowerSlot> activeSlots;
    public Level(int levelid){
        this.levelid = levelid;
        loadLevel();
    }
    private void loadLevel() {
        groundMap = LoadLevel.loadLevel("res/Map/Level" + levelid + "/ground.csv");
        pathMap  = LoadLevel.loadLevel("res/Map/Level" + levelid + "/path.csv");
        towerMap = LoadLevel.loadLevel("res/Map/Level" + levelid + "/tower.csv");
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
}
