package render;

import Manager.FlameManager;
import Manager.FrostManager;
import Manager.LightningManager;
import entity.monster.Monster;
import entity.tower.Tower;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Facade renderer cho cả 3 Wizard towers.
 * PlayingState chỉ cần khởi tạo và gọi duy nhất class này.
 *
 * Thứ tự vẽ (quan trọng cho z-order):
 *  1. Tower base + wizard sprites
 *  2. Projectiles (fireball, frost orb)
 *  3. Lightning bolts  ← vẽ sau cùng vì nó nằm "trên" mọi thứ
 *  4. Status effects trên quái (burn/slow/stun overlay)
 */
public class WirzardRenderer {

    private final FlameRenderer     flameRenderer     = new FlameRenderer();
    private final FrostRenderer     frostRenderer     = new FrostRenderer();
    private final LightningRenderer lightningRenderer = new LightningRenderer();

    // ── Vẽ towers ─────────────────────────────────────────────────────────────
    public void drawTowers(Graphics2D g2, ArrayList<Tower> towers, Tower selectedTower) {
        flameRenderer.drawTower(g2, towers, selectedTower);
        frostRenderer.drawTower(g2, towers, selectedTower);
        lightningRenderer.drawTower(g2, towers, selectedTower);
    }

    // ── Vẽ projectiles ────────────────────────────────────────────────────────
    public void drawProjectiles(Graphics2D g2,
                                 FlameManager flameManager,
                                 FrostManager frostManager,
                                 LightningManager lightningManager) {
        flameRenderer.drawFlames(g2, flameManager.getFlames());
        frostRenderer.drawFrosts(g2, frostManager.getFrosts());
        lightningRenderer.drawLightnings(g2, lightningManager.getLightnings());
    }

    // ── Vẽ status effects trên quái ───────────────────────────────────────────
    /**
     * Gọi SAU khi EnemyRenderer đã vẽ xong tất cả quái.
     * Thứ tự ưu tiên overlay: Burn → Slow → Stun (stun hiển thị trên cùng).
     */
    public void drawStatusEffects(Graphics2D g2, ArrayList<Monster> monsters) {
        flameRenderer.drawBurnEffect(g2, monsters);
        frostRenderer.drawSlowEffect(g2, monsters);
        lightningRenderer.drawStunEffect(g2, monsters);
    }
}