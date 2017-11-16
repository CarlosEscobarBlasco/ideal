package com.example.carlos.ideal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.carlos.ideal.adapters.MyListAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private int user_id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateButtons();
/*
        DBController dbController = DBController.getInstance(getApplicationContext());
        //dbController.creteUser("carlos","carlos@mail.com","password","description",null);
        ArrayList<Idea> ideas = dbController.getIdeasFromUser(user_id);
        //long a =dbController.createIdea("title","asd","a","asdasdasd",1);
        //ArrayList<User> users = dbController.getUsers();
        if(ideas.isEmpty()){
            TextView rowTextView = findViewById(R.id.textView);
            rowTextView.setVisibility(View.VISIBLE);
        }
        loadList(ideas);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        DBController dbController = DBController.getInstance(getApplicationContext());
        ArrayList<Idea> ideas = dbController.getIdeasFromUser(user_id);
        if(ideas.isEmpty()){
            TextView rowTextView = findViewById(R.id.textView);
            rowTextView.setVisibility(View.VISIBLE);
        }
        loadList(ideas);
    }

    private void activateButtons() {
        creteIdeaButton();
    }

    private void loadList(ArrayList<Idea> list) {
        listView = findViewById(R.id.list);
        listView.setAdapter(new MyListAdapter(this,R.layout.idea_row,list) {
            @Override
            public void input(final Object input, View view) {
                TextView rowTextView = view.findViewById(R.id.rowTextView);
                rowTextView.setText(((Idea) input).getTitle());
                rowTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, EditIdeaActivity.class);
                        intent.putExtra("user_id",user_id);
                        intent.putExtra("idea_id",((Idea) input).getId());
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void creteIdeaButton() {
        Button topicButton = (Button) findViewById(R.id.createIdea);
        topicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateIdeaActivity.class);
                intent.putExtra("user_id",user_id);
                startActivity(intent);
            }
        });
    }
}
