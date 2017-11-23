package com.example.carlos.ideal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PersonalSpaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_space);
        DBController dbController = DBController.getInstance(getApplicationContext());
        int i = this.getIntent().getExtras().getInt("id_user");
        //int i = this.getIntent().getIntArrayExtra("id_user")[0];
        User u =  dbController.getUser(i);
        String s = "Hello "+i;
        getSupportActionBar().setTitle(s);
    }
}
