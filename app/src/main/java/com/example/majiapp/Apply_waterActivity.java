package com.example.majiapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class Apply_waterActivity extends AppCompatActivity {
    ProgressDialog loadingBar;
    private EditText fullName, idNumber,phoneNumber, Email, County, Town;
    private Button saveApplicationDetails;
    private FirebaseAuth mAuth;
    private DatabaseReference applyuserRef,applywaterRef;

    String currentUserId;
    private Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_water);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        applyuserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        applywaterRef = FirebaseDatabase.getInstance().getReference().child("Water Application");

        mToolbar = (Toolbar) findViewById(R.id.apply_water_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Apply for water connection");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fullName = (EditText) findViewById(R.id.apply_fullname);
        idNumber = (EditText) findViewById(R.id.apply_idnumber);
        phoneNumber = (EditText) findViewById(R.id.apply_phonenumber);
        Email = (EditText) findViewById(R.id.apply_email);
        County = (EditText) findViewById(R.id.apply_county);
        Town = (EditText) findViewById(R.id.apply_town);
        saveApplicationDetails = (Button) findViewById(R.id.save_application);
        loadingBar = new ProgressDialog(this);


        saveApplicationDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveapplicationDetails();
            }
        });




    }

    private void SaveapplicationDetails() {

        applyuserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String userfullname  = dataSnapshot.child("Fullname").getValue().toString();
                    String fullname = fullName.getText().toString();
                    String idnumber = idNumber.getText().toString();
                    String phonenumber = phoneNumber.getText().toString();
                    String email = Email.getText().toString();
                    String county = County.getText().toString();
                    String towni = Town.getText().toString();

                    if (TextUtils.isEmpty(fullname))
                    {
                        fullName.setError("Fullname required!");
                    }
                    else if (TextUtils.isEmpty(idnumber))
                    {
                        idNumber.setError("Id Number required!");
                    }
                    else if (TextUtils.isEmpty(phonenumber))
                    {
                        phoneNumber.setError("Phone Number required!");
                    }
                    else if (TextUtils.isEmpty(email))
                    {
                        Email.setError("email required!");
                    }
                    else if (TextUtils.isEmpty(county))
                    {
                        County.setError("County required!");
                    }
                    else if (TextUtils.isEmpty(towni)) {
                        County.setError("Town required!");
                    }

                    else
                    {
                        HashMap userMap = new HashMap();
                        userMap.put("Fullname", fullname);
                        userMap.put("ID Number", idnumber);
                        userMap.put("Phone Number", phonenumber);
                        userMap.put("Email", email);
                        userMap.put("County", county);
                        userMap.put("Town", towni);

                        loadingBar.setTitle("Saving Information");
                        loadingBar.setMessage("Please wait while we are saving your application...");
                        loadingBar.show();
                        loadingBar.setCanceledOnTouchOutside(true);



                        applywaterRef.child(userfullname + currentUserId).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task)
                            {
                                if(task.isSuccessful())
                                {
                                    sendUserToMainActivity();
                                    loadingBar.dismiss();


                                }else {
                                    String messages = task.getException().getMessage();
                                    Toast.makeText(Apply_waterActivity.this, "Error occured :  " + messages, Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }

                            }
                        });

                    }


                }
                else
                {
                    Toast.makeText(Apply_waterActivity.this, "No datasnapshot found", Toast.LENGTH_SHORT).show();


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



                }

    private void sendUserToMainActivity() {
        startActivity(new Intent(this,MainActivity.class));
    }
}
