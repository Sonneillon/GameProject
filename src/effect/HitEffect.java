package src.effect;

import src.core.Constants;
import java.awt.*;

public class HitEffect {
    private double x, y;
    private int duration;
    public boolean active;
    
    public void trigger(double x, double y) {
        this.x = x;
        this.y = y;
        this.duration = Constants.HIT_EFFECT_DURATION;
        this.active = true;
    }
    
    public void update() {
        if (active && --duration <= 0) active = false;
    }
    
    public void render(Graphics2D g, int camX, int camY) {
        if (!active) return;
        float alpha = (float) duration / Constants.HIT_EFFECT_DURATION;
        int size = (int)(30 * (1 - alpha) + 10);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g.setColor(new Color(255, 255, 200));
        g.fillOval((int)(x - camX - size/2), (int)(y - camY - size/2), size, size);
        g.setComposite(AlphaComposite.SrcOver);
    }
}