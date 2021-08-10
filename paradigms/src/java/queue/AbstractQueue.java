package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {

    protected int size = 0;

    @Override
    public void clear() {
        size = 0;
        clearImpl();
    }

    protected abstract void clearImpl();

    @Override
    public void enqueue(final Object obj) {
        enqueueImpl(Objects.requireNonNull(obj));
        size++;
    }

    protected abstract void enqueueImpl(Object obj);

    @Override
    public Object element() {
        assert size > 0;
        return elementImpl();
    }

    protected abstract Object elementImpl();

    @Override
    public Object dequeue() {
        assert size > 0;
        final Object res = dequeueImpl();
        size--;
        return res;
    }

    protected abstract Object dequeueImpl();

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    public boolean containsAndRemoveIfNeed(final boolean delete, final Object element) {
        final int size = size();
        boolean found = false;
        for (int i = 0; i < size; i++) {
            final var e = dequeue();
            if (!found && e.equals(element)) {
                found = true;
                if (delete) {
                    continue;
                }
            }
            enqueue(e);
        }
        return found;
    }

    @Override
    public boolean contains(final Object element) {
        return containsAndRemoveIfNeed(false, element);
    }

    @Override
    public boolean removeFirstOccurrence(final Object element) {
        return containsAndRemoveIfNeed(true, element);
    }
}
