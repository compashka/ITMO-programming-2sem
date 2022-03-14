package queue;

import java.util.Arrays;
// Model: q[0]..q[size - 1]
// Invariant: for i=0..(size-1): q[i] != null

public class ArrayQueueADT {
    private Object[] elements = new Object[2];
    private int tail;
    private int head;
    private int size;

    // Pred: element != null
    // Post: size' = size + 1, for i = 0..(size - 1): q'[i] = q[i], q'[size' - 1] = element
    static public void enqueue(ArrayQueueADT queue, Object element) {
        assert element != null;
        ensureCapasity(queue);
        queue.elements[queue.tail] = element;
        queue.tail =(queue.tail + 1) % queue.elements.length;
        queue.size++;
    }
    
    static private void ensureCapasity(ArrayQueueADT queue) {
        if (queue.size >= queue.elements.length) {
            Object[] newElements = new Object[queue.elements.length * 2];
            System.arraycopy(queue.elements, queue.head, newElements, 0, queue.size - queue.head);
            System.arraycopy(queue.elements, 0, newElements, queue.size - queue.head, queue.head);
            queue.elements = newElements;
            queue.tail = queue.size;
            queue.head = 0;  
            
        }
    }

    // Pred: size > 0
    // Post: R = q[0], size' = size, for i = 0..(size - 1): q'[i] = q[i]
    static public Object element(ArrayQueueADT queue) {
        return queue.elements[queue.head];
    }

    // Pred: size > 0
    // Post: R = q[0], size' = size - 1, for i = 0..(size - 2): q'[i] = q[i + 1]
    static public Object dequeue(ArrayQueueADT queue) {
        if (queue.size < 1) {
            return null;
        }
        Object result = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        queue.head = (queue.head + 1) % queue.elements.length;
        queue.size--;
        return result;
    }

    // Pred: True
    // Post: R = size, size' = size, for i = 0..(size - 1): q'[i] = q[i]
    static public int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // Pred: True
    // Post: R = (size == 0), size' = size, for i = 0..(size - 1): q'[i] = q[i]
    static public Boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // Pred: True
    // Post: for i = 0..(size - 1): q'[i] = null, size' = 0
    static public void clear(ArrayQueueADT queue) {
        Arrays.fill(queue.elements, null);
        queue.size = queue.head = queue.tail = 0;
    }

    // Pred: element != null
    // Post: size' = size + 1, for i = 0..(size - 1): q'[i + 1] = q[i], q'[0] = element
    public static void push(ArrayQueueADT queue, Object element) {
        assert element != null;
        ensureCapasity(queue);
        queue.head = ((queue.head - 1) + queue.elements.length) % queue.elements.length;
        queue.elements[queue.head] = element;
        queue.size++;
    }

    // Pred: size > 0
    // Post: R = q[size - 1], size' = size, for i = 0..(size - 1): q'[i] = q[i]
    public static Object peek(ArrayQueueADT queue) {
        return queue.elements[(queue.tail - 1 + queue.elements.length) % queue.elements.length];
    }

    // Pred: size > 0
    // Post: R = q[size - 1], size' = size - 1, for i = 0..(size - 2): q'[i] = q[i]
    public static Object remove(ArrayQueueADT queue) {
        queue.tail = (queue.tail - 1 + queue.elements.length) % queue.elements.length;
        Object result = queue.elements[queue.tail];
        queue.elements[queue.tail] = null;
        queue.size--;
        return result;
    }

    // Pred: element != null
    // Post: R = number_of(element), size' = size, for i = 0..(size - 1): q'[i] = q[i]
    public static int count(ArrayQueueADT queue, Object element) {
        assert element != null;
        int res = 0;
        for (Object obj:queue.elements) {
            if (element.equals(obj)) {
                res++;
            }
        }
        return res;
    }
}

