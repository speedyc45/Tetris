
/***********************************************************************************************************************
 * Tetris.java
 * Description: A GUI based game where the user attempts to manage falling blocks and fill lines to earn points. If the
 *              tower of falling blocks becomes too high, the user loses and their score is recorded. The game begins in
 *              a main menu which gives the user any instructions or contact info needed.
 *
 * Coded by: Callum Kipin
 * Date Started: Oct 2nd, 2018
 * Date Finished: Nov 16th, 2018
 ***********************************************************************************************************************/

//import the necessary libraries
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.*;

/********************************************************
 * DESC: Main Tetris class (runs upon launch), only contains starting method (easy to change if needed)
 ********************************************************/
public class Tetris {
    public static StartWindow startMenu = null;

    /********************************************************
     * DESC: Starts the program and creates a new StartWindow object
     * PRE: Null
     * POST: startMenu object is initialized
     ********************************************************/
    public static void main(String[] args) {
        startMenu = new StartWindow();
    }

}

/********************************************************
 * DESC: StartWindow class is a JFrame window that is created on launch and can run the game, show instructions, or show the title screen
 ********************************************************/
class StartWindow extends JFrame{
    //define the variables for the main menu window
    private JPanel mainMenuPanel;
    private JPanel mainMenuNorthPanel;
    private JPanel mainMenuCenterPanel;
    private JPanel mainMenuSouthPanel;
    private JButton startGameButton;
    private JButton aboutGameButton;
    private JButton instructionsGameButton;
    private JLabel mainTitle;
    private JLabel gameLogo;
    private ImageIcon tetrisLogo; //320x222 pixels
    private ImageIcon tetrisInstructions; //320x245 pixels
    private int[] mainMenuButtonSizes = new int[2]; //x,y

    /********************************************************
     * DESC: A StartWindow object is created, which displays a GUI interface for the user to interact with (main menu)
     * PRE: Null
     * POST: Creates a StartWindow object
     ********************************************************/
    public StartWindow() {
        //set the size, location, close operation, title of the window, initialize the buttons, panels, button size array,
        //and images, and add a window listener
        mainMenuPanel = new JPanel();
        mainMenuNorthPanel = new JPanel();
        mainMenuCenterPanel = new JPanel();
        mainMenuSouthPanel = new JPanel();
        mainTitle = new JLabel("Welcome to TETRIS!");
        tetrisLogo = new ImageIcon("assets\\Tetris_Logo.png");
        tetrisInstructions = new ImageIcon("assets\\Tetris_Instructions.png");
        gameLogo = new JLabel(tetrisLogo);
        startGameButton = new JButton("Start Game");
        aboutGameButton = new JButton("About Game");
        instructionsGameButton = new JButton("Game Instructions");
        mainMenuButtonSizes[0] = 140;
        mainMenuButtonSizes[1] = 26;

        //set the main menu's default closing operation, size,
        //location, and add the mainMenuPanel
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setSize(GameWindow.WIDTH, GameWindow.HEIGHT);
        this.setLocationRelativeTo(null);
        this.addWindowListener(new WindowListener());
        this.setContentPane(mainMenuPanel);

        //set borderlayout for the main panel, and add the main panels
        mainMenuPanel.setLayout(new BorderLayout());
        mainMenuPanel.add(mainMenuNorthPanel, BorderLayout.NORTH);
        mainMenuPanel.add(mainMenuCenterPanel, BorderLayout.CENTER);
        mainMenuPanel.add(mainMenuSouthPanel, BorderLayout.SOUTH);

        //add the buttons and text to their respective panels, separating the buttons by 20 pixels each
        mainMenuNorthPanel.add(mainTitle);
        mainMenuCenterPanel.add(gameLogo);
        mainMenuSouthPanel.setLayout(new BoxLayout(mainMenuSouthPanel, BoxLayout.PAGE_AXIS));
        mainMenuSouthPanel.add(startGameButton);
        mainMenuSouthPanel.add(Box.createVerticalStrut(20));
        mainMenuSouthPanel.add(aboutGameButton);
        mainMenuSouthPanel.add(Box.createVerticalStrut(20));
        mainMenuSouthPanel.add(instructionsGameButton);
        mainMenuSouthPanel.add(Box.createVerticalStrut(20));

        //add listeners to all of the buttons, set their sizes, and align them to the center axis
        startGameButton.addActionListener(new ButtonListener());
        startGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutGameButton.addActionListener(new ButtonListener());
        aboutGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionsGameButton.addActionListener(new ButtonListener());
        instructionsGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startGameButton.setPreferredSize(new Dimension(mainMenuButtonSizes[0],mainMenuButtonSizes[1]));
        aboutGameButton.setPreferredSize(new Dimension(mainMenuButtonSizes[0],mainMenuButtonSizes[1]));
        instructionsGameButton.setPreferredSize(new Dimension(mainMenuButtonSizes[0],mainMenuButtonSizes[1]));

        this.setVisible(true);
    } //end of StartWindow constructor

    /********************************************************
     * DESC: The main menu window is made invisible, and the game window is created and opens to replace it
     * PRE: Null
     * POST: The visible boolean for this is set to false, and a GameWindow object is created
     ********************************************************/
    public void StartGame() {
        this.setVisible(false);
        new GameWindow();
    }

    /********************************************************
     * DESC: The gameLogo image is changed to show instructions for the user
     * PRE: gameLogl and tetrisInstructions cannot be null
     * POST: Calls the setIcon() method for the gameLogo object and passes it the tetrisInstructions object
     ********************************************************/
    public void showInstructions() {
        gameLogo.setIcon(tetrisInstructions);
    }

    /********************************************************
     * DESC: The gameLogo image is changed to show the logo and contact info for the user
     * PRE: gameLogo and tetrisLogo cannot be null
     * POST: Calls the setIcon() method for the gameLogo object and passes it the tetrisLogo object
     ********************************************************/
    public void showAboutGame() {
        gameLogo.setIcon(tetrisLogo);
    }

    /********************************************************
     * DESC: The system will close (if parameter == true) or ask the user if the wish to close (and close if necessary)
     * PRE: A boolean variable is sent as a parameter
     * POST: Application may close, and the pauseGame() method is sent true until the end of the method, then false
     ********************************************************/
    public static void closeApplication(boolean forceClose) {
        //if the application is being forceClosed, close it
        if (forceClose) {
            System.out.println("Closing application...");
            System.exit(0);
        }

        //check if the game is running, and pause it if needed
        if (GameWindow.getGameStart()) {
            GameWindow.pauseGame(true);
        }

        //otherwise ask the user if they want to close the application
        System.out.println("Close application process called...");
        int closeApp = JOptionPane.showConfirmDialog(null, "Are you sure you want to close the application?",
                "Close Application", JOptionPane.YES_NO_OPTION);

        //if the user selected yes, close the application
        if (closeApp == 0) {
            System.out.println("Closing application...");
            System.exit(0);
        }

        //otherwise, unpause the game
        GameWindow.pauseGame(false);
    }

    /********************************************************
     * DESC: Returns a string of basic information of the StartWindow object
     * PRE: Null
     * POST: Returns a string
     ********************************************************/
    public String toString() {
        String report = "";

        report += "-------------------------\n";
        report += "StartWindow Object\n";
        report += "WIDTH: " + GameWindow.WIDTH + "\n";
        report += "HEIGHT: " + GameWindow.HEIGHT + "\n";
        report += "Button Dimensions - X: " + mainMenuButtonSizes[0] + " Y: " + mainMenuButtonSizes[1] + "\n";
        report += "Title: " + mainTitle.getText() + "\n";
        report += "Main Image: " + ((ImageIcon)gameLogo.getIcon()).getDescription() + "\n";

        return report;
    }
} //end of StartWindow class

/********************************************************
 * DESC: Class for the main game window (inheriting from JFrame) that manages the game and displays the GUI for the user
 ********************************************************/
class GameWindow extends JFrame{
    //initialize the necessary variables
    public static final int WIDTH = 360;
    public static final int HEIGHT = 465;
    private final int ANIMATION_REFRESH_RATE = 50;
    private static int dropSpeed = 1000;
    private static boolean gameStart = false;
    private static boolean gamePaused = false;
    private static boolean gameOver = false;
    private static boolean gameRestart = false;
    private static String highScore;
    private static String highScoreName;
    private PaintSurface canvas;
    private static Tetrimino current;
    private static Tetrimino[] nextTetrimino = new Tetrimino[4];
    private static Timer dropTetriminoTimer;

    /********************************************************
     * DESC: Creates a new GameWindow object with the default values
     * PRE: Null
     * POST: Creates a GameWindow object
     ********************************************************/
    public GameWindow() {
        //set the dimensions, title, closing operation, center it in the screen, and add a window listener
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Callum's Tetris");
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowListener());
        this.setLocationRelativeTo(null);

        //make a canvas for animating and add it to the frame
        canvas = new PaintSurface();
        this.add(canvas, BorderLayout.CENTER);
        this.addKeyListener(new KeyListener());

        //create a timer for repainting the animation every 20 (ms?)
        //credit to Java for Dummies
        Timer t = new Timer(ANIMATION_REFRESH_RATE, new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent evt)
            {
                canvas.repaint();
            }
        });
        t.start();

        //initialize the tetrimino array
        for (int x = 0; x < 4; x++) {
            nextTetrimino[x] = new Tetrimino((int)Math.floor(Math.random() * 7 + 1));
        }

        loadHighScore();

        //create a new tetrimino to start the game and make the frame (window) visible
        newTetrimino();
        this.setVisible(true);
    } //end of GameWindow constructor method

    /********************************************************
     * DESC: Clears any full horizontal lines in the boardArray, and creates a new Tetrimino at the top of the boardArray
     *       (if possible)
     * PRE: nextTetrimino[0], current, gameRestart, and gameStart cannot be null
     * POST: dropTetriminoTimer is not null, a new Tetrimino is added to the boardArray, a Tetrimino is removed from the
     *       nextTetrimino array and a new one added, increases the number of blocks spawned by one, and restarts the
     *       timer if necessary
     ********************************************************/
    public static void newTetrimino() {
        //clear any lines if possible, then create another tetrimino
        Board.clearLine();

        //set the current tetrimino to the next in the list
        current = nextTetrimino[0];

        //shift all tetriminoes forward in the array, then make a new one for the end of the array
        for (int x = 0; x < 3; x++) {
            nextTetrimino[x] = nextTetrimino[x+1];
        }
        nextTetrimino[3] = new Tetrimino((int)Math.floor(Math.random() * 7 + 1));

        //add the tetrimino to the board, and add one to the counter
        Board.addTetrimino(current);
        Board.setBlocksSpawned(Board.getBlocksSpawned() + 1);

        //if the game has not started, created the timer for dropping the tetrimino (and set the initial delay to 0ms)
        if (!gameStart) {
            dropTetriminoTimer = new Timer(dropSpeed, new ActionListener(){
                @Override
                public void actionPerformed (ActionEvent evt)
                {
                    Board.tetriminoDrop(current, false);
                }
            });
            dropTetriminoTimer.setInitialDelay(0);
            dropTetriminoTimer.start();
            gameStart = true;
        }

        //if the game is restarted, restart the timer
        if (gameRestart) {
            dropTetriminoTimer.start();
            gameRestart = false;
        }
    }

    /********************************************************
     * DESC: Increases the drop speed of the falling Tetrimino blocks
     * PRE: dropSpeed and dropTetriminoTimer cannot be null
     * POST: Decreases the tick time (dropSpeed) by 200ms for the dropTetriminoTimer, and increases the level by 1
     ********************************************************/
    public static void levelUp() {
        if (Board.getLevel() < 4) {
            Board.setLevel(Board.getLevel() + 1);
            dropSpeed -= 200;
            dropTetriminoTimer.setDelay(dropSpeed);

            System.out.println("Level up - level: " + Board.getLevel());
        } else {
            System.out.println("Max level reached.");
        }

    }

    /********************************************************
     * DESC: Reads in the highScore and highScoreName from a txt file
     * PRE: highscore.txt is created, within the data folder, and has the highscore and name (formatted correctly)
     * POST: Sets the highScoreName and highScore variables as information read from a txt file
     ********************************************************/
    private static void loadHighScore() {
        //load the previous highscore from a file
        try {
            BufferedReader br = new BufferedReader(new FileReader("data/highscore.txt"));
            highScoreName = br.readLine();
            highScore = br.readLine();

            br.close();
        } catch (Exception e) {
            System.out.println("Error loading in high score. Setting to 0.");
            highScoreName = "John Smith";
            highScore = "0";
        }
    }

    /********************************************************
     * DESC: Overrides the high score (if surpassed), and resets the game (if the user wishes, otherwise the application is closed)
     * PRE: No more tetriminos can "spawn"
     * POST: The highScore and highScoreName variables may be overiden (if the score is surpassed) on both the file and in local
     *       variables, and the game is either reset or the application is closed
     ********************************************************/
    public static void gameOver() {
        String name = "";
        char letter;
        int playAgain;
        boolean invalidName = true;

        //stop the drop tetrimino timer, and set gameOver to true
        dropTetriminoTimer.stop();
        gameOver = true;

        //loop to ensure the user enters a valid name (letters only)
        while (invalidName) {
            name = JOptionPane.showInputDialog("Game Over\nPlease enter your name: ");

            while (name == null) {
                name = JOptionPane.showInputDialog("Invalid Entry\nPlease enter your name: ");
            }

            for (int x = 0; x < name.length(); x++) {
                letter = name.charAt(x);

                if (!Character.isLetter(letter) && !(letter == ' ')) {
                    break;
                } else if (x == name.length()-1) {
                    invalidName = false;
                }
            }
        }

        //saves the score and name if it's a new highscore
        if (Integer.parseInt(highScore) < Board.getScore()) {
            try {
                PrintWriter pw = new PrintWriter(new FileWriter("data/highscore.txt"));
                pw.println(name);
                pw.println(Board.getScore());

                pw.close();
            } catch (Exception e) {
                System.out.println("Error saving the new highscore.");
            }
        }

        //ask if the user wants to play again, and restart if application if so (otherwise, close it)
        playAgain = JOptionPane.showConfirmDialog(null, "Do you want to play again?",
                "Play Again?", JOptionPane.YES_NO_OPTION);

        //if the user wants to play again, reset the game and start again, otherwise close the application
        if (playAgain == 0) {
            //update the highscore and highscore name
            loadHighScore();

            //reset the boardArray, tetrimino array, score, level, blocksSpawned, and gameOver
            for (int x = 0; x < nextTetrimino.length; x++) {
                nextTetrimino[x] = new Tetrimino((int)Math.floor(Math.random() * 7 + 1));
            }
            Board.clearBoard();
            Board.setScore(0);
            Board.setLevel(1);
            Board.setBlocksSpawned(0);
            Board.setRowsCleared(0);
            gameOver = false;

            //set the gameRestart flag to true, and create a new tetrimino to start the game
            gameRestart = true;
            newTetrimino();
        } else {
            System.exit(0);
        }
    }

    /********************************************************
     * DESC: Calls a method to rotate the current Tetrimino object in a specified direction
     * PRE: rot and current cannot be null
     * POST: Calls the tetriminoRotate method in the board class, passing the current tetrimino and rotation direction
     ********************************************************/
    public static void rotateTetrimino(int rot) { Board.tetriminoRotate(current, rot); }

    /********************************************************
     * DESC: Calls a method to move the current Tetrimino object in the specified direction
     * PRE: dir and current cannot be null
     * POST: Calls the tetriminoRotate method in the board class, passing the current tetrimino and move direction
     ********************************************************/
    public static void moveTetrimino(int dir) { Board.tetriminoMove(current, dir); }

    /********************************************************
     * DESC: Speeds up the falling tetrimino speed (if given a true value), otherwise it returns the drop speed to its normal setting
     * PRE: drop, dropTetriminoTimer, and dropSpeed cannot be null
     * POST: Dencreases the dropTetriminoTimer tick speed to 100ms and resets it, or returns the timer to it's previous tick speed
     *       (depending on the boolean given)
     ********************************************************/
    public static void setSoftDropTetrimino(boolean drop) {
        if (drop) {
            //stop the timer, set it's delay to 100ms, then restart the timer
            dropTetriminoTimer.stop();
            dropTetriminoTimer.setDelay(100);
            dropTetriminoTimer.start();
        } else {
            //set the delay time back to its normal setting
            dropTetriminoTimer.setDelay(dropSpeed);
        }
    }

    /********************************************************
     * DESC: Calls methods to move the falling Tetrimino (current) to the bottom of the board (as far down as possible),
     *       then resets the dropTetriminoTimer (to spawn another block instantly - without any delay)
     * PRE: current object cannot be null, and the dropTetriminoTimer object cannot be null
     * POST: Calls the tetriminoInstantDrop method in the board class, passing the current tetrimino object, and calls the
     *       resetDropTimer method
     ********************************************************/
    public static void instantDropTetrimino() { Board.tetriminoInstantDrop(current); dropTetriminoTimer.restart(); }


    /********************************************************
     * DESC: "Pauses" the game by stopping the falling of blocks (stops the dropTimer), or "unpauses" it
     * PRE: pause is not null
     * POST: Stops the dropTetriminoTimer if given true and sets gamePaused to true, or starts the
     *       dropTetriminoTimer if given false and sets gamePaused to false
     ********************************************************/
    public static void pauseGame(boolean pause) {
        if (pause) {
            gamePaused = true;
            dropTetriminoTimer.stop();
        } else {
            gamePaused = false;
            dropTetriminoTimer.start();
        }
    }

    /********************************************************
     * DESC: Returns the array of Tetrimino objects nextTetrimino
     * PRE: nextTetrimino is not null
     * POST: Returns an array of Tetrimino objects
     ********************************************************/
    public static Tetrimino[] getNextTetrimino() { return nextTetrimino; }

    /********************************************************
     * DESC: Returns the boolean gameStart
     * PRE: gameStart is not null
     * POST: Returns a boolean
     ********************************************************/
    public static boolean getGameStart() { return gameStart; }

    /********************************************************
     * DESC: Returns the boolean gamePaused
     * PRE: gamePaused is not null
     * POST: Returns a boolean
     ********************************************************/
    public static boolean getGamePaused() { return gamePaused; }

    /********************************************************
     * DESC: Returns the boolean gameOver
     * PRE: gameOver is not null
     * POST: Returns a boolean
     ********************************************************/
    public static boolean getGameOver() { return gameOver; }

    /********************************************************
     * DESC: Returns the string highScore
     * PRE: highScore is not null
     * POST: Returns a string
     ********************************************************/
    public static String getHighScore() { return highScore; }

    /********************************************************
     * DESC: Returns the string highScoreName
     * PRE: highScoreName is not null
     * POST: Returns a string
     ********************************************************/
    public static String getHighScoreName() { return highScoreName; }

    /********************************************************
     * DESC: Returns a string that states the width and height of the GameWindow
     * PRE: Null
     * POST: Returns a string
     ********************************************************/
    public String toString() {
        String report = "";

        report += "-------------------------\n";
        report += "GameWindow Object\n";
        report += "WIDTH: " + WIDTH + "\n";
        report += "HEIGHT: " + HEIGHT + "\n";
        report += "GameStarted: " + gameStart + "\n";
        report += "GameOver: " + gameOver + "\n";
        report += "GamePaused: " + gamePaused + "\n";
        report += "Highscore Name: " + highScoreName + "\n";
        report += "Highscore: " + highScore + "\n";
        report += "Drop Speed: " + dropSpeed + "\n";
        report += "Animation Refresh Rate: " + ANIMATION_REFRESH_RATE + "\n";

        return report;
    } //end of the toString method

} //end of GameWindow class

/********************************************************
 * DESC: Class that allows for the custom rendering of images and other graphics with precision (coordinates) in a GUI
 ********************************************************/
class PaintSurface extends JComponent {
    //define the necessary variables
    private Image gridBackground = new ImageIcon("assets\\Tetris_Grid_Background.png").getImage();
    private Image nextBackground = new ImageIcon("assets\\Tetris_Grid_Next_Alt.png").getImage();
    //private Image holdBackground = new ImageIcon("assets\\Tetris_Grid_Hold.png").getImage();
    private Image block;
    private final int backgroundOffsetX = 5;
    private final int backgroundOffsetY = 5;
    private static final int gridBackgroundWidth = 210;
    private static final int gridBackgroundLength = 409;
    private int blockPosX = backgroundOffsetX + 4;
    private int blockPosY = backgroundOffsetY + 5;
    private Board tetrisBoard = new Board();
    private Graphics2D colouredText;

    /********************************************************
     * DESC: "Repaints" the canvas by redrawing any graphics inside it, checking whether any conditions are met for the GUI
     *       (e.g. gamePaused or gameOver) and restricts what is drawn and draws specific graphics for the scenario. Generally
     *       draws the main Tetris board, upcoming blocks, scores, level, and more)
     * PRE: Takes a Graphics object that cannot be null
     * POST:
     ********************************************************/
    public void paint(Graphics g) {
        //draw the images for the game (and the score factors)
        g.drawImage(gridBackground, backgroundOffsetX, backgroundOffsetY, null);
        g.drawImage(nextBackground, backgroundOffsetX + gridBackgroundWidth + 10, backgroundOffsetY, null);
        g.drawString("HIGHSCORE NAME:", blockPosX * 2 + 20 * Board.COLUMNS -2, 20 * Board.ROWS - 95);
        g.drawString(GameWindow.getHighScoreName(), blockPosX * 2 + 20 * Board.COLUMNS -2, 20 * Board.ROWS - 75);
        g.drawString("HIGHSCORE: " + GameWindow.getHighScore(), blockPosX * 2 + 20 * Board.COLUMNS -2, 20 * Board.ROWS - 55);
        g.drawString("SCORE: " + Board.getScore(), blockPosX * 2 + 20 * Board.COLUMNS -2, 20 * Board.ROWS - 35);
        g.drawString("LEVEL: " + Board.getLevel(), blockPosX * 2 + 20 * Board.COLUMNS -2, 20 * Board.ROWS - 15);
        g.drawString("LINES CLEARED: " + Board.getRowsCleared(), blockPosX * 2 + 20 * Board.COLUMNS -2, 20 * Board.ROWS + 5);

        if (GameWindow.getGameOver()) { //check if the game is over, and add the "Game Over" text if so
            colouredText = (Graphics2D)g;
            colouredText.setColor(Color.white);
            colouredText.drawString("GAME OVER", backgroundOffsetX + gridBackgroundWidth/2 - 37, backgroundOffsetY + gridBackgroundLength/2);
        } else if (GameWindow.getGamePaused()) { //check if the game is paused, and add the "Game Paused" text if so
            colouredText = (Graphics2D)g;
            colouredText.setColor(Color.white);
            colouredText.drawString("GAME PAUSED", backgroundOffsetX + gridBackgroundWidth/2 - 43, backgroundOffsetY + gridBackgroundLength/2);
        } else {
            //draw the board (check for any values in the board, and draw the correct block for it - in the correct position)
            for (int x = 0; x < Board.ROWS; x++) {
                for (int y = 0; y < Board.COLUMNS; y++) {
                    switch (tetrisBoard.getBoardArray()[x][y]) {
                        case 0:
                            //no block, do nothing
                            break;
                        case 1:
                            block = Board.blockBlue.getImage();
                            g.drawImage(block, blockPosX + y * 20, blockPosY + x * 20, null);
                            break;
                        case 2:
                            block = Board.blockOrange.getImage();
                            g.drawImage(block, blockPosX + y * 20, blockPosY + x * 20, null);
                            break;
                        case 3:
                            block = Board.blockYellow.getImage();
                            g.drawImage(block, blockPosX + y * 20, blockPosY + x * 20, null);
                            break;
                        case 4:
                            block = Board.blockGreen.getImage();
                            g.drawImage(block, blockPosX + y * 20, blockPosY + x * 20, null);
                            break;
                        case 5:
                            block = Board.blockPink.getImage();
                            g.drawImage(block, blockPosX + y * 20, blockPosY + x * 20, null);
                            break;
                        case 6:
                            block = Board.blockPurple.getImage();
                            g.drawImage(block, blockPosX + y * 20, blockPosY + x * 20, null);
                            break;
                        case 7:
                            block = Board.blockLightBlue.getImage();
                            g.drawImage(block, blockPosX + y * 20, blockPosY + x * 20, null);
                            break;
                        default:
                            //TODO ERROR!
                            break;
                    }
                }
            }

            //draw the next tetriminoes (check for any values in the board, and draw the correct block for it - in the correct position)
            for (int t = 0; t < GameWindow.getNextTetrimino().length; t++) {
                for (int x = 0; x < 2; x++) {
                    Outer:
                    for (int y = 0; y < GameWindow.getNextTetrimino()[t].getSize(); y++) {
                        switch (GameWindow.getNextTetrimino()[t].getShape()[x][y]) {
                            case 0:
                                //no block, do nothing
                                continue Outer;
                            case 1:
                                block = Board.blockBlue.getImage();
                                break;
                            case 2:
                                block = Board.blockOrange.getImage();
                                break;
                            case 3:
                                block = Board.blockYellow.getImage();
                                break;
                            case 4:
                                block = Board.blockGreen.getImage();
                                break;
                            case 5:
                                block = Board.blockPink.getImage();
                                break;
                            case 6:
                                block = Board.blockPurple.getImage();
                                break;
                            case 7:
                                block = Board.blockLightBlue.getImage();
                                break;
                            default:
                                //TODO ERROR!
                                break;
                        }
                        g.drawImage(block, blockPosX + y * 20 + gridBackgroundWidth + 10, blockPosY + x * 20 + t * 60, null);
                    }
                }
            }
        }

    } //end of paint method

    /********************************************************
     * DESC: Returns a string of basic information of the PaintSurface object
     * PRE: Null
     * POST: Returns a string
     ********************************************************/
    public String toString() {
        String report = "";

        report += "-------------------------\n";
        report += "PaintSurface Object\n";
        report += "Width: " + WIDTH + "\n";
        report += "Height: " + HEIGHT + "\n";
        report += "Memory Address: " + super.toString();

        return report;
    }

} //end of PaintSurface class

/********************************************************
 * DESC: Class that handles key input during the game's runtime
 ********************************************************/
class KeyListener implements java.awt.event.KeyListener {
    private static boolean releaseKeyReturn = false;
    private static boolean releaseKeySpace = false;
    private static boolean releaseKeyLeft = false;
    private static boolean releaseKeyRight = false;
    private static boolean releaseKeyUp = false;
    private static boolean releaseKeyDown = false;

    @Override
    public void keyTyped(KeyEvent e) { }

    /********************************************************
     * DESC: Calls the corresponding method (to the key press), and the system flags the key as "pressed"
     * PRE: Takes a KeyEvent object that cannot be null
     * POST: Respective flag to the key press is set to true
     ********************************************************/
    @Override
    public void keyPressed(KeyEvent e ) {
        if (e.getKeyCode() == 32 && !releaseKeySpace) { //check for the space bar, and if it's being pressed again
            System.out.println("Instant dropping tetrimino...");
            GameWindow.instantDropTetrimino();
            releaseKeySpace = true;
        } else
        if (e.getKeyCode() == 37 && !releaseKeyLeft) { //check for the left arrow key, and if it's being pressed again
            System.out.println("Moving the tetrimino left...");
            GameWindow.moveTetrimino(Board.LEFT);
            releaseKeyLeft = true;
        } else if (e.getKeyCode() == 38 && !releaseKeyUp) { //check for the up arrow key, and if it's being pressed again
            System.out.println("Rotating the tetrimino left...");
            GameWindow.rotateTetrimino(Tetrimino.ROTATE_LEFT);
            releaseKeyUp = true;
        } else if (e.getKeyCode() == 39 && !releaseKeyRight) { //check for the right arrow key, and if it's being pressed again
            System.out.println("Moving the tetrimino right...");
            GameWindow.moveTetrimino(Board.RIGHT);
            releaseKeyRight = true;
        } else if (e.getKeyCode() == 40 && !releaseKeyDown) { //check for the down arrow key, and if it's being pressed again
            System.out.println("Rotating the tetrimino right...");
            GameWindow.rotateTetrimino(Tetrimino.ROTATE_RIGHT);
            releaseKeyDown = true;
        } else if (e.getKeyCode() == 10 && !releaseKeyReturn) { //check for the return key, and if it's being pressed again
            GameWindow.setSoftDropTetrimino(true);
            releaseKeyReturn = true;
        } else if (e.getKeyCode() == 27) { //check for the escape key, then quit if pressed
            if (GameWindow.getGamePaused()) {
                GameWindow.pauseGame(false);
            } else {
                GameWindow.pauseGame(true);
            }
        }
    }

    /********************************************************
     * DESC: Checks for what key is released, and unflags that key if it is one of the used keys
     * PRE: Takes a KeyEvent object that cannot be null
     * POST: The system will unflag a pressed key it recognizes
     ********************************************************/
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 32) { //check for the left arrow key
            releaseKeySpace = false;
        } else if (e.getKeyCode() == 37) { //check for the left arrow key
            releaseKeyLeft = false;
        } else if (e.getKeyCode() == 38) { //check for the up arrow key
            releaseKeyUp = false;
        } else if (e.getKeyCode() == 39) { //check for the right arrow key
            releaseKeyRight = false;
        } else if (e.getKeyCode() == 40) { //check for the down arrow key
            releaseKeyDown = false;
        } else if (e.getKeyCode() == 10) { //check for the return key
            GameWindow.setSoftDropTetrimino(false);
            releaseKeyReturn = false;
        }
        System.out.println("Key Released: " + e.getKeyCode());
    }

    /********************************************************
     * DESC: Returns a string of basic information of the KeyListener object
     * PRE: N/A
     * POST: Returns a string
     ********************************************************/
    public String toString() {
        String report = "";

        report += "-------------------------\n";
        report += "KeyListener Object\n";
        report += "releaseKeyUp: " + releaseKeyUp + "\n";
        report += "releaseKeyDown: " + releaseKeyDown + "\n";
        report += "releaseKeyLeft: " + releaseKeyLeft + "\n";
        report += "releaseKeyRight: " + releaseKeyRight + "\n";
        report += "releaseKeySpace: " + releaseKeySpace + "\n";
        report += "releaseKeyReturn: " + releaseKeyReturn + "\n";
        report += "Memory Address: " + super.toString();

        return report;
    }
} //end of KeyListener class

/********************************************************
 * DESC: Class that acts as a listener for when the user interacts with the frame (window) buttons (e.g. close, minimize, etc.),
 *       and has methods that are called when they are interacted with
 ********************************************************/
class WindowListener implements java.awt.event.WindowListener {

    @Override
    public void windowActivated(WindowEvent e) { }

    @Override
    public void windowClosed(WindowEvent e) { }

    /********************************************************
     * DESC: This method is called when the user presses the close button on a window, and runs the closeApplication() method
     * PRE: N/A
     * POST: The closeApplication method is called
     ********************************************************/
    @Override
    public void windowClosing(WindowEvent e) { StartWindow.closeApplication(false); }

    @Override
    public void windowDeactivated(WindowEvent e) { }

    @Override
    public void windowDeiconified(WindowEvent e) { }

    @Override
    public void windowIconified(WindowEvent e) { }

    @Override
    public void windowOpened(WindowEvent e) { }

    /********************************************************
     * DESC: Returns a String of basic information of the WindowListener object
     * PRE: N/A
     * POST: Returns a String
     ********************************************************/
    public String toString() {
        String report = "";

        report += "-------------------------\n";
        report += "WindowListener Object\n";
        report += "Memory Address: " + super.toString();

        return report;
    }
}

/********************************************************
 * DESC: Class that acts as a listener for when the user interacts with one of the buttons in the GUI
 ********************************************************/
class ButtonListener implements java.awt.event.ActionListener {

    /********************************************************
     * DESC: Checks which GUI button has been clicked, and calls the required method for that button
     * PRE:  Takes an ActionEvent object that cannot be null and should be from one of the three GUI buttons
     * POST: Calls the required method to start the game, show the game logo, or show the instructions
     ********************************************************/
    @Override
    public void actionPerformed(ActionEvent e) {

        //check which button has been clicked of the main menu buttons
        if (e.getActionCommand().equals("Start Game")) {
            System.out.println("Start button clicked...");
            Tetris.startMenu.StartGame();
        } else if (e.getActionCommand().equals("About Game")) {
            System.out.println("About button clicked...");
            Tetris.startMenu.showAboutGame();
        } else if (e.getActionCommand().equals("Game Instructions")) {
            System.out.println("Instructions button clicked...");
            Tetris.startMenu.showInstructions();
        }
    }

    /********************************************************
     * DESC: Returns a string of basic information of the ButtonListener object
     * PRE: Null
     * POST: Returns a string
     ********************************************************/
    public String toString() {
        String report = "";

        report += "-------------------------\n";
        report += "ButtonListener Object\n";
        report += "Memory Address: " + super.toString();

        return report;
    }
}