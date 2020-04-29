package com.example.unishop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ConsultantDetailsActivity extends AppCompatActivity {
    private TextView nameTv, phoneTv, referred_byTv, national_idTv, emailTv, created_dateTv;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //init views
        nameTv = findViewById(R.id.nameTv);
        phoneTv = findViewById(R.id.phoneTv);
        referred_byTv = findViewById(R.id.referred_byTv);
        national_idTv = findViewById(R.id.national_idTv);
        emailTv = findViewById(R.id.emailTv);
        created_dateTv = findViewById(R.id.created_dateTv);

        intent = getIntent();

        //set data to views
        nameTv.setText(intent.getStringExtra("firstname")+" "+intent.getStringExtra("lastname"));
        phoneTv.setText(intent.getStringExtra("phone"));
        referred_byTv.setText(intent.getStringExtra("created_by"));
        national_idTv.setText(intent.getStringExtra("id_number"));
        emailTv.setText(intent.getStringExtra("email"));
        created_dateTv.setText(intent.getStringExtra("date_created"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ConsultantDetailsActivity.this, ConsultantsActivity.class);
        startActivity(intent);
        finish();
    }
}
