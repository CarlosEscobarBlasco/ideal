package com.example.carlos.ideal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PersonalSpaceActivity extends AppCompatActivity {
    int id_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_space);
        DBController dbController = DBController.getInstance(getApplicationContext());
        id_user = this.getIntent().getExtras().getInt("id_user");
        //int i = this.getIntent().getIntArrayExtra("id_user")[0];
        User u =  dbController.getUser(id_user);
        String s = "Hello "+id_user;
        getSupportActionBar().setTitle(s);

        Button buttonEditProfile = (Button)findViewById(R.id.button);

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), EditProfileActivity.class);
                intent.putExtra("id_user", id_user);
                getBaseContext().startActivity(intent);
            }
        });
    }
}
