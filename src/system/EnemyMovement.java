package system;

import java.awt.Point;

import entity.monster.EnemyState;
import entity.monster.Monster;
import utils.Constants;

import static utils.Constants.Monsters.*;

public class EnemyMovement {

    private Point[] path;

    public EnemyMovement(Point[] path) {
        this.path = path;
    }

    public void move(Monster m) {
        
        if (m.getPathIndex() >= path.length) {
            m.reachEnd();
            return;
        }

        if (m.getState() == EnemyState.DYING)
            return;

        if(m.getEnemyType() == Constants.Monsters.BEE){

        Point end = path[path.length - 1];

        float targetX = end.x + m.getxOffset();
        float targetY = end.y + m.getyOffset();

        float dx = targetX - m.getX();
        float dy = targetY - m.getY();

        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float speed = GetSpeed(m.getEnemyType());

        if (distance <= speed) {
            m.setPos(targetX, targetY);
            m.reachEnd();
            return;
        }

        float moveX = (dx / distance) * speed;
        float moveY = (dy / distance) * speed;

        m.updateDirection(moveX, moveY);

        m.setPos(m.getX() + moveX, m.getY() + moveY);
        }
        else {
            float targetX = path[m.getPathIndex()].x + m.getxOffset();
            float targetY = path[m.getPathIndex()].y + m.getyOffset();

            float dx = targetX - m.getX();
            float dy = targetY - m.getY();

            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            float speed = GetSpeed(m.getEnemyType());

            if (distance <= speed) {
                m.setPos(targetX, targetY);
                m.nextPath();
                return;
            }

            float moveX = (dx / distance) * speed;
            float moveY = (dy / distance) * speed;

            m.updateDirection(moveX, moveY);

            m.setPos(m.getX() + moveX,m.getY() + moveY);
        }
        
    }

    public Point getStartPoint() {
        return path[0];
    }
}