
import javax.swing.*;

/********************************************************
 * DESC: Class that manages the Tetris game board (positioning and collision of blocks within an int array)
 ********************************************************/
public class Board {
    //initialize the necessary variables
    private static int[][] boardArray;
    private static int rowsCleared = 0;
    private static int level = 1;
    private static int score = 0;
    private static int blocksSpawned = 0;
    public static final int COLUMNS = 10;
    public static final int ROWS = 20;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final ImageIcon blockYellow = new ImageIcon("assets\\Tetris_Block_Yellow.png");
    public static final ImageIcon blockPurple = new ImageIcon("assets\\Tetris_Block_Purple.png");
    public static final ImageIcon blockPink = new ImageIcon("assets\\Tetris_Block_Pink.png");
    public static final ImageIcon blockOrange = new ImageIcon("assets\\Tetris_Block_Orange.png");
    public static final ImageIcon blockLightBlue = new ImageIcon("assets\\Tetris_Block_LightBlue.png");
    public static final ImageIcon blockGreen = new ImageIcon("assets\\Tetris_Block_Green.png");
    public static final ImageIcon blockBlue = new ImageIcon("assets\\Tetris_Block_Blue.png");

    /********************************************************
     * DESC: Creates a default Board object and initializes the 2D int array for the board as empty
     * PRE: N/A
     * POST: A Board object is created and it's 2D int array is initialized with 0s
     ********************************************************/
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

    /********************************************************
     * DESC: Returns a boolean describing if a Tetrimino can be spawned on the board array
     * PRE: Takes a Tetrimino object as a parameter that cannot be null
     * POST: Returns a boolean
     ********************************************************/
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

        return validSpawn;
    } //end of addTetriminoCheck method

    /********************************************************
     * DESC: Adds a Tetrimino block to the board (according to https://harddrop.com/wiki/Spawn_Location locations) if possible
     * PRE: Takes a Tetrimino object as a parameter that cannot be null
     * POST: Adds the Tetrimino's shape[][] to the top center of the boardArray[][] if possible
     ********************************************************/
    public static void addTetrimino(Tetrimino t) {
        //if the tetrimino can be added, add it to its correct position, otherwise run gameOver()
        if (addTetriminoCheck(t)) {
            if (t.getSize() == 2) {
                //if the tetrimino is the cube, spawn it in the center of the boardArray
                for (int x = 0; x < 2; x++) {
                    for (int y = 0; y < t.getSize(); y++) {
                        boardArray[x][y+4] = t.getShape()[x][y];
                    }
                }
            } else if (t.getSize() == 3) {
                //if the tetrimino is a standard 3 size, spawn it in the default location of the boardArray
                for (int row = 0; row < 2; row++) {
                    for (int col = 0; col < t.getSize(); col++) {
                        if (t.getShape()[row][col] != 0) {
                            boardArray[row][col+3] = t.getShape()[row][col];
                        }

                    }
                }
            } else if(t.getSize() == 4) {
                //if the tetrimino is a line piece, spawn it on the 2nd top row of the board Array
                for (int col = 0; col < t.getSize(); col++) {
                    boardArray[1][col+3] = t.getShape()[1][col];
                }
            }

        } else {
            System.out.println("Cannot add a new tetrimino.\nGame Over");
            GameWindow.gameOver();
        }

    } //end of addTetrimino method

    /********************************************************
     * DESC: Updates the board (moves blocks if necessary inside the board array)
     * PRE: Takes a Tetrimino object and boolean as parameters that cannot be null
     * POST: The yCoord of the given Tetrimino object is increased by one and the respective values in the boardArray
     *       increased by one in the "y-axis"
     ********************************************************/
    public static void tetriminoDrop(Tetrimino t, boolean override) {
        int yCoord = t.getyCoord();

        //erase the previous block
        erase(t);

        if (override || tetriminoDropCheck(t)) {
            //update the location and redraw the tetrimino that was erased
            t.setyCoord(yCoord+1);
            reDraw(t);

        } else {
            //redraw the tetrimino that was erased and spawn another tetrimino
            reDraw(t);
            GameWindow.newTetrimino();
        }

    } //end of update method

    /********************************************************
     * DESC: Check to see if the Tetrimino's move is possible (compare arrays for conflicts)
     * PRE: Takes a Tetrimino object as a parameter that cannot be null
     * POST: Returns a boolean
     ********************************************************/
    private static boolean tetriminoDropCheck(Tetrimino t) {
        int xCoord = t.getxCoord();
        int yCoord = t.getyCoord();

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

    /********************************************************
     * DESC: Moves the tetrimino in the given horizontal direction if possible
     * PRE: Takes a Tetrimino object as a parameter that cannot be null, and takes an int which must be 1 or 2
     * POST: boardArray is altered and the Tetrimino object's xcoord are increased or decreased by 1 if possible
     ********************************************************/
    public static void tetriminoMove(Tetrimino t, int direction) {
        //erase the tetrimino (so it can't collide with itself) before checking for collisions, then move the Tetrimino
        //if possible
        erase(t);
        switch (direction) {
            //left move
            case LEFT:
                if (tetriminoMoveCheck(t, LEFT)) {
                    t.setxCoord(t.getxCoord()-1);
                } else {
                    System.out.println("Invalid move");
                }
                break;
            //right move
            case RIGHT:
                if (tetriminoMoveCheck(t, RIGHT)) {
                    t.setxCoord(t.getxCoord()+1);
                } else {
                    System.out.println("Invalid move");
                }
                break;
        }

        //redraw the tetrimino after moving it (or not moving, if it isn't valid)
        reDraw(t);
    }

    /********************************************************
     * DESC: Checks if the move of a tetrimino is valid
     * PRE: Takes a Tetrimino object as a parameter that cannot be null, and takes an int which must be 1 or 2
     * POST: A boolean is returned
     ********************************************************/
    private static boolean tetriminoMoveCheck(Tetrimino t, int direction) {
        //define and initialize the local variables
        int xCoord = t.getxCoord();
        int yCoord = t.getyCoord();

        //check if the horizontal move is possible, return true if so (false otherwise)
        switch (direction) {
            case LEFT:
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
            case RIGHT:
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

    /********************************************************
     * DESC: Rotates the tetrimino in the given direction if possible
     * PRE: Takes a Tetrimino object as a parameter that cannot be null, and takes an int which must be 1 or 2
     * POST: The given Tetrimino's shape may be altered and the boardArray may be altered
     ********************************************************/
    public static void tetriminoRotate(Tetrimino t, int rotation) {
        //erase the tetrimino (so it can't collide with itself) before checking for collisions, rotating if possible,
        //then redrawing the tetrimino
        erase(t);

        if (tetriminoRotateCheck(t, rotation)) {
            t.rotate(rotation);
        }

        reDraw(t);
    } //end of tetriminoRotate method

    /********************************************************
     * DESC: Returns a boolean which states whether it is possible for the given Tetrimino to rotate on the board
     * PRE: Takes a Tetrimino object as a parameter that cannot be null, and takes an int which must be 1 or 2
     * POST: Returns a boolean
     ********************************************************/
    private static boolean tetriminoRotateCheck(Tetrimino t, int rotation) {
        //rotate the Tetrimino's shape array
        t.rotate(rotation);

        //check if the leftmost or rightmost block in the tetrimino will go past the edge of the grid
        Outer: for (int col = 0; col < t.getShape()[0].length; col++) {
            for (int row = 0; row < t.getShape().length; row++) {
                if (t.getShape()[row][col] != 0) {
                    if (col + t.getxCoord() < 0) {
                        t.rotate(-rotation);
                        return false;
                    } else if (col + t.getxCoord() + 1 > boardArray[0].length) {
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
                System.out.print(t.getShape()[row][col]);
            }
            System.out.println();
        }

        //Undo the rotation of the Tetrimino's shape array and return true
        t.rotate(-rotation);
        return true;
    } //end of tetriminoRotateCheck method

    /********************************************************
     * DESC: Instantly drops a Tetrimino as far down the board as is possible
     * PRE: Takes a Tetrimino object as a parameter that cannot be null
     * POST: boardArray is altered
     ********************************************************/
    public static void tetriminoInstantDrop(Tetrimino t) {

        //while the drop is valid, continue to drop the block
        //(erase it, check for collisions, drop if possible, then redraw - loop while valid)
        erase(t);
        while (tetriminoDropCheck(t)) {
            tetriminoDrop(t, true);
            erase(t);
        }
        reDraw(t);
    } //end of tetriminoInstantDrop method

    /********************************************************
     * DESC: Removes the respective integer values in the board array of the given Tetrimino object ("erases" it)
     * PRE: Takes a Tetrimino object as a parameter that cannot be null
     * POST: boardArray is altered
     ********************************************************/
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

    /********************************************************
     * DESC: Recreates the respective integer values in the board array of the given Tetrimino object ("redraws" it)
     * PRE: Takes a Tetrimino object as a parameter that cannot be null
     * POST: Alters boardArray
     ********************************************************/
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

    /********************************************************
     * DESC: Returns an integer which signifies which row to clear (if it is a continuous row), or returns 20 if no row
     *       should be cleared
     * PRE: Takes an int which must be between 0 and 19 inclusive
     * POST: Returns an integer
     ********************************************************/
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

    /********************************************************
     * DESC: Removes a horizontal row of integer values from the board array given by clearLineCheck() (returns an int
     *       between 0 and 19 inclusive signifying the row number), and moves all integer values in the rows above down
     *       one row. Will also call the levelUp() method in the GameWindow class if enough lines are cleared (speeding
     *       up the drop speed of Tetriminoes)
     * PRE: N/A
     * POST: Alters boardArray
     ********************************************************/
    public static void clearLine() {
        boolean runLoop;
        int clearLineRow;
        int rowsClearedAtOnce = 0;
        double scoreMultiplier = 0.5;

        //check if there is a line to be cleared, and allow the loop to run if so
        clearLineRow = clearLineCheck(0);
        if (clearLineRow >= 0 && clearLineRow <= 19) {
            runLoop = true;
        } else {
            runLoop = false;
        }

        //continue clearing lines while it's possible
        while (runLoop) {
            rowsCleared++;
            rowsClearedAtOnce++;
            scoreMultiplier += 0.5;

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

        //update the score
        score += rowsClearedAtOnce * 10 * scoreMultiplier;

        //check if a level up is neccesary, and then level up (if needed)
        if (Math.floor(rowsCleared/(Board.getLevel()*15)) == 1) {
            GameWindow.levelUp();
        }
    }

    /********************************************************
     * DESC: The boardArray is "cleared" (filled with 0s)
     * PRE: N/A
     * POST: boardArray is set to default values
     ********************************************************/
    public static void clearBoard() {
        //set every value in the board equal to 0
        for (int row = 0; row < boardArray.length; row++) {
            for (int col = 0; col < boardArray[0].length; col++) {
                boardArray[row][col] = 0;
            }
        }
    }

    /********************************************************
     * DESC: Returns the boardArray int[][]
     * PRE: N/A
     * POST: Returns an int[][]
     ********************************************************/
    public static int[][] getBoardArray() {
        return boardArray;
    }

    /********************************************************
     * DESC: Returns the rowsCleared int
     * PRE: N/A
     * POST: Returns an int
     ********************************************************/
    public static int getRowsCleared() { return rowsCleared; }

    /********************************************************
     * DESC: Sets rowsCleared as num
     * PRE: num cannot be null
     * POST: Sets rowsCleared as num
     ********************************************************/
    public static void setRowsCleared(int num) { rowsCleared = num; }

    /********************************************************
     * DESC: Returns the level int
     * PRE: N/A
     * POST: Returns an int
     ********************************************************/
    public static int getLevel() { return level; }

    /********************************************************
     * DESC: Sets the level int as the given int
     * PRE: Takes an int parameter which cannot be null
     * POST: Sets the level int as the given int
     ********************************************************/
    public static void setLevel(int num) { level = num; }

    /********************************************************
     * DESC: Returns the score int
     * PRE: N/A
     * POST: Returns an int
     ********************************************************/
    public static int getScore() { return score; }

    /********************************************************
     * DESC: Sets the score int as the given int
     * PRE: Takes an int parameter which cannot be null
     * POST: Sets the score int as the given int
     ********************************************************/
    public static void setScore(int num) { score = num; }

    /********************************************************
     * DESC: Returns the blocksSpawned int
     * PRE: N/A
     * POST: Returns an int
     ********************************************************/
    public static int getBlocksSpawned() { return blocksSpawned; }

    /********************************************************
     * DESC: Sets the blockSpawned int as the given int
     * PRE: Takes an int parameter which cannot be null
     * POST: Sets the blockSpawned int as the given int
     ********************************************************/
    public static void setBlocksSpawned(int num) { blocksSpawned = num; }

    /********************************************************
     * DESC: Returns a string with the board int[][] array
     * PRE: N/A
     * POST: Returns a string
     ********************************************************/
    public String toString() {
        String report = "";
        report += "-------------------------\n";
        report += "Board:\n";
        for (int x = 0; x < boardArray.length; x++) {
            for (int y = 0; y < boardArray[0].length; y++) {
                report += "" + boardArray[x][y];
            }
            report += "\n";
        }

        return report;
    }

} //end of Board class
