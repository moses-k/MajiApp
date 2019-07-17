package com.example.majiapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TechniciansActivity extends AppCompatActivity
{
    private RecyclerView myFrindList;
    private DatabaseReference FriendsRef, UserRef,TechnicianRef,TechRef;
    private FirebaseAuth mAuth;
    private  String currentUserId;


    String online_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technicians);

        mAuth = FirebaseAuth.getInstance();
        TechnicianRef = FirebaseDatabase.getInstance().getReference().child("technicians");


        TechRef = FirebaseDatabase.getInstance().getReference().child("users");
         myFrindList = (RecyclerView) findViewById(R.id.friend_list);

        myFrindList.setLayoutManager(new LinearLayoutManager(this));
        myFrindList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myFrindList.setLayoutManager(linearLayoutManager);

        DisplayAllFriends();
    }



    private void DisplayAllFriends()
    {


        FirebaseRecyclerOptions<Technicians> options =
                new FirebaseRecyclerOptions.Builder<Technicians>()
                .setQuery(TechnicianRef, Technicians.class)
                .build();


        FirebaseRecyclerAdapter<Technicians, FriendsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Technicians, FriendsViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull final FriendsViewHolder holder, final int position, @NonNull final Technicians model)
            {

                {
                    final String usersID =getRef(position).getKey();
               TechnicianRef.child(usersID).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                   {
                       if (dataSnapshot.exists())
                       {
                           final String userName = dataSnapshot.child("fullname").getValue().toString();

                           holder.setFullname(userName);
                           //holder.setFullname(model.getFullname());
                           // holder.setStatus(model.getStatus());
                           holder.setProfileimage(model.getProfileimage());
                           //create oncline listener
                           holder.mView.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {

                                   String visit_user_id = getRef(position).getKey();
                                   Intent profileIntent = new Intent(TechniciansActivity.this, ChatActivity.class);
                                   profileIntent.putExtra("visitUserId",visit_user_id);
                                   profileIntent.putExtra("userName", userName);
                                   startActivity(profileIntent);
                               }
                           });

                       }

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });



                }

            }

            @NonNull
            @Override
            public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from( parent.getContext() ).inflate(R.layout.all_users_display_layout, parent, false);
                FriendsViewHolder viewHolder  =  new FriendsViewHolder(view);
                return viewHolder;
            }
        };


        myFrindList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }


    public static class FriendsViewHolder extends RecyclerView.ViewHolder
    {

        View mView;
        ImageView onlineStatusView;
        public FriendsViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
            onlineStatusView = (ImageView) itemView.findViewById(R.id.all_user_online_icon);
        }


        private void setProfileimage(String profileimage)
        {
            CircleImageView myImage = mView.findViewById(R.id.all_users_profile_image);
            Picasso.get().load(profileimage).placeholder(R.drawable.profile_image).into(myImage);
        }

        public void setFullname(String Fullname) {
            TextView myName =  mView.findViewById(R.id.all_users_fullname);
            myName.setText(Fullname);
        }

        public void setDate(String date) {
            TextView friendsdate =  mView.findViewById(R.id.all_users_status);
            //friendsdate.setText("Technicians since: " +date);
        }


    }



}
