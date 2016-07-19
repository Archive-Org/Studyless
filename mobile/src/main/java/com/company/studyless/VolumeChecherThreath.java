package com.company.studyless;

import android.os.AsyncTask;


public class VolumeChecherThreath extends AsyncTask<VolumeHandler, Void, VolumeHandler> {
    Long thisworker = 0L;
    boolean crash = false;

    @Override
    protected VolumeHandler doInBackground(final VolumeHandler... params) {
        try {
            VolumeHandler vh = params[0];
            thisworker = System.currentTimeMillis();
            vh.setLastWorker(thisworker);

            Thread.sleep(vh.delay);
            if (thisworker != vh.lastWorker) {
                crash = true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return params[0];
    }

    @Override
    protected void onPostExecute(VolumeHandler vh) {
        if (vh.lastWorker.equals(thisworker) && !crash) {
            vh.getVibrator().vibrate(500);
        }
    }

}


