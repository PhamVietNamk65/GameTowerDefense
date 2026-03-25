package entity;

import utils.Constants;

public class Tower {

    public static final int IDLE      = 0;
    public static final int PREATTACK = 1;
    public static final int ATTACK    = 2;

    public static final int SIDE = 0;
    public static final int UP   = 1;
    public static final int DOWN = 2;

    // Số frame idle đúng theo từng level (index 0..6 = lv1..7)
    // lv1=1 frame tĩnh, lv2-3=4 frames, lv4-7=6 frames
    public static final int[] IDLE_FRAME_COUNTS = {1, 4, 4, 6, 6, 6, 6};

    private int x, y, id, towerType, tileX, tileY;
    private int dmg;
    private float range, cooldown;
    private int cdTick;
    private boolean selected = false;

    // Archer animation
    private int animState = IDLE;
    private int animIndex = 0;
    private int animTick  = 0;
    private int animSpeed = 8;
    private int direction = SIDE;

    // Level tháp: 0..6 (lv1..7)
    private int towerLevel = 0;

    // Tower idle animation
    private int towerAnimFrame = 0;
    private int towerAnimTick  = 0;
    private static final int TOWER_ANIM_SPEED = 10;

    // Upgrade
    private static final int UPGRADE_TOTAL_TICKS = 60;
    private boolean upgrading           = false;
    private int     upgradeTick         = 0;
    private int     pendingLevel        = -1;
    private int     flashAlpha          = 0;
    private boolean justStartedUpgrade  = false;
    private boolean justFinishedUpgrade = false;

    public Tower(int x, int y, int id, int towerType, int tileX, int tileY) {
        this.x = x; this.y = y; this.id = id;
        this.towerType = towerType;
        this.tileX = tileX; this.tileY = tileY;
        setDefaultStats();
    }

    public void update() {
        cdTick++;
        updateTowerAnim();
        updateUpgrade();
    }

    private void updateTowerAnim() {
        if (upgrading) return;
        int maxFrames = IDLE_FRAME_COUNTS[towerLevel];
        if (maxFrames <= 1) { towerAnimFrame = 0; return; }
        towerAnimTick++;
        if (towerAnimTick >= TOWER_ANIM_SPEED) {
            towerAnimTick = 0;
            towerAnimFrame = (towerAnimFrame + 1) % maxFrames;
        }
    }

    private void updateUpgrade() {
        justStartedUpgrade  = false;
        justFinishedUpgrade = false;
        if (!upgrading) return;
        upgradeTick++;
        float half = UPGRADE_TOTAL_TICKS / 2f;
        if (upgradeTick <= half)
            flashAlpha = (int)(180f * (upgradeTick / half));
        else
            flashAlpha = (int)(180f * (1f - (upgradeTick - half) / half));

        if (upgradeTick >= UPGRADE_TOTAL_TICKS) {
            upgrading = false; upgradeTick = 0;
            towerAnimFrame = 0; towerAnimTick = 0; flashAlpha = 0;
            justFinishedUpgrade = true;
            if (pendingLevel != -1) { towerLevel = pendingLevel; pendingLevel = -1; }
        }
    }

    public void upgrade() {
        if (!canUpgrade() || upgrading) return;
        pendingLevel = towerLevel + 1; upgrading = true;
        upgradeTick = 0; towerAnimFrame = 0; justStartedUpgrade = true;
        dmg += 3; range += 10; cooldown = Math.max(10, cooldown - 2);
    }

    public void setAnimation(int state, int direction) {
        if (this.animState != state || this.direction != direction) {
            this.animState = state; this.direction = direction;
            this.animIndex = 0; this.animTick = 0;
        }
    }

    public void updateAnimation(int maxFrames) {
        if (maxFrames <= 0) return;
        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0; animIndex++;
            if (animIndex >= maxFrames) {
                animIndex = 0;
                if (animState == PREATTACK) animState = ATTACK;
                else if (animState == ATTACK) animState = IDLE;
            }
        }
    }

    private void setDefaultStats() {
        dmg = Constants.Towers.GetStartDmg(towerType);
        range = Constants.Towers.GetDefaultRange(towerType);
        cooldown = Constants.Towers.GetDefaultCoolDown(towerType);
    }

    public int     getTowerAnimFrame()     { return towerAnimFrame; }
    public boolean isUpgrading()           { return upgrading; }
    public boolean isJustStartedUpgrade()  { return justStartedUpgrade; }
    public boolean isJustFinishedUpgrade() { return justFinishedUpgrade; }
    public int     getFlashAlpha()         { return flashAlpha; }
    public float   getUpgradeProgress()    { return upgrading ? (float)upgradeTick / UPGRADE_TOTAL_TICKS : 0f; }
    public boolean canUpgrade()            { return towerLevel < 6; }
    public boolean isCooldownOver()        { return cdTick >= cooldown; }
    public void    resetCooldown()         { cdTick = 0; }
    public int     getTowerLevel()         { return towerLevel; }
    public int     getPendingLevel()       { return pendingLevel; }
    public int     getDisplayLevel()       { return towerLevel + 1; }
    public int     getSellValue()          { return 10 + towerLevel * 10; }
    public int     getCenterX()            { return x + 16; }
    public int     getCenterY()            { return y + 16; }
    public int     getX()                  { return x; }
    public int     getY()                  { return y; }
    public int     getId()                 { return id; }
    public int     getTowerType()          { return towerType; }
    public int     getTileX()              { return tileX; }
    public int     getTileY()              { return tileY; }
    public int     getDmg()                { return dmg; }
    public float   getRange()              { return range; }
    public float   getCooldown()           { return cooldown; }
    public int     getAnimState()          { return animState; }
    public int     getAnimIndex()          { return animIndex; }
    public int     getDirection()          { return direction; }
    public boolean isSelected()            { return selected; }
    public void    setSelected(boolean s)  { this.selected = s; }
}