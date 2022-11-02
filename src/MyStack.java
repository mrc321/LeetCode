import java.util.LinkedList;
import java.util.Queue;

class MyStack {
    Queue<Integer> queue1;
    Queue<Integer> queue2;
    public MyStack() {
        queue1 = new LinkedList<>();
        queue2 = new LinkedList<>();
    }

    public void push(int x) {
        if (!queue2.isEmpty())
            queue1.offer(queue2.remove());
        queue1.offer(x);
    }

    public int pop() {
        if (queue2.isEmpty())
            oneToTwo();
        return queue2.remove();
    }

    public int top() {
        if (queue2.isEmpty())
            oneToTwo();
        return queue2.peek();
    }

    public boolean empty() {
        return queue1.isEmpty() && queue2.isEmpty();
    }

    private void oneToTwo(){
        if (queue1.isEmpty())
            throw new RuntimeException("栈为空");
        while (queue1.size() > 1){
            queue2.offer(queue1.remove());
        }
        Queue<Integer> tmp = queue2;
        queue2 = queue1;
        queue1 = tmp;
    }
}