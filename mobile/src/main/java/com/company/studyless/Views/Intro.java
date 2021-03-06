/*
 *  Copyright (C) Alejandro Pacheco - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by  Alejandro Pacheco Bobillo <alepacheco99@gmail.com>
 *
 */

package com.company.studyless.Views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.company.studyless.MainActivity;
import com.company.studyless.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2Fragment;

public class Intro extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Welcome have more free time… enjoy…
        addSlide(AppIntro2Fragment.newInstance(
                getResources().getString(R.string.first_slide_title),
                getResources().getString(R.string.first_slide_description),
                R.drawable.ic_smile,
                Color.parseColor("#70D6FF")));


        addSlide(AppIntro2Fragment.newInstance(
                getResources().getString(R.string.second_slide_title),
                getResources().getString(R.string.second_slide_description),
                R.drawable.ic_thumbs_up_down,
                Color.parseColor("#FF70A6")));

        //Share the questions you know and see what your friends think is the one
        addSlide(AppIntro2Fragment.newInstance(
                getResources().getString(R.string.third_slide_title),
                getResources().getString(R.string.third_slide_description),
                R.drawable.ic_tik,
                Color.parseColor("#FF9770")));

        addSlide(AppIntro2Fragment.newInstance(
                getResources().getString(R.string.forth_slide_title),
                getResources().getString(R.string.forth_slide_description),
                R.drawable.ic_vibration,
                Color.parseColor("#FFD670")));


        addSlide(AppIntro2Fragment.newInstance(
                getResources().getString(R.string.fifth_slide_title),
                getResources().getString(R.string.fifth_slide_description),
                R.drawable.ic_important,
                Color.parseColor("#232ED1")));

        showSkipButton(true);
        setProgressButtonEnabled(true);
        setVibrate(false);
        setDoneText(getResources().getString(R.string.done));
        setSkipText(getResources().getString(R.string.skip));
    }

    private void loadMainActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        loadMainActivity();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        loadMainActivity();
    }

}