/*
 *  Copyright (C) Alejandro Pacheco - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by  Alejandro Pacheco Bobillo <alepacheco99@gmail.com>
 *
 */

package com.company.studyless;


class VolumeThreadObject {
    private com.company.studyless.Matrix Matrix;
    private VolumeHandler vh;

    com.company.studyless.Matrix getMatrix() {
        return Matrix;
    }

    void setMatrix(com.company.studyless.Matrix mtrx) {
        Matrix = mtrx;
    }

    VolumeHandler getVh() {
        return vh;
    }

    void setVh(VolumeHandler vh) {
        this.vh = vh;
    }
}
