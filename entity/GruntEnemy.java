package entity;

import combat.Attack;
import Constants;
import java.awt.Color;

public class GruntEnemy extends Enemy {
    
    private static final Color GRUNT_COLOR = new Color(180, 80, 80);
    
    public GruntEnemy(double x, double y) {
        super(x, y, Constants.GRUNT_WIDTH, Constants.GRUNT_HEIGHT, 
              Constants.GRUNT_HP, GRUNT_COLOR, Constants.GRUNT_SPEED, 100);
    }
    
    @Override
    protected void performAttack() {
        currentAttack = Attack.createEnemyAttack(Constants.GRUNT_DAMAGE);
        
        if (target != null) {
            facingDirection = (target.getCenterX() > getCenterX()) ? 1 : -1;
        }
    }
    
    @Override
    protected int getAttackCooldown() {
        return Constants.ATTACK_COOLDOWN + random.nextInt(20);
    }
}