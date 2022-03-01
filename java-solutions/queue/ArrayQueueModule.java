package queue;

// Model: q[0]..q[size - 1]
// Invariant: for i=0..(size-1): q[i] != null

public class ArrayQueueModule{
    private static Object[] elements = new Object[16];
    private static int tail = 0;
    private static int head = 0;
    private static int size = 0;

    // Pred: element != null
    // Post: size' = size + 1, for i = 0..(size - 1): q'[i] = q[i], q'[size' - 1] = element
    static public void enqueue(Object element) {
        assert element != null;
        ensureCapasity();
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    // Pred: element != null
    // Post: size' = size + 1, for i = 0..(size - 1): q'[i + 1] = q[i], q'[0] = element
    public static void push(Object element) {
        assert element != null;
        ensureCapasity();
        head = ((head - 1) + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    // Pred: size > 0
    // Post: R = q[size - 1], size' = size, for i = 0..(size - 1): q'[i] = q[i]
    public static Object peek() {
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    // Pred: size > 0
    // Post: R = q[size - 1], size' = size - 1, for i = 0..(size - 2): q'[i] = q[i]
    public static Object remove() {
        Object result = elements[(tail - 1 + elements.length) % elements.length];
        elements[(tail - 1 + elements.length) % elements.length] = null;
        tail--;
        size--;
        return result;
    }

    // Pred: element != null
    // Post: R = number_of(element), size' = size, for i = 0..(size - 1): q'[i] = q[i]
    public static int count(Object element) {
        assert element != null;
        int res = 0;
        for (Object obj:elements) {
            if (element.equals(obj)) {
                res++;
            }
        }
        return res;
    }

    // Pred: True
    /* Post: if (size >= capasity) => capasity' = capasity * 2, size' = size, 
                                          for i = 0..(size - 1): q'[i] = q[i]*/
    static private void ensureCapasity() {
        if (size >= elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            System.arraycopy(elements, head, newElements, 0, size - head);
            System.arraycopy(elements, 0, newElements, size - head, head);
            elements = newElements;
            tail = size;
            head = 0;  
        }
    }

    // Pred: size > 0
    // Post: R = q[0], size' = size, for i = 0..(size - 1): q'[i] = q[i]
    static public Object element() {
        return elements[head];
    }

    // Pred: size > 0
    // Post: R = q[0], size' = size - 1, for i = 0..(size - 2): q'[i] = q[i + 1]
    static public Object dequeue() {
        Object result = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return result;
    }

    // Pred: True
    // Post: R = size, size' = size, for i = 0..(size - 1): q'[i] = q[i]
    static public int size() {
        return size;
    }

    // Pred: True
    // Post: R = (size == 0), size' = size, for i = 0..(size - 1): q'[i] = q[i]
    static public Boolean isEmpty() {
        return size == 0;
    }

    // Pred: True
    // Post: for i = 0..(size - 1): q'[i] = null, size' = 0
    static public void clear() {
        for (int i = head; i < head + size; i++) {
            i = (i % elements.length);
            elements[i] = null;
        }
        size = 0;
        head = 0;
        tail = 0;
    }
}