/*
 *  Copyright (C) Alejandro Pacheco - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by  Alejandro Pacheco Bobillo <alepacheco99@gmail.com>
 *
 */

package com.company.studyless;

import android.os.AsyncTask;


class VolumeCheckerThread extends AsyncTask<VolumeThreadObject, Void, Void> {
    private Long thisWorker = 0L;

    @Override
    protected Void doInBackground(final VolumeThreadObject... params) {
        VolumeHandler vh = params[0].getVh();
        try {

            thisWorker = System.currentTimeMillis();
            vh.setLastWorker(thisWorker);

            Thread.sleep(vh.delay);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (vh.lastWorker.equals(thisWorker)) {
            int vibrations;
            switch (params[0].getMatrix().MostVoted(vh.volumePressedCount - 1)) {
                case "A":
                    vibrations = 1;
                    break;
                case "B":
                    vibrations = 2;
                    break;
                case "C":
                    vibrations = 3;
                    break;
                case "D":
                    vibrations = 4;
                    break;
                default:
                    vibrations = 0;
                    break;
            }
            try {
                int x = 0;
                while (x < vibrations) {
                    x++;
                    vh.getVibrator().vibrate(200);
                    Thread.sleep(500);
                }
                if (vibrations == 0) {
                    vh.getVibrator().vibrate(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}


