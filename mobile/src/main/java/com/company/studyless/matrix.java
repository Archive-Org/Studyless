package com.company.studyless;

import java.util.ArrayList;

public class matrix {
    public int questionsRows = 10;
    private int[][] Data = new int[questionsRows][4];

    public int[][] getData() {
        return Data;
    }

    public void SyncWDB(Object dbValue) {
        try {
            ArrayList level1 = (ArrayList) dbValue;
            int e = 0;
            int i = 0;
            while (e < questionsRows) {
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
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Exeption parsing matrix");
        }
    }

    public String MostVoted(int row) {
        String prefered = "Empate";
        if (Data[row][0] > Data[row][1] && Data[row][0] > Data[row][2] && Data[row][0] > Data[row][3]) {
            prefered = "A";
        } else if (Data[row][1] > Data[row][0] && Data[row][1] > Data[row][2] && Data[row][1] > Data[row][3]) {
            prefered = "B";
        } else if (Data[row][2] > Data[row][0] && Data[row][2] > Data[row][1] && Data[row][2] > Data[row][3]) {
            prefered = "C";
        } else if (Data[row][3] > Data[row][0] && Data[row][3] > Data[row][1] && Data[row][3] > Data[row][2]) {
            prefered = "D";
        }
        return prefered;

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
