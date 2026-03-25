package system;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import entity.Tower;


public class ParticleSystem {

    private ArrayList<Particle> particles = new ArrayList<>();
    private Random random;

    public void update() {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            if (!it.next().update())
                it.remove();
        }
    }

    public void draw(Graphics2D g2) {
        Composite old = g2.getComposite();

        for (Particle p : particles)
            p.draw(g2);

        g2.setComposite(old);
    }

    private static final int[][] PARTICLE_COLORS = {
        {200,160, 60}, {255,200, 50}, {120,200,255},
        { 80,220,100}, {200, 80,255}, {255,120, 40}, {255,255,180}
    };
    
    private void spawnParticles(Tower t, boolean burst) {
        int lv = clamp(t.getTowerLevel(), 0, 6);
        int[] col = PARTICLE_COLORS[lv];
        float cx = t.getCenterX(), cy = t.getCenterY();
        int count = burst ? 28 : 14;
        float spd = burst ? 3.2f : 1.8f;
        for (int i = 0; i < count; i++) {
            double angle = Math.PI*2*i/count + random.nextDouble()*0.4;
            float speed = spd + random.nextFloat()*1.5f;
            float vx = (float)(Math.cos(angle)*speed);
            float vy = (float)(Math.sin(angle)*speed) - 1.5f;
            float life = 0.6f + random.nextFloat()*0.5f;
            float size = burst ? (4+random.nextFloat()*4) : (3+random.nextFloat()*2);
            int r = (i%3==0)?255:col[0], g=(i%3==0)?220:col[1], b=(i%3==0)?50:col[2];
            particles.add(new Particle(cx, cy, vx, vy, life, r, g, b, size));
        }
    }
    private int clamp(int v, int min, int max) { return Math.max(min, Math.min(max, v)); }
}