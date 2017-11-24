package com.example.carlos.ideal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.example.carlos.ideal.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TypefaceProvider.registerDefaultIconSets();
        activateButtons();

    }

    private void activateButtons() {
        myProfileButton();
        myIdeasButton();
        universeOfIdeasButton();
    }


    private void myProfileButton() {
        BootstrapButton topicButton = findViewById(R.id.myProfileButton);
        topicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);

                startActivity(intent);
            }
        });
    }

    private void myIdeasButton() {
        BootstrapButton topicButton = findViewById(R.id.myIdeasButton);
        topicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyIdeasActivity.class);
                startActivity(intent);
            }
        });
    }

    private void universeOfIdeasButton() {
        BootstrapButton topicButton = findViewById(R.id.universeOfIdeasButton);
        topicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IdeasListActivity.class);
                startActivity(intent);
            }
        });
    }



}
