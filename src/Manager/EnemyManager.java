package Manager;

import asset.MonsterAsset;
import entity.monster.EnemyState;
import entity.monster.Monster;
import entity.trap.Wall;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import levels.Level;
import levels.LevelState;
import system.EnemyMovement;
import system.EnemySpawner;

public class EnemyManager {

    private ArrayList<Monster> monsters = new ArrayList<>();

    private EnemySpawner enemySpawner;
    private LevelState levelState;
    private Level level;

    private int aniSpeed = 20;

    public EnemyManager(Level level, LevelState levelState) {
        this.enemySpawner = new EnemySpawner();
        this.levelState   = levelState;
        this.level        = level;
    }

    public void update() {
        Iterator<Monster> it = monsters.iterator();

        while (it.hasNext()) {
            Monster m = it.next();

            // ── Đã tới đích → trừ máu người chơi, xóa quái ─────────────────
            if (m.hasReachedEnd()) {
                levelState.loseLife(1);
                it.remove();
                continue;
            }

            // ── Cập nhật status effects (burn dmg, slow timer, stun timer) ──
            // Phải gọi TRƯỚC move để stun/slow áp dụng ngay tick này
            m.update();

            // Nếu quái vừa chết do burn damage → xử lý death ngay
            if (m.getState() == EnemyState.DYING) {
                handleDying(m, it);
                continue;
            }

            // ── Cập nhật walk animation ──────────────────────────────────────
            // updateAnim() tự dừng khi stun (xem Monster.java)
            m.updateAnim();

            // ── Xử lý theo state ─────────────────────────────────────────────
            if (m.getState() == EnemyState.DYING) {
                handleDying(m, it);

            } else if (m.getState() == EnemyState.ATTACK) {
                Wall w = m.getTargetWall();
                if (w == null || w.isDestroyed()) {
                    m.setState(EnemyState.WALK);
                    m.setTargetWall(null);
                } else {
                    m.attackWall();
                }

            } else {
                // WALK — move() tự dừng khi stun, tự chậm khi slow
                m.getMovement().move(m, level);
            }
        }
    }

    /** Chạy death animation, khi xong thì thưởng vàng và xóa quái */
    private void handleDying(Monster m, Iterator<Monster> it) {
        BufferedImage[] df = MonsterAsset.getFrames(
            m.getEnemyType(),
            EnemyState.DYING,
            m.getDirection()
        );
        int totalFrames = (df != null) ? df.length : 1;
        m.tickDeath(totalFrames, aniSpeed);

        if (m.isDeathDone()) {
            levelState.addGold(m.getReward());
            it.remove();
        }
    }

    public void spawnMonster(int type, int pathIndex) {
        Point[] chosenPath = level.getPaths().get(pathIndex);

        Monster m = enemySpawner.spawn(type);
        if (m != null) {
            EnemyMovement movement = new EnemyMovement(chosenPath);
            m.setMovement(movement);
            m.createOffset();

            Point start = movement.getStartPoint();
            m.setPos(
                start.x + m.getxOffset(),
                start.y + m.getyOffset()
            );
            monsters.add(m);
        }
    }

    public ArrayList<Monster> getMonsters() { return monsters; }
}