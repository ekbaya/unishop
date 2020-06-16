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
import com.example.unishop.api.ConsultantsAPI;
import com.example.unishop.adapters.ConsultantAdapter;
import com.example.unishop.models.ModelConsultant;
import com.example.unishop.services.ConsultantsListener;
import com.example.unishop.utilities.Loader;
import com.example.unishop.utilities.NetworkConnection;

import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class ConsultantsActivity extends AppCompatActivity implements ConsultantsListener {
    @BindView(R.id.consultants_recyclerview) RecyclerView consultants_recyclerview;
    @BindView(R.id.consultant_dialog) ProgressBar consultant_dialog;
    private ConsultantAdapter consultantAdapter;

    private ConsultantsAPI consultantsAPI;
    private Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultants);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);

        //setting its properties
        consultants_recyclerview.setHasFixedSize(true);
        consultants_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        consultantsAPI = new ConsultantsAPI(this);
        consultantsAPI.setConsultantsListener(this);
        loader = new Loader(this);
        loadConsultants();
    }

    private void loadConsultants() {
        if (new NetworkConnection().get().isConnected(this)){
            // get all consultants
            consultantsAPI.getAllConsultants();
            loader.showDialogue();

        }else {
            Toasty.warning(this, getText(R.string.network_text), Toasty.LENGTH_LONG).show();
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

    @Override
    public void onConsultantsReceived(List<ModelConsultant> consultantArrayList) {
        loader.hideDialogue();
        //initialise adapter
        consultantAdapter = new ConsultantAdapter(ConsultantsActivity.this, consultantArrayList);
        //set adapter to recyclerView
        consultants_recyclerview.setAdapter(consultantAdapter);
        consultant_dialog.setVisibility(View.GONE);
        consultants_recyclerview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onVolleyErrorResponse(VolleyError error) {
        loader.hideDialogue();
        if (error instanceof NetworkError){
            Toasty.error(this, "Check your connection and try again", Toasty.LENGTH_LONG).show();
        }
        else Toasty.error(this, error.toString(), Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onJSONObjectException(JSONException e) {
        loader.hideDialogue();
        Toasty.error(this, e.toString(), Toasty.LENGTH_LONG).show();
    }
}
