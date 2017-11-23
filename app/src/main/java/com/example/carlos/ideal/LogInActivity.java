package com.example.carlos.ideal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity {
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        Button buttonLogin = (Button)findViewById(R.id.buttonLogin);
        username = (EditText) findViewById (R.id.username);
        password = (EditText) findViewById (R.id.password);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBController dbController = DBController.getInstance(getApplicationContext());
                 if (dbController.getUser(username.getText().toString(), password.getText().toString()) == null) {
                     // fail authentification
                     Toast.makeText(getBaseContext(), "Username or Password incorrect!",
                             Toast.LENGTH_SHORT).show();
                     return;
                 }
                User user = dbController.getUser(username.getText().toString());
                Intent intent = new Intent(getBaseContext(), PersonalSpaceActivity.class);
                intent.putExtra("id_user", user.getId());
                getBaseContext().startActivity(intent);





            }
        });
    }
}
