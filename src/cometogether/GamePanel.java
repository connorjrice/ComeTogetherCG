
package cometogether;

import cometogether.Listeners.MouseListener;
import cometogether.Listeners.KeyboardListener;
import cometogether.GameObjects.PlayerBox;
import cometogether.GameObjects.GameObject;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JPanel;

/**
 *
 * @author Connor
 */
public class GamePanel extends JPanel {
    private int GWIDTH = 1920;
    private int GHEIGHT = 1200;
    private final KeyboardListener keyL;
    private final MouseListener mouseL;
    private final HashMap gameObjects;
    private GameObject selectedObject;
    private boolean gameRunning;
    private long lastFpsTime;
    private int fps;
    private int lastMouseX, lastMouseY;
    private boolean mouseMotion = false;
    private boolean mouseSwitch = false;
    
    public GamePanel() {
        this.keyL = new KeyboardListener(this);
        this.mouseL = new MouseListener(this);
        this.gameObjects = new HashMap();
        
        addMouseMotionListener(mouseL);
        addMouseListener(mouseL);
        addKeyListener(keyL);
        setFocusable(true);
        newGame();
    }
    
    private void newGame() {
        PlayerBox pb = new PlayerBox(new Rectangle2D.Double(-GWIDTH/2, 0, 100,100));
        PlayerBox pb2 = new PlayerBox(new Rectangle2D.Double(GWIDTH/2-100, 0, 100,100));
        gameObjects.put("PlayerBox", pb);
        gameObjects.put("PlayerBox2", pb2);
        gameRunning = true;
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
    

    public void gameLoop() {
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 100000000 / TARGET_FPS;   
        
        while (gameRunning) { // Loop until game ends.
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double)OPTIMAL_TIME);
            
            lastFpsTime += updateLength;
            fps++;
            
            if (lastFpsTime >= 1000000000) {
                lastFpsTime = 0;
                fps = 0;
            }
            
            update(delta);
            repaint();
            
            /*try {
                Thread.sleep( (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );
            } catch (InterruptedException ex) {
                Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
            }*/

        }
    }
    
    private void update(double delta) {
        
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(GWIDTH/2, GHEIGHT/2);
        g2d.scale(1,-1);
        for (Iterator it = gameObjects.values().iterator(); it.hasNext();) {
            GameObject go = (GameObject) it.next();
            g2d.setColor(go.getColor());
            g2d.fill(go.getShape());
        }
    }
    
  /*  public void selectObject(int x, int y) {
        boolean overwriteSelectedObject = true;
        for (Iterator it = gameObjects.values().iterator(); it.hasNext();) {
            GameObject go = (GameObject) it.next();
            if (go.getShape().contains(x,y)) {
                selectedObject = go;
                overwriteSelectedObject = false;
            }
        }
        if (overwriteSelectedObject) {
            selectedObject = null;
        }

    }
    
    public void moveObject() {
        if (selectedObject != null) {
            selectedObject.translate(1, 0);
        }
    }*/
    
    public void mouseClick(int x, int y) {
        lastMouseX = x;
        lastMouseY = y;
        mouseMotion = true;
    }
    
    public void moveMouse(int x, int y) {
        if (mouseMotion) {
            if (!mouseSwitch) {
                getObject("PlayerBox").translate(x - lastMouseX, lastMouseY - y);
                getObject("PlayerBox2").translate(lastMouseX - x, y - lastMouseY);
  
            } else {
                getObject("PlayerBox").translate(lastMouseX - x, lastMouseY - y);
                getObject("PlayerBox2").translate(lastMouseX - x, lastMouseY - y);
            }
            lastMouseX = x;
            lastMouseY = y;
        }
    }
    
    public void moveUp() {
        getObject("PlayerBox").translate(0, 10);
        getObject("PlayerBox2").translate(0, -10);
    }
    
    public void moveDown() {
        getObject("PlayerBox").translate(0, -10);
        getObject("PlayerBox2").translate(0, 10);
    }
    
    public void moveLeft() {
        getObject("PlayerBox").translate(-10, 0);
        getObject("PlayerBox2").translate(10, 0);        
    }
    
    public void moveRight() {
        getObject("PlayerBox").translate(10, 0);
        getObject("PlayerBox2").translate(-10, 0);        
    }
    
    public void movementToggle() {
        mouseSwitch = !mouseSwitch;
    }
    
    public GameObject getObject(String name) {
        if (gameObjects.containsKey(name)) {
            return (GameObject) gameObjects.get(name);
        } else {
            return null;
        }
    } 
    
    public void exit() {
        System.exit(1);
    }
}