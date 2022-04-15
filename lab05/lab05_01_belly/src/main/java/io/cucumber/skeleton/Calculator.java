package io.cucumber.skeleton;

import java.util.Stack;

public class Calculator {
    
    private final Stack<Integer> stack = new Stack<>();

    public void push(int arg) {
        stack.push(arg);
    }

    public void push(String s) {
        int result;
        if (stack.size() < 2)
            throw new IllegalArgumentException();

        switch (s) {
            case "-":
                result = - stack.pop() + stack.pop();
                break;

            case "+":
                result = stack.pop() + stack.pop();
                break;

            default:
                result = 0;
        }
        stack.push(result);
    }

    public int value() {
        return stack.peek();
    }

    public void clear() {
        stack.clear();
    }
}
