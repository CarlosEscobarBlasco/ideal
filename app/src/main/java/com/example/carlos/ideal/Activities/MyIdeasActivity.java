package com.example.carlos.ideal.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.carlos.ideal.AppController;
import com.example.carlos.ideal.DBController;
import com.example.carlos.ideal.Idea;
import com.example.carlos.ideal.R;
import com.example.carlos.ideal.adapters.MyListAdapter;

import java.util.ArrayList;

public class MyIdeasActivity extends AppCompatActivity {

    private ListView listView;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ideas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        creteIdeaButton();
        initToolbar("My Ideas");
        user_id = AppController.getInstance().getUser_id();
    }


    @Override
    protected void onResume() {
        super.onResume();
        DBController dbController = DBController.getInstance(getApplicationContext());
        ArrayList<Idea> ideas = dbController.getIdeasFromUser(user_id);
        TextView rowTextView = findViewById(R.id.empty_message);
        if(ideas.isEmpty()){
            rowTextView.setVisibility(View.VISIBLE);
        }else {
            rowTextView.setVisibility(View.INVISIBLE);
        }
        loadList(ideas);
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
                Intent intent = new Intent(MyIdeasActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void loadList(ArrayList<Idea> list) {
        listView = findViewById(R.id.ideaList);
        listView.setAdapter(new MyListAdapter(this,R.layout.my_idea_row,list) {

            @Override
            public void input(final Object input, View view, int position) {
                LinearLayout layout = view.findViewById(R.id.row);
                layout.setBackgroundColor(position%2==0? ResourcesCompat.getColor(getResources(), R.color.white, null):ResourcesCompat.getColor(getResources(), R.color.lightGrey, null));
                TextView rowTextView = view.findViewById(R.id.rowTextTitle);
                TextView shortDescription = view.findViewById(R.id.rowTextShortDescription);
                rowTextView.setText(((Idea) input).getTitle());
                shortDescription.setText(((Idea) input).getShortDescription());

                LinearLayout idearow = view.findViewById(R.id.myIdeaRow);
                idearow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MyIdeasActivity.this, EditIdeaActivity.class);
                        intent.putExtra("user_id",user_id);
                        intent.putExtra("idea_id",((Idea) input).getId());
                        startActivity(intent);
                    }
                });

                BootstrapButton deleteButton = view.findViewById(R.id.deleteButton);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog diaBox = AskOption(input);
                        diaBox.show();
                    }
                });
            }
        });
    }

    private void creteIdeaButton() {
        FloatingActionButton topicButton = findViewById(R.id.createIdea);
        topicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyIdeasActivity.this, CreateIdeaActivity.class);
                intent.putExtra("user_id",user_id);
                startActivity(intent);
            }
        });
    }

    private AlertDialog AskOption(final Object input)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to delete "+((Idea) input).getTitle())

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        DBController dbController = DBController.getInstance(getApplicationContext());
                        dbController.deleteIdea(((Idea) input).getId());
                        Toast.makeText(getBaseContext(),"Idea "+((Idea) input).getTitle()+" deleted",Toast.LENGTH_SHORT).show();
                        onResume();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }

}
