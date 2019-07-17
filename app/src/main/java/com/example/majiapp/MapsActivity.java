package com.example.majiapp;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;


public  class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        NavigationView.OnNavigationItemSelectedListener {

private GoogleMap mMap;
private GoogleApiClient googleApiClient;
private LocationRequest locationRequest;
private Location lastLocation;
private Marker currentUserLocationMarker;
private static final int request_user_location_code = 99;
private DrawerLayout drawerLayout;
private FirebaseAuth firebaseAuth;

private static final LatLng Plumber_Logisticss = new LatLng (-1.290367, 36.82446);
private static final LatLng Vinny_Plumbers = new LatLng (-1.2930966, 36.8230223);
private static final LatLng Rick_Water_and_Sewers = new LatLng (-1.2942229, 36.8242561);
private static final LatLng Leak_Experts = new LatLng (-1.292555, 36.817336);
private static final LatLng Sagoo_Engineering = new LatLng (-1.292555, 36.817336);
private static final LatLng Central_Motors = new LatLng (-1.2981325, 36.8195247);
private static final LatLng Unity_Auto_Motors = new LatLng (-1.2688076, 36.8585718);
private static final LatLng Subaru_Masters_AutoTech_Garage = new LatLng (-1.2686355, 36.8585718);
private static final LatLng MotorZone_AutoRepair_Shop = new LatLng (-1.3041581, 36.7116811);
private static final LatLng Kobil_Koinange = new LatLng (-1.2681192, 36.8585716);
private static final LatLng Sonon_Benz_Center = new LatLng (-1.2519886, 36.8361971);
private static final LatLng JDM_Auto_Garage = new LatLng (-1.303131, 36.7910502);
private static final LatLng OILLIBYA_Buruburu = new LatLng (-1.2849395,36.8389437);
private static final LatLng Auto_Express = new LatLng (-1.2681209,36.8181727);
private static final LatLng BoganiK_Ltd = new LatLng (-1.3146293,36.7668459);

private Marker Plumber_Logistics;
private Marker mNumerical_machining_complex;
private Marker mStation_AutoMech;
private Marker mMars_Mechanical_Engineering;
private Marker mCentral_Motors;
private Marker mSonon_Benz_Center;
private Marker mOILLIBYA_Buruburu;
private Marker mSubaru_Masters_AutoTech_Garage;
private Marker James_Maingi;
private Marker mUnity_Auto_Motors;
private Marker mBoganiK_Ltd;
private Marker mMotorZone_AutoRepair_Shop;
private Marker mKobil_Koinange;

        String phone = "0721330877";
        String JDM = "0791062710";
        String Bogani = "0735558853";
        String other = "0736 225692";
        String Oil_Libya ="0722 899202";
        String Sonon_Benz="0722 519688";
        String Kobil_Koinange_c="020 2755000";
        String MotorZone_AutoRepair ="0713 056010";
        String Auto_Planet_c="0720392868";
        String Mars_Mechanical_Eng="0791062710";
        String Central_Station ="0726646630";
        String Subaru_Masters="0712340011";
        String Unity_Auto_c="0722511028";
        CircleImageView header_profile;
        TextView username, user_email;
        DatabaseReference reff, reff2;
        String currentUserID;

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
    private Button Report_button, Apply_water_button;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        //request permission  to access location from user
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission ();
        }

       SupportMapFragment mapa = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapa.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();
        UserRef  = FirebaseDatabase.getInstance().getReference().child("users");
        TechnicianRef  = FirebaseDatabase.getInstance().getReference().child("technicians");
        postsRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        current_user_ID = mAuth.getCurrentUser().getUid();
        /**
         * Toolbar and drawer toogle
         * */
        mToolbar = (Toolbar) findViewById(R.id.map_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Map");
        drawerLayout = findViewById (R.id.maps_activity);
        NavigationView navigationView = findViewById (R.id.nav_view);
        navigationView.setNavigationItemSelectedListener (this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (this, drawerLayout, mToolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener (toggle);
        toggle.syncState ();
        if (savedInstanceState == null) {
            navigationView.setCheckedItem (R.id.nav_report);
        }


        //inflate the layout
        navView = navigationView.inflateHeaderView(R.layout.header);
        NavProfileImage = (CircleImageView) navView.findViewById(R.id.nav_profile_image);
        navusername = (TextView) navView.findViewById(R.id.nav_username);

        //add onclick listener
        navigationView.setNavigationItemSelectedListener(this);

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
                        Toast.makeText(MapsActivity.this, "User has no username in the database",Toast.LENGTH_SHORT).show();
                    }

                    if(dataSnapshot.child(current_user_ID).hasChild("profileimage"))
                    {
                        String image =    dataSnapshot.child(current_user_ID).child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile_pic).into(NavProfileImage);
                    }
                    else
                    {
                        Toast.makeText(MapsActivity.this, "Profile name do not exist",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(MapsActivity.this, "Erooorrrrrrrrr",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MapsActivity.this, "User has no username in the database",Toast.LENGTH_SHORT).show();
                    }

                    if(dataSnapshot.child(current_user_ID).hasChild("profileimage"))
                    {
                        String image =    dataSnapshot.child(current_user_ID).child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile_pic).into(NavProfileImage);
                    }
                    else
                    {
                        Toast.makeText(MapsActivity.this, "Profile name do not exist",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            //
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(MapsActivity.this, "Erooorrrrrrrrr",Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            sendUserToLoginActivity();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            sendUserToLoginActivity();
        }
    }
    private void sendUserToLoginActivity()
    {
        startActivity(new Intent(this, User_Login.class));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener (new GoogleMap.OnMarkerClickListener () {

            public Button help = findViewById (R.id.request_btn);

            @Override
            public boolean onMarkerClick(final Marker marker) {
                if (marker.equals (currentUserLocationMarker)) {
                    // let help button remain invisible

                } else {

                    //set help button to visible
                    help.setVisibility (View.VISIBLE);
                    help.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick(View v) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                if (checkPermission ()) {
                                    Log.e ("permission", "Permission already granted.");
                                } else {

                                    //If the app NO CALL_PHONE permission, request it//

                                    requestPermission ();
                                }
                            }
                            if (marker.equals (Plumber_Logistics)){
                                if (!TextUtils.isEmpty (Auto_Planet_c)) {
                                    String dial = "tel:" + Auto_Planet_c;

                                    //Make an Intent object of type intent.ACTION_CALL//

                                    startActivity (new Intent(Intent.ACTION_CALL,

                                            //Extract the telephone number from the URI//

                                            Uri.parse (dial)));
                                } else {
                                    Toast.makeText (MapsActivity.this, "Sorry can not get contact information", Toast.LENGTH_SHORT).show ();
                                }

                            } else if (marker.equals (mMotorZone_AutoRepair_Shop)){
                                if (!TextUtils.isEmpty (MotorZone_AutoRepair)) {
                                    String dial = "tel:" + MotorZone_AutoRepair;

                                    //Make an Intent object of type intent.ACTION_CALL//

                                    startActivity (new Intent (Intent.ACTION_CALL,

                                            //Extract the telephone number from the URI//

                                            Uri.parse (dial)));
                                } else {
                                    Toast.makeText (MapsActivity.this, "Sorry can not get contact information", Toast.LENGTH_SHORT).show ();
                                }

                            }else if (marker.equals (mMars_Mechanical_Engineering)){
                                if (!TextUtils.isEmpty (Mars_Mechanical_Eng)) {
                                    String dial = "tel:" + Mars_Mechanical_Eng;

                                    //Make an Intent object of type intent.ACTION_CALL//

                                    startActivity (new Intent (Intent.ACTION_CALL,

                                            //Extract the telephone number from the URI//

                                            Uri.parse (dial)));
                                } else {
                                    Toast.makeText (MapsActivity.this, "Sorry can not get contact information", Toast.LENGTH_SHORT).show ();
                                }

                            }else if (marker.equals (mNumerical_machining_complex)){
                                if (!TextUtils.isEmpty (phone)) {
                                    String dial = "tel:" + phone;

                                    //Make an Intent object of type intent.ACTION_CALL//

                                    startActivity (new Intent (Intent.ACTION_CALL,

                                            //Extract the telephone number from the URI//

                                            Uri.parse (dial)));
                                } else {
                                    Toast.makeText (MapsActivity.this, "Sorry can not get contact information", Toast.LENGTH_SHORT).show ();
                                }

                            }else if (marker.equals (mOILLIBYA_Buruburu)){
                                if (!TextUtils.isEmpty (Oil_Libya)) {
                                    String dial = "tel:" + Oil_Libya;

                                    //Make an Intent object of type intent.ACTION_CALL//

                                    startActivity (new Intent (Intent.ACTION_CALL,

                                            //Extract the telephone number from the URI//

                                            Uri.parse (dial)));
                                } else {
                                    Toast.makeText (MapsActivity.this, "Sorry can not get contact information", Toast.LENGTH_SHORT).show ();
                                }

                            }else if (marker.equals (mSonon_Benz_Center)){
                                if (!TextUtils.isEmpty (Sonon_Benz)) {
                                    String dial = "tel:" + Sonon_Benz;

                                    //Make an Intent object of type intent.ACTION_CALL//

                                    startActivity (new Intent (Intent.ACTION_CALL,

                                            //Extract the telephone number from the URI//

                                            Uri.parse (dial)));
                                } else {
                                    Toast.makeText (MapsActivity.this, "Sorry can not get contact information", Toast.LENGTH_SHORT).show ();
                                }

                            } else if (marker.equals (James_Maingi)) {
                                if (!TextUtils.isEmpty (JDM)) {
                                    String dial = "tel:" + JDM;

                                    //Make an Intent object of type intent.ACTION_CALL//

                                    startActivity (new Intent (Intent.ACTION_CALL,

                                            //Extract the telephone number from the URI//

                                            Uri.parse (dial)));
                                } else {
                                    Toast.makeText (MapsActivity.this, "Sorry can not get contact information", Toast.LENGTH_SHORT).show ();
                                }

                            }else if (marker.equals (mSubaru_Masters_AutoTech_Garage)){
                                if (!TextUtils.isEmpty (Subaru_Masters)) {
                                    String dial = "tel:" + Subaru_Masters;

                                    //Make an Intent object of type intent.ACTION_CALL//

                                    startActivity (new Intent (Intent.ACTION_CALL,

                                            //Extract the telephone number from the URI//

                                            Uri.parse (dial)));
                                } else {
                                    Toast.makeText (MapsActivity.this, "Sorry can not get contact information", Toast.LENGTH_SHORT).show ();
                                }

                            }else if (marker.equals (mUnity_Auto_Motors)){
                                if (!TextUtils.isEmpty (Unity_Auto_c)) {
                                    String dial = "tel:" + Unity_Auto_c;

                                    //Make an Intent object of type intent.ACTION_CALL//

                                    startActivity (new Intent (Intent.ACTION_CALL,

                                            //Extract the telephone number from the URI//

                                            Uri.parse (dial)));
                                } else {
                                    Toast.makeText (MapsActivity.this, "Sorry can not get contact information", Toast.LENGTH_SHORT).show ();
                                }

                            }else if (marker.equals (mBoganiK_Ltd)){
                                if (!TextUtils.isEmpty (Bogani)) {
                                    String dial = "tel:" + Bogani;

                                    //Make an Intent object of type intent.ACTION_CALL//

                                    startActivity (new Intent (Intent.ACTION_CALL,

                                            //Extract the telephone number from the URI//

                                            Uri.parse (dial)));
                                } else {
                                    Toast.makeText (MapsActivity.this, "Sorry can not get contact information", Toast.LENGTH_SHORT).show ();
                                }

                            }else if (marker.equals (mCentral_Motors)){
                                if (!TextUtils.isEmpty (Central_Station)) {
                                    String dial = "tel:" + Central_Station;

                                    //Make an Intent object of type intent.ACTION_CALL//

                                    startActivity (new Intent (Intent.ACTION_CALL,

                                            //Extract the telephone number from the URI//

                                            Uri.parse (dial)));
                                } else {
                                    Toast.makeText (MapsActivity.this, "Sorry can not get contact information", Toast.LENGTH_SHORT).show ();
                                }
                            }else if (marker.equals (mKobil_Koinange)){
                                if (!TextUtils.isEmpty (Kobil_Koinange_c)) {
                                    String dial = "tel:" + Kobil_Koinange_c;

                                    //Make an Intent object of type intent.ACTION_CALL//

                                    startActivity (new Intent (Intent.ACTION_CALL,

                                            //Extract the telephone number from the URI//

                                            Uri.parse (dial)));
                                } else {
                                    Toast.makeText (MapsActivity.this, "Sorry can not get contact information", Toast.LENGTH_SHORT).show ();
                                }
                            }else {
                                if (!TextUtils.isEmpty (other)) {
                                    String dial = "tel:" + other;

                                    //Make an Intent object of type intent.ACTION_CALL//

                                    startActivity (new Intent (Intent.ACTION_CALL,

                                            //Extract the telephone number from the URI//

                                            Uri.parse (dial)));
                                } else {
                                    Toast.makeText (MapsActivity.this, "Sorry can not get contact information", Toast.LENGTH_SHORT).show ();
                                }
                            }
                        }
                    });
                }
                return false;
            }
        });
        mMap.setOnMapClickListener (new GoogleMap.OnMapClickListener () {
            @Override
            public void onMapClick(LatLng latLng) {
                Button help = findViewById (R.id.request_btn);
                help.setVisibility (View.GONE);
            }
        });
        if (mMap != null) {

            mMap.setInfoWindowAdapter (new GoogleMap.InfoWindowAdapter () {
                @Override
                public View getInfoWindow(Marker marker) {

                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    View v = getLayoutInflater ().inflate (R.layout.info_window, null);
                    TextView shop1 = v.findViewById (R.id.shop);
                    shop1.setText (marker.getTitle ());

                    if (marker.equals (currentUserLocationMarker)) {
                        //different custom window information
                        View view = getLayoutInflater ().inflate (R.layout.user_location, null);
                        TextView text = view.findViewById (R.id.locate_me);
                        text.setText (marker.getTitle ());
                        return view;
                    }
                    if (marker.equals (mNumerical_machining_complex)) {
                        View view = getLayoutInflater ().inflate (R.layout.info_window1, null);
                        TextView shop = view.findViewById (R.id.shop);
                        shop.setText (marker.getTitle ());
                        return view;
                    }
                    if (marker.equals (Plumber_Logistics)) {
                        View view = getLayoutInflater ().inflate (R.layout.info_window3, null);
                        TextView shop = view.findViewById (R.id.shop);
                        shop.setText (marker.getTitle ());
                        return view;
                    }
                    if (marker.equals (mCentral_Motors)) {
                        View view = getLayoutInflater ().inflate (R.layout.info_window1, null);
                        TextView shop = view.findViewById (R.id.shop);
                        shop.setText (marker.getTitle ());
                        return view;
                    }
                    if (marker.equals (mMars_Mechanical_Engineering)) {
                        View view = getLayoutInflater ().inflate (R.layout.info_window2, null);
                        TextView shop = view.findViewById (R.id.shop);
                        shop.setText (marker.getTitle ());
                        return view;
                    }
                    if (marker.equals (mStation_AutoMech)) {
                        View view = getLayoutInflater ().inflate (R.layout.info_window3, null);
                        TextView shop = view.findViewById (R.id.shop);
                        shop.setText (marker.getTitle ());
                        return view;
                    }
                    if (marker.equals (mBoganiK_Ltd)) {
                        View view = getLayoutInflater ().inflate (R.layout.info_window2, null);
                        TextView shop = view.findViewById (R.id.shop);
                        shop.setText (marker.getTitle ());
                        return view;
                    }
                    if (marker.equals (mKobil_Koinange)) {
                        View view = getLayoutInflater ().inflate (R.layout.info_window1, null);
                        TextView shop = view.findViewById (R.id.shop);
                        shop.setText (marker.getTitle ());
                        return view;
                    }
                    if (marker.equals (mMotorZone_AutoRepair_Shop)) {
                        View view = getLayoutInflater ().inflate (R.layout.info_window3, null);
                        TextView shop = view.findViewById (R.id.shop);
                        shop.setText (marker.getTitle ());
                        return view;
                    }
                    if (marker.equals (mSonon_Benz_Center)) {
                        View view = getLayoutInflater ().inflate (R.layout.info_window2, null);
                        TextView shop = view.findViewById (R.id.shop);
                        shop.setText (marker.getTitle ());
                        return view;
                    }
                    if (marker.equals (James_Maingi)) {
                        View view = getLayoutInflater ().inflate (R.layout.info_window1, null);
                        TextView shop = view.findViewById (R.id.shop);
                        shop.setText (marker.getTitle ());
                        return view;
                    }
                    if (marker.equals (mSubaru_Masters_AutoTech_Garage)) {
                        View view = getLayoutInflater ().inflate (R.layout.info_window3, null);
                        TextView shop = view.findViewById (R.id.shop);
                        shop.setText (marker.getTitle ());
                        return view;
                    }

                    return v;
                }
            });
        }

        //New marker
        James_Maingi = mMap.addMarker (new MarkerOptions ()
                .position (JDM_Auto_Garage)
                .title ("Plumber Logistics")
                .icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera (CameraUpdateFactory.zoomBy (10));
        James_Maingi.setTag (0);

        //New marker
        mSubaru_Masters_AutoTech_Garage = mMap.addMarker (new MarkerOptions ()
                .position (Subaru_Masters_AutoTech_Garage)
                .title ("Vinny Plumbers")
                .icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera (CameraUpdateFactory.zoomBy (10));
        mSubaru_Masters_AutoTech_Garage.setTag (0);

        //New marker
        mSonon_Benz_Center = mMap.addMarker (new MarkerOptions ()
                .position (Sonon_Benz_Center)
                .title ("Leak Experts")
                .icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera (CameraUpdateFactory.zoomBy (10));
        mSonon_Benz_Center.setTag (0);

        //New marker
        mOILLIBYA_Buruburu = mMap.addMarker (new MarkerOptions ()
                .position (OILLIBYA_Buruburu)
                .title ("Home Water Repair")
                .icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera (CameraUpdateFactory.zoomBy (10));
        mOILLIBYA_Buruburu.setTag (0);

        //New Marker
        mUnity_Auto_Motors = mMap.addMarker (new MarkerOptions ()
                .position (Unity_Auto_Motors)
                .title ("Seawarage Master")
                .icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera (CameraUpdateFactory.zoomBy (10));
        mUnity_Auto_Motors.setTag (0);
        //New marker

        mMotorZone_AutoRepair_Shop = mMap.addMarker (new MarkerOptions ()
                .position (MotorZone_AutoRepair_Shop)
                .title ("Moses Leakage Atendees")
                .icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera (CameraUpdateFactory.zoomBy (10));
        mMotorZone_AutoRepair_Shop.setTag (0);
        //New marker

        mBoganiK_Ltd = mMap.addMarker (new MarkerOptions ()
                .position (BoganiK_Ltd)
                .title ("Robert Otieno")
                .icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera (CameraUpdateFactory.zoomBy (10));
        mBoganiK_Ltd.setTag (0);
        //New marker

        mKobil_Koinange = mMap.addMarker (new MarkerOptions ()
                .position (Kobil_Koinange)
                .title ("James Mwangi")
                .icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera (CameraUpdateFactory.zoomBy (10));
        mKobil_Koinange.setTag (0);
        //New marker

        mNumerical_machining_complex = mMap.addMarker (new MarkerOptions ()
                .position (Plumber_Logisticss)
                .title ("Steve Nyambega Water Repair")
                .icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera (CameraUpdateFactory.zoomBy (10));
        mNumerical_machining_complex.setTag (0);

        //New marker
        Marker mAuto_Express = mMap.addMarker (new MarkerOptions ()
                .position (Auto_Express)
                .title ("La Mada Water and Sewarage")
                .icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera (CameraUpdateFactory.zoomBy (10));
        mAuto_Express.setTag (0);

        //New marker
        Plumber_Logistics = mMap.addMarker (new MarkerOptions ()
                .position (Rick_Water_and_Sewers)
                .title ("Auto Water Reapiar")
                .icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera (CameraUpdateFactory.zoomBy (10));
        Plumber_Logistics.setTag (0);

        //New marker
        mCentral_Motors = mMap.addMarker (new MarkerOptions ()
                .position (Central_Motors)
                .title ("Jacob Koech")
                .icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera (CameraUpdateFactory.zoomBy (10));
        mCentral_Motors.setTag (0);

        //New marker
        mMars_Mechanical_Engineering = mMap.addMarker (new MarkerOptions ()
                .position (Leak_Experts)
                .title ("Moses Water Master")
                .icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera (CameraUpdateFactory.zoomBy (10));
        mMars_Mechanical_Engineering.setTag (0);

        //New marker
        Marker mSagoo_Engineering = mMap.addMarker (new MarkerOptions ()
                .position (Sagoo_Engineering)
                .title ("Sagoo Sewarage Engineer")
                .icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera (CameraUpdateFactory.zoomBy (10));
        mSagoo_Engineering.setTag (0);

        //New marker
        mStation_AutoMech = mMap.addMarker (new MarkerOptions ()
                .position (Vinny_Plumbers)
                .title ("Steve Nyambega Repair ")
                .icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera (CameraUpdateFactory.zoomBy (10));
        mStation_AutoMech.setTag (0);

        // Check location permission if granted
        if (ContextCompat.checkSelfPermission (this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient ();
            mMap.setMyLocationEnabled (true);
        }
    }

    public boolean checkUserLocationPermission() {
        if (ContextCompat.checkSelfPermission (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale (this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions (this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, request_user_location_code);
            } else {
                ActivityCompat.requestPermissions (this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, request_user_location_code);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case request_user_location_code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission (this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient ();
                        }
                        mMap.setMyLocationEnabled (true);
                    }
                } else {
                    Toast.makeText (this, "Permission Denied...", Toast.LENGTH_SHORT).show ();
                }
                return;
        }
    }
    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder (this)
                .addConnectionCallbacks (this)
                .addOnConnectionFailedListener (this)
                .addApi (LocationServices.API)
                .build ();
        googleApiClient.connect ();
    }

    @Override
    public void onLocationChanged(Location location) {

        lastLocation = location;

        if (currentUserLocationMarker != null) {
            currentUserLocationMarker.remove ();
        }

        LatLng latLng = new LatLng (location.getLatitude (), location.getLongitude ());
        currentUserLocationMarker = mMap.addMarker (new MarkerOptions ()

                .position (latLng)
                .title ("Your Location")
                .icon (BitmapDescriptorFactory.defaultMarker (BitmapDescriptorFactory.HUE_GREEN)));



        mMap.moveCamera (CameraUpdateFactory.newLatLng (latLng));

        CameraPosition cameraPosition = new CameraPosition.Builder ()
                .target (latLng)     // Sets the center of the map to current location
                .zoom (16)                   // Sets the zoom
                .bearing (90)                // Sets the orientation of the camera to east
                .tilt (30)                   // Sets the tilt of the camera to 30 degrees
                .build ();                   // Creates a CameraPosition from the builder
        mMap.animateCamera (CameraUpdateFactory.newCameraPosition (cameraPosition));

        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates (googleApiClient, (com.google.android.gms.location.LocationListener) this);

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        locationRequest = new LocationRequest ();
        locationRequest.setInterval (1100);
        locationRequest.setFastestInterval (1100);
        locationRequest.setPriority (locationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission (this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates (googleApiClient, locationRequest, (com.google.android.gms.location.LocationListener) this);
        }
    }
    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
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
            case R.id.nav_apply_water:
                startActivity(new Intent(this,Apply_waterActivity.class));
                break;
            case R.id.nav_profile:
                startActivity(new Intent(this,ProfileActivity.class));
                break;
            case R.id.nav_chats:
                startActivity(new Intent(this,TechniciansActivity.class));
                break;
            case R.id.nav_settings:
                Intent settingIntent = new Intent(MapsActivity.this,SettingsActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.nav_locate:
                Toast.makeText(MapsActivity.this, "Our developers are working on it. Sorry temporarily out of service", Toast.LENGTH_SHORT).show();

            case R.id.nav_logout:
                AlertDialog.Builder ab = new AlertDialog.Builder(MapsActivity.this);
                ab.setTitle("confirm");
                ab.setIcon(R.drawable.ic_launch_black_24dp);
                ab.setMessage("Are you sure you want to logout?");

                ab.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(MapsActivity.this, "ok", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        Login();
                    }
                });
                ab.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(MapsActivity.this, "cancle", Toast.LENGTH_SHORT).show();
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.maps_activity);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    public void Login () {
        startActivity(new Intent(this, User_Login.class));
    }

    public boolean checkPermission() {

        int CallPermissionResult = ContextCompat.checkSelfPermission (getApplicationContext (), Manifest.permission.CALL_PHONE);

        return CallPermissionResult == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions (MapsActivity.this, new String[]
                {
                        Manifest.permission.CALL_PHONE
                }, PERMISSION_REQUEST_CODE);

    }

}

