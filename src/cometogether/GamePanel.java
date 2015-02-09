package cometogether;

import cometogether.States.GameState;
import cometogether.Listeners.MouseListener;
import cometogether.Listeners.KeyboardListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import javax.swing.JPanel;

/**
 *
 * @author Connor
 */
public class GamePanel extends JPanel {

    private final KeyboardListener keyL;
    private final MouseListener mouseL;
    private GameState game;

    public GamePanel() {
        
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.keyL = new KeyboardListener(this);
        this.mouseL = new MouseListener(this);
        this.game = new GameState(this);
        addMouseMotionListener(mouseL);
        addMouseListener(mouseL);
        addKeyListener(keyL);
        setFocusable(true);
        game.newGame(true);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(game.getImage(), 0,0, this);
    }
    
    public GameState getGame() {
        return game;
    }
    

}