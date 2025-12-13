package entity;

import combat.Hitbox;
import java.awt.Graphics2D;

public abstract class Entity {
    protected double x;
    protected double y;
    protected double velX;
    protected double velY;
    protected int width;
    protected int height;
    protected int facingDirection;
    protected boolean active;
    protected Hitbox hurtbox;
    
    public Entity(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velX = 0;
        this.velY = 0;
        this.facingDirection = 1;
        this.active = true;
        this.hurtbox = new Hitbox(x, y, width, height);
    }
    
    public abstract void update();
    public abstract void render(Graphics2D g2d, int cameraX, int cameraY);
    
    protected void updateHurtbox() {
        hurtbox.setX(x);
        hurtbox.setY(y);
        hurtbox.setWidth(width);
        hurtbox.setHeight(height);
    }
    
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double getVelX() {
        return velX;
    }
    
    public void setVelX(double velX) {
        this.velX = velX;
    }
    
    public double getVelY() {
        return velY;
    }
    
    public void setVelY(double velY) {
        this.velY = velY;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getFacingDirection() {
        return facingDirection;
    }
    
    public void setFacingDirection(int facingDirection) {
        this.facingDirection = facingDirection;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public Hitbox getHurtbox() {
        return hurtbox;
    }
    
    public double getCenterX() {
        return x + width / 2.0;
    }
    
    public double getCenterY() {
        return y + height / 2.0;
    }
    
    public double getDepth() {
        return y + height;
    }
}