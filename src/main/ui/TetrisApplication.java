package ui;

import model.Game;
import model.Scoreboard;
import model.ScoreboardEntry;
import model.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// Represents a Tetris application
public class TetrisApplication {
    private Scanner scanner;
    private Random random;
    private boolean running;
    private Scoreboard scoreboard;
    private Game game;

    // This field is true if the game has ended and the user has added
    // their score to the scoreboard.
    private boolean userAddedScoreToScoreboard;

    // This field is true if the game has just ended (i.e. the user has just topped
    // out and is seeing the game over commands for the first time after the game),
    // false otherwise.
    private boolean gameJustEnded;

    // Code for the initialization of the class, and the reading in and
    // processing of input is based on the example Teller app
    // from https://github.students.cs.ubc.ca/CPSC210/TellerApp

    // EFFECTS: makes and starts a new tetris application
    public TetrisApplication() {
        start();
    }

    // MODIFIES: this
    // EFFECTS: starts the application
    private void start() {
        initFields();

        while (running) {
            if (!gameJustEnded) {
                printBoard();
                if (!game.isGameOver()) {
                    System.out.println("Score: " + game.getScore());
                    System.out.println("Lines cleared: " + game.getLinesCleared());
                    printNextPiece();
                } else {
                    System.out.println("Game over!");
                    gameJustEnded = true;
                    System.out.println("Final score: " + game.getScore());
                    System.out.println("Final lines cleared: " + game.getLinesCleared());
                }
            }
            displayCommands();
            handleUserInput();
        }
    }

    // EFFECTS: initializes all the fields of this object to default values
    private void initFields() {
        scanner = new Scanner(System.in);
        random = new Random();
        running = true;
        scoreboard = new Scoreboard();
        game = new Game();
        game.startNewGame(random.nextInt());
        userAddedScoreToScoreboard = false;
        gameJustEnded = false;
    }

    // EFFECTS: prints a list of commands the user can currently perform
    private void displayCommands() {
        System.out.println("Type one of the following commands:");
        if (game.isGameOver()) {
            System.out.println("replay -> start a new game");
            if (!userAddedScoreToScoreboard) {
                System.out.println("add -> add your score to the scoreboard");
            }
            System.out.println("scoreboard -> see scoreboard");
        } else {
            System.out.println("left -> move piece left");
            System.out.println("right -> move piece right");
            System.out.println("rotate -> rotate piece");
            System.out.println("down -> move piece down");
            System.out.println("n -> do nothing; let game advance on its own");
        }
        System.out.println("q -> quit");
    }

    // EFFECTS: handle user input
    private void handleUserInput() {
        String input = scanner.next();
        scanner.nextLine();
        if (input.equalsIgnoreCase("q")) {
            running = false;
        } else {
            if (game.isGameOver()) {
                handleUserInputGameOver(input);
            } else {
                handleUserInputGameNotOver(input);
            }
        }
    }

    // EFFECTS: prints the tetris board onto the console, with X's marking
    //          tile locations
    private void printBoard() {
        for (int i = 0; i < Game.WIDTH + 2; i++) {
            System.out.print("-");
        }
        System.out.println();
        List<ArrayList<Boolean>> board = game.getBoard();
        for (int r = 0; r < Game.HEIGHT; r++) {
            System.out.print("|");
            for (int c = 0; c < Game.WIDTH; c++) {
                if (board.get(r).get(c)) {
                    System.out.print("X");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println("|");
        }
        for (int i = 0; i < Game.WIDTH + 2; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    // REQUIRES: game is over, and user did not decide to quit
    // EFFECTS: handles given user input
    private void handleUserInputGameOver(String input) {
        if (input.equalsIgnoreCase("replay")) {
            game.startNewGame(random.nextInt());
            userAddedScoreToScoreboard = false;
            gameJustEnded = false;
        } else if (!userAddedScoreToScoreboard && input.equalsIgnoreCase("add")) {
            helpUserAddScore();
            userAddedScoreToScoreboard = true;
        } else if (input.equalsIgnoreCase("scoreboard")) {
            printScoreboard();
        } else {
            System.out.println("Command not recognized.");
        }
    }

    // REQUIRES: game is not over, and user did not decide to quit
    // EFFECTS: handles given user input
    private void handleUserInputGameNotOver(String input) {
        if (input.equalsIgnoreCase("left")) {
            game.getActivePiece().moveLeft();
        } else if (input.equalsIgnoreCase("right")) {
            game.getActivePiece().moveRight();
        } else if (input.equalsIgnoreCase("rotate")) {
            game.getActivePiece().rotate();
        } else if (input.equalsIgnoreCase("down")) {
            game.getActivePiece().moveDown();
        } else if (input.equalsIgnoreCase("n")) {
            game.update();
        } else {
            System.out.println("Command not recognized.");
        }
    }

    // MODIFIES: this
    // EFFECTS: guide the user in adding an entry to the scoreboard
    private void helpUserAddScore() {
        System.out.println("Please enter your name:");
        String name = scanner.nextLine();

        ScoreboardEntry entry = new ScoreboardEntry(game.getScore(), name, game.getLinesCleared());
        scoreboard.add(entry);

        System.out.println("Your score was successfully added to the scoreboard!");
    }

    // EFFECTS: prints all entries on the scoreboard, sorted from greatest to least
    private void printScoreboard() {
        List<ScoreboardEntry> sortedEntries = scoreboard.getSortedEntries();
        if (sortedEntries.size() == 0) {
            System.out.println("Scoreboard is empty!");
        }
        for (int i = 0; i < sortedEntries.size(); i++) {
            ScoreboardEntry entry = sortedEntries.get(i);
            String output = "";
            output += (i + 1) + ". Name: ";
            output += entry.getPlayerName();
            output += "; Score: ";
            output += entry.getScore();
            output += "; Lines cleared: ";
            output += entry.getLinesCleared();
            System.out.println(output);
        }
    }

    // REQUIRES: game is not over
    // EFFECTS: print the name of the next piece
    private void printNextPiece() {
        Piece nextPiece = game.getNextPiece();
        System.out.print("The next piece is a(n) ");
        if (nextPiece instanceof IPiece) {
            System.out.println("I piece.");
        } else if (nextPiece instanceof JPiece) {
            System.out.println("J piece.");
        } else if (nextPiece instanceof LPiece) {
            System.out.println("L piece.");
        } else if (nextPiece instanceof OPiece) {
            System.out.println("O piece.");
        } else if (nextPiece instanceof SPiece) {
            System.out.println("S piece.");
        } else if (nextPiece instanceof TPiece) {
            System.out.println("T piece.");
        } else {
            System.out.println("Z piece.");
        }
    }
}
