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
        //canvasPanel.selectObject(e.getX(), e.getY());
        canvasPanel.mouseClick(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        canvasPanel.mouseClick(e.getX(), e.getY());
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
        System.out.println("moved");
        canvasPanel.moveMouse(e.getX(), e.getY());
    }

}
