package queue;

public class LinkedQueue extends AbstractQueue{
    private Node head;
    private Node tail;

    @Override
    protected void clearImpl() {
        head = tail = null;
    }

    @Override
    protected void enqueueImpl(final Object obj) {
        if (super.isEmpty()) {
            head = tail = new Node(obj);
            return;
        }
        tail.next = new Node(obj);
        tail = tail.next;
    }

    @Override
    protected Object elementImpl() {
        return head.val;
    }

    @Override
    protected Object dequeueImpl() {
        final Object res = head.val;
        head = head.next;
        return res;
    }

    private static class Node {
        public final Object val;
        private Node next;

        public Node(final Object val) {
            this.val = val;
        }
    }
}
