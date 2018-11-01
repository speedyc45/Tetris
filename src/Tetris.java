/*
 * Tetris.java
 * Description: /TODO/
 *
 * Written by: Callum Kipin
 * Date Started: Oct 2nd, 2018
 * Date Finished: /TODO/
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Tetris {
    //
    public static StartWindow startMenu = null;

    public static void main(String[] args) {
        startMenu = new StartWindow();
    } //end of main method

} //end of main Tetris class

//
class StartWindow extends JFrame{
    //for the main menu window
    private JPanel mainMenuPanel;
    private JPanel mainMenuNorthPanel;
    private JPanel mainMenuCenterPanel;
    private JPanel mainMenuSouthPanel;
    private JButton startGameButton;
    private JButton aboutGameButton;
    private JButton instructionsGameButton;
    private JLabel mainTitle;
    private JLabel gameLogo;
    private int[] mainMenuButtonSizes = new int[2];
    //private int[] backgroundColor = new int[3];

    //
    public StartWindow() {
        //set the size, location, close operation, and title of the window //TODO
        mainMenuPanel = new JPanel();
        mainMenuNorthPanel = new JPanel();
        mainMenuCenterPanel = new JPanel();
        mainMenuSouthPanel = new JPanel();
        mainTitle = new JLabel("Welcome to TETRIS!");
        gameLogo = new JLabel(new ImageIcon("assets\\Tetris_Logo.png"));
        startGameButton = new JButton("Start Game");
        aboutGameButton = new JButton("About Game");
        instructionsGameButton = new JButton("Game Instructions");
        mainMenuButtonSizes[0] = 140;
        mainMenuButtonSizes[1] = 26;
        /*backgroundColor[0] = 145;
        backgroundColor[1] = 187;
        backgroundColor[2] = 255;*/

        //set the main menu's default closing operation, size,
        //location, and add the mainMenuPanel
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setSize(GameWindow.width, GameWindow.height);
        this.setLocationRelativeTo(null);
        this.addWindowListener(new WindowListener());
        this.setContentPane(mainMenuPanel);

        //set borderlayout for the main panel, and add the main panels
        mainMenuPanel.setLayout(new BorderLayout());
        mainMenuPanel.add(mainMenuNorthPanel, BorderLayout.NORTH);
        mainMenuPanel.add(mainMenuCenterPanel, BorderLayout.CENTER);
        mainMenuPanel.add(mainMenuSouthPanel, BorderLayout.SOUTH);

        /*//set the colours of the main three panels
        mainMenuNorthPanel.setBackground(new Color(backgroundColor[0], backgroundColor[1], backgroundColor[2]));
        mainMenuCenterPanel.setBackground(new Color(backgroundColor[0], backgroundColor[1], backgroundColor[2]));
        mainMenuSouthPanel.setBackground(new Color(backgroundColor[0], backgroundColor[1], backgroundColor[2]));
        */

        //add the buttons and text to their respective panels
        mainMenuNorthPanel.add(mainTitle);
        mainMenuCenterPanel.add(gameLogo);
        mainMenuSouthPanel.add(startGameButton);
        mainMenuSouthPanel.add(aboutGameButton);
        mainMenuSouthPanel.add(instructionsGameButton);

        //add listeners to all of the buttons, and set their sizes
        startGameButton.addActionListener(new ButtonListener());
        aboutGameButton.addActionListener(new ButtonListener());
        instructionsGameButton.addActionListener(new ButtonListener());
        startGameButton.setPreferredSize(new Dimension(mainMenuButtonSizes[0],mainMenuButtonSizes[1]));
        aboutGameButton.setPreferredSize(new Dimension(mainMenuButtonSizes[0],mainMenuButtonSizes[1]));
        instructionsGameButton.setPreferredSize(new Dimension(mainMenuButtonSizes[0],mainMenuButtonSizes[1]));

        this.setVisible(true);
    } //end of StartWindow constructor

    //
    public void StartGame() {
        this.setVisible(false);
        new GameWindow();
    }

    //
    public static void CloseApplication(boolean forceClose) {
        //if the application is being forceClosed, close it
        if (forceClose) {
            System.out.println("Closing application...");
            System.exit(0);
        }

        //otherwise ask the user if they want to close the application
        System.out.println("Close application process called...");
        int closeApp = JOptionPane.showConfirmDialog(null, "Are you sure you want to close the application?",
                "Close Application", JOptionPane.YES_NO_OPTION);

        if (closeApp == 0) {
            System.out.println("Closing application...");
            System.exit(0);
        }
    }
}

//
class GameWindow extends JFrame{
    //initialize the necessary variables
    public static int width = 350;
    public static int height = 465;
    private final int ANIMATION_REFRESH_RATE = 100;
    private static int dropSpeed = 1000;
    private static boolean gameStart = false;
    private static boolean gamePaused = false;
    private PaintSurface canvas;
    private static Tetrimino current;
    private static Tetrimino[] nextTetrimino = new Tetrimino[4];
    private static Timer t2;

    public GameWindow() {
        //set the dimensions, title, closing operation, and center it in the screen
        this.setSize(width, height);
        this.setTitle("Callum's Tetris");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        //create a new tetrimino to start the game
        newTetrimino();

        //make the frame (window) visible
        this.setVisible(true);
    } //end of GameWindow constructor method

    //
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

        //if the game has not started, created the timer for dropping the tetrimino
        if (!gameStart) {
            t2 = new Timer(dropSpeed, new ActionListener(){
                @Override
                public void actionPerformed (ActionEvent evt)
                {
                    Board.tetriminoDrop(current, false);
                }
            });
            t2.start();
            gameStart = true;
        }
    }

    //increases the drop speed of the tetriminoes
    public static void levelUp() {
        if (Board.getLevel() < 4) {
            Board.setLevel(Board.getLevel() + 1);
            dropSpeed -= 200;
            t2.setDelay(dropSpeed);

            System.out.println("Level up - level " + Board.getLevel());
        } else {
            System.out.println("Max level reached.");
        }

    }

    //
    public static void rotateTetrimino(int rot) { Board.tetriminoRotate(current, rot); }

    //
    public static void moveTetrimino(int dir) { Board.tetriminoMove(current, dir); }

    //
    public static void setSoftDropTetrimino(boolean drop) {
        if (drop) {
            t2.setDelay(100);
            resetDropTimer();
        } else {
            t2.setDelay(dropSpeed);
        }
    }

    //
    public static void instantDropTetrimino() { Board.tetriminoInstantDrop(current); resetDropTimer(); }

    //
    private static void resetDropTimer() { t2.stop(); Board.tetriminoDrop(current, false); t2.start(); }

    //
    public static void pauseGame(boolean pause) {
        if (pause) {
            gamePaused = true;
            t2.stop();
        } else {
            gamePaused = false;
            t2.start();
        }
    }

    //
    public static Tetrimino[] getNextTetrimino() { return nextTetrimino; }

    //
    public static boolean getGamePaused() { return gamePaused; }

    //
    public String toString() {
        String report = "";

        report += "-------------------------\n";
        report += "GameWindow Object\n";
        report += "Width: " + WIDTH + "\n";
        report += "Height: " + HEIGHT + "\n";

        return report;
    } //end of the toString method

} //end of GameWindow class

class PaintSurface extends JComponent {
    //define the necessary variables
    private Image gridBackground; //note: border length is 4 pixels
    private Image holdBackground;
    private Image nextBackground;
    private Image block;
    private final int backgroundOffsetX = 5;
    private final int backgroundOffsetY = 5;
    private static final int gridBackgroundWidth = 210;
    private int blockPosX = backgroundOffsetX + 4;
    private int blockPosY = backgroundOffsetY + 5;
    private Board tetrisBoard = new Board();

    //method for repainting the canvas
    public void paint(Graphics g) {
        //create the images, and then draw them (and the score factors)
        gridBackground = new ImageIcon("assets\\Tetris_Grid_Background.png").getImage();
        nextBackground = new ImageIcon("assets\\Tetris_Grid_Next_Alt.png").getImage();
        holdBackground = new ImageIcon("assets\\Tetris_Grid_Hold.png").getImage();
        g.drawImage(gridBackground, backgroundOffsetX, backgroundOffsetY, null);
        g.drawImage(nextBackground, backgroundOffsetX + gridBackgroundWidth + 10, backgroundOffsetY, null);
        g.drawString("SCORE: " + Board.getScore(), blockPosX*2 + 20*Board.COLUMNS,  20*Board.ROWS - 35);
        g.drawString("LEVEL: " + Board.getLevel(), blockPosX*2 + 20*Board.COLUMNS,  20*Board.ROWS - 15);
        g.drawString("LINES CLEARED: " + Board.getRowsCleared(), blockPosX*2 + 20*Board.COLUMNS, 20*Board.ROWS + 5);

        if (GameWindow.getGamePaused()) {
            g.drawString("GAME PAUSED", blockPosX*2 + 20*Board.COLUMNS,  20*Board.ROWS - 65);
        }

        //draw the board (check for any values in the board, and draw the correct block for it - in the correct position)
        for (int x = 0; x < Board.ROWS; x++) {
            for (int y = 0; y < Board.COLUMNS; y++) {
                switch (tetrisBoard.getBoardArray()[x][y]) {
                    case 0:
                        //no block, do nothing
                        break;
                    case 1:
                        block = Board.blockBlue.getImage();
                        g.drawImage(block, blockPosX + y*20, blockPosY + x*20, null);
                        break;
                    case 2:
                        block = Board.blockOrange.getImage();
                        g.drawImage(block, blockPosX + y*20, blockPosY + x*20, null);
                        break;
                    case 3:
                        block = Board.blockYellow.getImage();
                        g.drawImage(block, blockPosX + y*20, blockPosY + x*20, null);
                        break;
                    case 4:
                        block = Board.blockGreen.getImage();
                        g.drawImage(block, blockPosX + y*20, blockPosY + x*20, null);
                        break;
                    case 5:
                        block = Board.blockPink.getImage();
                        g.drawImage(block, blockPosX + y*20, blockPosY + x*20, null);
                        break;
                    case 6:
                        block = Board.blockPurple.getImage();
                        g.drawImage(block, blockPosX + y*20, blockPosY + x*20, null);
                        break;
                    case 7:
                        block = Board.blockLightBlue.getImage();
                        g.drawImage(block, blockPosX + y*20, blockPosY + x*20, null);
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
                Outer: for (int y = 0; y < GameWindow.getNextTetrimino()[t].getSize(); y++) {
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

    } //end of paint method

    //
    public String toString() {
        String report = "";

        report += "-------------------------\n";
        report += "PaintSurface Object\n";
        report += "Width: " + WIDTH + "\n";
        report += "Height: " + HEIGHT + "\n";

        return report;
    } //end of the toString method

} //end of PaintSurface class

//handles key input
class KeyListener implements java.awt.event.KeyListener {
    private static boolean releaseKeyShift = false;
    private static boolean releaseKeySpace = false;
    private static boolean releaseKeyLeft = false;
    private static boolean releaseKeyRight = false;
    private static boolean releaseKeyUp = false;
    private static boolean releaseKeyDown = false;

    public void keyTyped(KeyEvent e) { }

    public void keyPressed(KeyEvent e ) {
        if (e.getKeyCode() == 32 && !releaseKeySpace) { //check for the space bar, and if it's being pressed again
            System.out.println("Instant dropping tetrimino...");
            GameWindow.instantDropTetrimino();
            releaseKeySpace = true;
        } else
        if (e.getKeyCode() == 37 && !releaseKeyLeft) { //check for the left arrow key, and if it's being pressed again
            System.out.println("Moving the tetrimino left...");
            GameWindow.moveTetrimino(Board.MOVE_LEFT);
            releaseKeyLeft = true;
        } else if (e.getKeyCode() == 38 && !releaseKeyUp) { //check for the up arrow key, and if it's being pressed again
            System.out.println("Rotating the tetrimino left...");
            GameWindow.rotateTetrimino(Tetrimino.ROTATE_LEFT);
            releaseKeyUp = true;
        } else if (e.getKeyCode() == 39 && !releaseKeyRight) { //check for the right arrow key, and if it's being pressed again
            System.out.println("Moving the tetrimino right...");
            GameWindow.moveTetrimino(Board.MOVE_RIGHT);
            releaseKeyRight = true;
        } else if (e.getKeyCode() == 40 && !releaseKeyDown) { //check for the down arrow key, and if it's being pressed again
            System.out.println("Rotating the tetrimino right...");
            GameWindow.rotateTetrimino(Tetrimino.ROTATE_RIGHT);
            releaseKeyDown = true;
        } else if (e.getKeyCode() == 16 && !releaseKeyShift) { //check for the shift key, and if it's being pressed again
            GameWindow.setSoftDropTetrimino(true);
            releaseKeyShift = true;
        } else if (e.getKeyCode() == 27) { //check for the escape key, then quit if pressed
            if (GameWindow.getGamePaused()) {
                GameWindow.pauseGame(false);
            } else {
                GameWindow.pauseGame(true);
            }
        }
        System.out.println("Key Pressed: " + e.getKeyCode());
    }

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
        } else if (e.getKeyCode() == 16) { //check for the shift key
            GameWindow.setSoftDropTetrimino(false);
            releaseKeyShift = false;
        }
        System.out.println("Key Released: " + e.getKeyCode());
    }
} //end of KeyListener class

//a listener for when the user interacts with the frame (window)
class WindowListener implements java.awt.event.WindowListener {

    @Override
    public void windowActivated(WindowEvent e) { }

    @Override
    public void windowClosed(WindowEvent e) { }

    @Override
    public void windowClosing(WindowEvent e) {
        StartWindow.CloseApplication(false);
    }

    @Override
    public void windowDeactivated(WindowEvent e) { }

    @Override
    public void windowDeiconified(WindowEvent e) { }

    @Override
    public void windowIconified(WindowEvent e) { }

    @Override
    public void windowOpened(WindowEvent e) { }
}

//a listener for when the user interacts with one of the buttons in the GUI
class ButtonListener implements java.awt.event.ActionListener {

    //interaction with the buttons "Start", "Help", and "Exit"
    @Override
    public void actionPerformed(ActionEvent e) {

        //check which button has been clicked of the main menu buttons
        if (e.getActionCommand().equals("Start Game")) {
            System.out.println("Start button clicked...");
            Tetris.startMenu.StartGame();
        } else if (e.getActionCommand().equals("About Game")) {
            System.out.println("About button clicked...");
            //TODO
        } else if (e.getActionCommand().equals("Game Instructions")) {
            System.out.println("Instructions button clicked...");
            //TODO
        }
    }
}