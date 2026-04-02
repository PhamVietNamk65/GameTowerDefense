package Manager;

import entity.ArcherTower;
import entity.CannonTower;
import entity.EnemyState;
import entity.Tower;
import entity.TowerSlot;
import java.util.ArrayList;
import utils.Constants;

public class TowerManager {

    private final ArrayList<Tower> towers = new ArrayList<>();

    private int          towerId      = 0;
    private Tower        selectedTower;
    private ArrowManager arrowManager = new ArrowManager();

    public void removeTower(Tower t) {
        towers.remove(t);
        if (t == selectedTower) selectedTower = null;
    }

    public Tower getTowerAt(int x, int y) {
        for (Tower t : towers) {
            if (t.getBounds().contains(x, y)) return t;
        }
        return null;
    }

    public void buildTower(TowerSlot slot, int type) {
        int x = slot.getX() * Constants.Tiles.TILE_SIZE;
        int y = slot.getY() * Constants.Tiles.TILE_SIZE;
        if (type == 1) {
            Tower t = new ArcherTower(x, y, towerId++);
            t.setUpgrading(true);
            towers.add(t);
        } else if (type == 2) {
            towers.add(new CannonTower(x, y, towerId++));
        }
    }

    public void handleClick(int mouseX, int mouseY) {
        selectedTower = null;
        for (Tower t : towers) {
            if (t.getBounds().contains(mouseX, mouseY)) {
                selectedTower = t;
                break;
            }
        }
    }

    public Tower getSelectedTower() { return selectedTower; }
    public ArrayList<Tower> getTowers() { return towers; }

    public void update(ArrayList<entity.Monster> monsters) {

        for (Tower t : towers) {
            t.update();

            if (t instanceof ArcherTower archer) {
                entity.Monster target = findTarget(archer, monsters);

                if (target != null) {
                    float dx = (target.getX() + 16) - archer.getCenterX();
                    float dy = (target.getY() + 16) - archer.getCenterY();

                    int dir;
                    if (Math.abs(dx) >= Math.abs(dy)) {
                        dir = Tower.SIDE;
                    } else {
                        dir = dy < 0 ? Tower.UP : Tower.DOWN;
                    }

                    // FIX archer ngược hướng:
                    // Sprite S_Attack nhìn sang PHẢI (→) mặc định
                    // TowerRenderer: facingLeft=true → vẽ flip → archer nhìn TRÁI
                    // Đã thử dx<0 bị ngược → đổi thành dx>0
                    archer.setFacingLeft(dx > 0);

                    if (archer.getAnimState() == Tower.IDLE && archer.canAttack()) {
                        archer.setAnimation(Tower.PREATTACK, dir);
                        archer.resetCooldown();
                    }

                    if (archer.shouldSpawnProjectile()) {
                        arrowManager.spawnArrow(archer, target);
                    }

                } else {
                    if (archer.getAnimState() == Tower.PREATTACK) {
                        archer.setAnimation(Tower.IDLE, archer.getDirection());
                    }
                }
            }
        }

        arrowManager.update(1280, 768);
    }

    private entity.Monster findTarget(Tower t, ArrayList<entity.Monster> monsters) {
        entity.Monster nearest = null;
        float minDist = Float.MAX_VALUE;
        for (entity.Monster m : monsters) {
            if (m.getState() == EnemyState.DEATH) continue;
            float dx = m.getX() - t.getX();
            float dy = m.getY() - t.getY();
            float dist = dx * dx + dy * dy;
            if (dist < minDist && dist <= t.getRange() * t.getRange()) {
                minDist = dist;
                nearest = m;
            }
        }
        return nearest;
    }

    public ArrowManager getArrowManager() { return arrowManager; }
}