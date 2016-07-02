package com.company.studyless;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {
    RadioGroup G1, G2, G3, G4, G5;
    TextView result1, result2, result3, result4, result5, matrixText, RoomTextView;
    EditText roomField;
    DatabaseReference mDatabase;
    matrix matrix = new matrix();
    Random random = new Random();
    int room = random.nextInt(1000) + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions);
        bindObjects();

        RoomTextView.setText(String.valueOf(room));
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initializeRoom(room);
        getDatabase();

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
        mDatabase.child("room_" + room).child(row + "/" + column + "").setValue(value);



    }

    public void initializeRoom(int roomNumber) {
        int e = 0;
        int i = 0;
        while (e < 5) {
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
        initializeRoom(n);
        room = n;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        G1.clearCheck();
        G2.clearCheck();
        G3.clearCheck();
        G4.clearCheck();
        G5.clearCheck();
        RoomTextView.setText(String.valueOf(room));

        getDatabase();
    }

    public void bindObjects() {
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
        roomField = (EditText) findViewById(R.id.roomField);
        matrixText = (TextView) findViewById(R.id.matrixText);
        RoomTextView = (TextView) findViewById(R.id.RoomTextViex);

    }

    public void getDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("room_" + room).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                matrix.SyncWDB(dataSnapshot.getValue());
                matrixText.setText(matrix.matrix2string(matrix.getData(), 5, 4));
                result1.setText(matrix.MostVoted(0));
                result2.setText(matrix.MostVoted(1));
                result3.setText(matrix.MostVoted(2));
                result4.setText(matrix.MostVoted(3));
                result5.setText(matrix.MostVoted(4));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }





}
