package zuo.structure;

public class ArrayStack {
    Integer[] nums; //数组
    Integer size; //栈的大小
    int p; //指针，-1表示当前栈为空

    public ArrayStack(Integer size) {
        this.size = size;
        this.nums = new Integer[size];
        this.p = -1;
    }

    public void push(int x) {
        if (p + 1 == size) throw new RuntimeException("栈已经满了");
        nums[++p] = x;
    }

    public int pop() {
        if (p == -1) throw new RuntimeException("栈中没有元素");
        return nums[p--];
    }

    public int top() {
        if (p == -1) throw new RuntimeException("栈中没有元素");
        return nums[p];
    }

    public boolean empty() {
        return p == -1;
    }
}