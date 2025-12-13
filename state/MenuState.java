package state;

import Input;
import Renderer;
import Constants;
import java.awt.Color;
import java.awt.Graphics2D;

public class MenuState implements GameState {
    private static final String[] MENU_OPTIONS = {"Start Game", "Controls", "Quit"};
    
    private int selectedIndex;
    private GameState nextState;
    private boolean shouldTransition;
    private boolean shouldQuit;
    
    private int animationTimer;
    
    public MenuState() {
        this.selectedIndex = 0;
        this.nextState = null;
        this.shouldTransition = false;
        this.shouldQuit = false;
        this.animationTimer = 0;
    }
    
    @Override
    public void enter() {
        selectedIndex = 0;
        nextState = null;
        shouldTransition = false;
        shouldQuit = false;
        animationTimer = 0;
    }
    
    @Override
    public void exit() {
    }
    
    @Override
    public void update(Input input) {
        animationTimer++;
        
        if (input.isUpJustPressed()) {
            selectedIndex--;
            if (selectedIndex < 0) {
                selectedIndex = MENU_OPTIONS.length - 1;
            }
        }
        
        if (input.isDownJustPressed()) {
            selectedIndex++;
            if (selectedIndex >= MENU_OPTIONS.length) {
                selectedIndex = 0;
            }
        }
        
        if (input.isConfirmJustPressed()) {
            switch (selectedIndex) {
                case 0:
                    nextState = new CharacterSelectState();
                    shouldTransition = true;
                    break;
                case 1:
                    nextState = new ControlsState();
                    shouldTransition = true;
                    break;
                case 2:
                    shouldQuit = true;
                    break;
            }
        }
        
        if (input.isEscapeJustPressed()) {
            shouldQuit = true;
        }
    }
    
    @Override
    public void render(Graphics2D g2d, Renderer renderer) {
        renderer.begin(g2d, 0, 0);
        
        renderer.fillBackground(Constants.COLOR_BACKGROUND);
        
        renderTitle(g2d, renderer);
        
        renderMenuOptions(g2d, renderer);
        
        renderFooter(g2d, renderer);
    }
    
    private void renderTitle(Graphics2D g2d, Renderer renderer) {
        String title = "STREET BRAWLER";
        
        float pulse = (float)(Math.sin(animationTimer * 0.05) * 0.3 + 0.7);
        Color titleColor = new Color(
            (int)(255 * pulse),
            (int)(200 * pulse),
            (int)(100 * pulse)
        );
        
        renderer.drawTitleTextCentered(title, 150, titleColor);
        
        renderer.drawSmallTextCentered("A Beat 'Em Up Adventure", 180, Constants.COLOR_TEXT);
    }
    
    private void renderMenuOptions(Graphics2D g2d, Renderer renderer) {
        int startY = 280;
        int spacing = 60;
        
        for (int i = 0; i < MENU_OPTIONS.length; i++) {
            int y = startY + i * spacing;
            
            if (i == selectedIndex) {
                int arrowX = Constants.SCREEN_WIDTH / 2 - 120;
                int arrowOffset = (int)(Math.sin(animationTimer * 0.1) * 5);
                renderer.drawSelectionArrow(arrowX + arrowOffset, y - 15, Constants.COLOR_TEXT_HIGHLIGHT);
                
                renderer.drawMenuTextCentered(MENU_OPTIONS[i], y, Constants.COLOR_TEXT_HIGHLIGHT);
                
                int boxWidth = 200;
                int boxHeight = 40;
                int boxX = Constants.SCREEN_WIDTH / 2 - boxWidth / 2;
                int boxY = y - 30;
                renderer.drawRectScreen(boxX, boxY, boxWidth, boxHeight, Constants.COLOR_TEXT_HIGHLIGHT);
            } else {
                renderer.drawMenuTextCentered(MENU_OPTIONS[i], y, Constants.COLOR_TEXT);
            }
        }
    }
    
    private void renderFooter(Graphics2D g2d, Renderer renderer) {
        renderer.drawSmallTextCentered("Use UP/DOWN arrows to navigate, ENTER to select", 
                                       Constants.SCREEN_HEIGHT - 60, new Color(150, 150, 150));
        renderer.drawSmallTextCentered("ESC to quit", 
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
    
    public boolean shouldQuit() {
        return shouldQuit;
    }
}