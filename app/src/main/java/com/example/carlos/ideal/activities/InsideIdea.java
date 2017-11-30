package com.example.carlos.ideal.activities;

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

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carlos.ideal.AppController;
import com.example.carlos.ideal.Comment;
import com.example.carlos.ideal.DBController;
import com.example.carlos.ideal.Idea;
import com.example.carlos.ideal.R;
import com.example.carlos.ideal.User;
import com.example.carlos.ideal.adapters.MyListAdapter;

import java.util.ArrayList;

public class InsideIdea extends AppCompatActivity {

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
        setContentView(R.layout.activity_inside_idea);

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
        TextView toolbarTitle = findViewById(R.id.title);
        toolbarTitle.setText(title);
        createHomeButton();
    }
    private void createHomeButton() {
        ImageButton button = findViewById(R.id.homeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InsideIdea.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, IdeasListActivity.class);
        intent.putExtra("idea_id",idea_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

            int tab = getArguments().getInt(ARG_SECTION_NUMBER);
            int fragment;
            View rootView;
            if (tab==1){
                fragment = R.layout.fragment_inside_idea_description;
                rootView = inflater.inflate(fragment, container, false);
                fillField(rootView);
            }else{
                fragment = R.layout.fragment_inside_idea_comments;
                rootView = inflater.inflate(fragment, container, false);
                DBController dbController = DBController.getInstance(getContext());
                ArrayList<Comment> comments =dbController.getIdeaComments(idea_id);
                loadComments(comments,rootView);
                commentButton(rootView);
            }

//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            return rootView;
        }

        private void commentButton(View rootView) {
            ImageButton commentButton = rootView.findViewById(R.id.commentButton);
            final EditText commentText = rootView.findViewById(R.id.commentText);
            commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DBController dbController = DBController.getInstance(getContext());
                    int user_id = AppController.getInstance().getUser_id();
                    String comment = commentText.getText().toString();

                    if(comment.equals("")){
                        Toast.makeText(getContext(),"Comment can not be empty",Toast.LENGTH_SHORT).show();
                        commentText.setError("");
                        return;
                    }
                    dbController.comment(idea_id,user_id,comment);

                    Intent intent = new Intent(getContext(), InsideIdea.class);
                    intent.putExtra("idea_id",idea_id);
                    intent.putExtra("tab_page",2);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

                }
            });
        }

        private void loadComments(ArrayList<Comment> list, View rootView) {
            ListView listView = rootView.findViewById(R.id.commentList);
            listView.setAdapter(new MyListAdapter(getContext(),R.layout.comment_row,list) {
                @Override
                public void input(final Object input, View view, int position) {
                    DBController dbController = DBController.getInstance(getContext());
                    User owner = dbController.getUser(((Comment) input).getOwner_id());
                    TextView ownerView = view.findViewById(R.id.commentOwner);
                    ownerView.setText(owner.getName());

                    TextView comment = view.findViewById(R.id.comment);
                    comment.setText(((Comment) input).getDescription());

                }

            });
        }

        private void fillField(View rootView) {
            DBController dbController = DBController.getInstance(getContext());
            Idea idea = dbController.getIdea(idea_id);

            TextView titleField = rootView.findViewById(R.id.IdeaTitle);
            titleField.setText(idea.getTitle());

            TextView shortDescriptionField = rootView.findViewById(R.id.IdeaShortDescription);
            shortDescriptionField.setText(idea.getShortDescription());

            TextView fullDescriptionField = rootView.findViewById(R.id.IdeaFullDescription);
            fullDescriptionField.setText(idea.getFullDescription());

            TextView ideaTag = rootView.findViewById(R.id.ideaTag);
            ideaTag.setText(idea.getTag());
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
