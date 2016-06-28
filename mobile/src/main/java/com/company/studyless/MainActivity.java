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

    public void buttonclicklistener1(View v) throws JSONException {
        String text = "null";
        switch (v.getId()) {
            case R.id.rb1a:
                matrix.setData(1, 0, 0);
                resultado1.setText("a");
                text = "1a";
                break;
            case R.id.rb1b:
                matrix.setData(1, 0, 1);
                resultado1.setText("b");
                text = "1b";
                break;
            case R.id.rb1c:
                matrix.setData(1, 0, 2);
                resultado1.setText("c");
                text = "1c";
                break;
            case R.id.rb1d:
                matrix.setData(1, 0, 3);
                resultado1.setText("d");
                text = "1d";
        }
        mDatabase.child("room_1/0").setValue(Arrays.toString(matrix.getData(0)));
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();



    }

    public void buttonclicklistener2(View v) {
        String text = "null";
        switch (v.getId()) {
            case R.id.rb2a:

                text = "2a";
                break;
            case R.id.rb2b:

                text = "2b";
                break;
            case R.id.rb2c:

                text = "2c";
                break;
            case R.id.rb2d:

                text = "2d";
        }
        mDatabase.child("room_1/1").setValue(Arrays.toString(matrix.getData(0)));
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();


    }

    public void buttonclicklistener3(View v) throws JSONException {
        String text = "null";
        switch (v.getId()) {
            case R.id.rb3a:

                text = "3a";
                break;
            case R.id.rb3b:

                text = "3b";
                break;
            case R.id.rb3c:

                text = "3c";
                break;
            case R.id.rb3d:

                text = "3d";
        }
        //mDatabase.child("room_1").setValue(Data[2]);
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();


    }

    public void buttonclicklistener4(View v) throws JSONException {
        String text = "null";
        switch (v.getId()) {
            case R.id.rb4a:

                text = "4a";
                break;
            case R.id.rb4b:

                text = "4b";
                break;
            case R.id.rb4c:

                text = "4c";
                break;
            case R.id.rb4d:

                text = "4d";
        }
        //mDatabase.child("room_1").setValue(Data[3]);
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();


    }

    public void buttonclicklistener5(View v) throws JSONException {
        String text = "null";
        switch (v.getId()) {
            case R.id.rb5a:

                text = "5a";
                break;
            case R.id.rb5b:

                text = "5b";
                break;
            case R.id.rb5c:

                text = "5c";
                break;
            case R.id.rb5d:

                text = "5d";
                break;
        }
        //mDatabase.child("room_1").setValue(Data[4]);
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();


    }

    /*public void push_answer(String answer) {
        mDatabase.child("room_1").child(answer).setValue(1);

    }*/

}
