/*
 *  Copyright (C) Alejandro Pacheco - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by  Alejandro Pacheco Bobillo <alepacheco99@gmail.com>
 *
 */

package com.company.studyless;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.*;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.company.studyless.Views.Intro;
import com.company.studyless.Views.Settings;
import com.company.studyless.fragments.*;
import com.google.firebase.database.*;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.lang.reflect.Field;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener {


    private static final RadioGroup[] G = new RadioGroup[com.company.studyless.Matrix.questionsRows];
    private static final TextView[] result = new TextView[com.company.studyless.Matrix.questionsRows];
    private static FirebaseRemoteConfig mFirebaseRemoteConfig;
    private static LinearLayout questionsLayout, blackScreen;
    private static Animation fadeIn, fadeOut;
    private static RelativeLayout loadingLayout;
    private static Matrix Matrix = new Matrix();
    private static TextView matrixText, volumeCount;
    private static EditText roomField;
    private static DatabaseReference mDatabase;
    private static Random random = new Random();
    private static VolumeHandler volumeHandler = new VolumeHandler();
    private static boolean showNotification, showMatrix;
    private static int[] checkedButtons = {
            9999, 9999, 9999, 9999, 9999,
            9999, 9999, 9999, 9999, 9999,
            9999, 9999, 9999, 9999, 9999,
            9999, 9999, 9999, 9999, 9999};
    private static SensorManager mSensorManager;
    private static Sensor mProximity;
    private static PowerManager.WakeLock wakeLock;
    private static int lastRingMode;
    private static AudioManager AudioManager;
    private static DrawerLayout drawer;
    private int room = random.nextInt(100000);
    private boolean otherFragment = false;

    private int getResId(String resName) {
        try {
            Field idField = R.id.class.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Load splash screen
        setContentView(R.layout.splash_screen);
        VideoView vV = (VideoView) findViewById(R.id.videoView);
        Uri path = Uri.parse("android.resource://com.company.studyless/"
                + R.raw.splash);
        vV.setVideoURI(path);
        vV.start();


        //Intro if 1st time app start
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean("Previously started", false);
        if (!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("Previously started", Boolean.TRUE).apply();
            Intent intent = new Intent(this, Intro.class);

            startActivity(intent);
        }


        fadeIn = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);
        //Display activity main
        setContentView(R.layout.activity_main);

        //Bind views
        loadingLayout = (RelativeLayout) findViewById(R.id.loadingLayout);
        loadingLayout.setVisibility(View.VISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        bindObjects();

        //Hide layout show
        questionsLayout.setVisibility(View.GONE);
        blackScreen.setVisibility(View.GONE);

        //Sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        //Prevent lock
        final PowerManager powerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        wakeLock.acquire();

        //Change colors at startup
        final String[] colorsArray = {"#6564DB", "#232ED1", "#DD0426", "#273043",
                "#AAA95A", "#414066", "#1B2D2A"};
        final int randomColor = Color.parseColor(colorsArray[random.nextInt(colorsArray.length)]);
        final int darkerColor = ColorUtils.blendARGB(randomColor, Color.BLACK, 0.2F);
        toolbar.setBackgroundColor(randomColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(darkerColor);
        }

        //Config toolbar
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Block drawer till DB loaded
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        //Initialize database and Vibrations
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        Vibrator vibratorService = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        volumeHandler.setVibrator(vibratorService);

        //Silence mode
        AudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        lastRingMode = AudioManager.getRingerMode();
        AudioManager.setRingerMode(android.media.AudioManager.RINGER_MODE_SILENT);
        AudioManager.setRingerMode(android.media.AudioManager.RINGER_MODE_VIBRATE);


        //Handle Show settings
        if (prefs.getBoolean("showMatrix", false)) {
            matrixText.setVisibility(View.VISIBLE);
        } else {
            matrixText.setVisibility(View.GONE);
        }
        if (prefs.getBoolean("showVolume", false)) {
            volumeCount.setVisibility(View.VISIBLE);
        } else {
            volumeCount.setVisibility(View.GONE);
        }
        showNotification = prefs.getBoolean("showNotification", false);
        showMatrix = prefs.getBoolean("showMatrix", false);


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        //Fetch data
        getDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Activity's been resumed
        wakeLock.acquire();
        AudioManager.setRingerMode(android.media.AudioManager.RINGER_MODE_SILENT);
        AudioManager.setRingerMode(android.media.AudioManager.RINGER_MODE_VIBRATE);
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        wakeLock.release();
        // Activity's been paused
        AudioManager.setRingerMode(lastRingMode);
        mSensorManager.unregisterListener(this);
    }

    //Handle all Matrix buttons
    public void buttonClickListener(View v) {
        hideKB();
        //Split buttons tags
        String tag = (String) v.getTag();
        String[] parts = tag.split(",");
        int row = Integer.parseInt(parts[0]);
        int column = Integer.parseInt(parts[1]);

        if (isChecked(row, column)) {
            G[row].clearCheck();
            lessOneEntry(row, column);
        } else {
            plusOneEntry(row, column);
        }
    }

    private void plusOneEntry(int row, int column) {
        mDatabase.child("Rooms/room_" + room).child(row + "/" + column + "")
                .setValue(Matrix.getData()[row][column] + 1);
    }

    private void lessOneEntry(int row, int column) {
        mDatabase.child("Rooms/room_" + room).child(row + "/" + column + "")
                .setValue(Matrix.getData()[row][column] - 1);
    }

    private void initializeRoom(int roomNumber) {
        new InitializeRoomThread().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, roomNumber);
    }

    public void changeRoom(View v) {
        if (!roomField.getText().toString().isEmpty()) {
            int n = Integer.parseInt(roomField.getText().toString());

            if (room == n) {
                Toast.makeText(getApplicationContext(),
                        R.string.Already_in_room,
                        Toast.LENGTH_SHORT).show();

            } else {
                Fragment fragment = null;
                switch (n) {
                    case 1337:
                        fragment = new Leet();
                        break;
                    case 1:
                        break;
                    case 2512:
                        break;
                    case 1000000:
                        break;
                    default:
                        break;
                }

                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, fragment).commit();
                } else {
                    this.getFragmentManager().popBackStack();
                    room = n;
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    clearSelection();
                    loadingLayout.setVisibility(View.VISIBLE);
                    getDatabase();
                }
                questionsLayout.setVisibility(View.GONE);
                topButtonArray();
                hideKB();
            }

        }
        roomField.setText("");
        roomField.setHint(getString(R.string.Room_) + String.valueOf(room));
    }

    private void bindObjects() {
        for (int i = 0; i < com.company.studyless.Matrix.questionsRows; i++) {
            G[i] = (RadioGroup) findViewById(getResId("radioGroup" + (i + 1)));
            result[i] = (TextView) findViewById(getResId("resultado" + (i + 1)));
        }
        roomField = (EditText) findViewById(R.id.roomField);
        matrixText = (TextView) findViewById(R.id.matrixText);
        volumeCount = (TextView) findViewById(R.id.volumecount);
        questionsLayout = (LinearLayout) findViewById(R.id.questionsLayout);
        blackScreen = (LinearLayout) findViewById(R.id.blackScreen);
    }

    private void getDatabase() {
        mDatabase.child("Rooms/room_" + room).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Matrix.SyncWDB(dataSnapshot.getValue());

                    if (showMatrix) {
                        matrixText.setText(Matrix.matrix2string());
                    }
                    for (int i = 0; i < result.length; i++) {
                        result[i].setText(betterMostVoted(i));
                    }

                    if (loadingLayout.getVisibility() != View.GONE) {
                        showQuestionsHideLoading();
                    }
                    if (showNotification) {
                        showNotification();
                    }
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                } else {
                    clearSelection();
                    roomField.setHint(getString(R.string.Room_) + String.valueOf(room));
                    topButtonArray();
                    initializeRoom(room);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private boolean isChecked(int row, int colum) {
        if (checkedButtons[row] == colum) {
            checkedButtons[row] = 9999;
            return true;
        } else {
            if (checkedButtons[row] != 9999) {
                lessOneEntry(row, checkedButtons[row]);
            }
            checkedButtons[row] = colum;
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_show_intro) {
            Intent intent = new Intent(this, Intro.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_questions) {
            otherFragment = false;
            questionsLayout.setVisibility(View.VISIBLE);
            fragment = new BlancFragment();
        } else if (id == R.id.nav_info) {
            otherFragment = true;
            fragment = new Info();
            questionsLayout.setVisibility(View.GONE);
            hideKB();
        } else if (id == R.id.nav_news) {
            fragment = new News();
            otherFragment = true;
            questionsLayout.setVisibility(View.GONE);
            hideKB();
        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.Share_subject))
                    .putExtra(Intent.EXTRA_TEXT, getString(R.string.share_body));
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.Share_via)));
        } else if (id == R.id.nav_chat) {
            fragment = new Chat();
            otherFragment = true;
            questionsLayout.setVisibility(View.GONE);
            hideKB();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, fragment).commit();
        } else {
            this.getFragmentManager().popBackStack();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_HEADSETHOOK) {
            event.startTracking();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volumeCount.setText(String.valueOf(volumeHandler.handleVolume(1)));
            triggerThread();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            volumeCount.setText(String.valueOf(volumeHandler.handleVolume(5)));
            triggerThread();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK) {
            volumeCount.setText(String.valueOf(volumeHandler.handleVolume(1)));
            triggerThread();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    private void topButtonArray() {
        checkedButtons = new int[]{9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999,
                9999, 9999, 9999, 9999, 9999, 9999};
    }

    private void clearSelection() {
        for (RadioGroup g : G) {
            g.clearCheck();
        }
    }

    private void triggerThread() {
        VolumeThreadObject data = new VolumeThreadObject();
        data.setVh(volumeHandler);
        data.setMatrix(Matrix);
        new VolumeCheckerThread().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
    }

    private String betterMostVoted(int row) {
        if (Matrix.MostVoted(row) == '?') {
            return getString(R.string.tie);
        } else {
            return String.valueOf(Matrix.MostVoted(row));
        }
    }

    private void showNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_nav_icon)
                        .setContentTitle(mostVoted2String())
                        .setContentText(getString(R.string.by_studyless))
                        .setPriority(Notification.PRIORITY_MAX)
                        .setSmallIcon(R.drawable.ic_tik);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void hideKB() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        try {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @NonNull
    private String mostVoted2String() {
        StringBuilder buf = new StringBuilder();
        for (int x = 0; x < com.company.studyless.Matrix.questionsRows; x++) {
            buf.append(Matrix.MostVoted(x));
        }
        return buf.toString();
    }

    private void showQuestionsHideLoading() {
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loadingLayout.setVisibility(View.GONE);
            }
        });
        loadingLayout.startAnimation(fadeOut);
        questionsLayout.setVisibility(View.VISIBLE);
        questionsLayout.startAnimation(fadeIn);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (questionsLayout.getVisibility() == View.VISIBLE) {
                if (event.values[0] == 0) {
                    blackScreen.setVisibility(View.VISIBLE);
                    //setContentView(R.layout.black_screen);
                    questionsLayout.setVisibility(View.GONE);
                    fullScreen(true);
                }
            } else {
                blackScreen.setVisibility(View.GONE);
                if (loadingLayout.getVisibility() != View.VISIBLE && !otherFragment) {
                    questionsLayout.setVisibility(View.VISIBLE);
                    blackScreen.setVisibility(View.GONE);
                    fullScreen(false);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void fullScreen(boolean x) {
        if (x) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                requestWindowFeature(Window.getDefaultFeatures(this));
            }
            getWindow().setFlags(WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW,
                    WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW);
        }
    }
}
