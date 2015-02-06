
package cometogether.GameObjects;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

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
    
    public void translate(int x, int y) {
        AffineTransform at = new AffineTransform();
        at.setToTranslation(x, y);
        setShape(at.createTransformedShape(getShape()));
    }
    
    public void setX() {
        
    }
    
}
