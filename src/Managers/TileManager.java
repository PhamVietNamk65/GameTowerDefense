package Managers;
//quan ly hinh anh 
import java.awt.image.BufferedImage;
import objects.Tile;

public class TileManager {

    public Tile CAY1, CAY2, DUONG1, DUONG2, NUOC1, NUOC2;
    public BufferedImage atlas;
    public Tile[] tiles;

    public TileManager(){
        loadTileAtlas();
        createTiles();
    }

    private void createTiles() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createTiles'");
    }

    private void loadTileAtlas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadTileAtlas'");
    }

    private BufferedImage getTileImage(int col, int row){
        int tileSize = 32;
        return atlas.getSubimage(col * tileSize, row * tileSize, tileSize, tileSize);
    }
}
