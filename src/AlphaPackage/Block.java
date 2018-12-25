/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlphaPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author larik
 */
public class Block {

    final static Color firstColor = new Color(255, 65, 82);
    final static Color lastColor = new Color(243, 158, 115);
    int fontSize = 13;
    Font font = new Font("TimesRoman", Font.BOLD, fontSize);
    int num = 0;
    int max = 0;
    Color background;
    int row;
    int column;

    /**
     * Default constructor
     */
    public Block() {
        row = 0;
        column = 0;
    }

    /**
     * Our Block constructor
     *
     * @param row the row it is in
     * @param column the column it is in
     * @param num the number it shows the player
     * @param max the number it started with, or the maximum number it shows
     * throughout its life
     * @param color color of the block; changes as num goes down
     */
    public Block(int row, int column, int num, int max, Color color) {
        this.row = row;
        this.column = column;
        this.num = num;
        this.max = max;
        this.background = color;
    }

    public void paint(Graphics2D g) {
        if (num > 0) {
            g.setColor(background);
            g.fillRect(column * Game.getWindowWidth() / Game.getCols(),
                    row * Game.getWindowHeight() / Game.getRows(),
                    Game.getWindowWidth() / Game.getCols(), Game.getWindowHeight() / Game.getRows());
            g.setColor(Color.BLACK);
            paintNumber(g);
        }
    }

    @Override
    public String toString() {
        return row + " " + column + " " + num + " " + column * Game.getWindowWidth() / Game.getCols() + " "
                + row * Game.getWindowHeight() / Game.getRows();
    }

    public Rectangle getBounds() {
        return new Rectangle(column * Game.getWindowWidth() / Game.getCols(),
                row * Game.getWindowHeight() / Game.getRows(),
                Game.getWindowWidth() / Game.getCols(), Game.getWindowHeight() / Game.getRows());
    }

    private void paintNumber(Graphics2D g) {
        g.setFont(font);
        g.setColor(Color.WHITE);
        if (num > 10) {
            fontSize = 9;
            g.drawString(num + "", column * Game.getWindowWidth() / Game.getCols() + Game.getWindowWidth() / Game.getCols() / 2 - fontSize / 2,
                    row * Game.getWindowHeight() / Game.getRows() + Game.getWindowHeight() / Game.getRows() / 2 + fontSize / 2);
        } else if (num > 100) {
            fontSize = 9;
        } else if (num > 1000) {
            fontSize = 8;
        } else {
            g.drawString(num + "", column * Game.getWindowWidth() / Game.getCols() + Game.getWindowWidth() / Game.getCols() / 2,
                    row * Game.getWindowHeight() / Game.getRows() + Game.getWindowHeight() / Game.getRows() / 2 + fontSize / 2);
        }
    }

    public static Color changeColorTo(int num, int max) {
        double percentage = 1 - (double) num / max;
        int dr = (int) ((lastColor.getRed() - firstColor.getRed()) * percentage);
        int dg = (int) ((lastColor.getGreen() - firstColor.getGreen()) * percentage);
        int db = (int) ((lastColor.getBlue() - firstColor.getBlue()) * percentage);
        System.out.println(num + " " + max + " " + (firstColor.getRed() + dr) + " " + (firstColor.getGreen() + dg) + " " + (firstColor.getBlue() + db));
        return new Color(firstColor.getRed() + dr, firstColor.getGreen() + dg, firstColor.getBlue() + db);
    }
}
