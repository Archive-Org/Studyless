package com.company.studyless;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button b1a, b1b, b1c, b1d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1a = (Button) findViewById(R.id.B1a);


    }


    public void buttonclicklistener(View v) {
        String text = "bad";
        switch (v.getId()) {
            case R.id.B1a:
                text = "a";
                break;
            case R.id.B1b:
                text = "b";
                break;
            case R.id.B1c:
                text = "c";
                break;
        }

        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();



    }

}



