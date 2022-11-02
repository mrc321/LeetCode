package zuo.structure;

public class ArrayQueue {
    Integer[] nums; //数组
    int size; //当前队列中的元素的个数
    int front; //头指针
    int rear; //尾指针
    final int limit; //队列的最大长度

    public ArrayQueue(int limit) {
        this.limit = limit;
        this.nums = new Integer[limit];
        size = front = rear = 0;
    }

    public void push(int x) {
        if (size == nums.length) throw new RuntimeException("队列元素已满");
        nums[rear] = x;
        size++;
        rear = nextIndex(rear);
    }

    private int nextIndex(int index) {
        return index == limit ? 0 : index + 1; //循环队列
    }

    public int pop() {
        if (size == 0) throw new RuntimeException("队列中没有元素");
        int x = nums[front];
        front = nextIndex(front);
        size--;
        return x;
    }

    public int peek() {
        return nums[front];
    }

    public boolean empty() {
        return size == 0;
    }
}
