Debuggin Log for Tetris.java


-------Problems and Solutions-------
I encountered numerous errors when developing this game, mostly due to logical errors within my code. Here are a few:

1) The Board.java class will run a seemingly impossible to reach statement that reaches an out of bounds array location in the game board. The "tetriminoDropCheck" method 
   checks if the block is near the edge, passes it, and then an exception was reached in the "tetriminoDrop" method.
Solution: Upon further investingating, I remembered that the shape arrays for the Tetrimino class often have a blank section at the bottom (to allow rotation). If a block
	  is above that blank section, the "tetriminoDropCheck" will allow it to drop (as it is still valid), but the "tetriminoDrop" method will try to update the array
	  entirely, including the "out of bounds" blank section. As such, modifying the if statement in the "tetriminoDrop" method easily prevents this from occurring.

1.5) The Board.java class will run a seemingly impossible to reach statement that reaches an out of bounds array location in the game board, again. However, it is now the
     "tetriminoDropCheck" method that throws an exception (despite the system having a check to prevent this).
Solution: The check being run to prevent this error was only checking if the bottom of a tetrimino was out of bounds, and if there was a block that would pass that boundary. 
	  However, tetriminoes may not occur the lower array(s) of their shape, which the method did not account for. To fix the check, it now finds the lowest block in the
	  tetrimino before comparing that position to see if it would fall out of bounds.

2) The Board.java class will attempt to search beneath a block for any conflicts (collisions) in the "tetriminoDropCheck" method. However, upon running the method, it appears
   to ignore blocks that clearly would be considered a "collision", and the falling tetrimino fazes through it.
Solution: Upon reviewing my code, I realized that the "tetriminoDropCheck" method was not viewing the correct row, but also may view itself as a collision. As such, I reworked
	  the "tetriminoDrop" method to erase the existing block, before performing the check, and then recreating it (either one down, if valid, or in the same place, if not
	  valid)

3) The setSoftDropTetrimino method (within the GameWindow class, in the Tetris.java file) SHOULD speed up the drop speed of the Tetrimino. However, the method will stop the
   movement of the Tetrimino for a short period of time before speeding up the Tetrimino
Solution: The system will wait until the timer has reached it's next tick before changing the tick speed (it will not reset upon having the delay time changed), causing the
	  delay noticed. As such, upon review the Java 8 API for Timer class (javax.swing.Timer specifically), I found that the Timer object has an initial delay before it 
	  begins. By setting this delay to 0ms, it removed the delay in time.

4) Upon a Game Over, the user is asked to enter a name in. If the user closes the JOptionPane, the window will not reopen and an error will occur (visible in the console), as
   no response was taken.
Solution: JOptionPane.showInputDialog("") should return a String, but if the user closes the dialog, it returns null. By checking if the String variable it assigns to, I can
	  check if the user entered anything (or just closed the dialog). As such, I can loop it until the user actually returns a String (before checking the String itself
	  to see if it's valid).


-------Test Cases-------
Input:	  | A tetrimino reaches falls to the end of the board 	        | A tetrimino reaches falls to the end of the board 		   | A tetrimino falls on top of a block
Expected: | The tetrimino will stop falling		      	        | The tetrimino will stop falling				   | The tetrimino stops falling
Output:	  | An array out of bounds error is thrown from tetriminoDrop() | An array out of bounds error is thrown from tetriminoDropCheck() | The tetrimino falls through the block (no collision)
Solution: | See above # 1						| See above # 1.5						   | See above # 2

Input:	  | The user presses the return key to speed the falling of the Tetrimino  | The user closes the JOptionPane asking for a name |
Expected: | The Tetrimino will fall at a faster pace 				   | The window will reopen			       |
Output:   | The Tetrimino stops moving for ~1-2sec before falling at a faster rate | A null pointer error is thrown from gameOver()    |
Solution: | See above # 3							   | See above # 4				       |


-------Arrays of Objects Usage-------
The use of arrays of objects within my program:
In my program, Tetris, an array of Tetrimino objects is used to keep track of the upcoming Tetrimino blocks that will fall. There are four Tetrimino objects within the array,
and they are pulled out consecutively as new blocks spawn (with all objects moving forward in the array (towards index 0) when the oldest Tetrimino object is removed, and 
a new Tetrimino object is created and filled into the end of the array. This way, the program can tell the user which blocks will arrive next, and use the objects to display them
before later entering them into the board. 