package AlphaPackage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
/**
 *
 * @author larik
 */
public class Ball {
    /*The position of the balls waiting for player to aim and release; used
      at the beginning of every turn; restPosition y will always be height
      minus ball diameter*/
    static int restPositionX = Game.WIDTH / 2;
    static int restPositionY = Game.HEIGHT - Ball.diameter;
    /*Set to true after player aims and releases; turns false after all the
      balls are in the line and waiting*/
    static boolean turnStarted = false;
    //determines if the rest position has already been set
    static boolean restPositionSet = false;
    /*Starts counting after turn has started and when a ball has oriented into
      the new rest position*/
    static int onLineWaiting = 0;
    /*This counter allows the balls to leave at a certain time, in order for
      them to not all go at once, but one after another*/
    static long levelFrameCounter = 0;
    /*Pretty self explanatory*/
    final static int diameter = 20;
    private static Color ballColor = new Color(91, 167, 244);
    
    //When ball has moved to its final resting position and is waiting
    boolean waitingForTurnToEnd = false;
    //Waiting to go after user has aimed and released
    boolean waitingToGo = false;
    //If the ball has left its resting position
    boolean leftRestPosition = false;
    //slope of the ball; calculated through the change in y over change in x
    double slope;
    /*The distance it travels during each iteration; calculated by setting
      1 (x) and slope (y) as the legs of a right triangle, and finiding both
      rise and run of a similiar triangle with a hypotnuse of 1*/
    double magX;
    double magY;
    /*Just an iteration to multiply the magnitudes by, in order for the 
      magnitudes and slope to round themselves, i.e., if slope is 0.789, adding
      it to y, (y += slope) will eventually add 0 due to the truncation. 
      Therefore, just doing mag * iteration will go over 1, and truncate
      every time, providing that "sporadic" rise and run. Iteration allows 
      proper rounding, and provides the complicated pixel trajectory. No need to
      calculate how many over and up.*/
    int iteration = 0;
    //use these coordinates everytime the ball changes direction
    int initialX = 0;
    int initialY = 0;
    //the position the ball is in, respect to the ball array
    int position;
    //real time updated values
    int x = 0;
    int y = 0;
    //direction of ball. Direction-x, Direction-y
    int dx = 0;
    int dy = 0;
    //the current game state
    private Game game;
    
    /**
     * Constructor used whenever a new ball is acquired
     * @param game state of the game for the ball to maneuver through
     */
    public Ball(Game game, int pos){
        this.game = game;
        this.position = pos;
        initialX = restPositionX;
        initialY = restPositionY;
        x = restPositionX;
        y = restPositionY;
    }
    
    /**
     * Paints the ball onto the canvas
     * @param g graphics2d object, paintbrush
     */
    public void paint(Graphics2D g){
        g.setColor(ballColor);
        g.fillOval(x, y, diameter, diameter);
    }
    
    /**
     * Determines where the ball needs to move based on position relative to
     * the walls, block edges, or block corners.
     */
    public void move(){
        levelFrameCounter++;
        //if ball goes too far left
        if(x + dx < 0){
            dx = Math.abs(dx);
            changeDirection();
        } 
        //if ball goes too far right
        if(x + dx > Game.getWindowWidth() - diameter){
            dx = -Math.abs(dx);
            changeDirection();
        }
        //if ball goes too far up
        if(y + dy < 0){
            //dy = Math.abs(dy);
            dy *= -1;
            changeDirection();
        }
        //if ball goes to the bottom
        if(y + dy > Game.getWindowHeight() - diameter && leftRestPosition){
            System.out.println("yeah im too low...");
            changeDirection();
            dx = 0;
            dy = 0;
            //
            if(!restPositionSet && turnStarted){
                restPositionX = x;
                restPositionSet = true;
            }
            if(translateToRest() == 0){
                leftRestPosition = false;
                waitingForTurnToEnd = true;
            }
//            dy = -Math.abs(dy);
        }
        if(restPositionSet && y == Game.getWindowHeight() - diameter){
            translateToRest();
        }
        if(collisionOnSides()){
            dx *= -1;
            changeDirection();
        }
        //System.out.println(Utilities.degree(game, this, dx, dy));
        hitCorner(Utilities.degree(game, this, dx, dy));
        if(collisionOnPoles()){
            dy *= -1;
            changeDirection();
        }
        if(levelFrameCounter > 100 * position){
            leftRestPosition = true;
        }
        if(turnStarted && leftRestPosition){ 
            magX = Math.abs(1.0 / Math.sqrt(1 + slope * slope));
            magY = Math.abs(slope / Math.sqrt(1 + slope * slope));
            System.out.println(slope + " " + magX + " " + magY + " " + dx + " " + dy + " " + x + " " + y);
            x = (int)(initialX + magX * dx * iteration);
            y = (int)(initialY + magY * dy * iteration++);
        }
//        if(initialY != 0 && slope < 1){
//            y = initialY + (int) (slope * iteration++ * dx);
//            x += dx;
//        }
//        else if(initialY != 0 && slope >= 1){
//            x = initialX + (int) (1.0 / slope * iteration++ * dy);
//            y += dy;
//        }
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

    
    private void changeDirection(){
        iteration = 0;
        initialX = x;
        initialY = y;
    }
    
    /**
     * Changes the direction of the ball if it hits the corner
     * @param degree the place the ball is hit using 360 degrees
     */
    private void hitCorner(double degree){
        double travelAngle = 180 / Math.PI * Math.atan(slope);
        //System.out.println(travelAngle);
    }
    
    /**
     * Determines if the ball has hit the left or right side of a block.
     * Calculates in O(n) where n is the amount of columns, default 5.
     * @return true if hit, false if not
     */
    private boolean collisionOnSides(){
        if(Utilities.hitsRightSide(game, getBounds(), dx, dy)){
            int r = (int)((y + diameter / 2) / (Game.HEIGHT / Game.ROWS));
            int c = (int)(x / (Game.WIDTH / Game.COLS) - 1);
            //System.out.println(r + " " + c);
            game.grid[r][c].num--;
            game.grid[r][c].background = Block.changeColorTo(game.grid[r][c].num, game.grid[r][c].max);
            return true;
        }
        if(Utilities.hitsLeftSide(game, getBounds(), dx, dy)){
            int r = (int)((y + diameter / 2) / (Game.HEIGHT / Game.ROWS));
            int c = (int)(x / (Game.WIDTH / Game.COLS)+ 1);
            //System.out.println(r + " " + c);
            game.grid[r][c].num--;
            game.grid[r][c].background = Block.changeColorTo(game.grid[r][c].num, game.grid[r][c].max);
            return true;
        }
        return false;
    }
    
    /**
     * Determines if the ball has hit the top or bottom of any block.
     * Calculates in O(n) where n is the amount of rows, default 9.
     * @return True if the ball has hit either of the poles
     */
    private boolean collisionOnPoles(){
        
        if(Utilities.hitsTopSide(game, getBounds(), dx, dy)){
            int r = (int)(y / (Game.HEIGHT / Game.ROWS)) + 1;
            int c = (int)((x + diameter / 2) / (Game.WIDTH / Game.COLS));
            //System.out.println(r + " " + c);
            game.grid[r][c].num--;
            game.grid[r][c].background = Block.changeColorTo(game.grid[r][c].num, game.grid[r][c].max);
            return true;
        }
        if(Utilities.hitsBottomSide(game, getBounds(), dx, dy)){
            int r = (int)(y / ((double) Game.HEIGHT / Game.ROWS));
            int c = (int)((x + diameter / 2) / (Game.WIDTH / Game.COLS));
            //System.out.println(r + " " + c);
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
    
    /**
     * Moves the ball to the restPosition after landing on the ground; changes
     * direction of x if need be
     * @return the dx for checking purposes
     */
    private int translateToRest(){
        if(restPositionX < x)
            x += -1;
        else if(x < restPositionX)
            x += 1;
        else
            dx = 0;
        return dx;
    }
    
    /**
     * Creates a rectangle hitbox of the ball
     * @return Rectangle object with top left corner and diameter as 
     *   position and length respectively.
     */
    private Rectangle getBounds(){
        return new Rectangle(x, y, diameter, diameter);
    }
}