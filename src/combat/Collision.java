package src.combat;

import src.core.Constants;

public final class Collision {
    private Collision() {}
    
    public static boolean checkAABB(Hitbox a, Hitbox b) {
        if (a == null || b == null || !a.active || !b.active) return false;
        return a.x < b.right() && a.right() > b.x && a.y < b.bottom() && a.bottom() > b.y;
    }
    
    public static boolean checkDepth(double d1, double d2) {
        return Math.abs(d1 - d2) <= Constants.DEPTH_TOLERANCE;
    }
    
    public static boolean checkHit(Hitbox attack, Hitbox target, double attackDepth, double targetDepth) {
        return checkDepth(attackDepth, targetDepth) && checkAABB(attack, target);
    }
    
    public static Hitbox createAttackHitbox(double entityX, double entityY, int entityWidth, int facing, Attack attack) {
        double hx = facing > 0 
            ? entityX + entityWidth / 2.0 + attack.hitboxOffsetX
            : entityX + entityWidth / 2.0 - attack.hitboxOffsetX - attack.hitboxWidth;
        return new Hitbox(hx, entityY + attack.hitboxOffsetY, attack.hitboxWidth, attack.hitboxHeight);
    }
}