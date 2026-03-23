package Manager;

import entity.Tower;
import helpz.Constants;
import helpz.LoadSave;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class TowerManager {

    // ── Particle ─────────────────────────────────────────────
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
            x+=vx; y+=vy; vy+=0.15f; vx*=0.96f; life-=0.025f;
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

    // ── Constants ────────────────────────────────────────────
    private static final int TILE_SIZE = 32;
    private static final int ARCHER_W  = 48;
    private static final int ARCHER_H  = 48;

    // Sprite sheet info per level — frame width đều 70px cho tất cả level
    private static final int[] FRAME_COUNTS = {1, 4, 4, 6, 6, 6, 6};
    private static final int[] FRAME_WIDTHS = {70, 70, 70, 70, 70, 70, 70};
    private static final int   FRAME_HEIGHT = 130;

    // drawW = 70 * 96 / 130 = 51 cho tất cả level
    private static final int DRAW_H = 96;

    // Archer chỉ xuất hiện ở level 1,2,3,5,6 (index 0,1,2,4,5)
    private static final boolean[] ARCHER_VISIBLE = {true, true, true, false, true, true, false};

    // drawW=51 cho tất cả, ARCHER_W=48, căn giữa: TOP_X = (51-48)/2 = 1
    // TOP_Y = screen_y_của_đỉnh_ván_gỗ - 48 (chiều cao archer)
    // lv1: wood_screen=64 -> TOP_Y=16
    // lv2: wood_screen=64 -> TOP_Y=16
    // lv3: wood_screen=56 -> TOP_Y=8
    // lv4: không dùng
    // lv5: wood_screen=57 -> TOP_Y=9
    // lv6: wood_screen=48 -> TOP_Y=0
    // lv7: không dùng
    private static final int[] ARCHER_TOP_X = {1, 1, 1, 0, 1, 1, 0};
    private static final int[] ARCHER_TOP_Y = {42, 36, 30, 0, 24, 24, 0};

    private static final int[][] PARTICLE_COLORS = {
        {200,160, 60}, {255,200, 50}, {120,200,255},
        { 80,220,100}, {200, 80,255}, {255,120, 40}, {255,255,180}
    };

    // ── Fields ───────────────────────────────────────────────
    private final ArrayList<Tower>    towers    = new ArrayList<>();
    private final ArrayList<Particle> particles = new ArrayList<>();
    private final Random random = new Random();

    private BufferedImage[][] towerFrames;
    private int[] towerDrawW;
    private BufferedImage[][][] archerAnimations;

    private int     towerId      = 0;
    private boolean placingTower = false;
    private int     towerToPlace = Constants.Towers.ARCHER;

    // ── Constructor ──────────────────────────────────────────
    public TowerManager() {
        loadTowerFrames();
        loadArcherAnimations();
    }

    // ── Load ─────────────────────────────────────────────────
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
            int fw = FRAME_WIDTHS[lv];     // kích thước frame gốc
            int fh = FRAME_HEIGHT;

            // Tính draw width giữ tỉ lệ với DRAW_H
            towerDrawW[lv] = Math.max(1, fw * DRAW_H / fh);

            towerFrames[lv] = new BufferedImage[n];
            for (int f = 0; f < n; f++) {
                // Đảm bảo không vượt quá chiều rộng sheet
                int x0 = f * fw;
                int x1 = Math.min(x0 + fw, sheet.getWidth());
                if (x0 >= sheet.getWidth()) break;
                towerFrames[lv][f] = sheet.getSubimage(x0, 0, x1 - x0, fh);
            }

            System.out.printf("Level %d: %d frames, frame=%dx%d, drawW=%d%n",
                    lv+1, n, fw, fh, towerDrawW[lv]);
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

    // ── Update ───────────────────────────────────────────────
    public void update() {
        for (Tower t : towers) {
            t.update();
            if (t.isJustStartedUpgrade())  spawnParticles(t, false);
            if (t.isJustFinishedUpgrade()) spawnParticles(t, true);
            if (t.getTowerType() == Constants.Towers.ARCHER) {
                autoUpdateArcher(t);
                t.updateAnimation(getFrameAmount(t));
            }
        }
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) if (!it.next().update()) it.remove();
    }

    private void spawnParticles(Tower t, boolean burst) {
        int lv = clamp(t.getTowerLevel(), 0, 6);
        int[] col = PARTICLE_COLORS[lv];
        float cx = t.getCenterX(), cy = t.getCenterY();
        int count = burst ? 28 : 14;
        float spd = burst ? 3.2f : 1.8f;
        for (int i = 0; i < count; i++) {
            double angle = Math.PI*2*i/count + random.nextDouble()*0.4;
            float speed = spd + random.nextFloat()*1.5f;
            float vx = (float)(Math.cos(angle)*speed);
            float vy = (float)(Math.sin(angle)*speed) - 1.5f;
            float life = 0.6f + random.nextFloat()*0.5f;
            float size = burst ? (4+random.nextFloat()*4) : (3+random.nextFloat()*2);
            int r = (i%3==0)?255:col[0], g=(i%3==0)?220:col[1], b=(i%3==0)?50:col[2];
            particles.add(new Particle(cx, cy, vx, vy, life, r, g, b, size));
        }
    }

    private void autoUpdateArcher(Tower t) {
        if (t.isUpgrading()) return;
        if (t.getAnimState() != Tower.IDLE) return;
        int dir = Tower.SIDE;
        int mode = (t.getId()/2)%3;
        if (mode==1) dir=Tower.UP; else if (mode==2) dir=Tower.DOWN;
        if (t.isCooldownOver()) { t.setAnimation(Tower.PREATTACK,dir); t.resetCooldown(); }
        else t.setAnimation(Tower.IDLE,dir);
    }

    // ── Draw ─────────────────────────────────────────────────
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        if (placingTower) {
            g2.setColor(new Color(0,255,0,100));
            g2.setFont(new Font("Arial",Font.BOLD,14));
            g2.drawString("PLACING ARCHER - click map to place",20,20);
        }

        for (Tower t : towers) drawTower(g2, t);

        // Particles trên cùng
        Composite oc = g2.getComposite();
        for (Particle p : particles) p.draw(g2);
        g2.setComposite(oc);

        Tower sel = getSelectedTower();
        if (sel != null) { drawSelectedInfo(g2,sel); drawTowerButtons(g2,sel); }
    }

    private void drawTower(Graphics2D g2, Tower t) {
        int lv    = clamp(t.getTowerLevel(), 0, 6);
        int frame = t.getTowerAnimFrame();

        if (towerFrames[lv] == null || towerFrames[lv].length == 0) return;
        frame = clamp(frame, 0, towerFrames[lv].length - 1);
        BufferedImage img = towerFrames[lv][frame];
        if (img == null) return;

        int dw = towerDrawW[lv];
        int dh = DRAW_H;

        // Căn giữa theo x, đáy ảnh = đáy ô tile
        int drawX = t.getX() - (dw - TILE_SIZE) / 2;
        int drawY = t.getY() + TILE_SIZE - dh;

        // Flash brightness khi upgrade
        if (t.isUpgrading() && t.getFlashAlpha() > 0) {
            float bright = 1.0f + (t.getFlashAlpha() / 180f);
            g2.drawImage(applyBrightness(img, bright), drawX, drawY, dw, dh, null);
        } else {
            g2.drawImage(img, drawX, drawY, dw, dh, null);
        }

        // Progress bar
        if (t.isUpgrading()) drawProgressBar(g2, t, drawX, drawY, dw);

        // Viền selected
        if (t.isSelected()) {
            g2.setColor(Color.YELLOW);
            g2.drawRect(t.getX()-2, t.getY()-2, TILE_SIZE+4, TILE_SIZE+4);
        }

        // Archer (ẩn khi upgrade)
        if (!t.isUpgrading()) drawArcher(g2, t, drawX, drawY);
    }

    private BufferedImage applyBrightness(BufferedImage src, float scale) {
        float[] s = {scale,scale,scale,1f};
        float[] o = {0f,0f,0f,0f};
        java.awt.image.RescaleOp op = new java.awt.image.RescaleOp(s, o, null);
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bg = dst.createGraphics(); bg.drawImage(src,0,0,null); bg.dispose();
        return op.filter(dst, null);
    }

    private void drawProgressBar(Graphics2D g2, Tower t, int drawX, int drawY, int dw) {
        int barW = dw;
        int barH = 5;
        int barX = drawX;
        int barY = drawY - 14;
        float progress = t.getUpgradeProgress();

        g2.setColor(new Color(30,30,30,180));
        g2.fillRoundRect(barX-1, barY-1, barW+2, barH+2, 3, 3);

        int filled = (int)(barW * progress);
        if (filled > 0) {
            g2.setColor(Color.getHSBColor(0.15f - progress*0.08f, 0.9f, 1.0f));
            g2.fillRoundRect(barX, barY, filled, barH, 2, 2);
        }

        Stroke os = g2.getStroke();
        g2.setStroke(new BasicStroke(0.5f));
        g2.setColor(new Color(180,180,180,100));
        g2.drawRoundRect(barX, barY, barW, barH, 2, 2);
        g2.setStroke(os);

        g2.setFont(new Font("Arial",Font.BOLD,8));
        g2.setColor(new Color(255,240,160));
        String label = "LV"+(t.getTowerLevel()+1)+"→"+(t.getTowerLevel()+2);
        int tw = g2.getFontMetrics().stringWidth(label);
        g2.drawString(label, barX+(barW-tw)/2, barY-1);
    }

    private void drawArcher(Graphics2D g2, Tower t, int drawX, int drawY) {
        if (t.getTowerType() != Constants.Towers.ARCHER) return;
        int lv = clamp(t.getTowerLevel(), 0, 6);
        // Chỉ vẽ archer ở các level được phép
        if (!ARCHER_VISIBLE[lv]) return;
        BufferedImage img = getArcherFrame(t);
        if (img == null) return;
        // Vị trí archer = đỉnh ván gỗ của tháp
        int ax = drawX + ARCHER_TOP_X[lv];
        int ay = drawY + ARCHER_TOP_Y[lv];
        g2.drawImage(img, ax, ay, null);
    }

    private BufferedImage getArcherFrame(Tower t) {
        BufferedImage[] frames = archerAnimations[t.getDirection()][t.getAnimState()];
        if (frames==null || frames.length==0) return null;
        return frames[t.getAnimIndex() % frames.length];
    }

    private int getFrameAmount(Tower t) {
        BufferedImage[] frames = archerAnimations[t.getDirection()][t.getAnimState()];
        return frames==null ? 0 : frames.length;
    }

    private int clamp(int v, int min, int max) { return Math.max(min, Math.min(max, v)); }

    // ── Tower placement ──────────────────────────────────────
    public void startPlacingTower(int type) { placingTower=true; towerToPlace=type; clearSelected(); }
    public boolean isPlacingTower()         { return placingTower; }
    public void cancelPlacing()             { placingTower=false; }

    public void placeTower(int tileX, int tileY) {
        towers.add(new Tower(tileX*TILE_SIZE, tileY*TILE_SIZE, towerId++, towerToPlace, tileX, tileY));
        placingTower=false;
    }

    public boolean canPlaceTower(int tileX, int tileY) {
        for (Tower t : towers) if (t.getTileX()==tileX && t.getTileY()==tileY) return false;
        return true;
    }

    public void selectTowerAt(int mx, int my) {
        Tower clicked = null;
        for (Tower t : towers)
            if (mx>=t.getX() && mx<=t.getX()+TILE_SIZE && my>=t.getY() && my<=t.getY()+TILE_SIZE) {
                clicked=t; break;
            }
        clearSelected();
        if (clicked!=null) clicked.setSelected(true);
    }

    private void clearSelected() { for (Tower t : towers) t.setSelected(false); }

    public Tower getSelectedTower() {
        for (Tower t : towers) if (t.isSelected()) return t;
        return null;
    }

    // ── Upgrade / Sell ────────────────────────────────────────
    public void upgradeSelectedTower() {
        Tower t = getSelectedTower();
        if (t!=null && t.canUpgrade()) t.upgrade();
    }

    public void sellSelectedTower() {
        Tower t = getSelectedTower();
        if (t!=null) towers.remove(t);
    }

    public boolean handleButtonClick(int mx, int my) {
        Tower t = getSelectedTower();
        if (t==null) return false;
        int bx = t.getX()+TILE_SIZE+4, by = t.getY()-8;
        if (t.canUpgrade() && !t.isUpgrading())
            if (mx>=bx && mx<=bx+88 && my>=by && my<=by+30) { upgradeSelectedTower(); return true; }
        if (mx>=bx && mx<=bx+88 && my>=by+40 && my<=by+70) { sellSelectedTower(); return true; }
        return false;
    }

    // ── UI ────────────────────────────────────────────────────
    private void drawTowerButtons(Graphics2D g2, Tower t) {
        int bx = t.getX()+TILE_SIZE+4, by = t.getY()-8;
        g2.setFont(new Font("Arial",Font.BOLD,14));
        if (t.canUpgrade() && !t.isUpgrading()) {
            g2.setColor(Color.YELLOW); g2.fillRect(bx,by,88,30);
            g2.setColor(Color.BLACK);  g2.drawRect(bx,by,88,30);
            g2.drawString("Upgrade", bx+12, by+20);
        } else if (t.isUpgrading()) {
            g2.setColor(new Color(160,160,80)); g2.fillRect(bx,by,88,30);
            g2.setColor(Color.DARK_GRAY); g2.drawRect(bx,by,88,30);
            g2.drawString("Upgrading...", bx+4, by+20);
        }
        g2.setColor(Color.RED);   g2.fillRect(bx,by+40,88,30);
        g2.setColor(Color.BLACK); g2.drawRect(bx,by+40,88,30);
        g2.drawString("Sell", bx+28, by+60);
    }

    private void drawSelectedInfo(Graphics2D g2, Tower t) {
        int x=t.getX()+TILE_SIZE+4, y=t.getY()-52;
        g2.setColor(new Color(0,0,0,150)); g2.fillRect(x,y,140,40);
        g2.setColor(Color.WHITE); g2.setFont(new Font("Arial",Font.PLAIN,12));
        g2.drawString("Lv:"+t.getDisplayLevel()+" Dmg:"+t.getDmg(), x+8, y+15);
        g2.drawString("Range:"+(int)t.getRange(), x+8, y+30);
    }

    public ArrayList<Tower> getTowers() { return towers; }
}