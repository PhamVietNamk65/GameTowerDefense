package system;

import java.awt.Point;

import entity.monster.EnemyState;
import entity.monster.Monster;
import entity.trap.Wall;
import levels.Level;
import utils.Constants;

import static utils.Constants.Monsters.*;

public class EnemyMovement {

    private Point[] path;

    public EnemyMovement(Point[] path) {
        this.path = path;
    }

    public void move(Monster m, Level level) {

        if (m.getPathIndex() >= path.length) {
            m.reachEnd();
            return;
        }

        if (m.getState() == EnemyState.DYING)
           return;
        Point nextTile = path[m.getPathIndex()];
        float speed = GetSpeed(m.getEnemyType());
        if( m.getEnemyType() != BEE){
            
            int tileX = nextTile.x / Constants.Tiles.TILE_SIZE;
            int tileY = nextTile.y / Constants.Tiles.TILE_SIZE;

            if (level.hasWall(tileX, tileY)) {

                float targetX;
                float targetY;

                if (m.getDirection() == Constants.Direction.DOWN) {

                    targetX = nextTile.x + m.getxOffset();
                    targetY = nextTile.y + m.getyOffset() - Constants.Tiles.TILE_SIZE / 2;

                } 
                else {

                    if (m.getPathIndex() == 0) return;

                    Point prevTile = path[m.getPathIndex() - 1];

                    targetX = prevTile.x + m.getxOffset();
                    targetY = prevTile.y + m.getyOffset();
                }

                float dx = targetX - m.getX();
                float dy = targetY - m.getY();

                float distance = (float) Math.sqrt(dx * dx + dy * dy);

                if (distance > speed) {

                    float moveX = (dx / distance) * speed;
                    float moveY = (dy / distance) * speed;

                    m.updateDirection(moveX, moveY);
                    m.setPos(m.getX() + moveX, m.getY() + moveY);

                    return;
                }

                m.setPos(targetX, targetY);

                if (m.getState() != EnemyState.ATTACK) {
                    Wall wall = level.getWallAt(tileX, tileY);

                    m.setTargetWall(wall);
                    m.setState(EnemyState.ATTACK);
                }

                return;
            }
        }
        
            float targetX = nextTile.x + m.getxOffset();
            float targetY = nextTile.y + m.getyOffset();

            float dx = targetX - m.getX();
            float dy = targetY - m.getY();

            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance <= speed) {
                m.setPos(targetX, targetY);
                m.nextPath();
                return;
            }

            float moveX = (dx / distance) * speed;
            float moveY = (dy / distance) * speed;

            m.updateDirection(moveX, moveY);
            m.setPos(m.getX() + moveX, m.getY() + moveY);
    }

    public Point getStartPoint() {
        return path[0];
    }
}