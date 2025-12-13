package state;

import combat.CharacterType;
import Input;
import Renderer;
import Constants;
import java.awt.Color;
import java.awt.Graphics2D;

public class CharacterSelectState implements GameState {
    private CharacterType[] characters;
    private int selectedIndex;
    private GameState nextState;
    private boolean shouldTransition;
    private int animationTimer;
    
    public CharacterSelectState() {
        this.characters = CharacterType.values();
        this.selectedIndex = 0;
        this.nextState = null;
        this.shouldTransition = false;
        this.animationTimer = 0;
    }
    
    @Override
    public void enter() {
        selectedIndex = 0;
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
        
        if (input.isLeftJustPressed()) {
            selectedIndex--;
            if (selectedIndex < 0) {
                selectedIndex = characters.length - 1;
            }
        }
        
        if (input.isRightJustPressed()) {
            selectedIndex++;
            if (selectedIndex >= characters.length) {
                selectedIndex = 0;
            }
        }
        
        if (input.isConfirmJustPressed()) {
            nextState = new PlayState(characters[selectedIndex]);
            shouldTransition = true;
        }
        
        if (input.isEscapeJustPressed()) {
            nextState = new MenuState();
            shouldTransition = true;
        }
    }
    
    @Override
    public void render(Graphics2D g2d, Renderer renderer) {
        renderer.begin(g2d, 0, 0);
        
        renderer.fillBackground(Constants.COLOR_BACKGROUND);
        
        renderer.drawTitleTextCentered("SELECT YOUR FIGHTER", 80, Constants.COLOR_TEXT_HIGHLIGHT);
        
        int characterWidth = 150;
        int characterHeight = 200;
        int spacing = 50;
        int totalWidth = characters.length * characterWidth + (characters.length - 1) * spacing;
        int startX = (Constants.SCREEN_WIDTH - totalWidth) / 2;
        int characterY = 150;
        
        for (int i = 0; i < characters.length; i++) {
            int x = startX + i * (characterWidth + spacing);
            renderCharacterPanel(g2d, renderer, characters[i], x, characterY, 
                                characterWidth, characterHeight, i == selectedIndex);
        }
        
        CharacterType selected = characters[selectedIndex];
        renderCharacterStats(g2d, renderer, selected);
        
        renderer.drawSmallTextCentered("Use LEFT/RIGHT arrows to select, ENTER to confirm", 
                                       Constants.SCREEN_HEIGHT - 60, new Color(150, 150, 150));
        renderer.drawSmallTextCentered("ESC to return to menu", 
                                       Constants.SCREEN_HEIGHT - 40, new Color(150, 150, 150));
    }
    
    private void renderCharacterPanel(Graphics2D g2d, Renderer renderer, CharacterType character,
                                      int x, int y, int width, int height, boolean selected) {
        Color bgColor = selected ? new Color(60, 65, 75) : new Color(45, 48, 55);
        renderer.fillRectScreen(x, y, width, height, bgColor);
        
        if (selected) {
            int bounce = (int)(Math.sin(animationTimer * 0.1) * 5);
            renderer.drawRectScreen(x - 3, y - 3 + bounce, width + 6, height + 6, 
                                   Constants.COLOR_TEXT_HIGHLIGHT);
            renderer.drawRectScreen(x - 1, y - 1 + bounce, width + 2, height + 2, 
                                   Constants.COLOR_TEXT_HIGHLIGHT);
        }
        
        int silhouetteWidth = 60;
        int silhouetteHeight = 100;
        int silhouetteX = x + (width - silhouetteWidth) / 2;
        int silhouetteY = y + 30;
        
        if (selected) {
            silhouetteY += (int)(Math.sin(animationTimer * 0.1) * 3);
        }
        
        renderer.drawCharacterSilhouette(silhouetteX, silhouetteY, silhouetteWidth, 
                                        silhouetteHeight, character.getColor());
        
        g2d.setFont(renderer.getMenuFont());
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        int nameWidth = fm.stringWidth(character.getName());
        int nameX = x + (width - nameWidth) / 2;
        
        renderer.drawMenuText(character.getName(), nameX, y + height - 30, 
                             selected ? Constants.COLOR_TEXT_HIGHLIGHT : Constants.COLOR_TEXT);
    }
    
    private void renderCharacterStats(Graphics2D g2d, Renderer renderer, CharacterType character) {
        int statsY = 380;
        int statsWidth = 400;
        int statsX = (Constants.SCREEN_WIDTH - statsWidth) / 2;
        
        renderer.fillRectScreen(statsX - 20, statsY - 10, statsWidth + 40, 160, 
                               new Color(35, 38, 45));
        renderer.drawRectScreen(statsX - 20, statsY - 10, statsWidth + 40, 160, 
                               character.getColor());
        
        renderer.drawSmallTextCentered(character.getDescription(), statsY + 10, Constants.COLOR_TEXT);
        
        int barY = statsY + 40;
        int barWidth = 150;
        int barHeight = 15;
        int labelX = statsX;
        int barX = statsX + 100;
        int spacing = 30;
        
        renderer.drawUIText("Speed:", labelX, barY + 12, Constants.COLOR_TEXT);
        renderStatBar(renderer, barX, barY, barWidth, barHeight, 
                     (int)(character.getSpeed() / 6.0 * 100), new Color(100, 200, 100));
        
        renderer.drawUIText("HP:", labelX, barY + spacing + 12, Constants.COLOR_TEXT);
        renderStatBar(renderer, barX, barY + spacing, barWidth, barHeight, 
                     character.getMaxHp(), new Color(200, 100, 100));
        
        renderer.drawUIText("Power:", labelX, barY + spacing * 2 + 12, Constants.COLOR_TEXT);
        renderStatBar(renderer, barX, barY + spacing * 2, barWidth, barHeight, 
                     (int)(character.getAttackPower() / 15.0 * 100), new Color(100, 100, 200));
    }
    
    private void renderStatBar(Renderer renderer, int x, int y, int width, int height, 
                              int value, Color color) {
        renderer.fillRectScreen(x, y, width, height, new Color(30, 30, 30));
        
        int fillWidth = (int)(value / 100.0 * width);
        fillWidth = Math.min(fillWidth, width);
        renderer.fillRectScreen(x, y, fillWidth, height, color);
        
        renderer.drawRectScreen(x, y, width, height, new Color(80, 80, 80));
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