package entity;

public class Tower {

    private int x, y, id, TowerType,cdTick,dmg;
    private float range,cooldown;

    public Tower(int x, int y, int id, int TowerType){
        this.x = x;
        this.y = y;
        this.id = id;
        this.TowerType = TowerType;
        setDefaultDmg();
        setDefaultRange();
        setDefaultCoolDown();
    }

    public void update(){
        cdTick++;
    }

    public boolean isCooldownOver(){
        return cdTick >= cooldown;

    }

    public void  resetCooldown(){
        cdTick = 0;

    }

    private void setDefaultDmg(){
        dmg = helpz.Constants.Towers.GetStartDmg(TowerType);
    }

    private void setDefaultRange(){
        range = helpz.Constants.Towers.GetDefaultRange(TowerType);
    }

    private void setDefaultCoolDown(){
        cooldown = helpz.Constants.Towers.GetDefaultCoolDown(TowerType);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTowerType() {
        return TowerType;
    }

    public void setTowerType(int towerType) {
        TowerType = towerType;
    }

    public int  getDmg() {
        return dmg;
    }

    public float getRange() {
        return range;
    }

    public float getCooldown() {
        return cooldown;
    }



}
