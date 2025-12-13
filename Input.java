import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class Input implements KeyListener {
    private final Set<Integer> pressedKeys;
    private final Set<Integer> justPressedKeys;
    private final Set<Integer> justReleasedKeys;
    private final Set<Integer> consumedKeys;
    
    public Input() {
        pressedKeys = new HashSet<>();
        justPressedKeys = new HashSet<>();
        justReleasedKeys = new HashSet<>();
        consumedKeys = new HashSet<>();
    }
    
    public synchronized void update() {
        justPressedKeys.clear();
        justReleasedKeys.clear();
        consumedKeys.clear();
    }
    
    @Override
    public synchronized void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (!pressedKeys.contains(code)) {
            justPressedKeys.add(code);
        }
        pressedKeys.add(code);
    }
    
    @Override
    public synchronized void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        pressedKeys.remove(code);
        justReleasedKeys.add(code);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    public synchronized boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }
    
    public synchronized boolean isKeyJustPressed(int keyCode) {
        return justPressedKeys.contains(keyCode) && !consumedKeys.contains(keyCode);
    }
    
    public synchronized boolean isKeyJustReleased(int keyCode) {
        return justReleasedKeys.contains(keyCode);
    }
    
    public synchronized void consumeKey(int keyCode) {
        consumedKeys.add(keyCode);
    }
    
    public boolean isUpPressed() {
        return isKeyPressed(KeyEvent.VK_UP);
    }
    
    public boolean isDownPressed() {
        return isKeyPressed(KeyEvent.VK_DOWN);
    }
    
    public boolean isLeftPressed() {
        return isKeyPressed(KeyEvent.VK_LEFT);
    }
    
    public boolean isRightPressed() {
        return isKeyPressed(KeyEvent.VK_RIGHT);
    }
    
    public boolean isAttackJustPressed() {
        return isKeyJustPressed(KeyEvent.VK_Z);
    }
    
    public boolean isHeavyAttackJustPressed() {
        return isKeyJustPressed(KeyEvent.VK_X);
    }
    
    public boolean isJumpJustPressed() {
        return isKeyJustPressed(KeyEvent.VK_C);
    }
    
    public boolean isJumpAttackJustPressed() {
        return isKeyJustPressed(KeyEvent.VK_V);
    }
    
    public boolean isConfirmJustPressed() {
        return isKeyJustPressed(KeyEvent.VK_ENTER);
    }
    
    public boolean isEscapeJustPressed() {
        return isKeyJustPressed(KeyEvent.VK_ESCAPE);
    }
    
    public boolean isRestartJustPressed() {
        return isKeyJustPressed(KeyEvent.VK_R);
    }
    
    public boolean isUpJustPressed() {
        return isKeyJustPressed(KeyEvent.VK_UP);
    }
    
    public boolean isDownJustPressed() {
        return isKeyJustPressed(KeyEvent.VK_DOWN);
    }
    
    public boolean isLeftJustPressed() {
        return isKeyJustPressed(KeyEvent.VK_LEFT);
    }
    
    public boolean isRightJustPressed() {
        return isKeyJustPressed(KeyEvent.VK_RIGHT);
    }
}