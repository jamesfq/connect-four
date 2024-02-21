import java.awt.*;
import java.util.ArrayList;

/**
 * GUI that displays all of the checkers and determines when a player wins.
 *
 * @author James Quirk, Dartmouth DALI Developer Challenge!
 */
public class ConnectGUI extends DrawingGUI {
    // create graphics window of specified size
    private static final int width = 800;
    private static final int height = 600;

    // store the variables that correspond to each player's and the background's color
    private Color p1Color;
    private Color p2Color;
    private Color boardColor;

    // a boolean that alternates every turn to indicate color of each new checker and who wins at the end
    private boolean player1;

    // integers that store the size of the game board
    private int numRows;
    private int numColumns;

    // a boolean that indicates if a player has won a game by connecting four checkers
    private boolean winner;

    // stores the size of the checkers
    private int radius;

    // a variable that counts the number of checkers placed in the board so that if the board is filled, it announces a draw
    private int globalCount;

    // a boolean that indicates if the user is playing in Space Mode
    private boolean spaceMode;

    // in Space Mode, these arrays hold the stars that decorate the background of the game
    private ArrayList<Integer> starsX;
    private ArrayList<Integer> starsY;

    // list of all checkers placed at indices that correspond to their locations (null if none)
    private ArrayList<Checker> checkers;

    /**
     * Activates the graphics window in SPACE MODE because no arguments specified means CUSTOM mode was not chosen.
     */
    public ConnectGUI() {
        // start running the timer for the graphics
        super("Default Run", width, height);

        // set board size
        numRows = 6;
        numColumns = 7;
        checkers = new ArrayList<Checker>(numRows * numColumns);

        // initialize the null checkers in each slot of the array
        for (int i = 0; i < numRows * numColumns; i++) {
            checkers.add(null);
        }

        // set colors for both players and the board
        p1Color = Color.RED;
        p2Color = Color.YELLOW;
        boardColor = Color.BLACK;

        // default radius
        radius = 20;

        // no checkers yet, so count is 0
        globalCount = 0;

        // start with player 1, so true
        player1 = true;

        // no one has one yet, so false
        winner = false;

        // Space Mode is activated
        spaceMode = true;

        // initialize the x-coordinate of each star in the array
        starsX = new ArrayList<Integer>(500);

        for (int i = 0; i < 500; i++) {
            starsX.add((int)(Math.random()*width));
        }

        // initialize the y-coordinate of each star in the array
        starsY = new ArrayList<Integer>(500);

        for (int i = 0; i < 500; i++) {
            starsY.add((int)(Math.random()*height));
        }

        // start the timer
        startTimer();
    }

    /**
     * Activates the graphics window in CUSTOM MODE because arguments being specified means CUSTOM mode was chosen.
     */
    public ConnectGUI(int numRows, int numColumns, Color p1Color, Color p2Color, Color boardColor) {
        // same layout as the initialization found above
        super("Custom Run", width, height);

        this.numRows = numRows;
        this.numColumns = numColumns;

        this.p1Color = p1Color;
        this.p2Color = p2Color;
        this.boardColor = boardColor;

        globalCount = 0;

        radius = 20;

        checkers = new ArrayList<Checker>(numRows * numColumns);

        for (int i = 0; i < numRows * numColumns; i++) {
            checkers.add(null);
        }

        player1 = true;

        winner = false;

        spaceMode = false;

        // start the timer
        startTimer();
    }

    /**
     * Overrides the method defined in DrawingGUI
     * Handles the user's mouse location to transform it into a checker
     */
    @Override
    public void handleMousePress(int x, int y) {
        // initialize variables
        Color color;
        float sizeCol;
        float clickColumn;

        // boolean functions such that if a winner has already been found, don't continue
        if (!winner) {
            sizeCol = 1 / (float) numColumns; // change name to avoid confusion
            clickColumn = (float) x / (float) width;

            // the low bound and high bound that define a column
            int low = 0;
            int high = 1;

            // if the clickColumn is within the current bounds, continue, otherwise increment the bounds to test next range
            while (clickColumn < ((float) low) * sizeCol || clickColumn >= ((float) high) * sizeCol) {
                low++;
                high++;
            }

            // the location corresponds to the spot in the graph where the new checker is stored
            int location = low;

            // the count corresponds to the number of checkers in a given column
            int count = 0;

            // while the column chosen (location) is still within the board and there are non-null checkers in a slot
            while (location < numRows * numColumns && checkers.get(location) != null) {
                // move up exactly numColumns spots to stay in the same column but shift up one row
                location += numColumns;

                // indicate that there is another checker below the one added
                count++;
            }

            // if the index selected would be be out of the board's range, print error.
            if (location >= numRows * numColumns) {
                System.out.println("Column full! Select another column");
            }

            // otherwise, continue
            else {
                // if it's Player 1's turn, set the color to Player 1's color
                if (player1) {
                    color = p1Color;
                    player1 = false; // toggle to Player 2's turn
                }

                // if Player 2's turn, set the color to Player 2's color
                else {
                    color = p2Color;
                    player1 = true; // toggle to Player 1's turn
                }

                // make the new checker
                Checker newChecker = addChecker(low, count, color);

                // set the neighors of the checker if they exist
                assignNeighbors(newChecker, location);

                // place the checker in the array of checkers that are drawn
                checkers.set(location, newChecker);

                // check to see if the new checker won the game
                winner = winCheck(0, 0, newChecker);

                // if it was a win,
                if (winner) {
                    if (player1) { // this means we just toggled to Player 1, so it was Player 2 who played the last checker
                        System.out.println("Player 2 wins!");
                        System.out.println("Click 'p' to play again! Click 'q' to quit.");

                    }

                    else { // this means we just toggled off Player 1, so Player 1 played the last checker
                        System.out.println("Player 1 wins!");
                        System.out.println("Click 'p' while on the Connect Four GUI to play again! Click 'q' to quit.");
                    }
                }

                // if the total number of checkers reaches the limit, and there is still no winner,
                else if (globalCount == numRows*numColumns) {
                    System.out.println("This game was a draw!"); // indicate a draw
                    System.out.println("Click 'p' to play again! Click 'q' to quit.");
                }
            }
        }
    }

    /**
     * Helper function that makes a new checker for handleMousePress
     * @param low indicates the low bound of a column (which column we're in, starting from 0)
     * @param count indicates the number of checkers in the selected column
     */
    public Checker addChecker(int low, int count, Color color) {
        // set the location of the checker that will be drawn
        float locX = width*(1 + 2*low)/((float)numColumns*2);
        float locY = height - height*(1 + 2*count)/((float)numRows*2);
        Checker newChecker;

        // if in space mode, indicate moons and suns
        if (spaceMode) {
            if (player1) { // if Player 2 just played
                newChecker = new Checker((int)locX, (int)locY, 2, color); // moons

            }

            else { // if Player 1 just played
                newChecker = new Checker((int)locX, (int)locY, 1, color); // suns
            }
        }

        // otherwise, just draw a normal checker
        else {
            newChecker = new Checker((int)locX, (int)locY, radius, color);
        }

        // increment the number of checkers in the system
        globalCount++;
        repaint();

        return newChecker;
    }

    /**
     * Assigns the neighbors of a certain checker after initialization
     */
    public void assignNeighbors(Checker newChecker, int location) {
        // if checker is not in the first column
        if ((location % numColumns) != 0) {

            // if the checker is not in the last column
            if ((location % numColumns != numColumns - 1)) {

                // if there is a checker to the left
                if (checkers.get(location-1) != null) {
                    // set the new checker to be the old checker's neighbor and vice versa
                    newChecker.setLeft(checkers.get(location - 1));
                    (checkers.get(location - 1)).setRight(newChecker);
                }

                // and if there is a checker to the right
                if (checkers.get(location+1) != null) {
                    // set the new checker to be the old checker's neighbor and vice versa
                    newChecker.setRight(checkers.get(location + 1));
                    (checkers.get(location + 1)).setLeft(newChecker);
                }
            }

            // if the checker is in the last column
            else {

                // only look at the checker to the left because the checker to the "right" will be on the next row
                if (checkers.get(location-1) != null) {
                    newChecker.setLeft(checkers.get(location - 1));
                    (checkers.get(location - 1)).setRight(newChecker);
                }
            }
        }

        // if on the leftmost column
        else {

            // only look at the right neighbor
            if (checkers.get(location+1) != null) {
                newChecker.setRight(checkers.get(location + 1));
                (checkers.get(location + 1)).setLeft(newChecker);
            }
        }

        // if we're not on the bottom column
        if (location >= numColumns) {
            // mark the new checker's down neighbor and the old checker's up neighbor
            newChecker.setBelow(checkers.get(location - numColumns));
            (checkers.get(location - numColumns)).setAbove(newChecker);
        }
    }

    /**
     * DrawingGUI method, takes a key as an input and gives an output in the form of quitting or reinitializing the game
     */
    @Override
    public void handleKeyPress(char k) {

        // allows the user to quit the game
        if (k == 'q') {
            System.out.println("Thanks for playing!");
            System.exit(0);
        }

        // allows the user to play the game again
        if (k == 'p') {
            System.out.println("Playing again...");
            if (spaceMode) {
                new ConnectGUI();
            }
            
            else {
                new ConnectGUI(this.numRows, this.numColumns, this.p1Color, this.p2Color, this.boardColor);
            }
        }
    }

    /**
     * DrawingGUI method, draws the board and all of the checkers
     */
    @Override
    public void draw(Graphics g) {
        // Draws the board
        drawBoard(g);

        // Makes each checker draw itself
        for (Checker checker : checkers) {
            if (checker != null) {
                g.setColor(checker.c);
                checker.draw(g);
            }
        }
    }

    public void drawBoard(Graphics g) {
        // initialize row, column, and x, y location variables for board
        int r;
        int c;
        float locX;
        float locY;

        // set the color to whatever the user input as the board color or the default value
        g.setColor(boardColor);

        // fill the entire screen as that color
        g.fillRect(0, 0, width, height);

        // change the color to white for stars (in SPACE MODE) and holes in the board
        g.setColor(Color.WHITE);

        // if starsX and starsY have been initialized (in space mode)
        if (starsX != null && starsY != null) {

            // draw every star found in the array
            for (int i = 0; i < 500; i++) {
                g.drawOval(starsX.get(i), starsY.get(i), 1, 1);
            }
        }

        // for every row and every column
        for (c = 0; c < numColumns; c++) {

            for (r = 0; r < numRows; r++) {
                // set the x,y location of each white hole
                locX = width*(1 + 2*c)/((float)numColumns*2) - radius;
                locY = height*(1 + 2*r)/((float)numRows*2) - radius;

                // draw the white hole
                g.fillOval((int)locX, (int)locY, radius*2, radius*2);
            }
        }

        // draws lines between each column
        for (c = 0; c < numColumns; c++) {
            locX = width*(c/(float)numColumns) - 1;
            g.fillRect((int)locX, 0, 2, height);
        }

    }

    /**
     * Checks recursively if someone wins the game when placing a new checker
     */
    public boolean winCheck(int i, int direction, Checker newChecker) {
        // base case: this is the fourth item in the connection (starting from 0 and incrementing by 1)
        if (i == 3) {
            return true;
        }

        // on initial call, direction will be zero. This is to indicate that we MUST check EVERY direction around a checker
        // direction 2 indicates up right, 3 indicates right, etc. until 8 which indicates up left
        if (direction == 0) {
            // false because no one has won yet
            boolean result = false;

            // if the checker above and to the right of the new checker exists and has matching color, recurse to the next case
            // direction 2 indicates that the winCheck will only look directly UP for a connection
            // note, we skip the arbitrary "direction 1" which would mean a match above because a new checker
            // will never have an up neighbor as it is on top as similar to a stack data structure.

            // IMPORTANT NOTE: we check to the right THEN above because in order for an upper right neighbor to exist
            // it must have a down neighbor that it stacked upon to reach that row. If conversely we checked above then
            // right, we could be attempting to scan the right neighbor of an uninitialized, null, above neighbor
            if (newChecker.right != null && newChecker.right.above != null && newChecker.c == newChecker.right.above.c) { // determine why highlighted
                if (winCheck(i + 1, 2, newChecker.right.above)) {
                    result = true;
                }

                // IMPORTANT NOTE: this second case is essential in every check but the above and below check.
                // Fixes a problem when the user connects four by placing a middle checker
                // If we only had the first case, if a player placed the 3rd connection in a diagonal of 4 to win, nothing would happen
                // This case says, if there is a checker above and to the right, go there and try to make a connect 4 by going down and left
                // In the above case of placing the 3rd connection (from the left) in a diagonal
                // The pointer would shift to the top right most checker and attempt to find a connection in direction 6
                // Meaning down and to the left. Every possible connect 4 will be checked.

                if (winCheck(i, 6, newChecker.right.above)) {
                    result = true;
                }
            }

            // check to the right
            if (newChecker.right != null && newChecker.c == newChecker.right.c) {
                if (winCheck(i + 1, 3, newChecker.right)) {
                    result = true;
                }

                if (winCheck(i, 7, newChecker.right)) {
                    result = true;
                }
            }

            // check down and to the right
            if (newChecker.below != null && newChecker.below.right != null && newChecker.c == newChecker.below.right.c) {
                if (winCheck(i + 1, 4, newChecker.below.right)) {
                    result = true;
                }

                if (winCheck(i, 8, newChecker.below.right)) {
                    result = true;
                }
            }

            // check down
            if (newChecker.below != null && newChecker.c == newChecker.below.c) {
                if (winCheck(i + 1, 5, newChecker.below)) {
                    result = true;
                }

            }

            // check down and to the left
            if (newChecker.below != null && newChecker.below.left != null && newChecker.c == newChecker.below.left.c) {
                if (winCheck(i + 1, 6, newChecker.below.left)) {
                    result = true;
                }

                if (winCheck(i, 2, newChecker.below.left)) {
                    result = true;
                }
            }

            // check to the left
            if (newChecker.left != null && newChecker.c == newChecker.left.c) {
                if (winCheck(i + 1, 7, newChecker.left)) {
                    result = true;
                }

                if (winCheck(i, 3, newChecker.left)) {
                    result = true;
                }
            }

            // check up and to the left
            if (newChecker.left != null && newChecker.left.above != null && newChecker.c == newChecker.left.above.c) {
                if (winCheck(i + 1, 8, newChecker.left.above)) {
                    result = true;
                }

                if (winCheck(i, 4, newChecker.left.above)) {
                    result = true;
                }
            }

            return result;
        }

        // if told to recurse up and to the right, only look up and to the right
        if (direction == 2) {
            if (newChecker.right == null || newChecker.right.above == null || newChecker.c != newChecker.right.above.c) {
                return false;
            }

            return winCheck(i+1, direction, newChecker.right.above);
        }

        // if told to recurse right, only look to the right
        if (direction == 3) {
            if (newChecker.right == null || newChecker.c != newChecker.right.c) {
                return false;
            }

            return winCheck(i+1, direction, newChecker.right);
        }

        // if told to recurse down and to the right, only look up and to the right
        if (direction == 4) {
            if (newChecker.below == null || newChecker.below.right == null || newChecker.c != newChecker.below.right.c) {
                return false;
            }

            return winCheck(i+1, direction, newChecker.below.right);
        }

        // if told to recurse down, only look down
        if (direction == 5) {
            if (newChecker.below == null || newChecker.c != newChecker.below.c) {
                return false;
            }

            return winCheck(i+1, direction, newChecker.below);
        }

        // if told to recurse down and to the left, only look down and to the left
        if (direction == 6) {
            if (newChecker.below == null || newChecker.below.left == null || newChecker.c != newChecker.below.left.c) {
                return false;
            }

            return winCheck(i+1, direction, newChecker.below.left);
        }

        // if told to recurse left, only look left
        if (direction == 7) {
            if (newChecker.left == null || newChecker.c != newChecker.left.c) {
                return false;
            }

            return winCheck(i+1, direction, newChecker.left);
        }

        // if told to recurse up and to the left, only look up and to the left
        if (direction == 8) {
            if (newChecker.left == null || newChecker.left.above == null || newChecker.c != newChecker.left.above.c) {
                return false;
            }

            return winCheck(i+1, direction, newChecker.left.above);
        }

        return false;
    }

}
