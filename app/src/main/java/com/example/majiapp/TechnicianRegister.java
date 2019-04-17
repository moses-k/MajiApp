package com.example.majiapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;

public class TechnicianRegister extends AppCompatActivity {


    EditText name, username, Email, phonenumber, residence, Password, confirmpassword;
    ProgressBar mprogressBar;
    RelativeLayout RegLayout;
    private TextView TaketechnicianToLogin;
    private DatabaseReference TechnicianRef;
    Button technicianRegisterButton;
    Handler handler;
    Runnable runnable;
    Timer timer;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog loadingBar;
    private String currentUserID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_register);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
       // currentUserID = mAuth.getUid();

//        TechnicianRef  = FirebaseDatabase.getInstance().getReference().child("technicians").child(currentUserID);


        Email = (EditText) findViewById(R.id.technicianEmail);
        Password = (EditText) findViewById(R.id.technicianPassword);
        confirmpassword = (EditText) findViewById(R.id.technicianconfirmPassword);
        technicianRegisterButton = (Button)  findViewById(R.id.technicianRegbutton);
        TaketechnicianToLogin = (TextView) findViewById(R.id.taketechniciantologin);
        loadingBar = new ProgressDialog(this);

        technicianRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                TechncianRegister(view);

            }
        });

        TaketechnicianToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final String User = "technician";
                Intent userloginIntenet = new Intent(TechnicianRegister.this, TechnicianLogin.class);
                userloginIntenet.putExtra("Members", User);
                startActivity(userloginIntenet);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {
            GoToMAinActivity();
        }
    }


    private void GoToMAinActivity() {

        final String User = "technician";
        Intent gotosetupIntenet = new Intent(this, MainActivity.class);
        gotosetupIntenet.putExtra("Members", User);
        startActivity(gotosetupIntenet);
    }


    public void TechncianRegister (View view){

        if (!isConnected(TechnicianRegister.this)) buildDialog(TechnicianRegister.this).show();
        else {

            String username = Email.getText().toString();
            String password = Password.getText().toString();
            String str_confirmpassword = confirmpassword.getText().toString();

            //check if username is empty
            if (username.isEmpty()) {
                Email.setError("username is required");


                return;
            }
            //validate username
            if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                Email.setError("Please enter a valid username");
                Email.requestFocus();
                return;
            }
            //check if password is empty
            if (password.isEmpty()) {
                Password.setError("Password is required");
                Password.requestFocus();
                return;
            }

            //validate  password if the match
            if (!str_confirmpassword.matches(password)) {
                confirmpassword.setError("Password does not match");
                confirmpassword.requestFocus();
                return;
            }

            if (password.length() < 6) {
                Password.setError("Minimun length of password should be 6");
                Password.requestFocus();
                return;

            }

            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait while we register you");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                GoToSetupActivity();
                                Toast.makeText(getApplicationContext(), "User registered successfull", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(), "Error occured: "+message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });

        }
    }


    private void GoToSetupActivity() {

        //TechnicianRef.child("Memmber").setValue("technician");
        final String User = "technician";
        Intent gotosetupIntenet = new Intent(this, SetupActivity.class);
        gotosetupIntenet.putExtra("Members", User);
        startActivity(gotosetupIntenet);
    }


    public boolean isConnected (Context context){

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

    public AlertDialog.Builder buildDialog (Context c){

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                ;

                // finish();
            }
        });

        return builder;


    }

}
