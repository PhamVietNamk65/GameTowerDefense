package Manager;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import helpz.LoadSave;
import objects.Tile;

//quan ly hinh anh 
public class TileManager {

    public Tile GRASS,ROAD;
    public BufferedImage atlas;
    public ArrayList<Tile> tileList = new ArrayList<>();

    public TileManager() {

        LoadAtaLas();
        createTile();
    }

    private void createTile() {
        
        tileList.add(GRASS = new Tile(getSprite(5, 4)));
        tileList.add(ROAD = new Tile(getSprite(0, 0)));
    }

    private void LoadAtaLas() {
       atlas = LoadSave.getSpriteAtlas("tile/1 Tiles/FieldsTileset.png");
    }
    
    public BufferedImage getSprite(int id){
        return tileList.get(id).getSprite();
    }
    public BufferedImage getSprite(int xCord, int yCord){
        return atlas.getSubimage(xCord * 32, yCord * 32, 32, 32);
    }
}
