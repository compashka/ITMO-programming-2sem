package queue;

public class LinkedQueue extends AbstractQueue {
    private Node tail;
    private Node head;

    @Override
    protected void enqueueImpl(Object element) {
        Node.next(tail, tail = new Node(element));
        if (size == 0) {
            head = tail;
        }
    }

    @Override
    protected Object elementImpl() {
        return head.element;
    }

    @Override
    protected void dequeueImpl() {
        head = head.next;
    }

    @Override
    protected void clearImpl() {
        head = tail = null;
    }

    @Override
    protected LinkedQueue createCopy() {
        LinkedQueue result = new LinkedQueue();
        Node temp = head;
        while (temp != null) {
            result.enqueue(temp.element);
            temp = temp.next;
        }
        return result;
    }
    
    private static class Node {
        private Object element;
        private Node next; 
    
        public Node(Object element) {
            this.element = element;
        }
        public static void next(Node current, Node next){
            if (current != null) {
                current.next = next;
            }
        }
    }
}
