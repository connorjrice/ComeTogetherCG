
package cometogether;

import cometogether.Listeners.MouseListener;
import cometogether.Listeners.KeyboardListener;
import cometogether.GameObjects.PlayerBox;
import cometogether.GameObjects.GameObject;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Connor
 */
public class GamePanel extends JPanel {
    private final KeyboardListener keyL;
    private final MouseListener mouseL;
    private final ArrayList<GameObject> gameObjects;
    private boolean gameRunning;
    private long lastFpsTime;
    private int fps;
    
    public GamePanel() {
        this.keyL = new KeyboardListener(this);
        this.mouseL = new MouseListener(this);
        this.gameObjects = new ArrayList<>();
        addMouseListener(mouseL);
        addKeyListener(keyL);
        setFocusable(true);
        newGame();
    }
    
    private void newGame() {
        PlayerBox pb = new PlayerBox(new Rectangle2D.Double(getWidth()-1/2, getHeight()-1/2, 100,100));
        gameObjects.add(pb);
        runGameLoop();
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
    
    /**
     * Loop for game logic.
     * Modified from Kevin Glass's code.
     */
    public void gameLoop() {
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;   
        
        while (gameRunning) { // Loop until game ends.
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double)OPTIMAL_TIME);
            
            // update the frame counter
            lastFpsTime += updateLength;
            fps++;
            
            // update our FPS counter if a second has passed since
            // we last recorded
            if (lastFpsTime >= 1000000000) {
                System.out.println("(FPS: "+fps+")");
                lastFpsTime = 0;
                fps = 0;
            }
            
            update(delta);
            repaint();
            
            try {
                Thread.sleep( (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );
            } catch (InterruptedException ex) {
                Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
    private void update(double delta) {
        
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (GameObject go : gameObjects) {
            g2d.setColor(go.getColor());
            g2d.fill(go.getShape());
        }
    }
    
    public void exit() {
        System.exit(1);
    }
}