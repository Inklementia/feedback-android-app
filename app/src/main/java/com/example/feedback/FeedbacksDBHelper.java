package com.example.feedback;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedbacksDBHelper extends SQLiteOpenHelper {
    public FeedbacksDBHelper(Context context) {
        super(context, "feedbacks.db", null, 1);
    }
    // TourUzbekistan (2020). Code is partially taken from Android Application from Seminars.
    @Override
    public void onCreate(SQLiteDatabase db) {
        //creation of a table
        String createTable = "CREATE TABLE feedbacks (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, user_name VARCHAR(255), feedback TEXT, date TEXT, type VARCHAR(255), rating REAL)";
        db.execSQL(createTable);

        // initial data
        String populateTable = "INSERT INTO feedbacks ('user_name', 'feedback', 'date', 'type', 'rating') VALUES " +
               "('Maria97', 'To be honest it is relatively simple app. But I find it nice looking and I really liked the jokes', '02.12.2020', 'Review', '4.5')," +
                "('Antonio Banderas', 'Great app for feedback tracking, yet it would be great to have alert or confirm message before deleting the feedback', '01.12.2020', 'Suggestion', '4')," +
                "('Inklementia', 'Nice app!', '20.11.2020', 'Review', '5')," +
                "('Mettaton003', 'Come on, horrible app, looks childish, not suggested', '08.11.2020', 'Review', '2.5'), " +
                "('Hada Jhin', 'My dear countryman, you remind me how little our culture has advanced.', '13.11.2020', 'Bug', '3.5')";
        db.execSQL(populateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
