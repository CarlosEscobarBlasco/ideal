package com.example.carlos.ideal.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carlos.ideal.AppController;
import com.example.carlos.ideal.DBController;
import com.example.carlos.ideal.R;

public class LogInActivity extends AppCompatActivity {

    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        initButtons();


    }

    private void initButtons() {
        logInButton();
        signIn();
    }

    private void signIn() {
        Button buttonLogin = findViewById(R.id.signIn);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SignInActivity.class);
                getBaseContext().startActivity(intent);
            }
        });
    }

    private void logInButton() {
        Button buttonLogin = findViewById(R.id.buttonLogin);
        username = findViewById (R.id.username);
        password = findViewById (R.id.password);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBController dbController = DBController.getInstance(getApplicationContext());
                int id = dbController.getIdOfUser(username.getText().toString(), password.getText().toString());
                if(id<1){
                    Toast.makeText(getBaseContext(), "Username or Password incorrect!",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                AppController.getInstance().setUser_id(id);
                getBaseContext().startActivity(intent);
            }
        });
    }
}
