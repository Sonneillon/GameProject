package entity;

import combat.Attack;
import combat.CharacterType;
import Constants;
import Input;
import java.awt.Color;
import java.awt.Graphics2D;

public class Player extends LivingEntity {
    private CharacterType characterType;
    private double speed;
    private int attackPower;
    private double attackSpeedMod;
    
    private int comboCount;
    private int comboWindowTimer;
    private boolean jumpAttackUsed;
    
    private int score;
    private int comboHits;
    private int comboTimer;
    
    public Player(double x, double y, CharacterType type) {
        super(x, y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT, type.getMaxHp(), type.getColor());
        this.characterType = type;
        this.speed = type.getSpeed();
        this.attackPower = type.getAttackPower();
        this.attackSpeedMod = type.getAttackSpeedModifier();
        
        this.comboCount = 0;
        this.comboWindowTimer = 0;
        this.jumpAttackUsed = false;
        
        this.score = 0;
        this.comboHits = 0;
        this.comboTimer = 0;
    }
    
    public void handleInput(Input input) {
        if (isDead) return;
        
        if (comboWindowTimer > 0) {
            comboWindowTimer--;
            if (comboWindowTimer <= 0) {
                comboCount = 0;
            }
        }
        
        if (comboTimer > 0) {
            comboTimer--;
            if (comboTimer <= 0) {
                comboHits = 0;
            }
        }
        
        if (canAct()) {
            double moveX = 0;
            double moveY = 0;
            
            if (input.isLeftPressed()) {
                moveX = -1;
                facingDirection = -1;
            }
            if (input.isRightPressed()) {
                moveX = 1;
                facingDirection = 1;
            }
            if (input.isUpPressed()) {
                moveY = -1;
            }
            if (input.isDownPressed()) {
                moveY = 1;
            }
            
            if (isOnGround) {
                if (moveX != 0 || moveY != 0) {
                    double length = Math.sqrt(moveX * moveX + moveY * moveY);
                    moveX /= length;
                    moveY /= length;
                    
                    x += moveX * speed;
                    groundY += moveY * speed * 0.5;
                    y = groundY;
                    
                    groundY = Math.max(Constants.DEPTH_MIN, Math.min(Constants.DEPTH_MAX, groundY));
                    y = groundY;
                }
            }
            
            if (input.isJumpJustPressed() && isOnGround) {
                jump();
            }
            
            if (input.isJumpAttackJustPressed()) {
                if (!isOnGround && !jumpAttackUsed) {
                    jumpAttack();
                } else if (isOnGround) {
                    jump();
                    jumpAttack();
                }
            }
            
            if (input.isAttackJustPressed() && isOnGround) {
                lightAttack();
            }
            
            if (input.isHeavyAttackJustPressed() && isOnGround) {
                heavyAttack();
            }
        }
    }
    
    private void jump() {
        velY = Constants.JUMP_FORCE;
        groundY = y;
        isOnGround = false;
        jumpAttackUsed = false;
    }
    
    private void lightAttack() {
        if (comboWindowTimer > 0 && comboCount < 3) {
            comboCount++;
        } else {
            comboCount = 0;
        }
        
        currentAttack = Attack.createLightAttack(attackPower, attackSpeedMod, comboCount);
        comboWindowTimer = Constants.COMBO_WINDOW + currentAttack.getTotalFrames();
        
        if (comboCount >= 2) {
            comboCount = 0;
        }
    }
    
    private void heavyAttack() {
        currentAttack = Attack.createHeavyAttack(attackPower, attackSpeedMod);
        comboCount = 0;
        comboWindowTimer = 0;
    }
    
    private void jumpAttack() {
        currentAttack = Attack.createJumpAttack(attackPower, attackSpeedMod);
        jumpAttackUsed = true;
    }
    
    @Override
    protected void onLand() {
        jumpAttackUsed = false;
        if (currentAttack != null && currentAttack.getType() == Attack.Type.JUMP_ATTACK) {
            currentAttack = null;
        }
    }
    
    @Override
    protected void die() {
        super.die();
    }
    
    public void addScore(int points) {
        score += points;
    }
    
    public void registerHit() {
        comboHits++;
        comboTimer = 90;
    }
    
    public void clampToLevel(double minX, double maxX) {
        x = Math.max(minX, Math.min(maxX - width, x));
    }
    
    @Override
    public void render(Graphics2D g2d, int cameraX, int cameraY) {
        super.render(g2d, cameraX, cameraY);
        
        if (currentAttack != null && currentAttack.isInStartup() && 
            currentAttack.getType() == Attack.Type.HEAVY) {
            renderChargeEffect(g2d, cameraX, cameraY);
        }
    }
    
    private void renderChargeEffect(Graphics2D g2d, int cameraX, int cameraY) {
        int screenX = (int)(x - cameraX);
        int screenY = (int)(y - cameraY);
        
        int progress = currentAttack.getCurrentFrame();
        int maxStartup = currentAttack.getStartup();
        float alpha = (float) progress / maxStartup;
        
        Color chargeColor = new Color(255, 200, 100, (int)(alpha * 150));
        g2d.setColor(chargeColor);
        
        int size = (int)(20 + alpha * 20);
        int cx = screenX + width / 2 - size / 2;
        int cy = screenY + height / 2 - size / 2;
        g2d.fillOval(cx, cy, size, size);
    }
    
    public CharacterType getCharacterType() {
        return characterType;
    }
    
    public int getScore() {
        return score;
    }
    
    public int getComboHits() {
        return comboHits;
    }
    
    public int getComboCount() {
        return comboCount;
    }
    
    public double getSpeed() {
        return speed;
    }
}