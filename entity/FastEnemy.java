package entity;

import combat.Attack;
import Constants;
import java.awt.Color;

public class FastEnemy extends Enemy {
    
    private static final Color FAST_COLOR = new Color(100, 150, 200);
    private int strafeDirection;
    private int strafeTimer;
    
    public FastEnemy(double x, double y) {
        super(x, y, Constants.FAST_ENEMY_WIDTH, Constants.FAST_ENEMY_HEIGHT,
              Constants.FAST_ENEMY_HP, FAST_COLOR, Constants.FAST_ENEMY_SPEED, 150);
        this.strafeDirection = random.nextBoolean() ? 1 : -1;
        this.strafeTimer = 0;
    }
    
    @Override
    protected void updateAI() {
        if (strafeTimer > 0) {
            strafeTimer--;
        }
        
        super.updateAI();
    }
    
    @Override
    protected void moveTowardTarget() {
        if (target == null || !canAct()) return;
        
        double dx = target.getCenterX() - getCenterX();
        double horizontalDist = Math.abs(dx);
        
        if (horizontalDist > attackRange * 1.5) {
            if (dx > 0) {
                x += baseSpeed;
                facingDirection = 1;
            } else {
                x -= baseSpeed;
                facingDirection = -1;
            }
        } else if (horizontalDist > attackRange * 0.5) {
            if (strafeTimer <= 0 && random.nextInt(100) < 5) {
                strafeDirection *= -1;
                strafeTimer = 20;
            }
            
            groundY += strafeDirection * baseSpeed * 0.4;
            groundY = Math.max(Constants.DEPTH_MIN - height, Math.min(Constants.DEPTH_MAX - height, groundY));
            y = groundY;
            
            facingDirection = (dx > 0) ? 1 : -1;
        }
    }
    
    @Override
    protected void performAttack() {
        int damage = Constants.FAST_ENEMY_DAMAGE;
        currentAttack = new Attack(Attack.Type.ENEMY_ATTACK, 6, 5, 10, damage,
                3.0, 0, 40, 30, 20, -5);
        
        if (target != null) {
            facingDirection = (target.getCenterX() > getCenterX()) ? 1 : -1;
        }
    }
    
    @Override
    protected int getAttackCooldown() {
        return (int)(Constants.ATTACK_COOLDOWN * 0.6) + random.nextInt(15);
    }
    
    @Override
    protected void performReposition() {
        if (!canAct()) return;
        
        x += strafeDirection * baseSpeed * 0.8;
        
        if (random.nextInt(100) < 10) {
            strafeDirection *= -1;
        }
    }
}