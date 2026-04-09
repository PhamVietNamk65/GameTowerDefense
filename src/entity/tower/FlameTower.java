package entity.tower;

import utils.Constants;

/**
 * Flame Wizard Tower – thiêu đốt quái, gây dmg theo thời gian (DoT).
 * Chỉ có 3 level (wizLevel 1 → 3).
 * Sprite được vẽ bởi FlameRenderer / WirzardRenderer, KHÔNG dùng TowerRenderer.
 */
public class FlameTower extends Tower {

    private static final int   COST          = 100;
    private static final int   DIRECT_DMG    = 8;
    private static final int   BURN_DMG      = 5;
    private static final int   BURN_DURATION = 180;
    private static final int   SPLASH_RADIUS = 40;
    private static final float RANGE         = 120f;
    private static final float COOLDOWN      = 80f;

    /** Level hiển thị: 1, 2, 3. */
    private int wizLevel = 1;

    private boolean fireFlag = false;

    public FlameTower(int x, int y, int id) {
        super(x, y, id, Constants.Towers.WIZARD, COST);
    }

    @Override
    public void update() {
        super.update();
        fireFlag = false;
    }

    public void triggerFire() {
        fireFlag = true;
        resetCooldown();
    }

    public boolean shouldFire() { return fireFlag; }

    // ── Giới hạn 3 level ─────────────────────────────────────────────────────
    @Override
    public boolean canUpgrade() { return wizLevel < 3; }

    @Override
    public boolean isMaxLevel() { return wizLevel >= 3; }

    @Override
    public void upgrade() {
        if (!canUpgrade() || isUpgrading()) return;
        wizLevel++;
        super.upgrade();
    }

    // ── Stats ─────────────────────────────────────────────────────────────────
    @Override public int   getDmg()      { return DIRECT_DMG; }
    @Override public float getRange()    { return RANGE; }
    @Override public float getCooldown() { return COOLDOWN; }
    @Override public int   getCost()     { return COST; }

    public int getBurnDmg()      { return BURN_DMG; }
    public int getBurnDuration() { return BURN_DURATION; }
    public int getSplashRadius() { return SPLASH_RADIUS; }

    /** Level 1-3, dùng cho FlameRenderer. */
    public int getLevel() { return wizLevel; }
}