package entity.tower;

import asset.TowerAsset;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import utils.Constants;
import static utils.Constants.Tiles.TILE_SIZE;

public class Tower {

    public static final int IDLE      = 0;
    public static final int PREATTACK = 1;
    public static final int ATTACK    = 2;

    public static final int SIDE = 0;
    public static final int UP   = 1;
    public static final int DOWN = 2;

    // Idle frame theo level
    public static final int[] IDLE_FRAME_COUNTS = {1, 4, 4, 6, 6, 6, 6};

    private int x, y, id, towerType;
    private int dmg;
    private float range, cooldown;
    private int cdTick;
    private Rectangle bounds;
    private int cost;
    private int value;


    // ===== Archer animation =====
    private int animState = IDLE;
    private int animIndex = 0;
    private int animTick  = 0;
    private int animSpeed = 14;
    private int direction = SIDE;

    // ===== Level =====
    private int towerLevel = 0;

    // ===== Facing =====
    private boolean facingLeft = false;

    // ===== Tower idle animation =====
    private int towerAnimFrame = 0;
    private int towerAnimTick  = 0;
    private static final int TOWER_ANIM_SPEED = 18; 

    private static final int ATTACK_HOLD = 4;
    private int attackHoldTick = 0;

    // ===== Upgrade =====
    private static final int UPGRADE_TOTAL_TICKS = 60;
    private boolean upgrading           = false;
    private int     upgradeTick         = 0;
    private int     pendingLevel        = -1;
    private int     flashAlpha          = 0;
    private boolean justStartedUpgrade  = false;
    private boolean justFinishedUpgrade = false;

    // ===== Select =====
    private boolean selected = false;

    // Flag báo hiệu cần spawn đạn khi PREATTACK → ATTACK
    private boolean shouldSpawnProjectile = false;

    public Tower(int x, int y, int id, int towerType, int cost) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.towerType = towerType;
        this.cost = cost;
        value = cost;

        initBounds();
        setDefaultStats();
    }

    private void initBounds(){
        this.bounds = new Rectangle(x, y, TILE_SIZE, TILE_SIZE);
    }

    public void update() {
        cdTick++;
        updateTowerAnim();
        updateUpgrade();

        if (!upgrading) {
            updateAnimation();
        }
    }

    // ===== Idle animation =====
    private void updateTowerAnim() {
        if (upgrading) return;

        int maxFrames = IDLE_FRAME_COUNTS[towerLevel];

        if (maxFrames <= 1) {
            towerAnimFrame = 0;
            return;
        }

        towerAnimTick++;
        if (towerAnimTick >= TOWER_ANIM_SPEED) {
            towerAnimTick = 0;
            towerAnimFrame = (towerAnimFrame + 1) % maxFrames;
        }
    }

    // ===== Upgrade =====
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
            upgrading = false;
            upgradeTick = 0;
            towerAnimFrame = 0;
            towerAnimTick = 0;
            flashAlpha = 0;
            justFinishedUpgrade = true;

            if (pendingLevel != -1) {
                towerLevel = pendingLevel;
                pendingLevel = -1;
            }
        }
    }

    // ===== Archer animation =====
    public void updateAnimation() {
        int maxFrames = getFrameAmount(this);
        if (maxFrames <= 0) return;

        animTick++;
        if (animTick < animSpeed) return;

        animTick = 0;

        // HOLD ATTACK FRAME
        if (animState == ATTACK && attackHoldTick < ATTACK_HOLD) {
            attackHoldTick++;
            return;
        }

        animIndex++;

        if (animIndex >= maxFrames) {
            animIndex = 0;

            switch (animState) {
                case PREATTACK:
                    animState = ATTACK;
                    attackHoldTick = 0;
                    shouldSpawnProjectile = true;
                    break;

                case ATTACK:
                    animState = IDLE;
                    break;

                case IDLE:
                    break;
            }
        }
    }

    /** Trả về true MỘT LẦN khi cần spawn arrow, rồi tự reset. */
    public boolean shouldSpawnProjectile() {
        boolean v = shouldSpawnProjectile;
        shouldSpawnProjectile = false;
        return v;
    }

    public void upgrade() {
        if (!canUpgrade() || upgrading) return;

        towerLevel = towerLevel + 1;
        upgrading = true;
        upgradeTick = 0;
        towerAnimFrame = 0;
        justStartedUpgrade = true;

        dmg += 3;
        range += 10;
        value += Constants.Towers.GetCostUpdate(towerType, towerLevel);

        cooldown = Math.max(10, cooldown - 2);
    }

    public void setAnimation(int state, int direction) {
        if (this.animState != state || this.direction != direction) {
            this.animState = state;
            this.direction = direction;
            this.animIndex = 0;
            this.animTick = 0;
        }
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    private void setDefaultStats() {
        dmg = Constants.Towers.GetStartDmg(towerType);
        range = Constants.Towers.GetDefaultRange(towerType);
        cooldown = Constants.Towers.GetDefaultCoolDown(towerType);
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public int getFrameAmount(Tower t) {
        BufferedImage[] frames = TowerAsset.archerAnimations[t.getDirection()][t.getAnimState()];
        return frames==null ? 0 : frames.length;
    }

    // ===== Arrow spawn (GIỮ NGUYÊN CHUẨN) =====
    public int getArrowSpawnX() {
        int cx = getCenterX();
        if (direction == UP || direction == DOWN)
            return cx;
        return facingLeft ? cx + 20 : cx + 10;
    }

    public int getArrowSpawnY() {
        int cy = getCenterY();

        switch (direction) {
            case UP:   return cy - 10;
            case DOWN: return cy + 10;
            default:   return cy;
        }
    }

    public boolean canAttack() {
        return cdTick >= cooldown;
    }

    // ===== GETTER =====
    public int getTowerAnimFrame() { return towerAnimFrame; }
    public boolean isUpgrading() { return upgrading; }
    public boolean isJustStartedUpgrade() { return justStartedUpgrade; }
    public boolean isJustFinishedUpgrade() { return justFinishedUpgrade; }
    public int getFlashAlpha() { return flashAlpha; }
    public float getUpgradeProgress() { return upgrading ? (float)upgradeTick / UPGRADE_TOTAL_TICKS : 0f; }
    public boolean canUpgrade() { return towerLevel < 6; }
    public boolean isCooldownOver() { return cdTick >= cooldown; }
    public void resetCooldown() { cdTick = 0; }
    public int getTowerLevel() { return towerLevel; }
    public int getDisplayLevel() { return towerLevel + 1; }
    public int getCenterX() { return x + 16; }
    public int getCenterY() { return y + 16; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getId() { return id; }
    public int getTowerType() { return towerType; }
    public int getDmg() { return dmg; }
    public float getRange() { return range; }
    public float getCooldown() { return cooldown; }
    public int getAnimState() { return animState; }
    public int getAnimIndex() { return animIndex; }
    public int getAnimTick() { return animTick; }
    public int getDirection() { return direction; }
    public boolean isFacingLeft() { return facingLeft; }
    public void setFacingLeft(boolean l) { this.facingLeft = l; }
    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }   
    public void setUpgrading(boolean b) { this.upgrading = b; }
    public int getCost() { return cost; }
    public int getSellValue(){ return (int) (value * 0.6);}
}