package queue;

import java.util.function.Predicate;
import java.util.function.Function;

// Model: q[0]..q[size - 1]
// Invariant: for i=0..(size-1): q[i] != null
public interface Queue {
    // Pred: element != null
    // Post: size' = size + 1, for i = 0..(size - 1): q'[i] = q[i], q'[size' - 1] = element
    void enqueue(Object element);

    // Pred: size > 0
    // Post: R = q[0], size' = size, for i = 0..(size - 1): q'[i] = q[i]
    Object element();

    // Pred: size > 0
    // Post: R = q[0], size' = size - 1, for i = 0..(size - 2): q'[i] = q[i + 1]
    Object dequeue();

    // Pred: True
    // Post: R = size, size' = size, for i = 0..(size - 1): q'[i] = q[i]
    int size();

    // Pred: True
    // Post: R = (size == 0), size' = size, for i = 0..(size - 1): q'[i] = q[i]
    Boolean isEmpty();

    // Pred: True
    // Post: for i = 0..(size - 1): q'[i] = null, size' = 0
    void clear();

    // Pred: True
    // Post: size' <= size, for i = 0..(size' - 1): predicate.test(q'[i]) = true, number_invers(q', q) = 0
    Queue filter(Predicate<Object> predicate);

    // Pred: True
    // Post: size' = size, for i = 0..(size' - 1): q'[i] = function.apply(q[i])
    Queue map(Function<Object, Object> function);
}
