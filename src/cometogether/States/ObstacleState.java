
package cometogether.States;

import cometogether.Game;
import cometogether.GameObjects.GameObject;
import cometogether.GameObjects.ObstacleBox;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author Connor
 */
public class ObstacleState {
    private Game game;
    private Random random;
    private ArrayList<GameObject> obstacles;
    private GradientPaint grad;
    
    public ObstacleState(Game g) {
        this.game = g;
        this.random = new Random();
        grad = new GradientPaint(new Point(0,0), 
                Color.white, new Point(500,50), Color.blue);
    }
    
    public ArrayList<GameObject> getObstacles() {
        return obstacles;
    }
    
    public void generateObstacles() {
        obstacles = new ArrayList<>();
        for (int i = 0; i < game.getLevel(); i++) {
            boolean added = false;
            int attempts = 0;
            while (!added) {
                int nextX = nextRandX();
                int nextY = nextRandY();
                ObstacleBox ob = new ObstacleBox(new Rectangle2D.Double(nextX, 
                        nextY, 50, 50), getPaint(nextX, nextY));
                if (!checkObstacleObstacleCollision(ob) && 
                        !checkObstaclePlayerCollision()) {
                    obstacles.add(ob);
                    added = true;
                } else {
                    if (attempts > 10) {
                        generateObstacles();
                        break;
                    } else {
                        attempts++;                        
                    }

                }

            }
        }
    }
    
    
    private GradientPaint getPaint(int x, int y) {
        Point p1 = new Point(x+5,y);
        p1.translate(game.getWidth()/2, game.getHeight()/2);
        Point p2 = new Point(x-50,y-50);
        p2.translate(game.getWidth()/2, game.getHeight()/2);
        return new GradientPaint(p1, getColor(), p2, getColor());
    }
    
    private Color getColor() {
        return new Color(nextColorInt(), nextColorInt(), nextColorInt());
    }
    
    private int nextColorInt(){
        return random.nextInt(256);
    }
    
    public boolean checkObstaclePlayerCollision() {
        boolean collision = false;
        Iterator<GameObject> it = obstacles.iterator();
        while (it.hasNext()) {
            GameObject obstacle = it.next();
            if (obstacle != null) {
                if (game.getObject("PlayerBox") != null) { 
                    if (game.getObject("PlayerBox").getShape().intersects(obstacle.getShape().getBounds())) {
                        collision = true;
                    }
                }
                if (game.getObject("PlayerBox2") != null) {
                    if (game.getObject("PlayerBox2").getShape().intersects(obstacle.getShape().getBounds())) {
                        collision = true;
                    }
                }
            }
        }
        return collision;
    }
    
        /**
     * Checks the input parameter obstacle with the ArrayList of obstacles.
     * @param ob
     * @return 
     */
    private boolean checkObstacleObstacleCollision(ObstacleBox ob) {
        boolean collision = false;
        if (ob != null) {
            Iterator<GameObject> it = obstacles.iterator();
            while (it.hasNext()) {
                GameObject obstacle = it.next();
                if (obstacle != null) {
                    if (ob.getShape().intersects(obstacle.getShape().getBounds())) {
                        collision = true;
                    }
                }
            }
        }
        return collision;
        
    }
        
    private int nextRandX() {
        if (random.nextBoolean()) {
            return random.nextInt((game.getWidth() - 50)/2 );
        } else {
            return -random.nextInt((game.getWidth() - 50)/2);
        }

    }
    
    private int nextRandY() {
        if (random.nextBoolean()) {
            return random.nextInt(game.getHeight()/2);
        } else {
            return -random.nextInt(game.getHeight()/2);
        }
    }

}
