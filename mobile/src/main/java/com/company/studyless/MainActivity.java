package com.company.studyless;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    RadioGroup G1, G2, G3, G4, G5;
    TextView resultado1, resultado2, resultado3, resultado4, resultado5;
    DatabaseReference mDatabase;
    matrix matrix = new matrix();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions);
        G1 = (RadioGroup) findViewById(R.id.radioGroup1);
        resultado1 = (TextView) findViewById(R.id.resultado1);
        resultado2 = (TextView) findViewById(R.id.resultado2);
        resultado3 = (TextView) findViewById(R.id.resultado3);
        resultado4 = (TextView) findViewById(R.id.resultado4);
        resultado5 = (TextView) findViewById(R.id.resultado5);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Read from Database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void buttonclicklistener(View v) throws JSONException {
        String text = "null";
        switch (v.getId()) {
            case R.id.rb1a:
                matrix.addOne(0, 0);
                text = "1a";
                break;
            case R.id.rb1b:
                matrix.addOne(0, 1);
                text = "1b";
                break;
            case R.id.rb1c:
                matrix.addOne(0, 2);
                text = "1c";
                break;
            case R.id.rb1d:
                matrix.addOne(0, 3);
                text = "1d";
                break;
            case R.id.rb2a:
                matrix.addOne(1, 0);
                text = "2a";
                break;
            case R.id.rb2b:
                matrix.addOne(1, 1);
                text = "2b";
                break;
            case R.id.rb2c:
                matrix.addOne(1, 2);
                text = "2c";
                break;
            case R.id.rb2d:
                matrix.addOne(1, 3);
                text = "2d";
                break;
            case R.id.rb3a:
                matrix.addOne(2, 0);
                text = "3a";
                break;
            case R.id.rb3b:
                matrix.addOne(2, 1);
                text = "3b";
                break;
            case R.id.rb3c:
                matrix.addOne(2, 2);
                text = "3c";
                break;
            case R.id.rb3d:
                matrix.addOne(2, 3);
                text = "3d";
                break;
            case R.id.rb4a:
                matrix.addOne(3, 0);
                text = "4a";
                break;
            case R.id.rb4b:
                matrix.addOne(3, 1);
                text = "4b";
                break;
            case R.id.rb4c:
                matrix.addOne(3, 2);
                text = "4c";
                break;
            case R.id.rb4d:
                matrix.addOne(3, 3);
                text = "4d";
                break;
            case R.id.rb5a:
                matrix.addOne(4, 0);
                text = "5a";
                break;
            case R.id.rb5b:
                matrix.addOne(4, 1);
                text = "5b";
                break;
            case R.id.rb5c:
                matrix.addOne(4, 2);
                text = "5c";
                break;
            case R.id.rb5d:
                matrix.addOne(4, 3);
                text = "5d";
                break;
            default:
                break;

        }
        updateRows(4353);
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();



    }

    public void updateRows(int room) {
        mDatabase.child("room_" + room + "/0").setValue(Arrays.toString(matrix.getData(0)));
        mDatabase.child("room_" + room + "/1").setValue(Arrays.toString(matrix.getData(1)));
        mDatabase.child("room_" + room + "/2").setValue(Arrays.toString(matrix.getData(2)));
        mDatabase.child("room_" + room + "/3").setValue(Arrays.toString(matrix.getData(3)));
        mDatabase.child("room_" + room + "/4").setValue(Arrays.toString(matrix.getData(4)));

    }
}
