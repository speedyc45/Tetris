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
    private static int height = 465;
    private final int ANIMATION_REFRESH_RATE = 5000;
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

        //TESTING STATEMENTS
        Tetrimino test = new Tetrimino(7);

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
    private Image background; //note: border length is 4 pixels
    private Image block;
    private final int backgroundOffsetX = 5;
    private final int backgroundOffsetY = 5;
    private int blockPosX = backgroundOffsetX + 4;
    private int blockPosY = backgroundOffsetY + 5;
    private Board tetrisBoard = new Board();

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
        //create the background Image then draw it
        background = new ImageIcon("assets\\Tetris_Grid_Background.png").getImage();
        g.drawImage(background, backgroundOffsetX, backgroundOffsetY, null);

        //draw the board
        for (int x = 0; x < Board.ROWS; x++) {
            for (int y = 0; y < Board.COLUMNS; y++) {
                switch (tetrisBoard.getBoardArray()[x][y]) {
                    case 0:
                        //no block, do nothing
                        break;
                    case 1:
                        System.out.println("Displaying block at x: " + x + ", y: " + y);
                        block = Board.blockBlue.getImage();
                        g.drawImage(block, blockPosX + y*20, blockPosY + x*20, null);
                        break;
                    case 2:
                        System.out.println("Displaying block at x: " + x + ", y: " + y);
                        block = Board.blockOrange.getImage();
                        g.drawImage(block, blockPosX + y*20, blockPosY + x*20, null);
                        break;
                    case 3:
                        System.out.println("Displaying block at x: " + x + ", y: " + y);
                        block = Board.blockYellow.getImage();
                        g.drawImage(block, blockPosX + y*20, blockPosY + x*20, null);
                        break;
                    case 4:
                        System.out.println("Displaying block at x: " + x + ", y: " + y);
                        block = Board.blockGreen.getImage();
                        g.drawImage(block, blockPosX + y*20, blockPosY + x*20, null);
                        break;
                    case 5:
                        System.out.println("Displaying block at x: " + x + ", y: " + y);
                        block = Board.blockPink.getImage();
                        g.drawImage(block, blockPosX + y*20, blockPosY + x*20, null);
                        break;
                    case 6:
                        System.out.println("Displaying block at x: " + x + ", y: " + y);
                        block = Board.blockPurple.getImage();
                        g.drawImage(block, blockPosX + y*20, blockPosY + x*20, null);
                        break;
                    case 7:
                        System.out.println("Displaying block at x: " + x + ", y: " + y);
                        block = Board.blockLightBlue.getImage();
                        g.drawImage(block, blockPosX + y*20, blockPosY + x*20, null);
                        break;
                    default:
                        //TODO ERROR!
                        break;
                }
            }
        }

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