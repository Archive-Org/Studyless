/*
 *  Copyright (C) Alejandro Pacheco - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by  Alejandro Pacheco Bobillo <alepacheco99@gmail.com>
 *
 */

package com.company.studyless;


import android.os.Vibrator;

class VolumeHandler {
    static final int delay = 1000;
    private static Vibrator vibrator;
    int volumePressedCount = 0;
    Long lastWorker = 0L;
    private long lastTime = 0L;

    int handleVolume(int points) {

        double difference = System.currentTimeMillis() - lastTime;
        if (difference <= delay) {
            volumePressedCount += points;
            lastTime = System.currentTimeMillis();
        } else {
            volumePressedCount = points;
            lastTime = System.currentTimeMillis();
        }
        return volumePressedCount;
    }

    Vibrator getVibrator() {
        return vibrator;
    }

    void setVibrator(Vibrator vb) {
        VolumeHandler.vibrator = vb;
    }

    void setLastWorker(Long l) {
        lastWorker = l;
    }
}
// If no presses in 4 seconds vibrate