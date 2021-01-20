package com.wheretobuy.adopteuncaddie;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.wheretobuy.adopteuncaddie.ui.basket.BasketFragment;
import com.wheretobuy.adopteuncaddie.ui.basket.BasketFragmentDirections;
import com.wheretobuy.adopteuncaddie.ui.settings.SettingsFragment;
import com.wheretobuy.adopteuncaddie.utils.CustomDebugTree;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Logger
        Timber.plant(new CustomDebugTree());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator();

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_barcodeScanner,
                R.id.nav_gallery,
                R.id.nav_basket)
                .setOpenableLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public void onBackPressed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavDirections action = BasketFragmentDirections.actionNavBasketToNavPayment();
//        navController.navigate(action);
        navController.navigate(R.id.nav_barcodeScanner);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavDirections action = BasketFragmentDirections.actionNavBasketToNavPayment();
//        navController.navigate(action);
        navController.navigate(R.id.nav_barcodeScanner);

        return true;
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration) ||
//            super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        switch (item.getItemId())
        {
            case(R.id.action_settings):
                navController.navigate(R.id.nav_settings);
                break;
            case(R.id.action_sources):
                navController.navigate(R.id.nav_sources);
                break;
            case(android.R.id.home): // android.R.id.home != R.id.home !!!
                onSupportNavigateUp();
                break;
            default:
                break;
        }
        return true;
    }
}