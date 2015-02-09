
package cometogether.States;

import cometogether.GameObjects.GameObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;

/**
 *
 * @author Connor
 */
public class GraphicsState {
    private GameState game;
    private BufferedImage bImg;
    private String levelString;
    private int GWIDTH, GHEIGHT;
    
    public GraphicsState(GameState game) {
        this.game = game;
        this.GWIDTH = game.getWidth();
        this.GHEIGHT = game.getHeight();
        this.bImg = new BufferedImage(game.getWidth(), 
                game.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }
    
    public void reset() {
        levelString = "Level: " + (game.getLevel()-5);
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
    public void updateBufferedImage() {
        if (game.getUpdate()) {
            bImg = new BufferedImage(game.getWidth(), game.getHeight(), 
                    BufferedImage.TYPE_INT_ARGB);
            
            Graphics2D bg2d = (Graphics2D) bImg.getGraphics();
            
            bg2d.setColor(Color.white);
            bg2d.drawString(levelString, 10, 10);
            bg2d.setColor(Color.black);
            bg2d.fillRect(0,0,GWIDTH, GHEIGHT);
            bg2d.translate(GWIDTH/2, GHEIGHT/2);
            bg2d.scale(1,-1);
            
            for (Iterator it = game.getGameObjects().values().iterator(); it.hasNext();) {
                GameObject go =  (GameObject) it.next();
                if (go.getPaint() != null) {
                    bg2d.setPaint(go.getPaint());
                } else {
                    bg2d.setColor(go.getColor());                   
                }
                bg2d.fill(go.getShape());
            }
            game.setUpdate(true);
        }
    }
}
