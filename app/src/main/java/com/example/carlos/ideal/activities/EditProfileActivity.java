package com.example.carlos.ideal.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carlos.ideal.AppController;
import com.example.carlos.ideal.DBController;
import com.example.carlos.ideal.R;
import com.example.carlos.ideal.User;

public class EditProfileActivity extends AppCompatActivity {
    int user_id;
    EditText description;
    EditText name;
    EditText email;
    EditText password;
    EditText password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initToolbar("My Profile");
        setValuesToView();
        initButtons();

    }

    private void initButtons() {
        saveButton();
    }

    private void saveButton() {
        Button buttonSave = findViewById(R.id.save_button);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues value = new ContentValues();

                String stringPassword1 = password.getText().toString();
                String stringPassword2 = password2.getText().toString();

                if(!stringPassword1.equals("")){
                    if(stringPassword1.equals(stringPassword2)){
                        value.put("password", stringPassword1);
                    }else {
                        Toast.makeText(getBaseContext(), "Passwords doesn't match",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(name.getText().toString().equals("") || email.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(), "Name and email must be filled",Toast.LENGTH_SHORT).show();
                    return;
                }

                value.put("name", name.getText().toString());
                value.put("description", description.getText().toString());
                value.put("email", email.getText().toString());

                DBController dbController = DBController.getInstance(getApplicationContext());
                dbController.updateUser(user_id, value);

                Toast.makeText(getBaseContext(), "Modifications done",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("user_id", user_id);
                getBaseContext().startActivity(intent);
            }
        });
    }

    private void setValuesToView() {
        description = findViewById(R.id.description);
        name = findViewById(R.id.name_field);
        email = findViewById(R.id.email);

        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);


        user_id = AppController.getInstance().getUser_id();
        DBController dbController = DBController.getInstance(getApplicationContext());

        User user = dbController.getUser(user_id);
        description.setText(user.getDescription());
        name.setText(user.getName());
        email.setText(user.getEmail());
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
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
