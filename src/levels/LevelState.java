package levels;

public class LevelState {
    int gold;
    int lives;

    public LevelState(LevelData levelData) {
        this.gold = levelData.getStartGold();
        this.lives = levelData.getStartLives();
    }

    public int getGold() {
        return gold;
    }
    
    public int getLives() {
        return lives;
    }

    public void addGold(int gold) {
        this.gold += gold;
    }

    public boolean spendGold(int gold) {
        if(this.gold < gold) {
            return false;
        }
        else{
            this.gold -= gold;
            return true;
        }
    }

    public void loseLife(int live) {
        this.lives -= live;
        if (this.lives <= 0) {
            // Xử lý Game Over ở đây
        }
    }

}
