package cometogether;

import javax.swing.JFrame;

/**
 *
 * @author Connor
 */
public class ComeTogether {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame jf = new JFrame("Come Together");
        GamePanel cp = new GamePanel();
        jf.setDefaultCloseOperation(3);
        jf.getContentPane().getGraphicsConfiguration().getDevice().setFullScreenWindow(jf);
        jf.add(cp);
    }
    
}
