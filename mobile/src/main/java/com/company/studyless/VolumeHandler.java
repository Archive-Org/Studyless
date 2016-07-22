package com.company.studyless;


import android.os.Vibrator;

public class VolumeHandler {
    public int volumePressedCount = 0;
    public long lastTime = 0L;
    int delay = 1000;
    public Long lastWorker = 0L;
    Vibrator vibrator;

    public int handleVolume(int points) {

        double diference = System.currentTimeMillis() - lastTime;
        if (diference <= delay) {
            volumePressedCount += points;
            lastTime = System.currentTimeMillis();
        } else {
            volumePressedCount = 1;
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

    public Long getLastWorker() {
        return lastWorker;
    }
}
// If no presses in 4 seconds vibrate