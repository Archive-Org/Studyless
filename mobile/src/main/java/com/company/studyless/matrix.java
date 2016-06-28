package com.company.studyless;

/**
 * Created by mac on 28/6/16.
 */
public class matrix {
    private int[][] Data = new int[5][4];

    public int[][] getData() {
        return Data;
    }

    public int[] getData(int row) {
        return Data[row];
    }

    public void setData(int add, int row, int colum) {
        this.Data[row][colum] += add;
    }
}
