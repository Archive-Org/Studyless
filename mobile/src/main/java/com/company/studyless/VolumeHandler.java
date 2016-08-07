/*
 *  Copyright (C) Alejandro Pacheco - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by  Alejandro Pacheco Bobillo <alepacheco99@gmail.com>
 *
 */

package com.company.studyless;


import android.os.Vibrator;

public class VolumeHandler {
    public int volumePressedCount = 0;
    public long lastTime = 0L;
    public Long lastWorker = 0L;
    int delay = 1000;
    Vibrator vibrator;

    public int handleVolume(int points) {

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

    public Vibrator getVibrator() {
        return vibrator;
    }

    public void setVibrator(Vibrator vb) {
        vibrator = vb;
    }

    public void setLastWorker(Long lastWorker) {
        this.lastWorker = lastWorker;
    }
}
// If no presses in 4 seconds vibrate