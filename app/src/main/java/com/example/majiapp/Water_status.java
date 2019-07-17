package com.example.majiapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Water_status extends AppCompatActivity {
    private Toolbar mToolbar;
    TextView status, name, email, date;
    FirebaseAuth mAuth;
    DatabaseReference applicantRef;
    String currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_status);

        mAuth = FirebaseAuth.getInstance();
        applicantRef = FirebaseDatabase.getInstance().getReference().child("Water Application");
        currentUser = mAuth.getCurrentUser().getUid();

        mToolbar = (Toolbar) findViewById(R.id.water_status_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Water Application Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (TextView) findViewById(R.id.status_name);
        email = (TextView) findViewById(R.id.status_email);
        status = (TextView) findViewById(R.id.status);
        date  = (TextView)  findViewById(R.id.status_date);
        //String fullname = dataSnapshot.child(current_user_ID).child("fullname").getValue().toString();
        //navusername.setText(fullname);

        applicantRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("fullname")){
                        String s_name = dataSnapshot.child("fullname").getValue().toString();
                        name.setText(s_name);
                    }else {
                        Toast.makeText(Water_status.this, "name not found",Toast.LENGTH_SHORT).show();

                    }

                    if(dataSnapshot.hasChild("fullname")){
                        String s_email = dataSnapshot.child("Email").getValue().toString();
                        email.setText(s_email);
                    }
                    if(dataSnapshot.hasChild("fullname")){
                        String s_status = dataSnapshot.child("status").getValue().toString();
                        status.setText(s_status);
                    }
                    if(dataSnapshot.hasChild("fullname")){
                        String s_date = dataSnapshot.child("date").getValue().toString();
                        date.setText(s_date);
                    }
                }
                else
                {
                    Toast.makeText(Water_status.this, "Water application details not found",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
