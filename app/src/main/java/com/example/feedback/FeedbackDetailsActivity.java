package com.example.feedback;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FeedbackDetailsActivity extends AppCompatActivity {
    long feedbackId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_item_details);

        // Get a reference to the Intent that launched this activity.
        Intent i = getIntent();
        // putting data in variable
        feedbackId = i.getLongExtra("feedbackId", 0);

        // if we have id in intent
        if (feedbackId > 0) {
            FeedbacksDBHelper dbHelper = new FeedbacksDBHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            // take data from database
            Cursor cursor = db.query("feedbacks", null, "id = ?", new String[]{String.valueOf(feedbackId)}, null, null, null);
            if (cursor.moveToNext()) {

                long feedbackId = cursor.getLong((cursor.getColumnIndex("id")));
                String userName = cursor.getString(cursor.getColumnIndex("user_name"));
                String feedbackMesssage = cursor.getString(cursor.getColumnIndex("feedback"));
                String feedbackDate = cursor.getString(cursor.getColumnIndex("date"));
                String feedbackRating = cursor.getString(cursor.getColumnIndex("rating"));
                String feedbackType = cursor.getString(cursor.getColumnIndex("type"));

                //  find EditTexts
                TextView tvId = findViewById(R.id.id_details);
                TextView tvUserName = findViewById(R.id.username_details);
                TextView tvFeedback = findViewById(R.id.feedback_details);
                TextView tvType = findViewById((R.id.type_details));
                TextView tvRating = findViewById(R.id.rating_details);
                TextView tvdate = findViewById(R.id.date_details);

                //prefill EditText with values from db
                setTitle("Feedback #" + feedbackId);
                tvId.setText("Feedback #" + feedbackId);
                tvType.setText(feedbackType);
                tvdate.setText(feedbackDate);
                tvRating.setText("x " + String.valueOf(feedbackRating));
                tvUserName.setText(userName);
                tvFeedback.setText(feedbackMesssage);

            }
            // Back to list button
            Button btnBackToList = findViewById(R.id.back_to_list);

            // delete feedback
            ImageView btnDelete = findViewById(R.id.delete_feedback_details);
            // edit feedback
            ImageView btnEdit = findViewById(R.id.edit_feedback_details);

            // Back to list
            btnBackToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //from FeedbackDetailsActivity we go to MainAtivity, then change HomeFragment to FeedbackListFragment
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("fragmentNumber",2); //to keep track which fragment we want to access from MainActivity
                    startActivity(intent);
                }
            });


            // Deleting feedback
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog diaBox = AreYouSure(db, v, feedbackId);
                    diaBox.show();


                }

            });
            // Editing feedback
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent is needed to path data (feedbackId and fragment number) from FeedbackDetailsActivity to MainActivity
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("fragmentNumber",1); //fragment number
                    intent.putExtra("feedbackId", feedbackId); // id
                    startActivity(intent);
                    //this data will be caught in MainActivity and then open needed Fragment
                }

            });
        }
    }

    private AlertDialog AreYouSure(SQLiteDatabase db, View v, long itemId)
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Are you sure?")
                .setMessage("Do you want to Delete this feedback forever?")
                .setIcon(R.drawable.ic_trash)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //delete from db
                        db.delete("feedbacks", "id = ?", new String[]{String.valueOf(itemId)});

                        // return to FeedbackListFragment from MainActivity
                        Intent i = new Intent(v.getContext(), MainActivity.class);
                        i.putExtra("fragmentNumber",2); //fragment number
                        startActivity(i);
                        //closing alert
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //closing alert
                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }
}
