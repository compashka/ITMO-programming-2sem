package queue;

import java.util.Arrays;

public class ArrayQueue extends AbstractQueue {
    private Object[] elements;
    private int tail;
    private int head;

    public ArrayQueue(){
        elements = new Object[2];
    }

    public ArrayQueue(final int length){
        elements = new Object[length];
    }

    @Override
    protected void enqueueImpl(final Object element) {
        ensureCapasity();
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
    }

    private void ensureCapasity() {
        if (size >= elements.length) {
            final Object[] newElements = new Object[elements.length * 2];
            System.arraycopy(elements, head, newElements, 0, size - head);
            System.arraycopy(elements, 0, newElements, size - head, head);
            elements = newElements;
            tail = size;
            head = 0; 
        }
    }

    @Override
    protected Object elementImpl() {
        return elements[head];
    }

    @Override
    public void dequeueImpl() {
        elements[head] = null;
        head = (head + 1) % elements.length;
    }

    @Override
    protected void clearImpl() {
        Arrays.fill(elements, null);
        head = tail = 0;
    }

    @Override
    protected ArrayQueue createCopy() {
        final ArrayQueue result = new ArrayQueue(elements.length);
        result.head = head;
        result.tail = tail;
        result.size = size;
        System.arraycopy(elements, 0, result.elements, 0, elements.length);
        return result;
    }

    // Pred: element != null
    // Post: size' = size + 1, for i = 0..(size - 1): q'[i + 1] = q[i], q'[0] = element
    public void push(final Object element) {
        assert element != null;
        ensureCapasity();
        head = ((head - 1) + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    // Pred: size > 0
    // Post: R = q[size - 1], size' = size, for i = 0..(size - 1): q'[i] = q[i]
    public Object peek() {
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    // Pred: size > 0
    // Post: R = q[size - 1], size' = size - 1, for i = 0..(size - 2): q'[i] = q[i]
    public Object remove() {
        tail = (tail - 1 + elements.length) % elements.length;
        final Object result = elements[tail];
        elements[tail] = null;
        size--;
        return result;
    }

    // :NOTE: Неформально
    // Pred: element != null
    // Post: R = |S|, S = {i = 0..size-1, q[i] = element}, size' = size, for i = 0..(size - 1): q'[i] = q[i]
    public int count(final Object element) {
        assert element != null;
        int res = 0;
        for (final Object obj:elements) {
            if (element.equals(obj)) {
                res++;
            }
        }
        return res;
    }
}
