package entity.tower;

import asset.CanonAsset;
import utils.Constants;

/**
 * Canon Tower – 4 cấp độ (mk1 → mk4), mỗi cấp dùng sprite riêng 11 frames.
 *
 * Logic animation:
 *  - Bình thường: giữ frame 0 (idle, nòng đóng).
 *  - Khi bắn: chạy frame 0→10 một lần → spawn bomb ở frame giữa (frame 5) → về frame 0.
 *
 * Cân bằng stats:
 *  MK1  cost=120  dmg=25  splash=48px  cd=120tick  range=110px
 *  MK2  cost=150  dmg=35  splash=56px  cd=110tick  range=120px
 *  MK3  cost=180  dmg=48  splash=64px  cd=100tick  range=130px
 *  MK4  cost=220  dmg=65  splash=72px  cd= 90tick  range=140px
 */
public class CanonTower extends Tower {

    // ── Stats per level ───────────────────────────────────────────────────────
    private static final int[]   SPLASH_RADIUS = {48, 56, 64, 72};
    private static final int[]   LEVEL_COST    = {120, 150, 180, 220};
    private static final int[]   LEVEL_DMG     = {25,  35,  48,  65};
    private static final float[] LEVEL_RANGE   = {110f, 120f, 130f, 140f};
    private static final float[] LEVEL_CD      = {120f, 110f, 100f,  90f};

    public static final int MAX_CANON_LEVEL = 4;   // cấp tối đa (index 0..3)

    /** Cấp hiện tại 0-based (0=MK1 … 3=MK4) */
    private int canonLevel = 0;

    // ── Turret rotation ───────────────────────────────────────────────────────
    private float turretAngle = -(float)(Math.PI / 2); // mặc định hướng lên
    private static final float ROTATE_SPEED = 0.10f;   // rad/tick
    private float targetAngle = -(float)(Math.PI / 2);

    // ── Shoot animation ───────────────────────────────────────────────────────
    private static final int TOTAL_FRAMES  = CanonAsset.FRAMES_PER_TURRET; // 11
    private static final int SPAWN_FRAME   = 5;   // frame giữa → spawn bomb
    private static final int ANIM_SPEED    = 4;   // ticks / frame khi đang bắn

    /** Frame đang hiển thị (0..10) */
    private int turretFrame = 0;
    private int turretTick  = 0;

    /** true = đang chạy animation bắn */
    private boolean shooting = false;

    /** Flag spawn bomb (chỉ bật đúng 1 tick tại frame SPAWN_FRAME) */
    private boolean spawnBombFlag = false;

    // ── Constructor ───────────────────────────────────────────────────────────
    public CanonTower(int x, int y, int id) {
        super(x, y, id, Constants.Towers.CANNON, LEVEL_COST[0]);
    }

    // ── Override update ───────────────────────────────────────────────────────
    @Override
    public void update() {
        super.update();          // xử lý upgrade, cdTick, v.v.
        rotateTurret();
        updateShootAnim();
    }

    // ── Turret rotation ───────────────────────────────────────────────────────
    /** Gọi từ TowerManager mỗi tick khi có target. */
    public void aimAt(float targetX, float targetY) {
        float dx = targetX - getCenterX();
        float dy = targetY - getCenterY();
        targetAngle = (float) Math.atan2(dy, dx);
    }

    private void rotateTurret() {
        float diff = targetAngle - turretAngle;
        while (diff >  Math.PI) diff -= 2 * Math.PI;
        while (diff < -Math.PI) diff += 2 * Math.PI;

        if (Math.abs(diff) <= ROTATE_SPEED) {
            turretAngle = targetAngle;
        } else {
            turretAngle += Math.signum(diff) * ROTATE_SPEED;
        }
    }

    // ── Shoot animation ───────────────────────────────────────────────────────
    /**
     * Bắt đầu animation bắn (gọi từ TowerManager khi canAttack() = true).
     * Không cần gọi nếu đang shooting.
     */
    public void triggerShoot() {
        if (shooting) return;
        shooting      = true;
        turretFrame   = 0;
        turretTick    = 0;
        spawnBombFlag = false;
    }

    private void updateShootAnim() {
        spawnBombFlag = false;          // reset mỗi tick

        if (!shooting) return;
        if (isUpgrading()) { shooting = false; turretFrame = 0; return; }

        turretTick++;
        if (turretTick < ANIM_SPEED) return;
        turretTick = 0;

        turretFrame++;

        // Đúng frame giữa → báo spawn bomb
        if (turretFrame == SPAWN_FRAME) {
            spawnBombFlag = true;
        }

        // Hết animation → về idle
        if (turretFrame >= TOTAL_FRAMES) {
            turretFrame = 0;
            shooting    = false;
        }
    }

    /**
     * Trả về true đúng 1 tick tại thời điểm cần spawn bomb.
     * TowerManager kiểm tra sau mỗi update().
     */
    public boolean shouldSpawnBomb() {
        return spawnBombFlag;
    }

    // ── Upgrade ───────────────────────────────────────────────────────────────

    /**
     * Override Tower.upgrade() — tăng canonLevel thay vì towerLevel.
     * Được gọi từ TowerActionListener.onUpgrade().
     */
    @Override
    public void upgrade() {
        upgradeCanon();
    }

    public void upgradeCanon() {
        if (!canUpgradeCanon() || isUpgrading()) return;
        canonLevel++;
        setUpgrading(true);
        turretFrame   = 0;
        turretTick    = 0;
        shooting      = false;
        System.out.printf("[CanonTower] Upgraded to MK%d (dmg=%d, splash=%d, range=%.0f)%n",
                canonLevel + 1, getDmg(), getSplashRadius(), getRange());
    }

    public boolean canUpgradeCanon() { return canonLevel < MAX_CANON_LEVEL - 1; }

    /** Dùng cho TowerUI để kiểm tra đã max chưa */
    @Override
    public boolean isMaxLevel() { return canonLevel >= MAX_CANON_LEVEL - 1; }

    /** Chi phí upgrade tiếp theo — dùng cho TowerUI */
    @Override
    public int getNextUpgradeCost() {
        return canUpgradeCanon() ? LEVEL_COST[canonLevel + 1] : 0;
    }

    // ── Stats override ────────────────────────────────────────────────────────
    @Override public int   getDmg()      { return LEVEL_DMG   [canonLevel]; }
    @Override public float getRange()    { return LEVEL_RANGE [canonLevel]; }
    @Override public float getCooldown() { return LEVEL_CD    [canonLevel]; }
    @Override public int   getCost()     { return LEVEL_COST  [canonLevel]; }

    public int getSplashRadius() { return SPLASH_RADIUS[canonLevel]; }
    public int getCanonLevel()   { return canonLevel; }
    public int getDisplayLevel() { return canonLevel + 1; }  // 1-based cho UI

    // ── Spawn point (đầu nòng) ────────────────────────────────────────────────
    /** X vị trí spawn bomb (đầu nòng, cách tâm 26px theo hướng turret). */
    public float getBombSpawnX() {
        return getCenterX() + (float)(Math.cos(turretAngle) * 26);
    }

    public float getBombSpawnY() {
        return getCenterY() + (float)(Math.sin(turretAngle) * 26);
    }

    // ── Getters cho renderer ──────────────────────────────────────────────────
    public float getTurretAngle() { return turretAngle; }
    public int   getTurretFrame() { return turretFrame; }
    public boolean isShooting()   { return shooting; }
}