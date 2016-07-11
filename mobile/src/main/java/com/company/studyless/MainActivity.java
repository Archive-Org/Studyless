package com.company.studyless;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RadioGroup G1, G2, G3, G4, G5, G6, G7, G8, G9, G10;
    TextView result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, matrixText, RoomTextView, volumecount;
    EditText roomField;
    DatabaseReference mDatabase;
    matrix matrix = new matrix();
    Random random = new Random();
    VolumeHandler volumeHandler = new VolumeHandler();
    int room = random.nextInt(1000) + 1;
    int[] checkedButtons = {9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999};
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bindObjects();
        RoomTextView.setText(String.valueOf(room));
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getDatabase();

        new VolumeChecherThreath().execute(1,1,1);

    }

    public void buttonClickListener(View v) {
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
                default:
                    break;

            }
            lessOneEntry(row, column);
        } else {
            plusOneEntry(row, column);
        }


        //String text = (1 + row) + "-" + (1 + column);
        //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

    }


    public void plusOneEntry(int row, int column) {
        int value = matrix.getData()[row][column] + 1;
        mDatabase.child("room_" + room).child(row + "/" + column + "").setValue(value);
    }

    public void lessOneEntry(int row, int column) {
        int value = matrix.getData()[row][column] - 1;
        mDatabase.child("room_" + room).child(row + "/" + column + "").setValue(value);
    }

    public void initializeRoom(int roomNumber) {
        int e = 0;
        int i = 0;
        while (e < 10) {
            while (i < 4) {
                mDatabase.child("room_" + roomNumber + "/" + e + "/" + i + "").setValue(0);
                i++;
            }
            i = 0;
            e++;

        }
    }

    public void changeRoom(View v) {
        int n = Integer.parseInt(roomField.getText().toString());
        if (room == n) {
            Toast.makeText(getApplicationContext(), "Ya en sala", Toast.LENGTH_SHORT).show();
        } else {
            room = n;
            mDatabase = FirebaseDatabase.getInstance().getReference();
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
            RoomTextView.setText("Sala: " + String.valueOf(room));
            checkedButtons = new int[]{9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999};
            getDatabase();
        }
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        roomField.setText("");
        roomField.setHint("Sala: " + String.valueOf(room));

    }

    public void bindObjects() {
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
        roomField = (EditText) findViewById(R.id.roomField);
        matrixText = (TextView) findViewById(R.id.matrixText);
        RoomTextView = (TextView) findViewById(R.id.RoomTextViex);
        volumecount = (TextView) findViewById(R.id.volumecount);

    }

    public void getDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("room_" + room).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    matrix.SyncWDB(dataSnapshot.getValue());
                    matrixText.setText(matrix.matrix2string(matrix.getData(), 10, 4));
                    result1.setText(matrix.MostVoted(0));
                    result2.setText(matrix.MostVoted(1));
                    result3.setText(matrix.MostVoted(2));
                    result4.setText(matrix.MostVoted(3));
                    result5.setText(matrix.MostVoted(4));
                    result6.setText(matrix.MostVoted(5));
                    result7.setText(matrix.MostVoted(6));
                    result8.setText(matrix.MostVoted(7));
                    result9.setText(matrix.MostVoted(8));
                    result10.setText(matrix.MostVoted(9));
                } else {
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
                    RoomTextView.setText("Sala: " + String.valueOf(room));
                    checkedButtons = new int[]{9999,
                            9999,
                            9999,
                            9999,
                            9999,
                            9999,
                            9999,
                            9999,
                            9999,
                            9999};
                    initializeRoom(room);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });


    }



    public boolean isChecked(int row, int colum) {
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


    /**
     * Side bar functions
     */
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
            setContentView(R.layout.settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_questions) {
            // Handle the camera action
        } else if (id == R.id.nav_info) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volumecount.setText(String.valueOf(volumeHandler.handleVolume(4)));
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

}