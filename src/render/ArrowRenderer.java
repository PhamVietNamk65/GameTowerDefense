package render;

import asset.TowerAsset;
import entity.Arrow;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ArrowRenderer {

    // Scale arrow lên để nhìn thấy rõ (sprite gốc 3x14 quá nhỏ)
    private static final int DRAW_W = 3;
    private static final int DRAW_H = 14;

    // Sprite 1.png hướng lên (↑) → offset -PI/2
    // Nếu đạn vẫn lệch: thử đổi thành +PI/2 hoặc 0
    private static final double ANGLE_OFFSET = -Math.PI / 2.0;

    public void render(Graphics2D g2, ArrayList<Arrow> arrows) {
        for (Arrow a : arrows) {
            if (!a.alive) continue;
            draw(g2, a);
        }
    }

    private void draw(Graphics2D g2, Arrow a) {
        BufferedImage[] frames = TowerAsset.arrowFrames;
        if (frames == null || frames.length == 0) return;

        BufferedImage img = frames[0];
        if (img == null) return;

        // Sprite gốc 3x14: chiều nào dài hơn là chiều mũi tên
        // Scale lên DRAW_W x DRAW_H để thấy rõ
        int srcW = img.getWidth();
        int srcH = img.getHeight();

        // Nếu sprite sheet rộng (nhiều frame ngang): chỉ lấy frame đầu
        // Phát hiện: nếu width > height → có thể là sprite sheet ngang
        BufferedImage frame;
        if (srcW > srcH && srcW > 4) {
            // Lấy frame đầu tiên có kích thước = srcH x srcH (vuông)
            frame = img.getSubimage(0, 0, srcH, srcH);
        } else {
            frame = img;
        }

        Graphics2D g = (Graphics2D) g2.create();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                           RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.translate((int) a.x, (int) a.y);
        g.rotate(a.angle + ANGLE_OFFSET);
        g.drawImage(frame, -DRAW_W / 2, -DRAW_H / 2, DRAW_W, DRAW_H, null);
        g.dispose();
    }
}