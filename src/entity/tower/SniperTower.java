package entity.tower;

import utils.Constants;

/**
 * SniperTower
 *
 * - 3 levels only (0, 1, 2)
 * - Base sprite is static per level — no base animation
 * - MC idle: 4-frame loop
 * - Targets ONLY Bee monsters (enforced in TowerManager)
 * - Default range: 450
 */
public class SniperTower extends Tower {

    // ── MC idle animation ─────────────────────────────────────────────────────
    private int mcAnimFrame = 0;
    private int mcAnimTick  = 0;
    private static final int MC_ANIM_SPEED  = 12;
    private static final int MC_FRAME_COUNT = 4;

    // ── shoot state ───────────────────────────────────────────────────────────
    private boolean readyToShoot = false;

    // ── facing ────────────────────────────────────────────────────────────────
    private boolean facingLeftSniper = false;

    public SniperTower(int x, int y, int id) {
        super(x, y, id, Constants.Towers.SNIPER,
              Constants.Towers.GetCost(Constants.Towers.SNIPER));
    }

    @Override
    public void update() {
        super.update();   // cooldown tick + upgrade flash (from Tower base class)
        updateMcAnim();
    }

    private void updateMcAnim() {
        mcAnimTick++;
        if (mcAnimTick >= MC_ANIM_SPEED) {
            mcAnimTick = 0;
            mcAnimFrame = (mcAnimFrame + 1) % MC_FRAME_COUNT;
        }
    }

    // ── shoot ─────────────────────────────────────────────────────────────────
    public void triggerShoot() {
        resetCooldown();
        readyToShoot = true;
    }

    /** Returns true ONCE per shot, then auto-resets. */
    public boolean shouldShoot() {
        if (readyToShoot) { readyToShoot = false; return true; }
        return false;
    }

    /**
     * Returns sprite index (0, 1, or 2) for towerBaseByLevel array.
     * Clamped to MAX_LEVEL-1 = 2.
     */
    public int getBaseLevel() {
        return Math.min(getTowerLevel(), Constants.Towers.GetMaxLevel(Constants.Towers.SNIPER));
    }

    // ── getters / setters ─────────────────────────────────────────────────────
    public int     getMcAnimFrame()               { return mcAnimFrame; }
    public boolean isFacingLeftSniper()           { return facingLeftSniper; }
    public void    setFacingLeftSniper(boolean v) { facingLeftSniper = v; }
}