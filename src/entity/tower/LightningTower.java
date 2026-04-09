package entity.tower;

import utils.Constants;

/**
 * Lightning Wizard Tower – stun quái 1.5 giây, chain sang 3 mục tiêu.
 * Chỉ có 3 level (wizLevel 1 → 3).
 * Sprite được vẽ bởi LightningRenderer / WirzardRenderer, KHÔNG dùng TowerRenderer.
 */
public class LightningTower extends Tower {

    private static final int   COST             = 130;
    private static final int   DIRECT_DMG       = 20;
    private static final int   STUN_DURATION    = 90;
    private static final int   CHAIN_TARGETS    = 3;
    private static final float CHAIN_DMG_FACTOR = 0.6f;
    private static final float RANGE            = 140f;
    private static final float COOLDOWN         = 100f;

    /** Level hiển thị: 1, 2, 3. */
    private int wizLevel = 1;

    private boolean lightningFlag = false;

    public LightningTower(int x, int y, int id) {
        super(x, y, id, Constants.Towers.WIZARD, COST);
    }

    @Override
    public void update() {
        super.update();
        lightningFlag = false;
    }

    public void triggerLightning() {
        lightningFlag = true;
        resetCooldown();
    }

    public boolean shouldLightning() { return lightningFlag; }

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

    public int   getStunDuration()   { return STUN_DURATION;   }
    public int   getChainTargets()   { return CHAIN_TARGETS;   }
    public float getChainDmgFactor() { return CHAIN_DMG_FACTOR; }

    /** Level 1-3, dùng cho LightningRenderer. */
    public int getLevel() { return wizLevel; }
}