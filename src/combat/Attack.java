package src.combat;

public class Attack {
    public enum Type { LIGHT, HEAVY, JUMP, ENEMY, BOSS }
    
    public final Type type;
    public final int startup, active, recovery, damage;
    public final double knockbackX, knockbackY;
    public final int hitboxWidth, hitboxHeight, hitboxOffsetX, hitboxOffsetY;
    
    private int currentFrame;
    public boolean hasHit;
    
    public Attack(Type type, int startup, int active, int recovery, int damage,
                  double kbX, double kbY, int hbW, int hbH, int hbOffX, int hbOffY) {
        this.type = type;
        this.startup = startup;
        this.active = active;
        this.recovery = recovery;
        this.damage = damage;
        this.knockbackX = kbX;
        this.knockbackY = kbY;
        this.hitboxWidth = hbW;
        this.hitboxHeight = hbH;
        this.hitboxOffsetX = hbOffX;
        this.hitboxOffsetY = hbOffY;
    }
    
    public void update() { currentFrame++; }
    public boolean isActive() { return currentFrame >= startup && currentFrame < startup + active; }
    public boolean isFinished() { return currentFrame >= startup + active + recovery; }
    public boolean isInStartup() { return currentFrame < startup; }
    public int getCurrentFrame() { return currentFrame; }
    
    public static Attack createLight(int damage, double speedMod, int combo) {
        int s = (int)(5 * speedMod), a = (int)(6 * speedMod), r = (int)(8 * speedMod);
        return new Attack(Type.LIGHT, s, a, r, damage, 4.0 + combo * 2, combo == 2 ? -3 : 0, 50, 40, 30, -10);
    }
    
    public static Attack createHeavy(int damage, double speedMod) {
        return new Attack(Type.HEAVY, (int)(15*speedMod), (int)(10*speedMod), (int)(20*speedMod),
                damage * 2, 12, -2, 70, 50, 35, -15);
    }
    
    public static Attack createJump(int damage, double speedMod) {
        return new Attack(Type.JUMP, (int)(4*speedMod), (int)(12*speedMod), (int)(6*speedMod),
                (int)(damage * 1.2), 6, 2, 55, 50, 25, 0);
    }
    
    public static Attack createEnemy(int damage) {
        return new Attack(Type.ENEMY, 10, 8, 15, damage, 5, 0, 45, 35, 25, -5);
    }
    
    public static Attack createBoss(int damage) {
        return new Attack(Type.BOSS, 20, 12, 25, damage, 10, -3, 80, 60, 40, -20);
    }
    
    public static Attack createBossCharge(int damage) {
        return new Attack(Type.BOSS, 30, 15, 35, (int)(damage * 1.5), 15, -5, 100, 70, 50, -25);
    }
}
