package com.example.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_writefeedback, R.id.nav_feedbackslist, R.id.nav_joke)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //setting title
        setActionBarTitle("Home");

        //catching passed intends with data

        // Amit (2013). Open a fragment of an activity from another activity. Stack Overflow.
        // Available from https://stackoverflow.com/questions/16836420/open-a-fragment-of-an-activity-from-another-activity [Accesssed 10 December 2020]
        //if 1 -> change HomeFragment to WriteFeedbackFragment
        if(getIntent().getIntExtra("fragmentNumber",0)==1){

            Intent i = getIntent();
            //receiving feedbackId
            long feedbackId = i.getLongExtra("feedbackId", 0);

            Fragment fragment= new WriteFeedbackFragment();

            // to pass feedbackId from fragment to fragment (if we wanna edit Fragment)
            Bundle bundle = new Bundle();
            bundle.putLong("feedbackId", feedbackId);
            // set Fragmentclass Arguments
            fragment.setArguments(bundle);

            //change Fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

            // change title
            setActionBarTitle("Write Feedback");
            //change active navDrawer item
            navigationView.setCheckedItem(R.id.nav_writefeedback);

        }
        //if 2 -> change HomeFragment to FeedbacksListFragment
        else if(getIntent().getIntExtra("fragmentNumber",0)==2)
        {
            Fragment fragment= new FeedbacksListFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

            //setting title
            setActionBarTitle("All Feedbacks");
            //change active navDrawer item
            navigationView.setCheckedItem(R.id.nav_feedbackslist);
        }

    }

    //creating menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // if we click Exit -> app will exit :D
    // David_D (2013). Quit item menu in Android application?. Stack Overflow
    // Available from https://stackoverflow.com/questions/18630796/quit-item-menu-in-android-application [Accessed 8 December 2020]
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.exit_app:
                finish();
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // for setting title to action title bar
    // Tarun Tak (2015). How change Actionbar title by switching between Fragments. Stack Overflow
    // Available from: https://stackoverflow.com/questions/29557747/how-change-actionbar-title-by-switching-between-fragments [Accessed 1 December 2020]

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    // to check action titles correct when we press Back
    // Saintjab (2015). Why Actionbar title keep displaying the backstack title on hardware back button pressed? Stack Overflow.
    // Available from: https://stackoverflow.com/questions/30359022/why-actionbar-title-keep-displaying-the-backstack-title-on-hardware-back-button [Accessed 8 December 2020]
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavigationView navigationView = findViewById(R.id.nav_view);
        // getting fragment index
        int T = getFragmentManager().getBackStackEntryCount();

       // depending on "index" put correct title and active navDrawer item
        if (T == 0) {
            setActionBarTitle("Home");
            navigationView.setCheckedItem(R.id.nav_home);
        }
        else if (T == 1) {
            setActionBarTitle("Write Feedback");
            navigationView.setCheckedItem(R.id.nav_writefeedback);
        }else if (T == 2) {
            setActionBarTitle("All Feedbacks");
            navigationView.setCheckedItem(R.id.nav_feedbackslist);
        }else if (T == 3) {
            setActionBarTitle("Random Chuck Norris joke");
            navigationView.setCheckedItem(R.id.nav_joke);
        }
        else {
            //if smth else -> return to first fragment
            String tr = getFragmentManager().getBackStackEntryAt(T-3).getName();
            setActionBarTitle(tr);
            getFragmentManager().popBackStack();
        }
    }

}