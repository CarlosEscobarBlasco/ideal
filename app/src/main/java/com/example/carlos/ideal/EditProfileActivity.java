package com.example.carlos.ideal;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carlos.ideal.Activities.MainActivity;

public class EditProfileActivity extends AppCompatActivity {
    int user_id;
    EditText description;
    EditText name;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
//        getSupportActionBar().setTitle("Edit profile");

        description = (EditText) findViewById(R.id.editText);
        name = (EditText) findViewById(R.id.editText3);
        email = (EditText) findViewById(R.id.editText4);

        user_id = AppController.getInstance().getUser_id();
        DBController dbController = DBController.getInstance(getApplicationContext());
        Log.d("user","###"+ user_id);

        User u = dbController.getUser(user_id);

        Log.d("user","###"+u);

        description.setText(u.getDescription());
        name.setText(u.getName());
        email.setText(u.getEmail());

        Button buttonChangePhoto =  (Button)findViewById (R.id.button2);
        Button buttonChangePassword = (Button)findViewById(R.id.button3);
        Button buttonSave = (Button)findViewById(R.id.button4);

//        buttonChangePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getBaseContext(), ChangePhotoActivity.class);
//                intent.putExtra("user_id", user_id);
//                getBaseContext().startActivity(intent);
//            }
//        });
//
//        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getBaseContext(), ChangePasswordActivity.class);
//                intent.putExtra("user_id", user_id);
//                getBaseContext().startActivity(intent);
//            }
//        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues value = new ContentValues();
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
}
