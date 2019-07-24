package com.example.majiapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_welcomer );
        mAuth = FirebaseAuth.getInstance();
       UserRef  = FirebaseDatabase.getInstance().getReference().child("Users");


       new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent homeIntent = new Intent(WelcomeActivity.this, Splash_screen.class );
                startActivity( homeIntent );
                finish();

            }

        },SPLASH_TIME_OUT);

    }



}
