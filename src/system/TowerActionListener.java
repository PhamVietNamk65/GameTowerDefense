package system;

import entity.tower.Tower;

public interface TowerActionListener {
    void onUpgrade(Tower t);
    void onSell(Tower t);

} 