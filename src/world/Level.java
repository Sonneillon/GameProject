package src.world;

import src.core.Constants;
import src.entity.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Level {
    public List<Segment> segments = new ArrayList<>();
    public List<Prop> props = new ArrayList<>();
    public int currentSegment;
    public final double width = Constants.LEVEL_WIDTH;
    public boolean bossDefeated;
    
    public Level() {
        createSegments();
        createProps();
    }
    
    private void createSegments() {
        Segment s1 = new Segment(0, 0, Constants.SEGMENT_1_END);
        s1.addSpawner(new Spawner(Spawner.EnemyType.GRUNT, 400, Constants.GROUND_Y - Constants.GRUNT_HEIGHT));
        s1.addSpawner(new Spawner(Spawner.EnemyType.GRUNT, 500, Constants.GROUND_Y - Constants.GRUNT_HEIGHT + 30, 30));
        segments.add(s1);
        
        Segment s2 = new Segment(1, Constants.SEGMENT_1_END, Constants.SEGMENT_2_END);
        s2.addSpawner(new Spawner(Spawner.EnemyType.GRUNT, 800, Constants.GROUND_Y - Constants.GRUNT_HEIGHT - 20));
        s2.addSpawner(new Spawner(Spawner.EnemyType.FAST, 900, Constants.GROUND_Y - Constants.FAST_HEIGHT + 20));
        s2.addSpawner(new Spawner(Spawner.EnemyType.GRUNT, 1000, Constants.GROUND_Y - Constants.GRUNT_HEIGHT, 60));
        segments.add(s2);
        
        Segment s3 = new Segment(2, Constants.SEGMENT_2_END, Constants.SEGMENT_3_END);
        s3.addSpawner(new Spawner(Spawner.EnemyType.GRUNT, 1500, Constants.GROUND_Y - Constants.GRUNT_HEIGHT));
        s3.addSpawner(new Spawner(Spawner.EnemyType.GRUNT, 1600, Constants.GROUND_Y - Constants.GRUNT_HEIGHT + 40));
        s3.addSpawner(new Spawner(Spawner.EnemyType.FAST, 1700, Constants.GROUND_Y - Constants.FAST_HEIGHT - 30));
        s3.addSpawner(new Spawner(Spawner.EnemyType.FAST, 1800, Constants.GROUND_Y - Constants.FAST_HEIGHT + 10, 90));
        s3.addSpawner(new Spawner(Spawner.EnemyType.GRUNT, 1650, Constants.GROUND_Y - Constants.GRUNT_HEIGHT - 10, 120));
        segments.add(s3);
        
        Segment s4 = new Segment(3, Constants.SEGMENT_3_END, Constants.SEGMENT_4_END);
        s4.addSpawner(new Spawner(Spawner.EnemyType.BOSS, 2600, Constants.GROUND_Y - Constants.BOSS_HEIGHT));
        segments.add(s4);
    }
    
    private void createProps() {
        props.add(new Prop(Prop.Type.BARREL, 200, Constants.GROUND_Y - Constants.BARREL_HEIGHT + 20));
        props.add(new Prop(Prop.Type.BENCH, 350, Constants.GROUND_Y - Constants.BENCH_HEIGHT + 50));
        props.add(new Prop(Prop.Type.HYDRANT, 750, Constants.GROUND_Y - 55));
        props.add(new Prop(Prop.Type.BARREL, 950, Constants.GROUND_Y - Constants.BARREL_HEIGHT + 40));
        props.add(new Prop(Prop.Type.BARREL, 1550, Constants.GROUND_Y - Constants.BARREL_HEIGHT));
        props.add(new Prop(Prop.Type.BARREL, 2400, Constants.GROUND_Y - Constants.BARREL_HEIGHT + 10));
    }
    
    public void update(Player player) {
        Segment current = getCurrentSegment();
        if (current == null) return;
        
        if (!current.activated && player.x >= current.startX + 100) {
            current.activate(player);
        }
        
        current.update(player);
        
        if (current.completed && currentSegment < segments.size() - 1 && player.x > current.endX - 100) {
            currentSegment++;
        }
        
        if (currentSegment == segments.size() - 1 && current.completed) {
            bossDefeated = true;
        }
    }
    
    public void render(Graphics2D g, int camX, int camY) {
        // Background
        g.setColor(Constants.COLOR_BACKGROUND);
        g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        
        // Buildings
        g.setColor(new Color(50, 54, 62));
        for (int i = 0; i < width; i += 200) {
            int sx = i - camX;
            if (sx > -100 && sx < Constants.SCREEN_WIDTH + 100) {
                int bw = 80 + (i % 3) * 30, bh = 150 + (i % 5) * 40;
                int by = Constants.DEPTH_MIN - bh - 50 - camY;
                g.fillRect(sx, by, bw, bh);
            }
        }
        
        // Ground
        g.setColor(Constants.COLOR_GROUND);
        g.fillRect(0, Constants.DEPTH_MIN - 30 - camY, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        
        // Props
        for (Prop p : props) p.render(g, camX, camY);
        
        // Segments
        for (Segment s : segments) s.render(g, camX, camY);
    }
    
    public Segment getCurrentSegment() {
        return (currentSegment >= 0 && currentSegment < segments.size()) ? segments.get(currentSegment) : null;
    }
    
    public List<Enemy> getAllEnemies() {
        List<Enemy> all = new ArrayList<>();
        for (Segment s : segments) all.addAll(s.enemies);
        return all;
    }
    
    public double getPlayerMinX() {
        Segment s = getCurrentSegment();
        return s != null ? s.startX : 0;
    }
    
    public double getPlayerMaxX() {
        Segment s = getCurrentSegment();
        return s != null ? s.getPlayerMaxX() : width;
    }
}
