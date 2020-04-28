package com.example.unishop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.unishop.data.adapters.ConsultantAdapter;
import com.example.unishop.data.models.ModelConsultant;

import java.util.ArrayList;

public class ConsultantsActivity extends AppCompatActivity {
    private ArrayList<ModelConsultant> consultantArrayList;
    private RecyclerView consultants_recyclerview;
    private ProgressBar consultant_dialog;
    private ConsultantAdapter consultantAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultants);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        consultantArrayList = new ArrayList<>();
        consultant_dialog = findViewById(R.id.consultant_dialog);
        //init recyclerview
        consultants_recyclerview = findViewById(R.id.consultants_recyclerview);
        //setting its properties
        consultants_recyclerview.setHasFixedSize(true);
        consultants_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        NetworkConnection connection = new NetworkConnection(this);

        if (connection.isConnected()){
            //
        }else {
            Toast.makeText(this, getText(R.string.network_text), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ConsultantsActivity.this, AdminHomeActivity.class);
        startActivity(intent);
        finish();
    }
}
