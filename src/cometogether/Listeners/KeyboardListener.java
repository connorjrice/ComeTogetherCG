
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
            canvasPanel.exit();
        } 
        if (e.getKeyCode() == KeyEvent.VK_W) {
            canvasPanel.moveUp();
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            canvasPanel.moveLeft();
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            canvasPanel.moveDown();
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            canvasPanel.moveRight();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            canvasPanel.movementToggle();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
