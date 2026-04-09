package entity.monster;

/**
 * Các trạng thái hiệu ứng áp lên Monster bởi Wizard towers.
 *
 *  BURN    – Flame Tower: gây dmg theo thời gian mỗi BURN_TICK_RATE tick.
 *  SLOW    – Frost Tower: giảm tốc độ di chuyển xuống còn SLOW_FACTOR%.
 *  STUNNED – Lightning Tower: quái đứng yên trong STUN_DURATION tick.
 *
 * Cách dùng trong Monster:
 *   monster.applyBurn(dmgPerTick, durationTicks);
 *   monster.applySlow(durationTicks);
 *   monster.applyStun(durationTicks);
 *   // Gọi monster.updateEffects() mỗi tick trong Monster.update()
 */
public class StatusEffect {

    // ── Burn ─────────────────────────────────────────────────────────────────
    public static final int BURN_TICK_RATE = 30;  // cứ 30 tick (0.5s) gây dmg 1 lần
    public static final int BURN_DURATION  = 180; // 3 giây mặc định

    // ── Slow ──────────────────────────────────────────────────────────────────
    public static final float SLOW_FACTOR   = 0.4f;  // còn 40% tốc độ gốc
    public static final int   SLOW_DURATION = 150;   // 2.5 giây

    // ── Stun ──────────────────────────────────────────────────────────────────
    public static final int STUN_DURATION = 90;  // 1.5 giây

    // ─────────────────────────────────────────────────────────────────────────
    // Trạng thái instance (gắn vào mỗi Monster)
    // ─────────────────────────────────────────────────────────────────────────

    // Burn
    private boolean burning     = false;
    private int     burnTick    = 0;
    private int     burnDuration= 0;
    private int     burnDmg     = 0;
    private int     burnTimer   = 0; // đếm interval

    // Slow
    private boolean slowed      = false;
    private int     slowTick    = 0;

    // Stun
    private boolean stunned     = false;
    private int     stunTick    = 0;

    // ─────────────────────────────────────────────────────────────────────────

    /** @return damage cần trừ khỏi monster tick này (0 nếu không bị burn tick) */
    public int update() {
        int dmgThisTick = 0;

        // ── Burn update ──
        if (burning) {
            burnTick++;
            burnTimer++;
            if (burnTimer >= BURN_TICK_RATE) {
                burnTimer = 0;
                dmgThisTick = burnDmg;
            }
            if (burnTick >= burnDuration) {
                burning   = false;
                burnTick  = 0;
                burnTimer = 0;
            }
        }

        // ── Slow update ──
        if (slowed) {
            slowTick++;
            if (slowTick >= slowTick) { // chỉ reset khi hết
            }
            if (slowTick >= SLOW_DURATION) {
                slowed   = false;
                slowTick = 0;
            }
        }

        // ── Stun update ──
        if (stunned) {
            stunTick++;
            if (stunTick >= STUN_DURATION) {
                stunned  = false;
                stunTick = 0;
            }
        }

        return dmgThisTick;
    }

    // ── Apply effects (gọi từ Wizard towers) ─────────────────────────────────

    public void applyBurn(int dmgPerTick, int durationTicks) {
        burning      = true;
        burnDmg      = Math.max(burnDmg, dmgPerTick); // lấy giá trị cao hơn nếu đang cháy
        burnDuration = Math.max(burnDuration - burnTick, durationTicks); // refresh
        burnTick     = 0;
        burnTimer    = 0;
    }

    public void applySlow(int durationTicks) {
        slowed   = true;
        slowTick = 0; // refresh
    }

    /** Slow duration tuỳ chỉnh */
    public void applySlowDuration(int durationTicks) {
        slowed   = true;
        slowTick = Math.max(0, SLOW_DURATION - durationTicks);
    }

    public void applyStun(int durationTicks) {
        stunned  = true;
        stunTick = Math.max(0, STUN_DURATION - durationTicks); // refresh
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public boolean isBurning()  { return burning;  }
    public boolean isSlowed()   { return slowed;   }
    public boolean isStunned()  { return stunned;  }

    /** Tỉ lệ tốc độ hiện tại (1.0 = bình thường, SLOW_FACTOR nếu bị slow/stun) */
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
        return stunned ? (float) stunTick / STUN_DURATION : 0f;
    }
}