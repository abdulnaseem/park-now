package com.example.parknow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);//replaces the actionbar
        setSupportActionBar(toolbar);//sets the toolbar

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//adds a home button to actionbar
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);//changes the home icon

        final Button booking_d_t = findViewById(R.id.searchHere);
        final TextView footer_sign_in = findViewById(R.id.nav_footer_sign_in);

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

        booking_d_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookingArrivLeave();
            }
        });
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
}
