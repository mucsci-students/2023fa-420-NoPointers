# 2023fa-420-NoPointers

Word Wizards - Spelling Bee Game
Authors: No Pointers

This program is an adaptation of the NYT Spelling Bee game. This is a command line game where players will input commands from a given list to play. Our team utilized SQLite databases to assist in the formation of puzzles and player guesses. Saving a puzzle will save it as a JSON file (puzzle.json) onto the player’s computer.
To play: Players will interact with the game by typing in the following commands in the command line.

- new – creates a new puzzle for the player.
-   custom (followed by an input of 7 unique letters) – creates a new puzzle using the letters inputted by the player.
-   show – displays the letters of the puzzle to the player and a list of the words they have correctly guessed.
-   shuffle – rearranges the letters of the current puzzle.
-   save - saves the current puzzle the player is using so that it can be loaded and used later.
-   load – loads the saved puzzle and makes it available for play. 
-   rules – explains the rules of the game to the player.
-   help – displays additional commands to the player.
-   exit – exits the program.
-   Rank - Displays your current score and rank in the puzzle.
USAGE
In order to run this game inside of the source dirctory run the "Game.jar" file with java -jar .\Game.jar
