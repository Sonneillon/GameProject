package src.world;

import src.entity.*;

public class Spawner {
    public enum EnemyType { GRUNT, FAST, BOSS }
    
    public final EnemyType type;
    public final double spawnX, spawnY;
    public final int delay;
    public boolean spawned;
    
    public Spawner(EnemyType type, double x, double y, int delay) {
        this.type = type;
        this.spawnX = x;
        this.spawnY = y;
        this.delay = delay;
    }
    
    public Spawner(EnemyType type, double x, double y) {
        this(type, x, y, 0);
    }
    
    public Enemy spawn() {
        if (spawned) return null;
        spawned = true;
        return switch (type) {
            case GRUNT -> new GruntEnemy(spawnX, spawnY);
            case FAST -> new FastEnemy(spawnX, spawnY);
            case BOSS -> new Boss(spawnX, spawnY);
        };
    }
    
    public boolean shouldSpawn(int frame) {
        return !spawned && frame >= delay;
    }
}
