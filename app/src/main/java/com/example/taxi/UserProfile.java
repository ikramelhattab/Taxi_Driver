package com.example.taxi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class UserProfile extends AppCompatActivity {

    TextView fullnamelabel,usernamelabel;
    TextInputLayout fullname,email,ph,pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        fullname =findViewById(R.id.profile_full_name);
        email =findViewById(R.id.profile_email);
        ph =findViewById(R.id.profile_phone);
        pwd =findViewById(R.id.profile_pwd);
        fullnamelabel =findViewById(R.id.full_name_field);
        usernamelabel =findViewById(R.id.username_field);

        //show all data

        showAllUserData();


    }

    private void showAllUserData() {
        Intent intent =getIntent();
        String username= intent.getStringExtra("username");
        String user_name= intent.getStringExtra("name");
        String useremail= intent.getStringExtra("email");
        String userphone= intent.getStringExtra("phoneNo");
        String userpassword= intent.getStringExtra("password");

        fullnamelabel.setText(user_name);
        usernamelabel.setText(username);
        fullname.getEditText().setText(user_name);
        email.getEditText().setText(useremail);
        ph.getEditText().setText(userphone);
        pwd.getEditText().setText(userpassword);

    }
}