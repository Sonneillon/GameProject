package effect;

import Constants;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;

public class HitEffect {
    private double x;
    private double y;
    private int duration;
    private int maxDuration;
    private boolean active;
    private Color color;
    
    public HitEffect() {
        this.active = false;
        this.maxDuration = Constants.HIT_EFFECT_DURATION;
    }
    
    public void trigger(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.duration = maxDuration;
        this.active = true;
    }
    
    public void trigger(double x, double y) {
        trigger(x, y, new Color(255, 255, 200));
    }
    
    public void update() {
        if (active) {
            duration--;
            if (duration <= 0) {
                active = false;
            }
        }
    }
    
    public void render(Graphics2D g2d, int cameraX, int cameraY) {
        if (!active) return;
        
        float alpha = (float) duration / maxDuration;
        int size = (int)(30 * (1.0 - alpha) + 10);
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(color);
        
        int screenX = (int)(x - cameraX - size / 2);
        int screenY = (int)(y - cameraY - size / 2);
        
        g2d.fillOval(screenX, screenY, size, size);
        
        g2d.setColor(Color.WHITE);
        int innerSize = size / 2;
        g2d.fillOval(screenX + innerSize / 2, screenY + innerSize / 2, innerSize, innerSize);
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
    
    public boolean isActive() {
        return active;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
}