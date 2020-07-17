# Tetris

## Introduction / Project Proposal

This project is a re-creation of [Tetris](https://en.wikipedia.org/wiki/Tetris). The main action of the game
happens on a board with 20 rows and 10 columns. The goal of the game is to earn as many points as possible by 
manipulating falling *tetrominos* (a.k.a. *pieces*) to fill the rows of the board. When all the cells of a row are 
filled with tetromino tiles, the tiles in that row are deleted—this is called a *line clear*. More points are earned 
when the player clears more lines at a time. The game ends if the player allows a piece to be placed at the top 
of the board such that there is no space for the piece to fit.

This classic singleplayer game can be played by **anyone**. Tetris is still popular to this day—see [the intensive
finals of the 2018 Classic Tetris World Championship](https://www.youtube.com/watch?v=L_UPHsGR6fM)). I decided to
re-create Tetris as a personal project because I have played some Tetris myself and am amazed at how such a simple 
game can be so fun to play.

## User Stories

- As a user, I want to be able to add an entry (containing my score, name, and level reached) to a scoreboard after
the game is over.
- As a user, I want to be able to move pieces horizontally.
- As a user, I want to be able to rotate pieces.
- As a user, I want to be able to clear line(s) by completely filling a row with the tiles of tetrominos.
- As a user, I want to be able to see my current in-game score.
- As a user, I want to be able to see my current in-game level.
- As a user, I want to be able to see the number of lines I have cleared so far.
- As a user, I want to be able to see the next piece that will come after I place the current piece.
- As a user, I want to be able to replay the game after it ends.