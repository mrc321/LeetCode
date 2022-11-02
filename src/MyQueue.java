import java.util.Deque;
import java.util.LinkedList;

class MyQueue {
    Deque<Integer> stackPush;
    Deque<Integer> stackPop;

    public MyQueue() {
        stackPush = new LinkedList<>();
        stackPop = new LinkedList<>();
    }

    public void push(int x) {
        stackPush.push(x);
    }

    public int pop() {
        pushToPop();
        return stackPop.pop();
    }

    public int peek() {
        pushToPop();
        return stackPop.peek();
    }

    public boolean empty() {
        return stackPush.isEmpty() && stackPop.isEmpty();
    }

    private void pushToPop(){
        if (stackPop.isEmpty()) {
            while (!stackPush.isEmpty()) {
                stackPop.push(stackPush.pop());
            }
        }
        if (stackPop.isEmpty()){
            throw new RuntimeException("queue is empty");
        }
    }
}