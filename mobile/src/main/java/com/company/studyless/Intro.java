package com.company.studyless;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2Fragment;

public class Intro extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.


        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntro2Fragment.newInstance(
                getResources().getString(R.string.first_slide_title), getResources().getString(R.string.first_slide_description),
                R.drawable.ic_arrow_forward_white_24px, Color.parseColor("#4CAF50")));

        addSlide(AppIntro2Fragment.newInstance(
                getResources().getString(R.string.second_slide_title), getResources().getString(R.string.second_slide_description),
                R.drawable.ic_arrow_forward_white_24px, Color.parseColor("#4CAF50")));

        addSlide(AppIntro2Fragment.newInstance(
                getResources().getString(R.string.third_slide_title), getResources().getString(R.string.third_slide_description),
                R.drawable.ic_arrow_forward_white_24px, Color.parseColor("#4CAF50")));

        // OPTIONAL METHODS
        // Override bar/separator color.
        //setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(true);
        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        setVibrate(true);
        setVibrateIntensity(300);
        setDoneText(getResources().getString(R.string.done));
        setSkipText(getResources().getString(R.string.skip));
    }

    private void loadMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        loadMainActivity();
        Toast.makeText(getApplicationContext(), "Skip", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        loadMainActivity();
    }
}