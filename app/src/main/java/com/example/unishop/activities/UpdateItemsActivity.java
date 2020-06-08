package com.example.unishop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.unishop.R;

public class UpdateItemsActivity extends AppCompatActivity {
    private EditText pp_editText, priceEt, qp_editText, quantityEt;
    private Button pupdateBtn, qupdateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_items);
        initViews();
    }

    private void initViews() {
        pp_editText = findViewById(R.id.pp_editText);
        priceEt = findViewById(R.id.priceEt);
        qp_editText = findViewById(R.id.qp_editText);
        quantityEt = findViewById(R.id.quantityEt);
        pupdateBtn = findViewById(R.id.pupdateBtn);
        qupdateBtn = findViewById(R.id.qupdateBtn);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UpdateItemsActivity.this, AdminHomeActivity.class);
        startActivity(intent);
        finish();
    }
}
