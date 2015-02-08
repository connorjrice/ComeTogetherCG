
package cometogether.Listeners;

import cometogether.GamePanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Connor
 */
public class KeyboardListener implements KeyListener {
    private final GamePanel canvasPanel;
    
    public KeyboardListener(GamePanel _canvasPanel) {
        this.canvasPanel = _canvasPanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            canvasPanel.getGame().exit();
        } 
        if (e.getKeyCode() == KeyEvent.VK_W) {
            canvasPanel.getGame().moveUp();
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            canvasPanel.getGame().moveLeft();
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            canvasPanel.getGame().moveDown();
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            canvasPanel.getGame().moveRight();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            canvasPanel.getGame().movementToggle();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
