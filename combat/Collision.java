package combat;

import Constants;

public final class Collision {
    
    private Collision() {}
    
    public static boolean checkAABB(Hitbox a, Hitbox b) {
        if (a == null || b == null || !a.isActive() || !b.isActive()) {
            return false;
        }
        
        return a.getX() < b.getRight() &&
               a.getRight() > b.getX() &&
               a.getY() < b.getBottom() &&
               a.getBottom() > b.getY();
    }
    
    public static boolean checkDepthOverlap(double depthA, double depthB) {
        return Math.abs(depthA - depthB) <= Constants.DEPTH_TOLERANCE;
    }
    
    public static boolean checkHit(Hitbox attackHitbox, Hitbox targetHurtbox, 
                                   double attackerDepth, double targetDepth) {
        if (!checkDepthOverlap(attackerDepth, targetDepth)) {
            return false;
        }
        return checkAABB(attackHitbox, targetHurtbox);
    }
    
    public static Hitbox createAttackHitbox(double entityX, double entityY, 
                                            int entityWidth, int facingDirection,
                                            Attack attack) {
        int offsetX = attack.getHitboxOffsetX();
        int offsetY = attack.getHitboxOffsetY();
        int hitWidth = attack.getHitboxWidth();
        int hitHeight = attack.getHitboxHeight();
        
        double hitX;
        if (facingDirection > 0) {
            hitX = entityX + entityWidth / 2.0 + offsetX;
        } else {
            hitX = entityX + entityWidth / 2.0 - offsetX - hitWidth;
        }
        
        double hitY = entityY + offsetY;
        
        return new Hitbox(hitX, hitY, hitWidth, hitHeight);
    }
    
    public static boolean checkPropCollision(Hitbox entityBox, Hitbox propBox, double entityDepth, double propDepth) {
        if (!checkDepthOverlap(entityDepth, propDepth)) {
            return false;
        }
        return checkAABB(entityBox, propBox);
    }
    
    public static double resolveHorizontalCollision(Hitbox movingBox, Hitbox staticBox, double velocityX) {
        if (velocityX > 0) {
            return staticBox.getX() - movingBox.getWidth();
        } else if (velocityX < 0) {
            return staticBox.getRight();
        }
        return movingBox.getX();
    }
}