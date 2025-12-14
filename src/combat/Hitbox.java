package src.combat;

public class Hitbox {
    public double x, y;
    public int width, height;
    public boolean active = true;
    
    public Hitbox(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public double right() { return x + width; }
    public double bottom() { return y + height; }
}