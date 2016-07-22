/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.company.studyless;


class VolumeThreadObject {
    private matrix Matrix;
    private VolumeHandler vh;

    void setMatrix(matrix mtrx) {
        Matrix = mtrx;
    }

    void setVh(VolumeHandler vh) {
        this.vh = vh;
    }

    matrix getMatrix() {
        return Matrix;
    }

    VolumeHandler getVh() {
        return vh;
    }
}
