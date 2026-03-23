package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utils.AssetManager;
import utils.Constants;

public class TileManager {

    private BufferedImage[] tiles;

    public TileManager(){
        tiles = AssetManager.tiles;
    }

    public void draw(Graphics g, int[][] map){
        int tileSize = Constants.Tile.SIZE;

        for(int row = 0; row < map.length; row++){
            for(int col = 0; col < map[row].length; col++){

                int tile = map[row][col];

                g.drawImage(
                    tiles[tile],
                    col * tileSize,
                    row * tileSize,
                    null
                );
            }
        }
    }
}