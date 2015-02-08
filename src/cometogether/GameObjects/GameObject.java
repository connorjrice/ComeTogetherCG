
package cometogether.GameObjects;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

/**
 *
 * @author Connor
 */
public abstract class GameObject {
    
    private Shape shape;
    private Color color;
    private String name;
    
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
    
    public void setName(String s) {
        this.name = s;
    }
    
    public String getName() {
        return name;
    }
    
    public void translate(int x, int y) {
        AffineTransform at = new AffineTransform();
        at.setToTranslation(x, y);
        setShape(at.createTransformedShape(getShape()));
    }
    
    public boolean checkCollision(int x, int y, int obstacles, HashMap objects) {
        AffineTransform at = new AffineTransform();
        at.setToTranslation(x, y);
        Shape s = at.createTransformedShape(getShape());
        for (int i = 0; i < obstacles; i++) {
            GameObject go = (GameObject) objects.get("Obstacle"+i);
            if (go.getShape().intersects(s.getBounds2D())) {
                return true;
            }
        }
        return false;
    }
    
    public void setX() {
        
    }
    
}
