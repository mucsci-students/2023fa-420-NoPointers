package org.nopointers;

// Command Lines to call on classes. Puzzle class, GameState class, Dictionary class.
public class Game {
    public static void main(String[] args) {

        String classpath = System.getProperty("java.class.path");
        System.out.println(classpath);
        System.out.println(1);
        CLI c = new CLI();
        System.out.println("\033[49m");
    }
}