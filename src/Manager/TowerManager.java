package Manager;

import entity.TowerSlot;
import entity.monster.EnemyState;
import entity.monster.Monster;
import entity.tower.ArcherTower;
import entity.tower.CanonTower;
import entity.tower.FlameTower;
import entity.tower.FrostTower;
import entity.tower.LightningTower;
import entity.tower.SniperTower;
import entity.tower.Tower;
import java.util.ArrayList;
import levels.LevelState;
import utils.Constants;

public class TowerManager {

    private final ArrayList<Tower> towers = new ArrayList<>();

    private int   towerId = 0;
    private Tower selectedTower;

    private ArrowManager     arrowManager;
    private BombManager      bombManager;
    private FlameManager     flameManager;
    private FrostManager     frostManager;
    private LightningManager lightningManager;
    private BulletManager    bulletManager;      // ← NEW (Sniper)
    private LevelState       levelState;

    public TowerManager(LevelState levelState, ArrowManager arrowManager) {
        this.levelState       = levelState;
        this.arrowManager     = arrowManager;
        this.bombManager      = new BombManager();
        this.flameManager     = new FlameManager();
        this.frostManager     = new FrostManager();
        this.lightningManager = new LightningManager();
        this.bulletManager    = new BulletManager();    // ← NEW
    }

    // ── Build ─────────────────────────────────────────────────────────────────
    public boolean buildTower(TowerSlot slot, int type) {
        int x = slot.getX() * Constants.Tiles.TILE_SIZE;
        int y = slot.getY() * Constants.Tiles.TILE_SIZE;

        Tower t = null;
        switch (type) {
            case Constants.Towers.ARCHER    -> t = new ArcherTower(x, y, towerId++);
            case Constants.Towers.CANNON    -> t = new CanonTower(x, y, towerId++);
            case Constants.Towers.FLAME     -> t = new FlameTower(x, y, towerId++);
            case Constants.Towers.FROST     -> t = new FrostTower(x, y, towerId++);
            case Constants.Towers.LIGHTNING -> t = new LightningTower(x, y, towerId++);
            case Constants.Towers.SNIPER    -> t = new SniperTower(x, y, towerId++);  // ← NEW
        }

        if (t != null && levelState.spendGold(t.getCost())) {
            t.setUpgrading(true);
            towers.add(t);
            return true;
        }
        return false;
    }

    // ── Remove / Select ───────────────────────────────────────────────────────
    public void removeTower(Tower t) {
        towers.remove(t);
        if (t == selectedTower) selectedTower = null;
    }

    public Tower getTowerAt(int x, int y) {
        for (Tower t : towers)
            if (t.getBounds().contains(x, y)) return t;
        return null;
    }

    public void handleClick(int mouseX, int mouseY) {
        selectedTower = null;
        for (Tower t : towers)
            if (t.getBounds().contains(mouseX, mouseY)) { selectedTower = t; break; }
    }

    // ── Update ────────────────────────────────────────────────────────────────
    public void update(ArrayList<Monster> monsters) {

        for (Tower t : towers) {
            t.update();

            // ── Archer ──────────────────────────────────────────────────────
            if (t instanceof ArcherTower archer) {
                Monster target = findTarget(archer, monsters);
                if (target != null) {
                    float dx = (target.getX() + 16) - archer.getCenterX();
                    float dy = (target.getY() + 16) - archer.getCenterY();
                    int dir = Math.abs(dx) >= Math.abs(dy) ? Tower.SIDE
                              : dy < 0 ? Tower.UP : Tower.DOWN;
                    archer.setFacingLeft(dx > 0);

                    if (archer.getAnimState() == Tower.IDLE && archer.canAttack()) {
                        archer.setAnimation(Tower.PREATTACK, dir);
                        archer.resetCooldown();
                    }
                    if (archer.shouldSpawnProjectile()) {
                        arrowManager.spawnArrow(archer, target);
                    }
                } else {
                    if (archer.getAnimState() == Tower.PREATTACK)
                        archer.setAnimation(Tower.IDLE, archer.getDirection());
                }
            }

            // ── Canon ───────────────────────────────────────────────────────
            if (t instanceof CanonTower canon) {
                Monster target = findTarget(canon, monsters);
                if (target != null) {
                    canon.aimAt(target.getX() + 16, target.getY() + 16);
                    if (canon.canAttack() && !canon.isShooting()) {
                        canon.triggerShoot();
                        canon.resetCooldown();
                    }
                }
                if (canon.shouldSpawnBomb()) {
                    Monster bombTarget = findTarget(canon, monsters);
                    if (bombTarget != null) bombManager.spawnBomb(canon, bombTarget);
                }
            }

            // ── Flame ────────────────────────────────────────────────────────
            if (t instanceof FlameTower flame) {
                if (!flame.isUpgrading()) {
                    Monster target = findTarget(flame, monsters);
                    if (target != null && flame.canAttack()) {
                        flame.triggerFire();
                        flameManager.spawnFlame(flame, target);
                    }
                }
            }

            // ── Frost ────────────────────────────────────────────────────────
            if (t instanceof FrostTower frost) {
                if (!frost.isUpgrading()) {
                    Monster target = findTarget(frost, monsters);
                    if (target != null && frost.canAttack()) {
                        frost.triggerFrost();
                        frostManager.spawnFrost(frost, target);
                    }
                }
            }

            // ── Lightning ────────────────────────────────────────────────────
            if (t instanceof LightningTower lightning) {
                if (!lightning.isUpgrading()) {
                    Monster target = findTarget(lightning, monsters);
                    if (target != null && lightning.canAttack()) {
                        lightning.triggerLightning();
                        lightningManager.spawnLightning(lightning, target, monsters);
                    }
                }
            }

            // ── Sniper (bee-only) ────────────────────────────────────────────
            if (t instanceof SniperTower sniper) {
                if (!sniper.isUpgrading()) {
                    Monster target = findBeeTarget(sniper, monsters);
                    if (target != null) {
                        // Update facing direction
                        float dx = (target.getX() + 16) - sniper.getCenterX();
                        sniper.setFacingLeftSniper(dx < 0);

                        if (sniper.canAttack()) {
                            sniper.triggerShoot();
                            bulletManager.spawnBullet(sniper, target);
                        }
                    }
                }
            }
        }

        arrowManager.update(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        bombManager.update(monsters, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        flameManager.update(monsters, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        frostManager.update(monsters, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        lightningManager.update();
        bulletManager.update();   // ← NEW
    }

    // ── Find nearest target in range (all monster types) ─────────────────────
    private Monster findTarget(Tower t, ArrayList<Monster> monsters) {
        Monster nearest = null;
        float   minDist = Float.MAX_VALUE;
        for (Monster m : monsters) {
            if (m.getState() == EnemyState.DYING) continue;
            float dx   = m.getX() - t.getX();
            float dy   = m.getY() - t.getY();
            float dist = dx * dx + dy * dy;
            if (dist < minDist && dist <= t.getRange() * t.getRange()) {
                minDist = dist;
                nearest = m;
            }
        }
        return nearest;
    }

    // ── Find nearest BEE in range (Sniper only) ───────────────────────────────
    private Monster findBeeTarget(Tower t, ArrayList<Monster> monsters) {
        Monster nearest = null;
        float   minDist = Float.MAX_VALUE;
        for (Monster m : monsters) {
            if (m.getState() == EnemyState.DYING) continue;
            if (m.getEnemyType() != Constants.Monsters.BEE) continue;  // bees only
            float dx   = m.getX() - t.getX();
            float dy   = m.getY() - t.getY();
            float dist = dx * dx + dy * dy;
            if (dist < minDist && dist <= t.getRange() * t.getRange()) {
                minDist = dist;
                nearest = m;
            }
        }
        return nearest;
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public Tower             getSelectedTower()   { return selectedTower;   }
    public ArrayList<Tower>  getTowers()          { return towers;          }
    public ArrowManager      getArrowManager()    { return arrowManager;    }
    public BombManager       getBombManager()     { return bombManager;     }
    public FlameManager      getFlameManager()    { return flameManager;    }
    public FrostManager      getFrostManager()    { return frostManager;    }
    public LightningManager  getLightningManager(){ return lightningManager;}
    public BulletManager     getBulletManager()   { return bulletManager;   }  // ← NEW
}