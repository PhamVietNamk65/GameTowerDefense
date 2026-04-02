package render;

import Manager.EnemyManager;
import entity.Monster;
import entity.EnemyState;
import asset.MonsterAsset;

import static utils.Constants.Monsters.ENEMY_SIZE;
import static utils.Constants.Monsters.HP_BAR_HEIGHT;
import static utils.Constants.Monsters.HP_BAR_WIDTH;
import static utils.Constants.Monsters.HP_BAR_Y_OFFSET;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EnemyRenderer {

    private EnemyManager enemyManager;

    private int aniTick;
    private int aniIndex;
    private final int aniSpeed = 20;

    public EnemyRenderer(EnemyManager enemyManager) {
        this.enemyManager = enemyManager;
    }

    public void draw(Graphics g) {
        updateAnimation();

        for (Monster m : enemyManager.getMonsters()) {
            drawEnemy(m, g);

            if (m.getState() != EnemyState.DEATH) {
                drawHealthBar(m, g);
            }
        }
    }

    private void updateAnimation() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
        }
    }

    private void drawEnemy(Monster m, Graphics g) {

        BufferedImage[] frames;

        if (m.getState() == EnemyState.DEATH) {
            frames = MonsterAsset.getFrames(
                m.getEnemyType(),
                EnemyState.DEATH,
                m.getDirection()
            );
        } else {
            frames = MonsterAsset.getFrames(
                m.getEnemyType(),
                m.getState(),
                m.getDirection()
            );
        }

        if (frames == null || frames.length == 0)
            return;

        int index = aniIndex % frames.length;

        if (m.getDirection() == utils.Constants.Direction.RIGHT) {
            g.drawImage(frames[index],
                    (int) m.getX() + utils.Constants.Monsters.ENEMY_SIZE,
                    (int) m.getY(),
                    -utils.Constants.Monsters.ENEMY_SIZE,
                    utils.Constants.Monsters.ENEMY_SIZE,
                    null);
        } else {
            g.drawImage(frames[index],
                    (int) m.getX(),
                    (int) m.getY(),
                    utils.Constants.Monsters.ENEMY_SIZE,
                    utils.Constants.Monsters.ENEMY_SIZE,
                    null);
        }
    }

    private void drawHealthBar(Monster m, Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int barWidth = HP_BAR_WIDTH;
        int barHeight = HP_BAR_HEIGHT;
        int barX = (int) m.getX() + (ENEMY_SIZE - barWidth) / 2;
        int barY = (int) m.getY() - HP_BAR_Y_OFFSET;

        float hpPercent = m.getHealthBarFloat();
        int currentWidth = (int) (barWidth * hpPercent);

        int arc = 8;

        // ===== Shadow =====
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRoundRect(barX + 2, barY + 2, barWidth, barHeight, arc, arc);

        // ===== Background (empty HP) =====
        g.setColor(new Color(40, 40, 40));
        g.fillRoundRect(barX, barY, barWidth, barHeight, arc, arc);

        // ===== Color theo % máu =====
        Color hpColor;
        if (hpPercent > 0.6f) {
            hpColor = new Color(60, 200, 80);   // xanh
        } else if (hpPercent > 0.3f) {
            hpColor = new Color(255, 200, 0);   // vàng
        } else {
            hpColor = new Color(220, 50, 50);   // đỏ
        }

        // ===== Gradient fill =====
        GradientPaint gp = new GradientPaint(
            barX, barY, hpColor.brighter(),
            barX, barY + barHeight, hpColor.darker()
        );
        g.setPaint(gp);
        g.fillRoundRect(barX, barY, currentWidth, barHeight, arc, arc);

        // ===== Highlight (ánh sáng phía trên) =====
        g.setColor(new Color(255, 255, 255, 60));
        g.fillRoundRect(barX + 1, barY + 1, currentWidth - 2, barHeight / 2, arc - 2, arc - 2);

        // ===== Border =====
        g.setColor(new Color(0, 0, 0, 150));
        g.setStroke(new BasicStroke(1.2f));
        g.drawRoundRect(barX, barY, barWidth, barHeight, arc, arc);

        g.dispose();
    }
}