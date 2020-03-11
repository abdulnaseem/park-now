package com.example.parknow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.navigation.NavigationView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    //private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);//this is the maps

        toolbar = findViewById(R.id.toolbar);//replaces the actionbar
        setSupportActionBar(toolbar);//sets the toolbar

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//adds a home button to actionbar
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);//changes the home icon

        //final Button booking_d_t = findViewById(R.id.searchHere);
        //final TextView footer_sign_in = findViewById(R.id.nav_footer_sign_in);

        drawerLayout = findViewById(R.id.drawer_layout);//opens drawer
        navigationView = findViewById(R.id.navigationView);//navigations within drawer

        navigationView.setNavigationItemSelectedListener
                (new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        switch (menuItem.getItemId()){
                            /*
                             * Add all navigation menus here and use "drawer_menu"
                             * to add your menu
                             */
                            case R.id.nav_rentYourSpace: {
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawers();
                                return true;
                            }
                            case R.id.nav_reserveSpace: {
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawers();
                                return true;
                            }
                            case R.id.nav_help: {
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawers();
                                return true;
                            }

                        }
                        return false;
                    }
                });

       /* booking_d_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookingArrivLeave();
            }
        });*/
    }
    //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    //getSupportActionBar().setCustomView(R.layout.txt_layout);


    public void bookingArrivLeave(){
        Intent searchHere = new Intent(this, SearchBooking.class);
        startActivity(searchHere);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /*
         * The home button that opens the drawer
         */
        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void sign_in(View view){
        Toast.makeText(getApplicationContext(), "Sign in", Toast.LENGTH_SHORT).show();

    }
    public void sign_up(View view){
        Toast.makeText(getApplicationContext(), "Sign up", Toast.LENGTH_SHORT).show();
    }

    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng london = new LatLng(51.5, -0.12);
        mMap.setMinZoomPreference(10.0f);
        mMap.setMaxZoomPreference(14.0f);
        mMap.addMarker(new MarkerOptions().position(london).title("Marker in London"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(london));
    }*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        /*MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Search Here");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);*/
        mMap.setOnMarkerClickListener(this);
        mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Search Here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        //move map camera
        mMap.setMinZoomPreference(8.0f);
        mMap.setMaxZoomPreference(18.0f);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(0));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(mCurrLocationMarker)) {
            bookingArrivLeave();
        }
        return false;
    }
}

