package src.state;

import src.combat.*;
import src.core.*;
import src.effect.*;
import src.entity.*;
import src.world.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayState implements GameState {
    private CharacterType characterType;
    private Player player;
    private Level level;
    private Camera camera;
    
    private List<HitEffect> hitEffects = new ArrayList<>();
    private List<FloatingText> floatingTexts = new ArrayList<>();
    
    private GameState next;
    private boolean transition;
    private int comboDisplayTimer;
    
    public PlayState(CharacterType type) {
        this.characterType = type;
    }
    
    @Override
    public void enter() {
        level = new Level();
        player = new Player(50, Constants.GROUND_Y - Constants.PLAYER_HEIGHT, characterType);
        camera = new Camera();
        
        for (int i = 0; i < 20; i++) {
            hitEffects.add(new HitEffect());
            floatingTexts.add(new FloatingText());
        }
        
        next = null;
        transition = false;
        comboDisplayTimer = 0;
    }
    
    @Override
    public void exit() {}
    
    @Override
    public void update(Input input) {
        if (player.dead) {
            if (input.restart()) { next = new MenuState(); transition = true; }
            if (input.escape()) System.exit(0);
            return;
        }
        
        if (level.bossDefeated) {
            if (input.restart()) { next = new MenuState(); transition = true; }
            if (input.escape()) System.exit(0);
            return;
        }
        
        player.handleInput(input);
        player.update();
        player.clamp(level.getPlayerMinX(), level.getPlayerMaxX());
        player.y = Math.max(Constants.DEPTH_MIN - player.height, Math.min(Constants.DEPTH_MAX - player.height, player.y));
        player.groundY = player.y;
        
        level.update(player);
        
        Segment seg = level.getCurrentSegment();
        if (seg != null && seg.activated && !seg.completed) {
            camera.setLocked(true);
            camera.setMaxX(seg.gateX + Constants.SCREEN_WIDTH / 2);
        } else {
            camera.setLocked(false);
            camera.setMaxX(level.width);
        }
        camera.update(player.x);
        
        processPlayerAttacks();
        processEnemyAttacks();
        
        for (HitEffect h : hitEffects) h.update();
        for (FloatingText f : floatingTexts) f.update();
        
        if (comboDisplayTimer > 0) comboDisplayTimer--;
        
        if (input.escape()) { next = new MenuState(); transition = true; }
    }
    
    private void processPlayerAttacks() {
        Attack atk = player.attack;
        if (atk == null || !atk.isActive() || atk.hasHit) return;
        
        Hitbox hb = Collision.createAttackHitbox(player.x, player.y, player.width, player.facing, atk);
        
        for (Enemy e : level.getAllEnemies()) {
            if (e.dead || e.invincibility > 0) continue;
            if (Collision.checkHit(hb, e.hitbox, player.depth(), e.depth())) {
                e.takeHit(atk, player.facing);
                atk.hasHit = true;
                spawnHitEffect(e.centerX(), e.centerY());
                spawnFloatingText(e.centerX(), e.y, atk.damage);
                player.registerHit();
                comboDisplayTimer = 90;
                if (atk.type == Attack.Type.HEAVY) camera.getShake().triggerHeavy();
                else camera.getShake().triggerLight();
                if (e.dead) player.addScore(e.scoreValue);
                break;
            }
        }
        
        for (Prop p : level.props) {
            if (p.broken || !p.breakable) continue;
            if (Collision.checkHit(hb, p.hitbox, player.depth(), p.depth)) {
                p.damage(atk.damage);
                atk.hasHit = true;
                spawnHitEffect(p.x + p.width / 2, p.y + p.height / 2);
                camera.getShake().triggerLight();
                break;
            }
        }
    }
    
    private void processEnemyAttacks() {
        if (player.dead || player.invincibility > 0) return;
        
        for (Enemy e : level.getAllEnemies()) {
            if (e.dead) continue;
            Attack atk = e.attack;
            if (atk == null || !atk.isActive() || atk.hasHit) continue;
            
            Hitbox hb = Collision.createAttackHitbox(e.x, e.y, e.width, e.facing, atk);
            if (Collision.checkHit(hb, player.hitbox, e.depth(), player.depth())) {
                player.takeHit(atk, e.facing);
                atk.hasHit = true;
                spawnHitEffect(player.centerX(), player.centerY());
                spawnFloatingText(player.centerX(), player.y, atk.damage);
                camera.getShake().triggerLight();
                player.invincibility = Constants.INVINCIBILITY_DURATION;
            }
        }
    }
    
    private void spawnHitEffect(double x, double y) {
        for (HitEffect h : hitEffects) if (!h.active) { h.trigger(x, y); break; }
    }
    
    private void spawnFloatingText(double x, double y, int damage) {
        for (FloatingText f : floatingTexts) if (!f.active) { f.trigger(x, y - 20, damage); break; }
    }
    
    @Override
    public void render(Graphics2D g) {
        level.render(g, camera.getX(), camera.getY());
        player.render(g, camera.getX(), camera.getY());
        
        for (HitEffect h : hitEffects) h.render(g, camera.getX(), camera.getY());
        for (FloatingText f : floatingTexts) f.render(g, camera.getX(), camera.getY());
        
        renderUI(g);
        
        if (player.dead) renderGameOver(g);
        else if (level.bossDefeated) renderVictory(g);
    }
    
    private void renderUI(Graphics2D g) {
        g.setColor(new Color(20, 22, 28, 200));
        g.fillRect(0, 0, Constants.SCREEN_WIDTH, 50);
        
        g.setColor(player.type.color);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(player.type.name, Constants.UI_PADDING, 30);
        
        g.setColor(Constants.COLOR_HP_BG);
        g.fillRect(Constants.UI_PADDING + 80, 15, Constants.HP_BAR_WIDTH, Constants.HP_BAR_HEIGHT);
        g.setColor(Constants.COLOR_HP_BAR);
        g.fillRect(Constants.UI_PADDING + 80, 15, (int)((double) player.hp / player.maxHp * Constants.HP_BAR_WIDTH), Constants.HP_BAR_HEIGHT);
        g.setColor(Color.WHITE);
        g.drawRect(Constants.UI_PADDING + 80, 15, Constants.HP_BAR_WIDTH, Constants.HP_BAR_HEIGHT);
        
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString(player.hp + "/" + player.maxHp, Constants.UI_PADDING + 85, 30);
        
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Constants.COLOR_TEXT);
        g.drawString("SCORE: " + player.score, Constants.SCREEN_WIDTH - 150, 30);
        
        if (comboDisplayTimer > 0 && player.hits > 1) {
            float alpha = Math.min(1f, comboDisplayTimer / 30f);
            g.setColor(new Color(255, 220, 100, (int)(alpha * 255)));
            g.setFont(new Font("Arial", Font.BOLD, 28));
            String combo = player.hits + " HITS!";
            FontMetrics fm = g.getFontMetrics();
            g.drawString(combo, (Constants.SCREEN_WIDTH - fm.stringWidth(combo)) / 2, 100);
        }
        
        g.setColor(new Color(150, 150, 150));
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("AREA " + (level.currentSegment + 1) + "/4", Constants.SCREEN_WIDTH / 2 - 30, 30);
    }
    
    private void renderGameOver(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        
        g.setColor(new Color(200, 50, 50));
        g.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        g.drawString("GAME OVER", (Constants.SCREEN_WIDTH - fm.stringWidth("GAME OVER")) / 2, Constants.SCREEN_HEIGHT / 2 - 50);
        
        g.setColor(Constants.COLOR_TEXT);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        String score = "Final Score: " + player.score;
        fm = g.getFontMetrics();
        g.drawString(score, (Constants.SCREEN_WIDTH - fm.stringWidth(score)) / 2, Constants.SCREEN_HEIGHT / 2 + 20);
        
        g.setColor(new Color(150, 150, 150));
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Press R for menu, ESC to quit", 280, Constants.SCREEN_HEIGHT / 2 + 80);
    }
    
    private void renderVictory(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        
        g.setColor(new Color(255, 220, 100));
        g.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        g.drawString("VICTORY!", (Constants.SCREEN_WIDTH - fm.stringWidth("VICTORY!")) / 2, Constants.SCREEN_HEIGHT / 2 - 50);
        
        g.setColor(Constants.COLOR_TEXT);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        String score = "Final Score: " + player.score;
        fm = g.getFontMetrics();
        g.drawString(score, (Constants.SCREEN_WIDTH - fm.stringWidth(score)) / 2, Constants.SCREEN_HEIGHT / 2 + 20);
        
        g.setColor(new Color(150, 150, 150));
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Press R for menu, ESC to quit", 280, Constants.SCREEN_HEIGHT / 2 + 80);
    }
    
    @Override
    public GameState nextState() { return next; }
    
    @Override
    public boolean shouldTransition() { return transition; }
}