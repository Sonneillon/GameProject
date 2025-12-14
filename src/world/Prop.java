package src.world;

import src.combat.Hitbox;
import src.core.Constants;
import java.awt.*;

public class Prop {
    public enum Type { BARREL, BENCH, HYDRANT }
    
    public final Type type;
    public final double x, y, depth;
    public final int width, height;
    public int hp;
    public final boolean breakable;
    public boolean broken;
    public Hitbox hitbox;
    public Color color;
    
    public Prop(Type type, double x, double y) {
        this.type = type;
        this.x = x;
        this.y = y;
        
        switch (type) {
            case BARREL -> {
                width = Constants.BARREL_WIDTH;
                height = Constants.BARREL_HEIGHT;
                hp = Constants.BARREL_HP;
                breakable = true;
                color = new Color(139, 90, 43);
            }
            case BENCH -> {
                width = Constants.BENCH_WIDTH;
                height = Constants.BENCH_HEIGHT;
                hp = 0;
                breakable = false;
                color = new Color(101, 67, 33);
            }
            case HYDRANT -> {
                width = 30;
                height = 45;
                hp = 15;
                breakable = true;
                color = new Color(200, 50, 50);
            }
            default -> {
                width = 40;
                height = 40;
                hp = 0;
                breakable = false;
                color = Color.GRAY;
            }
        }
        
        this.depth = y + height;
        this.hitbox = new Hitbox(x, y, width, height);
    }
    
    public void damage(int amount) {
        if (!breakable || broken) return;
        hp -= amount;
        if (hp <= 0) {
            hp = 0;
            broken = true;
            hitbox.active = false;
        }
    }
    
    public void render(Graphics2D g, int camX, int camY) {
        if (broken) return;
        
        int sx = (int)(x - camX), sy = (int)(y - camY);
        g.setColor(color);
        
        switch (type) {
            case BARREL -> {
                g.fillOval(sx, sy + height - 15, width, 15);
                g.fillRect(sx + 3, sy + 5, width - 6, height - 15);
                g.setColor(color.darker());
                g.fillOval(sx, sy, width, 15);
                g.setColor(new Color(80, 80, 80));
                g.fillRect(sx, sy + 10, width, 4);
                g.fillRect(sx, sy + height - 15, width, 4);
            }
            case BENCH -> {
                g.fillRect(sx, sy, width, 10);
                g.setColor(color.darker());
                g.fillRect(sx + 5, sy + 10, 8, height - 10);
                g.fillRect(sx + width - 13, sy + 10, 8, height - 10);
            }
            case HYDRANT -> {
                g.fillRect(sx + 5, sy + 15, width - 10, height - 15);
                g.setColor(color.brighter());
                g.fillOval(sx + 2, sy, width - 4, 20);
            }
        }
    }
}