//

//
public class Tetrimino {
    //initialize the necessary variables
    private char[][] shape;
    private int shapeNum;
    public static final int ROTATE_LEFT = 0;
    public static final int ROTATE_RIGHT = 1;


    //default constructor
    public Tetrimino() {

    } //end of Tetrimino default constructor

    //create a Tetrimino given a number to decide the shape
    public Tetrimino(int shapeNum) {
        this.shapeNum = shapeNum;

        //make the basic shape of the block
        switch (shapeNum) {
            case 1:
                shape = new char[][]{
                        {'x', '-', '-'},
                        {'x', 'x', 'x'},
                        {'-', '-', '-'}
                };
                break;
            case 2:
                shape = new char[][]{
                        {'-', '-', 'x'},
                        {'x', 'x', 'x'},
                        {'-', '-', '-'}
                };
                break;
            case 3:
                shape = new char[][]{
                        {'x', 'x'},
                        {'x', 'x'}
                };
                break;
            case 4:
                shape = new char[][]{
                        {'-', 'x', 'x'},
                        {'x', 'x', '-'},
                        {'-', '-', '-'}
                };
                break;
            case 5:
                shape = new char[][]{
                        {'-', 'x', '-'},
                        {'x', 'x', 'x'},
                        {'-', '-', '-'}
                };
                break;
            case 6:
                shape = new char[][]{
                        {'x', 'x', '-'},
                        {'-', 'x', 'x'},
                        {'-', '-', '-'}
                };
                break;
            case 7:
                shape = new char[][]{
                        {'-', '-', '-', '-'},
                        {'x', 'x', 'x', 'x'},
                        {'-', '-', '-', '-'},
                        {'-', '-', '-', '-'}
                };
                break;
            default:
                shape = new char[][]{
                        {'x', '-', '-'},
                        {'x', 'x', 'x'},
                        {'-', '-', '-'}
                };
        } //end of shape deciding switch statement
    } //end of Tetrimino(int shapeNum) constructor

    //
    public void rotate(int rotateDir) {
        char[][] newShape;
        if (rotateDir == ROTATE_LEFT) {
            if (shape[0].length == 2) {
                //do nothing, a square can't be rotated
            } else if (shape[0].length == 3) {
                System.out.println("Rotating a three shape left...");

                //swap the placement of the blocks in the grid
                newShape = new char[3][3];

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
                newShape = new char[4][4];

                //rotate the shape left one, and set it to the newShape array
                newShape[0][0] = '-'; //corners don't become anything
                newShape[0][1] = shape[1][3];
                newShape[0][2] = shape[2][3];
                newShape[0][3] = '-'; //corners don't become anything
                newShape[1][0] = shape[0][2];
                newShape[1][1] = shape[1][2];
                newShape[1][2] = shape[2][2];
                newShape[1][3] = shape[3][2];
                newShape[2][0] = shape[0][1];
                newShape[2][1] = shape[1][1];
                newShape[2][2] = shape[2][1];
                newShape[2][3] = shape[3][1];
                newShape[3][0] = '-'; //corners don't become anything
                newShape[3][1] = shape[1][0];
                newShape[3][2] = shape[2][0];
                newShape[3][3] = '-'; //corners don't become anything

                //set the original shape as the newShape
                for (int x = 0; x < shape.length; x++) {
                    for (int y = 0; y < shape.length; y++) {
                        shape[x][y] = newShape[x][y];
                    }
                }
            }
        } else if (rotateDir == ROTATE_RIGHT) {

        }


    } //end of rotate method

    //
    public String toString() {
        String report = "";
        report += "-------------------------\n";
        report += "Shape: " + shapeNum + "\n";
        for (int x = 0; x < shape.length; x++) {
            for (int y = 0; y < shape.length; y++) {
                report += shape[x][y];
            }
            report += "\n";
        }

        return report;
    } //end of toString method

} //end of Tetrimino class
