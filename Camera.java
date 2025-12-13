import effect.ScreenShake;

public class Camera {
    private double x;
    private double y;
    private int viewportWidth;
    private int viewportHeight;
    private double targetX;
    private double minX;
    private double maxX;
    private boolean locked;
    private ScreenShake screenShake;
    
    public Camera(int viewportWidth, int viewportHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.x = 0;
        this.y = 0;
        this.targetX = 0;
        this.minX = 0;
        this.maxX = 0;
        this.locked = false;
        this.screenShake = new ScreenShake();
    }
    
    public void update(double playerX) {
        screenShake.update();
        
        if (!locked) {
            double playerScreenX = playerX - x;
            
            if (playerScreenX > viewportWidth - Constants.CAMERA_DEAD_ZONE) {
                targetX = playerX - (viewportWidth - Constants.CAMERA_DEAD_ZONE);
            } else if (playerScreenX < Constants.CAMERA_DEAD_ZONE) {
                targetX = playerX - Constants.CAMERA_DEAD_ZONE;
            }
            
            targetX = Math.max(minX, Math.min(maxX - viewportWidth, targetX));
            
            x += (targetX - x) * Constants.CAMERA_SMOOTH;
        }
        
        x = Math.max(minX, Math.min(maxX - viewportWidth, x));
    }
    
    public void setBounds(double minX, double maxX) {
        this.minX = minX;
        this.maxX = maxX;
    }
    
    public void lock() {
        this.locked = true;
    }
    
    public void unlock() {
        this.locked = false;
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        this.targetX = x;
    }
    
    public int getX() {
        return (int) x + screenShake.getOffsetX();
    }
    
    public int getY() {
        return (int) y + screenShake.getOffsetY();
    }
    
    public double getRawX() {
        return x;
    }
    
    public double getRawY() {
        return y;
    }
    
    public int getViewportWidth() {
        return viewportWidth;
    }
    
    public int getViewportHeight() {
        return viewportHeight;
    }
    
    public ScreenShake getScreenShake() {
        return screenShake;
    }
    
    public void triggerShake(int intensity) {
        screenShake.trigger(intensity);
    }
    
    public void triggerHeavyShake() {
        screenShake.triggerHeavy();
    }
    
    public void triggerLightShake() {
        screenShake.triggerLight();
    }
    
    public double getMinX() {
        return minX;
    }
    
    public double getMaxX() {
        return maxX;
    }
    
    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }
}