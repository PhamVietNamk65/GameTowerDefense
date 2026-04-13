package entity.monster;

/**
 * Các trạng thái hiệu ứng áp lên Monster bởi Wizard towers.
 *
 *  BURN    – Flame Tower: gây dmg theo thời gian mỗi BURN_TICK_RATE tick.
 *  SLOW    – Frost Tower: giảm tốc độ di chuyển xuống còn SLOW_FACTOR%.
 *  STUNNED – Lightning Tower: quái đứng yên trong STUN_DURATION tick.
 */
public class StatusEffect {

    // ── Burn ─────────────────────────────────────────────────────────────────
    public static final int   BURN_TICK_RATE = 30;   // cứ 30 tick (0.5s) gây dmg 1 lần
    public static final int   BURN_DURATION  = 180;  // 3 giây mặc định

    // ── Slow ──────────────────────────────────────────────────────────────────
    public static final float SLOW_FACTOR    = 0.4f; // còn 40% tốc độ gốc
    public static final int   SLOW_DURATION  = 150;  // 2.5 giây

    // ── Stun ──────────────────────────────────────────────────────────────────
    public static final int   STUN_DURATION  = 90;   // 1.5 giây

    // ── Instance state ────────────────────────────────────────────────────────

    // Burn
    private boolean burning      = false;
    private int     burnTick     = 0;
    private int     burnDuration = 0;
    private int     burnDmg      = 0;
    private int     burnTimer    = 0; // đếm interval giữa các lần gây dmg

    // Slow
    private boolean slowed       = false;
    private int     slowTick     = 0;
    private int     slowDuration = SLOW_DURATION;

    // Stun
    private boolean stunned      = false;
    private int     stunTick     = 0;
    private int     stunDuration = STUN_DURATION;

    // ── Update (gọi mỗi game tick trong Monster.update()) ─────────────────────

    /** @return damage cần trừ khỏi monster tick này (0 nếu không có burn tick) */
    public int update() {
        int dmgThisTick = 0;

        // ── Burn ──
        if (burning) {
            burnTick++;
            burnTimer++;
            if (burnTimer >= BURN_TICK_RATE) {
                burnTimer   = 0;
                dmgThisTick = burnDmg;
            }
            if (burnTick >= burnDuration) {
                burning   = false;
                burnTick  = 0;
                burnTimer = 0;
                burnDmg   = 0;
            }
        }

        // ── Slow ──
        if (slowed) {
            slowTick++;
            if (slowTick >= slowDuration) {
                slowed       = false;
                slowTick     = 0;
                slowDuration = SLOW_DURATION;
            }
        }

        // ── Stun ──
        if (stunned) {
            stunTick++;
            if (stunTick >= stunDuration) {
                stunned      = false;
                stunTick     = 0;
                stunDuration = STUN_DURATION;
            }
        }

        return dmgThisTick;
    }

    // ── Apply effects ─────────────────────────────────────────────────────────

    /** Áp burn — refresh nếu đang cháy, lấy dmg cao hơn */
    public void applyBurn(int dmgPerTick, int durationTicks) {
        burnDmg      = Math.max(burnDmg, dmgPerTick);
        burnDuration = durationTicks;
        burnTick     = 0;
        burnTimer    = 0;
        burning      = true;
    }

    /** Áp slow với duration mặc định, refresh nếu đang slow */
    public void applySlow(int durationTicks) {
        slowDuration = durationTicks;
        slowTick     = 0;
        slowed       = true;
    }

    /** Áp stun với duration tuỳ chỉnh, refresh nếu đang stun */
    public void applyStun(int durationTicks) {
        stunDuration = durationTicks;
        stunTick     = 0;
        stunned      = true;
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public boolean isBurning() { return burning; }
    public boolean isSlowed()  { return slowed;  }
    public boolean isStunned() { return stunned; }

    /** Tỉ lệ tốc độ hiện tại: 0 nếu stun, SLOW_FACTOR nếu slow, 1 nếu bình thường */
    public float getSpeedMultiplier() {
        if (stunned) return 0f;
        if (slowed)  return SLOW_FACTOR;
        return 1f;
    }

    /** Tiến độ burn (0..1) dùng để vẽ hiệu ứng */
    public float getBurnProgress() {
        return burning ? (float) burnTick / burnDuration : 0f;
    }

    /** Tiến độ stun (0..1) */
    public float getStunProgress() {
        return stunned ? (float) stunTick / stunDuration : 0f;
    }

    /** Tiến độ slow (0..1) */
    public float getSlowProgress() {
        return slowed ? (float) slowTick / slowDuration : 0f;
    }
}