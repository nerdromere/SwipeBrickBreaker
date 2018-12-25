package AlphaPackage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import sun.net.www.content.image.jpeg;

/**
 *
 * @author larik
 */
public class Ball {
    private static int diameter = 10;
    private static Color ballColor = Color.BLUE;
    int x = 0;
    int y = 0;
    int dx = 1;
    int dy = -1;
    private Game game;
    static int startX;
    static int startY;
    static int endX;
    static int endY;
    
    public Ball(Game game, int startX, int startY){
        this.game = game;
        this.x = startX;
        this.y = startY;
    }
    
    public void paint(Graphics2D g){
        g.setColor(ballColor);
        g.fillOval(x, y, diameter, diameter);
    }
    
    public void move(){
        //if ball goes too far left
        if(x + dx < 0){
            dx = 1;
        }
        //if ball goes too far right
        if(x + dx > Game.getWindowWidth() - diameter){
            dx = -1;
        }
        //if ball goes too far up
        if(y + dy < 0){
            dy = 1;
        }
        //if ball
        if(y + dy > Game.getWindowHeight() - diameter){
            dx = 0;
            dy = 0;
        }
        if(collision()){
            System.out.println("Collided");
            dy *= -1;
        }
        x += dx;
        y += dy;
        System.out.println(x + " " + y + " " + dx + " " + dy);
    }
    
    private boolean collision(){
        for (int i = 0; i < Game.getRows(); i++) {
            for (int j = 0; j < Game.getCols(); j++) {
                if(game.grid[i][j].getBounds().intersects(getBounds()) && game.grid[i][j].num > 0)
                    return true;
            }
        }
        return false;
    }
    
    private Rectangle getBounds(){
        return new Rectangle(x, y, diameter, diameter);
    }
}
