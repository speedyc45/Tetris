//

import javax.swing.*;
import java.awt.*;

//class that manages the tetris game board
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

        //check if the tetrimino can spawn /TODO - add specific shape checking!/
        if (t.getSize() == 2) {
            //note: using OUTER: labels the outside loop, making it possible to break all loops
            OUTER: for (int x = 0; x < 2; x++) {
                for (int y = 1; y < t.getSize(); y++) {
                    if (validSpawnLocations[x][y] != 0) {
                        System.out.println("Row: " + x + " Col: " + y);
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

        //System.out.println("addTetriminoCheck returning: " + validSpawn);
        return validSpawn;
    } //end of addTetriminoCheck method

    //adds a tetrimino block to the board /TODO shape specific addition
    public static void addTetrimino(Tetrimino t) {
        //if the tetrimino can be added
        if (addTetriminoCheck(t)) {
            //System.out.println("Add tetrimino: true");

            //if the tetrimino is the cube, spawn it in the center
            if (t.getSize() == 2) {

                //add the tetrimino to the board array
                for (int x = 0; x < 2; x++) {
                    for (int y = 0; y < t.getSize(); y++) {
                        boardArray[x][y+4] = t.getShape()[x][y];
                    }
                }
            } else if(t.getSize() == 3) { //if the tetrimino is a standard 3 size, spawn in the default location

                //add the tetrimino to the board array
                for (int x = 0; x < 2; x++) {
                    for (int y = 0; y < t.getSize(); y++) {
                        boardArray[x][y+3] = t.getShape()[x][y];
                    }
                }
            } else if(t.getSize() == 4) { //if the tetrimino is a line piece, spawn on the top row

                //add the tetrimino to the board array
                for (int y = 0; y < t.getSize(); y++) {
                    boardArray[0][y+3] = t.getShape()[1][y];
                }
            }

        } else {
            System.out.println("Add tetrimino: false");
        }

    } //end of addTetrimino method

    //updates the board (moves blocks if necessary)
    public static void tetriminoDrop(Tetrimino t) {
        //System.out.println("Attempting to drop tetrimino...");

        int size = t.getSize();
        int xCoord = t.getxCoord();
        int yCoord = t.getyCoord();

        //erase the previous block
        //if it's the square, drop it
        if (size == 2) {
            //erase the square
            boardArray[yCoord][xCoord] = 0;
            boardArray[yCoord][xCoord+1] = 0;
            boardArray[yCoord+1][xCoord] = 0;
            boardArray[yCoord+1][xCoord+1] = 0;

            //if it's a standard block (not the square or line), drop it
        } else if (size == 3 || size == 4) {
            //erase the existing tetrimino
            for (int row = 0; row < t.getShape().length; row++) {
                for (int col = 0; col < t.getShape()[0].length; col++) {
                    //if the shape has a value, erase where the corresponding value on the board array would be
                    if (t.getShape()[row][col] != 0) {
                        boardArray[yCoord+row][xCoord+col] = 0;
                    }
                }
            }
        } else {
            System.out.println("Error: size impossible");
        }


        if (tetriminoDropCheck(t)) {
            //System.out.println("Valid drop. Dropping tetrimino now...");
            //System.out.println("Old - ROW: " + yCoord + " COL:" + xCoord + " Size:" + size);

            //if it's the square, drop it
            if (size == 2) {
                //recreate the square down one row
                boardArray[yCoord+1][xCoord] = 3;
                boardArray[yCoord+1][xCoord+1] = 3;
                boardArray[yCoord+2][xCoord] = 3;
                boardArray[yCoord+2][xCoord+1] = 3;

                //update the location
                t.setyCoord(yCoord+1);
            //if it's another block, recreate it down one row
            } else if (size == 3 || size == 4) {
                //create the new tetrimino
                for (int row = 0; row < t.getShape().length; row++) {
                    for (int col = 0; col < t.getShape()[0].length; col++) {
                        //if the shape has a value, create a block where the corresponding value on the board array would be
                        if (t.getShape()[row][col] != 0) {
                            boardArray[yCoord+row+1][xCoord+col] = t.getShape()[row][col];
                        }
                    }
                }

                //update the location
                t.setyCoord(yCoord+1);
            } else {
                System.out.println("Error: size impossible");
            }

            //test print statement
            //System.out.println("New - ROW: " + t.getyCoord() + " COL:" + t.getxCoord() + " Size:" + size);

        } else {
            //if the tetrimino can no longer fall, recreate it, then spawn another
            if (size == 2) {
                //recreate the square
                boardArray[yCoord][xCoord] = 3;
                boardArray[yCoord][xCoord+1] = 3;
                boardArray[yCoord+1][xCoord] = 3;
                boardArray[yCoord+1][xCoord+1] = 3;

                //if it's a standard block (not the square or line), drop it
            } else if (size == 3 || size == 4) {
                //create the new tetrimino
                for (int row = 0; row < t.getShape().length; row++) {
                    for (int col = 0; col < t.getShape()[0].length; col++) {
                        //if the shape has a value, create a block where the corresponding value on the board array would be
                        if (t.getShape()[row][col] != 0) {
                            boardArray[yCoord+row][xCoord+col] = t.getShape()[row][col];
                        }
                    }
                }
                //if it's the line, drop it
            } else {
                System.out.println("Error: size impossible");
            }

            //spawn another tetrimino
            System.out.println("Tetrimino can no longer fall, spawning a new block");
            GameWindow.newTetrimino();
        }

    } //end of update method

    //check to see if the move is possible (compare arrays for conflicts)
    private static boolean tetriminoDropCheck(Tetrimino t) {
        int xCoord = t.getxCoord();
        int yCoord = t.getyCoord();

        //check for the bottom of the grid
        for (int col = 0; col < t.getShape()[0].length; col++) {
            if (t.getShape()[t.getSize()-1][col] != 0 && yCoord + t.getSize()-1 == boardArray.length) {
                System.out.println("Will return false - edge of grid");
                return false;
            }
        }

        //
        for (int row = 0; row < t.getShape().length; row++) {
            for (int col = 0; col < t.getShape()[0].length; col++) {
                //if the shape has a value, create a block where the corresponding value on the board array would be
                if (t.getShape()[row][col] != 0 && boardArray[yCoord+row+1][xCoord+col] != 0) {
                    System.out.println("Will return false - block collision");
                    return false;
                }
            }
        }

        //System.out.println("Will return true...");
        return true;
    }

    public int[][] getBoardArray() {
        return boardArray;
    } //end of getBoardArray() method
} //end of Board class
