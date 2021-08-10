package queue.test;

import queue.ArrayQueue;
import queue.ArrayQueueADT;
import queue.ArrayQueueModule;

public class ArrayQueueModuleTest {

    public static void main(String[] args) {
        test();
    }

    public static void test() {
        testIsEmpty();
        testClear();
        testEnqueueAndSize();
        testDequeue();
        testElement();
        testPeek();
        testRemove();
        testPush();
        System.out.println("ArrayQueueModule tests passed");
    }

    private static void testIsEmpty() {
        assert ArrayQueueModule.isEmpty();
    }

    private static void testEnqueueAndSize() {
        add5IntegersToQueue();
        assert ArrayQueueModule.size() == 5;
        ArrayQueueModule.clear();
    }

    private static void testElement() {
        add5IntegersToQueue();
        assert (Integer) ArrayQueueModule.element() == 0;
        ArrayQueueModule.clear();
    }

    private static void testDequeue() {
        add5IntegersToQueue();
        assert (Integer) ArrayQueueModule.dequeue() == 0;
        assert (Integer) ArrayQueueModule.dequeue() == 1;
        ArrayQueueModule.clear();
    }

    private static void testClear() {
        add5IntegersToQueue();
        assert ArrayQueueModule.size() == 5;
        ArrayQueueModule.clear();
        assert ArrayQueueModule.size() == 0;
    }

    private static void add5IntegersToQueue() {
        for (int i = 0; i < 5; i++) {
            ArrayQueueModule.enqueue(i);
        }
    }

    private static void testPeek() {
        add5IntegersToQueue();
        assert (Integer) ArrayQueueModule.peek() == 4;
        ArrayQueueModule.clear();
    }

    private static void testRemove() {
        add5IntegersToQueue();
        assert (Integer) ArrayQueueModule.remove() == 4;
        assert (Integer) ArrayQueueModule.remove() == 3;
        ArrayQueueModule.clear();
    }

    private static void testPush() {
        ArrayQueueModule.push(0);
        assert  ArrayQueueModule.size() == 1;
        ArrayQueueModule.clear();
    }
}
