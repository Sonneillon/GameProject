import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Renderer {
    private Graphics2D g2d;
    private int cameraX;
    private int cameraY;
    
    private Font titleFont;
    private Font menuFont;
    private Font uiFont;
    private Font smallFont;
    
    public Renderer() {
        titleFont = new Font("Arial", Font.BOLD, 48);
        menuFont = new Font("Arial", Font.BOLD, 28);
        uiFont = new Font("Arial", Font.BOLD, 18);
        smallFont = new Font("Arial", Font.PLAIN, 14);
    }
    
    public void begin(Graphics2D g2d, int cameraX, int cameraY) {
        this.g2d = g2d;
        this.cameraX = cameraX;
        this.cameraY = cameraY;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
    
    public void fillRect(double x, double y, int width, int height, Color color) {
        g2d.setColor(color);
        g2d.fillRect((int)(x - cameraX), (int)(y - cameraY), width, height);
    }
    
    public void fillRectScreen(int x, int y, int width, int height, Color color) {
        g2d.setColor(color);
        g2d.fillRect(x, y, width, height);
    }
    
    public void drawRect(double x, double y, int width, int height, Color color) {
        g2d.setColor(color);
        g2d.drawRect((int)(x - cameraX), (int)(y - cameraY), width, height);
    }
    
    public void drawRectScreen(int x, int y, int width, int height, Color color) {
        g2d.setColor(color);
        g2d.drawRect(x, y, width, height);
    }
    
    public void fillOval(double x, double y, int width, int height, Color color) {
        g2d.setColor(color);
        g2d.fillOval((int)(x - cameraX), (int)(y - cameraY), width, height);
    }
    
    public void fillOvalScreen(int x, int y, int width, int height, Color color) {
        g2d.setColor(color);
        g2d.fillOval(x, y, width, height);
    }
    
    public void drawLine(double x1, double y1, double x2, double y2, Color color) {
        g2d.setColor(color);
        g2d.drawLine((int)(x1 - cameraX), (int)(y1 - cameraY), 
                     (int)(x2 - cameraX), (int)(y2 - cameraY));
    }
    
    public void drawLineScreen(int x1, int y1, int x2, int y2, Color color) {
        g2d.setColor(color);
        g2d.drawLine(x1, y1, x2, y2);
    }
    
    public void drawTitleText(String text, int x, int y, Color color) {
        g2d.setFont(titleFont);
        g2d.setColor(color);
        g2d.drawString(text, x, y);
    }
    
    public void drawTitleTextCentered(String text, int y, Color color) {
        g2d.setFont(titleFont);
        FontMetrics fm = g2d.getFontMetrics();
        int x = (Constants.SCREEN_WIDTH - fm.stringWidth(text)) / 2;
        g2d.setColor(color);
        g2d.drawString(text, x, y);
    }
    
    public void drawMenuText(String text, int x, int y, Color color) {
        g2d.setFont(menuFont);
        g2d.setColor(color);
        g2d.drawString(text, x, y);
    }
    
    public void drawMenuTextCentered(String text, int y, Color color) {
        g2d.setFont(menuFont);
        FontMetrics fm = g2d.getFontMetrics();
        int x = (Constants.SCREEN_WIDTH - fm.stringWidth(text)) / 2;
        g2d.setColor(color);
        g2d.drawString(text, x, y);
    }
    
    public void drawUIText(String text, int x, int y, Color color) {
        g2d.setFont(uiFont);
        g2d.setColor(color);
        g2d.drawString(text, x, y);
    }
    
    public void drawSmallText(String text, int x, int y, Color color) {
        g2d.setFont(smallFont);
        g2d.setColor(color);
        g2d.drawString(text, x, y);
    }
    
    public void drawSmallTextCentered(String text, int y, Color color) {
        g2d.setFont(smallFont);
        FontMetrics fm = g2d.getFontMetrics();
        int x = (Constants.SCREEN_WIDTH - fm.stringWidth(text)) / 2;
        g2d.setColor(color);
        g2d.drawString(text, x, y);
    }
    
    public void drawHPBar(int x, int y, int width, int height, int currentHP, int maxHP, 
                          Color barColor, Color bgColor) {
        g2d.setColor(bgColor);
        g2d.fillRect(x, y, width, height);
        
        int hpWidth = (int)((double) currentHP / maxHP * width);
        g2d.setColor(barColor);
        g2d.fillRect(x, y, hpWidth, height);
        
        g2d.setColor(Color.WHITE);
        g2d.drawRect(x, y, width, height);
    }
    
    public void drawHPBarWorld(double worldX, double worldY, int width, int height,
                               int currentHP, int maxHP, Color barColor, Color bgColor) {
        int screenX = (int)(worldX - cameraX);
        int screenY = (int)(worldY - cameraY);
        drawHPBar(screenX, screenY, width, height, currentHP, maxHP, barColor, bgColor);
    }
    
    public void fillBackground(Color color) {
        g2d.setColor(color);
        g2d.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    }
    
    public void drawCharacterSilhouette(int x, int y, int width, int height, Color color) {
        g2d.setColor(color);
        
        int headSize = width / 2;
        int headX = x + (width - headSize) / 2;
        int headY = y;
        g2d.fillOval(headX, headY, headSize, headSize);
        
        int bodyWidth = width - 10;
        int bodyHeight = height - headSize - 20;
        int bodyX = x + (width - bodyWidth) / 2;
        int bodyY = y + headSize;
        g2d.fillRect(bodyX, bodyY, bodyWidth, bodyHeight);
        
        int legWidth = bodyWidth / 3;
        int legHeight = 20;
        int legY = bodyY + bodyHeight;
        g2d.fillRect(bodyX, legY, legWidth, legHeight);
        g2d.fillRect(bodyX + bodyWidth - legWidth, legY, legWidth, legHeight);
    }
    
    public void drawSelectionArrow(int x, int y, Color color) {
        g2d.setColor(color);
        int[] xPoints = {x, x + 20, x};
        int[] yPoints = {y, y + 10, y + 20};
        g2d.fillPolygon(xPoints, yPoints, 3);
    }
    
    public Graphics2D getGraphics() {
        return g2d;
    }
    
    public int getCameraX() {
        return cameraX;
    }
    
    public int getCameraY() {
        return cameraY;
    }
    
    public Font getTitleFont() {
        return titleFont;
    }
    
    public Font getMenuFont() {
        return menuFont;
    }
    
    public Font getUIFont() {
        return uiFont;
    }
    
    public Font getSmallFont() {
        return smallFont;
    }
}