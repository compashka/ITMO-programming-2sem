package queue;

public class ArrQueueTest {
    public static void main(String[] args) {
        ArrayQueue q1 = new ArrayQueue();
        ArrayQueue q2 = new ArrayQueue();
        for (int i = 0; i < 10; i++) {
            System.out.println("size q1: " + q1.size());
            System.out.println("isEmpty q1: "+ q1.isEmpty());
            System.out.println("size q2: " + q2.size());
            System.out.println("isEmpty q2: "+ q2.isEmpty());
            q1.enqueue("q_1_" + i);
            q2.enqueue("q_2_" + i);
        }
        
        System.out.println(q1.remove());
        System.out.println(q2.remove());
        for (int i = 0; i < 10; i++) {
            System.out.println(q1.dequeue());
            System.out.println(q2.dequeue());
        }

        
    }

}
