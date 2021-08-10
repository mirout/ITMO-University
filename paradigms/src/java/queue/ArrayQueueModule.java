package queue;

import java.util.Arrays;
import java.util.Objects;

/*
    Model:
        [a0, a1, ..., a{n-1}]
        n -- queue size

    Inv:
        n >= 0
        forall i = 0..n: a[i] != null
 */

public class ArrayQueueModule {
    private static final int BASE_CAPACITY = 16;

    private static Object[] array = new Object[BASE_CAPACITY];
    private static int start = 0;
    private static int end = 0;
    private static int size = 0;

    // Pred: obj != null
    // Post: n = n' + 1 &&  a[n - 1] == obj && forall i = 0..n': a[i] == a'[i]
    public static void enqueue(Object obj) {
        Objects.requireNonNull(obj);
        ensureCapacity();

        array[end++] = obj;
        end %= array.length;
        size++;
    }

    private static void ensureCapacity() {
        if (size != array.length) {
            return;
        }
        reallocate();
    }

    private static void reallocate() {
        Object[] temp = new Object[array.length * 2];
        int shift = array.length - start;

        System.arraycopy(array, start, temp, 0, shift);
        System.arraycopy(array, 0, temp, shift, end);

        array = temp;
        start = 0;
        end = size;
    }

    // Pred: n > 0
    // Post: R == a[0] && n == n' && forall i = 0..n: a[i] == a'[i]
    public static Object element() {
        assert size > 0;
        return array[start];
    }

    // Pred: n > 0
    // Post: R = a'[0] && n = n' - 1 && forall i = 0..n: a[i] == a'[i + 1]
    public static Object dequeue() {
        assert size > 0;
        Object result = array[start];
        array[start++] = null;
        start %= array.length;
        size--;
        return result;
    }

    // Pred: true
    // Post: R == n
    public static int size() {
        return size;
    }

    // Pred: true
    // Post: R == (n == 0)
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pred: true
    // Post: n' == 0
    public static void clear() {
        if (start <= end) {
            Arrays.fill(array, start, end, null);
        } else {
            Arrays.fill(array, start, array.length, null);
            Arrays.fill(array, 0, end, null);
        }

        size = 0;
        end = 0;
        start = 0;
    }

    // Pred: n > 0
    // Post:  R == a[n] && n == n' && forall i = 0..n: a[i] == a'[i]
    public static Object peek() {
        assert size > 0;
        return array[(end - 1 + array.length) % array.length];
    }

    // Pred: n > 0
    // Post:  R == a'[n] && n == n' - 1 && forall i = 0..n: a[i] == a'[i]
    public static Object remove() {
        assert size > 0;
        end = (end - 1 + array.length) % array.length;
        var temp = array[end];
        array[end] = null;
        size--;
        return temp;
    }

    // Pred: obj != null
    // Post: n = n' + 1 &&  a[0] == obj && forall i = 1..n: a[i] == a'[i - 1]
    public static void push(Object obj) {
        Objects.requireNonNull(obj);
        ensureCapacity();
        start = (start - 1 + array.length) % array.length;
        size++;
        array[start] = obj;
    }
}
