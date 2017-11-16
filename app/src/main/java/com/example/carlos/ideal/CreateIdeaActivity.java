package com.example.carlos.ideal;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CreateIdeaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_idea);
        initSpinner();
        initButtons();
        setTitle("Create idea");
    }

    private void initButtons() {
        creteIdeaButton();
    }

    private void initSpinner() {
        Spinner spinner = findViewById(R.id.createIdeaSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tags, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void creteIdeaButton() {
        Button topicButton = findViewById(R.id.create);
        topicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBController dbController = DBController.getInstance(getApplicationContext());

                EditText titleField = findViewById(R.id.createIdeaTitle);
                String title = titleField.getText().toString();
                if(!checkField(title,titleField)) return;

                EditText shortDescriptionField = findViewById(R.id.createIdeaSHortDescription);
                String shortDescription = shortDescriptionField.getText().toString();
                if(!checkField(shortDescription,shortDescriptionField)) return;

                EditText fullDescriptionField = findViewById(R.id.createIdeaFullDescription);
                String fullDescription = fullDescriptionField.getText().toString();
                if(!checkField(fullDescription,fullDescriptionField)) return;

                Spinner tagSpinner = findViewById(R.id.createIdeaSpinner);
                String tag = tagSpinner.getSelectedItem().toString();

                dbController.createIdea(title,shortDescription,tag,fullDescription,getIntent().getExtras().getInt("user_id"));
                Intent intent = new Intent(CreateIdeaActivity.this, MainActivity.class);
                startActivity(intent);
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
}
