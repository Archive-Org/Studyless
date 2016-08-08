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

class InitializeRoomThread extends AsyncTask<Integer, Void, Void> {
    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Matrix Matrix = new Matrix();

    private void initialize(int roomNumber) {

        for (int x = 0; x < com.company.studyless.Matrix.questionsRows; x++) {
            for (int i = 0; i < 4; i++) {
                mDatabase.child("Rooms/" + "room_" + roomNumber + "/" + x + "/" + i + "").setValue(0);
            }
        }
    }

    @Override
    protected Void doInBackground(Integer... params) {
        initialize(params[0]);
        return null;
    }
}
