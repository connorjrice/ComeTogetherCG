
package cometogether.GameObjects;

import java.awt.Color;
import java.awt.Shape;

/**
 *
 * @author Connor
 */
public class ObstacleBox extends GameObject {
    
    public ObstacleBox(Shape s, Color c) {
        setShape(s);
        setColor(c);
        setName("Obstacle");
    }

}
