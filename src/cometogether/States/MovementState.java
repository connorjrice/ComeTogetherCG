
package cometogether.States;

/**
 *
 * @author Connor
 */
public class MovementState {
    private GameState game;
    private int lastMouseX, lastMouseY;
    private boolean mouseMotion = false;
    private boolean mouseSwitch = false;

    public MovementState(GameState _g) {
        this.game = _g;
    }
    
    public void reset() {
        lastMouseX = 0;
        lastMouseY = 0;
        mouseMotion = false;
    }
    
    public void mouseClick(int x, int y) {
        lastMouseX = x;
        lastMouseY = y;
        mouseMotion = !mouseMotion;
    }
    
    public void moveMouse(int x, int y) {
        if (mouseMotion) {
            if (!game.getObstacleState().checkObstaclePlayerCollision()) {
                game.setUpdate(true);
                if (!mouseSwitch) {
                    game.getObject("PlayerBox").translate(x - lastMouseX, lastMouseY - y);
                    game.getObject("PlayerBox2").translate(lastMouseX - x, y - lastMouseY);
                    
                } else {
                    game.getObject("PlayerBox").translate(x - lastMouseX, lastMouseY - y);
                    game.getObject("PlayerBox2").translate(x - lastMouseX, lastMouseY - y);
                }
                lastMouseX = x;
                lastMouseY = y;
            } else {
                game.newGame(false);
            }
       }
    }
    
    public void moveUp() {
        game.setUpdate(true);
        game.getObject("PlayerBox").translate(0, 10);
        game.getObject("PlayerBox2").translate(0, -10);
    }
    
    public void moveDown() {
        game.setUpdate(true);
        game.getObject("PlayerBox").translate(0, -10);
        game.getObject("PlayerBox2").translate(0, 10);
    }
    
    public void moveLeft() {
        game.setUpdate(true);
        game.getObject("PlayerBox").translate(-10, 0);
        game.getObject("PlayerBox2").translate(10, 0);        
    }
    
    public void moveRight() {
        game.setUpdate(true);
        game.getObject("PlayerBox").translate(10, 0);
        game.getObject("PlayerBox2").translate(-10, 0);        
    }
    
    public void movementToggle() {
        mouseSwitch = !mouseSwitch;
    }
}
