package com.example.carlos.ideal;

import android.content.ContentValues;
import android.content.Intent;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity {
    int id_user;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        id_user = this.getIntent().getExtras().getInt("id_user");

        password = (EditText) findViewById(R.id.editText2);
        Button buttonSave = (Button)findViewById (R.id.button5);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues value = new ContentValues();
                value.put("password", password.getText().toString());

                DBController dbController = DBController.getInstance(getApplicationContext());
                dbController.updateUser(id_user, value);

                Toast.makeText(getBaseContext(), "Password changed",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getBaseContext(), EditProfileActivity.class);
                intent.putExtra("id_user", id_user);
                getBaseContext().startActivity(intent);
            }
        });
    }
}
