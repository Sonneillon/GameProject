package src.world;

import src.core.Constants;
import src.entity.Enemy;
import src.entity.Player;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Segment {
    public final int index;
    public final double startX, endX, gateX;
    
    private List<Spawner> spawners = new ArrayList<>();
    public List<Enemy> enemies = new ArrayList<>();
    
    public boolean activated, completed, gateOpen;
    private int frameCount;
    
    public Segment(int index, double startX, double endX) {
        this.index = index;
        this.startX = startX;
        this.endX = endX;
        this.gateX = endX - 50;
    }
    
    public void addSpawner(Spawner s) {
        spawners.add(s);
    }
    
    public void activate(Player player) {
        if (activated) return;
        activated = true;
        frameCount = 0;
        
        for (Spawner s : spawners) {
            if (s.delay == 0) {
                Enemy e = s.spawn();
                if (e != null) {
                    e.setTarget(player);
                    enemies.add(e);
                }
            }
        }
    }
    
    public void update(Player player) {
        if (!activated) return;
        
        frameCount++;
        
        for (Spawner s : spawners) {
            if (s.shouldSpawn(frameCount)) {
                Enemy e = s.spawn();
                if (e != null) {
                    e.setTarget(player);
                    enemies.add(e);
                }
            }
        }
        
        for (Enemy e : enemies) {
            e.update();
            e.clampToSegment(startX, gateOpen ? endX + 200 : gateX);
        }
        
        enemies.removeIf(e -> e.dead && !e.active);
        
        if (!completed && allDefeated()) {
            completed = true;
            gateOpen = true;
        }
    }
    
    private boolean allDefeated() {
        for (Spawner s : spawners) if (!s.spawned) return false;
        for (Enemy e : enemies) if (!e.dead) return false;
        return true;
    }
    
    public void render(Graphics2D g, int camX, int camY) {
        if (activated && !gateOpen) {
            int sx = (int)(gateX - camX);
            int gateHeight = Constants.DEPTH_MAX - Constants.DEPTH_MIN + 100;
            int gateY = Constants.DEPTH_MIN - 50 - camY;
            g.setColor(Constants.COLOR_GATE);
            g.fillRect(sx, gateY, 20, gateHeight);
        }
        
        for (Enemy e : enemies) {
            e.render(g, camX, camY);
        }
    }
    
    public double getPlayerMaxX() {
        return gateOpen ? endX : gateX - 10;
    }
}
