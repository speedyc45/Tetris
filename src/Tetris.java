/*
 * Tetris.java
 * Description: /TODO/
 *
 * Written by: Callum Kipin
 * Date Started: Oct 2nd, 2018
 * Date Finished: /TODO/
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import javax.swing.*;

public class Tetris {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        new GameWindow();
    } //end of main method

} //end of main Tetris class

class GameWindow extends JFrame{
    //initialize the necessary variables
    private static int width = 350;
    private static int height = 300;
    private final int ANIMATION_REFRESH_RATE = 20;
    private PaintSurface canvas;

    public GameWindow() {
        //set the dimensions, title, closing operation, and center it in the screen
        this.setSize(width, height);
        this.setTitle("Callum's Tetris");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        //make a canvas for animating and add it to the frame
        canvas = new PaintSurface();
        this.add(canvas, BorderLayout.CENTER);

        //create a timer for repainting the animation every 20 (ms?)
        //credit to Java for Dummies
        Timer t = new Timer(ANIMATION_REFRESH_RATE, new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent evt)
            {
                canvas.repaint();
            }
        });
        t.start();


        //TESTING
        Tetrimino test = new Tetrimino(1);
        System.out.println(test);
        test.rotate(Tetrimino.ROTATE_LEFT);
        System.out.println(test);
        //TESTING


        //make the frame (window) visible
        this.setVisible(true);
    } //end of GameWindow constructor method

    public static int getHEIGHT() {
        return height;
    } //end of getHEIGHT method

    public static int getWIDTH() {
        return width;
    } //end of getWIDTH method

    //
    public String toString() {
        String report = "";

        report += "-------------------------\n";
        report += "GameWindow Object\n";
        report += "Width: " + WIDTH + "\n";
        report += "Height: " + HEIGHT + "\n";

        return report;
    } //end of the toString method

} //end of GameWindow class

class PaintSurface extends JComponent {
    //define the necessary variables
    int x_pos = 0;
    int y_pos = 0;
    int x_speed = 2;
    int y_speed = 3;
    int d = 20;

    //method for repainting the canvas
    public void paint(Graphics g) {
        //cast the graphics object as a Graphics2D object
        Graphics2D g2 = (Graphics2D)g;

        //set antialiasing to on (smooths lines to remove jagged edges)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //check to see if the ball hit any boundaries of the window
        if (x_pos < 0 || x_pos > GameWindow.getWIDTH() - 1.8*d) {
            x_speed = -x_speed;
        }
        if (y_pos < 0 || y_pos > GameWindow.getHEIGHT() - 2.8*d) {
            y_speed = -y_speed;
        }

        //move the ball
        x_pos += x_speed;
        y_pos += y_speed;

        //create the new ball, and set its colour to red
        Shape ball = new Ellipse2D.Float(x_pos, y_pos, d, d);
        g2.setColor(Color.red);
        g2.fill(ball);
    } //end of paint method

    //
    public String toString() {
        String report = "";

        report += "-------------------------\n";
        report += "PaintSurface Object\n";
        report += "Width: " + WIDTH + "\n";
        report += "Height: " + HEIGHT + "\n";

        return report;
    } //end of the toString method

} //end of PaintSurface class