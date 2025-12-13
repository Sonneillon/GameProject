package combat;

public class Hitbox {
    private double x;
    private double y;
    private int width;
    private int height;
    private boolean active;
    
    public Hitbox(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.active = true;
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
    
    public int getWidth() {
        return width;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public double getCenterX() {
        return x + width / 2.0;
    }
    
    public double getCenterY() {
        return y + height / 2.0;
    }
    
    public double getRight() {
        return x + width;
    }
    
    public double getBottom() {
        return y + height;
    }
    
    public boolean contains(double px, double py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }
}