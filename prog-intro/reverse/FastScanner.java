import java.io.*;

class FastScanner {

    private final Reader in;
    private final Checker check;

    private String nextWord;

    private int numOfCurrentLine = 0;
    private int symbolNow = 0;

    private boolean isEndScanner = false;
    private boolean hasReadNextWord = false;
    private boolean isStartOfLineSeparator = false;
    private boolean hasSavedChar = false;


    public FastScanner(Reader r, Checker check) {
        this.in = r;
        this.check = check;
    }

    public FastScanner (InputStream in, Checker check){
        this(new BufferedReader(new InputStreamReader(in)), check);
    }

    private boolean readNextWord() throws IOException{

        StringBuilder tok = new StringBuilder();

        if (!readNextSymbol()) return false;

        while (!check.isWordCharacter(symbolNow)) {

            if (symbolNow == '\r') {
                isStartOfLineSeparator = true;
                numOfCurrentLine++;
            } else if (symbolNow == '\n' && !isStartOfLineSeparator) {
                numOfCurrentLine++;
            } else {
                isStartOfLineSeparator = false;
            }

            hasSavedChar = false;
            if (!readNextSymbol()) return false;
        }

        while (check.isWordCharacter(symbolNow)) {
            tok.append((char) symbolNow);
            hasSavedChar = false;
            if (!readNextSymbol()) break;
        }

        hasReadNextWord = true;
        nextWord = tok.toString();
        return true;
    }

    private boolean readNextSymbol() throws IOException{
        if (hasSavedChar) {
            return true;
        }
        symbolNow = in.read();
        if (symbolNow == -1) {
            isEndScanner = true;
        }
        hasSavedChar = symbolNow != -1;
        return hasSavedChar;
    }

    public int getNumOfLine() {
        return numOfCurrentLine;
    }

    public boolean hasNext() throws IOException{
        return hasReadNextWord || readNextWord();
    }

    public String next() throws IOException {
        if (!hasReadNextWord) {
            readNextWord();
        }
        hasReadNextWord = false;
        return nextWord;
    }

    public int nextInt() throws IOException{
        String ans = next();
        if (ans.length() >= 3 && (ans.charAt(1) == 'x' || ans.charAt(1) == 'X')) {
            return (int) Long.parseLong(ans.substring(2), 16);
        }
        return Integer.parseInt(ans);
    }

    public boolean isEmpty() {
        return isEndScanner;
    }

    public void close() throws IOException{
        in.close();
    }
}