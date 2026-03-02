package objects;
//lay hinh anh vat the tu file 
import java.awt.image.BufferedImage;

public class Tile {
    public BufferedImage sprite;

    public Tile(BufferedImage sprite){
        this.sprite = sprite;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

}
