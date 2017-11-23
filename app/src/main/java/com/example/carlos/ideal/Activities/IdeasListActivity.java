package com.example.carlos.ideal.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.carlos.ideal.AppController;
import com.example.carlos.ideal.DBController;
import com.example.carlos.ideal.Idea;
import com.example.carlos.ideal.R;
import com.example.carlos.ideal.adapters.MyListAdapter;

import java.util.ArrayList;

public class IdeasListActivity extends AppCompatActivity {
    private static int user_id;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_tabs);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        initTabs(tabLayout);
        createHomeButton();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        user_id = AppController.getInstance().getUser_id();
    }

    private void createHomeButton() {
        ImageButton button = findViewById(R.id.homeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IdeasListActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void initTabs(TabLayout tabLayout) {

        for (String tab: getResources().getStringArray(R.array.tags)) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

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
            View rootView = inflater.inflate(R.layout.fragment_idea_list, container, false);
            String tag = getResources().getStringArray(R.array.tags)[getArguments().getInt(ARG_SECTION_NUMBER)-1];
            DBController dbController = DBController.getInstance(getContext());
            ArrayList<Idea> ideas = dbController.getIdeasFromTag(tag);
            loadList(ideas,rootView);
            return rootView;
        }

        private void loadList(ArrayList<Idea> list, View rootView) {
            ListView listView = rootView.findViewById(R.id.ideaList);
            listView.setAdapter(new MyListAdapter(getContext(),R.layout.idea_row,list) {
                @Override
                public void input(final Object input, View view, int position) {
                    LinearLayout layout = view.findViewById(R.id.row);
                    layout.setBackgroundColor(position%2==0? ResourcesCompat.getColor(getResources(), R.color.white, null):ResourcesCompat.getColor(getResources(), R.color.lightGrey, null));
                    TextView rowTextView = view.findViewById(R.id.rowTextTitle);
                    TextView shortDescription = view.findViewById(R.id.rowTextShortDescription);
                    rowTextView.setText(((Idea) input).getTitle());
                    shortDescription.setText(((Idea) input).getShortDescription());
                    TextView voteValue = view.findViewById(R.id.voteValue);
                    voteButtons(view, ((Idea) input),voteValue);
                    updateVoteValue(((Idea) input), voteValue);
                }

            });
        }

        private void voteButtons(View view, final Idea idea, final TextView voteValue) {
            final DBController dbController = DBController.getInstance(getContext());
            final ImageButton upVoteButton = view.findViewById(R.id.upVoteButton);
            final ImageButton downVoteButton = view.findViewById(R.id.downVoteButton);

            upVoteButton.setEnabled(dbController.canUserUpVote(idea.getId(),user_id));
            downVoteButton.setEnabled(dbController.canUserDownVote(idea.getId(),user_id));

            upVoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbController.voteIdea(1, idea.getId(), user_id);
                    updateVoteValue(idea, voteValue);
                    upVoteButton.setEnabled(dbController.canUserUpVote(idea.getId(),user_id));
                    downVoteButton.setEnabled(dbController.canUserDownVote(idea.getId(),user_id));
                }
            });

            downVoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DBController dbController = DBController.getInstance(getContext());
                    dbController.voteIdea(-1, idea.getId(), user_id);
                    updateVoteValue(idea,voteValue);
                    upVoteButton.setEnabled(dbController.canUserUpVote(idea.getId(),user_id));
                    downVoteButton.setEnabled(dbController.canUserDownVote(idea.getId(),user_id));
                }
            });

        }

        private void updateVoteValue(final Idea idea, TextView voteValue){
            DBController dbController = DBController.getInstance(getContext());
            int votes = dbController.getIdeaInformation(idea.getId()).getVotes();
            voteValue.setText(votes+"");
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
            return getResources().getStringArray(R.array.tags).length;
        }
    }
}
