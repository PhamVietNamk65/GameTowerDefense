package Manager;

import entity.Bee;
import entity.EnemyState;
import entity.Monster;
import entity.Orc;
import entity.Slime;
import entity.Wolf;
import static helpz.Constants.Monsters.*;
import helpz.LoadSave;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import scener.Playing;

public class EnemyManager {

    private Playing playing;
    private Map<Integer, Map<String, BufferedImage[]>> enemyAnimations;
    private ArrayList<Monster> monsters = new ArrayList<>();

    private int aniTick;
    private int aniIndex;
    private final int aniSpeed = 20;

    private static final int ENEMY_SIZE = 32;
    private static final int HALF_ENEMY = ENEMY_SIZE / 2;

    private static final int HP_BAR_WIDTH = 24;
    private static final int HP_BAR_HEIGHT = 4;
    private static final int HP_BAR_Y_OFFSET = 8;

    private Point[] levelPath = {
        new Point(32, 100),
        new Point(200, 100),
        new Point(200, 200),
        new Point(400, 200),
        new Point(400, 350),
        new Point(700, 350)
    };

    public EnemyManager(Playing playing) {
        this.playing = playing;
        this.enemyAnimations = new HashMap<>();
        loadEnemyImgs();
    }

    public void spawnMonster(int monsterType) {
        int x = levelPath[0].x - HALF_ENEMY;
        int y = levelPath[0].y - HALF_ENEMY;
        int id = monsters.size();

        switch (monsterType) {
            case ORC:
                monsters.add(new Orc(x, y, id));
                break;
            case BEE:
                monsters.add(new Bee(x, y, id));
                break;
            case SLIME:
                monsters.add(new Slime(x, y, id));
                break;
            case WOLF:
                monsters.add(new Wolf(x, y, id));
                break;
        }
    }

    private void loadEnemyImgs() {
        loadEnemyAnimation(SLIME, "enemies/1",
                new String[]{"U", "S", "D"}, new String[]{"U", "S", "D"}, false);

        loadEnemyAnimation(ORC, "enemies/2",
                new String[]{"U", "S", "D"}, new String[]{"U", "S", "D"}, true);

        loadEnemyAnimation(WOLF, "enemies/3",
                new String[]{"U", "S", "D"}, new String[]{"U", "S", "D"}, true);

        loadEnemyAnimation(BEE, "enemies/4",
                new String[]{"U", "S", "D"}, new String[]{"U", "S", "D"}, false);
    }

    private void loadEnemyAnimation(int type, String folder,
                                    String[] walkDirs, String[] deathDirs, boolean hasAttack) {
        Map<String, BufferedImage[]> map = new HashMap<>();

        for (String dir : walkDirs) {
            BufferedImage[] frames = LoadSave.getSpriteFrames(folder + "/" + dir + "_Walk.png", 48, 48);
            map.put("WALK_" + dir, frames);
            map.put("ATTACK_" + dir, frames);
        }

        if (hasAttack) {
            for (String dir : walkDirs) {
                BufferedImage[] frames = LoadSave.getSpriteFrames(folder + "/" + dir + "_Attack.png", 48, 48);
                if (frames != null && frames.length > 0) {
                    map.put("ATTACK_" + dir, frames);
                }
            }
        }

        for (String dir : deathDirs) {
            BufferedImage[] frames = LoadSave.getSpriteFrames(folder + "/" + dir + "_Death.png", 48, 48);
            if (frames != null && frames.length > 0) {
                map.put("DEATH_" + dir, frames);
            }
        }

        enemyAnimations.put(type, map);
    }

    public void update() {
        java.util.Iterator<Monster> it = monsters.iterator();

        while (it.hasNext()) {
            Monster m = it.next();

            if (m.hasReachedEnd()) {
                it.remove();
                continue;
            }

            if (m.IsAlive()) {
                moveMonsterAlongPath(m);
            } else {
                Map<String, BufferedImage[]> map = enemyAnimations.get(m.getEnemyType());
                int totalFrames = 1;

                if (map != null) {
                    BufferedImage[] df = map.get("DEATH_" + m.getAnimDir());
                    if (df != null && df.length > 0) {
                        totalFrames = df.length;
                    }
                }

                m.tickDeath(totalFrames, aniSpeed);

                if (m.isDeathDone()) {
                    it.remove();
                }
            }
        }

        updateAnimationTick();
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
        }
    }

    private void moveMonsterAlongPath(Monster m) {
        if (m.getPathIndex() >= levelPath.length) {
            playing.loseLife(1);
            m.reachEnd();
            return;
        }

        Point target = levelPath[m.getPathIndex()];
        float speed = GetSpeed(m.getEnemyType());

        float centerX = m.getX() + HALF_ENEMY;
        float centerY = m.getY() + HALF_ENEMY;

        float dx = target.x - centerX;
        float dy = target.y - centerY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance <= speed) {
            m.setPos(target.x - HALF_ENEMY, target.y - HALF_ENEMY);
            m.nextPath();
            return;
        }

        float moveX = (dx / distance) * speed;
        float moveY = (dy / distance) * speed;

        m.updateAnimDirection(moveX, moveY);
        m.setPos(m.getX() + moveX, m.getY() + moveY);
    }

    public void drawPath(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(194, 160, 120));
        g2.setStroke(new BasicStroke(22f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (int i = 0; i < levelPath.length - 1; i++) {
            Point p1 = levelPath[i];
            Point p2 = levelPath[i + 1];
            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        g2.setColor(Color.GREEN);
        g2.fillOval(levelPath[0].x - 8, levelPath[0].y - 8, 16, 16);

        Point end = levelPath[levelPath.length - 1];
        g2.setColor(Color.RED);
        g2.fillOval(end.x - 8, end.y - 8, 16, 16);
    }

    public void draw(Graphics g) {
        for (Monster m : monsters) {
            drawEnemy(m, g);
            if (m.IsAlive()) {
                drawHealthBar(m, g);
            }
        }
    }

    private void drawEnemy(Monster m, Graphics g) {
        Map<String, BufferedImage[]> map = enemyAnimations.get(m.getEnemyType());
        if (map == null) return;

        BufferedImage[] frames;
        int index;

        if (!m.IsAlive()) {
            String key = "DEATH_" + m.getAnimDir();
            frames = map.get(key);
            if (frames == null || frames.length == 0) frames = map.get("DEATH_U");
            if (frames == null || frames.length == 0) return;

            index = Math.min(m.getDeathTick() / aniSpeed, frames.length - 1);
        } else {
            EnemyState state = m.getState();
            String prefix = state == EnemyState.ATTACK ? "ATTACK_" : "WALK_";
            String dir = m.getAnimDir();

            frames = map.get(prefix + dir);
            if (frames == null || frames.length == 0) frames = map.get("WALK_U");
            if (frames == null || frames.length == 0) return;

            index = aniIndex % frames.length;
        }

        BufferedImage img = frames[index];

        // Nếu đang đi ngang sang phải thì lật ảnh
        if (m.getAnimDir().equals("S") && m.isFacingRight()) {
            g.drawImage(img,
                    (int) m.getX() + ENEMY_SIZE, (int) m.getY(),
                    -ENEMY_SIZE, ENEMY_SIZE, null);
        } else {
            g.drawImage(img,
                    (int) m.getX(), (int) m.getY(),
                    ENEMY_SIZE, ENEMY_SIZE, null);
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

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public Point[] getLevelPath() {
        return levelPath;
    }
}