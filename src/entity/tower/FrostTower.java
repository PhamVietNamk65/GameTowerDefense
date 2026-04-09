package entity.tower;

import utils.Constants;

/**
 * Frost Wizard Tower – làm chậm quái 40% trong 2.5 giây.
 * Chỉ có 3 level (wizLevel 1 → 3).
 * Sprite được vẽ bởi FrostRenderer / WirzardRenderer, KHÔNG dùng TowerRenderer.
 */
public class FrostTower extends Tower {

    private static final int   COST          = 110;
    private static final int   DIRECT_DMG    = 10;
    private static final int   SLOW_DURATION = 150;
    private static final int   SPLASH_RADIUS = 50;
    private static final float RANGE         = 130f;
    private static final float COOLDOWN      = 70f;

    /** Level hiển thị: 1, 2, 3. */
    private int wizLevel = 1;

    private boolean frostFlag = false;

    public FrostTower(int x, int y, int id) {
        super(x, y, id, Constants.Towers.WIZARD, COST);
    }

    @Override
    public void update() {
        super.update();
        frostFlag = false;
    }

    public void triggerFrost() {
        frostFlag = true;
        resetCooldown();
    }

    public boolean shouldFrost() { return frostFlag; }

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

    public int getSlowDuration() { return SLOW_DURATION; }
    public int getSplashRadius() { return SPLASH_RADIUS; }

    /** Level 1-3, dùng cho FrostRenderer. */
    public int getLevel() { return wizLevel; }
}