
/********************************************************
 * DESC: Class that represents a "Tetrimino" - a falling block within the Tetris game
 ********************************************************/
public class Tetrimino {
    //initialize the necessary variables
    private int[][] shape;
    private int shapeNum;
    private int size;
    private int xCoord; //coordinates for block of the top left point
    private int yCoord; //(according to their layouts, as shown below, even if it isn't a block!)
    public static final int ROTATE_LEFT = -1;
    public static final int ROTATE_RIGHT = 1;


    /********************************************************
     * DESC:
     * PRE: Null
     * POST: Runs the Tetrimino(int shapeNum) constructor with a value of 1
     ********************************************************/
    public Tetrimino() {
        this(1);
    } //end of Tetrimino default constructor

    /********************************************************
     * DESC:
     * PRE: Null
     * POST: Creates a copy of a given Tetrimino object
     ********************************************************/
    public Tetrimino(Tetrimino t) {
        shapeNum = t.getShapeNum();

        for (int x = 0; x < shapeNum; x++) {
            for (int y = 0; y < shapeNum; y++) {
                shape[x][y] = t.getShape()[x][y];
            }
        }

        xCoord = t.getxCoord();
        yCoord = t.getyCoord();
    }

    /********************************************************
     * DESC:
     * PRE: Null
     * POST: Creates a Tetrimino given a number to decide the shape
     ********************************************************/
    public Tetrimino(int shapeNum) {
        this.shapeNum = shapeNum;

        //make the basic shape of the block
        switch (shapeNum) {
            case 1:
                shape = new int[][]{
                        {1, 0, 0},
                        {1, 1, 1},
                        {0, 0, 0}
                };
                size = 3;
                xCoord = 3;
                yCoord = 0;
                break;
            case 2:
                shape = new int[][]{
                        {0, 0, 2},
                        {2, 2, 2},
                        {0, 0, 0}
                };
                size = 3;
                xCoord = 3;
                yCoord = 0;
                break;
            case 3:
                shape = new int[][]{
                        {3, 3},
                        {3, 3}
                };
                size = 2;
                xCoord = 4;
                yCoord = 0;
                break;
            case 4:
                shape = new int[][]{
                        {0, 4, 4},
                        {4, 4, 0},
                        {0, 0, 0}
                };
                size = 3;
                xCoord = 3;
                yCoord = 0;
                break;
            case 5:
                shape = new int[][]{
                        {0, 5, 0},
                        {5, 5, 5},
                        {0, 0, 0}
                };
                size = 3;
                xCoord = 3;
                yCoord = 0;
                break;
            case 6:
                shape = new int[][]{
                        {6, 6, 0},
                        {0, 6, 6},
                        {0, 0, 0}
                };
                size = 3;
                xCoord = 3;
                yCoord = 0;
                break;
            case 7:
                shape = new int[][]{
                        {0, 0, 0, 0},
                        {7, 7, 7, 7},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}
                };
                size = 4;
                xCoord = 3;
                yCoord = 0;
                break;
            default:
                shape = new int[][]{
                        {1, 0, 0},
                        {1, 1, 1},
                        {0, 0, 0}
                };
                size = 3;
                xCoord = 3;
                yCoord = 0;
                break;
        } //end of shape deciding switch statement

        //test print statement
        System.out.println("New Block - ROW: " + yCoord + " COL:" + xCoord + " Size:" + size + " Shape:" + shapeNum);

    } //end of Tetrimino(int shapeNum) constructor

    /********************************************************
     * DESC:
     * PRE: An integer of -1 or +1 must be passed as a parameter
     * POST: Rotates a Tetrimino's shape so that it pivots upon the central axis 90 degrees left
     *       based on this image: https://vignette.wikia.nocookie.net/tetrisconcept/images/3/3d/SRS-pieces.png/revision/latest?cb=20060626173148
     ********************************************************/
    public void rotate(int rotateDir) {
        int[][] newShape;
        int rotateLeftCounter = 0;

        //then rotate its shape array
        if (rotateDir == ROTATE_LEFT) {
            rotateLeftCounter = 1;
        } else if (rotateDir == ROTATE_RIGHT) {
            rotateLeftCounter = 3;
        }

        for (; rotateLeftCounter > 0; rotateLeftCounter--) {
            if (shape[0].length == 2) {
                //do nothing, a square can't be rotated
            } else if (shape[0].length == 3) {
                //System.out.println("Rotating a three shape left...");

                //swap the placement of the blocks in the grid
                newShape = new int[3][3];

                //rotate the shape left one, and set it to the newShape array
                newShape[0][0] = shape[0][2];
                newShape[0][1] = shape[1][2];
                newShape[0][2] = shape[2][2];
                newShape[1][0] = shape[0][1];
                newShape[1][1] = shape[1][1]; //redundant, but newShape must be filled
                newShape[1][2] = shape[2][1];
                newShape[2][0] = shape[0][0];
                newShape[2][1] = shape[1][0];
                newShape[2][2] = shape[2][0];

                //set the original shape as the newShape
                for (int x = 0; x < shape.length; x++) {
                    for (int y = 0; y < shape.length; y++) {
                        shape[x][y] = newShape[x][y];
                    }
                }
            } else if (shape[0].length == 4) {
                System.out.println("Rotating a four shape left...");

                //swap the placement of the blocks in the grid
                newShape = new int[4][4];

                //rotate the shape left one, and set it to the newShape array
                newShape[0][0] = 0; //corners don't become anything
                newShape[0][1] = shape[1][3];
                newShape[0][2] = shape[2][3];
                newShape[0][3] = 0; //corners don't become anything
                newShape[1][0] = shape[0][2];
                newShape[1][1] = shape[1][2];
                newShape[1][2] = shape[2][2];
                newShape[1][3] = shape[3][2];
                newShape[2][0] = shape[0][1];
                newShape[2][1] = shape[1][1];
                newShape[2][2] = shape[2][1];
                newShape[2][3] = shape[3][1];
                newShape[3][0] = 0; //corners don't become anything
                newShape[3][1] = shape[1][0];
                newShape[3][2] = shape[2][0];
                newShape[3][3] = 0; //corners don't become anything

                //set the original shape as the newShape
                for (int x = 0; x < shape.length; x++) {
                    for (int y = 0; y < shape.length; y++) {
                        shape[x][y] = newShape[x][y];
                    }
                }
            }
        } //end of rotation loop

    } //end of rotate method

    /********************************************************
     * DESC:
     * PRE: Null
     * POST: Returns the shapeNum int
     ********************************************************/
    public int getShapeNum() { return this.shapeNum; }

    /********************************************************
     * DESC:
     * PRE: Null
     * POST: Returns the size int
     ********************************************************/
    public int getSize() { return this.size; }

    /********************************************************
     * DESC:
     * PRE: Null
     * POST: Returns the shape int[][]
     ********************************************************/
    public int[][] getShape() { return this.shape; }

    /********************************************************
     * DESC:
     * PRE: Null
     * POST: Return the xCoord int
     ********************************************************/
    public int getxCoord() {return xCoord;}

    /*
     * PRE: Null
     * POST: Return the yCoord int
     */
    public int getyCoord() {return yCoord;}

    /*
     * PRE: Sent an int parameter
     * POST: Sets the xCoord int to the given parameter
     */
    public void setxCoord(int x) {this.xCoord = x;}

    /*
     * PRE: Sent an int parameter
     * POST: Sets the yCoord int to the given parameter
     */
    public void setyCoord(int y) {this.yCoord = y;}

    /*
     * PRE: Null
     * POST: Returns a string with the shape int[][] array
     */
    public String toString() {
        String report = "";
        report += "-------------------------\n";
        report += "Shape: " + shapeNum + "\n";
        for (int x = 0; x < shape.length; x++) {
            for (int y = 0; y < shape.length; y++) {
                report += shape[x][y] + " ";
            }
            report += "\n";
        }

        return report;
    }

    /*
     * PRE: Takes a Tetrimino object as a parameter which cannot be null
     * POST: Returns a boolean that states whether the two objects are the same
     */
    public boolean equals(Tetrimino t) {
        //if the shape (rotation and type of tetrimino) is the same, then they are equal
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[0].length; col++) {
                if (shape[row][col] != t.getShape()[row][col]) {
                    return false;
                }
            }
        }

        return true;
    }

} //end of Tetrimino class
