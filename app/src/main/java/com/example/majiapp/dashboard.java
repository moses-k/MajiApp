package com.example.majiapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class dashboard extends AppCompatActivity {
    private TextView navusername;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private Button Add_post_Button;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    boolean status = false;
    private RecyclerView postList;
    private WebView webView;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, postsRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CircleImageView NavProfileImage;
    String currentUserID;
    private View navView;


    //private ActionBar actionBar;
    private ActionBar actionbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();
        UserRef  = FirebaseDatabase.getInstance().getReference().child("Users");
        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        mAuth.getCurrentUser().getUid();

        postList = (RecyclerView) findViewById(R.id.all_users_post_view);
        postList.setLayoutManager(new LinearLayoutManager(this));
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //display new post at the top and old at the bottom
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
       postList.setLayoutManager(linearLayoutManager);
       Add_post_Button = (Button) findViewById(R.id.post_button);


      //  View navView = navigationView.inflateHeaderView(R.layout.header);
      //  navView.findViewById(R.id.naigation_view);


//app:headerLayout="@layout/header"

        mToolbar = (Toolbar) findViewById(R.id.nav_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Report centre");

        //display the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



      /* Add_post_Button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               SendUsersToPostActivity();
           }
       });

       */



    }

    @Override
    protected void onStart() {
        super.onStart();
        DisplAllUsersPost();
    }


    private void DisplAllUsersPost()
    {
        //with the help of firebase recycler we will retrieve all the post
//Posts.class, all_post_layout,PostViewHolder.class,postsRef
        FirebaseRecyclerOptions<Posts> options =
                new FirebaseRecyclerOptions.Builder<Posts >()
                        .setQuery(postsRef,Posts.class)
                        .build();

        FirebaseRecyclerAdapter<Posts, PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter <Posts, PostViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Posts model)
                    {
                        //get the key of that post and store in postkey
                        final String Postkey = getRef(position).getKey();

                        holder.setFullname(model.getUserfullname());
                        holder.setTime(model.getTime());
                        holder.setDate(model.getDate());
                        holder.setDescription(model.getDescription());
                        holder.setProfileimage(getApplicationContext(),model.getProfileimage());
                        holder.setPostimage(getApplicationContext(), model.getPostimage());

                        //when post is clicked take the user to the click activity together with the postkey
                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                 Intent clickpostIntent = new Intent(dashboard.this, ClickPostActivity.class);
                                 clickpostIntent.putExtra("Postkey", Postkey);
                                 startActivity(clickpostIntent);
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from( parent.getContext() ).inflate(R.layout.all_post_layout, parent, false);
                        PostViewHolder viewHolder  =  new PostViewHolder(view);
                        return viewHolder;
                    }
                };


                     // postList.setAdapter(firebaseRecyclerAdapter);
        postList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }

     public static class PostViewHolder extends RecyclerView.ViewHolder
     {

        //TextView username, postTime,postDate,postDescription,Postimage;
       // CircleImageView profileImage;


        View mView;

         public PostViewHolder(View itemView) {
             super(itemView);
             mView = itemView;

             /*  username = (TextView) mView.findViewById(R.id.post_username);
               profileImage = (CircleImageView) mView.findViewById(R.id.post_profile_image);
             postTime = (TextView) mView.findViewById(R.id.post_time);
             postDate = (TextView) mView.findViewById(R.id.post_date);
             postDescription = (TextView) mView.findViewById(R.id.post_decription);
             Postimage = (ImageView) mView.findViewById(R.id.post_image);*/
         }


         //access our all_post_layout in the view
         public void setFullname(String userfullname)
         {
             TextView  username = (TextView) mView.findViewById(R.id.post_username);
             username.setText(userfullname);
         }

         public void setProfileimage(Context ctx, String profileimage)
         {
             CircleImageView image = (CircleImageView) mView.findViewById(R.id.post_profile_image);

                     Picasso.get().load(profileimage).into(image);

         }
         public void setTime(String time)
         {
             TextView postTime = (TextView) mView.findViewById(R.id.post_time);
             postTime.setText("  "+time);
         }

         public void setDate(String date)
         {
             TextView postDate = (TextView) mView.findViewById(R.id.post_date);
             postDate.setText("  "+date);
         }
         public void setDescription(String description)
         {
             TextView postDescription = (TextView) mView.findViewById(R.id.post_description);
             postDescription.setText(description);
         }


         public void setPostimage(Context ctx, String postimage)
         {
             ImageView Postimage = (ImageView) mView.findViewById(R.id.click_post_image);

             Picasso.get().load(postimage).into(Postimage);
         }

     }


    //check if user is authenticated

    private void sendUserToLoginActivity() {
        startActivity(new Intent(this,Login.class));
    }

    //check for internet connectivity
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else
            return false;
    }
    //alert box
    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // finish();
            }
        });

        return builder;
    }



    public void onOptionsItemselected (Menu menu) {
        super.onOptionsMenuClosed(menu);
    }

    public void Login () {
        startActivity(new Intent(this, Login.class));
    }




}
