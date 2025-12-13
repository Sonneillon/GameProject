package world;

import entity.Enemy;
import entity.Player;
import Constants;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Level {
    private List<Segment> segments;
    private List<Prop> props;
    private int currentSegmentIndex;
    private double levelWidth;
    private boolean bossDefeated;
    
    public Level() {
        this.segments = new ArrayList<>();
        this.props = new ArrayList<>();
        this.currentSegmentIndex = 0;
        this.levelWidth = Constants.LEVEL_WIDTH;
        this.bossDefeated = false;
        
        createSegments();
        createProps();
    }
    
    private void createSegments() {
        Segment segment1 = new Segment(0, 0, Constants.SEGMENT_1_END);
        segment1.addSpawner(new Spawner(Spawner.EnemyType.GRUNT, 400, Constants.GROUND_Y - Constants.GRUNT_HEIGHT));
        segment1.addSpawner(new Spawner(Spawner.EnemyType.GRUNT, 500, Constants.GROUND_Y - Constants.GRUNT_HEIGHT + 30, 30));
        segments.add(segment1);
        
        Segment segment2 = new Segment(1, Constants.SEGMENT_1_END, Constants.SEGMENT_2_END);
        segment2.addSpawner(new Spawner(Spawner.EnemyType.GRUNT, 800, Constants.GROUND_Y - Constants.GRUNT_HEIGHT - 20));
        segment2.addSpawner(new Spawner(Spawner.EnemyType.FAST, 900, Constants.GROUND_Y - Constants.FAST_ENEMY_HEIGHT + 20));
        segment2.addSpawner(new Spawner(Spawner.EnemyType.GRUNT, 1000, Constants.GROUND_Y - Constants.GRUNT_HEIGHT, 60));
        segments.add(segment2);
        
        Segment segment3 = new Segment(2, Constants.SEGMENT_2_END, Constants.SEGMENT_3_END);
        segment3.addSpawner(new Spawner(Spawner.EnemyType.GRUNT, 1500, Constants.GROUND_Y - Constants.GRUNT_HEIGHT));
        segment3.addSpawner(new Spawner(Spawner.EnemyType.GRUNT, 1600, Constants.GROUND_Y - Constants.GRUNT_HEIGHT + 40));
        segment3.addSpawner(new Spawner(Spawner.EnemyType.FAST, 1700, Constants.GROUND_Y - Constants.FAST_ENEMY_HEIGHT - 30));
        segment3.addSpawner(new Spawner(Spawner.EnemyType.FAST, 1800, Constants.GROUND_Y - Constants.FAST_ENEMY_HEIGHT + 10, 90));
        segment3.addSpawner(new Spawner(Spawner.EnemyType.GRUNT, 1650, Constants.GROUND_Y - Constants.GRUNT_HEIGHT - 10, 120));
        segments.add(segment3);
        
        Segment segment4 = new Segment(3, Constants.SEGMENT_3_END, Constants.SEGMENT_4_END);
        segment4.addSpawner(new Spawner(Spawner.EnemyType.MINIBOSS, 2600, Constants.GROUND_Y - Constants.MINIBOSS_HEIGHT));
        segments.add(segment4);
    }
    
    private void createProps() {
        props.add(new Prop(Prop.PropType.BARREL, 200, Constants.GROUND_Y - Constants.PROP_BARREL_HEIGHT + 20));
        props.add(new Prop(Prop.PropType.BENCH, 350, Constants.GROUND_Y - Constants.PROP_BENCH_HEIGHT + 50));
        
        props.add(new Prop(Prop.PropType.HYDRANT, 750, Constants.GROUND_Y - 45 - 10));
        props.add(new Prop(Prop.PropType.BARREL, 950, Constants.GROUND_Y - Constants.PROP_BARREL_HEIGHT + 40));
        
        props.add(new Prop(Prop.PropType.BARREL, 1550, Constants.GROUND_Y - Constants.PROP_BARREL_HEIGHT));
        props.add(new Prop(Prop.PropType.BARREL, 1590, Constants.GROUND_Y - Constants.PROP_BARREL_HEIGHT + 30));
        props.add(new Prop(Prop.PropType.BENCH, 1850, Constants.GROUND_Y - Constants.PROP_BENCH_HEIGHT + 60));
        
        props.add(new Prop(Prop.PropType.BARREL, 2400, Constants.GROUND_Y - Constants.PROP_BARREL_HEIGHT + 10));
        props.add(new Prop(Prop.PropType.HYDRANT, 2700, Constants.GROUND_Y - 45 + 30));
    }
    
    public void update(Player player) {
        Segment currentSegment = getCurrentSegment();
        
        if (currentSegment != null) {
            if (!currentSegment.isActivated()) {
                double playerX = player.getX();
                if (playerX >= currentSegment.getStartX() + 100) {
                    currentSegment.activate(player);
                }
            }
            
            currentSegment.update(player);
            
            if (currentSegment.isCompleted() && currentSegmentIndex < segments.size() - 1) {
                if (player.getX() > currentSegment.getEndX() - 100) {
                    currentSegmentIndex++;
                }
            }
            
            if (currentSegmentIndex == segments.size() - 1 && currentSegment.isCompleted()) {
                bossDefeated = true;
            }
        }
    }
    
    public void render(Graphics2D g2d, int cameraX, int cameraY) {
        renderBackground(g2d, cameraX, cameraY);
        
        renderProps(g2d, cameraX, cameraY, true);
        
        for (Segment segment : segments) {
            segment.render(g2d, cameraX, cameraY);
        }
        
        renderProps(g2d, cameraX, cameraY, false);
    }
    
    private void renderBackground(Graphics2D g2d, int cameraX, int cameraY) {
        g2d.setColor(Constants.COLOR_BACKGROUND);
        g2d.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        
        g2d.setColor(new Color(50, 54, 62));
        for (int i = 0; i < levelWidth; i += 200) {
            int screenX = i - cameraX;
            if (screenX > -100 && screenX < Constants.SCREEN_WIDTH + 100) {
                int buildingWidth = 80 + (i % 3) * 30;
                int buildingHeight = 150 + (i % 5) * 40;
                int buildingY = Constants.DEPTH_MIN - buildingHeight - 50 - cameraY;
                g2d.fillRect(screenX, buildingY, buildingWidth, buildingHeight);
                
                g2d.setColor(new Color(70, 70, 50));
                for (int wy = buildingY + 20; wy < buildingY + buildingHeight - 20; wy += 30) {
                    for (int wx = screenX + 10; wx < screenX + buildingWidth - 10; wx += 25) {
                        g2d.fillRect(wx, wy, 15, 20);
                    }
                }
                g2d.setColor(new Color(50, 54, 62));
            }
        }
        
        g2d.setColor(Constants.COLOR_GROUND);
        int groundY = Constants.DEPTH_MIN - 30 - cameraY;
        int groundHeight = Constants.SCREEN_HEIGHT - groundY + 100;
        g2d.fillRect(0, groundY, Constants.SCREEN_WIDTH, groundHeight);
        
        g2d.setColor(new Color(80, 83, 92));
        g2d.fillRect(0, groundY, Constants.SCREEN_WIDTH, 5);
        
        g2d.setColor(new Color(70, 73, 82));
        int laneY1 = Constants.DEPTH_MIN + (Constants.DEPTH_MAX - Constants.DEPTH_MIN) / 3 - cameraY;
        int laneY2 = Constants.DEPTH_MIN + 2 * (Constants.DEPTH_MAX - Constants.DEPTH_MIN) / 3 - cameraY;
        g2d.drawLine(0, laneY1, Constants.SCREEN_WIDTH, laneY1);
        g2d.drawLine(0, laneY2, Constants.SCREEN_WIDTH, laneY2);
    }
    
    private void renderProps(Graphics2D g2d, int cameraX, int cameraY, boolean background) {
        for (Prop prop : props) {
            boolean isBackground = prop.getDepth() < Constants.GROUND_Y;
            if (isBackground == background) {
                prop.render(g2d, cameraX, cameraY);
            }
        }
    }
    
    public Segment getCurrentSegment() {
        if (currentSegmentIndex >= 0 && currentSegmentIndex < segments.size()) {
            return segments.get(currentSegmentIndex);
        }
        return null;
    }
    
    public List<Enemy> getAllActiveEnemies() {
        List<Enemy> allEnemies = new ArrayList<>();
        for (Segment segment : segments) {
            allEnemies.addAll(segment.getEnemies());
        }
        return allEnemies;
    }
    
    public List<Prop> getProps() {
        return props;
    }
    
    public double getLevelWidth() {
        return levelWidth;
    }
    
    public double getPlayerMinX() {
        Segment current = getCurrentSegment();
        return current != null ? current.getStartX() : 0;
    }
    
    public double getPlayerMaxX() {
        Segment current = getCurrentSegment();
        return current != null ? current.getPlayerMaxX() : levelWidth;
    }
    
    public boolean isBossDefeated() {
        return bossDefeated;
    }
    
    public int getCurrentSegmentIndex() {
        return currentSegmentIndex;
    }
    
    public List<Segment> getSegments() {
        return segments;
    }
}