Authors:
Andrew Elenbogen
Aidan Carroll
Richard Lin
Alex Calamaro

Instructions:

To evolve a population of AI:
1)open CoevoThreaded.java
2)Select desirable parameters from amongst the final variables in the CoevoThreaded class (not recommended to exceed popsize of 128 on most machines)
3)Run CoevoThreaded and wait for the "Done" printout.

This process will evolve the populations specified by the parameters.  
At the end of the program, the two best AI's out of the final population are selected and written to a file called evoOutput.txt.
The UI based portion of the program can then either play these individuals against each other, or play the better of the two (the top one) against a human player.

To see the AI perform:
Once you have completed evolution via CoevoThreaded or manually plugged in two AI's in the form:

x x x x x
x x x x x

...to evoOutput.txt (where x is an double), you may run Quoridor.java.

1) Double click the desired matchup (Player vs AI, AI vs AI or Quit)
2) Play the Game, or enjoy watching AI fight each other. Note, rapidly double-clicking may repeat your move (and more importantly, grant extra moves to your opponent), so please click carefully.

To make a move:
1) Select move, horizontal wall or vertical wall in the left side panel
2a) If you selected move, select the legal square to which you want to move
2b) If you selected wall, select the locus in which to place the wall. As the locuses are rather small, again, please click slowly and carefully to ensure your move is registered correctly.

Notes:
There is no "Victory!" screen, and even after one player wins, the other player visually gets to make an extra move, so watch carefully for which player actually wins.
To adjust the speed of a game, in Quoridor.java, line 52, there is a thread.sleep() call. Decreasing that value will speed up the game, and vice versa.

Credits:
All code was original, and written exclusively by the authors listed above.