package entity;

import combat.Attack;
import combat.Hitbox;
import Constants;
import java.awt.Color;
import java.awt.Graphics2D;

public abstract class LivingEntity extends Entity {
    protected int maxHp;
    protected int currentHp;
    protected int hitstunTimer;
    protected int invincibilityTimer;
    protected int hitFlashTimer;
    protected boolean isOnGround;
    protected boolean isDead;
    protected double groundY;
    protected Attack currentAttack;
    protected Color bodyColor;
    
    public LivingEntity(double x, double y, int width, int height, int maxHp, Color bodyColor) {
        super(x, y, width, height);
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.hitstunTimer = 0;
        this.invincibilityTimer = 0;
        this.hitFlashTimer = 0;
        this.isOnGround = true;
        this.isDead = false;
        this.groundY = y;
        this.currentAttack = null;
        this.bodyColor = bodyColor;
    }
    
    @Override
    public void update() {
        if (isDead) return;
        
        if (hitstunTimer > 0) {
            hitstunTimer--;
        }
        
        if (invincibilityTimer > 0) {
            invincibilityTimer--;
        }
        
        if (hitFlashTimer > 0) {
            hitFlashTimer--;
        }
        
        if (currentAttack != null) {
            currentAttack.update();
            if (currentAttack.isFinished()) {
                currentAttack = null;
            }
        }
        
        if (!isOnGround) {
            velY += Constants.GRAVITY;
            y += velY;
            
            if (y >= groundY) {
                y = groundY;
                velY = 0;
                isOnGround = true;
                onLand();
            }
        }
        
        if (hitstunTimer > 0) {
            x += velX;
            velX *= Constants.KNOCKBACK_FRICTION;
        }
        
        updateHurtbox();
    }
    
    protected void onLand() {
    }
    
    public void takeDamage(int damage, double knockbackX, double knockbackY) {
        if (invincibilityTimer > 0 || isDead) return;
        
        currentHp -= damage;
        hitstunTimer = Constants.HITSTUN_DURATION;
        hitFlashTimer = Constants.HIT_FLASH_DURATION;
        
        velX = knockbackX * facingDirection * -1;
        if (knockbackY != 0) {
            velY = knockbackY;
            groundY = y;
            isOnGround = false;
        }
        
        if (currentAttack != null) {
            currentAttack = null;
        }
        
        if (currentHp <= 0) {
            currentHp = 0;
            die();
        }
    }
    
    public void takeHit(Attack attack, int attackerFacing) {
        if (invincibilityTimer > 0 || isDead) return;
        
        currentHp -= attack.getDamage();
        hitstunTimer = Constants.HITSTUN_DURATION;
        hitFlashTimer = Constants.HIT_FLASH_DURATION;
        
        velX = attack.getKnockbackX() * attackerFacing;
        if (attack.getKnockbackY() != 0) {
            velY = attack.getKnockbackY();
            groundY = y;
            isOnGround = false;
        }
        
        if (currentAttack != null) {
            currentAttack = null;
        }
        
        if (currentHp <= 0) {
            currentHp = 0;
            die();
        }
    }
    
    protected void die() {
        isDead = true;
        active = false;
    }
    
    public void heal(int amount) {
        currentHp = Math.min(maxHp, currentHp + amount);
    }
    
    public void setInvincible(int frames) {
        invincibilityTimer = frames;
    }
    
    public boolean isInHitstun() {
        return hitstunTimer > 0;
    }
    
    public boolean isInvincible() {
        return invincibilityTimer > 0;
    }
    
    public boolean isAttacking() {
        return currentAttack != null;
    }
    
    public boolean canAct() {
        return !isDead && hitstunTimer <= 0 && !isAttacking();
    }
    
    public boolean isHitFlashing() {
        return hitFlashTimer > 0;
    }
    
    @Override
    public void render(Graphics2D g2d, int cameraX, int cameraY) {
        if (!active && !isDead) return;
        
        int screenX = (int)(x - cameraX);
        int screenY = (int)(y - cameraY);
        
        Color renderColor = bodyColor;
        if (hitFlashTimer > 0) {
            renderColor = Constants.COLOR_HIT_FLASH;
        } else if (invincibilityTimer > 0 && invincibilityTimer % 4 < 2) {
            renderColor = new Color(bodyColor.getRed(), bodyColor.getGreen(), 
                                   bodyColor.getBlue(), 128);
        }
        
        renderBody(g2d, screenX, screenY, renderColor);
    }
    
    protected void renderBody(Graphics2D g2d, int screenX, int screenY, Color color) {
        g2d.setColor(color);
        
        int headSize = width / 2;
        int headX = screenX + (width - headSize) / 2;
        int headY = screenY;
        g2d.fillOval(headX, headY, headSize, headSize);
        
        int bodyWidth = width - 10;
        int bodyHeight = height - headSize - 15;
        int bodyX = screenX + (width - bodyWidth) / 2;
        int bodyY = screenY + headSize - 5;
        g2d.fillRect(bodyX, bodyY, bodyWidth, bodyHeight);
        
        int legWidth = bodyWidth / 3;
        int legHeight = 15;
        int legY = bodyY + bodyHeight;
        g2d.fillRect(bodyX + 2, legY, legWidth, legHeight);
        g2d.fillRect(bodyX + bodyWidth - legWidth - 2, legY, legWidth, legHeight);
        
        if (currentAttack != null && currentAttack.isActive()) {
            renderAttackArm(g2d, screenX, screenY, color);
        } else {
            renderIdleArms(g2d, screenX, screenY, color);
        }
    }
    
    protected void renderIdleArms(Graphics2D g2d, int screenX, int screenY, Color color) {
        g2d.setColor(color.darker());
        int armWidth = 8;
        int armHeight = height / 3;
        int armY = screenY + width / 2;
        
        g2d.fillRect(screenX - armWidth + 5, armY, armWidth, armHeight);
        g2d.fillRect(screenX + width - 5, armY, armWidth, armHeight);
    }
    
    protected void renderAttackArm(Graphics2D g2d, int screenX, int screenY, Color color) {
        g2d.setColor(color.brighter());
        int armWidth = 12;
        int armHeight = 30;
        int armY = screenY + width / 2;
        
        if (facingDirection > 0) {
            g2d.fillRect(screenX + width, armY, armHeight, armWidth);
        } else {
            g2d.fillRect(screenX - armHeight, armY, armHeight, armWidth);
        }
    }
    
    public int getMaxHp() {
        return maxHp;
    }
    
    public int getCurrentHp() {
        return currentHp;
    }
    
    public void setCurrentHp(int hp) {
        this.currentHp = Math.max(0, Math.min(maxHp, hp));
    }
    
    public boolean isDead() {
        return isDead;
    }
    
    public boolean isOnGround() {
        return isOnGround;
    }
    
    public Attack getCurrentAttack() {
        return currentAttack;
    }
    
    public Color getBodyColor() {
        return bodyColor;
    }
    
    public double getGroundY() {
        return groundY;
    }
    
    public void setGroundY(double groundY) {
        this.groundY = groundY;
    }
}