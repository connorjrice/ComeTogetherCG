package cometogether.States;

import cometogether.GameObjects.GameObject;
import cometogether.GameObjects.PlayerBox;
import cometogether.GamePanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
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
    private GraphicsState graphicsState;
    
    private boolean gameRunning;
    private boolean update = true;
    
    private Dimension screenSize;
    private int GWIDTH;
    private int GHEIGHT;
    
    private ConcurrentHashMap gameObjects;
    private ConcurrentHashMap obstacles;
    
    private int level;
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

        this.obstacleState = new ObstacleState(this);
        this.movementState = new MovementState(this);
        this.graphicsState = new GraphicsState(this);
        this.level = 4;
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
                Thread.sleep(15);
            } catch (InterruptedException ex) {
                Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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

    
    private void update(double delta) {
        if (checkPlayerPlayerCollision()) {
            newGame(true);
        }
        obstacleState.checkObstaclePlayerCollision();
        graphicsState.updateBufferedImage();
        gamePanel.repaint();
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
        graphicsState.reset();
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
    
    public void setUpdate(boolean b) {
        update = b;
    }
    
    public boolean getUpdate() {
        return update;
    }
    
    public ConcurrentHashMap getGameObjects() {
        return gameObjects;
    }
    
    public BufferedImage getImage() {
        return graphicsState.getImage();
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
