package com.example.carlos.ideal.activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.carlos.ideal.DBController;
import com.example.carlos.ideal.Idea;
import com.example.carlos.ideal.R;
import com.example.carlos.ideal.User;
import com.example.carlos.ideal.adapters.MyListAdapter;

import java.util.ArrayList;

public class UsersAccessActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    protected static int idea_id;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_access);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        if(getIntent().hasExtra("tab_page")){
            mViewPager.setCurrentItem(1);
        }else {
            int tab = getIntent().getExtras().getInt("tab_page");
            mViewPager.setCurrentItem(tab);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        idea_id = getIntent().getExtras().getInt("idea_id");

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        initToolbar("Ideal");

    }

    private void initToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(title);
        createHomeButton();
    }

    private void createHomeButton() {
        ImageButton button = findViewById(R.id.homeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsersAccessActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, EditIdeaActivity.class);
        intent.putExtra("idea_id",idea_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        DBController dbController;
        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_users_access, container, false);
            dbController = DBController.getInstance(getContext());
            ArrayList<User> users;
            int tab = getArguments().getInt(ARG_SECTION_NUMBER);
            if(tab ==1){
                users = dbController.getUsersRequests(idea_id);
            }else{
                users = dbController.getAllowedUsers(idea_id);
            }

            loadList(users,rootView,tab);
            return rootView;
        }

        private void loadList(ArrayList<User> list, final View rootView, final int tab) {
            ListView listView = rootView.findViewById(R.id.ideaList);
            listView.setAdapter(new MyListAdapter(getContext(),R.layout.user_row,list) {
                @Override
                public void input(final Object input, View view, int position) {

                    TextView name = view.findViewById(R.id.rowTextName);
                    name.setText(((User)input).getName());

                    TextView description = view.findViewById(R.id.rowTextDescription);
                    description.setText(((User)input).getDescription());

                    BootstrapButton accept = view.findViewById(R.id.accepRequestButton);
                    BootstrapButton deny = view.findViewById(R.id.denyRequestButton);
                    if (tab==1){
                        initRequestsButtons(accept,deny,((User)input).getId(), idea_id);
                    }else {
                        initUserAccessButton(accept,deny, ((User)input).getId(), idea_id);
                    }

                }

            });
        }

        private void initUserAccessButton(BootstrapButton accept, BootstrapButton remove, final int user_id, final int idea_id) {
            accept.setVisibility(View.GONE);
            remove.setText("Remove");
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbController.removeUserAccess(user_id, idea_id);
                    Toast.makeText(getContext(),"Access Removed",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getContext(), UsersAccessActivity.class);
                    intent.putExtra("idea_id",idea_id);
                    intent.putExtra("tab_page",2);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });
        }

        private void initRequestsButtons(BootstrapButton accept, BootstrapButton deny, final int user_id, final int idea_id) {
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbController.accepRequest(user_id, idea_id);
                    Toast.makeText(getContext(),"Request accepted",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getContext(), UsersAccessActivity.class);
                    intent.putExtra("idea_id",idea_id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });

            deny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbController.denyRequest(user_id, idea_id);
                    Toast.makeText(getContext(),"Request denied",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), UsersAccessActivity.class);
                    intent.putExtra("idea_id",idea_id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });
        }

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
