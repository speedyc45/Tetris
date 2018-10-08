//

import javax.swing.*;
import java.awt.*;

//class that manages the tetris board
public class Board {
    //initialize the necessary variables
    public static final int COLUMNS = 10;
    public static final int ROWS = 20;
    private static int[][] boardArray;
    public static final ImageIcon blockYellow = new ImageIcon("assets\\Tetris_Block_Yellow.png");
    public static final ImageIcon blockPurple = new ImageIcon("assets\\Tetris_Block_Purple.png");
    public static final ImageIcon blockPink = new ImageIcon("assets\\Tetris_Block_Pink.png");
    public static final ImageIcon blockOrange = new ImageIcon("assets\\Tetris_Block_Orange.png");
    public static final ImageIcon blockLightBlue = new ImageIcon("assets\\Tetris_Block_LightBlue.png");
    public static final ImageIcon blockGreen = new ImageIcon("assets\\Tetris_Block_Green.png");
    public static final ImageIcon blockBlue = new ImageIcon("assets\\Tetris_Block_Blue.png");

    //default constructor
    public Board() {
        //create the size of the board, which starts with no blocks
        boardArray = new int[ROWS][COLUMNS];

        //set the default values
        for (int x = 0; x < ROWS; x++) {
            for (int y = 0; y < COLUMNS; y++) {
                boardArray[x][y] = 0;
            }
        }
    } //end of default Board() constructor

    //tests to see if a Tetrimino can be added to the board, and does so if possible
    //reference for legal spawn locations: https://harddrop.com/wiki/Spawn_Location
    private static boolean addTetriminoCheck(Tetrimino t) {
        int[][] validSpawnLocations = new int[2][4];
        boolean validSpawn = true;

        for (int x = 0; x < validSpawnLocations.length; x++) {
            for (int y = 0; y < validSpawnLocations[0].length; y++) {
                validSpawnLocations[x][y] = boardArray[x][y+3];
            }
        }

        //test print of validSpawnLocations array------------------
        for (int x = 0; x < validSpawnLocations.length; x++) {
            for (int y = 0; y < validSpawnLocations[0].length; y++) {
                System.out.print(validSpawnLocations[x][y]);
            }
            System.out.println();
        } //test print -----------------

        //check if the tetrimino can spawn /TODO - add specific shape checking!/
        if (t.getSize() == 2) {
            //note: using OUTER: labels the outside loop, making it possible to break all loops
            OUTER: for (int x = 0; x < 2; x++) {
                for (int y = 1; y < t.getSize(); y++) {
                    if (validSpawnLocations[x][y] != 0) {
                        validSpawn = false;
                        break OUTER;
                    }
                } //end of y loop
            } //end of OUTER loop
        } else if (t.getSize() == 3) {
            OUTER: for (int x = 0; x < 2; x++) {
                for (int y = 0; y < t.getSize(); y++) {
                    if (validSpawnLocations[x][y] != 0) {
                        validSpawn = false;
                        break OUTER;
                    }
                } //end of y loop
            } //end of OUTER loop
        } else if (t.getSize() == 4) {
            OUTER: for (int x = 0; x < 2; x++) {
                for (int y = 0; y < t.getSize(); y++) {
                    if (validSpawnLocations[x][y] != 0) {
                        validSpawn = false;
                        break OUTER;
                    }
                } //end of y loop
            } //end of OUTER loop
        }
            return validSpawn;
    } //end of addTetriminoCheck method

    //adds a tetrimino block to the board /TODO shape specific addition
    public static void addTetrimino(Tetrimino t) {
        //if the tetrimino can be added
        if (addTetriminoCheck(t)) {
            System.out.println("Add tetrimino: true");
            //add the tetrimino to the board array
            for (int x = 0; x < 2; x++) {
                for (int y = 0; y < t.getSize(); y++) {
                    boardArray[x][y+3] = t.getShape()[x][y];
                }
            }
        } else {
            System.out.println("Add tetrimino: false");
        }

    } //end of addTetrimino method

    public int[][] getBoardArray() {
        return boardArray;
    } //end of getBoardArray() method
} //end of Board class
