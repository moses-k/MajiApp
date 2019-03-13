package com.example.majiapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private View navView;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, postsRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String current_user_ID;
    private CircleImageView NavProfileImage;
    private TextView navusername;
    private Button Report_button, Apply_water_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        mAuth = FirebaseAuth.getInstance();
        UserRef  = FirebaseDatabase.getInstance().getReference().child("Users");
        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        current_user_ID = mAuth.getCurrentUser().getUid();

        mToolbar = (Toolbar) findViewById(R.id.mainActivity_toolbar);
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

        Report_button =(Button) findViewById(R.id.repport_water);
        Apply_water_button = (Button) findViewById(R.id.apply_water);

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



        //add profile image and username to the nav drawer
        UserRef.child(current_user_ID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("Fullname"))
                    {
                        String fullname = dataSnapshot.child("Fullname").getValue().toString();
                        navusername.setText(fullname);

                    }else
                    {
                        Toast.makeText(MainActivity.this, "User has no username in the database",Toast.LENGTH_SHORT).show();
                    }

                    if(dataSnapshot.hasChild("profileimage"))
                    {
                        String image =    dataSnapshot.child("profileimage").getValue().toString();
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
    protected void onStart() {
        super.onStart();

      /*  mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                if (firebaseAuth.getCurrentUser() == null)
                {
                    sendUserToLoginActivity();

                }else {
                    checkUserExistence();

                }
            }
        };*/
      FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            sendUserToLoginActivity();

        } else {

            checkUserExistence();
        }
    }


    //check if user exist in the database
    private void checkUserExistence()
    {
        final  String current_user_id = mAuth.getCurrentUser().getUid();

        UserRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.hasChild(current_user_id))
                {
                    sendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Toast.makeText(getApplicationContext(),"Database Error ocured",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendUserToSetupActivity()
    {
        startActivity(new Intent(this,SetupActivity.class));
    }

    private void sendUserToLoginActivity()
    {
        startActivity(new Intent(this,Login.class));
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
                Intent dashIntent = new Intent(MainActivity.this,dashboard.class);
                startActivity(dashIntent);                break;
            case R.id.nav_profile:
                startActivity(new Intent(this,ProfileActivity.class));
                break;
            case R.id.nav_notifications:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new NotificationFragment()).commit();
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
            case R.id.menu_logout2:
                ab = new AlertDialog.Builder(MainActivity.this);

                ab.setTitle("confirm");
                ab.setIcon(R.drawable.ic_launch_black_24dp);
                ab.setMessage("Are you sure you want to logout?");

                ab.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
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
            case R.id.menu_about2:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new AboutFragment()).commit();
                break;
            case R.id.menu_settings2:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new SettingsFragment()).commit();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_activity);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void Login () {
        startActivity(new Intent(this, Login.class));
    }
}
