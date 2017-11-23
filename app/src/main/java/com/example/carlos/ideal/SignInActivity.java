package com.example.carlos.ideal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carlos.ideal.Activities.MainActivity;

public class SignInActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Button buttonRegister = (Button)findViewById(R.id.register);
        username = (EditText) findViewById (R.id.username);
        password = (EditText) findViewById (R.id.password);
        email = (EditText) findViewById (R.id.password2);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    DBController dbController = DBController.getInstance(getApplicationContext());
                    User u = dbController.getUser(username.getText().toString());
                    if (u != null) {
                        Toast.makeText(getBaseContext(), "User with this name already exists, please choose another name!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    User u1 = dbController.getUserFromMail(email.getText().toString());
                    if (u1 != null) {
                        Toast.makeText(getBaseContext(), "User with this email already exists, please enter another email!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int user_id = (int)dbController.createUser(username.getText().toString(), email.getText().toString(), password.getText().toString(), null, null);

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    AppController.getInstance().setUser_id(user_id);
                    getBaseContext().startActivity(intent);

                } catch (Exception e) {
                    Log.v("Error open()", e.getMessage());
                }

            }
        });
    }
}
