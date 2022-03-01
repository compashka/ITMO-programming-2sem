package queue;

public class ArrQueueModuleTest {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println("size q1: " + ArrayQueueModule.size());
            System.out.println("isEmpty q1: "+ ArrayQueueModule.isEmpty());
            ArrayQueueModule.enqueue("q_1_" + i);
        }
        
        System.out.println(ArrayQueueModule.remove());
        System.out.println(ArrayQueueModule.remove());

        for (int i = 0; i < 10; i++) {
            System.out.println(ArrayQueueModule.dequeue());
        }

        System.out.println("head queue: " + ArrayQueueModule.element());
    }
}
