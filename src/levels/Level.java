package levels;

import static utils.Constants.Tiles.TILE_SIZE;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import asset.MapAsset;
import asset.MapType;
import entity.TowerSlot;
import entity.monster.Monster;
import entity.trap.Spikes;
import entity.trap.Wall;
import system.PathFinder;

public class Level {

    private int levelid;
    private MapType mapType;

    private int[][] pathMap;
    private int[][] towerMap;
    private int[][] groundMap;
    private int[][] objectsMap;

    private ArrayList<Point[]> paths = new ArrayList<>();

    private ArrayList<TowerSlot> allSlots = new ArrayList<>();
    private ArrayList<TowerSlot> activeSlots;
 
    private int levelWall = 1;
    private Wall[][] wallGrid;
    private ArrayList<Wall> walls = new ArrayList<>();

    private int levelSpikes = 1;
    private Spikes[][] spikesGrid;
    private ArrayList<Spikes> spikes = new ArrayList<>();

    public Level(int levelid, MapType mapType){
        this.levelid = levelid;
        this.mapType = mapType;
        loadLevel();
    }

    private void loadLevel() {

        groundMap  = LoadMap.loadLevel("res/Map/Level" + levelid + "/ground.csv");
        pathMap    = LoadMap.loadLevel("res/Map/Level" + levelid + "/path.csv");
        towerMap   = LoadMap.loadLevel("res/Map/Level" + levelid + "/tower.csv");
        objectsMap = LoadMap.loadLevel("res/Map/Level" + levelid + "/objects.csv");

        paths.add(PathFinder.buildPath(pathMap));

        try {
            int[][] pathMap1 = LoadMap.loadLevel("res/Map/Level" + levelid + "/path1.csv");
            paths.add(PathFinder.buildPath(pathMap1));
        } catch (Exception e) {

        }

        generateTowerSlots();
        activeSlots = new ArrayList<>(allSlots);

        wallGrid = new Wall[groundMap.length][groundMap[0].length];
        spikesGrid = new Spikes[groundMap.length][groundMap[0].length];
    }

    public void update(ArrayList<Monster> monsters) {

        Iterator<Wall> it = walls.iterator();
        while (it.hasNext()) {
            Wall w = it.next();
            w.update();

            if (w.isDestroyed()) {
                wallGrid[w.getY()][w.getX()] = null;
                it.remove();
            }
        }

        Iterator<Spikes> its = spikes.iterator();
        while (its.hasNext()) {
            Spikes s = its.next();
            s.update(monsters);

            if (!s.isActive()) {
                spikesGrid[s.getY()][s.getX()] = null;
                its.remove();
            }
        }
    }

    public void render(Graphics g) {

        BufferedImage[] tiles = MapAsset.getInstance().tiles.get(mapType);
        BufferedImage[] objects = MapAsset.getInstance().objects.get(mapType);

        for (int y = 0; y < groundMap.length; y++) {
            for (int x = 0; x < groundMap[0].length; x++) {

                int tileId = groundMap[y][x];

                if (tileId >= 0 && tileId < tiles.length) {
                    g.drawImage(tiles[tileId], x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                }
            }
        }

        for (int y = 0; y < objectsMap.length; y++) {
            for (int x = 0; x < objectsMap[0].length; x++) {

                int tileId = objectsMap[y][x];

                if (tileId > 0 && tileId < objects.length) {

                    if (tileId == 1 || tileId == 8) {
                        g.drawImage(objects[7], x * TILE_SIZE - 18, y * TILE_SIZE + 38, null);
                    }

                    else if (tileId == 6) {
                        g.drawImage(objects[8], x * TILE_SIZE - 11, y * TILE_SIZE - 3, null);
                        g.drawImage(objects[tileId], x * TILE_SIZE, y * TILE_SIZE, null);
                        g.drawImage(objects[5], x * TILE_SIZE - 8, y * TILE_SIZE + 20, null);
                        continue;
                    }

                    else if (tileId == 4) {
                        g.drawImage(objects[9], x * TILE_SIZE - 5, y * TILE_SIZE - 3, null);
                    }

                    g.drawImage(objects[tileId], x * TILE_SIZE, y * TILE_SIZE, null);
                }
            }
        }

        for (TowerSlot slot : activeSlots) {
            slot.render(g);
        }

        for (Spikes s : spikes) {
            s.render(g);
        }

        for (Wall w : walls) {
            w.render(g);
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

    public void removeSlot(TowerSlot slot) {
        if (slot == null) return;
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

    public ArrayList<Point[]> getPaths() {
        return paths;
    }

    public void buildWall(int x, int y) {
        int dir = getDirectionFromPath(x, y);
        Wall w = new Wall(x, y, dir, levelWall);

        wallGrid[y][x] = w;
        walls.add(w);
    }

    public boolean hasWall(int x, int y) {
        return wallGrid[y][x] != null;
    }

    public boolean canBuildWall(int x, int y) {
        if (!inBound(x, y)) return false;
        if (hasWall(x, y) || hasSpikes(x, y)) return false;
        return pathMap[y][x] != 0;
    }

    public void removeWall(Wall w) {
        wallGrid[w.getY()][w.getX()] = null;
        walls.remove(w);
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

        return 2;
    }

    public void buildSpikes(int x, int y) {
        Spikes s = new Spikes(x * TILE_SIZE, y * TILE_SIZE, levelSpikes);

        spikesGrid[y][x] = s;
        spikes.add(s);
    }

    public boolean hasSpikes(int x, int y) {
        return spikesGrid[y][x] != null;
    }

    public boolean canBuildSpikes(int x, int y) {
        if (!inBound(x, y)) return false;
        if (hasSpikes(x, y) || hasWall(x, y)) return false;
        return pathMap[y][x] != 0;
    }

    private boolean inBound(int x, int y) {
        return !(x < 0 || y < 0 || y >= groundMap.length || x >= groundMap[0].length);
    }

    public int getLevelID(){
        return levelid;
    }

    public void upgradeWall(){
        levelWall++;
        levelSpikes++;
    }

    public int getLevelWall(){
        return levelWall;
    }

    public int getLevelSpikes(){
        return levelSpikes;
    }

    public Wall getWallAt(int tileX, int tileY) {
        return wallGrid[tileY][tileX];
    }

    public void upgradeTraps() {
        this.levelWall++;
        this.levelSpikes++;

    }
}