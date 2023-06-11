package com.example.feedback;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class FeedbacksListFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_feedbackslist, container, false);

        // Read notes from the DB
        FeedbacksDBHelper dbHelper = new FeedbacksDBHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "feedbacks",
                null,
                null,
                null,
                null,
                null,
                "id DESC"
        );

        //create new ArrayList to hold feedbacks
        ArrayList<Feedback> itemsArrayList = new ArrayList<>(); // calls function to get items list

        // fill this arrayList with data from db
        while (cursor.moveToNext()){
            Feedback fb = new Feedback(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("user_name")),
                    cursor.getString(cursor.getColumnIndex("feedback")),
                    cursor.getString(cursor.getColumnIndex("date")),
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getFloat(cursor.getColumnIndex("rating"))
            );
            itemsArrayList.add(fb);

        }
        // get the ListView and attach the adapter
        ListView itemsListView  =  root.findViewById(R.id.feedbacks);
        TextView noFeedbacks = root.findViewById(R.id.noFeedbacks);
        TextView writeFeedbackFromList = root.findViewById(R.id.writeFeedbackFromList);

        // if there is no feedback is database
        if(itemsArrayList.isEmpty()){
            // remove ListView
            itemsListView.setVisibility(View.GONE);
            writeFeedbackFromList.setOnClickListener(view -> {

                // Lion Heart (2011). Replacing a fragment with another fragment inside activity group. Stack Overflow
                // Available from: https://stackoverflow.com/questions/5658675/replacing-a-fragment-with-another-fragment-inside-activity-group [Accessed 29 November 2020]

                WriteFeedbackFragment writeFeedbackFragment = new WriteFeedbackFragment();

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.nav_host_fragment, writeFeedbackFragment);

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                navigationView.setCheckedItem(R.id.nav_writefeedback);

                // Set title bar

                ((MainActivity) getActivity()).setActionBarTitle("Write Feedback");

            }
            );
        }else{
            // remove TextViews
            noFeedbacks.setVisibility(View.GONE);
            writeFeedbackFromList.setVisibility(View.GONE);

            // instantiate the custom list adapter
            FeedbackAdapter adapter = new FeedbackAdapter(getContext(), itemsArrayList, getActivity());


            // Mksso (2011). Remove the bottom divider of an android ListView. Stack Overflow
            // Available from https://stackoverflow.com/questions/4961999/remove-the-bottom-divider-of-an-android-listview [Accessed 7 December 2020]
            // For top and bottom padding on ListView
            itemsListView.addFooterView(new View(getContext()), null, false);
            itemsListView.addHeaderView(new View(getContext()), null, false);

            // set adapter
            itemsListView.setAdapter(adapter);

            // will reload after any change
            adapter.notifyDataSetChanged();
            cursor.close();
        }
        return root;
    }


}
