# Tetris

## Introduction / Project Proposal

This project is a re-creation of [Tetris](https://en.wikipedia.org/wiki/Tetris). The main action of the game
happens on a board with 20 rows and 10 columns. The goal of the game is to earn as many points as possible by 
manipulating falling *tetrominoes* (a.k.a. *pieces*) to fill the rows of the board. When all the cells of a row are 
filled with tetromino tiles, the tiles in that row are deleted—this is called a *line clear*. More points are earned 
when the player clears more lines at a time. The game ends if the player allows a piece to be placed at the top 
of the board such that there is no space for the piece to fit.

This classic singleplayer game can be played by **anyone**. Tetris is still popular to this day—see [the intensive
finals of the 2018 Classic Tetris World Championship](https://www.youtube.com/watch?v=L_UPHsGR6fM). I decided to
re-create Tetris as a personal project because I have played some Tetris myself and am amazed at how such a simple 
game can be so fun to play.

## User Stories

- As a user, I want to be able to add an entry (containing my score, name, and lines cleared) to a temporary
scoreboard after the game is over.
- As a user, I want to be able to remove entries from the temporary scoreboard.
- As a user, after I add entries to the temporary scoreboard, I want to be able to optionally save the entries to a 
file.
- As a user, I want to be able to see any unsaved entries that I have added to the temporary scoreboard.
- As a user, I want to be able to load in previously-saved scoreboard entries from file so that I can view them.
- As a user, I want to be able to move pieces horizontally.
- As a user, I want to be able to move pieces vertically.
- As a user, I want to be able to immediately drop a piece as far down as it can go.
- As a user, I want to be able to rotate pieces.
- As a user, I want to be able to clear a line by completely filling a row with tetromino tiles. Simultaneously 
filling multiple rows should result in all the filled rows being cleared at once.
- As a user, I want to be able to see my current in-game score.
- As a user, I want to be able to see the number of lines I have cleared so far.
- As a user, I want to be able to see my current in-game level.
- As a user, I want to be able to see the next piece that will come after I place the current piece.
- As a user, I want to be able to replay the game after it ends.

# Instructions for Grader

- When you first open the application, read through the instructions, set the starting level, then click "Start Game!"
- You can generate the first required event by playing the game until the game is over, then clicking
the "Add your score to the temporary scoreboard" button. Enter your name and click "OK". *[Note: once you have
added your score, you cannot add it again. Play another Tetris game (using the "Start a new game" button) to completion
 to add another score.]*
- You can generate the second required event by adding a score (or multiple scores) to the temporary scoreboard 
as described in the above bullet point, then clicking the "Remove scores from the temporary scoreboard" button. Check
the scoreboard entries that you want to remove, then click the "Remove selected entries" button.
- You can trigger my audio component by starting a Tetris game. The Tetris theme song will then play.
- You can save the state of my application by clicking the "Permanently save temporary scoreboard to file" button
in the game-over dialog window.
- You can reload the state of my application by clicking the "View permanently-saved scoreboard entries stored in file"
button in the game-over dialog window.

Note: you can verify that you have successfully added/removed scores from the temporary scoreboard by clicking
the "View unsaved scoreboard entries" button in the game-over dialog window. That button lets you view the temporary 
scoreboard (and is, coincidentally, how the GUI fulfills the "displays the Xs that have been added to the Y" 
requirement).

If the first event (adding a score to the scoreboard) does not suffice, then here's another event you can generate:

- If you have any unsaved scoreboard entries, then the program will prompt you to save the entries if you choose to
exit the program. To try this yourself, add a score to the temporary scoreboard but do not save it. Then exit the
program using the "Quit the program" button (this is the event). The program will handle the button click event by
prompting you to save your unsaved scoreboard entries. On the other hand, if you have no unsaved entries, the prompt 
will not appear.

# Phase 4: Task 2

I use the Map interface in the Piece class in the model.pieces package. The map is central to how each piece rotates.
Each piece has four possible orientations (represented with the integers 0, 1, 2, 3), and each orientation number is
mapped to a tile configuration. Rotation is very simple using the map: simply calculate the next orientation
number, then obtain the next tile configuration from the map.

# Phase 4: Task 3

Problem 1: the Piece class in the model.pieces package also deals with the Tetris board. For example, if a piece
rotates, then the board is updated from within the Piece class. However, the Tetris board itself is stored in the
Game class in the model package. As a result, there is too much coupling between the Piece and Game classes—the
Piece class has to know about how the board is stored in the Game class. The cohesiveness of the Piece class can also
be improved because dealing with the game board should not be a responsibility of the Piece class.

Solution to problem 1: I refactored all board-handling logic in the Piece class to be in the Game class instead.
The Piece class now calls the appropriate methods from the Game class instead of dealing with the board directly.
The Piece class is now more cohesive and has less coupling with the Game class.