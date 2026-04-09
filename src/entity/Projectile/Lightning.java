package entity.Projectile;

import entity.monster.EnemyState;
import entity.monster.Monster;
import java.util.ArrayList;

/**
 * Chain Lightning của Lightning Tower.
 *
 * Không bay theo đường thẳng như fireball — thay vào đó:
 * 1. Hit target ngay lập tức (instant).
 * 2. "Nhảy" sang các quái gần nhất trong chainRadius, tối đa chainTargets lần.
 * 3. Mỗi lần nhảy damage giảm theo chainDmgFactor.
 * 4. Tất cả quái bị trúng → applyStun().
 *
 * Lightning lưu lại danh sách các "đoạn bolt" (startX,startY→endX,endY)
 * để FlashRenderer vẽ tia sét trong vài tick.
 */
public class Lightning {

    // ── Bolt segment dùng cho renderer ───────────────────────────────────────
    public static class Bolt {
        public final float x1, y1, x2, y2;
        public Bolt(float x1, float y1, float x2, float y2) {
            this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
        }
    }

    private final ArrayList<Bolt> bolts = new ArrayList<>();

    // ── State ─────────────────────────────────────────────────────────────────
    private int  liveTicks = 0;
    private static final int MAX_LIVE_TICKS = 12; // hiển thị tia trong 12 tick
    private boolean alive = true;

    // ── Chain radius ──────────────────────────────────────────────────────────
    private static final float CHAIN_RADIUS = 100f;

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Tạo lightning tức thì: hit target + chain.
     * Gọi một lần từ TowerManager khi tower bắn.
     *
     * @param originX / originY  vị trí nòng tháp (điểm bắt đầu tia sét đầu tiên)
     * @param target             quái chính bị nhắm
     * @param allMonsters        toàn bộ quái để tìm chain
     * @param directDmg          dmg đợt 1
     * @param stunDuration       thời gian stun (tick)
     * @param chainTargets       số lần nhảy thêm
     * @param chainDmgFactor     hệ số dmg mỗi lần nhảy
     */
    public Lightning(float originX, float originY, Monster target,
                     ArrayList<Monster> allMonsters,
                     int directDmg, int stunDuration,
                     int chainTargets, float chainDmgFactor) {

        applyChain(originX, originY, target, allMonsters,
                   directDmg, stunDuration, chainTargets, chainDmgFactor,
                   new ArrayList<>());
    }

    private void applyChain(float fromX, float fromY, Monster current,
                            ArrayList<Monster> allMonsters,
                            int dmg, int stunDuration,
                            int remaining, float factor,
                            ArrayList<Monster> alreadyHit) {

        if (current == null || current.getState() == EnemyState.DYING) return;

        float cx = current.getX() + 16;
        float cy = current.getY() + 16;

        // Ghi bolt segment
        bolts.add(new Bolt(fromX, fromY, cx, cy));

        // Apply dmg + stun
        current.hurt(dmg);
        current.getStatusEffect().applyStun(stunDuration);
        alreadyHit.add(current);

        if (remaining <= 0) return;

        // Tìm quái gần nhất chưa bị hit
        Monster next = null;
        float minDist = CHAIN_RADIUS * CHAIN_RADIUS;
        for (Monster m : allMonsters) {
            if (alreadyHit.contains(m)) continue;
            if (m.getState() == EnemyState.DYING) continue;
            float dx = (m.getX() + 16) - cx;
            float dy = (m.getY() + 16) - cy;
            float d2 = dx * dx + dy * dy;
            if (d2 < minDist) { minDist = d2; next = m; }
        }

        if (next != null) {
            applyChain(cx, cy, next, allMonsters,
                       (int)(dmg * factor), stunDuration,
                       remaining - 1, factor, alreadyHit);
        }
    }

    /** Gọi mỗi tick sau khi tạo — trả về false khi hết thời gian hiển thị. */
    public boolean update() {
        liveTicks++;
        if (liveTicks >= MAX_LIVE_TICKS) alive = false;
        return alive;
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public ArrayList<Bolt> getBolts()   { return bolts; }
    public boolean isAlive()            { return alive; }
    /** 0..1 fade progress cho renderer */
    public float getFadeAlpha() {
        return 1f - (float) liveTicks / MAX_LIVE_TICKS;
    }
}