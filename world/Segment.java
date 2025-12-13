package world;

import entity.Enemy;
import entity.Player;
import Constants;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Segment {
    private int index;
    private double startX;
    private double endX;
    private double gateX;
    private List<Spawner> spawners;
    private List<Enemy> enemies;
    private boolean activated;
    private boolean completed;
    private boolean gateOpen;
    private int frameCount;
    
    public Segment(int index, double startX, double endX) {
        this.index = index;
        this.startX = startX;
        this.endX = endX;
        this.gateX = endX - 50;
        this.spawners = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.activated = false;
        this.completed = false;
        this.gateOpen = false;
        this.frameCount = 0;
    }
    
    public void addSpawner(Spawner spawner) {
        spawners.add(spawner);
    }
    
    public void activate(Player player) {
        if (activated) return;
        
        activated = true;
        frameCount = 0;
        
        for (Spawner spawner : spawners) {
            if (spawner.getDelay() == 0) {
                Enemy enemy = spawner.spawn();
                if (enemy != null) {
                    enemy.setTarget(player);
                    enemies.add(enemy);
                }
            }
        }
    }
    
    public void update(Player player) {
        if (!activated) return;
        
        frameCount++;
        
        for (Spawner spawner : spawners) {
            if (spawner.shouldSpawn(frameCount)) {
                Enemy enemy = spawner.spawn();
                if (enemy != null) {
                    enemy.setTarget(player);
                    enemies.add(enemy);
                }
            }
        }
        
        for (Enemy enemy : enemies) {
            enemy.update();
            enemy.clampToSegment(startX, gateOpen ? endX + 200 : gateX);
        }
        
        enemies.removeIf(enemy -> enemy.isDead() && !enemy.isActive());
        
        if (!completed && allEnemiesDefeated()) {
            completed = true;
            gateOpen = true;
        }
    }
    
    public boolean allEnemiesDefeated() {
        if (!activated) return false;
        
        for (Spawner spawner : spawners) {
            if (!spawner.hasSpawned()) return false;
        }
        
        for (Enemy enemy : enemies) {
            if (!enemy.isDead()) return false;
        }
        
        return true;
    }
    
    public void render(Graphics2D g2d, int cameraX, int cameraY) {
        if (activated && !gateOpen) {
            renderGate(g2d, cameraX, cameraY);
        }
        
        for (Enemy enemy : enemies) {
            enemy.render(g2d, cameraX, cameraY);
        }
    }
    
    private void renderGate(Graphics2D g2d, int cameraX, int cameraY) {
        int screenX = (int)(gateX - cameraX);
        int gateWidth = 20;
        int gateHeight = Constants.DEPTH_MAX - Constants.DEPTH_MIN + 100;
        int gateY = Constants.DEPTH_MIN - 50 - cameraY;
        
        g2d.setColor(Constants.COLOR_GATE);
        g2d.fillRect(screenX, gateY, gateWidth, gateHeight);
        
        g2d.setColor(Constants.COLOR_GATE.darker());
        for (int i = 0; i < gateHeight; i += 20) {
            g2d.fillRect(screenX + 2, gateY + i, gateWidth - 4, 2);
        }
        
        g2d.setColor(new Color(100, 80, 60));
        g2d.fillRect(screenX - 5, gateY - 10, gateWidth + 10, 15);
    }
    
    public List<Enemy> getEnemies() {
        return enemies;
    }
    
    public double getStartX() {
        return startX;
    }
    
    public double getEndX() {
        return endX;
    }
    
    public double getGateX() {
        return gateX;
    }
    
    public boolean isActivated() {
        return activated;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public boolean isGateOpen() {
        return gateOpen;
    }
    
    public int getIndex() {
        return index;
    }
    
    public double getPlayerMaxX() {
        return gateOpen ? endX : gateX - 10;
    }
}