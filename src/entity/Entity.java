package src.entity;

import src.combat.Hitbox;
import java.awt.Graphics2D;

public abstract class Entity {
    public double x, y, velX, velY;
    public int width, height;
    public int facing = 1;
    public boolean active = true;
    public Hitbox hitbox;
    
    public Entity(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hitbox = new Hitbox(x, y, width, height);
    }
    
    protected void updateHitbox() {
        hitbox.x = x;
        hitbox.y = y;
        hitbox.width = width;
        hitbox.height = height;
    }
    
    public double centerX() { return x + width / 2.0; }
    public double centerY() { return y + height / 2.0; }
    public double depth() { return y + height; }
    
    public abstract void update();
    public abstract void render(Graphics2D g, int camX, int camY);
}
