package entity.tower;

import utils.Constants;

/**
 * Frost Wizard Tower – làm chậm quái 40% trong 2.5 giây.
 * Chỉ có 3 level (wizLevel 1 → 3).
 */
public class FrostTower extends Tower {

    private static final int   COST          = 110;
    private static final int   DIRECT_DMG    = 10;
    private static final int   SLOW_DURATION = 150;
    private static final int   SPLASH_RADIUS = 50;
    private static final float RANGE         = 130f;
    private static final float COOLDOWN      = 70f;

    private int wizLevel     = 1;
    private int prevWizLevel = 0;

    private boolean frostFlag = false;

    public FrostTower(int x, int y, int id) {
        super(x, y, id, Constants.Towers.WIZARD, COST);
        prevWizLevel = 0;
    }

    @Override
    public void update() {
        super.update();
        frostFlag = false;
    }

    public void triggerFrost() { frostFlag = true; resetCooldown(); }
    public boolean shouldFrost() { return frostFlag; }

    @Override public boolean canUpgrade() { return wizLevel < 3; }
    @Override public boolean isMaxLevel() { return wizLevel >= 3; }

    @Override
    public void upgrade() {
        if (!canUpgrade() || isUpgrading()) return;
        prevWizLevel = wizLevel;   // lưu level cũ cho label
        super.upgrade();            // bật isUpgrading=true TRƯỚC (canUpgrade() vẫn còn true)
        wizLevel++;                 // tăng SAU để canUpgrade() không bị false sớm
    }

    @Override public int   getDmg()      { return DIRECT_DMG; }
    @Override public float getRange()    { return RANGE; }
    @Override public float getCooldown() { return COOLDOWN; }
    @Override public int   getCost()     { return COST; }

    public int getSlowDuration() { return SLOW_DURATION; }
    public int getSplashRadius() { return SPLASH_RADIUS; }
    public int getLevel()        { return wizLevel; }
    public int getPrevLevel()    { return prevWizLevel; }
}