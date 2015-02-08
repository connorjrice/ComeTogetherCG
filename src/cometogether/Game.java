
package cometogether;

import cometogether.GameObjects.Background;
import cometogether.GameObjects.GameObject;
import cometogether.GameObjects.ObstacleBox;
import cometogether.GameObjects.PlayerBox;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Connor
 */
public class Game {
    private boolean gameRunning;
    private int GWIDTH = 1920;
    private int GHEIGHT = 1200;
    private final ArrayList<GameObject> gameObjects;
    private ArrayList<GameObject> obstacles;
    private String timerString;
    private String levelString;
    private int level;
    private double timer;
    private Background background;
    private Random random;
    private BufferedImage bImg;
    private BufferedImage lImg;
    private boolean update = true;
    private int lastMouseX, lastMouseY;
    private boolean mouseMotion = false;
    private boolean mouseSwitch = false;
    private final GamePanel gamePanel;
    
    public Game(GamePanel _gamePanel) {
        this.gamePanel = _gamePanel;
        this.gameObjects = new ArrayList<>();
        this.background = new Background(new Rectangle2D.Double(0,0,2000,2000),
                Color.BLACK);
        this.random = new Random();
        this.bImg = new BufferedImage(GWIDTH, GHEIGHT, BufferedImage.TYPE_INT_ARGB);
        this.level = 4;
    }
    
    public void newGame(boolean win) {
        if (win) {
            level++;            
        } else {
            level--;
        }

        gameRunning = false;
        gameObjects.clear();
        PlayerBox pb = new PlayerBox(new Rectangle2D.Double(-GWIDTH/2, 0,
                100,100), Color.green);
        pb.setName("PlayerBox");
        PlayerBox pb2 = new PlayerBox(new Rectangle2D.Double(GWIDTH/2-150, 0,
                100,100), Color.red);
        pb2.setName("PlayerBox2");

        levelString = "Level: " + (level-5);
        gameObjects.add(pb);
        gameObjects.add(pb2);
        generateObstacles();
        lastMouseX = 0;
        lastMouseY = 0;
        gameRunning = true;
        update = true;
        mouseMotion = false;
        runGameLoop();
    }
    
    

    private void generateObstacles() {
        obstacles = new ArrayList<>();
        for (int i = 0; i < level; i++) {
            boolean added = false;
            while (!added) {
                ObstacleBox ob = new ObstacleBox(new Rectangle2D.Double(nextRandX(), 
                        nextRandY(), 50, 50), Color.blue);
                if (!checkObstacleObstacleCollision(ob) && 
                        !checkObstaclePlayerCollision()) {
                    gameObjects.add(ob);
                    obstacles.add(gameObjects.get(gameObjects.size()-1));
                    added = true;
                }

            }

        }
    }
    
    private int nextRandX() {
        if (random.nextBoolean()) {
            return random.nextInt((GWIDTH - 50)/2 );
        } else {
            return -random.nextInt((GWIDTH - 50)/2);
        }

    }
    
    private int nextRandY() {
        if (random.nextBoolean()) {
            return random.nextInt(GHEIGHT/2);
        } else {
            return -random.nextInt(GHEIGHT/2);
        }
    }
    
    private void runGameLoop() {
        Thread loop = new Thread() {
            
            @Override
            public void run() {
                gameLoop();
            }
        };
        loop.start();
    }
    
    public BufferedImage getImage() {
        return bImg;
    }
    
    
    
    
    private void updateBufferedImage() {
        if (update) {
            bImg = new BufferedImage(GWIDTH, GHEIGHT, 
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D bg2d = (Graphics2D) bImg.getGraphics();
            bg2d.setColor(Color.black);
            bg2d.fillRect(0, 0, GWIDTH, GHEIGHT);
            bg2d.setColor(Color.white);
            bg2d.drawString(levelString, 10, 10);
            bg2d.translate(GWIDTH/2, GHEIGHT/2);
            bg2d.scale(1,-1);
            for (GameObject go : gameObjects) {
                bg2d.setColor(go.getColor());
                bg2d.draw(go.getShape());
            }
            update = false;
            gamePanel.repaint();
        }

    }
    
    private String getTimer() {
        return Double.toString(timer);
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
    
    private boolean checkObstaclePlayerCollision() {
        boolean collision = false;
        Iterator<GameObject> it = obstacles.iterator();
        while (it.hasNext()) {
            GameObject obstacle = it.next();
            if (getObject("PlayerBox") != null) { 
                if (getObject("PlayerBox").getShape().intersects(obstacle.getShape().getBounds())) {
                    collision = true;
                }
            }
            if (getObject("PlayerBox2") != null) {
                if (getObject("PlayerBox2").getShape().intersects(obstacle.getShape().getBounds())) {
                    collision = true;
                }
            }
        }
        return collision;
        
    }

    public void gameLoop() {
        long lastLoopTime = System.nanoTime();
        final long OPTIMAL_TIME = 1666666;  
        
        while (gameRunning) { // Loop until game ends.
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double)OPTIMAL_TIME);
            

            update(delta);
            try {
                Thread.sleep(25);
            } catch (InterruptedException ex) {
                Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            

        }
    }
    
    private void update(double delta) {
        if (checkPlayerPlayerCollision()) {
            newGame(true);
        }
        checkObstaclePlayerCollision();
        updateBufferedImage();
    }
    
    public boolean checkPlayerPlayerCollision() {
        return(getObject("PlayerBox").getShape().intersects(
                getObject("PlayerBox2").getShape().getBounds()));
    }
    
    public void mouseClick(int x, int y) {
        lastMouseX = x;
        lastMouseY = y;
        mouseMotion = !mouseMotion;
    }
    
    public void moveMouse(int x, int y) {
        if (mouseMotion) {
            if (!checkObstaclePlayerCollision()) {
                update = true;
                if (!mouseSwitch) {
                    getObject("PlayerBox").translate(x - lastMouseX, lastMouseY - y);
                    getObject("PlayerBox2").translate(lastMouseX - x, y - lastMouseY);
                    
                } else {
                    getObject("PlayerBox").translate(x - lastMouseX, lastMouseY - y);
                    getObject("PlayerBox2").translate(x - lastMouseX, lastMouseY - y);
                }
                lastMouseX = x;
                lastMouseY = y;
            } else {
                newGame(false);
            }
       }
    }
    
    
    public boolean checkObstacleCollision(int x, int y) {

        return false;
    }
    
    
    public void moveUp() {
        update = true;
        getObject("PlayerBox").translate(0, 10);
        getObject("PlayerBox2").translate(0, -10);
    }
    
    public void moveDown() {
        update = true;
        getObject("PlayerBox").translate(0, -10);
        getObject("PlayerBox2").translate(0, 10);
    }
    
    public void moveLeft() {
        update = true;
        getObject("PlayerBox").translate(-10, 0);
        getObject("PlayerBox2").translate(10, 0);        
    }
    
    public void moveRight() {
        update = true;
        getObject("PlayerBox").translate(10, 0);
        getObject("PlayerBox2").translate(-10, 0);        
    }
    
    public void movementToggle() {
        mouseSwitch = !mouseSwitch;
    }
    
    public GameObject getObject(String name) {
        
        Iterator<GameObject> it = gameObjects.iterator();
        while (it.hasNext()) {
            GameObject go = it.next();
            if (go != null) {
                if (go.getName().equals(name)) {
                    return go;
                }
            }
        }
        return null;
    } 
    
    public void exit() {
        System.exit(1);
    }
}
