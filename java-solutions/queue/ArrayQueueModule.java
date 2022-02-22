package queue;



// Model: a[1]..a[n]
    // Invariant: for i=head..(head + size): a[i] != null

    
public class ArrayQueueModule{
    private static Object[] elements = new Object[16];
    private static int tail = 0;
    private static int head = 0;
    private static int size = 0;

    // Pred: True
    // Post: elements[tail++] = element, size' = size + 1
    static public void enqueue(Object element) {
        if (element == null) {
            return;
        }
        ensureCapasity();
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    // Pred: True
    // Post: elements[head--] = element, size' = size + 1
    public static void push(Object element) {
        if (element == null) {
            return;
        }
        ensureCapasity();
        head = ((head - 1) + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    // Pred: True
    // Post: R = elements[(tail - 1 + elements.length) % elements.length]
    public static Object peek() {
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    // Pred: True
    // Post: R = elements[(tail - 1 + elements.length) % elements.length] && size' = size - 1
    public static Object remove() {
        Object result = elements[(tail - 1 + elements.length) % elements.length];
        elements[(tail - 1 + elements.length) % elements.length] = null;
        tail--;
        size--;
        return result;
    }

    // Pred: element != null
    // Post: R = number(element)
    public static int count(Object element) {
        int res = 0;
        for (Object obj:elements) {
            if (element.equals(obj)) {
                res++;
            }
        }
        return res;
    }

    // Pred: True
    // Post: (size >= elements.length && elements.length' = length * 2 && tail' = size && head' = 0)
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

    // Pred: True
    // Post: R = element[head]
    static public Object element() {
        return elements[head];
    }

    // Pred: true
    // Post: R = elements[head] && size' = size - 1 && head' = (head + 1) % elements.length
    static public Object dequeue() {
        if (size < 1) {
            return null;
        }
        Object result = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return result;
    }

    // Pred: True
    // Post: R = size
    static public int size() {
        return size;
    }

    // Pred: True
    // Post: R = (size == 0)
    static public Boolean isEmpty() {
        return size == 0;
    }

    // Pred: True
    // Post: for i=0..elements.length, elements[i] = null
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