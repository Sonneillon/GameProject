package src.core;

import src.effect.ScreenShake;

public class Camera {
    private double x, y;
    private double targetX;
    private double minX, maxX;
    private boolean locked;
    private ScreenShake shake = new ScreenShake();
    
    public Camera() {
        this.maxX = Constants.LEVEL_WIDTH;
    }
    
    public void update(double playerX) {
        shake.update();
        
        if (!locked) {
            double playerScreenX = playerX - x;
            
            if (playerScreenX > Constants.SCREEN_WIDTH - Constants.CAMERA_DEAD_ZONE) {
                targetX = playerX - (Constants.SCREEN_WIDTH - Constants.CAMERA_DEAD_ZONE);
            } else if (playerScreenX < Constants.CAMERA_DEAD_ZONE) {
                targetX = playerX - Constants.CAMERA_DEAD_ZONE;
            }
            
            targetX = Math.max(minX, Math.min(maxX - Constants.SCREEN_WIDTH, targetX));
            x += (targetX - x) * Constants.CAMERA_SMOOTH;
        }
        
        x = Math.max(minX, Math.min(maxX - Constants.SCREEN_WIDTH, x));
    }
    
    public void setLocked(boolean locked) { this.locked = locked; }
    public void setMaxX(double maxX) { this.maxX = maxX; }
    public int getX() { return (int) x + shake.getOffsetX(); }
    public int getY() { return (int) y + shake.getOffsetY(); }
    public ScreenShake getShake() { return shake; }
}