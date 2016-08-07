/*
 *  Copyright (C) Alejandro Pacheco - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by  Alejandro Pacheco Bobillo <alepacheco99@gmail.com>
 *
 */

package com.company.studyless;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.company.studyless.Views.Intro;
import com.company.studyless.Views.Settings;
import com.company.studyless.fragments.BlancFragment;
import com.company.studyless.fragments.Chat;
import com.company.studyless.fragments.Info;
import com.company.studyless.fragments.Leet;
import com.company.studyless.fragments.News;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.jetbrains.annotations.Contract;

import java.lang.reflect.Field;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private LinearLayout questionsLayout, blackScreen;
    private Animation fadeIn, fadeOut;
    private RelativeLayout loadingLayout;
    private Matrix Matrix = new Matrix();
    private RadioGroup[] G = new RadioGroup[Matrix.questionsRows];
    private TextView[] result = new TextView[Matrix.questionsRows];
    private TextView matrixText, volumeCount;
    private EditText roomField;
    private DatabaseReference mDatabase;
    private Random random = new Random();
    private VolumeHandler volumeHandler = new VolumeHandler();
    private int room = random.nextInt(100000);
    private boolean showNotification, showMatrix;
    private boolean otherFragment = false;
    private int[] checkedButtons = {
            9999, 9999, 9999, 9999, 9999,
            9999, 9999, 9999, 9999, 9999,
            9999, 9999, 9999, 9999, 9999,
            9999, 9999, 9999, 9999, 9999};
    private int[] especialRooms = {1337, 2512, 1, 1234, 1000000};
    private SensorManager mSensorManager;
    private Sensor mProximity;
    private PowerManager.WakeLock wakeLock;
    private boolean currentFocus;
    private boolean isPaused;
    private Handler collapseNotificationHandler;
    private int lastRingMode;
    private AudioManager AudioManager;

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
        bindObjects();

        //Hide layout show
        questionsLayout.setVisibility(View.GONE);
        blackScreen.setVisibility(View.GONE);

        //Sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        //Prevent lock
        PowerManager powerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        wakeLock.acquire();


        //TODO implement inflater
        /*FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, new Question_list()).commit();*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //Change colors and startup
        String[] colorsArray = {"#6564DB", "#232ED1", "#DD0426", "#273043",
                "#AAA95A", "#414066", "#1B2D2A"};
        int randomColor = Color.parseColor(colorsArray[random.nextInt(colorsArray.length)]);
        int darkerColor = ColorUtils.blendARGB(randomColor, Color.BLACK, 0.2F);
        toolbar.setBackgroundColor(randomColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(darkerColor);
        }

        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        isPaused = false;
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
        isPaused = true;
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
                if (checkSpecialRoom(n)) {
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

                    }

                    if (fragment != null) {
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_layout, fragment).commit();
                    } else {
                        this.getFragmentManager().popBackStack();
                    }
                } else {

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


    @Contract(pure = true)
    private boolean checkSpecialRoom(int n) {
        for (int c : especialRooms) {
            if (c == n) {
                return true;
            }
        }
        return false;
    }

    private void bindObjects() {
        //TODO use getResId (faster)
        //Better way to this ugly stuff?
        for (int i = 0; i < Matrix.questionsRows; i++) {
            G[i] = (RadioGroup) findViewById(getResId("radioGroup" + (i + 1)));
            result[i] = (TextView) findViewById(getResId("resultado" + (i + 1)));
        }

        /*G[0] = (RadioGroup) findViewById(R.id.radioGroup1);
        G[1] = (RadioGroup) findViewById(R.id.radioGroup2);
        G[2] = (RadioGroup) findViewById(R.id.radioGroup3);
        G[3] = (RadioGroup) findViewById(R.id.radioGroup4);
        G[4] = (RadioGroup) findViewById(R.id.radioGroup5);
        G[5] = (RadioGroup) findViewById(R.id.radioGroup6);
        G[6] = (RadioGroup) findViewById(R.id.radioGroup7);
        G[7] = (RadioGroup) findViewById(R.id.radioGroup8);
        G[8] = (RadioGroup) findViewById(R.id.radioGroup9);
        G[9] = (RadioGroup) findViewById(R.id.radioGroup10);
        G[10] = (RadioGroup) findViewById(R.id.radioGroup11);
        G[11] = (RadioGroup) findViewById(R.id.radioGroup12);
        G[12] = (RadioGroup) findViewById(R.id.radioGroup13);
        G[13] = (RadioGroup) findViewById(R.id.radioGroup14);
        G[14] = (RadioGroup) findViewById(R.id.radioGroup15);
        G[15] = (RadioGroup) findViewById(R.id.radioGroup16);
        G[16] = (RadioGroup) findViewById(R.id.radioGroup17);
        G[17] = (RadioGroup) findViewById(R.id.radioGroup18);
        G[18] = (RadioGroup) findViewById(R.id.radioGroup19);
        G[19] = (RadioGroup) findViewById(R.id.radioGroup20);

        result[0] = (TextView) findViewById(R.id.resultado1);
        result[1] = (TextView) findViewById(R.id.resultado2);
        result[2] = (TextView) findViewById(R.id.resultado3);
        result[3] = (TextView) findViewById(R.id.resultado4);
        result[4] = (TextView) findViewById(R.id.resultado5);
        result[5] = (TextView) findViewById(R.id.resultado6);
        result[6] = (TextView) findViewById(R.id.resultado7);
        result[7] = (TextView) findViewById(R.id.resultado8);
        result[8] = (TextView) findViewById(R.id.resultado9);
        result[9] = (TextView) findViewById(R.id.resultado10);
        result[10] = (TextView) findViewById(R.id.resultado11);
        result[11] = (TextView) findViewById(R.id.resultado12);
        result[12] = (TextView) findViewById(R.id.resultado13);
        result[13] = (TextView) findViewById(R.id.resultado14);
        result[14] = (TextView) findViewById(R.id.resultado15);
        result[15] = (TextView) findViewById(R.id.resultado16);
        result[16] = (TextView) findViewById(R.id.resultado17);
        result[17] = (TextView) findViewById(R.id.resultado18);
        result[18] = (TextView) findViewById(R.id.resultado19);
        result[19] = (TextView) findViewById(R.id.resultado20);*/

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
                        matrixText.setText(Matrix.matrix2string(Matrix.getData(),
                                Matrix.questionsRows, 4));
                    }
                    int i = 0;
                    while (i < result.length) {
                        result[i].setText(betterMostVoted(i));
                        i++;
                    }

                    if (loadingLayout.getVisibility() != View.GONE) {
                        showQuestionsHideLoading();
                    }
                    if (showNotification) {
                        showNotification();
                    }


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

        //noinspection SimplifiableIfStatement
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
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            event.startTracking();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            event.startTracking();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK) {
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
            //AudioManager.setRingerMode(android.media.AudioManager.RINGER_MODE_SILENT);
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
            //volumeCount.setText(String.valueOf(volumeHandler.handleVolume(4)));
            //triggerThread();

            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    private void topButtonArray() {
        checkedButtons = new int[]{9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999,
                9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999};
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

    String betterMostVoted(int row) {
        if (Matrix.MostVoted(row).equals("?")) {
            return getString(R.string.tie);
        } else {
            return Matrix.MostVoted(row);
        }
    }

    private void showNotification() {

        Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher),
                getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_width),
                getResources()
                        .getDimensionPixelSize(android.R.dimen.notification_large_icon_height),
                true);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent,
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setContentTitle(mostVoted2String());
        builder.setContentText("By Studyless");
        //builder.setSubText("Some sub text");
        builder.setNumber(101);
        builder.setContentIntent(pendingIntent);
        //builder.setTicker("Fancy Notification");
        builder.setSmallIcon(R.drawable.ic_tik);
        builder.setLargeIcon(bm);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setOngoing(true);

        builder.setSmallIcon(android.R.color.transparent); //Tested and worked in API 14
        Notification notification = builder.build();
        NotificationManager notificationManger =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManger.notify(1, notification);
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

    private String mostVoted2String() {
        String response = "";
        for (int x = 0; x < Matrix.questionsRows; x++) {
            response += Matrix.MostVoted(x);
        }
        return response;
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
                if (event.values[0] == 0) { //if (event.sensor.getType() == Sensor.TYPE_PROXIMITY)
                    blackScreen.setVisibility(View.VISIBLE);
                    //setContentView(R.layout.black_screen);
                    questionsLayout.setVisibility(View.GONE);
                }
            } else {
                blackScreen.setVisibility(View.GONE);
                if (loadingLayout.getVisibility() != View.VISIBLE && !otherFragment) {
                    questionsLayout.setVisibility(View.VISIBLE);
                    blackScreen.setVisibility(View.GONE);
                    //setContentView(R.layout.activity_main);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
