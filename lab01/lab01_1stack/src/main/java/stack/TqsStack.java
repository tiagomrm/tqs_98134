package stack;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TqsStack<T> {

    private final List<T> stack;
    private int maxSize;

    public TqsStack() {
        stack = new ArrayList<>();
        maxSize = 0;
    }

    public TqsStack(int maxSize) {
        stack = new ArrayList<>();
        this.maxSize = maxSize;
    }

    public void push(T e) {
        if (maxSize > 0 && stack.size() >= maxSize)
            throw new IllegalStateException();
        stack.add(e);
    }

    public T pop() {
        if (isEmpty())
            throw new NoSuchElementException();

        T element = stack.get(stack.size() - 1);
        stack.remove(stack.size() - 1);
        return element;
    }

    public T peek() {
        if (isEmpty())
            throw new NoSuchElementException();

        return stack.get(stack.size() - 1);
    }

    public int size() {
        return stack.size();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }
}
