import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import java.util.concurrent.TimeUnit;

public class CLI {

	private Scanner scanner;
	private Puzzle puzzle;

	public CLI() {
		this.scanner = new Scanner(System.in);
		start(scanner);
		scanner.close();
	}

	/**
	 * 
	 * @param sc A scanner that will take input from system.in
	 */
	public void start(Scanner sc) {
		boolean isRun = true;
		intro();
		while (isRun) {
			System.out.print("> ");
			String command = sc.nextLine().toLowerCase().trim();
			parser(command);
			System.out.flush();
		}
	}

	/**
	 * 
	 * @param command The command from the user that will be parsed.
	 * @postcondition The users command is parsed and executed.
	 */
	private void parser(String command) {
		String[] args = command.split(" ");
		switch (args[0]) {
		case "exit":
			System.exit(0);
		case "":
			System.out.println("Please enter a command");
			break;
		case "show":
			puzzle.showPuzzle();
			break;
		case "save":
			if(puzzle != null)
			{
			save();
			System.out.println("Puzzle Saved!");
			break;
			}
			System.out.println("No Puzzle to Save");
			break;
		case "guess":
			if(args[1].isBlank() || args[1].length() < 4)
			{
				System.out.println("Guess is too short!");
				break;
			}
			puzzle.addGuess(args[1]);
			break;
		case "shuffle":
			puzzle.shuffleLetters();
			break;
		case "rules":
			rules();
			break;
		case "load":
			load(getdefaultPath());
			break;
		case "new":
			newPuzzle();
			break;
		default:
			System.out.println(command + ": Unknown Command");
		}

	}

	/**
	 * A default path if the user doesn't pass a custom path.
	 * 
	 * @return The default path to user.home.
	 */
	private Path getdefaultPath() {
		String home = System.getProperty("user.home");
		return Paths.get(home).resolve("puzzle.json");
	}

	/**
	 * @precondtion If the user has no puzzle generated a new puzzle is generated.
	 *              If the user has already generated a puzzle a new puzzle
	 *              overwrites the current one.
	 * 
	 *              A command that invokes the new puzzle constroctor and sets it as
	 *              our current puzzle.
	 * 
	 * @poscondition We have generated a new puzzle for the user to solve.
	 */
	void newPuzzle() {
		System.out.println("Generating New Puzzle...");
		time();
		this.puzzle = new Puzzle();
		System.out.println("\nNew Puzzle Generated!");
	}
	
	void time() {
		for (int i = 0; i < 100; ++i) {
			try {
				TimeUnit.MILLISECONDS.sleep(1);
				System.out.print("\u001b[1000D");
				System.out.flush();
				TimeUnit.MILLISECONDS.sleep(1);
				System.out.print((i + 1) + "%");
				System.out.flush();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @precondtion The user has a puzzle currently generated. Saves the users
	 *              "puzzle" as a jSON file in the users home directory.
	 * @postcondition The users puzzle is saved in the home directory.
	 */
	void save() {
		save(getdefaultPath());
	}

	/**
	 * @precondtion The user has a puzzle to save in the first place.
	 * 
	 * @param path The path we want to save the puzzle to.
	 * 
	 *             Saves the users current puzzle to a path if the path is valid.
	 * 
	 * @postcondition The users puzzle is saved to the given path.
	 */
	private void save(Path path) {
		
		JSONArray ja = new JSONArray();
		ja.add(puzzle.toJsonObject());
		String jsonText = ja.toJSONString();
		try {
			Files.write(path, jsonText.getBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {

			throw new RuntimeException(e);
		}

	}

	/**
	 * 
	 * @param path Loads the saved puzzle from a JSON file from the given path.
	 */
	private void load(Path path) {
		String jsonText = null;
		JsonArray ja = null;

		try {
			jsonText = new String(Files.readAllBytes(path));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try {
			ja = (JsonArray) Jsoner.deserialize(jsonText);
		} catch (JsonException e) {
			throw new RuntimeException(e);
		}
		JSONParser parser = new JSONParser();
		for (Object object : ja) {
			JsonObject jo = (JsonObject) object;
			Puzzle newPuzzle = Puzzle.fromJsonObject(jo);
			puzzle = newPuzzle;
		}

	}

	private void intro() {
		 
		System.out.println("\u001b[31;1m");
		System.out.println("\t ▄█     █▄   ▄██████▄     ▄████████ ████████▄        ▄█     █▄   ▄█   ▄███████▄     ▄████████    ▄████████ ████████▄     ▄████████ \n"
				+ "\t███     ███ ███    ███   ███    ███ ███   ▀███      ███     ███ ███  ██▀     ▄██   ███    ███   ███    ███ ███   ▀███   ███    ███ \n"
				+ "\t███     ███ ███    ███   ███    ███ ███    ███      ███     ███ ███▌       ▄███▀   ███    ███   ███    ███ ███    ███   ███    █▀  \n"
				+ "\t███     ███ ███    ███  ▄███▄▄▄▄██▀ ███    ███      ███     ███ ███▌  ▀█▀▄███▀▄▄   ███    ███  ▄███▄▄▄▄██▀ ███    ███   ███        \n"
				+ "\t███     ███ ███    ███ ▀▀███▀▀▀▀▀   ███    ███      ███     ███ ███▌   ▄███▀   ▀ ▀███████████ ▀▀███▀▀▀▀▀   ███    ███ ▀███████████ \n"
				+ "\t███     ███ ███    ███ ▀███████████ ███    ███      ███     ███ ███  ▄███▀         ███    ███ ▀███████████ ███    ███          ███ \n"
				+ "\t███ ▄█▄ ███ ███    ███   ███    ███ ███   ▄███      ███ ▄█▄ ███ ███  ███▄     ▄█   ███    ███   ███    ███ ███   ▄███    ▄█    ███ \n"
				+ " \t▀███▀███▀   ▀██████▀    ███    ███ ████████▀        ▀███▀███▀  █▀    ▀████████▀   ███    █▀    ███    ███ ████████▀   ▄████████▀  \n"
				+ "\t                         ███    ███                                                             ███    ███                         ");
		System.out.println("\n\t\t\tWelcome \u001b[24m To Word Wizards! To begin playing enter a command below!\n\n\n");
		System.out.println("\t\t\t \u001b[4m New \u001b[24m \254 Creates a New Puzzle\n\n");
		System.out.println("\t\t\t \u001b[4m Save \u001b[24m \254 Saves Your Current Puzzle to Your Home Directory\n\n");
		System.out.println("\t\t\t \u001b[4m Load \u001b[24m \254 Loads a Puzzle From Your Home Directory\n\n");
		System.out.println("\t\t\t \u001b[4m Commands \u001b[24m \254 Display Additional Commands \n\n");
		System.out.println("\t\t\t \u001b[4m Exit \u001b[24m \254 Exits the Program\n\n");
		System.out.flush();
	}
	
	public void commands() {
		System.out.println("\t\t\t\t ");
	}

	private void rules() {
		System.out.println("");
	}

}