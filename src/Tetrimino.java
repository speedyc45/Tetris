//

//
public class Tetrimino {
    //initialize the necessary variables
    Block[] blocks;
    char[][] shape;
    int shapeNum;

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
} //end of Tetrimino class
