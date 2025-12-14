package src.effect;

import src.core.Constants;
import java.awt.*;

public class FloatingText {
    private double x, y, startY;
    private String text;
    private int duration;
    public boolean active;
    private Color color;
    
    public void trigger(double x, double y, int damage) {
        this.x = x;
        this.y = y;
        this.startY = y;
        this.text = String.valueOf(damage);
        this.color = new Color(255, 100, 100);
        this.duration = Constants.FLOAT_TEXT_DURATION;
        this.active = true;
    }
    
    public void update() {
        if (active) {
            duration--;
            y = startY - (Constants.FLOAT_TEXT_DURATION - duration) * 0.8;
            if (duration <= 0) active = false;
        }
    }
    
    public void render(Graphics2D g, int camX, int camY) {
        if (!active) return;
        float alpha = Math.min(1f, (float) duration / (Constants.FLOAT_TEXT_DURATION * 0.5f));
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.BLACK);
        g.drawString(text, (int)(x - camX) + 1, (int)(y - camY) + 1);
        g.setColor(color);
        g.drawString(text, (int)(x - camX), (int)(y - camY));
        g.setComposite(AlphaComposite.SrcOver);
    }
}