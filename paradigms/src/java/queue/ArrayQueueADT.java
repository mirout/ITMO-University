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

public class ArrayQueueADT {
    private static final int BASE_CAPACITY = 16;

    private Object[] array = new Object[BASE_CAPACITY];
    private int start = 0;
    private int end = 0;
    private int size = 0;

    // Pred: queue != null
    // Post: R is new queue && R.n == 0
    public static ArrayQueueADT create() {
        return new ArrayQueueADT();
    }

    // Pred: queue != null && obj != null
    // Post: n = n' + 1 &&  a[n - 1] == obj && forall i = 0..n': a[i] == a'[i]
    public static void enqueue(final ArrayQueueADT queue, final Object obj) {
        Objects.requireNonNull(obj);
        Objects.requireNonNull(queue);
        ensureCapacity(queue);

        queue.array[queue.end++] = obj;
        queue.end %= queue.array.length;
        queue.size++;
    }

    private static void ensureCapacity(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        if (queue.size != queue.array.length) {
            return;
        }
        reallocate(queue);
    }

    private static void reallocate(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);

        final Object[] temp = new Object[queue.array.length * 2];
        final int shift = queue.array.length - queue.start;

        System.arraycopy(queue.array, queue.start, temp, 0, shift);
        System.arraycopy(queue.array, 0, temp, shift, queue.end);

        queue.array = temp;
        queue.start = 0;
        queue.end = queue.size;
    }

    // Pred: queue != null && n > 0
    // Post: R == a[0] && n == n' && forall i = 0..n: a[i] == a'[i]
    public static Object element(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert queue.size > 0;
        return queue.array[queue.start];
    }

    // Pred: queue != null && n > 0
    // Post: R = a'[0] && n = n' - 1 && forall i = 0..n: a[i] == a'[i + 1]
    public static Object dequeue(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert queue.size > 0;
        final Object result = queue.array[queue.start];
        queue.array[queue.start++] = null;
        queue.start %= queue.array.length;
        queue.size--;
        return result;
    }

    // Pred: queue != null
    // Post: R == n
    public static int size(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        return queue.size;
    }

    // Pred: queue != null
    // Post: R == (n == 0)
    public static boolean isEmpty(final ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // Pred: queue != null
    // Post: n' == 0
    public static void clear(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        if (queue.start <= queue.end) {
            Arrays.fill(queue.array, queue.start, queue.end, null);
        } else {
            Arrays.fill(queue.array, queue.start, queue.array.length, null);
            Arrays.fill(queue.array, 0, queue.end, null);
        }
        queue.size = 0;
        queue.end = 0;
        queue.start = 0;
    }

    // Pred: queue != null && n > 0
    // Post:  R == a[n] && n == n' && forall i = 0..n: a[i] == a'[i]
    public static Object peek(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert queue.size > 0;
        return queue.array[(queue.end - 1 + queue.array.length) % queue.array.length];
    }

    // Pred: queue != null && n > 0
    // Post:  R == a'[n] && n == n' - 1 && forall i = 0..n: a[i] == a'[i]
    public static Object remove(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert queue.size > 0;
        queue.end = (queue.end - 1 + queue.array.length) % queue.array.length;
        final var temp = queue.array[queue.end];
        queue.array[queue.end] = null;
        queue.size--;
        return temp;
    }

    // Pred: queue != null && obj != null
    // Post: n = n' + 1 &&  a[0] == obj && forall i = 1..n: a[i] == a'[i - 1]
    public static void push(final ArrayQueueADT queue, final Object obj) {
        Objects.requireNonNull(queue);
        Objects.requireNonNull(obj);
        ensureCapacity(queue);
        queue.start = (queue.start - 1 + queue.array.length) % queue.array.length;
        queue.size++;
        queue.array[queue.start] = obj;
    }
}
