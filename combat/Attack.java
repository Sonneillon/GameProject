package combat;

public class Attack {
    public enum Type {
        LIGHT,
        HEAVY,
        JUMP_ATTACK,
        ENEMY_ATTACK,
        BOSS_ATTACK
    }
    
    private final Type type;
    private final int startup;
    private final int active;
    private final int recovery;
    private final int damage;
    private final double knockbackX;
    private final double knockbackY;
    private final int hitboxWidth;
    private final int hitboxHeight;
    private final int hitboxOffsetX;
    private final int hitboxOffsetY;
    
    private int currentFrame;
    private boolean hasHit;
    private int comboIndex;
    
    public Attack(Type type, int startup, int active, int recovery, int damage,
                  double knockbackX, double knockbackY, int hitboxWidth, int hitboxHeight,
                  int hitboxOffsetX, int hitboxOffsetY) {
        this.type = type;
        this.startup = startup;
        this.active = active;
        this.recovery = recovery;
        this.damage = damage;
        this.knockbackX = knockbackX;
        this.knockbackY = knockbackY;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        this.hitboxOffsetX = hitboxOffsetX;
        this.hitboxOffsetY = hitboxOffsetY;
        this.currentFrame = 0;
        this.hasHit = false;
        this.comboIndex = 0;
    }
    
    public Attack(Attack other) {
        this.type = other.type;
        this.startup = other.startup;
        this.active = other.active;
        this.recovery = other.recovery;
        this.damage = other.damage;
        this.knockbackX = other.knockbackX;
        this.knockbackY = other.knockbackY;
        this.hitboxWidth = other.hitboxWidth;
        this.hitboxHeight = other.hitboxHeight;
        this.hitboxOffsetX = other.hitboxOffsetX;
        this.hitboxOffsetY = other.hitboxOffsetY;
        this.currentFrame = 0;
        this.hasHit = false;
        this.comboIndex = other.comboIndex;
    }
    
    public void update() {
        currentFrame++;
    }
    
    public boolean isInStartup() {
        return currentFrame < startup;
    }
    
    public boolean isActive() {
        return currentFrame >= startup && currentFrame < startup + active;
    }
    
    public boolean isInRecovery() {
        return currentFrame >= startup + active && currentFrame < getTotalFrames();
    }
    
    public boolean isFinished() {
        return currentFrame >= getTotalFrames();
    }
    
    public int getTotalFrames() {
        return startup + active + recovery;
    }
    
    public Type getType() {
        return type;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public double getKnockbackX() {
        return knockbackX;
    }
    
    public double getKnockbackY() {
        return knockbackY;
    }
    
    public int getHitboxWidth() {
        return hitboxWidth;
    }
    
    public int getHitboxHeight() {
        return hitboxHeight;
    }
    
    public int getHitboxOffsetX() {
        return hitboxOffsetX;
    }
    
    public int getHitboxOffsetY() {
        return hitboxOffsetY;
    }
    
    public boolean hasHit() {
        return hasHit;
    }
    
    public void setHasHit(boolean hasHit) {
        this.hasHit = hasHit;
    }
    
    public int getComboIndex() {
        return comboIndex;
    }
    
    public void setComboIndex(int comboIndex) {
        this.comboIndex = comboIndex;
    }
    
    public int getCurrentFrame() {
        return currentFrame;
    }
    
    public int getStartup() {
        return startup;
    }
    
    public int getActive() {
        return active;
    }
    
    public int getRecovery() {
        return recovery;
    }
    
    public static Attack createLightAttack(int damage, double speedMod, int comboIndex) {
        int startup = (int)(5 * speedMod);
        int active = (int)(6 * speedMod);
        int recovery = (int)(8 * speedMod);
        double kbX = 4.0 + (comboIndex * 2);
        double kbY = comboIndex == 2 ? -3.0 : 0;
        
        Attack attack = new Attack(Type.LIGHT, startup, active, recovery, damage,
                kbX, kbY, 50, 40, 30, -10);
        attack.setComboIndex(comboIndex);
        return attack;
    }
    
    public static Attack createHeavyAttack(int damage, double speedMod) {
        int startup = (int)(15 * speedMod);
        int active = (int)(10 * speedMod);
        int recovery = (int)(20 * speedMod);
        
        return new Attack(Type.HEAVY, startup, active, recovery, (int)(damage * 2),
                12.0, -2.0, 70, 50, 35, -15);
    }
    
    public static Attack createJumpAttack(int damage, double speedMod) {
        int startup = (int)(4 * speedMod);
        int active = (int)(12 * speedMod);
        int recovery = (int)(6 * speedMod);
        
        return new Attack(Type.JUMP_ATTACK, startup, active, recovery, (int)(damage * 1.2),
                6.0, 2.0, 55, 50, 25, 0);
    }
    
    public static Attack createEnemyAttack(int damage) {
        return new Attack(Type.ENEMY_ATTACK, 10, 8, 15, damage,
                5.0, 0, 45, 35, 25, -5);
    }
    
    public static Attack createBossAttack(int damage) {
        return new Attack(Type.BOSS_ATTACK, 20, 12, 25, damage,
                10.0, -3.0, 80, 60, 40, -20);
    }
    
    public static Attack createBossChargeAttack(int damage) {
        return new Attack(Type.BOSS_ATTACK, 30, 15, 35, (int)(damage * 1.5),
                15.0, -5.0, 100, 70, 50, -25);
    }
}