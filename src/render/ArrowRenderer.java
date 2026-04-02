package render;

import entity.Arrow;
import asset.TowerAsset;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ArrowRenderer {

    private static final int SIZE = 10;

    public void render(Graphics2D g2, ArrayList<Arrow> arrows) {
        for (Arrow a : arrows) {
            draw(g2, a);
        }
    }

    private void draw(Graphics2D g2, Arrow a) {
        BufferedImage[] frames = TowerAsset.arrowFrames;

        if (frames == null || frames.length == 0) return;

        BufferedImage img = frames[0];

        Graphics2D g = (Graphics2D) g2.create();
        g.translate(a.x, a.y);
        g.rotate(a.angle);

        g.drawImage(img, -SIZE/2, -SIZE/2, SIZE, SIZE, null);
        g.dispose();
    }
}