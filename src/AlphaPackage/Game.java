package AlphaPackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author larik
 */
public class Game extends JPanel {

    final static int ROWS = 9;
    final static int COLS = 6;
    final static int WIDTH = 320;
    final static int HEIGHT = 320;
    int startX;
    int startY;
    int endX;
    int endY;
    int ballYRest = WIDTH / 2;
    private int level = 0;
    public Block[][] grid = new Block[ROWS][COLS];
    ArrayList<Ball> balls = new ArrayList<>(5);

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("This is my phone");
        frame.setLocationRelativeTo(null);
        Game game = new Game();
        frame.add(game);
        frame.setSize(340, 420);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.initGame();
        game.nextLevel();
        while(true){
            game.move();
            game.repaint();
            Thread.sleep(10);
        }
    }
    
    private void initGame() {
        level = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j] = new Block(i, j, 0, 0, new Color(255, 0, 0 ));
            }
        }
    }
    
    private void nextLevel(){
        //move rows down
        for (int i = ROWS - 1; i > 1; i--) {
            //update row level
            for (int j = 0; j < COLS; j++) {
                grid[i - 1][j].row++;
                //System.out.println(grid[i - 1][j]);
            }
            grid[i] = grid[i - 1].clone();
        }
        if(!isAlive()){
             JOptionPane.showMessageDialog(null, "Uh-oh, you died!", "Final Score: " + level, 
                     JOptionPane.WARNING_MESSAGE);
             initGame();
        } else {
            level++;
            balls.add(new Ball(this, WIDTH / 2, HEIGHT - 10));
            boolean[] top = getRandom(0);
            for (int i = 0; i < COLS; i++) {
                grid[1][i] = new Block(1, i, 0, level, new Color(255, 0, 0));
                if (top[i]) {
                    grid[1][i].num = level;
                }
            }
        }
    }
    
    private boolean isAlive(){
        boolean alive = true;
        for(int i = 0; i < COLS; i++){
            if(grid[ROWS - 1][i].num != 0) {
                alive = false;
            }
        }
        return alive;
    }
    
    public boolean[] getRandom(int difficulty){
        boolean[] spawn = new boolean[COLS];
        for (int i = 0; i < COLS; i++) {
            spawn[i] = (Math.random()) > 0.5 ? true : false;
        }
        return spawn;
    }
    
    private void move(){
        for(Ball b : balls) {
            b.move();
        }
    }
    
    /**
     * Repaints the whole screen
     * @param g 
     */
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j].paint(g2d);
            }
        }
        for(Ball b : balls) {
            b.paint(g2d);
        }
        g.drawLine(0, HEIGHT, WIDTH, HEIGHT);
        g.drawLine(0, HEIGHT + 1, WIDTH, HEIGHT + 1);
    }
    
    private void startTurn(){
        int xChange = endX - startX;
        int yChange = endY - startY;
    }

    public Game() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
            }

            @Override
            public void mousePressed(MouseEvent e){
                
            }

            @Override
            public void mouseReleased(MouseEvent e){
                endX = e.getX();
                endY = e.getY();
                startTurn();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        setFocusable(true);
        this.setSize(WIDTH, HEIGHT);
    }
    
        public static int getWindowWidth(){
        return WIDTH;
    }
    
    public static int getWindowHeight(){
        return HEIGHT;
    }
    
    public static int getRows(){
        return ROWS;
    }
    
    public static int getCols(){
        return COLS;
    }
    private void coordinates(Graphics2D g, MouseEvent e){
        //g.drawString("(" + e.getXOnScreen() + ", " + e.getYOnScreen() + ")", 0, 0);
    }
}
