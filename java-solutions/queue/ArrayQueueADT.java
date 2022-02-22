package queue;


public class ArrayQueueADT {
    private Object[] elements = new Object[16];
    private int tail = 0;
    private int head = 0;
    private int size = 0;

    // Pred: True
    // Post: elements[tail++] = element, size' = size + 1
    static public void enqueue(ArrayQueueADT queue, Object element) {
        if (element == null) {
            return;
        }
        ensureCapasity(queue);
        queue.elements[queue.tail] = element;
        queue.tail = (queue.tail + 1) % queue.elements.length;
        queue.size++;
    }
    
    // Pred: True
    // Post: elements[head--] = element, size' = size + 1
    public static void push(ArrayQueueADT queue, Object element) {
        if (element == null) {
            return;
        }
        ensureCapasity(queue);
        queue.head = ((queue.head - 1) + queue.elements.length) % queue.elements.length;
        queue.elements[queue.head] = element;
        queue.size++;
    }

    // Pred: True
    // Post: R = elements[(tail - 1 + elements.length) % elements.length]
    public static Object peek(ArrayQueueADT queue) {
        return queue.elements[(queue.tail - 1 + queue.elements.length) % queue.elements.length];
    }

    // Pred: True
    // Post: R = elements[(tail - 1 + elements.length) % elements.length] && size' = size - 1
    public static Object remove(ArrayQueueADT queue) {
        Object result = queue.elements[(queue.tail - 1 + queue.elements.length) % queue.elements.length];
        queue.elements[(queue.tail - 1 + queue.elements.length) % queue.elements.length] = null;
        queue.tail--;
        queue.size--;
        return result;
    }

    // Pred: element != null
    // Post: R = number(element)
    public static int count(ArrayQueueADT queue, Object element) {
        int res = 0;
        for (Object obj:queue.elements) {
            if (element.equals(obj)) {
                res++;
            }
        }
        return res;
    }

    // Pred: True
    // Post: (size >= elements.length && elements.length' = length * 2 && tail' = size && head' = 0)
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

    // Pred: True
    // Post: R = element[head]
    static public Object element(ArrayQueueADT queue) {
        return queue.elements[queue.head];
    }

    // Pred: true
    // Post: R = elements[head] && size' = size - 1 && head' = (head + 1) % elements.length
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
    // Post: R = size
    static public int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // Pred: True
    // Post: R = (size == 0)
    static public Boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // Pred: True
    // Post: for i=0..elements.length, elements[i] = null
    static public void clear(ArrayQueueADT queue) {
        for (int i = queue.head; i < queue.head + queue.size; i++) {
            i = (i % queue.elements.length);
            queue.elements[i] = null;
        }
        queue.head = 0;
        queue.tail = 0;
        queue.size = 0;
    }
}

