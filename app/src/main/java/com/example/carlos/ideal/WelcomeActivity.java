package com.example.carlos.ideal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button buttonSignIn = (Button)findViewById(R.id.signIn);
        Button buttonLogIn = (Button)findViewById(R.id.logIn);

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogIn = new Intent(getBaseContext(), LogInActivity.class);
                getBaseContext().startActivity(intentLogIn);
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignIn = new Intent(getBaseContext(), SignInActivity.class);
                getBaseContext().startActivity(intentSignIn);
            }
        });
    }
}
