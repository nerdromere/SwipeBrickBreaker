/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlphaPackage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author larik
 */
public class Driver {
     public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("This is my phone");
        frame.setIconImage(new ImageIcon("src/Pictures/logo.jpg").getImage());
        frame.setLocationRelativeTo(null);
        Game game = new Game();
        Game.TopPane top = new Game.TopPane();
        frame.setLayout(new BorderLayout());
        frame.add(top, BorderLayout.NORTH);
        frame.add(game);
        frame.setSize(Game.phoneWidth, Game.phoneHeight);
        frame.setPreferredSize(new Dimension(Game.phoneWidth, Game.phoneHeight));
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.initGame();
        game.createSet();
        game.nextLevel();
        Statistics stats = new Statistics();
        stats.setIconImage(new ImageIcon("src/Pictures/logo.jpg").getImage());
        stats.setLocationRelativeTo(null);
        stats.setResizable(false);
        stats.setVisible(true);
        game.stats = stats;
        while(true){
            game.move();
            game.repaint();
            Thread.sleep(2);
//            if(Ball.turnEnded)
//                game.nextLevel();
        }
    }
}
