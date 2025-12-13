package combat;

import java.awt.Color;

public enum CharacterType {
    BALANCED(
        "Alex",
        new Color(70, 130, 180),
        4.0,
        100,
        10,
        1.0
    ),
    FAST(
        "Nina",
        new Color(220, 100, 150),
        5.5,
        70,
        7,
        0.7
    ),
    HEAVY(
        "Max",
        new Color(100, 160, 100),
        2.8,
        140,
        15,
        1.4
    );
    
    private final String name;
    private final Color color;
    private final double speed;
    private final int maxHp;
    private final int attackPower;
    private final double attackSpeedModifier;
    
    CharacterType(String name, Color color, double speed, int maxHp, int attackPower, double attackSpeedModifier) {
        this.name = name;
        this.color = color;
        this.speed = speed;
        this.maxHp = maxHp;
        this.attackPower = attackPower;
        this.attackSpeedModifier = attackSpeedModifier;
    }
    
    public String getName() {
        return name;
    }
    
    public Color getColor() {
        return color;
    }
    
    public double getSpeed() {
        return speed;
    }
    
    public int getMaxHp() {
        return maxHp;
    }
    
    public int getAttackPower() {
        return attackPower;
    }
    
    public double getAttackSpeedModifier() {
        return attackSpeedModifier;
    }
    
    public String getDescription() {
        switch (this) {
            case BALANCED:
                return "Well-rounded fighter with balanced stats.";
            case FAST:
                return "Quick and agile with rapid combos.";
            case HEAVY:
                return "Slow but powerful with strong knockback.";
            default:
                return "";
        }
    }
}