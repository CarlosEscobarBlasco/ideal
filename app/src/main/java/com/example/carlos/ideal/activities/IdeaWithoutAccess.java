package com.example.carlos.ideal.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.example.carlos.ideal.DBController;
import com.example.carlos.ideal.Idea;
import com.example.carlos.ideal.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.carlos.ideal.AppController;

public class IdeaWithoutAccess extends AppCompatActivity {
    int user_id;
    int idea_id;
    DBController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_without_access);
        user_id = AppController.getInstance().getUser_id();
        idea_id = getIntent().getExtras().getInt("idea_id");

        dbController = DBController.getInstance(getApplicationContext());
        boolean hasRequested = dbController.hasRequestedAccess(idea_id,user_id);
        fillField(hasRequested);
        initButton(hasRequested);

        createHomeButton();
    }

    private void createHomeButton() {
        ImageButton button = findViewById(R.id.homeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IdeaWithoutAccess.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void initButton(boolean hasRequested) {
        Button requestButton = findViewById(R.id.requestButton);
        if (hasRequested){
           requestButton.setAlpha(.5f);
           requestButton.setClickable(false);
           return;
        }

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbController.requestAccess(user_id,idea_id);
//                Intent intent = new Intent(IdeaWithoutAccess.this, IdeasListActivity.class);
                Toast.makeText(getBaseContext(), "Request sent",Toast.LENGTH_SHORT).show();
//                startActivity(intent);
                finish();
            }
        });
    }


    private void fillField(boolean hasRequested) {
        DBController dbController = DBController.getInstance(getApplicationContext());

        Idea idea = dbController.getIdeaInformation(getIntent().getExtras().getInt("idea_id"));

        TextView titleField = findViewById(R.id.IdeaTitle);
        titleField.setText(idea.getTitle());

        TextView shortDescriptionField = findViewById(R.id.IdeaSHortDescription);
        shortDescriptionField.setText(idea.getShortDescription());

        TextView tag = findViewById(R.id.IdeaTag);
        tag.setText(idea.getTag());

        if (hasRequested){
            TextView requestMessage = findViewById(R.id.requestMessage);
            requestMessage.setText("You already have requested access. Please wait until the owner accept it.");
        }

    }


}
