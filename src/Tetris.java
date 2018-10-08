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
import java.awt.image.ImageObserver;
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
    private static int height = 375;
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


        //---------------TESTING---------------
        Tetrimino test = new Tetrimino(7);
        System.out.println(test);
        for (int x = 0; x < 4; x++) {
            test.rotate(Tetrimino.ROTATE_LEFT);
            System.out.println(test);
        }
        //---------------TESTING---------------


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
    Image background;

    //testing variables -NOT ACTUAL-
    /*
    int x_pos = 0;
    int y_pos = 0;
    int x_pos2 = 20;
    int y_pos2 = 30;
    int x_speed = 2;
    int y_speed = 3;
    int x_speed2 = 2;
    int y_speed2 = 3;
    int d = 20;
    */

    //method for repainting the canvas
    public void paint(Graphics g) {
        background = new ImageIcon("assets\\Tetris_Grid_Background.png").getImage();
        g.drawImage(background, 5, 5, null);

    } //end of paint method


    //testing paint method -NOT ACTUAL-
    /*
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

        //check to see if ball2 hit any boundaries of the window
        if (x_pos2 < 0 || x_pos2 > GameWindow.getWIDTH() - 1.8*d) {
            x_speed2 = -x_speed2;
        }
        if (y_pos2 < 0 || y_pos2 > GameWindow.getHEIGHT() - 2.8*d) {
            y_speed2 = -y_speed2;
        }

        //move the balls
        x_pos += x_speed;
        y_pos += y_speed;

        x_pos2 += x_speed2;
        y_pos2 += y_speed2;

        //create the new ball, and set its colour to red
        Shape ball = new Ellipse2D.Float(x_pos, y_pos, d, d);
        g2.setColor(Color.red);
        g2.fill(ball);

        //create the new ball, and set its colour to red
        Shape ball2 = new Ellipse2D.Float(x_pos2, y_pos2, d, d);
        g2.setColor(Color.blue);
        g2.fill(ball2);
    } //end of paint method
    */

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