package ru.mipt.bit.platformer.models;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.ai.RandomAiController;
import ru.mipt.bit.platformer.interfaces.FieldObserver;
import ru.mipt.bit.platformer.interfaces.FieldObservable;
import ru.mipt.bit.platformer.interfaces.LevelRenderer;
import ru.mipt.bit.platformer.interfaces.TileObjectPositioner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Field implements FieldObservable {

    public static final boolean ENEMY_FRIENDLY_FIRE = true;

    private final LevelRenderer levelRenderer; 
    private final TiledMapTileLayer groundLayer;

    private final List<Tree> trees = new ArrayList<>();
    private final List<Tank> tanks = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList<>();
    private final java.util.Set<GridPoint2> blocked = new java.util.HashSet<>();

    private final TileObjectPositioner tileObjectPositioner;
    private final List<FieldObserver> observers = new CopyOnWriteArrayList<>();
    
    private Tank player;
    private RandomAiController aiController;
    private float movementSpeed;

    public boolean isEnemyFriendlyFire() {
        return ENEMY_FRIENDLY_FIRE;
    }

    public Field(LevelRenderer levelRenderer, TiledMapTileLayer groundLayer, TileObjectPositioner tileObjectPositioner) {
        this.levelRenderer = levelRenderer;
        this.groundLayer = groundLayer;
        this.tileObjectPositioner = tileObjectPositioner;
    }

    public TiledMapTileLayer ground() {
        return groundLayer;
    }

    public void render() {
        levelRenderer.render();
    }

    public boolean isInsideBounds(GridPoint2 c) {
        int width = groundLayer.getWidth();
        int height = groundLayer.getHeight();
        return c.x >= 0 && c.y >= 0 && c.x < width && c.y < height;
    }
    
    public boolean isBlocked(GridPoint2 c) {
        return blocked.contains(c);
    }

    public boolean playerCanMoveTo(GridPoint2 destination) {
        return isInsideBounds(destination) && !isBlocked(destination);
    }

    public void occupy(GridPoint2 coordinates) {
        blocked.add(new GridPoint2(coordinates));
    }

    public boolean reserveMove(GridPoint2 from, GridPoint2 to) {
        if (!isInsideBounds(to)) 
            return false;
        if (isBlocked(to)) 
            return false;

        blocked.add(new GridPoint2(to));
        return true;
    }

    public void finishMove(GridPoint2 from, GridPoint2 to) {
        blocked.remove(from);
        blocked.add(new GridPoint2(to));
    }

    public void renderTrees(Batch batch) {
        for (Tree tree : trees) {
            tree.render(batch);
        }
    }
    
    @Override
    public void addObserver(FieldObserver observer) {
        observers.add(observer);
    }
    
    @Override
    public void removeObserver(FieldObserver observer) {
        observers.remove(observer);
    }
    
    @Override
    public void notifyTankAdded(Tank tank) {
        for (FieldObserver observer : observers) {
            observer.onTankAdded(tank);
        }
    }
    
    @Override
    public void notifyTankRemoved(Tank tank) {
        for (FieldObserver observer : observers) {
            observer.onTankRemoved(tank);
        }
    }
    
    @Override
    public void notifyBulletAdded(Bullet bullet) {
        for (FieldObserver observer : observers) {
            observer.onBulletAdded(bullet);
        }
    }
    
    @Override
    public void notifyBulletRemoved(Bullet bullet) {
        for (FieldObserver observer : observers) {
            observer.onBulletRemoved(bullet);
        }
    }
    
    @Override
    public void notifyTreeAdded(Tree tree) {
        for (FieldObserver observer : observers) {
            observer.onTreeAdded(tree);
        }
    }
    
    @Override
    public void notifyTreeRemoved(Tree tree) {
        for (FieldObserver observer : observers) {
            observer.onTreeRemoved(tree);
        }
    }
    
    public void addTank(Tank tank) {
        tanks.add(tank);
        blocked.add(new GridPoint2(tank.getCoordinates()));
        notifyTankAdded(tank);
    }
    
    public void removeTank(Tank tank) {
        tanks.remove(tank);
        
        if (tank == player) {
            player = null;
        }
        
        GridPoint2 currentPos = tank.getCoordinates();
        blocked.remove(new GridPoint2(currentPos));
        
        if (tank.isMoving()) {
            GridPoint2 destinationPos = tank.getDestinationCoordinates();
            if (!destinationPos.equals(currentPos) && !isBlockedByTree(destinationPos)) {
                boolean hasOtherTank = false;
                for (Tank otherTank : tanks) {
                    if (otherTank != tank && otherTank.getCoordinates().equals(destinationPos)) {
                        hasOtherTank = true;
                        break;
                    }
                }
                if (!hasOtherTank) {
                    blocked.remove(new GridPoint2(destinationPos));
                }
            }
        }
        
        notifyTankRemoved(tank);
    }
    
    private boolean isBlockedByTree(GridPoint2 pos) {
        for (Tree tree : trees) {
            if (tree.getCoordinates().equals(pos)) {
                return true;
            }
        }
        return false;
    }
    
    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
        notifyBulletAdded(bullet);
    }
    
    public void removeBullet(Bullet bullet) {
        bullets.remove(bullet);
        notifyBulletRemoved(bullet);
    }
    
    public void addTree(Tree tree) {
        trees.add(tree);
        tileObjectPositioner.moveAtTileCenter(groundLayer, tree.getRectangle(), tree.getCoordinates());
        blocked.add(new GridPoint2(tree.getCoordinates()));
        notifyTreeAdded(tree);
    }
    
    public void removeTree(Tree tree) {
        trees.remove(tree);
        blocked.remove(new GridPoint2(tree.getCoordinates()));
        notifyTreeRemoved(tree);
    }
    
    public List<Tank> getTanks() {
        return new ArrayList<>(tanks);
    }
    
    public List<Bullet> getBullets() {
        return new ArrayList<>(bullets);
    }
    
    public void setPlayer(Tank player) {
        this.player = player;
    }
    
    public Tank getPlayer() {
        return player;
    }
    
    public void setAiController(RandomAiController aiController) {
        this.aiController = aiController;
    }
    
    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }
    
    public List<Tank> getEnemies() {
        List<Tank> enemies = new ArrayList<>();
        for (Tank tank : tanks) {
            if (tank != player) {
                enemies.add(tank);
            }
        }
        return enemies;
    }
    
    public void tickAll(float deltaTime) {
        List<Tank> enemies = getEnemies();
        for (Tank enemy : enemies) {
            if (enemy.getCurrentHealth() > 0) {
                enemy.tick(deltaTime, movementSpeed);
            }
        }
        
        if (aiController != null) {
            aiController.tick(deltaTime, enemies);
        }
        
        List<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            if (!bullet.isAlive()) {
                bulletsToRemove.add(bullet);
            } else {
                bullet.tick(deltaTime);
            }
        }
        for (Bullet bullet : bulletsToRemove) {
            removeBullet(bullet);
        }
        
        List<Tank> tanksToRemove = new ArrayList<>();
        for (Tank tank : tanks) {
            if (tank.getCurrentHealth() <= 0) {
                tanksToRemove.add(tank);
            }
        }
        for (Tank tank : tanksToRemove) {
            removeTank(tank);
        }
    }
}
