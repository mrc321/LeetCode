package leetcode;

public class Leetcode_48 {
    public void rotate(int[][] matrix) {
        //代表左上角的横纵坐标
        int lx = 0;
        int ly = 0;
        //代表右下角的横纵坐标
        int rx = matrix.length - 1;
        int ry = matrix[0].length - 1;
        while (lx < rx) {
            //旋转一圈，然后再向内圈缩
            rotateCircle(matrix, lx++, ly++, rx--, ry--);
        }
    }

    //旋转一圈
    private void rotateCircle(int[][] matrix, int lx, int ly, int rx, int ry) {
        for (int i = 0; i < ry - ly; i++) {
            int tmp = matrix[lx][ly + i];
            matrix[lx][ly + i] = matrix[rx - i][ly];
            matrix[rx - i][ly] = matrix[rx][ry - i];
            matrix[rx][ry - i] = matrix[lx + i][ry];
            matrix[lx + i][ry] = tmp;
        }
    }
}
