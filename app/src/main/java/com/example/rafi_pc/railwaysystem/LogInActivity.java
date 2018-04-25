package com.example.rafi_pc.railwaysystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rafi_pc.railwaysystem.classes.NetworkChecker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {

    //Instance
    EditText phoneNumber,password;
    Button logIn,signUp;


    //Creating object
    NetworkChecker networkChecker;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);



        //Creating shared preference
        SharedPreferences sharedPreferences=getSharedPreferences("profileinfo", Context.MODE_PRIVATE);



        //Move to Main Activity if already log in
        if(sharedPreferences.getString("login status","").equals("on"))
        {
            Intent intent=new Intent(LogInActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }



        //Hide Action Bar
        getSupportActionBar().hide();


        //Initializing object
        networkChecker=new NetworkChecker(this);
        database = FirebaseDatabase.getInstance();


        //Initializing Instance
        phoneNumber=(EditText)findViewById(R.id.edit_text_phone_number_log_in_screen);
        password=(EditText)findViewById(R.id.edit_text_password_log_in_screen);

        logIn=(Button)findViewById(R.id.button_log_in);
        signUp=(Button)findViewById(R.id.button_sign_up);



        //Creating Shared preference editor
        final SharedPreferences.Editor editor=sharedPreferences.edit();



        //Click listener for sign up button
        signUp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(LogInActivity.this,SignUpActivity.class);
                        startActivity(intent);
                    }
                }
        );


        //Click listener for log in button
        logIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(networkChecker.isConnected())
                        {
                            if(phoneNumber.getText().toString().trim().equals("")||password.getText().toString().trim().equals(""))
                            {
                                Toast.makeText(LogInActivity.this, "Please Insert Username and Password", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                final DatabaseReference ref = database.getReference("users");
                                ref.addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.hasChild(phoneNumber.getText().toString().trim()))
                                                {
                                                    DatabaseReference ref1 = ref.child(phoneNumber.getText().toString().trim());
                                                    ref1.addListenerForSingleValueEvent(
                                                            new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    String actualPassword = dataSnapshot.child("password").getValue().toString();
                                                                    if(actualPassword.equals(password.getText().toString().trim()))
                                                                    {
                                                                        editor.putString("login status","on");
                                                                        editor.putString("phone",phoneNumber.getText().toString().trim());
                                                                        editor.commit();
                                                                        Intent intent=new Intent(LogInActivity.this,MainActivity.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                    else{
                                                                        Toast.makeText(LogInActivity.this, "Wrong Username/Password", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            }
                                                    );
                                                }
                                                else{
                                                    Toast.makeText(LogInActivity.this, "Wrong Username/Password", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        }
                                );
                            }
                        }
                        else {
                            Toast.makeText(LogInActivity.this, "Please Check Internet Connection!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}
