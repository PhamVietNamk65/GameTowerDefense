package utils;


//Định nghĩa các hằng số dùng chung (ID của loại quái, loại trụ, loại gạch nền).
public class Constants {

    public static final int MAX_SCREEN_COL = 20;
    public static final int MAX_CREEN_ROW = 12;
    public static final int SCREEN_WIDTH = Tiles.TILE_SIZE * MAX_SCREEN_COL;
    public static final int SCREEN_HEIGHT = Tiles.TILE_SIZE * MAX_CREEN_ROW;

    public static final int TOTAL_LEVELS = 9;
    //Dan ban
    public static class Projectiles{
        public static final int ARROWS = 0;
        public static final int CHAINS = 1;
        public static final int BOMB = 2;

        public static float GetSpeed(int type){
            switch (type) {
                case ARROWS:
                    return 8f;
                case BOMB:
                    return 4f;
                case CHAINS:
                    return 6f;
            }
            return 0f;
        }
    }

    public static class Towers{
        public static final int CANNON = 0;
        public static final int ARCHER = 1;
        public static final int WIZARD = 2;

        public static final int ARCHER_W  = 48;
        public static final int ARCHER_H  = 48;

        public static String GetName(int towerType){
            switch (towerType) {
                case CANNON:
                    return "Cannon";
                case ARCHER:
                    return "Archer";
                case WIZARD:
                    return "Wizard";
            }
            return "";
        }

        public static int GetStartDmg(int towerType){
            switch (towerType) {
                case CANNON:
                    return 15;
                case ARCHER:
                    return 15;
                case WIZARD:
                    return 0;
            }
            return 0;
        }

        public static float GetDefaultRange(int towerType){
            switch (towerType) {
                case CANNON:
                    return 100;
                case ARCHER:
                    return 100;
                case WIZARD:
                    return 100;
            }
            return 0;
        }

        public static float GetDefaultCoolDown(int towerType){
            switch (towerType) {
                case CANNON:
                    return 120;
                case ARCHER:
                    return 25;
                case WIZARD:
                    return 40;
            }
            return 0;
        }

        public static int GetCostUpdate(int towerType,int towerLevel){
            switch (towerType) {
                case CANNON:
                    return 120;
                case ARCHER:
                    switch (towerLevel) {
                        case 1:
                            return 75;
                        case 2:
                            return 80;
                        case 3:
                            return 95;
                        case 4:
                            return 110;
                        case 5:
                            return 125;
                        case 6:
                            return 150;
                        case 7:
                            return 200;
                        default:
                            break;
                    }
                case WIZARD:
                    return 40;
            }
            return 0;
        }
    }

    public static class Direction{
        public static final int UP = 0;
        public static final int DOWN = 1;
        public static final int LEFT = 3;
        public static final int RIGHT = 4;
        
    }

    public static class Monsters{
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
            }
            return 0;
        }

        public static int GetStartHealth(int monsterType){
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

        public static int GetReward(int monsterType){
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

    public static class Tiles{
        //thong so co ban cua tile
        public static final int ORIGIANLTILESIZE = 32;
        public static final int SCALE = 2;
        public static final int TILE_SIZE = ORIGIANLTILESIZE * SCALE;
        //ID cua cac loai tile
        public static final int GRASS = 0;
        public static final int ROAD_HORIZONTAL = 1;
        public static final int ROAD_DOWN = 2;
        public static final int ROAD_LEFT_UP = 3;
        public static final int ROAD_LEFT_DOWN = 4;
        public static final int ROAD_RIGHT_UP = 5;
        public static final int ROAD_RIGHT_DOWN = 6;
        
        
    }


    public static class Projectile{
        public static final float SPEED      = 6f;
        public static final float HIT_RADIUS = 12f;
        public static final float TURN_SPEED = 0.18f;
    }

    public static class Arrow{
        public static final int ARROW_DRAW_SIZE = 10;
        public static final int ARROW_ANIM_SPEED = 4;
    }
}
