package com.example.carlos.ideal;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {
    int id_user;
    EditText description;
    EditText name;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setTitle("Edit profile");

        description = (EditText) findViewById(R.id.editText);
        name = (EditText) findViewById(R.id.editText3);
        email = (EditText) findViewById(R.id.editText4);

        id_user = this.getIntent().getExtras().getInt("id_user");
        DBController dbController = DBController.getInstance(getApplicationContext());
        User u = dbController.getUser(id_user);

        description.setText(u.getDescription());
        name.setText(u.getName());
        email.setText(u.getEmail());

        Button buttonChangePhoto =  (Button)findViewById (R.id.button2);
        Button buttonChangePassword = (Button)findViewById(R.id.button3);
        Button buttonSave = (Button)findViewById(R.id.button4);

        buttonChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ChangePhotoActivity.class);
                intent.putExtra("id_user", id_user);
                getBaseContext().startActivity(intent);
            }
        });

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ChangePasswordActivity.class);
                intent.putExtra("id_user", id_user);
                getBaseContext().startActivity(intent);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues value = new ContentValues();
                value.put("name", name.getText().toString());
                value.put("description", description.getText().toString());
                value.put("email", email.getText().toString());

                DBController dbController = DBController.getInstance(getApplicationContext());
                dbController.updateUser(id_user, value);

                Toast.makeText(getBaseContext(), "Modifications done",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getBaseContext(), PersonalSpaceActivity.class);
                intent.putExtra("id_user", id_user);
                getBaseContext().startActivity(intent);
            }
        });




    }
}
