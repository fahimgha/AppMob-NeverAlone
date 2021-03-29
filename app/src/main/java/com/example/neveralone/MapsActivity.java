package com.example.neveralone;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Map;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener,LocationListener {

    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;


    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LatLng mLastLocation;
    Marker mCurrLocationMarker;
    GoogleMap mMap;
    LatLng friendLatLng;
    String latitude,longitude,name,userId,myName,myLat,myLng;
    Marker marker;
    FirebaseUser user;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    ArrayList<String> mKeys;
    MarkerOptions myOptions;
    TextView t1_name,t2_email;
    String current_user_email;
    String current_user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_navigation);
        buildGoogleApiClient();
        Toolbar toolbar = findViewById(R.id.toolbar);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        //setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        //navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.menu_bottom_navigation);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        View header = navigationView.getHeaderView(0);
        t1_name = header.findViewById(R.id.name);
        t2_email = header.findViewById(R.id.email_text);


        mKeys = new ArrayList<>();
        Intent intent = getIntent();

        if(intent!=null)
        {
            latitude=intent.getStringExtra("lat");
            longitude = intent.getStringExtra("lng");
            name = intent.getStringExtra("name");
            userId = intent.getStringExtra("userId");

            //toolbar.setTitle(name +"'s" + "Location");

            if(getSupportActionBar()!=null)
            {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        SupportMapFragment mapFragment =  (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        myName = dataSnapshot.child("name").getValue(String.class);
                        myLat = dataSnapshot.child("lat").getValue(String.class);
                        myLng = dataSnapshot.child("lng").getValue(String.class);
                        current_user_name = dataSnapshot.child(user.getUid()).child("name").getValue(String.class);
                        current_user_email = dataSnapshot.child(user.getUid()).child("email").getValue(String.class);
                        t1_name.setText(current_user_name);
                        t2_email.setText(current_user_email);

                        if(friendLatLng!=null){
                            friendLatLng = new LatLng(Double.parseDouble(myLat),Double.parseDouble(myLng));
                            myOptions = new MarkerOptions();
                            myOptions.position(friendLatLng);
                            //myOptions.snippet("Last seen: "+myDate);
                            myOptions.title(myName);

                            if(marker == null)
                            {
                                marker = mMap.addMarker(myOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(friendLatLng,15));
                            }
                            else
                            {
                                marker.setPosition(friendLatLng);
                            }

                        }

                    }
                    private void setUpViews() {
                        t1_name = header.findViewById(R.id.name);
                        t2_email = header.findViewById(R.id.email_text);
                        t1_name.setText(current_user_name);
                        t2_email.setText(current_user_email);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_signOut:
                        Toast.makeText(getApplicationContext(), "SignOut is selected", Toast.LENGTH_LONG).show();
                        auth.signOut();
                        Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_myCirlce:
                        Toast.makeText(getApplicationContext(), "My Circle is selected", Toast.LENGTH_LONG).show();
                        Intent myIntent2 = new Intent(MapsActivity.this, MyCircleActivity.class);
                        startActivity(myIntent2);
                        break;
                    case R.id.nav_joinedCircle:
                        Toast.makeText(getApplicationContext(), "Join Circle is selected", Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(MapsActivity.this, JoinCircleActivity.class);
                        startActivity(myIntent);
                        break;
                    case R.id.nav_inviteFriends:
                        Toast.makeText(getApplicationContext(), "Invite Friends is selected", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_shareLoc:
                        Intent i = new Intent(Intent.ACTION_SEND);
                        //i.setType("text/plain");
                        //i.putExtra(Intent.EXTRA_TEXT, "Ma géolocalisation  : " + "https://www.google.com/maps/@" + latLng.latitude + "," + latLng.longitude + ",17z");
                        //startActivity(i.createChooser(i, "Partager en utilisant: "));
                        Toast.makeText(getApplicationContext(), "Share Location is selected", Toast.LENGTH_LONG).show();
                        break;
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    @Override

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);


        if(friendLatLng!=null){
            friendLatLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
            MarkerOptions optionsnew = new MarkerOptions();

            optionsnew.position(friendLatLng);
            optionsnew.title(name);
            optionsnew.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));




        if(marker == null)
        {
            marker = mMap.addMarker(optionsnew);
        }
        else
        {
            marker.setPosition(friendLatLng);
        }


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(friendLatLng,15));
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest().create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(7000);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );
        //result.setResultCallback((ResultCallback)this);  // dialog for location
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            Toast.makeText(getApplicationContext(), "Could not get Location", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mLastLocation = new LatLng(location.getLatitude(),location.getLongitude());
            MarkerOptions options = new MarkerOptions();
            options.position(mLastLocation);
            options.title("Current Location");
            mMap.addMarker(options);
        }
    }


}