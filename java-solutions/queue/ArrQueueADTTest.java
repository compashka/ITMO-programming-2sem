package queue;

public class ArrQueueADTTest {
    public static void main(String[] args) {
        ArrayQueueADT q1 = new ArrayQueueADT();
        ArrayQueueADT q2 = new ArrayQueueADT();
        for (int i = 0; i < 10; i++) {
            System.out.println("size q1: " + ArrayQueueADT.size(q1));
            System.out.println("isEmpty q1: "+ ArrayQueueADT.isEmpty(q1));
            System.out.println("size q2: " + ArrayQueueADT.size(q2));
            System.out.println("isEmpty q2: "+ ArrayQueueADT.isEmpty(q2));
            ArrayQueueADT.enqueue(q1, "q_1_" + i);
            ArrayQueueADT.enqueue(q2, "q_2_" + i);
        }
        
        System.out.println(ArrayQueueADT.remove(q1));
        System.out.println(ArrayQueueADT.remove(q2));

        for (int i = 0; i < 10; i++) {
            System.out.println(ArrayQueueADT.dequeue(q1));
            System.out.println(ArrayQueueADT.dequeue(q2));
        }

        
    }

}
