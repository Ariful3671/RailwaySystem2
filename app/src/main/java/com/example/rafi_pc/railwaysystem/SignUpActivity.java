package com.example.rafi_pc.railwaysystem;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafi_pc.railwaysystem.classes.DatePickerFragment;
import com.example.rafi_pc.railwaysystem.classes.NetworkChecker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{


    //Instance
    EditText name,password,phone;
    TextView dob,gender;
    Button create;

    //Object of network checker
    NetworkChecker networkChecker;


    //Variables for birthday
    String userBDay,userBMonth,userByear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        //Initializing object
        networkChecker=new NetworkChecker(this);


        //Hide Action Bar
        getSupportActionBar().hide();


        //Initializing Instance
        name=(EditText)findViewById(R.id.edit_text_user_name_sign_up_screen);
        password=(EditText)findViewById(R.id.edit_text_password_sign_up_screen);
        phone=(EditText)findViewById(R.id.edit_text_user_phone_sign_up_screen);

        dob=(TextView)findViewById(R.id.text_view_user_DOB_sign_up_screen);
        gender=(TextView)findViewById(R.id.text_view_user_gender_sign_up_screen);

        create=(Button)findViewById(R.id.button_create_profile_sign_up_screen);



        //click listener for dob text view
        dob.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dob.setError(null);
                        DialogFragment datePicker=new DatePickerFragment();
                        datePicker.show(getFragmentManager(),"date picker");
                    }
                }
        );


        //click listener for gender
        gender.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gender.setError(null);
                        android.support.v7.app.AlertDialog.Builder builder;
                        builder = new android.support.v7.app.AlertDialog.Builder(SignUpActivity.this,R.style.CustomDialogTheme);
                        View mview=getLayoutInflater().inflate(R.layout.dialog_gender_picker,null);
                        final RadioGroup RG=(RadioGroup)mview.findViewById(R.id.RG_gender);
                        builder.setView(mview);
                        builder.setMessage("Select your gender")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        int id=RG.getCheckedRadioButtonId();
                                        if(id==R.id.RB_male)
                                        {
                                            gender.setText("Male");
                                        }
                                        if(id==R.id.RB_female)
                                        {
                                            gender.setText("Female");
                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                }).show();
                    }
                }
        );


        //Click listener for create profile button
        create.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(networkChecker.isConnected())
                        {
                            if(name.getText().toString().trim().equals(""))
                            {
                                name.setError("Please set a username");
                            }
                            if(phone.getText().toString().trim().equals(""))
                            {
                                phone.setError("Please enter your phone number");
                            }
                            if(password.getText().toString().trim().equals(""))
                            {
                                password.setError("Please set a password");
                            }
                            if(dob.getText().toString().trim().equals(""))
                            {
                                dob.setError("Please set your birth date");
                            }
                            if(gender.getText().toString().trim().equals(""))
                            {
                                gender.setError("Please select your gender");
                            }
                            if(!name.getText().toString().trim().equals("")&&!phone.getText().toString().trim().equals("")&&!password.getText().toString().trim().equals("")&&!dob.getText().toString().trim().equals("")&&!gender.getText().toString().trim().equals(""))
                            {
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = database.getReference("users");
                                final String user_name = name.getText().toString().toLowerCase().trim();
                                final String user_phone = phone.getText().toString().trim();
                                final String user_gender = gender.getText().toString();
                                final String user_password = password.getText().toString().trim();
                                ref.addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.hasChild(user_phone))
                                                {
                                                    phone.setError("Phone number already exist");
                                                    Toast.makeText(SignUpActivity.this, "This Phone Number Already Exists", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(SignUpActivity.this, "Profile created successfully", Toast.LENGTH_SHORT).show();
                                                    Map<String, String> userData = new HashMap<String, String>();
                                                    userData.put("name",user_name);
                                                    userData.put("password", user_password);
                                                    userData.put("bday", userBDay);
                                                    userData.put("bmonth", userBMonth);
                                                    userData.put("byear", userByear);
                                                    userData.put("gender", user_gender);
                                                    DatabaseReference userRef = database.getReference("users").child(user_phone);
                                                    userRef.setValue(userData);
                                                    Intent intent=new Intent(SignUpActivity.this,LogInActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        }
                                );
                            }
                        }
                        else{
                            Toast.makeText(SignUpActivity.this, "Please check your internet connection!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );



    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c=Calendar.getInstance();
        c.set(c.YEAR,year);
        c.set(c.MONTH,month);
        c.set(c.DAY_OF_MONTH,dayOfMonth);
        dob=(TextView)findViewById(R.id.text_view_user_DOB_sign_up_screen);
        userBDay=String.valueOf(dayOfMonth);
        userBMonth=String.valueOf(month+1);
        userByear=String.valueOf(year);
        dob.setText(dayOfMonth+" : "+(month+1)+" : "+year);
    }


}
