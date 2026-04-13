package utils;

public class Constants {

    public static final int MAX_SCREEN_COL = 20;
    public static final int MAX_CREEN_ROW = 12;
    public static final int SCREEN_WIDTH = Tiles.TILE_SIZE * MAX_SCREEN_COL;
    public static final int SCREEN_HEIGHT = Tiles.TILE_SIZE * MAX_CREEN_ROW;

    public static final int TOTAL_LEVELS = 9;

    public static class Projectiles {
        public static final int ARROWS = 0;
        public static final int CHAINS = 1;
        public static final int BOMB   = 2;

        public static float GetSpeed(int type) {
            switch (type) {
                case ARROWS: return 8f;
                case BOMB:   return 4f;
                case CHAINS: return 6f;
            }
            return 0f;
        }
    }

    public static class Towers {
        public static final int CANNON    = 0;
        public static final int ARCHER    = 1;
        public static final int WIZARD    = 2;
        public static final int FLAME     = 3;
        public static final int FROST     = 4;
        public static final int LIGHTNING = 5;
        public static final int SNIPER    = 6;   // ← NEW

        public static final int ARCHER_W = 48;
        public static final int ARCHER_H = 48;

        /** Max upgradeable level for each tower type (0-based, so 2 = 3 levels total) */
        public static int GetMaxLevel(int towerType) {
            if (towerType == SNIPER) return 2;   // levels 0,1,2 → Lv1,Lv2,Lv3
            return 6;                             // all other towers: 7 levels
        }

        public static String GetName(int towerType) {
            switch (towerType) {
                case CANNON:    return "Cannon";
                case ARCHER:    return "Archer";
                case WIZARD:    return "Wizard";
                case FLAME:     return "Flame";
                case FROST:     return "Frost";
                case LIGHTNING: return "Lightning";
                case SNIPER:    return "Sniper";
            }
            return "";
        }

        public static int GetStartDmg(int towerType) {
            switch (towerType) {
                case CANNON:  return 30;
                case ARCHER:  return 15;
                case WIZARD:  return 0;
                case SNIPER:  return 80;
            }
            return 0;
        }

        public static float GetDefaultRange(int towerType) {
            switch (towerType) {
                case CANNON:  return 100;
                case ARCHER:  return 100;
                case WIZARD:  return 100;
                case SNIPER:  return 450;
            }
            return 0;
        }

        public static float GetDefaultCoolDown(int towerType) {
            switch (towerType) {
                case CANNON:  return 120;
                case ARCHER:  return 25;
                case WIZARD:  return 40;
                case SNIPER:  return 90;
            }
            return 0;
        }

        /** Base build cost */
        public static int GetCost(int towerType) {
            switch (towerType) {
                case CANNON:    return 120;
                case ARCHER:    return 65;
                case FLAME:     return 100;
                case FROST:     return 110;
                case LIGHTNING: return 130;
                case SNIPER:    return 140;
            }
            return 0;
        }

        public static int GetCostUpdate(int towerType, int towerLevel) {
            switch (towerType) {
                case CANNON:
                    switch (towerLevel) {
                        case 1: return 150;
                        case 2: return 180;
                        case 3: return 220;
                        default: return 0;
                    }
                case ARCHER:
                    switch (towerLevel) {
                        case 1: return 75;
                        case 2: return 80;
                        case 3: return 95;
                        case 4: return 110;
                        case 5: return 125;
                        case 6: return 150;
                        case 7: return 200;
                        default: return 0;
                    }
                case SNIPER:
                    // Only 3 levels (0→1→2); level 3+ = max
                    switch (towerLevel) {
                        case 1: return 110;   // Lv1 → Lv2
                        case 2: return 150;   // Lv2 → Lv3
                        default: return 0;    // Lv3 = max, no upgrade
                    }
                case WIZARD:
                    return 40;
            }
            return 0;
        }
    }

    public static class Direction {
        public static final int UP    = 0;
        public static final int DOWN  = 1;
        public static final int LEFT  = 3;
        public static final int RIGHT = 4;
    }

    public static class Monsters {
        public static final int SLIME = 0;
        public static final int ORC = 1;
        public static final int WOLF = 2;
        public static final int BEE = 3;
        public static final int RAT = 4;
        public static final int RIDER = 5;
        public static final int MAGE = 6;

        public static final int ENEMY_SIZE = 32 * 2;
	    public static final int HP_BAR_WIDTH = 24;
	    public static final int HP_BAR_HEIGHT = 4;
	    public static final int HP_BAR_Y_OFFSET = 8;

        public static int getAttackSpeed(int monsterType){
            switch (monsterType) {
                case ORC:
                    return 60;
                case SLIME:
                    return 100;
                case BEE:
                    return 0;
                case WOLF:
                    return 45;
                case RAT:
                    return 50;
                case RIDER:
                    return 40;
                case MAGE:
                    return 70;
            }
            return 0;
        }
        
        public static int getDame(int monsterType){
            switch (monsterType) {
                case ORC:
                    return 20;
                case SLIME:
                    return 1;
                case BEE:
                    return 0;
                case WOLF:
                    return 15;
                case RAT:
                    return 18;
                case RIDER:
                    return 25;
                case MAGE:
                    return 30;
            }
            return 0;
        }
        
        public static float GetSpeed(int monsterType){
            switch (monsterType) {
                case ORC:
                    return 1f;
                case SLIME:
                    return 0.8f;
                case BEE:
                    return 1.5f;
                case WOLF:
                    return 2f;
                case RAT:
                    return 1.2f;
                case RIDER:
                    return 2.5f;
            }
            return 0;
        }

        public static int GetStartHealth(int monsterType) {
            switch (monsterType) {
                case ORC:
                    return 250;
                case SLIME:
                    return 100;
                case BEE:
                    return 70;
                case WOLF:
                    return 150;
                case RAT:
                    return 200;
                case RIDER:
                    return 350;
            }
            return 0;
        }

        public static int GetReward(int monsterType) {
            switch (monsterType) {
                case ORC:
                    return 60;
                case SLIME:
                    return 20;
                case BEE:
                    return 10;
                case WOLF:
                    return 25;
                case RAT:
                    return 50;
                case RIDER:
                    return 120;
            }
            return 0;
        }
    }

    public static class Tiles {
        public static final int ORIGIANLTILESIZE = 32;
        public static final int SCALE     = 2;
        public static final int TILE_SIZE = ORIGIANLTILESIZE * SCALE;

        public static final int GRASS           = 0;
        public static final int ROAD_HORIZONTAL = 1;
        public static final int ROAD_DOWN       = 2;
        public static final int ROAD_LEFT_UP    = 3;
        public static final int ROAD_LEFT_DOWN  = 4;
        public static final int ROAD_RIGHT_UP   = 5;
        public static final int ROAD_RIGHT_DOWN = 6;
    }


    public static class Projectile {
        public static final float SPEED      = 6f;
        public static final float HIT_RADIUS = 12f;
        public static final float TURN_SPEED = 0.18f;
    }

    public static class Arrow {
        public static final int ARROW_DRAW_SIZE  = 10;
        public static final int ARROW_ANIM_SPEED = 4;
    }

    public static class Walls{
        public static int getStartHP(int level){
            switch (level) {
                case 0:
                    return 100;
                case 1:
                    return 150;
                case 2:
                    return 200;
                case 3:
                    return 250;
                case 4:
                    return 400;
                default:
                    break;
            }
            return 0;
        }
    }
}