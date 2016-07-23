package com.company.studyless;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String[] colorsArray = {"#6564DB", "#232ED1", "#DD0426", "#273043", "#AAA95A", "#414066", "#CEFF1A", "#1B2D2A"};
    private RadioGroup G1, G2, G3, G4, G5, G6, G7, G8, G9, G10, G11, G12, G13, G14, G15, G16, G17,
            G18, G19, G20;
    private TextView result1, result2, result3, result4, result5, result6, result7, result8,
            result9, result10, result11, result12, result13, result14, result15, result16,
            result17, result18, result19, result20, matrixText, volumecount;
    private EditText roomField;
    private DatabaseReference mDatabase;
    private matrix matrix = new matrix();
    private Random random = new Random();
    private VolumeHandler volumeHandler = new VolumeHandler();
    private int room = random.nextInt(100000);
    private int[] checkedButtons = {
            9999, 9999, 9999, 9999, 9999,
            9999, 9999, 9999, 9999, 9999,
            9999, 9999, 9999, 9999, 9999,
            9999, 9999, 9999, 9999, 9999
    };
    private LinearLayout questionsLayout;
    private RelativeLayout loadingLayout, infoLayout;

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
        //Display activity main
        setContentView(R.layout.activity_main);
        //TODO implement inflator

        /*FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, new question_list()).commit();
*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        int randomColor = Color.parseColor(colorsArray[random.nextInt(colorsArray.length)]);
        int darkerColor = ColorUtils.blendARGB(randomColor, Color.parseColor("#000000"), 0.2F);

        toolbar.setBackgroundColor(randomColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(darkerColor);
        }

        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Bind views
        bindObjects();

        //Hide layout till db loaded
        questionsLayout.setVisibility(View.GONE);

        //Initialize database and Vibrations
        //RoomTextView.setText(String.valueOf(room));
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Vibrator vibratorService = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        volumeHandler.setVibrator(vibratorService);

        //Handle Show settings
        if (prefs.getBoolean("showMatrix", false)) {
            matrixText.setVisibility(View.VISIBLE);
        } else {
            matrixText.setVisibility(View.GONE);
        }
        if (prefs.getBoolean("showVolume", false)) {
            volumecount.setVisibility(View.VISIBLE);
        } else {
            volumecount.setVisibility(View.GONE);
        }


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        //Fetch data
        getDatabase();

    }

    //Handle all matrix buttons
    public void buttonClickListener(View v) {
        //Split buttons tags
        String tag = (String) v.getTag();
        String[] parts = tag.split(",");
        int row = Integer.parseInt(parts[0]);
        int column = Integer.parseInt(parts[1]);

        if (isChecked(row, column)) {
            switch (row) {
                case 0:
                    G1.clearCheck();
                    break;
                case 1:
                    G2.clearCheck();
                    break;
                case 2:
                    G3.clearCheck();
                    break;
                case 3:
                    G4.clearCheck();
                    break;
                case 4:
                    G5.clearCheck();
                    break;
                case 5:
                    G6.clearCheck();
                    break;
                case 6:
                    G7.clearCheck();
                    break;
                case 7:
                    G8.clearCheck();
                    break;
                case 8:
                    G9.clearCheck();
                    break;
                case 9:
                    G10.clearCheck();
                    break;
                case 10:
                    G11.clearCheck();
                    break;
                case 11:
                    G12.clearCheck();
                    break;
                case 12:
                    G13.clearCheck();
                    break;
                case 13:
                    G14.clearCheck();
                    break;
                case 14:
                    G15.clearCheck();
                    break;
                case 15:
                    G16.clearCheck();
                    break;
                case 16:
                    G17.clearCheck();
                    break;
                case 17:
                    G18.clearCheck();
                    break;
                case 18:
                    G19.clearCheck();
                    break;
                case 19:
                    G20.clearCheck();
                    break;
                default:
                    break;

            }
            lessOneEntry(row, column);
        } else {
            plusOneEntry(row, column);
        }
    }

    private void plusOneEntry(int row, int column) {
        int value = matrix.getData()[row][column] + 1;
        mDatabase.child("room_" + room).child(row + "/" + column + "").setValue(value);
    }

    private void lessOneEntry(int row, int column) {
        int value = matrix.getData()[row][column] - 1;
        mDatabase.child("room_" + room).child(row + "/" + column + "").setValue(value);
    }

    private void initializeRoom(int roomNumber) {
        int e = 0;
        int i = 0;
        while (e < matrix.questionsRows) {
            while (i < 4) {
                mDatabase.child("room_" + roomNumber + "/" + e + "/" + i + "").setValue(0);
                i++;
            }
            i = 0;
            e++;

        }
    }

    public void changeRoom(View v) {
        if (!roomField.getText().toString().isEmpty()) {
            int n = Integer.parseInt(roomField.getText().toString());

            if (room == n) {
                Toast.makeText(getApplicationContext(),
                        R.string.Already_in_room,
                        Toast.LENGTH_SHORT).show();

            } else {
                room = n;
                mDatabase = FirebaseDatabase.getInstance().getReference();
                clearSelection();
                topButtonArray();
                getDatabase();
            }
        }

        roomField.setText("");
        roomField.setHint(getString(R.string.Room_) + String.valueOf(room));

    }

    private void bindObjects() {
        //Better way to this ugly stuff?

        questionsLayout = (LinearLayout) findViewById(R.id.questionsLayout);
        loadingLayout = (RelativeLayout) findViewById(R.id.loadingLayout);
        G1 = (RadioGroup) findViewById(R.id.radioGroup1);
        G2 = (RadioGroup) findViewById(R.id.radioGroup2);
        G3 = (RadioGroup) findViewById(R.id.radioGroup3);
        G4 = (RadioGroup) findViewById(R.id.radioGroup4);
        G5 = (RadioGroup) findViewById(R.id.radioGroup5);
        G6 = (RadioGroup) findViewById(R.id.radioGroup6);
        G7 = (RadioGroup) findViewById(R.id.radioGroup7);
        G8 = (RadioGroup) findViewById(R.id.radioGroup8);
        G9 = (RadioGroup) findViewById(R.id.radioGroup9);
        G10 = (RadioGroup) findViewById(R.id.radioGroup10);
        G11 = (RadioGroup) findViewById(R.id.radioGroup11);
        G12 = (RadioGroup) findViewById(R.id.radioGroup12);
        G13 = (RadioGroup) findViewById(R.id.radioGroup13);
        G14 = (RadioGroup) findViewById(R.id.radioGroup14);
        G15 = (RadioGroup) findViewById(R.id.radioGroup15);
        G16 = (RadioGroup) findViewById(R.id.radioGroup16);
        G17 = (RadioGroup) findViewById(R.id.radioGroup17);
        G18 = (RadioGroup) findViewById(R.id.radioGroup18);
        G19 = (RadioGroup) findViewById(R.id.radioGroup19);
        G20 = (RadioGroup) findViewById(R.id.radioGroup20);

        result1 = (TextView) findViewById(R.id.resultado1);
        result2 = (TextView) findViewById(R.id.resultado2);
        result3 = (TextView) findViewById(R.id.resultado3);
        result4 = (TextView) findViewById(R.id.resultado4);
        result5 = (TextView) findViewById(R.id.resultado5);
        result6 = (TextView) findViewById(R.id.resultado6);
        result7 = (TextView) findViewById(R.id.resultado7);
        result8 = (TextView) findViewById(R.id.resultado8);
        result9 = (TextView) findViewById(R.id.resultado9);
        result10 = (TextView) findViewById(R.id.resultado10);
        result11 = (TextView) findViewById(R.id.resultado11);
        result12 = (TextView) findViewById(R.id.resultado12);
        result13 = (TextView) findViewById(R.id.resultado13);
        result14 = (TextView) findViewById(R.id.resultado14);
        result15 = (TextView) findViewById(R.id.resultado15);
        result16 = (TextView) findViewById(R.id.resultado16);
        result17 = (TextView) findViewById(R.id.resultado17);
        result18 = (TextView) findViewById(R.id.resultado18);
        result19 = (TextView) findViewById(R.id.resultado19);
        result20 = (TextView) findViewById(R.id.resultado20);

        roomField = (EditText) findViewById(R.id.roomField);
        matrixText = (TextView) findViewById(R.id.matrixText);
        //RoomTextView = (TextView) findViewById(R.id.RoomTextViex);
        volumecount = (TextView) findViewById(R.id.volumecount);
        infoLayout = (RelativeLayout) findViewById(R.id.infoLayout);


    }

    private void getDatabase() {
        mDatabase.child("room_" + room).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    matrix.SyncWDB(dataSnapshot.getValue());

                    matrixText.setText(matrix.matrix2string(matrix.getData(),
                            matrix.questionsRows,
                            4));

                    result1.setText(betterMostVoted(0));
                    result2.setText(betterMostVoted(1));
                    result3.setText(betterMostVoted(2));
                    result4.setText(betterMostVoted(3));
                    result5.setText(betterMostVoted(4));
                    result6.setText(betterMostVoted(5));
                    result7.setText(betterMostVoted(6));
                    result8.setText(betterMostVoted(7));
                    result9.setText(betterMostVoted(8));
                    result10.setText(betterMostVoted(9));
                    result11.setText(betterMostVoted(10));
                    result12.setText(betterMostVoted(11));
                    result13.setText(betterMostVoted(12));
                    result14.setText(betterMostVoted(13));
                    result15.setText(betterMostVoted(14));
                    result16.setText(betterMostVoted(15));
                    result17.setText(betterMostVoted(16));
                    result18.setText(betterMostVoted(17));
                    result19.setText(betterMostVoted(18));
                    result20.setText(betterMostVoted(19));

                    loadingLayout.setVisibility(View.GONE);
                    questionsLayout.setVisibility(View.VISIBLE);

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
            questionsLayout.setVisibility(View.VISIBLE);
            fragment = new BlancFragment();
        } else if (id == R.id.nav_info) {
            fragment = new Info();
            questionsLayout.setVisibility(View.GONE);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here")
                    .putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.share_body));

            startActivity(Intent.createChooser(sharingIntent, getString(R.string.Share_via)));

        } else if (id == R.id.nav_send) {

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
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volumecount.setText(String.valueOf(volumeHandler.handleVolume(1)));
            triggerThread();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            volumecount.setText(String.valueOf(volumeHandler.handleVolume(5)));
            triggerThread();

            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            //volumecount.setText(String.valueOf(volumeHandler.handleVolume(4)));
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
        G1.clearCheck();
        G2.clearCheck();
        G3.clearCheck();
        G4.clearCheck();
        G5.clearCheck();
        G6.clearCheck();
        G7.clearCheck();
        G8.clearCheck();
        G9.clearCheck();
        G10.clearCheck();
        G11.clearCheck();
        G12.clearCheck();
        G13.clearCheck();
        G14.clearCheck();
        G15.clearCheck();
        G16.clearCheck();
        G17.clearCheck();
        G18.clearCheck();
        G19.clearCheck();
        G20.clearCheck();
    }

    private void triggerThread() {
        VolumeThreadObject data = new VolumeThreadObject();
        data.setVh(volumeHandler);
        data.setMatrix(matrix);
        new VolumeCheckerThread().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
    }

    String betterMostVoted(int row) {
        if (matrix.MostVoted(row) == "???") {
            return getString(R.string.tie);
        } else {
            return matrix.MostVoted(row);
        }
    }


}
