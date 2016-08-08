/*
 *  Copyright (C) Alejandro Pacheco - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by  Alejandro Pacheco Bobillo <alepacheco99@gmail.com>
 *
 */

package com.company.studyless.Views;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.company.studyless.R;

public class Settings extends Activity {
    private static boolean showMatrix, showVolume, showNotification;
    private static SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        final CheckBox showMatrixCB = (CheckBox) findViewById(R.id.showMatrixCheckBox);
        final CheckBox showVolumeCB = (CheckBox) findViewById(R.id.showVolumeCheckBox);
        final CheckBox showNotificationCB = (CheckBox) findViewById(R.id.showNotificationCheckBox);
        final TextView matrixText = (TextView) findViewById(R.id.matrixText);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        showMatrix = prefs.getBoolean("showMatrix", false);
        showVolume = prefs.getBoolean("showVolume", false);


        if (showMatrix) {
            showMatrixCB.toggle();
        }
        if (showVolume) {
            showVolumeCB.toggle();
        }
        if (showNotification) {
            showNotificationCB.toggle();
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

    public void showNotificationListener(View v) {
        if (!showNotification) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("showNotification", Boolean.TRUE);
            edit.apply();
        } else {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("showNotification", Boolean.FALSE);
            edit.apply();
        }
    }

}
