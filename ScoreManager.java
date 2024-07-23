import java.nio.file.*;
import java.util.stream.*;
import java.util.*;
import java.io.IOException;


public class ScoreManager { 
    private static final String SCORE_FILE = "scores.txt"; // Define constants for the score file name and maximum number of scores

    private static final int MAX_SCORES = 5;

    public static void addScore(int score) throws IOException {
        List<Integer> scores = readScores(); //Read the current scores
        scores.add(score); //Add the new score
        Collections.sort(scores, Collections.reverseOrder()); //Sort the scores in descending order
        while (scores.size() > MAX_SCORES) {// If there are more than the maximum number of scores, remove the lowest ones

            scores.remove(scores.size() - 1);
        }
        writeScores(scores);  // Write the updated scores back to the file
    }

    public static List<Integer> readScores() throws IOException { //Method to read scores from the file
        Path path = Paths.get(SCORE_FILE); //Get the path to the score file
        if (!Files.exists(path)) { //If the file doesn't exist, return an empty list
            Files.createFile(path); //Create a file if scoreboard doesn't exist
            return new ArrayList<>();
        }
        return Files.lines(path) // Read the lines from the file, parse them as integers, and collect them into a list
            .map(Integer::parseInt)
            .collect(Collectors.toList());
    }

    private static void writeScores(List<Integer> scores) throws IOException {
        String content = scores.stream()  // Convert the list of scores to a string, with each score on a new line
            .map(String::valueOf)
            .collect(Collectors.joining("\n"));
        Files.write(Paths.get(SCORE_FILE), content.getBytes()); //Write the string to the file
    }

    public static void displayScores() throws IOException {
        List<Integer> scores = readScores(); //Read the scores
        System.out.println("Top scores:"); //Print the top score
        for (int i = 0; i < scores.size(); i++) {
            System.out.println((i + 1) + ": " + scores.get(i));
        }
    }
}