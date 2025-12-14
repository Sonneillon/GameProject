package src.state;

import src.core.Constants;
import src.core.Input;
import java.awt.*;

public class ControlsState implements GameState {
    private GameState next;
    private boolean transition;
    
    @Override
    public void enter() { next = null; transition = false; }
    
    @Override
    public void exit() {}
    
    @Override
    public void update(Input input) {
        if (input.confirm() || input.escape()) {
            next = new MenuState();
            transition = true;
        }
    }
    
    @Override
    public void render(Graphics2D g) {
        g.setColor(Constants.COLOR_BACKGROUND);
        g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        
        g.setColor(Constants.COLOR_HIGHLIGHT);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        g.drawString("CONTROLS", (Constants.SCREEN_WIDTH - fm.stringWidth("CONTROLS")) / 2, 80);
        
        g.setFont(new Font("Arial", Font.BOLD, 18));
        String[][] controls = {
            {"Arrow Keys", "Move"},
            {"Z", "Attack (3-hit combo)"},
            {"X", "Heavy Attack"},
            {"C", "Jump"},
            {"V", "Jump Attack"},
            {"Enter", "Confirm"},
            {"Esc", "Back"},
            {"R", "Restart"}
        };
        
        for (int i = 0; i < controls.length; i++) {
            g.setColor(Constants.COLOR_HIGHLIGHT);
            g.drawString(controls[i][0], 200, 160 + i * 40);
            g.setColor(Constants.COLOR_TEXT);
            g.drawString(controls[i][1], 400, 160 + i * 40);
        }
        
        g.setColor(new Color(150, 150, 150));
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Press ENTER or ESC to return", 280, Constants.SCREEN_HEIGHT - 40);
    }
    
    @Override
    public GameState nextState() { return next; }
    
    @Override
    public boolean shouldTransition() { return transition; }
}