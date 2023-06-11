package com.example.feedback;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class WriteFeedbackFragment extends Fragment {
    //needed values
    EditText txtDate, etUserName, etMessage, etDate;
    DatePickerDialog datepicker;
    Object feedbackType;
    String cookieRating;
    long feedbackId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_writefeedback, container, false);
        //if we ADDING feedback, this will be null
        if (getArguments() != null)
        {
            feedbackId = getArguments().getLong("feedbackId");
        }

        // Feedback type spinner
        // Tarun (2013). How to customize a Spinner in Android. Stack Overflow.
        // Available from: https://stackoverflow.com/questions/16694786/how-to-customize-a-spinner-in-android [Accessed 28 November 2020]
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.spinner_options,
                R.layout.custom_spinner_layout
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_layout);
        Spinner customSpinner = root.findViewById(R.id.feedbackType);
        customSpinner.setAdapter(adapter);
        customSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //this not really needed, but let it be here
            @Override
            public void onItemSelected(AdapterView < ? > adapterView, View view,
                                       int position, long id){
                feedbackType = adapterView.getItemAtPosition(position);
                if (feedbackType != null) {
                    // if type is selected
                }
            }

            @Override
            public void onNothingSelected (AdapterView < ? > adapterView){
                // if nothing is selected
            };

        });

        // Cookies rating bar
        // Rana A. (2020). How to Customize RatingBar Component in Android. Hackernoon.
        // Available from: https://hackernoon.com/how-to-customize-ratingbar-component-in-android-gf1h3u0d [Accessed 28 Novemver 2020]
        RatingBar ratingBar = root.findViewById(R.id.ratingBar);
        // setting listener
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //Toast.makeText(getContext(), String.valueOf(rating), Toast.LENGTH_SHORT).show();
                //getting value of amount of selected cookies
                cookieRating = String.valueOf(rating);
            }
        });

        // Tutlane.com (no date). Android DatePicker with Examples.
        // Available from: https://www.tutlane.com/tutorial/android/android-datepicker-with-examples [Accesssed 28 November 2020]
        // finding date field
        txtDate = root.findViewById(R.id.date);
        // cannot be focused
        txtDate.setFocusable(false);
        txtDate.setInputType(InputType.TYPE_NULL);

        // today's date as default value
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        txtDate.setText(day + "." + (month + 1) + "." + year);

        //clicking on date TextView
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // date picker dialog
                datepicker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //filling TextView field with choosen date
                                txtDate.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });
        // find other fields
        etUserName = root.findViewById(R.id.userName);
        etMessage = root.findViewById(R.id.userMessage);
        etDate = root.findViewById(R.id.date);

        //  2. Get the value from the DB
        // if it is EDITING feedback, we have feedbackId
        if (feedbackId > 0) {
            FeedbacksDBHelper dbHelper = new FeedbacksDBHelper(getContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Cursor cursor = db.query("feedbacks", null, "id = ?", new String[] {String.valueOf(feedbackId)}, null, null, null);
            if (cursor.moveToNext()) {
                // taking values from db
                String editUserName = cursor.getString(cursor.getColumnIndex("user_name"));
                String editMesssage = cursor.getString(cursor.getColumnIndex("feedback"));
                String editDate = cursor.getString(cursor.getColumnIndex("date"));
                String editRating = cursor.getString(cursor.getColumnIndex("rating"));
                String editType = cursor.getString(cursor.getColumnIndex("type"));

                // filling fields with values from db
                etUserName.setText(editUserName);
                etMessage.setText(editMesssage);
                etDate.setText(editDate);
                ratingBar.setRating(Float.parseFloat(editRating));
                customSpinner.setSelection(getIndex(customSpinner, editType ));

                // Update the button's text for better UX
                Button btn = root.findViewById(R.id.sendFeedback);
                btn.setText("Update Feedback");
            }
        }

        // saving / updating feedback button
        Button btnSaveFeedback = root.findViewById(R.id.sendFeedback);

        btnSaveFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get db helper class
                FeedbacksDBHelper dbHelper = new FeedbacksDBHelper(getContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // if username field and feedback field is not empty
                if(validateIfEmpty(etUserName) && validateIfEmpty(etMessage)){
                    // putting values to variables
                    String feedbackUserName = etUserName.getText().toString().trim();
                    String feedbackMessage = etMessage.getText().toString().trim();
                    String feedbackDate = etDate.getText().toString().trim();

                // if rating is 0 -> do 0.0
                if(cookieRating == null){
                    cookieRating = "0.0";
                }
                // filling db with values from fields
                ContentValues values = new ContentValues();
                values.put("user_name", feedbackUserName);
                values.put("feedback", feedbackMessage);
                values.put("date", feedbackDate);
                values.put("type", feedbackType.toString());
                values.put("rating", cookieRating);

                long id;

                if (feedbackId > 0) {
                    // This is an update operation
                    id = db.update("feedbacks", values, "id = ?", new String[]{String.valueOf(feedbackId)});
                    id = feedbackId;
                } else {

                    // This is an creation operation
                    id = db.insert("feedbacks", null, values);

                }
                // close connection
                db.close();

                if (id <= 0) {
                    Toast.makeText(getContext(), "There was an error inserting your feedback!", Toast.LENGTH_LONG).show();
                }else{
                    // return to DetailsPage
                    Intent i = new Intent(getContext(), FeedbackDetailsActivity.class);
                    i.putExtra("feedbackId", id);
                    startActivity(i);
                }
            }
            }
        });

        return root;
    }
    // for validation
    // Coding With Tea (2020). Complete Form Validation in android studio - City Guide App - Part 12. Youtube.
    // Available from: https://youtu.be/qcDlcITNqnE [Accessed 30 November 2020]
    private boolean validateIfEmpty(EditText editTextField){
        //removing white spaces
        String editText =  editTextField.getText().toString().trim();

        if(editText.isEmpty()){
            editTextField.setError("Field cannot be empty");
            editTextField.setBackgroundResource(R.drawable.rounded_text_field_error);
            return false;
        }else {
            editTextField.setError(null);
            return true;
        }
    }

    // if user goes "back" empty fields to avoid insertion to database
    @Override
    public void onResume() {
        super.onResume();
        if(feedbackId <= 0){
            etUserName.setText("");
            etMessage.setText("");
        }

    }

    // get index of selected value from feedbackType spinner (by providing a string)


    private int getIndex(Spinner mySpinner, String myString){

        int index = 0;

        for (int i = 0; i < mySpinner.getCount(); i++){
            if (mySpinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
}
