package src.entity;

import src.combat.Attack;
import src.combat.CharacterType;
import src.core.Constants;
import src.core.Input;
import java.awt.*;

public class Player extends LivingEntity {
    public final CharacterType type;
    public final double speed;
    public final int power;
    public final double speedMod;
    
    private int comboCount, comboWindow;
    private boolean jumpAttackUsed;
    public int score, hits, hitTimer;
    
    public Player(double x, double y, CharacterType type) {
        super(x, y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT, type.maxHp, type.color);
        this.type = type;
        this.speed = type.speed;
        this.power = type.attackPower;
        this.speedMod = type.attackSpeedMod;
    }
    
    public void handleInput(Input input) {
        if (dead) return;
        
        if (comboWindow > 0 && --comboWindow <= 0) comboCount = 0;
        if (hitTimer > 0) hitTimer--;
        
        if (canAct()) {
            double moveX = 0, moveY = 0;
            
            if (input.left()) { moveX = -1; facing = -1; }
            if (input.right()) { moveX = 1; facing = 1; }
            if (input.up()) moveY = -1;
            if (input.down()) moveY = 1;
            
            if (onGround && (moveX != 0 || moveY != 0)) {
                double len = Math.sqrt(moveX * moveX + moveY * moveY);
                moveX /= len;
                moveY /= len;
                x += moveX * speed;
                groundY += moveY * speed * 0.5;
                y = groundY;
                groundY = Math.max(Constants.DEPTH_MIN, Math.min(Constants.DEPTH_MAX, groundY));
                y = groundY;
            }
            
            if (input.jump() && onGround) jump();
            
            if (input.jumpAttack()) {
                if (!onGround && !jumpAttackUsed) doJumpAttack();
                else if (onGround) { jump(); doJumpAttack(); }
            }
            
            if (input.attack() && onGround) doLightAttack();
            if (input.heavyAttack() && onGround) doHeavyAttack();
        }
    }
    
    private void jump() {
        velY = Constants.JUMP_FORCE;
        groundY = y;
        onGround = false;
        jumpAttackUsed = false;
    }
    
    private void doLightAttack() {
        if (comboWindow > 0 && comboCount < 3) comboCount++;
        else comboCount = 0;
        attack = Attack.createLight(power, speedMod, comboCount);
        comboWindow = Constants.COMBO_WINDOW + 20;
        if (comboCount >= 2) comboCount = 0;
    }
    
    private void doHeavyAttack() {
        attack = Attack.createHeavy(power, speedMod);
        comboCount = 0;
        comboWindow = 0;
    }
    
    private void doJumpAttack() {
        attack = Attack.createJump(power, speedMod);
        jumpAttackUsed = true;
    }
    
    @Override
    protected void onLand() {
        jumpAttackUsed = false;
        if (attack != null && attack.type == Attack.Type.JUMP) attack = null;
    }
    
    public void addScore(int points) { score += points; }
    public void registerHit() { hits++; hitTimer = 90; }
    
    public void clamp(double minX, double maxX) {
        x = Math.max(minX, Math.min(maxX - width, x));
    }
    
    @Override
    public void render(Graphics2D g, int camX, int camY) {
        super.render(g, camX, camY);
        
        // Heavy attack charge effect
        if (attack != null && attack.isInStartup() && attack.type == Attack.Type.HEAVY) {
            int sx = (int)(x - camX), sy = (int)(y - camY);
            float progress = (float) attack.getCurrentFrame() / attack.startup;
            int size = (int)(20 + progress * 20);
            g.setColor(new Color(255, 200, 100, (int)(progress * 150)));
            g.fillOval(sx + width/2 - size/2, sy + height/2 - size/2, size, size);
        }
    }
}
