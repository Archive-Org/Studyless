/*
 *  Copyright (C) Alejandro Pacheco - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by  Alejandro Pacheco Bobillo <alepacheco99@gmail.com>
 *
 */

package com.company.studyless;

import java.util.ArrayList;

class Matrix {
    static int questionsRows = 20;
    private static int[][] Data = new int[questionsRows][4];

    int[][] getData() {
        return Data;
    }

    void SyncWDB(Object dbValue) {
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
            System.out.println("Exception parsing Matrix");
        }
    }

    char MostVoted(int row) {
        char a = '?';
        if (row > questionsRows) {
            return a;
        }
        if (Data[row][0] > Data[row][1] && Data[row][0] > Data[row][2] && Data[row][0] > Data[row][3]) {
            a = 'A';
        } else if (Data[row][1] > Data[row][0] && Data[row][1] > Data[row][2] && Data[row][1] > Data[row][3]) {
            a = 'B';
        } else if (Data[row][2] > Data[row][0] && Data[row][2] > Data[row][1] && Data[row][2] > Data[row][3]) {
            a = 'C';
        } else if (Data[row][3] > Data[row][0] && Data[row][3] > Data[row][1] && Data[row][3] > Data[row][2]) {
            a = 'D';
        }
        return a;

    }

    int[] getData(int row) {
        return Data[row];
    }

    void addOne(int row, int column) {
        Data[row][column] += 1;
    }

    void restOne(int row, int column) {
        Data[row][column] -= 1;
    }

    String matrix2string() {
        StringBuilder buf = new StringBuilder();
        for (int x = 0; x < questionsRows; x++) {
            buf.append("\n");
            for (int y = 0; y < 4; y++) {
                buf.append(Data[x][y]).append(" ");

            }
        }
        return buf.toString();
    }
}
