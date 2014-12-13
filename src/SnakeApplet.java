
import java.applet.Applet;
import java.awt.Dimension;

public class SnakeApplet extends Applet {

    SnakeCanvas game;
    
    @Override
    public void init() {
        game = new SnakeCanvas();
        
        game.setPreferredSize(new Dimension(640,480));
        game.setVisible(true);
        game.setFocusable(true);
        
        this.add(game);
        this.setVisible(true);
        this.setSize(new Dimension(640, 480));
    }
}
