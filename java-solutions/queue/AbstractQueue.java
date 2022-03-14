package queue;

import java.util.function.Predicate;
import java.util.function.Function;

public abstract class AbstractQueue implements Queue {
    protected int size;
    
    @Override
    public int size() {
        return size;
    }

    @Override
    public Boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void enqueue(Object element){
        assert element != null;
        enqueueImpl(element);
        size++;
    }
    protected abstract void enqueueImpl(Object element);

    @Override
    public Object element(){
        return elementImpl();
    }
    protected abstract Object elementImpl();

    @Override
    public Object dequeue() {
        Object result = element();
        dequeueImpl();
        size--;
        return result;
    }
    protected abstract void dequeueImpl();

    @Override
    public void clear() {
        clearImpl();
        size = 0;
    }
    protected abstract void clearImpl();

    protected abstract Queue createCopy();

    @Override
    public Queue filter(Predicate<Object> predicate) {
        Queue result = createCopy();
        for (int i = 0; i < size; i++) {
            Object temp = result.dequeue();
            if (predicate.test(temp)) {
                result.enqueue(temp);
            }
        }
        return result;
    }

    @Override
    public Queue map(Function<Object, Object> function) {
        Queue result = createCopy();
        for (int i = 0; i < size; i++) {
            result.enqueue(function.apply(result.dequeue()));
        }
        return result;
    }
}
