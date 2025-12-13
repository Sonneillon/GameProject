package world;

import combat.Hitbox;
import Constants;
import java.awt.Color;
import java.awt.Graphics2D;

public class Prop {
    public enum PropType {
        BARREL,
        BENCH,
        HYDRANT
    }
    
    private PropType type;
    private double x;
    private double y;
    private int width;
    private int height;
    private double depth;
    private int maxHp;
    private int currentHp;
    private boolean breakable;
    private boolean broken;
    private Hitbox hitbox;
    private Color color;
    
    public Prop(PropType type, double x, double y) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.broken = false;
        
        switch (type) {
            case BARREL:
                this.width = Constants.PROP_BARREL_WIDTH;
                this.height = Constants.PROP_BARREL_HEIGHT;
                this.maxHp = Constants.PROP_BARREL_HP;
                this.breakable = true;
                this.color = new Color(139, 90, 43);
                break;
            case BENCH:
                this.width = Constants.PROP_BENCH_WIDTH;
                this.height = Constants.PROP_BENCH_HEIGHT;
                this.maxHp = 0;
                this.breakable = false;
                this.color = new Color(101, 67, 33);
                break;
            case HYDRANT:
                this.width = 30;
                this.height = 45;
                this.maxHp = 15;
                this.breakable = true;
                this.color = new Color(200, 50, 50);
                break;
            default:
                this.width = 40;
                this.height = 40;
                this.maxHp = 0;
                this.breakable = false;
                this.color = Constants.COLOR_PROP;
        }
        
        this.currentHp = maxHp;
        this.depth = y + height;
        this.hitbox = new Hitbox(x, y, width, height);
    }
    
    public void takeDamage(int damage) {
        if (!breakable || broken) return;
        
        currentHp -= damage;
        if (currentHp <= 0) {
            currentHp = 0;
            broken = true;
            hitbox.setActive(false);
        }
    }
    
    public void render(Graphics2D g2d, int cameraX, int cameraY) {
        if (broken) return;
        
        int screenX = (int)(x - cameraX);
        int screenY = (int)(y - cameraY);
        
        switch (type) {
            case BARREL:
                renderBarrel(g2d, screenX, screenY);
                break;
            case BENCH:
                renderBench(g2d, screenX, screenY);
                break;
            case HYDRANT:
                renderHydrant(g2d, screenX, screenY);
                break;
        }
    }
    
    private void renderBarrel(Graphics2D g2d, int screenX, int screenY) {
        g2d.setColor(color);
        g2d.fillOval(screenX, screenY + height - 15, width, 15);
        g2d.fillRect(screenX + 3, screenY + 5, width - 6, height - 15);
        g2d.setColor(color.darker());
        g2d.fillOval(screenX, screenY, width, 15);
        
        g2d.setColor(new Color(80, 80, 80));
        g2d.fillRect(screenX, screenY + 10, width, 4);
        g2d.fillRect(screenX, screenY + height - 15, width, 4);
    }
    
    private void renderBench(Graphics2D g2d, int screenX, int screenY) {
        g2d.setColor(color);
        g2d.fillRect(screenX, screenY, width, 10);
        
        g2d.setColor(color.darker());
        int legWidth = 8;
        int legHeight = height - 10;
        g2d.fillRect(screenX + 5, screenY + 10, legWidth, legHeight);
        g2d.fillRect(screenX + width - legWidth - 5, screenY + 10, legWidth, legHeight);
        
        g2d.setColor(color.brighter());
        g2d.fillRect(screenX, screenY + 15, width, 5);
    }
    
    private void renderHydrant(Graphics2D g2d, int screenX, int screenY) {
        g2d.setColor(color);
        g2d.fillRect(screenX + 5, screenY + 15, width - 10, height - 15);
        
        g2d.setColor(color.brighter());
        g2d.fillOval(screenX + 2, screenY, width - 4, 20);
        
        g2d.setColor(color.darker());
        g2d.fillRect(screenX, screenY + 20, 8, 10);
        g2d.fillRect(screenX + width - 8, screenY + 20, 8, 10);
        
        g2d.setColor(new Color(150, 150, 150));
        g2d.fillOval(screenX + width/2 - 4, screenY + 5, 8, 8);
    }
    
    public PropType getType() {
        return type;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public double getDepth() {
        return depth;
    }
    
    public Hitbox getHitbox() {
        return hitbox;
    }
    
    public boolean isBreakable() {
        return breakable;
    }
    
    public boolean isBroken() {
        return broken;
    }
    
    public boolean isSolid() {
        return !broken;
    }
}