package com.example.unishop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.unishop.data.adapters.ConsultantAdapter;
import com.example.unishop.data.models.ModelConsultant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            // get all consultants
            getAllConsultants();
        }else {
            Toast.makeText(this, getText(R.string.network_text), Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllConsultants() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, getString(R.string.CONSULTANT_URL),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("RESPONSE", response.toString());
                        int count = 0;
                        while (count<response.length()){
                            try {
                                JSONObject jsonObject = response.getJSONObject(count);
                                ModelConsultant modelConsultant = new ModelConsultant(
                                        jsonObject.getString("user_id"),
                                        jsonObject.getString("firstname"),
                                        jsonObject.getString("lastname"),
                                        jsonObject.getString("id_number"),
                                        jsonObject.getString("phone"),
                                        jsonObject.getString("email"),
                                        jsonObject.getString("reg_date"),
                                        jsonObject.getString("refferred_by")
                                );
                                consultantArrayList.add(modelConsultant);
                                count++;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //initialise adapter
                        consultantAdapter = new ConsultantAdapter(ConsultantsActivity.this, consultantArrayList);
                        //set adapter to recyclerView
                        consultants_recyclerview.setAdapter(consultantAdapter);
                        consultant_dialog.setVisibility(View.GONE);
                        consultants_recyclerview.setVisibility(View.VISIBLE);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
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
