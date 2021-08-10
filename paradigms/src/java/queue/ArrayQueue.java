package queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class ArrayQueue extends AbstractQueue {
    private static final int BASE_CAPACITY = 16;

    private Object[] array = new Object[BASE_CAPACITY];
    private int start = 0;

    @Override
    protected void clearImpl() {
        Arrays.fill(array, null);
    }

    @Override
    protected void enqueueImpl(Object obj) {
        ensureCapacity();
        array[end()] = obj;
    }

    @Override
    protected Object elementImpl() {
        return array[start];
    }

    @Override
    protected Object dequeueImpl() {
        Object res = array[start];
        array[start] = null;

        start = (start + 1) % array.length;

        return res;
    }

    private void ensureCapacity() {
        if (super.size != array.length) {
            return;
        }
        reallocate();
    }

    private void reallocate() {
        final Object[] temp = new Object[array.length * 2];
        final int shift = array.length - start;

        System.arraycopy(array, start, temp, 0, shift);
        System.arraycopy(array, 0, temp, shift, end());

        array = temp;
        start = 0;
    }

    public int end() {
        return (start + super.size) % array.length;
    }

    // Pred: n > 0
    // Post:  R == a[n] && n == n' && forall i = 0..n: a[i] == a'[i]
    public Object peek() {
        assert size > 0;
        return array[(end() - 1 + array.length) % array.length];
    }

    // Pred: n > 0
    // Post:  R == a'[n] && n == n' - 1 && forall i = 0..n: a[i] == a'[i]
    public Object remove() {
        assert size > 0;
        super.size--;
        final var temp = array[end()];
        array[end()] = null;
        return temp;
    }

    // Pred: obj != null
    // Post: n = n' + 1 &&  a[0] == obj && forall i = 1..n: a[i] == a'[i - 1]
    public void push(final Object obj) {
        Objects.requireNonNull(obj);
        ensureCapacity();
        start = (start - 1 + array.length) % array.length;
        size++;
        array[start] = obj;
    }

}
