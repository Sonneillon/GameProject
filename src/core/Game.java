package src.core;

import src.state.GameState;
import src.state.MenuState;

public class Game implements Runnable {
    private GamePanel panel;
    private Input input;
    private Thread thread;
    private boolean running;
    private GameState state;
    
    public Game(GamePanel panel, Input input) {
        this.panel = panel;
        this.input = input;
        this.state = new MenuState();
        this.state.enter();
        this.panel.setState(state);
    }
    
    public void start() {
        if (running) return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }
    
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double delta = 0;
        
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / Constants.NS_PER_UPDATE;
            lastTime = now;
            
            while (delta >= 1) {
                update();
                delta--;
            }
            
            render();
            
            try {
                Thread.sleep(1);
            } catch (Exception e) {}
        }
    }
    
    private void update() {
        if (state != null) {
            state.update(input);
            
            if (state instanceof MenuState menu && menu.quit) {
                System.exit(0);
            }
            
            if (state.shouldTransition()) {
                GameState next = state.nextState();
                if (next != null) {
                    state.exit();
                    state = next;
                    state.enter();
                    panel.setState(state);
                }
            }
        }
        
        input.update();
    }
    
    private void render() {
        panel.doRender();
    }
}