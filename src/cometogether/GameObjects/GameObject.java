
package cometogether.GameObjects;

import java.awt.Color;
import java.awt.Shape;

/**
 *
 * @author Connor
 */
public abstract class GameObject {
    
    private Shape shape;
    private Color color;
    
    public void setShape(Shape s) {
        this.shape = s;
    }
    
    public void setColor(Color c) {
        this.color = c;
    }
    
    public Shape getShape() {
        return shape;
    }
    
    public Color getColor() {
        return color;
    }
    
}
