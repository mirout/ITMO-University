package search;

import java.util.Objects;

public class BinarySearch {

    // Pred: args != null && for all i, j = 1..args.length - 1 : i < j -> args[i] >= args[j]
    // Post: print result of Binary Search
    public static void main(String[] args) {
        Objects.requireNonNull(args);
        // Pred: args[0] != null
        // Post: x == args[0]
        int x = Integer.parseInt(args[0]);

        // Pred: true
        // Post: values.length == args.length - 1
        int[] values = new int[args.length - 1];

        // Inv: 1 <= i < args.length && for all j = 1..i values[j - 1] == args[j]
        for(int i = 1; i < args.length; i++) {
            // Pred: true
            values[i - 1] = Integer.parseInt(args[i]);
            // values[i - 1] == args[i]
        }
        // Post: for all j = 1..i values[j - 1] == args[j] &&
        // for all i, j = 0..values.length - 1 : i < j -> values[i] >= values[j]

        // Pred: for all i, j = 0..values.length - 1 : i < j -> values[i] >= values[j]
        // Post: print result of Binary Search
        System.out.println(iterateBinarySearch(values, x));
    }

    // Pred: for all i, j = 0..values.length - 1 : i < j -> values[i] >= values[j]
    // Post: R = i : 0 <= i < values.length && values[i] <= x && (i == 0 || values[i - 1] > x) || i == values.length && values[i - 1] > x
    private static int iterateBinarySearch(final int[] values, int x) {
        // Pred: true
        // Post: l == -1
        int l = -1;

        // Pred: true
        // Post: r == values.length
        int r = values.length;

        // Inv: l' <= l < r <= r' && (r - l) * 2 <= (r' - l') + 1 && (l == -1 || values[l] > x) &&
        // (values[r] <= x || r == values.length) && r - l >= 1
        while (r - l != 1) {
            // Pred: r - l
            // Post: mid = l + (r - l) / 2
            int mid = l + (r - l) / 2;

            if (values[mid] <= x) {
                // Pred: values[mid] <= x
                r = mid;
                // Post: l = l && r = mid &&
                // values[l] > x && values[r] <= x && (mid - l') * 2 <= (r' - l') + 1 -> (r - l) * 2 <= (r' - l') + 1
            } else {
                // Pred: values[mid] > x
                l = mid;
                // Post: l = mid && r = r'
                // values[l] > x && values[r] <= x && (r' - mid) * 2 <= (r' - l') + 1 -> (r - l) * 2 <= (r' - l') + 1
            }
        }
        // Post: r : r - l == 1 && 0 <= r < values.length && values[r] <= x && (r == 0 || values[r - 1] > x) ||
        // (r == values.length && values[r - 1] > x)

        return r;
    }

    // Pred: for all i, j = 0..values.length - 1 : i < j -> values[i] >= values[j]
    // Post: R = i : 0 <= i <= values.length && (values[i] <= x && (i == 0 || values[i - 1] > x) || i == values.length && values[i - 1] > x)
    private static int recursiveBinarySearch(final int[] values, int x) {
        return recursiveBinarySearch(values, x, -1, values.length);
    }

    // Pred: for all i, j = 0..values.length - 1 : i < j -> values[i] >= values[j] && -1 <= l < r <= values.length
    // Post: R = i : 0 <= i <= values.length && (values[i] <= x && (i == 0 || values[i - 1] > x) || i == values.length && values[i - 1] > x)
    // Inv: l' <= l < r <= r' && (r - l) * 2 <= (r' - l') + 1 && (l == -1 || values[l] > x) && (values[r] <= x || r == values.length) && r - l != 1
    private static int recursiveBinarySearch(final int[] values, int x, int l, int r) {
        if (r - l == 1) {
            // R = (r : r - l == 1 && 0 <= r < values.length && values[r] <= x && (r == 0 || values[r - 1] > x) ||
            // (r == values.length && values[r - 1] > x))
            return r;
        }

        // Pred: true
        // Post: mid == l + (r - l) / 2
        int mid = l + (r - l) / 2;

        if (values[mid] <= x) {
            // Pred: values[mid] <= x
            r = mid;
            // Post: l = l' && r = mid &&
            // values[l] > x && values[r] <= x && (mid - l') * 2 <= (r' - l') + 1 -> (r - l) * 2 <= (r' - l') + 1 &&
            // -1 <= l < r <= values.length &&
            // for all i, j = 0.. values.length - 1 : i < j -> values[i] >= values[j]
            return recursiveBinarySearch(values, x, l, r);
        } else {
            // Pred: values[mid] > x
            l = mid;
            // Post: l = mid && r = r'
            // values[l] > x && values[r] <= x && (r' - mid) * 2 <= (r' - l') + 1 -> (r - l) * 2 <= (r' - l') + 1 &&
            // -1 <= l < r <= values.length &&
            // for all i, j = 0.. values.length - 1 : i < j -> values[i] >= values[j]
            return recursiveBinarySearch(values, x, l, r);
        }
    }
}
