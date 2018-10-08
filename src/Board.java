//

//class that manages the tetris board
public class Board {
    //initialize the necessary variables
    private static final int COLUMNS = 10;
    private static final int ROWS = 22;
    private static final int VISIBLE_ROWS = 20;
    private int[][] boardArray;

    //default constructor
    public Board() {
        //create the size of the board, which starts with no blocks
        boardArray = new int[ROWS][COLUMNS];
    }
}
