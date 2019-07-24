package com.example.majiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Splash_screen extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    //private SlideAdapter slideAdapter;
    private TextView[] mDots;

    private Button mNextBtn,mBackBtn;
    private int mCurrentPage;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        firebaseAuth = FirebaseAuth.getInstance();
       if (firebaseAuth.getCurrentUser()  != null){
            //had already logged in
            finish();
            Intent maps = new Intent(getBaseContext(),MapsActivity.class);
            startActivity(maps);
            Toast.makeText (this, "Welcome Back", Toast.LENGTH_SHORT).show ();
        }

        mSlideViewPager = findViewById (R.id.slideViewPager);
        mDotLayout = findViewById (R.id.dotslayout);

        mNextBtn = findViewById (R.id.nextBtn);
        mBackBtn = findViewById (R.id.prevBtn);


        //slideAdapter = new SlideAdapter (this);
        //mSlideViewPager.setAdapter (slideAdapter);

        addDotsIndicator (0);
        mSlideViewPager.addOnPageChangeListener (viewListener);

        mNextBtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                    ShowDriver();

            }
        });
        mBackBtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                mSlideViewPager.setCurrentItem (mCurrentPage -1);
            }
        });


    }


    public void ShowDriver() {
        Intent userlogin = new Intent (this, User_Login.class);
        startActivity (userlogin);

    }

    public void addDotsIndicator(int position) {
        mDots = new TextView[3];
        mDotLayout.removeAllViews ();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView (this);
            mDots[i].setText (Html.fromHtml ("&#8226;"));
            mDots[i].setTextSize (35);
            mDots[i].setTextColor (getResources ().getColor (R.color.colorAccent));

            mDotLayout.addView (mDots[i]);

        }
        if (mDots.length > 0){
            mDots[position].setTextColor (getResources ().getColor (R.color.black));
        }
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener () {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator (i);
            mCurrentPage = i ;

            if (i==0){
                mNextBtn.setEnabled (true);
                mBackBtn.setEnabled (false);
                mBackBtn.setVisibility (View.INVISIBLE);

                mNextBtn.setText ("NEXT");
                mBackBtn.setText ("");
            }else if (i == mDots.length - 1){
                mNextBtn.setEnabled (true);
                mBackBtn.setEnabled (true);
                mBackBtn.setVisibility (View.VISIBLE);

                mNextBtn.setText ("Finish");
                mBackBtn.setText ("BACK");
            }else {
                mNextBtn.setEnabled (true);
                mBackBtn.setEnabled (true);
                mBackBtn.setVisibility (View.VISIBLE);

                mNextBtn.setText ("NEXT");
                mBackBtn.setText ("BACK");
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };


}
