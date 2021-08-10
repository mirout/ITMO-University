import java.io.*;

import java.lang.StringBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.Set;

import java.nio.charset.StandardCharsets;

class WordStatChecker implements Checker {
    public boolean isWordCharacter(int c) {
        return Character.isLetter(c)
            || Character.getType(c) == Character.DASH_PUNCTUATION
            || c == '\'';
    }
} 

public class WordStatWordsPrefix {

    private static boolean isPartOfWord(char c) {
        return Character.isLetter(c)
            || Character.getType(c) == Character.DASH_PUNCTUATION
            || c == '\'';
    }

    public static void main(String[] args) {

        Map<String, Integer> wordStat = new HashMap<String, Integer>();

        try {

            FastScanner in = new FastScanner(new BufferedReader(new FileReader(args[0], StandardCharsets.UTF_8)), new WordStatChecker());

            try {

                while (in.hasNext()) {

                    String word = in.next().toLowerCase();
                    if (word.length() >= 3) {
                        word = word.substring(0, 3);
                        if (wordStat.containsKey(word)){
                            wordStat.put(word, wordStat.get(word)+1);
                        } else {
                            wordStat.put(word, 1);
                        }
                    }
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
                String[] ans = wordStat.keySet().toArray(new String[0]);
                Arrays.sort(ans);
                for (String i : ans) {
                    writer.write(String.format("%s %d\n", i, wordStat.get(i)));
                }
            } finally {
                writer.close();
            }

        } catch (IOException e) {
            System.out.println("Invalid input " + e.getMessage());
        }
        
    }
}