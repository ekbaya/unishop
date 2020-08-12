package com.example.unishop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unishop.R;
import com.example.unishop.api.AccountAPI;
import com.example.unishop.models.User;
import com.example.unishop.services.AccountListener;
import com.google.firebase.database.DatabaseError;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements AccountListener.AccountInstantListener {
    @BindView(R.id.nameTv) TextView nameTv;
    @BindView(R.id.phoneTv) TextView phoneTv;
    @BindView(R.id.referred_byTv) TextView referred_byTv;
    @BindView(R.id.national_idTv) TextView national_idTv;
    @BindView(R.id.emailTv) TextView emailTv;
    @BindView(R.id.created_dateTv) TextView created_dateTv;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private Intent intent;
    private AccountAPI accountAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);

        intent = getIntent();
        accountAPI = new AccountAPI(this);
        accountAPI.setAccountInstantListener(this);
        //set data to views
        setDataToViews();
    }

    private void setDataToViews() {
        nameTv.setText(intent.getStringExtra("firstname")+" "+intent.getStringExtra("lastname"));
        phoneTv.setText(intent.getStringExtra("phone"));
        referred_byTv.setText(intent.getStringExtra("created_by"));
        national_idTv.setText(intent.getStringExtra("id_number"));
        emailTv.setText(intent.getStringExtra("email"));
        created_dateTv.setText(intent.getStringExtra("date_created"));

        String user_id = intent.getStringExtra("created_by");
        //accountAPI.getUserAccount(user_id);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, UsersActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAccountReceived(User user) {
        referred_byTv.setText(user.getFirstname()+" "+user.getLastname());
        progressBar.setVisibility(View.GONE);
        referred_byTv.setVisibility(View.VISIBLE);

    }

    @Override
    public void onRequestCancelled(DatabaseError error) {

    }
}
