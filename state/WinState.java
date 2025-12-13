package state;

import Input;
import Renderer;
import Constants;
import java.awt.Color;
import java.awt.Graphics2D;

public class WinState implements GameState {
    private int finalScore;
    private String characterName;
    private GameState nextState;
    private boolean shouldTransition;
    private int animationTimer;
    
    public WinState(int score, String characterName) {
        this.finalScore = score;
        this.characterName = characterName;
        this.nextState = null;
        this.shouldTransition = false;
        this.animationTimer = 0;
    }
    
    @Override
    public void enter() {
        nextState = null;
        shouldTransition = false;
        animationTimer = 0;
    }
    
    @Override
    public void exit() {
    }
    
    @Override
    public void update(Input input) {
        animationTimer++;
        
        if (input.isRestartJustPressed()) {
            nextState = new MenuState();
            shouldTransition = true;
        }
        
        if (input.isEscapeJustPressed()) {
            System.exit(0);
        }
    }
    
    @Override
    public void render(Graphics2D g2d, Renderer renderer) {
        renderer.begin(g2d, 0, 0);
        
        renderer.fillBackground(Constants.COLOR_BACKGROUND);
        
        float pulse = (float)(Math.sin(animationTimer * 0.05) * 0.3 + 0.7);
        Color titleColor = new Color(
            (int)(255 * pulse),
            (int)(220 * pulse),
            (int)(100 * pulse)
        );
        
        renderer.drawTitleTextCentered("VICTORY!", 150, titleColor);
        
        renderer.drawMenuTextCentered("Congratulations, " + characterName + "!", 
                                     220, Constants.COLOR_TEXT);
        
        renderer.drawMenuTextCentered("You have defeated the boss!", 
                                     280, Constants.COLOR_TEXT);
        
        String scoreText = "Final Score: " + finalScore;
        renderer.drawTitleTextCentered(scoreText, 380, new Color(100, 200, 255));
        
        if (animationTimer % 60 < 40) {
            renderer.drawSmallTextCentered("Press R to return to menu", 
                                          Constants.SCREEN_HEIGHT - 80, Constants.COLOR_TEXT);
        }
        
        renderer.drawSmallTextCentered("Press ESC to quit", 
                                      Constants.SCREEN_HEIGHT - 50, new Color(150, 150, 150));
    }
    
    @Override
    public GameState getNextState() {
        return nextState;
    }
    
    @Override
    public boolean shouldTransition() {
        return shouldTransition;
    }
}