package cometogether.States;

import cometogether.States.MovementState;
import cometogether.States.ObstacleState;
import cometogether.GameObjects.GameObject;
import cometogether.GameObjects.PlayerBox;
import cometogether.GamePanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Game State class. Creates other states and sets up gameplay.
 * @author Connor
 */
public class GameState {
    
    private ObstacleState obstacleState;
    private MovementState movementState;
    
    private boolean gameRunning;
    private boolean update = true;
    
    private Dimension screenSize;
    private int GWIDTH;
    private int GHEIGHT;
    
    private ConcurrentHashMap gameObjects;
    private ConcurrentHashMap obstacles;
    
    private String levelString;
    private int level;
    
    private BufferedImage bImg;

    private final GamePanel gamePanel;
    
    /**
     * Default constructor.
     * @param _gamePanel JPanel where game is drawn.
     */
    public GameState(GamePanel _gamePanel) {
        this.gamePanel = _gamePanel;
        this.gameObjects = new ConcurrentHashMap();
        this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.GWIDTH = screenSize.width;
        this.GHEIGHT = screenSize.height;
        this.bImg = new BufferedImage(GWIDTH, GHEIGHT, BufferedImage.TYPE_INT_ARGB);
        this.obstacleState = new ObstacleState(this);
        this.movementState = new MovementState(this);
        this.level = 4;
    }
    
    /**
     * Sets up a new game.
     * if (win), increments number of obstacles (level)
     * else, decrements number of obstacles
     * @param win 
     */
    public void newGame(boolean win) {
        nextLevel(win);
        pauseGame();
        createPlayer();
        levelString = "Level: " + (level-5);
        obstacleState.generateObstacles();
        addObstacles();
        startGame();
    }
     
    /**
     * Resets movement state, and starts the game.
     */
    private void startGame() {
        movementState.reset();
        gameRunning = true;
        update = true;
        runGameLoop();
    }
    
    /**
     * Adds obstacles to the game.
     */
    private void addObstacles() {
        obstacles = obstacleState.getObstacles();
        gameObjects.putAll(obstacles);
    }
    
    /**
     * Increments or decrements level based upon win.
     * @param win 
     */
    private void nextLevel(boolean win) {
        if (win) {
            level++;            
        } else {
            level--;
        }
    }
    
    /**
     * Pauses the update loop.
     */
    private void pauseGame() {
        gameRunning = false;
    }

    /**
     * Creates player geometry.
     */
    private void createPlayer() {
        gameObjects.clear();
        PlayerBox pb = new PlayerBox(new Rectangle2D.Double(-GWIDTH/2, 0,
                100,100), Color.green);
        pb.setName("PlayerBox");
        PlayerBox pb2 = new PlayerBox(new Rectangle2D.Double(GWIDTH/2-150, 0,
                100,100), Color.red);
        pb2.setName("PlayerBox2");
        gameObjects.put("PlayerBox", pb);
        gameObjects.put("PlayerBox2", pb2);
    }
    
    /**
     * @return height of GamePanel.
     */
    public int getHeight() {
        return GHEIGHT;
    }
    
    /**
     * @return width of GamePanel.
     */
    public int getWidth() {
        return GWIDTH;
    }
    
    /**
     * @return Current level (number of obstacles)
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Creates and runs the game loop.
     */
    private void runGameLoop() {
        Thread loop = new Thread() {
            
            @Override
            public void run() {
                gameLoop();
            }
        };
        loop.start();
    }
    
    /**
     * @return BufferedImage of current game.
     */
    public BufferedImage getImage() {
        return bImg;
    }
    
    /**
     * Updates the game.
     */
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
            for (Iterator it = gameObjects.values().iterator(); it.hasNext();) {
                GameObject go =  (GameObject) it.next();
                if (go.getPaint() != null) {
                    bg2d.setPaint(go.getPaint());
                } else {
                    bg2d.setColor(go.getColor());                   
                }
                bg2d.fill(go.getShape());
            }
            update = false;
            gamePanel.repaint();
        }
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
        if (gameObjects.containsKey(name)) {
            return (GameObject) gameObjects.get(name);
        } else {
            return null;
        }
    } 
    
    public ObstacleState getObstacleState() {
        return obstacleState;
    }
    
    public MovementState getMovementState() {
        return movementState;
    }
    
    public void exit() {
        System.exit(0);
    }
}
