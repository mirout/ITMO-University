import java.io.*;

import java.lang.StringBuilder;

import java.util.*;

import java.nio.charset.StandardCharsets;

class WordStatIndexChecker implements Checker {
    public boolean isWordCharacter(int c) {
        return Character.isLetter(c)
                || Character.getType(c) == Character.DASH_PUNCTUATION
                || c == '\'';
    }
}

public class WordStatCountLineIndex {

    public static void main(String[] args) {

        Map<String, PairOfIntList> wordStat = new HashMap<>();

        int indexOfWordInColumn = 0;
        int lastNumOfLine = 0;

        try {

            FastScanner in = new FastScanner(new FileReader(args[0], StandardCharsets.UTF_8), new WordStatIndexChecker());

            try {

                while (in.hasNext()) {

                    String word = in.next().toLowerCase();

                    if (in.getNumOfCurrentLine() != lastNumOfLine) {
                        lastNumOfLine = in.getNumOfCurrentLine();
                        indexOfWordInColumn = 0;
                    }
                    indexOfWordInColumn += 1;

                    wordStat.computeIfAbsent(word, k -> new PairOfIntList()).add(lastNumOfLine + 1, indexOfWordInColumn);
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            System.out.println("Invalid input " + e.getMessage());
        }

        try {

            Writer writer = new BufferedWriter(new FileWriter(args[1], StandardCharsets.UTF_8));

            try {
                List<Map.Entry<String, PairOfIntList>> ans = new ArrayList<>(wordStat.entrySet());
                ans.sort(Map.Entry.comparingByValue());
                for (var elem : ans) {
                    writer.write(String.format("%s %d %s%n", elem.getKey(), elem.getValue().size(), elem.getValue().toString()));
                }
            } finally {
                writer.close();
            }

        } catch (IOException e) {
            System.out.println("Invalid input " + e.getMessage());
        }

    }
}