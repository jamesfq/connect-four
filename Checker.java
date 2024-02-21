import java.awt.*;
import java.awt.Graphics;

/**
 * Data structure that represents one single checker in the connect four board.
 *
 * @author James Quirk, Dartmouth DALI Developer Challenge! Spring 2023
 */

public class Checker {
    // initialize variables
    protected double x, y, r; // position and radius
    protected Color c; // color of the checker

    // pointers to the checker located next to any given checker on the board (null if none)
    protected Checker below;
    protected Checker above;
    protected Checker left;
    protected Checker right;

    // classifiers specifying if a checker is being used in SPACE MODE
    protected boolean moon;
    protected boolean sun;

    /**
     * @param x		initial x coordinate
     * @param y		initial y coordinate
     * @param c     initial color
     */
    public Checker(double x, double y, Color c) {
        this.x = x;
        this.y = y;
        this.r = 20; // default value if not provided
        this.c = c;

        this.below = null;
        this.above = null;
        this.left = null;
        this.right = null;

        this.moon = false;
        this.sun = false;
    }

    /**
     * @param x		initial x coordinate
     * @param y		initial y coordinate
     * @param r     initial radius
     * @param c     initial color
     */
    public Checker(double x, double y, double r, Color c) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.c = c;

        this.below = null;
        this.above = null;
        this.left = null;
        this.right = null;

        this.moon = false;
        this.sun = false;
    }

    /**
     * @param x		        initial x coordinate
     * @param y		        initial y coordinate
     * @param spaceMode     initial boolean indicating Space Mode or not
     * @param c             initial color (note: still provided to distinguish between players in winCheck)
     */
    public Checker(double x, double y, int spaceMode, Color c) {
        this.x = x;
        this.y = y;
        this.r = 20;
        this.c = c;

        this.below = null;
        this.above = null;
        this.left = null;
        this.right = null;

        // if the program passes in a value of 1, draw suns
        if (spaceMode == 1) {
            this.sun = true;
        }

        // if the program passes in a value of 2, draw moons
        else if (spaceMode == 2) {
            this.moon = true;
        }
    }

    // functions that reference the checker below the current checker.
    public Checker getBelow() {
        return below;
    }

    public void setBelow(Checker below) { this.below = below; }

    // functions that reference the checker above the current checker.
    public Checker getAbove() {
        return above;
    }

    public void setAbove(Checker above) { this.above = above; }

    // functions that reference the checker left of the current checker.
    public Checker getLeft() {
        return left;
    }

    public void setLeft(Checker left) { this.left = left; }

    // functions that reference the checker right of the current checker.
    public Checker getRight() {
        return right;
    }

    public void setRight(Checker right) { this.right = right; }

    // functions that reference the position of the checker.
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


    /**
     * Draws the checker on the graphics.
     * @param g
     */
    public void draw(Graphics g) {
        g.setColor(Color.black); // draw an outline
        g.drawOval((int)(x-r-1), (int)(y-r-1), (int)(2*r+2), (int)(2*r+2));

        if (sun) {
            g.setColor(Color.decode("#FF3E3E")); // background of checker
            g.fillOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
            g.setColor(Color.decode("#F8FF62")); // details of checker
            g.fillOval((int)(x-0.5*r), (int)(y-0.5*r), (int)(r), (int)(r));

            // upper and lower rays
            g.fillOval((int)(x-0.15*r), (int)(y-1.55*r), (int)(r/3), (int)(r*0.75));
            g.fillOval((int)(x-0.15*r), (int)(y+0.8*r), (int)(r/3), (int)(r*0.75));

            // left and right rays
            g.fillOval((int)(x-1.5*r), (int)(y-0.2*r), (int)(r*0.75), (int)(r/3));
            g.fillOval((int)(x+0.75*r), (int)(y-0.2*r), (int)(r*0.75), (int)(r/3));
        }

        else if (moon) {
            g.setColor(Color.decode("#005F8B")); // background of checker
            g.fillOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));

            // light side of the moon
            g.setColor(Color.decode("#E6E6E6"));
            g.fillOval((int)(x-11*r/15), (int)(y-11*r/15), (int)(3*r/2), (int)(3*r/2));

            // dark side of the moon
            g.setColor(Color.decode("#005F8B"));
            g.fillOval((int)(x-9*r/15), (int)(y-9*r/15), (int)(3*r/2), (int)(3*r/2));
        }

        // if neither sun nor moon mode is activated, just draw a normal checker
        else {
            g.setColor(Color.black);
            g.drawOval((int)(x-r-1), (int)(y-r-1), (int)(2*r+2), (int)(2*r+2));
            g.setColor(c);
            g.fillOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
        }
    }
}
