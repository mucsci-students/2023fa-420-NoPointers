# Word Wizards


Word Wizards - Spelling Bee Game

Authors: No Pointers

This program is an adaptation of the NYT Spelling Bee game. Game can be run from either the command-line or with a GUI interface. Our team utilized SQLite databases to assist in the formation of puzzles and player guesses. Saving a puzzle will save it as a JSON file (puzzle.json) onto the player’s computer.
To play: Players will interact with the game by typing in the following commands in the command line.

-   new – creates a new puzzle for the player.
-   custom - (followed by an input of 7 unique letters) – creates a new puzzle using the letters inputted by the player.
-   show – displays the letters of the puzzle to the player and a list of the words they have correctly guessed.
-   shuffle – rearranges the letters of the current puzzle.
-   save - saves the current puzzle the player is using so that it can be loaded and used later.
-   load – loads the saved puzzle and makes it available for play.
-   rules – explains the rules of the game to the player.
-   help – displays additional commands to the player.
-   exit – exits the program.



## Deployment

To deploy this project navigate to the project using a command-line interface navigate to the directory where  **gradle.build** is contained.

### Running using GUI ###
To run with a GUI in your command-line type the following commands depending on your platform.

Linux :penguin:
* ```gradle build ```
* ```gradle run ```

Windows
* ```.\gradlew build ```
* ```.\gradlew run ```

**Linux Lab** :penguin:

If this is being run on the linux lab you might need to specify a jdk that is at at least above jdk 17 or else you might get an error. To do this run with the following commands .

```
gradle build -Dorg.gradle.java.home=/usr/lib/jvm/java-20-openjdk
```
```gradle run -Dorg.gradle.java.home=/usr/lib/jvm/java-20-openjdk```

### Running using CLI ###
To run the CLI version of Word Wizards use the following commands.

Linux :penguin:
* ```gradle build ```
* ```gradle -q --console plain run --args='cli' ```

Windows
* ```.\gradlew build ```
* ```.\gradlew -q --console plain run --args='cli' ```

**Linux Lab** :penguin:

```gradle build -Dorg.gradle.java.home=/usr/lib/jvm/java-20-openjdk```


```gradle run -Dorg.gradle.java.home=/usr/lib/jvm/java-20-openjdk -q --args='cli' ```

This will look like it takes a while to run but it ensures that gradles output does not mess with the CLI output you will not see gradles progress bar.

Design Patterns Used :
1. Memento - the fields of the puzzle that are saved are stored within a Memento class nested in the Puzzle class. 
The GameState class saves the Memento to a JSON file and loads that saved Memento to load a puzzle.
2. Command - Clicking the New button in the GUI uses a NewPuzzleCommand to called on by the Controller to create a new Puzzle.
3. Builder - A new GameState is created via GameStateBuilder nested within the GameState class. 
4. Singleton - The Database class is implemented as a Singleton and used to access the sqlite database.
5. State - The GameState has two States, FreshState and Completed State. When a user successfully guesses all 
the valid words in a puzzle, the State will change from FreshState to CompletedState. Afterwards a new message 
will be shown to the user whenever they try further guesses.
6. Factory - A Factory for Commands has been implemented via the CommandFactory class. When the controller 
wants to create a NewPuzzleCommand, it will call on the CommandFactory's getCommand method, which returns the 
desired Command.
