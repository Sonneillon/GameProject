package src.entity;

import src.combat.Attack;
import src.core.Constants;
import java.awt.*;

public abstract class LivingEntity extends Entity {
    public int maxHp, hp;
    public int hitstun, invincibility, hitFlash;
    public boolean onGround = true, dead;
    public double groundY;
    public Attack attack;
    public Color color;
    
    public LivingEntity(double x, double y, int width, int height, int hp, Color color) {
        super(x, y, width, height);
        this.maxHp = hp;
        this.hp = hp;
        this.groundY = y;
        this.color = color;
    }
    
    @Override
    public void update() {
        if (dead) return;
        
        if (hitstun > 0) hitstun--;
        if (invincibility > 0) invincibility--;
        if (hitFlash > 0) hitFlash--;
        
        if (attack != null) {
            attack.update();
            if (attack.isFinished()) attack = null;
        }
        
        if (!onGround) {
            velY += Constants.GRAVITY;
            y += velY;
            if (y >= groundY) {
                y = groundY;
                velY = 0;
                onGround = true;
                onLand();
            }
        }
        
        if (hitstun > 0) {
            x += velX;
            velX *= Constants.KNOCKBACK_FRICTION;
        }
        
        updateHitbox();
    }
    
    protected void onLand() {}
    
    public void takeHit(Attack atk, int attackerFacing) {
        if (invincibility > 0 || dead) return;
        
        hp -= atk.damage;
        hitstun = Constants.HITSTUN_DURATION;
        hitFlash = Constants.HIT_FLASH_DURATION;
        velX = atk.knockbackX * attackerFacing;
        
        if (atk.knockbackY != 0) {
            velY = atk.knockbackY;
            groundY = y;
            onGround = false;
        }
        
        attack = null;
        
        if (hp <= 0) {
            hp = 0;
            die();
        }
    }
    
    protected void die() {
        dead = true;
        active = false;
    }
    
    public boolean canAct() {
        return !dead && hitstun <= 0 && attack == null;
    }
    
    @Override
    public void render(Graphics2D g, int camX, int camY) {
        if (!active && !dead) return;
        
        int screenX = (int)(x - camX);
        int screenY = (int)(y - camY);
        
        Color renderColor = color;
        if (hitFlash > 0) {
            renderColor = Constants.COLOR_HIT_FLASH;
        } else if (invincibility > 0 && invincibility % 4 < 2) {
            renderColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 128);
        }
        
        renderBody(g, screenX, screenY, renderColor);
    }
    
    protected void renderBody(Graphics2D g, int screenX, int screenY, Color c) {
        g.setColor(c);
        
        // Head
        int headSize = width / 2;
        g.fillOval(screenX + (width - headSize) / 2, screenY, headSize, headSize);
        
        // Body
        int bodyW = width - 10, bodyH = height - headSize - 15;
        int bodyX = screenX + (width - bodyW) / 2, bodyY = screenY + headSize - 5;
        g.fillRect(bodyX, bodyY, bodyW, bodyH);
        
        // Legs
        int legW = bodyW / 3, legH = 15, legY = bodyY + bodyH;
        g.fillRect(bodyX + 2, legY, legW, legH);
        g.fillRect(bodyX + bodyW - legW - 2, legY, legW, legH);
        
        // Arms
        if (attack != null && attack.isActive()) {
            g.setColor(c.brighter());
            int armY = screenY + width / 2;
            if (facing > 0) g.fillRect(screenX + width, armY, 30, 12);
            else g.fillRect(screenX - 30, armY, 30, 12);
        } else {
            g.setColor(c.darker());
            int armH = height / 3, armY = screenY + width / 2;
            g.fillRect(screenX - 3, armY, 8, armH);
            g.fillRect(screenX + width - 5, armY, 8, armH);
        }
    }
}