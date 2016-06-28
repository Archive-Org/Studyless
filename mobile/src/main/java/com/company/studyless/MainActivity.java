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

public class MainActivity extends AppCompatActivity {
    RadioGroup G1, G2, G3, G4, G5;
    TextView result1, result2, result3, result4, result5, matrixText;
    DatabaseReference mDatabase;
    matrix matrix = new matrix();
    int room = 4353;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions);
        G1 = (RadioGroup) findViewById(R.id.radioGroup1);
        G2 = (RadioGroup) findViewById(R.id.radioGroup2);
        G3 = (RadioGroup) findViewById(R.id.radioGroup3);
        G4 = (RadioGroup) findViewById(R.id.radioGroup4);
        G5 = (RadioGroup) findViewById(R.id.radioGroup5);
        result1 = (TextView) findViewById(R.id.resultado1);
        result2 = (TextView) findViewById(R.id.resultado2);
        result3 = (TextView) findViewById(R.id.resultado3);
        result4 = (TextView) findViewById(R.id.resultado4);
        result5 = (TextView) findViewById(R.id.resultado5);
        matrixText = (TextView) findViewById(R.id.matrixText);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initializeRoom();
        //Read from Database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                matrix.SyncWDB(dataSnapshot.child("room_" + room).getValue());
                matrixText.setText(matrix.matrix2string(matrix.getData(), 5, 4));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void buttonClickListener(View v) {
        String tag = (String) v.getTag();
        String[] parts = tag.split(",");
        int row = Integer.parseInt(parts[0]);
        int column = Integer.parseInt(parts[1]);
        //matrix.addOne(row, column);


        updateEntry(row, column);


        String text = (1 + row) + "-" + (1 + column);
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

    }


    public void updateEntry(int row, int column) {
        int value = matrix.getData()[row][column] + 1;
        mDatabase.child("room_" + room + "/" + row + "/" + column + "").setValue(value);



    }

    public void initializeRoom() {
        int e = 0;
        int i = 0;
        while (e < 5) {
            while (i < 4) {
                mDatabase.child("room_" + room + "/" + e + "/" + i + "").setValue(0);
                i++;
            }
            i = 0;
            e++;

        }
    }

}
