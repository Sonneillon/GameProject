package src.entity;

import src.combat.Attack;
import src.core.Constants;
import java.awt.*;
import java.util.Random;

public abstract class Enemy extends LivingEntity {
    public enum AIState { IDLE, CHASE, ALIGN, ATTACK, REPOSITION, STUNNED }
    
    protected AIState aiState = AIState.IDLE;
    protected Player target;
    protected int attackCooldown, stateTimer;
    public final int scoreValue;
    protected double aggroRange, attackRange, baseSpeed;
    protected Random random = new Random();
    
    public Enemy(double x, double y, int w, int h, int hp, Color color, double speed, int score) {
        super(x, y, w, h, hp, color);
        this.aggroRange = Constants.AGGRO_RANGE;
        this.attackRange = Constants.ATTACK_RANGE;
        this.baseSpeed = speed;
        this.scoreValue = score;
    }
    
    public void setTarget(Player p) { target = p; }
    
    @Override
    public void update() {
        super.update();
        if (dead) return;
        
        if (attackCooldown > 0) attackCooldown--;
        if (stateTimer > 0) stateTimer--;
        
        if (hitstun > 0) { aiState = AIState.STUNNED; return; }
        if (target == null || target.dead) { aiState = AIState.IDLE; return; }
        
        runAI();
    }
    
    protected void runAI() {
        double dist = distanceToTarget();
        double depthDiff = Math.abs(depth() - target.depth());
        
        switch (aiState) {
            case IDLE -> { if (dist < aggroRange) aiState = AIState.CHASE; }
            case CHASE -> {
                if (depthDiff > Constants.DEPTH_TOLERANCE) aiState = AIState.ALIGN;
                else if (dist < attackRange && attackCooldown <= 0) aiState = AIState.ATTACK;
                else {
                    moveTowardTarget();
                    if (random.nextInt(100) < Constants.REPOSITION_CHANCE) {
                        aiState = AIState.REPOSITION;
                        stateTimer = 30 + random.nextInt(30);
                    }
                }
            }
            case ALIGN -> { alignDepth(); if (depthDiff <= Constants.DEPTH_TOLERANCE) aiState = AIState.CHASE; }
            case ATTACK -> { if (attack == null) { doAttack(); attackCooldown = getAttackCooldown(); aiState = AIState.CHASE; } }
            case REPOSITION -> { doReposition(); if (stateTimer <= 0) aiState = AIState.CHASE; }
            case STUNNED -> { if (hitstun <= 0) aiState = AIState.CHASE; }
        }
    }
    
    protected void moveTowardTarget() {
        if (target == null || !canAct()) return;
        double dx = target.centerX() - centerX();
        if (Math.abs(dx) > attackRange * 0.8) {
            if (dx > 0) { x += baseSpeed; facing = 1; }
            else { x -= baseSpeed; facing = -1; }
        }
    }
    
    protected void alignDepth() {
        if (target == null || !canAct()) return;
        double targetD = target.depth(), myD = depth(), spd = baseSpeed * 0.7;
        if (myD < targetD - Constants.DEPTH_TOLERANCE) { groundY += spd; y = groundY; }
        else if (myD > targetD + Constants.DEPTH_TOLERANCE) { groundY -= spd; y = groundY; }
        groundY = Math.max(Constants.DEPTH_MIN - height, Math.min(Constants.DEPTH_MAX - height, groundY));
        y = groundY;
    }
    
    protected void doReposition() {
        if (!canAct()) return;
        int dir = (stateTimer % 60 < 30) ? 1 : -1;
        groundY += dir * baseSpeed * 0.5;
        groundY = Math.max(Constants.DEPTH_MIN - height, Math.min(Constants.DEPTH_MAX - height, groundY));
        y = groundY;
    }
    
    protected abstract void doAttack();
    protected int getAttackCooldown() { return Constants.ATTACK_COOLDOWN; }
    
    protected double distanceToTarget() {
        if (target == null) return 9999;
        double dx = target.centerX() - centerX(), dy = target.depth() - depth();
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    @Override
    public void render(Graphics2D g, int camX, int camY) {
        if (dead) return;
        super.render(g, camX, camY);
        
        // HP bar
        if (hp < maxHp) {
            int bx = (int)(x - camX), by = (int)(y - camY - 10);
            g.setColor(Constants.COLOR_HP_BG);
            g.fillRect(bx, by, width, 5);
            g.setColor(Constants.COLOR_ENEMY_HP);
            g.fillRect(bx, by, (int)((double) hp / maxHp * width), 5);
        }
    }
    
    public void clampToSegment(double minX, double maxX) {
        x = Math.max(minX, Math.min(maxX - width, x));
    }
}