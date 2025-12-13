package world;

import entity.Enemy;
import entity.GruntEnemy;
import entity.FastEnemy;
import entity.MiniBossEnemy;

public class Spawner {
    public enum EnemyType {
        GRUNT,
        FAST,
        MINIBOSS
    }
    
    private EnemyType enemyType;
    private double spawnX;
    private double spawnY;
    private int delay;
    private boolean spawned;
    
    public Spawner(EnemyType enemyType, double spawnX, double spawnY, int delay) {
        this.enemyType = enemyType;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.delay = delay;
        this.spawned = false;
    }
    
    public Spawner(EnemyType enemyType, double spawnX, double spawnY) {
        this(enemyType, spawnX, spawnY, 0);
    }
    
    public Enemy spawn() {
        if (spawned) return null;
        
        Enemy enemy;
        switch (enemyType) {
            case GRUNT:
                enemy = new GruntEnemy(spawnX, spawnY);
                break;
            case FAST:
                enemy = new FastEnemy(spawnX, spawnY);
                break;
            case MINIBOSS:
                enemy = new MiniBossEnemy(spawnX, spawnY);
                break;
            default:
                enemy = new GruntEnemy(spawnX, spawnY);
        }
        
        spawned = true;
        return enemy;
    }
    
    public boolean shouldSpawn(int frameCount) {
        return !spawned && frameCount >= delay;
    }
    
    public boolean hasSpawned() {
        return spawned;
    }
    
    public void reset() {
        spawned = false;
    }
    
    public EnemyType getEnemyType() {
        return enemyType;
    }
    
    public double getSpawnX() {
        return spawnX;
    }
    
    public double getSpawnY() {
        return spawnY;
    }
    
    public int getDelay() {
        return delay;
    }
}