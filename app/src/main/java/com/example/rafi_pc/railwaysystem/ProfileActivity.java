package com.example.rafi_pc.railwaysystem;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafi_pc.railwaysystem.Adapter.AdapterProfile;
import com.example.rafi_pc.railwaysystem.classes.DatePickerFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{


    int icon[]={R.drawable.ic_action_name,R.drawable.ic_action_phone,R.drawable.ic_action_birthday,R.drawable.ic_action_age,R.drawable.ic_action_gender};
    String[] options={"  Name","  Phone","  Birthday","  Age","  Gender"};
    String[] value={"Set","Set","Set","Set","Set"};
    int right_arrow[]={R.drawable.ic_action_right_arrow};


    //List view
    ListView listViewProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Activity Title
        setTitle("Profile");

        //Initializing list view
        listViewProfile=(ListView)findViewById(R.id.list_view_profile);


        //setting adapter for profile
        final AdapterProfile adapter=new AdapterProfile(options,value,icon,right_arrow,ProfileActivity.this);
        listViewProfile.setAdapter(adapter);


        listViewProfile.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        SharedPreferences sharedPreferences=getSharedPreferences("profileinfo",Context.MODE_PRIVATE);
                        final SharedPreferences.Editor editor=sharedPreferences.edit();
                        final String phone=sharedPreferences.getString("phone","");
                        final DatabaseReference ref=database.getReference("users").child(phone);
                        AlertDialog.Builder builder;
                        View dialogView;
                        final TextView dialogTextView;

                        switch (position)
                        {
                            case 0:
                                builder = new AlertDialog.Builder(ProfileActivity.this,R.style.CustomDialogTheme);
                                dialogView=getLayoutInflater().inflate(R.layout.dialog_custom,null);
                                dialogTextView=(TextView)dialogView.findViewById(R.id.text_view_custom_dialog);
                                dialogTextView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_action_name, 0, 0, 0);
                                dialogTextView.setHint("Name");
                                builder.setView(dialogView);
                                builder.setMessage("Enter your name")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                if(!dialogTextView.getText().toString().trim().equals(""))
                                                {
                                                    String name=dialogTextView.getText().toString().trim();
                                                    ref.child("name").setValue(name);
                                                    editor.putString("name",name);
                                                    editor.commit();
                                                    adapter.notifyDataSetChanged();
                                                    listViewProfile.setAdapter(adapter);

                                                }
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        }).show();
                                break;
                            case 1:
                                Toast.makeText(ProfileActivity.this, "Phone number can not be changed", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                DialogFragment datePicker=new DatePickerFragment();
                                datePicker.show(getFragmentManager(),"date picker");
                                break;
                            case 3:
                                Toast.makeText(ProfileActivity.this, "Please change your birth date to change age", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                builder = new AlertDialog.Builder(ProfileActivity.this,R.style.CustomDialogTheme);
                                dialogView=getLayoutInflater().inflate(R.layout.dialog_gender_picker,null);
                                final RadioGroup RG=(RadioGroup)dialogView.findViewById(R.id.RG_gender);
                                builder.setView(dialogView);
                                builder.setMessage("Select gender")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                int id=RG.getCheckedRadioButtonId();
                                                if(id==R.id.RB_male)
                                                {
                                                    ref.child("gender").setValue("Male");
                                                    editor.putString("gender","Male");
                                                    editor.commit();
                                                    adapter.notifyDataSetChanged();
                                                    listViewProfile.setAdapter(adapter);
                                                }
                                                if(id==R.id.RB_female)
                                                {
                                                    ref.child("gender").setValue("Female");
                                                    editor.putString("gender","Female");
                                                    editor.commit();
                                                    adapter.notifyDataSetChanged();
                                                    listViewProfile.setAdapter(adapter);
                                                }
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        }).show();
                                break;
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

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        SharedPreferences sharedPreferences=getSharedPreferences("profileinfo",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        final String phone=sharedPreferences.getString("phone","");
        final DatabaseReference ref=database.getReference("users").child(phone);

        ref.child("bday").setValue(String.valueOf(dayOfMonth));
        ref.child("bmonth").setValue(String.valueOf(month+1));
        ref.child("byear").setValue(String.valueOf(year));


        int currentYear= Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth= Calendar.getInstance().get(Calendar.MONTH)+1;
        int currentDay= Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        int age=currentYear-year;
        if((currentMonth-(month+1))<0)
        {
            age--;
        }
        if(currentMonth-(month+1)==0)
        {
            if(currentDay-dayOfMonth<0)
            {
                age--;
            }
        }

        editor.putString("birthday",String.valueOf(dayOfMonth)+":"+String.valueOf(month+1)+":"+String.valueOf(year));
        editor.putString("age",String.valueOf(age));
        editor.commit();
        listViewProfile=(ListView)findViewById(R.id.list_view_profile);
        final AdapterProfile adapter=new AdapterProfile(options,value,icon,right_arrow,ProfileActivity.this);
        listViewProfile.setAdapter(adapter);
    }
}
