import state.GameState;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    private BufferedImage buffer;
    private Graphics2D bufferGraphics;
    private Renderer renderer;
    private GameState currentState;
    
    public GamePanel() {
        setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);
        
        buffer = new BufferedImage(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, 
                                   BufferedImage.TYPE_INT_ARGB);
        bufferGraphics = buffer.createGraphics();
        renderer = new Renderer();
    }
    
    public void setCurrentState(GameState state) {
        this.currentState = state;
    }
    
    public void render() {
        if (currentState != null) {
            currentState.render(bufferGraphics, renderer);
        }
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buffer, 0, 0, null);
    }
    
    public Renderer getRenderer() {
        return renderer;
    }
}