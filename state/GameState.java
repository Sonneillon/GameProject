package state;

import Input;
import Renderer;
import java.awt.Graphics2D;

public interface GameState {
    void enter();
    void exit();
    void update(Input input);
    void render(Graphics2D g2d, Renderer renderer);
    GameState getNextState();
    boolean shouldTransition();
}
