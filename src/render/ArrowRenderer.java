package render;

import entity.Arrow;
import helpz.LoadSave;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ArrowRenderer {

    private BufferedImage[] arrowFrames;
    private static final int SIZE = 10;
    public ArrowRenderer() {
        arrowFrames = LoadSave.getSpriteFramesFromFolder("tower/3 Units/Arrow");
    }

    public void draw(Graphics2D g2, ArrayList<Arrow> arrows) {

        for (Arrow a : arrows) {
            AffineTransform old = g2.getTransform();

            g2.translate(a.getX(), a.getY());
            g2.rotate(a.getAngle());

            BufferedImage img = arrowFrames[a.getAnimIndex() % arrowFrames.length];
            g2.drawImage(img, -SIZE/2, -SIZE/2, SIZE, SIZE, null);

            g2.setTransform(old);
        }
    }
}