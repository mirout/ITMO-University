import java.util.Arrays;
import java.io.IOException;

class ReverseAvgChecker implements Checker {
    public boolean isWordCharacter(char c) {
        return !(Character.SPACE_SEPARATOR == Character.getType(c));
    }
}

public class ReverseAvg {

    private static final int MIN_SIZE = 10;

    private static int[] expandArrayInt(int[] arr) {
        return Arrays.copyOf(arr, arr.length*2);
    }
    
    private static long[] expandArrayLong(long[] arr) {
        return Arrays.copyOf(arr, arr.length*2);
    }

    public static void main(String[] args) {

        int[] numbers = new int[MIN_SIZE];

        long[] sumInRow = new long[MIN_SIZE];
        long[] sumInColumn = new long[MIN_SIZE];

        int[] numInRow = new int[MIN_SIZE];
        int[] numInColumn = new int[MIN_SIZE];

        Scanner in = new Scanner(System.in, new ReverseChecker());

        int indexRow = 0;
        int indexColumn = 0;
        int indexNum = 0;
        try {

            while (!in.isEmpty()) {

                while (!in.isEndOfLine()) {

                    int x = in.nextInt();

                    numbers[indexNum++] = x;

                    if (indexNum == numbers.length) {
                        numbers = expandArrayInt(numbers);
                    }

                    sumInRow[indexRow] += x;
                    sumInColumn[indexColumn] += x;

                    numInRow[indexRow]++;
                    numInColumn[indexColumn]++;

                    indexColumn++;

                    if (indexColumn == numInColumn.length) {
                        numInColumn = expandArrayInt(numInColumn);
                        sumInColumn = expandArrayLong(sumInColumn);
                    }


                }

                indexRow++;

                if (indexRow == numInRow.length) {
                    numInRow = expandArrayInt(numInRow);
                    sumInRow = expandArrayLong(sumInRow);
                }

                in.skipLine();

                indexColumn = 0;
            }
        } catch (IOException e) {
            System.out.println("Oops");
        }

        indexNum = 0;

        for (int i = 0; i < indexRow; i++) {
            for (int j = 0; j < numInRow[i]; j++) {
                System.out.print((sumInRow[i] + sumInColumn[j] - numbers[indexNum++]) / (numInRow[i] + numInColumn[j] - 1 ) + " ");    
            }
            System.out.println();
        }
    }
}