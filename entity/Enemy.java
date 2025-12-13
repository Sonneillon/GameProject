package entity;

import combat.Attack;
import Constants;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public abstract class Enemy extends LivingEntity {
    public enum AIState {
        IDLE,
        CHASE,
        ALIGN_DEPTH,
        ATTACK,
        REPOSITION,
        STUNNED
    }
    
    protected AIState aiState;
    protected Player target;
    protected int attackCooldown;
    protected int stateTimer;
    protected int scoreValue;
    protected double aggroRange;
    protected double attackRange;
    protected double baseSpeed;
    protected Random random;
    
    public Enemy(double x, double y, int width, int height, int maxHp, 
                 Color color, double speed, int scoreValue) {
        super(x, y, width, height, maxHp, color);
        this.aiState = AIState.IDLE;
        this.target = null;
        this.attackCooldown = 0;
        this.stateTimer = 0;
        this.scoreValue = scoreValue;
        this.aggroRange = Constants.AGGRO_RANGE;
        this.attackRange = Constants.ATTACK_RANGE;
        this.baseSpeed = speed;
        this.random = new Random();
    }
    
    public void setTarget(Player player) {
        this.target = player;
    }
    
    @Override
    public void update() {
        super.update();
        
        if (isDead) return;
        
        if (attackCooldown > 0) {
            attackCooldown--;
        }
        
        if (stateTimer > 0) {
            stateTimer--;
        }
        
        if (hitstunTimer > 0) {
            aiState = AIState.STUNNED;
            return;
        }
        
        if (target == null || target.isDead()) {
            aiState = AIState.IDLE;
            return;
        }
        
        updateAI();
    }
    
    protected void updateAI() {
        double distanceToTarget = getDistanceToTarget();
        double depthDiff = Math.abs(getDepth() - target.getDepth());
        
        switch (aiState) {
            case IDLE:
                if (distanceToTarget < aggroRange) {
                    aiState = AIState.CHASE;
                }
                break;
                
            case CHASE:
                if (depthDiff > Constants.DEPTH_TOLERANCE) {
                    aiState = AIState.ALIGN_DEPTH;
                } else if (distanceToTarget < attackRange && attackCooldown <= 0) {
                    aiState = AIState.ATTACK;
                } else {
                    moveTowardTarget();
                    
                    if (random.nextInt(100) < Constants.REPOSITION_CHANCE) {
                        aiState = AIState.REPOSITION;
                        stateTimer = 30 + random.nextInt(30);
                    }
                }
                break;
                
            case ALIGN_DEPTH:
                alignDepthWithTarget();
                if (depthDiff <= Constants.DEPTH_TOLERANCE) {
                    aiState = AIState.CHASE;
                }
                break;
                
            case ATTACK:
                if (!isAttacking()) {
                    performAttack();
                    attackCooldown = getAttackCooldown();
                    aiState = AIState.CHASE;
                }
                break;
                
            case REPOSITION:
                performReposition();
                if (stateTimer <= 0) {
                    aiState = AIState.CHASE;
                }
                break;
                
            case STUNNED:
                if (hitstunTimer <= 0) {
                    aiState = AIState.CHASE;
                }
                break;
        }
    }
    
    protected void moveTowardTarget() {
        if (target == null || !canAct()) return;
        
        double dx = target.getCenterX() - getCenterX();
        if (Math.abs(dx) > attackRange * 0.8) {
            if (dx > 0) {
                x += baseSpeed;
                facingDirection = 1;
            } else {
                x -= baseSpeed;
                facingDirection = -1;
            }
        }
    }
    
    protected void alignDepthWithTarget() {
        if (target == null || !canAct()) return;
        
        double targetDepth = target.getDepth();
        double myDepth = getDepth();
        double depthSpeed = baseSpeed * 0.7;
        
        if (myDepth < targetDepth - Constants.DEPTH_TOLERANCE) {
            groundY += depthSpeed;
            y = groundY;
        } else if (myDepth > targetDepth + Constants.DEPTH_TOLERANCE) {
            groundY -= depthSpeed;
            y = groundY;
        }
        
        groundY = Math.max(Constants.DEPTH_MIN - height, Math.min(Constants.DEPTH_MAX - height, groundY));
        y = groundY;
    }
    
    protected void performReposition() {
        if (!canAct()) return;
        
        int direction = (stateTimer % 60 < 30) ? 1 : -1;
        groundY += direction * baseSpeed * 0.5;
        groundY = Math.max(Constants.DEPTH_MIN - height, Math.min(Constants.DEPTH_MAX - height, groundY));
        y = groundY;
    }
    
    protected abstract void performAttack();
    
    protected int getAttackCooldown() {
        return Constants.ATTACK_COOLDOWN;
    }
    
    protected double getDistanceToTarget() {
        if (target == null) return Double.MAX_VALUE;
        double dx = target.getCenterX() - getCenterX();
        double dy = target.getDepth() - getDepth();
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    protected double getHorizontalDistanceToTarget() {
        if (target == null) return Double.MAX_VALUE;
        return Math.abs(target.getCenterX() - getCenterX());
    }
    
    @Override
    protected void die() {
        super.die();
    }
    
    @Override
    public void render(Graphics2D g2d, int cameraX, int cameraY) {
        if (isDead) {
            renderDeathEffect(g2d, cameraX, cameraY);
            return;
        }
        super.render(g2d, cameraX, cameraY);
        
        renderHealthBar(g2d, cameraX, cameraY);
    }
    
    protected void renderHealthBar(Graphics2D g2d, int cameraX, int cameraY) {
        if (currentHp >= maxHp) return;
        
        int barWidth = width;
        int barHeight = 5;
        int barX = (int)(x - cameraX);
        int barY = (int)(y - cameraY - 10);
        
        g2d.setColor(Constants.COLOR_HP_BG);
        g2d.fillRect(barX, barY, barWidth, barHeight);
        
        int hpWidth = (int)((double) currentHp / maxHp * barWidth);
        g2d.setColor(Constants.COLOR_ENEMY_HP);
        g2d.fillRect(barX, barY, hpWidth, barHeight);
    }
    
    protected void renderDeathEffect(Graphics2D g2d, int cameraX, int cameraY) {
    }
    
    public int getScoreValue() {
        return scoreValue;
    }
    
    public AIState getAIState() {
        return aiState;
    }
    
    public void clampToSegment(double minX, double maxX) {
        x = Math.max(minX, Math.min(maxX - width, x));
    }
}