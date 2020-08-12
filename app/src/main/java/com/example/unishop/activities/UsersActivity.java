package com.example.unishop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkError;
import com.android.volley.VolleyError;
import com.example.unishop.R;
import com.example.unishop.adapters.UsersAdapter;
import com.example.unishop.api.AccountAPI;
import com.example.unishop.models.Consultant;
import com.example.unishop.models.User;
import com.example.unishop.services.AccountListener;
import com.google.firebase.database.DatabaseError;

import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class UsersActivity extends AppCompatActivity implements AccountListener.LoadAccountsListener {
    @BindView(R.id.users_recyclerview) RecyclerView users_recyclerview;
    @BindView(R.id.users_dialog) ProgressBar users_dialog;
    private UsersAdapter usersAdapter;
    private AccountAPI accountAPI;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);

        //setting its properties
        users_recyclerview.setHasFixedSize(true);
        users_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        accountAPI = new AccountAPI(this);
        accountAPI.setLoadAccountsListener(this);
        loadUsers();

    }

    public void loadUsers() {
        // get all consultants
        accountAPI.getAllAccounts();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UsersActivity.this, AdminHomeActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onAccountsReceived(List<User> userList) {
        users_dialog.setVisibility(View.GONE);
        //initialise adapter
        usersAdapter = new UsersAdapter(UsersActivity.this, userList);
        //set adapter to recyclerView
        users_recyclerview.setAdapter(usersAdapter);
        users_recyclerview.setVisibility(View.GONE);
        users_recyclerview.setVisibility(View.VISIBLE);

    }

    @Override
    public void onDatabaseCancelled(DatabaseError error) {
        users_dialog.setVisibility(View.GONE);
    }
}
