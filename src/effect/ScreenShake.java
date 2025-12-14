package src.effect;

import src.core.Constants;
import java.util.Random;

public class ScreenShake {
    private int duration, intensity;
    private int offsetX, offsetY;
    private Random random = new Random();
    
    public void trigger(int intensity) {
        this.duration = Constants.SHAKE_DURATION;
        this.intensity = intensity;
    }
    
    public void triggerHeavy() { trigger(Constants.SHAKE_INTENSITY); }
    public void triggerLight() { trigger(Constants.SHAKE_INTENSITY / 2); }
    
    public void update() {
        if (duration > 0) {
            duration--;
            int current = (int)(intensity * (double) duration / Constants.SHAKE_DURATION);
            offsetX = current > 0 ? random.nextInt(current * 2 + 1) - current : 0;
            offsetY = current > 0 ? random.nextInt(current * 2 + 1) - current : 0;
        } else {
            offsetX = offsetY = 0;
        }
    }
    
    public int getOffsetX() { return offsetX; }
    public int getOffsetY() { return offsetY; }
}
