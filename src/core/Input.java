package src.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Input implements KeyListener {
    private final Set<Integer> pressedKeys = Collections.synchronizedSet(new HashSet<>());
    private final Set<Integer> justPressedKeys = Collections.synchronizedSet(new HashSet<>());
    
    public void update() {
        justPressedKeys.clear();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (!pressedKeys.contains(e.getKeyCode())) {
            justPressedKeys.add(e.getKeyCode());
        }
        pressedKeys.add(e.getKeyCode());
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    public boolean isPressed(int key) { return pressedKeys.contains(key); }
    public boolean isJustPressed(int key) { return justPressedKeys.contains(key); }
    
    public boolean up() { return isPressed(KeyEvent.VK_UP); }
    public boolean down() { return isPressed(KeyEvent.VK_DOWN); }
    public boolean left() { return isPressed(KeyEvent.VK_LEFT); }
    public boolean right() { return isPressed(KeyEvent.VK_RIGHT); }
    public boolean attack() { return isJustPressed(KeyEvent.VK_Z); }
    public boolean heavyAttack() { return isJustPressed(KeyEvent.VK_X); }
    public boolean jump() { return isJustPressed(KeyEvent.VK_C); }
    public boolean jumpAttack() { return isJustPressed(KeyEvent.VK_V); }
    public boolean confirm() { return isJustPressed(KeyEvent.VK_ENTER); }
    public boolean escape() { return isJustPressed(KeyEvent.VK_ESCAPE); }
    public boolean restart() { return isJustPressed(KeyEvent.VK_R); }
    public boolean upJust() { return isJustPressed(KeyEvent.VK_UP); }
    public boolean downJust() { return isJustPressed(KeyEvent.VK_DOWN); }
    public boolean leftJust() { return isJustPressed(KeyEvent.VK_LEFT); }
    public boolean rightJust() { return isJustPressed(KeyEvent.VK_RIGHT); }
}