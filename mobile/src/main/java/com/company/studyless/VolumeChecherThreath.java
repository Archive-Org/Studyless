package com.company.studyless;

import android.os.AsyncTask;


public class VolumeChecherThreath extends AsyncTask<Integer, Integer, Integer> {

    @Override
    protected Integer doInBackground(Integer... params) {
        int i = 0;
        while (i < 100) {
            i++;
            System.out.println("Async Task running....");
        }
        //Toast.makeText(this.Activity.this, "Hello", Toast.LENGTH_SHORT).show();
        return null;
    }
}

