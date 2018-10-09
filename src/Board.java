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

        //test print of validSpawnLocations array------------------
        System.out.println("validSpawnLocationsArray:");
        for (int x = 0; x < validSpawnLocations.length; x++) {
            for (int y = 0; y < validSpawnLocations[0].length; y++) {
                System.out.print(validSpawnLocations[x][y]);
            }
            System.out.println();
        }
        System.out.println("validSpawnLocationsArray - end");
        //test print -----------------

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

        System.out.println("addTetriminoCheck returning: " + validSpawn);
        return validSpawn;
    } //end of addTetriminoCheck method

    //adds a tetrimino block to the board /TODO shape specific addition
    public static void addTetrimino(Tetrimino t) {
        //if the tetrimino can be added
        if (addTetriminoCheck(t)) {
            System.out.println("Add tetrimino: true");

            //if the tetrimino is the cube, spawn it in the center
            if (t.getSize() == 2) {
                //set the tetrimino's location
                t.setxCoord(4);
                t.setyCoord(0);

                //add the tetrimino to the board array
                for (int x = 0; x < 2; x++) {
                    for (int y = 0; y < t.getSize(); y++) {
                        boardArray[x][y+4] = t.getShape()[x][y];
                    }
                }
            } else if(t.getSize() == 3) { //if the tetrimino is a standard 3 size, spawn in the default location
                //set the tetrimino's location
                t.setxCoord(3);
                t.setyCoord(0);

                //add the tetrimino to the board array
                for (int x = 0; x < 2; x++) {
                    for (int y = 0; y < t.getSize(); y++) {
                        boardArray[x][y+3] = t.getShape()[x][y];
                    }
                }
            } else if(t.getSize() == 4) { //if the tetrimino is a line piece, spawn on the top row
                //set the tetrimino's location
                t.setxCoord(3);
                t.setyCoord(0);

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
        System.out.println("Attempting to drop tetrimino...");

        int size = t.getSize();
        int xCoord = t.getxCoord();
        int yCoord = t.getyCoord();

        if (tetriminoDropCheck(t)) {
            System.out.println("Valid drop. Dropping tetrimino now...");

            //if it's the square, drop it
            if (size == 2) {
                System.out.println("ROW: " + yCoord + " COL:" + xCoord);
                //clear the top of the square
                boardArray[yCoord][xCoord] = 0;
                boardArray[yCoord][xCoord+1] = 0;

                //add the bottom of the square
                boardArray[yCoord+2][xCoord] = 3;
                boardArray[yCoord+2][xCoord+1] = 3;

                //update the location
                t.setyCoord(yCoord+1);
            //if it's a standard block (not the square or line), drop it
            } else if (size == 3) {
                //TODO-----------------------------------------------------
            //if it's the line, drop it
            } else if (size == 4) {
                //TODO-----------------------------------------------------
            }
        } else {
            //if the tetrimino can no longer fall, spawn another
            System.out.println("Tetrimino can no longer fall, spawning a new block...");
            GameWindow.newTetrimino();
        }

    } //end of update method

    private static boolean tetriminoDropCheck(Tetrimino t) {
        int shapeNum = t.getShapeNum();
        int xCoord = t.getxCoord();
        int yCoord = t.getyCoord();

        //check the shape of the tetrimino, and search for any conflicts (depending on the shape)
        switch (shapeNum) {
            case 1:
                if (yCoord+2 < boardArray.length && boardArray[yCoord+2][xCoord] == 0 &&
                        boardArray[yCoord+2][xCoord+1] == 0 && boardArray[yCoord+2][xCoord+2] == 0) {
                    return true;
                } else {
                    return false;
                }
            case 2:
                if (yCoord+2 < boardArray.length && boardArray[yCoord+2][xCoord] == 0 &&
                        boardArray[yCoord+2][xCoord+1] == 0 && boardArray[yCoord+2][xCoord+2] == 0) {
                    return true;
                } else {
                    return false;
                }
            case 3:
                if (yCoord+2 < boardArray.length && boardArray[yCoord+2][xCoord] == 0 && boardArray[yCoord+2][xCoord+1] == 0) {
                    return true;
                } else {
                    return false;
                }
            case 4:
                if (yCoord+2 < boardArray.length && boardArray[yCoord+2][xCoord] == 0 && boardArray[yCoord+2][xCoord+1] == 0) {
                    return true;
                } else {
                    return false;
                }
            case 5:
                if (yCoord+2 < boardArray.length && boardArray[yCoord+2][xCoord] == 0 &&
                        boardArray[yCoord+2][xCoord+1] == 0 && boardArray[yCoord+2][xCoord+2] == 0) {
                    return true;
                } else {
                    return false;
                }
            case 6:
                if (yCoord+2 < boardArray.length && boardArray[yCoord+2][xCoord+1] == 0 &&
                        boardArray[yCoord+2][xCoord+2] == 0) {
                    return true;
                } else {
                    return false;
                }
            case 7:
                if (yCoord+2 < boardArray.length && boardArray[yCoord+2][xCoord] == 0 &&
                        boardArray[yCoord+2][xCoord+1] == 0 && boardArray[yCoord+2][xCoord+2] == 0
                        && boardArray[yCoord+2][xCoord+3] == 0) {
                    return true;
                } else {
                    return false;
                }
            default:
                return false;
        } //end of shapeNum switch statement
    }

    public int[][] getBoardArray() {
        return boardArray;
    } //end of getBoardArray() method
} //end of Board class
