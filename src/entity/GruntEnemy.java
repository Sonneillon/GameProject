package src.entity;

import src.combat.Attack;
import src.core.Constants;
import java.awt.Color;

public class GruntEnemy extends Enemy {
    public GruntEnemy(double x, double y) {
        super(x, y, Constants.GRUNT_WIDTH, Constants.GRUNT_HEIGHT, 
              Constants.GRUNT_HP, new Color(180, 80, 80), Constants.GRUNT_SPEED, 100);
    }
    
    @Override
    protected void doAttack() {
        attack = Attack.createEnemy(Constants.GRUNT_DAMAGE);
        if (target != null) facing = (target.centerX() > centerX()) ? 1 : -1;
    }
    
    @Override
    protected int getAttackCooldown() {
        return Constants.ATTACK_COOLDOWN + random.nextInt(20);
    }
}