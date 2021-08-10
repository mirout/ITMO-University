package queue.test;

import queue.ArrayQueue;

public class ArrayQueueTest {

    public static void main(String[] args) {
        test();
    }

    public static void test() {
        testIsEmpty();
        testEnqueueAndSize();
        testElement();
        testDequeue();
        testClear();
        testPeek();
        testPush();
        testRemove();
        System.out.println("ArrayQueue tests passed");
    }

    private static ArrayQueue makeQueueOf5Integers() {
        ArrayQueue queue = new ArrayQueue();
        for (int i = 0; i < 5; i++) {
            queue.enqueue(i);
        }
        return queue;
    }

    private static void testIsEmpty() {
        ArrayQueue queue = new ArrayQueue();
        assert queue.isEmpty();
    }

    private static void testEnqueueAndSize() {
        ArrayQueue queue = makeQueueOf5Integers();
        assert queue.size() == 5;
    }

    private static void testElement() {
        ArrayQueue queue = makeQueueOf5Integers();
        assert (Integer) queue.element() == 0;
    }

    private static void testDequeue() {
        ArrayQueue queue = makeQueueOf5Integers();
        assert (Integer) queue.dequeue() == 0;
        assert (Integer) queue.dequeue() == 1;
    }

    private static void testClear() {
        ArrayQueue queue = makeQueueOf5Integers();
        assert queue.size() == 5;
        queue.clear();
        assert queue.size() == 0;
    }

    private static void testPeek() {
        ArrayQueue queue = makeQueueOf5Integers();
        //assert (Integer) queue.peek() == 4;
    }

    private static void testRemove() {
        ArrayQueue queue = makeQueueOf5Integers();
        //assert (Integer) queue.remove() == 4;
        //assert (Integer) queue.remove() == 3;
    }

    private static void testPush() {
        ArrayQueue queue = new ArrayQueue();
        //queue.push(0);
        assert  queue.size() == 1;
    }
}
