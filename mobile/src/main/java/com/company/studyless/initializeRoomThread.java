/*
 *  Copyright (C) Alejandro Pacheco - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by  Alejandro Pacheco Bobillo <alepacheco99@gmail.com>
 *
 */

package com.company.studyless;

import android.os.AsyncTask;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InitializeRoomThread extends AsyncTask<Integer, Void, Void> {
    matrix matrix = new matrix();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    void initialize(int roomNumber) {
        int e = 0;
        int i = 0;
        while (e < matrix.questionsRows) {
            while (i < 4) {
                mDatabase.child("Rooms/" + "room_" + roomNumber + "/" + e + "/" + i + "").setValue(0);
                i++;
            }
            i = 0;
            e++;

        }
    }

    @Override
    protected Void doInBackground(Integer... params) {
        initialize(params[0]);
        return null;
    }
}
