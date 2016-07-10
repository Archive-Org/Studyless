package com.company.studyless;


public class VolumeHandler {
    public int volumePressedCount = 0;
    long lastTime = 0;
    int delay = 4000;

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

}
// If no presses in 4 seconds vibrate