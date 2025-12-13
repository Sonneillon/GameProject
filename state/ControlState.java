package state;

import Input;
import Renderer;
import Constants;
import java.awt.Color;
import java.awt.Graphics2D;

public class ControlsState implements GameState {
    private GameState nextState;
    private boolean shouldTransition;
    
    public ControlsState() {
        this.nextState = null;
        this.shouldTransition = false;
    }
    
    @Override
    public void enter() {
        nextState = null;
        shouldTransition = false;
    }
    
    @Override
    public void exit() {
    }
    
    @Override
    public void update(Input input) {
        if (input.isConfirmJustPressed() || input.isEscapeJustPressed()) {
            nextState = new MenuState();
            shouldTransition = true;
        }
    }
    
    @Override
    public void render(Graphics2D g2d, Renderer renderer) {
        renderer.begin(g2d, 0, 0);
        
        renderer.fillBackground(Constants.COLOR_BACKGROUND);
        
        renderer.drawTitleTextCentered("CONTROLS", 80, Constants.COLOR_TEXT_HIGHLIGHT);
        
        int startY = 160;
        int spacing = 45;
        int leftCol = 200;
        int rightCol = 450;
        
        String[][] controls = {
            {"Arrow Keys", "Move"},
            {"Z", "Attack (3-hit combo)"},
            {"X", "Heavy Attack"},
            {"C", "Jump"},
            {"V", "Jump Attack"},
            {"Enter", "Confirm / Select"},
            {"Esc", "Quit / Back"},
            {"R", "Restart (after game over)"}
        };
        
        for (int i = 0; i < controls.length; i++) {
            int y = startY + i * spacing;
            
            renderer.drawUIText(controls[i][0], leftCol, y, Constants.COLOR_TEXT_HIGHLIGHT);
            renderer.drawUIText(controls[i][1], rightCol, y, Constants.COLOR_TEXT);
        }
        
        int tipsY = startY + controls.length * spacing + 40;
        renderer.drawMenuTextCentered("Combat Tips", tipsY, new Color(200, 150, 100));
        
        String[] tips = {
            "Chain Z attacks for a 3-hit combo!",
            "Heavy attack (X) has knockback but slow startup",
            "Jump attack (V) is great for closing distance",
            "Align your depth (Y position) with enemies to hit them"
        };
        
        for (int i = 0; i < tips.length; i++) {
            renderer.drawSmallTextCentered(tips[i], tipsY + 35 + i * 25, Constants.COLOR_TEXT);
        }
        
        renderer.drawSmallTextCentered("Press ENTER or ESC to return to menu", 
                                       Constants.SCREEN_HEIGHT - 40, new Color(150, 150, 150));
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