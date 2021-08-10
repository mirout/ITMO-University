package md2html;

import java.io.IOException;
import java.util.*;

public class Md2HtmlParser {

    private final FileSource fs;
    private String paragraphNow;
    private int index = 0;
    private static final Map<String, String> TAGS = new LinkedHashMap<>() {{
        put("**", "strong");
        put("*", "em");
        put("__", "strong");
        put("_", "em");
        put("--", "s");
        put("`", "code");
        put("++", "u");
    }};

    private final Map<Character, String> specialSymbols = Map.of(
            '&', "&amp;",
            '>', "&gt;",
            '<', "&lt;"
    );

    private final Set<String> isOldSequence = new HashSet<>();
    private String savedTag = "";

    public Md2HtmlParser(FileSource fs) {
        this.fs = fs;
    }

    public String parse() throws IOException, Md2HtmlParserException {
        StringBuilder result = new StringBuilder();

        while (fs.hasNextParagraph()) {
            paragraphNow = fs.nextParagraph();
            result.append(parseParagraph()).append('\n');
        }

        return result.toString();
    }

    private String parseText() throws Md2HtmlParserException {

        StringBuilder result = new StringBuilder();

        while (index < paragraphNow.length()) {
            if (specialSymbols.containsKey(paragraphNow.charAt(index))) {
                result.append(specialSymbols.get(paragraphNow.charAt(index)));
            } else if (index > 0 && paragraphNow.charAt(index - 1) == '\\') {
                result.append(convertEscapeCharacters());
            } else if (paragraphNow.charAt(index) == '\\') {
                if (index + 1 >= paragraphNow.length()) {
                    result.append('\\');
                }
            } else if (checkImage()) {
                result.append(convertImage());
            } else if (checkCloseSequence()) {
                break;
            } else if (isMarkupSequence()) {
                result.append(convertMarkup());
            } else {
                result.append(paragraphNow.charAt(index));
            }
            index++;
        }

        return result.toString();
    }

    private boolean checkImage() {
        return index + 1 < paragraphNow.length() && paragraphNow.charAt(index) == '!' && paragraphNow.charAt(index + 1) == '[';
    }

    private void checkException(char ch, int pos) throws Md2HtmlParserException {
        if (pos >= paragraphNow.length() || paragraphNow.charAt(pos) != ch) {
            throw new Md2HtmlParserException(String.format("Expected '%c', but found end of paragraph", ch));
        }
    }

    private String convertImage() throws Md2HtmlParserException {
        index += 2;

        isOldSequence.add("]");
        String name = parseWithoutMarkup("]");
        isOldSequence.remove("]");

        checkException(']', index);
        checkException('(', index + 1);

        index += 2;

        isOldSequence.add(")");
        String source = parseWithoutMarkup(")");
        isOldSequence.remove(")");

        checkException(')', index);

        return String.format("<img alt='%s' src='%s'>", name, source);
}

    private String parseWithoutMarkup(String end) {
        StringBuilder result = new StringBuilder();
        while (index < paragraphNow.length() && !Character.toString(paragraphNow.charAt(index)).equals(end)) {
            result.append(paragraphNow.charAt(index));
            index++;
        }
        return result.toString();
    }

    private String convertEscapeCharacters() {
        char currentChar = paragraphNow.charAt(index);

        if (currentChar == '\\' || currentChar == '`' || currentChar == '_' || currentChar == '*') {
            return Character.toString(currentChar);
        } else {
            return String.format("\\%c", currentChar);
        }
    }

    private boolean checkCloseSequence() {
        for (var close : isOldSequence) {
            if (paragraphNow.startsWith(close, index) && (
                            index + 1 == paragraphNow.length() ||
                            index + 1 < paragraphNow.length() &&
                            !Character.toString(paragraphNow.charAt(index + 1)).equals(close))) {
                return true;
            }
        }

        return false;
    }

    private String convertMarkup() throws Md2HtmlParserException {

        StringBuilder result = new StringBuilder();
        String sq = savedTag;

        index += savedTag.length();

        String tag = TAGS.get(sq);

        isOldSequence.add(sq);
        String parsedText = parseText();
        isOldSequence.remove(sq);

        if (index + sq.length() <= paragraphNow.length() && paragraphNow.startsWith(sq, index)) {
            result.append("<").append(tag).append(">");
            result.append(parsedText);
            result.append("</").append(tag).append(">");
            index += sq.length() - 1;
        } else {
            result.append(sq);
            result.append(parsedText);
            index--;
        }

        return result.toString();
    }

    private boolean isMarkupSequence() {
        for (String sequence : TAGS.keySet()) {
            if (paragraphNow.startsWith(sequence, index)){
                savedTag = sequence;
                return true;
            }
        }
        return false;
    }

    private String parseParagraph() throws Md2HtmlParserException {
        StringBuilder result = new StringBuilder();
        index = 0;

        int countHashes = readHashes();

        if (countHashes == 0 || countHashes > 6) {
            index = 0;
            result.append("<p>");
            result.append(parseText());
            result.append("</p>");
        } else {
            result.append(String.format("<h%d>", countHashes));
            result.append(parseText());
            result.append(String.format("</h%d>", countHashes));
        }

        return result.toString();
    }

    private int readHashes() {
        while (index < paragraphNow.length() && paragraphNow.charAt(index) == '#') {
            index++;
        }

        return Character.isWhitespace(paragraphNow.charAt(index)) ? index++ : 0;
    }
}
