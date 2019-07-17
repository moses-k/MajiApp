package com.example.majiapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener

{
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private View navView;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, TechnicianRef, postsRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String current_user_ID,User;
    private CircleImageView NavProfileImage;
    private TextView navusername;
    private Button Report_button, Apply_water_button,View_Reports;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        mAuth = FirebaseAuth.getInstance();
        UserRef  = FirebaseDatabase.getInstance().getReference().child("users");
        TechnicianRef  = FirebaseDatabase.getInstance().getReference().child("technicians");
        postsRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        current_user_ID = mAuth.getCurrentUser().getUid();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_activity);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nav_view);

        //inflate the layout
        navView = navigationView.inflateHeaderView(R.layout.header);
        NavProfileImage = (CircleImageView) navView.findViewById(R.id.nav_profile_image);
        navusername = (TextView) navView.findViewById(R.id.nav_username);
       // Report_button =(Button) findViewById(R.id.repport_water);
        //Apply_water_button = (Button) findViewById(R.id.apply_water);
        //View_Reports = (Button) findViewById(R.id.see_reports);
        navigationView.setNavigationItemSelectedListener(this);
        Report_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                takeUserTouploadActivity();
            }
        });

        Apply_water_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeUserToApplywaterActivity();
            }
        });
        View_Reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeUserToReports();
            }
        });

        Report_button.setVisibility(View.INVISIBLE);
        Apply_water_button.setVisibility(View.INVISIBLE);
        View_Reports.setVisibility(View.INVISIBLE);

        //add profile image and username to the nav drawer
        UserRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(current_user_ID).exists())
                {
                    if(dataSnapshot.child(current_user_ID).hasChild("fullname"))
                    {
                        String fullname = dataSnapshot.child(current_user_ID).child("fullname").getValue().toString();
                        navusername.setText(fullname);
                    }else
                    {
                        Toast.makeText(MainActivity.this, "User has no username in the database",Toast.LENGTH_SHORT).show();
                    }

                    if(dataSnapshot.child(current_user_ID).hasChild("profileimage"))
                    {
                        String image =    dataSnapshot.child(current_user_ID).child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile_pic).into(NavProfileImage);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Profile name do not exist",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            //
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(MainActivity.this, "Erooorrrrrrrrr",Toast.LENGTH_SHORT).show();
            }
        });

        //add profile image and username to the nav drawer
        TechnicianRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(current_user_ID).exists())
                {
                    if(dataSnapshot.child(current_user_ID).hasChild("fullname"))
                    {
                        String fullname = dataSnapshot.child(current_user_ID).child("fullname").getValue().toString();
                        navusername.setText(fullname);

                    }else
                    {
                        Toast.makeText(MainActivity.this, "User has no username in the database",Toast.LENGTH_SHORT).show();
                    }

                    if(dataSnapshot.child(current_user_ID).hasChild("profileimage"))
                    {
                        String image =    dataSnapshot.child(current_user_ID).child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile_pic).into(NavProfileImage);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Profile name do not exist",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            //
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(MainActivity.this, "Erooorrrrrrrrr",Toast.LENGTH_SHORT).show();
            }
        });

       // Displaybuttons();

    }

    private void takeUserToReports()
    {
        final String User = "technician";
        Intent gotosetupIntenet = new Intent(this, dashboard.class);
        gotosetupIntenet.putExtra("Members", User);
        startActivity(gotosetupIntenet);

    }




    private void takeUserToApplywaterActivity() {
        startActivity(new Intent(this,Apply_waterActivity.class));
    }

    private void takeUserTouploadActivity()
    {
        startActivity(new Intent(this,Post.class));
    }


    //check user existence in the database
    @Override
    protected void onStart()
    {
        super.onStart();


      FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            sendUserToLoginActivity();

        } else
        {
            //check who the current user is either USER OR TECHNICIAN
            UserRef.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.child(current_user_ID).exists())
                    {
                         String User = "user";

                           Report_button.setVisibility(View.VISIBLE);
                            Apply_water_button.setVisibility(View.VISIBLE);
                            View_Reports.setVisibility(View.INVISIBLE);
                            CheckuserExistence();



                        }else {


                        TechnicianRef.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                if(dataSnapshot.child(current_user_ID).exists())
                                {
                                        String User = "technician";

                                        Report_button.setVisibility(View.INVISIBLE);
                                        Apply_water_button.setVisibility(View.INVISIBLE);
                                        View_Reports.setVisibility(View.VISIBLE);

                                        CheckTechnicianExixtence();

                                }else {

                                    CheckTechnicianExixtence();
                                    CheckuserExistence();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });


        }
    }// end of onstart

    @Override
    protected void onRestart() {
        super.onRestart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            sendUserToLoginActivity();

        }
    }

    //check if user exist in the database
    private void CheckTechnicianExixtence()
    {
        TechnicianRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.hasChild(current_user_ID))
                {
                    final String User = "technician";
                    Intent gotosetupIntenet = new Intent(MainActivity.this, SetupActivity.class);
                    gotosetupIntenet.putExtra("Members", User);
                    startActivity(gotosetupIntenet);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Toast.makeText(getApplicationContext(),"Database Error ocured",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //check if user exist in the database
    private void CheckuserExistence()
    {
        UserRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.hasChild(current_user_ID))
                {

                    Toast.makeText(MainActivity.this, "seeeeeeeeeeeeeeeeen", Toast.LENGTH_SHORT).show();

                    final String User = "user";
                    Intent gotosetupIntenet = new Intent(MainActivity.this, SetupActivity.class);
                    gotosetupIntenet.putExtra("Members", User);
                    startActivity(gotosetupIntenet);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Toast.makeText(getApplicationContext(),"Database Error ocured",Toast.LENGTH_SHORT).show();
            }
        });

    }




    private void sendUserToLoginActivity()
    {
        startActivity(new Intent(this, User_Login.class));
    }

    //anable onclick to the mtoogle
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
/*    @Override
   public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_report:
                startActivity(new Intent(this,Post.class));
                break;
            case R.id.nav_view_reports:
                final String User = "user";
                Intent viewreportsIntenet = new Intent(this, dashboard.class);
                viewreportsIntenet.putExtra("Members", User);
                startActivity(viewreportsIntenet);
                break;
            case R.id.nav_profile:
                startActivity(new Intent(this,ProfileActivity.class));
                break;
            case R.id.nav_chats:
                startActivity(new Intent(this,TechniciansActivity.class));

                break;
            case R.id.nav_settings:
                Intent settingIntent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.nav_locate:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new LocateUsFragment()).commit();
                break;

            case R.id.nav_logout:
                AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);

                ab.setTitle("confirm");
                ab.setIcon(R.drawable.ic_launch_black_24dp);
                ab.setMessage("Are you sure you want to logout?");

                ab.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        Login();
                    }
                });

                ab.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(MainActivity.this, "cancle", Toast.LENGTH_SHORT).show();
                    }
                });
                ab.show();
                break;
            case R.id.share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.send:
                Toast.makeText(this, "Send successful", Toast.LENGTH_SHORT).show();
                break;

        }
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_activity);
        //drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void Login () {
        startActivity(new Intent(this, User_Login.class));
    }
}
