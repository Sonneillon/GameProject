package src.entity;

import src.combat.Attack;
import src.core.Constants;
import java.awt.*;

public class Boss extends Enemy {
    private boolean enraged, charging;
    private int chargeCooldown, phaseTimer;
    
    public Boss(double x, double y) {
        super(x, y, Constants.BOSS_WIDTH, Constants.BOSS_HEIGHT,
              Constants.BOSS_HP, new Color(120, 60, 120), Constants.BOSS_SPEED, 500);
        aggroRange = 500;
        attackRange = 70;
    }
    
    @Override
    public void update() {
        super.update();
        if (!enraged && hp <= maxHp / 2) {
            enraged = true;
            color = new Color(180, 60, 60);
            baseSpeed *= 1.3;
            attackRange *= 1.2;
        }
        if (chargeCooldown > 0) chargeCooldown--;
        if (phaseTimer > 0) phaseTimer--;
    }
    
    @Override
    protected void runAI() {
        if (charging) { chargeMove(); return; }
        double dist = target != null ? Math.abs(target.centerX() - centerX()) : 0;
        if (enraged && chargeCooldown <= 0 && dist > 150 && dist < 400 && random.nextInt(100) < 3) {
            startCharge();
            return;
        }
        super.runAI();
    }
    
    private void startCharge() {
        charging = true;
        chargeCooldown = 120;
        phaseTimer = 60;
        if (target != null) facing = (target.centerX() > centerX()) ? 1 : -1;
    }
    
    private void chargeMove() {
        if (phaseTimer > 40) return;
        x += facing * baseSpeed * 3;
        if (--phaseTimer <= 0) {
            charging = false;
            attack = Attack.createBossCharge(Constants.BOSS_DAMAGE);
        }
    }
    
    @Override
    protected void doAttack() {
        attack = enraged && random.nextInt(100) < 40 
            ? Attack.createBossCharge(Constants.BOSS_DAMAGE) 
            : Attack.createBoss(Constants.BOSS_DAMAGE);
        if (target != null) facing = (target.centerX() > centerX()) ? 1 : -1;
    }
    
    @Override
    protected int getAttackCooldown() {
        int base = (int)(Constants.ATTACK_COOLDOWN * 1.5);
        if (enraged) base = (int)(base * 0.7);
        return base + random.nextInt(30);
    }
    
    @Override
    protected void moveTowardTarget() {
        if (target == null || !canAct()) return;
        double dx = target.centerX() - centerX();
        double spd = enraged ? baseSpeed * 1.2 : baseSpeed;
        if (Math.abs(dx) > attackRange * 0.8) {
            if (dx > 0) { x += spd; facing = 1; }
            else { x -= spd; facing = -1; }
        }
    }
    
    @Override
    public void render(Graphics2D g, int camX, int camY) {
        super.render(g, camX, camY);
        if (charging && phaseTimer > 40) {
            int sx = (int)(x - camX), sy = (int)(y - camY);
            int flash = (phaseTimer % 10 < 5) ? 255 : 100;
            g.setColor(new Color(255, flash, flash, 150));
            g.fillRect(sx - 5, sy - 5, width + 10, height + 10);
        }
    }
    
    @Override
    protected void renderBody(Graphics2D g, int sx, int sy, Color c) {
        g.setColor(c);
        int headSize = (int)(width * 0.6);
        g.fillOval(sx + (width - headSize) / 2, sy, headSize, headSize);
        
        int bodyH = height - headSize - 20, bodyY = sy + headSize - 10;
        g.fillRect(sx, bodyY, width, bodyH);
        
        int legW = width / 3, legY = bodyY + bodyH;
        g.fillRect(sx + 5, legY, legW, 25);
        g.fillRect(sx + width - legW - 5, legY, legW, 25);
        
        if (attack != null && attack.isActive()) {
            g.setColor(c.brighter());
            int armY = sy + (int)(width * 0.4);
            if (facing > 0) g.fillRect(sx + width, armY, 50, 20);
            else g.fillRect(sx - 50, armY, 50, 20);
        } else {
            g.setColor(c.darker());
            int armY = sy + (int)(width * 0.4);
            g.fillRect(sx - 12, armY, 15, height / 2);
            g.fillRect(sx + width - 3, armY, 15, height / 2);
        }
        
        if (enraged) {
            g.setColor(new Color(255, 100, 100, 100));
            g.fillOval(sx - 10, sy - 10, width + 20, height + 20);
        }
    }
}