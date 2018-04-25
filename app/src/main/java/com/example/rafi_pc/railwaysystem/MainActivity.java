package com.example.rafi_pc.railwaysystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Shared preference
        final SharedPreferences sharedPreferences=getSharedPreferences("profileinfo", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();



        //Creating firebase instance
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("users").child(sharedPreferences.getString("phone",""));



        //Getting user information from firebase
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        editor.putString("name",dataSnapshot.child("name").getValue().toString());
                        editor.putString("bday",dataSnapshot.child("bday").getValue().toString());
                        editor.putString("bmonth",dataSnapshot.child("bmonth").getValue().toString());
                        editor.putString("byear",dataSnapshot.child("byear").getValue().toString());
                        editor.putString("gender",dataSnapshot.child("gender").getValue().toString().toLowerCase());
                        editor.putString("birthday",dataSnapshot.child("bday").getValue().toString()+":"
                                +dataSnapshot.child("bmonth").getValue().toString()+":"
                                +dataSnapshot.child("byear").getValue().toString());
                        editor.commit();




                        int currentYear= Calendar.getInstance().get(Calendar.YEAR);
                        int currentMonth= Calendar.getInstance().get(Calendar.MONTH)+1;
                        int currentDay= Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                        int bYear=Integer.parseInt(dataSnapshot.child("byear").getValue().toString());
                        int bMonth=Integer.parseInt(dataSnapshot.child("bmonth").getValue().toString());
                        int bDay=Integer.parseInt(dataSnapshot.child("bday").getValue().toString());

                        int age=currentYear-bYear;
                        if(currentMonth-bMonth<0)
                        {
                            age--;
                        }
                        if(currentMonth-bMonth==0)
                        {
                            if(currentDay-bDay<0)
                            {
                                age--;
                            }
                        }
                        editor.putString("age",String.valueOf(age));
                        editor.commit();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar,menu);
        return true;

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.item_profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.item_log_out) {
            SharedPreferences sharedPreferences=getSharedPreferences("profileinfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("login status","off");
            editor.commit();
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return true;
    }
}
