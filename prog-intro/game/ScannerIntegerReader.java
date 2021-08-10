package game;

import java.util.Scanner;

public class ScannerIntegerReader {

    private final Scanner in;

    public ScannerIntegerReader(Scanner in) {
        this.in = in;
    }

    public ScannerIntegerReader() {
        this(new Scanner(System.in));
    }

    public int nextPositive() {
        int result = nextNotNegative();
        if (result > 0) {
            return result;
        }
        return -1;
    }

    public int nextNotNegative() {
        if (in.hasNextInt()) {
            int result = in.nextInt();
            if (result >= 0) {
                return result;
            }
        } else {
            in.next();
        }
        return -1;
    }

}
