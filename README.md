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
- You can locate my visual component by starting a Tetris game. The Tetris board with all its tiles is the visual 
component.
- You can save the state of my application by clicking the "Permanently save temporary scoreboard to file" button
in the game-over dialog window.
- You can reload the state of my application by clicking the "View permanently-saved scoreboard entries stored in file"
button in the game-over dialog window.

Note: you can verify that you have successfully added/removed scores from the temporary scoreboard by clicking
the "View unsaved scoreboard entries" button in the game-over dialog window. That button lets you view the temporary 
scoreboard.