package src.core;

import java.awt.Color;

public final class Constants {
    // Screen
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final double NS_PER_UPDATE = 1_000_000_000.0 / 60;
    
    // World
    public static final int LEVEL_WIDTH = 3200;
    public static final int GROUND_Y = 450;
    public static final int DEPTH_MIN = 380;
    public static final int DEPTH_MAX = 520;
    public static final int DEPTH_TOLERANCE = 20;
    
    // Physics
    public static final double GRAVITY = 0.8;
    public static final double JUMP_FORCE = -14.0;
    public static final double KNOCKBACK_FRICTION = 0.9;
    
    // Combat timing
    public static final int COMBO_WINDOW = 20;
    public static final int HITSTUN_DURATION = 15;
    public static final int INVINCIBILITY_DURATION = 30;
    public static final int HIT_FLASH_DURATION = 6;
    
    // Player
    public static final int PLAYER_WIDTH = 40;
    public static final int PLAYER_HEIGHT = 80;
    
    // Grunt enemy
    public static final int GRUNT_WIDTH = 36;
    public static final int GRUNT_HEIGHT = 70;
    public static final int GRUNT_HP = 50;
    public static final double GRUNT_SPEED = 1.5;
    public static final int GRUNT_DAMAGE = 8;
    
    // Fast enemy
    public static final int FAST_WIDTH = 32;
    public static final int FAST_HEIGHT = 65;
    public static final int FAST_HP = 30;
    public static final double FAST_SPEED = 2.5;
    public static final int FAST_DAMAGE = 5;
    
    // Boss
    public static final int BOSS_WIDTH = 60;
    public static final int BOSS_HEIGHT = 100;
    public static final int BOSS_HP = 200;
    public static final double BOSS_SPEED = 1.0;
    public static final int BOSS_DAMAGE = 20;
    
    // AI
    public static final double AGGRO_RANGE = 300;
    public static final double ATTACK_RANGE = 50;
    public static final int ATTACK_COOLDOWN = 60;
    public static final int REPOSITION_CHANCE = 3;
    
    // Camera
    public static final int CAMERA_DEAD_ZONE = 100;
    public static final double CAMERA_SMOOTH = 0.1;
    
    // UI
    public static final int UI_PADDING = 20;
    public static final int HP_BAR_WIDTH = 200;
    public static final int HP_BAR_HEIGHT = 20;
    
    // Props
    public static final int BARREL_WIDTH = 40;
    public static final int BARREL_HEIGHT = 50;
    public static final int BARREL_HP = 20;
    public static final int BENCH_WIDTH = 100;
    public static final int BENCH_HEIGHT = 40;
    
    // Effects
    public static final int SHAKE_DURATION = 10;
    public static final int SHAKE_INTENSITY = 8;
    public static final int FLOAT_TEXT_DURATION = 40;
    public static final int HIT_EFFECT_DURATION = 12;
    
    // Segments
    public static final int SEGMENT_1_END = 600;
    public static final int SEGMENT_2_END = 1400;
    public static final int SEGMENT_3_END = 2200;
    public static final int SEGMENT_4_END = 3000;
    
    // Colors
    public static final Color COLOR_BACKGROUND = new Color(40, 44, 52);
    public static final Color COLOR_GROUND = new Color(60, 63, 72);
    public static final Color COLOR_HP_BAR = new Color(220, 50, 50);
    public static final Color COLOR_HP_BG = new Color(80, 20, 20);
    public static final Color COLOR_ENEMY_HP = new Color(200, 100, 50);
    public static final Color COLOR_GATE = new Color(150, 120, 80);
    public static final Color COLOR_TEXT = new Color(240, 240, 240);
    public static final Color COLOR_HIGHLIGHT = new Color(255, 220, 100);
    public static final Color COLOR_HIT_FLASH = new Color(255, 255, 255, 180);
    
    private Constants() {}
}