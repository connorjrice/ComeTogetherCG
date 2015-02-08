package cometogether.Listeners;

import cometogether.GamePanel;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputListener;

/**
 *
 * @author Connor
 */
public class MouseListener implements MouseInputListener {
    private final GamePanel canvasPanel;
    
    public MouseListener(GamePanel _canvasPanel) {
        this.canvasPanel = _canvasPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        canvasPanel.getGame().getMovementState().mouseClick(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        canvasPanel.getGame().getMovementState().mouseClick(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }
    
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (100 < e.getXOnScreen() && e.getXOnScreen() < 1920 &&
                100 < e.getYOnScreen() && e.getYOnScreen() < 1200 ) {
            canvasPanel.getGame().getMovementState().
                    moveMouse(e.getXOnScreen(), e.getYOnScreen());            
        }

    }

}
