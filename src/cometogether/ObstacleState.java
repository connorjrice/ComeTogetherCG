
package cometogether;

import cometogether.GameObjects.GameObject;
import cometogether.GameObjects.ObstacleBox;
import java.awt.Color;
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
    
    public ObstacleState(Game g) {
        this.game = g;
        this.random = new Random();

    }
    
    public ArrayList<GameObject> getObstacles() {
        return obstacles;
    }
    
    public void generateObstacles() {
        obstacles = new ArrayList<>();
        for (int i = 0; i < game.getLevel(); i++) {
            boolean added = false;
            while (!added) {
                ObstacleBox ob = new ObstacleBox(new Rectangle2D.Double(nextRandX(), 
                        nextRandY(), 50, 50), Color.blue);
                if (!checkObstacleObstacleCollision(ob) && 
                        !checkObstaclePlayerCollision()) {
                    obstacles.add(ob);
                    added = true;
                }

            }
        }
    }
    
    public boolean checkObstaclePlayerCollision() {
        boolean collision = false;
        Iterator<GameObject> it = obstacles.iterator();
        while (it.hasNext()) {
            GameObject obstacle = it.next();
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
        return collision;
    }
    
        /**
     * Checks the input parameter obstacle with the ArrayList of obstacles.
     * @param ob
     * @return 
     */
    private boolean checkObstacleObstacleCollision(ObstacleBox ob) {
        boolean collision = false;
        Iterator<GameObject> it = obstacles.iterator();
        while (it.hasNext()) {
            GameObject obstacle = it.next();
            if (ob.getShape().intersects(obstacle.getShape().getBounds())) {
                collision = true;
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
