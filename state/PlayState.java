package state;

import combat.Attack;
import combat.CharacterType;
import combat.Collision;
import combat.Hitbox;
import effect.FloatingText;
import effect.HitEffect;
import entity.Enemy;
import entity.Player;
import world.Level;
import world.Prop;
import Camera;
import Constants;
import Input;
import Renderer;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class PlayState implements GameState {
    private CharacterType selectedCharacter;
    private Player player;
    private Level level;
    private Camera camera;
    
    private List<HitEffect> hitEffects;
    private List<FloatingText> floatingTexts;
    
    private GameState nextState;
    private boolean shouldTransition;
    
    private int comboDisplayTimer;
    
    public PlayState(CharacterType character) {
        this.selectedCharacter = character;
        this.nextState = null;
        this.shouldTransition = false;
    }
    
    @Override
    public void enter() {
        level = new Level();
        
        double startX = 50;
        double startY = Constants.GROUND_Y - Constants.PLAYER_HEIGHT;
        player = new Player(startX, startY, selectedCharacter);
        
        camera = new Camera(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        camera.setBounds(0, level.getLevelWidth());
        camera.setPosition(0, 0);
        
        hitEffects = new ArrayList<>();
        floatingTexts = new ArrayList<>();
        
        for (int i = 0; i < 20; i++) {
            hitEffects.add(new HitEffect());
            floatingTexts.add(new FloatingText());
        }
        
        comboDisplayTimer = 0;
        nextState = null;
        shouldTransition = false;
    }
    
    @Override
    public void exit() {
    }
    
    @Override
    public void update(Input input) {
        if (player.isDead()) {
            if (input.isRestartJustPressed()) {
                nextState = new MenuState();
                shouldTransition = true;
            }
            if (input.isEscapeJustPressed()) {
                System.exit(0);
            }
            return;
        }
        
        if (level.isBossDefeated()) {
            if (input.isRestartJustPressed()) {
                nextState = new MenuState();
                shouldTransition = true;
            }
            if (input.isEscapeJustPressed()) {
                System.exit(0);
            }
            return;
        }
        
        player.handleInput(input);
        player.update();
        
        player.clampToLevel(level.getPlayerMinX(), level.getPlayerMaxX());
        
        player.setY(Math.max(Constants.DEPTH_MIN - player.getHeight(), 
                   Math.min(Constants.DEPTH_MAX - player.getHeight(), player.getY())));
        player.setGroundY(player.getY());
        
        level.update(player);
        
        updateCameraLocking();
        camera.update(player.getX());
        
        processPlayerAttacks();
        processEnemyAttacks();
        
        for (HitEffect effect : hitEffects) {
            effect.update();
        }
        for (FloatingText text : floatingTexts) {
            text.update();
        }
        
        if (comboDisplayTimer > 0) {
            comboDisplayTimer--;
        }
        
        if (input.isEscapeJustPressed()) {
            nextState = new MenuState();
            shouldTransition = true;
        }
    }
    
    private void updateCameraLocking() {
        var segment = level.getCurrentSegment();
        if (segment != null && segment.isActivated() && !segment.isCompleted()) {
            camera.lock();
            camera.setMaxX(segment.getGateX() + Constants.SCREEN_WIDTH / 2);
        } else {
            camera.unlock();
            camera.setMaxX(level.getLevelWidth());
        }
    }
    
    private void processPlayerAttacks() {
        Attack attack = player.getCurrentAttack();
        if (attack == null || !attack.isActive() || attack.hasHit()) {
            return;
        }
        
        Hitbox attackHitbox = Collision.createAttackHitbox(
            player.getX(), player.getY(), player.getWidth(),
            player.getFacingDirection(), attack
        );
        
        for (Enemy enemy : level.getAllActiveEnemies()) {
            if (enemy.isDead() || enemy.isInvincible()) continue;
            
            if (Collision.checkHit(attackHitbox, enemy.getHurtbox(), 
                                   player.getDepth(), enemy.getDepth())) {
                enemy.takeHit(attack, player.getFacingDirection());
                attack.setHasHit(true);
                
                spawnHitEffect(enemy.getCenterX(), enemy.getCenterY());
                spawnFloatingText(enemy.getCenterX(), enemy.getY(), attack.getDamage());
                
                player.registerHit();
                comboDisplayTimer = 90;
                
                if (attack.getType() == Attack.Type.HEAVY) {
                    camera.triggerHeavyShake();
                } else {
                    camera.triggerLightShake();
                }
                
                if (enemy.isDead()) {
                    player.addScore(enemy.getScoreValue());
                }
                
                break;
            }
        }
        
        for (Prop prop : level.getProps()) {
            if (prop.isBroken() || !prop.isBreakable()) continue;
            
            if (Collision.checkPropCollision(attackHitbox, prop.getHitbox(),
                                             player.getDepth(), prop.getDepth())) {
                prop.takeDamage(attack.getDamage());
                attack.setHasHit(true);
                
                spawnHitEffect(prop.getX() + prop.getWidth() / 2, 
                              prop.getY() + prop.getHeight() / 2);
                
                camera.triggerLightShake();
                break;
            }
        }
    }
    
    private void processEnemyAttacks() {
        if (player.isDead() || player.isInvincible()) return;
        
        for (Enemy enemy : level.getAllActiveEnemies()) {
            if (enemy.isDead()) continue;
            
            Attack attack = enemy.getCurrentAttack();
            if (attack == null || !attack.isActive() || attack.hasHit()) continue;
            
            Hitbox attackHitbox = Collision.createAttackHitbox(
                enemy.getX(), enemy.getY(), enemy.getWidth(),
                enemy.getFacingDirection(), attack
            );
            
            if (Collision.checkHit(attackHitbox, player.getHurtbox(),
                                   enemy.getDepth(), player.getDepth())) {
                player.takeHit(attack, enemy.getFacingDirection());
                attack.setHasHit(true);
                
                spawnHitEffect(player.getCenterX(), player.getCenterY());
                spawnFloatingText(player.getCenterX(), player.getY(), attack.getDamage());
                
                camera.triggerLightShake();
                
                player.setInvincible(Constants.INVINCIBILITY_DURATION);
            }
        }
    }
    
    private void spawnHitEffect(double x, double y) {
        for (HitEffect effect : hitEffects) {
            if (!effect.isActive()) {
                effect.trigger(x, y);
                break;
            }
        }
    }
    
    private void spawnFloatingText(double x, double y, int damage) {
        for (FloatingText text : floatingTexts) {
            if (!text.isActive()) {
                text.triggerDamage(x, y - 20, damage);
                break;
            }
        }
    }
    
    @Override
    public void render(Graphics2D g2d, Renderer renderer) {
        renderer.begin(g2d, camera.getX(), camera.getY());
        
        level.render(g2d, camera.getX(), camera.getY());
        
        player.render(g2d, camera.getX(), camera.getY());
        
        for (HitEffect effect : hitEffects) {
            effect.render(g2d, camera.getX(), camera.getY());
        }
        for (FloatingText text : floatingTexts) {
            text.render(g2d, camera.getX(), camera.getY());
        }
        
        renderUI(g2d, renderer);
        
        if (player.isDead()) {
            renderGameOver(g2d, renderer);
        } else if (level.isBossDefeated()) {
            renderVictory(g2d, renderer);
        }
    }
    
    private void renderUI(Graphics2D g2d, Renderer renderer) {
        renderer.fillRectScreen(0, 0, Constants.SCREEN_WIDTH, 50, 
                               new Color(20, 22, 28, 200));
        
        renderer.drawUIText(player.getCharacterType().getName(), 
                           Constants.UI_PADDING, 30, player.getCharacterType().getColor());
        
        renderer.drawHPBar(Constants.UI_PADDING + 80, 15, Constants.HP_BAR_WIDTH, 
                          Constants.HP_BAR_HEIGHT, player.getCurrentHp(), 
                          player.getMaxHp(), Constants.COLOR_HP_BAR, Constants.COLOR_HP_BG);
        
        String hpText = player.getCurrentHp() + "/" + player.getMaxHp();
        renderer.drawSmallText(hpText, Constants.UI_PADDING + 85, 30, Constants.COLOR_TEXT);
        
        String scoreText = "SCORE: " + player.getScore();
        renderer.drawUIText(scoreText, Constants.SCREEN_WIDTH - 150, 30, Constants.COLOR_TEXT);
        
        if (comboDisplayTimer > 0 && player.getComboHits() > 1) {
            String comboText = player.getComboHits() + " HITS!";
            float alpha = Math.min(1.0f, comboDisplayTimer / 30.0f);
            Color comboColor = new Color(255, 220, 100, (int)(alpha * 255));
            renderer.drawMenuTextCentered(comboText, 100, comboColor);
        }
        
        int segmentNum = level.getCurrentSegmentIndex() + 1;
        String segmentText = "AREA " + segmentNum + "/4";
        renderer.drawSmallText(segmentText, Constants.SCREEN_WIDTH / 2 - 30, 30, 
                              new Color(150, 150, 150));
    }
    
    private void renderGameOver(Graphics2D g2d, Renderer renderer) {
        renderer.fillRectScreen(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, 
                               new Color(0, 0, 0, 180));
        
        renderer.drawTitleTextCentered("GAME OVER", Constants.SCREEN_HEIGHT / 2 - 50, 
                                      new Color(200, 50, 50));
        
        String scoreText = "Final Score: " + player.getScore();
        renderer.drawMenuTextCentered(scoreText, Constants.SCREEN_HEIGHT / 2 + 20, 
                                     Constants.COLOR_TEXT);
        
        renderer.drawSmallTextCentered("Press R to return to menu", 
                                      Constants.SCREEN_HEIGHT / 2 + 80, new Color(150, 150, 150));
        renderer.drawSmallTextCentered("Press ESC to quit", 
                                      Constants.SCREEN_HEIGHT / 2 + 105, new Color(150, 150, 150));
    }
    
    private void renderVictory(Graphics2D g2d, Renderer renderer) {
        renderer.fillRectScreen(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, 
                               new Color(0, 0, 0, 180));
        
        renderer.drawTitleTextCentered("VICTORY!", Constants.SCREEN_HEIGHT / 2 - 50, 
                                      new Color(255, 220, 100));
        
        String scoreText = "Final Score: " + player.getScore();
        renderer.drawMenuTextCentered(scoreText, Constants.SCREEN_HEIGHT / 2 + 20, 
                                     Constants.COLOR_TEXT);
        
        renderer.drawSmallTextCentered("Press R to return to menu", 
                                      Constants.SCREEN_HEIGHT / 2 + 80, new Color(150, 150, 150));
        renderer.drawSmallTextCentered("Press ESC to quit", 
                                      Constants.SCREEN_HEIGHT / 2 + 105, new Color(150, 150, 150));
    }
    
    @Override
    public GameState getNextState() {
        return nextState;
    }
    
    @Override
    public boolean shouldTransition() {
        return shouldTransition;
    }
}