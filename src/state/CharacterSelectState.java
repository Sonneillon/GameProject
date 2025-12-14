package src.state;

import src.combat.CharacterType;
import src.core.Constants;
import src.core.Input;
import java.awt.*;

public class CharacterSelectState implements GameState {
    private CharacterType[] characters = CharacterType.values();
    private int selected;
    private GameState next;
    private boolean transition;
    private int anim;
    
    @Override
    public void enter() { selected = 0; next = null; transition = false; anim = 0; }
    
    @Override
    public void exit() {}
    
    @Override
    public void update(Input input) {
        anim++;
        
        if (input.leftJust()) { selected--; if (selected < 0) selected = characters.length - 1; }
        if (input.rightJust()) { selected++; if (selected >= characters.length) selected = 0; }
        
        if (input.confirm()) {
            next = new PlayState(characters[selected]);
            transition = true;
        }
        
        if (input.escape()) {
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
        String title = "SELECT FIGHTER";
        g.drawString(title, (Constants.SCREEN_WIDTH - fm.stringWidth(title)) / 2, 80);
        
        int cardW = 150, cardH = 200, spacing = 50;
        int totalW = characters.length * cardW + (characters.length - 1) * spacing;
        int startX = (Constants.SCREEN_WIDTH - totalW) / 2;
        int cardY = 150;
        
        for (int i = 0; i < characters.length; i++) {
            int x = startX + i * (cardW + spacing);
            CharacterType c = characters[i];
            
            g.setColor(i == selected ? new Color(60, 65, 75) : new Color(45, 48, 55));
            g.fillRect(x, cardY, cardW, cardH);
            
            if (i == selected) {
                g.setColor(Constants.COLOR_HIGHLIGHT);
                g.drawRect(x - 3, cardY - 3, cardW + 6, cardH + 6);
            }
            
            // Character silhouette
            g.setColor(c.color);
            int silW = 60, silH = 100;
            int silX = x + (cardW - silW) / 2;
            int silY = cardY + 30 + (i == selected ? (int)(Math.sin(anim * 0.1) * 3) : 0);
            int headSize = silW / 2;
            g.fillOval(silX + (silW - headSize) / 2, silY, headSize, headSize);
            g.fillRect(silX + 5, silY + headSize, silW - 10, silH - headSize - 20);
            
            // Name
            g.setFont(new Font("Arial", Font.BOLD, 28));
            fm = g.getFontMetrics();
            g.setColor(i == selected ? Constants.COLOR_HIGHLIGHT : Constants.COLOR_TEXT);
            g.drawString(c.name, x + (cardW - fm.stringWidth(c.name)) / 2, cardY + cardH - 30);
        }
        
        // Stats panel
        CharacterType c = characters[selected];
        g.setColor(new Color(35, 38, 45));
        g.fillRect(200, 380, 400, 140);
        g.setColor(c.color);
        g.drawRect(200, 380, 400, 140);
        
        g.setColor(Constants.COLOR_TEXT);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        fm = g.getFontMetrics();
        g.drawString(c.getDescription(), (Constants.SCREEN_WIDTH - fm.stringWidth(c.getDescription())) / 2, 400);
        
        String[] labels = {"Speed:", "HP:", "Power:"};
        int[] values = {(int)(c.speed / 6 * 100), c.maxHp, (int)(c.attackPower / 15.0 * 100)};
        Color[] colors = {new Color(100, 200, 100), new Color(200, 100, 100), new Color(100, 100, 200)};
        
        for (int i = 0; i < 3; i++) {
            int by = 420 + i * 30;
            g.setColor(Constants.COLOR_TEXT);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString(labels[i], 220, by + 12);
            g.setColor(new Color(30, 30, 30));
            g.fillRect(320, by, 150, 15);
            g.setColor(colors[i]);
            g.fillRect(320, by, Math.min(150, values[i] * 150 / 100), 15);
        }
        
        g.setColor(new Color(150, 150, 150));
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("LEFT/RIGHT to select, ENTER to confirm, ESC for menu", 200, Constants.SCREEN_HEIGHT - 40);
    }
    
    @Override
    public GameState nextState() { return next; }
    
    @Override
    public boolean shouldTransition() { return transition; }
}
