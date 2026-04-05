package levels;

public class LevelNode {
    private int id;
    private boolean unlocked;
    private int stars; // 0-3

    public LevelNode(int id, boolean unlocked) {
        this.id = id;
        this.unlocked = unlocked;
        this.stars = 0;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void unlock() {
        this.unlocked = true;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        if (stars > this.stars && stars <= 3) {
            this.stars = stars;
        }
    }
}