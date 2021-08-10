import java.io.*;

class Scanner {

    private final Reader in;
    private final Checker check;

    private String nextToken;

    private int numOfCurrentLine = 0;
    private int symbolNow = 0;

    private boolean isEndScanner = false;
    private boolean hasReadToken = false;
    private boolean isStartOfLineSeparator = false;


    public Scanner(Reader r, Checker check) {
        this.in = r;
        this.check = check;
    }

    public Scanner (InputStream in, Checker check){
        this(new BufferedReader(new InputStreamReader(in)), check);
    }

    private boolean readNewToken() throws IOException{

        StringBuilder tok = new StringBuilder();

        if (!readNextSymbol()) return false;

        while (!check.isWordCharacter(symbolNow)) {

            if (symbolNow == '\r') {
                isStartOfLineSeparator = true;
            } else if (isStartOfLineSeparator || symbolNow == '\n') {
                numOfCurrentLine++;
                isStartOfLineSeparator = false;
            }

            if (!readNextSymbol()) return false;
        }

        while (check.isWordCharacter(symbolNow)) {
            tok.append((char) symbolNow);
            if (!readNextSymbol()) break;
        }
        hasReadToken = true;
        nextToken = tok.toString();
        return true;
    }

    private boolean readNextSymbol() throws IOException{
        symbolNow = in.read();
        if (symbolNow == -1) {
            isEndScanner = true;
        }
        return symbolNow != -1;
    }

    public int getNumOfLine() {
        return numOfCurrentLine;
    }

    public boolean hasNext() throws IOException{
        return hasReadToken || readNewToken();
    }

    public String next() throws IOException {
        if (!hasReadToken) {
            readNewToken();
        }
        hasReadToken = false;
        return nextToken;
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