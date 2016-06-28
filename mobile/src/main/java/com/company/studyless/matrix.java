package com.company.studyless;

import java.util.ArrayList;

public class matrix {
    private int[][] Data = new int[5][4];

    public int[][] getData() {
        return Data;
    }

    public void SyncWDB(Object dbValue) {
        ArrayList level1 = (ArrayList) dbValue;
        int e = 0;
        int i = 0;
        while (e < 5) {
            ArrayList esto = (ArrayList) level1.get(e);
            while (i < 4) {
                Long esto2 = Long.parseLong(String.valueOf(esto.get(i)));
                int esto3 = esto2.intValue();
                Data[e][i] = esto3;

                i++;
            }
            i = 0;
            e++;
        }

    }

    public int[] getData(int row) {
        return Data[row];
    }

    public void addOne(int row, int column) {
        this.Data[row][column] += 1;
    }

    public void restOne(int row, int column) {
        this.Data[row][column] -= 1;
    }

    public String matrix2string(int[][] matrix2convert, int i, int e) {
        String converted = "";
        int ii = 0;
        int ee = 0;
        while (ii < i) {
            converted += "\n";
            while (ee < e) {
                converted += matrix2convert[ii][ee] + " ";
                ee++;


            }
            ee = 0;
            ii++;
        }
        return converted;
    }
}
