package com.example.carlos.ideal;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EditIdeaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_idea);
        initSpinner();
        fillField();
        creteIdeaButton();
        setTitle("Edit idea");
    }

    private void fillField() {
        DBController dbController = DBController.getInstance(getApplicationContext());

        Idea idea = dbController.getIdeaInformation(getIntent().getExtras().getInt("idea_id"));
        EditText titleField = findViewById(R.id.editIdeaTitle);
        titleField.setText(idea.getTitle());

        EditText shortDescriptionField = findViewById(R.id.editIdeaSHortDescription);
        shortDescriptionField.setText(idea.getShortDescription());

        EditText fullDescriptionField = findViewById(R.id.editIdeaFullDescription);
        fullDescriptionField.setText(idea.getFullDescription());

        Spinner tagSpinner = findViewById(R.id.editIdeaSpinner);

        for(int i=0;i<tagSpinner.getCount();i++){
            if(tagSpinner.getItemAtPosition(i).equals(idea.getTag())) tagSpinner.setSelection(i);
        }
    }

    private void initSpinner() {
        Spinner spinner = findViewById(R.id.editIdeaSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tags, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void creteIdeaButton() {
        Button topicButton = findViewById(R.id.update);
        topicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBController dbController = DBController.getInstance(getApplicationContext());

                EditText titleField = findViewById(R.id.editIdeaTitle);
                String title = titleField.getText().toString();
                if(!checkField(title,titleField)) return;

                EditText shortDescriptionField = findViewById(R.id.editIdeaSHortDescription);
                String shortDescription = shortDescriptionField.getText().toString();
                if(!checkField(shortDescription,shortDescriptionField)) return;

                EditText fullDescriptionField = findViewById(R.id.editIdeaFullDescription);
                String fullDescription = fullDescriptionField.getText().toString();
                if(!checkField(fullDescription,fullDescriptionField)) return;

                Spinner tagSpinner = findViewById(R.id.editIdeaSpinner);
                String tag = tagSpinner.getSelectedItem().toString();
                int idea_id = getIntent().getExtras().getInt("idea_id");

                dbController.updateIdea(idea_id,title,shortDescription,tag,fullDescription);
                Intent intent = new Intent(EditIdeaActivity.this, MainActivity.class);
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
