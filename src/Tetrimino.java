//

//
public class Tetrimino {
    //initialize the necessary variables
    private Block[] blocks;
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
                        {'x', 'x', 'x', 'x'},
                        {'-', '-', '-', '-'},
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
                //swap the placement of the blocks in the grid
                newShape = new char[3][3];

                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 3; x++) {
                        if (y == 0) {
                            newShape[y+(2-x)][y] = shape[y][x];
                        } else if (y == 1 && x == 0) {
                            newShape[y][2] = shape[y][x];
                        } else if (y == 1 && x == 1) {
                            //skip the rotation point in the shape
                        } else if (y == 1 && x == 2) {
                            newShape[y][0] = shape[y][x];
                        } else if (y == 2) {
                            newShape[x][x+(2-y)] = shape[y][x];
                        }
                    } //end of x for loop
                } //end of y for loop
                System.out.println(newShape);
            } else if (shape[0].length == 4) {

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
