package com.example.majiapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView userName, fullName, phoneNumber, Residence, userStatus;
    private CircleImageView userProfileImage;
    private DatabaseReference profileUserRef, profileTechnicianRef;
    private FirebaseAuth mAuth;
    private String currentuserId, User;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
         mAuth = FirebaseAuth.getInstance();
         currentuserId = mAuth.getUid();
         profileUserRef = FirebaseDatabase.getInstance().getReference().child("users");
        profileTechnicianRef = FirebaseDatabase.getInstance().getReference().child("technicians");

        userName = (TextView) findViewById(R.id.my_profile_username);
        fullName = (TextView) findViewById(R.id.my_profile_full_name);
        phoneNumber = (TextView) findViewById(R.id.my_profile_phone_number);
        Residence = (TextView) findViewById(R.id.my_profile_residence);
        userStatus = (TextView) findViewById(R.id.my_profile_status);
        userProfileImage = (CircleImageView) findViewById(R.id.my_profile_pic);


    }

    @Override
    protected void onStart()
    {
        super.onStart();
        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(currentuserId).exists())
                {
                    //User = "user";
                    showuserprofile();

                }
                else {
                    profileTechnicianRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            if(dataSnapshot.child(currentuserId).exists())
                            {
                                User = "technician";
                                showtechnicianprofile();

                            }else
                            {
                                Toast.makeText(ProfileActivity.this,"Unrecognised user",Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showtechnicianprofile()
    {
        profileTechnicianRef.child(currentuserId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    //get data from the firebase and store into the strings
                    String mystatus  = dataSnapshot.child("status").getValue().toString();
                    String myProfileImage  = dataSnapshot.child("profileimage").getValue().toString();
                    String myusername = dataSnapshot.child("Username").getValue().toString();
                    String myfullname = dataSnapshot.child("fullname").getValue().toString();
                    String myPhonenumber = dataSnapshot.child("phone Number").getValue().toString();
                    String myResidence= dataSnapshot.child("residence").getValue().toString();

                    //load image stored in the string to the profile image and set data to the one i the database
                    Picasso.get().load(myProfileImage).placeholder(R.drawable.profile_pic).into(userProfileImage);

                    //display data
                    userStatus.setText(mystatus);
                    userName.setText("@"+ myusername);
                    fullName.setText(myfullname);
                    phoneNumber.setText("Phone Number: "+ myPhonenumber);
                    Residence.setText("Residence: "+ myResidence);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showuserprofile()
    {


        profileUserRef.child(currentuserId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    //get data from the firebase and store into the strings
                    String mystatus  = dataSnapshot.child("status").getValue().toString();
                    String myProfileImage  = dataSnapshot.child("profileimage").getValue().toString();
                    String myusername = dataSnapshot.child("username").getValue().toString();
                    String myfullname = dataSnapshot.child("fullname").getValue().toString();
                    String myPhonenumber = dataSnapshot.child("phone Number").getValue().toString();
                    String myResidence= dataSnapshot.child("residence").getValue().toString();

                    //load image stored in the string to the profile image and set data to the one i the database
                    Picasso.get().load(myProfileImage).placeholder(R.drawable.profile_pic).into(userProfileImage);

                    //display data
                    userStatus.setText(mystatus);
                    userName.setText("@"+ myusername);
                    fullName.setText(myfullname);
                    phoneNumber.setText("Phone Number: "+ myPhonenumber);
                    Residence.setText("Residence: "+ myResidence);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
