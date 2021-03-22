package org.cooltutors.student;

// Darren Stults

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.cooltutors.student.ui.CustomTheme;

public class MainActivity extends AppCompatActivity {

    // Declarations
    public static MainActivity me;
    private ProgressBar loadingSpinner;

    // Creation
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assignments
        me = this;
        BottomNavigationView navView = findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        CustomTheme customTheme = new CustomTheme(this.getColor(R.color.background_dark), this.getColor(R.color.background_light), actionBar);
        loadingSpinner = findViewById(R.id.main_loading_spinner);

        // Set nav bar background color
        navView.setBackgroundColor(customTheme.backgroundDark);
        // Set action bar background color
        customTheme.setActionBarBackgroundColor();

        // Include here to designate "top level destination" - prevents back button from appearing.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_services, R.id.navigation_openings, R.id.navigation_appointments, R.id.navigation_profile).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void showSpinner() {
        loadingSpinner.setVisibility(View.VISIBLE);
    }

    public void hideSpinner() {
        loadingSpinner.setVisibility(View.INVISIBLE);
    }

}