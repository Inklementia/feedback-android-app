package com.example.feedback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //Write feedback button
        Button writeFeedbackBtn = root.findViewById(R.id.writeFeedback);

        //View all Feedback button
        TextView viewAllFeedbacks = (TextView) root.findViewById(R.id.viewAllFeedbacks);


        //Open WriteFeedbackFragment
        writeFeedbackBtn.setOnClickListener(view -> {

            // Lion Heart (2011). Replacing a fragment with another fragment inside activity group. Stack Overflow
            // Available from: https://stackoverflow.com/questions/5658675/replacing-a-fragment-with-another-fragment-inside-activity-group [Accessed 29 November 2020]

            WriteFeedbackFragment writeFeedbackFragment = new WriteFeedbackFragment();

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.nav_host_fragment, writeFeedbackFragment);

            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_writefeedback);

            // Set title bar

            ((MainActivity) getActivity()).setActionBarTitle("Write Feedback");

        }
        );
        //Open FeedbacksListFragment
        viewAllFeedbacks.setOnClickListener(view -> {
            FeedbacksListFragment feedbackListFragment = new FeedbacksListFragment();

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.nav_host_fragment, feedbackListFragment);

            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_feedbackslist);

            // Set title bar
            ((MainActivity) getActivity()).setActionBarTitle("All Feedbacks");

        }
        );
        return root;
    }

}