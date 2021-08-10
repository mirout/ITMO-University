import java.util.Arrays;
import java.io.IOException;

class ReverseChecker implements Checker {
    public boolean isWordCharacter(int c) {
        return Character.isDigit(c) 
            || c == '-'
            || c == 'a'
            || c == 'b'
            || c == 'c'
            || c == 'd'
            || c == 'e'
            || c == 'f'
            || c == 'x'
            || c == 'X';
    }
}

public class ReverseHexDec {

    private static final int MIN_SIZE = 10;

    private static int[] expandArray(int[] arr) {
        return Arrays.copyOf(arr, arr.length*2);
    }

    public static void main(String[] args) {
        int[] numbers = new int[MIN_SIZE];
        int[] numInLine = new int[MIN_SIZE];

        FastScanner in = new FastScanner(System.in, new ReverseChecker());

        int indexNum = 0;
        int indexRow = 0;

        try {

            while (in.hasNext()) {

                int x = in.nextInt();

                if (indexRow != in.getNumOfLine()) {
                    indexRow = in.getNumOfLine();
                    while (indexRow >= numInLine.length) {
                        numInLine = expandArray(numInLine);
                    }
                }

                numbers[indexNum++] = x;

                if (indexNum == numbers.length) {
                    numbers = expandArray(numbers);
                }
                numInLine[indexRow]++;

            }
        } catch (IOException e) {
            System.out.println("Oops");
        }

        if (indexRow != in.getNumOfLine()) {
            indexRow = in.getNumOfLine();
            while (indexRow >= numInLine.length) {
                numInLine = expandArray(numInLine);
            }
        }

        for (int i = --indexRow; i >= 0; i--) {
            for (int j = indexNum-1; j >= indexNum - numInLine[i]; j--) {
                System.out.print(numbers[j] + " ");
            }
            indexNum -= numInLine[i];
            System.out.println();

        }
    }
}