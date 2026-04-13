package system;

import entity.monster.*;

import static utils.Constants.Monsters.*;

public class EnemySpawner {

    public Monster spawn(int type) {

        Monster m = null;

        switch (type) {
            case ORC:
                m = new Orc(0, 0, 0);
                break;

            case BEE:
                m = new Bee(0, 0, 0);
                break;

            case SLIME:
                m = new Slime(0, 0, 0);
                break;

            case WOLF:
                m = new Wolf(0, 0, 0);
                break;
            case RAT:
                m = new Rat(0, 0, 0);
                break;
            case RIDER:
                m = new Rider(0, 0, 0);
                break;
            case MAGE:
                m = new Mage(0, 0, 0);
                break;
        }

        return m;
    }
}