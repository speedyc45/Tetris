//

import javax.swing.*;
import java.awt.*;

//class that manages the tetris game board
public class Board {
    //initialize the necessary variables
    public static final int COLUMNS = 10;
    public static final int ROWS = 20;
    private static int[][] boardArray;
    public static final int MOVE_LEFT = 1;
    public static final int MOVE_RIGHT = 2;
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

        //check if the tetrimino can spawn
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

        } else if (t.getSize() == 3 || t.getSize() == 4) {
            //second: check for a block collision
            for (int row = 0; row < t.getShape().length; row++) {
                for (int col = 0; col < t.getShape()[0].length; col++) {
                    if (t.getShape()[row][col] != 0 && boardArray[t.getyCoord()+row][t.getxCoord()+col] != 0) {
                        System.out.println("Block cannot spawn");
                        return false;
                    }
                }
            }
        }

        //System.out.println("addTetriminoCheck returning: " + validSpawn);
        return validSpawn;
    } //end of addTetriminoCheck method

    //adds a tetrimino block to the board
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
            } else if (t.getSize() == 3) { //if the tetrimino is a standard 3 size, spawn in the default location

                //add the tetrimino to the board array
                for (int row = 0; row < 2; row++) {
                    for (int col = 0; col < t.getSize(); col++) {
                        if (t.getShape()[row][col] != 0) {
                            boardArray[row][col+3] = t.getShape()[row][col];
                        }

                    }
                }
            } else if(t.getSize() == 4) { //if the tetrimino is a line piece, spawn on the top row

                //add the tetrimino to the board array
                for (int col = 0; col < t.getSize(); col++) {
                    boardArray[1][col+3] = t.getShape()[1][col];
                }
            }

        } else {
            //end the game (game over!)
            System.out.println("Cannot add a new tetrimino.\nGame Over");
            System.exit(0);
        }

    } //end of addTetrimino method

    //updates the board (moves blocks if necessary)
    public static void tetriminoDrop(Tetrimino t) {
        int size = t.getSize();
        int xCoord = t.getxCoord();
        int yCoord = t.getyCoord();

        //erase the previous block
        erase(t);

        if (tetriminoDropCheck(t)) {
            //System.out.println("Valid drop. Dropping tetrimino now...");
            //System.out.println("Old - ROW: " + yCoord + " COL:" + xCoord + " Size:" + size);

            //update the location
            t.setyCoord(yCoord+1);

            //redraw the tetrimino that was erased
            reDraw(t);

            //test print statement
            //System.out.println("New - ROW: " + t.getyCoord() + " COL:" + t.getxCoord() + " Size:" + size);

        } else {
            //redraw the tetrimino that was erased
            reDraw(t);

            //spawn another tetrimino
            System.out.println("Tetrimino can no longer fall, spawning a new block");
            GameWindow.newTetrimino();
        }

    } //end of update method

    //check to see if the move is possible (compare arrays for conflicts)
    private static boolean tetriminoDropCheck(Tetrimino t) {
        int xCoord = t.getxCoord();
        int yCoord = t.getyCoord();
        int lowestBlockHeight = 0;

        //first: find the lowest block in the tetrimino, check if it will go past the bottom of the grid
        Outer: for (int bottomRow = t.getSize()-1; bottomRow > 0; bottomRow--) {
            for (int col = 0; col < t.getShape()[0].length; col++) {
                if (t.getShape()[bottomRow][col] != 0) {
                    if (bottomRow + yCoord + 1 >= boardArray.length) {
                        return false;
                    } else {
                        //System.out.println("Found block at " + bottomRow + " local, or " + (bottomRow+yCoord) +
                        //        " on grid. Did not go past boundary.");
                        break Outer;
                    }
                }
            }
        }

        //second: check for a block collision
        for (int row = 0; row < t.getShape().length; row++) {
            for (int col = 0; col < t.getShape()[0].length; col++) {
                if (t.getShape()[row][col] != 0 && boardArray[yCoord+row+1][xCoord+col] != 0) {
                    System.out.println("Will return false - block collision");
                    return false;
                }
            }
        }
        return true;
    } //end of tetriminoDropCheck method

    //moves the tetrimino in the given direction
    public static void tetriminoMove(Tetrimino t, int direction) {
        //direction 1=left, 2=right

        //erase the tetrimino (so it can't collide with itself) before checking for collisions
        erase(t);
        switch (direction) {
            //left
            case 1:
                if (tetriminoMoveCheck(t, 1)) {
                    t.setxCoord(t.getxCoord()-1);
                } else {
                    System.out.println("Invalid move");
                }
                break;
            //right
            case 2:
                if (tetriminoMoveCheck(t, 2)) {
                    t.setxCoord(t.getxCoord()+1);
                } else {
                    System.out.println("Invalid move");
                }
                break;
        }

        //redraw the tetrimino after moving it (or not moving, if it isn't valid)
        reDraw(t);
    }

    //checks if the move of a tetrimino is valid
    private static boolean tetriminoMoveCheck(Tetrimino t, int direction) {
        //directions: 1=left,2=right
        int xCoord = t.getxCoord();
        int yCoord = t.getyCoord();
        int rowOffset = 0;
        int colOffset = 0;

        switch (direction) {
            case 1: //left
                //find the leftmost block in the tetrimino, check if it will go past the left edge of the grid
                Outer: for (int col = 0; col < t.getShape()[0].length; col++) {
                    for (int row = 0; row < t.getShape().length; row++) {
                        if (t.getShape()[row][col] != 0) {
                            if (col + xCoord <= 0) {
                                return false;
                            } else {
                                break Outer;
                            }
                        }
                    }
                }

                //otherwise, check for a block collision
                for (int row = 0; row < t.getShape().length; row++) {
                    for (int col = 0; col < t.getShape()[0].length; col++) {
                        if (t.getShape()[row][col] != 0 && boardArray[yCoord+row][xCoord+col-1] != 0) {
                            System.out.println("Will return false - block collision left");
                            return false;
                        }
                    }
                }
                break;
            case 2: //right
                //find the rightmost block in the tetrimino, check if it will go past the right edge of the grid
                Outer: for (int col = t.getSize()-1; col > 0; col--) {
                    for (int row = 0; row < t.getShape().length; row++) {
                        if (t.getShape()[row][col] != 0) {
                            if (col + xCoord + 1 >= boardArray[0].length) {
                                return false;
                            } else {
                                break Outer;
                            }
                        }
                    }
                }

                //otherwise, check for a block collision
                for (int row = 0; row < t.getShape().length; row++) {
                    for (int col = 0; col < t.getShape()[0].length; col++) {
                        if (t.getShape()[row][col] != 0 && boardArray[yCoord+row][xCoord+col+1] != 0) {
                            System.out.println("Will return false - block collision right");
                            return false;
                        }
                    }
                }
                break;
        } //end of direction switch statement

        return true;
    }

    //moves the tetrimino in the given direction
    public static void tetriminoRotate(Tetrimino t, int rotation) {
        //rotation -1=left, 1=right

        //erase the tetrimino (so it can't collide with itself) before checking for collisions, rotating if possible,
        //then redrawing the tetrimino
        erase(t);

        if (tetriminoRotateCheck(t, rotation)) {
            t.rotate(rotation);
        }

        reDraw(t);
    }

    //moves the tetrimino in the given direction
    private static boolean tetriminoRotateCheck(Tetrimino t, int rotation) {
        //rotation -1=left, 1=right

        t.rotate(rotation);

        //check if the leftmost or rightmost block in the tetrimino will go past the edge of the grid
        Outer: for (int col = 0; col < t.getShape()[0].length; col++) {
            for (int row = 0; row < t.getShape().length; row++) {
                if (t.getShape()[row][col] != 0) {
                    if (col + t.getxCoord() <= 0) {
                        t.rotate(-rotation);
                        return false;
                    } else if (col + t.getxCoord() + 1 >= boardArray[0].length) {
                        t.rotate(-rotation);
                        return false;
                    }
                }
            }
        }

        //check for a block collision
        for (int row = 0; row < t.getShape().length; row++) {
            for (int col = 0; col < t.getShape()[0].length; col++) {
                if (t.getShape()[row][col] != 0 && boardArray[t.getyCoord()+row][t.getxCoord()+col] != 0) {
                    System.out.println("Will return false - block collision on rotation");
                    t.rotate(-rotation);
                    return false;
                }
            }
        }

        t.rotate(-rotation);
        return true;
    }

    //redraws a block after a rotation or drop
    public static void erase(Tetrimino t) {
        int size = t.getSize();
        int xCoord = t.getxCoord();
        int yCoord = t.getyCoord();

        //erase the previous block
        if (size == 2) {
            //erase the square
            boardArray[yCoord][xCoord] = 0;
            boardArray[yCoord][xCoord+1] = 0;
            boardArray[yCoord+1][xCoord] = 0;
            boardArray[yCoord+1][xCoord+1] = 0;
        } else if (size == 3 || size == 4) {
            //erase the existing tetrimino
            for (int row = 0; row < t.getShape().length; row++) {
                for (int col = 0; col < t.getShape()[0].length; col++) {
                    //if the shape has a value, erase where the corresponding value on the board array would be
                    if (t.getShape()[row][col] != 0) {
                        try {
                            boardArray[yCoord+row][xCoord+col] = 0;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("yCoord: " + yCoord + " xCoord: " + xCoord + "\nrow: " + row + " col: " + col);

                            for (int row2 = 0; row2 < t.getSize(); row2++) {
                                for (int col2 = 0; col2 < t.getSize(); col2++) {
                                    System.out.print(t.getShape()[row2][col2]);
                                }
                                System.out.println();
                            }

                            System.exit(0);
                        }

                    }
                }
            }
        } else {
            System.out.println("Error: size impossible");
        }
    } //end of erase method

    //redraws a block after a rotation or drop
    public static void reDraw(Tetrimino t) {
        int yCoord = t.getyCoord();
        int xCoord = t.getxCoord();

        //draw the tetrimino, depending on its size
        if (t.getSize() == 2) {
            //recreate the square
            boardArray[yCoord][xCoord] = 3;
            boardArray[yCoord][xCoord+1] = 3;
            boardArray[yCoord+1][xCoord] = 3;
            boardArray[yCoord+1][xCoord+1] = 3;

            //if it's a standard block (not the square or line), drop it
        } else if (t.getSize() == 3 || t.getSize() == 4) {
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
    } //end of reDraw method

    public static void clearLine() {
        boolean runLoop;
        int clearLineRow;

        //check if there is a line to be cleared, and allow the loop to run if so
        clearLineRow = clearLineCheck(0);
        if (clearLineRow >= 0 && clearLineRow <= 19) {
            runLoop = true;
        } else {
            runLoop = false;
        }

        //continue clearing lines while it's possible
        while (runLoop) {
            //delete the row that should be cleared
            for (int col = 0; col < boardArray[0].length; col++) {
                boardArray[clearLineRow][col] = 0;
            }

            //cycle through the array and move all of the above blocks down one row
            for (int row = clearLineRow; row > 0; row--) {
                for (int col = 0; col < boardArray[0].length; col++) {
                    boardArray[row][col] = boardArray[row-1][col];
                }
            }


            //check if there are any other lines to clear, and continue the loop if there are
            clearLineRow = clearLineCheck(clearLineRow);

            if (clearLineRow >= 0 && clearLineRow <= 19) {
                runLoop = true;
            } else {
                runLoop = false;
            }
        }
    }

    private static int clearLineCheck(int startRow) {
        int rowCounter = 0;

        //cycle through the board and checks for full lines to be cleared
        for (int row = startRow; row < boardArray.length; row++) {
            for (int col = 0; col < boardArray[0].length; col++) {
                //add the the counter if there is a block in the row
                if (boardArray[row][col] != 0) {
                    rowCounter++;
                }

                //if the entire row is blocks, return the row number
                if (rowCounter == boardArray[0].length) {
                    return row;
                }
            }
            //restart the rowCounter as the loop reaches a new row
            rowCounter = 0;
        }

        //if no row was return, return an illegal row
        return 20;
    }

    public int[][] getBoardArray() {
        return boardArray;
    } //end of getBoardArray() method
} //end of Board class
