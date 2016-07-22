package com.company.studyless;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class Intro extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Welcome have more free time… enjoy…
        addSlide(AppIntro2Fragment.newInstance(
                getResources().getString(R.string.first_slide_title),
                getResources().getString(R.string.first_slide_description),
                R.drawable.ic_smile,
                Color.parseColor("#2BC55E")));

        //Get the best response with your friedns
        addSlide(AppIntro2Fragment.newInstance(
                getResources().getString(R.string.second_slide_title),
                getResources().getString(R.string.second_slide_description),
                R.drawable.ic_thumbs_up_down,
                Color.parseColor("#DE6461")));

        //Share the questions you know and see what your friends think is the one
        addSlide(AppIntro2Fragment.newInstance(
                getResources().getString(R.string.third_slide_title),
                getResources().getString(R.string.third_slide_description),
                R.drawable.ic_tik,
                Color.parseColor("#B44E86")));

        //Be descret getings the answers via vibrations
        addSlide(AppIntro2Fragment.newInstance(
                getResources().getString(R.string.third_slide_title),
                getResources().getString(R.string.third_slide_description),
                R.drawable.ic_vibration,
                Color.parseColor("#B44E86")));

        //disclaimer
        addSlide(AppIntro2Fragment.newInstance(
                getResources().getString(R.string.third_slide_title),
                getResources().getString(R.string.third_slide_description),
                R.drawable.ic_important,
                Color.parseColor("#B44E86")));


        showSkipButton(true);
        setProgressButtonEnabled(true);
        setVibrate(false);
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
        ConfigureGoogleSingIn();
        loadMainActivity();
        //Toast.makeText(getApplicationContext(), "Skip", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        ConfigureGoogleSingIn();

        loadMainActivity();

    }

    void ConfigureGoogleSingIn() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }
}