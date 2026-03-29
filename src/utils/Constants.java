package utils;
//Định nghĩa các hằng số dùng chung (ID của loại quái, loại trụ, loại gạch nền).
public class Constants {

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
                    return 5;
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
    }

    public static class Direction{
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class Monsters{
        public static final int SLIME = 0;
        public static final int ORC = 1;
        public static final int WOLF = 2;
        public static final int BEE = 3;

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
                    return 150;
                case SLIME:
                    return 100;
                case BEE:
                    return 50;
                case WOLF:
                    return 80;
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
