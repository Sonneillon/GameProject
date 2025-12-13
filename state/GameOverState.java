package state;

import Input;
import Renderer;
import Constants;
import java.awt.Color;
import java.awt.Graphics2D;

public class GameOverState implements GameState {
    private int finalScore;
    private String characterName;
    private GameState nextState;
    private boolean shouldTransition;
    private int animationTimer;
    
    public GameOverState(int score, String characterName) {
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
        
        renderer.fillBackground(new Color(30, 20, 20));
        
        float pulse = (float)(Math.sin(animationTimer * 0.08) * 0.3 + 0.7);
        Color titleColor = new Color(
            (int)(200 * pulse),
            (int)(50 * pulse),
            (int)(50 * pulse)
        );
        
        renderer.drawTitleTextCentered("GAME OVER", 150, titleColor);
        
        renderer.drawMenuTextCentered(characterName + " has fallen...", 
                                     230, Constants.COLOR_TEXT);
        
        String scoreText = "Final Score: " + finalScore;
        renderer.drawMenuTextCentered(scoreText, 320, new Color(200, 200, 200));
        
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