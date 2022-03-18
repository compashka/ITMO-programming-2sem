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

    // :NOTE: Неформально
    /* Pred: True
       Post: R = q', size' <= size
         for all i < j: 0..(size' - 1) exist k < l: 0..(size - 1): q'[i] = q[k] && q'[j] = q[l] &&
         for all a < b (pred.test(q[a]) && pred.test(q[b]) = true): 0..(size - 1) exist c < d: 0..(size' - 1):
                                                                                q[a] = q'[c] && q[b] = q'[d] &&
         for all f: 0..(size' - 1) pred.test(q'[f]) = true                                 */
    Queue filter(Predicate<Object> predicate);

    // Pred: True
    // Post: R = q', size' = size, for i = 0..(size' - 1): q'[i] = function.apply(q[i])
    Queue map(Function<Object, Object> function);
}
