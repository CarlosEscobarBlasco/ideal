package com.example.carlos.ideal.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carlos.ideal.AppController;
import com.example.carlos.ideal.DBController;
import com.example.carlos.ideal.R;
import com.example.carlos.ideal.User;

public class SignInActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Button buttonRegister = findViewById(R.id.register);
        username = findViewById (R.id.username);
        password = findViewById (R.id.password);
        email = findViewById (R.id.email);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkAllFieldsAreFilled(username, password,email)){
                    Toast.makeText(getBaseContext(), "All fields must be filled",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                DBController dbController = DBController.getInstance(getApplicationContext());

                try {
                    User user = dbController.getUser(username.getText().toString());
                    if (user != null) {
                        Toast.makeText(getBaseContext(), "User with this name already exists, please choose another name!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    user = dbController.getUserFromMail(email.getText().toString());
                    if (user != null) {
                        Toast.makeText(getBaseContext(), "User with this email already exists, please enter another email!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int user_id = (int)dbController.createUser(username.getText().toString(), email.getText().toString(), password.getText().toString(), null, null);

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    AppController.getInstance().setUser_id(user_id);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.v("Error open()", e.getMessage());
                }

            }
        });
    }

    private boolean checkAllFieldsAreFilled(EditText username, EditText password, EditText email) {
        return !(username.getText().toString().matches("") ||
                password.getText().toString().matches("") ||
                email.getText().toString().matches(""));
    }
}
