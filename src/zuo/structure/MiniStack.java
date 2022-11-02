package zuo.structure;

import java.util.Deque;
import java.util.LinkedList;

class MinStack {
    //最小
    private Deque<Integer> stackData;
    private Deque<Integer> stackMini;

    public MinStack() {
        stackData = new LinkedList<>();
        stackMini = new LinkedList<>();
    }

    public void push(int val) {
        stackData.push(val);
        if (stackMini.isEmpty())
            stackMini.push(val);
        else
            stackMini.push(Math.min(stackMini.peek(),val));
    }

    public void pop() {
        stackData.pop();
        stackMini.pop();
    }

    public int top() {
        return stackData.peek();
    }

    public int getMin() {
        return stackMini.peek();
    }
}