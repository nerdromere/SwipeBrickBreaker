package AlphaPackage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javafx.scene.shape.Circle;

/**
 *
 * @author larik
 */
public class Ball {
    static int restPositionX = -1;
    static boolean turnStarted = false;
    boolean leftRestPosition = false;
    final static int diameter = 20;
    private static Color ballColor = new Color(91, 167, 244);
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
            dx = Math.abs(dx);
        } 
        //if ball goes too far right
        if(x + dx > Game.getWindowWidth() - diameter){
            dx = -Math.abs(dx);
        }
        //if ball goes too far up
        if(y + dy < 0){
            dy = Math.abs(dy);
        }
        //if ball
        if(y + dy > Game.getWindowHeight() - diameter){
            dx = 0;
            dy = 0;
            if(restPositionX < 0){
                restPositionX = x;
            }
            translateToRest();
//            dy = -Math.abs(dy);
        }
        if(restPositionX > 0 && y == Game.getWindowHeight() - diameter){
            translateToRest();
        }
        if(collisionOnSides()){
            dx *= -1;            
        }
        
        if(collisionOnPoles()){
            dy *= -1;            
        }
        x += dx;
        y += dy;
    }
    
//    private boolean collisionOnSides(){
//        for (int i = 0; i < Game.getRows(); i++) {
//            for (int j = 0; j < Game.getCols(); j++) {
//                if(Utilities.hitsSides(game.grid[i][j].getBounds(), getBounds()) && game.grid[i][j].num > 0){
//                    game.grid[i][j].num--;
//                    game.grid[i][j].background = Block.changeColorTo(game.grid[i][j].num, game.grid[i][j].max);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
    
    private boolean collisionOnSides(){
        if(Utilities.hitsRightSide(game, getBounds(), dx, dy)){
            int r = (int)((y + diameter / 2) / (Game.HEIGHT / Game.ROWS));
            int c = (int)(x / (Game.WIDTH / Game.COLS) - 1);
            System.out.println(r + " " + c);
            game.grid[r][c].num--;
            game.grid[r][c].background = Block.changeColorTo(game.grid[r][c].num, game.grid[r][c].max);
            return true;
        }
        if(Utilities.hitsLeftSide(game, getBounds(), dx, dy)){
            int r = (int)((y + diameter / 2) / (Game.HEIGHT / Game.ROWS));
            int c = (int)(x / (Game.WIDTH / Game.COLS)+ 1);
            System.out.println(r + " " + c);
            game.grid[r][c].num--;
            game.grid[r][c].background = Block.changeColorTo(game.grid[r][c].num, game.grid[r][c].max);
            return true;
        }
        return false;
    }
    
    private boolean collisionOnPoles(){
        
        if(Utilities.hitsTopSide(game, getBounds(), dx, dy)){
            int r = (int)(y / (Game.HEIGHT / Game.ROWS)) + 1;
            int c = (int)((x + diameter / 2) / (Game.WIDTH / Game.COLS));
            System.out.println(r + " " + c);
            game.grid[r][c].num--;
            game.grid[r][c].background = Block.changeColorTo(game.grid[r][c].num, game.grid[r][c].max);
            return true;
        }
        if(Utilities.hitsBottomSide(game, getBounds(), dx, dy)){
            int r = (int)(y / ((double) Game.HEIGHT / Game.ROWS));
            int c = (int)((x + diameter / 2) / (Game.WIDTH / Game.COLS));
            System.out.println(r + " " + c);
            game.grid[r][c].num--;
            game.grid[r][c].background = Block.changeColorTo(game.grid[r][c].num, game.grid[r][c].max);
            return true;
        }
        return false;
    }
//    private boolean collisionOnPoles(){
//        for (int i = 0; i < Game.getRows(); i++) {
//            for (int j = 0; j < Game.getCols(); j++) {
//                if(Utilities.hitsPoles(game.grid[i][j].getBounds(), getBounds()) && game.grid[i][j].num > 0){
//                    game.grid[i][j].num--;
//                    game.grid[i][j].background = Block.changeColorTo(game.grid[i][j].num, game.grid[i][j].max);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
    
    private void translateToRest(){
        if(restPositionX < x)
            dx = -1;
        else if(x < restPositionX)
            dx = 1;
        else
            dx = 0;
    }
    
    private Rectangle getBounds(){
        return new Rectangle(x, y, diameter, diameter);
    }
}