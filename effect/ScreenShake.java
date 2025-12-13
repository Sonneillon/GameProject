package effect;

import Constants;
import java.util.Random;

public class ScreenShake {
    private int duration;
    private int intensity;
    private int offsetX;
    private int offsetY;
    private final Random random;
    
    public ScreenShake() {
        this.duration = 0;
        this.intensity = 0;
        this.offsetX = 0;
        this.offsetY = 0;
        this.random = new Random();
    }
    
    public void trigger(int intensity) {
        this.duration = Constants.SCREEN_SHAKE_DURATION;
        this.intensity = intensity;
    }
    
    public void triggerHeavy() {
        trigger(Constants.SCREEN_SHAKE_INTENSITY);
    }
    
    public void triggerLight() {
        trigger(Constants.SCREEN_SHAKE_INTENSITY / 2);
    }
    
    public void update() {
        if (duration > 0) {
            duration--;
            double factor = (double) duration / Constants.SCREEN_SHAKE_DURATION;
            int currentIntensity = (int)(intensity * factor);
            offsetX = random.nextInt(currentIntensity * 2 + 1) - currentIntensity;
            offsetY = random.nextInt(currentIntensity * 2 + 1) - currentIntensity;
        } else {
            offsetX = 0;
            offsetY = 0;
        }
    }
    
    public int getOffsetX() {
        return offsetX;
    }
    
    public int getOffsetY() {
        return offsetY;
    }
    
    public boolean isShaking() {
        return duration > 0;
    }
}