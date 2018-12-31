package AlphaPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.ImageIcon;
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
    final static int WIDTH = 512;
    final static int HEIGHT = 512;
    final static int WINDOWERROR = 7;
    final static int phoneWidth = WIDTH + WINDOWERROR;
    final static int phoneHeight = HEIGHT + 100;
    final static Color lineColor = new Color(235, 201, 166, 128);
    final static Color arrowColor = new Color(91, 167, 244, 128);
    //the little bars which form the line that user dragged/ predicted line
    final static int lengthOfBar = 7;
    //the spacing between bars
    final static int lengthOfSegment = lengthOfBar + 5;
    final static double ratio = 1.0 * lengthOfBar / lengthOfSegment;
    double slope = 0;
    //when mouse is clicked
    int startX = -1;
    int startY = -1;
    int endX = -1;
    int endY = -1;
    //int ballYRest = WIDTH / 2;
    private int level = 0;
    private boolean mouseHeld = false;
    private boolean aimStage = false;
    public Block[][] grid = new Block[ROWS][COLS];
    ArrayList<Ball> balls = new ArrayList<>(5);
    Set<Point> corners = new HashSet<Point>();

   public Game() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nextLevel();
            }

            @Override
            public void mousePressed(MouseEvent e){
                if(startX < 0){
                    startX = endX = e.getX();
                    startY = endY = e.getY();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e){
                aimStage = false;
                Ball.turnStarted = true;
                //still need to implement
               // if(Ball.onLineWaiting == balls.size())
                    startTurn();
                startX = startY = endX = endY = -1;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                aimStage = true;
                if(0 <= e.getX()|| e.getX() <= WIDTH)
                    endX = e.getX();
                if(0 <= e.getY()|| e.getY() <= HEIGHT)
                    endY = e.getY();
                int xChange = endX - startX;
                int yChange = endY - startY;
                slope = (double) yChange / xChange;
                if (slope < 0.2 && slope >= 0 || (endX < startX && endY > startY)
                        || slope == Double.POSITIVE_INFINITY)
                    slope = 0.2;
                if (slope > -0.2 && slope < 0 || (startX < endX && startY <= endY))
                    slope = -0.2;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                
            }
        });
        setFocusable(true);
        setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        //this.setSize(WIDTH, HEIGHT);
    }
    
    /**
     * Repaints the whole screen
     * @param g 
     */
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.WHITE);
        for(int i = 0; i < ROWS; i++){
            g.drawLine(0, i * HEIGHT / ROWS - 1, 
                    WIDTH, i * HEIGHT / ROWS - 1);
            g.drawLine(0, i * HEIGHT / ROWS - 2, 
                    WIDTH, i * HEIGHT / ROWS - 2);
        }
        for (int i = 0; i <= COLS; i++) {
            g.drawLine(i * Game.getWindowWidth() / Game.getCols(), 0,
                    i * Game.getWindowWidth() / Game.getCols(), HEIGHT);
            g.drawLine(i * Game.getWindowWidth() / Game.getCols() - 1, 0,
                    i * Game.getWindowWidth() / Game.getCols() - 1, HEIGHT);
        }
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j].paintShadow(g2d);
            }
        }
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j].paint(g2d);
            }
        }
        
        for(Ball b : balls) {
            b.paint(g2d);
        }
        g2d.setColor(Color.BLACK);
        //segment at the top
        g2d.drawLine(0, 0, WIDTH, 0);
        g2d.drawLine(0, 1, WIDTH, 1);
        g2d.drawLine(0, 2, WIDTH, 2);
        //segment at the bottom
        g2d.drawLine(0, HEIGHT, WIDTH, HEIGHT);
        g2d.drawLine(0, HEIGHT + 1, WIDTH, HEIGHT + 1);
        g2d.drawLine(0, HEIGHT + 2, WIDTH, HEIGHT + 2);
        
        for(Point p : corners){
            g2d.drawOval(p.x, p.y, 3, 3);
        }

        if(aimStage){
            //draw the firing line
            g2d.setColor(lineColor);
            drawFatPath(g2d, startX, startY, endX, endY);
            g2d.setColor(arrowColor);
            drawArrow(g2d);
        }
        if(mouseHeld){
//            drawFatPath(g, Ball.restPositionX, HEIGHT - Ball.diameter, 
//                    getEndPoint().x, getEndPoint().y);
        }
    }
    
    public void initGame() {
        level = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j] = new Block(i, j, 0, 0, new Color(255, 64, 81));
            }
        }
    }
    
    public void nextLevel(){
        //move rows down
        for (int i = ROWS - 1; i > 1; i--) {
            //update row level
            for (int j = 0; j < COLS; j++) {
                grid[i - 1][j].row++;
            }
            grid[i] = grid[i - 1].clone();
        }
        if(!isAlive()){
            for(Ball b : balls){
                 b.dx = 0;
                 b.dy = 0;
            }
            JOptionPane.showMessageDialog(null, "Uh-oh, you died!", "Final Score: " + level, 
                    JOptionPane.WARNING_MESSAGE);
            balls.clear();
            initGame();
        } else {
            level++;
            Ball.onLineWaiting = 0;
            Ball.restPositionSet = false;
            balls.add(new Ball(this, level));
            boolean[] top = getRandom(0);
            for (int i = 0; i < COLS; i++) {
                grid[1][i] = new Block(1, i, 0, level, new Color(255, 64, 81));
                if (top[i]) {
                    grid[1][i].num = level;
                }
            }
        }
    }
    
    /**
     * Looks where the ball will go after the release
     * @return Point at which the edge of the ball will hit...
     */
    private Point getEndPoint(double slope, int x, int y){
        Point p = new Point();
        return p;
    }
    /**
     * checks if the last last row contains any blocks
     * @return 
     */
    private boolean isAlive(){
        boolean alive = true;
        for(int i = 0; i < COLS; i++){
            if(grid[ROWS - 1][i].num != 0) {
                alive = false;
            }
        }
        return alive;
    }
    
    /**
     * Get random blocks at the top
     * @param difficulty increases as the levels increase
     * @return an array of booleans, should spawn or not.
     */
    public boolean[] getRandom(int difficulty){
        boolean[] spawn = new boolean[COLS];
        for (int i = 0; i < COLS; i++) {
            spawn[i] = (Math.random()) > 0.5 ? true : false;
        }
        return spawn;
    }
    
    public void move() throws InterruptedException{
        for(Ball b : balls) {
            b.move();
        }
    }
    
    public void startTurn(){
        Ball.turnStarted = true;
        Ball.levelFrameCounter = 0;
        for(Ball b : balls){
             b.slope = slope;
             b.initialY = HEIGHT - Ball.diameter;
             b.initialX = Ball.restPositionX;
             b.dy = -1;
             if(endX > startX)
                b.dx = 1;
             else
                b.dx = -1;
        }
        balls.get(0).waitingToGo = false;
    }
    
    public void drawFatPath(Graphics2D g, int x1, int y1, int x2, int y2){
        drawPath(g, startX, startY, endX, endY);
        drawPath(g, x1 + 1, y1, x2 + 1, y2);
        drawPath(g, x1 - 1, y1, x2 - 1, y2);
        drawPath(g, x1, y1 + 1, x2, y2 + 1);
        drawPath(g, x1, y1 - 1, x2, y2 - 1);
    }
    
    /**
     * Draws the arrow which appears while user is in the aim stage
     * @param g graphics
     */
    public void drawArrow(Graphics2D g){
        int centerX = Ball.restPositionX + Ball.diameter / 2;
        int centerY = HEIGHT - Ball.diameter / 2;
        Point end = arrowEnd(slope, centerX, centerY);
        //g.drawPolygon(new int[], yPoints, 3);
        for (int i = -4; i <= 4; i++) {
            g.drawLine(centerX + i, centerY, end.x + i, end.y);
            g.drawLine(centerX, centerY + i, end.x, end.y + i);
        }
    }
    
    public Point arrowEnd(double ratio, int x, int y){
        int length = HEIGHT / 4;
        int dx = (int)(length * Math.sqrt(1 / (slope * slope + 1)));
        int dy = (int) (slope * dx);
        if(slope == Double.NEGATIVE_INFINITY){
            dy = -length;
            dx = 0;
        }
        if(slope > 0){
            dx *= -1;
            dy *= -1;
        }
        if (Math.abs(slope) > 40) {
            dy = -length;
            dx = 0;
        }
        return new Point(x + dx, y + dy);
    }
    
    /**
     * Just returns the 
     * @param ratio
     * @return 
     */
    private double ratioToRadians(double ratio){
        return Math.tan(ratio);
    }
    public void drawPath(Graphics2D g, int x1, int y1, int x2, int y2){
        double dx = 0, dy = 0;
        double amountSegments = (int)(distance(x1, y1, x2, y2) / lengthOfSegment);
        if(amountSegments != 0){
            dx = (x2 - x1) / amountSegments;
            dy = (y2 - y1) / amountSegments;
        }
        for(int i = 0; i < amountSegments; i++){
            g.drawLine((int)(x1 + i * dx), (int)(y1 + i * dy), 
                (int)(x1 + i * dx + ratio * dx), (int) (y1 + i * dy + ratio * dy));
        }
    }

    private double distance(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
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
    
    static class TopPane extends JPanel {
        public TopPane(){
            setFocusable(true);
            setBackground(Color.WHITE);
            setSize(WIDTH, HEIGHT / 10);
        }
    }
    
    public void createSet(){
        int wide = Game.WIDTH / Game.COLS;
        int high = Game.HEIGHT / Game.ROWS;
         for(int i = 1; i < Game.COLS; i++) {
            for (int j = 1; j < Game.ROWS; j++) {
                corners.add(new Point(i * wide, j * high));
            }
        }
    }
}