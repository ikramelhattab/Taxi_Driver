package com.example.taxi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taxi.Common.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button callSignUp, login_btn, btnfor;
    ImageView image;
    TextView logoText, sloganText;
    TextInputEditText edemail, edpassword;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        users=db.getReference(Common.user_driver_tb1);



        callSignUp = findViewById(R.id.signup);
        login_btn = findViewById(R.id.bprofil);
        btnfor = findViewById(R.id.forget);

        image = findViewById(R.id.logo_Image);
        logoText = findViewById(R.id.logo_name);
        sloganText = findViewById(R.id.slogo_name);
        edemail = findViewById(R.id.edemail_login);
        edpassword = findViewById(R.id.edpwd_login);

        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });


        btnfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Maps.class);
                startActivity(intent);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

      auth.signInWithEmailAndPassword(edemail.getText().toString(),edpassword.getText().toString())
        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                FirebaseDatabase.getInstance().getReference(Common.user_driver_tb1)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Common.currentUser=dataSnapshot.getValue(User.class);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                startActivity(new Intent(Login.this,Maps.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(getApplicationContext(),
                "Failed", Toast.LENGTH_SHORT).show();

    }
});
                //loginUser(v);
                // Intent intent = new Intent(Login.this,UserProfile.class);
                // startActivity(intent);
            }
        });


    }




    private Boolean ValidateEmail() {
        String val = edemail.getText().toString();
        // String noWhiteSpace ="\\A\\w{4,20}\\z";
        if (val.isEmpty()) {
            edemail.setError("Field cannot be empty");
            return false;
        } else {
            edemail.setError(null);
            //email.setErrorEnabled(false);
            return true;
        }

    }

    private boolean ValidatePassword() {
        String val = edpassword.getText().toString();

        if (val.isEmpty()) {
            edpassword.setError("Field cannot be empty");
            return false;
        } else {
            edpassword.setError(null);
            //password.setErrorEnabled(false);
            return true;
        }
    }


    public void loginUser(View view) {
        if (!ValidateEmail() | !ValidatePassword()) {
            return;
        } else {
            isUser();
        }
    }

    private void isUser() {
        final String userEnteredEmail = edemail.getText().toString().trim();
        final String userEnteredPassword = edpassword.getText().toString().trim();

        //final String userEnteredEmail = "ikramelhattab90@gmail1com";
        //final String userEnteredPassword = "0000i@";

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.user_driver_tb1);

        Query checkUser = reference.orderByChild("email").equalTo(userEnteredEmail);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    //  email.setError(null);
                    // email.setErrorEnabled(false);

                    String passwordFromDB = dataSnapshot.child(userEnteredEmail).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userEnteredPassword)) {

                        // email.setError(null);
                        //   email.setErrorEnabled(false);

                 /*       String nameFromDB = dataSnapshot.child(userEnteredEmail).child("name").getValue(String.class);
                        String usernameFromDB = dataSnapshot.child(userEnteredEmail).child("username").getValue(String.class);
                        String phoneNoFromDB = dataSnapshot.child(userEnteredEmail).child("phoneNo").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredEmail).child("email").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(), UserProfile.class);

                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phoneNo", phoneNoFromDB);
                        intent.putExtra("password", passwordFromDB);

                        startActivity(intent);*/
                    } else {
                        edpassword.setError("Wrong Password");
                        edpassword.requestFocus();
                    }
                } else {
                    edemail.setError("No such User Exist");
                    edemail.requestFocus();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}