package zuo.class2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class HeapGreater<T> {
    private final ArrayList<T> heap;
    private final HashMap<T, Integer> indexMap;
    private int heapSize;
    private final Comparator<? super T> comp;

    public HeapGreater(Comparator<? super T> c) {
        heap = new ArrayList<>();
        indexMap = new HashMap<>();
        heapSize = 0;
        comp = c;
    }

    /**
     * 判断堆是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return heapSize == 0;
    }

    /**
     * @return 堆中此时元素个数
     */
    public int size() {
        return heapSize;
    }

    /**
     * 堆中此时是否包含指定元素
     *
     * @param obj
     * @return
     */
    public boolean contains(T obj) {
        return indexMap.containsKey(obj);
    }

    /**
     * @return 堆顶元素
     */
    public T peek() {
        return heap.get(0);
    }

    /**
     * 向堆中插入元素
     *
     * @param obj
     */
    public void push(T obj) {
        heap.add(obj);
        indexMap.put(obj, heapSize);
        adjustUp(heapSize++);
    }

    /**
     * @return 弹出堆顶元素
     */
    public T pop() {
        T ans = heap.get(0);
        swap(0, heapSize - 1);
        indexMap.remove(ans);
        heap.remove(--heapSize);
        adjustDown(0);
        return ans;
    }

    /**
     * 删除指定元素
     * @param obj
     */
    public void remove(T obj) {
        //取出要删除的元素的索引
        int targetIndex = indexMap.get(obj);
        //删除元素与最后一个元素进行交换
        swap(targetIndex, heapSize - 1);
        //在反向索引表中删除该元素
        indexMap.remove(obj);
        //在堆中删除该元素
        heap.remove(--heapSize);
        //避免原本要删除的元素就是原本堆中最后一个，防止越界
        if (targetIndex < heapSize) {
            resign(heap.get(targetIndex));
        }
    }

    /**
     * 从指定元素开始做堆调整
     */
    public void resign(T obj){
        int index = indexMap.get(obj);
        adjustUp(index);
        adjustDown(index);
    }

    /**
     * @return 堆中所有元素
     */
    public List<T> getAllElements() {
        return new ArrayList<>(heap);
    }

    /**
     * 向下对堆进行调整
     *
     * @param index
     */
    private void adjustDown(int index) {
        int leftChild = index * 2 + 1;
        while (leftChild < heapSize) {
            int rightChild = leftChild + 1;
            //TODO 这个compare到底怎么用
            int largestIndex = rightChild < heapSize && comp.compare(heap.get(rightChild), heap.get(leftChild)) < 0 ? rightChild : leftChild;
            largestIndex = comp.compare(heap.get(index), heap.get(largestIndex)) < 0 ? index : largestIndex;
            if (largestIndex == index) {
                break;
            }
            swap(index, largestIndex);
            index = largestIndex;
            leftChild = index * 2 + 1;
        }
    }

    /**
     * 向上对堆进行调整
     *
     * @param index
     */
    private void adjustUp(int index) {
        while (comp.compare(heap.get(index), heap.get((index - 1) / 2)) < 0) {
            swap(index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    private void swap(int i, int j) {
        T a = heap.get(i);
        T b = heap.get(j);
        heap.set(j, a);
        heap.set(i, b);
        indexMap.put(a, j);
        indexMap.put(b, i);
    }
}
