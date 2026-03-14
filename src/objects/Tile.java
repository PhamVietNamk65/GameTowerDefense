package objects;
import java.awt.image.BufferedImage;

//lay hinh anh vat the tu file 
public class Tile {

    private BufferedImage sprite;

    public Tile(BufferedImage sprite){
        this.sprite = sprite;
    }

    public BufferedImage getSprite() {
        return sprite;
    }
    
}
