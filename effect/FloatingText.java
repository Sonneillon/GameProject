package effect;

import Constants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;

public class FloatingText {
    private double x;
    private double y;
    private double startY;
    private String text;
    private int duration;
    private int maxDuration;
    private boolean active;
    private Color color;
    private Font font;
    
    public FloatingText() {
        this.active = false;
        this.maxDuration = Constants.FLOATING_TEXT_DURATION;
        this.font = new Font("Arial", Font.BOLD, 16);
    }
    
    public void trigger(double x, double y, String text, Color color) {
        this.x = x;
        this.y = y;
        this.startY = y;
        this.text = text;
        this.color = color;
        this.duration = maxDuration;
        this.active = true;
    }
    
    public void trigger(double x, double y, String text) {
        trigger(x, y, text, Color.WHITE);
    }
    
    public void triggerDamage(double x, double y, int damage) {
        trigger(x, y, String.valueOf(damage), new Color(255, 100, 100));
    }
    
    public void triggerHit(double x, double y) {
        trigger(x, y, "HIT!", new Color(255, 220, 100));
    }
    
    public void update() {
        if (active) {
            duration--;
            y = startY - (maxDuration - duration) * 0.8;
            if (duration <= 0) {
                active = false;
            }
        }
    }
    
    public void render(Graphics2D g2d, int cameraX, int cameraY) {
        if (!active) return;
        
        float alpha = Math.min(1.0f, (float) duration / (maxDuration * 0.5f));
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setFont(font);
        
        int screenX = (int)(x - cameraX);
        int screenY = (int)(y - cameraY);
        
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, screenX + 1, screenY + 1);
        
        g2d.setColor(color);
        g2d.drawString(text, screenX, screenY);
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
    
    public boolean isActive() {
        return active;
    }
}