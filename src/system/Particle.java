package system;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
public class Particle {

    float x, y; 
    float vx, vy; //van toc
    float life, maxLife; //vòng đời
    float size; 
    int r, g, b;

    public Particle(float x, float y, float vx, float vy, float life,int r , int g, int b, float size) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.life = life;
        this.maxLife = life; 
        this.r = r;
        this.g = g;
        this.b = b;
        this.size = size;

    }

    public boolean update() {
        x += vx;
        y += vy;
        vy += 0.15f;
        vx *= 0.96f;
        life -= 0.025f;

        return life > 0;
    }

    public void draw(Graphics2D g2) {
        float alpha = Math.max(0, life / maxLife);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)); //hạt mờ dần

        g2.setColor(new Color(r, g, b));

        int s = Math.max(1,(int)(size*alpha));

        g2.fillOval((int)(x-s/2f),(int)(y-s/2f),s,s);
    }
}