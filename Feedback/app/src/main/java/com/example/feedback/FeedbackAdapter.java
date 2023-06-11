package com.example.feedback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;
import java.util.ArrayList;

public class FeedbackAdapter extends BaseAdapter {

    // Codepath.com (no date). Using a BaseAdapter with ListView. Codepath
    // Available from https://guides.codepath.com/android/Using-a-BaseAdapter-with-ListView#using-the-custom-adapter [Accessed 29 November 2020]

    // TourUzbekistan (2020). Code is partially taken from Android Application from Seminars.

    private final Context context;
    private final SQLiteDatabase db;
    private ArrayList<Feedback> items; //data source of the list adapter
    private Activity activity;

    public FeedbackAdapter(Context context, ArrayList<Feedback> items, Activity activity) {

        this.context = context;
        this.items = items;
        this.activity = activity;

        FeedbacksDBHelper dbHelper = new FeedbacksDBHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }

    @Override
    public int getCount() {
    return items.size(); //returns total of items in the list
}

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        Feedback currentItem = (Feedback) getItem(position);
        return currentItem.Id;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.feedback_item, null);
        }
        // get current item to be displayed
        Feedback currentItem = (Feedback) getItem(position);
        // current item id
        long currentItemId = currentItem.Id;

        // filling fields with data
        TextView tvID= convertView.findViewById(R.id.feedback_id);
        tvID.setText("#"+currentItem.Id);

        TextView tvDate= convertView.findViewById(R.id.feedback_date);
        tvDate.setText(currentItem.date);

        TextView tvUser = convertView.findViewById(R.id.feedback_user);
        tvUser.setText("By "+currentItem.username);

        TextView tvType = convertView.findViewById(R.id.feedback_type);
        tvType.setText(currentItem.type);

        TextView tvRating = convertView.findViewById(R.id.feedback_rating);
        tvRating.setText(String.valueOf(currentItem.rating));

        ImageView btnDelete = convertView.findViewById(R.id.delete_feedback);

        // delete functionality
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog diaBox = AreYouSure(currentItemId, currentItem);
                diaBox.show();
            }

        });

        //clicking on item
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sending intend to details activity with selected feedbackId
                Intent i = new Intent(context, FeedbackDetailsActivity.class);
                i.putExtra("feedbackId", currentItemId);
                context.startActivity(i);
            }
        });

        // returning view
        return convertView;
    }
    // Ram Kiran (2012). Android confirmation message for delete. Stack Overflow
    // Available from: https://stackoverflow.com/questions/11740311/android-confirmation-message-for-delete/11740348 [Accessed 13 December 2020]
    private AlertDialog AreYouSure(long itemId, Feedback item)
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(activity)
                // set message, title, and icon
                .setTitle("Are you sure?")
                .setMessage("Do you want to Delete this feedback forever?")
                .setIcon(R.drawable.ic_trash)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //deleting from table
                          db.delete("feedbacks", "id = ?", new String[]{String.valueOf(itemId)});
                        //deleting from list array
                          items.remove(item);


                          //notify ListView to Rebuild
                          notifyDataSetChanged();

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
