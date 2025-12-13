package entity;

import combat.Attack;
import Constants;
import java.awt.Color;
import java.awt.Graphics2D;

public class MiniBossEnemy extends Enemy {
    
    private static final Color BOSS_COLOR = new Color(120, 60, 120);
    private static final Color ENRAGED_COLOR = new Color(180, 60, 60);
    
    private boolean isEnraged;
    private int chargeTimer;
    private boolean isCharging;
    private int phaseTimer;
    
    public MiniBossEnemy(double x, double y) {
        super(x, y, Constants.MINIBOSS_WIDTH, Constants.MINIBOSS_HEIGHT,
              Constants.MINIBOSS_HP, BOSS_COLOR, Constants.MINIBOSS_SPEED, 500);
        this.isEnraged = false;
        this.chargeTimer = 0;
        this.isCharging = false;
        this.phaseTimer = 0;
        this.aggroRange = 500;
        this.attackRange = 70;
    }
    
    @Override
    public void update() {
        super.update();
        
        if (!isEnraged && currentHp <= maxHp / 2) {
            enterEnragedPhase();
        }
        
        if (chargeTimer > 0) {
            chargeTimer--;
        }
        
        if (phaseTimer > 0) {
            phaseTimer--;
        }
    }
    
    private void enterEnragedPhase() {
        isEnraged = true;
        bodyColor = ENRAGED_COLOR;
        baseSpeed *= 1.3;
        attackRange *= 1.2;
    }
    
    @Override
    protected void updateAI() {
        if (isCharging) {
            performChargeMovement();
            return;
        }
        
        double distanceToTarget = getHorizontalDistanceToTarget();
        
        if (isEnraged && chargeTimer <= 0 && distanceToTarget > 150 && 
            distanceToTarget < 400 && random.nextInt(100) < 3) {
            startCharge();
            return;
        }
        
        super.updateAI();
    }
    
    private void startCharge() {
        isCharging = true;
        chargeTimer = 120;
        phaseTimer = 60;
        
        if (target != null) {
            facingDirection = (target.getCenterX() > getCenterX()) ? 1 : -1;
        }
    }
    
    private void performChargeMovement() {
        if (phaseTimer > 40) {
            return;
        }
        
        x += facingDirection * baseSpeed * 3;
        
        if (phaseTimer <= 0) {
            isCharging = false;
            currentAttack = Attack.createBossChargeAttack(Constants.MINIBOSS_DAMAGE);
        }
        
        if (phaseTimer > 0) {
            phaseTimer--;
        }
    }
    
    @Override
    protected void performAttack() {
        if (isEnraged && random.nextInt(100) < 40) {
            currentAttack = Attack.createBossChargeAttack(Constants.MINIBOSS_DAMAGE);
        } else {
            currentAttack = Attack.createBossAttack(Constants.MINIBOSS_DAMAGE);
        }
        
        if (target != null) {
            facingDirection = (target.getCenterX() > getCenterX()) ? 1 : -1;
        }
    }
    
    @Override
    protected int getAttackCooldown() {
        int baseCooldown = (int)(Constants.ATTACK_COOLDOWN * 1.5);
        if (isEnraged) {
            baseCooldown = (int)(baseCooldown * 0.7);
        }
        return baseCooldown + random.nextInt(30);
    }
    
    @Override
    protected void moveTowardTarget() {
        if (target == null || !canAct()) return;
        
        double dx = target.getCenterX() - getCenterX();
        double speed = isEnraged ? baseSpeed * 1.2 : baseSpeed;
        
        if (Math.abs(dx) > attackRange * 0.8) {
            if (dx > 0) {
                x += speed;
                facingDirection = 1;
            } else {
                x -= speed;
                facingDirection = -1;
            }
        }
    }
    
    @Override
    public void render(Graphics2D g2d, int cameraX, int cameraY) {
        super.render(g2d, cameraX, cameraY);
        
        if (isCharging && phaseTimer > 40) {
            renderChargeWarning(g2d, cameraX, cameraY);
        }
    }
    
    private void renderChargeWarning(Graphics2D g2d, int cameraX, int cameraY) {
        int screenX = (int)(x - cameraX);
        int screenY = (int)(y - cameraY);
        
        int flash = (phaseTimer % 10 < 5) ? 255 : 100;
        g2d.setColor(new Color(255, flash, flash, 150));
        g2d.fillRect(screenX - 5, screenY - 5, width + 10, height + 10);
    }
    
    @Override
    protected void renderBody(Graphics2D g2d, int screenX, int screenY, Color color) {
        g2d.setColor(color);
        
        int headSize = (int)(width * 0.6);
        int headX = screenX + (width - headSize) / 2;
        int headY = screenY;
        g2d.fillOval(headX, headY, headSize, headSize);
        
        int bodyWidth = width;
        int bodyHeight = height - headSize - 20;
        int bodyX = screenX;
        int bodyY = screenY + headSize - 10;
        g2d.fillRect(bodyX, bodyY, bodyWidth, bodyHeight);
        
        int legWidth = bodyWidth / 3;
        int legHeight = 25;
        int legY = bodyY + bodyHeight;
        g2d.fillRect(bodyX + 5, legY, legWidth, legHeight);
        g2d.fillRect(bodyX + bodyWidth - legWidth - 5, legY, legWidth, legHeight);
        
        if (currentAttack != null && currentAttack.isActive()) {
            renderBossAttackArm(g2d, screenX, screenY, color);
        } else {
            renderBossIdleArms(g2d, screenX, screenY, color);
        }
        
        if (isEnraged) {
            g2d.setColor(new Color(255, 100, 100, 100));
            g2d.fillOval(screenX - 10, screenY - 10, width + 20, height + 20);
        }
    }
    
    private void renderBossIdleArms(Graphics2D g2d, int screenX, int screenY, Color color) {
        g2d.setColor(color.darker());
        int armWidth = 15;
        int armHeight = height / 2;
        int armY = screenY + (int)(width * 0.4);
        
        g2d.fillRect(screenX - armWidth + 3, armY, armWidth, armHeight);
        g2d.fillRect(screenX + width - 3, armY, armWidth, armHeight);
    }
    
    private void renderBossAttackArm(Graphics2D g2d, int screenX, int screenY, Color color) {
        g2d.setColor(color.brighter());
        int armWidth = 20;
        int armLength = 50;
        int armY = screenY + (int)(width * 0.4);
        
        if (facingDirection > 0) {
            g2d.fillRect(screenX + width, armY, armLength, armWidth);
        } else {
            g2d.fillRect(screenX - armLength, armY, armLength, armWidth);
        }
    }
    
    public boolean isEnraged() {
        return isEnraged;
    }
    
    public boolean isCharging() {
        return isCharging;
    }
}