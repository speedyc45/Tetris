//

//class that represents a "Tetrimino" - a falling block within the Tetris game
public class Tetrimino {
    //initialize the necessary variables
    private int[][] shape;
    private int shapeNum;
    private int size;
    private int xCoord; //coordinates for block of the top left point
    private int yCoord; //(according to their layouts, as shown below, even if it isn't a block!)
    public static final int ROTATE_LEFT = -1;
    public static final int ROTATE_RIGHT = 1;


    //default constructor
    public Tetrimino() {
        this(1);
    } //end of Tetrimino default constructor

    //create a Tetrimino given a number to decide the shape
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

        //add the tetrimino to the board, and add one to the counter
        Board.addTetrimino(this);
        Board.setBlocksSpawned(Board.getBlocksSpawned() + 1);

    } //end of Tetrimino(int shapeNum) constructor

    //rotates a Tetriminos shape so that it pivots upon the central axis 90deg left
    //based on this image: https://vignette.wikia.nocookie.net/tetrisconcept/images/3/3d/SRS-pieces.png/revision/latest?cb=20060626173148
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

    public int getShapeNum() {
        return this.shapeNum;
    }

    public int getSize() {
        return this.size;
    }

    public int[][] getShape() {
        return this.shape;
    }

    public int getxCoord() {return xCoord;}

    public int getyCoord() {return yCoord;}

    public void setxCoord(int x) {this.xCoord = x;}

    public void setyCoord(int y) {this.yCoord = y;}
    //
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
    } //end of toString method

} //end of Tetrimino class
