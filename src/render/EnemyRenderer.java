package render;

import Manager.EnemyManager;
import asset.MonsterAsset;
import entity.monster.EnemyState;
import entity.monster.Monster;

import static utils.Constants.Direction.*;
import static utils.Constants.Monsters.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class EnemyRenderer {

    private EnemyManager enemyManager;

    public EnemyRenderer(EnemyManager enemyManager) {
        this.enemyManager = enemyManager;
    }

    public void draw(Graphics g) {
        for (Monster m : enemyManager.getMonsters()) {
            // FIX: mỗi quái tự update animation của mình
            if (m.getState() == EnemyState.DYING) {
                BufferedImage[] df = MonsterAsset.getFrames(
                    m.getEnemyType(), EnemyState.DYING, m.getDirection());
                int totalFrames = (df != null) ? df.length : 1;
                m.updateDeathAnim(totalFrames);
            } else {
                m.updateAnim();
            }

            drawEnemy(m, g);

            if (m.getState() != EnemyState.DYING) {
                drawHealthBar(m, g);
            }
        }
    }

    private void drawEnemy(Monster m, Graphics g) {
        BufferedImage[] frames;
        int index;

        if (m.getState() == EnemyState.DYING) {
            frames = MonsterAsset.getFrames(
                m.getEnemyType(), EnemyState.DYING, m.getDirection());
            if (frames == null || frames.length == 0) return;
            // FIX: dùng deathAnimIndex riêng → death chỉ chạy 1 lần, không loop
            index = m.getDeathAnimIndex();
        } else {
            int direction = m.getDirection() == RIGHT ? LEFT : m.getDirection(); // Nếu đang đi sang phải, lấy animation LEFT (vì sprite gốc hướng sang trái)
            frames = MonsterAsset.getFrames(
                m.getEnemyType(), m.getState(), m.getDirection());
            if (frames == null || frames.length == 0) return;
            // Walk/attack animation loop bình thường
            index = m.getAnimIndex(frames.length);
        }

        // Clamp để chắc chắn không out of bounds
        index = Math.min(index, frames.length - 1);

        if (m.getDirection() == utils.Constants.Direction.RIGHT) {
            g.drawImage(frames[index],
                    (int) m.getX() + ENEMY_SIZE,
                    (int) m.getY(),
                    -ENEMY_SIZE,
                    ENEMY_SIZE,
                    null);
        } else {
            g.drawImage(frames[index],
                    (int) m.getX(),
                    (int) m.getY(),
                    ENEMY_SIZE,
                    ENEMY_SIZE,
                    null);
        }
    }

    private void drawHealthBar(Monster m, Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int barWidth  = HP_BAR_WIDTH;
        int barHeight = HP_BAR_HEIGHT;
        int barX = (int) m.getX() + (ENEMY_SIZE - barWidth) / 2;
        int barY = (int) m.getY() - HP_BAR_Y_OFFSET;

        float hpPercent   = m.getHealthBarFloat();
        int currentWidth  = (int) (barWidth * hpPercent);
        int arc = 8;

        // Shadow
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRoundRect(barX + 2, barY + 2, barWidth, barHeight, arc, arc);

        // Background
        g.setColor(new Color(40, 40, 40));
        g.fillRoundRect(barX, barY, barWidth, barHeight, arc, arc);

        // Color theo % máu
        Color hpColor;
        if      (hpPercent > 0.6f) hpColor = new Color(60, 200, 80);
        else if (hpPercent > 0.3f) hpColor = new Color(255, 200, 0);
        else                       hpColor = new Color(220, 50, 50);

        // Gradient fill
        GradientPaint gp = new GradientPaint(
            barX, barY, hpColor.brighter(),
            barX, barY + barHeight, hpColor.darker()
        );
        g.setPaint(gp);
        g.fillRoundRect(barX, barY, currentWidth, barHeight, arc, arc);

        // Highlight
        g.setColor(new Color(255, 255, 255, 60));
        g.fillRoundRect(barX + 1, barY + 1, currentWidth - 2, barHeight / 2, arc - 2, arc - 2);

        // Border
        g.setColor(new Color(0, 0, 0, 150));
        g.setStroke(new BasicStroke(1.2f));
        g.drawRoundRect(barX, barY, barWidth, barHeight, arc, arc);

        g.dispose();
    }
}