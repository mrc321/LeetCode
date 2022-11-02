package leetcode;

public class Leetcode_215 {
    public static void main(String[] args) {

        int[] nums = {3,2,3,1,2,4,5,5,6};
        System.out.println(new Leetcode_215().findKthLargest2(nums, 4));
    }

    //这个需要从大到小排序
    public int findKthLargest(int[] nums, int k) {
        //因为要与下标进行比较，所以传入时 k 要减一
        return process(nums, 0, nums.length - 1, k - 1);
    }

    public int findKthLargest2(int[] nums, int k) {
        int L = 0;
        int R = nums.length - 1;
        k--;
        int pivot = 0;
        while (L < R) {
            pivot = nums[(int) (L + Math.random() * (R - L + 1))];
            int[] range = partition(nums, L, R, pivot);
            if (k < range[0]) {
                R = range[0] - 1;
            } else if (range[1] < k) {
                L = range[1] + 1;
            } else {
                return pivot;
            }
        }
        //运行到这里的情况是 L == R ,并且此时nums[L]就是第K大的值
        return nums[L];
    }

    private int process(int[] nums, int L, int R, int k) {
        if (L == R) {
            return nums[L];
        }
        //随机挑选一个标杆值
        int pivot = nums[(int) (L + Math.random() * (R - L + 1))];
        //做一次partition
        int[] range = partition(nums, L, R, pivot);
        //如果k在这个范围内，则直接返回
        if (range[0] <= k && k <= range[1]) {
            return pivot;
        } else if (k < range[0]) {
            return process(nums, L, range[0] - 1, k);
        } else {
            return process(nums, range[1] + 1, R, k);
        }
    }

    private int[] partition(int[] nums, int l, int r, int pivot) {
        int less = l - 1;
        int more = r + 1;
        int cur = l;
        while (cur < more) {
            if (nums[cur] > pivot) {
                swap(nums, ++less, cur++);
            } else if (nums[cur] < pivot) {
                swap(nums, --more, cur);
            } else {
                cur++;
            }
        }
        return new int[]{less + 1, more - 1};
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}
