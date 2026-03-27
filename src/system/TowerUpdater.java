package system;

import java.util.ArrayList;

import asset.TowerAsset;
import entity.Tower;
import utils.Constants;

public class TowerUpdater{
    
    public void update(ArrayList<Tower> towers) {
        for (Tower t : towers) {
            t.update();
            if (t.getTowerType() == Constants.Towers.ARCHER) {
                autoUpdateArcher(t);
                t.updateAnimation(t.getFrameAmount(t));
            }
            }
    }
    //─────────────────
    private void autoUpdateArcher(Tower t) {
        if (t.isUpgrading()) return;
        if (t.getAnimState() != Tower.IDLE) return;
        int dir = Tower.SIDE;
        int mode = (t.getId()/2)%3;
        if (mode==1) 
            dir=Tower.UP; 
        else 
            if (mode==2) 
                dir=Tower.DOWN;
        if (t.isCooldownOver()) { 
            t.setAnimation(Tower.PREATTACK,dir); 
            t.resetCooldown();
        }
        else t.setAnimation(Tower.IDLE,dir);
    }

}