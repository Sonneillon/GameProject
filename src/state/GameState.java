package src.state;

import src.core.Input;
import java.awt.Graphics2D;

public interface GameState {
    void enter();
    void exit();
    void update(Input input);
    void render(Graphics2D g);
    GameState nextState();
    boolean shouldTransition();
}