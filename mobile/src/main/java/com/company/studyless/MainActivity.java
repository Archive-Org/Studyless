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
        String tag = (String) v.getTag();
        String[] parts = tag.split(",");
        int row = Integer.parseInt(parts[0]);
        int colum = Integer.parseInt(parts[1]);
        matrix.addOne(row, colum);
        String text = row + " " + colum;
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
