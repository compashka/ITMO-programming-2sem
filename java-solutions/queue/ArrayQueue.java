package queue;

// Model: queue
// Invariant: (head < i < tail || tail < i < head) => elements[i] != null

public class ArrayQueue {
    private Object[] elements = new Object[16];
    private int tail = 0;
    private int head = 0;
    private int size = 0;



    // Pred: True
    // Post: elements[tail++] = element, size' = size + 1
    public void enqueue(Object element) {
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
    public void push(Object element) {
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
    public Object peek() {
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    // Pred: True
    // Post: R = elements[(tail - 1 + elements.length) % elements.length] && size' = size - 1
    public Object remove() {
        Object result = elements[(tail - 1 + elements.length) % elements.length];
        elements[(tail - 1 + elements.length) % elements.length] = null;
        tail--;
        size--;
        return result;
    }

    // Pred: element != null
    // Post: R = number(element)
    public int count(Object element) {
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
    private void ensureCapasity() {
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
    public Object element() {
        return elements[head];
    }

    // Pred: true
    // Post: R = elements[head] && size' = size - 1 && head' = (head + 1) % elements.length
    public Object dequeue() {
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
    public int size() {
        return size;
    }

    // Pred: True
    // Post: R = (size == 0)
    public Boolean isEmpty() {
        return size == 0;
    }

    // Pred: True
    // Post: for i=0..elements.length, elements[i] = null
    public void clear() {
        for (int i = head; i < head + size; i++) {
            i = (i % elements.length);
            elements[i] = null;
        }
        head = 0;
        tail = 0;
        size = 0;
    }
}
