package Manager;

import entity.TowerSlot;
import entity.monster.EnemyState;
import entity.tower.ArcherTower;
import entity.tower.CannonTower;
import entity.tower.Tower;
import levels.LevelState;
import utils.Constants;

import java.util.ArrayList;

public class TowerManager {

    private final ArrayList<Tower>    towers    = new ArrayList<>();  
    
    private int     towerId      = 0;
    private Tower   selectedTower;
    private ArrowManager arrowManager;
    private LevelState levelState;

    public TowerManager(LevelState levelState, ArrowManager arrowManager){
        this.levelState = levelState;
        this.arrowManager = arrowManager;
    }

    public void removeTower(Tower t) {
        towers.remove(t);
        if (t == selectedTower) selectedTower = null;
    }

    public Tower getTowerAt(int x, int y) {
        for (Tower t : towers) {
            if (t.getBounds().contains(x, y)) {
                return t;
            }
        }
        return null;
    }

    public boolean buildTower(TowerSlot slot, int type) {
        int x = slot.getX() * Constants.Tiles.TILE_SIZE ;
        int y = slot.getY() * Constants.Tiles.TILE_SIZE ;
        if (type == 1) {
            Tower t = new ArcherTower(x, y, towerId++);
            int cost = t.getCost();
            if( levelState.spendGold(cost) ){
                t.setUpgrading(true);
                towers.add(t);
                return true;
            }
        } else if (type == 2) {
            Tower t = new CannonTower(x, y, towerId++);
            int cost = t.getCost();
            if( levelState.spendGold(cost) ){
                t.setUpgrading(true);
                towers.add(t);
                return true;
            }
        }
        return false;
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

    public Tower getSelectedTower() {
        return selectedTower;
    }

    public ArrayList<Tower> getTowers() { return towers; }

    public void update(ArrayList<entity.monster.Monster> monsters) {

        for (Tower t : towers) {
            t.update();
            // CHỈ Archer mới bắn
            if (t instanceof ArcherTower archer) {
                entity.monster.Monster target = findTarget(archer, monsters);
                if (target != null && archer.canAttack()) {
                    arrowManager.spawnArrow(archer, target);
                    archer.resetCooldown();
                }
            }
        }

        arrowManager.update(1280, 768); // nên thay bằng screenW/H sau
    }

    private entity.monster.Monster findTarget(Tower t, ArrayList<entity.monster.Monster> monsters) {
        entity.monster.Monster nearest = null;
        float minDist = Float.MAX_VALUE;
        for (entity.monster.Monster m : monsters) {
            if (m.getState() == EnemyState.DYING) continue;
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
    
    public ArrowManager getArrowManager() {
        return arrowManager;
    }
}