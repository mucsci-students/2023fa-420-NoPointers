import java.util.Scanner;
import java.io.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.json.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import netscape.javascript.JSObject;
 
public class CLI {
	
private Scanner scanner;
private Puzzle puzzle;
public CLI() {
	this.scanner = new Scanner(System.in);
	puzzle = new Puzzle();
	save();
	start(scanner);

	scanner.close();
}

public void start(Scanner sc) {
    boolean isRun = true;
    while (isRun) {
    	System.out.print("> ");
        String command = sc.nextLine().toLowerCase().trim();
        parser(command);
        System.out.flush();
    }
}
private void parser(String command) {

    switch (command) {
        case "exit":
            System.exit(0);
        case "":
        	System.out.println("Please enter a command");
        	break;
        case "show":
        	puzzle.showPuzzle();
        	break;
        case "save":
        	save();
        	System.out.println("Puzzle Saved!");
        	break;
        default:
        		System.out.println("Unknown Command");
    }

}

private Path getdefaultPath() {
	String home = System.getProperty("user.home");
	return Paths.get(home).resolve("puzzle.json");
}

void save()
{
	save(getdefaultPath());
}

private void save(Path path)
{
	JSONArray ja = new JSONArray();
	ja.add(puzzle.toJsonObject());
	String jsonText = ja.toJSONString();
	try {
		Files.write(path, jsonText.getBytes(), StandardOpenOption.CREATE);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		throw new RuntimeException(e);
	}
 
}



}
