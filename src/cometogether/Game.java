package cometogether;

import cometogether.GameObjects.GameObject;
import cometogether.GameObjects.PlayerBox;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Connor
 */
public class Game {
    
    private ObstacleState obstacleState;
    private MovementState movementState;
    
    private boolean gameRunning;
    private int GWIDTH = 1920;
    private int GHEIGHT = 1200;
    private final ArrayList<GameObject> gameObjects;
    private ArrayList<GameObject> obstacles;
    private String timerString;
    private String levelString;
    private int level;
    private double timer;
    private BufferedImage bImg;
    private BufferedImage lImg;
    private boolean update = true;

    private final GamePanel gamePanel;
    
    public Game(GamePanel _gamePanel) {
        this.gamePanel = _gamePanel;
        this.gameObjects = new ArrayList<>();
        this.bImg = new BufferedImage(GWIDTH, GHEIGHT, BufferedImage.TYPE_INT_ARGB);
        this.obstacleState = new ObstacleState(this);
        this.movementState = new MovementState(this);
        this.level = 4;
    }
    
    public void newGame(boolean win) {
        nextLevel(win);
        pauseGame();

        createPlayer();
        levelString = "Level: " + (level-5);
  
        obstacleState.generateObstacles();
        addObstacles();

        startGame();

    }
    
    private void addObstacles() {
        obstacles = obstacleState.getObstacles();
        gameObjects.addAll(obstacles);
    }
    
    private void nextLevel(boolean win) {
        if (win) {
            level++;            
        } else {
            level--;
        }
    }
    
    private void pauseGame() {
        gameRunning = false;
    }
    
    private void startGame() {
        movementState.reset();
        gameRunning = true;
        update = true;
        runGameLoop();
    }
    
    private void createPlayer() {
        gameObjects.clear();
        PlayerBox pb = new PlayerBox(new Rectangle2D.Double(-GWIDTH/2, 0,
                100,100), Color.green);
        pb.setName("PlayerBox");
        PlayerBox pb2 = new PlayerBox(new Rectangle2D.Double(GWIDTH/2-150, 0,
                100,100), Color.red);
        pb2.setName("PlayerBox2");
        gameObjects.add(pb);
        gameObjects.add(pb2);
    }
    
    public int getHeight() {
        return GHEIGHT;
    }
    
    public int getWidth() {
        return GWIDTH;
    }
    
    public int getLevel() {
        return level;
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
            bg2d.setColor(Color.white);
            bg2d.drawString(levelString, 10, 10);
            bg2d.setColor(Color.black);
            bg2d.fillRect(0,0,GWIDTH, GHEIGHT);
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
    
    public void setUpdate(boolean newUpdate) {
        update = newUpdate;
    }
    
    private void update(double delta) {
        if (checkPlayerPlayerCollision()) {
            newGame(true);
        }
        obstacleState.checkObstaclePlayerCollision();
        updateBufferedImage();
    }
    
    public boolean checkPlayerPlayerCollision() {
        return(getObject("PlayerBox").getShape().intersects(
                getObject("PlayerBox2").getShape().getBounds()));
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
    
    public ObstacleState getObstacleState() {
        return obstacleState;
    }
    
    public MovementState getMovementState() {
        return movementState;
    }
    
    public void exit() {
        System.exit(1);
    }
}
