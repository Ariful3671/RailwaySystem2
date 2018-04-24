package com.example.rafi_pc.railwaysystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {

    Button rd1,rd2,rd3,rd4,rd5,rd6,rd7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rd1= (Button) findViewById(R.id.button);
        rd2= (Button) findViewById(R.id.button2);
        rd3= (Button) findViewById(R.id.button3);
        rd4= (Button) findViewById(R.id.button4);
        rd5= (Button) findViewById(R.id.button5);
        rd6= (Button) findViewById(R.id.button6);
        rd7= (Button) findViewById(R.id.button7);

        rd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,DhakaRailway.class);
                startActivity(i);
            }
        });

        rd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ChittagongRailway.class);
                startActivity(i);
            }
        });

        rd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,RajshahiRailway.class);
                startActivity(i);
            }
        });

        rd4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SylhetRailway.class);
                startActivity(i);
            }
        });

        rd5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,BarisalRailway.class);
                startActivity(i);
            }
        });

        rd6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,RangpurRailway.class);
                startActivity(i);
            }
        });

        rd7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,KhulnaRailway.class);
                startActivity(i);
            }
        });


    }
}
