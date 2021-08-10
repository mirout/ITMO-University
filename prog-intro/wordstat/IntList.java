import java.util.Arrays;

public class IntList{
    private static final int MIN_SIZE = 10;

    private int[] arr;
    private int size;

    public IntList() {
        arr = new int[MIN_SIZE];
    }

    public IntList(int... elem) {
        arr = elem.clone();
        size = elem.length;
    }

    public int get(int i) {
        return arr[i];
    }

    public void add(int x) {
        if (size == arr.length) {
            expandArray();
        }
        arr[size++] = x;
    }

    public String toString() {

        StringBuilder ans = new StringBuilder();

        for (int i = 0; i < size; i++) {
            ans.append(arr[i]);
            ans.append(' ');
        }

        ans.setLength(ans.length() - 1);
        return ans.toString();
    }

    public int indexOf(int elem) {
        for (int i = 0; i < size; i++) {
            if (arr[i] == elem) return i;
        }
        return -1;
    }

    private void expandArray() {
        arr = Arrays.copyOf(arr, size*2);
    }

    public int size() {
        return size;
    }
}
