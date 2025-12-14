package src.combat;

import java.awt.Color;

public enum CharacterType {
    BALANCED("Lochlan", new Color(70, 130, 180), 4.0, 100, 10, 1.0),
    FAST("Liam", new Color(220, 100, 150), 5.5, 70, 7, 0.7),
    HEAVY("Dexter", new Color(100, 160, 100), 2.8, 140, 15, 1.4);
    
    public final String name;
    public final Color color;
    public final double speed;
    public final int maxHp;
    public final int attackPower;
    public final double attackSpeedMod;
    
    CharacterType(String name, Color color, double speed, int maxHp, int power, double mod) {
        this.name = name;
        this.color = color;
        this.speed = speed;
        this.maxHp = maxHp;
        this.attackPower = power;
        this.attackSpeedMod = mod;
    }
    
    public String getDescription() {
        return switch (this) {
            case BALANCED -> "Well-rounded fighter with balanced stats.";
            case FAST -> "Quick and agile with rapid combos.";
            case HEAVY -> "Slow but powerful with strong knockback.";
        };
    }
}