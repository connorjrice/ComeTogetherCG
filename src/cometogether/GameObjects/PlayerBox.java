
package cometogether.GameObjects;

import java.awt.Color;
import java.awt.Shape;

/**
 *
 * @author Connor
 */
public class PlayerBox extends GameObject {

    public PlayerBox() {
        
    }
    
    public PlayerBox(Shape s) {
        setShape(s);
    }
    
    public PlayerBox(Shape s, Color c) {
        setShape(s);
        setColor(c);
    }
    
}
