package levels;
import static utils.Constants.Tiles.TILE_SIZE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import asset.MapAsset;
import entity.TowerSlot;
import entity.trap.Wall;
import system.PathFinder;
import utils.Constants;

public class Level {
    private int levelid;

    private int[][] pathMap;
    private int[][] towerMap;
    private int[][] groundMap;
    private int[][] objectsMap;

    private ArrayList<Point[]> paths = new ArrayList<>();

    private ArrayList<TowerSlot> allSlots = new ArrayList<>();
    private ArrayList<TowerSlot> activeSlots;

    private Wall[][] wallGrid ;
    private ArrayList<Wall> walls = new ArrayList<>();

    public Level(int levelid){
        this.levelid = levelid;
        loadLevel();
    }
    private void loadLevel() {
        groundMap = LoadMap.loadLevel("res/Map/Level" + levelid + "/ground.csv");
        pathMap  = LoadMap.loadLevel("res/Map/Level" + levelid + "/path.csv");
        
        towerMap = LoadMap.loadLevel("res/Map/Level" + levelid + "/tower.csv");
        objectsMap = LoadMap.loadLevel("res/Map/Level" + levelid + "/objects.csv");

        paths.add(PathFinder.buildPath(pathMap));
        generateTowerSlots();
        activeSlots = new ArrayList<>(allSlots);
        wallGrid = new Wall[groundMap.length][groundMap[0].length];
        try {
            int[][] pathMap1 = LoadMap.loadLevel("res/Map/Level" + levelid + "/path1.csv");
            paths.add(PathFinder.buildPath(pathMap1));
        } catch (Exception e) {}
    }

    public void update() {

        Iterator<Wall> it = walls.iterator();

        while (it.hasNext()) {
            Wall w = it.next();

            w.update();

            if (w.isDestroyed()) {
                wallGrid[w.getY()][w.getX()] = null;
                it.remove();
            }
        }
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
                if( levelid == 3){
                    drawMore(g, x, y, 10);
                    
                }
            }
        }

        for (int y = 0; y < objectsMap.length; y++) {
            for (int x = 0; x < objectsMap[0].length; x++) {
                int tileId = objectsMap[y][x];
                if (tileId > 0 && tileId < MapAsset.objects.length) {
                    if( tileId == 1 || tileId == 8) {
                        g.drawImage(
                            MapAsset.objects[7], 
                            x * TILE_SIZE - 18, 
                            y * TILE_SIZE + 38, 
                            null
                        );
                        g.drawImage(MapAsset.objects[tileId], x * TILE_SIZE, y * TILE_SIZE,MapAsset.objects[tileId].getWidth() , MapAsset.objects[tileId].getHeight(), null);
                    }
                    else if( tileId == 6){
                        g.drawImage(MapAsset.objects[8], x * TILE_SIZE - 11 , y * TILE_SIZE - 3 , null);
                        g.drawImage(MapAsset.objects[tileId], x * TILE_SIZE, y * TILE_SIZE, null);
                        g.drawImage(MapAsset.objects[5], x * TILE_SIZE - 8, y * TILE_SIZE + 20, null);
                    }
                    else if( tileId == 4){
                        g.drawImage(MapAsset.objects[9], x * TILE_SIZE - 5 , y * TILE_SIZE - 3 , null);
                        g.drawImage(MapAsset.objects[tileId], x * TILE_SIZE, y * TILE_SIZE, null);
                    }
                    else if( tileId == 7 ) {
                        drawMore(g, x, y, tileId);
                    }
                    else g.drawImage(MapAsset.objects[tileId], x * TILE_SIZE, y * TILE_SIZE, null);
                }

            }
        }
        // ===== VẼ TOWER SLOT (debug) =====
        for (TowerSlot slot : activeSlots) {
            slot.render(g);
        }
        
        for(Wall w : walls){
            w.render(g);
        }
    }

    public ArrayList<Point[]> getPaths() {
        return paths;
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

    private void drawMore(Graphics g, int x, int y, int id){
        // 1. Dùng tọa độ x, y để tạo Seed cố định cho ô này
        long seed = (long)x * 73856093 ^ (long)y * 19349663;
        java.util.Random rand = new java.util.Random(seed);

        // 2. Ngẫu nhiên số lượng khóm cỏ (ví dụ từ 3 đến 6 khóm)
        int grassCount = 2 + rand.nextInt(3); 

         for (int i = 0; i < grassCount; i++) {
        // Ngẫu nhiên vị trí x, y bên trong ô TILE_SIZE
        // Trừ đi một khoảng nhỏ (ví dụ 10-15) để cỏ không bị tràn sang ô bên cạnh
        int offsetX = rand.nextInt(TILE_SIZE - 12);
        int offsetY = rand.nextInt(TILE_SIZE - 12);

        g.drawImage(
            MapAsset.objects[id], 
            x * TILE_SIZE + offsetX, 
            y * TILE_SIZE + offsetY, 
            null);
        }
    }
    
            
    public int getlevelID(){
        return levelid;
    }

    public void buildWall(int x, int y) {
        int dir = getDirectionFromPath(x, y);
        Wall w = new Wall(x, y, dir);

        wallGrid[y][x] = w;
        walls.add(w);
    }

    public boolean hasWall(int x, int y) {
        
        return wallGrid[y][x] != null;
    }

    public Wall getWallAt(int x, int y) {
        return wallGrid[y][x];
    }

    public void removeWall(Wall w) {
        wallGrid[w.getY()][w.getX()] = null;
        walls.remove(w);
    }

    public boolean canBuildWall(int x, int y) {

        if (x < 0 || y < 0 || y >= groundMap.length || x >= groundMap[0].length)
            return false;

        // đã có tường
        if (hasWall(x, y)) return false;

        // không phải ô build (ví dụ != 0)
        if (pathMap[y][x] == 0) return false;

        return true;
    }

    private int getDirectionFromPath(int x, int y) {

        for (Point[] path : paths) {
            for (int i = 1; i < path.length; i++) {

                int px = path[i].x / TILE_SIZE;
                int py = path[i].y / TILE_SIZE;

                if (px == x && py == y) {

                    int prevX = path[i - 1].x / TILE_SIZE;
                    int prevY = path[i - 1].y / TILE_SIZE;

                    int dx = px - prevX;
                    int dy = py - prevY;

                    if (dx == 1) return 2;   
                    if (dx == -1) return 3;  
                    if (dy == -1) return 1;   
                    if (dy == 1) return 0;  
                }
            }
        }

        return 2; // default
    }
}
