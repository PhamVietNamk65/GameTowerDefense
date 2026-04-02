package system;

import entity.Tower;

public interface TowerActionListener {
    void onUpgrade(Tower t);
    void onSell(Tower t);

} 