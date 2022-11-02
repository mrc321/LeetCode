package leetcode;

import java.util.ArrayList;
import java.util.List;

public class Leetcode_51_X {
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> solveList = new ArrayList<>();
        String[] option = new String[n];
        for (int i = 0; i < n; i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < n; j++) {
                if (j == i) {
                    builder.append('Q');
                } else {
                    builder.append('.');
                }
            }
            option[i] = builder.toString();
        }
        int[] record = new int[n];
        process(solveList, record, 0, option);
        return solveList;
    }

    private int process(List<List<String>> solveList, int[] record, int row, String[] option) {
        if (row == record.length) {
            return 1;
        }
        StringBuilder builder = new StringBuilder();
        for (int col = 0; col < record.length; col++) {
            List<String> solve = new ArrayList<>();
            if (isValid(record, row, col)) {
                builder.append("Q");
            } else {
                builder.append(".");
            }
        }
        return 0;
    }

    //判断是否有效
    private boolean isValid(int[] record, int row, int col) {
        for (int i = 0; i < row; i++) {
            if (record[i] == col || Math.abs(row - i) == Math.abs(col - record[i])) {
                return false;
            }
        }
        return true;
    }


}
