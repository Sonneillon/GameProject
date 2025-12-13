import state.GameState;
import state.MenuState;

public class Game implements Runnable {
    private GamePanel gamePanel;
    private Input input;
    private Thread gameThread;
    private boolean running;
    
    private GameState currentState;
    
    public Game(GamePanel gamePanel, Input input) {
        this.gamePanel = gamePanel;
        this.input = input;
        this.running = false;
        
        this.currentState = new MenuState();
        this.currentState.enter();
        this.gamePanel.setCurrentState(currentState);
    }
    
    public void start() {
        if (running) return;
        
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    public void stop() {
        running = false;
        try {
            if (gameThread != null) {
                gameThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double delta = 0;
        
        long fpsTimer = System.currentTimeMillis();
        int frames = 0;
        int updates = 0;
        
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / Constants.NS_PER_UPDATE;
            lastTime = now;
            
            while (delta >= 1) {
                update();
                updates++;
                delta--;
            }
            
            render();
            frames++;
            
            if (System.currentTimeMillis() - fpsTimer >= 1000) {
                fpsTimer = System.currentTimeMillis();
                frames = 0;
                updates = 0;
            }
            
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void update() {
        if (currentState != null) {
            currentState.update(input);
            
            if (currentState instanceof MenuState) {
                MenuState menuState = (MenuState) currentState;
                if (menuState.shouldQuit()) {
                    System.exit(0);
                }
            }
            
            if (currentState.shouldTransition()) {
                GameState nextState = currentState.getNextState();
                if (nextState != null) {
                    currentState.exit();
                    currentState = nextState;
                    currentState.enter();
                    gamePanel.setCurrentState(currentState);
                }
            }
        }
        
        input.update();
    }
    
    private void render() {
        gamePanel.render();
    }
    
    public GameState getCurrentState() {
        return currentState;
    }
}