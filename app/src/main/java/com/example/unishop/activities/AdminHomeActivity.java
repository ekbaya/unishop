package com.example.unishop.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.unishop.R;
import com.example.unishop.data.SharedHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AdminHomeActivity extends AppCompatActivity {
    //views
    private  TextView headerTv;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_items_manage, R.id.nav_consultants_manage)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_admin_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        headerTv = (TextView) headerView.findViewById(R.id.headerTv);


        String firstname = SharedHelper.getKey(AdminHomeActivity.this, "firstname");
        String lastname = SharedHelper.getKey(AdminHomeActivity.this, "lastname");
        headerTv.setText(firstname+" "+lastname);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // get item id
        int id = item.getItemId();
        if (id == R.id.action_logout){
            showLogoutDialogue();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialogue() {
        //AlertDialog
        AlertDialog.Builder builder= new AlertDialog.Builder(AdminHomeActivity.this);
        //set Layout Linear Layout
        LinearLayout linearLayout = new LinearLayout(AdminHomeActivity.this);
        // Views to set in dialog
        final TextView textView = new TextView(AdminHomeActivity.this);
        textView.setText("Are you sure you want to exit?");
        textView.setTextSize(20);
        linearLayout.addView(textView);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);


        //cancel button
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss dialog
                dialog.dismiss();
            }
        });

        //Reset pin button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedHelper.clearSharedPreferences(AdminHomeActivity.this);
                startActivity(new Intent(new Intent(AdminHomeActivity.this, LoginActivity.class)));
                finish();
            }
        });

        //create and show dialog
        builder.create().show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_admin_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
