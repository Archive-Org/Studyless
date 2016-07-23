/*
 *  Copyright (C) Alejandro Pacheco - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by  Alejandro Pacheco Bobillo <alepacheco99@gmail.com>
 *
 */

package com.company.studyless;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class Settings extends Activity {
    CheckBox showMatrixCB, showVolumeCB;
    TextView matrixText;
    boolean showMatrix, showVolume;
    SharedPreferences prefs;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        showMatrixCB = (CheckBox) findViewById(R.id.showMatrixCheckBox);
        showVolumeCB = (CheckBox) findViewById(R.id.showVolumeCheckBox);
        matrixText = (TextView) findViewById(R.id.matrixText);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        showMatrix = prefs.getBoolean("showMatrix", false);
        showVolume = prefs.getBoolean("showVolume", false);


        if (showMatrix) {
            showMatrixCB.toggle();
        }
        if (showVolume) {
            showVolumeCB.toggle();
        }
    }

    public void showMatrixListener(View v) {
        if (!showMatrix) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("showMatrix", Boolean.TRUE);
            edit.apply();
        } else {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("showMatrix", Boolean.FALSE);
            edit.apply();
        }

    }

    public void showVolumeListener(View v) {
        if (!showVolume) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("showVolume", Boolean.TRUE);
            edit.apply();
        } else {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("showVolume", Boolean.FALSE);
            edit.apply();
        }
    }

}
