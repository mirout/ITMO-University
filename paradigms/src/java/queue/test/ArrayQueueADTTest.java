package queue.test;

import queue.ArrayQueueADT;

public class ArrayQueueADTTest {

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
        testRemove();
        testPush();
        System.out.println("ArrayQueueADT tests passed");
    }

    private static ArrayQueueADT makeQueueOf5Integers() {
        ArrayQueueADT queue = ArrayQueueADT.create();
        for (int i = 0; i < 5; i++) {
            ArrayQueueADT.enqueue(queue, i);
        }
        return queue;
    }

    private static void testIsEmpty() {
        ArrayQueueADT queue = ArrayQueueADT.create();
        assert ArrayQueueADT.isEmpty(queue);
    }

    private static void testEnqueueAndSize() {
        ArrayQueueADT queue = makeQueueOf5Integers();
        assert ArrayQueueADT.size(queue) == 5;
    }

    private static void testElement() {
        ArrayQueueADT queue = makeQueueOf5Integers();
        assert (Integer) ArrayQueueADT.element(queue) == 0;
    }

    private static void testDequeue() {
        ArrayQueueADT queue = makeQueueOf5Integers();
        assert (Integer) ArrayQueueADT.dequeue(queue) == 0;
        assert (Integer) ArrayQueueADT.dequeue(queue) == 1;
    }

    private static void testClear() {
        ArrayQueueADT queue = makeQueueOf5Integers();
        assert ArrayQueueADT.size(queue) == 5;
        ArrayQueueADT.clear(queue);
        assert ArrayQueueADT.size(queue) == 0;
    }

    private static void testPeek() {
        ArrayQueueADT queue = makeQueueOf5Integers();
        assert (Integer) ArrayQueueADT.peek(queue) == 4;
    }

    private static void testRemove() {
        ArrayQueueADT queue = makeQueueOf5Integers();
        assert (Integer) ArrayQueueADT.remove(queue) == 4;
        assert (Integer) ArrayQueueADT.remove(queue) == 3;
    }

    private static void testPush() {
        ArrayQueueADT queue = new ArrayQueueADT();
        ArrayQueueADT.push(queue, 0);
        assert  ArrayQueueADT.size(queue) == 1;
    }

}
