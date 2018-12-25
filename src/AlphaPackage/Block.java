/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlphaPackage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author larik
 */
public class Block {
    int num = 0;
    int max = 0;
    private Color background;
    int row;
    int column;
    
    public Block(){
        row = 0;
        column = 0;
    }
    
    public Block(int row, int column, int num, int max, Color color){
        this.row = row;
        this.column = column;
        this.num = num;
        this.max = max;
        this.background = color;
    }
    
    public void paint(Graphics2D g) {
        if(num > 0) {
            g.setColor(background);
            g.fillRect(column * Game.getWindowWidth() / Game.getCols(), 
                row * Game.getWindowHeight() / Game.getRows(), 
                Game.getWindowWidth() / Game.getCols(), Game.getWindowHeight() / Game.getRows());
            g.setColor(Color.BLACK);
            g.drawString(num + "", column * Game.getWindowWidth() / Game.getCols(),
                    row * Game.getWindowHeight() / Game.getRows() + Game.getWindowHeight() / Game.getRows());
        }
    }
    

    @Override
    public String toString() {
        return row + " " + column + " " + num + " " + column * Game.getWindowWidth() / Game.getCols() + " "
                + row * Game.getWindowHeight() / Game.getRows();
    }
    
    public Rectangle getBounds(){
        return new Rectangle(column * Game.getWindowWidth() / Game.getCols(), 
                row * Game.getWindowHeight() / Game.getRows(), 
                Game.getWindowWidth() / Game.getCols(), Game.getWindowHeight() / Game.getRows());
    }
}
