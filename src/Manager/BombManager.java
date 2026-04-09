package Manager;

import entity.Projectile.Bomb;
import entity.monster.Monster;
import entity.tower.CanonTower;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Quản lý vòng đời của tất cả Bomb đang bay / đang nổ.
 */
public class BombManager {

    private final ArrayList<Bomb> bombs = new ArrayList<>();

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Spawn một quả bom từ vị trí nòng pháo của canon đến target.
     */
    public void spawnBomb(CanonTower canon, Monster target) {
        if (target == null) return;
        Bomb bomb = new Bomb(
                canon.getBombSpawnX(),
                canon.getBombSpawnY(),
                target,
                canon.getDmg(),
                canon.getSplashRadius());
        bombs.add(bomb);
    }

    /**
     * Update tất cả bombs mỗi tick.
     * @param monsters danh sách monster đang sống (dùng splash).
     * @param screenW / screenH kích thước màn hình để cull bom ra ngoài.
     */
    public void update(ArrayList<Monster> monsters, int screenW, int screenH) {
        Iterator<Bomb> it = bombs.iterator();
        while (it.hasNext()) {
            Bomb b = it.next();
            if (!b.update(monsters, screenW, screenH)) {
                it.remove();
            }
        }
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public ArrayList<Bomb> getBombs() { return bombs; }
}