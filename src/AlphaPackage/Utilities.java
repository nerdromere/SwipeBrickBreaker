/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlphaPackage;

import java.awt.Rectangle;

/**
 *
 * @author larik
 */
public class Utilities {
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
}
