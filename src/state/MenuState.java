package src.state;

import src.core.Constants;
import src.core.Input;
import java.awt.*;

public class MenuState implements GameState {
    private static final String[] OPTIONS = {"Start Game", "Controls", "Quit"};
    
    private int selected;
    private GameState next;
    private boolean transition;
    public boolean quit;
    private int anim;
    
    @Override
    public void enter() {
        selected = 0;
        next = null;
        transition = false;
        quit = false;
        anim = 0;
    }
    
    @Override
    public void exit() {}
    
    @Override
    public void update(Input input) {
        anim++;
        
        if (input.upJust()) { selected--; if (selected < 0) selected = OPTIONS.length - 1; }
        if (input.downJust()) { selected++; if (selected >= OPTIONS.length) selected = 0; }
        
        if (input.confirm()) {
            switch (selected) {
                case 0 -> { next = new CharacterSelectState(); transition = true; }
                case 1 -> { next = new ControlsState(); transition = true; }
                case 2 -> quit = true;
            }
        }
        
        if (input.escape()) quit = true;
    }
    
    @Override
    public void render(Graphics2D g) {
        g.setColor(Constants.COLOR_BACKGROUND);
        g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        
        float pulse = (float)(Math.sin(anim * 0.05) * 0.3 + 0.7);
        g.setColor(new Color((int)(255 * pulse), (int)(200 * pulse), (int)(100 * pulse)));
        g.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        String title = "STREET BRAWLER";
        g.drawString(title, (Constants.SCREEN_WIDTH - fm.stringWidth(title)) / 2, 150);
        
        g.setFont(new Font("Arial", Font.BOLD, 28));
        fm = g.getFontMetrics();
        for (int i = 0; i < OPTIONS.length; i++) {
            int y = 280 + i * 60;
            g.setColor(i == selected ? Constants.COLOR_HIGHLIGHT : Constants.COLOR_TEXT);
            g.drawString(OPTIONS[i], (Constants.SCREEN_WIDTH - fm.stringWidth(OPTIONS[i])) / 2, y);
            if (i == selected) {
                g.drawRect(Constants.SCREEN_WIDTH / 2 - 100, y - 30, 200, 40);
            }
        }
        
        g.setColor(new Color(150, 150, 150));
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("UP/DOWN to navigate, ENTER to select, ESC to quit", 220, Constants.SCREEN_HEIGHT - 40);
    }
    
    @Override
    public GameState nextState() { return next; }
    
    @Override
    public boolean shouldTransition() { return transition; }
}
