/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlphaPackage;

import java.awt.Rectangle;
import javafx.scene.shape.Circle;

/**
 *
 * @author larik
 */
public class Utilities {
    /**
     * Will improve collision mechanism later, right now it's super inefficient
     * @param block
     * @param circle
     * @return 
     */
    public static boolean hitsSides(Rectangle block, Rectangle circle){
        /*check if any part of the ball is vertically inline with the block,
          so check if the top or bottom of the ball is within the top or 
          bottom of the block*/
        if((block.y <= circle.y && circle.y <= block.y + block.height) ||
           (block.y <= circle.y + circle.height && circle.y + circle.height <= block.y + block.height)){
            /*Now we know that the ball is virtically aligned with the block,
              and this if statement checks if the two objects are touching;
              double if for readability*/
            if((circle.x + circle.width == block.x) || 
               (circle.x == block.x + block.width)) {
                return true;
            }
        }
        return false;
    }
    public static boolean hitsPoles(Rectangle block, Rectangle circle){
        if((block.x <= circle.x && circle.x <= block.x + block.width) ||
           (block.x <= circle.x + circle.width && circle.x + circle.width <= block.x + block.width)){
            
            if((circle.y + circle.height == block.y) || 
               (circle.y == block.y + block.height)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean hitsRightSide(Game game, Rectangle circle, int dx, int dy){
        for (int i = 1; i < Game.COLS; i++) {
            /*if the left side of the ball has crossed one of the vertical
              lines AND it is traveling to the left, i.e., hits the right 
              side of the block*/
            if(circle.x == (int)(i * ((double) Game.WIDTH / Game.COLS)) && dx < 0){
                /*Check if there is a block at that block*/
                if(game.grid[(int)((circle.y + circle.height / 2) / (Game.HEIGHT / Game.ROWS))][i].num > 0)
                    return true;
            }
        }
        return false;
    }
    
    public static boolean hitsLeftSide(Game game, Rectangle circle, int dx, int dy){
        for (int i = 1; i < Game.COLS; i++) {
            /*could condense this all into one if statement; this is for 
              readability purposes*/
            if(circle.x + circle.width == (int)(i * ((double) Game.WIDTH / Game.COLS)) && dx > 0){
                /*Check if there is a block at that block*/
                if(game.grid[(int)((circle.y + circle.height / 2)/ (Game.HEIGHT / Game.ROWS))][i].num > 0)
                    return true;
            }
        }
        return false;
    }
    
    public static boolean hitsTopSide(Game game, Rectangle circle, int dx, int dy){
        for (int i = 1; i < Game.ROWS; i++) {
            /*if the left side of the ball has crossed one of the vertical
              lines AND it is traveling to the left, i.e., hits the right 
              side of the block*/
            if(circle.y + circle.height == (int)(i * ((double) Game.HEIGHT / Game.ROWS)) && dy > 0){
                /*Check if there is a block at that block*/
                if(game.grid[i][(int)((circle.x + circle.width / 2) / (Game.WIDTH / Game.COLS))].num > 0)
                    return true;
            }
        }
        return false;
    }
    
    public static boolean hitsBottomSide(Game game, Rectangle circle, int dx, int dy){
        for (int i = 1; i < Game.ROWS; i++) {
            /*if the left side of the ball has crossed one of the vertical
              lines AND it is traveling to the left, i.e., hits the right 
              side of the block*/
            if(circle.y == (int)(i * ((double) Game.HEIGHT / Game.ROWS)) && dy < 0){
                /*Check if there is a block at that block*/
                if(game.grid[i - 1][(int)((circle.x + circle.width / 2) / (Game.WIDTH / Game.COLS))].num > 0)
                    return true;
            }
        }
        return false;
    }
    
    
    
    /**
     * Credit to e.James on StackOverflow
     * @param circle
     * @param rect
     * @return 
     */
    public static boolean intersects(Circle circle, Rectangle rect)
{
    int circleDistanceX = (int)Math.abs(circle.getCenterX() - (rect.x-rect.getWidth()/2)); 
    int circleDistanceY = (int)Math.abs(circle.getCenterY() - (rect.y-rect.getWidth()/2));
    
    if (circleDistanceX > (int)(rect.width/2 + circle.getRadius())) { return false; }
    if (circleDistanceY > (int)(rect.height/2 + circle.getRadius())) { return false; }

    if (circleDistanceX <= (rect.width/2)) { return true; }
    if (circleDistanceY <= (rect.height/2)) { return true; }

    double cornerDistance_sq = Math.pow(circleDistanceX - rect.width/2, 2) +
                         Math.pow(circleDistanceY - rect.height/2, 2);

    return (cornerDistance_sq <= (circle.getRadius() * circle.getRadius()));
}
}
