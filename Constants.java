public final class Constants {
    // Screen dimensions
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    
    // Game timing
    public static final int TARGET_UPS = 60;
    public static final double NS_PER_UPDATE = 1_000_000_000.0 / TARGET_UPS;
    
    // World dimensions
    public static final int LEVEL_WIDTH = 3200;
    public static final int GROUND_Y = 450;
    public static final int DEPTH_MIN = 380;
    public static final int DEPTH_MAX = 520;
    public static final int DEPTH_TOLERANCE = 20;
    
    // Physics
    public static final double GRAVITY = 0.8;
    public static final double JUMP_FORCE = -14.0;
    public static final double FRICTION = 0.85;
    public static final double KNOCKBACK_FRICTION = 0.9;
    
    // Combat timing (in frames)
    public static final int COMBO_WINDOW = 20;
    public static final int HITSTUN_DURATION = 15;
    public static final int INVINCIBILITY_DURATION = 30;
    public static final int HIT_FLASH_DURATION = 6;
    
    // Player defaults
    public static final int PLAYER_WIDTH = 40;
    public static final int PLAYER_HEIGHT = 80;
    public static final double PLAYER_BASE_SPEED = 4.0;
    public static final int PLAYER_BASE_HP = 100;
    public static final int PLAYER_BASE_ATTACK = 10;
    
    // Enemy defaults
    public static final int GRUNT_WIDTH = 36;
    public static final int GRUNT_HEIGHT = 70;
    public static final int GRUNT_HP = 50;
    public static final double GRUNT_SPEED = 1.5;
    public static final int GRUNT_DAMAGE = 8;
    
    public static final int FAST_ENEMY_WIDTH = 32;
    public static final int FAST_ENEMY_HEIGHT = 65;
    public static final int FAST_ENEMY_HP = 30;
    public static final double FAST_ENEMY_SPEED = 2.5;
    public static final int FAST_ENEMY_DAMAGE = 5;
    
    public static final int MINIBOSS_WIDTH = 60;
    public static final int MINIBOSS_HEIGHT = 100;
    public static final int MINIBOSS_HP = 200;
    public static final double MINIBOSS_SPEED = 1.0;
    public static final int MINIBOSS_DAMAGE = 20;
    
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
    public static final int PROP_BARREL_WIDTH = 40;
    public static final int PROP_BARREL_HEIGHT = 50;
    public static final int PROP_BARREL_HP = 20;
    public static final int PROP_BENCH_WIDTH = 100;
    public static final int PROP_BENCH_HEIGHT = 40;
    
    // Effects
    public static final int SCREEN_SHAKE_DURATION = 10;
    public static final int SCREEN_SHAKE_INTENSITY = 8;
    public static final int FLOATING_TEXT_DURATION = 40;
    public static final int HIT_EFFECT_DURATION = 12;
    
    // Segment boundaries
    public static final int SEGMENT_1_END = 600;
    public static final int SEGMENT_2_END = 1400;
    public static final int SEGMENT_3_END = 2200;
    public static final int SEGMENT_4_END = 3000;
    
    // Colors
    public static final java.awt.Color COLOR_BACKGROUND = new java.awt.Color(40, 44, 52);
    public static final java.awt.Color COLOR_GROUND = new java.awt.Color(60, 63, 72);
    public static final java.awt.Color COLOR_UI_BG = new java.awt.Color(20, 22, 28);
    public static final java.awt.Color COLOR_HP_BAR = new java.awt.Color(220, 50, 50);
    public static final java.awt.Color COLOR_HP_BG = new java.awt.Color(80, 20, 20);
    public static final java.awt.Color COLOR_ENEMY_HP = new java.awt.Color(200, 100, 50);
    public static final java.awt.Color COLOR_GATE = new java.awt.Color(150, 120, 80);
    public static final java.awt.Color COLOR_PROP = new java.awt.Color(100, 80, 60);
    public static final java.awt.Color COLOR_TEXT = new java.awt.Color(240, 240, 240);
    public static final java.awt.Color COLOR_TEXT_HIGHLIGHT = new java.awt.Color(255, 220, 100);
    public static final java.awt.Color COLOR_HIT_FLASH = new java.awt.Color(255, 255, 255, 180);
    
    private Constants() {}
}