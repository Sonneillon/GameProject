package src.entity;

import src.combat.Attack;
import src.core.Constants;
import java.awt.Color;

public class FastEnemy extends Enemy {
    private int strafeDir;
    
    public FastEnemy(double x, double y) {
        super(x, y, Constants.FAST_WIDTH, Constants.FAST_HEIGHT,
              Constants.FAST_HP, new Color(100, 150, 200), Constants.FAST_SPEED, 150);
        strafeDir = random.nextBoolean() ? 1 : -1;
    }
    
    @Override
    protected void moveTowardTarget() {
        if (target == null || !canAct()) return;
        double dx = target.centerX() - centerX();
        double horizDist = Math.abs(dx);
        
        if (horizDist > attackRange * 1.5) {
            if (dx > 0) { x += baseSpeed; facing = 1; }
            else { x -= baseSpeed; facing = -1; }
        } else if (horizDist > attackRange * 0.5) {
            groundY += strafeDir * baseSpeed * 0.4;
            groundY = Math.max(Constants.DEPTH_MIN - height, Math.min(Constants.DEPTH_MAX - height, groundY));
            y = groundY;
            facing = (dx > 0) ? 1 : -1;
            if (random.nextInt(100) < 5) strafeDir *= -1;
        }
    }
    
    @Override
    protected void doAttack() {
        attack = new Attack(Attack.Type.ENEMY, 6, 5, 10, Constants.FAST_DAMAGE, 3, 0, 40, 30, 20, -5);
        if (target != null) facing = (target.centerX() > centerX()) ? 1 : -1;
    }
    
    @Override
    protected int getAttackCooldown() {
        return (int)(Constants.ATTACK_COOLDOWN * 0.6) + random.nextInt(15);
    }
}