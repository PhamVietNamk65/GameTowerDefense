package Manager;

import entity.Tower;
import helpz.Constants;
import helpz.LoadSave;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class TowerManager {

    private static class Particle {
        float x, y, vx, vy, life, maxLife, size;
        int r, g, b;
        Particle(float x, float y, float vx, float vy,
                 float life, int r, int g, int b, float size) {
            this.x=x; this.y=y; this.vx=vx; this.vy=vy;
            this.life=life; this.maxLife=life;
            this.r=r; this.g=g; this.b=b; this.size=size;
        }
        boolean update() {
            x += vx;
            y += vy;
            vy += 0.15f;
            vx *= 0.96f;
            life -= 0.025f;
            return life > 0;
        }
        void draw(Graphics2D g2) {
            float a = Math.max(0, life/maxLife);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
            g2.setColor(new Color(r,g,b));
            int s = Math.max(1,(int)(size*a));
            g2.fillOval((int)(x-s/2f),(int)(y-s/2f),s,s);
        }
    }

    private static class Arrow {
        float x, y;
        float vx, vy;
        float angle;
        boolean alive;
        int dmg;
        entity.Monster target;

        private static final float SPEED      = 6f;
        private static final float HIT_RADIUS = 12f;
        private static final float TURN_SPEED = 0.18f;

        Arrow(float startX, float startY, entity.Monster target, int dmg) {
            this.x = startX;
            this.y = startY;
            this.target = target;
            this.dmg = dmg;

            float dx = (target.getX() + 16) - startX;
            float dy = (target.getY() + 16) - startY;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            this.vx = dist > 0 ? (dx / dist) * SPEED : 0;
            this.vy = dist > 0 ? (dy / dist) * SPEED : 0;
            this.angle = (float) Math.atan2(dy, dx);
            this.alive = true;
        }

        boolean update(int screenW, int screenH) {
            if (target != null && target.IsAlive()) {
                float tx = target.getX() + 16;
                float ty = target.getY() + 16;
                float desiredAngle = (float) Math.atan2(ty - y, tx - x);

                float diff = desiredAngle - angle;
                while (diff >  Math.PI) diff -= 2 * Math.PI;
                while (diff < -Math.PI) diff += 2 * Math.PI;

                if (Math.abs(diff) < TURN_SPEED) angle = desiredAngle;
                else angle += Math.signum(diff) * TURN_SPEED;

                vx = (float)(Math.cos(angle) * SPEED);
                vy = (float)(Math.sin(angle) * SPEED);

                float dx = x - tx;
                float dy = y - ty;
                if (dx * dx + dy * dy <= HIT_RADIUS * HIT_RADIUS) {
                    target.hurt(dmg);
                    alive = false;
                    return false;
                }
            }

            x += vx;
            y += vy;

            if (x < -64 || x > screenW + 64 || y < -64 || y > screenH + 64)
                alive = false;

            return alive;
        }
    }

    private static final int TILE_SIZE = 32;
    private static final int ARCHER_W  = 48;
    private static final int ARCHER_H  = 48;

    private static final int[] FRAME_COUNTS = {1, 4, 4, 6, 6, 6, 6};
    private static final int[] FRAME_WIDTHS = {70, 70, 70, 70, 70, 70, 70};
    private static final int   FRAME_HEIGHT = 130;
    private static final int DRAW_H = 96;

    private static final boolean[] ARCHER_VISIBLE = {true, true, true, false, true, true, false};

    private static final int[] ARCHER_TOP_X = {1, 1, 1, 0, 1, 1, 0};
    private static final int[] ARCHER_TOP_Y = {42, 36, 30, 0, 24, 24, 0};

    private static final int[][] PARTICLE_COLORS = {
        {200,160, 60}, {255,200, 50}, {120,200,255},
        { 80,220,100}, {200, 80,255}, {255,120, 40}, {255,255,180}
    };

    private final ArrayList<Tower> towers = new ArrayList<>();
    private final ArrayList<Particle> particles = new ArrayList<>();
    private final ArrayList<Arrow> arrows = new ArrayList<>();
    private final Random random = new Random();

    private BufferedImage[][] towerFrames;
    private int[] towerDrawW;
    private BufferedImage[][][] archerAnimations;
    private BufferedImage[] arrowFrames;
    private int arrowAnimTick  = 0;
    private int arrowAnimIndex = 0;
    private static final int ARROW_ANIM_SPEED = 4;

    // nhỏ mũi tên lại
    private static final int ARROW_DRAW_SIZE  = 10;

    private int towerId = 0;
    private boolean placingTower = false;
    private int towerToPlace = Constants.Towers.ARCHER;

    public TowerManager() {
        loadTowerFrames();
        loadArcherAnimations();
        loadArrowFrames();
    }

    private void loadTowerFrames() {
        towerFrames = new BufferedImage[7][];
        towerDrawW  = new int[7];

        for (int lv = 0; lv < 7; lv++) {
            String path = "tower/2 Idle/" + (lv + 1) + ".png";
            BufferedImage sheet = LoadSave.getSprite(path);
            if (sheet == null) {
                System.out.println("Missing: " + path);
                towerFrames[lv] = new BufferedImage[0];
                continue;
            }

            int n  = FRAME_COUNTS[lv];
            int fw = FRAME_WIDTHS[lv];
            int fh = FRAME_HEIGHT;

            towerDrawW[lv] = Math.max(1, fw * DRAW_H / fh);

            towerFrames[lv] = new BufferedImage[n];
            for (int f = 0; f < n; f++) {
                int x0 = f * fw;
                int x1 = Math.min(x0 + fw, sheet.getWidth());
                if (x0 >= sheet.getWidth()) break;
                towerFrames[lv][f] = sheet.getSubimage(x0, 0, x1 - x0, fh);
            }
        }
    }

    private void loadArcherAnimations() {
        archerAnimations = new BufferedImage[3][3][];
        archerAnimations[Tower.SIDE][Tower.IDLE]      = LoadSave.getSpriteFrames("tower/3 Units/1/S_Idle.png",      ARCHER_W, ARCHER_H);
        archerAnimations[Tower.SIDE][Tower.PREATTACK] = LoadSave.getSpriteFrames("tower/3 Units/1/S_Preattack.png", ARCHER_W, ARCHER_H);
        archerAnimations[Tower.SIDE][Tower.ATTACK]    = LoadSave.getSpriteFrames("tower/3 Units/1/S_Attack.png",    ARCHER_W, ARCHER_H);
        archerAnimations[Tower.UP  ][Tower.IDLE]      = LoadSave.getSpriteFrames("tower/3 Units/1/U_Idle.png",      ARCHER_W, ARCHER_H);
        archerAnimations[Tower.UP  ][Tower.PREATTACK] = LoadSave.getSpriteFrames("tower/3 Units/1/U_Preattack.png", ARCHER_W, ARCHER_H);
        archerAnimations[Tower.UP  ][Tower.ATTACK]    = LoadSave.getSpriteFrames("tower/3 Units/1/U_Attack.png",    ARCHER_W, ARCHER_H);
        archerAnimations[Tower.DOWN][Tower.IDLE]      = LoadSave.getSpriteFrames("tower/3 Units/1/D_Idle.png",      ARCHER_W, ARCHER_H);
        archerAnimations[Tower.DOWN][Tower.PREATTACK] = LoadSave.getSpriteFrames("tower/3 Units/1/D_Preattack.png", ARCHER_W, ARCHER_H);
        archerAnimations[Tower.DOWN][Tower.ATTACK]    = LoadSave.getSpriteFrames("tower/3 Units/1/D_Attack.png",    ARCHER_W, ARCHER_H);
    }

    private void loadArrowFrames() {
        arrowFrames = LoadSave.getSpriteFramesFromFolder("tower/3 Units/Arrow/1");
        if (arrowFrames == null || arrowFrames.length == 0) {
            System.out.println("Warning: No arrow frames found at tower/3 Units/Arrow/1");
        }
    }

    public void update(ArrayList<entity.Monster> monsters) {
        for (Tower t : towers) {
            t.update();

            if (t.isJustStartedUpgrade())  spawnParticles(t, false);
            if (t.isJustFinishedUpgrade()) spawnParticles(t, true);

            if (t.getTowerType() == Constants.Towers.ARCHER) {
                entity.Monster target = findNearestEnemyInRange(t, monsters);

                if (target != null) {
                    autoUpdateArcher(t, target.getX(), target.getY());

                    if (t.getAnimState() == Tower.ATTACK && t.getAnimIndex() == 0
                            && t.getAnimTick() == 0) {
                        spawnArrow(t, target);
                    }
                } else {
                    if (t.getAnimState() != Tower.IDLE) {
                        t.setAnimation(Tower.IDLE, t.getDirection());
                    }
                }

                t.updateAnimation(getFrameAmount(t));
            }
        }

        updateArrows();

        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            if (!it.next().update()) it.remove();
        }
    }

    private void updateArrows() {
        arrowAnimTick++;
        if (arrowAnimTick >= ARROW_ANIM_SPEED) {
            arrowAnimTick = 0;
            if (arrowFrames != null && arrowFrames.length > 0)
                arrowAnimIndex = (arrowAnimIndex + 1) % arrowFrames.length;
        }

        Iterator<Arrow> it = arrows.iterator();
        while (it.hasNext()) {
            if (!it.next().update(1280, 720)) it.remove();
        }
    }

    // bắn ra từ cây cung / tay archer
    private void spawnArrow(Tower t, entity.Monster target) {
        arrows.add(new Arrow(t.getArrowSpawnX(), t.getArrowSpawnY(), target, t.getDmg()));
    }

    private entity.Monster findNearestEnemyInRange(Tower t, ArrayList<entity.Monster> monsters) {
        entity.Monster nearest = null;
        float minDist = Float.MAX_VALUE;
        float rangeSq = t.getRange() * t.getRange();

        for (entity.Monster m : monsters) {
            if (!m.IsAlive()) continue;

            float mx = m.getX() + 16;
            float my = m.getY() + 16;
            float dx = mx - t.getCenterX();
            float dy = my - t.getCenterY();
            float dist = dx * dx + dy * dy;

            if (dist <= rangeSq && dist < minDist) {
                minDist = dist;
                nearest = m;
            }
        }
        return nearest;
    }

    private void spawnParticles(Tower t, boolean burst) {
        int lv = clamp(t.getTowerLevel(), 0, 6);
        int[] col = PARTICLE_COLORS[lv];
        float cx = t.getCenterX(), cy = t.getCenterY();
        int count = burst ? 28 : 14;
        float spd = burst ? 3.2f : 1.8f;

        for (int i = 0; i < count; i++) {
            double angle = Math.PI * 2 * i / count + random.nextDouble() * 0.4;
            float speed = spd + random.nextFloat() * 1.5f;
            float vx = (float)(Math.cos(angle) * speed);
            float vy = (float)(Math.sin(angle) * speed) - 1.5f;
            float life = 0.6f + random.nextFloat() * 0.5f;
            float size = burst ? (4 + random.nextFloat() * 4) : (3 + random.nextFloat() * 2);
            int r = (i % 3 == 0) ? 255 : col[0];
            int g = (i % 3 == 0) ? 220 : col[1];
            int b = (i % 3 == 0) ? 50  : col[2];
            particles.add(new Particle(cx, cy, vx, vy, life, r, g, b, size));
        }
    }

    private void autoUpdateArcher(Tower t, float enemyX, float enemyY) {
        if (t.isUpgrading()) return;

        float dx = t.getCenterX() - enemyX;
        float dy = t.getCenterY() - enemyY;
        double angle = Math.toDegrees(Math.atan2(dy, dx));

        int dir;
        if (angle > -45 && angle <= 45) {
            dir = Tower.SIDE;
            t.setFacingLeft(false);
        } else if (angle > 45 && angle <= 135) {
            dir = Tower.UP;
            t.setFacingLeft(false);
        } else if (angle > -135 && angle <= -45) {
            dir = Tower.DOWN;
            t.setFacingLeft(false);
        } else {
            dir = Tower.SIDE;
            t.setFacingLeft(true);
        }

        if (t.getAnimState() == Tower.IDLE) {
            if (t.isCooldownOver()) {
                t.setAnimation(Tower.PREATTACK, dir);
                t.resetCooldown();
            } else {
                t.setAnimation(Tower.IDLE, dir);
            }
        } else {
            t.setDirection(dir);
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        if (placingTower) {
            g2.setColor(new Color(0,255,0,100));
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("PLACING ARCHER - click map to place", 20, 20);
        }

        for (Tower t : towers) drawTower(g2, t);
        for (Arrow a : arrows) drawArrow(g2, a);

        Composite oc = g2.getComposite();
        for (Particle p : particles) p.draw(g2);
        g2.setComposite(oc);

        Tower sel = getSelectedTower();
        if (sel != null) {
            drawSelectedInfo(g2, sel);
            drawTowerButtons(g2, sel);
        }
    }

    private void drawTower(Graphics2D g2, Tower t) {
        int lv = clamp(t.getTowerLevel(), 0, 6);
        int frame = t.getTowerAnimFrame();

        if (towerFrames[lv] == null || towerFrames[lv].length == 0) return;
        frame = clamp(frame, 0, towerFrames[lv].length - 1);

        BufferedImage img = towerFrames[lv][frame];
        if (img == null) return;

        int dw = towerDrawW[lv];
        int dh = DRAW_H;

        int drawX = t.getX() - (dw - TILE_SIZE) / 2;
        int drawY = t.getY() + TILE_SIZE - dh;

        if (t.isUpgrading() && t.getFlashAlpha() > 0) {
            float bright = 1.0f + (t.getFlashAlpha() / 180f);
            g2.drawImage(applyBrightness(img, bright), drawX, drawY, dw, dh, null);
        } else {
            g2.drawImage(img, drawX, drawY, dw, dh, null);
        }

        if (t.isUpgrading()) drawProgressBar(g2, t, drawX, drawY, dw);

        if (t.isSelected()) {
            Stroke os = g2.getStroke();
            g2.setStroke(new BasicStroke(1.2f));
            g2.setColor(new Color(255, 220, 0, 200));
            g2.drawRoundRect(t.getX() + 2, t.getY() + 2, TILE_SIZE - 4, TILE_SIZE - 4, 4, 4);
            g2.setStroke(os);
        }

        if (!t.isUpgrading()) drawArcher(g2, t, drawX, drawY);
    }

    private BufferedImage applyBrightness(BufferedImage src, float scale) {
        float[] s = {scale,scale,scale,1f};
        float[] o = {0f,0f,0f,0f};
        java.awt.image.RescaleOp op = new java.awt.image.RescaleOp(s, o, null);
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bg = dst.createGraphics();
        bg.drawImage(src, 0, 0, null);
        bg.dispose();
        return op.filter(dst, null);
    }

    private void drawProgressBar(Graphics2D g2, Tower t, int drawX, int drawY, int dw) {
        int barW = dw;
        int barH = 5;
        int barX = drawX;
        int barY = drawY - 14;
        float progress = t.getUpgradeProgress();

        g2.setColor(new Color(30,30,30,180));
        g2.fillRoundRect(barX - 1, barY - 1, barW + 2, barH + 2, 3, 3);

        int filled = (int)(barW * progress);
        if (filled > 0) {
            g2.setColor(Color.getHSBColor(0.15f - progress * 0.08f, 0.9f, 1.0f));
            g2.fillRoundRect(barX, barY, filled, barH, 2, 2);
        }

        Stroke os = g2.getStroke();
        g2.setStroke(new BasicStroke(0.5f));
        g2.setColor(new Color(180,180,180,100));
        g2.drawRoundRect(barX, barY, barW, barH, 2, 2);
        g2.setStroke(os);

        g2.setFont(new Font("Arial", Font.BOLD, 8));
        g2.setColor(new Color(255,240,160));
        String label = "LV" + (t.getTowerLevel() + 1) + "→" + (t.getTowerLevel() + 2);
        int tw = g2.getFontMetrics().stringWidth(label);
        g2.drawString(label, barX + (barW - tw) / 2, barY - 1);
    }

    private void drawArcher(Graphics2D g2, Tower t, int drawX, int drawY) {
        if (t.getTowerType() != Constants.Towers.ARCHER) return;
        int lv = clamp(t.getTowerLevel(), 0, 6);
        if (!ARCHER_VISIBLE[lv]) return;

        BufferedImage img = getArcherFrame(t);
        if (img == null) return;

        int ax = drawX + ARCHER_TOP_X[lv];
        int ay = drawY + ARCHER_TOP_Y[lv];

        if (t.isFacingLeft()) {
            g2.drawImage(img, ax + ARCHER_W, ay, -ARCHER_W, ARCHER_H, null);
        } else {
            g2.drawImage(img, ax, ay, ARCHER_W, ARCHER_H, null);
        }
    }

    private void drawArrow(Graphics2D g2, Arrow a) {
        if (arrowFrames == null || arrowFrames.length == 0) {
            g2.setColor(new Color(139, 90, 43));
            Graphics2D g2r = (Graphics2D) g2.create();
            g2r.translate(a.x, a.y);
            g2r.rotate(a.angle);
            g2r.fillRect(-5, -1, 10, 2);
            g2r.dispose();
            return;
        }

        BufferedImage frame = arrowFrames[arrowAnimIndex % arrowFrames.length];
        if (frame == null) return;

        Graphics2D g2r = (Graphics2D) g2.create();
        g2r.translate(a.x, a.y);
        g2r.rotate(a.angle);
        int half = ARROW_DRAW_SIZE / 2;
        g2r.drawImage(frame, -half, -half, ARROW_DRAW_SIZE, ARROW_DRAW_SIZE, null);
        g2r.dispose();
    }

    private BufferedImage getArcherFrame(Tower t) {
        BufferedImage[] frames = archerAnimations[t.getDirection()][t.getAnimState()];
        if (frames == null || frames.length == 0) return null;
        return frames[t.getAnimIndex() % frames.length];
    }

    private int getFrameAmount(Tower t) {
        BufferedImage[] frames = archerAnimations[t.getDirection()][t.getAnimState()];
        return frames == null ? 0 : frames.length;
    }

    private int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    public void startPlacingTower(int type) {
        placingTower = true;
        towerToPlace = type;
        clearSelected();
    }

    public boolean isPlacingTower() {
        return placingTower;
    }

    public void cancelPlacing() {
        placingTower = false;
    }

    public void placeTower(int tileX, int tileY) {
        towers.add(new Tower(tileX * TILE_SIZE, tileY * TILE_SIZE, towerId++, towerToPlace, tileX, tileY));
        placingTower = false;
    }

    public boolean canPlaceTower(int tileX, int tileY) {
        for (Tower t : towers)
            if (t.getTileX() == tileX && t.getTileY() == tileY) return false;
        return true;
    }

    public void selectTowerAt(int mx, int my) {
        Tower clicked = null;
        for (Tower t : towers) {
            if (mx >= t.getX() && mx <= t.getX() + TILE_SIZE &&
                my >= t.getY() && my <= t.getY() + TILE_SIZE) {
                clicked = t;
                break;
            }
        }
        clearSelected();
        if (clicked != null) clicked.setSelected(true);
    }

    private void clearSelected() {
        for (Tower t : towers) t.setSelected(false);
    }

    public Tower getSelectedTower() {
        for (Tower t : towers) if (t.isSelected()) return t;
        return null;
    }

    public void upgradeSelectedTower() {
        Tower t = getSelectedTower();
        if (t != null && t.canUpgrade()) t.upgrade();
    }

    public void sellSelectedTower() {
        Tower t = getSelectedTower();
        if (t != null) towers.remove(t);
    }

    public boolean handleButtonClick(int mx, int my) {
        Tower t = getSelectedTower();
        if (t == null) return false;

        int bx = t.getX() + TILE_SIZE + 4;
        int by = t.getY();
        int bw = 60, bh = 18;

        if (t.canUpgrade() && !t.isUpgrading())
            if (mx >= bx && mx <= bx + bw && my >= by && my <= by + bh) {
                upgradeSelectedTower(); return true;
            }

        int sellY = by + bh + 4;
        if (mx >= bx && mx <= bx + bw && my >= sellY && my <= sellY + bh) {
            sellSelectedTower(); return true;
        }

        return false;
    }

    private void drawTowerButtons(Graphics2D g2, Tower t) {
    int bx = t.getX() + TILE_SIZE + 8;
    int by = t.getY();
    int bw = 84;
    int bh = 24;
    int gap = 8;

    Graphics2D g = (Graphics2D) g2.create();
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    Font font = new Font("Arial", Font.BOLD, 12);
    g.setFont(font);
    FontMetrics fm = g.getFontMetrics();

    // ===== Upgrade / Upgrading button =====
    if (t.canUpgrade() && !t.isUpgrading()) {
        drawModernButton(
                g,
                bx, by, bw, bh,
                new Color(255, 210, 60),   // main
                new Color(255, 235, 120),  // highlight
                new Color(180, 130, 20),   // border
                new Color(0, 0, 0, 80),    // shadow
                "Upgrade",
                Color.BLACK,
                fm
        );
    } else if (t.isUpgrading()) {
        drawModernButton(
                g,
                bx, by, bw, bh,
                new Color(120, 140, 170),
                new Color(170, 185, 205),
                new Color(70, 85, 105),
                new Color(0, 0, 0, 80),
                "Upgrading",
                Color.WHITE,
                fm
        );
    }

    // ===== Sell button =====
    int sellY = by + bh + gap;
    drawModernButton(
            g,
            bx, sellY, bw, bh,
            new Color(220, 70, 70),
            new Color(245, 120, 120),
            new Color(150, 35, 35),
            new Color(0, 0, 0, 80),
            "Sell",
            Color.WHITE,
            fm
    );

    g.dispose();
}

private void drawModernButton(Graphics2D g, int x, int y, int w, int h,
                              Color baseColor, Color topColor, Color borderColor,
                              Color shadowColor, String text, Color textColor,
                              FontMetrics fm) {

    int arc = 12;

    // Shadow
    g.setColor(shadowColor);
    g.fillRoundRect(x + 3, y + 3, w, h, arc, arc);

    // Main gradient
    GradientPaint gp = new GradientPaint(
            x, y, topColor,
            x, y + h, baseColor
    );
    g.setPaint(gp);
    g.fillRoundRect(x, y, w, h, arc, arc);

    // Top shine
    g.setColor(new Color(255, 255, 255, 60));
    g.fillRoundRect(x + 2, y + 2, w - 4, h / 2, arc - 4, arc - 4);

    // Border
    g.setColor(borderColor);
    g.setStroke(new BasicStroke(1.5f));
    g.drawRoundRect(x, y, w, h, arc, arc);

    // Text centered
    int tx = x + (w - fm.stringWidth(text)) / 2;
    int ty = y + ((h - fm.getHeight()) / 2) + fm.getAscent();

    // Text shadow
    g.setColor(new Color(0, 0, 0, 90));
    g.drawString(text, tx + 1, ty + 1);

    // Main text
    g.setColor(textColor);
    g.drawString(text, tx, ty);
}

    private void drawSelectedInfo(Graphics2D g2, Tower t) {
        int x = t.getX() + TILE_SIZE + 4;
        int y = t.getY() - 30;

        g2.setColor(new Color(0, 0, 0, 130));
        g2.fillRoundRect(x, y, 100, 26, 6, 6);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.drawString("Lv:" + t.getDisplayLevel() + " Dmg:" + t.getDmg(), x + 6, y + 11);
        g2.drawString("Range:" + (int)t.getRange(), x + 6, y + 22);
    }

    public ArrayList<Tower> getTowers() {
        return towers;
    }
}