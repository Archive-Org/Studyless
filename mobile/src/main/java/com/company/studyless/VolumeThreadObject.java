/*
 *  Copyright (C) Alejandro Pacheco - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by  Alejandro Pacheco Bobillo <alepacheco99@gmail.com>
 *
 */

package com.company.studyless;


class VolumeThreadObject {
    private matrix Matrix;
    private VolumeHandler vh;

    matrix getMatrix() {
        return Matrix;
    }

    void setMatrix(matrix mtrx) {
        Matrix = mtrx;
    }

    VolumeHandler getVh() {
        return vh;
    }

    void setVh(VolumeHandler vh) {
        this.vh = vh;
    }
}
