package com.example.ruber;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CustomerPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,com.google.android.gms.location.LocationListener {
    FirebaseAuth firebaseAuth;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest request;
    Location mLasLocation;
    LatLng startL;
    Marker currentMarker;
    Button btnn_source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_page);

        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnn_source = (Button)findViewById(R.id.bntS);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        request = new LocationRequest();
        request.setInterval(500);
        request.setFastestInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,request,this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mGoogleApiClient = new  GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.help) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.singout)
        {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user!=null){
                firebaseAuth.signOut();
                Intent myintent = new Intent(CustomerPage.this,MainActivity.class);
                startActivity(myintent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"User is already signed out.",Toast.LENGTH_LONG).show();
            }


        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {

        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);

        if (location == null)
        {
            Toast.makeText(getApplicationContext(), "Location could not be found", Toast.LENGTH_SHORT).show();
        }else{
            startL =new LatLng(location.getLatitude(),location.getLongitude());


            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try
            {
                List<Address> myadresses = geocoder.getFromLocation(startL.latitude,startL.longitude,1);
                String address = myadresses.get(0).getAddressLine(0);
                String city = myadresses.get(0).getLocality();
                btnn_source.setText(address+""+city);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (currentMarker == null) {
                MarkerOptions options = new MarkerOptions();
                options.position(startL);
                options.title("Current Position");
                currentMarker = mMap.addMarker(options);

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startL, 15));
            }else {
                currentMarker.setPosition(startL);
            }
        }
    }

}
