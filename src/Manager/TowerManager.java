package Manager;

import entity.Tower;
import java.util.ArrayList;

public class TowerManager {

    private final ArrayList<Tower>    towers    = new ArrayList<>();  
    
    private int     towerId      = 0;
    private Tower   selectedTower;

    public void addTower(int x, int y, int type, int tileX, int tileY) {
        towers.add(new Tower(x, y, towerId++, type, tileX, tileY));
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
        for (Tower t : towers)
            if (t.isSelected()) return t;
        return null;
    }

    public ArrayList<Tower> getTowers() { return towers; }

}