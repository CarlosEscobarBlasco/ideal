package com.example.carlos.ideal.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carlos.ideal.DBController;
import com.example.carlos.ideal.R;

public class CreateIdeaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_and_modify_idea);
        initSpinner();
        initButtons();
        initToolbar("Create idea");
        Toast.makeText(getBaseContext(),"All fields are required",Toast.LENGTH_LONG).show();
    }

    private void initToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(title);
        createHomeButton();
    }

    private void initButtons() {
        creteIdeaButton();
    }

    private void initSpinner() {
        Spinner spinner = findViewById(R.id.IdeaSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tags, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void creteIdeaButton() {
        Button createButton = findViewById(R.id.acceptButton);
        createButton.setText("Create");
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBController dbController = DBController.getInstance(getApplicationContext());

                EditText titleField = findViewById(R.id.IdeaTitle);
                String title = titleField.getText().toString();
                if(!checkField(title,titleField)) return;

                EditText shortDescriptionField = findViewById(R.id.IdeaSHortDescription);
                String shortDescription = shortDescriptionField.getText().toString();
                if(!checkField(shortDescription,shortDescriptionField)) return;

                EditText fullDescriptionField = findViewById(R.id.IdeaFullDescription);
                String fullDescription = fullDescriptionField.getText().toString();
                if(!checkField(fullDescription,fullDescriptionField)) return;

                Spinner tagSpinner = findViewById(R.id.IdeaSpinner);
                String tag = tagSpinner.getSelectedItem().toString();

                dbController.createIdea(title,shortDescription,tag,fullDescription,getIntent().getExtras().getInt("user_id"));
                finish();
                Toast.makeText(getBaseContext(),"Idea successfully created",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkField(String text, EditText editText){
        if(text.equals("")){
            editText.setHint("This field can not be empty");
            editText.setHintTextColor(Color.RED);
            return false;
        }
        return true;
    }

    private void createHomeButton() {
        ImageButton button = findViewById(R.id.homeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateIdeaActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
