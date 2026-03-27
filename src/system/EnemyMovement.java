package system;

import java.awt.Point;

import entity.EnemyState;
import entity.Monster;

import static utils.Constants.Monsters.*;

public class EnemyMovement {

    private Point[] path;
    public EnemyMovement(Point[] path) {
        this.path = path;
    }
    
    public void move(Monster m) {

        if (m.getPathIndex() >= path.length) {
            m.setState(EnemyState.ATTACK);
            return;
        }

        // Đích đến = Tọa độ Path + Độ lệch riêng của con quái đó
        float targetX = path[m.getPathIndex()].x + m.getxOffset();
        float targetY = path[m.getPathIndex()].y + m.getyOffset();

        float dx = targetX - m.getX();
        float dy = targetY - m.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float speed = GetSpeed(m.getEnemyType());

        if (distance < speed) {
            m.setPos(targetX, targetY);
            m.nextPath();
            return;
        }

        if (Math.abs(dx) > Math.abs(dy)) {
            // Di chuyển ngang là chủ yếu
            if (dx > 0) 
                m.setDirection(utils.Constants.Direction.RIGHT);
            else 
                m.setDirection(utils.Constants.Direction.LEFT);
        } else {
            // Di chuyển dọc là chủ yếu
            if (dy > 0) 
                m.setDirection(utils.Constants.Direction.DOWN);
            else m.setDirection(utils.Constants.Direction.UP);
        }   
        m.setPos(
            (m.getX() + (dx / distance) * speed),
            (m.getY() + (dy / distance) * speed)
        );
    }

    public Point getStartPoint() {
        return path[0];
    }
}